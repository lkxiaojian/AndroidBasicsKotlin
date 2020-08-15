package com.zky.basics.common.event


interface RequestCode {
    interface Main { //1000开始
    }

    interface News { //2000开始
    }

    interface Find { //3000开始
    }

    interface Me {
        companion object {
            //4000开始
            const val NEWS_TYPE_ADD = 4000
        }
    }
}