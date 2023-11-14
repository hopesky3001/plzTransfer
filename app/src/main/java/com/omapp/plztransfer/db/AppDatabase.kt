package com.omapp.plztransfer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(TFEntity::class), version = 1)
abstract class AppDatabase :RoomDatabase() {

    abstract fun getTFDao() : TFDao

    companion object{
        val databaseName = "db_tf" // db의 이름
        var appDatabase : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase? {
            if(appDatabase == null){
                appDatabase = Room.databaseBuilder(context,
                    AppDatabase::class.java,
                    databaseName).fallbackToDestructiveMigration().build()
            }
            return appDatabase
        }
    }
}