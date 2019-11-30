package com.codebrew.encober.models.requestModel

import android.os.Parcel
import android.os.Parcelable

data class FinalizePaymentNotificationRequest(

    var serviceId: String? = null,
    var agreedAmount: Double? = null,
    var promiseDate: Long? = null,
    var SignatureOriginal: String? = null,
    var SignatureThumbnail: String? = null,
    var questionEn: String? = null,
    var questionSp: String? = null

) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(serviceId)
        writeValue(agreedAmount)
        writeValue(promiseDate)
        writeString(SignatureOriginal)
        writeString(SignatureThumbnail)
        writeString(questionEn)
        writeString(questionSp)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FinalizePaymentNotificationRequest> =
            object : Parcelable.Creator<FinalizePaymentNotificationRequest> {
                override fun createFromParcel(source: Parcel): FinalizePaymentNotificationRequest =
                    FinalizePaymentNotificationRequest(source)

                override fun newArray(size: Int): Array<FinalizePaymentNotificationRequest?> =
                    arrayOfNulls(size)
            }
    }
}