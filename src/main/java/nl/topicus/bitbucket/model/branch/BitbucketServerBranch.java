package nl.topicus.bitbucket.model.branch;

public class BitbucketServerBranch
{
	private String displayId;
	private String latestCommit;

	public BitbucketServerBranch()
	{
	}

	public BitbucketServerBranch(String name, String headHash)
	{
		this.displayId = name;
		this.latestCommit = headHash;
	}

	public String getRawNode()
	{
		return latestCommit;
	}

	public String getName()
	{
		return displayId;
	}

	public void setDisplayId(String displayId)
	{
		this.displayId = displayId;
	}

	public void setLatestCommit(String latestCommit)
	{
		this.latestCommit = latestCommit;
	}

}
