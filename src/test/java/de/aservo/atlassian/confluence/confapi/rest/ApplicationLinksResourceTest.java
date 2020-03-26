package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.applinks.spi.auth.AuthenticationConfigurationException;
import com.atlassian.applinks.spi.manifest.ManifestNotFoundException;
import de.aservo.atlassian.confapi.model.ApplicationLinkBean;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confapi.service.api.ApplicationLinkService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationLinksResourceTest {

    @Mock
    private ApplicationLinkService applicationLinkService;

    private ApplicationLinksImpl resource;

    @Before
    public void setup() {
        resource = new ApplicationLinksImpl(applicationLinkService);
    }

    @Test
    public void testGetApplicationLinks() {
        ApplicationLinkBean bean = createApplicationLinkBean();
        List<ApplicationLinkBean> linkBeans = new ArrayList<>();
        linkBeans.add(bean);

        doReturn(linkBeans).when(applicationLinkService).getApplicationLinks();

        final Response response = resource.getApplicationLinks();
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked") final List<ApplicationLinkBean> beans = (List<ApplicationLinkBean>) response.getEntity();

        assertEquals(beans.get(0), bean);
    }

    @Test
    public void testGetApplicationLinksWithError() {
        doThrow(new RuntimeException()).when(applicationLinkService).getApplicationLinks();

        final Response response = resource.getApplicationLinks();
        assertEquals(400, response.getStatus());

        assertNotNull(response.getEntity());
        assertEquals(ErrorCollection.class, response.getEntity().getClass());
    }

    @Test
    public void testPostApplicationLink() {
        ApplicationLinkBean bean = createApplicationLinkBean();
        List<ApplicationLinkBean> linkBeans = new ArrayList<>();
        linkBeans.add(bean);

        doReturn(linkBeans).when(applicationLinkService).getApplicationLinks();

        final Response response = resource.addApplicationLink(bean);
        assertEquals(200, response.getStatus());

        @SuppressWarnings("unchecked") final List<ApplicationLinkBean> beans = (List<ApplicationLinkBean>) response.getEntity();

        assertEquals(beans.get(0), bean);
    }

    @Test
    public void testPostApplicationLinkWithError() throws ManifestNotFoundException, AuthenticationConfigurationException, URISyntaxException {
        ApplicationLinkBean bean = createApplicationLinkBean();

        doThrow(new RuntimeException()).when(applicationLinkService).addApplicationLink(any(ApplicationLinkBean.class));

        final Response response = resource.addApplicationLink(bean);
        assertEquals(400, response.getStatus());

        assertNotNull(response.getEntity());
        assertEquals(ErrorCollection.class, response.getEntity().getClass());
    }

    private ApplicationLinkBean createApplicationLinkBean() {
        ApplicationLinkBean bean = new ApplicationLinkBean();
        bean.setName("test");
        bean.setDisplayUrl("http://localhost");
        bean.setRpcUrl("http://localhost");
        bean.setPrimary(true);
        return bean;
    }
}
