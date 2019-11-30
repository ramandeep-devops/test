package com.codebrew.encober.models.responseModel.profileSetup

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BankListItem(

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("bankName")
	val bankName: String? = null,

	@field:SerializedName("_id")
	val id: String? = null
) : Parcelable {
	constructor(source: Parcel) : this(
		source.readValue(Long::class.java.classLoader) as Long?,
		source.readValue(Boolean::class.java.classLoader) as Boolean?,
		source.readValue(Boolean::class.java.classLoader) as Boolean?,
		source.readString(),
		source.readString()
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeValue(createdAt)
		writeValue(isDeleted)
		writeValue(isBlocked)
		writeString(bankName)
		writeString(id)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<BankListItem> = object : Parcelable.Creator<BankListItem> {
			override fun createFromParcel(source: Parcel): BankListItem = BankListItem(source)
			override fun newArray(size: Int): Array<BankListItem?> = arrayOfNulls(size)
		}
	}
}