package nl.topicus.bitbucket.model;

import com.atlassian.bitbucket.project.Project;
import com.atlassian.bitbucket.pull.PullRequest;
import com.atlassian.bitbucket.pull.PullRequestRef;
import com.atlassian.bitbucket.repository.Repository;
import nl.topicus.bitbucket.model.pullrequest.BitbucketServerPullRequest;
import nl.topicus.bitbucket.model.pullrequest.BitbucketServerPullRequestSource;
import nl.topicus.bitbucket.model.repository.BitbucketServerProject;
import nl.topicus.bitbucket.model.repository.BitbucketServerRepository;

public final class Models
{
	private Models()
	{
	}

	public static BitbucketServerRepository createRepository(Repository repository)
	{
		BitbucketServerRepository repoType = new BitbucketServerRepository();
		repoType.setProject(createProject(repository.getProject()));
		repoType.setPublic(repository.isPublic());
		repoType.setScmId(repository.getScmId());
		repoType.setSlug(repository.getSlug());
		return repoType;
	}

	public static BitbucketServerProject createProject(Project project)
	{
		BitbucketServerProject serverProject = new BitbucketServerProject();
		serverProject.setName(project.getName());
		serverProject.setKey(project.getKey());
		return serverProject;
	}

	public static BitbucketServerPullRequest createPullrequest(PullRequest pullRequest)
	{
		BitbucketServerPullRequest pullRequestType = new BitbucketServerPullRequest();
		pullRequestType.setId(String.valueOf(pullRequest.getId()));
		pullRequestType.setFromRef(createSource(pullRequest.getFromRef()));
		pullRequestType.setToRef(createSource(pullRequest.getToRef()));
		return pullRequestType;
	}

	public static BitbucketServerPullRequestSource createSource(PullRequestRef pullRequestRef)
	{
		BitbucketServerPullRequestSource source = new BitbucketServerPullRequestSource();
		source.setDisplayId(pullRequestRef.getDisplayId());
		source.setLatestCommit(pullRequestRef.getLatestCommit());
		source.setRepository(createRepository(pullRequestRef.getRepository()));
		return source;
	}
}
