package com.juanasoco.compasschallenge.data.data_source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compass_content")
data class CompassContent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1,
    val content: String
)