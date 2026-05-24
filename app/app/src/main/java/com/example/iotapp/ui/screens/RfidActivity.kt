package com.example.iotapp.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R
import com.example.iotapp.data.RetrofitClient
import com.example.iotapp.model.RegisterRfidRequest
import com.example.iotapp.model.RfidTagResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class RfidActivity : AppCompatActivity() {

    private lateinit var tagNameEditText: EditText
    private lateinit var tagUidEditText: EditText
    private lateinit var messageTextView: TextView
    private lateinit var tokensTextView: TextView

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_rfid)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val generateUidButton = findViewById<Button>(R.id.generateUidButton)
        val saveRfidButton = findViewById<Button>(R.id.saveRfidButton)

        tagNameEditText = findViewById(R.id.tagNameEditText)
        tagUidEditText = findViewById(R.id.tagUidEditText)
        messageTextView = findViewById(R.id.messageTextView)
        tokensTextView = findViewById(R.id.tokensTextView)

        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        userId = sharedPreferences.getInt("userId", -1)

        backButton.setOnClickListener {
            finish()
        }

        generateUidButton.setOnClickListener {
            tagUidEditText.setText(generateFakeRfidUid())
        }

        saveRfidButton.setOnClickListener {
            saveRfidToken()
        }

        setupBottomNavigation()
        loadUserTokens()
    }

    private fun generateFakeRfidUid(): String {
        return UUID.randomUUID()
            .toString()
            .substring(0, 8)
            .uppercase()
    }

    private fun saveRfidToken() {
        val tagName = tagNameEditText.text.toString().trim()
        val tagUid = tagUidEditText.text.toString().trim()

        if (userId == -1) {
            messageTextView.text = "Usuario no identificado"
            return
        }

        if (tagName.isEmpty()) {
            messageTextView.text = "Introduce un nombre para el token"
            return
        }

        if (tagUid.isEmpty()) {
            messageTextView.text = "Introduce o simula un UID RFID"
            return
        }

        val request = RegisterRfidRequest(
            userId = userId,
            tagUid = tagUid,
            tagName = tagName
        )

        RetrofitClient.apiService.registerRfid(request)
            .enqueue(object : Callback<RfidTagResponse> {

                override fun onResponse(
                    call: Call<RfidTagResponse>,
                    response: Response<RfidTagResponse>
                ) {
                    if (response.isSuccessful) {
                        messageTextView.text = "Token RFID guardado correctamente"
                        tagNameEditText.text.clear()
                        tagUidEditText.text.clear()
                        loadUserTokens()
                    } else {
                        messageTextView.text = "Error al guardar el token"
                    }
                }

                override fun onFailure(
                    call: Call<RfidTagResponse>,
                    t: Throwable
                ) {
                    messageTextView.text = "Error de conexión"
                }
            })
    }

    private fun loadUserTokens() {
        if (userId == -1) {
            tokensTextView.text = "No hay usuario activo"
            return
        }

        RetrofitClient.apiService.getRfidTagsByUser(userId)
            .enqueue(object : Callback<List<RfidTagResponse>> {

                override fun onResponse(
                    call: Call<List<RfidTagResponse>>,
                    response: Response<List<RfidTagResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val tokens = response.body()!!

                        if (tokens.isEmpty()) {
                            tokensTextView.text = "No hay tokens vinculados"
                            return
                        }

                        val text = StringBuilder()
                        text.append("Total de tokens: ${tokens.size}\n\n")

                        for (token in tokens) {
                            text.append("Nombre: ${token.tagName}\n")
                            text.append("UID: ${token.tagUid}\n")
                            text.append("Fecha: ${token.createdAt}\n\n")
                        }

                        tokensTextView.text = text.toString()
                    } else {
                        tokensTextView.text = "Error al cargar tokens"
                    }
                }

                override fun onFailure(
                    call: Call<List<RfidTagResponse>>,
                    t: Throwable
                ) {
                    tokensTextView.text = "Error de conexión"
                }
            })
    }

    private fun setupBottomNavigation() {

        val bottomHomeButton =
            findViewById<LinearLayout>(R.id.bottomHomeButton)

        val bottomHistoryButton =
            findViewById<LinearLayout>(R.id.bottomHistoryButton)

        val bottomAccountButton =
            findViewById<LinearLayout>(R.id.bottomAccountButton)

        bottomHomeButton.setOnClickListener {

            startActivity(
                Intent(this, HomeActivity::class.java)
            )

            finish()
        }

        bottomHistoryButton.setOnClickListener {

            startActivity(
                Intent(this, HistoryActivity::class.java)
            )
        }

        bottomAccountButton.setOnClickListener {

            startActivity(
                Intent(this, CuentaActivity::class.java)
            )
        }
    }
}