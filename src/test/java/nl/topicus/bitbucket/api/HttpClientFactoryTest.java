package nl.topicus.bitbucket.api;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class HttpClientFactoryTest {
    @Rule
    public WireMockRule server;
    @Rule
    public WireMockRule proxy;

    private String hostName;
    private HttpClientFactory httpClientFactory;
    private URI serverURI;

    public HttpClientFactoryTest() throws UnknownHostException {
        hostName = InetAddress.getLocalHost().getHostName();
        server = new WireMockRule(options().port(9999).bindAddress(hostName));
        proxy = new WireMockRule(options().port(8888).bindAddress(hostName));
    }

    @Before
    public void setUp() throws Exception {
        httpClientFactory = new HttpClientFactory();
        serverURI = new URIBuilder().setScheme("http").setHost(hostName).setPort(9999).build();
        server.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse().withStatus(200)));
        proxy.stubFor(get(urlMatching("/"))
                .willReturn(aResponse().withStatus(200)));
    }

    @After
    public void tearDown() throws Exception {
        System.clearProperty("http.proxyHost");
        System.clearProperty("http.nonProxyHosts");
        System.clearProperty("http.proxyPort");
    }

    @Test
    public void testCreateClient() throws Exception {
        HttpClient httpClient = httpClientFactory.create();
        assertThat(httpClient, is(notNullValue()));
    }

    @Test
    public void testThatProxyIsSelected() throws Exception {
        System.setProperty("http.proxyHost", hostName);
        System.setProperty("http.proxyPort", "8888");

        HttpClient httpClient = httpClientFactory.create();
        assertThat(httpClient, is(notNullValue()));
        HttpGet httpGet = new HttpGet(serverURI);
        HttpResponse execute = httpClient.execute(httpGet);
        assertThat(execute.getStatusLine().getStatusCode(), is(200));

        proxy.verify(getRequestedFor(urlEqualTo("/")));
        server.verify(0, getRequestedFor(urlEqualTo("/")));
    }

    @Test
    public void testThatNonProxyHostsIsApplied() throws Exception {

        System.setProperty("http.proxyHost", hostName);
        System.setProperty("http.nonProxyHosts", hostName);
        System.setProperty("http.proxyPort", "8888");

        HttpClient httpClient = httpClientFactory.create();
        assertThat(httpClient, is(notNullValue()));
        HttpGet httpGet = new HttpGet(serverURI);
        HttpResponse execute = httpClient.execute(httpGet);
        assertThat(execute.getStatusLine().getStatusCode(), is(200));

        proxy.verify(0, getRequestedFor(urlEqualTo("/")));
        server.verify(getRequestedFor(urlEqualTo("/")));
    }

}
