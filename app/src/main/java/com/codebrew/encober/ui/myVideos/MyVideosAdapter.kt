package com.codebrew.encober.ui.myVideos

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.videosData.VideoListingItem
import com.codebrew.encober.utils.extensions.loadImage
import kotlinx.android.synthetic.main.item_youtube_videos.view.*

class MyVideosAdapter(private val callback: OnClickYoutubeVideoCallback) : PagedListAdapter<VideoListingItem, MyVideosAdapter.ViewHolder>
    (VideoListingItem.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_youtube_videos, null))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        fun bind(data: VideoListingItem?) = with(itemView) {

            tvItemVideoName.text = data?.videoName
            tvItemVideoDescription.text = data?.description
            loadImage(context,ivItemVideo,data?.videoImage,data?.videoImage)

            itemView.setOnClickListener {
                callback.onClickYoutube(data?:VideoListingItem())
            }
        }

    }


}
