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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R
import com.example.iotapp.data.RetrofitClient
import com.example.iotapp.model.*
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
import android.widget.TableLayout
import android.widget.TableRow

class ContinuousSimultaneousMeasurementActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var isMonitoring = false
    private var measurementIndex = 0f
    private var currentSessionId: Int = -1

    private lateinit var intervalEditText: EditText
    private lateinit var measurementsTable: TableLayout
    private lateinit var chartsContainer: LinearLayout

    private lateinit var temperatureCheck: CheckBox
    private lateinit var humidityCheck: CheckBox
    private lateinit var pressureCheck: CheckBox
    private lateinit var lightCheck: CheckBox
    private lateinit var distanceCheck: CheckBox
    private lateinit var currentCheck: CheckBox
    private lateinit var voltageCheck: CheckBox
    private lateinit var powerCheck: CheckBox
    private lateinit var gasCheck: CheckBox

    private val chartMap = mutableMapOf<String, LineChart>()
    private val entriesMap = mutableMapOf<String, ArrayList<Entry>>()
    private val sessionMeasurementsMap =
        mutableMapOf<String, ArrayList<SessionMeasurement>>()

    private val monitoringTask = object : Runnable {
        override fun run() {
            if (isMonitoring) {
                generateMeasurements()

                val intervalSeconds =
                    intervalEditText.text.toString().toLongOrNull() ?: 2

                handler.postDelayed(this, intervalSeconds * 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_continuous_simultaneous_measurement)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        val savePdfButton = findViewById<TextView>(R.id.savePdfButton)
        val saveCsvButton = findViewById<TextView>(R.id.saveCsvButton)

        intervalEditText = findViewById(R.id.intervalEditText)
        measurementsTable = findViewById(R.id.measurementsTable)
        chartsContainer = findViewById(R.id.chartsContainer)

        temperatureCheck = findViewById(R.id.temperatureCheck)
        humidityCheck = findViewById(R.id.humidityCheck)
        pressureCheck = findViewById(R.id.pressureCheck)
        lightCheck = findViewById(R.id.lightCheck)
        distanceCheck = findViewById(R.id.distanceCheck)
        currentCheck = findViewById(R.id.currentCheck)
        voltageCheck = findViewById(R.id.voltageCheck)
        powerCheck = findViewById(R.id.powerCheck)
        gasCheck = findViewById(R.id.gasCheck)

        backButton.setOnClickListener {
            finish()
        }

        setupBottomNavigation()
        setupMeasurementsTable()

        startButton.setOnClickListener {
            val selectedSensors = getSelectedSensors()

            if (selectedSensors.isEmpty()) {
                return@setOnClickListener
            }

            clearSession()
            createChartsForSelectedSensors(selectedSensors)
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

    private fun createSessionAndStartMonitoring() {
        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        val userId =
            sharedPreferences.getInt("userId", -1)

        if (userId == -1) {
            Toast.makeText(
                this,
                "Usuario no identificado",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val request = CreateMeasurementSessionRequest(
            userId = userId,
            mode = "CONTINUOUS",
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

                        isMonitoring = true
                        handler.post(monitoringTask)

                    } else {
                        Toast.makeText(
                            this@ContinuousSimultaneousMeasurementActivity,
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
                        this@ContinuousSimultaneousMeasurementActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun getSelectedSensors(): List<String> {

        val sensors = mutableListOf<String>()

        if (temperatureCheck.isChecked) {
            sensors.add("BME280 Temp — Temperatura (°C)")
            sensors.add("DS18B20 Temp — Temperatura del agua (°C)")
        }

        if (humidityCheck.isChecked) {
            sensors.add("BME280 Humidity — Humedad (%)")
        }

        if (pressureCheck.isChecked) {
            sensors.add("BME280 Pressure — Presión (hPa)")
        }

        if (lightCheck.isChecked) {
            sensors.add("BH1750 Light — Luz (lux)")
        }

        if (distanceCheck.isChecked) {
            sensors.add("VL53L1X Distance — Distancia (cm)")
            sensors.add("HC-SR04 Ultrasonic — Distancia ultrasónica")
        }

        if (currentCheck.isChecked) {
            sensors.add("INA219 Current — Corriente (A)")
        }

        if (voltageCheck.isChecked) {
            sensors.add("INA219 Voltage — Voltaje (V)")
        }

        if (powerCheck.isChecked) {
            sensors.add("INA219 Power — Potencia (W)")
        }

        if (gasCheck.isChecked) {
            sensors.add("MQ135 Gas Sensor — Gas (ppm)")
        }

        return sensors
    }

    private fun createChartsForSelectedSensors(selectedSensors: List<String>) {
        for (sensor in selectedSensors) {
            val title = TextView(this)
            title.text = sensor
            title.textSize = 18f
            title.setTextColor(Color.rgb(74, 35, 90))
            title.setTypeface(null, android.graphics.Typeface.BOLD)
            title.setPadding(0, 28, 0, 8)

            val chart = LineChart(this)
            chart.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                320
            )

            chartsContainer.addView(title)
            chartsContainer.addView(chart)

            chartMap[sensor] = chart
            entriesMap[sensor] = ArrayList()
            sessionMeasurementsMap[sensor] = ArrayList()
        }
    }

    private fun generateMeasurements() {
        val selectedSensors = getSelectedSensors()

        val currentTime = SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        for (sensor in selectedSensors) {
            val value = generateValue(sensor)

            sendMeasurementToServer(sensor, value)

            val entries = entriesMap[sensor] ?: continue
            entries.add(
                Entry(
                    measurementIndex,
                    value.toFloat()
                )
            )

            sessionMeasurementsMap[sensor]?.add(
                SessionMeasurement(
                    time = currentTime,
                    value = value
                )
            )

            val dataSet = LineDataSet(entries, sensor)
            dataSet.color = getColorForSensor(sensor)
            dataSet.setCircleColor(getColorForSensor(sensor))
            dataSet.lineWidth = 2f
            dataSet.circleRadius = 4f

            val chart = chartMap[sensor] ?: continue
            chart.data = LineData(dataSet)
            chart.invalidate()

            addMeasurementRow(
                date = currentTime,
                sensor = sensor,
                value = "%.3f".format(value),
                isHeader = false
            )
        }

        measurementIndex++
    }

    private fun sendMeasurementToServer(sensor: String, value: Double) {
        val sharedPreferences =
            getSharedPreferences("user_session", MODE_PRIVATE)

        val userId =
            sharedPreferences.getInt("userId", -1)

        val request = MeasurementRequest(
            sensorId = getSensorIdByName(sensor),
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
                            this@ContinuousSimultaneousMeasurementActivity,
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
                        this@ContinuousSimultaneousMeasurementActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
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
            sensor.contains("Current") -> Random.nextDouble(0.0, 5.0)
            sensor.contains("Voltage") -> Random.nextDouble(0.0, 12.0)
            sensor.contains("Power") -> Random.nextDouble(0.0, 60.0)
            sensor.contains("Gas") -> Random.nextDouble(200.0, 1200.0)
            else -> Random.nextDouble(0.0, 100.0)
        }
    }

    private fun getColorForSensor(sensor: String): Int {
        return when {
            sensor.contains("Temperature") -> Color.rgb(239, 83, 80)
            sensor.contains("Humidity") -> Color.rgb(66, 165, 245)
            sensor.contains("Pressure") -> Color.rgb(171, 71, 188)
            sensor.contains("Light") -> Color.rgb(255, 167, 38)
            sensor.contains("Distance") -> Color.rgb(38, 166, 154)
            sensor.contains("Current") -> Color.rgb(102, 187, 106)
            sensor.contains("Voltage") -> Color.rgb(92, 107, 192)
            sensor.contains("Power") -> Color.rgb(141, 110, 99)
            sensor.contains("Gas") -> Color.rgb(84, 110, 122)
            else -> Color.BLACK
        }
    }

    private fun getSensorIdByName(sensorName: String): Long {
        return when {
            sensorName.contains("Temperature") -> 1L
            sensorName.contains("Humidity") -> 2L
            sensorName.contains("Pressure") -> 3L
            sensorName.contains("Light") -> 4L
            sensorName.contains("Distance") -> 5L
            sensorName.contains("Current") -> 13L
            sensorName.contains("Voltage") -> 14L
            sensorName.contains("Power") -> 15L
            sensorName.contains("Gas") -> 16L
            else -> 1L
        }
    }

    private fun clearSession() {
        isMonitoring = false
        handler.removeCallbacks(monitoringTask)

        measurementIndex = 0f
        currentSessionId = -1

        chartMap.clear()
        entriesMap.clear()
        sessionMeasurementsMap.clear()

        chartsContainer.removeAllViews()
    }

    private fun saveSessionAsCsv() {
        if (sessionMeasurementsMap.isEmpty()) {
            Toast.makeText(
                this,
                "No hay mediciones para guardar",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val fileName =
            "monitorizacion_simultanea_${System.currentTimeMillis()}.csv"

        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            fileName
        )

        val csv = StringBuilder()
        csv.append("Fecha y hora,Sensor,Valor\n")

        for ((sensor, measurements) in sessionMeasurementsMap) {
            for (measurement in measurements) {
                csv.append(
                    "${measurement.time}," +
                            "$sensor," +
                            "${"%.3f".format(measurement.value)}\n"
                )
            }
        }

        file.writeText(csv.toString())

        Toast.makeText(
            this,
            "CSV guardado: ${file.name}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun saveSessionAsPdf() {
        if (sessionMeasurementsMap.isEmpty()) {
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

        fun startNewPage(): PdfDocument.Page {
            val pageInfo =
                PdfDocument.PageInfo.Builder(
                    pageWidth,
                    pageHeight,
                    pageNumber
                ).create()

            pageNumber++
            return pdfDocument.startPage(pageInfo)
        }

        for ((sensor, measurements) in sessionMeasurementsMap) {
            val chart = chartMap[sensor] ?: continue

            var page = startNewPage()
            var canvas = page.canvas

            paint.color = Color.BLACK
            paint.textSize = 22f
            paint.isFakeBoldText = true
            canvas.drawText(
                "Informe de monitorización simultánea",
                40f,
                50f,
                paint
            )

            paint.textSize = 14f
            paint.isFakeBoldText = false
            canvas.drawText("Sensor: $sensor", 40f, 80f, paint)

            val bitmap = Bitmap.createBitmap(
                chart.width,
                chart.height,
                Bitmap.Config.ARGB_8888
            )

            val chartCanvas = Canvas(bitmap)
            chart.draw(chartCanvas)

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

            for (measurement in measurements) {
                if (y > 800f) {
                    pdfDocument.finishPage(page)

                    page = startNewPage()
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
                canvas.drawText(
                    "%.3f".format(measurement.value),
                    330f,
                    y,
                    paint
                )

                y += 22f
            }

            pdfDocument.finishPage(page)
        }

        val fileName =
            "monitorizacion_simultanea_${System.currentTimeMillis()}.pdf"

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
            if (isHeader)
                Color.BLACK
            else
                Color.rgb(70, 70, 70)
        )

        textView.setPadding(8, 10, 8, 10)

        if (isHeader) {
            textView.setTypeface(
                null,
                android.graphics.Typeface.BOLD
            )
        }

        return textView
    }

    override fun onDestroy() {
        super.onDestroy()

        isMonitoring = false
        handler.removeCallbacks(monitoringTask)
    }
}