package de.aservo.atlassian.confluence.confapi.rest;

import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.service.ExternalGadgetsService;
import org.apache.commons.lang.NullArgumentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class ExternalGadgetsResourceTest {

    @Mock
    private ExternalGadgetsService externalGadgetsService;

    private ExternalGadgetsImpl resource;

    @Before
    public void setup() {
        resource = new ExternalGadgetsImpl(externalGadgetsService);
    }

    @Test
    public void testGetExternalGadgets() {
        String url = "http://localhost";

        doReturn(Collections.singletonList(url)).when(externalGadgetsService).getRegisteredExternalGadgetURls();

        final Response response = resource.getGadgets();
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked") final List<String> gadgetUris = (List<String>) response.getEntity();

        assertEquals(gadgetUris.get(0), url);
    }

    @Test
    public void testGetExternalGadgetsWithError() {
        doThrow(new RuntimeException()).when(externalGadgetsService).getRegisteredExternalGadgetURls();

        final Response response = resource.getGadgets();
        assertEquals(400, response.getStatus());

        assertNotNull(response.getEntity());
        assertEquals(ErrorCollection.class, response.getEntity().getClass());
    }

    @Test
    public void testAddExternalGadget() {
        String url = "http://localhost";

        final Response response = resource.addGadget(Boolean.FALSE, url);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testAddExternalGadgetNoContent() throws URISyntaxException {
        doThrow(new NullArgumentException("")).when(externalGadgetsService).addExternalGadgetUrl(any(String.class), any(Boolean.class));

        final Response response = resource.addGadget(Boolean.FALSE, "");
        assertEquals(204, response.getStatus());
    }

    @Test
    public void testAddExternalGadgetWithError() throws URISyntaxException {
        doThrow(new RuntimeException()).when(externalGadgetsService).addExternalGadgetUrl(any(String.class), any(Boolean.class));

        final Response response = resource.addGadget(Boolean.FALSE, "localhost");
        assertEquals(400, response.getStatus());

        assertNotNull(response.getEntity());
        assertEquals(ErrorCollection.class, response.getEntity().getClass());
    }
}
