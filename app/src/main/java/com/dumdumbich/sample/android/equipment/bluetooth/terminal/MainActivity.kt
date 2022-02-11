package com.dumdumbich.sample.android.equipment.bluetooth.terminal

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dumdumbich.sample.android.equipment.bluetooth.terminal.databinding.ActivityMainBinding
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "@@@ " + MainActivity::class.java.simpleName
        private val BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    private lateinit var ui: ActivityMainBinding
    private val handlerThread: HandlerThread = HandlerThread("thread_pool")
    private val uiHandler by lazy { Handler(mainLooper) }
    private val jobHandler by lazy { Handler(handlerThread.looper) }

    private val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
    private val bluetoothEnableLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                ui.bluetoothTurnOnOffButton.text = "ON"
                bluetoothConnect()
            }
        }
    private var bluetoothPairedDevices: Set<BluetoothDevice>? = null
    private var bluetoothDevice: BluetoothDevice? = null
    private var bluetoothSocket: BluetoothSocket? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        handlerThread.start()

        if (app.bluetoothAdapter.isEnabled) {
            ui.bluetoothTurnOnOffButton.text = "ON"
            ui.bluetoothStatusTextView.text = "Undefined"
        } else {
            ui.bluetoothTurnOnOffButton.text = "OFF"
            ui.bluetoothStatusTextView.text = "Disconnected"
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
            ui.bluetoothStatusTextView.text = "Disconnected"
        }
    }

    private fun bluetoothConnect() {
        jobHandler.post {
            var connectionFail = false
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothPairedDevices = app.bluetoothAdapter.bondedDevices
            }
            bluetoothPairedDevices?.let { devices ->
                devices.forEach { device ->
                    if (device.name == "Sketch") {
                        bluetoothDevice = app.bluetoothAdapter.getRemoteDevice(device.address)
                    }
                }
            }
            try {
                bluetoothDevice?.let { device ->
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(BT_MODULE_UUID)
                }
            } catch (e: IOException) {
                connectionFail = true
                Log.d(TAG, "Socket creation fail")
            }
            Log.d(TAG, "Establish the Bluetooth socket connection...")
            try {
                bluetoothSocket?.connect()
            } catch (e: IOException) {
                try {
                    connectionFail = true
                    bluetoothSocket?.close()
                    Log.d(TAG, "Socket connection fail")
                } catch (e2: IOException) {
                    //insert code to deal with this
                    Log.d(TAG, "Socket connection failed")
                }
            }
            if (!connectionFail) {
                Log.d(TAG, "Connection complete")
                uiHandler.post {
                    ui.bluetoothStatusTextView.text = "${bluetoothDevice?.name} connected"
                }
            }
        }
    }

    override fun onDestroy() {
        handlerThread.quit()
        super.onDestroy()
    }

}