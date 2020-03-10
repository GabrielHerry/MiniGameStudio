package com.isen.minigamestudio.Classes

class PlayerPawn(val id: Int, val color: Int, val startPos: Int) {
    val endPos = startPos + 55 // can be greater than 56!
    var pawnNotStarted: ArrayList<Pawn> = arrayListOf(
        Pawn(0, startPos, color),
        Pawn(1, startPos, color),
        Pawn(2, startPos, color),
        Pawn(3, startPos, color))
    var pawnStarted: ArrayList<Pawn> = arrayListOf()
    var pawnEnded: ArrayList<Pawn> = arrayListOf()
    var pawnFinished: ArrayList<Pawn> = arrayListOf()
}
