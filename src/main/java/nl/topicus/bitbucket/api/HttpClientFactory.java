package nl.topicus.bitbucket.api;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class HttpClientFactory
{

    public HttpClient create()
    {
        return HttpClientBuilder.create().useSystemProperties().build();
    }

}
