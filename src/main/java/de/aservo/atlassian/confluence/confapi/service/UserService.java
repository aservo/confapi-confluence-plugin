package de.aservo.atlassian.confluence.confapi.service;

import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.plugin.spring.scanner.annotation.component.ConfluenceComponent;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.user.EntityException;
import com.atlassian.user.User;
import com.atlassian.user.UserManager;
import de.aservo.atlassian.confluence.confapi.model.UserBean;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type User service.
 */
@ExportAsService
@ConfluenceComponent
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserManager userManager;

    /**
     * Instantiates a new User service.
     *
     * @param userManager the user manager
     */
    public UserService(@ComponentImport UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     * @throws EntityException       the entity exception
     * @throws UserNotFoundException the user not found exception
     */
    public UserBean getUser(String username) throws EntityException, UserNotFoundException {
        User user = userManager.getUser(username);
        if (user instanceof ConfluenceUserImpl) {
            return new UserBean((ConfluenceUserImpl)user);
        } else {
            throw new UserNotFoundException(username);
        }
    }

    /**
     * Update user bean.
     *
     * @param user the user
     * @return the user bean
     * @throws EntityException        the entity exception
     * @throws IllegalAccessException the illegal access exception
     * @throws UserNotFoundException  the user not found exception
     */
    public UserBean updateUser(UserBean user) throws EntityException, IllegalAccessException, UserNotFoundException {
        BeanValidationService.validate(user);
        User atlUser = userManager.getUser(user.getUsername());
        if (atlUser instanceof ConfluenceUserImpl) {
            ConfluenceUserImpl confluenceUser = (ConfluenceUserImpl) atlUser;
            FieldUtils.writeDeclaredField(confluenceUser, "email", user.getEmail(), true);
            userManager.saveUser(confluenceUser);
            return getUser(user.getUsername());
        } else {
            throw new UserNotFoundException(user.getUsername());
        }
    }

    /**
     * Update user password.
     *
     * @param username the username
     * @param password the password
     * @return the user bean
     * @throws EntityException       the entity exception
     * @throws UserNotFoundException the user not found exception
     */
    public UserBean updateUserPassword(String username, String password) throws EntityException, UserNotFoundException {
        User atlUser = userManager.getUser(username);
        if (atlUser instanceof ConfluenceUserImpl) {
            userManager.alterPassword(atlUser, password);
            return getUser(username);
        } else {
            throw new UserNotFoundException(username);
        }
    }
}
