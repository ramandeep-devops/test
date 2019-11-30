package com.codebrew.encober.models.responseModel.start

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ServiceListItem(

	@field:SerializedName("serviceType")
	val serviceType: Int? = null,

	@field:SerializedName("loc")
	val loc: List<Double>? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("commissionPer")
	val commissionPer: Int? = null,

	@field:SerializedName("serviceName")
	val serviceName: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("long")
	val jsonMemberLong: Double? = null,

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("servicePrice")
	val servicePrice: Int? = null,

	@field:SerializedName("countryCode")
	val countryCode: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("lng")
	val lng: Double? = null
) : Parcelable {
	constructor(source: Parcel) : this(
		source.readValue(Int::class.java.classLoader) as Int?,
		ArrayList<Double>().apply { source.readList(this as List<Double>, Double::class.java.classLoader) },
		source.readString(),
		source.readValue(Boolean::class.java.classLoader) as Boolean?,
		source.readValue(Int::class.java.classLoader) as Int?,
		source.readString(),
		source.readString(),
		source.readValue(Double::class.java.classLoader) as Double?,
		source.readValue(Long::class.java.classLoader) as Long?,
		source.readString(),
		source.readValue(Boolean::class.java.classLoader) as Boolean?,
		source.readValue(Int::class.java.classLoader) as Int?,
		source.readString(),
		source.readString(),
		source.readValue(Double::class.java.classLoader) as Double?,
		source.readValue(Double::class.java.classLoader) as Double?
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeValue(serviceType)
		writeList(loc)
		writeString(address)
		writeValue(isBlocked)
		writeValue(commissionPer)
		writeString(serviceName)
		writeString(title)
		writeValue(jsonMemberLong)
		writeValue(createdAt)
		writeString(phoneNumber)
		writeValue(isDeleted)
		writeValue(servicePrice)
		writeString(countryCode)
		writeString(id)
		writeValue(lat)
		writeValue(lng)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<ServiceListItem> =
			object : Parcelable.Creator<ServiceListItem> {
				override fun createFromParcel(source: Parcel): ServiceListItem =
					ServiceListItem(source)

				override fun newArray(size: Int): Array<ServiceListItem?> = arrayOfNulls(size)
			}
	}
}