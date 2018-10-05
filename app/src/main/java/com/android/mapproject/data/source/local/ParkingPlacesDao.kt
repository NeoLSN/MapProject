package com.android.mapproject.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

/**
 * Created by JasonYang.
 */
@Dao
interface ParkingPlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(place: ParkingPlaceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(places: List<ParkingPlaceEntity>)

    @Query("SELECT * FROM parking_places WHERE id = :id LIMIT 1")
    fun placeById(id: String): Flowable<ParkingPlaceEntity>

    @Query("SELECT * FROM parking_places ORDER BY id ASC")
    fun allPlaces(): Flowable<List<ParkingPlaceEntity>>
}