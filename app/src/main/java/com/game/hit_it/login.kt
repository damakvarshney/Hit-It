package com.game.hit_it

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

abstract class login : AppCompatActivity() {

    abstract var sharedPreferences: SharedPreferences;
    abstract var editor: SharedPreferences.Editor;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = this.getSharedPreferences("HITIT_DATA",
            MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val bundle: Bundle? = intent.extras
        val userid: String? = bundle?.getString("USER_ID")
        Log.e("User ID", "User Id: $userid" )


        not_user.setOnClickListener{
            finish()
        }

        login_btn.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = email_login.text.toString()
        val password = password_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                editor.putBoolean("LOGIN_STATUS",true)
                editor.commit()
                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")
                Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, play_page::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                editor.putBoolean("LOGIN_STATUS",false)
                editor.commit()
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()

            }
    }

}