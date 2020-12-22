package com.zky.basics.api.splash.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lk
 * Date 2019-11-11
 * Time 14:01
 * Detail:
 */
class Userinfo(
    var token: String? = null,
    var code: String? = null,
    var userName: String? = null,
    var password: String? = null,
    var province: String? = null,
    var city: String? = null,
    var county: String? = null,
    var college: String? = null,
    var accountLevel: Int = 0,
    var accountStatus: Int = 0,
    var isAdmin: Int = 0,
    var phone: String? = null,
    var headImg: String? = null,
    var createDate: String? = null,
    var provinceName: String? = null,
    var cityName: String? = null,
    var countyName: String? = null,
    var regionName: String? = null,
    var schoolName: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(token)
        parcel.writeString(code)
        parcel.writeString(userName)
        parcel.writeString(password)
        parcel.writeString(province)
        parcel.writeString(city)
        parcel.writeString(county)
        parcel.writeString(college)
        parcel.writeInt(accountLevel)
        parcel.writeInt(accountStatus)
        parcel.writeInt(isAdmin)
        parcel.writeString(phone)
        parcel.writeString(headImg)
        parcel.writeString(createDate)
        parcel.writeString(provinceName)
        parcel.writeString(cityName)
        parcel.writeString(countyName)
        parcel.writeString(regionName)
        parcel.writeString(schoolName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Userinfo> {
        override fun createFromParcel(parcel: Parcel): Userinfo {
            return Userinfo(parcel)
        }

        override fun newArray(size: Int): Array<Userinfo?> {
            return arrayOfNulls(size)
        }
    }
}

