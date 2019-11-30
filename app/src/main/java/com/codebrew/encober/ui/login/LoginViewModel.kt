package com.codebrew.encober.ui.login

import androidx.lifecycle.ViewModel
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.models.responseModel.UserProfileData
import com.codebrew.encober.utils.local.PrefsManager
import com.codebrew.encober.utils.local.UserManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    val checkEmailRes by lazy { SingleLiveEvent<Resource<UserProfileData>>() }
    val loginRes by lazy { SingleLiveEvent<Resource<UserProfileData>>() }
    val setPasswordRes by lazy { SingleLiveEvent<Resource<UserProfileData>>() }

    fun checkEmail(email: String) {

        checkEmailRes.value = Resource.loading()
        RetrofitClient.getApi().checkEmail(email,UserManager.getDeviceToken())
            .enqueue(object : Callback<ApiResponse<UserProfileData>> {
                override fun onFailure(
                    call: Call<ApiResponse<UserProfileData>>,
                    throwable: Throwable
                ) {
                    checkEmailRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<UserProfileData>>,
                    response: Response<ApiResponse<UserProfileData>>
                ) {
                    if (response.isSuccessful) {
                        checkEmailRes.value = Resource.success(response.body()?.data)
                    } else {
                        checkEmailRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }


    fun login(email: String, password: String) {

        loginRes.value = Resource.loading()
        RetrofitClient.getApi().login(PrefsManager.LANGUAGE_CODE, email, password)
            .enqueue(object : Callback<ApiResponse<UserProfileData>> {
                override fun onFailure(
                    call: Call<ApiResponse<UserProfileData>>,
                    throwable: Throwable
                ) {
                    loginRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<UserProfileData>>,
                    response: Response<ApiResponse<UserProfileData>>
                ) {
                    if (response.isSuccessful) {
                        loginRes.value = Resource.success(response.body()?.data)
                    } else {
                        loginRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }

    fun setPassword(email: String, password: String) {
        setPasswordRes.value = Resource.loading()
        RetrofitClient.getApi().setPassword(email, password)
            .enqueue(object : Callback<ApiResponse<UserProfileData>> {
                override fun onFailure(
                    call: Call<ApiResponse<UserProfileData>>,
                    throwable: Throwable
                ) {
                    setPasswordRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<UserProfileData>>,
                    response: Response<ApiResponse<UserProfileData>>
                ) {
                    if (response.isSuccessful) {
                        setPasswordRes.value = Resource.success(response.body()?.data)
                    } else {
                        setPasswordRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }
}