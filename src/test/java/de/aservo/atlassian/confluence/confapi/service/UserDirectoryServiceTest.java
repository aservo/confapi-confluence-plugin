package de.aservo.atlassian.confluence.confapi.service;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import de.aservo.atlassian.confluence.confapi.model.UserDirectoryBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserDirectoryServiceTest {

    private CrowdDirectoryService crowdDirectoryService;
    private UserDirectoryService userDirectoryService;

    @Before
    public void inits() {
        crowdDirectoryService = mock(CrowdDirectoryService.class);
        userDirectoryService = new UserDirectoryService(crowdDirectoryService);
    }

    @Test
    public void testGetDirectories() {
        Directory directory = createDirectory();

        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();

        List<UserDirectoryBean> directories = userDirectoryService.getDirectories();

        assertEquals(directories.get(0), UserDirectoryBean.buildUserDirectoryBean(directory));
    }

    @Test
    public void testAddDirectory() throws DirectoryCurrentlySynchronisingException {
        Directory directory = createDirectory();
        UserDirectoryBean directoryBean = UserDirectoryBean.buildUserDirectoryBean(directory);
        directoryBean.setCrowdUrl("http://localhost");
        directoryBean.setClientName("confluence-client");
        directoryBean.setAppPassword("test");

        doReturn(directory).when(crowdDirectoryService).addDirectory(directory);

        UserDirectoryBean directoryAdded = userDirectoryService.addDirectory(directoryBean, false);

        assertEquals(directoryAdded.getName(), directoryBean.getName());
    }

    private Directory createDirectory() {
        return new DirectoryImpl("test", DirectoryType.CROWD, "test.class");
    }
}
