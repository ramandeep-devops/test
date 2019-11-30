package com.codebrew.encober.ui.serviceDetails

import androidx.lifecycle.ViewModel
import com.codebrew.encober.models.requestModel.SubmitReasonRequest
import com.codebrew.encober.models.requestModel.VisitorWithPopUpRequest
import com.codebrew.encober.models.responseModel.serviceDetails.ServiceDetailsData
import com.codebrew.encober.models.responseModel.serviceDetails.happenOnVisit.HappenOnVisitData
import com.codebrew.encober.models.responseModel.serviceDetails.questionsOfVisit.QuestionOfVisitData
import com.codebrew.encober.models.responseModel.serviceDetails.reasons.ReasonForServiceData
import com.codebrew.encober.models.responseModel.serviceDetails.submitReason.SubmitReasonData
import com.codebrew.encober.models.responseModel.serviceDetails.visitorPopUp.VisitorWithPopUpData
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.utils.extensions.getAccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceDetailsViewModel : ViewModel() {
    val reasonForServiceRes by lazy { SingleLiveEvent<Resource<ReasonForServiceData>>() }
    val serviceDetailsRes by lazy { SingleLiveEvent<Resource<ServiceDetailsData>>() }
    val happenOnVisitRes by lazy { SingleLiveEvent<Resource<HappenOnVisitData>>() }
    val questionOfVisitRes by lazy { SingleLiveEvent<Resource<QuestionOfVisitData>>() }
    val visitorPopUpRes by lazy { SingleLiveEvent<Resource<VisitorWithPopUpData>>() }
    val submitReasonRes by lazy { SingleLiveEvent<Resource<SubmitReasonData>>() }


    fun serviceDetails(serviceId: String) {
        serviceDetailsRes.value = Resource.loading()
        RetrofitClient.getApi().serviceDetails(getAccessToken(), serviceId)
            .enqueue(object : Callback<ApiResponse<ServiceDetailsData>> {

                override fun onFailure(
                    call: Call<ApiResponse<ServiceDetailsData>>,
                    throwable: Throwable
                ) {
                    serviceDetailsRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<ServiceDetailsData>>,
                    response: Response<ApiResponse<ServiceDetailsData>>
                ) {
                    if (response.isSuccessful) {
                        serviceDetailsRes.value = Resource.success(response.body()?.data)
                    } else {
                        serviceDetailsRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }
            })
    }


    fun reasonForService(serviceId: String) {
        reasonForServiceRes.value = Resource.loading()
        RetrofitClient.getApi().reasonForService(getAccessToken(), serviceId)
            .enqueue(object : Callback<ApiResponse<ReasonForServiceData>> {

                override fun onFailure(
                    call: Call<ApiResponse<ReasonForServiceData>>,
                    throwable: Throwable
                ) {
                    reasonForServiceRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<ReasonForServiceData>>,
                    response: Response<ApiResponse<ReasonForServiceData>>
                ) {
                    if (response.isSuccessful) {
                        reasonForServiceRes.value = Resource.success(response.body()?.data)
                    } else {
                        reasonForServiceRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }
            })
    }


    fun happenOnVisit(serviceId: String) {
        happenOnVisitRes.value = Resource.loading()
        RetrofitClient.getApi().happenOnVisitList(getAccessToken(), serviceId)
            .enqueue(object : Callback<ApiResponse<HappenOnVisitData>> {

                override fun onFailure(
                    call: Call<ApiResponse<HappenOnVisitData>>,
                    throwable: Throwable
                ) {
                    happenOnVisitRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<HappenOnVisitData>>,
                    response: Response<ApiResponse<HappenOnVisitData>>
                ) {
                    if (response.isSuccessful) {
                        happenOnVisitRes.value = Resource.success(response.body()?.data)
                    } else {
                        happenOnVisitRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }
            })
    }

    fun questionsOfVisit(id: String, order: Int) {
        questionOfVisitRes.value = Resource.loading()
        RetrofitClient.getApi().questionOfVisitList(getAccessToken(), id, order)
            .enqueue(object : Callback<ApiResponse<QuestionOfVisitData>> {

                override fun onFailure(
                    call: Call<ApiResponse<QuestionOfVisitData>>,
                    throwable: Throwable
                ) {
                    questionOfVisitRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<QuestionOfVisitData>>,
                    response: Response<ApiResponse<QuestionOfVisitData>>
                ) {
                    if (response.isSuccessful) {
                        questionOfVisitRes.value = Resource.success(response.body()?.data)
                    } else {
                        questionOfVisitRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }
            })
    }

    fun visitorPopUp(obj: VisitorWithPopUpRequest) {
        visitorPopUpRes.value = Resource.loading()
        RetrofitClient.getApi().visitorWithPopUp(getAccessToken(), obj)
            .enqueue(object : Callback<ApiResponse<VisitorWithPopUpData>> {

                override fun onFailure(call: Call<ApiResponse<VisitorWithPopUpData>>, throwable: Throwable) {
                    visitorPopUpRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<VisitorWithPopUpData>>,
                    response: Response<ApiResponse<VisitorWithPopUpData>>
                ) {
                    if (response.isSuccessful) {
                        visitorPopUpRes.value = Resource.success(response.body()?.data)
                    } else {
                        visitorPopUpRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }
            })
    }

    fun submitReason(obj: SubmitReasonRequest) {
        submitReasonRes.value = Resource.loading()
        RetrofitClient.getApi().submitReasonForService(getAccessToken(), obj)
            .enqueue(object : Callback<ApiResponse<SubmitReasonData>> {

                override fun onFailure(call: Call<ApiResponse<SubmitReasonData>>, throwable: Throwable) {
                    submitReasonRes.value = Resource.error(ApiUtils.failure(throwable))
                }

                override fun onResponse(
                    call: Call<ApiResponse<SubmitReasonData>>,
                    response: Response<ApiResponse<SubmitReasonData>>
                ) {
                    if (response.isSuccessful) {
                        submitReasonRes.value = Resource.success(response.body()?.data)
                    } else {
                        submitReasonRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(), response.errorBody()?.string()
                            )
                        )
                    }
                }
            })
    }
}