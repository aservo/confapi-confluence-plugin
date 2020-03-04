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

    @XmlElement
    private boolean active;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String name;

    @XmlElement
    @NotNull
    @Size(min = 1)
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
        directoryBean.setImplClass(dir.getImplementationClass());
        return directoryBean;
    }
}
