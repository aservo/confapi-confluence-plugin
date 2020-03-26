package de.aservo.atlassian.confluence.confapi.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confapi.model.ApplicationLinkBean;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confapi.service.api.ApplicationLinkService;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.rest.api.ApplicationLinksResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static de.aservo.atlassian.confapi.constants.ConfAPI.APPLICATION_LINKS;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * The type Application links resource.
 */
@Path(value = APPLICATION_LINKS)
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(AdminOnlyResourceFilter.class)
@Component
public class ApplicationLinksImpl implements ApplicationLinksResource {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLinksImpl.class);

    private final ApplicationLinkService applicationLinkService;

    /**
     * Instantiates a new Application links resource.
     *
     * @param applicationLinkService the application link service
     */
    @Inject
    public ApplicationLinksImpl(ApplicationLinkService applicationLinkService) {
        this.applicationLinkService = applicationLinkService;
    }

    /**
     * Gets application links.
     *
     * @return the application links
     */
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
