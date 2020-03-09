package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Bean for user directory settings in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = "userDirectory")
public class UserDirectoryBean {

    /**
     * The constant ATTR_CROWD_URL.
     */
    public static final String ATTR_CROWD_URL = "crowd.server.url";
    /**
     * The constant ATTR_CROWD_PASSWORD.
     */
    public static final String ATTR_CROWD_PASSWORD = "application.password";
    /**
     * The constant ATTR_CROWD_CLIENT_NAME.
     */
    public static final String ATTR_CROWD_CLIENT_NAME = "application.name";
    /**
     * The constant ATTR_CROWD_SYNC_INTERVAL.
     */
    public static final String ATTR_CROWD_SYNC_INTERVAL = "directory.cache.synchronise.interval";
    /**
     * The constant ATTR_CROWD_NESTED_GROUPS.
     */
    public static final String ATTR_CROWD_NESTED_GROUPS = "useNestedGroups";
    /**
     * The constant ATTR_CROWD_INCREMENTAL_SYNC.
     */
    public static final String ATTR_CROWD_INCREMENTAL_SYNC = "crowd.sync.incremental.enabled";
    /**
     * The constant ATTR_CROWD_GROUP_MEMBERSHIP_SYNC.
     */
    public static final String ATTR_CROWD_GROUP_MEMBERSHIP_SYNC = "crowd.sync.group.membership.after.successful.user.auth.enabled";
    /**
     * The constant ATTR_CROWD_PROXY_HOST.
     */
    public static final String ATTR_CROWD_PROXY_HOST = "crowd.server.http.proxy.host";
    /**
     * The constant ATTR_CROWD_PROXY_PORT.
     */
    public static final String ATTR_CROWD_PROXY_PORT = "crowd.server.http.proxy.port";
    /**
     * The constant ATTR_CROWD_PROXY_USERNAME.
     */
    public static final String ATTR_CROWD_PROXY_USERNAME = "crowd.server.http.proxy.username";
    /**
     * The constant ATTR_CROWD_PROXY_PASSWORD.
     */
    public static final String ATTR_CROWD_PROXY_PASSWORD = "crowd.server.http.proxy.password";

    @XmlElement
    private boolean active;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String name;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String clientName;

    @XmlElement
    @NotNull
    private DirectoryType type;

    @XmlElement
    private String description;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String crowdUrl;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String appPassword;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String implClass;

    @XmlElement
    private String proxyHost;

    @XmlElement
    private String proxyPort;

    @XmlElement
    private String proxyUsername;

    @XmlElement
    private String proxyPassword;

    /**
     * Build directory directory.
     *
     * @return the directory
     */
    public DirectoryImpl buildDirectoryImpl() {
        DirectoryImpl directory = new DirectoryImpl();
        directory.setName(name);
        directory.setActive(active);
        directory.setType(type);
        directory.setActive(true);
        directory.setImplementationClass(implClass);
        directory.setAttribute(ATTR_CROWD_URL, crowdUrl);
        directory.setAttribute(ATTR_CROWD_PASSWORD, appPassword);
        directory.setAttribute(ATTR_CROWD_CLIENT_NAME, clientName);
        directory.setAttribute(ATTR_CROWD_PROXY_HOST, proxyHost);
        directory.setAttribute(ATTR_CROWD_PROXY_PORT, proxyPort);
        directory.setAttribute(ATTR_CROWD_PROXY_USERNAME, proxyUsername);
        directory.setAttribute(ATTR_CROWD_PROXY_PASSWORD, proxyPassword);
        directory.setAttribute(ATTR_CROWD_SYNC_INTERVAL, "3600");
        directory.setAttribute(ATTR_CROWD_NESTED_GROUPS, "false");
        directory.setAttribute(ATTR_CROWD_INCREMENTAL_SYNC, "true");
        directory.setAttribute(ATTR_CROWD_GROUP_MEMBERSHIP_SYNC, "only_when_first_created");
        return directory;
    }

    /**
     * Build user directory bean user directory bean.
     *
     * @param dir the dir
     * @return the user directory bean
     */
    public static UserDirectoryBean buildUserDirectoryBean(Directory dir) {
        Map<String, String> attributes = dir.getAttributes();
        UserDirectoryBean directoryBean = new UserDirectoryBean();
        directoryBean.setName(dir.getName());
        directoryBean.setActive(dir.isActive());
        directoryBean.setType(dir.getType());
        directoryBean.setDescription(dir.getDescription());
        directoryBean.setCrowdUrl(attributes.get(ATTR_CROWD_URL));
        directoryBean.setClientName(attributes.get(ATTR_CROWD_CLIENT_NAME));
        directoryBean.setProxyHost(attributes.get(ATTR_CROWD_PROXY_HOST));
        directoryBean.setProxyPort(attributes.get(ATTR_CROWD_PROXY_PORT));
        directoryBean.setProxyUsername(attributes.get(ATTR_CROWD_PROXY_USERNAME));
        directoryBean.setImplClass(dir.getImplementationClass());
        return directoryBean;
    }
}
