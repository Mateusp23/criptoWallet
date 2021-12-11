package com.example.cryptowalet.models

import android.os.Parcel
import android.os.Parcelable

data class Buy(
    var id: Int?,
    val name: String?,
    val date: String?,
    val quantity: Double?,
    var value: Double?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        this.id?.let { parcel.writeInt(it) }
        parcel.writeString(name)
        parcel.writeString(date)
        if (quantity != null) {
            parcel.writeDouble(quantity)
        }
        if (value != null) {
            parcel.writeDouble(value!!)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Action> {
        override fun createFromParcel(parcel: Parcel): Action {
            return Action(parcel)
        }

        override fun newArray(size: Int): Array<Action?> {
            return arrayOfNulls(size)
        }
    }
}