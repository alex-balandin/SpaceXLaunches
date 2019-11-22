package com.test.spacexlaunches.ui.details

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.test.spacexlaunches.R
import com.test.spacexlaunches.SpaceXLaunchesApp
import com.test.spacexlaunches.data.model.Launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-22
 */
class DetailsActivity : AppCompatActivity() {

    companion object {
        const val flightNumberExtraKey = "flightNumberExtraKey"
    }

    private val launchDateFormat: SimpleDateFormat
            = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    @Inject
    lateinit var viewModelFactory: DetailsViewModelFactory
    private lateinit var viewModel: DetailsViewModel

    private lateinit var logoView: ImageView
    private lateinit var flightNumberView: TextView
    private lateinit var missionNameView: TextView
    private lateinit var dateView: TextView
    private lateinit var upcomingOrPastView: TextView
    private lateinit var detailsView: TextView
    private lateinit var articleLinkView: TextView
    private lateinit var imagesContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        (application as SpaceXLaunchesApp).appComponent.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)

        val flightNumber = intent?.extras?.getInt(flightNumberExtraKey, -1) ?: -1

        logoView = findViewById(R.id.logo)
        flightNumberView = findViewById(R.id.flight_number)
        missionNameView = findViewById(R.id.mission_name)
        dateView = findViewById(R.id.date)
        upcomingOrPastView = findViewById(R.id.is_upcoming)
        detailsView = findViewById(R.id.details)
        articleLinkView = findViewById(R.id.article_link)
        imagesContainer = findViewById(R.id.images_container)

        observeData()
        viewModel.onDetailsViewCreated(flightNumber)
    }

    private fun observeData() {
        viewModel.getLaunchLiveData().observe(this, Observer {
            bindLaunchData(it)
        })

        viewModel.getProgressVisibilityData().observe(this, Observer {

        })
    }

    private fun bindLaunchData(launch: Launch) {
        flightNumberView.text = getString(
            R.string.launches_list_item_flight_number, launch.flightNumber.toString())
        missionNameView.text = getString(
            R.string.launches_list_item_mission_name, launch.missionName)

        if (launch.launchDateUnix != null) {
            dateView.text = launchDateFormat.format(Date(launch.launchDateUnix * 1000L))
        }

        if (launch.upcoming) {
            upcomingOrPastView.text = getString(R.string.launches_list_item_upcoming)
            upcomingOrPastView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
        } else {
            upcomingOrPastView.text = getString(R.string.launches_list_item_past)
            upcomingOrPastView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
        }

        Picasso.get()
            .load(launch.missionPatchSmall)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(logoView)

        if (launch.details != null) {
            detailsView.visibility = View.VISIBLE
            detailsView.text = launch.details
        } else {
            detailsView.visibility = View.GONE
        }

        if (launch.articleLink != null) {
            articleLinkView.visibility = View.VISIBLE
            articleLinkView.text = launch.articleLink
        } else {
            articleLinkView.visibility = View.GONE
        }

        //TODO implement gallery with recycler view:
        if (launch.flickrImages != null && launch.flickrImages.isNotEmpty()) {
            for (imageLink in launch.flickrImages) {
                val width = resources.getDimension(R.dimen.details_screen_image_size).toInt()
                val height = resources.getDimension(R.dimen.details_screen_image_size).toInt()
                val layoutParams = LinearLayout.LayoutParams(width, height)

                val margin = resources.getDimension(R.dimen.details_screen_image_margins).toInt()
                layoutParams.setMargins(margin, margin, margin, margin)
                layoutParams.gravity = Gravity.CENTER

                val imageView = ImageView(this)
                imageView.layoutParams = layoutParams
                imageView.setBackgroundColor(resources.getColor(android.R.color.darker_gray))

                imagesContainer.addView(imageView)

                Picasso.get()
                    .load(imageLink)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageView)
            }
        }
    }
}