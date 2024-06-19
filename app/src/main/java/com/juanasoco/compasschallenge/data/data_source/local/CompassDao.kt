package com.juanasoco.compasschallenge.data.data_source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompassDao {
    @Query("SELECT content FROM compass_content WHERE id = 1")
    fun getCachedContent(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: CompassContent)
}
