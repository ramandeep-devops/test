package com.codebrew.encober.ui.serviceDetails.comments

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.codebrew.encober.models.responseModel.serviceDetails.comments.CommentsData
import com.codebrew.encober.models.responseModel.serviceDetails.comments.CommentsDataItem
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

class CommentsDataSource(private val serviceId : String) : PageKeyedDataSource<Long, CommentsDataItem>() {


    val videosRes by lazy { SingleLiveEvent<Resource<CommentsData>>() }

    private val networkState = MutableLiveData<NetworkState>()

    fun getNetworkState(): MutableLiveData<NetworkState> {
        return networkState
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, CommentsDataItem>
    ) {
        networkState.postValue(NetworkState.LOADING)


        RetrofitClient.getApi()
            .commentsOnService(authorization = getAccessToken(),serviceId = serviceId, page = 0, perPage = 10)
            .enqueue(object : Callback<ApiResponse<CommentsData>> {
                override fun onFailure(
                    call: Call<ApiResponse<CommentsData>>,
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
                    call: Call<ApiResponse<CommentsData>>,
                    response: Response<ApiResponse<CommentsData>>
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
        callback: LoadCallback<Long, CommentsDataItem>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, CommentsDataItem>
    ) {
        networkState.postValue(NetworkState.LOADING)


        RetrofitClient.getApi()
            .commentsOnService(authorization = getAccessToken(),serviceId = serviceId, page = params.key.toInt(), perPage = 10)
            .enqueue(object : Callback<ApiResponse<CommentsData>> {
                override fun onFailure(
                    call: Call<ApiResponse<CommentsData>>,
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
                    call: Call<ApiResponse<CommentsData>>,
                    response: Response<ApiResponse<CommentsData>>
                ) {
                    if (response.isSuccessful) {
                        val nextKey =
                            if (((params.key + 1) * 10).toInt() >= response.body()?.data?.count ?: 0)
                                null
                            else
                                (params.key + 1)

                        callback.onResult(
                            response.body()?.data?.listing ?: mutableListOf(),
                            nextKey
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


}