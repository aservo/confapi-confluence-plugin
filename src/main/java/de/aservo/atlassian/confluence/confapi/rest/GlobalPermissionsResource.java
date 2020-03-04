package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.security.service.AnonymousUserPermissionsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * The type Global permissions resource.
 */
@Path("/permissions/anonymous-access")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(AdminOnlyResourceFilter.class)
public class GlobalPermissionsResource {

    private static final Logger log = LoggerFactory.getLogger(GlobalPermissionsResource.class);

    private final AnonymousUserPermissionsService anonymousUserPermissionsService;

    /**
     * Instantiates a new Global permissions resource.
     *
     * @param anonymousUserPermissionsService the anonymous user permissions service
     */
    public GlobalPermissionsResource(@ComponentImport AnonymousUserPermissionsService anonymousUserPermissionsService) {
        this.anonymousUserPermissionsService = anonymousUserPermissionsService;
    }

    /**
     * Sets global access permissions.
     *
     * @param activateUse          the activate use
     * @param activateViewProfiles the activate view profiles
     * @return the global access permissions
     */
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
