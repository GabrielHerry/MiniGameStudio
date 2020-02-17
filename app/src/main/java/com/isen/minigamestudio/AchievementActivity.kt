package com.isen.minigamestudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AchievementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun goMenu() {
        val intent = Intent( this, HomeActivity::class.java)
        startActivity(intent)
    }
}
