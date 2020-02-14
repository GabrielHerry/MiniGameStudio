package com.isen.minigamestudio

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_dungeon_card.*
import kotlinx.android.synthetic.main.gamebox.view.*
import java.security.AccessController.getContext

class DungeonCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dungeon_card)

      box5.changepdv(5)
      box5.changeIm(R.drawable.bomb)


        box6.vpNumb.text = "6"





    }
}
