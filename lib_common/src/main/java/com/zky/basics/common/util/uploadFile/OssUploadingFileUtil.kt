package utils.utils.uploadFile

import android.app.Activity
import android.app.Application
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.util.Log
import com.alibaba.sdk.android.oss.*
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.utils.IOUtils
import com.alibaba.sdk.android.oss.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zky.basics.api.config.API
import com.zky.basics.api.dto.RespDTO
import com.zky.basics.common.constant.Contants
import com.zky.basics.common.util.*
import com.zky.basics.common.util.uploadFile.*
import top.zibin.luban.OnCompressListener
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import kotlin.math.min


/**
 * Created by wangzihao
 * Date 2019/7/15
 * Time 19:14
 * Detail:
 */
class OssUploadingFileUtil(

) :
    UploadingFileUtil() {
    private var oss: OSS? = null
    private val bucketName = "edu-zxzjjg"
    private var activity: FragmentActivity? = null
    private var initImageCompress = false
    private val callBackParam = hashMapOf<String, String>()
    private var mApplication: Application? = null
    fun setActivity(activity: FragmentActivity) {
        this.activity = activity
    }

    private fun initOSS(mApplication: Application) {


        //        移动端是不安全环境，不建议直接使用阿里云主账号ak，sk的方式。建议使用STS方式。具体参
        //        https://help.aliyun.com/document_detail/31920.html
        //        注意：SDK 提供的 PlainTextAKSKCredentialProvider 只建议在测试环境或者用户可以保证阿里云主账号AK，SK安全的前提下使用。具体使用如下
        //        主账户使用方式
        //        String AK = "******";
        //        String SK = "******";
        //        credentialProvider = new PlainTextAKSKCredentialProvider(AK,SK)
        //        以下是使用STS Sever方式。
        //        如果用STS鉴权模式，推荐使用OSSAuthCredentialProvider方式直接访问鉴权应用服务器，token过期后可以自动更新。
        //        详见：https://help.aliyun.com/document_detail/31920.html
        //        OSSClient的生命周期和应用程序的生命周期保持一致即可。在应用程序启动时创建一个ossClient，在应用程序结束时销毁即可。

        try {
            val conf = ClientConfiguration()
            conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒
            conf.socketTimeout = 15 * 1000 // socket超时，默认15秒
            conf.maxConcurrentRequest = 5 // 最大并发请求书，默认5个
            conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次
            oss = OSSClient(mApplication, API.ImageAliFolderPath, Contants.ossStsTokenCredentialProvider, conf)
            OSSLog.enableLog()
            val file = File("${mApplication.cacheDir}${File.separator}TempImage")
            if (!file.exists()) {
                file.mkdirs()
            }
            initImageCompress = true

            object : Thread() {
                override fun run() {
                    getImageCompress(uploadingFiles[index], file)
                }
            }.start()

        } catch (e: Exception) {
            Log.e("e", "e--" + e.message.toString())

        }
    }

    /**
     * 上传文件
     *
     * @param list     文件列表
     * @param listener 监听
     */
    override fun upLoadingImageFile(
        mApplication: Application,
        list: List<UploadingFile>,
        listener: UpLoadingFileListener
    ) {
        this.mApplication = mApplication
        upLoadingImageFile(mApplication, list, listener, 0)
    }

    /**
     * @param list     文件列表
     * @param listener 监听
     * @param retry    失败重试次数
     */
    override fun upLoadingImageFile(
        mApplication: Application,
        list: List<UploadingFile>,
        listener: UpLoadingFileListener,
        retry: Int
    ) {
        this.mApplication = mApplication
        if (list.isEmpty()) {
            return
        }
        time = 0L
        upLoadingFileListener = listener
        retrySum = retry
        index = 0
        fileTotal = 0
        fileCurrentProgress = 0
        uploadingFiles.addAll(list)
        var servicePath = ""
        for (uploadingFile in uploadingFiles) {
            servicePath = uploadingFile.servicePath ?: ""
            var file = uploadingFile.file
            if (file == null) {
                file = File(uploadingFile.filePath)
            }
            uploadingFile.file = file
        }
        callBackParam.put("callbackUrl", servicePath)
        callBackParam.put("callbackBodyType", "application/json")
        initOSS(mApplication)
    }

    private fun getImageCompress(uploadingFile: UploadingFile, targetDir: File) {
        if (MediaFileUtil.isImageFileType(uploadingFile.file?.path ?: "")) {
            getImageCompress(mApplication!!, uploadingFile.file, targetDir, object : OnCompressListener {
                override fun onStart() {}

                override fun onSuccess(file: File) {
                    fileTotal += file.length()
                    uploadingFile.tempFilePath = file.path
                    index++
                    if (index >= uploadingFiles.size) {
                        index = 0
                        upLoadingFile(uploadingFiles.get(index))
                    } else {
                        getImageCompress(uploadingFiles.get(index), targetDir)
                    }
                }

                override fun onError(e: Throwable) {
                    fileTotal += uploadingFiles.get(index).file?.length() ?: 0
                    index++
                    if (index >= uploadingFiles.size) {
                        index = 0
                        upLoadingFile(uploadingFiles.get(index))
                    } else {
                        getImageCompress(uploadingFiles.get(index), targetDir)
                    }
                }
            })
        } else {
            fileTotal += uploadingFiles.get(index).file?.length() ?: 0
            index++
            if (index >= uploadingFiles.size) {
                index = 0
                upLoadingFile(uploadingFiles.get(index))
            } else {
                getImageCompress(uploadingFiles.get(index), targetDir)
            }
        }
    }

    /**
     * 上传文件具体方法
     *
     * @param uploadingFile
     */
    override fun upLoadingFile(uploadingFile: UploadingFile) {
        if (!NetUtil.checkNet()) {
            return
        }
        if (uploadingFile.file == null) {
            return
        }
//        if (isFileTooLong(uploadingFile)) {
//            beginMulti(uploadingFile)
//        } else {
//            asynBeginResumable(uploadingFile)
//        }
        asynBeginMulti(uploadingFile)
    }

    val fileLong = 5 * 1024 * 1024
    var time = 0L

    private fun isFileTooLong(uploadingFile: UploadingFile): Boolean {
        val length =
            if (uploadingFile.tempFilePath?.isNotEmpty() == true) {
                File(uploadingFile.tempFilePath).length()
            } else {
                File(uploadingFile.filePath).length()
            }
        return length >= fileLong
    }

    private fun beginByte(objectKey: String, filePath: String) {

        val request = PutObjectRequest(bucketName, objectKey, filePath)
        if (oss == null) {
            initOSS(mApplication!!)
        }
       oss!!.asyncPutObject(request, object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest, result: PutObjectResult) {
                deleteFile(File(filePath))
            }

            override fun onFailure(
                request: PutObjectRequest,
                clientException: ClientException,
                serviceException: ServiceException
            ) {
            }
        })
    }

    private fun beginMulti(uploadingFile: UploadingFile) {
        try {
            val uploadId: String
            val params = uploadingFile.params
            val objectKey = params?.get("filePath") ?: ""

            val init = InitiateMultipartUploadRequest(bucketName, objectKey)
            val initResult = oss!!.initMultipartUpload(init)
            uploadId = initResult.uploadId
            val partSize = (2 * 1024 * 1024).toLong() // 设置分片大小
            var currentIndex = 1 // 上传分片编号，从1开始
            // 需要分片上传的文件
            val uploadFile = if (uploadingFile.tempFilePath?.isNotEmpty() == true) {
                File(uploadingFile.tempFilePath)
            } else {
                File(uploadingFile.filePath)
            }
            val input = FileInputStream(uploadFile)
            val fileLength = uploadFile.length()
            val map = hashMapOf<Int, Long>()
            var uploadedLength: Long = 0
            val partETags = ArrayList<PartETag>() // 保存分片上传的结果
            while (uploadedLength < fileLength) {
                val partLength = min(partSize, fileLength - uploadedLength)
                val partData = IOUtils.readStreamAsBytesArray(input, partLength.toInt()) // 按照分片大小读取文件的一段内容
                val uploadPart = UploadPartRequest(bucketName, objectKey, uploadId, currentIndex)
                uploadPart.partContent = partData // 设置分片内容
                uploadPart.progressCallback =
                    OSSProgressCallback { request, currentSize, _ ->
                        val millis = System.currentTimeMillis()
                        map[request.partNumber] = currentSize
                        if (millis - time > 1000) {
                            var currentUpdateLength = 0L
                            map.forEach {
                                currentUpdateLength += it.value
                            }
//                            for ((num, length) in map) {
//                                currentUpdateLength += length
//                            }

                            val progress = ((100.0 * currentUpdateLength) / fileLength).toInt()
                            sendMsg(ONPROGRESS, progress, 0)
                            time = millis
                        }
                    }
                val uploadPartResult = oss!!.uploadPart(uploadPart)
                partETags.add(PartETag(currentIndex, uploadPartResult.eTag)) // 保存分片上传成功后的结果
                uploadedLength += partLength
                map[currentIndex] = 0L
                currentIndex++
            }
            val complete = CompleteMultipartUploadRequest(bucketName, objectKey, uploadId, partETags)
            callBackParam["callbackBody"] = getCallbackVars(params)
            if (uploadingFile.servicePath != null) {
                complete.callbackParam = callBackParam
            }
            val completeResult = oss!!.completeMultipartUpload(complete)
            val statusCode = completeResult.statusCode
            if (statusCode == 200) {
                returnSuccess(uploadingFile, completeResult?.serverCallbackReturnBody ?: "", objectKey)
            }
        } catch (e: ClientException) {
            sendMsg(OSSERROR, 0, 0)
        } catch (e: ServiceException) {
            sendMsg(OSSERROR, 0, 0)
        } catch (e: IOException) {
            sendMsg(OSSERROR, 0, 0)
        }

    }

    private fun asynBeginResumable(uploadingFile: UploadingFile) {
        val params = uploadingFile.params

        val recordDirectory = Environment.getExternalStorageDirectory().absolutePath + "/oss_record/"

        val recordDir = File(recordDirectory)

        // 要保证目录存在，如果不存在则主动创建
        if (!recordDir.exists()) {
            recordDir.mkdirs()
        }
        val objectKey = params?.get("filePath") ?: ""
        val request =
            if (!TextUtils.isEmpty(uploadingFile.tempFilePath)) {
                ResumableUploadRequest(
                    bucketName, objectKey,
                    uploadingFile.tempFilePath,
                    recordDirectory
                )
            } else {
                ResumableUploadRequest(
                    bucketName, objectKey,
                    uploadingFile.filePath,
                    recordDirectory
                )
            }
        request.crC64 = OSSRequest.CRC64Config.YES
        request.progressCallback =
            OSSProgressCallback<ResumableUploadRequest> { _, currentSize, totalSize ->
                val millis = System.currentTimeMillis()
                val progress = (100 * currentSize / totalSize).toInt()
                val fileTotalProgress = ((fileCurrentProgress + totalSize * progress) * 100.0 / fileTotal).toInt()
                if (millis - time > 2000) {
                    sendMsg(ONPROGRESS, progress, fileTotalProgress)
                    time = millis
                }
                if (progress >= 1) {
                    fileCurrentProgress += totalSize
                }
            }
        callBackParam.put("callbackBody", getCallbackVars(params))
        if (uploadingFile.servicePath != null) {
            request.callbackParam = callBackParam
        }
       oss!!.asyncResumableUpload(request,
            object : OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> {
                override fun onSuccess(request: ResumableUploadRequest?, result: ResumableUploadResult?) {
                    returnSuccess(uploadingFile, result?.serverCallbackReturnBody ?: "", objectKey)
                }

                override fun onFailure(
                    request: ResumableUploadRequest?,
                    clientException: ClientException?,
                    serviceException: ServiceException?
                ) {
                    sendMsg(OSSERROR, 0, 0)
                }
            })
//        resumableUpload.waitUntilFinished()
    }

    private fun asynBeginMulti(uploadingFile: UploadingFile) {
        val params = uploadingFile.params

        val recordDirectory = Environment.getExternalStorageDirectory().absolutePath + "/oss_record/"

        val recordDir = File(recordDirectory)

        // 要保证目录存在，如果不存在则主动创建
        if (!recordDir.exists()) {
            recordDir.mkdirs()
        }
        val objectKey = params?.get("filePath") ?: ""
        val request =
            if (!TextUtils.isEmpty(uploadingFile.tempFilePath)) {
                MultipartUploadRequest(
                    bucketName, objectKey,
                    uploadingFile.tempFilePath
                )
            } else {
                MultipartUploadRequest<MultipartUploadRequest<*>>(
                    bucketName, objectKey,
                    uploadingFile.filePath
                )
            }
        request.crC64 = OSSRequest.CRC64Config.YES
        request.progressCallback =
            OSSProgressCallback<MultipartUploadRequest<*>> { _, currentSize, totalSize ->
                val millis = System.currentTimeMillis()
                val progress = (100 * currentSize / totalSize).toInt()
                val fileTotalProgress = ((fileCurrentProgress + totalSize * progress) * 100.0 / fileTotal).toInt()
                if (millis - time > 2000) {
                    sendMsg(ONPROGRESS, progress, fileTotalProgress)
                    time = millis
                }
                if (progress >= 1) {
                    fileCurrentProgress += totalSize
                }
            }
        callBackParam["callbackBody"] = getCallbackVars(params)
        if (uploadingFile.servicePath != null) {
            request.callbackParam = callBackParam
        }
          oss!!.asyncMultipartUpload(request,
            object : OSSCompletedCallback<MultipartUploadRequest<*>, CompleteMultipartUploadResult> {
                override fun onSuccess(request: MultipartUploadRequest<*>?, result: CompleteMultipartUploadResult?) {


                    handler.post {
                        returnSuccess(uploadingFile, result?.serverCallbackReturnBody ?: "", objectKey)
                    }
                }


                override fun onFailure(
                    request: MultipartUploadRequest<*>?,
                    clientException: ClientException?,
                    serviceException: ServiceException?
                ) {
                    if (clientException != null) {
                        // 本地异常，如网络异常等。

                        Log.e("RequestId", clientException.message.toString())
                    }
                    if (serviceException != null) {
                        // 服务异常。
                        Log.e("ErrorCode", serviceException.errorCode)
                        Log.e("RequestId", serviceException.requestId)
                        Log.e("HostId", serviceException.hostId)
                        Log.e("RawMessage", serviceException.rawMessage)
                    }
                    sendMsg(OSSERROR, 0, 0)
                }

            })
    }

    private fun returnSuccess(uploadingFile: UploadingFile, body: String, objectKey: String) {

        Activity().runOnUiThread {
        if (uploadingFile.servicePath != null) {
            val data = Gson().fromJson(body, RespDTO::class.java)
            data?.let {
                (it.code == 200).yes {
                    if (!TextUtils.isEmpty(uploadingFile.tempFilePath)) {
                        File(uploadingFile.tempFilePath).delete()
                    }
                    uploadingFile.servicePath = "${it.data}"
                    uploadingFile.tmpCode=""
                    uploadingFile.filePath = objectKey
                    uploadingFile.success = true
                    uploadingFile.tmpCode="${it.data}"
                    sendMsg(OSSUCCESS, 0, 0)
                    upLoadingFileListener?.upLoadSuccessPostiion(uploadingFile)
                } otherwise {
                    sendMsg(OSSERROR, 0, 0)
                }
            } ?: sendMsg(OSSERROR, 0, 0)
        } else {
            if (!TextUtils.isEmpty(uploadingFile.tempFilePath)) {
                File(uploadingFile.tempFilePath).delete()
            }
            uploadingFile.success = true
            uploadingFile.filePath = objectKey
            sendMsg(OSSUCCESS, 0, 0)
        }
        }
    }


    private fun getCallbackVars(map: HashMap<String, String>?): String {
        val jsonObject = JsonObject()
        map?.entries?.forEach {
            val key = it.key
            val value = it.value
            jsonObject.addProperty(key, value)
        }
        return jsonObject.toString()
    }

    private fun sendMsg(what: Int, progress: Int, fileCurrentProgress: Int) {
        val msg = handler.obtainMessage()
        msg.what = what
        msg.arg1 = progress
        msg.arg2 = fileCurrentProgress
        handler.sendMessage(msg)
    }

    override fun destroy() {
        initImageCompress = false
        super.destroy()
    }

    private val OSSUCCESS = 1
    private val OSSERROR = 2
    private val ONPROGRESS = 3
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                OSSUCCESS -> checkUploadingFile()
                OSSERROR -> checkUploadingFile()
                ONPROGRESS -> {
                    val progress = msg.arg1
                    val fileTotalProgress = msg.arg2
                    upLoadingFileListener?.upLoadingProgress(progress, index, fileTotalProgress)
                }
            }
        }
    }
}