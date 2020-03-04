package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import de.aservo.atlassian.confluence.confapi.service.ApplicationLinkTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Bean for licence infos in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = "applicationLink")
public class ApplicationLinkBean {

    @XmlElement
    private String serverId;

    @XmlElement
    private String appType;

    @XmlElement
    @NotNull
    private ApplicationLinkTypes linkType;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String name;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String displayUrl;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String rpcUrl;

    @XmlElement
    private boolean primary;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String username;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String password;

    /**
     * Instantiates a new Application link bean.
     *
     * @param linkDetails the link details
     */
    public ApplicationLinkBean(ApplicationLink linkDetails) {
        serverId = linkDetails.getId().toString();
        appType = linkDetails.getType().toString();
        name = linkDetails.getName();
        displayUrl = linkDetails.getDisplayUrl().toString();
        rpcUrl = linkDetails.getRpcUrl().toString();
        primary = linkDetails.isPrimary();
    }

    /**
     * To application link details application link details.
     *
     * @return the application link details
     * @throws URISyntaxException the uri syntax exception
     */
    public ApplicationLinkDetails toApplicationLinkDetails() throws URISyntaxException {
        return ApplicationLinkDetails.builder().name(name).displayUrl(new URI(displayUrl)).rpcUrl(new URI(rpcUrl)).isPrimary(primary).build();
    }
}
