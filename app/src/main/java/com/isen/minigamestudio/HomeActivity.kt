package com.isen.minigamestudio

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setImageLanguageButton()

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

        buttonLanguage.setOnClickListener {
            changeLanguage()
        }

    }

    @Suppress("DEPRECATION")
    private fun setImageLanguageButton() {
        when (this.resources.configuration.locale.displayLanguage)
        {
            "français" -> buttonLanguage.setImageResource(R.drawable.flagen)
            "english" -> buttonLanguage.setImageResource(R.drawable.flagfr)
        }
    }

    @Suppress("DEPRECATION")
    private fun changeLanguage() {
        // get the language of the phone
        val langue = this.resources.configuration.locale.displayLanguage
        Toast.makeText(
            this,
            "langue is : "+ langue,
            Toast.LENGTH_SHORT
        ).show()
        // change to put the other one
        when (this.resources.configuration.locale.displayLanguage)
        {
            "français" ->
            {
                val language  = "english"
                val locale = Locale(language)
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                resources.updateConfiguration(config, baseContext.resources.displayMetrics)
                buttonLanguage.setImageResource(R.drawable.flagfr)
            }

            "english" ->
            {
                val language  = "français"
                val locale = Locale(language)
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                resources.updateConfiguration(config, baseContext.resources.displayMetrics)
                buttonLanguage.setImageResource(R.drawable.flagen)
            }
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
