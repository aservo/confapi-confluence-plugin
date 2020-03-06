package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.model.ApplicationLinkBean;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.model.UserDirectoryBean;
import de.aservo.atlassian.confluence.confapi.service.BeanValidationService;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The type User directory resource.
 */
@Path("/user-directories")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(AdminOnlyResourceFilter.class)
public class UserDirectoryResource {

    private static final Logger log = LoggerFactory.getLogger(UserDirectoryResource.class);

    private final CrowdDirectoryService crowdDirectoryService;

    /**
     * Instantiates a new User directory resource.
     *
     * @param crowdDirectoryService the crowd directory service
     */
    public UserDirectoryResource(@ComponentImport CrowdDirectoryService crowdDirectoryService) {
        this.crowdDirectoryService = checkNotNull(crowdDirectoryService);
    }

    /**
     * Gets directories.
     *
     * @return the directories
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Retrieves user directory information",
            description = "Upon successful request, returns a list of `UserDirectoryBean` object containing user directory details, e.g. \n```\n" +
                    "[\n" +
                    "  {\n" +
                    "    \"active\": true,\n" +
                    "    \"name\": \"Confluence Internal Directory\",\n" +
                    "    \"type\": \"INTERNAL\",\n" +
                    "    \"description\": \"Confluence default internal directory\",\n" +
                    "    \"implClass\": \"com.atlassian.crowd.directory.InternalDirectory\"\n" +
                    "  }\n" +
                    "]" +
                    "\n```",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "user directory details list", content = @Content(schema = @Schema(implementation = UserDirectoryBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while retrieving the user directory list")
            })
    public Response getDirectories() {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            List<UserDirectoryBean> directories = crowdDirectoryService.findAllDirectories().stream().map(UserDirectoryBean::buildUserDirectoryBean).collect(Collectors.toList());
            return Response.ok(directories).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }

    /**
     * Add directory.
     *
     * @param testConnection the test connection
     * @param directory      the directory
     * @return the response
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Adds a new user directory",
            description = "Upon successful request, returns the added `UserDirectoryBean` object",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "user directory added", content = @Content(schema = @Schema(implementation = ApplicationLinkBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while setting adding the new user directory")
            },
            parameters = @Parameter(description = "Whether or not to test the connection to the user directory service, e.g. CROWD (DEFAULT = true)", schema = @Schema(implementation = Boolean.class)),
            requestBody = @RequestBody(description = "The user directory to add", required = true, content = @Content(schema = @Schema(implementation = UserDirectoryBean.class))))
    public Response addDirectory(@QueryParam("test") Boolean testConnection, UserDirectoryBean directory) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            BeanValidationService.validate(directory);
            DirectoryImpl atlDir = directory.buildDirectoryImpl();
            if (testConnection == null || testConnection) {
                crowdDirectoryService.testConnection(atlDir);
            }
            Directory dir = crowdDirectoryService.addDirectory(atlDir);
            return Response.ok(UserDirectoryBean.buildUserDirectoryBean(dir)).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }
}
