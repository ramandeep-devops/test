package com.codebrew.encober.ui.serviceDetails.registerVisitForService

import androidx.lifecycle.ViewModel
import com.codebrew.encober.models.requestModel.FinalizePaymentNotificationRequest
import com.codebrew.encober.models.responseModel.serviceDetails.finalizePayment.FinalizePaymentData
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.utils.extensions.getAccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterVisitViewModel : ViewModel() {
    val finalizePaymentRes by lazy { SingleLiveEvent<Resource<FinalizePaymentData>>() }


    fun finalizePaymentNotification(obj: FinalizePaymentNotificationRequest) {
        finalizePaymentRes.value = Resource.loading()
        RetrofitClient.getApi().finalizePaymentNotification(getAccessToken(), obj)
            .enqueue(object : Callback<ApiResponse<FinalizePaymentData>> {
                override fun onFailure(call: Call<ApiResponse<FinalizePaymentData>>, throwable: Throwable) {
                    finalizePaymentRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<FinalizePaymentData>>,
                    response: Response<ApiResponse<FinalizePaymentData>>
                ) {
                    if (response.isSuccessful) {
                        finalizePaymentRes.value = Resource.success(response.body()?.data)
                    } else {
                        finalizePaymentRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }

            })
    }
}