package com.codebrew.encober.ui.myVideos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.videosData.VideoListingItem
import com.codebrew.encober.ui.myVideos.videoDetails.VideoDetailsActivity
import com.codebrew.encober.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_main_my_videos.*


class MyVideosFragment : Fragment(), OnClickYoutubeVideoCallback {
    override fun onClickYoutube(data: VideoListingItem) {
        val intent =Intent(requireActivity(), VideoDetailsActivity::class.java)
        intent.putExtra(AppConstants.MY_VIDEO_ITEM, data)
        startActivity(intent)
    }

    companion object {
        const val TAG = "com.codebrew.vipcarts.ui.main.myVideos.MyVideosFragment"
    }

    private lateinit var viewModel: VideoListViewModel
    private val videosAdapter = MyVideosAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_main_my_videos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterSetup()
        apiHit()
        setListeners()
    }

    private fun setListeners() {
        srlMyVideos.setOnRefreshListener {
            apiHit()
        }

    }

    private fun adapterSetup() {
        rvMyVideos.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        rvMyVideos.adapter = videosAdapter
    }

    private fun apiHit() {
        viewModel = VideoListViewModel()

        viewModel.getNetworkState()?.observe(viewLifecycleOwner, Observer {
            if (it.status.name == "RUNNING" && videosAdapter.currentList?.isNullOrEmpty() == true) {
                vfMyVideos.displayedChild = 0
            } else if (it.status.name == "SUCCESS") {
                if (videosAdapter.itemCount == 0) {
                    vfMyVideos.displayedChild = 1
                } else {
                    srlMyVideos.isRefreshing = false
                    vfMyVideos.displayedChild = 2
                }
            }

        })

        viewModel.getArticleLiveData()?.observe(viewLifecycleOwner, Observer {
            videosAdapter.submitList(it)
            if (videosAdapter.currentList?.isNullOrEmpty() == false && it.isNotEmpty()) {
                vfMyVideos.displayedChild = 2
            }
        })
    }


}