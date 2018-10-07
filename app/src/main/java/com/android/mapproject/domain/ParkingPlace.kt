package com.android.mapproject.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by JasonYang.
 */
data class ParkingPlace(
        @SerializedName("ID")
        @Expose
        var id: String? = null
) : Parcelable {

    @SerializedName("AREA")
    @Expose
    var area: String? = null

    @SerializedName("NAME")
    @Expose
    var name: String? = null

    @SerializedName("TYPE")
    @Expose
    var type: String? = null

    @SerializedName("SUMMARY")
    @Expose
    var summary: String? = null

    @SerializedName("ADDRESS")
    @Expose
    var address: String? = null

    @SerializedName("TEL")
    @Expose
    var tel: String? = null

    @SerializedName("PAYEX")
    @Expose
    var payEx: String? = null

    @SerializedName("SERVICETIME")
    @Expose
    var serviceTime: String? = null

    @SerializedName("TW97X")
    @Expose
    var tw97x: String? = null

    @SerializedName("TW97Y")
    @Expose
    var tw97y: String? = null

    @SerializedName("TOTALCAR")
    @Expose
    var totalCar: String? = null

    @SerializedName("TOTALMOTOR")
    @Expose
    var totalMotor: String? = null

    @SerializedName("TOTALBIKE")
    @Expose
    var totalBike: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        area = parcel.readString()
        name = parcel.readString()
        type = parcel.readString()
        summary = parcel.readString()
        address = parcel.readString()
        tel = parcel.readString()
        payEx = parcel.readString()
        serviceTime = parcel.readString()
        tw97x = parcel.readString()
        tw97y = parcel.readString()
        totalCar = parcel.readString()
        totalMotor = parcel.readString()
        totalBike = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(area)
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeString(summary)
        parcel.writeString(address)
        parcel.writeString(tel)
        parcel.writeString(payEx)
        parcel.writeString(serviceTime)
        parcel.writeString(tw97x)
        parcel.writeString(tw97y)
        parcel.writeString(totalCar)
        parcel.writeString(totalMotor)
        parcel.writeString(totalBike)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParkingPlace> {
        override fun createFromParcel(parcel: Parcel): ParkingPlace {
            return ParkingPlace(parcel)
        }

        override fun newArray(size: Int): Array<ParkingPlace?> {
            return arrayOfNulls(size)
        }
    }
}