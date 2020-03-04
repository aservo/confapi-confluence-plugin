package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.user.EntityException;
import de.aservo.atlassian.confluence.confapi.model.UserBean;
import de.aservo.atlassian.confluence.confapi.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

    @Mock
    private UserService userService;

    private UserResource resource;

    @Before
    public void inits() {
        resource = new UserResource(userService);
    }

    @Test
    public void testGetUser() throws EntityException, UserNotFoundException {
        ConfluenceUserImpl user = new ConfluenceUserImpl("test", "test user", "user@user.de");
        UserBean bean = new UserBean(user);

        doReturn(bean).when(userService).getUser(bean.getUsername());

        final Response response = resource.getUser(bean.getUsername());
        assertEquals(response.getStatus(), 200);
        final UserBean userBean = (UserBean) response.getEntity();

        assertEquals(userBean, bean);
    }

    @Test
    public void testUpdateUser() throws EntityException, UserNotFoundException, IllegalAccessException {
        ConfluenceUserImpl user = new ConfluenceUserImpl("test", "test user", "user@user.de");
        UserBean bean = new UserBean(user);

        doReturn(bean).when(userService).updateUser(bean);

        final Response response = resource.updateUser(bean);
        assertEquals(response.getStatus(), 200);
        final UserBean userBean = (UserBean) response.getEntity();

        assertEquals(userBean, bean);
    }

    @Test
    public void testUpdateUserPassword() throws EntityException, UserNotFoundException, IllegalAccessException {
        ConfluenceUserImpl user = new ConfluenceUserImpl("test", "test user", "user@user.de");
        UserBean bean = new UserBean(user);

        doReturn(bean).when(userService).updateUserPassword(bean.getUsername(), "newPW");

        final Response response = resource.updateUserPassword(bean.getUsername(), "newPW");
        assertEquals(response.getStatus(), 200);
        final UserBean userBean = (UserBean) response.getEntity();

        assertEquals(userBean, bean);
    }
}
