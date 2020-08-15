package com.zky.basics.common.binding.command

class BindingCommand<T>(private val execute: BindingAction?) {
    fun execute() {
        execute?.call()
    }
}