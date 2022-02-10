package com.dumdumbich.sample.android.equipment.bluetooth.terminal

import android.app.Application
import android.content.pm.PackageManager


class App : Application() {

    private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)

    override fun onCreate() {
        super.onCreate()
        // Check to see if the Bluetooth classic feature is available.
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH) }?.also {
            throw IllegalAccessException("Bluetooth not supported on this device")
        }
    }

}