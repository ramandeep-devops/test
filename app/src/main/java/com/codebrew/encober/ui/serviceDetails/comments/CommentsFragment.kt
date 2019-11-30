package com.codebrew.encober.ui.serviceDetails.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.extensions.isNetworkActive
import kotlinx.android.synthetic.main.layout_all_comments.*

class CommentsFragment : Fragment() {

    companion object {
        const val TAG = "com.codebrew.vipcarts.ui.main.start.serviceDetails.comments.CommentsFragment"
    }

    private lateinit var viewModel: CommentsViewModel
    private val commentsAdapter = CommentsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_all_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterSetup()
        setListeners()
        if (requireActivity().isNetworkActive()) {
            apiHit()
        }

    }



    private fun setListeners() {
        tbrAllComments.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        srlAllComments.setOnRefreshListener {
            if (requireActivity().isNetworkActive()) {
                apiHit()
            }

        }
    }


    private fun adapterSetup() {
        rvAllComments.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        rvAllComments.adapter = commentsAdapter
    }

    private fun apiHit() {
        val serviceId = arguments?.getString(AppConstants.SERVICE_ID) ?: ""

        viewModel = CommentsViewModel(serviceId)

        viewModel.getNetworkState()?.observe(viewLifecycleOwner, Observer {
            if(it.status.name == "RUNNING" && commentsAdapter.currentList?.isNullOrEmpty() == true){
                vfAllComments.displayedChild = 0
            }
            else if(it.status.name == "SUCCESS"){
                if(commentsAdapter.itemCount == 0){
                    vfAllComments.displayedChild = 1
                }
                else {
                    srlAllComments.isRefreshing = false
                    vfAllComments.displayedChild = 2
                }
            }

        })

        viewModel.getArticleLiveData()?.observe(viewLifecycleOwner, Observer {
            commentsAdapter.submitList(it)
            if(commentsAdapter.currentList?.isNullOrEmpty() == false  && it.isNotEmpty()){
                vfAllComments.displayedChild = 2
            }
        })
    }



}