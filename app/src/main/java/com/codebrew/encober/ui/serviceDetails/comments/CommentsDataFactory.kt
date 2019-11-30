package com.codebrew.encober.ui.serviceDetails.comments

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.codebrew.encober.models.responseModel.serviceDetails.comments.CommentsDataItem

class CommentsDataFactory (private val serviceId : String): DataSource.Factory<Long, CommentsDataItem>() {

    private val mutableLiveData: MutableLiveData<CommentsDataSource> = MutableLiveData()
    private lateinit var dataSource: CommentsDataSource

    override  fun create(): DataSource<Long, CommentsDataItem> {
        dataSource = CommentsDataSource(serviceId)
        mutableLiveData.postValue(dataSource)
        return dataSource
    }

    fun getMutableLiveData(): MutableLiveData<CommentsDataSource> {
        return mutableLiveData
    }

}