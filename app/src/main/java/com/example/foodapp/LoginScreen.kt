package com.example.foodapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.apiService.NotificationApi
import com.example.foodapp.apiService.UserApi
import com.example.foodapp.databinding.ActivityLoginScreenBinding
import com.example.foodapp.model.LoginResult
import com.example.foodapp.utils.SecureStorage
import com.google.firebase.messaging.FirebaseMessaging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginScreen : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var binding: ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dontHaveAccount.setOnClickListener {
            val intent = Intent(this, SignupScreen::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            email = binding.editTextTextEmailAddress.text.toString().trim()
            password = binding.editTextTextPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                signIn(email, password)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun signIn(email: String, password: String) {
        // You might want to show a loading indicator here
        lifecycleScope.launch {
            // Call the suspend function from the API layer
            when (val result = UserApi.login(email, password)) {
                is LoginResult.Success -> {
                    // Handle successful login
                    SecureStorage.saveData(this@LoginScreen, result.data)
                    Toast.makeText(
                        this@LoginScreen,
                        "Auth successful. Welcome ${result.data.name}",
                        Toast.LENGTH_SHORT
                    ).show()

                    sendTokenToServer()
                    updateUi() // Navigate to the next screen
                }
                is LoginResult.Error -> {
                    // Handle specific API error messages (e.g., "Username or password incorrect")
                    Toast.makeText(this@LoginScreen, result.error, Toast.LENGTH_LONG).show()
                }
                is LoginResult.NetworkError -> {
                    // Handle network/server issues
                    Toast.makeText(this@LoginScreen, "Connection failed: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
            // You might want to hide the loading indicator here
        }
    }

    private fun sendTokenToServer() {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.e("FCM", "Fetching FCM token failed", task.exception)
                            return@addOnCompleteListener
                        }

                        val token = task.result
                        Log.d("FCM Token", token)
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                val res = NotificationApi.sendFcmToken(token)
                                if (res.success) {
                                    Log.d("FCM", "Token sent successfully")
                                } else {
                                    Log.e("FCM", "Failed to send token: ${res.message}")
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Log.e("FCM", "Error sending token: ${e.message}")
                            }
                        }
                    }
                }


    private fun updateUi() {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
}
