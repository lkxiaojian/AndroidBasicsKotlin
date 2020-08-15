package com.zky.basics.api.room.bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

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
    var type: String?
)