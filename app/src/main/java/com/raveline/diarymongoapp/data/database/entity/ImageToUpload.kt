package com.raveline.diarymongoapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raveline.diarymongoapp.common.utlis.Constants.IMAGES_UPLOAD_TABLE

@Entity(tableName = IMAGES_UPLOAD_TABLE)
data class ImageToUpload(

    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val remoteImagePath:String,
    val imageUri:String,
    val sessionUri:String

)
