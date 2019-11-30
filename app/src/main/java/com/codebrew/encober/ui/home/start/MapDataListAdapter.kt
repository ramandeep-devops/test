package com.codebrew.encober.ui.home.start

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.start.ServiceListItem
import com.codebrew.encober.utils.AppConstants
import kotlinx.android.synthetic.main.item_home_collection.view.*

class MapDataListAdapter(private val callback: OnClickMapDataListCallback) : RecyclerView.Adapter<MapDataListAdapter.ViewHolder>() {

    private val l1 = ArrayList<ServiceListItem>()


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
        private lateinit var obj : ServiceListItem
        init {
            itemView.ivItemCollectionUserIcon.setOnClickListener {
                callback.onClickMapListItem(obj,AppConstants.TYPE_FOR_ADD_SERVICE)
            }
            itemView.btnItemCollectionCommission.setOnClickListener {
                callback.onClickMapListItem(obj,AppConstants.TYPE_FOR_DETAILS)
            }

        }

        fun bind(data: ServiceListItem?) = with(itemView) {
            obj = data?: ServiceListItem()
            tvItemCollectionTitle.text = data?.title
            tvItemCollectionName.text = data?.serviceName
            tvItemCollectionAddress.text = data?.address
            val commission=
                (data?.servicePrice?.times(data.commissionPer!!))?.div(100).toString()
            btnItemCollectionCommission.text = "Commission $$commission"
        }
    }

    fun setData(list: List<ServiceListItem>) {
        l1.clear()
        l1.addAll(list)
        notifyDataSetChanged()
    }

}
