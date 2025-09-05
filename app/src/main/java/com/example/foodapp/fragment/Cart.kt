package com.example.foodapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapter.cartItemAdapter
import com.example.foodapp.databinding.CartItemBinding
import com.example.foodapp.databinding.FragmentCartBinding

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
    }



    companion object {
    }
}