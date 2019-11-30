package com.codebrew.encober.ui.login.forgotPassword

import androidx.lifecycle.ViewModel
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.utils.local.PrefsManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class ForgotPasswordViewModel :ViewModel(){
    val forgotPasswordRes by lazy { SingleLiveEvent<Resource<Any>>() }

    fun forgotPassword(email:String) {
        forgotPasswordRes.value = Resource.loading()
        RetrofitClient.getApi().forgotPassword(PrefsManager.LANGUAGE_CODE,email)
            .enqueue(object : Callback<ApiResponse<Any>> {

                override fun onFailure(call: Call<ApiResponse<Any>>, throwable: Throwable) {
                    forgotPasswordRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<Any>>,
                    response: Response<ApiResponse<Any>>
                ) {
                    if (response.isSuccessful) {
                        forgotPasswordRes.value = Resource.success(response.body()?.data)
                    } else {
                        forgotPasswordRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }
            })
    }

}