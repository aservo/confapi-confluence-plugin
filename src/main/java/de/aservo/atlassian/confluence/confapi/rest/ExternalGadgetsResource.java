package de.aservo.atlassian.confluence.confapi.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.service.ExternalGadgetsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

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
    @Operation(summary = "Retrieves currently configured external gadgets",
            description = "Upon successful request, returns a list of `String` containing all configured gadget configuration urls",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "Gadget successfully added. Returns the list of configured gadget links", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "![Status 204][status-204]", description = "Provided gadget link was null or empty. Operation is skipped"),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while retrieving the gadget links")
            })
    public Response getGadgets() {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            return Response.ok(externalGadgetsService.getRegisteredExternalGadgetURls()).build();
        } catch (NullArgumentException nae) {
            return Response.status(NO_CONTENT).build();
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
    @POST
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Adds a new external gadget link",
            description = "Upon successful request, returns a list of `String` containing all configured gadget configuration urls",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "All configured gadget links", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while creating the gadget link")
            },
            parameters = @Parameter(description = "Whether or not to test the external gadget link url (DEFAULT = true)", schema = @Schema(implementation = Boolean.class)),
            requestBody = @RequestBody(description = "The external gadget link url to add", required = true, content = @Content(schema = @Schema(implementation = String.class))))
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
