package com.dumdumbich.sample.android.equipment.bluetooth.terminal

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.util.*


class App : Application() {

    companion object {
        private val TAG = "@@@ " + App::class.java.simpleName
        val NOTIFICATION_CHANNEL_ID = UUID.randomUUID().toString()
    }

    private lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter


    override fun onCreate() {
        Log.d(TAG, "onCreate() called")
        super.onCreate()
        // Check to see if the Bluetooth classic feature is available.
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH) }?.also {
            throw IllegalAccessException("Bluetooth not supported on this device")
        }
        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        setNotificationChannel()
        startStewardService()

    }

    private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)

    private fun setNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Steward", importance)
            manager.createNotificationChannel(channel)
        }
    }

    private fun startStewardService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, StewardService::class.java))
        } else {
            startService(Intent(this, StewardService::class.java))
        }
    }

}


val Context.app: App
    get() = applicationContext as App