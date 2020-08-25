package com.zky.basics.common.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.widget.AppCompatImageView
import android.util.AttributeSet


class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    AppCompatImageView(context, attrs, defStyle) {

    override fun onDraw(canvas: Canvas) {

        val drawable = drawable ?: return

        if (width == 0 || height == 0) {
            return
        }

        if (drawable !is BitmapDrawable) {
            return
        }

        val b = drawable.bitmap ?: return

        val bitmap = b.copy(Bitmap.Config.ARGB_8888, true)

        val w = width
//        val h = height
        val roundBitmap = getCroppedBitmap(bitmap, w)
        canvas.drawBitmap(roundBitmap, 0f, 0f, null)

    }

    fun getCroppedBitmap(bmp: Bitmap, radius: Int): Bitmap {
        val sbmp: Bitmap
        sbmp = if (bmp.width != radius || bmp.height != radius)
            Bitmap.createScaledBitmap(bmp, radius, radius, false)
        else
            bmp
        val output = Bitmap.createBitmap(sbmp.width, sbmp.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, sbmp.width, sbmp.height)

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = Color.parseColor("#BAB399")
        canvas.drawCircle(sbmp.width / 2 + 0.7f, sbmp.height / 2 + 0.7f, sbmp.width / 2 + 0.1f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(sbmp, rect, rect, paint)

        return output
    }

}