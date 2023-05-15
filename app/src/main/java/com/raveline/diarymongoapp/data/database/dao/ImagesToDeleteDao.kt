package com.raveline.diarymongoapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raveline.diarymongoapp.data.database.entity.ImageToDelete

@Dao
interface ImagesToDeleteDao {

    @Query("SELECT * FROM IMAGES_TO_DELETE_TABLE ORDER BY ID ASC")
    suspend fun getImagesToDelete(): List<ImageToDelete>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageToDelete(imageToDelete: ImageToDelete)

    @Query("DELETE FROM IMAGES_TO_DELETE_TABLE WHERE ID =:imageId")
    suspend fun deleteImage(imageId: Int)

}