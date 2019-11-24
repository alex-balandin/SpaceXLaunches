package com.test.spacexlaunches.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.spacexlaunches.R

/**
 * Created by alex-balandin on 2019-11-22
 */
class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    var imageUrls: List<String> = emptyList()
        set(urls) {
            field = urls
            notifyDataSetChanged()
        }

    var downloadIconClickListener: ((url: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.v_gallery_item, parent, false)
        return GalleryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val url = imageUrls[position]

        Picasso.get()
            .load(url)
            .placeholder(R.drawable.images_placeholder)
            .into(holder.image)

        holder.downloadIcon.setOnClickListener {
            downloadIconClickListener?.invoke(url)
        }
    }

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val downloadIcon: ImageView = itemView.findViewById(R.id.ic_download)
    }
}