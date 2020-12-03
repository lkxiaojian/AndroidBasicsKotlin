package com.zky.basics.api.room.Dao

import androidx.room.*
import com.zky.basics.api.room.bean.TestRoomDb

/**
 * Created by lk
 * Date 2020-01-08
 * Time 10:39
 * Detail:
 */
@Dao
interface TestRoomDbDao {
    @Query("SELECT * FROM test")
    suspend fun all(): List<TestRoomDb>

    @Query("SELECT * FROM test WHERE u_id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray?): List<TestRoomDb?>?

    @Query("SELECT * FROM test WHERE u_id = (:userIds)")
    fun loadAllByKey(userIds: Int): List<TestRoomDb?>?

    @Query(
        "SELECT * FROM test WHERE user_name LIKE :first AND " +
                "user_name LIKE :last LIMIT 1"
    )
    suspend fun findByName(first: String?, last: String?): TestRoomDb?

    suspend fun insertOrUpdate(vararg users: TestRoomDb?) {
        deleteNoSuspend(*users)
        insertNoSuspend(*users)
    }
    suspend fun insertOrUpdate( users: ArrayList<TestRoomDb>) {
        deleteNoSuspend(users)
        insertNoSuspend(users)
    }

    @Insert
    suspend fun insertNoSuspend(vararg users: TestRoomDb?)

    @Insert
    suspend fun insertNoSuspend( users: List<TestRoomDb>)
    @Delete
    suspend fun delete(user: TestRoomDb?)

    @Delete
    suspend fun deleteNoSuspend(vararg users: TestRoomDb?)

    @Delete
    suspend fun deleteNoSuspend( users: List<TestRoomDb>)


    @Query("delete from test where u_id = :userID")
    suspend fun deleteById(userID: String)

    @Update
    suspend fun updateUsers(vararg users: TestRoomDb?): Int

    @Query("select * from test")
    suspend fun users(): List<TestRoomDb?>?
}