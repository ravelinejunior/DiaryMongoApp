package com.raveline.diarymongoapp.domain.di

import android.content.Context
import com.raveline.diarymongoapp.connectivity.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkConnectivityObserver(
        @ApplicationContext context: Context
    ): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context = context)

}