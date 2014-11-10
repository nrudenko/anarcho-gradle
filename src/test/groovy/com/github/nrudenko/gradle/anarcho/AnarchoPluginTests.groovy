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

        project.apply plugin: AnarchoPlugin
    }

    @Test
    public void hasCorrectAnarchoExtFields() {
        def anarchoExt = project.extensions.findByName('anarcho')
        def allExtFields = ['__mapping__', '__dyn_obj__', '__host__', '__endPoint__', '__apiToken__', '__appKey__']
        def fields = anarchoExt.class.declaredFields
        assertTrue(allExtFields.size() == fields.size())
        fields.each { assertTrue("Field ${it.name} not found in ext", allExtFields.contains(it.name)) }
    }

    @Test
    public void hasCorrectAnarchoExtInitial() {
        def anarchoExt = project.extensions.findByName('anarcho')
        assertNotNull(anarchoExt)
        assertNull(anarchoExt.host)
        assertEquals('/api/apps/', anarchoExt.endPoint)
        assertNull(anarchoExt.apiToken)
        assertNull(anarchoExt.appKey)
    }

    @Test
    public void canAddTaskForBuildVariants() {
        assertTrue(project.tasks.uploadDebug instanceof AnarchoUploadTask)
        assertTrue(project.tasks.uploadRelease instanceof AnarchoUploadTask)
    }

    @Test
    public void shouldDependsOnAndroidTasks() {
        def uploadDebugDependsOn = project.tasks.uploadDebug.getDependsOn()
        // uploadDebugDependsOn = {java.util.HashSet@2260} size = 3
        // [0] = {java.lang.String@2272}"clean"
        // [1] = {org.codehaus.groovy.runtime.GStringImpl@2273}"assembleDebug"
        // [2] = {org.gradle.api.internal.file.UnionFileCollection@2279}"file collection"
        assertTrue("dependencies count ${uploadDebugDependsOn.size()} wrong", uploadDebugDependsOn.size() == 3)
        assertNotNull('dependsOn clean absence', uploadDebugDependsOn.find { it.toString().equals("clean") })
        assertNotNull('dependsOn assembleDebug absence', uploadDebugDependsOn.find {
            it.toString().equals("assembleDebug")
        })
    }

}
