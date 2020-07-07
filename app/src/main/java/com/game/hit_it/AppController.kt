package com.game.hit_it

import android.app.Application
import com.google.firebase.auth.FirebaseAuth

class AppController : Application(){

    companion object{public lateinit var uid :String}

    override fun onCreate() {
        super.onCreate()

        uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""


    }
}