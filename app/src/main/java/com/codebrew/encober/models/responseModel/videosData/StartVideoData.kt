package com.codebrew.encober.models.responseModel.videosData

import com.google.gson.annotations.SerializedName

data class StartVideoData(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("listing")
	val listing: ArrayList<VideoListingItem?>? = null
)