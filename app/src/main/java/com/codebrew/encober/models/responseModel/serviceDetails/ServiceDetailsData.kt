package com.codebrew.encober.models.responseModel.serviceDetails

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ServiceDetailsData(

    @field:SerializedName("serviceType")
    val serviceType: Int? = null,

    @field:SerializedName("loc")
    val loc: List<Double>? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("lng")
    val lng: Double? = null,

    @field:SerializedName("serviceName")
    val serviceName: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: Long? = null,

    @field:SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    @field:SerializedName("servicePrice")
    val servicePrice: Int? = null,

    @field:SerializedName("countryCode")
    val countryCode: String? = null,

    @field:SerializedName("isAdded")
    val isAdded: Boolean? = null,

    @field:SerializedName("userCommentsData")
    val userCommentsData: UserCommentsData? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("lat")
    val lat: Double? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Int::class.java.classLoader) as Int?,
        ArrayList<Double>().apply {
            source.readList(
                this as List<*>,
                Double::class.java.classLoader
            )
        },
        source.readString(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readString(),
        source.readString(),
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readParcelable<UserCommentsData>(UserCommentsData::class.java.classLoader),
        source.readString(),
        source.readValue(Double::class.java.classLoader) as Double?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(serviceType)
        writeList(loc)
        writeString(address)
        writeValue(lng)
        writeString(serviceName)
        writeString(title)
        writeValue(createdAt)
        writeString(phoneNumber)
        writeValue(servicePrice)
        writeString(countryCode)
        writeValue(isAdded)
        writeParcelable(userCommentsData, 0)
        writeString(id)
        writeValue(lat)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ServiceDetailsData> =
            object : Parcelable.Creator<ServiceDetailsData> {
                override fun createFromParcel(source: Parcel): ServiceDetailsData =
                    ServiceDetailsData(source)

                override fun newArray(size: Int): Array<ServiceDetailsData?> = arrayOfNulls(size)
            }
    }
}