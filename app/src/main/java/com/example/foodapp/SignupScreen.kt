package com.example.foodapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodapp.databinding.ActivitySignupScreenBinding
import com.example.foodapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignupScreen : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var name : String
    private lateinit var database : DatabaseReference

    private lateinit var binding: ActivitySignupScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference

        binding.signupButton.setOnClickListener {
            email = binding.editTextEmail.text.toString().trim()
            name = binding.editTextName.text.toString().trim()
            password = binding.editTextPassword.text.toString().trim()

            if (email.isBlank() || name.isBlank() || password.isBlank())
            {
                Toast.makeText(this, "please fill all details", Toast.LENGTH_SHORT).show()
            }
            else
            {
                createAccount(email, password)
            }
        }

        binding.alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                Toast.makeText(this, "account created successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginScreen::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                Toast.makeText(this, "account creation failed", Toast.LENGTH_SHORT).show()
                Log.d("account", "create account: failed", task.exception)
            }
        }
    }

    private fun saveUserData() {
        val user = UserModel(name, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}