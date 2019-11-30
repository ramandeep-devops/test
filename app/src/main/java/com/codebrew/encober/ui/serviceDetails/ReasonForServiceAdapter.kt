package com.codebrew.encober.ui.serviceDetails

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.serviceDetails.reasons.ReasonForServiceItem
import kotlinx.android.synthetic.main.item_happened_on_visit.view.*

class ReasonForServiceAdapter(private val reasonList: ArrayList<ReasonForServiceItem>, private val callback: OnClickDialogCallback) :
    RecyclerView.Adapter<ReasonForServiceAdapter.ViewHolder>() {

    private var lastCheckedPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v1 = View.inflate(parent.context, R.layout.item_happened_on_visit, null)
        return ViewHolder(v1)
    }

    override fun getItemCount(): Int {
        return reasonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reasonList[position])
    }


    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private lateinit var obj: ReasonForServiceItem

        init {
            itemView.setOnClickListener {
                lastCheckedPos = adapterPosition
                obj.isSelected = true
                notifyDataSetChanged()
                callback.onClickReason(obj)
            }
        }

        fun bind(data: ReasonForServiceItem) = with(itemView) {
            obj = data
            tvHappenVisitReason.text = data.questionEn
            chkBoxHappenVisit.isChecked = false
            if (lastCheckedPos == adapterPosition) {
                chkBoxHappenVisit.isChecked = true
            }
        }
    }




}
