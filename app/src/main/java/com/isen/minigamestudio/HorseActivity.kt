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
        val yellowPawns = arrayOf(R.id.yellowHorse1, R.id.yellowHorse2, R.id.yellowHorse3, R.id.yellowHorse4)
        val greenPawns = arrayOf(R.id.greenHorse1, R.id.greenHorse2, R.id.greenHorse3, R.id.greenHorse4)
        val bluePawns = arrayOf(R.id.blueHorse1, R.id.blueHorse2, R.id.blueHorse3, R.id.blueHorse4)
        val redPawns = arrayOf(R.id.redHorse1, R.id.redHorse2, R.id.redHorse3, R.id.redHorse4)
        var yellowPosition = arrayOf(0, 0, 0, 0)
        var greenPosition = arrayOf(14, 14, 14, 14)
        var bluePosition = arrayOf(28, 28, 28, 28)
        var redPosition = arrayOf(42, 42, 42, 42)
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
            val diceResults: Array<Int> = arrayOf(0, 0)
            waitTurn.visibility = View.INVISIBLE
            diceButton.visibility = View.VISIBLE
            diceButton.setOnClickListener {
                startGame(diceResults)
            }
        }
    }

    private fun startGame(diceResults: Array<Int>) {
        while (!Pawn.started){
            diceResults[0] = rollDice(0)
            diceResult(diceResults[0], Pawn.position)
            if (diceResults[0] == 6){
                pawnStart(0)
            }
            diceResults[1] = rollDice(1)
            computerPlay(diceResults[1])
        }
        diceResult(diceResults[0], Pawn.position)
    }

    private fun runGame() {

    }

    private fun maxPosition(pawnsPosition: Array<Int>): Int {
        var max = 0
        for (i in 0 until 4){
            if (max < pawnsPosition[i]){
                max = i
            }
        }
        return max
    }

    private fun diceResult(number: Int, position: Int) {
        //if (number == 6){
            Pawn.position = position + number
            var imageBoard = HorseActivity.horseBoard[position]
            var imageToChange: ImageView = findViewById(imageBoard)
            imageToChange.setImageResource(yellow)
            imageBoard = HorseActivity.horseBoard[Pawn.position]
            imageToChange = findViewById(imageBoard)
            imageToChange.setImageResource(yellow_horse)

       // }
    }

    private fun computerPlay(diceNumber: Int): Int {
        return diceNumber
    }

    private fun pawnStart(player: Int) {
        if (player == 0){
            yellowHorse1.setOnClickListener {
                Pawn.idPawn = "yellow1"
                Pawn.started = true
                movePawnStart(0)
                yellowHorse1.visibility = View.INVISIBLE
            }
            yellowHorse2.setOnClickListener {
                Pawn.idPawn = "yellow2"
                Pawn.started = true
                movePawnStart(0)
                yellowHorse2.visibility = View.INVISIBLE
            }
            yellowHorse3.setOnClickListener {
                Pawn.idPawn = "yellow3"
                Pawn.started = true
                movePawnStart(0)
                yellowHorse3.visibility = View.INVISIBLE
            }
            yellowHorse4.setOnClickListener {
                Pawn.idPawn = "yellow4"
                Pawn.started = true
                movePawnStart(0)
                yellowHorse4.visibility = View.INVISIBLE
            }
        }
    }

    private fun movePawnStart(player: Int) {
        if (player == 0) {
            val imageBoard = HorseActivity.horseBoard[0]
            val imageToChange: ImageView = findViewById(imageBoard)
            imageToChange.setImageResource(yellow_horse)
        }
        if (player == 1){
            val imageBoard = HorseActivity.horseBoard[28]
            val colorValue = ContextCompat.getColor(this, R.color.colorHorseBlue)
            val imageToChange: ImageView = findViewById(imageBoard)
            imageToChange.setImageResource(blue_horse)
            imageToChange.setBackgroundColor(colorValue)
        }
        if (player == 2){
            val imageBoard = HorseActivity.horseBoard[42]
            val imageToChange: ImageView = findViewById(imageBoard)
            imageToChange.setImageResource(red_horse)
        }
        if (player == 3){
            val imageBoard = HorseActivity.horseBoard[14]
            val imageToChange: ImageView = findViewById(imageBoard)
            imageToChange.setImageResource(green_horse)
        }
    }

    private fun rollDice(player: Int): Int {
        var result = 0
        if (player == 0) {
            val diceNumber = (1..6).shuffled().first()
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
        else {
            diceButton.visibility = View.INVISIBLE
            waitTurn.visibility = View.VISIBLE
            val diceNumber = (1..6).random()
            result = diceNumber
            return result
        }
    }
}

