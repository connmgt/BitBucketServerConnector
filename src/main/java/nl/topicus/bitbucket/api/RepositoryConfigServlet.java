package nl.topicus.bitbucket.api;

import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.google.common.collect.ImmutableMap;
import nl.topicus.bitbucket.persistence.DummyWebHookConfiguration;
import nl.topicus.bitbucket.persistence.WebHookConfiguration;
import nl.topicus.bitbucket.persistence.WebHookConfigurationDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RepositoryConfigServlet extends HttpServlet
{
	private final SoyTemplateRenderer soyTemplateRenderer;
	private final RepositoryService repositoryService;
	private final WebHookConfigurationDao webHookConfigurationDao;

	@Autowired
	public RepositoryConfigServlet(@ComponentImport SoyTemplateRenderer soyTemplateRenderer, @ComponentImport RepositoryService repositoryService, WebHookConfigurationDao webHookConfigurationDao)
	{
		this.soyTemplateRenderer = soyTemplateRenderer;
		this.repositoryService = repositoryService;
		this.webHookConfigurationDao = webHookConfigurationDao;
	}

	protected void render(HttpServletResponse resp, String templateName, Map<String, Object> data) throws IOException, ServletException
	{
		resp.setContentType("text/html;charset=UTF-8");
		try
		{
			soyTemplateRenderer.render(resp.getWriter(), "nl.topicus.bitbucket.bitbucket-webhooks:templates-soy", templateName, data);
		}
		catch (SoyException e)
		{
			Throwable cause = e.getCause();
			if (cause instanceof IOException)
			{
				throw (IOException) cause;
			}
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		Repository repository = getRepository(req, resp);
		if (repository == null)
		{
			return;
		}

		List<NameValuePair> queryParams = URLEncodedUtils.parse(getFullURL(req), "UTF-8");
		if (queryParams.stream().anyMatch(nameValuePair -> nameValuePair.getName().equals("edit")))
		{
			Optional<NameValuePair> id = queryParams.stream().filter(nameValuePair -> nameValuePair.getName().equals("id") || StringUtils.isNotBlank(nameValuePair.getValue())).findFirst();
			ImmutableMap.Builder<String, Object> properties = ImmutableMap.<String, Object>builder().put("repository", repository);
			if (id.isPresent())
			{
				WebHookConfiguration webHookConfiguration = webHookConfigurationDao.getWebHookConfigurations(id.get().getValue());
				if (webHookConfiguration != null && webHookConfiguration.getRepositoryId().equals(repository.getId()))
				{
					properties.put("configuration", webHookConfiguration);
				}
			}
			String template = "nl.topicus.templates.edit";
			render(resp, template, properties.build());
		}
		else
		{
			if (queryParams.stream().anyMatch(param -> param.getName().equals("delete")))
			{
				Optional<NameValuePair> id = queryParams.stream().filter(param -> param.getName().equals("id")).findFirst();
				if (id.isPresent())
				{
					WebHookConfiguration webHookConfiguration = webHookConfigurationDao.getWebHookConfigurations(id.get().getValue());
					if (webHookConfiguration != null && webHookConfiguration.getRepositoryId().equals(repository.getId()))
					{
						webHookConfigurationDao.deleteWebhookConfiguration(webHookConfiguration);
					}
				}
			}
			WebHookConfiguration[] webHookConfigurations = webHookConfigurationDao.getWebHookConfigurations(repository);
			String template = "nl.topicus.templates.repositorySettings";
			render(resp, template, ImmutableMap.<String, Object>builder().put("repository", repository).put("configurations", webHookConfigurations).build());
		}
	}

	private Repository getRepository(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		// Get repoSlug from path
		String pathInfo = req.getPathInfo();

		String[] components = pathInfo.split("/");

		if (components.length < 3)
		{
			return null;
		}

		Repository repository = repositoryService.getBySlug(components[1], components[2]);
		if (repository == null)
		{
			return null;
		}
		return repository;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		Repository repository = getRepository(req, resp);
		if (repository == null)
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String title = req.getParameter("title");
		String url = req.getParameter("url");
		String id = req.getParameter("id");
		boolean enabled = "on".equalsIgnoreCase(req.getParameter("enabled"));

		boolean isTagCreated = "on".equalsIgnoreCase(req.getParameter("isTagCreated"));
		boolean isBranchDeleted = "on".equalsIgnoreCase(req.getParameter("isBranchDeleted"));
		boolean isBranchCreated = "on".equalsIgnoreCase(req.getParameter("isBranchCreated"));
		boolean isRepoPush = "on".equalsIgnoreCase(req.getParameter("isRepoPush"));
		boolean isPrDeclined = "on".equalsIgnoreCase(req.getParameter("isPrDeclined"));
		boolean isPrRescoped = "on".equalsIgnoreCase(req.getParameter("isPrRescoped"));
		boolean isPrMerged = "on".equalsIgnoreCase(req.getParameter("isPrMerged"));
		boolean isPrReopened = "on".equalsIgnoreCase(req.getParameter("isPrReopened"));
		boolean isPrUpdated = "on".equalsIgnoreCase(req.getParameter("isPrUpdated"));
		boolean isPrCreated = "on".equalsIgnoreCase(req.getParameter("isPrCreated"));

		WebHookConfiguration webHookConfiguration = webHookConfigurationDao.createOrUpdateWebHookConfiguration(repository, id, title, url, enabled, isTagCreated, isBranchDeleted, isBranchCreated, isRepoPush, isPrDeclined, isPrRescoped, isPrMerged, isPrReopened, isPrUpdated, isPrCreated);
		if (webHookConfiguration == null)
		{
			webHookConfiguration = new DummyWebHookConfiguration(title, url, enabled, isTagCreated, isBranchDeleted, isBranchCreated, isRepoPush, isPrDeclined, isPrRescoped, isPrMerged, isPrReopened, isPrUpdated, isPrCreated);
			String template = "nl.topicus.templates.edit";
			render(resp, template, ImmutableMap.<String, Object>builder().put("repository", repository).put("configuration", webHookConfiguration).build());
		}
		else
		{
			WebHookConfiguration[] webHookConfigurations = webHookConfigurationDao.getWebHookConfigurations(repository);
			String template = "nl.topicus.templates.repositorySettings";
			render(resp, template, ImmutableMap.<String, Object>builder().put("repository", repository).put("configurations", webHookConfigurations).build());
		}
	}

	public static URI getFullURL(HttpServletRequest request) throws ServletException
	{
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		String url;
		if (queryString == null)
		{
			url = requestURL.toString();

		}
		else
		{
			url = requestURL.append('?').append(queryString).toString();
		}
		try
		{
			return new URI(url);
		}
		catch (URISyntaxException e)
		{
			throw new ServletException(e);
		}
	}

}