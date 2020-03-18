package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.setup.settings.Settings;
import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.model.SettingsBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource to set general configuration.
 */
@Path("/settings")
@Produces({MediaType.APPLICATION_JSON})
@ResourceFilters(AdminOnlyResourceFilter.class)
public class SettingsResource {

    private static final Logger log = LoggerFactory.getLogger(SettingsResource.class);

    @ComponentImport
    private final SettingsManager settingsManager;

    @Inject
    public SettingsResource(final SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @GET
    @Operation(summary = "Get Confluence application settings",
            description = "Returns a `SettingsBean` object with general Confluence settings like the base url or the title., e.g. \n```\n" +
                    "{\n" +
                    "   \"baseurl\": \"http://localhost:1990/confluence\",\n" +
                    "   \"title\": \"Your Confluence\"\n" +
                    "}" +
                    "\n```",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "settings object", content = @Content(schema = @Schema(implementation = SettingsBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while retrieving the settings")
            })
    public Response getSettings() {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            Settings settings = settingsManager.getGlobalSettings();
            SettingsBean settingsBean = new SettingsBean(
                    settings.getBaseUrl(),
                    settings.getSiteTitle()
            );
            return Response.ok(settingsBean).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }

    @PUT
    @Operation(summary = "Updates Confluence application settings",
            description = "Upon successful request, returns a `SettingsBean` object containing the updates settings",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "updated settings object", content = @Content(schema = @Schema(implementation = SettingsBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while updating the settings")
            },
            requestBody = @RequestBody(description = "The product license string to add", required = true, content = @Content(schema = @Schema(implementation = SettingsBean.class))))
    public Response putSettings(final SettingsBean bean) {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            Settings settings = settingsManager.getGlobalSettings();
            if (StringUtils.isNotBlank(bean.getBaseurl())) {
                settings.setBaseUrl(bean.getBaseurl());
            }
            if (StringUtils.isNotBlank(bean.getTitle())) {
                settings.setSiteTitle(bean.getTitle());
            }
            settingsManager.updateGlobalSettings(settings);
            return Response.ok(SettingsBean.from(settings)).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }
}
