package com.example.iotapp.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R
import com.example.iotapp.data.RetrofitClient
import com.example.iotapp.model.MeasurementSessionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {

    private lateinit var sessionsContainer: LinearLayout
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)

        sessionsContainer = findViewById(R.id.sessionsContainer)

        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        userId = sharedPreferences.getInt("userId", -1)

        setupBottomNavigation()
        loadSessions()
    }

    private fun loadSessions() {
        if (userId == -1) {
            showMessage("Usuario no identificado")
            return
        }

        RetrofitClient.apiService.getMeasurementSessionsByUser(userId)
            .enqueue(object : Callback<List<MeasurementSessionResponse>> {

                override fun onResponse(
                    call: Call<List<MeasurementSessionResponse>>,
                    response: Response<List<MeasurementSessionResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val sessions = response.body()!!

                        sessionsContainer.removeAllViews()

                        if (sessions.isEmpty()) {
                            showMessage("No hay sesiones guardadas")
                            return
                        }

                        for (session in sessions) {
                            createSessionCard(session)
                        }

                    } else {
                        showMessage("Error al cargar el historial")
                    }
                }

                override fun onFailure(
                    call: Call<List<MeasurementSessionResponse>>,
                    t: Throwable
                ) {
                    showMessage("Error de conexión")
                }
            })
    }

    private fun createSessionCard(session: MeasurementSessionResponse) {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.background = getDrawable(R.drawable.bg_home_card)
        card.elevation = 8f
        card.setPadding(40, 36, 40, 36)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 28)
        card.layoutParams = params

        val title = TextView(this)
        title.text = formatSessionTitle(session.mode, session.scope)
        title.textSize = 18f
        title.setTextColor(getColor(android.R.color.black))
        title.setTypeface(null, android.graphics.Typeface.BOLD)

        val date = TextView(this)
        date.text = "Fecha: ${formatDate(session.createdAt)}"
        date.textSize = 15f
        date.setTextColor(getColor(android.R.color.darker_gray))
        date.setPadding(0, 14, 0, 0)

        card.addView(title)
        card.addView(date)

        card.setOnClickListener {
            val intent = Intent(this, HistoryDetailActivity::class.java)
            intent.putExtra("sessionId", session.id)
            startActivity(intent)
        }

        sessionsContainer.addView(card)
    }

    private fun formatSessionTitle(mode: String, scope: String): String {
        val modeText =
            if (mode == "CONTINUOUS") "Medición continua" else "Medición instantánea"

        val scopeText =
            if (scope == "SIMULTANEOUS") "simultánea" else "individual"

        return "$modeText $scopeText"
    }

    private fun formatDate(rawDate: String): String {
        return rawDate
            .replace("T", " ")
            .substringBefore(".")
    }

    private fun showMessage(message: String) {
        sessionsContainer.removeAllViews()

        val text = TextView(this)
        text.text = message
        text.textSize = 16f
        text.setTextColor(getColor(android.R.color.black))

        sessionsContainer.addView(text)
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