package com.zky.basics.api.room.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by lk
 * Date 2020/9/24
 * Time 17:10
 * Detail:
 */
@Entity(tableName = "Fruit")
data class Fruit(@PrimaryKey var id: Int?, var name: String)