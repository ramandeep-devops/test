package com.codebrew.encober.ui.home.start

import com.codebrew.encober.models.responseModel.start.ServiceListItem

interface OnClickMapDataListCallback {
    fun onClickMapListItem(data: ServiceListItem, type : String)
}