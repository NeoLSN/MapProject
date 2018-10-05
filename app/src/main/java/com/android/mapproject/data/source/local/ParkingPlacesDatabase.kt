package com.android.mapproject.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by JasonYang.
 */
@Database(
        entities = [ParkingPlaceEntity::class],
        version = 1,
        exportSchema = false
)
abstract class ParkingPlacesDatabase : RoomDatabase() {

    abstract fun placesDao(): ParkingPlacesDao

    companion object {
        fun getInstance(context: Context): ParkingPlacesDatabase =
                Room.databaseBuilder(context, ParkingPlacesDatabase::class.java, "parking_places_db")
                        .build()
    }
}