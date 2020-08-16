package com.zky.basics.api.splash.entity

import com.zky.basics.annotation.AllowNoArg

/**
 * Created by lk
 * Date 2020/8/16
 * Time 08:51
 * Detail: kotlin 无参构造 测试类
 *
 * 1.使用注解 AllowNoArg
 * 2.给该类的每个属性赋默认值
 */
@AllowNoArg
data class TestE(var value: String="")