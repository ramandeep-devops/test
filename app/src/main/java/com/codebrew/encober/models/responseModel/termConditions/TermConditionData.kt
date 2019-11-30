package com.codebrew.encober.models.responseModel.termConditions

import com.google.gson.annotations.SerializedName

data class TermConditionData(

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: Int? = null
)