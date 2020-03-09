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

        var yellowPawn1 = Pawn(0, 0)
        var yellowPawn2 = Pawn(0, 0)
        var yellowPawn3 = Pawn(0, 0)
        var yellowPawn4 = Pawn(0, 0)

        var bluePawn1 = Pawn(28, 1)
        var bluePawn2 = Pawn(28, 1)
        var bluePawn3 = Pawn(28, 1)
        var bluePawn4 = Pawn(28, 1)

        var redPawn1 = Pawn(42, 2)
        var redPawn2 = Pawn(42, 2)
        var redPawn3 = Pawn(42, 2)
        var redPawn4 = Pawn(42, 2)

        var greenPawn1 = Pawn(14, 3)
        var greenPawn2 = Pawn(14, 3)
        var greenPawn3 = Pawn(14, 3)
        var greenPawn4 = Pawn(14, 3)

        var pawnStartedYellow: ArrayList<Pawn> = ArrayList()
        var pawnEndedYellow: ArrayList<Pawn> = ArrayList()
        var pawnFinishedYellow: ArrayList<Pawn> = ArrayList()

        var pawnStartedBlue: ArrayList<Pawn> = ArrayList()
        var pawnEndedBlue: ArrayList<Pawn> = ArrayList()
        var pawnFinishedBlue: ArrayList<Pawn> = ArrayList()

        var pawnStartedRed: ArrayList<Pawn> = ArrayList()
        var pawnEndedRed: ArrayList<Pawn> = ArrayList()
        var pawnFinishedRed: ArrayList<Pawn> = ArrayList()

        var pawnStartedGreen: ArrayList<Pawn> = ArrayList()
        var pawnEndedGreen: ArrayList<Pawn> = ArrayList()
        var pawnFinishedGreen: ArrayList<Pawn> = ArrayList()
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
            buttonAI.setOnClickListener {
               // goComputer(players)
            }
        }
    }

    private fun game(players: Int) {
        val result = rollDice()
        var played = 0
        var homeRun = 0
        if (pawnStartedYellow.isEmpty() && result == 6){
            pawnStart()
            played = 1
           // goComputer(players)
        }
        if (pawnEndedYellow.isNotEmpty() && pawnFinishedYellow.isEmpty()){
            val id = pawnFinishedYellow.size
            homeRun = finishLineYellow(result, pawnEndedYellow[id])
            played = 1
            //goComputer(players)
        }
        if (pawnEndedYellow.isNotEmpty() && pawnFinishedYellow.isNotEmpty()){
            val id = pawnFinishedYellow.size-1
            homeRun = finishLineYellow(result, pawnEndedYellow[id])
            played = 1
            //goComputer(players)
        }
        if (pawnStartedYellow.isNotEmpty() && pawnEndedYellow.isNotEmpty() && pawnStartedYellow.size == pawnEndedYellow.size){
            if (result == 6) {
                yellowHorse1.isEnabled = true
                yellowHorse2.isEnabled = true
                yellowHorse3.isEnabled = true
                yellowHorse4.isEnabled = true
                pawnStart()
                played = 1
               // goComputer(players)
            }
        }
        if (pawnStartedYellow.size > pawnEndedYellow.size && homeRun == 0){
            val pawnOn = pawnStartedYellow[pawnStartedYellow.size-1]
            diceResult(result, pawnOn, 0)
            played = 1
            //goComputer(players)
        }
    }

    private fun goComputer(players: Int) {
        val result = rollDice()
        var homeRun = 0
        if (players >= 2){
            if (pawnStartedBlue.isEmpty() && result == 6){
                movePawnStart(1)
            }
            if (pawnEndedBlue.isNotEmpty() && pawnFinishedBlue.isEmpty()){
                val id = pawnFinishedBlue.size
                homeRun = finishLineBlue(result, pawnEndedBlue[id])
            }
            if (pawnEndedBlue.isNotEmpty() && pawnFinishedBlue.isNotEmpty()){
                val id = pawnFinishedBlue.size-1
                homeRun = finishLineBlue(result, pawnEndedBlue[id])
            }
            if (pawnStartedBlue.isNotEmpty() && pawnEndedBlue.isNotEmpty() && pawnStartedBlue.size == pawnEndedBlue.size){
                if (result == 6 && pawnStartedBlue.isNotEmpty()) {
                    movePawnStart(1)
                }
            }
            if (pawnStartedBlue.size > pawnEndedBlue.size && homeRun == 0){
                val pawnOn = pawnStartedBlue[pawnStartedBlue.size-1]
                diceResult(result, pawnOn, 0)
            }
        }
    }

    private fun finishLineBlue(result: Int, pawn: Pawn): Int {
        if (pawn.position == 28 && result == 1){
            changeImageBoard(pawn.position, green, 0)
            pawn.position = blueFinish[0]
            changeImageFinish(0, blue_horse, 1)
            return 1
        }
        if (pawn.position == blueFinish[0] && result == 2){
            changeImageFinish(0, blue_square, 1)
            pawn.position = blueFinish[1]
            changeImageFinish(1, blue_horse, 1)
            return 1
        }
        if (pawn.position == blueFinish[1] && result == 3){
            changeImageFinish(1, blue_square, 1)
            pawn.position = blueFinish[2]
            changeImageFinish(2, blue_horse, 1)
            return 1
        }
        if (pawn.position == blueFinish[2] && result == 4){
            changeImageFinish(2, blue_square, 1)
            pawn.position = blueFinish[3]
            changeImageFinish(3, blue_horse, 1)
            return 1
        }
        if (pawn.position == blueFinish[3] && result == 5){
            changeImageFinish(3, blue_square, 1)
            pawn.position = blueFinish[4]
            changeImageFinish(4, blue_horse, 1)
            return 1
        }
        if (pawn.position == blueFinish[4] && result == 6){
            changeImageFinish(4, blue_square,  1)
            pawn.position = blueFinish[5]
            changeImageFinish(5, blue_horse, 1)
            return 1
        }
        if (pawn.position == blueFinish[5] && result == 6){
            changeImageFinish(5, blue_square, 1)
            pawnFinishedBlue.add(pawn)
            if (pawnFinishedYellow.size == 4){
                endGame(1)
            }
            return 1
        }
        return 0
    }

    private fun finishLineYellow(result: Int, pawn: Pawn): Int {
        if (pawn.position == 55 && result == 1){
            changeImageBoard(pawn.position, red, 0)
            pawn.position = yellowFinish[0]
            changeImageFinish(0, yellow_horse, 0)
            return 1
        }
        if (pawn.position == yellowFinish[0] && result == 2){
            changeImageFinish(0, yellow_square, 0)
            pawn.position = yellowFinish[1]
            changeImageFinish(1, yellow_horse, 0)
            return 1
        }
        if (pawn.position == yellowFinish[1] && result == 3){
            changeImageFinish(1, yellow_square, 0)
            pawn.position = yellowFinish[2]
            changeImageFinish(2, yellow_horse, 0)
            return 1
        }
        if (pawn.position == yellowFinish[2] && result == 4){
            changeImageFinish(2, yellow_square, 0)
            pawn.position = yellowFinish[3]
            changeImageFinish(3, yellow_horse, 0)
            return 1
        }
        if (pawn.position == yellowFinish[3] && result == 5){
            changeImageFinish(3, yellow_square, 0)
            pawn.position = yellowFinish[4]
            changeImageFinish(4, yellow_horse, 0)
            return 1
        }
        if (pawn.position == yellowFinish[4] && result == 6){
            changeImageFinish(4, yellow_square, 0)
            pawn.position = yellowFinish[5]
            changeImageFinish(5, yellow_horse, 0)
            return 1
        }
        if (pawn.position == yellowFinish[5] && result == 6){
            changeImageFinish(5, yellow_square, 0)
            pawnFinishedYellow.add(pawn)
            if (pawnFinishedYellow.size == 4){
                endGame(0)
            }
            return 1
        }
        return 0
    }

    private fun endGame(player: Int) {

    }

    private fun changeImageBoard(position: Int, idImage: Int, background: Int) {
        val imageBoard = HorseActivity.horseBoard[position]
        val imageToChange: ImageView = findViewById(imageBoard)
        imageToChange.setImageResource(idImage)
        if (background == 1){
            val colorValue = ContextCompat.getColor(this, R.color.colorHorseBlue)
            imageToChange.setBackgroundColor(colorValue)
        }
    }

    private fun changeImageFinish(position: Int, idImage: Int, color: Int) {
        if (color == 0){
            val imageBoard = HorseActivity.yellowFinish[position]
            val imageToChange: ImageView = findViewById(imageBoard)
            imageToChange.setImageResource(idImage)
        }
        if (color == 1){
            val imageBoard = HorseActivity.blueFinish[position]
            val colorValue = ContextCompat.getColor(this, R.color.colorHorseBlue)
            val imageToChange: ImageView = findViewById(imageBoard)
            imageToChange.setImageResource(idImage)
            imageToChange.setBackgroundColor(colorValue)
        }
        if (color == 2){
            val imageBoard = HorseActivity.redFinish[position]
            val imageToChange: ImageView = findViewById(imageBoard)
            imageToChange.setImageResource(idImage)
        }
        if (color == 3){
            val imageBoard = HorseActivity.greenFinish[position]
            val imageToChange: ImageView = findViewById(imageBoard)
            imageToChange.setImageResource(idImage)
        }

    }

    private fun diceResult(number: Int, pawn: Pawn, player: Int) {
        val lastPosition = pawn.position
        pawn.position = pawn.position + number
        if (pawn.position >= 55) {
            pawn.position = 55
            pawnEndedYellow.add(pawn)
        }
        if (lastPosition == 0){
            changeImageBoard(lastPosition, yellow_square, 0)
        }
        if (lastPosition in 1..13){
            changeImageBoard(lastPosition, yellow, 0)
        }
        if (lastPosition == 14){
            changeImageBoard(lastPosition, green_square, 0)
        }
        if (lastPosition in 15..27){
            changeImageBoard(lastPosition, green, 0)
        }
        if (lastPosition == 28){
            changeImageBoard(lastPosition, blue_square, 0)
        }
        if (lastPosition in 29..41){
            changeImageBoard(lastPosition, moon, 0)
        }
        if (lastPosition == 42){
            changeImageBoard(lastPosition, red_square, 0)
        }
        if (lastPosition in 43..55){
            changeImageBoard(lastPosition, red, 0)
        }

        if (player == 0){
            changeImageBoard(pawn.position, yellow_horse, 0)
        }
        if (player == 1){
            changeImageBoard(pawn.position, blue_horse, 1)
        }
        if (player == 2){
            changeImageBoard(pawn.position, red_horse, 0)
        }
        if (player == 3){
            changeImageBoard(pawn.position, green_horse, 0)
        }
    }

    private fun pawnStart() {
        yellowHorse1.setOnClickListener{
            movePawnStart(0)
            yellowHorse1.visibility = View.INVISIBLE
            yellowHorse2.isEnabled = false
            yellowHorse3.isEnabled = false
            yellowHorse4.isEnabled = false
            pawnStartedYellow.add(yellowPawn1)
        }
        yellowHorse2.setOnClickListener {
            movePawnStart(0)
            yellowHorse2.visibility = View.INVISIBLE
            yellowHorse1.isEnabled = false
            yellowHorse3.isEnabled = false
            yellowHorse4.isEnabled = false
            pawnStartedYellow.add(yellowPawn2)
        }
        yellowHorse3.setOnClickListener {
            movePawnStart(0)
            yellowHorse3.visibility = View.INVISIBLE
            yellowHorse2.isEnabled = false
            yellowHorse1.isEnabled = false
            yellowHorse4.isEnabled = false
            pawnStartedYellow.add(yellowPawn3)
        }
        yellowHorse4.setOnClickListener {
            movePawnStart(0)
            yellowHorse4.visibility = View.INVISIBLE
            yellowHorse2.isEnabled = false
            yellowHorse3.isEnabled = false
            yellowHorse1.isEnabled = false
            pawnStartedYellow.add(yellowPawn4)
        }
    }

    private fun movePawnStart(player: Int) {
        if (player == 0) {
            changeImageBoard(0, yellow_horse, 0)
        }
        if (player == 1){
            changeImageBoard(28, blue_horse, 1)
            if (pawnStartedBlue.size == 3){
                blueHorse4.visibility = View.INVISIBLE
                pawnStartedBlue.add(bluePawn4)
            }
            if (pawnStartedBlue.size == 2){
                blueHorse3.visibility = View.INVISIBLE
                pawnStartedBlue.add(bluePawn3)
            }
            if (pawnStartedBlue.size == 1){
                blueHorse2.visibility = View.INVISIBLE
                pawnStartedBlue.add(bluePawn2)
            }
            if (pawnStartedBlue.size == 0){
                blueHorse1.visibility = View.INVISIBLE
                pawnStartedBlue.add(bluePawn1)
            }
        }
        if (player == 2){
            changeImageBoard(42, red_horse, 0)
        }
        if (player == 3){
            changeImageBoard(14, green_horse, 0)
        }
    }

    private fun rollDice(): Int {
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
        return diceNumber
    }
}

