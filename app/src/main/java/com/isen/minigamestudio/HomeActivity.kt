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

        mineSweeperButton.setOnClickListener {
            goMinesweeper()
        }

        horseButton.setOnClickListener {
            goParcheesi()
        }
    }

    private fun goParcheesi() {
        val intent = Intent( this, HorseSettingsActivity::class.java)
        startActivity(intent)
    }

    private fun goMinesweeper() {
        startActivity(Intent(this, MineSweeperActivity::class.java))
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
