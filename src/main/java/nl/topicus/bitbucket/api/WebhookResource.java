package nl.topicus.bitbucket.api;

import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import nl.topicus.bitbucket.persistence.WebHookConfiguration;
import nl.topicus.bitbucket.persistence.WebHookConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("/projects/{projectKey}/repos/{repoSlug}/configurations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WebhookResource {

    private RepositoryService repositoryService;
    private final WebHookConfigurationDao webHookConfigurationDao;

    @Autowired
    public WebhookResource(@ComponentImport RepositoryService repositoryService, WebHookConfigurationDao webHookConfigurationDao) {
        this.repositoryService = repositoryService;
        this.webHookConfigurationDao = webHookConfigurationDao;
    }

    @GET
    public List<WebHookConfigurationModel> getWebhooks(@PathParam("projectKey") String projectKey, @PathParam("repoSlug") String repoSlug) {
        Repository repo = repositoryService.getBySlug(projectKey, repoSlug);
        if (repo == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Repository not found").build());
        }
        return Arrays.asList(webHookConfigurationDao.getWebHookConfigurations(repo)).stream().map(WebHookConfigurationModel::new).collect(Collectors.toList());
    }

    @PUT
    public WebHookConfigurationModel createWebhook(@PathParam("projectKey") String projectKey, @PathParam("repoSlug") String repoSlug, WebHookConfigurationModel newWebhook) {
        Repository repo = repositoryService.getBySlug(projectKey, repoSlug);
        if (repo == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Repository not found").build());
        }
        WebHookConfiguration createdWebhook = webHookConfigurationDao.createOrUpdateWebHookConfiguration(repo, null, newWebhook.getTitle(), newWebhook.getUrl(), newWebhook.isEnabled());
        return new WebHookConfigurationModel(createdWebhook);
    }

    @Path("/{configId}")
    @POST
    public WebHookConfigurationModel updateWebhook(@PathParam("projectKey") String projectKey, @PathParam("repoSlug") String repoSlug, WebHookConfigurationModel updatedWebhook, @PathParam("configId") String configId) {
        Repository repo = repositoryService.getBySlug(projectKey, repoSlug);
        if (repo == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Repository not found").build());
        }
        WebHookConfiguration createdWebhook = webHookConfigurationDao.createOrUpdateWebHookConfiguration(repo, configId, updatedWebhook.getTitle(), updatedWebhook.getUrl(), updatedWebhook.isEnabled());
        return new WebHookConfigurationModel(createdWebhook);
    }

    @Path("/{configId}")
    @DELETE
    public void removeWebhook(@PathParam("projectKey") String projectKey, @PathParam("repoSlug") String repoSlug, @PathParam("configId") String configId) {
        Repository repo = repositoryService.getBySlug(projectKey, repoSlug);
        if (repo == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Repository not found").build());
        }
        WebHookConfiguration webhookCOnfiguration = webHookConfigurationDao.getWebHookConfigurations(configId);
        if (webhookCOnfiguration == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Webhook not found").build());
        }
        if (webhookCOnfiguration.getRepositoryId().equals(repo.getId())) {
            webHookConfigurationDao.deleteWebhookConfiguration(webhookCOnfiguration);
        }
    }
}
