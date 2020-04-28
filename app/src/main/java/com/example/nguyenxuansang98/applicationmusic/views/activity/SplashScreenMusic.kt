package com.example.nguyenxuansang98.applicationmusic.views.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.nguyenxuansang98.applicationmusic.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash_screen_music.*

class SplashScreenMusic : AppCompatActivity() {
    var annimation : Animation?=null
    var annimation1 : Animation?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen_music)
        annimation1 = AnimationUtils.loadAnimation(this,R.anim.flicker)
        imageView.startAnimation(annimation1)
        var timer = object : CountDownTimer(5000,1000){
            override fun onFinish() {
                startActivity(Intent(this@SplashScreenMusic,MainActivity::class.java))
            }

            override fun onTick(millisUntilFinished: Long) {

            }

        }.start()
    }
}
