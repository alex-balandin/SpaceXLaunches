package com.test.spacexlaunches.ui.main.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.test.spacexlaunches.R
import com.test.spacexlaunches.SpaceXLaunchesApp
import com.test.spacexlaunches.ui.main.MainViewModel
import com.test.spacexlaunches.ui.main.MainViewModelFactory
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class LaunchesListFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel

    lateinit var testView: TextView
    lateinit var lastUpdateTimeView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity!!.application as SpaceXLaunchesApp).appComponent.inject(this)
        viewModel = ViewModelProviders.of(activity!!, mainViewModelFactory).get(MainViewModel::class.java)
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

        testView = view.findViewById(R.id.test_view)
        lastUpdateTimeView = view.findViewById(R.id.last_update_time_text)

        observeData()
        viewModel.onLaunchesListViewCreated()
    }

    private fun observeData() {
        viewModel.getLaunchesLiveData().observe(this, Observer {
            testView.text = it.toString()
        })

        viewModel.getLastUpdateTimestamp().observe(this, Observer {
            val lastUpdateStr = getString(R.string.launches_last_update_time_label, it.toString())
            lastUpdateTimeView.text = lastUpdateStr
        })
    }
}