package nl.topicus.bitbucket.events;

import com.atlassian.bitbucket.event.pull.PullRequestEvent;
import com.atlassian.bitbucket.event.repository.AbstractRepositoryRefsChangedEvent;
import nl.topicus.bitbucket.model.Models;

public final class Events
{
	private Events()
	{
	}

	public static BitbucketPushEvent createPushEvent(AbstractRepositoryRefsChangedEvent event)
	{
		BitbucketPushEvent pushEvent = new BitbucketPushEvent();
		pushEvent.setRepository(Models.createRepository(event.getRepository()));
		return pushEvent;
	}

	public static BitbucketServerPullRequestEvent createPullrequestEvent(PullRequestEvent event)
	{
		BitbucketServerPullRequestEvent pullRequestEvent = new BitbucketServerPullRequestEvent();
		pullRequestEvent.setPullrequest(Models.createPullrequest(event.getPullRequest()));
		pullRequestEvent.setRepository(Models.createRepository(event.getPullRequest().getToRef().getRepository()));
		return pullRequestEvent;
	}
}
