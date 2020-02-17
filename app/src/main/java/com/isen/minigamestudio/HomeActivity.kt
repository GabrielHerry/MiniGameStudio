package com.isen.minigamestudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        achievementButton.setOnClickListener {
            getAchievements()
        }

        dungeonButton.setOnClickListener {
            goDungeon()
        }
    }

    private fun getAchievements() {
        val intent = Intent( this, AchievementActivity::class.java)
        startActivity(intent)
    }

    private fun goDungeon() {
        val intent = Intent( this, DungeonCardActivity::class.java)
        startActivity(intent)
    }
}
