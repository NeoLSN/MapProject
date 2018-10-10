package com.android.mapproject.presentation.places

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.android.mapproject.domain.model.ParkingPlace
import com.android.mapproject.domain.usecase.FilterParkingPlacesUseCase
import com.android.mapproject.domain.usecase.GetParkingPlacesUseCase
import com.android.mapproject.domain.usecase.RefreshParkingPlacesUseCase
import com.android.mapproject.presentation.BaseViewModel
import com.android.mapproject.presentation.common.Result
import com.android.mapproject.presentation.common.toResult
import com.android.mapproject.util.rx.SchedulerProvider
import io.reactivex.rxkotlin.addTo
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

    val places = MutableLiveData<Result<List<ParkingPlace>>>()
    val searchTerm = ObservableField<String>()
    val refreshState = MutableLiveData<Result<*>>()

    private var isLoaded = false
    private val subject = PublishSubject.create<String>()

    init {
        subject.debounce(500, TimeUnit.MILLISECONDS)
                .doOnNext { searchTerm.set(it) }
                .switchMap { term ->
                    if (term.isBlank()) getPlaces.allPlaces().toObservable()
                    else filter.filter(term).toObservable()
                }
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe { places.postValue(it) }
                .addTo(disposables)
    }

    fun allPlaces(forceReload: Boolean = false) {
        if (isLoaded && !forceReload) return
        searchTerm.set("")
        getPlaces.allPlaces()
                .firstOrError()
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe {
                    places.postValue(it)
                    if (it is Result.Success) {
                        isLoaded = true
                        if (it.data.isEmpty()) refreshParkingPlaces()
                    }
                }
                .addTo(disposables)
    }

    fun refreshParkingPlaces() {
        if (refreshState.value is Result.InProgress) return
        refresh.refreshPlaces()
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe {
                    refreshState.postValue(it)
                    if (it is Result.Success) allPlaces(true)
                }
                .addTo(disposables)
    }

    fun filterPlaces(term: String) {
        subject.onNext(term)
    }

    override fun onCleared() {
        super.onCleared()
        subject.onComplete()
    }
}