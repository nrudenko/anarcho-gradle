package com.github.nrudenko.gradle.anarcho

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.entity.mime.MultipartEntityBuilder
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
        println("${conf.host} ${conf.apiToken} ${variant.outputFile}")
    }

    void uploadBuild(File apkFile, String releaseNotes, String apiToken) {

        def http = new HTTPBuilder(host + endpoint)

        http.request(Method.POST) { req ->

            MultipartEntityBuilder builder = new MultipartEntityBuilder()
                    .addBinaryBody("file", apkFile)
                    .addTextBody("releaseNotes", releaseNotes)
            req.setEntity(builder.build())

            response.success = { resp ->

                if (resp.statusLine.statusCode == 200) {
                    print resp
                    // response handling
                }
            }
        }
    }

}
