package com.example.foodapp.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Slide
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodapp.FoodMenuFragment

import com.example.foodapp.R
import com.example.foodapp.adapter.popularItemAdapter
import com.example.foodapp.databinding.FragmentFoodMenuBinding
import com.example.foodapp.databinding.FragmentHomeBinding


class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewMenu.setOnClickListener {
            val bottomSheetDialog = FoodMenuFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT ))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener{
            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "selected image $position"

                Toast.makeText(requireContext(),itemMessage, Toast.LENGTH_SHORT).show()
            }

            override fun doubleClick(position: Int) {
                val itemMessage = "Double clicked image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })

        val items = listOf<String>("Burger", "Naan chapati", "pizza max", "jiye Bhutto")
        val prices = listOf<String>("$6", "$10", "$5", "$100")
        val images = listOf(R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4)

        val adapter = popularItemAdapter(items,prices, images)
        binding.popularItemRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularItemRecyclerView.adapter = adapter




    }

    companion object {
    }
}