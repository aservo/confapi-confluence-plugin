package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.confluence.settings.setup.DefaultProductLicenseDetailsView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class LicenseBeanTest {

    @Test
    public void testParameterConstructor() {
        DefaultProductLicenseDetailsView license = new DefaultProductLicenseDetailsView();
        license.setProductDisplayName("jira");
        license.setOrganisationName("orga");
        license.setLicenseTypeName("dev");
        license.setDescription("blah");
        license.setLicenseExpiryDate(new Date());
        license.setNumberOfUsers(10);

        LicenseBean bean = new LicenseBean(license);

        assertNotNull(bean);
        assertEquals(bean.getProductName(), license.getProductDisplayName());
        assertEquals(bean.getOrganization(), license.getOrganisationName());
        assertEquals(bean.getLicenseType(), license.getLicenseTypeName());
        assertEquals(bean.getDescription(), license.getDescription());
        assertEquals(bean.getExpiryDate(), license.getMaintenanceExpiryDate());
        assertEquals(bean.getNumUsers(), license.getNumberOfUsers());
    }
}
