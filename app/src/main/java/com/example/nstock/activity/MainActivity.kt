package com.example.nstock.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.nstock.Config
import com.example.nstock.R
import com.example.nstock.chart.StockChartManager
import com.example.nstock.data.StockResponse
import com.example.nstock.databinding.ActivityMainBinding
import com.example.nstock.viewmodel.MainViewModel
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        initViewModel()
    }

    private fun initViewModel() {

        mainViewModel.getStock(Config.STOCK_TSMC).observe(this) { response ->
            val stockData = response.body()?.data?.get(0)
            stockData?.let {
                StockChartManager.initChart(this, binding.chart, it)
            }
        }
    }
}