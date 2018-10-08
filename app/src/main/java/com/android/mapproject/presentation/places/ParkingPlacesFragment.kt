package com.android.mapproject.presentation.places

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mapproject.R
import com.android.mapproject.databinding.FragmentParkingPlacesBinding
import com.android.mapproject.di.androidx.AndroidXInjection
import com.android.mapproject.domain.model.ParkingPlace
import com.android.mapproject.presentation.OnBackPressed
import com.android.mapproject.presentation.places.ParkingPlacesFragmentDirections.actionPlaceListToPlaceDetail
import java.util.*
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class ParkingPlacesFragment : Fragment(), OnBackPressed {

    @Inject
    lateinit var viewModelFactory: ParkingPlacesViewModelFactory

    private lateinit var viewDataBinding: FragmentParkingPlacesBinding
    private lateinit var listAdapter: ParkingPlacesAdapter
    private lateinit var searchView: SearchView

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

        setupSwipeRefreshLayout()

        setHasOptionsMenu(true)
    }

    private fun setupListView() {
        viewDataBinding.viewModel?.let {
            val lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            viewDataBinding.placeList.layoutManager = lm

            listAdapter = ParkingPlacesAdapter()
            viewDataBinding.placeList.adapter = listAdapter

            listAdapter.onItemClick = { place ->
                if (place is ParkingPlace) {
                    val navDirections = actionPlaceListToPlaceDetail(place.id!!)
                    view?.run { Navigation.findNavController(this).navigate(navDirections) }
                }
            }

            it.places.observe(this, Observer { list ->
                listAdapter.items = list
            })
        }
    }

    private fun setupSwipeRefreshLayout() {
        viewDataBinding.viewModel?.let {
            it.isRefreshing.observe(this, Observer { isRefreshing ->
                viewDataBinding.refreshLayout.isRefreshing = isRefreshing
            })
            viewDataBinding.refreshLayout.setScrollUpChild(viewDataBinding.placeList)
            viewDataBinding.refreshLayout.setOnRefreshListener {
                it.refreshParkingPlaces()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_menu, menu)

        searchView = menu?.findItem(R.id.action_search)
                ?.actionView as SearchView

        searchView.let { it ->

            it.setOnSearchClickListener {
                viewDataBinding.refreshLayout.isEnabled = false
            }

            it.setOnCloseListener {
                listAdapter.items = Collections.emptyList()
                viewDataBinding.viewModel?.allPlaces(true)
                viewDataBinding.refreshLayout.isEnabled = true
                false
            }

            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewDataBinding.viewModel?.filterPlaces(query)
                    return false
                }

                override fun onQueryTextChange(query: String): Boolean {
                    viewDataBinding.viewModel?.filterPlaces(query)
                    return false
                }
            })
        }

        viewDataBinding.viewModel?.let {
            val term = it.searchTerm.get()
            if (!term.isNullOrBlank()) {
                searchView.isIconified = false
                searchView.setQuery(term, true)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        viewDataBinding.viewModel?.allPlaces()
    }

    override fun onBackPressed(): Boolean {
        return if (!searchView.isIconified) {
            searchView.isIconified = true
            searchView.setQuery("", false)
            true
        } else false
    }
}