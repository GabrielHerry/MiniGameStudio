package com.isen.minigamestudio.Classes

class MSmap(val rows: Int, val cols: Int, val minesStartNumber: Int) {

    val casesNumber: Int
    val map: ArrayList<MScase> = arrayListOf()

    init {
        casesNumber = rows * cols

        for (i in 0 until casesNumber) {
            map.add(MScase()) // adding to the list end.
        }
    }

    // 2D matrix to flat mapping
    private fun getCaseIndex(row: Int, col: Int): Int {
        return row * cols + col
    }

    // flat matrix to 2D mapping
    private fun getCoordFromIndex(index: Int): Pair<Int, Int> {
        return Pair(index / cols, index % cols)
    }

    // Only done when the user clicks the first time on a case:
    fun initialize(chosenPivot: Int) {
        placeMines(chosenPivot)
        computeValues()
        revealNeighbourhood(chosenPivot)
    }

    private fun neighboursIndex(caseIndex: Int): MutableList<Int> {
        val (row, col) = getCoordFromIndex(caseIndex)

        val rowStart = Math.max(0, row - 1)
        val rowEnd = Math.min(rows - 1, row + 1)
        val colStart = Math.max(0, col - 1)
        val colEnd = Math.min(cols - 1, col + 1)

        val neighboursIndexList = mutableListOf<Int>()

        for (i in rowStart .. rowEnd) {
            for (j in colStart .. colEnd) {
                if (i != row || j != col) {
                    neighboursIndexList.add(getCaseIndex(i, j))
                }
            }
        }

        return neighboursIndexList
    }

    private fun placeMines(chosenPivot: Int) {
        val minesBound = Math.min(casesNumber - 9, (0.9f * casesNumber).toInt())
        var minesLeftToPlace = Math.min(minesStartNumber, minesBound)

        val neighboursIndexList = neighboursIndex(chosenPivot)
        neighboursIndexList.add(chosenPivot)

        while (minesLeftToPlace > 0) {
            val randomIndex = (Math.random() * casesNumber).toInt()

            // To be sure the pivot and its neighbours do not contain any mine:
            val checkLocation = neighboursIndexList.any() { it == randomIndex }

            if (!checkLocation && !map[randomIndex].containsMine) {
                map[randomIndex].containsMine = true
                --minesLeftToPlace
            }
        }
    }

    private fun computeValues() {
        for (caseIndex in 0 until casesNumber) {
            neighboursIndex(caseIndex).forEach() {
                if (map[it].containsMine) {
                    ++map[caseIndex].value
                }
            }
        }
    }

    fun revealNeighbourhood(caseIndex: Int) {
        val case = map[caseIndex]

        if (case.isRevealed)
            return // Case already visited

        case.isRevealed = true
        case.refreshImageView()

        if (case.value == 0) {
            neighboursIndex(caseIndex).forEach() {
                revealNeighbourhood(it)
            }
        }
    }

    fun hideMapPart(): Boolean {
        val candidatesIndex: ArrayList<Int> = arrayListOf()

        for (index in 0 until casesNumber) {
            if (map[index].isRevealed && map[index].value > 0) {
                candidatesIndex.add(index)
            }
        }

        if (candidatesIndex.size <= 9)
            return false // Too few cases for adding a new mine.

        val randomIndex = candidatesIndex[(Math.random() * candidatesIndex.size).toInt()]
        val currentCase = map[randomIndex]

        currentCase.containsMine = true
        currentCase.isRevealed = false
        currentCase.refreshImageView()
        currentCase.blink()

        neighboursIndex(randomIndex).forEach() {
            ++map[it].value
            map[it].refreshImageView()
        }

        // Now: checking if a neighbour has a revealed empty neighbour:

        neighboursIndex(randomIndex).forEach() {
            val hasRevealedEmptyNeighbour = neighboursIndex(it).any() {
                    it2 -> map[it2].isRevealed && map[it2].value == 0
            }

            if (!hasRevealedEmptyNeighbour && !map[it].isMarked) {
                map[it].isRevealed = false
                map[it].refreshImageView()
                map[it].blink()
            }
        }

        return true
    }

    fun revealAllMines() {
        for (case in map) {
            if (case.containsMine) {
                case.isRevealed = true
                case.refreshImageView()
            }
        }
    }

    fun winStatus(): Boolean {
        return map.none() { it.containsMine && !it.isMarked }
    }
}
