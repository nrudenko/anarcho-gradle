package com.github.nrudenko.gradle.anarcho

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class AnarchoUploadTask extends DefaultTask {

    @Input
    def variant

    def AnarchoUploadTask() {
    }

    @TaskAction
    def doUpload() {
        AnarchoExtension anarchoExt = project.anarcho
        AppConfig config = anarchoExt.buildTypes.defaultConfig
        if (anarchoExt.buildTypes.hasProperty(variant.name)) {
            def overrideConfig = anarchoExt.buildTypes.getByName(variant.name)
            config.update(overrideConfig)
        }
        println("\n\nUploading: ${variant.outputs.outputFile}")
        String uploadUrl = "${anarchoExt.host}/api/apps/${config.appKey}".replaceAll("//api", "/api")
        variant.outputs.outputFile.each { apkFile ->
            uploadBuild(uploadUrl, apkFile, config.releaseNotes, config.apiToken)
        }
    }

    void uploadBuild(String url, File apkFile, String releaseNotes, String apiToken) {
        def http = new HTTPBuilder(url)

        http.request(Method.POST) { req ->
            headers.'x-auth-token' = apiToken
            headers.'User-Agent' = "Anarcho-gradle plugin"
            headers.'Accept' = 'application/json'

            MultipartEntityBuilder builder = new MultipartEntityBuilder()
                    .addPart("file", new FileBody(apkFile))
                    .addPart('releaseNotes', new StringBody(releaseNotes))
            req.setEntity(builder.build())
            response.success = { resp, build ->
                if (resp.statusLine.statusCode == 200) {
                    println build
                    String publicUrl = url.replaceAll("api", "#") + "/" + build.id
                    println("Go to:\n\t\t ${publicUrl}")
                }
            }

            response.failure = { resp, reader ->
                println "Upload failed: ${reader}"
                assert resp.status >= 400
            }
        }
    }

}
