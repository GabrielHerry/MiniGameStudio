package com.isen.minigamestudio

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.isen.minigamestudio.Classes.Pawn
import com.isen.minigamestudio.R.drawable.*
import kotlinx.android.synthetic.main.activity_horse.*


class HorseActivity : AppCompatActivity() {
    companion object {
        val horseBoard: Array<Int> = arrayOf(R.id.startYellow, R.id.yellow1, R.id.yellow2, R.id.yellow3, R.id.yellow4, R.id.yellow5, R.id.yellow6, R.id.yellow7, R.id.yellow8, R.id.yellow9, R.id.yellow10, R.id.yellow11, R.id.yellow12, R.id.yellow13, R.id.startGreen, R.id.green1, R.id.green2, R.id.green3, R.id.green4, R.id.green5, R.id.green6, R.id.green7, R.id.green8, R.id.green9, R.id.green10, R.id.green11, R.id.green12, R.id.green13, R.id.startBlue, R.id.blue1, R.id.blue2, R.id.blue3, R.id.blue4, R.id.blue5, R.id.blue6, R.id.blue7, R.id.blue8, R.id.blue9, R.id.blue10, R.id.blue11, R.id.blue12, R.id.blue13, R.id.startRed, R.id.red1, R.id.red2, R.id.red3, R.id.red4, R.id.red5, R.id.red6, R.id.red7, R.id.red8, R.id.red9, R.id.red10, R.id.red11, R.id.red12, R.id.red13)
        val yellowFinish: Array<Int> = arrayOf(R.id.yellowFinish1, R.id.yellowFinish2, R.id.yellowFinish3, R.id.yellowFinish4, R.id.yellowFinish5, R.id.yellowFinish6)
        val greenFinish: Array<Int> = arrayOf(R.id.greenFinish1, R.id.greenFinish2, R.id.greenFinish3, R.id.greenFinish4, R.id.greenFinish5, R.id.greenFinish6)
        val blueFinish: Array<Int> = arrayOf(R.id.blueFinish1, R.id.blueFinish2, R.id.blueFinish3, R.id.blueFinish4, R.id.blueFinish5, R.id.blueFinish6)
        val redFinish: Array<Int> = arrayOf(R.id.redFinish1, R.id.redFinish2, R.id.redFinish3, R.id.redFinish4, R.id.redFinish5, R.id.redFinish6)

        var yellowPawn1 = Pawn(0, started = false, ended = false, finish = false)
        var yellowPawn2 = Pawn(0, started = false, ended = false, finish = false)
        var yellowPawn3 = Pawn(0, started = false, ended = false, finish = false)
        var yellowPawn4 = Pawn(0, started = false, ended = false, finish = false)

        var bluePawn1 = Pawn(28, started = false, ended = false, finish = false)
        var bluePawn2 = Pawn(28, started = false, ended = false, finish = false)
        var bluePawn3 = Pawn(28, started = false, ended = false, finish = false)
        var bluePawn4 = Pawn(28, started = false, ended = false, finish = false)

        var redPawn1 = Pawn(42, started = false, ended = false, finish = false)
        var redPawn2 = Pawn(42, started = false, ended = false, finish = false)
        var redPawn3 = Pawn(42, started = false, ended = false, finish = false)
        var redPawn4 = Pawn(42, started = false, ended = false, finish = false)

        var greenPawn1 = Pawn(42, started = false, ended = false, finish = false)
        var greenPawn2 = Pawn(42, started = false, ended = false, finish = false)
        var greenPawn3 = Pawn(42, started = false, ended = false, finish = false)
        var greenPawn4 = Pawn(42, started = false, ended = false, finish = false)

        var pawnStarted: ArrayList<Pawn> = ArrayList()
        var pawnEnded: ArrayList<Pawn> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horse)

        val nbAI = intent.getIntExtra("nbAI", 0)
        waitTurn.visibility = View.INVISIBLE
        diceButton.visibility = View.INVISIBLE
        goGame(nbAI)
    }

    private fun goGame(nbAI: Int) {
        if (nbAI == 1) {
            val players = 2
            waitTurn.visibility = View.INVISIBLE
            diceButton.visibility = View.VISIBLE
            diceButton.setOnClickListener {
                game(players)
            }
        }
    }

    private fun game(players: Int) {
        val result = rollDice()
        if (pawnStarted.isEmpty() && result == 6){
            pawnStart()
        }
        if (pawnStarted.isNotEmpty() && pawnEnded.isNotEmpty() && pawnStarted.size == pawnEnded.size){
            if (result == 6 && pawnStarted.isNotEmpty()) {
                yellowHorse1.isEnabled = true
                yellowHorse2.isEnabled = true
                yellowHorse3.isEnabled = true
                yellowHorse4.isEnabled = true
                pawnStart()
            }
        }
        if (pawnStarted.size > pawnEnded.size){
            val pawnOn = pawnStarted[pawnStarted.size-1]
            diceResult(result, pawnOn)
        }
        if (pawnEnded.isNotEmpty()){
            var id =0
            for (i in 0 until pawnEnded.size-1){
                if (!pawnEnded[i].finish){
                    id = i
                }
            }
            finishLine(result, pawnEnded[id])
        }
    }

    private fun finishLine(result: Int, pawn: Pawn) {

    }

    private fun diceResult(number: Int, pawn: Pawn) {
            val lastPosition = pawn.position
            pawn.position = pawn.position + number
            if (pawn.position >= 55) {
                pawn.position = 55
                pawn.ended = true
                pawnEnded.add(pawn)
            }
            if (lastPosition == 0){
                changeImage(lastPosition, yellow_square, 0)
            }
            if (lastPosition in 1..13){
                changeImage(lastPosition, yellow, 0)
            }
            if (lastPosition == 14){
                changeImage(lastPosition, green_square, 0)
            }
            if (lastPosition in 15..27){
                changeImage(lastPosition, green, 0)
            }
            if (lastPosition == 28){
                changeImage(lastPosition, blue_square, 0)
            }
            if (lastPosition in 29..41){
                changeImage(lastPosition, moon, 0)
            }
            if (lastPosition == 42){
                changeImage(lastPosition, red_square, 0)
            }
            if (lastPosition in 43..55){
                changeImage(lastPosition, red, 0)
            }
            changeImage(pawn.position, yellow_horse, 0)
    }

    private fun changeImage(position: Int, idImage: Int, background: Int) {
        val imageBoard = HorseActivity.horseBoard[position]
        val imageToChange: ImageView = findViewById(imageBoard)
        imageToChange.setImageResource(idImage)
        if (background == 1){
            val colorValue = ContextCompat.getColor(this, R.color.colorHorseBlue)
            imageToChange.setBackgroundColor(colorValue)
        }
    }

    private fun computerPlay(diceNumber: Int): Int {
        return diceNumber
    }

    private fun pawnStart() {
        yellowHorse1.setOnClickListener{
            movePawnStart(0)
            yellowHorse1.visibility = View.INVISIBLE
            yellowPawn1.started = true
            yellowHorse2.isEnabled = false
            yellowHorse3.isEnabled = false
            yellowHorse4.isEnabled = false
            pawnStarted.add(yellowPawn1)
        }
        yellowHorse2.setOnClickListener {
            movePawnStart(0)
            yellowHorse2.visibility = View.INVISIBLE
            yellowPawn2.started = true
            yellowHorse1.isEnabled = false
            yellowHorse3.isEnabled = false
            yellowHorse4.isEnabled = false
            pawnStarted.add(yellowPawn2)
        }
        yellowHorse3.setOnClickListener {
            movePawnStart(0)
            yellowHorse3.visibility = View.INVISIBLE
            yellowPawn3.started = true
            yellowHorse2.isEnabled = false
            yellowHorse1.isEnabled = false
            yellowHorse4.isEnabled = false
            pawnStarted.add(yellowPawn3)
        }
        yellowHorse4.setOnClickListener {
            movePawnStart(0)
            yellowHorse4.visibility = View.INVISIBLE
            yellowPawn4.started = true
            yellowHorse2.isEnabled = false
            yellowHorse3.isEnabled = false
            yellowHorse1.isEnabled = false
            pawnStarted.add(yellowPawn4)
        }
    }

    private fun movePawnStart(player: Int) {
        if (player == 0) {
            changeImage(0, yellow_horse, 0)
        }
        if (player == 1){
            changeImage(28, blue_horse, 1)
        }
        if (player == 2){
            changeImage(42, red_horse, 0)
        }
        if (player == 3){
            changeImage(14, green_horse, 0)
        }
    }

    private fun rollDice(): Int {
        var diceNumber = 0
        var result = 0
        diceNumber = (1..6).shuffled().first()
        val drawableResource = when (diceNumber){
            1 -> dice_1
            2 -> dice_2
            3 -> dice_3
            4 -> dice_4
            5 -> dice_5
            6 -> dice_6
            else -> empty
        }
        val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val diceImage : ImageView = findViewById(R.id.diceImage)
        diceImage.setImageResource(drawableResource)
        diceImage.startAnimation(animation)
        result = diceNumber
        return result
    }
}

