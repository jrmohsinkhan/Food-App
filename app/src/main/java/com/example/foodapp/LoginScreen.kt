package com.example.foodapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodapp.databinding.ActivityLoginScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class LoginScreen : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var database: DatabaseReference

    private lateinit var binding: ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference

        binding.dontHaveAccount.setOnClickListener {
            val intent = Intent(this, SignupScreen::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {

            email = binding.editTextTextEmailAddress.text.toString().trim()
            password = binding.editTextTextPassword.text.toString().trim()

            if (email.isBlank() || email.isBlank()  )
            {
                Toast.makeText(this, "please fill all details", Toast.LENGTH_SHORT).show()
            }
            else
            {
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
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                Toast.makeText(this, "auth successful", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                updateUi(user)
            }
            else
            {
                Toast.makeText(this , "auth failed", Toast.LENGTH_SHORT).show()
                Log.d("login", "login: failed", task.exception)
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}