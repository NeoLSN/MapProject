package com.android.mapproject.presentation.places

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mapproject.databinding.FragmentParkingPlacesBinding
import com.android.mapproject.di.androidx.AndroidXInjection
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class ParkingPlacesFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ParkingPlacesViewModelFactory

    private lateinit var viewDataBinding: FragmentParkingPlacesBinding
    private lateinit var listAdapter: ParkingPlacesAdapter

    override fun onAttach(context: Context?) {
        AndroidXInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentParkingPlacesBinding
                .inflate(inflater, container, false)
                .apply {
                    viewModel = ViewModelProviders
                            .of(this@ParkingPlacesFragment, viewModelFactory)
                            .get(ParkingPlacesViewModel::class.java)
                }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListView()
    }

    private fun setupListView() {
        viewDataBinding.viewModel?.let {
            val lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            viewDataBinding.placeList.layoutManager = lm

            listAdapter = ParkingPlacesAdapter()
            viewDataBinding.placeList.adapter = listAdapter

            it.places.observe(this, Observer { list ->
                listAdapter.items = list
                listAdapter.notifyDataSetChanged()
            })
        }
    }

    override fun onStart() {
        super.onStart()

        viewDataBinding.viewModel?.allPlaces()
    }
}