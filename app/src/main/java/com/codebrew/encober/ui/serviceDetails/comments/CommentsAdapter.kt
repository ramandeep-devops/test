package com.codebrew.encober.ui.serviceDetails.comments

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.serviceDetails.comments.CommentsDataItem
import com.codebrew.encober.utils.extensions.timeAgo
import kotlinx.android.synthetic.main.item_task_comments.view.*


class CommentsAdapter : PagedListAdapter<CommentsDataItem, CommentsAdapter.ViewHolder>
    (CommentsDataItem.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_task_comments, null))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        fun bind(data: CommentsDataItem?) = with(itemView) {
            tvItemCommentName.text = data?.userData?.firstName +" "+data?.userData?.lastName
            tvItemCommentText.text = data?.questionEn
            tvItemCommentTime.text = data?.createdAt?.timeAgo()

        }

    }


}


/*

*/
