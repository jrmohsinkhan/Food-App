package com.example.foodapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapter.cartItemAdapter
import com.example.foodapp.apiService.NotificationApi
import com.example.foodapp.databinding.CartItemBinding
import com.example.foodapp.databinding.FragmentCartBinding
import com.example.foodapp.model.ConfirmationResult
import kotlinx.coroutines.launch

class Cart : Fragment() {

    private lateinit var binding: FragmentCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = mutableListOf(
            "bhaya the great",
            "french fries",
            "barish ka pani",
            "gherat ke gont",
            "Jiye bhutto"
        )
        val prices = mutableListOf(
            "$33",
            "$33",
            "$33",
            "$33",
            "$33"
        )
        val images = mutableListOf(
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu5,
        )

        val adapter = cartItemAdapter(items, prices, images){ position ->
            Toast.makeText(requireContext(), "Item removed at position $position", Toast.LENGTH_SHORT).show()
        }
        binding.cartItemRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartItemRecyclerView.adapter = adapter

        binding.proceedButton.setOnClickListener {
            confirmBooking()
        }
    }

    private fun confirmBooking() {
        lifecycleScope.launch {
            // You might want to show a loading indicator here (e.g., binding.progressBar.visibility = View.VISIBLE)

            when (val result = NotificationApi.confirmBooking()) {

                is ConfirmationResult.Success -> {
                    // Success Case: API call succeeded (HTTP 200)
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                    // Add navigation logic or cart clearing here
                }

                is ConfirmationResult.Error -> {
                    // API Error Case: Server returned an explicit error (e.g., 400 No tokens)
                    Toast.makeText(requireContext(), "Error: ${result.message}", Toast.LENGTH_LONG).show()
                }

                is ConfirmationResult.NetworkError -> {
                    // Network/Server Failure Case: Connection issues or unhandled 5xx errors
                    Toast.makeText(requireContext(), "Network Failed: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }

            // You might want to hide the loading indicator here (e.g., binding.progressBar.visibility = View.GONE)
        }
    }


    companion object {
    }
}