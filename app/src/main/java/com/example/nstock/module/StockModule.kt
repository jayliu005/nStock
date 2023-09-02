package com.example.nstock.module

import com.example.nstock.network.APIService
import com.example.nstock.repo.StockRepo
import com.example.nstock.repo.StockRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class StockModule {

    @Singleton
    @Provides
    fun provideStockRepo(apiService: APIService): StockRepo = StockRepoImpl(apiService)
}