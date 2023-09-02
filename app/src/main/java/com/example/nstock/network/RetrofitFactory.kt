package com.example.nstock.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {

    private const val timeout = 30L

    fun retrofit(
        baseUrl: String,
        interceptorList: List<Interceptor>? = null,
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient(interceptorList))
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun okHttpClient(
        interceptorList: List<Interceptor>? = null,
    ): OkHttpClient {

        val httpClientBuilder = OkHttpClient()
            .newBuilder()
            .followRedirects(true)
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)

        interceptorList?.forEach {
            httpClientBuilder.addInterceptor(it)
        }

        return httpClientBuilder.build()
    }
}