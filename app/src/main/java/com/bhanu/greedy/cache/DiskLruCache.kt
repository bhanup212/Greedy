package com.bhanu.greedy.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bhanu.greedy.utils.format
import com.jakewharton.disklrucache.DiskLruCache
import java.io.*
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


/**
 * Created by Bhanu Prakash Pasupula on 02,Jun-2020.
 * Email: pasupula1995@gmail.com
 */
class DiskLruCache(cacheDir: File){
    companion object{
        private const val DISK_CACHE_SIZE:Long = 1024 * 1024 * 10 // 10MB
        const val DISK_CACHE_SUB_DIR = "thumbnails"
    }

    private val diskCacheLock = ReentrantLock()
    private val diskCacheLockCondition: Condition = diskCacheLock.newCondition()
    private var diskCacheStarting = true
    private lateinit var diskLruCache:DiskLruCache

    init {
        diskCacheLock.withLock {
            diskLruCache = DiskLruCache.open(cacheDir, 1,1,DISK_CACHE_SIZE)
            diskCacheStarting = false // Finished initialization
            diskCacheLockCondition.signalAll() // Wake any waiting threads
        }

    }

    fun getBitmap(url: String): Bitmap? {

        val key = url.format()
        val snapshot: DiskLruCache.Snapshot? = diskLruCache.get(key)
        return if (snapshot != null) {
            val inputStream: InputStream = snapshot.getInputStream(0)
            val buffIn = BufferedInputStream(inputStream, 8 * 1024)
            BitmapFactory.decodeStream(buffIn)
        } else {
            null
        }

    }

    fun put(url: String, bitmap: Bitmap){
        val key = url.format()
        var editor:DiskLruCache.Editor?=null

        try {
            editor =diskLruCache.edit(key)
            writeBitmapToFile(bitmap,editor)
            diskLruCache?.flush()
            editor.commit()
        } catch (e: Exception) {
            editor?.abort()
        }

    }
    private fun writeBitmapToFile(bitmap: Bitmap,editor: DiskLruCache.Editor){
        var out: OutputStream? = null
        try {
            out = BufferedOutputStream(editor.newOutputStream(0), 8 * 1024)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        } finally {
            out?.close()
        }
    }

}