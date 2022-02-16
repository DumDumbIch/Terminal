package com.dumdumbich.sample.android.equipment.bluetooth.terminal.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dumdumbich.sample.android.equipment.bluetooth.terminal.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var ui: ActivitySplashBinding
    private val handler: Handler by lazy { Handler(mainLooper) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ObjectAnimator.ofFloat(ui.startPictureImageView, View.SCALE_Y, -1f).apply {
            duration = 1_000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        handler.postDelayed({
            Intent(this@SplashActivity, MainActivity::class.java).also { intent ->
                startActivity(intent)
            }
            finish()
        }, 3_000)

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}