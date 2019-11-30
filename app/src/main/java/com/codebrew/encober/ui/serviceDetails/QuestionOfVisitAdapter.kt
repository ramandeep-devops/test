package com.codebrew.encober.ui.serviceDetails

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.serviceDetails.questionsOfVisit.QuestionOfVisitItem
import com.codebrew.encober.utils.extensions.gone
import com.codebrew.encober.utils.extensions.visible
import kotlinx.android.synthetic.main.item_result_got.view.*

class QuestionOfVisitAdapter(
    private val reasonList: ArrayList<QuestionOfVisitItem>,
    private val callback: OnClickDialogCallback
) :
    RecyclerView.Adapter<QuestionOfVisitAdapter.ViewHolder>() {

    private var lastCheckedPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v1 = View.inflate(parent.context, R.layout.item_result_got, null)
        return ViewHolder(v1)
    }

    override fun getItemCount(): Int {
        return reasonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reasonList[position])
    }


    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private lateinit var obj: QuestionOfVisitItem

        init {
            itemView.setOnClickListener {
                lastCheckedPos = adapterPosition
                obj.isSelected = true
                notifyDataSetChanged()
                callback.onClickQuestionOfVisit(obj)
            }
        }

        fun bind(data: QuestionOfVisitItem) = with(itemView) {
            obj = data
            tvResult.text = data.questionSp
            ivCheckResultGot.gone()

            if (lastCheckedPos == adapterPosition) {
                ivCheckResultGot.visible()
            }
        }
    }


}
