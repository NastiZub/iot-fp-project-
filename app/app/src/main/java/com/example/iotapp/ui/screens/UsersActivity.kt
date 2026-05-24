package com.example.iotapp.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R
import com.example.iotapp.data.RetrofitClient
import com.example.iotapp.model.UserStatsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.Gravity

class UsersActivity : AppCompatActivity() {
    private lateinit var usersContainer: LinearLayout
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        val backButton =
            findViewById<ImageButton>(R.id.backButton)
        usersContainer =
            findViewById(R.id.usersContainer)

        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)
        currentUserId =
            sharedPreferences.getInt("userId", -1)
        backButton.setOnClickListener {
            finish()
        }
        setupBottomNavigation()
        loadUsers()
    }

    private fun loadUsers() {
        RetrofitClient.apiService.getUsersStats()
            .enqueue(object : Callback<List<UserStatsResponse>> {

                override fun onResponse(
                    call: Call<List<UserStatsResponse>>,
                    response: Response<List<UserStatsResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val users = response.body()!!

                        usersContainer.removeAllViews()

                        if (users.isEmpty()) {
                            showMessage("No hay usuarios registrados")
                            return
                        }
                        for (user in users) {
                            createUserCard(user)
                        }
                    } else {
                        showMessage("Error al cargar usuarios")
                    }
                }

                override fun onFailure(
                    call: Call<List<UserStatsResponse>>,
                    t: Throwable
                ) {
                    showMessage("Error de conexión")
                }
            })
    }

    private fun createUserCard(user: UserStatsResponse) {
        val card = LinearLayout(this)

        card.orientation = LinearLayout.VERTICAL
        card.background = getDrawable(R.drawable.bg_home_card)
        card.elevation = 8f
        card.setPadding(40, 36, 40, 36)

        val cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        cardParams.setMargins(0, 0, 0, 28)
        card.layoutParams = cardParams

        val table = LinearLayout(this)
        table.orientation = LinearLayout.VERTICAL

        addRow(table, "Usuario", user.username)
        addRow(table, "Email", user.email)
        addRow(table, "Rol", user.role)
        addRow(table, "Registro", formatDate(user.createdAt))
        addRow(table, "Mediciones", user.measurementsCount.toString())
        addRow(table, "RFID", user.rfidTagsCount.toString())

        card.addView(table)

        if (user.userId != currentUserId) {
            val deleteButton = TextView(this)
            deleteButton.text = "Eliminar usuario"
            deleteButton.textSize = 15f
            deleteButton.setTextColor(
                getColor(android.R.color.holo_red_dark)
            )
            deleteButton.gravity = Gravity.CENTER
            deleteButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            deleteButton.setPadding(0, 24, 0, 0)
            deleteButton.isClickable = true
            deleteButton.isFocusable = true

            val deleteParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            deleteParams.setMargins(0, 24, 0, 0)
            deleteButton.layoutParams = deleteParams

            deleteButton.setOnClickListener {
                deleteUser(user.userId)
            }

            card.addView(deleteButton)
        }

        usersContainer.addView(card)
    }

    private fun addRow(
        table: LinearLayout,
        title: String,
        value: String
    ) {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.setPadding(0, 10, 0, 10)

        val titleText = TextView(this)
        titleText.text = title
        titleText.textSize = 15f
        titleText.setTextColor(getColor(android.R.color.darker_gray))

        titleText.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )

        val valueText = TextView(this)
        valueText.text = value
        valueText.textSize = 15f
        valueText.setTextColor(getColor(android.R.color.black))

        valueText.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.4f
        )

        row.addView(titleText)
        row.addView(valueText)

        table.addView(row)
    }

    private fun deleteUser(userId: Int) {
        RetrofitClient.apiService.deleteUser(userId)
            .enqueue(object : Callback<Void> {

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (response.isSuccessful) {
                        loadUsers()
                    } else {
                        showMessage("No se pudo eliminar el usuario")
                    }
                }

                override fun onFailure(
                    call: Call<Void>,
                    t: Throwable
                ) {
                    showMessage("Error de conexión")
                }
            })
    }

    private fun formatDate(rawDate: String): String {
        return rawDate
            .replace("T", " ")
            .substringBefore(".")
    }

    private fun showMessage(message: String) {
        usersContainer.removeAllViews()

        val textView = TextView(this)
        textView.text = message
        textView.textSize = 16f
        textView.setTextColor(getColor(android.R.color.black))

        usersContainer.addView(textView)
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