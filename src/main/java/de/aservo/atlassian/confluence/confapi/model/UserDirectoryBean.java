package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Bean for user directory settings in REST requests.
 */
@XmlRootElement(name = "userDirectory")
public class UserDirectoryBean {

    private static final String ATTR_CROWD_URL = "crowd.server.url";
    private static final String ATTR_CROWD_PASSWORD = "application.password";

    @XmlElement
    private boolean active;

    @XmlElement
    private String name;

    @XmlElement
    private DirectoryType type;

    @XmlElement
    private String description;

    @XmlElement
    private String crowdUrl;

    @XmlElement
    private String appPassword;

    @XmlElement
    private String implClass;

    public UserDirectoryBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DirectoryType getType() {
        return type;
    }

    public void setType(DirectoryType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCrowdUrl() {
        return crowdUrl;
    }

    public void setCrowdUrl(String crowdUrl) {
        this.crowdUrl = crowdUrl;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public String getImplClass() {
        return implClass;
    }

    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public DirectoryImpl buildDirectoryImpl() {
        DirectoryImpl directory = new DirectoryImpl();
        directory.setName(name);
        directory.setActive(active);
        directory.setType(type);
        directory.setActive(true);
        directory.setImplementationClass(implClass);
        directory.setAttribute(ATTR_CROWD_URL, crowdUrl);
        directory.setAttribute(ATTR_CROWD_PASSWORD, appPassword);
        return directory;
    }

    public static UserDirectoryBean buildUserDirectoryBean(Directory dir) {
        Map<String, String> attributes = dir.getAttributes();
        UserDirectoryBean directoryBean = new UserDirectoryBean();
        directoryBean.setName(dir.getName());
        directoryBean.setActive(dir.isActive());
        directoryBean.setType(dir.getType());
        directoryBean.setDescription(dir.getDescription());
        directoryBean.setCrowdUrl(attributes.get(ATTR_CROWD_URL));
        directoryBean.setImplClass(dir.getImplementationClass());
        return directoryBean;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

}
