package com.example.foodapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.LoginScreen
import com.example.foodapp.R
import com.example.foodapp.apiService.UserApi
import com.example.foodapp.config.ApiClient
import com.example.foodapp.databinding.FragmentProfileBinding
import com.example.foodapp.databinding.HistoryItemBinding
import com.example.foodapp.utils.SecureStorage
import kotlinx.coroutines.launch

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutButton.setOnClickListener {
            Logout()
        }
    }

    private fun Logout() {
        lifecycleScope.launch {
            try {
                val response = UserApi.logout()

                if (response.message == "Logged out") {
                    // Clear locally stored secure data
                    SecureStorage.clearData(requireContext())

                    Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    // Redirect to login screen
                    val intent = Intent(requireContext(), LoginScreen::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Logout failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
    }
}