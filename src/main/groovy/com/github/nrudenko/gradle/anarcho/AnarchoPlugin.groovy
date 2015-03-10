package com.github.nrudenko.gradle.anarcho

import groovy.transform.ToString
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.FactoryNamedDomainObjectContainer


class AnarchoPlugin implements Plugin<Project> {

    @Override
    def void apply(Project project) {
        project.android.applicationVariants.all { v ->
            def variantName = v.name.capitalize()
            def taskName = "upload${variantName}"
            project.task(taskName, type: AnarchoUploadTask,
                    dependsOn: "assemble${variantName}") {
                description "Assemble and upload ${variantName} apk to Anarcho"
                group "Deploy"
                variant = v
            }
        }

        project.extensions.anarcho = new AnarchoExtension()
        project.extensions.anarcho.buildTypes = project.container(AppConfig)
    }

}

@ToString
class AnarchoExtension {
    String host
    FactoryNamedDomainObjectContainer<AppConfig> buildTypes

    def buildTypes(Closure types) {
        buildTypes.configure(types)
    }
}

@ToString
class AppConfig {
    final String name

    String appKey
    String apiToken
    String releaseNotes

    AppConfig(String name) {
        this.name = name
    }

    def update(AppConfig config) {
        this.appKey = config.appKey ? config.appKey : this.appKey
        this.apiToken = config.apiToken ? config.apiToken : this.apiToken
        this.releaseNotes = config.releaseNotes ? config.releaseNotes : this.releaseNotes
    }
}