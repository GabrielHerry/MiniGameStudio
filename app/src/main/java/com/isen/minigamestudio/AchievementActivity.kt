package com.isen.minigamestudio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_achievement.*
import org.json.JSONObject

class AchievementActivity : AppCompatActivity() {
    companion object {

    var firstScoreMines = 0
    var secondScoreMines = 0
    var thirdScoreMines = 0
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)
        readScores()
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
    fun readScores() {
        val sharedScoresMines = this.getSharedPreferences("sharedScoresMines", Context.MODE_PRIVATE) ?: return
        val readString = sharedScoresMines.getString("scoresMines", "") ?: ""
        val jsonObj = JSONObject(readString)
        Log.d("MINES", jsonObj.toString())

        firstScoreMines = jsonObj.getString(MineSweeperActivity.firstDiffScore).toInt()
        secondScoreMines = jsonObj.getString(MineSweeperActivity.secondDiffScore).toInt()
        thirdScoreMines = jsonObj.getString(MineSweeperActivity.thirdDiffScore).toInt()
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
