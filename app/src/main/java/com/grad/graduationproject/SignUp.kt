package com.grad.graduationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.etpassword

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        var user: FirebaseUser? = auth.currentUser

        signupCard.setOnClickListener {
            if (valid()) {
                if (etpassword.text.toString() == etpasswordrepeat.text.toString()) {
                    auth.createUserWithEmailAndPassword(
                        ETemail.text.toString(),
                        etpassword.text.toString()
                    )
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("success", "createUserWithEmail:success")
                                val user = auth.currentUser
                                Toast.makeText(
                                    baseContext, "Sign up success, please login",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this@SignUp, LoginScreen::class.java)
                                intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                                startActivity(intent)
                                auth.signOut()
//                            updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("failed", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, "Sign up failed",
                                    Toast.LENGTH_SHORT
                                ).show()
//                            updateUI(null)
                            }

                        }
                } else {
                    Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                Toast.makeText(this, "Enter your email and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun valid(): Boolean {
        var valid = false
        if (ETemail.text.toString() != "" && etpassword.text.toString() != "" && etpasswordrepeat.text.toString() != "")
            valid = true

        return valid
    }
}