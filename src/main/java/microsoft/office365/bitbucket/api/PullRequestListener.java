package microsoft.office365.bitbucket.api;

import com.atlassian.bitbucket.event.pull.PullRequestDeclinedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestEvent;
import com.atlassian.bitbucket.event.pull.PullRequestMergedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestOpenedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestReopenedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestRescopedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestUpdatedEvent;
import com.atlassian.bitbucket.event.repository.AbstractRepositoryRefsChangedEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.pull.PullRequest;
import com.atlassian.bitbucket.pull.PullRequestService;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;
import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.httpclient.api.Request;
import com.atlassian.httpclient.api.Response;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import microsoft.office365.bitbucket.persistence.WebHookConfiguration;
import microsoft.office365.bitbucket.persistence.WebHookConfigurationDao;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import microsoft.office365.bitbucket.modelbuilder.EventsToNotify;
import microsoft.office365.bitbucket.modelbuilder.ModelBuilder;
import microsoft.office365.bitbucket.modelbuilder.PullRequestCreatedModelBuilder;
import microsoft.office365.bitbucket.modelbuilder.PullRequestDeclinedModelBuilder;
import microsoft.office365.bitbucket.modelbuilder.PullRequestMergedModelBuilder;
import microsoft.office365.bitbucket.modelbuilder.PullRequestUpdatedModelBuilder;

@Component
public class PullRequestListener implements DisposableBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PullRequestListener.class);
    
    private static final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

    private EventPublisher eventPublisher;
    private HttpClient httpClient;
    private NavBuilder navBuilder;
    private WebHookConfigurationDao webHookConfigurationDao;
    private PullRequestService pullRequestService;
    private ApplicationPropertiesService applicationPropertiesService;

    @Autowired
    public PullRequestListener(@ComponentImport EventPublisher eventPublisher,
                               @ComponentImport PullRequestService pullRequestService,
                               AtlassianHttpClientFactory httpClientFactory,
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
        sendPullRequestEvent(event, EventsToNotify.PULL_REQUEST_CREATED);
    }

    @EventListener
    public void updatedEvent(PullRequestUpdatedEvent event) throws IOException
    {
        sendPullRequestEvent(event, EventsToNotify.PULL_REQUEST_UPDATED);
    }

    @EventListener
    public void reopenedEvent(PullRequestReopenedEvent event) throws IOException
    {
        sendPullRequestEvent(event, EventsToNotify.PULL_REQUEST_UPDATED);
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
            sendPullRequestEvent(event, EventsToNotify.PULL_REQUEST_UPDATED);
        }
    }

    @EventListener
    public void mergedEvent(PullRequestMergedEvent event) throws IOException
    {
        sendPullRequestEvent(event, EventsToNotify.PULL_REQUEST_MERGED);
    }

    @EventListener
    public void declinedEvent(PullRequestDeclinedEvent event) throws IOException
    {
        sendPullRequestEvent(event, EventsToNotify.PULL_REQUEST_DECLINED);
    }

    @EventListener
    public void repoChangedEvent(AbstractRepositoryRefsChangedEvent event) throws IOException
    {
 //       BitbucketPushEvent pushEvent = Events.createPushEvent(event, applicationPropertiesService);
    //    sendEvents(pushEvent, event.getRepository());
    }

    private void sendPullRequestEvent(PullRequestEvent event, EventsToNotify eventType) throws IOException
    {
        ModelBuilder modelBuilder = GetModelBuilder(eventType, event);
        Repository repository = event.getPullRequest().getToRef().getRepository();
        sendEvents(gson.toJson(modelBuilder.Build()), repository);
    }

    private void sendEvents(String event, Repository repo) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        //String jsonBody = mapper.writeValueAsString(event);
        for (WebHookConfiguration webHookConfiguration : webHookConfigurationDao.getEnabledWebHookConfigurations(repo))
        {
            Request.Builder builder = httpClient.newRequest(webHookConfiguration.getURL());
            builder.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try
            {
                Response response = builder.setEntity(event).post().get();
                if (response.isError())
                {
                    LOGGER.error(
                            "[repo: {}]| Something went wrong while posting (response code:{}) the following body to webhook: [{}({})] \n{}",
                            repo,
                            response.getStatusCode(),
                            webHookConfiguration.getTitle(),
                            webHookConfiguration.getURL(),
                            event);
                }
            } catch (InterruptedException | ExecutionException e)
            {
                LOGGER.error(
                        "[repo: {}]| Something when wrong while posting the following body to webhook: [{}({})] \n{}",
                        repo,
                        webHookConfiguration.getTitle(),
                        webHookConfiguration.getURL(),
                        event,
                        e);
            }

        }
    }

    @Override
    public void destroy() throws Exception
    {
        eventPublisher.unregister(this);
    }
    
    private ModelBuilder GetModelBuilder(EventsToNotify event, PullRequestEvent pullRequest)
    {
        switch (event)
        {
            case PULL_REQUEST_CREATED:
                return new PullRequestCreatedModelBuilder(pullRequest, navBuilder);
            case PULL_REQUEST_UPDATED:
                return new PullRequestUpdatedModelBuilder(pullRequest, navBuilder);
            case PULL_REQUEST_MERGED:
                return new PullRequestMergedModelBuilder(pullRequest, navBuilder);
            case PULL_REQUEST_DECLINED:
                return new PullRequestDeclinedModelBuilder(pullRequest, navBuilder);
 /*           case REPO_PUSH:
                return new RepoPushModelBuilder(pullRequest);*/
        }
        return null;
    }
}
