package com.test.spacexlaunches.ui.main.chart

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
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
            val message = if (chartItem.value == 1) {
                getString(R.string.single_launch_chart_hint, chartItem.label)
            } else {
                getString(R.string.multiple_launches_chart_hint, chartItem.value, chartItem.label)
            }

            val hintView = layoutInflater.inflate(R.layout.v_chart_hint, null)
            hintView.findViewById<TextView>(R.id.message).text = message

            AlertDialog.Builder(activity, R.style.ChartDialogTheme)
                .setView(hintView)
                .show()
        }

        observeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onChartViewResumed()
    }

    private fun observeData() {
        viewModel.getChartItemsData().observe(this, Observer {
            chart.data = it
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