package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class UserDirectoryBeanTest {

    @Test
    public void testBuildDirectoryImpl() {
        UserDirectoryBean bean = new UserDirectoryBean();
        bean.setName("dir1");
        bean.setType(DirectoryType.CROWD);
        bean.setImplClass("test.class");
        bean.setCrowdUrl("http://localhost");
        bean.setAppPassword("test");

        DirectoryImpl directory = bean.buildDirectoryImpl();

        assertNotNull(directory);
        assertEquals(directory.getName(), bean.getName());
        assertEquals(directory.getType(), bean.getType());
        assertEquals(directory.getImplementationClass(), bean.getImplClass());
        Map<String, String> attributes = directory.getAttributes();
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_URL), bean.getCrowdUrl());
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_PASSWORD), bean.getAppPassword());
    }

    @Test
    public void testbuildUserDirectoryBean() {
        DirectoryImpl directory = new DirectoryImpl("test", DirectoryType.CROWD, "test.class");
        directory.setAttribute(UserDirectoryBean.ATTR_CROWD_URL, "http://localhost");
        directory.setAttribute(UserDirectoryBean.ATTR_CROWD_PASSWORD, "test");

        UserDirectoryBean directoryBean = UserDirectoryBean.buildUserDirectoryBean(directory);

        assertNotNull(directoryBean);
        assertEquals(directory.getName(), directoryBean.getName());
        assertEquals(directory.getType(), directoryBean.getType());
        assertEquals(directory.getImplementationClass(), directoryBean.getImplClass());
        Map<String, String> attributes = directory.getAttributes();
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_URL), directoryBean.getCrowdUrl());
        assertEquals(null, directoryBean.getAppPassword());
    }
}
