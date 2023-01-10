package com.example.listexternalfiles
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.InputStreamReader
import java.net.URL



class InternetClass (endpoint: String, tosend : File?= null) {


    // create a prviuate tasl and execute the webreques



    val endpoints= endpoint

    fun main(endpoints: String) {
        val url = URL(endpoints)
        val postData = "-----------------------------1\n" +
                "Content-Disposition: form-data; name=\"files\"; filename=\"killabsoi (6sssgggh)xgx.zip\n" + "foo1=bar1&foo2=bar2"

        val conn = url.openConnection()
        conn.doOutput = true
        conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=---------------------------1")
        conn.setRequestProperty("Content-Length", postData.length.toString())
        conn.setRequestProperty("Content-Disposition", "form-data; name=\"files\"; filename=\"webuilourownworld.zip\"")


        DataOutputStream(conn.getOutputStream()).use { it.writeBytes(postData) }
        BufferedReader(InputStreamReader(conn.getInputStream())).use { bf ->
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                println(line)
            }
        }
    }


}