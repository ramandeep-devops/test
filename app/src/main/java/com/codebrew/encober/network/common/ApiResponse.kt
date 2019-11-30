package com.codebrew.encober.network.common

data class ApiResponse<out T>(
        val msg: String? = null,
        val status: Int? = null,
        val data: T? = null
)