package com.example.finalproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.widget.Button
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var sharedPreferences: SharedPreferences
    private var date : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView = findViewById(R.id.calendarView)
        date = calendarView.date
        calendarView.setOnDateChangeListener { view, year, month, day ->
            val cal = Calendar.getInstance()
            cal.set(year, month, day)
            date = cal.timeInMillis
        }
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val adView = findViewById<AdView>(R.id.adView)
        val buttonColorGreen: Button = findViewById(R.id.buttonColorGreen)
        val buttonColorRed: Button = findViewById(R.id.buttonColorRed)
        val buttonColorBlue: Button = findViewById(R.id.buttonColorBlue)
        val buttonSend: Button = findViewById(R.id.buttonSend)
        val buttonRate:Button = findViewById(R.id.Rating)
        val emailHistory:Button = findViewById(R.id.emailHistory)


        buttonColorRed.setOnClickListener { changeColor("#FF0000") }  // Red color
        buttonColorBlue.setOnClickListener { changeColor("#0000FF") } // Blue color
        buttonColorGreen.setOnClickListener { changeColor("#00FF00") } // Green color
        buttonSend.setOnClickListener { sendEmail(timePicker.hour, timePicker.minute, date) }
        buttonRate.setOnClickListener { addRating() }
        emailHistory.setOnClickListener { displayHistory() }


        timePicker.setIs24HourView(true)
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        loadColor()  // Load the saved color

        val adRequest = AdRequest.Builder().build() // Build a new ad request
        adView.loadAd(adRequest) // Load the ad into the AdView
    }

    private fun changeColor(colorHex: String) {
        val color = Color.parseColor(colorHex)
        calendarView.setBackgroundColor(color)
        saveColor(colorHex)
    }

    private fun saveColor(colorHex: String) {
        with(sharedPreferences.edit()) {
            putString("calendarColor", colorHex)
            apply()
        }
    }

    private fun loadColor() {
        val defaultColorHex = "#FFFFFF"
        val savedColorHex = sharedPreferences.getString("calendarColor", defaultColorHex)
        val color = Color.parseColor(savedColorHex)
        calendarView.setBackgroundColor(color)
    }

    private fun sendEmail(hour: Int, minute: Int, date : Long) {
        var myIntent : Intent = Intent(this, EmailActivity::class.java)
        myIntent.putExtra("hour", hour)
        myIntent.putExtra("minute",  minute)
        myIntent.putExtra("date", date)
        startActivity(myIntent)
    }
    private fun addRating(){
        var myIntent: Intent = Intent(this,RatingActivity::class.java)
        val lastRating = sharedPreferences.getFloat("rating",2.5f)
        myIntent.putExtra("lastRating",lastRating)
        this.startActivity(myIntent)

    }

    private fun displayHistory() {
        var myIntent = Intent(this, EmailHistoryActivity::class.java)
        startActivity(myIntent)
    }

}
