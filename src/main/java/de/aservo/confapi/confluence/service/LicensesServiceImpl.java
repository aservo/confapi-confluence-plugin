package de.aservo.confapi.confluence.service;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.i18n.InvalidOperationException;
import com.atlassian.sal.api.license.LicenseHandler;
import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;
import de.aservo.confapi.commons.service.api.LicensesService;
import de.aservo.confapi.confluence.model.util.LicenseBeanUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Optional;

import static com.atlassian.confluence.setup.ConfluenceBootstrapConstants.DEFAULT_LICENSE_REGISTRY_KEY;

@Component
@ExportAsService(LicensesService.class)
public class LicensesServiceImpl implements LicensesService {

    private final LicenseHandler licenseHandler;

    @Inject
    public LicensesServiceImpl(@ComponentImport final LicenseHandler licenseHandler) {
        this.licenseHandler = licenseHandler;
    }

    @Override
    public LicensesBean getLicenses() {
        SingleProductLicenseDetailsView confluenceLicenseView = licenseHandler.getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);
        LicensesBean licensesBean = new LicensesBean();
        licensesBean.setLicenses(Collections.singletonList(LicenseBeanUtil.toLicenseBean(confluenceLicenseView)));
        return licensesBean;
    }

    @Override
    public LicenseBean getLicense(@NotNull String product) {
        Optional<LicenseBean> bean = getLicenses().getLicenses().stream()
                .filter(licenseBean -> licenseBean.getProducts().contains(product)).findFirst();
        if (!bean.isPresent()) {
            throw new NotFoundException(String.format("no license details found for product '%s'", product));
        } else {
            return bean.get();
        }
    }

    @Override
    public LicensesBean setLicenses(LicensesBean licensesBean) {
        //remove existing licenses (only if hostAllowsMultipleLicenses == true, otherwise InvalidOperationException throws)
        if (licenseHandler.hostAllowsMultipleLicenses()) {
            getLicenses().getLicenses().forEach(licenseBean -> licenseBean.getProducts().forEach(product -> {
                try {
                    licenseHandler.removeProductLicense(product);
                } catch (InvalidOperationException e) {
                    throw new BadRequestException(String.format("The license for product %s cannot be removed", product));
                }
            }));

            //set licenses
            licensesBean.getLicenses().forEach(this::addLicense);
        } else {
            //set first license from bean for non multi-license hosts
            addLicense(licensesBean.getLicenses().iterator().next());
        }

        return getLicenses();
    }

    @Override
    public LicenseBean setLicense(String product, @NotNull LicenseBean licenseBean) {
        //Confluence only allows to set the Confluence license, add method can be used for put because it replaces the current license
        return addLicense(licenseBean);
    }

    @Override
    public LicenseBean addLicense(LicenseBean licenseBean) {
        try {
            licenseHandler.addProductLicense(DEFAULT_LICENSE_REGISTRY_KEY, licenseBean.getKey());
        } catch (InvalidOperationException e) {
            throw new BadRequestException("The new license cannot be set");
        }
        return licenseBean;
    }

}
