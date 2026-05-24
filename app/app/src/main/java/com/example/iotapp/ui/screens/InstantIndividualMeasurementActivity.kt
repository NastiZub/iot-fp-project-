package com.example.iotapp.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R
import com.example.iotapp.data.RetrofitClient
import com.example.iotapp.model.CreateMeasurementSessionRequest
import com.example.iotapp.model.MeasurementRequest
import com.example.iotapp.model.MeasurementResponse
import com.example.iotapp.model.MeasurementSessionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class InstantIndividualMeasurementActivity : AppCompatActivity() {

    private lateinit var sensorSpinner: Spinner
    private lateinit var resultTextView: TextView

    private var currentSessionId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_instant_individual_measurement)

        val backButton =
            findViewById<ImageButton>(R.id.backButton)

        val measureButton =
            findViewById<Button>(R.id.measureButton)

        sensorSpinner =
            findViewById(R.id.sensorSpinner)

        resultTextView =
            findViewById(R.id.resultTextView)

        backButton.setOnClickListener {
            finish()
        }

        setupBottomNavigation()
        setupSensorSpinner()

        measureButton.setOnClickListener {

            val value =
                Random.nextDouble(10.0, 80.0)

            createSessionAndSendMeasurement(value)
        }
    }

    private fun setupSensorSpinner() {

        val sensors = listOf(
            "Temperature Sensor (°C)",
            "Humidity Sensor (%)",
            "Pressure Sensor (hPa)",
            "Light Sensor (lux)",
            "Distance Sensor (cm)",
            "Speed Sensor (m/s)",
            "Acceleration Sensor (m/s²)",
            "Gyroscope Sensor (deg/s)",
            "Current Sensor (A)",
            "Voltage Sensor (V)",
            "Power Sensor (W)",
            "Gas Sensor (ppm)"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            sensors
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        sensorSpinner.adapter = adapter
    }

    private fun createSessionAndSendMeasurement(value: Double) {

        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        val userId =
            sharedPreferences.getInt("userId", -1)

        val request = CreateMeasurementSessionRequest(
            userId = userId,
            mode = "INSTANT",
            scope = "INDIVIDUAL"
        )

        RetrofitClient.apiService.createMeasurementSession(request)
            .enqueue(object : Callback<MeasurementSessionResponse> {

                override fun onResponse(
                    call: Call<MeasurementSessionResponse>,
                    response: Response<MeasurementSessionResponse>
                ) {

                    if (
                        response.isSuccessful &&
                        response.body() != null
                    ) {

                        currentSessionId =
                            response.body()!!.id

                        sendMeasurementToServer(value)
                    }
                }

                override fun onFailure(
                    call: Call<MeasurementSessionResponse>,
                    t: Throwable
                ) {

                    resultTextView.text =
                        "Error al crear sesión"
                }
            })
    }

    private fun sendMeasurementToServer(value: Double) {

        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        val userId =
            sharedPreferences.getInt("userId", -1)

        val request = MeasurementRequest(
            sensorId = getSensorIdByName(
                sensorSpinner.selectedItem.toString()
            ),
            value = value,
            userId = userId,
            sessionId = currentSessionId
        )

        RetrofitClient.apiService.sendMeasurement(request)
            .enqueue(object : Callback<MeasurementResponse> {

                override fun onResponse(
                    call: Call<MeasurementResponse>,
                    response: Response<MeasurementResponse>
                ) {

                    if (
                        response.isSuccessful &&
                        response.body() != null
                    ) {

                        resultTextView.text =
                            "Valor medido:\n%.3f".format(value)

                    } else {

                        resultTextView.text =
                            "Error del servidor"
                    }
                }

                override fun onFailure(
                    call: Call<MeasurementResponse>,
                    t: Throwable
                ) {

                    resultTextView.text =
                        "Error de conexión"
                }
            })
    }

    private fun getSensorIdByName(sensorName: String): Long {

        return when {

            sensorName.contains("Temperature") -> 1L
            sensorName.contains("Humidity") -> 2L
            sensorName.contains("Pressure") -> 3L
            sensorName.contains("Light") -> 4L
            sensorName.contains("Distance") -> 5L
            sensorName.contains("Speed") -> 9L
            sensorName.contains("Acceleration") -> 11L
            sensorName.contains("Gyroscope") -> 12L
            sensorName.contains("Current") -> 13L
            sensorName.contains("Voltage") -> 14L
            sensorName.contains("Power") -> 15L
            sensorName.contains("Gas") -> 16L

            else -> 1L
        }
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