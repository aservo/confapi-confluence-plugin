package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.*;

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
        bean.setProxyHost("http://localhost/proxy");
        bean.setProxyPort("8080");
        bean.setProxyUsername("user");
        bean.setProxyPassword("pass");

        DirectoryImpl directory = bean.buildDirectoryImpl();

        assertNotNull(directory);
        assertEquals(directory.getName(), bean.getName());
        assertEquals(directory.getType(), bean.getType());
        assertEquals(directory.getImplementationClass(), bean.getImplClass());
        Map<String, String> attributes = directory.getAttributes();
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_URL), bean.getCrowdUrl());
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_PASSWORD), bean.getAppPassword());
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_PROXY_HOST), bean.getProxyHost());
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_PROXY_PORT), bean.getProxyPort());
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_PROXY_USERNAME), bean.getProxyUsername());
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_PROXY_PASSWORD), bean.getProxyPassword());
    }

    @Test
    public void testbuildUserDirectoryBean() {
        DirectoryImpl directory = new DirectoryImpl("test", DirectoryType.CROWD, "test.class");
        directory.setAttribute(UserDirectoryBean.ATTR_CROWD_URL, "http://localhost");
        directory.setAttribute(UserDirectoryBean.ATTR_CROWD_PASSWORD, "test");
        directory.setAttribute(UserDirectoryBean.ATTR_CROWD_CLIENT_NAME, "confluence-client");
        directory.setAttribute(UserDirectoryBean.ATTR_CROWD_PROXY_HOST, "http://localhost/proxy");
        directory.setAttribute(UserDirectoryBean.ATTR_CROWD_PROXY_PORT, "8080");
        directory.setAttribute(UserDirectoryBean.ATTR_CROWD_PROXY_USERNAME, "user");
        directory.setAttribute(UserDirectoryBean.ATTR_CROWD_PROXY_PASSWORD, "pass");

        UserDirectoryBean directoryBean = UserDirectoryBean.buildUserDirectoryBean(directory);

        assertNotNull(directoryBean);
        assertEquals(directory.getName(), directoryBean.getName());
        assertEquals(directory.getType(), directoryBean.getType());
        assertEquals(directory.getImplementationClass(), directoryBean.getImplClass());
        Map<String, String> attributes = directory.getAttributes();
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_URL), directoryBean.getCrowdUrl());
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_CLIENT_NAME), directoryBean.getClientName());
        assertNull(directoryBean.getAppPassword());
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_PROXY_HOST), directoryBean.getProxyHost());
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_PROXY_PORT), directoryBean.getProxyPort());
        assertEquals(attributes.get(UserDirectoryBean.ATTR_CROWD_PROXY_USERNAME), directoryBean.getProxyUsername());
        assertNull(directoryBean.getProxyPassword());
    }
}
