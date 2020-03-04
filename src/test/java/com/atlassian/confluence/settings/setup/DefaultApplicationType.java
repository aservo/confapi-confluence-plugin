package com.atlassian.confluence.settings.setup;

import com.atlassian.applinks.api.ApplicationType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;

public class DefaultApplicationType implements ApplicationType {
    @Nonnull
    @Override
    public String getI18nKey() {
        return null;
    }

    @Nullable
    @Override
    public URI getIconUrl() {
        return null;
    }
}
