package de.aservo.atlassian.confluence.confapi.rest.api;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.ApplicationLinkBean;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ApplicationLinksResource {

    @GET
    @Operation(summary = "Retrieves currently configured application links",
            tags = { ConfAPI.APPLICATION_LINKS },
            description = "Upon successful request, creates a list of `ApplicationLinkBean` objects",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ApplicationLinkBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            })
    Response getApplicationLinks();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Adds a new application link",
            tags = { ConfAPI.APPLICATION_LINKS },
            description = "Upon successful request, returns a list of all configured `ApplicationLinkBean` object",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ApplicationLinkBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            })
    Response addApplicationLink(ApplicationLinkBean linkBean);
}
