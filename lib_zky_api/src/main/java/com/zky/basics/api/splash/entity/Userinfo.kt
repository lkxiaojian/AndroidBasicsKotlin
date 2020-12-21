package com.zky.basics.api.splash.entity

/**
 * Created by lk
 * Date 2019-11-11
 * Time 14:01
 * Detail:
 */
class Userinfo(
    var token: String?=null,
    var code: String? = null,
    var userName: String? = null,
    var password: String? = null,
    var province: String? = null,
    var city: String? = null,
    var county: String? = null,
    var college: String? = null,
    var accountLevel:Int  = 0,
    var accountStatus:Int  = 0,
    var isAdmin:Int = 0,
    var phone: String? = null,
    var headImg: String? = null,
    var createDate: String? = null,
    var provinceName: String? = null,
    var cityName: String? = null,
    var countyName: String? = null,
    var regionName: String? = null,
    var schoolName: String? = null
)

