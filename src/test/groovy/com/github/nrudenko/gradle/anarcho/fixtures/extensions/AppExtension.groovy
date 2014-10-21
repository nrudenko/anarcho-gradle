package com.github.nrudenko.gradle.anarcho.fixtures.extensions

import org.gradle.api.internal.DefaultDomainObjectSet

public class AppExtension {
    private final DefaultDomainObjectSet<ApplicationVariant> applicationVariantList =
            new DefaultDomainObjectSet<ApplicationVariant>(ApplicationVariant.class)

    public DefaultDomainObjectSet<ApplicationVariant> getApplicationVariants() {
        return applicationVariantList
    }

    void addApplicationVariant(ApplicationVariant applicationVariant) {
        applicationVariantList.add(applicationVariant)
    }
}
