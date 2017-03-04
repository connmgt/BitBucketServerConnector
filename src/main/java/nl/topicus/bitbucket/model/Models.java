package nl.topicus.bitbucket.model;

import com.atlassian.bitbucket.project.PersonalProject;
import com.atlassian.bitbucket.project.Project;
import com.atlassian.bitbucket.pull.PullRequest;
import com.atlassian.bitbucket.pull.PullRequestRef;
import com.atlassian.bitbucket.repository.RefChange;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;
import com.atlassian.bitbucket.user.ApplicationUser;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.topicus.bitbucket.events.BitbucketPushChange;
import nl.topicus.bitbucket.model.pullrequest.BitbucketServerPullRequest;
import nl.topicus.bitbucket.model.pullrequest.BitbucketServerPullRequestSource;
import nl.topicus.bitbucket.model.repository.BitbucketServerProject;
import nl.topicus.bitbucket.model.repository.BitbucketServerRepository;
import nl.topicus.bitbucket.model.repository.BitbucketServerRepositoryOwner;

public final class Models
{
	private Models()
	{
	}

	public static BitbucketServerRepository createRepository(Repository repository,
															 ApplicationPropertiesService appPropSvc)
	{
		BitbucketServerRepository repoType = new BitbucketServerRepository();
		repoType.setProject(createProject(repository.getProject()));
		repoType.setPublic(repository.isPublic());
		repoType.setScmId(repository.getScmId());
		repoType.setSlug(repository.getSlug());
		URI baseUrl = appPropSvc.getBaseUrl();
		if (baseUrl != null) {
			Map<String, List<BitbucketServerRepository.Link>> links = new HashMap<>();
			if (repository.getProject() instanceof PersonalProject) {
				PersonalProject pp = (PersonalProject) repository.getProject();
				links.put("self", Collections.singletonList(new BitbucketServerRepository.Link(
						String.format("%s/users/%s/repos/%s/browse", baseUrl, pp.getOwner().getSlug(),
								repository.getSlug()))));
			} else {
				links.put("self", Collections.singletonList(new BitbucketServerRepository.Link(
						String.format("%s/projects/%s/repos/%s/browse", baseUrl, repository.getProject().getKey(),
								repository.getSlug()))));
			}
			repoType.setLinks(links);
		}
		return repoType;
	}

	public static BitbucketServerProject createProject(Project project)
	{
		BitbucketServerProject serverProject = new BitbucketServerProject();
		serverProject.setName(project.getName());
		serverProject.setKey(project.getKey());
		return serverProject;
	}

	public static BitbucketServerPullRequest createPullrequest(PullRequest pullRequest,
															   ApplicationPropertiesService appPropSvc)
	{
		BitbucketServerPullRequest pullRequestType = new BitbucketServerPullRequest();
		pullRequestType.setId(String.valueOf(pullRequest.getId()));
		pullRequestType.setFromRef(createSource(pullRequest.getFromRef(), appPropSvc));
		pullRequestType.setToRef(createSource(pullRequest.getToRef(), appPropSvc));
		pullRequestType.setTitle(pullRequest.getTitle());
		pullRequestType.setAuthorLogin(pullRequest.getAuthor().getUser().getDisplayName());
		return pullRequestType;
	}

	public static BitbucketServerPullRequestSource createSource(PullRequestRef pullRequestRef,
																ApplicationPropertiesService appPropSvc)
	{
		BitbucketServerPullRequestSource source = new BitbucketServerPullRequestSource();
		source.setDisplayId(pullRequestRef.getDisplayId());
		source.setLatestCommit(pullRequestRef.getLatestCommit());
		source.setRepository(createRepository(pullRequestRef.getRepository(), appPropSvc));
		return source;
	}

	public static BitbucketServerRepositoryOwner createActor(ApplicationUser user) {
		return new BitbucketServerRepositoryOwner(user.getName(), user.getDisplayName());
	}

	public static BitbucketPushChange createChange(RefChange change) {
		BitbucketPushChange result = new BitbucketPushChange();
		BitbucketPushChange.State _new = null;
		BitbucketPushChange.State _old = null;
		switch (change.getType()) {
			case ADD:
				_new = new BitbucketPushChange.State();
				_new.setType(change.getRef().getType());
				_new.setName(change.getRef().getDisplayId());
				_new.setTarget(new BitbucketPushChange.State.Target(change.getToHash()));
				result.setCreated(true);
				result.setClosed(false);
				break;
			case DELETE:
				_old = new BitbucketPushChange.State();
				_old.setType(change.getRef().getType());
				_old.setName(change.getRef().getDisplayId());
				_old.setTarget(new BitbucketPushChange.State.Target(change.getFromHash()));
				result.setCreated(false);
				result.setClosed(true);
				break;
			case UPDATE:
				_new = new BitbucketPushChange.State();
				_new.setType(change.getRef().getType());
				_new.setName(change.getRef().getDisplayId());
				_new.setTarget(new BitbucketPushChange.State.Target(change.getToHash()));
				_old = new BitbucketPushChange.State();
				_old.setType(change.getRef().getType());
				_old.setName(change.getRef().getDisplayId());
				_old.setTarget(new BitbucketPushChange.State.Target(change.getFromHash()));
				result.setCreated(false);
				result.setClosed(false);
				break;
		}
		result.setNew(_new);
		result.setOld(_old);
		return result;
	}
}
