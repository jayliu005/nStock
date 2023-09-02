package com.example.nstock.data

import com.google.gson.annotations.SerializedName

data class StockResponse(
    @SerializedName("data") val data:List<Stock>
) {

    data class Stock(
        @SerializedName("股票代號") val id: String,
        @SerializedName("股票名稱") val name: String,
        @SerializedName("本益比基準") val peRatio: List<String>,
        @SerializedName("本淨比基準") val pbRatio: List<String>,
        @SerializedName("河流圖資料") val chartData: List<StockDetail>
    )

    data class StockDetail(
        @SerializedName("年月") val date: String,
        @SerializedName("月平均收盤價") val monthlyAveragePrice: String,
        @SerializedName("近四季EPS") val eps4Seasons: String,
        @SerializedName("月近四季本益比") val pe4Seasons: String,
        @SerializedName("本益比股價基準") val peRatioBenchmark: List<String>,
        @SerializedName("近一季BPS") val lastQuarterBPS: String,
        @SerializedName("月近一季本淨比") val pbMonthlyQuarterlyRatio: String,
        @SerializedName("本淨比股價基準") val pbRatioBenchmark: List<String>,
        @SerializedName("平均本益比") val peAverage: String,
        @SerializedName("平均本淨比") val pbAverage: String,
        @SerializedName("近3年年複合成長") val growthIn3Years: String,
    )

}
