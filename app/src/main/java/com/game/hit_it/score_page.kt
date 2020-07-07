package com.game.hit_it

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.game.hit_it.AppController.Companion.uid
import com.google.firebase.database.FirebaseDatabase

class score_page : AppCompatActivity() {

    internal lateinit var score_board: TextView
    internal lateinit var to_ranking_layout: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_page)

        score_board = findViewById(R.id.score_page_layout)
        to_ranking_layout = findViewById(R.id.to_ranking_page)

        val bundle: Bundle? = intent.extras
        val score: Int? = bundle?.getInt("SCORE_ACHIEVED")
        score_board.text = getString(R.string.score_display, score)

        val ref = FirebaseDatabase.getInstance().getReference("/users/${uid}/score")

        ref.setValue(score)
            .addOnSuccessListener {
                Log.e("Score Updated", "Your updated score: $score")
            }
            .addOnFailureListener {
                Log.e("Failed_Database", "Failed to set value to database: ${it.message}")

            }

        to_ranking_layout.setOnClickListener {
            val intent = Intent(this, ranking_page::class.java)
            startActivity(intent)
            finish()
        }
    }


}