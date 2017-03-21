package nl.topicus.bitbucket.persistence;

import net.java.ao.EntityManager;
import net.java.ao.RawEntity;

import java.beans.PropertyChangeListener;

public class DummyWebHookConfiguration implements WebHookConfiguration
{
	private String title;
	private String URL;
	private Integer repoId;
	private boolean enabled;

	private boolean isTagCreated;
	private boolean isBranchDeleted;
	private boolean isBranchCreated;
	private boolean isRepoPush;
	private boolean isPrDeclined;
	private boolean isPrRescoped;
	private boolean isPrMerged;
	private boolean isPrReopened;
	private boolean isPrUpdated;
	private boolean isPrCreated;

	public DummyWebHookConfiguration(String title, String url, boolean enabled, boolean isTagCreated,
									 boolean isBranchDeleted, boolean isBranchCreated, boolean isRepoPush,
									 boolean isPrDeclined, boolean isPrRescoped, boolean isPrMerged,
									 boolean isPrReopened, boolean isPrUpdated, boolean isPrCreated)
	{
		this.title = title;
		this.URL = url;
		this.enabled = enabled;
		this.isTagCreated = isTagCreated;
		this.isBranchDeleted = isBranchDeleted;
		this.isBranchCreated = isBranchCreated;
		this.isRepoPush = isRepoPush;
		this.isPrDeclined = isPrDeclined;
		this.isPrRescoped = isPrRescoped;
		this.isPrMerged = isPrMerged;
		this.isPrReopened = isPrReopened;
		this.isPrUpdated = isPrUpdated;
		this.isPrCreated = isPrCreated;
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
	public boolean isTagCreated()
	{
		return isTagCreated;
	}

	@Override
	public void setTagCreated(boolean tagCreated)
	{
		isTagCreated = tagCreated;
	}

	@Override
	public boolean isBranchDeleted()
	{
		return isBranchDeleted;
	}

	@Override
	public void setBranchDeleted(boolean branchDeleted)
	{
		isBranchDeleted = branchDeleted;
	}

	@Override
	public boolean isBranchCreated()
	{
		return isBranchCreated;
	}

	@Override
	public void setBranchCreated(boolean branchCreated)
	{
		isBranchCreated = branchCreated;
	}

	@Override
	public boolean isRepoPush()
	{
		return isRepoPush;
	}

	@Override
	public void setRepoPush(boolean repoPush)
	{
		isRepoPush = repoPush;
	}

	@Override
	public boolean isPrDeclined()
	{
		return isPrDeclined;
	}

	@Override
	public void setPrDeclined(boolean prDeclined)
	{
		isPrDeclined = prDeclined;
	}

	@Override
	public boolean isPrRescoped()
	{
		return isPrRescoped;
	}

	@Override
	public void setPrRescoped(boolean prRescoped)
	{
		isPrRescoped = prRescoped;
	}

	@Override
	public boolean isPrMerged()
	{
		return isPrMerged;
	}

	@Override
	public void setPrMerged(boolean prMerged)
	{
		isPrMerged = prMerged;
	}

	@Override
	public boolean isPrReopened()
	{
		return isPrReopened;
	}

	@Override
	public void setPrReopened(boolean prReopened)
	{
		isPrReopened = prReopened;
	}

	@Override
	public boolean isPrUpdated()
	{
		return isPrUpdated;
	}

	@Override
	public void setPrUpdated(boolean prUpdated)
	{
		isPrUpdated = prUpdated;
	}

	@Override
	public boolean isPrCreated()
	{
		return isPrCreated;
	}

	@Override
	public void setPrCreated(boolean prCreated)
	{
		isPrCreated = prCreated;
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
		return (Class<X>) DummyWebHookConfiguration.class;
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
