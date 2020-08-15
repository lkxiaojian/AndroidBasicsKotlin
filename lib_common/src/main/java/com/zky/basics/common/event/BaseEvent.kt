package com.zky.basics.common.event

open class BaseEvent<T> {
    var code: Int
    var data: T? = null
        private set

    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, data: T) {
        this.code = code
        this.data = data
    }

    fun setData(data: T) {
        this.data = data
    }
}