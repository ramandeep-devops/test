package com.codebrew.encober.ui.myVideos

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.codebrew.encober.models.responseModel.videosData.VideoListingItem
import com.codebrew.encober.utils.NetworkState
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class VideoListViewModel : ViewModel() {


    private lateinit var executor: Executor
    private var networkState: LiveData<NetworkState>? = null
    private var articleLiveData: LiveData<PagedList<VideoListingItem>>? = null

    init {
        init()
    }



    private fun init() {
        executor = Executors.newFixedThreadPool(5)

        val dataFactory = VideoListDataFactory()
        networkState = Transformations.switchMap(dataFactory.getMutableLiveData()
        ) { dataSource -> dataSource.getNetworkState() }

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10).build()

        articleLiveData = LivePagedListBuilder(dataFactory, pagedListConfig)
            .setFetchExecutor(executor)
            .build()
    }


    fun getNetworkState(): LiveData<NetworkState>? {
        return networkState
    }

    fun getArticleLiveData(): LiveData<PagedList<VideoListingItem>>? {
        return articleLiveData
    }
}