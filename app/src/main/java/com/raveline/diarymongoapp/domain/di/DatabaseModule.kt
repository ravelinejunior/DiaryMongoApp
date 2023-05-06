package com.raveline.diarymongoapp.domain.di

import android.content.Context
import androidx.room.Room
import com.raveline.diarymongoapp.common.utlis.Constants
import com.raveline.diarymongoapp.data.database.ImagesDatabase
import com.raveline.diarymongoapp.data.database.repository.ImagesUploadDao
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

}