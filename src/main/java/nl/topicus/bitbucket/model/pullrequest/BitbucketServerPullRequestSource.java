package nl.topicus.bitbucket.model.pullrequest;

import nl.topicus.bitbucket.model.branch.BitbucketServerBranch;
import nl.topicus.bitbucket.model.branch.BitbucketServerCommit;
import nl.topicus.bitbucket.model.repository.BitbucketServerRepository;

public class BitbucketServerPullRequestSource
{
	private String latestCommit;

	private String displayId;

	private BitbucketServerRepository repository;

	public BitbucketServerRepository getRepository()
	{
		return repository;
	}

	public BitbucketServerBranch getBranch()
	{
		return new BitbucketServerBranch(displayId, latestCommit);
	}

	public BitbucketServerCommit getCommit()
	{
		return new BitbucketServerCommit(latestCommit);
	}

	public void setLatestCommit(String latestCommit)
	{
		this.latestCommit = latestCommit;
	}

	public void setDisplayId(String displayId)
	{
		this.displayId = displayId;
	}

	public void setRepository(BitbucketServerRepository repository)
	{
		this.repository = repository;
	}

}
