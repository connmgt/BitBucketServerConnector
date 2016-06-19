package nl.topicus.bitbucket.persistence;

import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import net.java.ao.EntityManager;
import net.java.ao.RawEntity;

import java.beans.PropertyChangeListener;

public class NonpersistantWebHookConfiguration implements WebHookConfiguration
{
	private String title;
	private String URL;
	private Integer repoId;
	private boolean enabled;

	public NonpersistantWebHookConfiguration(String title, String url, boolean enabled)
	{
		this.title = title;
		this.URL = url;
		this.enabled = enabled;
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	@Override
	public void setTitle(String title)
	{
		this.title = title;
	}

	@Override
	public String getURL()
	{
		return URL;
	}

	@Override
	public void setURL(String URL)
	{
		this.URL = URL;
	}

	@Override
	public Integer getRepositoryId()
	{
		return repoId;
	}

	@Override
	public void setRepositoryId(Integer repoId)
	{
		this.repoId = repoId;
	}

	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	@Override
	public void setEnabled(boolean isEnabled)
	{
		enabled = isEnabled;
	}

	@Override
	public Repository getRepository(RepositoryService rs)
	{
		return rs.getById(repoId);
	}

	@Override
	public void setRepository(Repository repo)
	{

	}

	@Override
	public int getID()
	{
		return 0;
	}

	@Override
	public void init()
	{
		throw new IllegalArgumentException("Method not implemented");
	}

	@Override
	public void save()
	{
		throw new IllegalArgumentException("Method not implemented");
	}

	@Override
	public EntityManager getEntityManager()
	{
		throw new IllegalArgumentException("Method not implemented");
	}

	@Override
	public <X extends RawEntity<Integer>> Class<X> getEntityType()
	{
		return (Class<X>) NonpersistantWebHookConfiguration.class;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
	{
		throw new IllegalArgumentException("Method not implemented");
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
	{
		throw new IllegalArgumentException("Method not implemented");
	}
}
