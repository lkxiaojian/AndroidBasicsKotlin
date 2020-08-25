package com.zky.basics.api.room

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import android.content.Context
import com.zky.basics.api.room.Dao.TestRoomDbDao
import com.zky.basics.api.room.bean.TestRoomDb
import java.io.File

/**
 * Created by lk
 * Date 2019-12-25
 * Time 15:45
 * Detail:
 */
@Database(entities = [TestRoomDb::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun testRoomDbDao(): TestRoomDbDao?

    companion object {
        private var INSTANCE //创建单例
                : AppDatabase? = null
        fun getDatabase(context: Context?): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        val path = (
                                "${File.separator}mnt${File.separator}sdcard${File.separator}db${File.separator}test.db")

                        INSTANCE = Room.databaseBuilder(context!!, AppDatabase::class.java, path)
                            .addCallback(sOnOpenCallback)
                            .addMigrations(MIGRATION_1_2)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {}
        }
        private val sOnOpenCallback: Callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
    }
}