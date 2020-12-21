package com.zky.basics.api.dto

import java.io.Serializable

class RespDTO<Any> : Serializable {
    var code = 0
    var msg = ""
    var data: Any? = null
    override fun toString(): String {
        return "RespDTO{" +
                "code=" + code +
                ", error='" + msg + '\'' +
                ", data=" + data +
                '}'
    }

//    fun setCode(code: Int) {
//        this.code = code
//    }
//
//    fun setError(error: String) {
//        msg = error
//    }



}