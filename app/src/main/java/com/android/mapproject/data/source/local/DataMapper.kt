package com.android.mapproject.data.source.local

import com.android.mapproject.data.source.remote.ParkingDataRecord
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class DataMapper @Inject constructor() {

    fun fromDb(from: ParkingPlaceEntity): ParkingDataRecord {
        val place = ParkingDataRecord()

        place.id = from.id
        place.area = from.area
        place.name = from.name
        place.type = from.type
        place.summary = from.summary
        place.address = from.address
        place.tel = from.tel
        place.payEx = from.payEx
        place.serviceTime = from.serviceTime
        place.tw97x = from.tw97x
        place.tw97y = from.tw97y
        place.totalCar = from.totalCar
        place.totalMotor = from.totalMotor
        place.totalBike = from.totalBike

        return place
    }

    fun toDb(from: ParkingDataRecord): ParkingPlaceEntity {
        val place = ParkingPlaceEntity(from.id!!)

        place.area = from.area
        place.name = from.name
        place.type = from.type
        place.summary = from.summary
        place.address = from.address
        place.tel = from.tel
        place.payEx = from.payEx
        place.serviceTime = from.serviceTime
        place.tw97x = from.tw97x
        place.tw97y = from.tw97y
        place.totalCar = from.totalCar
        place.totalMotor = from.totalMotor
        place.totalBike = from.totalBike

        return place
    }
}