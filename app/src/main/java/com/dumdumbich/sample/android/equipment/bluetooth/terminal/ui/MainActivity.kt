package com.dumdumbich.sample.android.equipment.bluetooth.terminal.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dumdumbich.sample.android.equipment.bluetooth.terminal.app
import com.dumdumbich.sample.android.equipment.bluetooth.terminal.data.hal.bluetooth.Bluetooth
import com.dumdumbich.sample.android.equipment.bluetooth.terminal.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "@@@ " + MainActivity::class.java.simpleName
    }

    private lateinit var ui: ActivityMainBinding
    private val handlerThread: HandlerThread = HandlerThread("thread_pool")
    private val uiHandler by lazy { Handler(mainLooper) }
    private val jobHandler by lazy { Handler(handlerThread.looper) }

    private val bluetoothEnableLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                ui.bluetoothTurnOnOffButton.text = "ON"
                bluetoothConnect()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = $savedInstanceState")
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        handlerThread.start()

        if (app.bluetooth.isOn()) {
            ui.bluetoothTurnOnOffButton.text = "ON"
            ui.bluetoothStatusTextView.text = "Undefined"
        } else {
            ui.bluetoothTurnOnOffButton.text = "OFF"
            ui.bluetoothStatusTextView.text = "Disconnected"
        }

        ui.bluetoothTurnOnOffButton.setOnClickListener {
            if (app.bluetooth.isOn()) {
                bluetoothOff()
            } else {
                bluetoothOn()
            }
        }

        ui.outputMessageTextInputLayout.setEndIconOnClickListener {
            val outputMessage = ui.outputMessageEditText.text.toString()
            Toast.makeText(this, "Output message: $outputMessage", Toast.LENGTH_SHORT).show()
            app.bluetooth.outputMessage = outputMessage
        }

    }

    private fun bluetoothOn() {
        if (app.bluetooth.isOff()) {
            bluetoothEnableLauncher.launch(Bluetooth.enableIntent)
        }
    }

    private fun bluetoothOff() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            app.bluetooth.off()
            ui.bluetoothTurnOnOffButton.text = "OFF"
            ui.bluetoothStatusTextView.text = "Disconnected"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bluetoothConnect() {
        jobHandler.post {
            if (app.bluetooth.connect()) {
                uiHandler.post {
                    ui.bluetoothStatusTextView.text =
                        "${app.bluetooth.deviceName} connected"

                }
            }
        }
    }

    override fun onDestroy() {
        handlerThread.quit()
        super.onDestroy()
    }

}