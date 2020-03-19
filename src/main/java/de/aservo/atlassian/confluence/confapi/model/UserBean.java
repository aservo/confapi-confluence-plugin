package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull
    @Size(min = 1)
    private String fullName;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String email;

    /**
     * Instantiates a new User bean.
     *
     * @param confluenceUser the confluence user
     */
    public UserBean(User confluenceUser) {
        username = confluenceUser.getName();
        fullName = confluenceUser.getFullName();
        email = confluenceUser.getEmail();
    }
}
