package nl.topicus.bitbucket.api;

import com.atlassian.bitbucket.event.branch.BranchCreatedEvent;
import com.atlassian.bitbucket.event.branch.BranchDeletedEvent;
import com.atlassian.bitbucket.event.pull.*;
import com.atlassian.bitbucket.event.repository.RepositoryPushEvent;
import com.atlassian.bitbucket.event.repository.RepositoryRefsChangedEvent;
import com.atlassian.bitbucket.event.tag.TagCreatedEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.pull.PullRequest;
import com.atlassian.bitbucket.pull.PullRequestService;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;
import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import nl.topicus.bitbucket.events.BitbucketPushEvent;
import nl.topicus.bitbucket.events.BitbucketServerPullRequestEvent;
import nl.topicus.bitbucket.events.EventType;
import nl.topicus.bitbucket.events.Events;
import nl.topicus.bitbucket.persistence.WebHookConfiguration;
import nl.topicus.bitbucket.persistence.WebHookConfigurationDao;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PullRequestListener implements DisposableBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PullRequestListener.class);

    private EventPublisher eventPublisher;
    private CloseableHttpClient httpClient;
    private NavBuilder navBuilder;
    private WebHookConfigurationDao webHookConfigurationDao;
    private PullRequestService pullRequestService;
    private ApplicationPropertiesService applicationPropertiesService;

    @Autowired
    public PullRequestListener(@ComponentImport EventPublisher eventPublisher,
                               @ComponentImport PullRequestService pullRequestService,
                               HttpClientFactory httpClientFactory,
                               @ComponentImport NavBuilder navBuilder,
                               @ComponentImport ApplicationPropertiesService applicationPropertiesService,
                               WebHookConfigurationDao webHookConfigurationDao)
    {
        this.eventPublisher = eventPublisher;
        this.httpClient = httpClientFactory.create();
        this.navBuilder = navBuilder;
        this.webHookConfigurationDao = webHookConfigurationDao;
        this.pullRequestService = pullRequestService;
        this.applicationPropertiesService = applicationPropertiesService;
        eventPublisher.register(this);
    }

    @EventListener
    public void createdEvent(PullRequestOpenedEvent event) throws IOException
    {
        sendPullRequestEvent(event, EventType.PULL_REQUEST_CREATED);
    }

    @EventListener
    public void updatedEvent(PullRequestUpdatedEvent event) throws IOException
    {
        sendPullRequestEvent(event, EventType.PULL_REQUEST_UPDATED);
    }

    @EventListener
    public void reopenedEvent(PullRequestReopenedEvent event) throws IOException
    {
        sendPullRequestEvent(event, EventType.PULL_REQUEST_REOPENED);
    }

    @EventListener
    public void rescopedEvent(PullRequestRescopedEvent event) throws IOException
    {
        final PullRequest pullRequest = event.getPullRequest();

        // see this atlassian page for explanation of the logic in this handler:
        // https://answers.atlassian.com/questions/239988

        // only trigger when changes were pushed to the "from" side of the PR
        if (!event.getPreviousFromHash().equals(pullRequest.getFromRef().getLatestCommit()))
        {
            // canMerge forces the update of refs in the destination repository
            pullRequestService.canMerge(pullRequest.getToRef().getRepository().getId(), pullRequest.getId());
            sendPullRequestEvent(event, EventType.PULL_REQUEST_RESCOPED);
        }
    }

    @EventListener
    public void mergedEvent(PullRequestMergedEvent event) throws IOException
    {
        sendPullRequestEvent(event, EventType.PULL_REQUEST_MERGED);
    }

    @EventListener
    public void declinedEvent(PullRequestDeclinedEvent event) throws IOException
    {
        sendPullRequestEvent(event, EventType.PULL_REQUEST_DECLINED);
    }

    @EventListener
    public void branchDeletedEvent(BranchDeletedEvent event) throws IOException {
        repoChangedEvent(event, EventType.BRANCH_DELETED);
    }

    @EventListener
    public void branchCreatedEvent(BranchCreatedEvent event) throws IOException {
        repoChangedEvent(event, EventType.BRANCH_CREATED);
    }

    @EventListener
    public void tagCreatedEvent(TagCreatedEvent event) throws IOException {
        repoChangedEvent(event, EventType.TAG_CREATED);
    }

    @EventListener
    public void repositoryPushEvent(RepositoryPushEvent event) throws IOException {
        repoChangedEvent(event, EventType.REPO_PUSH);
    }


    public void repoChangedEvent(RepositoryRefsChangedEvent event, EventType eventType) throws IOException
    {
        BitbucketPushEvent pushEvent = Events.createPushEvent(event, applicationPropertiesService);
        sendEvents(pushEvent, event.getRepository(), eventType);
    }

    private void sendPullRequestEvent(PullRequestEvent event, EventType eventType) throws IOException
    {
        BitbucketServerPullRequestEvent pullRequestEvent = Events.createPullrequestEvent(event,
                                                                                         applicationPropertiesService);
        Repository repository = event.getPullRequest().getToRef().getRepository();
        String prUrl = navBuilder.repo(repository).pullRequest(event.getPullRequest().getId()).buildAbsolute();
        pullRequestEvent.getPullrequest().setLink(prUrl);
        sendEvents(pullRequestEvent, repository, eventType);
    }

    private void sendEvents(Object event, Repository repo, EventType eventType) throws IOException
    {
        Header[] headers = {
                new BasicHeader("X-Event-Key", eventType.getHeaderValue()),
                new BasicHeader("X-Bitbucket-Type", "server")
        };

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(event);
        StringEntity bodyEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
        for (WebHookConfiguration webHookConfiguration : webHookConfigurationDao.getEnabledWebHookConfigurations(repo, eventType))
        {
            HttpPost post = new HttpPost(webHookConfiguration.getURL());
            post.setHeaders(headers);
            post.setEntity(bodyEntity);
            try (CloseableHttpResponse response = httpClient.execute(post))
            {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode >= 400)
                {
                    LOGGER.error(
                            "[repo: {}]| Something when wrong while posting (response code:{}) the following body to webhook: [{}({})] \n{}",
                            repo,
                            statusCode,
                            webHookConfiguration.getTitle(),
                            webHookConfiguration.getURL(),
                            jsonBody);
                }
            } catch (IOException e)
            {
                LOGGER.error(
                        "[repo: {}]| Something when wrong while posting the following body to webhook: [{}({})] \n{}",
                        repo,
                        webHookConfiguration.getTitle(),
                        webHookConfiguration.getURL(),
                        jsonBody,
                        e);
            }

        }
    }

    @Override
    public void destroy() throws Exception
    {
        eventPublisher.unregister(this);
        httpClient.close();
    }
}
