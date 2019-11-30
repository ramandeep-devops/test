package com.codebrew.encober.models.responseModel.serviceDetails.questionsOfVisit

import com.google.gson.annotations.SerializedName

data class QuestionOfVisitData(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("listing")
	val listing: ArrayList<QuestionOfVisitItem>? = null
)