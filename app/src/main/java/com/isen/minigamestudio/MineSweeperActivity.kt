package com.isen.minigamestudio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.widget.*
import android.widget.GridLayout.spec
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.isen.minigamestudio.Classes.Difficulty
import com.isen.minigamestudio.Classes.GameState
import com.isen.minigamestudio.Classes.MSgame
import kotlinx.android.synthetic.main.activity_minesweeper.*
import java.util.*

class MineSweeperActivity : AppCompatActivity() {
    companion object {
        var enableSound = true
        val soundVolume = 1.0f
        val allowRespawning = true

        // Real settings
        val caseSize = 50
        val numberOfRows = 15
        val numberOfCols = 12

        // Emulator settings
        //val caseSize = 75
        //val numberOfRows = 12
        //val numberOfCols = 12

        val alertWidth = 600
        val alertHeight = 450

        var difficulty = Difficulty.MEDIUM // default
        var cooldown = 30000L // 30 sec, default
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minesweeper)

        startGame()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startGame() {
        val game = MSgame(numberOfRows, numberOfCols, difficulty)

        setRadioButtonsState(difficulty)

        updateScoreTextView(game)
        updateMineLeftTextView(game)

        val chronometer = findViewById(R.id.chronometer) as Chronometer

        gridLayout.removeAllViews() // emptying in case of a restart.

        gridLayout.rowCount = game.map.rows
        gridLayout.columnCount = game.map.cols

        for (caseIndex in 0 until game.map.casesNumber) {
            val layoutParams: GridLayout.LayoutParams = GridLayout.LayoutParams(
                spec(caseIndex, 0f),
                spec(caseIndex, 0f)).apply {
                width = caseSize
                height = caseSize
            }

            val currentCase = game.map.map[caseIndex]
            val imageView = ImageView(this)
            currentCase.imageView = imageView
            currentCase.refreshImageView()

            imageView.setOnClickListener {
                if (game.gameState == GameState.NOT_STARTED) {
                    game.map.initialize(caseIndex)
                    game.gameState = GameState.CONTINUE

                    chronometer.base = SystemClock.elapsedRealtime()
                    chronometer.start()

                    respawingTimer(game)
                }
                else if (game.gameState == GameState.CONTINUE) {
                    game.revealCase(caseIndex, minutesPassed(chronometer))
                    updateScoreTextView(game)
                    drawEndGameWarning(game, chronometer)
                    playEndGameSound(game)
                }
            }

            imageView.setOnLongClickListener {
                if (game.gameState == GameState.CONTINUE) {
                    game.flagCase(caseIndex)
                    updateMineLeftTextView(game)
                    drawEndGameWarning(game, chronometer)
                    playEndGameSound(game)
                }

                return@setOnLongClickListener true
            }

            gridLayout.addView(imageView, layoutParams)
        }

        soundButton.setOnClickListener {
            enableSound = !enableSound

            val soundButtonImage = when(enableSound) {
                true -> android.R.drawable.ic_lock_silent_mode_off
                false -> android.R.drawable.ic_lock_silent_mode
            }
            soundButton.setImageResource(soundButtonImage)
        }

        restartButton.setOnClickListener {
            chronometer.stop()
            chronometer.base = SystemClock.elapsedRealtime()
            game.gameState = GameState.NOT_STARTED // necessary for the Timer to be killed.
            startGame()
        }
    }

    private fun respawingTimer(game: MSgame) {
        if (allowRespawning) {
            updateCooldown()
            System.out.println("Timer set with period: $cooldown")

            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    timer.cancel() // cancel this Timer.

                    this@MineSweeperActivity.runOnUiThread(java.lang.Runnable { // for thread access

                        if (game.gameState == GameState.CONTINUE) {
                            mineRespawn(game)
                            respawingTimer(game) // restart the Timer with a new period.
                        }

                        else { System.out.println("Timer killed.") }
                    })
                }
            }, cooldown) // cooldown passed as delay.
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio1Button ->
                    if (checked) {
                        difficulty = Difficulty.EASY
                    }
                R.id.radio2Button ->
                    if (checked) {
                        difficulty = Difficulty.MEDIUM
                    }
                R.id.radio3Button ->
                    if (checked) {
                        difficulty = Difficulty.HARD
                    }
            }

            setRadioButtonsState(difficulty)
        }
    }

    private fun setRadioButtonsState(difficulty: Difficulty) {
        when (difficulty) {
            Difficulty.EASY -> {
                radio1Button.setChecked(true)
                radio2Button.setChecked(false)
                radio3Button.setChecked(false)
            }
            Difficulty.MEDIUM -> {
                radio1Button.setChecked(false)
                radio2Button.setChecked(true)
                radio3Button.setChecked(false)
            }
            Difficulty.HARD -> {
                radio1Button.setChecked(false)
                radio2Button.setChecked(false)
                radio3Button.setChecked(true)
            }
        }
    }

    private fun updateScoreTextView(game: MSgame) {
        scoreTextView.setText(getString(R.string.textScore) + " " + game.score)
    }

    private fun updateMineLeftTextView(game: MSgame) {
        flagsLeftTextView.setText(getString(R.string.textMinesLeft) + " " + game.minesLeft)
    }

    private fun drawEndGameWarning(game: MSgame, chronometer: Chronometer) {
        if (game.gameState != GameState.CONTINUE) {
            chronometer.stop()

            if (game.gameState == GameState.GAME_WON) {
                game.scoreGainEnd(secondsPassed(chronometer))
                updateScoreTextView(game)
            }

            gameStateAlert(game)
        }
    }

    private fun playEndGameSound(game: MSgame) {
        if (game.gameState == GameState.GAME_WON) {
            val soundIndex = (Math.random() * 2).toInt()

            when (soundIndex) {
                0 -> playSound(R.raw.nice1)
                else -> playSound(R.raw.nice2)
            }
        }
        else if (game.gameState == GameState.GAME_OVER) {
            playSound(R.raw.unemine)
        }
    }

    private fun gameStateAlert(game: MSgame) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Score: " + game.score)
        dialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener {
                dialog, _ -> dialog.cancel()
        })

        val alertTitle = when (game.gameState) {
            GameState.GAME_WON -> "YOU WON!"
            GameState.GAME_OVER -> "GAME OVER!"
            else -> "unsupported case"
        }

        val alert = dialogBuilder.create()
        alert.setTitle(alertTitle)
        alert.show()
        alert.getWindow()?.setLayout(alertWidth, alertHeight)
    }

    private fun minutesPassed(chronometer: Chronometer): Int {
        return ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 60000).toInt()
    }

    private fun secondsPassed(chronometer: Chronometer): Int {
        return ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000).toInt()
    }

    private fun updateCooldown() {
        cooldown = 30000L + (5000 * Math.random()).toLong() // 30 ~ 35 sec
    }

    private fun mineRespawn(game: MSgame) {
        if (game.gameState == GameState.CONTINUE && game.map.hideMapPart()) {
            ++game.minesLeft
            updateMineLeftTextView(game)
            playSound(R.raw.anotherone)
        }
    }

    private fun playSound(soundID: Int) {
        if (enableSound) {
            val beepSound = MediaPlayer.create(this, soundID)
            beepSound.setVolume(soundVolume, soundVolume)
            beepSound.start()
        }
    }
}
