package de.aservo.atlassian.confluence.confapi.service;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.exception.DirectoryInstantiationException;
import com.atlassian.plugin.spring.scanner.annotation.component.ConfluenceComponent;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.atlassian.confluence.confapi.model.UserDirectoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@ExportAsService
@ConfluenceComponent
public class UserDirectoryService {

    private static final Logger log = LoggerFactory.getLogger(UserDirectoryService.class);

    private final CrowdDirectoryService crowdDirectoryService;

    public UserDirectoryService(@ComponentImport CrowdDirectoryService crowdDirectoryService) {
        this.crowdDirectoryService = checkNotNull(crowdDirectoryService);
    }

    public List<UserDirectoryBean> findDirectories() {
        return crowdDirectoryService.findAllDirectories().stream().map(UserDirectoryBean::buildUserDirectoryBean).collect(Collectors.toList());
    }

    public UserDirectoryBean addDirectory(UserDirectoryBean directory) throws DirectoryInstantiationException {
        Directory dir = crowdDirectoryService.addDirectory(directory.buildDirectoryImpl());
        return UserDirectoryBean.buildUserDirectoryBean(dir);
    }
}
