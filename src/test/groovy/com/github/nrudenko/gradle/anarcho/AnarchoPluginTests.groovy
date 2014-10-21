package com.github.nrudenko.gradle.anarcho

import com.github.nrudenko.gradle.anarcho.fixtures.extensions.AppExtension
import com.github.nrudenko.gradle.anarcho.fixtures.extensions.ApplicationVariant
import org.junit.Before
import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project

import static org.junit.Assert.*

class AnarchoPluginTests {

    Project project

    @Before
    public void setUp() {
        // fake a project
        project = ProjectBuilder
                .builder()
                .build()
        def ext = project.extensions.create("android", AppExtension)
        ext.addApplicationVariant(new ApplicationVariant("Debug"))
        ext.addApplicationVariant(new ApplicationVariant("Release"))

        project.apply plugin: 'anarcho'
    }

    @Test
    public void canAddTaskForBuildVariants() {
        assertTrue(project.tasks.uploadDebug instanceof AnarchoUploadTask)
        assertTrue(project.tasks.uploadRelease instanceof AnarchoUploadTask)
    }


}
