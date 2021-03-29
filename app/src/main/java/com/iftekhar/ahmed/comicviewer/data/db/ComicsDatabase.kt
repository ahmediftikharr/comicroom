/*
 * Developed by Michail Fotiadis.
 * Copyright (c) 2018.
 * All rights reserved.
 */

package com.michaelfotiads.xkcdreader.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.michaelfotiads.xkcdreader.data.db.dao.ComicsDao
import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity

@Database(
    entities = [ComicEntity::class],
    version = 2
)
abstract class ComicsDatabase : RoomDatabase() {

    abstract fun comicsDao(): ComicsDao


    companion object {
        @Volatile
        private var INSTANCE: ComicsDatabase? = null

        fun getInstance(context: Context): ComicsDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): ComicsDatabase {
            return Room.databaseBuilder(context, ComicsDatabase::class.java, "app-db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }


}
