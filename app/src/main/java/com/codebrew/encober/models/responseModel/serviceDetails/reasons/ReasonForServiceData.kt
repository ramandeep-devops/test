package com.codebrew.encober.models.responseModel.serviceDetails.reasons

import com.google.gson.annotations.SerializedName

data class ReasonForServiceData(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("listing")
	val listing: ArrayList<ReasonForServiceItem>? = null
)