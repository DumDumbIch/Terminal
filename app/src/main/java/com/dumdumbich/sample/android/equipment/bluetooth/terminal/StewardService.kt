package com.dumdumbich.sample.android.equipment.bluetooth.terminal

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import com.dumdumbich.sample.android.equipment.bluetooth.terminal.ui.MainActivity


class StewardService : Service() {

    companion object {
        private val TAG = "@@@ " + StewardService::class.java.simpleName
    }

    private var messageId = 0
    private lateinit var icon: Drawable
    private lateinit var bitmapLargeIcon: Bitmap


    override fun onCreate() {
        Log.d(TAG, "onCreate() called")
        super.onCreate()
        icon = AppCompatResources.getDrawable(this, R.drawable.totoro)!!
        bitmapLargeIcon = icon.toBitmap(200, 200)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(
            TAG,
            "onStartCommand() called with: intent = $intent, flags = $flags, startId = $startId"
        )
        startForeground(++messageId, buildNotification("Data exchange in proceed..."))
        startDataExchange()
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy() called")
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    private fun startDataExchange() {
        Log.d(TAG, "startDataExchange() called")
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun buildNotification(message: String): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, App.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_house_24)
            .setLargeIcon(bitmapLargeIcon)
            .setContentTitle("Steward Service")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Interaction with the smart home system server")
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
        return builder.build()
    }

}