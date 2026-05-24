package com.example.iotapp.ui.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val instantIndividualButton =
            findViewById<LinearLayout>(R.id.instantIndividualButton)

        val instantSimultaneousButton =
            findViewById<LinearLayout>(R.id.instantSimultaneousButton)

        val continuousIndividualButton =
            findViewById<LinearLayout>(R.id.continuousIndividualButton)

        val continuousSimultaneousButton =
            findViewById<LinearLayout>(R.id.continuousSimultaneousButton)

        val usersButton =
            findViewById<TextView>(R.id.usersButton)

        usersButton.setOnClickListener {
            startActivity(
                Intent(this, UsersActivity::class.java)
            )
        }

        val logoutButton =
            findViewById<TextView>(R.id.logoutButton)

        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        val role =
            sharedPreferences.getString("role", "USER")

        if (role == "ADMIN") {
            usersButton.visibility = View.VISIBLE
        } else {
            usersButton.visibility = View.GONE
        }

        instantIndividualButton.setOnClickListener {
            startActivity(
                Intent(this, InstantIndividualMeasurementActivity::class.java)
            )
        }
        instantSimultaneousButton.setOnClickListener {
            startActivity(
                Intent(this, InstantSimultaneousMeasurementActivity::class.java)
            )
        }
        continuousIndividualButton.setOnClickListener {
            startActivity(
                Intent(this, ContinuousIndividualMeasurementActivity::class.java)
            )
        }
        continuousSimultaneousButton.setOnClickListener {
            startActivity(
                Intent(this, ContinuousSimultaneousMeasurementActivity::class.java)
            )
        }
        usersButton.setOnClickListener {
            startActivity(
                Intent(this, UsersActivity::class.java)
            )
        }
        logoutButton.setOnClickListener {
            sharedPreferences.edit().clear().apply()

            val intent =
                Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val rfidRegisterButton =
            findViewById<LinearLayout>(R.id.rfidRegisterButton)
        rfidRegisterButton.setOnClickListener {
            startActivity(
                Intent(this, RfidActivity::class.java)
            )
        }
        setupBottomNavigation()
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