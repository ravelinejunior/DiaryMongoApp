package com.raveline.diarymongoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raveline.diarymongoapp.data.database.entity.ImageToUpload
import com.raveline.diarymongoapp.data.database.repository.ImagesUploadDao

@Database(
    entities = [ImageToUpload::class],
    version = 1,
    exportSchema = true
)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageUploadDao():ImagesUploadDao
}