package com.codebrew.encober.ui.myVideos

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.codebrew.encober.models.responseModel.videosData.VideoListingItem

class VideoListDataFactory: DataSource.Factory<Long, VideoListingItem>() {

    private val mutableLiveData: MutableLiveData<VideosListDataSource> = MutableLiveData()
    private lateinit var dataSource: VideosListDataSource

    override  fun create(): DataSource<Long, VideoListingItem> {
        dataSource = VideosListDataSource()
        mutableLiveData.postValue(dataSource)
        return dataSource
    }

    fun getMutableLiveData(): MutableLiveData<VideosListDataSource> {
        return mutableLiveData
    }

}