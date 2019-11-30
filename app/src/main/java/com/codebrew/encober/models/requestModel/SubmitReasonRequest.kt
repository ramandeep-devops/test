package com.codebrew.encober.models.requestModel

data class SubmitReasonRequest(
    var serviceId: String? = null,
    var questionEn: String? = null,
    var questionSp: String? = null
)