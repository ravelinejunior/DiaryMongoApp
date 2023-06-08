package com.raveline.diarymongoapp.domain.di

import android.content.Context
import androidx.room.Room
import com.diary.data.database.ImagesDatabase
import com.diary.data.database.dao.ImagesToDeleteDao
import com.diary.data.database.dao.ImagesUploadDao
import com.raveline.diary.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ImagesDatabase =
        Room.databaseBuilder(
            context = context,
            klass = ImagesDatabase::class.java,
            name = Constants.MONGO_LOCAL_DATABASE
        ).fallbackToDestructiveMigrationOnDowngrade()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideImageUploadDao(database: ImagesDatabase): ImagesUploadDao = database.imageUploadDao()

    @Provides
    @Singleton
    fun provideImageToDeleteDao(database: ImagesDatabase): ImagesToDeleteDao =
        database.imagesToDeleteDao()

}