package com.codebrew.encober.models.responseModel.serviceDetails.reasons

import com.google.gson.annotations.SerializedName

data class ReasonForServiceItem(

	@field:SerializedName("serviceType")
	val serviceType: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("questionSp")
	val questionSp: String? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("serviceId")
	val serviceId: String? = null,

	@field:SerializedName("questionEn")
	val questionEn: String? = null,

	var isSelected : Boolean = false

	)