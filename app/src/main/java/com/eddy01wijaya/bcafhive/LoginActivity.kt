package com.eddy01wijaya.bcafhive

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eddy01wijaya.bcafhive.viewmodel.RecruitmentViewModel
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import androidx.activity.viewModels

class LoginActivity : AppCompatActivity() {

    private val authViewModel: RecruitmentViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val txtUsername: EditText = findViewById(R.id.txtUsername)
        val txtPassword: EditText = findViewById(R.id.txtPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        // Load saved username and password
        loadCredentials(txtUsername, txtPassword)

        btnLogin.setOnClickListener {
            val username = txtUsername.text.toString()
            val password = txtPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(
                    username,
                    password,
                    onSuccess = {
                        // Load saved username and password
                        saveCredentials(username, password) // Save credentials
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onError = { error ->
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Save username and password to SharedPreferences
    private fun saveCredentials(username: String, password: String) {
        sharedPreferences.edit()
            .putString("username", username)
            .putString("password", password)
            .apply()
    }

    // Load saved username and password from SharedPreferences
    private fun loadCredentials(edtUsername: EditText, edtPassword: EditText) {
        val savedUsername = sharedPreferences.getString("username", "")
        val savedPassword = sharedPreferences.getString("password", "")
        edtUsername.setText(savedUsername)
        edtPassword.setText(savedPassword)
    }


}