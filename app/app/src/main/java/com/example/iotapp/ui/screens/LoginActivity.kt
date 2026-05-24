package com.example.iotapp.ui.screens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R
import com.example.iotapp.data.RetrofitClient
import com.example.iotapp.model.AuthResponse
import com.example.iotapp.model.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val usernameEditText =
            findViewById<EditText>(R.id.usernameEditText)

        val passwordEditText =
            findViewById<EditText>(R.id.passwordEditText)

        val loginButton =
            findViewById<Button>(R.id.loginButton)
        val rfidButton =
            findViewById<Button>(R.id.rfidButton)
        val rfidUidEditText =
            findViewById<EditText>(R.id.rfidUidEditText)

        val goToRegisterButton =
            findViewById<TextView>(R.id.goToRegisterButton)

        val messageTextView =
            findViewById<TextView>(R.id.messageTextView)

        val sharedPreferences: SharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        goToRegisterButton.setOnClickListener {

            val intent = Intent(
                this,
                RegisterActivity::class.java
            )

            startActivity(intent)
        }

        loginButton.setOnClickListener {

            val username =
                usernameEditText.text.toString()

            val password =
                passwordEditText.text.toString()

            val request = LoginRequest(
                username = username,
                password = password
            )

            RetrofitClient.apiService.login(request)
                .enqueue(object : Callback<AuthResponse> {

                    override fun onResponse(
                        call: Call<AuthResponse>,
                        response: Response<AuthResponse>
                    ) {

                        if (response.isSuccessful &&
                            response.body() != null
                        ) {

                            val authResponse =
                                response.body()!!

                            messageTextView.text =
                                authResponse.message

                            if (authResponse.success) {

                                sharedPreferences.edit()
                                    .putInt(
                                        "userId",
                                        authResponse.userId ?: -1
                                    )
                                    .putString(
                                        "username",
                                        authResponse.username
                                    )
                                    .putString(
                                        "role",
                                        authResponse.role
                                    )
                                    .apply()

                                val intent = Intent(
                                    this@LoginActivity,
                                    HomeActivity::class.java
                                )

                                startActivity(intent)

                                finish()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<AuthResponse>,
                        t: Throwable
                    ) {

                        messageTextView.text =
                            "Error de conexión"
                    }
                })
        }
        rfidButton.setOnClickListener {

            val tagUid =
                rfidUidEditText.text.toString().trim()

            if (tagUid.isEmpty()) {
                messageTextView.text = "Introduce el UID RFID"
                return@setOnClickListener
            }

            RetrofitClient.apiService.loginByRfid(tagUid)
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

                                sharedPreferences.edit()
                                    .putInt("userId", authResponse.userId ?: -1)
                                    .putString("username", authResponse.username)
                                    .putString("role", authResponse.role)
                                    .apply()

                                val intent = Intent(
                                    this@LoginActivity,
                                    HomeActivity::class.java
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