package com.example.tetrisgame

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.activity_name_input.button_cancel
import kotlinx.android.synthetic.main.activity_name_input.button_save
import kotlinx.android.synthetic.main.activity_name_input.editTextName
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        supportActionBar?.hide()

        displayUserName()

        button_rotate.setOnClickListener {
            if (Rotate.isRotable()) {
                Rotate.doRotate()
                canvas.invalidate()
            }
        }

        button_left.setOnClickListener {
            if (MoveLeft.isMovableLeft()) {
                MoveLeft.moveLeft()
                canvas.invalidate()
            }
        }
        button_right.setOnClickListener {
            if (MoveRight.isMovableRight()) {
                MoveRight.moveRight()
                canvas.invalidate()
            }
        }
        button_fast_down.setOnClickListener {
            while (!Falling.willLanding(1)) {
                Falling.fallingStep()
            }
        }
        // run game
        game()
    }

    private fun displayUserName() {
        val sharedPreference = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val userName = sharedPreference.getString("user_name", "Player")
        userNameTextView.text = "$userName"
    }

    public fun game() {
        CoroutineScope(Dispatchers.IO).launch {
            // todo eliminate this
            Level.reset()
            Tetromino.newPiece()
            Level.insertNewPosition()
            setBest()

            // gamplay infinite
            while (true) {
                if (Falling.willLanding(1)) {
                    // check is need to clear rows
                    Level.checkRows()
                    // if landed piece cant entered
                    if (Level.isGameOver()) {
                        resetBest()
                        Level.reset()
                    }
                    Tetromino.newPiece()
                    Level.insertNewPosition()
                } else {
                    Falling.fallingStep()
                }
                //game speed in millisecond
                delay(Tetromino.speed)
                canvas.invalidate()
            }
        }
    }

    private fun setBest() {
        val sharedPreference = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE)
        Level.best = sharedPreference.getInt("high_score", 0)
    }
    private fun resetBest(){
        val sharedPreference = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE)
        if (Level.score > sharedPreference.getInt("high_score", 0)) {
            var editor = sharedPreference.edit()
            editor.putInt("high_score", Level.score)
            editor.commit()
            Level.best = sharedPreference.getInt("high_score", 0)
        }
    }
}

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        button_play.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


        button_setting.setOnClickListener {
            startActivity(Intent(this, NameInputDialogActivity::class.java))
        }

        button_exit.setOnClickListener {
            finish()
        }
    }
}

