package nl.topicus.bitbucket.persistence;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebHookConfigurationDao
{
	private final ActiveObjects activeObjects;

	@Autowired
	public WebHookConfigurationDao(@ComponentImport ActiveObjects activeObjects)
	{
		this.activeObjects = activeObjects;
	}

	public WebHookConfiguration[] getWebHookConfigurations(Repository repo)
	{
		return activeObjects.find(WebHookConfiguration.class, Query.select().where("REPO_ID = ?", repo.getId()));
	}

	public WebHookConfiguration[] getEnabledWebHookConfigurations(Repository repo)
	{
		return activeObjects.find(WebHookConfiguration.class, Query.select().where("REPO_ID = ? AND IS_ENABLED = ?", repo.getId(), true));
	}

	public WebHookConfiguration getWebHookConfigurations(String id)
	{
		return activeObjects.get(WebHookConfiguration.class, Integer.valueOf(id));
	}

	public WebHookConfiguration createOrUpdateWebHookConfiguration(Repository rep, String id, String title, String url, boolean enabled)
	{
		if (id != null)
		{
			WebHookConfiguration webHookConfiguration = getWebHookConfigurations(id);
			if (webHookConfiguration != null && webHookConfiguration.getRepositoryId().equals(rep.getId()))
			{
				webHookConfiguration.setTitle(title);
				webHookConfiguration.setEnabled(enabled);
				webHookConfiguration.setURL(url);
				webHookConfiguration.save();
				return webHookConfiguration;
			}
		}
		return activeObjects.create(WebHookConfiguration.class, new DBParam("REPO_ID", rep.getId()), new DBParam("TITLE", title), new DBParam("URL", url), new DBParam("IS_ENABLED", enabled));
	}
}
