package nl.topicus.bitbucket.api;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.httpclient.api.factory.HttpClientFactory;
import com.atlassian.httpclient.api.factory.HttpClientOptions;
import com.atlassian.httpclient.api.factory.ProxyOptions;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AtlassianHttpClientFactory
{

    private final HttpClientFactory httpClientFactory;

    @Autowired
    public AtlassianHttpClientFactory(@ComponentImport HttpClientFactory httpClientFactory)
    {
        this.httpClientFactory = httpClientFactory;
    }

    public HttpClient create()
    {
        HttpClientOptions options = new HttpClientOptions();
        ProxyOptions proxyOptions = ProxyOptions.ProxyOptionsBuilder.create().withDefaultSystemProperties().build();
        options.setProxyOptions(proxyOptions);
        return httpClientFactory.create(options);
    }

}
