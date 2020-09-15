package com.game.hit_it

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

 class splash_screen : AppCompatActivity() {


    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences(
            "HITIT_DATA",
            MODE_PRIVATE
        )

       Handler().postDelayed({
           if (sharedPreferences.getBoolean("LOGIN_STATUS", false)) {

               startActivity(Intent(this, play_page::class.java))

               finish()

           } else {

               startActivity(Intent(this, register::class.java))

               finish()

           }
       },2000L)


    }


}