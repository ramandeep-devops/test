package com.codebrew.encober.models.responseModel.serviceDetails.comments

import com.google.gson.annotations.SerializedName

data class UserData(

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)