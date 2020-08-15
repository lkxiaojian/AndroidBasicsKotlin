package com.zky.basics.api.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import android.os.Environment
import android.util.Log
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

                        Log.e("tag"," Environment.getDataDirectory()---> ${Environment.getDataDirectory()}")
                        Log.e("tag"," Environment.getDownloadCacheDirectory()---> ${Environment.getDownloadCacheDirectory()}")
                        Log.e("tag"," Environment.getExternalStorageDirectory()---> ${Environment.getExternalStorageDirectory()}")
                        Log.e("tag"," Environment.getRootDirectory()---> ${Environment.getRootDirectory()}")
                        Log.e("tag"," Environment.getExternalStoragePublicDirectory()---> ${ Environment.getExternalStoragePublicDirectory("zip")}")
//                        val path = "/mnt/sdcard" + File.separator + "test" + File.separator + "db" + File.separator + "test.db"

                        val path  = (
                                "${File.separator}mnt${File.separator}sdcard${File.separator}db${File.separator}test.db")

                        val dbDir =
                            Environment.getExternalStorageDirectory().absolutePath
                        Log.e("tag","Environment dbDir--->$dbDir")
                        Log.e("tag","Environment path--->$path")
                        INSTANCE = Room.databaseBuilder(context!!,AppDatabase::class.java, path)
                            .addCallback(sOnOpenCallback)
                            .addMigrations(MIGRATION_1_2)
                            .allowMainThreadQueries()
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