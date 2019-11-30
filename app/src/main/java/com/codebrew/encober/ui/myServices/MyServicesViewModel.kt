package com.codebrew.encober.ui.myServices

import androidx.lifecycle.ViewModel
import com.codebrew.encober.models.responseModel.myServices.MyServiceDataItem
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.utils.extensions.getAccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyServicesViewModel :ViewModel(){
    val servicesRes by lazy { SingleLiveEvent<Resource<ArrayList<MyServiceDataItem>>>() }

    fun getMyServices(type:Int) {

        servicesRes.value = Resource.loading()
        RetrofitClient.getApi().getMyServices(getAccessToken(),type)
            .enqueue(object : Callback<ApiResponse<ArrayList<MyServiceDataItem>>> {
                override fun onFailure(
                    call: Call<ApiResponse<ArrayList<MyServiceDataItem>>>,
                    throwable: Throwable
                ) {
                    servicesRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<ArrayList<MyServiceDataItem>>>,
                    response: Response<ApiResponse<ArrayList<MyServiceDataItem>>>
                ) {
                    if (response.isSuccessful) {
                        servicesRes.value = Resource.success(response.body()?.data)
                    } else {
                        servicesRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }

}