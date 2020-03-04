package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.settings.setup.DefaultProductLicenseDetailsView;
import com.atlassian.sal.api.license.LicenseHandler;
import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import de.aservo.atlassian.confluence.confapi.model.LicenseBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class LicenseResourceTest {

    private LicenseHandler licenseHandler;
    private LicenceResource resource;

    @Before
    public void inits() {
        licenseHandler = mock(LicenseHandler.class);
        resource = new LicenceResource(licenseHandler);
    }

    @Test
    public void testGetLicense() {
        SingleProductLicenseDetailsView view = createLicenseDetails();

        doReturn(view).when(licenseHandler).getProductLicenseDetails(LicenceResource.CONFLUENCE_APP_ID);

        final Response response = resource.getLicense();
        assertEquals(response.getStatus(), 200);
        final LicenseBean licenseBean = (LicenseBean) response.getEntity();

        assertEquals(licenseBean, new LicenseBean(view));
    }

    @Test
    public void testSetLicense() {
        SingleProductLicenseDetailsView view = createLicenseDetails();

        doReturn(view).when(licenseHandler).getProductLicenseDetails(LicenceResource.CONFLUENCE_APP_ID);

        final Response response = resource.addLicense("ABCDEFG");
        assertEquals(response.getStatus(), 200);
    }

    private SingleProductLicenseDetailsView createLicenseDetails() {
        DefaultProductLicenseDetailsView license = new DefaultProductLicenseDetailsView();
        license.setProductDisplayName("jira");
        license.setOrganisationName("orga");
        license.setLicenseTypeName("dev");
        license.setDescription("blah");
        license.setLicenseExpiryDate(new Date());
        license.setNumberOfUsers(10);
        return license;
    }
}
