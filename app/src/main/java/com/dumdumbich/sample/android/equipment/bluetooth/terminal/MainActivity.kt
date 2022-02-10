package com.dumdumbich.sample.android.equipment.bluetooth.terminal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dumdumbich.sample.android.equipment.bluetooth.terminal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var ui: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)
    }

}