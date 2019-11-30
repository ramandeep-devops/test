package com.codebrew.encober.ui.myVideos

import com.codebrew.encober.models.responseModel.videosData.VideoListingItem


interface OnClickYoutubeVideoCallback {
    fun onClickYoutube(data:VideoListingItem)
}