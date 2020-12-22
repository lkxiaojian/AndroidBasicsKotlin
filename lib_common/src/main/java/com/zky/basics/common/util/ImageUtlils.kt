package com.zky.basics.common.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zky.basics.api.config.API

/**
 * Created by lk
 * Date 2019-11-06
 * Time 10:22
 * Detail:
 */
object ImageUtlils {
    /**
     * 加载图片url 显示加载中和加载失败的占位图
     *
     * @param imageView
     * @param imageUrl
     * @param placeDrawableId
     * @param errorDrawableId
     */
    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["imageUrl", "placeDrawableId", "errorDrawableId"], requireAll = false)
    fun loadimage(
        imageView: ImageView,
        imageUrl: String?,
        placeDrawableId: Int,
        errorDrawableId: Int
    ) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(placeDrawableId)
        requestOptions.error(errorDrawableId)
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(requestOptions)
            .into(imageView)
    }

    /**
     * 加载Bitmap 显示加载中和加载失败的占位图
     *
     * @param imageView
     * @param bitmap
     * @param placeDrawableId
     * @param errorDrawableId
     */
    @JvmStatic
    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["Bitmap", "placeDrawableId", "errorDrawableId"], requireAll = false)
    fun Bitmap(imageView: ImageView, bitmap: Bitmap?, placeDrawableId: Int, errorDrawableId: Int) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(placeDrawableId)
        requestOptions.error(errorDrawableId)
        Glide.with(imageView.context)
            .load(bitmap)
            .apply(requestOptions)
            .into(imageView)
    }

    /**
     * 如果是本地或者不是完整的url 就去加载oss 上的照片
     * @param imageView
     * @param url
     */
    @BindingAdapter("loadimage")
    fun loadimage(imageView: ImageView, _url: String?) {
        var url = _url
        if (url != null && !url.startsWith("/storage/emulated/0") && !url.startsWith("http")) {
            url = API.ImageFolderPath + url
        }
        val requestOptions = RequestOptions()
        Glide.with(imageView.context)
            .load(url)
            .apply(requestOptions)
            .into(imageView)
    }

    /**
     * 加载圆形的图片
     * @param imageView
     * @param circleImageUrl
     * @param placeDrawableId
     * @param errorDrawableId
     */
    @SuppressLint("CheckResult")
    @BindingAdapter(
        value = ["circleImageUrl", "placeDrawableId", "errorDrawableId"],
        requireAll = false
    )
    fun circleImageUrl(
        imageView: ImageView,
        circleImageUrl: String?,
        placeDrawableId: Int,
        errorDrawableId: Int
    ) {
        val requestOptions = RequestOptions()
        requestOptions.optionalCircleCrop()
        requestOptions.placeholder(placeDrawableId)
        requestOptions.error(errorDrawableId)
        Glide.with(imageView.context)
            .load(circleImageUrl)
            .apply(requestOptions)
            .into(imageView)
    }
}