package com.grad.graduationproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SplashScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val SPLASH_DISPLAY_LENGTH = 3000
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_splash_screen)
        auth = FirebaseAuth.getInstance();

        val user: FirebaseUser? = auth.currentUser

        Handler().postDelayed(Runnable {

            if (user != null) {
                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(intent)
            } else {
                // User is signed out, send to register/login
                startActivity(Intent(this@SplashScreen, LoginScreen::class.java))
            }
            finish()

        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}