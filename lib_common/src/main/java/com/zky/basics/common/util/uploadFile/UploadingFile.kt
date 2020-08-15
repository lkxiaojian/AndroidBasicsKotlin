package com.zky.basics.common.util.uploadFile

import java.io.File
import java.util.*

/**
 * Created by wangzihao
 * Date 2019/7/15
 * Time 18:18
 * Detail:
 */
class UploadingFile {
    var success: Boolean = false
    var servicePath: String? = null
    //filePath与file 任选其一，以file为主
    var filePath: String? = null
    var file: File? = null
    var fileName: String? = null//服务器文件名,要有唯一性
    var params: HashMap<String, String>? = null
    var tempFilePath: String? = null//临时文件
    var tmpCode:String?=null
}