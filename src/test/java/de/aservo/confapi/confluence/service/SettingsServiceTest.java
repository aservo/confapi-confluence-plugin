package de.aservo.confapi.confluence.service;

import com.atlassian.confluence.settings.setup.DefaultTestSettings;
import com.atlassian.confluence.settings.setup.OtherTestSettings;
import com.atlassian.confluence.setup.settings.Settings;
import com.atlassian.confluence.setup.settings.SettingsManager;
import de.aservo.confapi.commons.model.SettingsBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SettingsServiceTest {

    @Mock
    private SettingsManager settingsManager;

    private SettingsServiceImpl settingsService;

    @BeforeEach
    public void setup() {
        settingsService = new SettingsServiceImpl(settingsManager);
    }

    @Test
    void testGetSettings() {
        final Settings settings = new DefaultTestSettings();

        doReturn(settings).when(settingsManager).getGlobalSettings();

        final SettingsBean settingsBean = settingsService.getSettings();

        final SettingsBean settingsBeanRef = new SettingsBean();
        settingsBeanRef.setBaseUrl(URI.create(settings.getBaseUrl()));
        settingsBeanRef.setTitle(settings.getSiteTitle());
        settingsBeanRef.setContactMessage(settings.getCustomContactMessage());
        settingsBeanRef.setExternalUserManagement(settings.isExternalUserManagement());

        assertEquals(settingsBeanRef, settingsBean);
    }

    @Test
    void testPutSettings() {
        final Settings defaultSettings = new DefaultTestSettings();
        doReturn(defaultSettings).when(settingsManager).getGlobalSettings();

        final Settings updateSettings = new OtherTestSettings();

        final SettingsBean requestBean = new SettingsBean();
        requestBean.setBaseUrl(URI.create(updateSettings.getBaseUrl()));
        requestBean.setTitle(updateSettings.getSiteTitle());
        requestBean.setContactMessage(updateSettings.getCustomContactMessage());
        requestBean.setExternalUserManagement(updateSettings.isExternalUserManagement());

        final SettingsBean responseBean = settingsService.setSettings(requestBean);

        final ArgumentCaptor<Settings> settingsCaptor = ArgumentCaptor.forClass(Settings.class);
        verify(settingsManager).updateGlobalSettings(settingsCaptor.capture());
        final Settings settings = settingsCaptor.getValue();

        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setBaseUrl(URI.create(settings.getBaseUrl()));
        settingsBean.setTitle(settings.getSiteTitle());
        settingsBean.setContactMessage(settings.getCustomContactMessage());
        settingsBean.setExternalUserManagement(settings.isExternalUserManagement());

        assertEquals(requestBean, settingsBean);
        assertEquals(requestBean, responseBean);
    }

    @Test
    void testPutSettingsDefaultConfig(){
        final SettingsBean settingsBean = new SettingsBean();
        
        final Settings defaultSettings = new DefaultTestSettings();
        doReturn(defaultSettings).when(settingsManager).getGlobalSettings();

        settingsService.setSettings(settingsBean);

        final ArgumentCaptor<Settings> settingsCaptor = ArgumentCaptor.forClass(Settings.class);
        verify(settingsManager).updateGlobalSettings(settingsCaptor.capture());
        final Settings settings = settingsCaptor.getValue();

        assertEquals(defaultSettings.getBaseUrl(), settings.getBaseUrl());
        assertEquals(defaultSettings.getSiteTitle(), settings.getSiteTitle());
        assertEquals(defaultSettings.getCustomContactMessage(), settings.getCustomContactMessage());
        assertEquals(defaultSettings.isExternalUserManagement(), settings.isExternalUserManagement());
    }



}
