package com.github.nrudenko.gradle.anarcho

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.InputStreamBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.message.BasicHeader
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
        def conf = project.anarcho
        println("${conf.uploadUrl} ${conf.apiToken} ${variant.outputs.outputFile}")
        variant.outputs.outputFile.each {
            uploadBuild(conf.uploadUrl, it, "empty", conf.apiToken)
        }
    }

    void uploadBuild(String url, File apkFile, String releaseNotes, String apiToken) {
        def http = new HTTPBuilder(url)

        http.request(Method.POST) { req ->
            headers.'x-auth-token' = apiToken
            headers.'User-Agent' = "Anarcho-gradle plugin"
            headers.'Accept' = 'application/json'

            MultipartEntityBuilder builder = new MultipartEntityBuilder()
                    .addBinaryBody("file", apkFile, ContentType.APPLICATION_OCTET_STREAM, apkFile.name)
                    .addTextBody("releaseNotes", releaseNotes)
            req.setEntity(builder.build())
            response.success = { resp, reader ->
                if (resp.statusLine.statusCode == 200) {
                    println reader
                }
            }

            response.failure = { resp, reader ->
                println "request failed ${reader}"
                assert resp.status >= 400
            }
        }
    }

}
