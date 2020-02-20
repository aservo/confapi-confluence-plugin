package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.mail.server.PopMailServer;
import de.aservo.atlassian.confluence.confapi.exception.NotConfiguredException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for POP mail server in REST requests.
 */
@XmlRootElement(name = "pop")
public class PopMailServerBean {

    public static final long DEFAULT_TIMEOUT = 10000L;

    @XmlElement
    private final String name;

    @XmlElement
    private final String description;

    @XmlElement
    private final String protocol;

    @XmlElement
    private final String host;

    @XmlElement
    private final Integer port;

    @XmlElement
    private final long timeout;

    @XmlElement
    private final String username;

    @XmlElement
    @EqualsExclude
    @HashCodeExclude
    private final String password;

    /**
     * The default constructor is needed for JSON request deserialization.
     */
    public PopMailServerBean() {
        this.name = null;
        this.description = null;
        this.protocol = null;
        this.host = null;
        this.port = null;
        this.timeout = DEFAULT_TIMEOUT;
        this.username = null;
        this.password = null;
    }

    private PopMailServerBean(
            final String name,
            final String description,
            final String protocol,
            final String host,
            final Integer port,
            final long timeout,
            final String username,
            final String password) {

        this.name = name;
        this.description = StringUtils.isNoneBlank(description)
                ? description
                : null;
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.username = username;
        this.password = StringUtils.isNotBlank(password)
                ? "<HIDDEN>"
                : null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProtocol() {
        return protocol != null ? protocol.toLowerCase() : null;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public long getTimeout() {
        return timeout;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static PopMailServerBean from(
            final PopMailServer popMailServer) throws NotConfiguredException {

        if (popMailServer == null) {
            throw new NotConfiguredException("No mail server defined");
        }

        Integer port = null;

        if (StringUtils.isNotBlank(popMailServer.getPort())) {
            port = Integer.parseInt(popMailServer.getPort());
        }

        return new PopMailServerBean(
                popMailServer.getName(),
                popMailServer.getDescription(),
                popMailServer.getMailProtocol().getProtocol(),
                popMailServer.getHostname(),
                port,
                popMailServer.getTimeout(),
                popMailServer.getUsername(),
                popMailServer.getPassword());
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
