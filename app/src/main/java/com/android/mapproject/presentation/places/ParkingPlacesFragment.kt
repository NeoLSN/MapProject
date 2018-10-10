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
import com.android.mapproject.R
import com.android.mapproject.databinding.FragmentParkingPlacesBinding
import com.android.mapproject.di.androidx.AndroidXInjection
import com.android.mapproject.domain.model.ParkingPlace
import com.android.mapproject.presentation.OnBackPressed
import com.android.mapproject.presentation.ViewModelFactory
import com.android.mapproject.presentation.common.Result
import com.android.mapproject.presentation.places.ParkingPlacesFragmentDirections.actionPlaceListToPlaceDetail
import com.android.mapproject.util.recyclerview.setLinearDivider
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class ParkingPlacesFragment : Fragment(), OnBackPressed {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ParkingPlacesViewModel by lazy {
        ViewModelProviders
                .of(this, viewModelFactory)
                .get(ParkingPlacesViewModel::class.java)
    }

    private lateinit var viewDataBinding: FragmentParkingPlacesBinding
    private lateinit var listAdapter: ParkingPlacesAdapter
    private lateinit var searchView: SearchView

    override fun onAttach(context: Context?) {
        AndroidXInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentParkingPlacesBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListView()

        setupSwipeRefreshLayout()

        setHasOptionsMenu(true)
    }

    private fun setupListView() {
        viewDataBinding.placeList.apply {
            listAdapter = ParkingPlacesAdapter()
            adapter = listAdapter
            listAdapter.onItemClick = { place ->
                if (place is ParkingPlace) {
                    val navDirections = actionPlaceListToPlaceDetail(place.id!!)
                    view?.run { Navigation.findNavController(this).navigate(navDirections) }
                }
            }

            val lm = LinearLayoutManager(context)
            layoutManager = lm
            setLinearDivider(R.drawable.shape_divider_1dp_line, lm)
        }

        viewModel.places.observe(this@ParkingPlacesFragment, Observer { result ->
            viewDataBinding.refreshLayout.isRefreshing = result.inProgress
            if (result is Result.Success) listAdapter.items = result.data
        })

        viewModel.refreshState.observe(this@ParkingPlacesFragment, Observer { result ->
            viewDataBinding.refreshLayout.isRefreshing = result.inProgress
        })
    }

    private fun setupSwipeRefreshLayout() {
        viewDataBinding.refreshLayout.apply {
            setScrollUpChild(viewDataBinding.placeList)
            setOnRefreshListener { viewModel.refreshParkingPlaces() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_menu, menu)

        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        searchView.let { it ->

            it.setOnSearchClickListener {
                viewDataBinding.refreshLayout.isEnabled = false
            }

            it.setOnCloseListener {
                listAdapter.items = emptyList()
                viewDataBinding.refreshLayout.isEnabled = true
                viewModel.allPlaces(true)
                false
            }

            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.filterPlaces(query)
                    return false
                }

                override fun onQueryTextChange(query: String): Boolean {
                    viewModel.filterPlaces(query)
                    return false
                }
            })
        }

        with(viewModel) {
            val term = searchTerm.get()
            if (!term.isNullOrBlank()) {
                searchView.isIconified = false
                searchView.setQuery(term, true)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.allPlaces()
    }

    override fun onBackPressed(): Boolean {
        return if (!searchView.isIconified) {
            searchView.isIconified = true
            searchView.setQuery("", false)
            true
        } else false
    }
}