package com.dumdumbich.sample.android.equipment.bluetooth.terminal.data.hal.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.*


class Bluetooth(private val context: Context) {

    companion object {
        private val TAG = "@@@ " + Bluetooth::class.java.simpleName
        val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        val BT_MODULE_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    private var manager: BluetoothManager =
        context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager

    private var adapter: BluetoothAdapter = manager.adapter

    private var pairedDevices: Set<BluetoothDevice>? = null

    private var device: BluetoothDevice? = null

    private var socket: BluetoothSocket? = null

    var deviceName = "Undefined"
        private set

    var isConnected: Boolean = false
        private set

    var outputMessage: String = ""


    fun connect(): Boolean {
        var connectionFail = false
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            pairedDevices = adapter.bondedDevices
        }
        pairedDevices?.let { devices ->
            devices.forEach { it ->
                if (it.name == "Sketch") {
                    Log.d(TAG, "connect() called with: it = ${it.name} : ${it.address}")
                    device = adapter.getRemoteDevice(it.address)
                }
            }
        }
        try {
            device?.let { device ->
                socket =
                    device.createRfcommSocketToServiceRecord(BT_MODULE_UUID)
            }
        } catch (e: IOException) {
            connectionFail = true
            Log.d(TAG, "Socket creation fail")
            isConnected = false
        }
        Log.d(TAG, "Establish the Bluetooth socket connection...")
        try {
            socket?.connect()
        } catch (e: IOException) {
            try {
                connectionFail = true
                socket?.close()
                Log.d(TAG, "Socket connection fail")
            } catch (e2: IOException) {
                //insert code to deal with this
                Log.d(TAG, "Socket connection failed")
            }
        }
        if (!connectionFail) {
            Log.d(TAG, "Connection complete")
            isConnected = true
            deviceName = device?.name.toString()
            socket?.let {
                // Run data exchange with Steward device
            }
        }
        return !connectionFail
    }

    fun isOn() = adapter.isEnabled

    fun isOff() = !adapter.isEnabled

    @SuppressLint("MissingPermission")
    fun off() {
        Log.d(TAG, "off() called")
        adapter.disable()
    }

}