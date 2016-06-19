package nl.topicus.bitbucket.persistence;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.bitbucket.repository.Repository;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.sql.SQLException;

public class WebHookConfigurationImpl
{
	public static WebHookConfiguration[] getByRepository(final ActiveObjects ao, final Repository repo) throws SQLException
	{
		return ao.find(WebHookConfiguration.class, Query.select().where("REPO_ID = ?", repo.getId()));
	}

	public static WebHookConfiguration getByRepositoryAndId(ActiveObjects activeObjects, Repository repository, String id)
	{
		WebHookConfiguration[] configs = activeObjects.find(WebHookConfiguration.class, Query.select().where("REPO_ID = ? AND ID = ?", repository.getId(), Integer.parseInt(id)));
		if (configs.length > 0)
		{
			return configs[0];
		}
		return null;
	}

	public static WebHookConfiguration createOrUpdateWebHookConfiguration(ActiveObjects activeObjects, Repository rep, String id, String title, String url, boolean enabled)
	{
		if (id != null)
		{
			WebHookConfiguration webHookConfiguration = getByRepositoryAndId(activeObjects, rep, id);
			if (webHookConfiguration != null)
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
