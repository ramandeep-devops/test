package com.codebrew.encober.ui.common

import androidx.lifecycle.ViewModel
import com.codebrew.encober.models.responseModel.ImageUrl
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageUploadViewModel : ViewModel(){

    val imageUploadRes by lazy { SingleLiveEvent<Resource<ImageUrl>>() }


    fun uploadImage(image : MultipartBody.Part) {
        imageUploadRes.value = Resource.loading()
        RetrofitClient.getApi().uploadImage(image)
            .enqueue(object : Callback<ApiResponse<ImageUrl>> {
                override fun onFailure(call: Call<ApiResponse<ImageUrl>>, t: Throwable) {
                    imageUploadRes.value = Resource.error(ApiUtils.failure(t))
                }

                override fun onResponse(
                    call: Call<ApiResponse<ImageUrl>>,
                    response: Response<ApiResponse<ImageUrl>>) {

                    if (response.isSuccessful) {
                        imageUploadRes.value = Resource.success(response.body()?.data)
                    } else {
                        imageUploadRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })

    }
}