package de.aservo.atlassian.confluence.confapi.suite;

import de.aservo.atlassian.confluence.confapi.filter.AdminOnlyResourceFilterTest;
import de.aservo.atlassian.confluence.confapi.model.*;
import de.aservo.atlassian.confluence.confapi.rest.*;
import de.aservo.atlassian.confluence.confapi.service.ApplicationLinksServiceTest;
import de.aservo.atlassian.confluence.confapi.service.ExternalGadgetServiceTest;
import de.aservo.atlassian.confluence.confapi.service.UserServiceTest;
import de.aservo.atlassian.confluence.confapi.util.MailProtocolUtilTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        SettingsBeanTest.class,
        PopMailServerBeanTest.class,
        SmtpMailServerBeanTest.class,
        ApplicationLinkBeanTest.class,
        LicenseBeanTest.class,
        UserBeanTest.class,
        UserDirectoryBeanTest.class,

        PingResourceTest.class,
        SettingsResourceTest.class,
        MailServerResourceTest.class,
        ApplicationLinksResourceTest.class,
        ExternalGadgetsResourceTest.class,
        GlobalPermissionsResourceTest.class,
        LicenseResourceTest.class,
        UserDirectoryResourceTest.class,
        UserResourceTest.class,

        ApplicationLinksServiceTest.class,
        ExternalGadgetServiceTest.class,
        UserServiceTest.class,

        AdminOnlyResourceFilterTest.class,

        MailProtocolUtilTest.class
})
public class TestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}

