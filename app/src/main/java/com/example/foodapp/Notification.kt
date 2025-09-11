package com.example.foodapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.adapter.NotificationItemAdapter
import com.example.foodapp.databinding.FragmentNotificationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Notification : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            dismiss()
        }

        val images = mutableListOf<Int>(
            R.drawable.sademoji,
            R.drawable.congrats,
            R.drawable.truck
        )
        val messages = mutableListOf<String>(
            "Your order has been Canceled Successfully",
            "Congrats Your Order Placed",
            "Order has been taken by the driver"
        )

        val adapter = NotificationItemAdapter(images, messages)
        binding.notificationRecyclerView.adapter = adapter
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
    }
}