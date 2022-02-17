package com.dumdumbich.sample.android.equipment.bluetooth.terminal.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dumdumbich.sample.android.equipment.bluetooth.terminal.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    companion object {
        private val TAG = "@@@ " + SplashActivity::class.java.simpleName
    }

    private lateinit var ui: ActivitySplashBinding
    private val handler: Handler by lazy { Handler(mainLooper) }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = $savedInstanceState")
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
        Log.d(TAG, "onDestroy() called")
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}