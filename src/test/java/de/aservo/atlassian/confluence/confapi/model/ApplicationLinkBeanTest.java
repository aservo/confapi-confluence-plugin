package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.atlassian.confluence.settings.setup.DefaultApplicationLink;
import com.atlassian.confluence.settings.setup.DefaultApplicationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationLinkBeanTest {

    @Test
    public void testParameterConstructor() throws URISyntaxException {
        ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        URI uri = new URI("http://localhost");
        ApplicationLink applicationLink = new DefaultApplicationLink(applicationId, new DefaultApplicationType(), "test", uri, uri, false, false);
        final ApplicationLinkBean bean = new ApplicationLinkBean(applicationLink);

        assertNotNull(bean);
        assertEquals(bean.getName(), applicationLink.getName());
        assertEquals(bean.getDisplayUrl(), applicationLink.getDisplayUrl().toString());
        assertEquals(bean.getRpcUrl(), applicationLink.getRpcUrl().toString());
        assertEquals(bean.isPrimary(), applicationLink.isPrimary());
    }

    @Test
    public void testToLinkDetails() throws Exception {
        ApplicationLinkBean bean = new ApplicationLinkBean();
        bean.setName("test");
        bean.setDisplayUrl("http://localhost");
        bean.setRpcUrl("http://localhost");
        bean.setPrimary(true);
        ApplicationLinkDetails linkDetails = bean.toApplicationLinkDetails();

        assertNotNull(linkDetails);
        assertEquals(bean.getName(), linkDetails.getName());
        assertEquals(bean.getDisplayUrl(), linkDetails.getDisplayUrl().toString());
        assertEquals(bean.getRpcUrl(), linkDetails.getRpcUrl().toString());
        assertEquals(bean.isPrimary(), linkDetails.isPrimary());
    }
}
