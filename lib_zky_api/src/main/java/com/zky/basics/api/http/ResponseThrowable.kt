package com.zky.basics.api.http


class ResponseThrowable(throwable: Throwable?, var code: Int) :
    Exception(throwable) {
    override var message: String? = null
}