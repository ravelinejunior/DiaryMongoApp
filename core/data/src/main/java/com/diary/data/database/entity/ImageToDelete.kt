package com.diary.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raveline.diary.util.Constants

@Entity(tableName = Constants.IMAGES_TO_DELETE_TABLE)
data class ImageToDelete(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteImagePath: String
)
