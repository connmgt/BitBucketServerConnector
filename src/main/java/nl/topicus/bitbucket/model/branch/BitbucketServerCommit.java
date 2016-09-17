package nl.topicus.bitbucket.model.branch;

public class BitbucketServerCommit
{
	private String message;
	private String date;
	private String hash;
	private long authorTimestamp;

	public BitbucketServerCommit()
	{
	}

	public BitbucketServerCommit(String message, String date, String hash, long authorTimestamp)
	{
		this.message = message;
		this.date = date;
		this.hash = hash;
		this.authorTimestamp = authorTimestamp;
	}

	public BitbucketServerCommit(String hash)
	{
		this.hash = hash;
	}

	public String getMessage()
	{
		return message;
	}

	public String getDate()
	{
		return date;
	}

	public String getHash()
	{
		return hash;
	}

	public long getAuthorTimestamp()
	{
		return authorTimestamp;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public void setHash(String hash)
	{
		this.hash = hash;
	}

	public void setAuthorTimestamp(long authorTimestamp)
	{
		this.authorTimestamp = authorTimestamp;
	}

}
