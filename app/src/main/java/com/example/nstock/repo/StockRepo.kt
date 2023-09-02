package com.example.nstock.repo

import com.example.nstock.data.StockResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface StockRepo {
    fun getStock(stockId: Int): Flow<Response<StockResponse>>
}