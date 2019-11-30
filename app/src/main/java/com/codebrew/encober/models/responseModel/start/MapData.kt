package com.codebrew.encober.models.responseModel.start

import com.google.gson.annotations.SerializedName

data class MapData(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("listing")
	val listing: List<ServiceListItem>? = null
)