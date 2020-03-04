package de.aservo.atlassian.confluence.confapi.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.service.ExternalGadgetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * The type External gadgets resource.
 */
@Path("/gadgets/external")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(AdminOnlyResourceFilter.class)
public class ExternalGadgetsResource {

    private static final Logger log = LoggerFactory.getLogger(ExternalGadgetsResource.class);

    private final ExternalGadgetsService externalGadgetsService;

    /**
     * Instantiates a new External gadgets resource.
     *
     * @param externalGadgetsService the external gadgets service
     */
    public ExternalGadgetsResource(ExternalGadgetsService externalGadgetsService) {
        this.externalGadgetsService = externalGadgetsService;
    }

    /**
     * Gets gadgets.
     *
     * @return the gadgets
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getGadgets() {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            return Response.ok(externalGadgetsService.getRegisteredExternalGadgetURls()).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }

    /**
     * Add gadget.
     *
     * @param testGadgetUrl the test gadget url
     * @param gadgetUrl     the gadget url
     * @return the response
     */
    @PUT
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addGadget(@QueryParam("test") Boolean testGadgetUrl, String gadgetUrl) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            externalGadgetsService.addExternalGadgetUrl(gadgetUrl, testGadgetUrl);
            return getGadgets();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }
}
