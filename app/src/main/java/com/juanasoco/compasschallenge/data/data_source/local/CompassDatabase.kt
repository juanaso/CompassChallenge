package com.juanasoco.compasschallenge.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CompassContent::class], version = 1)
abstract class CompassDatabase : RoomDatabase() {
    abstract fun compassDao(): CompassDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}
