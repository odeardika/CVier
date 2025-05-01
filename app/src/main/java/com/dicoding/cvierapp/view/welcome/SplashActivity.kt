package com.dicoding.cvierapp.view.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.cvierapp.databinding.ActivitySplashscreenBinding
import com.dicoding.cvierapp.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity: AppCompatActivity() {

    private lateinit var splashBinding: ActivitySplashscreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        auth = FirebaseAuth.getInstance()

        setAction()
    }

    private fun setAction() {

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000

        fadeIn.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                    checkLoginStatus()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        splashBinding.splashscreenImage.startAnimation(fadeIn)
    }

    private fun checkLoginStatus() {
        val user = auth.currentUser
        Log.d(TAG, "Current user: ${user?.uid}")
        if (user != null) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
        }
        finish()
    }

    companion object {
        private const val TAG = "Splash Activity"
    }
}