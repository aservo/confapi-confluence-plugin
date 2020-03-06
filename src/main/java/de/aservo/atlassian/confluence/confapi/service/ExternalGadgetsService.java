package de.aservo.atlassian.confluence.confapi.service;

import com.atlassian.confluence.languages.LocaleManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.gadgets.GadgetRequestContext;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpec;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpecStore;
import com.atlassian.gadgets.spec.GadgetSpecFactory;
import com.atlassian.plugin.spring.scanner.annotation.component.ConfluenceComponent;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type External gadgets service. This work is insipred by class AddOrRemoveGadgetSpecAction of plugin file gadgets-plugin-9.0.4.jar
 * <p>
 * Note: if ever needed: com.atlassian.gadgets.GadgetSpecProvider provides internal gadget specs
 */
@ExportAsService
@ConfluenceComponent
public class ExternalGadgetsService {

    private static final Logger log = LoggerFactory.getLogger(ExternalGadgetsService.class);

    private final ExternalGadgetSpecStore externalGadgetSpecStore;
    private final GadgetSpecFactory gadgetSpecFactory;
    private final LocaleManager localeManager;

    /**
     * Instantiates a new External gadgets service.
     *
     * @param externalGadgetSpecStore the external gadget spec store
     * @param gadgetSpecFactory       the gadget spec factory
     * @param localeManager           the locale manager
     */
    public ExternalGadgetsService(@ComponentImport ExternalGadgetSpecStore externalGadgetSpecStore,
                                  @ComponentImport GadgetSpecFactory gadgetSpecFactory,
                                  @ComponentImport LocaleManager localeManager) {
        this.externalGadgetSpecStore = externalGadgetSpecStore;
        this.gadgetSpecFactory = gadgetSpecFactory;
        this.localeManager = localeManager;
    }

    /**
     * Gets registered external gadget urls.
     *
     * @return the registered external gadget urls
     */
    public List<String> getRegisteredExternalGadgetURls() {
        Iterable<ExternalGadgetSpec> specIterable = externalGadgetSpecStore.entries();
        return StreamSupport.stream(specIterable.spliterator(), false)
                .map(spec -> spec.getSpecUri().toString()).collect(Collectors.toList());
    }

    /**
     * Add external gadget url.
     *
     * @param url           the url
     * @param testGadgetUrl whether to test gadget url
     * @throws URISyntaxException the uri syntax exception
     */
    public void addExternalGadgetUrl(String url, Boolean testGadgetUrl) throws URISyntaxException {

        //initial checks
        if (url == null || StringUtils.isBlank(url.trim())) {
            throw new NullArgumentException("'url' must not be null or empty!");
        }
        String gadgetUrlToAdd = url.trim();
        URI uri = new URI(gadgetUrlToAdd);

        //validate gadget url
        if (testGadgetUrl == null || testGadgetUrl) {
            ConfluenceUser user = AuthenticatedUserThreadLocal.get();
            Locale locale = localeManager.getLocale(user);
            GadgetRequestContext requestContext = GadgetRequestContext.Builder.gadgetRequestContext().locale(locale)
                    .ignoreCache(false).user(new GadgetRequestContext.User(user.getKey().getStringValue(), user.getName())).build();
            gadgetSpecFactory.getGadgetSpec(uri, requestContext);
        }

        //add gadget url to store
        externalGadgetSpecStore.add(uri);
    }
}
