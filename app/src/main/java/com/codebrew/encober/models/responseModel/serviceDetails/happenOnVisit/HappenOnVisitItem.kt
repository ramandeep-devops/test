package com.codebrew.encober.models.responseModel.serviceDetails.happenOnVisit

import com.google.gson.annotations.SerializedName

data class HappenOnVisitItem(

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("nameSp")
	val nameSp: String? = null,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("nameEn")
	val nameEn: String? = null,

	@field:SerializedName("order")
	val order: Int? = null,


	var isSelected : Boolean = false
)