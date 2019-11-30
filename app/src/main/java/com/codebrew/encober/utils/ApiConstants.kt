package com.codebrew.encober.utils

object ApiConstants {

    // LIVE
//    const val BASE_PATH = "http://44.225.43.233:8002/"


    // LOCAL
    const val BASE_PATH = "http://192.168.100.57:8003/"


    const val ENDPOINT_TERM_CONDITIONS = "user/termAndConditions"
    const val ENDPOINT_LOGIN = "user/login"
    const val ENDPOINT_FORGOT_PASSWORD = "user/forgotPassword"
    const val ENDPOINT_CHECK_EMAIL = "user/checkEmail"
    const val ENDPOINT_SET_PASSWORD = "user/setPassword"
    const val ENDPOINT_BANK_LISTING = "user/bankListing"
    const val ENDPOINT_UPDATE_PROFILE = "user/updateProfile"
    const val ENDPOINT_VIDEO_LISTING = "user/videoListing"
    const val ENDPOINT_SERVICE_LISTING = "user/serviceListing"
    const val ENDPOINT_GET_ADDRESS_FROM_POSTAL_CODE ="user/getAddressFromPostalCode"
    const val ENDPOINT_ACTIVE_INACTIVE_ACCOUNT ="user/activeInactiveAccount"
    const val ENDPOINT_ADD_REMOVE_SERVICE_FROM_ACCOUNT ="user/addRemoveServiceFromAccount"
    const val ENDPOINT_SERVICE_DETAILS ="user/getServiceDetails"
    const val ENDPOINT_REASON_FOR_SERVICE ="user/reasonForService"
    const val ENDPOINT_COMMENT_ON_SERVICE ="user/userCommentsForService"
    const val ENDPOINT_REGISTER_VISIT_SERVICE ="user/finalizeService"
    const val ENDPOINT_MY_SERVICES ="user/myServices"
    const val ENDPOINT_USER_REASON ="user/userReason"
    const val ENDPOINT_CHANGE_PASSWORD ="user/changePassword"
    const val ENDPOINT_USER_LOGOUT ="user/logout"


// New Apis
    const val ENDPOINT_HAPPEN_ON_VISIT ="newUser/happenOnVisitList"
    const val ENDPOINT_QUESTION_OF_VISIT ="newUser/questionOfVisitList"
    const val ENDPOINT_FINALIZE_PAYMENT_NOTIFICATION = "newUser/finalizeServiceForVisit"
    const val ENDPOINT_VISITOR_WITH_POP_UP ="newUser/countIncrementForVisitorWithPopUp"


    const val ENDPOINT_UPLOAD_IMAGE = "admin/uploader"




    const val PARAM_AUTHORIZATION = "authorization"
    const val PARAM_PAGE = "page"
    const val PARAM_PER_PAGE = "perPage"
    const val PARAM_LANGUAGE = "Language"
    const val PARAM_EMAIL = "email"
    const val PARAM_DEVICE_TOKEN="deviceToken"
    const val PARAM_PASSWORD = "password"
    const val PARAM_POSTAL_CODE = "postalCode"
    const val PARAM_IS_ACTIVE = "isActive"
    const val PARAM_TYPE = "type"
    const val PARAM_SERVICE_ID = "serviceId"
    const val PARAM_ID = "id"
    const val PARAM_ID_CAPS = "Id"
    const val PARAM_ORDER= "order"

    const val PARAM_QUES_EN = "questionEn"
    const val PARAM_QUES_SP = "questionSp"
    const val PARAM_IMAGE = "image"

}