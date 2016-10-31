package nl.topicus.bitbucket.api;

import com.atlassian.bitbucket.event.pull.PullRequestEvent;
import com.atlassian.bitbucket.event.pull.PullRequestMergedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestOpenedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestReopenedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestRescopedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestUpdatedEvent;
import com.atlassian.bitbucket.event.repository.AbstractRepositoryRefsChangedEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.httpclient.api.Request;
import com.atlassian.httpclient.api.Response;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import nl.topicus.bitbucket.events.BitbucketPushEvent;
import nl.topicus.bitbucket.events.BitbucketServerPullRequestEvent;
import nl.topicus.bitbucket.events.EventType;
import nl.topicus.bitbucket.events.Events;
import nl.topicus.bitbucket.persistence.WebHookConfiguration;
import nl.topicus.bitbucket.persistence.WebHookConfigurationDao;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class PullRequestListener implements DisposableBean
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PullRequestListener.class);

	private EventPublisher eventPublisher;
	private HttpClient httpClient;
	private NavBuilder navBuilder;
	private WebHookConfigurationDao webHookConfigurationDao;

	@Autowired
	public PullRequestListener(@ComponentImport EventPublisher eventPublisher, @ComponentImport HttpClient httpClient, @ComponentImport NavBuilder navBuilder, WebHookConfigurationDao webHookConfigurationDao)
	{
		this.eventPublisher = eventPublisher;
		this.httpClient = httpClient;
		this.navBuilder = navBuilder;
		this.webHookConfigurationDao = webHookConfigurationDao;
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
		sendPullRequestEvent(event, EventType.PULL_REQUEST_UPDATED);
	}

	@EventListener
	public void rescopedEvent(PullRequestRescopedEvent event) throws IOException
	{
		sendPullRequestEvent(event, EventType.PULL_REQUEST_UPDATED);
	}

	@EventListener
	public void mergedEvent(PullRequestMergedEvent event) throws IOException
	{
		sendPullRequestEvent(event, EventType.PULL_REQUEST_UPDATED);
	}

	@EventListener
	public void repoChangedEvent(AbstractRepositoryRefsChangedEvent event) throws IOException
	{
		BitbucketPushEvent pushEvent = Events.createPushEvent(event);
		sendEvents(pushEvent, event.getRepository(), EventType.REPO_PUSH);
	}

	private void sendPullRequestEvent(PullRequestEvent event, EventType eventType) throws IOException
	{
		BitbucketServerPullRequestEvent pullRequestEvent = Events.createPullrequestEvent(event);
		Repository repository = event.getPullRequest().getToRef().getRepository();
		String prUrl = navBuilder.repo(repository).pullRequest(event.getPullRequest().getId()).buildAbsolute();
		pullRequestEvent.getPullrequest().setLink(prUrl);
		sendEvents(pullRequestEvent, repository, eventType);
	}

	private void sendEvents(Object event, Repository repo, EventType eventType) throws IOException
	{
		Map<String, String> header = new HashMap<>();
		header.put("X-Event-Key", eventType.getHeaderValue());
		header.put("X-Bitbucket-Type", "server");

		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(event);
		for (WebHookConfiguration webHookConfiguration : webHookConfigurationDao.getEnabledWebHookConfigurations(repo))
		{
			Request.Builder builder = httpClient.newRequest(webHookConfiguration.getURL());
			builder.setHeaders(header);
			builder.setContentType(MediaType.APPLICATION_JSON_VALUE);
			try
			{
				Response response = builder.setEntity(jsonBody).post().get();
				if (response.isError())
				{
					LOGGER.error("[repo: {}]| Something when wrong while posting (response code:{}) the following body to webhook: [{}({})] \n{}", repo, response.getStatusCode(), webHookConfiguration.getTitle(), webHookConfiguration.getURL(), jsonBody);
				}
			}
			catch (InterruptedException | ExecutionException e)
			{
				LOGGER.error("[repo: {}]| Something when wrong while posting the following body to webhook: [{}({})] \n{}", repo, webHookConfiguration.getTitle(), webHookConfiguration.getURL(), jsonBody, e);
			}

		}
	}

	@Override
	public void destroy() throws Exception
	{
		eventPublisher.unregister(this);
	}
}
