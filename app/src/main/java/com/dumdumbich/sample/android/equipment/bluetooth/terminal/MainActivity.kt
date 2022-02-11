package com.dumdumbich.sample.android.equipment.bluetooth.terminal

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dumdumbich.sample.android.equipment.bluetooth.terminal.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "@@@ " + MainActivity::class.java.simpleName
    }

    private lateinit var ui: ActivityMainBinding

    private val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
    private val bluetoothEnableLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                ui.bluetoothTurnOnOffButton.text = "ON"
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ui.bluetoothStatusTextView.text = "Disconnected"
        if (app.bluetoothAdapter.isEnabled) {
            ui.bluetoothTurnOnOffButton.text = "ON"
        } else {
            ui.bluetoothTurnOnOffButton.text = "OFF"
        }

        ui.bluetoothTurnOnOffButton.setOnClickListener {
            if (app.bluetoothAdapter.isEnabled) {
                bluetoothOff()
            } else {
                bluetoothOn()
            }
        }

    }

    private fun bluetoothOn() {
        app.bluetoothAdapter.takeIf { !it.isEnabled }.also {
            bluetoothEnableLauncher.launch(enableIntent)
        }
    }

    private fun bluetoothOff() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            app.bluetoothAdapter.disable()
            ui.bluetoothTurnOnOffButton.text = "OFF"
        }
    }

}