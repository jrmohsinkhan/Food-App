package com.example.foodapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.adapter.menuItemAdapter
import com.example.foodapp.databinding.CartItemBinding
import com.example.foodapp.databinding.FragmentFoodMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FoodMenuFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFoodMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFoodMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            dismiss()
        }

        val items = mutableListOf<String>(
            "Burger",
            "Naan chapati",
            "pizza max",
            "jiye Bhutto",
            "jiye Bhutto",
            "jiye Bhutto",
            "Burger",
            "Naan chapati",
            "pizza max",
            "jiye Bhutto",
            "jiye Bhutto",
            "jiye Bhutto"
        )
        val prices = mutableListOf<String>(
            "$6",
            "$10",
            "$5",
            "$100",
            "$100",
            "$100",
            "$6",
            "$10",
            "$5",
            "$100",
            "$100",
            "$100"
        )
        val images = mutableListOf(
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu5,
            R.drawable.menu6,
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu5,
            R.drawable.menu6,
        )

        val adapter = menuItemAdapter(items, prices, images)
        val view = binding.menuRecyclerView
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
    }
}