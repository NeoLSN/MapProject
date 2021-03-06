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
import androidx.navigation.fragment.navArgs
import com.android.mapproject.databinding.FragmentPlaceDetailBinding
import com.android.mapproject.di.androidx.AndroidXInjection
import com.android.mapproject.presentation.ViewModelFactory
import com.android.mapproject.presentation.common.Result
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import com.tbruyelle.rxpermissions2.RxPermissions
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class PlaceDetailFragment : Fragment(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewDataBinding: FragmentPlaceDetailBinding
    private lateinit var viewModel: PlaceDetailViewModel
    private lateinit var mapView: MapView
    private val args: PlaceDetailFragmentArgs by navArgs()

    override fun onAttach(context: Context?) {
        AndroidXInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentPlaceDetailBinding
                .inflate(inflater, container, false)
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PlaceDetailViewModel::class.java)

        with(viewDataBinding.map) {
            mapView = this
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this@PlaceDetailFragment)
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getParkingPlace(args.placeId)

        checkPlayServicesAvailable()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
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
        val request = rxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
        viewModel.requestLocation(request)
    }

    override fun onMapReady(map: GoogleMap?) {
        with(viewModel) {
            place.observe(this@PlaceDetailFragment, Observer {
                when (it) {
                    is Result.Success -> viewDataBinding.place = it.data
                    is Result.Failure -> Log.w("ParkingPlacesViewModel", "Get places error: ${it.e}")
                }
            })

            destination.observe(this@PlaceDetailFragment, Observer { dest ->
                val pValue = place.value
                val placeName = if (pValue is Result.Success) pValue.data.name else ""
                map?.let { map ->
                    val markerOptions = MarkerOptions()
                            .position(dest)
                            .title(placeName)
                    map.addMarker(markerOptions)

                    val move = moveCamera(dest)
                    map.moveCamera(move)
                    val zoom = CameraUpdateFactory.zoomTo(17f)
                    map.animateCamera(zoom)
                }
            })

            locations.observe(this@PlaceDetailFragment, Observer {
                when (it) {
                    is Result.Success -> {
                        val origin = it.data.first
                        val dest = it.data.second
                        val pValue = place.value
                        val placeName = if (pValue is Result.Success) pValue.data.name else ""
                        map?.let { map ->
                            map.clear()

                            val sMarkerOptions = MarkerOptions()
                                    .position(origin)
                            map.addMarker(sMarkerOptions)

                            val eMarkerOptions = MarkerOptions()
                                    .position(dest)
                                    .title(placeName)
                            map.addMarker(eMarkerOptions)

                            val move = moveCamera(origin, dest)
                            map.animateCamera(move)
                        }
                    }
                    is Result.Failure -> Log.w("PlaceDetailViewModel", "Location wrong ${it.e}")
                }
            })

            route.observe(this@PlaceDetailFragment, Observer {
                when (it) {
                    is Result.Success -> {
                        val route = PolylineOptions()
                                .addAll(it.data)
                                .color(Color.parseColor("#039BE5"))
                        map?.addPolyline(route)
                    }
                    is Result.Failure -> Log.w("PlaceDetailViewModel", "Calculate route error ${it.e}")
                }
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