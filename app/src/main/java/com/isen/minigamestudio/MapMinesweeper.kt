package com.isen.minigamestudio

enum class PrintOption {
    REAL, DRAWABLE
}

class MapMinesweeper(val rows: Int, val cols: Int, val minesStartNumber: Int) {

    companion object {
        const val emptyCaseChar = ' '
        const val unknownCaseChar = '.'
        const val unknownMineChar = '#'
        const val markedLocationChar = '?'
        const val markedMineChar = 'X'
    }

    private var revealProbability = 0.5f // default value.
    var minesLeft: Int
    var map: Array<CharArray>

    init {
        minesLeft = minesStartNumber
        map = Array(rows) { CharArray(cols) }
        reset()
    }

    fun changeRevealProbability(probability: Float) {
        if (probability <= 0.0f) revealProbability =
            0.0f else if (probability >= 1.0f) revealProbability =
            1.0f else revealProbability = probability
    }

    fun reset() {
        minesLeft = minesStartNumber
        var minesLeftToPlace = minesStartNumber
        if (minesLeftToPlace >= rows * cols) {
            System.out.printf(
                "\nToo much mines for this MapMinesweeper: %d vs %d x %d\n\n",
                minesLeftToPlace, rows, cols
            )
            throw RuntimeException()
        }
        for (i in 0 until rows) {
            for (j in 0 until cols) map[i][j] =
                unknownCaseChar
        }
        while (minesLeftToPlace > 0) {
            val randomRow = (Math.random() * rows).toInt()
            val randomCol = (Math.random() * cols).toInt()
            if (map[randomRow][randomCol] == unknownCaseChar) {
                map[randomRow][randomCol] =
                    unknownMineChar
                --minesLeftToPlace
            }
        }
    }

    fun print(option: PrintOption) {
        if (option == PrintOption.REAL) {
            System.out.printf(
                "\nRows: %d\nCols: %d\nStarting mines number: %d\nMines left: %d\n\n",
                rows, cols, minesStartNumber, minesLeft
            )
        }
        print("\n   ")
        for (j in 0 until cols) System.out.printf("%d ", j % 10)
        print("\n\n")
        for (i in 0 until rows) {
            System.out.printf("%d  ", i % 10)
            for (j in 0 until cols) {
                if (option != PrintOption.REAL && map[i][j] == unknownMineChar) System.out.printf(
                    "%c ",
                    unknownCaseChar
                ) else if (option != PrintOption.REAL && map[i][j] == markedMineChar) System.out.printf(
                    "%c ",
                    markedLocationChar
                ) else System.out.printf("%c ", map[i][j])
            }
            println()
        }
        println()
    }

    private fun countCase(i0: Int, j0: Int): Int {
        val i_start = Math.max(0, i0 - 1)
        val i_end = Math.min(rows - 1, i0 + 1)
        val j_start = Math.max(0, j0 - 1)
        val j_end = Math.min(cols - 1, j0 + 1)
        var count = 0
        for (i in i_start..i_end) {
            for (j in j_start..j_end) {
                if ((i != i0 || j != j0) && (map[i][j] == unknownMineChar || map[i][j] == markedMineChar)
                ) ++count
            }
        }
        return count
    }

    // Converting integer to char:
    fun intTochar(i: Int): Char {
        return (i + 48).toChar()
    }

    fun revealCase(i: Int, j: Int): Int {
        val countCase = countCase(i, j)
        if (countCase > 0) map[i][j] =
            intTochar(countCase) else map[i][j] = emptyCaseChar
        return countCase
    }

    // This needs to be done once the difficulty has been set:
    fun revealMapMinesweeper() {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (map[i][j] != unknownMineChar && Math.random() < revealProbability)
                    revealCase(i,j)
            }
        }
    }

    fun winStatus(): Boolean {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (map[i][j] == '#')
                    return false
            }
        }
        return true
    }

    fun save(filename: String?) {
        println("Not ready yet!")
        // private int rows;
        // private int cols;
        // private int minesStartNumber;
        // public int marksLeft;
        // public char[][] MapMinesweeper;
        // writeMatrix(this.MapMinesweeper, filename);
    }

    fun load(filename: String?): MapMinesweeper? {
        println("Not ready yet!")
        return null
    }

        //@JvmStatic
        //fun main(args: Array<String>) {
        //    val rows = 10
        //    val cols = 15
        //    val minesStartNumber = 10
        //    val MapMinesweeper = MapMinesweeper(rows, cols, minesStartNumber)
        //    MapMinesweeper.print(PrintOption.REAL)
        //    MapMinesweeper.revealMapMinesweeper()
        //    MapMinesweeper.print(PrintOption.REAL)
        //}
}