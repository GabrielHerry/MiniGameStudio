package com.isen.minigamestudio

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.littleboxlayout.view.*
import java.util.jar.Attributes
import androidx.constraintlayout.widget.ConstraintSet
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log


class LittleBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var boxPosition = 0

    init {
        inflate(context, R.layout.littleboxlayout, this)

        // set as a test
        imageGameBox.setImageResource(R.drawable.bomb)




    }
    // -----------------------------------------------------------------------------
    //function of class littleBox

    //getter
    fun getPv(): Int {
        return vpNumb.text.toString().toInt()
    }

    fun getPa(): Int {
        return paNumb.text.toString().toInt()
    }

 /*   fun getIm(): Int {
        return imageGameBox.getDrawa....
    }*/

    //setter
    fun setPv(pv: Int) {
        vpNumb.text = pv.toString()
    }

    fun setPa(pa: Int) {
        paNumb.text = pa.toString()
    }

    fun setIm(image: Int) {
        imageGameBox.setImageResource(image)
    }

    fun change2box (secondBox: LittleBox) {
        val tempPa = this.getPa()
        val tempPv = this.getPv()
       // var tempIm = this.getIm()

        this.setPa(secondBox.getPa())
        this.setPv(secondBox.getPv())
     //   this.setIm(secondBox.getIm())

        secondBox.setPa(tempPa)
        secondBox.setPv(tempPv)
      //  secondBox.setIm(tempIm)
    }






}
