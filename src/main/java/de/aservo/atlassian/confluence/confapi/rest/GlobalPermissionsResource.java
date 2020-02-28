package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.security.service.AnonymousUserPermissionsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * Resource to set global permissions configuration.
 */
@Path("/permissions/anonymous-access")
@Produces(MediaType.APPLICATION_JSON)
@Named
public class GlobalPermissionsResource {

    private static final Logger log = LoggerFactory.getLogger(GlobalPermissionsResource.class);

    private final AnonymousUserPermissionsService anonymousUserPermissionsService;

    /**
     * Constructor.
     *
     * @param anonymousUserPermissionsService the injected {@link AnonymousUserPermissionsService}
     */
    public GlobalPermissionsResource(@ComponentImport AnonymousUserPermissionsService anonymousUserPermissionsService) {
        this.anonymousUserPermissionsService = anonymousUserPermissionsService;
    }

    @PUT
    public Response setGlobalAccessPermissions(@QueryParam("activateUse") boolean activateUse, @QueryParam("activateViewProfiles") boolean activateViewProfiles) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            anonymousUserPermissionsService.setUsePermission(activateUse);
            anonymousUserPermissionsService.setViewUserProfilesPermission(activateViewProfiles);
            return Response.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }
}
