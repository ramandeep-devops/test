package com.codebrew.encober.ui.home.start

import androidx.lifecycle.ViewModel
import com.codebrew.encober.models.responseModel.serviceDetails.AddRemoveServiceData
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.utils.extensions.getAccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRemoveServiceViewModel : ViewModel(){
    val addRemoveServiceRes by lazy { SingleLiveEvent<Resource<AddRemoveServiceData>>() }

    fun addRemoveService(serviceId:String, type : Int) {
        addRemoveServiceRes.value = Resource.loading()
        RetrofitClient.getApi().addRemoveServiceFromAccount(getAccessToken(),serviceId,type)
            .enqueue(object : Callback<ApiResponse<AddRemoveServiceData>> {

                override fun onFailure(call: Call<ApiResponse<AddRemoveServiceData>>, throwable: Throwable) {
                    addRemoveServiceRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<AddRemoveServiceData>>,
                    response: Response<ApiResponse<AddRemoveServiceData>>
                ) {
                    if (response.isSuccessful) {
                        addRemoveServiceRes.value = Resource.success(response.body()?.data)
                    } else {
                        addRemoveServiceRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }
}