package com.codebrew.encober.models.responseModel.videosData

import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class VideoListingItem(

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("videoUrl")
	val videoUrl: String? = null,

	@field:SerializedName("videoName")
	val videoName: String? = null,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("videoImage")
	val videoImage: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("_id")
	val id: String? = null
) : Parcelable {
	override fun equals(obj: Any?): Boolean {
		if (obj === this)
			return true

		val article = obj as VideoListingItem?
		return article!!.id == this.id
	}

	constructor(source: Parcel) : this(
		source.readValue(Long::class.java.classLoader) as Long?,
		source.readValue(Boolean::class.java.classLoader) as Boolean?,
		source.readString(),
		source.readString(),
		source.readValue(Boolean::class.java.classLoader) as Boolean?,
		source.readString(),
		source.readString(),
		source.readString()
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeValue(createdAt)
		writeValue(isDeleted)
		writeString(videoUrl)
		writeString(videoName)
		writeValue(isBlocked)
		writeString(videoImage)
		writeString(description)
		writeString(id)
	}



	companion object {
		var DIFF_CALLBACK: DiffUtil.ItemCallback<VideoListingItem> =
			object : DiffUtil.ItemCallback<VideoListingItem>() {
				override fun areItemsTheSame(
					oldItem: VideoListingItem,
					newItem: VideoListingItem
				): Boolean {
					return oldItem.id == newItem.id
				}

				override fun areContentsTheSame(
					oldItem: VideoListingItem,
					newItem: VideoListingItem
				): Boolean {
					return oldItem == newItem
				}
			}

		@JvmField
		val CREATOR: Parcelable.Creator<VideoListingItem> =
			object : Parcelable.Creator<VideoListingItem> {
				override fun createFromParcel(source: Parcel): VideoListingItem =
					VideoListingItem(source)

				override fun newArray(size: Int): Array<VideoListingItem?> = arrayOfNulls(size)
			}
	}
}