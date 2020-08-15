package com.zky.basics.api.splash.entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

/**
 * Created by lk
 * Date 2019-11-06
 * Time 17:47
 * Detail:
 */
data class ImageUrl(var token: String?, var image: String?) {


    fun getBitmap(): Bitmap? {
        if (image.isNullOrEmpty()) {
            return null
        }
        val s = image.toString().replace("data:image/png;base64,".toRegex(), "")
        val bitmapByte =
            Base64.decode(s, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.size)
    }


}