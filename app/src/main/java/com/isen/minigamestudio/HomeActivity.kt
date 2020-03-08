package com.isen.minigamestudio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        val sharedPref = this.getSharedPreferences("backupjsonstring", Context.MODE_PRIVATE) ?: return
        val readString = sharedPref.getString("backupgame", "") ?:""
        Toast.makeText(
            this,
            "myjson ${readString}",
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent( this, AchievementActivity::class.java)
        startActivity(intent)



       // val intent = Intent( this, AchievementActivity::class.java)
       // startActivity(intent)
    }

    private fun goDungeon() {
        val intent = Intent( this, DungeonCardActivity::class.java)
        startActivity(intent)
    }

}
