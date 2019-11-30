package com.codebrew.encober.ui.serviceDetails

import com.codebrew.encober.models.responseModel.serviceDetails.happenOnVisit.HappenOnVisitItem
import com.codebrew.encober.models.responseModel.serviceDetails.questionsOfVisit.QuestionOfVisitItem
import com.codebrew.encober.models.responseModel.serviceDetails.reasons.ReasonForServiceItem


interface OnClickDialogCallback {
    fun onClickReason(data:ReasonForServiceItem)
    fun onClickHappenOnVisit(data:HappenOnVisitItem)
    fun onClickQuestionOfVisit(data:QuestionOfVisitItem)

}