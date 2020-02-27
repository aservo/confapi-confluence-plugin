package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Bean for licence infos in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = "license")
public class LicenseBean {

    @XmlElement
    private String productName;

    @XmlElement
    private String licenseType;

    @XmlElement
    private String organization;

    @XmlElement
    private String description;

    @XmlElement
    private Date expiryDate;

    @XmlElement
    private int numUsers;

    public LicenseBean(SingleProductLicenseDetailsView productLicense) {
        productName = productLicense.getProductDisplayName();
        licenseType = productLicense.getLicenseTypeName();
        organization = productLicense.getOrganisationName();
        description = productLicense.getDescription();
        expiryDate = productLicense.getMaintenanceExpiryDate();
        numUsers = productLicense.getNumberOfUsers();
    }
}
