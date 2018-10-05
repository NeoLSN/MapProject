package com.android.mapproject.data.source.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by JasonYang.
 */
class ParkingDataResponse {

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("result")
    @Expose
    var result: ParkingDataResult? = null

}