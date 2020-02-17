package com.isen.minigamestudio

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dungeon_card.*
import kotlinx.android.synthetic.main.littleboxlayout.view.*


class DungeonCardActivity : AppCompatActivity() {

    private val dungeonAnim = AnimatorSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dungeon_card)

        dungeonAnim.pause()

        box5.setIm(R.drawable.gamer)

        box1.textBoxNumber.text = "1"
        box2.textBoxNumber.text = "2"
        box3.textBoxNumber.text = "3"
        box4.textBoxNumber.text = "4"
        box5.textBoxNumber.text = "5"
        box6.textBoxNumber.text = "6"
        box7.textBoxNumber.text = "7"
        box8.textBoxNumber.text = "8"
        box9.textBoxNumber.text = "9"

        box1.boxPosition = 1
        box2.boxPosition = 2
        box3.boxPosition = 3
        box4.boxPosition = 4
        box5.boxPosition = 5
        box6.boxPosition = 6
        box7.boxPosition = 7
        box8.boxPosition = 8
        box9.boxPosition = 9


        box1.setOnClickListener {
            if (checkAdjacentWithBox5(box1.boxPosition))
                selectTheMove(establishMoveDirection(box1.boxPosition), R.id.box1)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box2.setOnClickListener {
            if (checkAdjacentWithBox5(box2.boxPosition))
                selectTheMove(establishMoveDirection(box2.boxPosition), R.id.box2)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box3.setOnClickListener {
            if (checkAdjacentWithBox5(box3.boxPosition))
                selectTheMove(establishMoveDirection(box3.boxPosition), R.id.box3)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box4.setOnClickListener {
            if (checkAdjacentWithBox5(box4.boxPosition))
                selectTheMove(establishMoveDirection(box4.boxPosition), R.id.box4)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box5.setOnClickListener {
            Toast.makeText(this, "box 5 pos : x =${box5.x}, y =${box5.y} !", Toast.LENGTH_SHORT)
                .show()
        }

        box6.setOnClickListener {
            if (checkAdjacentWithBox5(box6.boxPosition))
                selectTheMove(establishMoveDirection(box6.boxPosition), R.id.box6)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box7.setOnClickListener {
            if (checkAdjacentWithBox5(box7.boxPosition))
                selectTheMove(establishMoveDirection(box7.boxPosition), R.id.box7)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box8.setOnClickListener {
            if (checkAdjacentWithBox5(box8.boxPosition))
                selectTheMove(establishMoveDirection(box8.boxPosition), R.id.box8)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box9.setOnClickListener {
            if (checkAdjacentWithBox5(box9.boxPosition))
                selectTheMove(establishMoveDirection(box9.boxPosition), R.id.box9)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }


    }

    private fun checkAdjacentWithBox5(clickBoxPos: Int): Boolean {

        when (box5.boxPosition) {
            1 -> if (clickBoxPos == 2 || clickBoxPos == 4) {
                return true
            }
            2 -> if (clickBoxPos == 1 || clickBoxPos == 3 || clickBoxPos == 5) {
                return true
            }
            3 -> if (clickBoxPos == 2 || clickBoxPos == 6) {
                return true
            }
            4 -> if (clickBoxPos == 1 || clickBoxPos == 5 || clickBoxPos == 7) {
                return true
            }
            5 -> if (clickBoxPos == 2 || clickBoxPos == 4 || clickBoxPos == 6 || clickBoxPos == 8) {
                return true
            }
            6 -> if (clickBoxPos == 3 || clickBoxPos == 5 || clickBoxPos == 9) {
                return true
            }
            7 -> if (clickBoxPos == 4 || clickBoxPos == 8) {
                return true
            }
            8 -> if (clickBoxPos == 7 || clickBoxPos == 5 || clickBoxPos == 9) {
                return true
            }
            9 -> if (clickBoxPos == 6 || clickBoxPos == 8) {
                return true
            }
        }
        return false
    }

    private fun establishMoveDirection(clickBoxPos: Int): String {
        when (box5.boxPosition) {
            1 -> when (clickBoxPos) {
                2 -> return "mvRight"
                4 -> return "mvLow"
            }

            2 -> when (clickBoxPos) {
                1 -> return "mvLeft"
                4 -> return "mvRight"
                5 -> return "mvLow"
            }

            3 -> when (clickBoxPos) {
                2 -> return "mvLeft"
                6 -> return "mvLow"
            }

            4 -> when (clickBoxPos) {
                1 -> return "mvTop"
                5 -> return "mvRight"
                7 -> return "mvLow"
            }

            5 -> when (clickBoxPos) {
                2 -> return "mvTop"
                4 -> return "mvLeft"
                6 -> return "mvRight"
                8 -> return "mvLow"
            }

            6 -> when (clickBoxPos) {
                3 -> return "mvTop"
                5 -> return "mvLeft"
                9 -> return "mvLow"
            }

            7 -> when (clickBoxPos) {
                4 -> return "mvTop"
                8 -> return "mvRight"
            }

            8 -> when (clickBoxPos) {
                7 -> return "mvLeft"
                9 -> return "mvRight"
                5 -> return "mvTop"
            }

            9 -> when (clickBoxPos) {
                6 -> return "mvTop"
                8 -> return "mvLeft"
            }
        }
        return "Error"
    }

    private fun selectTheMove(myMove: String, boxToMoveId: Int) {
        when (myMove) {
            "mvTop" -> animMoveToTop(boxToMoveId)
            "mvLow" -> animMoveToLow(boxToMoveId)
            "mvRight" -> animMoveToRight(boxToMoveId)
            "mvLeft" -> animMoveToLeft(boxToMoveId)
        }

    }


    private fun animMoveToTop(boxToMoveId: Int) {

        val littleBoxSize = box1.height.toFloat()

        ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "Y", (box5.y - littleBoxSize))
            .apply {
                duration = 1000
                start()
            }

        ObjectAnimator.ofFloat(
            findViewById<LittleBox>(boxToMoveId),
            "Y",
            (findViewById<LittleBox>(boxToMoveId).y + littleBoxSize)
        ).apply {
            duration = 1000
            start()
        }

        // change the position number of the two boxs
        val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
        (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
        (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

        box5.boxPosition = newPos
        box5.textBoxNumber.text = newPos.toString()
        //  Toast.makeText(this,"fin du top",Toast.LENGTH_LONG).show()

    }

    private fun animMoveToRight(boxToMoveId: Int) {

        val littleBoxSize = box1.height.toFloat()

        ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "X", (box5.x + littleBoxSize))
            .apply {
                duration = 1000
                start()
            }

        ObjectAnimator.ofFloat(
            findViewById<LittleBox>(boxToMoveId),
            "X",
            (findViewById<LittleBox>(boxToMoveId).x - littleBoxSize)
        ).apply {
            duration = 1000
            start()
        }


        // change the position number of the two boxs
        val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
        (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
        (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

        box5.boxPosition = newPos
        box5.textBoxNumber.text = newPos.toString()

    }


    private fun animMoveToLeft(boxToMoveId: Int) {

        val littleBoxSize = box1.height.toFloat()

        ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "X", (box5.x - littleBoxSize))
            .apply {
                duration = 1000
                start()
            }

        ObjectAnimator.ofFloat(
            findViewById<LittleBox>(boxToMoveId),
            "X",
            (findViewById<LittleBox>(boxToMoveId).x + littleBoxSize)
        ).apply {
            duration = 1000
            start()
        }


        // change the position number of the two boxs
        val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
        (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
        (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

        box5.boxPosition = newPos
        box5.textBoxNumber.text = newPos.toString()

    }

    private fun animMoveToLow(boxToMoveId: Int) {


        val littleBoxSize = box1.height.toFloat()
        ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "Y", box5.y + littleBoxSize)
            .apply {
                duration = 1000
                start()
            }

        ObjectAnimator.ofFloat(
            findViewById<LittleBox>(boxToMoveId),
            "Y",
            (findViewById<LittleBox>(boxToMoveId).y - littleBoxSize)
        ).apply {
            duration = 1000
            start()
        }


        // change the position number of the two boxs
        val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
        (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
        (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

        box5.boxPosition = newPos
        box5.textBoxNumber.text = newPos.toString()

    }

}


//-------------- an other way but without a smooth transition
/*
val constraintSet1 = ConstraintSet()
constraintSet1.clone(activity_dungeon_card)
val constraintSet2 = ConstraintSet()
constraintSet2.clone(activity_dungeon_card)
constraintSet2.setTranslationY(R.id.box5, 900F)


var mytransition = AutoTransition()
mytransition.duration = 1000


TransitionManager.beginDelayedTransition(activity_dungeon_card,mytransition)
constraintSet2.applyTo(activity_dungeon_card)*/


/*
val constraintSet1 = ConstraintSet()
constraintSet1.clone(activity_dungeon_card)
val constraintSet2 = ConstraintSet()
constraintSet2.load(this,activity_dungeon_card)
constraintSet2.setTranslationY(R.id.box5, 900F)


var mytransition = AutoTransition()
mytransition.duration = 1000


TransitionManager.beginDelayedTransition(activity_dungeon_card,mytransition)
constraintSet2.applyTo(activity_dungeon_card)*/

/*
// return to the normal configuration of activity_dungeon_card
val constraintSetArrival2 = ConstraintSet()
constraintSetArrival2.load(this, R.layout.activity_home)

TransitionManager.beginDelayedTransition(mylayout)
constraintSetArrival2.applyTo(mylayout)   */


/*    // put 2 invisible in my main
     box2.visibility = View.INVISIBLE

     // put 2 invisible in my new one (2 <=> 5)
     var inflatedview = getLayoutInflater().inflate(R.layout.dungeon_change5to2, null)
     var inflatebox = inflatedview.findViewById<LittleBox>(R.id.box2)
     inflatebox.visibility = View.INVISIBLE


      // move current view to dungeon_change5to2

      val constraintSetArrival = ConstraintSet()
      constraintSetArrival.load(this, R.layout.dungeon_change5to2)

     var mytransition = AutoTransition()
     mytransition.duration = 0

      TransitionManager.beginDelayedTransition(activity_dungeon_card,mytransition)
      constraintSetArrival.applyTo(activity_dungeon_card)

      // now we are in dungeon_change5to2
      // the inflater will be gone so :
      box2.visibility = View.INVISIBLE

*/


// Log.d("I click","click")


/*

        val boxFiveAnim = ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "translationY", littleBoxSize).apply{
            duration = 1000
        }

        val boxToMoveAnim = ObjectAnimator.ofFloat(findViewById<LittleBox>(boxToMoveId), "translationY", -littleBoxSize).apply{
            duration = 1000
        }

        AnimatorSet().apply {
            play(boxFiveAnim).after(boxToMoveAnim)
            start()
        }



 */