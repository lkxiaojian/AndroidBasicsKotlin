package com.zky.basics.common.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

import static com.zky.basics.common.util.CommonUtilsKt.PermissionToSetting;

/**
 * Description: <h3>多媒体工具类</h3>
 * <ul>
 * <li>1.图片选择器，可算多张图片</li>
 * <li>2.拍照</li>
 * <li>3.拍视频</li>
 * <li>4.创建一个图片路径</li>
 * <li>5.创建一个视频路径</li>
 * </ul>
 * <h3>注意事项：</h3>
 * <ul><li>1. 拍照、拍视频、选择图片完成的回调都在onActivityResult中回调的</l1>
 * <li>2.选择图片获取：List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)</li>
 * </ul>
 * <p>
 * Date:        2018/12/25<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class MultiMediaUtil {
    public static final int SELECT_IMAGE = 1001;
    public static final int TAKE_PHONE = 1002;
    public static final int TAKE_VIDEO = 1003;

    /**
     * 打开图片选择器，选择图片<br>
     * 来获取图片
     *
     * @param activity
     * @param count：选择图片个数
     * @param requestcode
     */
    public static void pohotoSelect(FragmentActivity activity, int count, int requestcode) {
        pohotoSelect(activity, null, count, requestcode);
    }

    public static void pohotoSelect(Fragment fragment, int count, int requestcode) {
        pohotoSelect(null, fragment, count, requestcode);
    }

    @SuppressLint("CheckResult")
    private static void pohotoSelect(final FragmentActivity activity, final Fragment fragment, final int count, final int requestcode) {

        if (activity == null && fragment == null) {
            return;
        }

        Activity activity1 = activity;
        if (activity1 == null) {
            activity1 = fragment.getActivity();
        }
        Activity finalActivity = activity1;
        XXPermissions.with(activity1).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).request(new OnPermission() {
            @Override
            public void hasPermission(List<String> granted, boolean all) {
                if (all) {
                    if (activity != null) {
                        MultiImageSelector.create().showCamera(false).count(count).single().multi()
                                //.origin(ArrayList<String>)
                                .start(activity, requestcode);
                    } else if (fragment != null) {
                        MultiImageSelector.create().showCamera(false).count(count).single().multi()
                                //.origin(ArrayList<String>)
                                .start(fragment, requestcode);
                    }
                }
            }

            @Override
            public void noPermission(List<String> denied, boolean never) {
                if (never) {

                    ToastUtil.showToast("被永久拒绝授权，请手动授予存储和拍照权限");
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(finalActivity, denied);
                } else {
                    ToastUtil.showToast("获取存储权限失败");
                }

            }
        });


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
    public static void takePhoto(FragmentActivity activity, String path, int requestcode) {
        takePhoto(activity, null, path, requestcode);
    }

    public static void takePhoto(Fragment fragment, String path, int requestcode) {
        takePhoto(null, fragment, path, requestcode);
    }

    @SuppressLint("CheckResult")
    private static void takePhoto(FragmentActivity activity, final Fragment fragment, final String path, final int requestcode) {
        if (activity == null && fragment == null) {
            return;
        }

        if (activity == null) {
            activity = fragment.getActivity();
        }
        FragmentActivity finalActivity = activity;
        XXPermissions.with(activity).permission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).request(new OnPermission() {
            @Override
            public void hasPermission(List<String> granted, boolean all) {
                if (all) {

                    File file = new File(path);
                    try {
                        if (file.createNewFile()) {
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            if (finalActivity != null) {


                                Uri uri = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    uri = FileProvider.getUriForFile(fragment.getContext(), "com.zky.jyb.fileprovider", file);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                } else {
                                    uri = Uri.fromFile(file);
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                                finalActivity.startActivityForResult(intent, requestcode);
                            } else if (fragment != null) {
                                Uri uri = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    uri = FileProvider.getUriForFile(fragment.getContext(), "com.zky.jyb.fileprovider", file);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                } else {
                                    uri = Uri.fromFile(file);
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                fragment.startActivityForResult(intent, requestcode);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast("无法启动拍照程序");
                    }

                }

            }

            @Override
            public void noPermission(List<String> denied, boolean never) {
//                if (never) {
//                    ToastUtil.showToast("被永久拒绝授权，请手动授予存储和拍照权限");
//                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                    XXPermissions.startPermissionActivity(finalActivity, denied);
//                } else {
//                    ToastUtil.showToast("获取存储权限失败");
//                }

                PermissionToSetting(finalActivity,denied,never,"获取存储权限失败");
            }
        });


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
    public static void takeVideo(final FragmentActivity activity, final String path, final int requestcode) {
        takeVideo(activity, null, path, requestcode);
    }

    public static void takeVideo(final Fragment fragment, final String path, final int requestcode) {
        takeVideo(null, fragment, path, requestcode);
    }

    @SuppressLint("CheckResult")
    private static void takeVideo(FragmentActivity activity, final Fragment fragment, final String path, final int requestcode) {
        if (activity == null && fragment == null) {
            return;
        }

        if (activity == null) {
            activity = fragment.getActivity();
        }
        final Activity finalActivity = activity;
        XXPermissions.with(finalActivity).permission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).request(new OnPermission() {
            @Override
            public void hasPermission(List<String> granted, boolean all) {
                if (all) {
                    File file = new File(path);
                    try {
                        if (file.createNewFile()) {
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            if (finalActivity != null) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(finalActivity, "com.zky.jyb.fileprovider", file));
                                finalActivity.startActivityForResult(intent, requestcode);
                            } else if (fragment != null) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(fragment.getContext(), "com.zky.jyb.fileprovider", file));
                                fragment.startActivityForResult(intent, requestcode);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast("无法启动拍视频程序");
                    }
                }

            }

            @Override
            public void noPermission(List<String> denied, boolean never) {
                if (never) {
                    ToastUtil.showToast("被永久拒绝授权，请手动授予存储和拍照权限");
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(finalActivity, denied);
                } else {
                    ToastUtil.showToast("无摄像头权限,无法进行拍视频!");
                }

            }
        });


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
    public static String getPhotoPath(Activity activity) {
        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
//        String filepath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + filename;
        String s = Environment.getExternalStorageDirectory() + File.separator + "jyb/image/";
        File file = new File(s);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filepath = s + filename;
        return filepath;
    }

    //获取视频的路径
    public static String getVideoPath(Activity activity) {
        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".3gp";
        String filepath = activity.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + filename;
        return filepath;
    }
}
