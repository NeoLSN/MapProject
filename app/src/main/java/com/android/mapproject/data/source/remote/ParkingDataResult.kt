package com.android.mapproject.data.source.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by JasonYang.
 */
class ParkingDataResult {

    @SerializedName("resource_id")
    @Expose
    var resourceId: String? = null

    @SerializedName("limit")
    @Expose
    var limit: Int? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("fields")
    @Expose
    var fields: List<ParkingDataField>? = null

    @SerializedName("records")
    @Expose
    var records: List<ParkingDataRecord>? = null
}