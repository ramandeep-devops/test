package com.codebrew.encober.models.responseModel.serviceDetails.finalizePayment

import com.codebrew.encober.models.responseModel.ImageUrl
import com.google.gson.annotations.SerializedName

data class FinalizePaymentData(

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("promiseDate")
	val promiseDate: Long? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("Signature")
	val signature: ImageUrl? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("serviceId")
	val serviceId: String? = null,

	@field:SerializedName("agreedAmount")
	val agreedAmount: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)