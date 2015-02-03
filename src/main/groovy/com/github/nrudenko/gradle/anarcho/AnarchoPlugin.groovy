package com.github.nrudenko.gradle.anarcho

import org.gradle.api.Plugin
import org.gradle.api.Project;

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

        project.configure(project) {
            extensions.create("anarcho", AnarchoExtension)
        }
    }

}

class AnarchoExtension {
    String uploadUrl
    String apiToken
    String releaseNotesFile
}
