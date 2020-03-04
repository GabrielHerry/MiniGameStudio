package com.isen.minigamestudio

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import kotlinx.android.synthetic.main.activity_dungeon_card.*
import kotlinx.android.synthetic.main.littleboxlayout.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import com.isen.minigamestudio.LittleBoxSave as LittleBoxSave




class DungeonCardActivity : AppCompatActivity() {

    private val dungeonAnim = AnimatorSet()

    companion object{
        var pos = "position"
        var pv = "Point de Vie"
        var pa = "Point d'attaque"
        var x = "position x"
        var y = "position y"
        var type = "type"
        var save = "data.json"
        var saveBool = "Etat sauvegarde partie"
        var savedEscapeDungeon = false
        var arrayBoxes: Array<LittleBox> = arrayOf()
        //var arrayBoxesSave: Array<LittleBoxSave?> = arrayOfNulls(9)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fun saveStateGame(){
            val jsonObj = JSONObject()
            jsonObj.put(saveBool, savedEscapeDungeon)
            val file = File(cacheDir.absolutePath + "/stateSavedDungeon.json")
            file.writeText(jsonObj.toString())
        }
        saveStateGame()
        setContentView(R.layout.activity_dungeon_card)
        readStateGame()

        if(!savedEscapeDungeon){ //if did not save
            System.out.println("Je suis dans le if non sauvée")
            arrayBoxes = arrayOf(box1, box2, box3, box4, box5, box6, box7, box8, box9)
            arrayBoxes.forEachIndexed { index, littleBox ->
                littleBox.textBoxNumber.text = (index+1).toString()
                littleBox.boxPosition = index+1

                //Log.d("DungeonCardActivity", "BOUCLE FOR TABLEAU SAVE "+index)
                //Log.d("DungeonCardActivity", littleBox.boxPosition.toString())
                //Log.d("DungeonCardActivity", littleBox.getPa().toString())
            }
            arrayBoxes[0].setPa(10)
            arrayBoxes[4].setPv(10)
            arrayBoxes[4].setIm(R.drawable.gamer)

            //arrayInitialization()
    }
        else{
            System.out.println("If sauvée")
            arrayBoxes = arrayOf(box1, box2, box3, box4, box5, box6, box7, box8, box9)
            savedEscapeDungeon = false
            read()
            //printAfterSave(arrayBoxes)

            //arrayBoxes = arrayOf(box1, box2, box3, box4, box5, box6, box7, box8, box9)

            val listBox = activity_dungeon_card.children.filter { it is LittleBox }
            listBox.forEachIndexed { i, littleBox ->
                littleBox as LittleBox
                System.out.println(" A VOIR SI CA MARCHE")
                System.out.println(littleBox.boxPosition)
                System.out.println(littleBox.getPa())
                System.out.println(littleBox.getPv())
                System.out.println(littleBox.x)
                System.out.println(littleBox.y)
            }
}

        box1.setOnClickListener {
            if (checkAdjacentWithBox5(box1.boxPosition))
                selectTheMove(establishMoveDirection(box1.boxPosition), R.id.box1)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box2.setOnClickListener {
            if (checkAdjacentWithBox5(box2.boxPosition))
                selectTheMove(establishMoveDirection(box2.boxPosition), R.id.box2)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box3.setOnClickListener {
            if (checkAdjacentWithBox5(box3.boxPosition))
                selectTheMove(establishMoveDirection(box3.boxPosition), R.id.box3)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box4.setOnClickListener {
            if (checkAdjacentWithBox5(box4.boxPosition))
                selectTheMove(establishMoveDirection(box4.boxPosition), R.id.box4)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box5.setOnClickListener {
            Toast.makeText(this, "box 5 pos : x =${box5.x}, y =${box5.y} !", Toast.LENGTH_SHORT)
                .show()
        }

        box6.setOnClickListener {
            if (checkAdjacentWithBox5(box6.boxPosition))
                selectTheMove(establishMoveDirection(box6.boxPosition), R.id.box6)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box7.setOnClickListener {
            if (checkAdjacentWithBox5(box7.boxPosition))
                selectTheMove(establishMoveDirection(box7.boxPosition), R.id.box7)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box8.setOnClickListener {
            if (checkAdjacentWithBox5(box8.boxPosition))
                selectTheMove(establishMoveDirection(box8.boxPosition), R.id.box8)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }

        box9.setOnClickListener {
            if (checkAdjacentWithBox5(box9.boxPosition))
                selectTheMove(establishMoveDirection(box9.boxPosition), R.id.box9)
            else
                Toast.makeText(
                    this,
                    "Attention tu ne peux pas aller ici !",
                    Toast.LENGTH_SHORT
                ).show()
        }


        fun save() { //write in the save file
            val jsonArray = JSONArray()
            arrayBoxes.forEach {
                System.out.println("BOUCLE SAVE")
                val jsonObj = JSONObject()
                jsonObj.put(pos,it.boxPosition)
                jsonObj.put(pv, it.getPa())
                jsonObj.put(pa, it.getPv())
                jsonObj.put(x,it.x)
                jsonObj.put(y,it.y)
                jsonArray.put(jsonObj)
                System.out.println(it.x)
                System.out.println(it.y)
            }

            Log.d("DungeonCardActivity", jsonArray.toString())
            val file = File(cacheDir.absolutePath + "/jsonEscapeDungeon.json")
            file.writeText(jsonArray.toString())

        }

        saveQuitButton.setOnClickListener(){
            save()
            savedEscapeDungeon = true
            saveStateGame()
            val intent = Intent( this, HomeActivity::class.java)
            startActivity(intent)

        }
    }

    private fun checkAdjacentWithBox5(clickBoxPos: Int): Boolean {

        when (box5.boxPosition) {
            1 -> if (clickBoxPos == 2 || clickBoxPos == 4) {
                return true
            }
            2 -> if (clickBoxPos == 1 || clickBoxPos == 3 || clickBoxPos == 5) {
                return true
            }
            3 -> if (clickBoxPos == 2 || clickBoxPos == 6) {
                return true
            }
            4 -> if (clickBoxPos == 1 || clickBoxPos == 5 || clickBoxPos == 7) {
                return true
            }
            5 -> if (clickBoxPos == 2 || clickBoxPos == 4 || clickBoxPos == 6 || clickBoxPos == 8) {
                return true
            }
            6 -> if (clickBoxPos == 3 || clickBoxPos == 5 || clickBoxPos == 9) {
                return true
            }
            7 -> if (clickBoxPos == 4 || clickBoxPos == 8) {
                return true
            }
            8 -> if (clickBoxPos == 7 || clickBoxPos == 5 || clickBoxPos == 9) {
                return true
            }
            9 -> if (clickBoxPos == 6 || clickBoxPos == 8) {
                return true
            }
        }
        return false
    }

    private fun establishMoveDirection(clickBoxPos: Int): String {
        when (box5.boxPosition) {
            1 -> when (clickBoxPos) {
                2 -> return "mvRight"
                4 -> return "mvLow"
            }

            2 -> when (clickBoxPos) {
                1 -> return "mvLeft"
                4 -> return "mvRight"
                5 -> return "mvLow"
            }

            3 -> when (clickBoxPos) {
                2 -> return "mvLeft"
                6 -> return "mvLow"
            }

            4 -> when (clickBoxPos) {
                1 -> return "mvTop"
                5 -> return "mvRight"
                7 -> return "mvLow"
            }

            5 -> when (clickBoxPos) {
                2 -> return "mvTop"
                4 -> return "mvLeft"
                6 -> return "mvRight"
                8 -> return "mvLow"
            }

            6 -> when (clickBoxPos) {
                3 -> return "mvTop"
                5 -> return "mvLeft"
                9 -> return "mvLow"
            }

            7 -> when (clickBoxPos) {
                4 -> return "mvTop"
                8 -> return "mvRight"
            }

            8 -> when (clickBoxPos) {
                7 -> return "mvLeft"
                9 -> return "mvRight"
                5 -> return "mvTop"
            }

            9 -> when (clickBoxPos) {
                6 -> return "mvTop"
                8 -> return "mvLeft"
            }
        }
        return "Error"
    }

    private fun selectTheMove(myMove: String, boxToMoveId: Int) {
        when (myMove) {
            "mvTop" -> animMoveToTop(boxToMoveId)
            "mvLow" -> animMoveToLow(boxToMoveId)
            "mvRight" -> animMoveToRight(boxToMoveId)
            "mvLeft" -> animMoveToLeft(boxToMoveId)
        }

    }


    private fun animMoveToTop(boxToMoveId: Int) {

        val littleBoxSize = arrayBoxes[0].height.toFloat()

        ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "Y", (box5.y - littleBoxSize))
            .apply {
                duration = 1000
                start()
            }

        ObjectAnimator.ofFloat(
            findViewById<LittleBox>(boxToMoveId),
            "Y",
            (findViewById<LittleBox>(boxToMoveId).y + littleBoxSize)
        ).apply {
            duration = 1000
            start()
        }

        // change the position number of the two boxs
        val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
        (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
        (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

        box5.boxPosition = newPos
        box5.textBoxNumber.text = newPos.toString()
        //  Toast.makeText(this,"fin du top",Toast.LENGTH_LONG).show()

    }

    private fun animMoveToRight(boxToMoveId: Int) {

        val littleBoxSize = arrayBoxes[0].height.toFloat()

        ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "X", (box5.x + littleBoxSize))
            .apply {
                duration = 1000
                start()
            }

        ObjectAnimator.ofFloat(
            findViewById<LittleBox>(boxToMoveId),
            "X",
            (findViewById<LittleBox>(boxToMoveId).x - littleBoxSize)
        ).apply {
            duration = 1000
            start()
        }


        // change the position number of the two boxs
        val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
        (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
        (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

        box5.boxPosition = newPos
        box5.textBoxNumber.text = newPos.toString()

    }


    private fun animMoveToLeft(boxToMoveId: Int) {

        val littleBoxSize = arrayBoxes[0].height.toFloat()

        ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "X", (box5.x - littleBoxSize))
            .apply {
                duration = 1000
                start()
            }






        ObjectAnimator.ofFloat(
            findViewById<LittleBox>(boxToMoveId),
            "X",
            (findViewById<LittleBox>(boxToMoveId).x + littleBoxSize)
        ).apply {
            duration = 1000
            start()
        }
        // change the position number of the two boxs
        val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
        (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
        (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

        box5.boxPosition = newPos
        box5.textBoxNumber.text = newPos.toString()

    }

    private fun animMoveToLow(boxToMoveId: Int) {


        val littleBoxSize = arrayBoxes[0].height.toFloat()
        ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "Y", box5.y + littleBoxSize)
            .apply {
                duration = 1000
                start()
            }

        ObjectAnimator.ofFloat(
            findViewById<LittleBox>(boxToMoveId),
            "Y",
            (findViewById<LittleBox>(boxToMoveId).y - littleBoxSize)
        ).apply {
            duration = 1000
            start()
        }

        // change the position number of the two boxs
        val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
        (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
        (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

        box5.boxPosition = newPos
        box5.textBoxNumber.text = newPos.toString()
    }
    fun readStateGame(){
        val file = File(cacheDir.absolutePath+"/stateSavedDungeon.json")
        val readString = file.readText()
        val jsonObj = JSONObject(readString)
        Log.d("READBackup", jsonObj.toString())
    }

    fun read() {
        val file = File(cacheDir.absolutePath+"/jsonEscapeDungeon.json")
        val readString = file.readText()
        val jsonArray = JSONArray(readString)

        //arrayBoxes.forEachIndexed { index, littleBox ->
         /*   littleBox.boxPosition = jsonArray.getJSONObject(index).getString(pos).toInt()
            littleBox.setPv(jsonArray.getJSONObject(index).getString(pv).toInt())
            littleBox.setPa(jsonArray.getJSONObject(index).getString(pa).toInt())
            littleBox.x = jsonArray.getJSONObject(index).getString(x).toFloat()
            littleBox.y = jsonArray.getJSONObject(index).getString(y).toFloat()
            System.out.println(littleBox.y)

          */

        Log.d("DungeonCardActivityREAD", jsonArray.toString())
        val listBox = activity_dungeon_card.children.filter { it is LittleBox }
        listBox.forEachIndexed { index, littleBox ->
            littleBox as LittleBox
            if(index == 4){
                littleBox.setIm(R.drawable.gamer)
            }

            littleBox.boxPosition = jsonArray.getJSONObject(index).getString(pos).toInt()
            littleBox.setPv(jsonArray.getJSONObject(index).getString(pv).toInt())
            littleBox.setPa(jsonArray.getJSONObject(index).getString(pa).toInt())
            ObjectAnimator.ofFloat(littleBox, "X", (jsonArray.getJSONObject(index).getString(x).toFloat()))
                .apply {
                    duration = 1
                    addListener((object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            ObjectAnimator.ofFloat(littleBox, "Y", (jsonArray.getJSONObject(index).getString(y).toFloat()))
                                .apply {
                                    duration = 1
                                    start()
                                }
                        }
                    }))
                    start()
                }

      //      littleBox.x = jsonArray.getJSONObject(index).getString(x).toFloat()
        //    littleBox.y = jsonArray.getJSONObject(index).getString(y).toFloat()
            littleBox.textBoxNumber.text = littleBox.boxPosition.toString()
            System.out.println("TEST")
            //System.out.println(littleBox.y)
            System.out.println(jsonArray.getJSONObject(index).getString(x).toFloat())
            System.out.println(jsonArray.getJSONObject(index).getString(y).toFloat())

        }

    }



}
