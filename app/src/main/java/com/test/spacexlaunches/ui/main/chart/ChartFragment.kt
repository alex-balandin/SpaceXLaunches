package com.test.spacexlaunches.ui.main.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.test.spacexlaunches.R
import com.test.spacexlaunches.SpaceXLaunchesApp
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class ChartFragment : Fragment() {

    @Inject
    lateinit var chartViewModelFactory: ChartViewModelFactory
    private lateinit var viewModel: ChartViewModel

    private lateinit var chart: Chart
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity!!.application as SpaceXLaunchesApp).appComponent.inject(this)
        viewModel = ViewModelProviders.of(this, chartViewModelFactory).get(ChartViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progress)

        chart = view.findViewById(R.id.chart)
        chart.chartItemClickListener = { chartItem ->
            Toast.makeText(activity, "$chartItem", Toast.LENGTH_SHORT).show()
        }

        observeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onChartViewResumed()
    }

    private fun observeData() {
        viewModel.getLaunchesLiveData().observe(this, Observer {

            val chartItems: List<Chart.ChartItem> = listOf(
                Chart.ChartItem(1, "Jan 2019"),
                Chart.ChartItem(0, "Feb 2019"),
                Chart.ChartItem(5, "Mar 2019"),
                Chart.ChartItem(1, "Apr 2019"),
                Chart.ChartItem(2, "May 2019"),
                Chart.ChartItem(0, "Jun 2019"),
                Chart.ChartItem(1, "Jul 2019"),
                Chart.ChartItem(3, "Aug 2019"),
                Chart.ChartItem(1, "Sep 2019"))
            chart.data = chartItems
        })

        viewModel.getProgressVisibilityData().observe(this, Observer {isVisible ->
            if (isVisible) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }
}