package com.github.nrudenko.gradle.anarcho

import com.github.nrudenko.gradle.anarcho.fixtures.extensions.AppExtension
import com.github.nrudenko.gradle.anarcho.fixtures.extensions.ApplicationVariant
import org.junit.Before
import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project

import java.util.regex.Matcher
import java.util.regex.Pattern

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

        project.apply plugin: AnarchoPlugin
    }

    @Test
    public void hasCorrectAnarchoExtFields() {
        def anarchoExt = project.extensions.findByName('anarcho')
        def allExtFields = ['__mapping__', '__dyn_obj__', '__uploadUrl__', '__apiToken__', '__releaseNotesFile__']
        def fields = anarchoExt.class.declaredFields
        assertEquals('Wrong ext fields count', allExtFields.size(), fields.size())
        fields.each { assertTrue("Field ${it.name} not found in ext", allExtFields.contains(it.name)) }
    }

    @Test
    public void canAddTaskForBuildVariants() {
        assertTrue(project.tasks.uploadDebug instanceof AnarchoUploadTask)
        assertTrue(project.tasks.uploadRelease instanceof AnarchoUploadTask)
    }

    @Test
    public void shouldDependsOnAndroidTasks() {
        def uploadDebugDependsOn = project.tasks.uploadDebug.getDependsOn()
        assertNotNull('dependsOn assembleDebug absence', uploadDebugDependsOn.find {
            it.toString().equals("assembleDebug")
        })
    }

}
