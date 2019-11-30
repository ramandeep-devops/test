package com.codebrew.encober.models.responseModel.serviceDetails.comments

import com.google.gson.annotations.SerializedName

data class CommentsData(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("listing")
	val listing: ArrayList<CommentsDataItem>? = null
)