package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.MenuItemBinding
import com.example.foodapp.model.Venue
import com.google.android.flexbox.FlexboxLayout

class VenueAdapter(
    private val venues: MutableList<Venue>
) : RecyclerView.Adapter<VenueAdapter.VenueViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VenueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.bind(venues[position])
    }

    override fun getItemCount(): Int = venues.size

    inner class VenueViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(venue: Venue) {
            // Load image from URL
            Glide.with(binding.root.context)
                .load(venue.imageURL)
                .into(binding.menuItemImage)

            // Set venue name and location
            binding.venueName.text = venue.venueName
            binding.venueLocation.text = venue.city

            // Populate sports inside FlexboxLayout
            binding.sportsContainer.removeAllViews()
            for (venueSport in venue.venueSports) {
                val sportTextView = TextView(binding.root.context).apply {
                    text = venueSport.sport.sportName
                    setPadding(12, 12, 12, 12)
                    setTextSize(12f)
                    setTextColor(ContextCompat.getColor(context,R.color.textColor))
                    setBackgroundResource(R.drawable.whitebutton) // Replace with custom drawable for chip-like UI

                }
                val params = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 8, 8, 8)
                }
                binding.sportsContainer.addView(sportTextView, params)
            }

            // Handle view details click
            binding.viewDetails.setOnClickListener {
                // TODO: Navigate to Venue detail screen
            }
        }
    }
}
