package com.zky.basics.api.room.Dao

import androidx.room.*
import com.zky.basics.api.room.bean.Fruit

//import com.zky.basics.api.room.bean.Fruit

/**
 * Created by lk
 * Date 2020/9/24
 * Time 17:13
 * Detail:
 */
@Dao
 interface FruitDao {

    @Query("SELECT * FROM Fruit")
    suspend fun all(): List<Fruit>

    @Query("SELECT * FROM Fruit WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray?): List<Fruit?>?

    @Query("SELECT * FROM Fruit WHERE id = (:userIds)")
    fun loadAllByKey(userIds: Int): List<Fruit?>?

    @Query(
        "SELECT * FROM Fruit WHERE name LIKE :first AND " +
                "name LIKE :last LIMIT 1"
    )
    suspend fun findByName(first: String?, last: String?): Fruit?

    suspend fun insertOrUpdate(vararg fruits: Fruit?) {
        deleteNoSuspend(*fruits)
        insertNoSuspend(*fruits)
    }


    @Insert
    suspend fun insertNoSuspend(vararg fruits: Fruit?)


    @Delete
    suspend fun delete(fruit: Fruit?)

    @Delete
    suspend fun deleteNoSuspend(vararg fruits: Fruit?)


    @Query("delete from Fruit where id = :userID")
    suspend fun deleteById(userID: String)

    @Update
    suspend fun updateUsers(vararg fruits: Fruit?): Int

    @Query("select * from Fruit")
    suspend fun fruits(): List<Fruit?>?

}