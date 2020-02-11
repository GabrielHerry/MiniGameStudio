package com.isen.minigamestudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_achievement.*

class AchievementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)

        menuButton.setOnClickListener {
            goMenu()
        }
    }

    private fun goMenu() {
        val intent = Intent( this, HomeActivity::class.java)
        startActivity(intent)
    }
}
