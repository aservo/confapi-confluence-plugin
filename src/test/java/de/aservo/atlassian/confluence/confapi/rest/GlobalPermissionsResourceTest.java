package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.security.service.AnonymousUserPermissionsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GlobalPermissionsResourceTest {

    private GlobalPermissionsResource resource;

    @Before
    public void inits() {
        AnonymousUserPermissionsService anonymousUserPermissionsService = mock(AnonymousUserPermissionsService.class);
        resource = new GlobalPermissionsResource(anonymousUserPermissionsService);
    }

    @Test
    public void testSetGlobalPermissions() {
        Response response = resource.setGlobalAccessPermissions(true, true);
        assertEquals(200, response.getStatus());
    }
}
