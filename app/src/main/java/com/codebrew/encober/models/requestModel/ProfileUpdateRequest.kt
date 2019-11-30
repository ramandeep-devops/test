package com.codebrew.encober.models.requestModel

import android.os.Parcel
import android.os.Parcelable

data class ProfileUpdateRequest(
    var firstName: String? = null,
    var lastName: String? = null,
    var original: String? = null,
    var thumbnail: String? = null,
    var street: String? = null,
    var dob: Long? = null,
    var outsideNo: String? = null,
    var interiorNo: String? = null,
    var postalCode: String? = null,
    var city: String? = null,
    var state: String? = null,
    var delegationOrMunicipality: String? = null,
    var countryCode: String? = null,
    var phoneNumber: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(firstName)
        writeString(lastName)
        writeString(original)
        writeString(thumbnail)
        writeString(street)
        writeValue(dob)
        writeString(outsideNo)
        writeString(interiorNo)
        writeString(postalCode)
        writeString(city)
        writeString(state)
        writeString(delegationOrMunicipality)
        writeString(countryCode)
        writeString(phoneNumber)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProfileUpdateRequest> =
            object : Parcelable.Creator<ProfileUpdateRequest> {
                override fun createFromParcel(source: Parcel): ProfileUpdateRequest =
                    ProfileUpdateRequest(source)

                override fun newArray(size: Int): Array<ProfileUpdateRequest?> = arrayOfNulls(size)
            }
    }
}