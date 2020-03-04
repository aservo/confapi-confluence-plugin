package com.atlassian.confluence.settings.setup;

import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

@Data
@NoArgsConstructor
public class DefaultProductLicenseDetailsView implements SingleProductLicenseDetailsView {

    private boolean evaluationLicense;
    private String licenseTypeName;
    private String organisationName;
    private String supportEntitlementNumber;
    private String description;
    private String serverId;
    private boolean perpetualLicense;
    private Date licenseExpiryDate;
    private Date maintenanceExpiryDate;
    private boolean dataCenter;
    private boolean enterpriseLicensingAgreement;
    private String productKey;
    private boolean unlimitedNumberOfUsers;
    private String productDisplayName;
    private int numberOfUsers;

    @Nullable
    @Override
    public String getProperty(@Nonnull String s) {
        return null;
    }
}
