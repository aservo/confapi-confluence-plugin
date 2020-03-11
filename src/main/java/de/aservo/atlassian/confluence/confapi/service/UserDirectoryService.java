package de.aservo.atlassian.confluence.confapi.service;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import com.atlassian.plugin.spring.scanner.annotation.component.ConfluenceComponent;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.atlassian.confluence.confapi.model.UserDirectoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The type User directory service.
 */
@ExportAsService
@ConfluenceComponent
public class UserDirectoryService {

    private static final Logger log = LoggerFactory.getLogger(UserDirectoryService.class);

    private final CrowdDirectoryService crowdDirectoryService;

    /**
     * Instantiates a new User directory service.
     *
     * @param crowdDirectoryService the crowd directory service
     */
    public UserDirectoryService(@ComponentImport CrowdDirectoryService crowdDirectoryService) {
        this.crowdDirectoryService = checkNotNull(crowdDirectoryService);
    }

    /**
     * Gets directories.
     *
     * @return the directories
     */
    public List<UserDirectoryBean> getDirectories() {
        return crowdDirectoryService.findAllDirectories().stream().map(UserDirectoryBean::buildUserDirectoryBean).collect(Collectors.toList());
    }

    /**
     * Adds a new directory configurations. Any existing configurations with the same 'name' property are removed before adding the new configuration.
     *
     * @param directory      the directory
     * @param testConnection whether to test connection
     * @return the configuration added
     * @throws DirectoryCurrentlySynchronisingException the directory currently synchronising exception
     */
    public UserDirectoryBean addDirectory(UserDirectoryBean directory, boolean testConnection) throws DirectoryCurrentlySynchronisingException {
        //preps and validation
        BeanValidationService.validate(directory);
        DirectoryImpl atlDir = directory.buildDirectoryImpl();
        if (testConnection) {
            log.debug("testing user directory connection for " + directory.getName());
            crowdDirectoryService.testConnection(atlDir);
        }

        //check if directory exists already and if yes, remove it
        Optional<Directory> presentDirectory = crowdDirectoryService.findAllDirectories().stream().filter(dir -> dir.getName().equals(atlDir.getName())).findFirst();
        if (presentDirectory.isPresent()) {
            Directory presentDir = presentDirectory.get();
            log.info("removing existing user directory configuration '{}' before adding new configuration '{}'", presentDir.getName(), atlDir.getName());
            crowdDirectoryService.removeDirectory(presentDir.getId());
        }

        //add new directory
        return UserDirectoryBean.buildUserDirectoryBean(crowdDirectoryService.addDirectory(atlDir));
    }
}
