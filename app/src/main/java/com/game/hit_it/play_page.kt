package com.game.hit_it

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_play_page.*

class play_page : AppCompatActivity() {

    internal var score = 0
    internal lateinit var tap_me_btn: Button
    internal lateinit var time_left_txtview: TextView
    internal lateinit var before_start_text: TextView
    internal lateinit var after_start_text: TextView
    internal lateinit var score_layout: LinearLayout
    internal lateinit var playing_page: FrameLayout
    internal lateinit var take_to_score: ImageView
    internal lateinit var gamescoretxtview: TextView

    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal var initialCountdown: Long = 20000
    internal var countdowninterval: Long = 1000
    internal var timeLeftOnTimer: Long = 20000
    private lateinit var mediaPlayer: MediaPlayer


    companion object {
        private val TAG = play_page::class.java.simpleName
        private const val SCORE_KEY = "SCORE KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_page)


        Log.e(TAG, "onCreate called. Score is: $score")

        tap_me_btn = findViewById(R.id.play_button)
        gamescoretxtview = findViewById(R.id.your_score_textview)
        time_left_txtview = findViewById(R.id.timer_textview)
        take_to_score = findViewById(R.id.score_display_imageview)
        before_start_text = findViewById(R.id.before_start_text)
        after_start_text = findViewById(R.id.after_start_text)
        score_layout = findViewById(R.id.Score_layout)
        playing_page = findViewById(R.id.play_page_framelayout)

        mediaPlayer = MediaPlayer.create(this, R.raw.click_sound)
        mediaPlayer.setOnPreparedListener {
            Log.e("Sound", "ting")
        }
        before_start_text.visibility = View.VISIBLE


        time_left_txtview.text = getString(R.string.time_left, 0)
        gamescoretxtview.text = getString(R.string.your_score, 0)



        tap_me_btn.setOnClickListener {
            incrementFunction()
        }
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoregame()
        } else {
            resetgame()
        }


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("SCORE_KEY", score)
        outState.putLong("TIME_LEFT_KEY", timeLeftOnTimer)
        countDownTimer.cancel()

        Log.e(TAG, "onSavedInstanceState: Saving Score: $score & Time Left: $timeLeftOnTimer  ")
    }

    override fun onResume() {
        super.onResume()
        if(score>0){
            restoregame()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ")
    }

    private fun resetgame() {
        gameStarted = false
        score = 0
        val initialTimeLeft = initialCountdown / 1000
        time_left_txtview.text = getString(R.string.time_left, initialTimeLeft)
        gamescoretxtview.text = getString(R.string.your_score, score)

        countDownTimer = object : CountDownTimer(initialCountdown, countdowninterval) {
            override fun onTick(millisUntilFinished: Long) {
                val time_left = millisUntilFinished / 1000
                timeLeftOnTimer = millisUntilFinished
                time_left_txtview.text = getString(R.string.time_left, time_left)
            }

            override fun onFinish() {
                endgame()
            }
        }
        gameStarted = false
    }

    private fun restoregame() {
        gamescoretxtview.text = getString(R.string.your_score, score)
        val restoredTime = timeLeftOnTimer / 1000
        time_left_txtview.text = getString(R.string.time_left, restoredTime)

        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countdowninterval) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val time_left = millisUntilFinished / 1000
                time_left_txtview.text = getString(R.string.time_left, time_left)
            }

            override fun onFinish() {
                endgame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }


    private fun incrementFunction() {
        if (!gameStarted) {
            startgame()
        }
        score += 1
        val newscore = getString(R.string.your_score, score)
        gamescoretxtview.text = newscore
        before_start_text.visibility = View.GONE
        after_start_text.visibility = View.VISIBLE
        mediaPlayer.start()
        val animation1 = AnimationUtils.loadAnimation(this, R.anim.zoom_out)

        tap_me_btn.startAnimation(animation1)
        after_start_text.startAnimation(animation1)


        val send_score: Int = score
        Log.e(TAG, "incrementFunction: ${send_score}")

        score_display_imageview.setOnClickListener {
            val intent = Intent(this, score_page::class.java)

            intent.putExtra("SCORE_ACHIEVED", send_score)
            startActivity(intent)
            finish()

        }


    }


    private fun startgame() {
        countDownTimer.start()
        gameStarted = true
        after_start_text.visibility = View.VISIBLE
        before_start_text.visibility = View.GONE

    }

    private fun endgame() {
        score_layout.visibility = View.VISIBLE
        after_start_text.visibility = View.GONE
        before_start_text.visibility = View.GONE
        resetgame()
        playing_page.setOnClickListener {
            Toast.makeText(this, "Check Your Score", Toast.LENGTH_LONG).show()
        }
        play_button.setOnClickListener { null }


    }
}