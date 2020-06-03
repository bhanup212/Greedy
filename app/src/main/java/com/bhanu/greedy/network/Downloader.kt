package com.bhanu.greedy.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.bhanu.greedy.model.ImageRequest
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * Created by Bhanu Prakash Pasupula on 02,Jun-2020.
 * Email: pasupula1995@gmail.com
 */
class Downloader(private val httpClient: HttpClient) {

    companion object{

        private const val THREAD_POOL_COUNT = 3
        interface DownloadListener{
            fun onImageLoad(url:String,bitmap: Bitmap)
            fun onCancelled()
            fun onError(message:String?)
        }
    }

    private val threadPoolExecutor: ThreadPoolExecutor

    init {
        threadPoolExecutor = ThreadPoolExecutor(
            THREAD_POOL_COUNT,
            THREAD_POOL_COUNT,
            35,
            TimeUnit.SECONDS,
            LinkedBlockingDeque<Runnable>()
        )
    }
    fun downloadImage(imageRequest: ImageRequest, listener: DownloadListener){
        threadPoolExecutor.execute {
            if (!imageRequest.isCancel) {
                downloadImageFromHttpClient(imageRequest, listener)
            }else{
                listener.onCancelled()
            }
        }
    }
    private fun downloadImageFromHttpClient(imageRequest: ImageRequest, listener: DownloadListener){
        val response = httpClient.run(imageRequest.url)
        when {
            imageRequest.isCancel -> {
                listener.onCancelled()
            }
            response?.getStatusCode() == 200 -> {
                val bitmap = BitmapFactory.decodeStream(response.getBody())
                listener.onImageLoad(imageRequest.url, bitmap)
            }
            else -> {
                listener.onError(response?.getErrorMessage())
            }
        }
    }
}