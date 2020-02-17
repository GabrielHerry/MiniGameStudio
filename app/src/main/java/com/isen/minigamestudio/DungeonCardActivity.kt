package com.isen.minigamestudio

import android.animation.Animator
import android.animation.AnimatorListenerAdapter

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.Toast
import androidx.core.view.children

import kotlinx.android.synthetic.main.activity_dungeon_card.*
import kotlinx.android.synthetic.main.littleboxlayout.view.*

import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlin.random.Random.Default.nextDouble


class DungeonCardActivity : AppCompatActivity() {

    private var score = 0
    private var gameLevel = 1

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dungeon_card)

        // ---------------------------------------------
        // Set the random
        val sdf = SimpleDateFormat("ss")
        val currentDate = sdf.format(Date())
        Toast.makeText(this, "The number of second is : $currentDate !", Toast.LENGTH_SHORT).show()
        Random(currentDate.toInt())
        // ---------------------------------------------


        box5.setIm(R.drawable.gamer)

        generateNewObject(R.id.box1,gameLevel)
        generateNewObject(R.id.box2,gameLevel)
        generateNewObject(R.id.box3,gameLevel)
        generateNewObject(R.id.box4,gameLevel)
        generateNewObject(R.id.box6,gameLevel)
        generateNewObject(R.id.box7,gameLevel)
        generateNewObject(R.id.box8,gameLevel)
        generateNewObject(R.id.box9,gameLevel)

        box1.boxPosition = 1
        box2.boxPosition = 2
        box3.boxPosition = 3
        box4.boxPosition = 4
        box5.boxPosition = 5
        box6.boxPosition = 6
        box7.boxPosition = 7
        box8.boxPosition = 8
        box9.boxPosition = 9


        box1.textBoxNumber.text = "1"
        box2.textBoxNumber.text = "2"
        box3.textBoxNumber.text = "3"
        box4.textBoxNumber.text = "4"
        box5.textBoxNumber.text = "5"
        box6.textBoxNumber.text = "6"
        box7.textBoxNumber.text = "7"
        box8.textBoxNumber.text = "8"
        box9.textBoxNumber.text = "9"


        val boxList = activity_dungeon_card.children.filter { it is LittleBox && it.boxPosition != 5 }



        boxList.forEach {
            it as LittleBox
            val currentBox = it
            currentBox.setOnClickListener {
                if (checkAdjacentWithBox5(currentBox.boxPosition)) {
                    if (boxAction(currentBox.id)) {
                        selectTheMove(establishMoveDirection(currentBox.boxPosition), currentBox.id)
                    }
                } else
                    Toast.makeText(
                        this,
                        "Attention tu ne peux pas aller ici !",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }


    private fun checkAdjacentWithBox5 (clickBoxPos:Int) : Boolean {

        when (box5.boxPosition) {
            1 -> if (clickBoxPos == 2 || clickBoxPos == 4) { return true }
            2 -> if (clickBoxPos == 1 || clickBoxPos == 3 || clickBoxPos == 5) { return true }
            3 -> if (clickBoxPos == 2 || clickBoxPos == 6) { return true }
            4 -> if (clickBoxPos == 1 || clickBoxPos == 5 || clickBoxPos == 7) { return true }
            5 -> if (clickBoxPos == 2 || clickBoxPos == 4 || clickBoxPos == 6 || clickBoxPos == 8 ) { return true }
            6 -> if (clickBoxPos == 3 || clickBoxPos == 5 || clickBoxPos == 9) { return true }
            7 -> if (clickBoxPos == 4 || clickBoxPos == 8 ) { return true }
            8 -> if (clickBoxPos == 7 || clickBoxPos == 5 || clickBoxPos == 9) { return true }
            9 -> if (clickBoxPos == 6 || clickBoxPos == 8 ) { return true }
        }
        return false
    }

    private fun establishMoveDirection (clickBoxPos:Int) : String {
        when (box5.boxPosition) {
            1 -> when (clickBoxPos) {
                2 -> return "mvRight"
                4 -> return "mvLow"
            }

            2 -> when (clickBoxPos) {
                1 -> return "mvLeft"
                3 -> return "mvRight"
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

    private fun selectTheMove (myMove: String, boxToMoveId: Int) {
        when (myMove) {
            "mvTop" -> animMoveToTop(boxToMoveId)
            "mvLow" -> animMoveToLow(boxToMoveId)
            "mvRight" -> animMoveToRight(boxToMoveId)
            "mvLeft" -> animMoveToLeft(boxToMoveId)
        }

    }




    private fun animMoveToTop(boxToMoveId: Int) {

        findViewById<LittleBox>(boxToMoveId).visibility = View.INVISIBLE
        generateNewObject (boxToMoveId,gameLevel)
        val littleBoxSize = box1.height.toFloat()

        val boxToMovePosition = findViewById<LittleBox>(boxToMoveId).boxPosition
        if (boxToMovePosition == 1 || boxToMovePosition == 2 || boxToMovePosition == 3)
        {

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(R.id.box5),
                "Y",
                (box5.y - littleBoxSize)
            ).apply {
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMoveId),
                "Y",
                (findViewById<LittleBox>(boxToMoveId).y + 2*littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                    }
                })
                start()
            }


            var boxToMove2 = box1

            when (boxToMovePosition) {
                1 -> {
                    boxToMove2 = getBoxAtPosition(7)
                }
                2 -> {
                    boxToMove2 = getBoxAtPosition(8)
                }
                3 -> {
                    boxToMove2 = getBoxAtPosition(9)
                }
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMove2.id), "Y", (boxToMove2.y - littleBoxSize)
            ).apply {
                duration = 400
                start()
            }

            val newPos = box5.boxPosition

            box5.boxPosition = (findViewById<LittleBox>(boxToMoveId)).boxPosition
            box5.textBoxNumber.text = (findViewById<LittleBox>(boxToMoveId)).boxPosition.toString()

            (findViewById<LittleBox>(boxToMoveId)).boxPosition = boxToMove2.boxPosition
            (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = boxToMove2.boxPosition.toString()

            boxToMove2.boxPosition = newPos
            boxToMove2.textBoxNumber.text = newPos.toString()

        }
        else
        {

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(R.id.box5),
                "Y",
                (box5.y - littleBoxSize)
            ).apply {
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMoveId),
                "Y",
                (findViewById<LittleBox>(boxToMoveId).y + littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                    }
                })
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
    }

    private fun animMoveToRight(boxToMoveId: Int) {

        findViewById<LittleBox>(boxToMoveId).visibility = View.INVISIBLE
        generateNewObject (boxToMoveId,gameLevel)
        val littleBoxSize = box1.height.toFloat()

        val boxToMovePosition = findViewById<LittleBox>(boxToMoveId).boxPosition
        if (boxToMovePosition == 3 || boxToMovePosition == 6 || boxToMovePosition == 9) {

            ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "X", (box5.x + littleBoxSize)).apply{
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(findViewById<LittleBox>(boxToMoveId), "X",(findViewById<LittleBox>(boxToMoveId).x - 2*littleBoxSize)).apply{
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                    }
                })
                start()
            }

            var boxToMove2 = box1

            when (boxToMovePosition) {
                3 -> {boxToMove2 = getBoxAtPosition(1) }
                6 -> {boxToMove2 = getBoxAtPosition(4) }
                9 -> {boxToMove2 = getBoxAtPosition(7) }
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMove2.id), "X", (boxToMove2.x + littleBoxSize)).apply {
                duration = 400
                start()
            }

            val newPos = box5.boxPosition

            box5.boxPosition = (findViewById<LittleBox>(boxToMoveId)).boxPosition
            box5.textBoxNumber.text = (findViewById<LittleBox>(boxToMoveId)).boxPosition.toString()

            (findViewById<LittleBox>(boxToMoveId)).boxPosition = boxToMove2.boxPosition
            (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = boxToMove2.boxPosition.toString()

            boxToMove2.boxPosition = newPos
            boxToMove2.textBoxNumber.text = newPos.toString()

        }
        else {


            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(R.id.box5),
                "X",
                (box5.x + littleBoxSize)
            ).apply {
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMoveId),
                "X",
                (findViewById<LittleBox>(boxToMoveId).x - littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                    }
                })
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


    private fun animMoveToLeft(boxToMoveId: Int) {

        findViewById<LittleBox>(boxToMoveId).visibility = View.INVISIBLE
        generateNewObject (boxToMoveId,gameLevel)

        val littleBoxSize = box1.height.toFloat()

        // if boxToMove Position = 1 or 4 or 7
        // we repop at the end of the line
        val boxToMovePosition = findViewById<LittleBox>(boxToMoveId).boxPosition

        if (boxToMovePosition == 1 || boxToMovePosition == 4 || boxToMovePosition == 7) {

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(R.id.box5), "X", (box5.x - littleBoxSize)).apply {
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(findViewById<LittleBox>(boxToMoveId), "X", (findViewById<LittleBox>(boxToMoveId).x + 2*littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                    }
                })
                start()
            }
            var boxToMove2 = box1

            when (boxToMovePosition) {
                1 -> {boxToMove2 = getBoxAtPosition(3) }
                4 -> {boxToMove2 = getBoxAtPosition(6) }
                7 -> {boxToMove2 = getBoxAtPosition(9) }
            }

                ObjectAnimator.ofFloat(
                    findViewById<LittleBox>(boxToMove2.id), "X", (boxToMove2.x - littleBoxSize)).apply {
                    duration = 400
                    start()
                }

                // change the position number of the three boxs (  newPos <- box5 <- boxToMoveId <- boxToMove2)
                val newPos = box5.boxPosition

                box5.boxPosition = (findViewById<LittleBox>(boxToMoveId)).boxPosition
                box5.textBoxNumber.text = (findViewById<LittleBox>(boxToMoveId)).boxPosition.toString()

                (findViewById<LittleBox>(boxToMoveId)).boxPosition = boxToMove2.boxPosition
                (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = boxToMove2.boxPosition.toString()

                boxToMove2.boxPosition = newPos
                boxToMove2.textBoxNumber.text = newPos.toString()



        }
        else
        {
            ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "X",(box5.x - littleBoxSize)).apply{
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(findViewById<LittleBox>(boxToMoveId), "X",(findViewById<LittleBox>(boxToMoveId).x + littleBoxSize)).apply{
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                    }
                })
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

    private fun animMoveToLow(boxToMoveId: Int) {

        val littleBoxSize = box1.height.toFloat()

        findViewById<LittleBox>(boxToMoveId).visibility = View.INVISIBLE
        generateNewObject (boxToMoveId,gameLevel)


        val boxToMovePosition = findViewById<LittleBox>(boxToMoveId).boxPosition

        if (boxToMovePosition == 7 || boxToMovePosition == 8 || boxToMovePosition == 9) {

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(R.id.box5), "Y", (box5.y + littleBoxSize)).apply {
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(findViewById<LittleBox>(boxToMoveId), "Y", (findViewById<LittleBox>(boxToMoveId).y - 2*littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                    }
                })
                start()
            }
            var boxToMove2 = box1

            when (boxToMovePosition) {
                7 -> {boxToMove2 = getBoxAtPosition(1) }
                8 -> {boxToMove2 = getBoxAtPosition(2) }
                9 -> {boxToMove2 = getBoxAtPosition(3) }
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMove2.id), "Y", (boxToMove2.y + littleBoxSize)).apply {
                duration = 400
                start()
            }

            // change the position number of the three boxs (  newPos <- box5 <- boxToMoveId <- boxToMove2)
            val newPos = box5.boxPosition

            box5.boxPosition = (findViewById<LittleBox>(boxToMoveId)).boxPosition
            box5.textBoxNumber.text = (findViewById<LittleBox>(boxToMoveId)).boxPosition.toString()

            (findViewById<LittleBox>(boxToMoveId)).boxPosition = boxToMove2.boxPosition
            (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = boxToMove2.boxPosition.toString()

            boxToMove2.boxPosition = newPos
            boxToMove2.textBoxNumber.text = newPos.toString()



        }
        else
        {

            ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "Y", box5.y + littleBoxSize)
                .apply {
                    duration = 400
                    start()
                }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMoveId),
                "Y",
                (findViewById<LittleBox>(boxToMoveId).y - littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                    }
                })
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


    private fun generateNewObject (boxToMoveId: Int, gameLevel: Int) {

        // difficulty : at lvl 1 33% potions/swords/monster
        //              at lvl 10 OnlyMonster
        // 0.33
        var gameLevelDouble = gameLevel.toDouble()


        val randomNumber = nextDouble()
        when {
            randomNumber < (0.3/gameLevelDouble) -> // generate potion
            {
                findViewById<LittleBox>(boxToMoveId).boxType ="potion"
                findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.potion)
                findViewById<LittleBox>(boxToMoveId).setPv(1+(nextDouble() * 9).toInt())

                findViewById<LittleBox>(boxToMoveId).setPa(0)
             //   findViewById<LittleBox>(boxToMoveId).paText.visibility = View.INVISIBLE
             //   findViewById<LittleBox>(boxToMoveId).paNumb.visibility = View.INVISIBLE
            }

            randomNumber < ((0.3/gameLevelDouble)*2) -> // generate sword
            {
                findViewById<LittleBox>(boxToMoveId).boxType ="sword"
                findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.sword)
                findViewById<LittleBox>(boxToMoveId).setPa(1+(nextDouble() * 9).toInt())

                findViewById<LittleBox>(boxToMoveId).setPv(0)
                //    findViewById<LittleBox>(boxToMoveId).pvText.visibility = View.INVISIBLE
                //   findViewById<LittleBox>(boxToMoveId).pvNumb.visibility = View.INVISIBLE
            }

            else -> // generate monster
            {

                findViewById<LittleBox>(boxToMoveId).boxType ="monster"
                findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.monster)
                findViewById<LittleBox>(boxToMoveId).setPv(1+(nextDouble() * 9).toInt())

                findViewById<LittleBox>(boxToMoveId).setPa(0)
                //    findViewById<LittleBox>(boxToMoveId).paText.visibility = View.INVISIBLE
                //    findViewById<LittleBox>(boxToMoveId).paNumb.visibility = View.INVISIBLE
            }
        }
    }

    private fun boxAction (boxToMoveId: Int) : Boolean {
        // if box to move is a potion
        if (findViewById<LittleBox>(boxToMoveId).boxType =="sword") {
            box5.setPa(box5.getPa() + findViewById<LittleBox>(boxToMoveId).getPa())
            return true
        }
        else if (findViewById<LittleBox>(boxToMoveId).boxType == "potion") {
            box5.setPv(box5.getPv() + findViewById<LittleBox>(boxToMoveId).getPv())
            return true
        }
        else if (findViewById<LittleBox>(boxToMoveId).boxType == "monster") {
            // if box5.pa >0 first fight with pa
            if (box5.getPa() > 0)
            {
                return if (box5.getPa() >= findViewById<LittleBox>(boxToMoveId).getPv())
                {
                    // kill the mob

                    score += (box5.getPa() - findViewById<LittleBox>(boxToMoveId).getPv())
                    scoreNumber.text = score.toString()
                    updateLevel (score)

                    box5.setPa(box5.getPa() - findViewById<LittleBox>(boxToMoveId).getPv() )
                    true
                }
                else
                {
                    // hit but don't kill
                    score += box5.getPa()
                    scoreNumber.text = score.toString()
                    updateLevel (score)

                    findViewById<LittleBox>(boxToMoveId).setPv(findViewById<LittleBox>(boxToMoveId).getPv() - box5.getPa())
                    box5.setPa(0)
                    false
                }
            }
            else
            {
                // box5.getPa() = 0
                // kill the monster with my pvs
                if (box5.getPv() > findViewById<LittleBox>(boxToMoveId).getPv() )
                {
                    score += findViewById<LittleBox>(boxToMoveId).getPv()
                    scoreNumber.text = score.toString()
                    updateLevel (score)

                    box5.setPv(box5.getPv() - findViewById<LittleBox>(boxToMoveId).getPv())
                    return true
                }
                else
                {
                    // END OF GAME

                    val builder = AlertDialog.Builder(this)

                    builder.setMessage("Fin du jeu !")
                    // Finally, make the alert dialog using builder
                    val dialog: AlertDialog = builder.create()

                    // Display the alert dialog on app interface
                    dialog.show()
                }
            }

        }
        return false
    }

    private fun getBoxAtPosition (pos:Int) : LittleBox {

        val boxList = activity_dungeon_card.children.filter { it is LittleBox && it.boxPosition != 5 }

        boxList.forEach {
            it as LittleBox
            if(it.boxPosition == pos)
            {
                return it
            }
        }
        return box5
    }

    private fun updateLevel (score:Int) {
        gameLevel = (score/50) + 1
        levelNumber.text = gameLevel.toString()
    }

}