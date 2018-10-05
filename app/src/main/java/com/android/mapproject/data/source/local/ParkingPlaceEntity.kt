package com.android.mapproject.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by JasonYang.
 */
@Entity(tableName = "parking_places")
data class ParkingPlaceEntity(@PrimaryKey val id: String) {

    @ColumnInfo(name = "area")
    var area: String? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "type")
    var type: String? = null

    @ColumnInfo(name = "summary")
    var summary: String? = null

    @ColumnInfo(name = "address")
    var address: String? = null

    @ColumnInfo(name = "tel")
    var tel: String? = null

    @ColumnInfo(name = "pay_ex")
    var payEx: String? = null

    @ColumnInfo(name = "service_time")
    var serviceTime: String? = null

    @ColumnInfo(name = "tw97x")
    var tw97x: String? = null

    @ColumnInfo(name = "tw97y")
    var tw97y: String? = null

    @ColumnInfo(name = "total_car")
    var totalCar: String? = null

    @ColumnInfo(name = "total_motor")
    var totalMotor: String? = null

    @ColumnInfo(name = "total_bike")
    var totalBike: String? = null
}