package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import de.aservo.atlassian.confluence.confapi.model.UserDirectoryBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserDirectoryResourceTest {

    private CrowdDirectoryService crowdDirectoryService;
    private UserDirectoryResource resource;

    @Before
    public void inits() {
        crowdDirectoryService = mock(CrowdDirectoryService.class);
        resource = new UserDirectoryResource(crowdDirectoryService);
    }

    @Test
    public void testGetDirectories() {
        Directory directory = createDirectory();

        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();

        final Response response = resource.getDirectories();
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked") final List<UserDirectoryBean> userDirectoryBeans = (List<UserDirectoryBean>) response.getEntity();

        assertEquals(userDirectoryBeans.get(0), UserDirectoryBean.buildUserDirectoryBean(directory));
    }

    @Test
    public void testAddDirectory() {
        Directory directory = createDirectory();
        UserDirectoryBean directoryBean = UserDirectoryBean.buildUserDirectoryBean(directory);
        directoryBean.setCrowdUrl("http://localhost");
        directoryBean.setClientName("confluence-client");
        directoryBean.setAppPassword("test");

        doReturn(directory).when(crowdDirectoryService).addDirectory(directory);

        final Response response = resource.addDirectory(Boolean.FALSE, directoryBean);
        assertEquals(200, response.getStatus());
        final UserDirectoryBean userDirectoryBean = (UserDirectoryBean) response.getEntity();

        assertEquals(userDirectoryBean.getName(), directoryBean.getName());
    }

    private Directory createDirectory() {
        return new DirectoryImpl("test", DirectoryType.CROWD, "test.class");
    }
}
