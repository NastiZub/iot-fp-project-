package com.example.iotapp.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R
import com.example.iotapp.data.RetrofitClient
import com.example.iotapp.model.AuthResponse
import com.example.iotapp.model.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        val backButton =
            findViewById<ImageButton>(R.id.backButton)

        val usernameEditText =
            findViewById<EditText>(R.id.usernameEditText)

        val emailEditText =
            findViewById<EditText>(R.id.emailEditText)

        val passwordEditText =
            findViewById<EditText>(R.id.passwordEditText)

        val registerButton =
            findViewById<Button>(R.id.registerButton)

        val messageTextView =
            findViewById<TextView>(R.id.messageTextView)

        backButton.setOnClickListener {
            finish()
        }

        registerButton.setOnClickListener {

            val username =
                usernameEditText.text.toString().trim()

            val email =
                emailEditText.text.toString().trim()

            val password =
                passwordEditText.text.toString()

            val emailRegex =
                Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

            if (username.isEmpty()) {
                messageTextView.text = "Introduce un nombre de usuario"
                return@setOnClickListener
            }

            if (!email.matches(emailRegex)) {
                messageTextView.text = "Introduce un correo electrónico válido"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                messageTextView.text = "Introduce una contraseña"
                return@setOnClickListener
            }

            val request = RegisterRequest(
                username = username,
                email = email,
                password = password
            )

            RetrofitClient.apiService.register(request)
                .enqueue(object : Callback<AuthResponse> {

                    override fun onResponse(
                        call: Call<AuthResponse>,
                        response: Response<AuthResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {

                            val authResponse =
                                response.body()!!

                            messageTextView.text =
                                authResponse.message

                            if (authResponse.success) {

                                val intent = Intent(
                                    this@RegisterActivity,
                                    LoginActivity::class.java
                                )

                                startActivity(intent)
                                finish()
                            }

                        } else {
                            messageTextView.text = "Error del servidor"
                        }
                    }

                    override fun onFailure(
                        call: Call<AuthResponse>,
                        t: Throwable
                    ) {
                        messageTextView.text = "Error de conexión"
                    }
                })
        }
    }
}