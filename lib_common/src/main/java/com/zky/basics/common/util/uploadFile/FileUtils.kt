package com.zky.basics.common.util.uploadFile


import java.io.File

/**
 * Created by wangzihao
 * Date 2019/5/23
 * Time 14:23
 * Detail:
 */
const val FILE_EXTENSION_SEPARATOR = "."

//fun readFile(filePath: String, charsetName: String? = null): StringBuilder {
//    val stringBuilder = StringBuilder()
//    val file = File(filePath)
//    val bufferedReader = if (charsetName == null) {
//        BufferedReader(InputStreamReader(FileInputStream(file)))
//    } else {
//        BufferedReader(InputStreamReader(FileInputStream(file), charsetName))
//    }
//    bufferedReader.use {
//        var line :String?
//        while (true) {
//            line = bufferedReader.readLine()
//            if (line == null) {
//                break
//            }
//            stringBuilder.append(line)
//        }
//    }
//    return stringBuilder
//}

//fun writeFile(filePath: String, content: String, isDelete: Boolean = false, isAppend: Boolean = false): Boolean {
//
//    val file=File(filePath)
//    if(isDelete){
//        deleteFile(file)
//    }
//    if(Verify.isEmpty(content)){
//        return false
//    }
//    makeDirs(filePath)
//    val fileWriter=FileWriter(filePath,isAppend)
//
//    fileWriter.use {
//        it.write(content)
//        it.flush()
//    }
//    return true
//}
fun deleteFile(file: File){
    if(!file.exists()){
        return
    }
    if(file.isFile){
        file.delete()
    }else{
        for (s in file.list()) {
            deleteFile(File(s))
        }
    }

}

/**
 * get file name from path, not include suffix
 *
 *
 * <pre>
 * getFileNameWithoutExtension(null)               =   null
 * getFileNameWithoutExtension("")                 =   ""
 * getFileNameWithoutExtension("   ")              =   "   "
 * getFileNameWithoutExtension("abc")              =   "abc"
 * getFileNameWithoutExtension("a.mp3")            =   "a"
 * getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
 * getFileNameWithoutExtension("c:\\")              =   ""
 * getFileNameWithoutExtension("c:\\a")             =   "a"
 * getFileNameWithoutExtension("c:\\a.b")           =   "a"
 * getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
 * getFileNameWithoutExtension("/home/admin")      =   "admin"
 * getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
</pre> *
 *
 * @param filePath
 * @return file name from path, not include suffix
 * @see
 */

/**
 * get file name from path, include suffix
 *
 *
 * <pre>
 * getFileName(null)               =   null
 * getFileName("")                 =   ""
 * getFileName("   ")              =   "   "
 * getFileName("a.mp3")            =   "a.mp3"
 * getFileName("a.b.rmvb")         =   "a.b.rmvb"
 * getFileName("abc")              =   "abc"
 * getFileName("c:\\")              =   ""
 * getFileName("c:\\a")             =   "a"
 * getFileName("c:\\a.b")           =   "a.b"
 * getFileName("c:a.txt\\a")        =   "a"
 * getFileName("/home/admin")      =   "admin"
 * getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
</pre> *
 *
 * @param filePath
 * @return file name from path, include suffix
// */
//fun getFileName(filePath: String): String {
//    if (filePath.isEmpty()) {
//        return filePath
//    }
//
//    val filePosi = filePath.lastIndexOf(File.separator)
//    return if (filePosi == -1) filePath else filePath.substring(filePosi + 1)
//}
/**
 * get folder name from path
 *
 *
 * <pre>
 * getFolderName(null)               =   null
 * getFolderName("")                 =   ""
 * getFolderName("   ")              =   ""
 * getFolderName("a.mp3")            =   ""
 * getFolderName("a.b.rmvb")         =   ""
 * getFolderName("abc")              =   ""
 * getFolderName("c:\\")              =   "c:"
 * getFolderName("c:\\a")             =   "c:"
 * getFolderName("c:\\a.b")           =   "c:"
 * getFolderName("c:a.txt\\a")        =   "c:a.txt"
 * getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
 * getFolderName("/home/admin")      =   "/home"
 * getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
</pre> *
 *
 * @param filePath
 * @return
 */
//fun getFolderName(filePath: String): String {
//
//    if (filePath.isEmpty()) {
//        return filePath
//    }
//
//    val filePosi = filePath.lastIndexOf(File.separator)
//    return if (filePosi == -1) "" else filePath.substring(0, filePosi)
//}

//fun makeDirs(filePath: String): Boolean {
//    val folderName = getFolderName(filePath)
//    if (folderName.isEmpty()) {
//        return false
//    }
//
//    val folder = File(folderName)
//    return if (folder.exists() && folder.isDirectory) true else folder.mkdirs()
//}