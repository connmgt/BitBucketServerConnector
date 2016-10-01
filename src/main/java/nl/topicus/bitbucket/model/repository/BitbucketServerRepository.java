package nl.topicus.bitbucket.model.repository;

public class BitbucketServerRepository
{
	private String scmId;
	private BitbucketServerProject project;
	private String slug;

	// JSON mapping added in setter because the field can not be called "public"
	private Boolean publc;

	public String getScmId()
	{
		return scmId;
	}

	public void setScmId(String scmId)
	{
		this.scmId = scmId;
	}

	public String getFullName()
	{
		return project.getKey() + "/" + slug;
	}

	public BitbucketServerRepositoryOwner getOwner()
	{
		return new BitbucketServerRepositoryOwner(project.getKey(), project.getKey());
	}

	public String getOwnerName()
	{
		return project.getKey();
	}

	public String getSlug()
	{
		return slug;
	}

	public void setSlug(String slug)
	{
		this.slug = slug;
	}

	public BitbucketServerProject getProject()
	{
		return project;
	}

	public void setProject(BitbucketServerProject p)
	{
		this.project = p;
	}

	public boolean isPublic()
	{
		return publc;
	}

	public void setPublic(Boolean publc)
	{
		this.publc = publc;
	}
}
