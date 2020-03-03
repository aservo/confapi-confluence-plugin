package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.license.LicenseHandler;
import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.model.LicenseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource to set mail server configuration.
 */
@Path("/license")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(AdminOnlyResourceFilter.class)
public class LicenceResource {

    private static final Logger log = LoggerFactory.getLogger(LicenceResource.class);

    private static final String CONFLUENCE_APP_ID = "CONF";

    private final LicenseHandler licenseHandler;

    /**
     * Constructor.
     *
     * @param licenseHandler the injected {@link LicenseHandler}
     */
    public LicenceResource(@ComponentImport LicenseHandler licenseHandler) {
        this.licenseHandler = licenseHandler;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getLicense() {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            SingleProductLicenseDetailsView conf = licenseHandler.getProductLicenseDetails(CONFLUENCE_APP_ID);
            assert conf != null;
            LicenseBean licenseBean = new LicenseBean(conf);
            return Response.ok(licenseBean).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }

    @POST
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addLicense(String licenseKey) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            licenseHandler.addProductLicense(CONFLUENCE_APP_ID, licenseKey);
            return getLicense();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }
}
