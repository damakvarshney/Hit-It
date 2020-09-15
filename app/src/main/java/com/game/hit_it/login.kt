package com.game.hit_it

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*

// asshole
 class login : AppCompatActivity() {

     lateinit var sharedPreferences: SharedPreferences;
     lateinit var editor: SharedPreferences.Editor;
        lateinit var progressDialog: SpotsDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressDialog = SpotsDialog(this, R.style.custom_progressDialog)
        sharedPreferences = this.getSharedPreferences(
            "HITIT_DATA",
            MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
        val bundle: Bundle? = intent.extras
        val userid: String? = bundle?.getString("USER_ID")
        Log.e("User ID", "User Id: $userid")


        not_user.setOnClickListener{
            startActivity(Intent(this, register::class.java))
            finish()
        }

        login_btn.setOnClickListener {
            performLogin()
            onPreExecute()
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
                onPostExecute();
                editor.putBoolean("LOGIN_STATUS", true)
                editor.commit()
                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")
                Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, play_page::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                onPostExecute()
                editor.putBoolean("LOGIN_STATUS", false)
                editor.commit()
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()

            }
    }

    fun to_register(view: View) {
        startActivity(Intent(this, register::class.java))
    }

    //Am using it in an AsyncTask. So in  my onPreExecute, I do this:
    fun onPreExecute() {
        progressDialog.show()
    }

    //dismiss in onPostExecute
    fun onPostExecute() {
        progressDialog.dismiss()
    }
}