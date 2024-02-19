package com.example.videoclub

import android.os.Parcel
import android.os.Parcelable

data class PeliSerie(
    val backdrop_path: String?,
    val id: Int,
    val overview: String?,
    val poster_path: String?,
    val title: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(backdrop_path)
        parcel.writeInt(id)
        parcel.writeString(overview)
        parcel.writeString(poster_path)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PeliSerie> {
        override fun createFromParcel(parcel: Parcel): PeliSerie {
            return PeliSerie(parcel)
        }

        override fun newArray(size: Int): Array<PeliSerie?> {
            return arrayOfNulls(size)
        }
    }
}
