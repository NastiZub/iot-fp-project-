package com.example.iotapp.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R
import com.example.iotapp.data.RetrofitClient
import com.example.iotapp.model.MeasurementRequest
import com.example.iotapp.model.MeasurementResponse
import com.example.iotapp.model.SessionMeasurement
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random
import com.example.iotapp.model.CreateMeasurementSessionRequest
import com.example.iotapp.model.MeasurementSessionResponse
import android.widget.TableLayout
import android.widget.TableRow

class ContinuousIndividualMeasurementActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var isMonitoring = false
    private var isFirstSpinnerSelection = true

    private lateinit var sensorSpinner: Spinner
    private lateinit var intervalEditText: EditText
    private lateinit var currentValueTextView: TextView
    private lateinit var measurementsTable: TableLayout
    private lateinit var lineChart: LineChart

    private val entries = ArrayList<Entry>()
    private val sessionMeasurements = ArrayList<SessionMeasurement>()
    private var measurementIndex = 0f
    private var currentSessionId: Int = -1

    private val monitoringTask = object : Runnable {
        override fun run() {
            if (isMonitoring) {
                generateMeasurement()

                val intervalSeconds =
                    intervalEditText.text.toString().toLongOrNull() ?: 2

                handler.postDelayed(this, intervalSeconds * 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continuous_individual_measurement)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        val savePdfButton = findViewById<TextView>(R.id.savePdfButton)
        val saveCsvButton = findViewById<TextView>(R.id.saveCsvButton)

        sensorSpinner = findViewById(R.id.sensorSpinner)
        intervalEditText = findViewById(R.id.intervalEditText)
        currentValueTextView = findViewById(R.id.currentValueTextView)
        measurementsTable = findViewById(R.id.measurementsTable)
        lineChart = findViewById(R.id.lineChart)

        backButton.setOnClickListener {
            finish()
        }

        setupBottomNavigation()
        setupSensorSpinner()
        setupMeasurementsTable()

        startButton.setOnClickListener {
            clearCurrentSession()
            createSessionAndStartMonitoring()
        }

        stopButton.setOnClickListener {
            isMonitoring = false
            handler.removeCallbacks(monitoringTask)
        }

        savePdfButton.setOnClickListener {
            saveSessionAsPdf()
        }
        saveCsvButton.setOnClickListener {
            saveSessionAsCsv()
        }
    }

    private fun setupSensorSpinner() {
        val sensors = listOf(
            "BME280 Temp — Temperatura (°C)",
            "BME280 Humidity — Humedad (%)",
            "BME280 Pressure — Presión (hPa)",
            "DS18B20 Temp — Temperatura del agua (°C)",
            "BH1750 Light — Luz (lux)",
            "VL53L1X Distance — Distancia (cm)",
            "HC-SR501 PIR — Movimiento",
            "Touch Sensor — Tacto",
            "RFID MFRC522 — RFID",
            "Obstacle Sensor — Obstáculo",
            "Speed Sensor — Velocidad (m/s)",
            "INA219 Voltage — Voltaje (V)",
            "INA219 Current — Corriente (A)",
            "INA219 Power — Potencia (W)",
            "HC-SR04 Ultrasonic — Distancia ultrasónica",
            "Reed Switch — Campo magnético",
            "BMI160 Acceleration — Aceleración (m/s²)",
            "BMI160 Gyroscope — Giroscopio (deg/s)",
            "MPU6050 Acceleration — Aceleración MPU6050",
            "MPU6050 Gyroscope — Giroscopio MPU6050",
            "MQ135 Gas Sensor — Gas (ppm)"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            sensors
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sensorSpinner.adapter = adapter

        sensorSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    if (isFirstSpinnerSelection) {
                        isFirstSpinnerSelection = false
                        return
                    }

                    isMonitoring = false
                    handler.removeCallbacks(monitoringTask)
                    clearCurrentSession()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun createSessionAndStartMonitoring() {
        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        val userId =
            sharedPreferences.getInt("userId", -1)

        if (userId == -1) {
            Toast.makeText(this, "Usuario no identificado", Toast.LENGTH_SHORT).show()
            return
        }

        val request = CreateMeasurementSessionRequest(
            userId = userId,
            mode = "CONTINUOUS",
            scope = "INDIVIDUAL"
        )

        RetrofitClient.apiService.createMeasurementSession(request)
            .enqueue(object : Callback<MeasurementSessionResponse> {

                override fun onResponse(
                    call: Call<MeasurementSessionResponse>,
                    response: Response<MeasurementSessionResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        currentSessionId = response.body()!!.id

                        isMonitoring = true
                        setupMeasurementsTable()
                        handler.post(monitoringTask)

                    } else {
                        Toast.makeText(
                            this@ContinuousIndividualMeasurementActivity,
                            "Error al crear la sesión",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<MeasurementSessionResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@ContinuousIndividualMeasurementActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
    private fun generateMeasurement() {
        val selectedSensor = sensorSpinner.selectedItem.toString()
        val value = when {

            selectedSensor.contains("BME280 Temp") ->
                Random.nextDouble(18.0, 35.0)

            selectedSensor.contains("BME280 Humidity") ->
                Random.nextDouble(30.0, 80.0)

            selectedSensor.contains("BME280 Pressure") ->
                Random.nextDouble(950.0, 1050.0)

            selectedSensor.contains("DS18B20 Temp") ->
                Random.nextDouble(15.0, 28.0)

            selectedSensor.contains("BH1750 Light") ->
                Random.nextDouble(100.0, 900.0)

            selectedSensor.contains("VL53L1X Distance") ->
                Random.nextDouble(5.0, 250.0)

            selectedSensor.contains("HC-SR501 PIR") ->
                Random.nextDouble(0.0, 1.0)

            selectedSensor.contains("Touch Sensor") ->
                Random.nextDouble(0.0, 1.0)

            selectedSensor.contains("RFID MFRC522") ->
                Random.nextDouble(1000.0, 9999.0)

            selectedSensor.contains("Obstacle Sensor") ->
                Random.nextDouble(0.0, 1.0)

            selectedSensor.contains("Speed Sensor") ->
                Random.nextDouble(0.0, 15.0)

            selectedSensor.contains("INA219 Voltage") ->
                Random.nextDouble(3.0, 12.0)

            selectedSensor.contains("INA219 Current") ->
                Random.nextDouble(0.0, 5.0)

            selectedSensor.contains("INA219 Power") ->
                Random.nextDouble(0.0, 60.0)

            selectedSensor.contains("HC-SR04 Ultrasonic") ->
                Random.nextDouble(2.0, 400.0)

            selectedSensor.contains("Reed Switch") ->
                Random.nextDouble(0.0, 1.0)

            selectedSensor.contains("BMI160 Acceleration") ->
                Random.nextDouble(0.0, 20.0)

            selectedSensor.contains("BMI160 Gyroscope") ->
                Random.nextDouble(0.0, 500.0)

            selectedSensor.contains("MPU6050 Acceleration") ->
                Random.nextDouble(0.0, 20.0)

            selectedSensor.contains("MPU6050 Gyroscope") ->
                Random.nextDouble(0.0, 500.0)

            selectedSensor.contains("MQ135 Gas Sensor") ->
                Random.nextDouble(200.0, 1200.0)

            else ->
                Random.nextDouble(0.0, 100.0)
        }

        val currentTime = SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        sendMeasurementToServer(value)

        entries.add(
            Entry(
                measurementIndex,
                value.toFloat()
            )
        )

        sessionMeasurements.add(
            SessionMeasurement(
                time = currentTime,
                value = value
            )
        )

        measurementIndex++

        val dataSet = LineDataSet(entries, selectedSensor)
        dataSet.color = Color.RED
        dataSet.setCircleColor(Color.RED)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f

        lineChart.data = LineData(dataSet)
        lineChart.invalidate()

        val valueText = "%.3f".format(value)

        currentValueTextView.text =
            "$selectedSensor: $valueText"

        addMeasurementRow(
            date = currentTime,
            sensor = selectedSensor,
            value = valueText,
            isHeader = false
        )
    }
    private fun clearCurrentSession() {
        entries.clear()
        sessionMeasurements.clear()
        measurementIndex = 0f

        lineChart.clear()
        lineChart.invalidate()

        currentValueTextView.text = "Valor actual: --"
    }

    private fun sendMeasurementToServer(value: Double) {
        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        val userId =
            sharedPreferences.getInt("userId", -1)

        val request = MeasurementRequest(
            sensorId = getSensorIdByName(sensorSpinner.selectedItem.toString()),
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

                        Toast.makeText(
                            this@ContinuousIndividualMeasurementActivity,
                            "Error servidor",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<MeasurementResponse>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@ContinuousIndividualMeasurementActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun getSensorIdByName(sensorName: String): Long {

        return when {
            sensorName.contains("BME280 Temp") -> 1L
            sensorName.contains("BME280 Humidity") -> 2L
            sensorName.contains("BME280 Pressure") -> 3L
            sensorName.contains("DS18B20 Temp") -> 4L
            sensorName.contains("BH1750 Light") -> 5L
            sensorName.contains("VL53L1X Distance") -> 6L
            sensorName.contains("HC-SR501 PIR") -> 7L
            sensorName.contains("Touch Sensor") -> 8L
            sensorName.contains("RFID MFRC522") -> 9L
            sensorName.contains("Obstacle Sensor") -> 10L
            sensorName.contains("Speed Sensor") -> 11L
            sensorName.contains("INA219 Voltage") -> 12L
            sensorName.contains("INA219 Current") -> 13L
            sensorName.contains("INA219 Power") -> 14L
            sensorName.contains("HC-SR04 Ultrasonic") -> 15L
            sensorName.contains("Reed Switch") -> 16L
            sensorName.contains("BMI160 Acceleration") -> 21L
            sensorName.contains("BMI160 Gyroscope") -> 22L
            sensorName.contains("MPU6050 Acceleration") -> 23L
            sensorName.contains("MPU6050 Gyroscope") -> 24L
            sensorName.contains("MQ135 Gas Sensor") -> 25L
            else -> 1L
        }
    }

    private fun saveSessionAsPdf() {
        if (sessionMeasurements.isEmpty()) {
            Toast.makeText(
                this,
                "No hay mediciones para guardar",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val pdfDocument = PdfDocument()
        val paint = Paint()
        val pageWidth = 595
        val pageHeight = 842

        var pageNumber = 1
        var pageInfo =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()

        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        paint.textSize = 22f
        paint.isFakeBoldText = true
        paint.color = Color.BLACK
        canvas.drawText("Informe de monitorización", 40f, 50f, paint)

        paint.textSize = 14f
        paint.isFakeBoldText = false
        canvas.drawText("Sensor: ${sensorSpinner.selectedItem}", 40f, 80f, paint)

        val bitmap = Bitmap.createBitmap(
            lineChart.width,
            lineChart.height,
            Bitmap.Config.ARGB_8888
        )

        val chartCanvas = Canvas(bitmap)
        lineChart.draw(chartCanvas)

        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, 500, 260, true)

        canvas.drawBitmap(scaledBitmap, 40f, 110f, paint)

        var y = 410f

        paint.textSize = 16f
        paint.isFakeBoldText = true
        canvas.drawText("Fecha y hora", 40f, y, paint)
        canvas.drawText("Valor", 330f, y, paint)

        y += 24f

        paint.textSize = 14f
        paint.isFakeBoldText = false

        for (measurement in sessionMeasurements) {
            if (y > 800f) {
                pdfDocument.finishPage(page)

                pageNumber++

                pageInfo =
                    PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()

                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas

                y = 50f

                paint.textSize = 16f
                paint.isFakeBoldText = true
                canvas.drawText("Fecha y hora", 40f, y, paint)
                canvas.drawText("Valor", 330f, y, paint)

                y += 24f

                paint.textSize = 14f
                paint.isFakeBoldText = false
            }

            canvas.drawText(measurement.time, 40f, y, paint)
            canvas.drawText("%.3f".format(measurement.value), 330f, y, paint)

            y += 22f
        }

        pdfDocument.finishPage(page)

        val fileName =
            "monitorizacion_${System.currentTimeMillis()}.pdf"

        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            fileName
        )

        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        Toast.makeText(
            this,
            "PDF guardado: ${file.name}",
            Toast.LENGTH_LONG
        ).show()
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

    private fun saveSessionAsCsv() {

        if (sessionMeasurements.isEmpty()) {

            Toast.makeText(
                this,
                "No hay mediciones para guardar",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val fileName =
            "monitorizacion_${System.currentTimeMillis()}.csv"

        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            fileName
        )

        val csv = StringBuilder()

        csv.append("Fecha y hora,Sensor,Valor\n")

        val sensorName =
            sensorSpinner.selectedItem.toString()

        for (measurement in sessionMeasurements) {

            csv.append(
                "${measurement.time}," +
                        "$sensorName," +
                        "${"%.3f".format(measurement.value)}\n"
            )
        }

        file.writeText(csv.toString())

        Toast.makeText(
            this,
            "CSV guardado: ${file.name}",
            Toast.LENGTH_LONG
        ).show()
    }
        private fun setupMeasurementsTable() {
            measurementsTable.removeAllViews()

            addMeasurementRow(
                date = "Fecha",
                sensor = "Sensor",
                value = "Valor",
                isHeader = true
            )
        }

        private fun addMeasurementRow(
            date: String,
            sensor: String,
            value: String,
            isHeader: Boolean
        ) {
            val row = TableRow(this)
            row.setPadding(0, 8, 0, 8)

            row.addView(createTableCell(date, isHeader))
            row.addView(createTableCell(sensor, isHeader))
            row.addView(createTableCell(value, isHeader))

            measurementsTable.addView(row)
        }

        private fun createTableCell(
            text: String,
            isHeader: Boolean
        ): TextView {
            val textView = TextView(this)

            textView.text = text
            textView.textSize = if (isHeader) 15f else 14f
            textView.setTextColor(
                if (isHeader) Color.BLACK else Color.rgb(70, 70, 70)
            )
            textView.setPadding(8, 10, 8, 10)

            if (isHeader) {
                textView.setTypeface(null, android.graphics.Typeface.BOLD)
            }

            return textView
        }



    override fun onDestroy() {
        super.onDestroy()
        isMonitoring = false
        handler.removeCallbacks(monitoringTask)
    }

}