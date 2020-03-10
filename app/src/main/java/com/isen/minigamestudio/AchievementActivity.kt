package com.isen.minigamestudio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_achievement.*

class AchievementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)

        dungeonResetButton.setOnClickListener {
            resetDungeonScore()
        }

        // update score for Dungeon
        val sharedPrefDungeon = this.getSharedPreferences("sharedPrefDungeon", Context.MODE_PRIVATE)
        val nameOfBestScoreDifficulty = arrayOf("bestScoreDifficulty1","bestScoreDifficulty2","bestScoreDifficulty3")

        dungeonFirstScore.text = sharedPrefDungeon.getInt(nameOfBestScoreDifficulty[0], 0).toString()
        dungeonSecondScore.text = sharedPrefDungeon.getInt(nameOfBestScoreDifficulty[1], 0).toString()
        dungeonThirdScore.text = sharedPrefDungeon.getInt(nameOfBestScoreDifficulty[2], 0).toString()

    }

    private fun goMenu() {
        val intent = Intent( this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun resetDungeonScore () {
        val sharedPrefDungeon = this.getSharedPreferences("sharedPrefDungeon", Context.MODE_PRIVATE)
        val nameOfBestScoreDifficulty = arrayOf("bestScoreDifficulty1","bestScoreDifficulty2","bestScoreDifficulty3")
        for (i in 0 .. 2)
        {
            with(sharedPrefDungeon.edit()) {
                putInt(nameOfBestScoreDifficulty[i], 0)
                commit()
            }
        }

        dungeonFirstScore.text = "0"
        dungeonSecondScore.text = "0"
        dungeonThirdScore.text = "0"
    }
}
