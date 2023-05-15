package com.raveline.diarymongoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raveline.diarymongoapp.data.database.dao.ImagesToDeleteDao
import com.raveline.diarymongoapp.data.database.dao.ImagesUploadDao
import com.raveline.diarymongoapp.data.database.entity.ImageToDelete
import com.raveline.diarymongoapp.data.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class, ImageToDelete::class],
    version = 2,
    exportSchema = false
)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageUploadDao(): ImagesUploadDao
    abstract fun imagesToDeleteDao(): ImagesToDeleteDao
}