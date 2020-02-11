package com.isen.minigamestudio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
class BoxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var box1 = GameBox(R.drawable.cactus, "Level 3",0,1)

        //infoBox.setText(box1.text)
        //System.out.println("IS IN LAYOUT"+imageBox.isInLayout)
        //System.out.println(box1.text)

        //imageBox.setImageResource(R.drawable.ic_launcher_background);
        setContentView(R.layout.box_cell)

    }
}
