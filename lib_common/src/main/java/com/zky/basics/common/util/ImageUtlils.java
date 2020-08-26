package com.zky.basics.common.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zky.basics.api.config.API;

/**
 * Created by lk
 * Date 2019-11-06
 * Time 10:22
 * Detail:
 */
public class ImageUtlils {
    /**
     * 加载图片url 显示加载中和加载失败的占位图
     *
     * @param imageView
     * @param imageUrl
     * @param placeDrawableId
     * @param errorDrawableId
     */
    @SuppressLint("CheckResult")
    @BindingAdapter(value = {"imageUrl", "placeDrawableId", "errorDrawableId"}, requireAll = false)
    public static void loadimage(ImageView imageView, String imageUrl, int placeDrawableId, int errorDrawableId) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(placeDrawableId);
        requestOptions.error(errorDrawableId);
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载Bitmap 显示加载中和加载失败的占位图
     *
     * @param imageView
     * @param bitmap
     * @param placeDrawableId
     * @param errorDrawableId
     */
    @SuppressLint("CheckResult")
    @BindingAdapter(value = {"Bitmap", "placeDrawableId", "errorDrawableId"}, requireAll = false)
    public static void Bitmap(ImageView imageView, Bitmap bitmap, int placeDrawableId, int errorDrawableId) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(placeDrawableId);
        requestOptions.error(errorDrawableId);
        Glide.with(imageView.getContext())
                .load(bitmap)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 如果是本地或者不是完整的url 就去加载oss 上的照片
     * @param imageView
     * @param url
     */
    @BindingAdapter({"loadimage"})
    public static void loadimage(ImageView imageView, String url) {
        if (url != null && !url.startsWith("/storage/emulated/0") && !url.startsWith("http")) {
            url = API.ImageFolderPath + url;
        }
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(imageView.getContext())
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载圆形的图片
     * @param imageView
     * @param circleImageUrl
     * @param placeDrawableId
     * @param errorDrawableId
     */
    @SuppressLint("CheckResult")
    @BindingAdapter(value = {"circleImageUrl", "placeDrawableId", "errorDrawableId"}, requireAll = false)
    public static void circleImageUrl(ImageView imageView, String circleImageUrl, int placeDrawableId, int errorDrawableId) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.optionalCircleCrop();
        requestOptions.placeholder(placeDrawableId);
        requestOptions.error(errorDrawableId);
        Glide.with(imageView.getContext())
                .load(circleImageUrl)
                .apply(requestOptions)
                .into(imageView);
    }

}
