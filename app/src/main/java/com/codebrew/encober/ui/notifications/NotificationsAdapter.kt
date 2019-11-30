package com.codebrew.encober.ui.notifications

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.videosData.VideoListingItem


class NotificationsAdapter(/*private val listVideos: ArrayList<VideoListingItem?>*/) :
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v1 = View.inflate(parent.context, R.layout.item_notifications, null)
        return ViewHolder(v1)
    }

    override fun getItemCount(): Int {
//        return listVideos.size
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(listVideos[position])
    }


    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        init {

        }

        fun bind(data: VideoListingItem?) = with(itemView) {

        }
    }


}
