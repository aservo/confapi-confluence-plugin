package de.aservo.atlassian.confluence.confapi.rest.api;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ExternalGadgetsResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(
            summary = "Retrieves currently configured external gadgets",
            tags = { ConfAPI.GADGETS },
            description = "Upon successful request, returns a list of `String` containing all configured gadget configuration urls",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getGadgets();

    @POST
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(
            summary = "Adds a new external gadget link",
            tags = { ConfAPI.GADGETS },
            description = "Upon successful request, returns a list of `String` containing all configured gadget configuration urls",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "204", description = "Provided gadget link was null or empty. Operation is skipped"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response addGadget(
            @QueryParam("test-gadget-url") boolean testGadgetUrl,
            String gadgetUrl);
}
