package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.utils.cancelNotifications
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.custom_button
import kotlinx.android.synthetic.main.content_main.radioButton_glide
import kotlinx.android.synthetic.main.content_main.radioButton_retrofit
import kotlinx.android.synthetic.main.content_main.radioButton_udacity
import kotlinx.android.synthetic.main.content_main.radioGroup

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private var downloadTitle: String = ""
    private var downloadStatus: String = ""
    private lateinit var downloadManager: DownloadManager

    private lateinit var notificationManager: NotificationManager
//    private lateinit var pendingIntent: PendingIntent
//    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        appCreateNotificationChannel()

        setButtonListeners()
    }

    private fun setButtonListeners() {
        custom_button.setOnClickListener {
            when (radioGroup.checkedRadioButtonId) {
                radioButton_glide.id -> {
                    download(URL_GLIDE)
                    downloadTitle = getString(R.string.glide_radioButton_text)
                }
                radioButton_udacity.id -> {
                    download(URL_LOAD_APP)
                    downloadTitle = getString(R.string.udacity_radioButton_text)
                }
                radioButton_retrofit.id -> {
                    download(URL_RETROFIT)
                    downloadTitle = getString(R.string.retrofit_radioButton_text)
                }
                -1 -> Toast.makeText(
                    this@MainActivity,
                    getString(R.string.toast_select_repo),
                    Toast.LENGTH_SHORT
                ).show()
                else -> Toast.makeText(
                    this@MainActivity,
                    getString(R.string.error_select_repo),
                    Toast.LENGTH_SHORT
                ).show()
            }
            custom_button.defineButtonState(ButtonState.Clicked)
        }
    }

    private val receiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE && downloadID == id) {
                custom_button.downloadComplete()

                val query = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))

                if (query.moveToFirst()) {
                    downloadStatus =
                        when (query.getInt(query.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                            DownloadManager.STATUS_SUCCESSFUL -> "Success"
                            DownloadManager.STATUS_FAILED -> "Failed"
                            else -> return
                        }
                }
                notificationManager.sendNotification(context!!, downloadTitle, downloadStatus)
            }
        }
    }

    private fun download(url: String) {

        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

        notificationManager.cancelNotifications()
    }

    private fun appCreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "LoadAppChannel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lightColor = getColor(R.color.colorSecondaryLight)
            notificationChannel.description = "Your download is complete!"

            notificationManager = ContextCompat.getSystemService(
                applicationContext, NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val URL_LOAD_APP =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_GLIDE = "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL_RETROFIT = "https://github.com/square/retrofit/archive/master.zip"

        private const val CHANNEL_ID = "channelId"
    }

}
