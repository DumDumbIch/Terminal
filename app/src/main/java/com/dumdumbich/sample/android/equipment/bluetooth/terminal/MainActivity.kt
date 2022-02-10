package com.dumdumbich.sample.android.equipment.bluetooth.terminal

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.dumdumbich.sample.android.equipment.bluetooth.terminal.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val TAG = "@@@ Terminal: MainActivity"
    private lateinit var ui: ActivityMainBinding


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        app.bluetoothAdapter.takeIf { !it.isEnabled }.also {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val bluetoothEnableLauncher: ActivityResultLauncher<Intent> =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
                    }
                }
            bluetoothEnableLauncher.launch(enableIntent)
        }

        val pairedDevices: Set<BluetoothDevice>? = app.bluetoothAdapter.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            Log.d(TAG, "onCreate() called with: device = [ $deviceName : $deviceHardwareAddress ] ")
        }

    }

}