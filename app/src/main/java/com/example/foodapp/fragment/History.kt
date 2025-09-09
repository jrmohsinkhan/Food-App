package com.example.foodapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapter.historyItemAdapter
import com.example.foodapp.databinding.FragmentHistoryBinding
import com.example.foodapp.databinding.HistoryItemBinding

class History : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodNames = mutableListOf<String>("Burger", "Naan chapati", "pizza max", "jiye Bhutto")
        val foodPrices = mutableListOf<String>("$6", "$10", "$5", "$100")
        val foodImages = mutableListOf<Int>(R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4)

        val adapter = historyItemAdapter(foodNames, foodPrices, foodImages)
        binding.historyRecyclerView.adapter = adapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    companion object {
    }
}