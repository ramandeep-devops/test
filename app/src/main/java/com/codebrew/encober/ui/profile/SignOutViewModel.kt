package com.codebrew.encober.ui.profile

import androidx.lifecycle.ViewModel
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.utils.extensions.getAccessToken
import com.codebrew.encober.utils.local.PrefsManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignOutViewModel : ViewModel() {
    val signOutRes by lazy { SingleLiveEvent<Resource<Any>>() }

    fun signOut() {
        signOutRes.value = Resource.loading()
        RetrofitClient.getApi().signOutUser(getAccessToken(), PrefsManager.LANGUAGE_CODE)
            .enqueue(object : Callback<ApiResponse<Any>> {

                override fun onFailure(call: Call<ApiResponse<Any>>, throwable: Throwable) {
                    signOutRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<Any>>,
                    response: Response<ApiResponse<Any>>
                ) {
                    if (response.isSuccessful) {
                        signOutRes.value = Resource.success(response.body()?.data)
                    } else {
                        signOutRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }
}