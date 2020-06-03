package com.bhanu.greedy.cache

import android.graphics.Bitmap
import android.util.LruCache


/**
 * Created by Bhanu Prakash Pasupula on 02,Jun-2020.
 * Email: pasupula1995@gmail.com
 */
class MemoryLruCache:LruCache<String,Bitmap>(getLimitSize()) {

    override fun sizeOf(key: String?, value: Bitmap): Int {
        return value.byteCount/1024
    }

    companion object{
        private fun getMemorySize():Long{
            return Runtime.getRuntime().maxMemory()/1024
        }
        fun getLimitSize() = (getMemorySize()/8).toInt()
    }

}