package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.HistoryItemBinding

class historyItemAdapter(
    private val foodNames: MutableList<String>,
    private val foodPrices: MutableList<String>,
    private val foodImages: MutableList<Int>
) : RecyclerView.Adapter<historyItemAdapter.historyItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): historyItemViewHolder {
        return historyItemViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: historyItemViewHolder,
        position: Int
    ) {
        val foodName = foodNames[position]
        val foodPrice = foodPrices[position]
        val foodImage = foodImages[position]

        holder.bind(foodName, foodPrice, foodImage)
    }

    override fun getItemCount(): Int {
        return foodNames.size
    }

    class historyItemViewHolder(private val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(foodName: String, foodPrice: String, foodImage: Int) {
            binding.historyFoodName.text = foodName
            binding.historyPrice.text = foodPrice
            binding.historyItemImage.setImageResource(foodImage)
        }

    }
}