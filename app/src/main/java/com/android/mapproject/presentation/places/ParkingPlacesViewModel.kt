package com.android.mapproject.presentation.places

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.mapproject.domain.GetParkingPlacesUseCase
import com.android.mapproject.domain.ParkingPlace
import com.android.mapproject.domain.RefreshParkingPlacesUseCase
import com.android.mapproject.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by JasonYang.
 */
class ParkingPlacesViewModel(
        private val refresh: RefreshParkingPlacesUseCase,
        private val getPlaces: GetParkingPlacesUseCase
) : BaseViewModel() {

    val places = MutableLiveData<List<ParkingPlace>>()
    val isRefreshing by lazy {
        val bool = MutableLiveData<Boolean>()
        bool.postValue(false)
        bool
    }

    private var isLoaded = false

    fun allPlaces(forceReload: Boolean = false) {
        if (isLoaded && !forceReload) return
        disposables += getPlaces.allPlaces()
                .doOnSubscribe { isRefreshing.postValue(true) }
                .subscribeOn(Schedulers.io())
                .firstOrError()
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { isRefreshing.postValue(false) }
                .subscribeBy (
                        onSuccess = {
                            isLoaded = true
                            if (it.isEmpty()) refreshParkingPlaces()
                            places.value = it
                        },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Get places error: $e") }
                )
    }

    fun refreshParkingPlaces() {
        if (isRefreshing.value == true) return
        disposables += refresh.refreshParkingPlaces()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { isRefreshing.postValue(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { isRefreshing.postValue(false) }
                .subscribeBy(
                        onComplete = { allPlaces(true) },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Refresh error: $e") }
                )
    }
}