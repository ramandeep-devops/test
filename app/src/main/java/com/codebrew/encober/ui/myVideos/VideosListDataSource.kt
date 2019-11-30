package com.codebrew.encober.ui.myVideos

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.codebrew.encober.models.responseModel.videosData.StartVideoData
import com.codebrew.encober.models.responseModel.videosData.VideoListingItem
import com.codebrew.encober.network.RetrofitClient
import com.codebrew.encober.network.common.ApiResponse
import com.codebrew.encober.network.common.ApiUtils
import com.codebrew.encober.network.common.Resource
import com.codebrew.encober.network.common.SingleLiveEvent
import com.codebrew.encober.utils.NetworkState
import com.codebrew.encober.utils.extensions.getAccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideosListDataSource : PageKeyedDataSource<Long, VideoListingItem>() {


    val videosRes by lazy { SingleLiveEvent<Resource<StartVideoData>>() }

    private val networkState = MutableLiveData<NetworkState>()

    fun getNetworkState(): MutableLiveData<NetworkState> {
        return networkState
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, VideoListingItem>
    ) {
        networkState.postValue(NetworkState.LOADING)


        RetrofitClient.getApi()
            .videoListing(authorization = getAccessToken(), page = 0, perPage = 10)
            .enqueue(object : Callback<ApiResponse<StartVideoData>> {
                override fun onFailure(
                    call: Call<ApiResponse<StartVideoData>>,
                    throwable: Throwable
                ) {
                    videosRes.value = Resource.error(ApiUtils.failure(throwable))
                    networkState.postValue(
                        NetworkState(
                            NetworkState.Status.FAILED,
                            throwable.message
                        )
                    )
                }

                override fun onResponse(
                    call: Call<ApiResponse<StartVideoData>>,
                    response: Response<ApiResponse<StartVideoData>>
                ) {
                    if (response.isSuccessful) {

                        callback.onResult(
                            response.body()?.data?.listing?.toMutableList() ?: mutableListOf(),
                            null, 1
                        )
                        videosRes.value = Resource.success(response.body()?.data)
                        networkState.postValue(NetworkState.LOADED)

                    } else {
                        videosRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(),
                                response.errorBody()?.string()
                            )
                        )
                        networkState.postValue(
                            NetworkState(
                                NetworkState.Status.FAILED,
                                response.message()
                            )
                        )
                    }
                }
            })
    }

    override fun loadBefore(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, VideoListingItem>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, VideoListingItem>
    ) {
        networkState.postValue(NetworkState.LOADING)


        RetrofitClient.getApi()
            .videoListing(authorization = getAccessToken(), page = params.key.toInt(), perPage = 10)
            .enqueue(object : Callback<ApiResponse<StartVideoData>> {
                override fun onFailure(
                    call: Call<ApiResponse<StartVideoData>>,
                    throwable: Throwable
                ) {
                    videosRes.value = Resource.error(ApiUtils.failure(throwable))
                    networkState.postValue(
                        NetworkState(
                            NetworkState.Status.FAILED,
                            throwable.message
                        )
                    )
                }

                override fun onResponse(
                    call: Call<ApiResponse<StartVideoData>>,
                    response: Response<ApiResponse<StartVideoData>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.data?.count ?: 0 > 10) {
                            val nextKey =
                                if (((params.key + 1) * 10).toInt() >= response.body()?.data?.count ?: 0)
                                    null
                                else
                                    (params.key + 1)

                            callback.onResult(
                                response.body()?.data?.listing ?: mutableListOf(),
                                nextKey
                            )
                        }
                        videosRes.value = Resource.success(response.body()?.data)
                        networkState.postValue(NetworkState.LOADED)

                    } else {
                        videosRes.value = Resource.error(
                            ApiUtils.getError(
                                response.code(),
                                response.errorBody()?.string()
                            )
                        )
                        networkState.postValue(
                            NetworkState(
                                NetworkState.Status.FAILED,
                                response.message()
                            )
                        )

                    }
                }

            })
    }


}