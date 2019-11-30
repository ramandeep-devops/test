package com.codebrew.encober.models.responseModel.serviceDetails

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UserCommentsData(

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("questionSp")
	val questionSp: String? = null,

	@field:SerializedName("serviceId")
	val serviceId: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("questionEn")
	val questionEn: String? = null
) : Parcelable {
	constructor(source: Parcel) : this(
		source.readValue(Long::class.java.classLoader) as Long?,
		source.readString(),
		source.readString(),
		source.readString(),
		source.readString()
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeValue(createdAt)
		writeString(questionSp)
		writeString(serviceId)
		writeString(userId)
		writeString(questionEn)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<UserCommentsData> =
			object : Parcelable.Creator<UserCommentsData> {
				override fun createFromParcel(source: Parcel): UserCommentsData =
					UserCommentsData(source)

				override fun newArray(size: Int): Array<UserCommentsData?> = arrayOfNulls(size)
			}
	}
}