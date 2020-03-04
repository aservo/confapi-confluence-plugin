package de.aservo.atlassian.confluence.confapi.filter;

import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.plugins.rest.common.security.AuthenticationRequiredException;
import com.atlassian.plugins.rest.common.security.AuthorisationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.expect;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//power mockito required here for mocking static methods of AuthenticatedUserThreadLocal
@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthenticatedUserThreadLocal.class)
public class AdminOnlyResourceFilterTest {

    private ConfluenceUserImpl user;
    private  PermissionManager permissionManager;
    private AdminOnlyResourceFilter filter;

    @Before
    public void setup() {
        user  = new ConfluenceUserImpl("test", "test test", "test@test.de");
        permissionManager = mock(PermissionManager.class);
        filter = new AdminOnlyResourceFilter(permissionManager);
    }

    @Test(expected = AuthenticationRequiredException.class)
    public void testAdminAccessNoUser() {
        filter.filter(null);
    }

    @Test
    public void testAdminAccess() {
        PowerMock.mockStatic(AuthenticatedUserThreadLocal.class);
        expect(AuthenticatedUserThreadLocal.get()).andReturn(user);
        PowerMock.replay(AuthenticatedUserThreadLocal.class);

        when(permissionManager.isConfluenceAdministrator(user)).thenReturn(Boolean.TRUE);

        filter.filter(null);
    }

    @Test(expected = AuthorisationException.class)
    public void testNonAdminAccess() {
        PowerMock.mockStatic(AuthenticatedUserThreadLocal.class);
        expect(AuthenticatedUserThreadLocal.get()).andReturn(user);
        PowerMock.replay(AuthenticatedUserThreadLocal.class);

        when(permissionManager.isConfluenceAdministrator(user)).thenReturn(Boolean.FALSE);

        filter.filter(null);
    }
}
