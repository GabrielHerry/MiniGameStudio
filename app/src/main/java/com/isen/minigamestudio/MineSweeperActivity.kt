package com.isen.minigamestudio

import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.GridLayout.spec
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.isen.minigamestudio.Classes.Difficulty
import com.isen.minigamestudio.Classes.GameState
import com.isen.minigamestudio.Classes.MSgame
import kotlinx.android.synthetic.main.activity_minesweeper.*
import java.util.*

class MineSweeperActivity : AppCompatActivity() {
    companion object {
        var enableSound = true
        var savedSoundSetting = enableSound
        val soundVolume = 1.0f
        val enableRespawning = true

        val baseCaseSize = 25
        val numberOfRows = 15
        val numberOfCols = 12

        val alertWidth = 500
        val alertHeight = 350

        var difficulty = Difficulty.MEDIUM // default
        var cooldown = 30000L // 30 sec, default
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minesweeper)

        startGame()
    }

    override fun onPause() {
        savedSoundSetting = enableSound
        enableSound = false
        changeSoundButtonState()
        super.onPause()
    }

    override fun onResume() {
        enableSound = savedSoundSetting
        changeSoundButtonState()
        super.onResume()
    }

    private fun Int.toPX(): Int = (this * baseContext.resources.displayMetrics.density).toInt()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startGame() {
        val game = MSgame(numberOfRows, numberOfCols, difficulty)

        setRadioButtonsState()
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
                width = baseCaseSize.toPX()
                height = baseCaseSize.toPX()
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

                    respawningTimer(game)
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
            changeSoundButtonState()
        }

        restartButton.setOnClickListener {
            chronometer.stop()
            chronometer.base = SystemClock.elapsedRealtime()
            game.gameState = GameState.NOT_STARTED // necessary for the Timer to be killed.
            startGame()
        }

        radio1Button.setOnClickListener {
            onRadioButtonClicked(game, Difficulty.EASY)
        }

        radio2Button.setOnClickListener {
            onRadioButtonClicked(game, Difficulty.MEDIUM)
        }

        radio3Button.setOnClickListener {
            onRadioButtonClicked(game, Difficulty.HARD)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onRadioButtonClicked(game: MSgame, chosenDifficulty: Difficulty) {
        difficulty = chosenDifficulty // changing 'difficulty' during a game as no side effect.
        setRadioButtonsState()

        if (game.gameState != GameState.CONTINUE) {
            chronometer.base = SystemClock.elapsedRealtime()
            startGame()
        }
    }

    private fun setRadioButtonsState() {
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

    private fun respawningTimer(game: MSgame) {
        if (enableRespawning) {
            updateCooldown()
            System.out.println("Timer set with period: $cooldown")

            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    timer.cancel() // cancel this Timer.

                    this@MineSweeperActivity.runOnUiThread(java.lang.Runnable { // for thread access
                        if (game.gameState == GameState.CONTINUE) {
                            mineRespawn(game)
                            respawningTimer(game) // restart the Timer with a new period.
                        }
                        else { System.out.println("Timer killed.") }
                    })
                }
            }, cooldown) // cooldown passed as delay.
        }
    }

    private fun updateScoreTextView(game: MSgame) {
        val string = getString(R.string.textScore) + ": " + game.score
        scoreTextView.text = string
    }

    private fun updateMineLeftTextView(game: MSgame) {
        val string = getString(R.string.textMinesLeft) + ": " + game.minesLeft
        flagsLeftTextView.text = string
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

        dialogBuilder.setMessage(getString(R.string.textScore) + ": " + game.score)
        dialogBuilder.setPositiveButton(getString(R.string.okMessage), DialogInterface.OnClickListener {
                dialog, _ -> dialog.cancel()
        })

        val alertTitle = when (game.gameState) {
            GameState.GAME_WON -> getString(R.string.winMessage)
            GameState.GAME_OVER -> getString(R.string.gameoverMessage)
            else -> getString(R.string.unsupportedMessage)
        }

        val alert = dialogBuilder.create()
        alert.setTitle(alertTitle)
        alert.show()
        alert.window?.setLayout(alertWidth, alertHeight)
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

    private fun changeSoundButtonState() {
        val soundButtonImage = when (enableSound) {
            true -> android.R.drawable.ic_lock_silent_mode_off
            false -> android.R.drawable.ic_lock_silent_mode
        }
        soundButton.setImageResource(soundButtonImage)
    }
}
