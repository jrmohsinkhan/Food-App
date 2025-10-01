package com.example.foodapp.adapter

import android.R
import android.content.Intent
import android.media.Image
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.MapsActivity
import com.example.foodapp.databinding.PopularItemBinding

class popularItemAdapter (private val items: List<String>,private val prices: List<String>, private val images: List<Int>) : RecyclerView.Adapter<popularItemAdapter.popularItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): popularItemViewHolder {
        return popularItemViewHolder(PopularItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false ))
    }

    override fun onBindViewHolder(
        holder: popularItemViewHolder,
        position: Int
    ) {
        val item = items[position]
        val image = images[position]
        val price = prices[position]

        holder.bind(item, image, price)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class popularItemViewHolder (private val binding: PopularItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: String, image: Int, price: String) {
            binding.foodName.text = item
            binding.price.text = price
            binding.popularItemImage.setImageResource(image)

            binding.addToCart.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, MapsActivity::class.java)
                context.startActivity(intent)
            }
        }

    }
}