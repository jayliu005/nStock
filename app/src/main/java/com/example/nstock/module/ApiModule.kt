package com.example.nstock.module

import com.example.nstock.network.APIService
import com.example.nstock.network.NetworkConfig
import com.example.nstock.network.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideApiService(): APIService = RetrofitFactory.retrofit(NetworkConfig.API_IRL, provideInterceptors()).create(APIService::class.java)

    @Provides
    fun provideInterceptors(): List<Interceptor> = listOf(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
}