package com.codebrew.encober.models.responseModel.myServices

import com.google.gson.annotations.SerializedName

data class MyServiceDataItem(

	@field:SerializedName("myServiceData")
	val myServiceData: ServiceData? = null
)