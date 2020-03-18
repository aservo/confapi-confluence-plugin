package de.aservo.atlassian.confluence.confapi.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.model.ApplicationLinkBean;
import de.aservo.atlassian.confluence.confapi.service.ApplicationLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * The type Application links resource.
 */
@Path("/application-links")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(AdminOnlyResourceFilter.class)
public class ApplicationLinksResource {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLinksResource.class);

    private final ApplicationLinkService applicationLinkService;

    /**
     * Instantiates a new Application links resource.
     *
     * @param applicationLinkService the application link service
     */
    public ApplicationLinksResource(ApplicationLinkService applicationLinkService) {
        this.applicationLinkService = applicationLinkService;
    }

    /**
     * Gets application links.
     *
     * @return the application links
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieves currently configured application links",
        description = "Upon successful request, creates a list of `ApplicationLinkBean` objects, e.g. \n```\n" +
                "[\n" +
                "  {\n" +
                "    \"serverId\": \"9f2d636e-c842-3388-8a66-17c1b951dd45\",\n" +
                "    \"appType\": \"jira\",\n" +
                "    \"name\": \"Jira TEST\",\n" +
                "    \"displayUrl\": \"http://localhost:2990/jira\",\n" +
                "    \"rpcUrl\": \"http://localhost:2990/jira\",\n" +
                "    \"primary\": true\n" +
                "  }\n" +
                "]" +
                "\n```",
        responses = {
                @ApiResponse(responseCode = "![Status 200][status-200]", description = "List of application links", content = @Content(schema = @Schema(implementation = ApplicationLinkBean.class))),
                @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while retrieving the application links")
        })
    public Response getApplicationLinks() {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            return Response.ok(applicationLinkService.getApplicationLinks()).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }

    /**
     * Add application link.
     *
     * @param linkBean the link bean
     * @return the response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Adds a new application link",
            description = "Upon successful request, returns a list of all configured `ApplicationLinkBean` object",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "List of all configured application links", content = @Content(schema = @Schema(implementation = ApplicationLinkBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while creating the application link")
            },
            requestBody = @RequestBody(description = "The application link to add", required = true, content = @Content(schema = @Schema(implementation = ApplicationLinkBean.class))))
    public Response addApplicationLink(ApplicationLinkBean linkBean) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            applicationLinkService.addApplicationLink(linkBean);
            return getApplicationLinks();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }
}
