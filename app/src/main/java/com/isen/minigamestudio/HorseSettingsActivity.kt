package com.isen.minigamestudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_horse_settings.*

class HorseSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horse_settings)

        ai1Button.setOnClickListener {
            nbPlayers(1)
        }
        ai2Button.setOnClickListener {
            nbPlayers(2)
        }
        ai3Button.setOnClickListener {
            nbPlayers(3)
        }
    }

    private fun nbPlayers(nb: Int) {
        val intent = Intent( this, HorseActivity::class.java)
        intent.putExtra("nbAI", nb)
        startActivity(intent)
    }
}
