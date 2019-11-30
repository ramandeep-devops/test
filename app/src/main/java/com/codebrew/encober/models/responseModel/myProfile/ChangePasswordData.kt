package com.codebrew.encober.models.responseModel.myProfile

import com.google.gson.annotations.SerializedName

data class ChangePasswordData(

	@field:SerializedName("customMessage")
	val customMessage: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)