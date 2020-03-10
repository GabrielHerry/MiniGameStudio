package com.isen.minigamestudio

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.isen.minigamestudio.Classes.Pawn
import com.isen.minigamestudio.Classes.PlayerPawn
import com.isen.minigamestudio.R.drawable.*
import kotlinx.android.synthetic.main.activity_horse.*

class HorseActivity : AppCompatActivity() {
    companion object {
        val horseBoard: Array<Int> = arrayOf(R.id.startYellow, R.id.yellow1, R.id.yellow2, R.id.yellow3, R.id.yellow4, R.id.yellow5, R.id.yellow6, R.id.yellow7, R.id.yellow8,
            R.id.yellow9, R.id.yellow10, R.id.yellow11, R.id.yellow12, R.id.yellow13, R.id.startGreen, R.id.green1, R.id.green2, R.id.green3, R.id.green4, R.id.green5, R.id.green6,
            R.id.green7, R.id.green8, R.id.green9, R.id.green10, R.id.green11, R.id.green12, R.id.green13, R.id.startBlue, R.id.blue1, R.id.blue2, R.id.blue3, R.id.blue4, R.id.blue5,
            R.id.blue6, R.id.blue7, R.id.blue8, R.id.blue9, R.id.blue10, R.id.blue11, R.id.blue12, R.id.blue13, R.id.startRed, R.id.red1, R.id.red2, R.id.red3, R.id.red4, R.id.red5,
            R.id.red6, R.id.red7, R.id.red8, R.id.red9, R.id.red10, R.id.red11, R.id.red12, R.id.red13)

        val yellowFinish: Array<Int> = arrayOf(R.id.yellowFinish1, R.id.yellowFinish2, R.id.yellowFinish3, R.id.yellowFinish4, R.id.yellowFinish5, R.id.yellowFinish6)
        val greenFinish: Array<Int> = arrayOf(R.id.greenFinish1, R.id.greenFinish2, R.id.greenFinish3, R.id.greenFinish4, R.id.greenFinish5, R.id.greenFinish6)
        val blueFinish: Array<Int> = arrayOf(R.id.blueFinish1, R.id.blueFinish2, R.id.blueFinish3, R.id.blueFinish4, R.id.blueFinish5, R.id.blueFinish6)
        val redFinish: Array<Int> = arrayOf(R.id.redFinish1, R.id.redFinish2, R.id.redFinish3, R.id.redFinish4, R.id.redFinish5, R.id.redFinish6)

        val player0 = PlayerPawn(0, 0, 0) // yellow
        val player1 = PlayerPawn(1, 1, 28) // blue
        val player2 = PlayerPawn(2, 2, 42) // red
        val player3 = PlayerPawn(3, 3, 14) // green

        val players: Array<PlayerPawn> = arrayOf(player0, player1, player2, player3)
        var playersNumber = 2 // Default
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horse)

        val nbAI = intent.getIntExtra("nbAI", 0)
        playersNumber = 1 + nbAI
        //System.out.println("-----------------------------------------------> playersNumber = " + playersNumber)

        diceImage.setOnClickListener {
            game()
        }
    }

    private fun game() {
        //System.out.println("-----------------------------------------------> New round!")
        for (playerID in 0 until playersNumber) {
            val result = rollDice(playerID)
            //System.out.println("-----------------------------------------------> Player " + playerID + " die result = " + result)

            val theCurrentPlayer = players[playerID]

            if (theCurrentPlayer.pawnEnded.isNotEmpty()) {
                val pawn = theCurrentPlayer.pawnEnded[0]
                finishLinePlayer(theCurrentPlayer, result, pawn)
            }

            else if (theCurrentPlayer.pawnStarted.isEmpty() && result == 6) {
                if (playerID == 0) {
                    yellowHorse1.isEnabled = true
                    yellowHorse2.isEnabled = true
                    yellowHorse3.isEnabled = true
                    yellowHorse4.isEnabled = true
                    playerStartPawn(theCurrentPlayer) // Player plays
                }
                else {
                    movePawnStart(theCurrentPlayer, theCurrentPlayer.pawnNotStarted.size - 1) // Computer plays
                }
            }

            else if (theCurrentPlayer.pawnStarted.isNotEmpty()) {
                movePawn(theCurrentPlayer, result, theCurrentPlayer.pawnStarted[0])
            }
        }
    }

    private fun finishLinePlayer(playerPawn: PlayerPawn, result: Int, pawn: Pawn) {
        when (playerPawn.id) {
            0 -> finishLineColor(yellow_horse, yellow_square, playerPawn, result, pawn)
            1 -> finishLineColor(blue_horse, blue_square, playerPawn, result, pawn)
            2 -> finishLineColor(red_horse, red_square, playerPawn, result, pawn)
            else -> finishLineColor(green_horse, green_square, playerPawn, result, pawn)
        }
    }

    private fun finishLineColor(horse: Int, horse_square: Int, playerPawn: PlayerPawn, result: Int, pawn: Pawn) {
        if (result == 1 && pawn.position == playerPawn.endPos) {
            playerPawn.pawnEnded.add(pawn)
            playerPawn.pawnStarted.remove(pawn)
            pawn.position = 0
            changeImageFinish(0, horse, playerPawn.color)
        }
        else if (result == 6 && pawn.position == 5) {
            changeImageFinish(5, horse_square, playerPawn.color)
            playerPawn.pawnFinished.add(pawn)
            playerPawn.pawnEnded.remove(pawn)
            if (playerPawn.pawnFinished.size == 4) {
                endGame(playerPawn.id)
            }
        }
        else if (result > 1 && pawn.position == result - 2) {
            changeImageFinish(result - 2, horse_square, playerPawn.color)
            pawn.position = result - 1
            changeImageFinish(result - 1, horse, playerPawn.color)
        }
    }

    private fun endGame(playerID: Int) {
        //System.out.println("-----------------------------------------------> Player " + playerID + " WON !!!!!!!!!")
        val player: Int
        if (playerID == 0){
            player = R.string.you
        }
        else{
            player = R.string.computer
        }
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(player))
        builder.setOnDismissListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun changeImageBoard(position: Int, idImage: Int) {
        val imageBoard = HorseActivity.horseBoard[position]
        val imageToChange: ImageView = findViewById(imageBoard)
        imageToChange.setImageResource(idImage)
    }

    // Moves a pawn as long as it isn't on the last line:
    private fun movePawn(playerPawn: PlayerPawn, number: Int, pawn: Pawn) {
        val oldDrawingPosition = pawn.position % 56
        changeImageBoard(oldDrawingPosition, getBoardImage(oldDrawingPosition)) // board

        pawn.position = Math.min(playerPawn.endPos, pawn.position + number)
        //System.out.println("-----------------------------------------------> New position: " + pawn.position)

        if (pawn.position == playerPawn.endPos && number == 1) {
            finishLinePlayer(playerPawn, number, pawn)
            return
        }

        val newDrawingPosition = pawn.position % 56
        changeImageBoard(newDrawingPosition, getHorseColor(playerPawn.id)) // horse
    }

    private fun movePawnStart(playerPawn: PlayerPawn, horseIndex: Int) {
        refreshNotStartedPawns(playerPawn, horseIndex)

        if (playerPawn.pawnNotStarted.isNotEmpty()) {
            val currentPawn = playerPawn.pawnNotStarted[0]
            playerPawn.pawnStarted.add(currentPawn)
            playerPawn.pawnNotStarted.remove(currentPawn)
            changeImageBoard(playerPawn.startPos, getHorseColor(playerPawn.id))
        }
    }

    // Only for a player:
    private fun playerStartPawn(playerPawn: PlayerPawn) {
        yellowHorse1.setOnClickListener{
            movePawnStart(playerPawn, 0)
        }
        yellowHorse2.setOnClickListener {
            movePawnStart(playerPawn, 1)
        }
        yellowHorse3.setOnClickListener {
            movePawnStart(playerPawn, 2)
        }
        yellowHorse4.setOnClickListener {
            movePawnStart(playerPawn, 3)
        }
    }

    private fun getBoardImage(position: Int): Int {
        return when (position) {
            0 -> yellow_square
            in 1..13 -> yellow
            14 -> green_square
            in 15..27 -> green
            28 -> blue_square
            in 29..41 -> blue
            42 -> red_square
            else -> red
        }
    }

    private fun changeImageFinish(position: Int, idImage: Int, color: Int) {
        val imageBoard = when(color) {
            0 -> yellowFinish[position]
            1 -> blueFinish[position]
            2 -> redFinish[position]
            else -> greenFinish[position]
        }

        val imageToChange: ImageView = findViewById(imageBoard)
        imageToChange.setImageResource(idImage)
    }

    private fun getHorseColor(playerID: Int): Int {
        return when (playerID) {
            0 -> yellow_horse
            1 -> blue_horse
            2 -> red_horse
            else -> green_horse
        }
    }

    private fun refreshNotStartedPawns(playerPawn: PlayerPawn, horseIndex: Int) {
        when (playerPawn.id) {
            0 -> {
                yellowHorse1.isEnabled = false
                yellowHorse2.isEnabled = false
                yellowHorse3.isEnabled = false
                yellowHorse4.isEnabled = false

                when (horseIndex) {
                    0 -> yellowHorse1.visibility = View.INVISIBLE
                    1 -> yellowHorse2.visibility = View.INVISIBLE
                    2 -> yellowHorse3.visibility = View.INVISIBLE
                    else -> yellowHorse4.visibility = View.INVISIBLE
                }
            }
            1 -> {
                blueHorse1.isEnabled = false
                blueHorse2.isEnabled = false
                blueHorse3.isEnabled = false
                blueHorse4.isEnabled = false

                when (horseIndex) {
                    0 -> blueHorse1.visibility = View.INVISIBLE
                    1 -> blueHorse2.visibility = View.INVISIBLE
                    2 -> blueHorse3.visibility = View.INVISIBLE
                    else -> blueHorse4.visibility = View.INVISIBLE
                }
            }
            2 -> {
                redHorse1.isEnabled = false
                redHorse2.isEnabled = false
                redHorse3.isEnabled = false
                redHorse4.isEnabled = false

                when (horseIndex) {
                    0 -> redHorse1.visibility = View.INVISIBLE
                    1 -> redHorse2.visibility = View.INVISIBLE
                    2 -> redHorse3.visibility = View.INVISIBLE
                    else -> redHorse4.visibility = View.INVISIBLE
                }
            }
            else -> {
                greenHorse1.isEnabled = false
                greenHorse2.isEnabled = false
                greenHorse3.isEnabled = false
                greenHorse4.isEnabled = false

                when (horseIndex) {
                    0 -> greenHorse1.visibility = View.INVISIBLE
                    1 -> greenHorse2.visibility = View.INVISIBLE
                    2 -> greenHorse3.visibility = View.INVISIBLE
                    else -> greenHorse4.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun rollDice(playerID: Int): Int {
        val diceNumber = (1..6).shuffled().first() // new age RNG

        if (playerID == 0) {
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
            val diceImage: ImageView = findViewById(R.id.diceImage)
            diceImage.setImageResource(drawableResource)
            diceImage.startAnimation(animation)
        }
        return diceNumber
    }
}
