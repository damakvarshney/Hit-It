package com.game.hit_it

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class splash_screen : AppCompatActivity() {


    abstract var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences(
            "HITIT_DATA",
            MODE_PRIVATE
        )

        if (sharedPreferences.getBoolean("LOGIN_STATUS", false)) {

            startActivity(Intent(this, play_page::class.java))

            finish()

        } else {

            startActivity(Intent(this, login::class.java))

            finish()

        }


    }


}