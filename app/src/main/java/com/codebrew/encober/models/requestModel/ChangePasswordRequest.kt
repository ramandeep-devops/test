package com.codebrew.encober.models.requestModel

data class ChangePasswordRequest(
    var oldPassword: String? = null,
    var newPassword: String? = null
)