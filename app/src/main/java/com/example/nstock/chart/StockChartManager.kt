package com.example.nstock.chart

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.nstock.R
import com.example.nstock.data.StockResponse
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.lang.ref.WeakReference

object StockChartManager {

    private lateinit var stockData: StockResponse.Stock
    private lateinit var contextRef: WeakReference<Context>

    fun initChart(context: Context, chart: CombinedChart, stockData: StockResponse.Stock) {
        contextRef = WeakReference(context)
        this.stockData = stockData

        chart.description.isEnabled = false
        chart.setBackgroundColor(Color.WHITE)
        chart.setDrawGridBackground(false)
        chart.setDrawBarShadow(false)
        chart.isHighlightFullBarEnabled = false

        chart.drawOrder = arrayOf(
            CombinedChart.DrawOrder.LINE
        )

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
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
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
        chart.setVisibleXRange(0f, 20f)
        chart.data = data
        chart.invalidate()
    }

    private fun generateStockLineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData.chartData.let { stickDetailList ->

            val list = stickDetailList.reversed()
            for (index in list.indices){
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

    private fun generatePE0LineData(): LineDataSet {
        val entries = ArrayList<Entry>()


        stockData.chartData.let { stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[0].toFloat()))
            }
        }

        val labelDescription = (stockData.peRatio[0]) + " 倍 " + (stockData.chartData[0].peRatioBenchmark[0])
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.lineWidth = 4f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = Color.WHITE
        contextRef.get()?.let {
            lineDataSet.color = ContextCompat.getColor(it, R.color.darkcyan)
        }

        return lineDataSet
    }

    private fun generatePE1LineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData.chartData.let { stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[1].toFloat()))
            }
        }

        val labelDescription = (stockData.peRatio[1]) + " 倍 " + (stockData.chartData[0].peRatioBenchmark[1])
        val lineDataSet = LineDataSet(entries, labelDescription)

        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        contextRef.get()?.let {
            lineDataSet.color = ContextCompat.getColor(it, R.color.royalblue)
            lineDataSet.fillColor = ContextCompat.getColor(it, R.color.royalblue)
        }


        return lineDataSet
    }

    private fun generatePE2LineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData.chartData.let { stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[2].toFloat()))
            }
        }

        val labelDescription = (stockData.peRatio[2]) + " 倍 " + (stockData.chartData[0].peRatioBenchmark[2])
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        contextRef.get()?.let {
            lineDataSet.color = ContextCompat.getColor(it, R.color.lightskyblue)
            lineDataSet.fillColor = ContextCompat.getColor(it, R.color.lightskyblue)
        }

        return lineDataSet
    }

    private fun generatePE3LineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData.chartData.let { stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[3].toFloat()))
            }
        }

        val labelDescription = (stockData.peRatio[3]) + " 倍 " + (stockData.chartData[0].peRatioBenchmark[3])
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        contextRef.get()?.let {
            lineDataSet.color = ContextCompat.getColor(it, R.color.navajowhite)
            lineDataSet.fillColor = ContextCompat.getColor(it, R.color.navajowhite)
        }

        return lineDataSet
    }

    private fun generatePE4LineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData.chartData.let {stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[4].toFloat()))
            }
        }

        val labelDescription = (stockData.peRatio[4]) + " 倍 " + (stockData.chartData[0].peRatioBenchmark[4])
        val lineDataSet = LineDataSet(entries, labelDescription)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        contextRef.get()?.let {
            lineDataSet.color = ContextCompat.getColor(it, R.color.peru)
            lineDataSet.fillColor = ContextCompat.getColor(it, R.color.peru)
        }

        return lineDataSet
    }

    private fun generatePE5LineData(): LineDataSet {
        val entries = ArrayList<Entry>()

        stockData.chartData.let {stockDetailList ->

            val list = stockDetailList.reversed()
            for (index in list.indices){
                entries.add(Entry(index.toFloat(), list[index].peRatioBenchmark[5].toFloat()))
            }
        }

        val labelDescription = (stockData.peRatio[5]) + " 倍 " + (stockData.chartData[0].peRatioBenchmark[5])
        val lineDataSet = LineDataSet(entries, labelDescription)

        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawFilled(true)
        contextRef.get()?.let{
            lineDataSet.color = ContextCompat.getColor(it, R.color.salmon)
            lineDataSet.fillColor = ContextCompat.getColor(it, R.color.salmon)
        }

        return lineDataSet
    }
}