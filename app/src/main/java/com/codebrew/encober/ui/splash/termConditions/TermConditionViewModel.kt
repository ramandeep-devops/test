package com.codebrew.encober.ui.splash.termConditions

import androidx.lifecycle.ViewModel
import com.codebrew.encober.models.responseModel.termConditions.TermConditionData
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermConditionViewModel : ViewModel() {

    val termConditionRes by lazy { SingleLiveEvent<Resource<TermConditionData>>() }

    fun getTermCondition() {

        termConditionRes.value = Resource.loading()
        RetrofitClient.getApi().termAndConditions()
            .enqueue(object : Callback<ApiResponse<TermConditionData>> {
                override fun onFailure(
                    call: Call<ApiResponse<TermConditionData>>,
                    throwable: Throwable
                ) {
                    termConditionRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<TermConditionData>>,
                    response: Response<ApiResponse<TermConditionData>>
                ) {
                    if (response.isSuccessful) {
                        termConditionRes.value = Resource.success(response.body()?.data)
                    } else {
                        termConditionRes.value = Resource.error(
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