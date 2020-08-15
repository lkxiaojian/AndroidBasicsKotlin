package com.zky.basics.common.event


interface KeyCode {
    interface Main
    interface News {
        companion object {
            const val NEWS_TYPE = "newstype"
            const val NEWS_ID = "newsid"
        }
    }

    interface Find
    interface Me
}