package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.mail.MailException;
import com.atlassian.mail.MailProtocol;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.mail.server.PopMailServer;
import com.atlassian.mail.server.SMTPMailServer;
import com.atlassian.mail.server.impl.PopMailServerImpl;
import com.atlassian.mail.server.impl.SMTPMailServerImpl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilter;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.model.PopMailServerBean;
import de.aservo.atlassian.confluence.confapi.model.SmtpMailServerBean;
import de.aservo.atlassian.confluence.confapi.util.MailProtocolUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource to set mail server configuration.
 */
@Path("/mail")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(AdminOnlyResourceFilter.class)
public class MailServerResource {

    private static final Logger log = LoggerFactory.getLogger(MailServerResource.class);

    @ComponentImport
    private final MailServerManager mailServerManager;

    /**
     * Constructor.
     *
     * @param mailServerManager the injected {@link MailServerManager}
     */
    @Inject
    public MailServerResource(
            final MailServerManager mailServerManager) {

        this.mailServerManager = mailServerManager;
    }

    @GET
    @Path("smtp")
    @Operation(summary = "Retrieves the current SMTP mailserver configuration",
            description = "Returns a `SmtpMailServerBean` object with the configuration of the SMTP mail server, if any server is defined.., e.g. \n```\n" +
                    "{\n" +
                    "    \"name\": \"Localhost\",\n" +
                    "    \"description\": \"The localhost SMTP server\",\n" +
                    "    \"from\": \"confluence@localhost\",\n" +
                    "    \"prefix\": \"Confluence\",\n" +
                    "    \"protocol\": \"smtp\",\n" +
                    "    \"host\": \"localhost\",\n" +
                    "    \"port\": 25,\n" +
                    "    \"tls\", false,\n" +
                    "    \"timeout\": 10000,\n" +
                    "    \"username\": \"admin\",\n" +
                    "    \"password\": \"admin\"\n" +
                    "}" +
                    "\n```",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "smtp mailserver configuration", content = @Content(schema = @Schema(implementation = SmtpMailServerBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while retrieving the settings")
            })
    public Response getSmtpMailServer() {
        final ErrorCollection errorCollection = new ErrorCollection();

        try {
            final SMTPMailServer smtpMailServer = mailServerManager.getDefaultSMTPMailServer();
            final SmtpMailServerBean bean = SmtpMailServerBean.from(smtpMailServer);
            return Response.ok(bean).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(Response.Status.NOT_FOUND).entity(errorCollection).build();
    }

    @PUT
    @Path("smtp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Updates the SMTP mailserver configuration",
            description = "Upon successful request, returns a `SmtpMailServerBean` object containing the updates settings",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "updated settings object", content = @Content(schema = @Schema(implementation = SmtpMailServerBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while updating the settings")
            },
            requestBody = @RequestBody(description = "The mailserver configuration to update", required = true, content = @Content(schema = @Schema(implementation = SmtpMailServerBean.class))))
    public Response putSmtpMailServer(
            final SmtpMailServerBean bean) {

        final ErrorCollection errorCollection = new ErrorCollection();

        final SMTPMailServer smtpMailServer = mailServerManager.isDefaultSMTPMailServerDefined()
                ? mailServerManager.getDefaultSMTPMailServer()
                : new SMTPMailServerImpl();

        assert smtpMailServer != null;

        if (StringUtils.isNotBlank(bean.getName())) {
            smtpMailServer.setName(bean.getName());
        }

        if (StringUtils.isNotBlank(bean.getDescription())) {
            smtpMailServer.setDescription(bean.getDescription());
        }

        if (StringUtils.isNotBlank(bean.getFrom())) {
            smtpMailServer.setDefaultFrom(bean.getFrom());
        }

        if (StringUtils.isNotBlank(bean.getPrefix())) {
            smtpMailServer.setPrefix(bean.getPrefix());
        }

        smtpMailServer.setMailProtocol(MailProtocolUtil.find(bean.getProtocol(), MailProtocol.SMTP));

        if (StringUtils.isNotBlank(bean.getHost())) {
            smtpMailServer.setHostname(bean.getHost());
        }

        if (bean.getPort() != null) {
            smtpMailServer.setPort(String.valueOf(bean.getPort()));
        } else {
            smtpMailServer.setPort(smtpMailServer.getMailProtocol().getDefaultPort());
        }

        smtpMailServer.setTlsRequired(bean.isTls());

        if (StringUtils.isNotBlank(bean.getUsername())) {
            smtpMailServer.setUsername(bean.getUsername());
        }

        smtpMailServer.setTimeout(bean.getTimeout());

        try {
            if (mailServerManager.isDefaultSMTPMailServerDefined()) {
                mailServerManager.update(smtpMailServer);
            } else {
                smtpMailServer.setId(mailServerManager.create(smtpMailServer));
            }

            return Response.ok(bean).build();
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }

    @GET
    @Path("pop")
    @Operation(summary = "Retrieves the current POP mailserver configuration",
            description = "Returns a `PopMailServerBean` object with the configuration of the POP mail server, if any server is defined.., e.g. \n```\n" +
                    "{\n" +
                    "    \"name\": \"Localhost\",\n" +
                    "    \"description\": \"The localhost SMTP server\",\n" +
                    "    \"protocol\": \"pop\",\n" +
                    "    \"host\": \"localhost\",\n" +
                    "    \"port\": 110,\n" +
                    "    \"timeout\": 10000,\n" +
                    "    \"username\": \"admin\",\n" +
                    "    \"password\": \"admin\"\n" +
                    "}" +
                    "\n```",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "pop mailserver configuration", content = @Content(schema = @Schema(implementation = PopMailServerBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while retrieving the settings")
            })
    public Response getPopMailServer() {
        final ErrorCollection errorCollection = new ErrorCollection();

        try {
            final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer();
            final PopMailServerBean bean = PopMailServerBean.from(popMailServer);
            return Response.ok(bean).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(Response.Status.NOT_FOUND).entity(errorCollection).build();
    }

    @PUT
    @Path("pop")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Updates the POP mailserver configuration",
            description = "Upon successful request, returns a `PopMailServerBean` object containing the updates settings",
            responses = {
                    @ApiResponse(responseCode = "![Status 200][status-200]", description = "updated settings object", content = @Content(schema = @Schema(implementation = PopMailServerBean.class))),
                    @ApiResponse(responseCode = "![Status 400][status-400]", description = "An error occured while updating the settings")
            },
            requestBody = @RequestBody(description = "The mailserver configuration to update", required = true, content = @Content(schema = @Schema(implementation = PopMailServerBean.class))))
    public Response putPopMailServer(
            final PopMailServerBean bean) {

        final ErrorCollection errorCollection = new ErrorCollection();

        final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer() != null
                ? mailServerManager.getDefaultPopMailServer()
                : new PopMailServerImpl();

        assert popMailServer != null;

        if (StringUtils.isNotBlank(bean.getName())) {
            popMailServer.setName(bean.getName());
        }

        if (StringUtils.isNotBlank(bean.getDescription())) {
            popMailServer.setDescription(bean.getDescription());
        }

        popMailServer.setMailProtocol(MailProtocolUtil.find(bean.getProtocol(), MailProtocol.POP));

        if (StringUtils.isNotBlank(bean.getHost())) {
            popMailServer.setHostname(bean.getHost());
        }

        if (bean.getPort() != null) {
            popMailServer.setPort(String.valueOf(bean.getPort()));
        } else {
            popMailServer.setPort(popMailServer.getMailProtocol().getDefaultPort());
        }

        if (StringUtils.isNotBlank(bean.getUsername())) {
            popMailServer.setUsername(bean.getUsername());
        }

        popMailServer.setTimeout(bean.getTimeout());

        try {
            if (mailServerManager.getDefaultPopMailServer() != null) {
                mailServerManager.update(popMailServer);
            } else {
                popMailServer.setId(mailServerManager.create(popMailServer));
            }

            return Response.ok(bean).build();
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }

}
