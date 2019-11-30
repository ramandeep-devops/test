package com.codebrew.encober.network.common

sealed class AppError {
    data class ApiError(val status: Int, val message: String) : AppError()
    data class ApiUnauthorized(val message: String) : AppError()
    data class ApiAccountBlock(val message: String) : AppError()
    data class ApiAccountRuleChanged(val message: String) : AppError()
    data class ApiFailure(val message: String) : AppError()

}