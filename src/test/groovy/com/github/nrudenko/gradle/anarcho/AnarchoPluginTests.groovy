package com.github.nrudenko.gradle.anarcho

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project

import static org.junit.Assert.*

class AnarchoPluginTests {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('upload', type: AnarchoUploadTask)
        assertTrue(task instanceof AnarchoUploadTask)
    }

    @Test
    public void canPluginAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'anarcho'
        assertTrue(project.tasks.upload instanceof AnarchoUploadTask)
    }

    @Test
    public void canGetExtConfig() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'anarcho'
        println project.anarcho.host
    }

    @Test
    public void canHandleAllApplicationVariants() {
        Project project = ProjectBuilder.builder().build()
        project.
        project.apply plugin: 'anarcho'
        println project.anarcho.host
    }
}
