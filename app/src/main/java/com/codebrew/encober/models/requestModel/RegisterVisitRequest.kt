package com.codebrew.encober.models.requestModel

data class RegisterVisitRequest(
    var serviceId: String? = null,
    var agreedAmount: Double? = null,
    var promiseDate: Long? = null,
    var SignatureOriginal: String? = null,
    var SignatureThumbnail: String? = null
)