package com.example.nstock.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nstock.Config
import com.example.nstock.chart.StockChartManager
import com.example.nstock.databinding.ActivityMainBinding
import com.example.nstock.viewmodel.MainViewModel
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