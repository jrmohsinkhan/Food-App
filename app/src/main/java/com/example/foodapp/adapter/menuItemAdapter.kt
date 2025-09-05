package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.MenuItemBinding

class menuItemAdapter (private val items: MutableList<String>, private val prices: MutableList<String>, private val images: MutableList<Int>) : RecyclerView.Adapter<menuItemAdapter.menuItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): menuItemViewHolder {
        return menuItemViewHolder(MenuItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(
        holder: menuItemViewHolder,
        position: Int
    ) {
        val image = images[position]
        val price = prices[position]
        val item = items[position]

        holder.bind(item, image, price)


    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class menuItemViewHolder(val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: String, image: Int, price: String) {
            binding.menuItemImage.setImageResource(image)
            binding.price.text = price
            binding.foodName.text = item
        }

    }
}