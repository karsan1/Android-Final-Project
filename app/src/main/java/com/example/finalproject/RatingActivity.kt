package com.example.finalproject

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView

class RatingActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rating)
        val buttonGoBack: Button = findViewById(R.id.goBack)
        val buttonSubmit: Button = findViewById(R.id.submit)
        val lastRating = intent.getFloatExtra("lastRating",0f)
        val ratingBar: RatingBar = findViewById(R.id.ratingBar)
        ratingBar.rating = lastRating
        buttonSubmit.setOnClickListener{ saveRating()}
        buttonGoBack.setOnClickListener { finish()}
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
    }

    private fun saveRating(){
        val feedbackText: TextView = findViewById(R.id.feedbackText)
        feedbackText.text = "We appreciate the feedback!"
        val ratingBar: RatingBar = findViewById(R.id.ratingBar)
        val value = ratingBar.rating
        with(sharedPreferences.edit()){
            putFloat("rating", value)
            apply()
        }

    }
}