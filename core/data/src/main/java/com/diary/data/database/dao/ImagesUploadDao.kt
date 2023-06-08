package com.diary.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.diary.data.database.entity.ImageToUpload

@Dao
interface ImagesUploadDao {

    @Query("SELECT * FROM IMAGES_UPLOAD_TABLE ORDER BY ID ASC")
    suspend fun getImages(): List<ImageToUpload>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageToUpload(imageToUpload: ImageToUpload)

    @Query("DELETE FROM IMAGES_UPLOAD_TABLE WHERE ID=:imageId")
    suspend fun deleteImage(imageId: Int)

}