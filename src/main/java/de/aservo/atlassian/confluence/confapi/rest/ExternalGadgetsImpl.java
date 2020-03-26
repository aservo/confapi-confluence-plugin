package de.aservo.atlassian.confluence.confapi.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.rest.api.ExternalGadgetsResource;
import de.aservo.atlassian.confluence.confapi.service.ExternalGadgetsService;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/gadgets/external")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(AdminOnlyResourceFilter.class)
@Component
public class ExternalGadgetsImpl implements ExternalGadgetsResource {

    private static final Logger log = LoggerFactory.getLogger(ExternalGadgetsImpl.class);

    private final ExternalGadgetsService externalGadgetsService;

    /**
     * Instantiates a new External gadgets resource.
     *
     * @param externalGadgetsService the external gadgets service
     */
    @Inject
    public ExternalGadgetsImpl(ExternalGadgetsService externalGadgetsService) {
        this.externalGadgetsService = externalGadgetsService;
    }

    /**
     * Gets gadgets.
     *
     * @return the gadgets
     */
    public Response getGadgets() {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            return Response.ok(externalGadgetsService.getRegisteredExternalGadgetURls()).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }

    /**
     * Add gadget.
     *
     * @param testGadgetUrl the test gadget url
     * @param gadgetUrl     the gadget url
     * @return the response
     */
    public Response addGadget(@DefaultValue("true") boolean testGadgetUrl, String gadgetUrl) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            externalGadgetsService.addExternalGadgetUrl(gadgetUrl, testGadgetUrl);
            return getGadgets();
        } catch (NullArgumentException nae) {
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }
}
