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
        var firstScoreDungeon: Int = 0
        var secondScoreDungeon: Int = 0
        var thirdScoreDungeon: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)
        readBestScores()
        setScores()
        menuButton.setOnClickListener {
            goMenu()
        }

    }
    fun readBestScores(){

        val sharedPrefBestScoresDungeon = this.getSharedPreferences("sharedPrefBestScoresDungeon",
            Context.MODE_PRIVATE) ?: return
        val readString = sharedPrefBestScoresDungeon.getString("backupScoresJson", "") ?:""
        val jsonObj = JSONObject(readString)
        Log.d("DungeonCardActivityREAD", jsonObj.toString())
        firstScoreDungeon = jsonObj.getString(DungeonCardActivity.firstScoreJson).toInt()
        secondScoreDungeon = jsonObj.getString(DungeonCardActivity.secondScoreJson).toInt()
        thirdScoreDungeon = jsonObj.getString(DungeonCardActivity.thirdScoreJson).toInt()

    }
    fun setScores(){
        dungeonFirstScore.text = firstScoreDungeon.toString()
        dungeonSecondScore.text = secondScoreDungeon.toString()
        dungeonThirdScore.text = thirdScoreDungeon.toString()
    }

    private fun goMenu() {
        val intent = Intent( this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun getFirstScoreDungeon(): Int {
        return(firstScoreDungeon)
    }
    private fun getSecondScoreDungeon():Int{
        return(secondScoreDungeon)

    }
    private fun getThirdScoreDungeon():Int{
        return(thirdScoreDungeon)

    }
    private fun setFirstScoreDungeon(score:Int){
        firstScoreDungeon = score
    }
    private fun setSecondScoreDungeon(score:Int){
        secondScoreDungeon = score

    }
    private fun setThirdScoreDungeon(score:Int){
        thirdScoreDungeon = score

    }

}
