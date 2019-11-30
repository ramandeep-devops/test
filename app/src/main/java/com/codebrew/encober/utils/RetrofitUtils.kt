package com.codebrew.encober.utils

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object RetrofitUtils
{
    fun stringToRequestBody(string: String): RequestBody
            = RequestBody.create(MediaType.parse("text/plain"), string)

    fun imageToRequestBody(imageFile: File): RequestBody
            = RequestBody.create(MediaType.parse("image/*"), imageFile)

    fun imageToRequestBodyKey(parameterName: String, fileName: File?): String
            = parameterName + "\"; filename=\"" + fileName?.name

    fun createMultiPart(file: File, key: String): MultipartBody.Part {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData(key, file.name, requestFile)
    }

}