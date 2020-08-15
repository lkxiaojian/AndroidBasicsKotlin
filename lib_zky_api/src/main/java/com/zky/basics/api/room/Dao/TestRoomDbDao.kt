package com.zky.basics.api.room.Dao

import android.arch.persistence.room.*
import com.zky.basics.api.room.bean.TestRoomDb

/**
 * Created by lk
 * Date 2020-01-08
 * Time 10:39
 * Detail:
 */
@Dao
interface TestRoomDbDao {
    @get:Query("SELECT * FROM test")
    val all: List<TestRoomDb?>?

    @Query("SELECT * FROM test WHERE u_id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray?): List<TestRoomDb?>?

    @Query("SELECT * FROM test WHERE u_id = (:userIds)")
    fun loadAllByKey(userIds: Int): List<TestRoomDb?>?

    @Query(
        "SELECT * FROM test WHERE user_name LIKE :first AND " +
                "user_name LIKE :last LIMIT 1"
    )
    fun findByName(first: String?, last: String?): TestRoomDb?

    @Insert
    fun insertAll(vararg users: TestRoomDb?)

    @Delete
    fun delete(user: TestRoomDb?)

    @Update
    fun updateUsers(vararg users: TestRoomDb?): Int

    @get:Query("select * from test")
    val users: List<TestRoomDb?>?
}