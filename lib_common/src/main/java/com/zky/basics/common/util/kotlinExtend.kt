package com.zky.basics.common.util

/**
 * Created by lk
 * Date 2019-11-12
 * Time 08:54
 * Detail:
 */
sealed class BooleanExt<out T>

object OtherBooleanExt : BooleanExt<Nothing>()
class YesBooleanExt<T>(val value: T) : BooleanExt<T>()

inline fun <T> Boolean.yes(block: () -> T) =
    when{
        this -> YesBooleanExt(block())
        else -> OtherBooleanExt
    }


inline infix fun <T> BooleanExt<T>.otherwise(block: () -> T) =
    when (this) {
        is YesBooleanExt<T> -> this.value
        else -> block()
    }