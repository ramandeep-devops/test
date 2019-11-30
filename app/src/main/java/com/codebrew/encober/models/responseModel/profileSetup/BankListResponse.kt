package com.codebrew.encober.models.responseModel.profileSetup

import com.google.gson.annotations.SerializedName

data class BankListResponse(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("videosListing")
	val listing: ArrayList<BankListItem>? = null
)