package com.codebrew.encober.models.responseModel.myServices

import com.google.gson.annotations.SerializedName

data class ServiceData(

	@field:SerializedName("serviceType")
	val serviceType: Int? = null,

	@field:SerializedName("loc")
	val loc: List<Double?>? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("lng")
	val lng: Double? = null,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("commissionPer")
	val commissionPer: Int? = null,

	@field:SerializedName("serviceName")
	val serviceName: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("servicePrice")
	val servicePrice: Int? = null,

	@field:SerializedName("countryCode")
	val countryCode: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("isAdded")
	val isAdded: Boolean? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
)