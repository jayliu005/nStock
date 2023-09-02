package com.example.nstock.network

import com.example.nstock.data.StockResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIService {

    @GET("v2/per-river/interview")
    @Headers("Content-Type: application/json")
    suspend fun getStock(@Query("stock_id") stockId: Int): Response<StockResponse>

}