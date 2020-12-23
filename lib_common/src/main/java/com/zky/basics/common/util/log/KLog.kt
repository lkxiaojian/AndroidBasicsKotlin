package com.zky.basics.common.util.log

import android.text.TextUtils
import com.zky.basics.common.util.log.klog.BaseLog.printDefault
import com.zky.basics.common.util.log.klog.FileLog.printFile
import com.zky.basics.common.util.log.klog.JsonLog.printJson
import com.zky.basics.common.util.log.klog.XmlLog.printXml
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

object KLog {
    val LINE_SEPARATOR = System.getProperty("line.separator")
    const val NULL_TIPS = "Log with null object"
    private const val DEFAULT_MESSAGE = "execute"
    private const val PARAM = "Param"
    private const val NULL = "null"
    private const val TAG_DEFAULT = "KLog"
    private const val SUFFIX = ".java"
    const val JSON_INDENT = 4
    const val V = 0x1
    const val D = 0x2
    const val I = 0x3
    const val W = 0x4
    const val E = 0x5
    const val A = 0x6
    private const val JSON = 0x7
    private const val XML = 0x8
    private const val STACK_TRACE_INDEX_5 = 5
    private const val STACK_TRACE_INDEX_4 = 4
    private var mGlobalTag: String? = null
    private var mIsGlobalTagEmpty = true
    private var IS_SHOW_LOG = true
    fun init(isShowLog: Boolean) {
        IS_SHOW_LOG = isShowLog
    }

    fun init(isShowLog: Boolean, tag: String?) {
        IS_SHOW_LOG = isShowLog
        mGlobalTag = tag
        mIsGlobalTagEmpty = TextUtils.isEmpty(mGlobalTag)
    }

    fun v() {
        printLog(V, null, DEFAULT_MESSAGE)
    }

    fun v(msg: Any?) {
        printLog(V, null, msg!!)
    }

    @JvmStatic
    fun v(tag: String?, vararg objects: Any?) {
        printLog(V, tag, *objects)
    }

    fun d() {
        printLog(D, null, DEFAULT_MESSAGE)
    }

    fun d(msg: Any?) {
        printLog(D, null, msg!!)
    }

    fun d(tag: String?, vararg objects: Any?) {
        printLog(D, tag, *objects)
    }

    fun i() {
        printLog(I, null, DEFAULT_MESSAGE)
    }

    fun i(msg: Any?) {
        printLog(I, null, msg!!)
    }

    fun i(tag: String?, vararg objects: Any?) {
        printLog(I, tag, *objects)
    }

    fun w() {
        printLog(W, null, DEFAULT_MESSAGE)
    }

    fun w(msg: Any?) {
        printLog(W, null, msg!!)
    }

    fun w(tag: String?, vararg objects: Any?) {
        printLog(W, tag, *objects)
    }

    fun e() {
        printLog(E, null, DEFAULT_MESSAGE)
    }

    fun e(msg: Any?) {
        printLog(E, null, msg!!)
    }

    fun e(tag: String?, vararg objects: Any?) {
        printLog(E, tag, *objects)
    }

    fun a() {
        printLog(A, null, DEFAULT_MESSAGE)
    }

    fun a(msg: Any?) {
        printLog(A, null, msg!!)
    }

    fun a(tag: String?, vararg objects: Any?) {
        printLog(A, tag, *objects)
    }

    fun json(jsonFormat: String?) {
        printLog(JSON, null, jsonFormat!!)
    }

    fun json(tag: String?, jsonFormat: String?) {
        printLog(JSON, tag, jsonFormat!!)
    }

    fun xml(xml: String?) {
        printLog(XML, null, xml!!)
    }

    fun xml(tag: String?, xml: String?) {
        printLog(XML, tag, xml!!)
    }

    fun file(targetDirectory: File, msg: Any) {
        printFile(null, targetDirectory, null, msg)
    }

    fun file(tag: String?, targetDirectory: File, msg: Any) {
        printFile(tag, targetDirectory, null, msg)
    }

    fun file(tag: String?, targetDirectory: File, fileName: String?, msg: Any) {
        printFile(tag, targetDirectory, fileName, msg)
    }

    fun debug() {
        printDebug(null, DEFAULT_MESSAGE)
    }

    fun debug(msg: Any?) {
        printDebug(null, msg!!)
    }

    fun debug(tag: String?, vararg objects: Any?) {
        printDebug(tag, *objects)
    }

    fun trace() {
        printStackTrace()
    }

    private fun printStackTrace() {
        if (!IS_SHOW_LOG) {
            return
        }
        val tr = Throwable()
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        val message = sw.toString()
        val traceString = message.split("\\n\\t").toTypedArray()
        val sb = StringBuilder()
        sb.append("\n")
        for (trace in traceString) {
            if (trace.contains("at com.socks.library.KLog")) {
                continue
            }
            sb.append(trace).append("\n")
        }
        val contents = wrapperContent(STACK_TRACE_INDEX_4, null, sb.toString())
        val tag = contents[0]
        val msg = contents[1]
        val headString = contents[2]
        printDefault(D, tag!!, headString + msg)
    }

    private fun printLog(type: Int, tagStr: String?, vararg objects: Any?) {
        if (!IS_SHOW_LOG) {
            return
        }
        val contents = wrapperContent(STACK_TRACE_INDEX_5, tagStr, *objects)
        val tag = contents[0]
        val msg = contents[1]
        val headString = contents[2]
        when (type) {
            V, D, I, W, E, A -> printDefault(type, tag!!, headString + msg)
            JSON -> printJson(tag, msg!!, headString!!)
            XML -> printXml(tag, msg, headString!!)
        }
    }

    private fun printDebug(tagStr: String?, vararg objects: Any?) {
        val contents = wrapperContent(STACK_TRACE_INDEX_5, tagStr, *objects)
        val tag = contents[0]
        val msg = contents[1]
        val headString = contents[2]
        printDefault(D, tag!!, headString + msg)
    }

    private fun printFile(
        tagStr: String?,
        targetDirectory: File,
        fileName: String?,
        objectMsg: Any
    ) {
        if (!IS_SHOW_LOG) {
            return
        }
        val contents = wrapperContent(STACK_TRACE_INDEX_5, tagStr, objectMsg)
        val tag = contents[0]
        val msg = contents[1]
        val headString = contents[2]
        printFile(tag, targetDirectory, fileName, headString!!, msg!!)
    }

    private fun wrapperContent(
        stackTraceIndex: Int,
        tagStr: String?,
        vararg objects: Any?
    ): Array<String?> {
        val stackTrace = Thread.currentThread().stackTrace
        val targetElement = stackTrace[stackTraceIndex]
        var className = targetElement.className
        val classNameInfo = className.split("\\.").toTypedArray()
        if (classNameInfo.size > 0) {
            className = classNameInfo[classNameInfo.size - 1] + SUFFIX
        }
        if (className.contains("$")) {
            className = className.split("\\$").toTypedArray()[0] + SUFFIX
        }
        val methodName = targetElement.methodName
        var lineNumber = targetElement.lineNumber
        if (lineNumber < 0) {
            lineNumber = 0
        }
        var tag = tagStr ?: className
        if (mIsGlobalTagEmpty && TextUtils.isEmpty(tag)) {
            tag = TAG_DEFAULT
        } else if (!mIsGlobalTagEmpty) {
            tag = mGlobalTag
        }
        val msg = getObjectsString(*objects)
        val headString = "[ ($className:$lineNumber)#$methodName ] "
        return arrayOf(tag, msg, headString)
    }

    private fun getObjectsString(vararg obj: Any?): String {
        return if (obj.size > 1) {
            val stringBuilder = StringBuilder()
            stringBuilder.append("\n")
            for (i in obj.indices) {
                val objectTmp = obj[i]
                if (objectTmp == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ")
                        .append(NULL).append("\n")
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ")
                        .append(objectTmp.toString()).append("\n")
                }
            }
            stringBuilder.toString()
        } else {
            if (obj.isNotEmpty()) {
                obj[0]?.toString() ?: NULL
            } else {
                NULL
            }

        }
    }
}