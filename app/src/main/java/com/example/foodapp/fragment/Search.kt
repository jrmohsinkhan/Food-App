package com.example.foodapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.adapter.VenueAdapter
import com.example.foodapp.databinding.FragmentSearchBinding
import com.example.foodapp.model.Venue
import com.example.foodapp.apiService.VenueApi
import kotlinx.coroutines.launch

class Search : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: VenueAdapter

    // Full list of venues from API
    private val venues = mutableListOf<Venue>()

    // Filtered list for search
    private val filteredVenues = mutableListOf<Venue>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VenueAdapter(filteredVenues)
        binding.menuRecyclerView.adapter = adapter
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch venues from API
        fetchVenues()

        // Setup search
        setUpSearchView()
    }

    private fun fetchVenues() {
        lifecycleScope.launch {
            val result = VenueApi.getVenues()
            if (result.isSuccess) {
                venues.clear()
                venues.addAll(result.getOrDefault(emptyList()))
                showAllVenues()
            } else {
                // Handle error, show toast or snackbar
                val errorMessage = result.exceptionOrNull()?.message ?: "Failed to fetch venues"
                // e.g., Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAllVenues() {
        filteredVenues.clear()
        filteredVenues.addAll(venues)
        adapter.notifyDataSetChanged()
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterVenues(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterVenues(newText)
                return true
            }
        })
    }

    private fun filterVenues(query: String) {
        filteredVenues.clear()
        filteredVenues.addAll(
            venues.filter {
                it.venueName.contains(query, ignoreCase = true) ||
                        it.city.contains(query, ignoreCase = true)
            }
        )
        adapter.notifyDataSetChanged()
    }
}
