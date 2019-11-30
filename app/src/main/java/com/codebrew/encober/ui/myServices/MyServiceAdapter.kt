package com.codebrew.encober.ui.myServices

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.myServices.MyServiceDataItem
import com.codebrew.encober.utils.AppConstants
import kotlinx.android.synthetic.main.item_home_collection.view.*

class MyServiceAdapter(private val callback: OnClickMyServiceItemCallback) :
    RecyclerView.Adapter<MyServiceAdapter.ViewHolder>() {

    private val l1 = ArrayList<MyServiceDataItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v1 = View.inflate(parent.context, R.layout.item_home_collection, null)
        return ViewHolder(v1)
    }

    override fun getItemCount(): Int {
        return l1.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(l1[position])
    }


    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private lateinit var obj : MyServiceDataItem

        init {
            itemView.ivItemCollectionUserIcon.setOnClickListener {
                callback.onClickMyServiceItem(obj,AppConstants.TYPE_FOR_DELETE)
            }

            itemView.btnItemCollectionCommission.setOnClickListener {
                callback.onClickMyServiceItem(obj,AppConstants.TYPE_FOR_DETAILS)
            }

        }

        fun bind(data: MyServiceDataItem) = with(itemView) {
            obj = data
            ivItemCollectionUserIcon.setImageResource(R.drawable.ic_delete)
            tvItemCollectionTitle.text = data.myServiceData?.title
            tvItemCollectionName.text = data.myServiceData?.serviceName
            tvItemCollectionAddress.text = data.myServiceData?.address
            val commission =
                (data.myServiceData?.servicePrice?.times(data.myServiceData.commissionPer!!))?.div(100).toString()
            btnItemCollectionCommission.text = "Commission $$commission"
        }
    }

    fun setData(list: List<MyServiceDataItem>) {
        l1.clear()
        l1.addAll(list)
        notifyDataSetChanged()
    }

}
