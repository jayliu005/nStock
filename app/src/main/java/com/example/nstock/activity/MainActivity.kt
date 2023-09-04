package com.example.nstock.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.nstock.Config
import com.example.nstock.R
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
    private var stockData: StockResponse.Stock? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        initViewModel()
    }

    private fun initViewModel() {

        mainViewModel.getStock(Config.STOCK_TSMC).observe(this) { response ->
            stockData = response.body()?.data?.get(0)
            initChart()
        }
    }

    private fun initChart() {
        binding.chart.description.isEnabled = false
        binding.chart.setBackgroundColor(Color.WHITE)
        binding.chart.setDrawGridBackground(false)
        binding.chart.setDrawBarShadow(false)
        binding.chart.isHighlightFullBarEnabled = false

        binding.chart.drawOrder = arrayOf(
            DrawOrder.LINE
        )

        val legend: Legend = binding.chart.legend
        legend.isWordWrapEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)

        val rightAxis: YAxis = binding.chart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val leftAxis: YAxis = binding.chart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val xAxis: XAxis = binding.chart.xAxis
        xAxis.position = XAxisPosition.BOTH_SIDED
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {

                stockData?.chartData?.let {stockDetailList ->
                    val list = stockDetailList.reversed()
                    return list[value.toInt() % list.size].date
                }
                return "N/A"
            }
        }
        xAxis.position = XAxisPosition.BOTTOM //X軸僅顯示下方

        val dataSets = java.util.ArrayList<ILineDataSet>()

        dataSets.add(generatePE5LineData())
        dataSets.add(generatePE4LineData())
        dataSets.add(generatePE3LineData())
        dataSets.add(generatePE2LineData())
        dataSets.add(generatePE1LineData())
        dataSets.add(generatePE0LineData())

        dataSets.add(generateStockLineData())

        val lineData = LineData(dataSets)
        val data = CombinedData()
        data.setData(lineData)

        xAxis.axisMaximum = data.xMax   //必須在資料產生好後設定
        binding.chart.setVisibleXRange(0f, 20f)
        binding.chart.data = data
//        binding.chart.setGridBackgroundColor(Color.WHITE)
        binding.chart.invalidate()
    }

    private fun generateStockLineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData?.chartData?.let {stickDetailList ->

            val list = stickDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].monthlyAveragePrice.toFloat()))
            }
        }

        val lineDataSet = LineDataSet(entries, "股價")
        lineDataSet.color = ContextCompat.getColor(this, android.R.color.holo_red_light)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.fillColor = Color.rgb(240, 238, 70)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT


        return lineDataSet
    }

    private fun generatePE0LineData(): LineDataSet {
        val entries = ArrayList<Entry>()


        stockData?.chartData?.let {stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[0].toFloat()))
            }
        }

        val labelDescription = (stockData?.peRatio?.get(0) ?: "") + " 倍 " + (stockData?.chartData?.get(0)?.peRatioBenchmark?.get(0) ?: "")
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.color = ContextCompat.getColor(this, R.color.darkcyan)
        lineDataSet.lineWidth = 4f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = Color.WHITE

        return lineDataSet
    }

    private fun generatePE1LineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData?.chartData?.let {stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[1].toFloat()))
            }
        }

        val labelDescription = (stockData?.peRatio?.get(1) ?: "") + " 倍 " + (stockData?.chartData?.get(0)?.peRatioBenchmark?.get(1) ?: "")
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.color = ContextCompat.getColor(this, R.color.royalblue)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ContextCompat.getColor(this, R.color.royalblue)

        return lineDataSet
    }

    private fun generatePE2LineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData?.chartData?.let {stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[2].toFloat()))
            }
        }

        val labelDescription = (stockData?.peRatio?.get(2) ?: "") + " 倍 " + (stockData?.chartData?.get(0)?.peRatioBenchmark?.get(2) ?: "")
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.color = ContextCompat.getColor(this, R.color.lightskyblue)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ContextCompat.getColor(this, R.color.lightskyblue)

        return lineDataSet
    }

    private fun generatePE3LineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData?.chartData?.let {stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[3].toFloat()))
            }
        }

        val labelDescription = (stockData?.peRatio?.get(3) ?: "") + " 倍 " + (stockData?.chartData?.get(0)?.peRatioBenchmark?.get(3) ?: "")
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.color = ContextCompat.getColor(this, R.color.navajowhite)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ContextCompat.getColor(this, R.color.navajowhite)

        return lineDataSet
    }

    private fun generatePE4LineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData?.chartData?.let {stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[4].toFloat()))
            }
        }

        val labelDescription = (stockData?.peRatio?.get(4) ?: "") + " 倍 " + (stockData?.chartData?.get(0)?.peRatioBenchmark?.get(4) ?: "")
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.color = ContextCompat.getColor(this, R.color.peru)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ContextCompat.getColor(this, R.color.peru)

        return lineDataSet
    }

    private fun generatePE5LineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData?.chartData?.let {stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[5].toFloat()))
            }
        }

        val labelDescription = (stockData?.peRatio?.get(5) ?: "") + " 倍 " + (stockData?.chartData?.get(0)?.peRatioBenchmark?.get(5) ?: "")
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.color = ContextCompat.getColor(this, R.color.salmon)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ContextCompat.getColor(this, R.color.salmon)

        return lineDataSet
    }
}