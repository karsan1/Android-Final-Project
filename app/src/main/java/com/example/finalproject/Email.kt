package com.example.finalproject

import android.content.Intent
import android.os.IBinder.DeathRecipient
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class Email {
    private var hour: Int = 0
    private var minute: Int = 0
    private var date: Long = 0L
    private var recipients : List<String> = listOf()

    constructor()

    constructor(hour: Int, minute: Int, date: Long) {
        this.hour = hour
        this.minute = minute
        this.date = date
    }

    fun createEmail() : Intent {
        var emailIntent : Intent = Intent(Intent.ACTION_SEND)
        var dateFormatted : Date = Date(date)
        emailIntent.setType( "text/plain" )
        emailIntent.putExtra( Intent.EXTRA_SUBJECT, "Is this a good time to meet?" )
        if (recipients.isNotEmpty()) {
            emailIntent.putExtra( Intent.EXTRA_EMAIL, recipients.toTypedArray() )
        }
        emailIntent.putExtra( Intent.EXTRA_TEXT, "Let's meet on " + dateFormatted.toString())
        return (Intent.createChooser(emailIntent, "Share the date."))
    }

    fun getTime() : String {
        var dateFormatted : Date = Date(date)
        return (dateFormatted.month + 1).toString() + "/" + dateFormatted.date + "/" + (dateFormatted.year + 1900) + " at " + dateFormatted.hours + ":" + dateFormatted.minutes
    }

    fun setRecipients(recipient : List<String>) {
        this.recipients = recipient
    }

    fun getRecipients() : List<String> {
        return this.recipients
    }
}
