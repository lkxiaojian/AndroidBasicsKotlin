package com.zky.basics.common.event

interface EventCode {
    interface MainCode { //1000开始
    }

    interface NewsCode { //2000开始
    }

    interface FindCode { //3000开始
    }

    interface MeCode {
        companion object {
            //4000开始
            const val NEWS_TYPE_ADD = 4000
            const val NEWS_TYPE_DELETE = 4001
            const val NEWS_TYPE_UPDATE = 4002
            const val NEWS_TYPE_QUERY = 4003
            const val news_detail_add = 4004
        }
    }
}