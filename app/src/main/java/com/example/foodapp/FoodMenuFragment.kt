package com.example.foodapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.adapter.VenueAdapter
import com.example.foodapp.apiService.VenueApi
import com.example.foodapp.databinding.FragmentFoodMenuBinding
import com.example.foodapp.model.Venue
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class FoodMenuFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFoodMenuBinding
    private lateinit var adapter: VenueAdapter

    // Full list of venues
    private val venues = mutableListOf<Venue>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            dismiss()
        }

        adapter = VenueAdapter(venues)
        binding.menuRecyclerView.adapter = adapter
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch venues from API
        fetchVenues()
    }

    private fun fetchVenues() {
        lifecycleScope.launch {
            val result = VenueApi.getVenues()
            if (result.isSuccess) {
                venues.clear()
                venues.addAll(result.getOrDefault(emptyList()))
                adapter.notifyDataSetChanged()
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Failed to fetch venues"
                // TODO: Show error to user, e.g., Toast or Snackbar
            }
        }
    }
}
