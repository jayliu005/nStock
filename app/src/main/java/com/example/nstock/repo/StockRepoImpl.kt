package com.example.nstock.repo

import com.example.nstock.data.StockResponse
import com.example.nstock.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class StockRepoImpl @Inject constructor(private val apiService: APIService) : StockRepo {

    override fun getStock(stockId: Int): Flow<Response<StockResponse>> = flow {
        emit(apiService.getStock(stockId))
    }
        .flowOn(Dispatchers.IO)
}