package com.zky.basics.common.util.uploadFile

import android.app.Application
import android.graphics.Bitmap
import android.text.TextUtils
import com.zky.basics.common.util.UpLoadingFileListener
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Created by wangzihao
 * Date 2019/7/15
 * Time 19:11
 * Detail:
 */
open class UploadingFileUtil {
    protected var uploadingFiles = arrayListOf<UploadingFile>()
    protected var index: Int = 0
    protected var retryNum: Int = 0
    protected var retrySum: Int = 0//有不成功重试
    protected var fileTotal: Long = 0
    protected var fileCurrentProgress: Long = 0
    protected var upLoadingFileListener: UpLoadingFileListener? = null
    /**
     * 上传文件
     *
     * @param list     文件列表
     * @param listener 监听
     */
    open fun upLoadingImageFile(mApplication:Application,list: List<UploadingFile>, listener: UpLoadingFileListener) {
        upLoadingImageFile(mApplication,list, listener, 0)
    }

    /**
     *
     * @param list      文件列表
     * @param listener  监听
     * @param retry     失败重试次数
     */
    open fun upLoadingImageFile(mApplication:Application,list: List<UploadingFile>, listener: UpLoadingFileListener, retry: Int) {}

    protected open fun upLoadingFile(uploadingFile: UploadingFile) {}
    /**
     * 检查文件上传情况
     */
    protected fun checkUploadingFile() {
        index++
        if (index > uploadingFiles.size - 1) {
            if (retryNum < retrySum) {
                retryNum++
                for (i in uploadingFiles.indices) {
                    if (!uploadingFiles[i].success) {
                        index = i
                        break
                    }
                }
                upLoadingFile(uploadingFiles[index])
            } else {
                destroy()
            }
        } else {
            val uploadingFile = uploadingFiles[index]
            if (uploadingFile.success) {
                checkUploadingFile()
            } else {
                upLoadingFile(uploadingFile)
            }
        }
    }

    /**
     * 销毁监听
     */
    protected open fun destroy() {
        var failNum = 0
        for (i in uploadingFiles.indices) {
            val uploadingFile = uploadingFiles[i]
            if (!uploadingFile.success) {
                failNum++
            }
            if (!TextUtils.isEmpty(uploadingFile.tempFilePath)) {
                File(uploadingFile.tempFilePath).delete()
            }
        }
        upLoadingFileListener?.upLoadSuccess(failNum)
        uploadingFiles.clear()
        upLoadingFileListener = null
    }

    fun destroyUpLoadingFileListener() {
        upLoadingFileListener = null
    }

   open fun saveBitmapFile(bitmap: Bitmap, filePath: String): File {
        val file = File(filePath)
        val bos = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos)
        bos.flush()
        bos.close()
        return file
    }


}