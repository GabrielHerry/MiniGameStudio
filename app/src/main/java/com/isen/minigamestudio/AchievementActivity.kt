package com.isen.minigamestudio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_achievement.*

class AchievementActivity : AppCompatActivity() {
    var firstScoreMines = 0
    var secondScoreMines = 0
    var thirdScoreMines = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)
        setScores()
        menuButton.setOnClickListener {
            goMenu()
        }
        // update score for Dungeon
        val sharedPrefDungeon = this.getSharedPreferences("sharedPrefDungeon", Context.MODE_PRIVATE)
        val nameOfBestScoreDifficulty = arrayOf("bestScoreDifficulty1","bestScoreDifficulty2","bestScoreDifficulty3")
        dungeonFirstScore.text = sharedPrefDungeon.getInt(nameOfBestScoreDifficulty[0], 0).toString()
        dungeonSecondScore.text = sharedPrefDungeon.getInt(nameOfBestScoreDifficulty[1], 0).toString()
        dungeonThirdScore.text = sharedPrefDungeon.getInt(nameOfBestScoreDifficulty[2], 0).toString()
        }
    fun setScores() {
        mineFirstScore.text = firstScoreMines.toString()
        mineSecondScore.text = secondScoreMines.toString()
        mineThirdScore.text = thirdScoreMines.toString()
    }
    private fun goMenu() {
        val intent = Intent( this, HomeActivity::class.java)
        startActivity(intent)
    }
}
