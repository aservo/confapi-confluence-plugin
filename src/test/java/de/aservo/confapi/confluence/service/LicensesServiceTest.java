package de.aservo.confapi.confluence.service;

import com.atlassian.confluence.settings.setup.DefaultSingleProductLicenseDetailsView;
import com.atlassian.sal.api.i18n.InvalidOperationException;
import com.atlassian.sal.api.license.LicenseHandler;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.atlassian.confluence.setup.ConfluenceBootstrapConstants.DEFAULT_LICENSE_REGISTRY_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class LicensesServiceTest {

    @Mock
    private LicenseHandler licenseHandler;

    private LicensesServiceImpl licenseService;

    @Before
    public void setup() {
        licenseService = new LicensesServiceImpl(licenseHandler);
    }

    @Test
    public void testGetLicenses() {
        DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(LicenseBean.EXAMPLE_1);
        doReturn(testLicense).when(licenseHandler).getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);

        LicensesBean licenses = licenseService.getLicenses();

        assertEquals(testLicense.getDescription(), licenses.getLicenses().iterator().next().getDescription());
    }

    @Test
    public void testGetLicense() {
        DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(LicenseBean.EXAMPLE_1);
        doReturn(testLicense).when(licenseHandler).getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);

        LicenseBean license = licenseService.getLicense(LicenseBean.EXAMPLE_1.getProducts().iterator().next());

        assertNotNull(license);
    }

    @Test(expected = NotFoundException.class)
    public void testGetLicenseNotFound() {
        DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(LicenseBean.EXAMPLE_1);
        doReturn(testLicense).when(licenseHandler).getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);
        licenseService.getLicense("not_exists");
    }

    @Test(expected = BadRequestException.class)
    public void testSetLicensesWithError() throws InvalidOperationException {
        LicensesBean licensesBean = LicensesBean.EXAMPLE_1;
        DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(licensesBean.getLicenses().iterator().next());
        doReturn(true).when(licenseHandler).hostAllowsMultipleLicenses();
        doReturn(testLicense).when(licenseHandler).getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);
        doThrow(new InvalidOperationException("", "")).when(licenseHandler).removeProductLicense(any(String.class));

        licenseService.setLicenses(licensesBean);
    }

    @Test
    public void testSetLicenses() {
        LicensesBean licensesBean = LicensesBean.EXAMPLE_1;
        DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(licensesBean.getLicenses().iterator().next());
        doReturn(false).when(licenseHandler).hostAllowsMultipleLicenses();
        doReturn(testLicense).when(licenseHandler).getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);

        LicensesBean updatedLicensesBean = licenseService.setLicenses(licensesBean);

        assertEquals(testLicense.getDescription(), updatedLicensesBean.getLicenses().iterator().next().getDescription());
    }

    @Test
    public void testSetLicense() {
        LicenseBean licenseBean = LicenseBean.EXAMPLE_1;
        DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(licenseBean);

        LicenseBean updatedLicenseBean = licenseService.setLicense(LicenseBean.EXAMPLE_1.getProducts().iterator().next(), licenseBean);

        assertEquals(testLicense.getDescription(), updatedLicenseBean.getDescription());
    }

    @Test
    public void testAddLicense() {
        LicenseBean licenseBean = LicenseBean.EXAMPLE_1;
        DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(licenseBean);

        LicenseBean updatedLicenseBean = licenseService.addLicense(licenseBean);

        assertEquals(testLicense.getDescription(), updatedLicenseBean.getDescription());
    }

    @Test(expected = BadRequestException.class)
    public void testSetLicenseWithError() throws InvalidOperationException {
        LicenseBean licenseBean = LicenseBean.EXAMPLE_1;
        doThrow(new InvalidOperationException("", "")).when(licenseHandler).addProductLicense(any(), any());

        licenseService.addLicense(licenseBean);
    }
}
