package com.example.finalproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.Email
import com.google.firebase.database.FirebaseDatabase

class EmailActivity : AppCompatActivity() {
    private lateinit var email : Email
    private lateinit var recipients : EditText
    private lateinit var timeConfirm : TextView
    private val databaseReference = FirebaseDatabase.getInstance().getReference("emails")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val date = intent.getLongExtra("date", 0L)

        email = Email(hour, minute, date)

        recipients = findViewById(R.id.emails)
        timeConfirm = findViewById(R.id.timeConfirm)
        timeConfirm.setText("is " + email.getTime() + " the correct time?")

        val confirmButton : Button = findViewById(R.id.confirmButton)
        val returnButton : Button = findViewById(R.id.returnButton)

        confirmButton.setOnClickListener {confirm()}
        returnButton.setOnClickListener { goBack() }
    }

    fun goBack() {
        finish()
    }

    fun confirm() {
//        val recipientList = recipients.text.split(",").toTypedArray()\
        val recipientList = recipients.text.toString().split(",").map { it.trim() }

        email.setRecipients(recipientList)
        var intent = email.createEmail()
        sendEmailToFirebase(email)
        startActivity(intent)
        finish()
    }

    fun sendEmailToFirebase(email: Email) {
        Log.w("MainActivity", "in send to firebase")
        val key = databaseReference.push().key
        if (key != null) {
            databaseReference.child(key).setValue(email)
                .addOnSuccessListener {
                    Log.w("MainActivity", "we chillin")
                    Toast.makeText(this, "Email saved", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add email", Toast.LENGTH_LONG).show()
                }
        }
    }

}