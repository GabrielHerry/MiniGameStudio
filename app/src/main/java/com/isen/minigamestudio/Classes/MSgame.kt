package com.isen.minigamestudio.Classes

import android.widget.Chronometer

enum class GameState {
    NOT_STARTED,
    CONTINUE,
    GAME_OVER,
    GAME_WON
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}

class MSgame(private val rows: Int, private val cols: Int, private var difficulty: Difficulty) {

    var map = MSmap(rows, cols, chooseMinesNumber(difficulty))
    var score: Int = 0
    var minesLeft: Int = map.minesStartNumber
    var gameState = GameState.NOT_STARTED

    private fun chooseMinesNumber(difficulty: Difficulty): Int {
        return when (difficulty) {
            Difficulty.EASY -> 12
            Difficulty.MEDIUM -> 24
            Difficulty.HARD -> 36
        }
    }

    fun revealCase(index: Int, minutesPassed: Int) {
        val currentCase = map.map[index]

        if (!currentCase.isMarked && currentCase.containsMine) {
            map.revealAllMines()
            gameState = GameState.GAME_OVER
        }

        else if (!currentCase.isMarked && !currentCase.containsMine && !currentCase.isRevealed) {
            map.revealNeighbourhood(index) // refreshes some imageViews.
            scoreGainCase(currentCase.value, minutesPassed)
        }
    }

    fun flagCase(index: Int) {
        val currentCase = map.map[index]

        if (currentCase.isMarked) {
            currentCase.isMarked = false
            currentCase.refreshImageView()
            ++minesLeft
        }

        else if (!currentCase.isRevealed && minesLeft > 0) {
            currentCase.isMarked = true
            currentCase.refreshImageView()
            --minesLeft

            if (map.winStatus()) {
                gameState = GameState.GAME_WON
            }
        }
    }

    private fun scoreGainCase(caseValue: Int, minutesPassed: Int) {
        score += caseValue * Math.max(1, 3 - minutesPassed)
    }

    fun scoreGainEnd(secondsPassed: Int) {
        score += Math.max(1, 180 - secondsPassed)
    }
}
