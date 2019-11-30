package com.codebrew.encober.ui.myServices

import com.codebrew.encober.models.responseModel.myServices.MyServiceDataItem

interface OnClickMyServiceItemCallback {
    fun onClickMyServiceItem (data: MyServiceDataItem, type : String)
}