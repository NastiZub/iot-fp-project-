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

class InstantSimultaneousMeasurementActivity : AppCompatActivity() {

    private lateinit var resultsTextView: TextView
    private var currentSessionId: Int = -1

    private lateinit var temperatureCheck: CheckBox
    private lateinit var humidityCheck: CheckBox
    private lateinit var pressureCheck: CheckBox
    private lateinit var lightCheck: CheckBox
    private lateinit var distanceCheck: CheckBox
    private lateinit var speedCheck: CheckBox
    private lateinit var accelerationCheck: CheckBox
    private lateinit var gyroscopeCheck: CheckBox
    private lateinit var currentCheck: CheckBox
    private lateinit var voltageCheck: CheckBox
    private lateinit var powerCheck: CheckBox
    private lateinit var gasCheck: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_instant_simultaneous_measurement)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val measureButton = findViewById<Button>(R.id.measureSimultaneousButton)

        resultsTextView = findViewById(R.id.resultsTextView)

        temperatureCheck = findViewById(R.id.temperatureCheck)
        humidityCheck = findViewById(R.id.humidityCheck)
        pressureCheck = findViewById(R.id.pressureCheck)
        lightCheck = findViewById(R.id.lightCheck)
        distanceCheck = findViewById(R.id.distanceCheck)
        speedCheck = findViewById(R.id.speedCheck)
        accelerationCheck = findViewById(R.id.accelerationCheck)
        gyroscopeCheck = findViewById(R.id.gyroscopeCheck)
        currentCheck = findViewById(R.id.currentCheck)
        voltageCheck = findViewById(R.id.voltageCheck)
        powerCheck = findViewById(R.id.powerCheck)
        gasCheck = findViewById(R.id.gasCheck)

        backButton.setOnClickListener {
            finish()
        }

        setupBottomNavigation()

        measureButton.setOnClickListener {
            val selectedSensors = getSelectedSensors()

            if (selectedSensors.isEmpty()) {
                resultsTextView.text = "Seleccione al menos un sensor."
                return@setOnClickListener
            }

            createSessionAndSendMeasurements(selectedSensors)
        }
    }

    private fun getSelectedSensors(): List<String> {
        val sensors = mutableListOf<String>()

        if (temperatureCheck.isChecked) sensors.add("Temperature Sensor")
        if (humidityCheck.isChecked) sensors.add("Humidity Sensor")
        if (pressureCheck.isChecked) sensors.add("Pressure Sensor")
        if (lightCheck.isChecked) sensors.add("Light Sensor")
        if (distanceCheck.isChecked) sensors.add("Distance Sensor")
        if (speedCheck.isChecked) sensors.add("Speed Sensor")
        if (accelerationCheck.isChecked) sensors.add("Acceleration Sensor")
        if (gyroscopeCheck.isChecked) sensors.add("Gyroscope Sensor")
        if (currentCheck.isChecked) sensors.add("Current Sensor")
        if (voltageCheck.isChecked) sensors.add("Voltage Sensor")
        if (powerCheck.isChecked) sensors.add("Power Sensor")
        if (gasCheck.isChecked) sensors.add("Gas Sensor")

        return sensors
    }

    private fun createSessionAndSendMeasurements(
        selectedSensors: List<String>
    ) {
        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        val userId =
            sharedPreferences.getInt("userId", -1)

        val request = CreateMeasurementSessionRequest(
            userId = userId,
            mode = "INSTANT",
            scope = "SIMULTANEOUS"
        )

        RetrofitClient.apiService.createMeasurementSession(request)
            .enqueue(object : Callback<MeasurementSessionResponse> {

                override fun onResponse(
                    call: Call<MeasurementSessionResponse>,
                    response: Response<MeasurementSessionResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        currentSessionId = response.body()!!.id
                        sendSelectedMeasurements(selectedSensors)
                    } else {
                        resultsTextView.text = "Error al crear sesión"
                    }
                }

                override fun onFailure(
                    call: Call<MeasurementSessionResponse>,
                    t: Throwable
                ) {
                    resultsTextView.text = "Error de conexión"
                }
            })
    }

    private fun sendSelectedMeasurements(selectedSensors: List<String>) {
        val results = StringBuilder()

        for (sensor in selectedSensors) {
            val value = generateValue(sensor)

            results.append(
                "$sensor: %.3f ${getUnit(sensor)}\n".format(value)
            )

            sendMeasurementToServer(sensor, value)
        }

        resultsTextView.text = results.toString()
    }

    private fun sendMeasurementToServer(
        sensorName: String,
        value: Double
    ) {
        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        val userId =
            sharedPreferences.getInt("userId", -1)

        val request = MeasurementRequest(
            sensorId = getSensorIdByName(sensorName),
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
                    if (!response.isSuccessful) {
                        resultsTextView.append(
                            "\nError servidor: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(
                    call: Call<MeasurementResponse>,
                    t: Throwable
                ) {
                    resultsTextView.append(
                        "\nError conexión: ${t.message}"
                    )
                }
            })
    }

    private fun generateValue(sensor: String): Double {
        return when {
            sensor.contains("Temperature") -> Random.nextDouble(18.0, 35.0)
            sensor.contains("Humidity") -> Random.nextDouble(30.0, 80.0)
            sensor.contains("Pressure") -> Random.nextDouble(950.0, 1050.0)
            sensor.contains("Light") -> Random.nextDouble(100.0, 900.0)
            sensor.contains("Distance") -> Random.nextDouble(2.0, 200.0)
            sensor.contains("Speed") -> Random.nextDouble(0.0, 10.0)
            sensor.contains("Acceleration") -> Random.nextDouble(0.0, 20.0)
            sensor.contains("Gyroscope") -> Random.nextDouble(0.0, 500.0)
            sensor.contains("Current") -> Random.nextDouble(0.0, 5.0)
            sensor.contains("Voltage") -> Random.nextDouble(0.0, 12.0)
            sensor.contains("Power") -> Random.nextDouble(0.0, 60.0)
            sensor.contains("Gas") -> Random.nextDouble(200.0, 1200.0)
            else -> Random.nextDouble(0.0, 100.0)
        }
    }

    private fun getUnit(sensor: String): String {
        return when {
            sensor.contains("Temperature") -> "°C"
            sensor.contains("Humidity") -> "%"
            sensor.contains("Pressure") -> "hPa"
            sensor.contains("Light") -> "lux"
            sensor.contains("Distance") -> "cm"
            sensor.contains("Speed") -> "m/s"
            sensor.contains("Acceleration") -> "m/s²"
            sensor.contains("Gyroscope") -> "deg/s"
            sensor.contains("Current") -> "A"
            sensor.contains("Voltage") -> "V"
            sensor.contains("Power") -> "W"
            sensor.contains("Gas") -> "ppm"
            else -> ""
        }
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