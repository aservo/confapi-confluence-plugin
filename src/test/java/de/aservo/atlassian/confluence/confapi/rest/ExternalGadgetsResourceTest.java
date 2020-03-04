package de.aservo.atlassian.confluence.confapi.rest;

import de.aservo.atlassian.confluence.confapi.service.ExternalGadgetsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ExternalGadgetsResourceTest {

    @Mock
    private ExternalGadgetsService externalGadgetsService;

    private ExternalGadgetsResource resource;

    @Before
    public void inits() {
        resource = new ExternalGadgetsResource(externalGadgetsService);
    }

    @Test
    public void testGetExternalGadgets() {
        String url = "http://localhost";

        doReturn(Collections.singletonList(url)).when(externalGadgetsService).getRegisteredExternalGadgetURls();

        final Response response = resource.getGadgets();
        assertEquals(response.getStatus(), 200);
        @SuppressWarnings("unchecked") final List<String> gadgetUris = (List<String>) response.getEntity();

        assertEquals(gadgetUris.get(0), url);
    }

    @Test
    public void testAddExternalGadget() {
        String url = "http://localhost";

        final Response response = resource.addGadget(Boolean.FALSE, url);
        assertEquals(response.getStatus(), 200);
    }
}
