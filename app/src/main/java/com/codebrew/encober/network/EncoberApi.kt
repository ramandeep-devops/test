package com.codebrew.encober.network

import com.codebrew.encober.models.requestModel.*
import com.codebrew.encober.models.responseModel.ImageUrl
import com.codebrew.encober.models.responseModel.UserProfileData
import com.codebrew.encober.models.responseModel.myProfile.ChangePasswordData
import com.codebrew.encober.models.responseModel.myServices.MyServiceDataItem
import com.codebrew.encober.models.responseModel.profileSetup.BankListResponse
import com.codebrew.encober.models.responseModel.profileSetup.PostalCodeResponse
import com.codebrew.encober.models.responseModel.serviceDetails.AddRemoveServiceData
import com.codebrew.encober.models.responseModel.serviceDetails.ServiceDetailsData
import com.codebrew.encober.models.responseModel.serviceDetails.reasons.ReasonForServiceData
import com.codebrew.encober.models.responseModel.serviceDetails.comments.CommentsData
import com.codebrew.encober.models.responseModel.serviceDetails.finalizePayment.FinalizePaymentData
import com.codebrew.encober.models.responseModel.serviceDetails.happenOnVisit.HappenOnVisitData
import com.codebrew.encober.models.responseModel.serviceDetails.questionsOfVisit.QuestionOfVisitData
import com.codebrew.encober.models.responseModel.serviceDetails.submitReason.SubmitReasonData
import com.codebrew.encober.models.responseModel.serviceDetails.visitorPopUp.VisitorWithPopUpData
import com.codebrew.encober.models.responseModel.start.MapData
import com.codebrew.encober.models.responseModel.termConditions.TermConditionData
import com.codebrew.encober.models.responseModel.videosData.StartVideoData
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.utils.ApiConstants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface EncoberApi {

    @FormUrlEncoded
    @POST(ApiConstants.ENDPOINT_LOGIN)
    fun login(
        @Header(ApiConstants.PARAM_LANGUAGE) language: Int,
        @Field(ApiConstants.PARAM_EMAIL) email: String,
        @Field(ApiConstants.PARAM_PASSWORD) password: String
    ): Call<ApiResponse<UserProfileData>>


    @FormUrlEncoded
    @POST(ApiConstants.ENDPOINT_FORGOT_PASSWORD)
    fun forgotPassword(
        @Header(ApiConstants.PARAM_LANGUAGE) language: Int,
        @Field(ApiConstants.PARAM_EMAIL) email: String):Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST(ApiConstants.ENDPOINT_CHECK_EMAIL)
    fun checkEmail(
        @Field(ApiConstants.PARAM_EMAIL) email: String,
        @Field(ApiConstants.PARAM_DEVICE_TOKEN) deviceToken: String
    ): Call<ApiResponse<UserProfileData>>


    @FormUrlEncoded
    @POST(ApiConstants.ENDPOINT_SET_PASSWORD)
    fun setPassword(
        @Field(ApiConstants.PARAM_EMAIL) email: String,
        @Field(ApiConstants.PARAM_PASSWORD) password: String
    ): Call<ApiResponse<UserProfileData>>


    // This page api is not in use ,Bank Page had been removed
    @GET(ApiConstants.ENDPOINT_BANK_LISTING)
    fun bankList(): Call<ApiResponse<BankListResponse>>


    @POST(ApiConstants.ENDPOINT_UPDATE_PROFILE)
    fun updateProfile(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Body request: ProfileUpdateRequest
    ): Call<ApiResponse<UserProfileData>>

    @Multipart
    @POST(ApiConstants.ENDPOINT_UPLOAD_IMAGE)
    fun uploadImage(
        @Part image : MultipartBody.Part
    ): Call<ApiResponse<ImageUrl>>


    @GET(ApiConstants.ENDPOINT_GET_ADDRESS_FROM_POSTAL_CODE)
    fun getAddressFromPostalCode(
        @Query(ApiConstants.PARAM_POSTAL_CODE) postalCode: Double
    ): Call<ApiResponse<PostalCodeResponse>>


    @FormUrlEncoded
    @POST(ApiConstants.ENDPOINT_ACTIVE_INACTIVE_ACCOUNT)
    fun activeInactiveAccount(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Field(ApiConstants.PARAM_IS_ACTIVE) isActive: Boolean
    ): Call<ApiResponse<UserProfileData>>


    @FormUrlEncoded
    @POST(ApiConstants.ENDPOINT_ADD_REMOVE_SERVICE_FROM_ACCOUNT)
    fun addRemoveServiceFromAccount(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Field(ApiConstants.PARAM_SERVICE_ID) serviceId: String,
        @Field(ApiConstants.PARAM_TYPE) type: Int
    ): Call<ApiResponse<AddRemoveServiceData>>


    @GET(ApiConstants.ENDPOINT_VIDEO_LISTING)
    fun videoListing(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
             @Query(ApiConstants.PARAM_PAGE) page: Int,
             @Query(ApiConstants.PARAM_PER_PAGE) perPage: Int
    ): Call<ApiResponse<StartVideoData>>


    @GET(ApiConstants.ENDPOINT_SERVICE_LISTING)
    fun mapServiceData(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @QueryMap map: HashMap<String,Any>): Call<ApiResponse<MapData>>


    @GET(ApiConstants.ENDPOINT_SERVICE_DETAILS)
    fun serviceDetails(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Query(ApiConstants.PARAM_SERVICE_ID) serviceId: String
    ): Call<ApiResponse<ServiceDetailsData>>



    @GET(ApiConstants.ENDPOINT_TERM_CONDITIONS)
    fun termAndConditions(): Call<ApiResponse<TermConditionData>>


    @GET(ApiConstants.ENDPOINT_HAPPEN_ON_VISIT)
    fun happenOnVisitList(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Query(ApiConstants.PARAM_ID) serviceId: String
    ): Call<ApiResponse<HappenOnVisitData>>


    @GET(ApiConstants.ENDPOINT_QUESTION_OF_VISIT)
    fun questionOfVisitList(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Query(ApiConstants.PARAM_ID_CAPS) Id: String,
        @Query(ApiConstants.PARAM_ORDER) order: Int
    ): Call<ApiResponse<QuestionOfVisitData>>



    @GET(ApiConstants.ENDPOINT_REASON_FOR_SERVICE)
    fun reasonForService(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Query(ApiConstants.PARAM_SERVICE_ID) serviceId: String
    ): Call<ApiResponse<ReasonForServiceData>>

    @GET(ApiConstants.ENDPOINT_COMMENT_ON_SERVICE)
    fun commentsOnService(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Query(ApiConstants.PARAM_SERVICE_ID) serviceId: String,
        @Query(ApiConstants.PARAM_PAGE) page: Int,
        @Query(ApiConstants.PARAM_PER_PAGE) perPage: Int
    ): Call<ApiResponse<CommentsData>>



    @POST(ApiConstants.ENDPOINT_REGISTER_VISIT_SERVICE)
    fun registerVisitForService(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Body request: RegisterVisitRequest
    ): Call<ApiResponse<Any>>

    @GET(ApiConstants.ENDPOINT_MY_SERVICES)
    fun getMyServices(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Query(ApiConstants.PARAM_TYPE) type: Int
    ): Call<ApiResponse<ArrayList<MyServiceDataItem>>>



    @POST(ApiConstants.ENDPOINT_CHANGE_PASSWORD)
    fun changePassword(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Body request: ChangePasswordRequest
    ): Call<ApiResponse<ChangePasswordData>>


    @FormUrlEncoded
    @PUT(ApiConstants.ENDPOINT_USER_LOGOUT)
    fun signOutUser(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Field(ApiConstants.PARAM_LANGUAGE) language: Int
    ): Call<ApiResponse<Any>>

    @POST(ApiConstants.ENDPOINT_FINALIZE_PAYMENT_NOTIFICATION)
    fun finalizePaymentNotification(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Body request: FinalizePaymentNotificationRequest
    ): Call<ApiResponse<FinalizePaymentData>>

    @POST(ApiConstants.ENDPOINT_VISITOR_WITH_POP_UP)
    fun visitorWithPopUp(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Body request: VisitorWithPopUpRequest
    ): Call<ApiResponse<VisitorWithPopUpData>>


    @POST(ApiConstants.ENDPOINT_USER_REASON)
    fun submitReasonForService(
        @Header(ApiConstants.PARAM_AUTHORIZATION) authorization: String,
        @Body request: SubmitReasonRequest
    ): Call<ApiResponse<SubmitReasonData>>

}