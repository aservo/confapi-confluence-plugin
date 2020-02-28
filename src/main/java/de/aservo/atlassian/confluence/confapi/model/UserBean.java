package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.confluence.user.ConfluenceUserImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for licence infos in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = "user")
public class UserBean {

    @XmlElement
    private String username;

    @XmlElement
    private String fullName;

    @XmlElement
    private String email;

    public UserBean(ConfluenceUserImpl confluenceUser) {
        username = confluenceUser.getName();
        fullName = confluenceUser.getFullName();
        email = confluenceUser.getEmail();
    }
}
