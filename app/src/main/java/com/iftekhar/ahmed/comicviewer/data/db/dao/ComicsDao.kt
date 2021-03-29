/*
 * Developed by Michail Fotiadis.
 * Copyright (c) 2018.
 * All rights reserved.
 */

package com.michaelfotiads.xkcdreader.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*

import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity

@Dao
interface ComicsDao {

    @Insert
    suspend fun insert(comic: ComicEntity)

    @Insert
    suspend fun insert(comics: List<ComicEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(comic: ComicEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(comics: List<ComicEntity>)

    @Query("DELETE FROM comics_table")
    suspend fun deleteAll()

    @Query("DELETE FROM comics_table WHERE num = :num")
    suspend fun deleteById(num: Int)

    @Query("SELECT * from comics_table ORDER BY num DESC")
    suspend fun getAllComics(): List<ComicEntity>

    @Query("SELECT * from comics_table  ORDER BY num DESC")
    suspend fun getAllFavouritesLiveData(): List<ComicEntity>

    @Query("SELECT * FROM comics_table where num = :num")
    suspend fun getForId(num: Int): ComicEntity?
}
