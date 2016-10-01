package nl.topicus.bitbucket.api;

import nl.topicus.bitbucket.persistence.WebHookConfiguration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WebHookConfigurationModel {
    @XmlElement
    private Integer id;
    @XmlElement
    private String title;
    @XmlElement
    private String url;
    @XmlElement
    private boolean enabled;

    WebHookConfigurationModel(WebHookConfiguration webHookConfiguration) {
        id = webHookConfiguration.getID();
        title = webHookConfiguration.getTitle();
        url = webHookConfiguration.getURL();
        enabled = webHookConfiguration.isEnabled();
    }

    public WebHookConfigurationModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "WebHookConfigurationModel{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
