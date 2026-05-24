package com.example.iotapp.ui.screens

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R

class CuentaActivity : AppCompatActivity() {

    private lateinit var accountTable: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cuenta)

        accountTable = findViewById(R.id.accountTable)

        val logoutButton =
            findViewById<TextView>(R.id.logoutButton)

        setupAccountInfo()
        setupBottomNavigation()

        logoutButton.setOnClickListener {
            val sharedPreferences =
                getSharedPreferences("user_session", MODE_PRIVATE)

            sharedPreferences.edit().clear().apply()

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }
    }

    private fun setupAccountInfo() {
        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        val userId =
            sharedPreferences.getInt("userId", -1)

        val username =
            sharedPreferences.getString("username", "Usuario")

        val role =
            sharedPreferences.getString("role", "USER")

        accountTable.removeAllViews()

        addRow("ID usuario", userId.toString())
        addRow("Usuario", username ?: "Usuario")
        addRow("Rol", role ?: "USER")
    }

    private fun addRow(title: String, value: String) {
        val row = TableRow(this)
        row.setPadding(0, 10, 0, 10)

        val titleText = TextView(this)
        titleText.text = title
        titleText.textSize = 15f
        titleText.setTextColor(Color.rgb(120, 120, 120))
        titleText.setPadding(8, 10, 8, 10)

        val valueText = TextView(this)
        valueText.text = value
        valueText.textSize = 15f
        valueText.setTextColor(Color.BLACK)
        valueText.setPadding(8, 10, 8, 10)

        row.addView(titleText)
        row.addView(valueText)

        accountTable.addView(row)
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