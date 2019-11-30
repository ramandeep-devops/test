package com.codebrew.encober.ui.profile.changePassword

import androidx.lifecycle.ViewModel
import com.codebrew.encober.models.requestModel.ChangePasswordRequest
import com.codebrew.encober.models.responseModel.myProfile.ChangePasswordData
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.utils.extensions.getAccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePasswordViewModel : ViewModel() {

    val changePasswordRes by lazy { SingleLiveEvent<Resource<ChangePasswordData>>() }

    fun changePassword(req: ChangePasswordRequest) {

        changePasswordRes.value = Resource.loading()
        RetrofitClient.getApi().changePassword(getAccessToken(), req)
            .enqueue(object : Callback<ApiResponse<ChangePasswordData>> {
                override fun onFailure(
                    call: Call<ApiResponse<ChangePasswordData>>,
                    throwable: Throwable
                ) {
                    changePasswordRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<ChangePasswordData>>,
                    response: Response<ApiResponse<ChangePasswordData>>
                ) {
                    if (response.isSuccessful) {
                        changePasswordRes.value = Resource.success(response.body()?.data)
                    } else {
                        changePasswordRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(),
                                response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }
}