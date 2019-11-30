package com.codebrew.encober.models.responseModel.profileSetup

import com.codebrew.encober.models.responseModel.ImageUrl
import com.google.gson.annotations.SerializedName

data class ProfileUpdateResponse(

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("selectedService")
	val selectedService: List<Any?>? = null,

	@field:SerializedName("postalCode")
	val postalCode: String? = null,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("countryCode")
	val countryCode: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("outsideNo")
	val outsideNo: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("image")
	val image: ImageUrl? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("accountNumber")
	val accountNumber: String? = null,

	@field:SerializedName("deviceToken")
	val deviceToken: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("delegationOrMunicipality")
	val delegationOrMunicipality: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("interiorNo")
	val interiorNo: String? = null,

	@field:SerializedName("suburb")
	val suburb: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("isActivate")
	val isActivate: Boolean? = null,

	@field:SerializedName("isProfileComplete")
	val isProfileComplete: Boolean? = null
)