package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.NotificationItemBinding

class NotificationItemAdapter (private val images: MutableList<Int>, private val messages: MutableList<String>) :
    RecyclerView.Adapter<NotificationItemAdapter.NotificationItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationItemViewHolder {
        return NotificationItemViewHolder(
            NotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: NotificationItemViewHolder,
        position: Int
    ) {
        val image = images[position]
        val message = messages[position]

        holder.bind(image, message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class NotificationItemViewHolder(private val binding: NotificationItemBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(image: Int, message: String) {
            binding.notificationImage.setImageResource(image)
            binding.notificationMessage.text = message
        }

    }
}