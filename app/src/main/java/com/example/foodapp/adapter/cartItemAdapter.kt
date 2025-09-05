package com.example.foodapp.adapter

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.example.foodapp.databinding.CartItemBinding

class cartItemAdapter(
    private val items: MutableList<String>,
    private val prices: MutableList<String>,
    private val images: MutableList<Int>,
    private val onItemRemoved: (Int) -> Unit
) : RecyclerView.Adapter<cartItemAdapter.cartItemViewHolder>() {

    private val quantities = MutableList(items.size){1}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): cartItemViewHolder {
        return cartItemViewHolder(CartItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(
        holder: cartItemViewHolder,
        position: Int
    ) {
        val item = items[position]
        val price = prices[position]
        val image = images[position]
        val quantity = quantities[position]

        holder.bind(
            item,
            price,
            image,
            quantity,
            onQuantityChanged = { newQuantity ->
            quantities[position] = newQuantity },
            onRemove = {
                items.removeAt(position)
                prices.removeAt(position)
                images.removeAt(position)
                quantities.removeAt(position)

                notifyItemRemoved(position)
                notifyItemRangeChanged(position,items.size)

                onItemRemoved(position)
            }
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class cartItemViewHolder(private val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(
            item: String,
            price: String,
            image: Int,
            quantity: Int,
            onQuantityChanged: (Int) -> Unit,
            onRemove: () -> Unit
        ) {
            binding.cartFoodName.text = item
            binding.cartPrice.text = price
            binding.cartImage.setImageResource(image)
            binding.cartItemQuantity.text = quantity.toString()

            binding.cartMinus.setOnClickListener {
                var q = binding.cartItemQuantity.text.toString().toInt()
                if (q>1){
                    q--
                    binding.cartItemQuantity.text = q.toString()
                    onQuantityChanged(q)
                }
            }

            binding.cartPlus.setOnClickListener {
                var q = binding.cartItemQuantity.text.toString().toInt()
                if (q<10){
                    q++
                    binding.cartItemQuantity.text = q.toString()
                    onQuantityChanged(q)
                }
            }

            binding.cartRemove.setOnClickListener {
                onRemove()
            }
        }

    }
}