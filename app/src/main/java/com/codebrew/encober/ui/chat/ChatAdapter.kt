package com.codebrew.encober.ui.chat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.myServices.MyServiceDataItem

class ChatAdapter(private val isUserMessage: Boolean) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val l1 = ArrayList<MyServiceDataItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            if (isUserMessage) {
                View.inflate(parent.context, R.layout.item_chat_text_right, null)
            } else {
                View.inflate(parent.context, R.layout.item_chat_text_left, null)
            }
        )
    }

    override fun getItemCount(): Int {
        return l1.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(l1[position])
    }


    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private lateinit var obj: MyServiceDataItem

        init { }

        fun bind(data: MyServiceDataItem) = with(itemView) {

        }
    }

    fun setChatList(list: List<MyServiceDataItem>) {
        l1.clear()
        l1.addAll(list)
        notifyDataSetChanged()
    }

}
