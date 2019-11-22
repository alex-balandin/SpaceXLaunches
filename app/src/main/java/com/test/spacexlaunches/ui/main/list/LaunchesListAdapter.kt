package com.test.spacexlaunches.ui.main.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.spacexlaunches.R
import com.test.spacexlaunches.data.model.Launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by alex-balandin on 2019-11-22
 */
class LaunchesListAdapter(private val context: Context) :
    RecyclerView.Adapter<LaunchesListAdapter.LaunchesViewHolder>() {

    private val launchDateFormat: SimpleDateFormat
            = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    var launches: List<Launch> = emptyList()
        set(launches) {
            field = launches
            notifyDataSetChanged()
        }

    var listItemClickListener: ((flightNumber: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var view = inflater.inflate(R.layout.v_launches_list_item, parent, false)
        return LaunchesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return launches.size
    }

    override fun onBindViewHolder(holder: LaunchesViewHolder, position: Int) {
        val launch = launches[position]
        holder.flightNumberView.text = context.getString(
            R.string.launches_list_item_flight_number, launch.flightNumber.toString())
        holder.missionNameView.text = context.getString(
            R.string.launches_list_item_mission_name, launch.missionName)

        if (launch.launchDateUnix != null) {
            holder.dateView.text = launchDateFormat.format(Date(launch.launchDateUnix * 1000L))
        }

        if (launch.upcoming) {
            holder.isUpcomingView.text =  context.getString(R.string.launches_list_item_upcoming)
            holder.isUpcomingView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_light))
        } else {
            holder.isUpcomingView.text =  context.getString(R.string.launches_list_item_past)
            holder.isUpcomingView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_light))
        }

        Picasso.get()
            .load(launch.missionPatchSmall)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.logo)

        holder.itemView.setOnClickListener {
            listItemClickListener?.invoke(launch.flightNumber)
        }
    }

    class LaunchesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flightNumberView: TextView = itemView.findViewById(R.id.flight_number)
        val missionNameView: TextView = itemView.findViewById(R.id.mission_name)
        val dateView: TextView = itemView.findViewById(R.id.date)
        val isUpcomingView: TextView = itemView.findViewById(R.id.is_upcoming)
        val logo: ImageView = itemView.findViewById(R.id.logo)
    }
}