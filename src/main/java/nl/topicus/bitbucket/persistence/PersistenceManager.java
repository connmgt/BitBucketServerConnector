package nl.topicus.bitbucket.persistence;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class PersistenceManager
{
	private final ActiveObjects activeObjects;

	@Autowired
	public PersistenceManager(@ComponentImport ActiveObjects activeObjects)
	{
		this.activeObjects = activeObjects;
	}

	public WebHookConfiguration[] getWebHookConfigurations(Repository repo)
	{
		try
		{
			return WebHookConfigurationImpl.getByRepository(activeObjects, repo);
		}
		catch (SQLException e)
		{
			throw new RuntimeException("Something went wrong while retrieving the webhook configuration", e);
		}
	}

	public WebHookConfiguration getWebHookConfigurations(Repository repository, String id)
	{
		return WebHookConfigurationImpl.getByRepositoryAndId(activeObjects, repository, id);
	}

	public WebHookConfiguration createOrUpdateWebHookConfiguration(Repository rep, String id, String title, String url, boolean enabled)
	{
		return WebHookConfigurationImpl.createOrUpdateWebHookConfiguration(activeObjects, rep, id, title, url, enabled);
	}
}
