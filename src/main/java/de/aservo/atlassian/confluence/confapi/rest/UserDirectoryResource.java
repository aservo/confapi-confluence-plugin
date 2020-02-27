package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.model.UserDirectoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Resource to set mail server configuration.
 */
@Path("/user-directories")
@Produces(MediaType.APPLICATION_JSON)
@Named
public class UserDirectoryResource {

    private static final Logger log = LoggerFactory.getLogger(UserDirectoryResource.class);

    private final CrowdDirectoryService crowdDirectoryService;

    /**
     * Constructor.
     *
     * @param crowdDirectoryService the injected {@link CrowdDirectoryService}
     */
    public UserDirectoryResource(@ComponentImport CrowdDirectoryService crowdDirectoryService) {
        this.crowdDirectoryService = checkNotNull(crowdDirectoryService);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
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

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addDirectory(@QueryParam("test") Boolean testConnection, UserDirectoryBean directory) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
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
