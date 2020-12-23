package com.zky.basics.api.splash.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import android.graphics.Bitmap
import com.zky.basics.api.BR

/**
 * Created by lk
 * Date 2019-11-06
 * Time 14:58
 * Detail:
 * https://medium.com/@jencisov/androids-data-binding-s-baseobservable-class-and-bindable-annotation-in-kotlin-1a5c6682a3c1
 */

class SplashViewBean : BaseObservable {

    var name: String? = null
    private var paw: String? = null
    private var _timeMeesage: String? = null
    private var _rgPhone: String? = null
    private var _rgName: String? = null
    private var _rgImageCode: String? = null
    private var _rgCode: String? = null
    private var _rgLevel: String? = null
    private var _rgProvince: String? = null
    private var _rgCity: String? = null
    private var _rgTwon: String? = null
    private var _rgSchool: String? = null
    private var _rgPaw: String? = null
    private var _rgqrPaw: String? = null
    private var _rgImageUrl: Bitmap? = null
    var rgErrorImageUrl: Int = -1
    private var _levelIndel: Int = -1
    private var _writeLevel: Boolean = false
    private var _writeProvince: Boolean = false
    private var _writeCity: Boolean = false
    private var _writeTwon: Boolean = false
    private var _writeSchool: Boolean = false

    constructor()

    var timeMeesage: String?
        @Bindable get() = _timeMeesage
        set(value) {
            _timeMeesage = value
            notifyPropertyChanged(BR.timeMeesage)
        }

    var rgPhone: String?
        @Bindable get() = _rgPhone
        set(value) {
            _rgPhone = value
            notifyPropertyChanged(BR.rgPhone)
        }
    var rgName: String?
        @Bindable get() = _rgName
        set(value) {
            _rgName = value
            notifyPropertyChanged(BR.rgName)
        }

    var rgImageCode: String?
        @Bindable get() = _rgImageCode
        set(value) {
            _rgImageCode = value
            notifyPropertyChanged(BR.rgImageCode)
        }


    var rgCode: String?
        @Bindable get() = _rgCode
        set(value) {
            _rgCode = value
            notifyPropertyChanged(BR.rgCode)
        }

    var levelIndel: Int
        @Bindable get() = _levelIndel
        set(value) {
            _levelIndel = value
            notifyPropertyChanged(BR.levelIndel)
        }
    var rgqrPaw: String?
        @Bindable get() = _rgqrPaw
        set(value) {
            _rgqrPaw = value
            notifyPropertyChanged(BR.rgqrPaw)
        }

    var rgPaw: String?
        @Bindable get() = _rgPaw
        set(value) {
            _rgPaw = value
            notifyPropertyChanged(BR.rgPaw)
        }


    var rgSchool: String?
        @Bindable get() = _rgSchool
        set(value) {
            _rgSchool = value
            notifyPropertyChanged(BR.rgSchool)
        }


    var rgTwon: String?
        @Bindable get() = _rgTwon
        set(value) {
            _rgTwon = value
            notifyPropertyChanged(BR.rgTwon)
        }

    var rgCity: String?
        @Bindable get() = _rgCity
        set(value) {
            _rgCity = value
            notifyPropertyChanged(BR.rgCity)
        }

    var rgProvince: String?
        @Bindable get() = _rgProvince
        set(value) {
            _rgProvince = value
            notifyPropertyChanged(BR.rgProvince)
        }


    var writeSchool: Boolean
        @Bindable get() = _writeSchool
        set(value) {
            _writeSchool = value
            notifyPropertyChanged(BR.writeSchool)
        }

    var writeTwon: Boolean
        @Bindable get() = _writeTwon
        set(value) {
            _writeTwon = value
            notifyPropertyChanged(BR.writeTwon)
        }

    var writeCity: Boolean
        @Bindable get() = _writeCity
        set(value) {
            _writeCity = value
            notifyPropertyChanged(BR.writeCity)
        }

    var rgImageUrl: Bitmap?
        @Bindable get() = _rgImageUrl
        set(value) {
            _rgImageUrl = value
            notifyPropertyChanged(BR.rgImageUrl)
        }


    var writeProvince: Boolean
        @Bindable get() = _writeProvince
        set(value) {
            _writeProvince = value
            notifyPropertyChanged(BR.writeProvince)
        }


    var rgLevel: String?
        @Bindable get() = _rgLevel
        set(value) {
            _rgLevel = value
            notifyPropertyChanged(BR.rgLevel)
        }


    var writeLevel: Boolean
        @Bindable get() = _writeLevel
        set(value) {
            _writeLevel = value
            notifyPropertyChanged(BR.writeLevel)
        }

}