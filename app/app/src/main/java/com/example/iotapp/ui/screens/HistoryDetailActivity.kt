package com.example.iotapp.ui.screens

import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapp.R
import com.example.iotapp.data.RetrofitClient
import com.example.iotapp.model.MeasurementHistoryResponse
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import android.widget.TableLayout
import android.widget.TableRow


class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var historyChart: LineChart
    private lateinit var historyTable: TableLayout


    private var sessionId: Int = -1
    private var measurements = listOf<MeasurementHistoryResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history_detail)

        sessionId = intent.getIntExtra("sessionId", -1)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
        val savePdfButton = findViewById<TextView>(R.id.savePdfButton)
        val saveCsvButton = findViewById<TextView>(R.id.saveCsvButton)

        historyChart = findViewById(R.id.historyChart)
        historyTable = findViewById(R.id.historyTable)

        backButton.setOnClickListener {
            finish()
        }

        savePdfButton.setOnClickListener {
            savePdf()
        }

        saveCsvButton.setOnClickListener {
            saveCsv()
        }

        setupBottomNavigation()
        loadMeasurements()
    }

    private fun loadMeasurements() {
        RetrofitClient.apiService.getMeasurementsBySession(sessionId)
            .enqueue(object : Callback<List<MeasurementHistoryResponse>> {

                override fun onResponse(
                    call: Call<List<MeasurementHistoryResponse>>,
                    response: Response<List<MeasurementHistoryResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        measurements = response.body()!!

                        if (measurements.isEmpty()) {
                            Toast.makeText(
                                this@HistoryDetailActivity,
                                "No hay mediciones en esta sesión",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        drawChart()
                        drawTable()

                    } else {
                        Toast.makeText(
                            this@HistoryDetailActivity,
                            "Error al cargar mediciones",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<MeasurementHistoryResponse>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@HistoryDetailActivity,
                        "Error de conexion",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun drawChart() {
        val grouped = measurements.groupBy { it.sensorName }
        val dataSets = ArrayList<LineDataSet>()

        var colorIndex = 0
        val colors = listOf(
            Color.rgb(239, 83, 80),
            Color.rgb(66, 165, 245),
            Color.rgb(102, 187, 106),
            Color.rgb(171, 71, 188),
            Color.rgb(255, 167, 38),
            Color.rgb(38, 166, 154)
        )

        for ((sensorName, sensorMeasurements) in grouped) {
            val entries = sensorMeasurements.mapIndexed { index, measurement ->
                Entry(index.toFloat(), measurement.value.toFloat())
            }

            val dataSet = LineDataSet(entries, sensorName)
            val color = colors[colorIndex % colors.size]

            dataSet.color = color
            dataSet.setCircleColor(color)
            dataSet.lineWidth = 2f
            dataSet.circleRadius = 4f

            dataSets.add(dataSet)
            colorIndex++
        }

        historyChart.data = LineData(dataSets as List<ILineDataSet>)
        historyChart.description.isEnabled = false
        historyChart.invalidate()
    }

    private fun drawTable() {
        historyTable.removeAllViews()

        addTableRow("Fecha", "Sensor", "Valor", true)

        for (m in measurements) {
            addTableRow(
                formatDate(m.measuredAt),
                m.sensorName.replace(" Sensor", ""),
                "%.3f ${m.unit}".format(m.value),
                false
            )
        }
    }

    private fun addTableRow(
        date: String,
        sensor: String,
        value: String,
        isHeader: Boolean
    ) {
        val row = TableRow(this)
        row.setPadding(0, 8, 0, 8)

        val dateText = createTableCell(date, isHeader)
        val sensorText = createTableCell(sensor, isHeader)
        val valueText = createTableCell(value, isHeader)

        row.addView(dateText)
        row.addView(sensorText)
        row.addView(valueText)

        historyTable.addView(row)
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

    private fun saveCsv() {
        if (measurements.isEmpty()) {
            Toast.makeText(this, "No hay datos para guardar", Toast.LENGTH_SHORT).show()
            return
        }

        val fileName = "historial_${System.currentTimeMillis()}.csv"
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        val csv = StringBuilder()
        csv.append("Fecha y hora,Sensor,Valor,Unidad\n")

        for (m in measurements) {
            csv.append(
                "${formatDate(m.measuredAt)},${m.sensorName},${"%.3f".format(m.value)},${m.unit}\n"
            )
        }

        file.writeText(csv.toString())

        Toast.makeText(this, "CSV guardado: ${file.name}", Toast.LENGTH_LONG).show()
    }

    private fun savePdf() {
        if (measurements.isEmpty()) {
            Toast.makeText(this, "No hay datos para guardar", Toast.LENGTH_SHORT).show()
            return
        }

        val pdfDocument = PdfDocument()
        val paint = Paint()
        val pageWidth = 595
        val pageHeight = 842

        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        paint.color = Color.BLACK
        paint.textSize = 22f
        paint.isFakeBoldText = true
        canvas.drawText("Detalle del historial", 40f, 50f, paint)

        val bitmap = Bitmap.createBitmap(
            historyChart.width,
            historyChart.height,
            Bitmap.Config.ARGB_8888
        )

        val chartCanvas = Canvas(bitmap)
        historyChart.draw(chartCanvas)

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 500, 260, true)
        canvas.drawBitmap(scaledBitmap, 40f, 90f, paint)

        var y = 390f

        paint.textSize = 16f
        paint.isFakeBoldText = true
        canvas.drawText("Fecha y hora", 40f, y, paint)
        canvas.drawText("Sensor", 220f, y, paint)
        canvas.drawText("Valor", 410f, y, paint)

        y += 24f
        paint.textSize = 13f
        paint.isFakeBoldText = false

        for (m in measurements) {
            if (y > 800f) {
                pdfDocument.finishPage(page)

                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas

                y = 50f
            }

            canvas.drawText(formatDate(m.measuredAt), 40f, y, paint)
            canvas.drawText(m.sensorName.take(20), 220f, y, paint)
            canvas.drawText("%.3f ${m.unit}".format(m.value), 410f, y, paint)

            y += 22f
        }

        pdfDocument.finishPage(page)

        val fileName = "historial_${System.currentTimeMillis()}.pdf"
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        Toast.makeText(this, "PDF guardado: ${file.name}", Toast.LENGTH_LONG).show()
    }

    private fun formatDate(rawDate: String): String {
        return rawDate
            .replace("T", " ")
            .substringBefore(".")
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