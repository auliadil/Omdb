package com.muhammadauliaadil.project.omdb.view

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.muhammadauliaadil.project.omdb.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)
        val timerThread = object : Thread() {
            override fun run() {
                try {
                    sleep(DELAY_DURATION)
                    val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                } catch (e: Exception) {
                }
            }
        }
        timerThread.start()
    }

    companion object {
        private const val DELAY_DURATION = 3000L
    }
}
