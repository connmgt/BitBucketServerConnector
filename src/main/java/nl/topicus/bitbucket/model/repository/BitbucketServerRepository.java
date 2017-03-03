package nl.topicus.bitbucket.model.repository;

import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class BitbucketServerRepository
{
	private String scmId;
	private BitbucketServerProject project;
	private String slug;
	private Map<String,List<Link>> links;

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

	public Map<String, List<Link>> getLinks() {
		return links;
	}

	public void setLinks(Map<String, List<Link>> links) {
		this.links = links;
	}

	public static class Link {
		private String href;
		private String name;

		public Link() {
		}

		public Link(String href) {
			this.href = href;
		}

		public Link(String href, String name) {
			this.href = href;
			this.name = name;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}
		@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
