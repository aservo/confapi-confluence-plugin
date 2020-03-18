package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.api.application.bamboo.BambooApplicationType;
import com.atlassian.applinks.api.application.bitbucket.BitbucketApplicationType;
import com.atlassian.applinks.api.application.confluence.ConfluenceApplicationType;
import com.atlassian.applinks.api.application.crowd.CrowdApplicationType;
import com.atlassian.applinks.api.application.fecru.FishEyeCrucibleApplicationType;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.applinks.api.application.refapp.RefAppApplicationType;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.atlassian.confluence.settings.setup.DefaultApplicationLink;
import com.atlassian.confluence.settings.setup.DefaultApplicationType;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static de.aservo.atlassian.confluence.confapi.model.ApplicationLinkTypes.JIRA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

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
        bean.setServerId(UUID.randomUUID().toString());
        bean.setAppType(JIRA.name());
        bean.setUsername("user");
        bean.setPassword("pass");
        ApplicationLinkDetails linkDetails = bean.toApplicationLinkDetails();

        assertNotNull(linkDetails);
        assertEquals(bean.getName(), linkDetails.getName());
        assertEquals(bean.getDisplayUrl(), linkDetails.getDisplayUrl().toString());
        assertEquals(bean.getRpcUrl(), linkDetails.getRpcUrl().toString());
        assertEquals(bean.isPrimary(), linkDetails.isPrimary());
    }

    @Test
    public void testLinkTypeGenerator() throws URISyntaxException {
        for (ApplicationLinkTypes linkType : ApplicationLinkTypes.values()) {
            ApplicationType applicationType = null;
            switch (linkType) {
                case BAMBOO:
                    applicationType = mock(BambooApplicationType.class);
                    break;
                case JIRA:
                    applicationType = mock(JiraApplicationType.class);
                    break;
                case BITBUCKET:
                    applicationType = mock(BitbucketApplicationType.class);
                    break;
                case CONFLUENCE:
                    applicationType = mock(ConfluenceApplicationType.class);
                    break;
                case FISHEYE:
                    applicationType = mock(FishEyeCrucibleApplicationType.class);
                    break;
                case CROWD:
                    applicationType = mock(CrowdApplicationType.class);
                    break;
            }
            ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
            URI uri = new URI("http://localhost");
            ApplicationLink applicationLink = new DefaultApplicationLink(applicationId, applicationType, "test", uri, uri, false, false);
            ApplicationLinkBean bean = new ApplicationLinkBean(applicationLink);
            assertEquals(linkType, bean.getLinkType());
        }
    }

    @Test(expected = NotImplementedException.class)
    public void testNonImplementedLinkTypeGenerator() throws URISyntaxException {
        ApplicationType applicationType = mock(RefAppApplicationType.class);
        ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        URI uri = new URI("http://localhost");
        ApplicationLink applicationLink = new DefaultApplicationLink(applicationId, applicationType, "test", uri, uri, false, false);
        new ApplicationLinkBean(applicationLink);
    }
}
