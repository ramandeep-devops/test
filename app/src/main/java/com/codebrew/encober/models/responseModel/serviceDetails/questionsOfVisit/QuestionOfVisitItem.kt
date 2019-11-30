package com.codebrew.encober.models.responseModel.serviceDetails.questionsOfVisit

import com.google.gson.annotations.SerializedName

data class QuestionOfVisitItem(

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

	@field:SerializedName("Id")
	val reasonId: String? = null,

	@field:SerializedName("questionEn")
	val questionEn: String? = null,

	@field:SerializedName("order")
	val order: Int? = null,

	var isSelected : Boolean = false
)