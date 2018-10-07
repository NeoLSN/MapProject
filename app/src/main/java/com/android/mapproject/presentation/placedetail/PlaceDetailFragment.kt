package com.android.mapproject.presentation.placedetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.mapproject.R
import com.android.mapproject.databinding.FragmentPlaceDetailBinding
import com.android.mapproject.di.androidx.AndroidXInjection
import com.android.mapproject.presentation.placedetail.PlaceDetailFragmentArgs.fromBundle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class PlaceDetailFragment :
        Fragment(),
        OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: PlaceDetailViewModelFactory

    private lateinit var viewDataBinding: FragmentPlaceDetailBinding

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
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewDataBinding.viewModel?.run {
            getParkingPlace(fromBundle(arguments).placeId)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        viewDataBinding.viewModel?.run {
            val place = place.get()
            coordinate.observe(this@PlaceDetailFragment, Observer { coordinate ->
                map?.let {
                    val markerOptions = MarkerOptions()
                            .position(coordinate)
                            .title(place?.name)
                    val marker = it.addMarker(markerOptions)
                    marker.tag = 0

                    val move = moveCamera(coordinate)
                    map.moveCamera(move)
                    val zoom = CameraUpdateFactory.zoomTo(17f)
                    map.animateCamera(zoom)
                }
            })
        }
    }

    private fun moveCamera(coordinate: LatLng): CameraUpdate {
        val builder = LatLngBounds.Builder()
        builder.include(coordinate)
        val bounds = builder.build()
        val padding = 10
        return CameraUpdateFactory.newLatLngBounds(bounds, padding)
    }
}