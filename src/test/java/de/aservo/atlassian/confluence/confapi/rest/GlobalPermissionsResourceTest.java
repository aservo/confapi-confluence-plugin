package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.security.service.AnonymousUserPermissionsService;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GlobalPermissionsResourceTest {

    private GlobalPermissionsResource resource;
    private AnonymousUserPermissionsService anonymousUserPermissionsService;

    @Before
    public void setup() {
        anonymousUserPermissionsService = mock(AnonymousUserPermissionsService.class);
        resource = new GlobalPermissionsResource(anonymousUserPermissionsService);
    }

    @Test
    public void testSetGlobalPermissions() {
        Response response = resource.setGlobalAccessPermissions(true, true);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testSetGlobalPermissionsWithError() {
        doThrow(new RuntimeException()).when(anonymousUserPermissionsService).setUsePermission(true);

        Response response = resource.setGlobalAccessPermissions(true, true);
        assertEquals(400, response.getStatus());

        assertNotNull(response.getEntity());
        assertEquals(ErrorCollection.class, response.getEntity().getClass());
    }
}
