package com.zky.basics.common.util

import com.zky.basics.common.util.uploadFile.UploadingFile

/**
 * Created by lk
 * Date 2019-11-11
 * Time 18:17
 * Detail:
 */
interface UpLoadingFileListener {
    /**
     * 0-100
     *
     * @param progress          当前文件的进度
     * @param index             当前文件的下标
     * @param fileTotalProgress //文件总进度
     */
    abstract fun upLoadingProgress(progress: Int, index: Int, fileTotalProgress: Int)

    abstract fun upLoadSuccess(failNum: Int)

    abstract fun upLoadSuccessPostiion(file: UploadingFile)
}