package com.bhanu.greedy

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.os.Environment.isExternalStorageRemovable
import android.util.ArrayMap
import android.util.Log
import android.widget.ImageView
import com.bhanu.greedy.cache.DiskLruCache
import com.bhanu.greedy.cache.MemoryLruCache
import com.bhanu.greedy.model.ImageRequest
import com.bhanu.greedy.network.Downloader
import com.bhanu.greedy.network.ImageHttpClient
import com.bhanu.greedy.view.GreedyImageView
import java.io.File
import java.lang.ref.WeakReference


/**
 * Created by Bhanu Prakash Pasupula on 02,Jun-2020.
 * Email: pasupula1995@gmail.com
 */
class ImageLoader private constructor():Downloader.Companion.DownloadListener {



    private val mCache:MemoryLruCache = MemoryLruCache()

    private lateinit var diskCache:DiskLruCache

    private val client:ImageHttpClient = ImageHttpClient()

    private val download:Downloader = Downloader(client)

    private val urlRequestMap = ArrayMap<String,WeakReference<GreedyImageView>>()

    private val imageRequestMap = ArrayMap<String,ImageRequest>()

    private fun load(url: String,imageView: GreedyImageView){

        when{
            getBitMapFromMemory(url) != null ->{
                imageView.setImageBitmap(getBitMapFromMemory(url))
            }
            diskCache.getBitmap(url) != null ->{
                imageView.setImageBitmap(diskCache.getBitmap(url)!!)
            }
            else -> {
                val imageRequest = ImageRequest(url)
                urlRequestMap[url] = WeakReference<GreedyImageView>(imageView)
                imageRequestMap[url] = imageRequest
                download.downloadImage(imageRequest, imageLoader)
            }
        }
    }

    private fun getBitMapFromMemory(url: String) = mCache[url]

    private fun handleDownloadedImage(url: String,bitmap: Bitmap){

        mCache.put(url,bitmap)
        diskCache.put(url,bitmap)
        loadImage(url,bitmap)
    }
    private fun loadImage(url: String,bitmap: Bitmap){
        urlRequestMap[url]?.get()?.run {
            setImageBitmap(bitmap)
        }
    }
    private fun cancelImageLoad(url: String){
        Log.d(TAG,"Image cancel (view detached to window)")
        imageRequestMap[url]?.run {
            isCancel = true
        }
    }

    override fun onImageLoad(url:String,bitmap: Bitmap) {
        Log.d(TAG,"onImageLoaded")
        handleDownloadedImage(url,bitmap)
    }

    override fun onCancelled() {
        Log.e(TAG,"ImageCancelled")
    }

    override fun onError(message: String?) {
        Log.e(TAG,"image loading failed: $message")
    }

    private fun checkCacheDir(context: Context){
        if (!::diskCache.isInitialized){
            diskCache = DiskLruCache(getDiskCacheDir(context))
        }
    }

    private fun getDiskCacheDir(context: Context): File {

        val cachePath =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                || !isExternalStorageRemovable()) {
                context.externalCacheDir?.path
            } else {
                context.cacheDir.path
            }

        return File(cachePath + File.separator + DiskLruCache.DISK_CACHE_SUB_DIR)
    }

    companion object{

        private const val TAG = "ImageLoader"

        private val imageLoader:ImageLoader by lazy {
            ImageLoader()
        }

        fun load(url: String,imageView: ImageView){
            imageLoader.checkCacheDir(imageView.context.applicationContext)
            imageLoader.load(url, GreedyImageView(url,imageView))
        }
        internal fun cancel(url: String){
            imageLoader.cancelImageLoad(url)
        }
    }

}