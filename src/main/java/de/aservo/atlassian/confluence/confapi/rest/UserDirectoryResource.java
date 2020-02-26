package de.aservo.atlassian.confluence.confapi.rest;

import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.model.UserDirectoryBean;
import de.aservo.atlassian.confluence.confapi.service.UserDirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource to set mail server configuration.
 */
@Path("/user-directories")
@Produces(MediaType.APPLICATION_JSON)
@Named
public class UserDirectoryResource {

    private static final Logger log = LoggerFactory.getLogger(UserDirectoryResource.class);

    private final UserDirectoryService directoryService;

    /**
     * Constructor.
     *
     * @param directoryService the injected {@link UserDirectoryService}
     */
    public UserDirectoryResource(
            final UserDirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GET
    public Response getDirectories() {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            return Response.ok(directoryService.findDirectories()).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(Response.Status.NOT_FOUND).entity(errorCollection).build();
    }

    @POST
    public Response addDirectory(UserDirectoryBean directory) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            return Response.ok(directoryService.addDirectory(directory)).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(Response.Status.NOT_FOUND).entity(errorCollection).build();
    }
}
