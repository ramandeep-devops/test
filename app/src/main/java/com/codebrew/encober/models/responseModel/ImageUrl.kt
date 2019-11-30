package com.codebrew.encober.models.responseModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ImageUrl(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("original")
	val original: String? = null
) : Parcelable {
	constructor(source: Parcel) : this(
		source.readString(),
		source.readString()
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeString(thumbnail)
		writeString(original)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<ImageUrl> = object : Parcelable.Creator<ImageUrl> {
			override fun createFromParcel(source: Parcel): ImageUrl = ImageUrl(source)
			override fun newArray(size: Int): Array<ImageUrl?> = arrayOfNulls(size)
		}
	}
}