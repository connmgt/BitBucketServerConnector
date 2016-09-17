package nl.topicus.bitbucket.model.pullrequest;

public class BitbucketServerPullRequest
{
	private String id;
	private String title;
	private String link;
	private String authorLogin;
	private BitbucketServerPullRequestSource fromRef;
	private BitbucketServerPullRequestSource toRef;

	public BitbucketServerPullRequestSource getFromRef()
	{
		return fromRef;
	}

	public void setFromRef(BitbucketServerPullRequestSource fromRef)
	{
		this.fromRef = fromRef;
	}

	public BitbucketServerPullRequestSource getToRef()
	{
		return toRef;
	}

	public void setToRef(BitbucketServerPullRequestSource toRef)
	{
		this.toRef = toRef;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getAuthorLogin()
	{
		return authorLogin;
	}

	public void setAuthorLogin(String authorLogin)
	{
		this.authorLogin = authorLogin;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}
}
