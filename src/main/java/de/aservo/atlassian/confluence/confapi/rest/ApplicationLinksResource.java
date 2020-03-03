package de.aservo.atlassian.confluence.confapi.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.model.ApplicationLinkBean;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.service.ApplicationLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * Resource to set global permissions configuration.
 */
@Path("/application-links")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(AdminOnlyResourceFilter.class)
public class ApplicationLinksResource {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLinksResource.class);

    private final ApplicationLinkService applicationLinkService;

    /**
     * Constructor.
     *
     * @param applicationLinkService the injected {@link ApplicationLinkService}
     */
    public ApplicationLinksResource(ApplicationLinkService applicationLinkService) {
        this.applicationLinkService = applicationLinkService;
    }

    @GET
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

    @POST
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
