package com.test.spacexlaunches.ui.details

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val permissionsRequestCode = 123

    private val launchDateFormat: SimpleDateFormat
            = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    @Inject
    lateinit var viewModelFactory: DetailsViewModelFactory
    private lateinit var viewModel: DetailsViewModel

    private lateinit var progressBar: View
    private lateinit var logoView: ImageView
    private lateinit var flightNumberView: TextView
    private lateinit var missionNameView: TextView
    private lateinit var rocketNameView: TextView
    private lateinit var dateView: TextView
    private lateinit var upcomingOrPastView: TextView
    private lateinit var detailsView: TextView
    private lateinit var articleLinkView: TextView

    private lateinit var galleryRecyclerView: RecyclerView
    private lateinit var galleryAdapter: GalleryAdapter

    private var currentLaunch: Launch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        (application as SpaceXLaunchesApp).appComponent.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)

        val flightNumber = intent?.extras?.getInt(flightNumberExtraKey, -1) ?: -1

        progressBar = findViewById(R.id.progress)
        logoView = findViewById(R.id.logo)
        flightNumberView = findViewById(R.id.flight_number)
        missionNameView = findViewById(R.id.mission_name)
        rocketNameView = findViewById(R.id.rocket_name)
        dateView = findViewById(R.id.date)
        upcomingOrPastView = findViewById(R.id.is_upcoming)
        detailsView = findViewById(R.id.details)
        articleLinkView = findViewById(R.id.article_link)

        galleryRecyclerView = findViewById(R.id.gallery_recycler_view)
        galleryRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        galleryAdapter = GalleryAdapter()
        galleryRecyclerView.adapter = galleryAdapter

        galleryAdapter.downloadIconClickListener = { url ->
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val targetPath =
                    Environment.getExternalStorageDirectory().toString() + "/SpaceXLaunches" + "/${currentLaunch?.flightNumber.toString()}_${currentLaunch?.missionName}"

                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.download_image_confirmation_question, targetPath))
                    .setPositiveButton(R.string.download) { _, _ ->
                        viewModel.downloadAndSaveImage(url, targetPath)
                    }
                    .setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .create().show()
            } else {
                requestStoragePermission()
            }
        }

        observeData()
        viewModel.onDetailsViewCreated(flightNumber)
    }

    private fun observeData() {
        viewModel.getLaunchLiveData().observe(this, Observer {
            bindLaunchData(it)
        })

        viewModel.getProgressVisibilityData().observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.getViewAction().observe(this, Observer { action ->
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (action) {
                DetailsViewModel.SimpleViewAction.SHOW_SAVE_IMAGE_SUCCESS_MESSAGE -> {
                    Toast.makeText(
                        this,
                        getString(R.string.image_successfully_saved_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                DetailsViewModel.SimpleViewAction.SHOW_SAVE_IMAGE_ERROR_MESSAGE -> {
                    Toast.makeText(
                        this,
                        getString(R.string.failed_to_save_image_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun bindLaunchData(launch: Launch) {
        currentLaunch = launch

        flightNumberView.text = getString(
            R.string.launch_flight_number, launch.flightNumber.toString())
        missionNameView.text = getString(
            R.string.launch_mission_name, launch.missionName)
        rocketNameView.text = getString(
            R.string.launch_rocket_name, launch.rocketName)

        if (launch.launchDateUnix != null) {
            dateView.text = launchDateFormat.format(Date(launch.launchDateUnix * 1000L))
        }

        if (launch.upcoming) {
            upcomingOrPastView.text = getString(R.string.launch_upcoming)
            upcomingOrPastView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
        } else {
            upcomingOrPastView.text = getString(R.string.launch_past)
            upcomingOrPastView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
        }

        Picasso.get()
            .load(launch.missionPatchSmall)
            .placeholder(R.drawable.images_placeholder)
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

        if (launch.flickrImages != null && launch.flickrImages.isNotEmpty()) {
            galleryRecyclerView.visibility = View.VISIBLE
            galleryAdapter.imageUrls = launch.flickrImages
        } else {
            galleryRecyclerView.visibility = View.GONE
        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            permissionsRequestCode)
    }
}