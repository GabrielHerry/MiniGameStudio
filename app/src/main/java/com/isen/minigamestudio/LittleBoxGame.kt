package com.isen.minigamestudio

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.gamebox.view.*
import java.util.jar.Attributes

class LittleBoxGame @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var pdv: Int = 10
    var picture: Int = 0

    init {
        inflate(context, R.layout.gamebox, this)


        vpNumb.text=pdv.toString()
        imageGameBox.setImageResource(R.drawable.gamer)
    }

}