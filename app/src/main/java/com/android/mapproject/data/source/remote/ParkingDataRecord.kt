package com.android.mapproject.data.source.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by JasonYang.
 */
class ParkingDataRecord {

    @SerializedName("ID")
    @Expose
    var id: String? = null

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
}