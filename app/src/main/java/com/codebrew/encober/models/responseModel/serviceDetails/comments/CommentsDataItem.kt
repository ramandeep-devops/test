package com.codebrew.encober.models.responseModel.serviceDetails.comments

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class CommentsDataItem(

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("questionSp")
	val questionSp: String? = null,

	@field:SerializedName("userData")
	val userData: UserData? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("serviceId")
	val serviceId: String? = null,

	@field:SerializedName("questionEn")
	val questionEn: String? = null
){
	companion object{
		var DIFF_CALLBACK: DiffUtil.ItemCallback<CommentsDataItem> = object : DiffUtil.ItemCallback<CommentsDataItem>() {
			override  fun areItemsTheSame(oldItem: CommentsDataItem, newItem: CommentsDataItem): Boolean {
				return oldItem.id == newItem.id
			}

			override  fun areContentsTheSame(oldItem: CommentsDataItem, newItem: CommentsDataItem): Boolean {
				return oldItem == newItem
			}
		}
	}


	override fun equals(obj: Any?): Boolean {
		if (obj === this)
			return true

		val article = obj as CommentsDataItem?
		return article!!.id == this.id
	}
}