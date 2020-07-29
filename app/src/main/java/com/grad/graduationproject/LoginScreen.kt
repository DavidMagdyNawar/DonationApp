package com.grad.graduationproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login_screen.*

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        signup.setOnClickListener {
            val intent = Intent(this@LoginScreen, SignUp::class.java)
            startActivity(intent)
        }
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        var user: FirebaseUser? = auth.currentUser


        loginCard.setOnClickListener {
            if (valid()) {


                auth.signInWithEmailAndPassword(etEmail.text.toString(), etpassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "signInWithEmail:success")
                            user = auth.currentUser
                            Toast.makeText(
                                baseContext, "Authentication success.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@LoginScreen, MainActivity::class.java)
                            intent.flags =
                                intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                            startActivity(intent)
                            user = auth.currentUser

//                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("failed", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
//                            updateUI(null)
                        }

                    }
            } else {
                Toast.makeText(this, "Enter your email and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun valid(): Boolean {
        var valid = false
        if (etEmail.text.toString() != "" && etpassword.text.toString() != "")
            valid = true

        return valid
    }
}