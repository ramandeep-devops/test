package com.codebrew.encober.models.responseModel.serviceDetails.happenOnVisit

import com.google.gson.annotations.SerializedName

data class HappenOnVisitData(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("listing")
	val listing: ArrayList<HappenOnVisitItem>? = null
)