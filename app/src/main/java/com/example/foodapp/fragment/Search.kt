package com.example.foodapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapter.menuItemAdapter
import com.example.foodapp.databinding.FragmentSearchBinding

class Search : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: menuItemAdapter

    private val items = mutableListOf<String>(
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
    private val prices = mutableListOf<String>(
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
    private val images = mutableListOf(
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

    private val filterItems = mutableListOf<String>()
    private val filterPrices = mutableListOf<String>()
    private val filterImages = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = menuItemAdapter(filterItems, filterPrices, filterImages)
        val view = binding.menuRecyclerView
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(requireContext())

        //for search
        setUpSearchView()


        // for all menu items
        showAllMenu()
    }

    private fun showAllMenu() {
        filterItems.clear()
        filterPrices.clear()
        filterImages.clear()

        filterItems.addAll(items)
        filterPrices.addAll(prices)
        filterImages.addAll(images)

        adapter.notifyDataSetChanged()

    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {
        filterItems.clear()
        filterPrices.clear()
        filterImages.clear()

        items.forEachIndexed { index, item ->
            if (item.contains(query, ignoreCase = true)){
                filterItems.add(item)
                filterPrices.add(prices[index])
                filterImages.add(images[index])
        } }

        adapter.notifyDataSetChanged()
    }

    companion object {
    }
}