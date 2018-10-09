package com.android.mapproject.presentation.places

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.android.mapproject.domain.model.ParkingPlace
import com.android.mapproject.domain.usecase.FilterParkingPlacesUseCase
import com.android.mapproject.domain.usecase.GetParkingPlacesUseCase
import com.android.mapproject.domain.usecase.RefreshParkingPlacesUseCase
import com.android.mapproject.presentation.BaseViewModel
import com.android.mapproject.util.rx.SchedulerProvider
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class ParkingPlacesViewModel @Inject constructor(
        private val refresh: RefreshParkingPlacesUseCase,
        private val getPlaces: GetParkingPlacesUseCase,
        private val filter: FilterParkingPlacesUseCase,
        private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    val places = MutableLiveData<List<ParkingPlace>>()
    val isRefreshing by lazy {
        val bool = MutableLiveData<Boolean>()
        bool.postValue(false)
        bool
    }
    val searchTerm = ObservableField<String>()

    private var isLoaded = false
    private val subject = PublishSubject.create<String>()

    init {
        val worker = subject
                .debounce(500, TimeUnit.MILLISECONDS)
                .doOnNext { searchTerm.set(it) }
                .switchMap { term ->
                    if (term.isBlank()) getPlaces.allPlaces().toObservable()
                    else filter.filter(term).toObservable()
                }
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                        onNext = { places.postValue(it) },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Filter places error: $e") }
                )
        disposables += worker
    }

    fun allPlaces(forceReload: Boolean = false) {
        if (isLoaded && !forceReload) return
        searchTerm.set("")
        disposables += getPlaces.allPlaces()
                .observeOn(schedulerProvider.ui())
                .firstOrError()
                .subscribeBy(
                        onSuccess = {
                            isLoaded = true
                            if (it.isEmpty()) refreshParkingPlaces()
                            places.postValue(it)
                        },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Get places error: $e") }
                )
    }

    fun refreshParkingPlaces() {
        if (isRefreshing.value == true) return
        disposables += refresh.refreshPlaces()
                .doOnSubscribe { isRefreshing.postValue(true) }
                .observeOn(schedulerProvider.ui())
                .doAfterTerminate { isRefreshing.postValue(false) }
                .subscribeBy(
                        onComplete = { allPlaces(true) },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Refresh error: $e") }
                )
    }

    fun filterPlaces(term: String) {
        subject.onNext(term)
    }

    override fun onCleared() {
        super.onCleared()
        subject.onComplete()
    }
}