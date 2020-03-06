package de.aservo.atlassian.confluence.confapi.rest;

import de.aservo.atlassian.confluence.confapi.model.ApplicationLinkBean;
import de.aservo.atlassian.confluence.confapi.service.ApplicationLinkService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationLinksResourceTest {

    @Mock
    private ApplicationLinkService applicationLinkService;

    private ApplicationLinksResource resource;

    @Before
    public void inits() {
        resource = new ApplicationLinksResource(applicationLinkService);
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

    private ApplicationLinkBean createApplicationLinkBean() {
        ApplicationLinkBean bean = new ApplicationLinkBean();
        bean.setName("test");
        bean.setDisplayUrl("http://localhost");
        bean.setRpcUrl("http://localhost");
        bean.setPrimary(true);
        return bean;
    }
}
