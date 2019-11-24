package com.test.spacexlaunches.ui.main.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import com.test.spacexlaunches.R

/**
 * Created by alex-balandin on 2019-11-24
 */
class Chart : RelativeLayout, View.OnTouchListener {

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    var data: List<ChartItem> = emptyList()
        set(data) {
            field = data
            updateChart()
        }

    var chartItemClickListener: ((chartItem: ChartItem) -> Unit)? = null

    private val paint = Paint()

    private var chartColor = Color.GREEN

    private var axisLineWidth: Float = 0f
    private var viewPadding: Float = 0f
    private var chartItemPadding: Float = 0f
    private var yAxisLabelPadding: Float = 0f

    private var maxValue: Int = 0

    private var yAxisLabelX: Float = 0f
    private var yAxisLabelY: Float = 0f

    private var yAxisStartX: Float = 0f
    private var yAxisStartY: Float = 0f
    private var yAxisStopX: Float = 0f
    private var yAxisStopY: Float = 0f

    private var xAxisStartX: Float = 0f
    private var xAxisStartY: Float = 0f
    private var xAxisStopX: Float = 0f
    private var xAxisStopY: Float = 0f

    private var yAxisLabel: String = "1"

    private var chartDrawItems: MutableList<ChartDrawItem> = mutableListOf()

    private fun init() {
        minimumWidth = context.resources.displayMetrics.widthPixels

        paint.textSize = resources.getDimensionPixelSize(R.dimen.chart_text_size).toFloat()
        axisLineWidth = resources.getDimensionPixelSize(R.dimen.chart_axis_line_width).toFloat()
        viewPadding = resources.getDimensionPixelSize(R.dimen.chart_padding).toFloat()
        chartItemPadding = resources.getDimensionPixelSize(R.dimen.chart_item_padding).toFloat()
        yAxisLabelPadding = resources.getDimensionPixelSize(R.dimen.chart_y_axis_label_padding).toFloat()

        chartColor = resources.getColor(R.color.colorAccent)

        setOnTouchListener(this)
    }

    private fun updateChart() {
        if (layoutParams == null || data.isEmpty()) {
            return
        }

        maxValue = findMaxValue()
        yAxisLabel = if (maxValue > 0) maxValue.toString() else "1"

        val yAxisLabelWidth = paint.measureText(maxValue.toString())
        val labelHeight = paint.textSize

        val itemWidth = calcChartItemWidth()
        val requiredViewWidth = (itemWidth + chartItemPadding) * data.size + chartItemPadding +
                viewPadding * 2 + yAxisLabelWidth + axisLineWidth
        layoutParams.width = if (requiredViewWidth > minimumWidth) {
            requiredViewWidth.toInt()
        } else {
            minimumWidth
        }

        yAxisLabelX = viewPadding
        yAxisLabelY = viewPadding + paint.textSize

        yAxisStartX = viewPadding + yAxisLabelWidth + yAxisLabelPadding
        yAxisStartY = viewPadding
        yAxisStopX = yAxisStartX
        yAxisStopY = layoutParams.height - viewPadding - labelHeight

        xAxisStartX = viewPadding + yAxisLabelWidth + yAxisLabelPadding
        xAxisStartY = layoutParams.height - viewPadding - labelHeight
        xAxisStopX = layoutParams.width - viewPadding
        xAxisStopY = xAxisStartY

        chartDrawItems = mutableListOf()
        for ((index, chartItem) in data.withIndex()) {
            val itemX = xAxisStartX + chartItemPadding + (itemWidth + chartItemPadding) * index
            //centralize label:
            val labelX = itemX + (itemWidth - paint.measureText(chartItem.label)) / 2

            val labelY = layoutParams.height.toFloat() - viewPadding

            val rectLeft = itemX
            val rectRight = itemX + itemWidth
            val rectBottom = xAxisStartY - axisLineWidth / 2

            val rectMaxPossibleHeight = rectBottom - viewPadding - labelHeight / 2
            val rectHeight = (chartItem.value.toFloat() / maxValue) * rectMaxPossibleHeight
            val rectTop = rectBottom - rectHeight

            val rect = RectF(rectLeft, rectTop, rectRight, rectBottom)
            chartDrawItems.add(ChartDrawItem(chartItem, labelX, labelY, rect))
        }

        requestLayout()
        invalidate()
    }

    private fun findMaxValue(): Int {
        var maxValue = 0
        for (chartItem in data) {
            if (chartItem.value > maxValue) {
                maxValue = chartItem.value
            }
        }
        return maxValue
    }

    private fun calcChartItemWidth(): Float {
        // it will be equal to width of longest label
        var maxLabelWidth = 0f
        for (chartItem in data) {
            val labelWidth = paint.measureText(chartItem.label)
            if (labelWidth > maxLabelWidth) {
                maxLabelWidth = labelWidth
            }
        }
        return maxLabelWidth + chartItemPadding
    }

    override fun onDraw(canvas: Canvas?) {
        if (data.isEmpty()) {
            return
        }

        paint.color = Color.WHITE
        paint.strokeWidth = axisLineWidth

        canvas?.drawLine(yAxisStartX, yAxisStartY, yAxisStopX, yAxisStopY, paint)

        canvas?.drawLine(xAxisStartX, xAxisStartY, xAxisStopX, xAxisStopY, paint)

        canvas?.drawText("0", viewPadding, height.toFloat() - viewPadding, paint)

        canvas?.drawText(yAxisLabel, yAxisLabelX, yAxisLabelY, paint)

        for (item in chartDrawItems) {
            paint.color = Color.WHITE
            canvas?.drawText(item.chartItem.label, item.labelX, item.labelY, paint)

            paint.color = chartColor
            canvas?.drawRect(item.rect, paint)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (MotionEvent.ACTION_UP == event?.action) {
            handleClick(event.x)
        }
        return true
    }

    private fun handleClick(x: Float) {
        for (item in chartDrawItems) {
            if (x >= item.rect.left && x <= item.rect.right) {
                chartItemClickListener?.invoke(item.chartItem)
            }
        }
    }

    data class ChartItem(
        val value: Int,
        val label: String
    )

    private data class ChartDrawItem(
        val chartItem: ChartItem,
        val labelX: Float,
        val labelY: Float,
        val rect: RectF
    )
}