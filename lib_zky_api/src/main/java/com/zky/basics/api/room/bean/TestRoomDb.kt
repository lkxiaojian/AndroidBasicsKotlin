package com.zky.basics.api.room.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by lk
 * Date 2020-01-08
 * Time 10:38
 * Detail:
 */
@Entity(tableName = "test")
data class TestRoomDb(
    @PrimaryKey
    var u_id: Long,
    @ColumnInfo(name = "user_name")
    var name: String?,
    var age: Int?,
    var sex: String?,
    var type: String?,
    var pub_year: Int = 0,
    var pub_year1: Int = 0,
    var pub_year2: Int = 0
)