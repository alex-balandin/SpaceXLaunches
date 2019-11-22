package com.test.spacexlaunches.ui.main.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.test.spacexlaunches.R
import com.test.spacexlaunches.SpaceXLaunchesApp
import com.test.spacexlaunches.ui.details.DetailsActivity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class LaunchesListFragment : Fragment() {

    private val lastUpdateDateFormat: SimpleDateFormat
            = SimpleDateFormat("dd/MM/yyyy kk:mm:ss", Locale.ENGLISH)

    @Inject
    lateinit var launchesListViewModelFactory: LaunchesListViewModelFactory
    private lateinit var viewModel: LaunchesListViewModel

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: View

    private lateinit var recyclerView: RecyclerView
    private lateinit var launchesListAdapter: LaunchesListAdapter

    private lateinit var lastUpdateTimeView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity!!.application as SpaceXLaunchesApp).appComponent.inject(this)
        viewModel = ViewModelProviders.of(this, launchesListViewModelFactory).get(LaunchesListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_launches_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lastUpdateTimeView = view.findViewById(R.id.last_update_time_text)
        lastUpdateTimeView.text = getString(R.string.launches_last_update_time_label, "")

        observeData()

        progressBar = view.findViewById(R.id.progress)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            viewModel.refreshLaunchesData()
        }

        recyclerView = view.findViewById(R.id.launches_recycler_view)
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        launchesListAdapter = LaunchesListAdapter(activity!!)
        recyclerView.adapter = launchesListAdapter

        launchesListAdapter.listItemClickListener = { flightNumber ->
            viewModel.onClickedListItem(flightNumber)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onLaunchesListViewCreated()

    }

    private fun observeData() {
        viewModel.getLaunchesLiveData().observe(this, Observer {
            launchesListAdapter.launches = it
        })

        viewModel.getProgressVisibilityData().observe(this, Observer { isProgress ->
            if (isProgress && !swipeRefreshLayout.isRefreshing) {
                progressBar.visibility = View.VISIBLE
            } else if (!isProgress) {
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
            }
        })

        viewModel.getLastUpdateTimeData().observe(this, Observer { timestamp ->
            val timeStr = lastUpdateDateFormat.format(Date(timestamp))
            val lastUpdateStr = getString(R.string.launches_last_update_time_label, timeStr)
            lastUpdateTimeView.text = lastUpdateStr
        })

        viewModel.getViewAction().observe(this, Observer { action ->
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (action) {
                LaunchesListViewModel.SimpleViewAction.SHOW_NETWORK_ERROR_MESSAGE -> {
                    Toast.makeText(activity, getString(R.string.network_error_message), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.getStartDetailsScreenAction().observe(this, Observer { flightNumber ->
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.flightNumberExtraKey, flightNumber)
            startActivity(intent)
        })
    }
}