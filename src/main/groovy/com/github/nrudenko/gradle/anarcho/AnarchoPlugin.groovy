package com.github.nrudenko.gradle.anarcho

import org.gradle.api.Plugin
import org.gradle.api.Project;

class AnarchoPlugin implements Plugin<Project> {

    @Override
    def void apply(Project project) {
        project.android.applicationVariants.all { v ->
            def variantName = v.name.capitalize()

            def task = project.task("upload${variantName}", type: AnarchoUploadTask) {
                description "Clean, assemble and upload ${variantName} apk to Anarcho"
                group "Deploy"
                variant = v
            }
            task.dependsOn("assemble${variantName}")
        }

        project.configure(project) {
            extensions.create("anarcho", AnarchoExtension)
        }
    }

}

class AnarchoExtension {
    String host = 'http://54.164.28.98/'
    String endpoint = 'api/add_build'
    String apiToken
}
