package nl.topicus.bitbucket.persistence;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import net.java.ao.DBParam;
import net.java.ao.Query;
import nl.topicus.bitbucket.events.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebHookConfigurationDao {
    private final ActiveObjects activeObjects;

    @Autowired
    public WebHookConfigurationDao(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    public WebHookConfiguration[] getWebHookConfigurations(Repository repo) {
        return activeObjects.find(WebHookConfiguration.class, Query.select().where("REPO_ID = ?", repo.getId()));
    }

    public WebHookConfiguration[] getEnabledWebHookConfigurations(Repository repo, EventType eventType) {
        String clause = String.format("REPO_ID = ? AND IS_ENABLED = ? AND %s = ?", eventType.getQueryColumn());
        return activeObjects.find(WebHookConfiguration.class, Query.select().where(clause, repo.getId(), true, true));
    }

    public WebHookConfiguration getWebHookConfigurations(String id) {
        return activeObjects.get(WebHookConfiguration.class, Integer.valueOf(id));
    }

    public void deleteWebhookConfiguration(WebHookConfiguration webHookConfiguration) {
        activeObjects.delete(webHookConfiguration);
    }

    public WebHookConfiguration createOrUpdateWebHookConfiguration(Repository rep, String id, String title, String url,
                                                                   boolean enabled) {
        return createOrUpdateWebHookConfiguration(rep, id, title, url, enabled, false, true, true, true, true, true, true, true, true, true);
    }

    public WebHookConfiguration createOrUpdateWebHookConfiguration(Repository rep, String id, String title, String url,
                                                                   boolean enabled, boolean isTagCreated, boolean isBranchDeleted,
                                                                   boolean isBranchCreated, boolean isRepoPush, boolean isPrDeclined,
                                                                   boolean isPrRescoped, boolean isPrMerged, boolean isPrReopened,
                                                                   boolean isPrUpdated, boolean isPrCreated) {
        WebHookConfiguration webHookConfiguration = null;

        if (id != null) {
            webHookConfiguration = getWebHookConfigurations(id);
        }
        if (webHookConfiguration == null || !webHookConfiguration.getRepositoryId().equals(rep.getId())) {
            webHookConfiguration = activeObjects.create(WebHookConfiguration.class, new DBParam("REPO_ID", rep.getId()), new DBParam("TITLE", title), new DBParam("URL", url), new DBParam("IS_ENABLED", enabled));

        }
        webHookConfiguration.setTitle(title);
        webHookConfiguration.setEnabled(enabled);
        webHookConfiguration.setURL(url);
        webHookConfiguration.setTagCreated(isTagCreated);
        webHookConfiguration.setBranchDeleted(isBranchDeleted);
        webHookConfiguration.setBranchCreated(isBranchCreated);
        webHookConfiguration.setRepoPush(isRepoPush);
        webHookConfiguration.setPrDeclined(isPrDeclined);
        webHookConfiguration.setPrRescoped(isPrRescoped);
        webHookConfiguration.setPrMerged(isPrMerged);
        webHookConfiguration.setPrReopened(isPrReopened);
        webHookConfiguration.setPrUpdated(isPrUpdated);
        webHookConfiguration.setPrCreated(isPrCreated);

        webHookConfiguration.save();
        return webHookConfiguration;

    }
}
