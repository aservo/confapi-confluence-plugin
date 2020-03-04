package de.aservo.atlassian.confluence.confapi.service;

import com.atlassian.confluence.languages.LocaleManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpec;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpecId;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpecStore;
import com.atlassian.gadgets.spec.GadgetSpec;
import com.atlassian.gadgets.spec.GadgetSpecFactory;
import com.atlassian.sal.api.user.UserKey;
import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

//power mockito required here for mocking static methods of AuthenticatedUserThreadLocal
@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthenticatedUserThreadLocal.class)
public class ExternalGadgetServiceTest {

    private ExternalGadgetSpecStore externalGadgetSpecStore;
    private GadgetSpecFactory gadgetSpecFactory;
    private LocaleManager localeManager;
    private ExternalGadgetsService externalGadgetsService;

    @Before
    public void inits() {
        externalGadgetSpecStore = mock(ExternalGadgetSpecStore.class);
        gadgetSpecFactory = mock(GadgetSpecFactory.class);
        localeManager = mock(LocaleManager.class);
        externalGadgetsService = new ExternalGadgetsService(externalGadgetSpecStore, gadgetSpecFactory, localeManager);
    }

    @Test
    public void testGetRegisteredGadgetUris() throws URISyntaxException {
        ExternalGadgetSpec externalGadgetSpec = createExternalGadgetSpec();
        doReturn(Collections.singletonList(externalGadgetSpec)).when(externalGadgetSpecStore).entries();

        List<String> registeredExternalGadgetURls = externalGadgetsService.getRegisteredExternalGadgetURls();

        assertEquals(registeredExternalGadgetURls.get(0), externalGadgetSpec.getSpecUri().toString());
    }

    @Test
    public void testAddRegisteredGadgetUrisWithoutConnectionTest() throws URISyntaxException {
        ExternalGadgetSpec externalGadgetSpec = createExternalGadgetSpec();

        externalGadgetsService.addExternalGadgetUrl(externalGadgetSpec.getSpecUri().toString(), Boolean.FALSE);
    }

    @Test
    public void testAddRegisteredGadgetUrisWithConnectionTest() throws URISyntaxException, IllegalAccessException {
        ExternalGadgetSpec externalGadgetSpec = createExternalGadgetSpec();
        ConfluenceUser user = createConfluenceUser();

        GadgetSpec gadgetSpec = GadgetSpec.gadgetSpec(externalGadgetSpec.getSpecUri()).build();

        PowerMock.mockStatic(AuthenticatedUserThreadLocal.class);
        expect(AuthenticatedUserThreadLocal.get()).andReturn(user);
        PowerMock.replay(AuthenticatedUserThreadLocal.class);

        doReturn(Locale.GERMAN).when(localeManager).getLocale(user);
        doReturn(gadgetSpec).when(gadgetSpecFactory).getGadgetSpec(externalGadgetSpec.getSpecUri(), null);

        externalGadgetsService.addExternalGadgetUrl(externalGadgetSpec.getSpecUri().toString(), Boolean.TRUE);
    }

    private ExternalGadgetSpec createExternalGadgetSpec() throws URISyntaxException {
        ExternalGadgetSpecId id = ExternalGadgetSpecId.valueOf(UUID.randomUUID().toString());
        return new ExternalGadgetSpec(id, new URI("http://localhost"));
    }

    private ConfluenceUser createConfluenceUser() throws IllegalAccessException {
        ConfluenceUser user = new ConfluenceUserImpl("test", "test test", "test@test.de");
        FieldUtils.writeDeclaredField(user, "key", new UserKey(UUID.randomUUID().toString()), true);
        return user;
    }
}
