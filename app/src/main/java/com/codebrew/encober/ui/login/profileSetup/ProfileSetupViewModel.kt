package com.codebrew.encober.ui.login.profileSetup

import androidx.lifecycle.ViewModel
import com.codebrew.encober.models.requestModel.ProfileUpdateRequest
import com.codebrew.encober.models.responseModel.UserProfileData
import com.codebrew.encober.models.responseModel.profileSetup.PostalCodeResponse
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.utils.extensions.getAccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileSetupViewModel : ViewModel() {
    val postalCodeRes by lazy { SingleLiveEvent<Resource<PostalCodeResponse>>() }
    val profileUpdateRes by lazy { SingleLiveEvent<Resource<UserProfileData>>() }

    fun getPostalCodeDetails(postalCode: Double) {
        postalCodeRes.value = Resource.loading()
        RetrofitClient.getApi().getAddressFromPostalCode(postalCode)
            .enqueue(object : Callback<ApiResponse<PostalCodeResponse>> {
                override fun onFailure(
                    call: Call<ApiResponse<PostalCodeResponse>>,
                    throwable: Throwable
                ) {
                    postalCodeRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<PostalCodeResponse>>,
                    response: Response<ApiResponse<PostalCodeResponse>>
                ) {
                    if (response.isSuccessful) {
                        postalCodeRes.value = Resource.success(response.body()?.data)
                    } else {
                        postalCodeRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }





    fun profileUpdate(req: ProfileUpdateRequest) {
        profileUpdateRes.value = Resource.loading()
        RetrofitClient.getApi().updateProfile(getAccessToken(), req)
            .enqueue(object : Callback<ApiResponse<UserProfileData>> {
                override fun onFailure(
                    call: Call<ApiResponse<UserProfileData>>,
                    throwable: Throwable
                ) {
                    profileUpdateRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<UserProfileData>>,
                    response: Response<ApiResponse<UserProfileData>>
                ) {
                    if (response.isSuccessful) {
                        profileUpdateRes.value = Resource.success(response.body()?.data)
                    } else {
                        profileUpdateRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }

}