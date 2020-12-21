package com.zky.basics.common.util.log.klog

import android.util.Log
import com.zky.basics.common.util.log.KLog
import com.zky.basics.common.util.log.KLogUtil
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object JsonLog {
    @JvmStatic
    fun printJson(tag: String?, msg: String, headString: String) {
        var message: String
        message = try {
            if (msg.startsWith("{")) {
                val jsonObject = JSONObject(msg)
                jsonObject.toString(KLog.JSON_INDENT)
            } else if (msg.startsWith("[")) {
                val jsonArray = JSONArray(msg)
                jsonArray.toString(KLog.JSON_INDENT)
            } else {
                msg
            }
        } catch (e: JSONException) {
            msg
        }
        KLogUtil.printLine(tag, true)
        message = headString + KLog.LINE_SEPARATOR + message
        val lines = message.split(KLog.LINE_SEPARATOR).toTypedArray()
        for (line in lines) {
            Log.d(tag, "║ $line")
        }
        KLogUtil.printLine(tag, false)
    }
}