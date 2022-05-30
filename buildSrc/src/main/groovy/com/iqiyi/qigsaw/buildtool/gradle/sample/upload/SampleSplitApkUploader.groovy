package com.iqiyi.qigsaw.buildtool.gradle.sample.upload

import com.iqiyi.qigsaw.buildtool.gradle.upload.SplitApkUploadException
import com.iqiyi.qigsaw.buildtool.gradle.upload.SplitApkUploader
import org.gradle.api.Project
import groovy.json.JsonSlurper

class SampleSplitApkUploader implements SplitApkUploader {

    @Override
    String uploadSync(Project appProject, File splitApk, String splitName) throws SplitApkUploadException {
        List<String> testOnly = appProject.extensions.splitUpload.testOnly
        boolean useTestEnv = appProject.extensions.splitUpload.useTestEnv
        if (useTestEnv) {
            return uploadSplitApk(splitApk, splitName, true)
        } else {
            return uploadSplitApk(splitApk, splitName, usingTestEnvAnyWay(testOnly, splitName))
        }
    }


    static boolean usingTestEnvAnyWay(List<String> testOnly, String splitName) {
        return testOnly != null && testOnly.contains(splitName)
    }

    /**
     * Implement this method to upload split apks to your own server.
     */
    static String uploadSplitApk(File splitApk, String splitName, boolean useTestEnv) {
        println("Upload split " + splitName + " split apk file path: " + splitApk + " useTestEnv: " + useTestEnv)
        String url = ""

        HttpURLConnectionUtil connectionUtil = new HttpURLConnectionUtil(url, HttpURLConnection.HTTPMETHOD_POST);
        connectionUtil.addTextParameter("remotePath","test/apk")
        connectionUtil.addFileParameter("file",splitApk)
        String result = new String(connectionUtil.post(), "UTF-8")
        def data = new JsonSlurper().parseText(result)
        println("result:data:"+data)
        return null
    }
}
