package com.codebrew.encober.models.responseModel.profileSetup


data class PostalCodeResponse(
    val __v: Int,
    val _id: String,
    val city: String,
    val createdAt: Long,
    val isBlocked: Boolean,
    val isDeleted: Boolean,
    val municipality: String,
    val neighborhood: String,
    val state: String,
    val zip_code: Int
)