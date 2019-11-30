package com.codebrew.encober.ui.home.start

import androidx.lifecycle.ViewModel
import com.codebrew.encober.models.responseModel.UserProfileData
import com.codebrew.encober.models.responseModel.start.MapData
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.utils.extensions.getAccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartViewModel : ViewModel() {
    val activeInactiveRes by lazy { SingleLiveEvent<Resource<UserProfileData>>() }
    val mapServiceDataRes by lazy { SingleLiveEvent<Resource<MapData>>() }


    fun activeInactiveAccount(isActive: Boolean) {
        activeInactiveRes.value = Resource.loading()
        RetrofitClient.getApi().activeInactiveAccount(getAccessToken(), isActive)
            .enqueue(object : Callback<ApiResponse<UserProfileData>> {

                override fun onFailure(
                    call: Call<ApiResponse<UserProfileData>>,
                    throwable: Throwable
                ) {
                    activeInactiveRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<UserProfileData>>,
                    response: Response<ApiResponse<UserProfileData>>
                ) {
                    if (response.isSuccessful) {
                        activeInactiveRes.value = Resource.success(response.body()?.data)
                    } else {
                        activeInactiveRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }

    fun getMapServiceData(map: HashMap<String, Any>) {
        mapServiceDataRes.value = Resource.loading()
        RetrofitClient.getApi().mapServiceData(getAccessToken(), map)
            .enqueue(object : Callback<ApiResponse<MapData>> {

                override fun onFailure(call: Call<ApiResponse<MapData>>, throwable: Throwable) {
                    mapServiceDataRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<MapData>>,
                    response: Response<ApiResponse<MapData>>
                ) {
                    if (response.isSuccessful) {
                        mapServiceDataRes.value = Resource.success(response.body()?.data)
                    } else {
                        mapServiceDataRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }


}