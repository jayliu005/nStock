package com.example.nstock.chart

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.nstock.R
import com.example.nstock.data.StockResponse
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.lang.ref.WeakReference

object StockChartManager {

    private const val RIVER_NUMBER = 6
    private lateinit var stockData: StockResponse.Stock
    private lateinit var contextRef: WeakReference<Context>

    fun initChart(context: Context, chart: LineChart, stockData: StockResponse.Stock) {
        contextRef = WeakReference(context)
        this.stockData = stockData

        chart.description.isEnabled = false
        chart.setBackgroundColor(Color.WHITE)
        chart.setDrawGridBackground(false)

        val legend: Legend = chart.legend
        legend.isWordWrapEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)

        val rightAxis: YAxis = chart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val leftAxis: YAxis = chart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.isEnabled = false

        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
        xAxis.setDrawGridLines(false)
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                stockData.chartData.let { stockDetailList ->
                    val list = stockDetailList.reversed()
                    return list[value.toInt() % list.size].date
                }
            }
        }
        xAxis.position = XAxis.XAxisPosition.BOTTOM //X軸僅顯示下方

        val dataSets = java.util.ArrayList<ILineDataSet>()

        for (i in RIVER_NUMBER - 1 downTo 0) {
            dataSets.add(generatePELineData(i))
        }
        dataSets.add(generateStockLineData())

        val lineData = LineData(dataSets)

        xAxis.axisMaximum = lineData.xMax   //必須在資料產生好後設定
        chart.setVisibleXRange(0f, 20f)
        chart.data = lineData
        chart.invalidate()
    }

    private fun generateStockLineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData.chartData.let { stickDetailList ->

            val list = stickDetailList.reversed()
            for (index in list.indices) {
                entries.add(Entry(index.toFloat(), list[index].monthlyAveragePrice.toFloat()))
            }
        }

        val lineDataSet = LineDataSet(entries, "股價")
        contextRef.get()?.let {
            lineDataSet.color = ContextCompat.getColor(it, android.R.color.holo_red_light)
        }

        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.fillColor = Color.rgb(240, 238, 70)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT

        return lineDataSet
    }

    private fun generatePELineData(index: Int): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData.chartData.let { stockDetailList ->

            val list = stockDetailList.reversed()
            for (i in list.indices) {
                entries.add(Entry(i.toFloat(), list[i].peRatioBenchmark[index].toFloat()))
            }
        }

        val labelDescription =
            (stockData.peRatio[index]) + " 倍 " + (stockData.chartData[0].peRatioBenchmark[index])
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.lineWidth = 4f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)

        contextRef.get()?.let {
            var color: Int = Color.TRANSPARENT
            when (index) {
                0 -> color = ContextCompat.getColor(it, R.color.darkcyan)
                1 -> color = ContextCompat.getColor(it, R.color.cornflowerblue)
                2 -> color = ContextCompat.getColor(it, R.color.lightskyblue)
                3 -> color = ContextCompat.getColor(it, R.color.navajowhite)
                4 -> color = ContextCompat.getColor(it, R.color.peru)
                5 -> color = ContextCompat.getColor(it, R.color.salmon)
            }
            lineDataSet.color = color
            lineDataSet.fillColor = color
        }
        if (index == 0) lineDataSet.fillColor = Color.WHITE

        return lineDataSet
    }
}