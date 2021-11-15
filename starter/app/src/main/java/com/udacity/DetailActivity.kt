package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.utils.cancelNotifications
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.button_ok
import kotlinx.android.synthetic.main.content_detail.textView_fileName
import kotlinx.android.synthetic.main.content_detail.textView_status

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.cancelNotifications()

        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        intent.getStringExtra("download").let {
            textView_fileName.text = it
        }
        intent.getStringExtra("status").let {
            textView_status.text = it
        }

        button_ok.setOnClickListener {
            val intent = Intent(this@DetailActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
