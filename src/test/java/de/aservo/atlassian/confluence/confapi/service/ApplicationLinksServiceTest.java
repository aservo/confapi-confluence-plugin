package de.aservo.atlassian.confluence.confapi.service;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationException;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.manifest.ManifestNotFoundException;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.atlassian.confluence.settings.setup.DefaultApplicationLink;
import com.atlassian.confluence.settings.setup.DefaultApplicationType;
import de.aservo.atlassian.confluence.confapi.model.ApplicationLinkBean;
import de.aservo.atlassian.confluence.confapi.model.ApplicationLinkTypes;
import de.aservo.atlassian.confluence.confapi.model.DefaultAuthenticationScenario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ValidationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationLinksServiceTest {

    private MutatingApplicationLinkService mutatingApplicationLinkService;
    private ApplicationLinkService applicationLinkService;

    @Before
    public void inits() {
        mutatingApplicationLinkService = mock(MutatingApplicationLinkService.class);
        TypeAccessor typeAccessor = mock(TypeAccessor.class);
        applicationLinkService = new ApplicationLinkService(mutatingApplicationLinkService, typeAccessor);
    }

    @Test
    public void testDefaultDefaultAuthenticationScenarioImpl() {
        DefaultAuthenticationScenario defaultAuthenticationScenario = new DefaultAuthenticationScenario();
        assertTrue(defaultAuthenticationScenario.isCommonUserBase());
        assertTrue(defaultAuthenticationScenario.isTrusted());
    }

    @Test
    public void testGetApplicationLinks() throws URISyntaxException {
        ApplicationLink applicationLink = createApplicationLink();
        doReturn(Collections.singletonList(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();

        List<ApplicationLinkBean> applicationLinks = applicationLinkService.getApplicationLinks();

        assertEquals(applicationLinks.get(0), new ApplicationLinkBean(applicationLink));
    }

    @Test
    public void testaddApplicationLink() throws URISyntaxException, ManifestNotFoundException, AuthenticationConfigurationException {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();

        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(null);
        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(any(ApplicationType.class), any(ApplicationLinkDetails.class));

        ApplicationLink applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkBean);

        assertEquals(applicationLinkResponse.getName(), applicationLinkBean.getName());
    }

    @Test(expected = ValidationException.class)
    public void testaddApplicationLinkMissingLinkType() throws URISyntaxException, ManifestNotFoundException, AuthenticationConfigurationException {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();
        applicationLinkBean.setLinkType(null);

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(null, null);

        applicationLinkService.addApplicationLink(applicationLinkBean);
    }

    @Test
    public void testApplicationLinkTypeConverter() throws URISyntaxException, ManifestNotFoundException, AuthenticationConfigurationException {
        for (ApplicationLinkTypes linkType : ApplicationLinkTypes.values()) {
            ApplicationLink applicationLink = createApplicationLink();
            ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();
            applicationLinkBean.setLinkType(linkType);

            doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(null);
            doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(any(ApplicationType.class), any(ApplicationLinkDetails.class));

            ApplicationLink applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkBean);

            assertEquals(applicationLinkResponse.getName(), applicationLinkBean.getName());
            assertEquals(applicationLinkResponse.getType(), applicationLink.getType());
        }
    }

    private ApplicationLinkBean createApplicationLinkBean() throws URISyntaxException {
        ApplicationLinkBean bean = new ApplicationLinkBean(createApplicationLink());
        bean.setLinkType(ApplicationLinkTypes.CROWD);
        bean.setUsername("test");
        bean.setPassword("test");
        return bean;
    }

    private ApplicationLink createApplicationLink() throws URISyntaxException {
        ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        URI uri = new URI("http://localhost");
        return new DefaultApplicationLink(applicationId, new DefaultApplicationType(), "test", uri, uri, false, false);
    }
}
