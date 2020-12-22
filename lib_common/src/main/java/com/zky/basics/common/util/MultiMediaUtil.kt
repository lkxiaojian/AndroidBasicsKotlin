@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.zky.basics.common.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import me.nereo.multi_image_selector.MultiImageSelector
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description: <h3>多媒体工具类</h3>
 *
 *  * 1.图片选择器，可算多张图片
 *  * 2.拍照
 *  * 3.拍视频
 *  * 4.创建一个图片路径
 *  * 5.创建一个视频路径
 *
 * <h3>注意事项：</h3>
 *  * 1. 拍照、拍视频、选择图片完成的回调都在onActivityResult中回调的
 *  * 2.选择图片获取：List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)</String>
 *
 *
 *
 * Date:        2018/12/25<br></br>
 * Version:     V1.0.0<br></br>
 * Update:     <br></br>
 */
object MultiMediaUtil {
    const val SELECT_IMAGE = 1001
    const val TAKE_PHONE = 1002
    const val TAKE_VIDEO = 1003

    /**
     * 打开图片选择器，选择图片<br></br>
     * 来获取图片
     *
     * @param activity
     * @param count：选择图片个数
     * @param requestcode
     */
    fun pohotoSelect(activity: FragmentActivity?, count: Int, requestcode: Int) {
        pohotoSelect(activity, null, count, requestcode)
    }

    fun pohotoSelect(fragment: Fragment?, count: Int, requestcode: Int) {
        pohotoSelect(null, fragment, count, requestcode)
    }

    @SuppressLint("CheckResult")
    private fun pohotoSelect(
        activity: FragmentActivity?,
        fragment: Fragment?,
        count: Int,
        requestcode: Int
    ) {
        if (activity == null && fragment == null) {
            return
        }
        var activityTmp: Activity? = activity
        if (activityTmp == null) {
            activityTmp = fragment!!.activity
        }
        val finalActivity = activityTmp
        XXPermissions.with(activityTmp).permission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).request(object : OnPermission {
            override fun hasPermission(granted: List<String>, all: Boolean) {
                if (all) {
                    if (activity != null) {
                        MultiImageSelector.create().showCamera(false).count(count).single()
                            .multi() //.origin(ArrayList<String>)
                            .start(activity, requestcode)
                    } else if (fragment != null) {
                        MultiImageSelector.create().showCamera(false).count(count).single()
                            .multi() //.origin(ArrayList<String>)
                            .start(fragment, requestcode)
                    }
                }
            }

            override fun noPermission(denied: List<String>, never: Boolean) {
                if (never) {
                    ToastUtil.showToast("被永久拒绝授权，请手动授予存储和拍照权限")
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(finalActivity, denied)
                } else {
                    ToastUtil.showToast("获取存储权限失败")
                }
            }
        })


//        RxPermissions rxPermissions = null;
//        if (activity != null) {
//            rxPermissions = new RxPermissions(activity);
//        } else if (fragment != null) {
//            rxPermissions = new RxPermissions(fragment);
//        }
//
//        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) throws Exception {
//                if (aBoolean) {
//                    if (activity != null) {
//                        MultiImageSelector.create().showCamera(false).count(count).single().multi()
//                                //.origin(ArrayList<String>)
//                                .start(activity, requestcode);
//                    } else if (fragment != null) {
//                        MultiImageSelector.create().showCamera(false).count(count).single().multi()
//                                //.origin(ArrayList<String>)
//                                .start(fragment, requestcode);
//                    }
//                } else {
//                    ToastUtil.showToast("无读写外部存储设备权限");
//                }
//            }
//        });
    }

    /**
     * 拍照
     *
     * @param activity
     * @param path:照片存放的路径
     * @param requestcode
     */
    fun takePhoto(activity: FragmentActivity?, path: String, requestcode: Int) {
        takePhoto(activity, null, path, requestcode)
    }

    fun takePhoto(fragment: Fragment?, path: String, requestcode: Int) {
        takePhoto(null, fragment, path, requestcode)
    }

    @SuppressLint("CheckResult")
    private fun takePhoto(
        _activity: FragmentActivity?,
        fragment: Fragment?,
        path: String,
        requestcode: Int
    ) {
        var activity = _activity
        if (activity == null && fragment == null) {
            return
        }
        if (activity == null) {
            activity = fragment!!.activity
        }
        val finalActivity = activity
        XXPermissions.with(activity).permission(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).request(object : OnPermission {
            override fun hasPermission(granted: List<String>, all: Boolean) {
                if (all) {
                    val file = File(path)
                    try {
                        if (file.createNewFile()) {
                            val intent = Intent()
                            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
                            intent.addCategory(Intent.CATEGORY_DEFAULT)
                            if (finalActivity != null) {
                                var uri: Uri
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    uri = FileProvider.getUriForFile(
                                        fragment!!.context!!,
                                        "com.zky.jyb.fileprovider",
                                        file
                                    )
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                } else {
                                    uri = Uri.fromFile(file)
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                                finalActivity.startActivityForResult(intent, requestcode)
                            } else if (fragment != null) {
                                var uri: Uri
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    uri = FileProvider.getUriForFile(
                                        fragment.context!!,
                                        "com.zky.jyb.fileprovider",
                                        file
                                    )
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                } else {
                                    uri = Uri.fromFile(file)
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                                fragment.startActivityForResult(intent, requestcode)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ToastUtil.showToast("无法启动拍照程序")
                    }
                }
            }

            override fun noPermission(denied: List<String>, never: Boolean) {
//                if (never) {
//                    ToastUtil.showToast("被永久拒绝授权，请手动授予存储和拍照权限");
//                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                    XXPermissions.startPermissionActivity(finalActivity, denied);
//                } else {
//                    ToastUtil.showToast("获取存储权限失败");
//                }
                PermissionToSetting(finalActivity!!, denied, never, "获取存储权限失败")
            }
        })


//        RxPermissions rxPermissions = null;
//        if (activity != null) {
//            rxPermissions = new RxPermissions(activity);
//        } else if (fragment != null) {
//            rxPermissions = new RxPermissions(fragment);
//        }
//
//        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) throws Exception {
//                if (aBoolean) {
//                    File file = new File(path);
//                    try {
//                        if (file.createNewFile()) {
//                            Intent intent = new Intent();
//                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                            intent.addCategory(Intent.CATEGORY_DEFAULT);
//                            if (activity != null) {
//
//
//                                Uri uri = null;
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    uri = FileProvider.getUriForFile(fragment.getContext(), "com.zky.jyb.fileprovider", file);
//                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                } else {
//                                    uri = Uri.fromFile(file);
//                                }
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//
//                                activity.startActivityForResult(intent, requestcode);
//                            } else if (fragment != null) {
//                                Uri uri = null;
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    uri = FileProvider.getUriForFile(fragment.getContext(), "com.zky.jyb.fileprovider", file);
//                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                } else {
//                                    uri = Uri.fromFile(file);
//                                }
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                                fragment.startActivityForResult(intent, requestcode);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        ToastUtil.showToast("无法启动拍照程序");
//                    }
//                } else {
//                    ToastUtil.showToast("无摄像头权限,无法进行拍照!");
//                }
//            }
//        });
    }

    /**
     * 拍视频
     *
     * @param activity
     * @param path:视频存放的路径
     * @param requestcode
     */
    fun takeVideo(activity: FragmentActivity?, path: String, requestcode: Int) {
        takeVideo(activity, null, path, requestcode)
    }

    fun takeVideo(fragment: Fragment?, path: String, requestcode: Int) {
        takeVideo(null, fragment, path, requestcode)
    }

    @SuppressLint("CheckResult")
    private fun takeVideo(
        _activity: FragmentActivity?,
        fragment: Fragment?,
        path: String,
        requestcode: Int
    ) {
        var activity = _activity
        if (activity == null && fragment == null) {
            return
        }
        if (activity == null) {
            activity = fragment!!.activity
        }
        val finalActivity: Activity? = activity
        XXPermissions.with(finalActivity)
            .permission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request(object : OnPermission {
                override fun hasPermission(granted: List<String>, all: Boolean) {
                    if (all) {
                        val file = File(path)
                        try {
                            if (file.createNewFile()) {
                                val intent = Intent()
                                intent.action = MediaStore.ACTION_VIDEO_CAPTURE
                                intent.addCategory(Intent.CATEGORY_DEFAULT)
                                //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                if (finalActivity != null) {
                                    intent.putExtra(
                                        MediaStore.EXTRA_OUTPUT,
                                        FileProvider.getUriForFile(
                                            finalActivity,
                                            "com.zky.jyb.fileprovider",
                                            file
                                        )
                                    )
                                    finalActivity.startActivityForResult(intent, requestcode)
                                } else if (fragment != null) {
                                    intent.putExtra(
                                        MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                                            fragment.context!!, "com.zky.jyb.fileprovider", file
                                        )
                                    )
                                    fragment.startActivityForResult(intent, requestcode)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            ToastUtil.showToast("无法启动拍视频程序")
                        }
                    }
                }

                override fun noPermission(denied: List<String>, never: Boolean) {
                    if (never) {
                        ToastUtil.showToast("被永久拒绝授权，请手动授予存储和拍照权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(finalActivity, denied)
                    } else {
                        ToastUtil.showToast("无摄像头权限,无法进行拍视频!")
                    }
                }
            })


//        RxPermissions rxPermissions = null;
//        if (activity != null) {
//            rxPermissions = new RxPermissions(activity);
//        } else if (fragment != null) {
//            rxPermissions = new RxPermissions(fragment);
//        }
//        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) throws Exception {
//                if (aBoolean) {
//                    File file = new File(path);
//                    try {
//                        if (file.createNewFile()) {
//                            Intent intent = new Intent();
//                            intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
//                            intent.addCategory(Intent.CATEGORY_DEFAULT);
//                            //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                            if (activity != null) {
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity, "com.zky.jyb.fileprovider", file));
//                                activity.startActivityForResult(intent, requestcode);
//                            } else if (fragment != null) {
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(fragment.getContext(), "com.zky.jyb.fileprovider", file));
//                                fragment.startActivityForResult(intent, requestcode);
//                            }
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        ToastUtil.showToast("无法启动拍视频程序");
//                    }
//                } else {
//                    ToastUtil.showToast("无摄像头权限,无法进行拍视频!");
//                }
//            }
//        });
    }

    //获取图片路径
    @SuppressLint("SimpleDateFormat")
    fun getPhotoPath(): String {
        val filename = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".jpg"
        //        String filepath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + filename;
        val s = Environment.getExternalStorageDirectory().toString() + File.separator + "jyb/image/"
        val file = File(s)
        if (!file.exists()) {
            file.mkdirs()
        }
        return s + filename
    }

    //获取视频的路径
    @SuppressLint("SimpleDateFormat")
    fun getVideoPath(activity: Activity): String {
        val filename = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".3gp"
        return activity.getExternalFilesDir(Environment.DIRECTORY_MOVIES).absolutePath + File.separator + filename
    }
}