package nl.eernie.bitbucket.api;

import com.atlassian.bitbucket.hook.repository.AsyncPostReceiveRepositoryHook;
import com.atlassian.bitbucket.hook.repository.RepositoryHookContext;
import com.atlassian.bitbucket.repository.RefChange;

import javax.annotation.Nonnull;
import java.util.Collection;

public class PostRecieveHook implements AsyncPostReceiveRepositoryHook
{
	@Override
	public void postReceive(@Nonnull RepositoryHookContext repositoryHookContext, @Nonnull Collection<RefChange> collection)
	{

	}
}
