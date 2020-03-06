package de.aservo.atlassian.confluence.confapi.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.model.UserBean;
import de.aservo.atlassian.confluence.confapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
 * The type User resource.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(AdminOnlyResourceFilter.class)
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    /**
     * Instantiates a new User resource.
     *
     * @param userService the user service
     */
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Retrieves user information",
            description = "Upon successful request, returns a `UserBean` object containing user details, e.g. \n```\n" +
                    "{\n" +
                    "  \"username\": \"admin\",\n" +
                    "  \"fullName\": \"admin\",\n" +
                    "  \"email\": \"admin@example.com\"\n" +
                    "}" +
                    "\n```",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "user details", content = @Content(schema = @Schema(implementation = UserBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while retrieving the user details")
            })
    public Response getUser(@QueryParam("username") String username) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            return Response.ok(userService.getUser(username)).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }

    /**
     * Update user.
     *
     * @param user the user
     * @return the response
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Updates user details",
            description = "Upon successful request, returns the updated `UserBean` object. NOTE: Currently only the email address is updated from the provided `UserBean` parameter.",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "updated user details", content = @Content(schema = @Schema(implementation = UserBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while updating the user")
            },
            requestBody = @RequestBody(description = "The user details to update", required = true, content = @Content(schema = @Schema(implementation = UserBean.class))))
    public Response updateUser(UserBean user) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            return Response.ok(userService.updateUser(user)).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }

    /**
     * Update user password.
     *
     * @param username the username
     * @param password the password
     * @return the response
     */
    @PUT
    @Path("/password")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Updates the user password",
            description = "Upon successful request, returns the updated `UserBean` object.",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "updated user details", content = @Content(schema = @Schema(implementation = UserBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while updating the user password")
            },
            parameters = {
                    @Parameter(description = "The username of the user for which a new password shall be stored", schema = @Schema(implementation = String.class)),
                    @Parameter(description = "The new password", schema = @Schema(implementation = String.class))
            })
    public Response updateUserPassword(@QueryParam("username") String username, @QueryParam("password") String password) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            return Response.ok(userService.updateUserPassword(username, password)).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }
}
