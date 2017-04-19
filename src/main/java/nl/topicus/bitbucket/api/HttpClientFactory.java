package nl.topicus.bitbucket.api;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class HttpClientFactory
{

    public CloseableHttpClient create()
    {
        return HttpClientBuilder.create().useSystemProperties().build();
    }

}
