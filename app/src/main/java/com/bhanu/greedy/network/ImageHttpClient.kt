package com.bhanu.greedy.network

import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL


/**
 * Created by Bhanu Prakash Pasupula on 02,Jun-2020.
 * Email: pasupula1995@gmail.com
 */
class ImageHttpClient:HttpClient {

    override fun run(url: String): HttpClient.HttpResponse? {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            return getInstance(connection.responseCode,connection)
        }catch (e:IOException){
            Log.e(TAG,"httpError: ${e.message}")
            //throw RuntimeException("HttpError: ${e.message}")
        }
        return null

    }

    companion object ImageHttpResponse:HttpClient.HttpResponse{

        fun getInstance(code:Int,conn:HttpURLConnection):ImageHttpResponse{
            statusCode=code
            connection = conn
            imageHttpResponse = this

            return imageHttpResponse
        }

        private const val TAG = "ImageHttpClient"
        private lateinit var imageHttpResponse:ImageHttpResponse
        private var statusCode:Int = 0
        private lateinit var connection:HttpURLConnection

        override fun getBody(): InputStream {
            return connection.inputStream
        }

        override fun getStatusCode(): Int {
           return connection.responseCode
        }

        override fun getErrorMessage(): String {
           return connection.responseMessage
        }

    }
}