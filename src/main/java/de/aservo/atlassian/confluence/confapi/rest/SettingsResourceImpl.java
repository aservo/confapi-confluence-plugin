package de.aservo.atlassian.confluence.confapi.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.rest.AbstractSettingsResourceImpl;
import de.aservo.confapi.commons.service.api.SettingsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.SETTINGS)
@ResourceFilters(AdminOnlyResourceFilter.class)
@Component
public class SettingsResourceImpl extends AbstractSettingsResourceImpl {

    @Inject
    public SettingsResourceImpl(SettingsService settingsService) {
        super(settingsService);
    }
}
