package com.test.spacexlaunches.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.test.spacexlaunches.R
import com.test.spacexlaunches.ui.main.chart.ChartFragment
import com.test.spacexlaunches.ui.main.list.LaunchesListFragment
import java.lang.RuntimeException
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pagerAdapter = MainPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = pagerAdapter

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }

    class MainPagerAdapter(private val context: Context, fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> LaunchesListFragment()
                1 -> ChartFragment()
                else -> throw RuntimeException("wrong position!")
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> context.getString(R.string.launches_list_tab_title)
                1 -> context.getString(R.string.launches_chart_tab_title)
                else -> throw RuntimeException("wrong position!")
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
