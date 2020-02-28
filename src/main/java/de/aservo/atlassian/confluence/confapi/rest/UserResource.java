package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.user.User;
import com.atlassian.user.UserManager;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.model.UserBean;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Resource to set mail server configuration.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Named
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserManager userManager;

    /**
     * Constructor.
     *
     * @param userManager the injected {@link UserManager}
     */
    public UserResource(@ComponentImport UserManager userManager) {
        this.userManager = userManager;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUser(@QueryParam("username") String username) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            User user = userManager.getUser(username);
            if (user instanceof ConfluenceUserImpl) {
                return Response.ok(new UserBean((ConfluenceUserImpl)user)).build();
            } else {
                return Response.status(NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateUser(UserBean user) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            User atlUser = userManager.getUser(user.getUsername());
            if (atlUser instanceof ConfluenceUserImpl) {
                ConfluenceUserImpl confluenceUser = (ConfluenceUserImpl) atlUser;
                FieldUtils.writeDeclaredField(confluenceUser, "email", user.getEmail(), true);
                userManager.saveUser(confluenceUser);
                return getUser(user.getUsername());
            } else {
                return Response.status(NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }

    @PUT
    @Path("/password")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateUserPassword(@QueryParam("username") String username, @QueryParam("password") String password) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            User atlUser = userManager.getUser(username);
            if (atlUser instanceof ConfluenceUserImpl) {
                userManager.alterPassword(atlUser, password);
                return getUser(username);
            } else {
                return Response.status(NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }
}
