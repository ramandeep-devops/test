package com.codebrew.encober.models.responseModel.serviceDetails

import com.google.gson.annotations.SerializedName

data class AddRemoveServiceData(

	@field:SerializedName("isAdded")
	val isAdded: Boolean? = null
)