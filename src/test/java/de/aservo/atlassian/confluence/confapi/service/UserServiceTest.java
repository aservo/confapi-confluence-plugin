package de.aservo.atlassian.confluence.confapi.service;

import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.user.EntityException;
import com.atlassian.user.User;
import com.atlassian.user.UserManager;
import de.aservo.atlassian.confluence.confapi.model.UserBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private UserManager userManager;
    private UserService userService;

    @Before
    public void inits() {
        userManager = mock(UserManager.class);
        userService = new UserService(userManager);
    }

    @Test
    public void testGetUser() throws EntityException, UserNotFoundException {
        User user = createUser();
        doReturn(user).when(userManager).getUser(user.getName());

        UserBean userBean = userService.getUser(user.getName());

        assertEquals(userBean, new UserBean((ConfluenceUserImpl) user));
    }

    @Test
    public void testUpdateUser() throws EntityException, UserNotFoundException, IllegalAccessException {
        User user = createUser();
        UserBean userBeanUpdate = new UserBean((ConfluenceUserImpl) user);

        doReturn(user).when(userManager).getUser(user.getName());

        UserBean userBean = userService.updateUser(userBeanUpdate);

        assertEquals(userBean, userBeanUpdate);
    }

    @Test
    public void testUpdateUserNotConfluenceUser() throws EntityException, UserNotFoundException, IllegalAccessException {
        User user = createUser();
        UserBean userBeanUpdate = new UserBean((ConfluenceUserImpl) user);
        doReturn(user).when(userManager).getUser(user.getName());

        UserBean userBean = userService.updateUser(userBeanUpdate);

        assertEquals(userBean, userBeanUpdate);
    }

    @Test
    public void testUpdateUserPassword() throws EntityException, UserNotFoundException, IllegalAccessException {
        User user = createUser();
        UserBean userBeanUpdate = new UserBean((ConfluenceUserImpl) user);
        doReturn(user).when(userManager).getUser(user.getName());

        UserBean userBean = userService.updateUserPassword(user.getName(), "newPW");

        assertEquals(userBean, userBeanUpdate);
    }

    private User createUser() {
        return new ConfluenceUserImpl("test", "test user", "user@user.de");
    }
}
