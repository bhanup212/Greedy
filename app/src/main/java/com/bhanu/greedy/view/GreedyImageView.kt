package com.bhanu.greedy.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bhanu.greedy.ImageLoader
import java.lang.ref.WeakReference


/**
 * Created by Bhanu Prakash Pasupula on 02,Jun-2020.
 * Email: pasupula1995@gmail.com
 */
class GreedyImageView(private val url:String,view: ImageView): View.OnAttachStateChangeListener {

    private val imageView = WeakReference<ImageView>(view)
    init {
        imageView.get()?.addOnAttachStateChangeListener(this)
    }

    fun setImageBitmap(bitmap: Bitmap){
        Log.d(TAG,"setting bitmap: isViewAttached: ${imageView.get()?.isAttachedToWindow}")
        imageView.get()?.run {
            post {
                setImageBitmap(bitmap)
            }
        }
    }

    override fun onViewDetachedFromWindow(v: View?) {
        Log.d(TAG,"onViewDetachedFromWindow")
        ImageLoader.cancel(url)
    }

    override fun onViewAttachedToWindow(v: View?) {
       Log.d(TAG,"onViewAttachedToWindow")
    }

    companion object{
        private val TAG = "GreedyImageView"
    }
}