package com.example.tetrisgame

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_name_input.*

class NameInputDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_input)

        button_save.setOnClickListener {
            val userName = editTextName.text.toString()
            if (userName.isNotEmpty()) {
                val sharedPreference = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("user_name", userName)
                editor.apply()
                finish()
            }
        }

        button_cancel.setOnClickListener {
            finish()
        }
    }
}
