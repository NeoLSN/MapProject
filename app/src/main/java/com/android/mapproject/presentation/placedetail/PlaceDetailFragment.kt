package com.android.mapproject.presentation.placedetail

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.mapproject.databinding.FragmentPlaceDetailBinding
import com.android.mapproject.di.androidx.AndroidXInjection
import com.android.mapproject.presentation.ViewModelFactory
import com.android.mapproject.presentation.placedetail.PlaceDetailFragmentArgs.fromBundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class PlaceDetailFragment : Fragment(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewDataBinding: FragmentPlaceDetailBinding
    private lateinit var mapView: MapView
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context?) {
        AndroidXInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentPlaceDetailBinding
                .inflate(inflater, container, false)
                .apply {
                    viewModel = ViewModelProviders
                            .of(this@PlaceDetailFragment, viewModelFactory)
                            .get(PlaceDetailViewModel::class.java)
                }

        with(viewDataBinding.map) {
            mapView = this
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this@PlaceDetailFragment)
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.viewModel?.run {
            getParkingPlace(fromBundle(arguments).placeId)
        }

        checkPlayServicesAvailable()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        disposables.clear()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun checkPlayServicesAvailable() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val status = apiAvailability.isGooglePlayServicesAvailable(context)

        if (status != ConnectionResult.SUCCESS) {
            viewDataBinding.planningPath.isEnabled = false
            if (apiAvailability.isUserResolvableError(status)) {
                apiAvailability.getErrorDialog(activity, status, 1).show()
            } else {
                Snackbar.make(view!!, "Google Play Services unavailable. This app will not work", Snackbar.LENGTH_INDEFINITE).show()
            }
        } else {
            viewDataBinding.planningPath.run {
                isEnabled = true
                setOnClickListener { planningPath() }
            }
        }
    }

    private fun planningPath() {
        val rxPermissions = RxPermissions(this)
        disposables += rxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribeBy(
                        onNext = { granted ->
                            if (granted) viewDataBinding.viewModel?.getCurrentLocation()
                        },
                        onError = { e -> Log.w("PlaceDetailViewModel", "Location wrong $e") }
                )
    }

    override fun onMapReady(map: GoogleMap?) {
        viewDataBinding.viewModel?.run {
            val place = place.get()
            destination.observe(this@PlaceDetailFragment, Observer { coordinate ->
                map?.let {
                    val markerOptions = MarkerOptions()
                            .position(coordinate)
                            .title(place?.name)
                    it.addMarker(markerOptions)

                    val move = moveCamera(coordinate)
                    map.moveCamera(move)
                    val zoom = CameraUpdateFactory.zoomTo(17f)
                    map.animateCamera(zoom)
                }
            })

            locations.observe(this@PlaceDetailFragment, Observer {
                val origin = it.first
                val dest = it.second
                map?.let { map ->
                    map.clear()

                    val sMarkerOptions = MarkerOptions()
                            .position(origin)
                    map.addMarker(sMarkerOptions)

                    val eMarkerOptions = MarkerOptions()
                            .position(dest)
                            .title(place?.name)
                    map.addMarker(eMarkerOptions)

                    val move = moveCamera(origin, dest)
                    map.animateCamera(move)
                }
                calculateRoute(origin, dest)
            })

            route.observe(this@PlaceDetailFragment, Observer {
                val route = PolylineOptions()
                        .addAll(it)
                        .color(Color.parseColor("#039BE5"))
                map?.addPolyline(route)
            })
        }
    }

    private fun moveCamera(vararg coordinates: LatLng): CameraUpdate {
        val builder = LatLngBounds.Builder()
        coordinates.forEach { coordinate ->
            builder.include(coordinate)
        }
        val bounds = builder.build()
        val padding = 10
        return CameraUpdateFactory.newLatLngBounds(bounds, padding)
    }
}