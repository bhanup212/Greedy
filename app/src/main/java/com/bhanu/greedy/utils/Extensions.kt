package com.bhanu.greedy.utils


/**
 * Created by Bhanu Prakash Pasupula on 02,Jun-2020.
 * Email: pasupula1995@gmail.com
 */

fun String.format():String{
    val MAX_LENGTH = 64
    var url = if (this.length>MAX_LENGTH) this.substring(0,MAX_LENGTH) else this
    return url.toLowerCase().replace("[^a-z0-9_-]".toRegex(),"_")
}