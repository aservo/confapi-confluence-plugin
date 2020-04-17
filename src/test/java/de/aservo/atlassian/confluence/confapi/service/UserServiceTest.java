package de.aservo.atlassian.confluence.confapi.service;

import com.atlassian.user.EntityException;
import com.atlassian.user.User;
import com.atlassian.user.UserManager;
import com.atlassian.user.impl.DefaultUser;
import de.aservo.atlassian.confapi.exception.BadRequestException;
import de.aservo.atlassian.confapi.exception.NotFoundException;
import de.aservo.atlassian.confapi.model.UserBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static de.aservo.atlassian.confluence.confapi.model.util.UserBeanUtil.toUser;
import static de.aservo.atlassian.confluence.confapi.model.util.UserBeanUtil.toUserBean;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserManager userManager;

    private UserServiceImpl userService;

    @Before
    public void setup() {
        userService = new UserServiceImpl(userManager);
    }

    @Test
    public void testGetUser() throws EntityException, NotFoundException {
        User user = toUser(UserBean.EXAMPLE_1);
        doReturn(user).when(userManager).getUser(user.getName());

        UserBean userBean = toUserBean(user);
        UserBean gotUserBean = userService.getUser(user.getName());

        assertEquals(userBean, gotUserBean);
        assertNull(userBean.getPassword());
    }

    @Test(expected = NotFoundException.class)
    public void testGetUserIsNotConfluenceUser() throws NotFoundException, EntityException {
        User user = new DefaultUser(toUser(UserBean.EXAMPLE_1));
        doReturn(user).when(userManager).getUser(user.getName());
        userService.getUser(user.getName());
    }

    @Test
    public void testUpdateUser() throws EntityException, NotFoundException, BadRequestException {
        final User user = toUser(UserBean.EXAMPLE_1);
        doReturn(user).when(userManager).getUser(user.getName());

        final UserBean updateUserBean = UserBean.EXAMPLE_2;
        final UserBean updatedUserBean = userService.updateUser(user.getName(), updateUserBean);

        assertEquals(updateUserBean, updatedUserBean);
    }

    @Test
    public void testUpdateUserEmptyBean() throws EntityException, NotFoundException, BadRequestException {
        final User user = toUser(UserBean.EXAMPLE_1);
        doReturn(user).when(userManager).getUser(user.getName());

        final UserBean updateUserBean = new UserBean();
        final UserBean notUpdatedUserBean = userService.updateUser(user.getName(), updateUserBean);

        assertEquals(UserBean.EXAMPLE_1, notUpdatedUserBean);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateUserNotConfluenceUser() throws EntityException, NotFoundException, BadRequestException {
        final User user = new DefaultUser(toUser(UserBean.EXAMPLE_1));
        doReturn(user).when(userManager).getUser(user.getName());

        userService.updateUser(user.getName(), UserBean.EXAMPLE_2);
    }

    @Test
    public void testUpdateUserPassword() throws EntityException, NotFoundException, BadRequestException {
        final UserBean userBean = UserBean.EXAMPLE_1;
        doReturn(toUser(userBean)).when(userManager).getUser(userBean.getUserName());

        final UserBean updateUserBean = new UserBean();
        updateUserBean.setPassword("new password");

        final UserBean updatedUserBean = userService.updateUser(userBean.getUserName(), userBean);
        assertEquals(userBean, updatedUserBean);
        // user password is not returned here, getting user bean shows update was successful
        assertNull(updatedUserBean.getPassword());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateUserPasswordNotConfluenceUser() throws EntityException, NotFoundException, BadRequestException {
        final User user = new DefaultUser(toUser(UserBean.EXAMPLE_1));
        doReturn(user).when(userManager).getUser(user.getName());

        userService.updatePassword(user.getName(), "newPW");
    }

}
