package com.isen.minigamestudio.Classes

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.isen.minigamestudio.R

class MScase {

    var isRevealed: Boolean = false

    
    var isMarked: Boolean = false
    var containsMine: Boolean = false
    var value: Int = 0
    var imageView: ImageView? = null

    fun refreshImageView() {
        imageView?.setImageResource(this.getCaseImage())
    }

    private fun getCaseImage(): Int {
        if (this.isMarked)
            return R.drawable.ic_marked
        else if (!this.isRevealed)
            return R.drawable.ic_unrevealed
        else if (this.containsMine)
            return R.drawable.ic_mine
        else {
            return when (this.value) {
                0 -> R.drawable.ic_case_0
                1 -> R.drawable.ic_case_1
                2 -> R.drawable.ic_case_2
                3 -> R.drawable.ic_case_3
                4 -> R.drawable.ic_case_4
                5 -> R.drawable.ic_case_5
                6 -> R.drawable.ic_case_6
                7 -> R.drawable.ic_case_7
                8 -> R.drawable.ic_case_8
                else -> R.drawable.ic_unrevealed // default
            }
        }
    }

    fun blink() {
        val animation = AlphaAnimation(0.5.toFloat(), 0f)
        animation.duration = 500
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = 2
        animation.repeatMode = Animation.REVERSE
        this.imageView?.startAnimation(animation)
    }
}
