package com.example.finalproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log

class EmailHistoryActivity : AppCompatActivity() {
    private lateinit var emailHistoryList: MutableList<Email>
    private var currentEmailIndex : Int = 0
    private lateinit var emailContentView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emailhistory)
        emailContentView = findViewById(R.id.emailContent)
        emailHistoryList = mutableListOf()
        displayEmailHistory(emailHistoryList)
        val returnButton : Button = findViewById(R.id.backButton)
        returnButton.setOnClickListener { goBack() }
        val previous : Button = findViewById(R.id.previousEmail)
        previous.setOnClickListener { showPreviousEmail() }
        val next : Button = findViewById(R.id.nextEmail)
        next.setOnClickListener { showNextEmail() }
    }

    fun goBack() {
        finish()
    }

    private fun showPreviousEmail() {
        if (currentEmailIndex > 0) {
            currentEmailIndex--
            updateEmailHistoryUI(emailHistoryList[currentEmailIndex])
        }
    }

    private fun showNextEmail() {
        if (currentEmailIndex < emailHistoryList.size - 1) {
            currentEmailIndex++
            updateEmailHistoryUI(emailHistoryList[currentEmailIndex])
        }
    }

    private fun updateEmailHistoryUI(email: Email?) {
        val emailContent = if (email != null) buildString {
            appendLine("Recipients: ${email.getRecipients().joinToString(", ")}")
            appendLine()
            appendLine("Navigate using the buttons below:")
            appendLine("- 'Go Back' to return to the previous screen")
            appendLine("- 'Previous Email' to view the previous email in the history")
            appendLine("- 'Next Email' to view the next email in the history")
        } else {
            "No emails"
        }

        emailContentView.text = emailContent
    }

    private fun displayEmailHistory(emailHistoryList: MutableList<Email>) {
        val database = FirebaseDatabase.getInstance()
        val emailsRef = database.reference.child("emails")

        val emailListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                emailHistoryList.clear()

                for (emailSnapshot in dataSnapshot.children) {
                    val email = emailSnapshot.getValue(Email::class.java)
                    if (email != null) {
                        emailHistoryList.add(email)
                    }
                }
                if (emailHistoryList.isNotEmpty()) {
                    currentEmailIndex = 0
                    updateEmailHistoryUI(emailHistoryList[currentEmailIndex])
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("EmailHistoryActivity", "Failed to read emails: ${databaseError.message}")
            }
        }

        emailsRef.addValueEventListener(emailListener)
    }
}