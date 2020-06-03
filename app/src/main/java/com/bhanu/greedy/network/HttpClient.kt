package com.bhanu.greedy.network

import java.io.InputStream


/**
 * Created by Bhanu Prakash Pasupula on 02,Jun-2020.
 * Email: pasupula1995@gmail.com
 */
interface HttpClient {

    fun run(url:String):HttpResponse?

    interface HttpResponse{

        fun getBody():InputStream

        fun getStatusCode():Int

        fun getErrorMessage():String
    }
}