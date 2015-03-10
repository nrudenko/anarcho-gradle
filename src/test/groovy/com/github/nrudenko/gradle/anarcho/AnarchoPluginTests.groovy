package com.github.nrudenko.gradle.anarcho

import com.github.nrudenko.gradle.anarcho.fixtures.extensions.AppExtension
import com.github.nrudenko.gradle.anarcho.fixtures.extensions.ApplicationVariant
import org.gradle.api.internal.FactoryNamedDomainObjectContainer
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
        project.apply plugin: AnarchoPlugin
    }

    @Test
    public void hasCorrectAnarchoExtFields() {
        AnarchoExtension anarchoExt = project.extensions.findByName("anarcho")
        def expectedProperties = ["class", "buildTypes", "host"]
        def actualProperties = anarchoExt.properties
        assertEquals('Wrong ext actualProperties count', expectedProperties.size(), actualProperties.size())
        actualProperties.collect {
            key, value ->
                assertTrue("Field ${key} not found in ext", expectedProperties.contains(key))
        }
    }

    @Test
    public void hasCorrectBuildTypes() {
        AnarchoExtension anarcho = project.extensions.findByName('anarcho')
        anarcho.with {
            host = "asd"
            buildTypes {
                defaultConfig {
                    appKey = "defaultAppKey"
                    apiToken = "defaultApiToken"
                }
                release {
                    appKey = "d3e846b2-c431-11e4-baef-62dd4457b65e"
                    apiToken = "66c9dd080128d16cc3fa3fdd04da0d71"
                }
            }
        }

        AnarchoExtension anarchoExt = project.extensions.findByName("anarcho")
        FactoryNamedDomainObjectContainer<AppConfig> buildTypes = anarchoExt.buildTypes
        assertEquals('Wrong buildTypesCount count', 2, buildTypes.size())
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
