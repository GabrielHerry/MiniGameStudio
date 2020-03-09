package com.isen.minigamestudio

import android.animation.Animator
import android.animation.AnimatorListenerAdapter

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.children
import kotlinx.android.synthetic.main.activity_dungeon_card.*
import kotlinx.android.synthetic.main.littleboxlayout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlin.random.Random.Default.nextDouble
import android.util.Log
import com.isen.minigamestudio.Classes.LittleBox
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.pow


class DungeonCardActivity : AppCompatActivity() , SensorEventListener  {


    private var difficulty : Int = 0
    private var score = 0
    private var gameLevel : Int = 1
    private var changeBoxNumber = 0
    private var allowChangeBox = false
    private var aMoveIsHappening = false

    private var probChangeBox : Int = 0
    private var probPotion : Int = 0
    private var probSword : Int = 0

    private var pos = "position"
    private var pv = "Point de Vie"
    private var pa = "Point d'attaque"
    private var x = "position x"
    private var y = "position y"
    private var type = "type"
    private var scoreJson = "score"
    private var difficultyJson = "difficulty"
    private var changeBoxJson = "changeboxs"
    private var gameLevelJson = "gameLevel"

    private var arrayBoxes: Array<LittleBox> = arrayOf()

    private lateinit var sensorManager : SensorManager
    private var accelerometer : Sensor?= null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dungeon_card)



        arrayBoxes = arrayOf(box1, box2, box3, box4, box5, box6, box7, box8, box9)

        val sharedPrefDungeon = this.getSharedPreferences("sharedPrefDungeon",Context.MODE_PRIVATE) ?: return
        val saveState = sharedPrefDungeon.getBoolean("saveState", false)

        if(saveState) { //if we have a save
            readBackupFromJson()

            with(sharedPrefDungeon.edit()) {
                putBoolean("saveState",false)
                commit()
            }

            val probGame = setDifficulty(difficulty)
            probChangeBox = probGame[0]
            probPotion = probGame[1]
            probSword = probGame[2]
            difficultyNumber.text = difficulty.toString()



        }
        else
        {
            // if we don't have a save

            // ---------------------------------------------
            // choose the level :
            activity_dungeon_card.children.forEach {
                it.visibility = View.INVISIBLE
            }

            val builder1 = AlertDialog.Builder(this)
            builder1.setMessage(R.string.alertDialogLevel)
            builder1.setPositiveButton(R.string.alertDialogLevel1) { _, _ ->
                difficulty = 1
            }
            builder1.setNeutralButton(R.string.alertDialogLevel3) { _, _ ->
                difficulty = 3
            }
            builder1.setNegativeButton(R.string.alertDialogLevel2) { _, _ ->
                difficulty = 2
            }
            builder1.setOnDismissListener {

                if (difficulty == 0) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    val probGame = setDifficulty(difficulty)
                    probChangeBox = probGame[0]
                    probPotion = probGame[1]
                    probSword = probGame[2]
                    difficultyNumber.text = difficulty.toString()

                    // generate the values of boxs
                    activity_dungeon_card.children.forEach {
                        it.visibility = View.VISIBLE
                    }
                    activity_dungeon_card.children.filter { it is LittleBox && it.boxPosition != 5 }
                        .forEach {
                            generateNewObject(it.id, gameLevel)
                        }
                }
            }


            val dialog: AlertDialog = builder1.create()
            dialog.show()

            // init boxs --------------------------------------------
            val orderedBoxList = listOf(box1,box2,box3,box4,box5,box6,box7,box8,box9)

            // enter the box position
            for (i in 0..8)
            {
                orderedBoxList[i].boxPosition = i+1
                orderedBoxList[i].textBoxNumber.text = (i+1).toString()
                orderedBoxList[i].textBoxNumber.visibility = View.INVISIBLE
            }

            box5.setIm(R.drawable.gamer)
            box5.boxPosition = 5
            box5.boxType = "gamer"
            box5.textBoxNumber.visibility = View.INVISIBLE
        }

        // end of else

        buttonExit.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


        // ---------------------------------------------
        // Set the random
        val sdf = SimpleDateFormat("ss")
        val currentDate = sdf.format(Date())
        Random(currentDate.toInt())
        // ---------------------------------------------
        // Sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // focus in accelerometer
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // ------------------------------------------------
        // access to rules
        buttonRules.setOnClickListener {
            val builder2 = AlertDialog.Builder(this)
            builder2.setMessage(R.string.alertDialogRules )
            val dialog: AlertDialog = builder2.create()
            dialog.show() }

        // ------------------------------------------------


        val boxList = activity_dungeon_card.children.filter { it is LittleBox && it != box5 }

        boxList.forEach {
            it as LittleBox
            val currentBox = it


            currentBox.setOnClickListener {
                if (changeBoxNumber>0)
                {
                    allowChangeBox = true
                }

                if (!aMoveIsHappening)
                {
                    if (checkAdjacentWithBox5(currentBox.boxPosition)) {
                        if (boxAction(currentBox.id)) {
                            selectTheMove(establishMoveDirection(currentBox.boxPosition), currentBox.id)
                        }
                    } else
                        Toast.makeText(
                            this,
                            "Attention tu ne peux pas aller ici !",
                            Toast.LENGTH_SHORT
                        ).show()

                }
                else
                {
                    Toast.makeText(
                        this,
                        "Relax, ne clique pas trop vite ;)",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

        textChangeBox.setOnClickListener {

            if (allowChangeBox) {
                Toast.makeText(
                    this,
                    "Cartes redistribuées!",
                    Toast.LENGTH_SHORT
                ).show()
                if (changeBoxNumber > 0) {
                    // change the box
                    activity_dungeon_card.children.filter { it is LittleBox && it != box5 }
                        .forEach {
                            it as LittleBox
                            generateNewObject(it.id, gameLevel)
                        }

                    changeBoxNumber--
                    numbChange.text = changeBoxNumber.toString()
                    allowChangeBox = false

                }
            }
        }

        savebutton.setOnClickListener {
            saveBoxDataInJson()
            with(sharedPrefDungeon.edit()) {
                putBoolean("saveState",true)
                commit()
            }

            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }


    override fun onResume() {
        super.onResume()
        accelerometer?.also {it ->
            sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL)
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
}

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if(event!!.values[0] + event.values[1]+event.values[2] >= 20 && allowChangeBox)
        {

            Toast.makeText(
                this,
                "Cartes redistribuées!",
                Toast.LENGTH_SHORT
            ).show()
            if (changeBoxNumber>0)
            {
                // change the box
                activity_dungeon_card.children.filter { it is LittleBox && it != box5 }.forEach {
                    it as LittleBox
                    generateNewObject(it.id,gameLevel)
                }

                changeBoxNumber--
                numbChange.text = changeBoxNumber.toString()
                allowChangeBox = false

            }
        }
    }

    private fun checkAdjacentWithBox5 (clickBoxPos:Int) : Boolean {

        when (box5.boxPosition) {
            1 -> if (clickBoxPos == 2 || clickBoxPos == 4) { return true }
            2 -> if (clickBoxPos == 1 || clickBoxPos == 3 || clickBoxPos == 5) { return true }
            3 -> if (clickBoxPos == 2 || clickBoxPos == 6) { return true }
            4 -> if (clickBoxPos == 1 || clickBoxPos == 5 || clickBoxPos == 7) { return true }
            5 -> if (clickBoxPos == 2 || clickBoxPos == 4 || clickBoxPos == 6 || clickBoxPos == 8 ) { return true }
            6 -> if (clickBoxPos == 3 || clickBoxPos == 5 || clickBoxPos == 9) { return true }
            7 -> if (clickBoxPos == 4 || clickBoxPos == 8 ) { return true }
            8 -> if (clickBoxPos == 7 || clickBoxPos == 5 || clickBoxPos == 9) { return true }
            9 -> if (clickBoxPos == 6 || clickBoxPos == 8 ) { return true }
        }
        return false
    }

    private fun establishMoveDirection (clickBoxPos:Int) : String {
        when (box5.boxPosition) {
            1 -> when (clickBoxPos) {
                2 -> return "mvRight"
                4 -> return "mvLow"
            }

            2 -> when (clickBoxPos) {
                1 -> return "mvLeft"
                3 -> return "mvRight"
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

    private fun selectTheMove (myMove: String, boxToMoveId: Int) {
        when (myMove) {
            "mvTop" -> animMoveToTop(boxToMoveId)
            "mvLow" -> animMoveToLow(boxToMoveId)
            "mvRight" -> animMoveToRight(boxToMoveId)
            "mvLeft" -> animMoveToLeft(boxToMoveId)
        }

    }

    private fun animMoveToTop(boxToMoveId: Int) {

        aMoveIsHappening = true

        findViewById<LittleBox>(boxToMoveId).visibility = View.INVISIBLE
        generateNewObject (boxToMoveId,gameLevel)
        val littleBoxSize = box1.height.toFloat()

        val boxToMovePosition = findViewById<LittleBox>(boxToMoveId).boxPosition
        if (boxToMovePosition == 1 || boxToMovePosition == 2 || boxToMovePosition == 3)
        {

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(R.id.box5),
                "Y",
                (box5.y - littleBoxSize)
            ).apply {
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMoveId),
                "Y",
                (findViewById<LittleBox>(boxToMoveId).y + 2*littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                        aMoveIsHappening = false
                    }
                })
                start()
            }


            var boxToMove2 = box1

            when (boxToMovePosition) {
                1 -> {
                    boxToMove2 = getBoxAtPosition(7)
                }
                2 -> {
                    boxToMove2 = getBoxAtPosition(8)
                }
                3 -> {
                    boxToMove2 = getBoxAtPosition(9)
                }
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMove2.id), "Y", (boxToMove2.y - littleBoxSize)
            ).apply {
                duration = 400
                start()
            }

            val newPos = box5.boxPosition

            box5.boxPosition = (findViewById<LittleBox>(boxToMoveId)).boxPosition
            box5.textBoxNumber.text = (findViewById<LittleBox>(boxToMoveId)).boxPosition.toString()

            (findViewById<LittleBox>(boxToMoveId)).boxPosition = boxToMove2.boxPosition
            (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = boxToMove2.boxPosition.toString()

            boxToMove2.boxPosition = newPos
            boxToMove2.textBoxNumber.text = newPos.toString()

        }
        else
        {

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(R.id.box5),
                "Y",
                (box5.y - littleBoxSize)
            ).apply {
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMoveId),
                "Y",
                (findViewById<LittleBox>(boxToMoveId).y + littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                        aMoveIsHappening = false
                    }
                })
                start()
            }

            // change the position number of the two boxs
            val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
            (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
            (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

            box5.boxPosition = newPos
            box5.textBoxNumber.text = newPos.toString()
        }
    }

    private fun animMoveToRight(boxToMoveId: Int) {

        aMoveIsHappening = true

        findViewById<LittleBox>(boxToMoveId).visibility = View.INVISIBLE
        generateNewObject (boxToMoveId,gameLevel)
        val littleBoxSize = box1.height.toFloat()

        val boxToMovePosition = findViewById<LittleBox>(boxToMoveId).boxPosition
        if (boxToMovePosition == 3 || boxToMovePosition == 6 || boxToMovePosition == 9) {

            ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "X", (box5.x + littleBoxSize)).apply{
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(findViewById<LittleBox>(boxToMoveId), "X",(findViewById<LittleBox>(boxToMoveId).x - 2*littleBoxSize)).apply{
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                        aMoveIsHappening = false
                    }
                })
                start()
            }

            var boxToMove2 = box1

            when (boxToMovePosition) {
                3 -> {boxToMove2 = getBoxAtPosition(1) }
                6 -> {boxToMove2 = getBoxAtPosition(4) }
                9 -> {boxToMove2 = getBoxAtPosition(7) }
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMove2.id), "X", (boxToMove2.x + littleBoxSize)).apply {
                duration = 400
                start()
            }

            val newPos = box5.boxPosition

            box5.boxPosition = (findViewById<LittleBox>(boxToMoveId)).boxPosition
            box5.textBoxNumber.text = (findViewById<LittleBox>(boxToMoveId)).boxPosition.toString()

            (findViewById<LittleBox>(boxToMoveId)).boxPosition = boxToMove2.boxPosition
            (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = boxToMove2.boxPosition.toString()

            boxToMove2.boxPosition = newPos
            boxToMove2.textBoxNumber.text = newPos.toString()

        }
        else {


            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(R.id.box5),
                "X",
                (box5.x + littleBoxSize)
            ).apply {
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMoveId),
                "X",
                (findViewById<LittleBox>(boxToMoveId).x - littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                        aMoveIsHappening = false
                    }
                })
                start()
            }


            // change the position number of the two boxs
            val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
            (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
            (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

            box5.boxPosition = newPos
            box5.textBoxNumber.text = newPos.toString()
        }

    }


    private fun animMoveToLeft(boxToMoveId: Int) {

        aMoveIsHappening = true

        findViewById<LittleBox>(boxToMoveId).visibility = View.INVISIBLE
        generateNewObject (boxToMoveId,gameLevel)

        val littleBoxSize = box1.height.toFloat()

        // if boxToMove Position = 1 or 4 or 7
        // we repop at the end of the line
        val boxToMovePosition = findViewById<LittleBox>(boxToMoveId).boxPosition

        if (boxToMovePosition == 1 || boxToMovePosition == 4 || boxToMovePosition == 7) {

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(R.id.box5), "X", (box5.x - littleBoxSize)).apply {
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(findViewById<LittleBox>(boxToMoveId), "X", (findViewById<LittleBox>(boxToMoveId).x + 2*littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                        aMoveIsHappening = false
                    }
                })
                start()
            }
            var boxToMove2 = box1

            when (boxToMovePosition) {
                1 -> {boxToMove2 = getBoxAtPosition(3) }
                4 -> {boxToMove2 = getBoxAtPosition(6) }
                7 -> {boxToMove2 = getBoxAtPosition(9) }
            }

                ObjectAnimator.ofFloat(
                    findViewById<LittleBox>(boxToMove2.id), "X", (boxToMove2.x - littleBoxSize)).apply {
                    duration = 400
                    start()
                }

                // change the position number of the three boxs (  newPos <- box5 <- boxToMoveId <- boxToMove2)
                val newPos = box5.boxPosition

                box5.boxPosition = (findViewById<LittleBox>(boxToMoveId)).boxPosition
                box5.textBoxNumber.text = (findViewById<LittleBox>(boxToMoveId)).boxPosition.toString()

                (findViewById<LittleBox>(boxToMoveId)).boxPosition = boxToMove2.boxPosition
                (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = boxToMove2.boxPosition.toString()

                boxToMove2.boxPosition = newPos
                boxToMove2.textBoxNumber.text = newPos.toString()



        }
        else
        {
            ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "X",(box5.x - littleBoxSize)).apply{
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(findViewById<LittleBox>(boxToMoveId), "X",(findViewById<LittleBox>(boxToMoveId).x + littleBoxSize)).apply{
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                        aMoveIsHappening = false
                    }
                })
                start()
            }

            // change the position number of the two boxs
            val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
            (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
            (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

            box5.boxPosition = newPos
            box5.textBoxNumber.text = newPos.toString()
        }
    }

    private fun animMoveToLow(boxToMoveId: Int) {

        aMoveIsHappening = true

        val littleBoxSize = box1.height.toFloat()

        findViewById<LittleBox>(boxToMoveId).visibility = View.INVISIBLE
        generateNewObject (boxToMoveId,gameLevel)


        val boxToMovePosition = findViewById<LittleBox>(boxToMoveId).boxPosition

        if (boxToMovePosition == 7 || boxToMovePosition == 8 || boxToMovePosition == 9) {

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(R.id.box5), "Y", (box5.y + littleBoxSize)).apply {
                duration = 400
                start()
            }

            ObjectAnimator.ofFloat(findViewById<LittleBox>(boxToMoveId), "Y", (findViewById<LittleBox>(boxToMoveId).y - 2*littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                        aMoveIsHappening = false
                    }
                })
                start()
            }
            var boxToMove2 = box1

            when (boxToMovePosition) {
                7 -> {boxToMove2 = getBoxAtPosition(1) }
                8 -> {boxToMove2 = getBoxAtPosition(2) }
                9 -> {boxToMove2 = getBoxAtPosition(3) }
            }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMove2.id), "Y", (boxToMove2.y + littleBoxSize)).apply {
                duration = 400
                start()
            }

            // change the position number of the three boxs (  newPos <- box5 <- boxToMoveId <- boxToMove2)
            val newPos = box5.boxPosition

            box5.boxPosition = (findViewById<LittleBox>(boxToMoveId)).boxPosition
            box5.textBoxNumber.text = (findViewById<LittleBox>(boxToMoveId)).boxPosition.toString()

            (findViewById<LittleBox>(boxToMoveId)).boxPosition = boxToMove2.boxPosition
            (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = boxToMove2.boxPosition.toString()

            boxToMove2.boxPosition = newPos
            boxToMove2.textBoxNumber.text = newPos.toString()



        }
        else
        {

            ObjectAnimator.ofFloat(findViewById<LittleBox>(R.id.box5), "Y", box5.y + littleBoxSize)
                .apply {
                    duration = 400
                    start()
                }

            ObjectAnimator.ofFloat(
                findViewById<LittleBox>(boxToMoveId),
                "Y",
                (findViewById<LittleBox>(boxToMoveId).y - littleBoxSize)
            ).apply {
                duration = 400
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        findViewById<LittleBox>(boxToMoveId).visibility = View.VISIBLE
                        aMoveIsHappening = false
                    }
                })
                start()
            }


            // change the position number of the two boxs
            val newPos = (findViewById<LittleBox>(boxToMoveId)).boxPosition
            (findViewById<LittleBox>(boxToMoveId)).boxPosition = box5.boxPosition
            (findViewById<LittleBox>(boxToMoveId)).textBoxNumber.text = box5.boxPosition.toString()

            box5.boxPosition = newPos
            box5.textBoxNumber.text = newPos.toString()
        }
    }

    private fun setDifficulty (difficulty:Int) : (List<Int>)
    {
        var probaChangeBox = 0
        var probaPotion = 0
        var probaSword = 0

        when (difficulty)
        {
            1 ->
            {
                probaChangeBox = 20
                probaPotion = 10
                probaSword = 40
            }
            2 ->
            {
                probaChangeBox = 15
                probaPotion = 7
                probaSword = 35
            }
            3 ->
            {
                probaChangeBox = 10
                probaPotion = 5
                probaSword = 30
            }
        }
        return (listOf(probaChangeBox, probaPotion, probaSword))
    }

    private fun generateNewObject (boxToMoveId: Int, gameLevel: Int) {

        findViewById<LittleBox>(boxToMoveId).pvText.visibility = View.VISIBLE
        findViewById<LittleBox>(boxToMoveId).pvNumb.visibility = View.VISIBLE
        findViewById<LittleBox>(boxToMoveId).paText.visibility = View.VISIBLE
        findViewById<LittleBox>(boxToMoveId).paNumb.visibility = View.VISIBLE

        val randomNumber = nextDouble() * 100
        when {
            randomNumber < (probChangeBox) -> {
                if (changeBoxNumber < 2) {
                    findViewById<LittleBox>(boxToMoveId).boxType = "changeBox"
                    findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.changebox)
                    findViewById<LittleBox>(boxToMoveId).setPv(0)

                    findViewById<LittleBox>(boxToMoveId).setPa(0)
                } else {
                    findViewById<LittleBox>(boxToMoveId).boxType = "victoryPoint"
                    findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.victorypoint)
                    findViewById<LittleBox>(boxToMoveId).setPv(0)
                    findViewById<LittleBox>(boxToMoveId).setPa(0)

                }
                findViewById<LittleBox>(boxToMoveId).pvText.visibility = View.INVISIBLE
                findViewById<LittleBox>(boxToMoveId).pvNumb.visibility = View.INVISIBLE
                findViewById<LittleBox>(boxToMoveId).paText.visibility = View.INVISIBLE
                findViewById<LittleBox>(boxToMoveId).paNumb.visibility = View.INVISIBLE

            }

            randomNumber < (probChangeBox + probPotion) -> // generate potion
            {
                findViewById<LittleBox>(boxToMoveId).boxType = "potion"
                findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.potion)
                // give between 10 and 30% of pdv max
                findViewById<LittleBox>(boxToMoveId).setPv( (nbPaPvMax.text.toString().toInt()/10)
                        + ((nextDouble() * (nbPaPvMax.text.toString().toInt()/5) ).toInt()) )

                findViewById<LittleBox>(boxToMoveId).setPa(0)
                findViewById<LittleBox>(boxToMoveId).paText.visibility = View.INVISIBLE
                findViewById<LittleBox>(boxToMoveId).paNumb.visibility = View.INVISIBLE


            }

            randomNumber < (probChangeBox + probPotion + probSword) -> // generate sword
            {
                // sword pa approximately = ( monster pv / 2 ) * 0.8 ^ (lvl/4)
                when ((gameLevel - 1)/4 % 3) {
                    0 -> {
                        findViewById<LittleBox>(boxToMoveId).boxType = "sword"
                        findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.swordone)
                        findViewById<LittleBox>(boxToMoveId).setPa( ((((gameLevel-1)/4)*10)* (8.0.pow((gameLevel - 1)/4)).toInt() / (10.0.pow((gameLevel - 1)/4)).toInt())
                                + 2 + ((nextDouble() * 9).toInt()))
                    }
                    1 -> {
                        findViewById<LittleBox>(boxToMoveId).boxType = "sword"
                        findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.swordtwo)
                        findViewById<LittleBox>(boxToMoveId).setPa( ((((gameLevel-1)/4)*10)* (8.0.pow((gameLevel - 1)/4)).toInt() / (10.0.pow((gameLevel - 1)/4)).toInt())
                                + 2 + ((nextDouble() * 9).toInt()))
                    }
                    2 -> {
                        findViewById<LittleBox>(boxToMoveId).boxType = "sword"
                        findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.swordthree)
                        findViewById<LittleBox>(boxToMoveId).setPa( ((((gameLevel-1)/4)*10)* (8.0.pow((gameLevel - 1)/4)).toInt() / (10.0.pow((gameLevel - 1)/4)).toInt())
                                + 2 + ((nextDouble() * 9).toInt()))
                    }

                }


                findViewById<LittleBox>(boxToMoveId).setPv(0)
                findViewById<LittleBox>(boxToMoveId).pvText.visibility = View.INVISIBLE
                findViewById<LittleBox>(boxToMoveId).pvNumb.visibility = View.INVISIBLE
            }

            else -> // generate monster
            {
                findViewById<LittleBox>(boxToMoveId).boxType = "monster"
                when ((gameLevel - 1)/2 % 3) {
                    0 -> {
                        findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.monsterone)
                        findViewById<LittleBox>(boxToMoveId).setPv(((gameLevel - 1) / 2) * 10 + 2 + (nextDouble() * 9).toInt())
                    }
                    1 -> {
                        findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.monstertwo)
                        findViewById<LittleBox>(boxToMoveId).setPv(((gameLevel - 1) / 2) * 10 + 2 + (nextDouble() * 9).toInt())
                    }
                    2 -> {
                        findViewById<LittleBox>(boxToMoveId).setIm(R.drawable.monsterthree)
                        findViewById<LittleBox>(boxToMoveId).setPv(((gameLevel - 1) / 2) * 10 + 2 + (nextDouble() * 9).toInt())
                    }



                }
                findViewById<LittleBox>(boxToMoveId).setPa(0)
                findViewById<LittleBox>(boxToMoveId).paText.visibility = View.INVISIBLE
                findViewById<LittleBox>(boxToMoveId).paNumb.visibility = View.INVISIBLE
            }
        }
    }

    private fun boxAction (boxToMoveId: Int) : Boolean {
        // if box to move is a potion
        if (findViewById<LittleBox>(boxToMoveId).boxType =="sword") {
            box5.setPa(box5.getPa() + findViewById<LittleBox>(boxToMoveId).getPa())
            if (box5.getPa() > (15+gameLevel*5)  )
            {
                box5.setPa(15+gameLevel*5)
            }
            return true
        }
        else if (findViewById<LittleBox>(boxToMoveId).boxType == "potion") {
            box5.setPv(box5.getPv() + findViewById<LittleBox>(boxToMoveId).getPv())
            if (box5.getPv() > (15+gameLevel*5))
            {
                box5.setPv(15+gameLevel*5)
            }
            return true
        }
        else if (findViewById<LittleBox>(boxToMoveId).boxType == "monster") {
            // if box5.pa >0 first fight with pa
            if (box5.getPa() > 0)
            {
                return if (box5.getPa() >= findViewById<LittleBox>(boxToMoveId).getPv())
                {
                    // kill the mob
                    score += 10
                    scoreNumber.text = score.toString()
                    updateLevel (score)

                    box5.setPa(box5.getPa() - findViewById<LittleBox>(boxToMoveId).getPv() )
                    true
                }
                else
                {

                    findViewById<LittleBox>(boxToMoveId).setPv(findViewById<LittleBox>(boxToMoveId).getPv() - box5.getPa())
                    box5.setPa(0)
                    false
                }
            }
            else
            {
                // box5.getPa() = 0
                // kill the monster with my pvs
                if (box5.getPv() > findViewById<LittleBox>(boxToMoveId).getPv() )
                {
                    score += 10
                    scoreNumber.text = score.toString()
                    updateLevel (score)

                    box5.setPv(box5.getPv() - findViewById<LittleBox>(boxToMoveId).getPv())
                    return true
                }
                else {
                    // END OF GAME
                    // update best result
                    val sharedPrefDungeon = this.getSharedPreferences("sharedPrefDungeon",Context.MODE_PRIVATE)
                    val nameOfBestScoreDifficulty = arrayOf("bestScoreDifficulty1","bestScoreDifficulty2","bestScoreDifficulty3")

                    val previousScore = sharedPrefDungeon.getInt(nameOfBestScoreDifficulty[difficulty-1], 0)
                    if (previousScore < score)
                    {
                        with(sharedPrefDungeon.edit()) {
                            putInt(nameOfBestScoreDifficulty[difficulty-1], score)
                            commit()
                        }
                    }

                    // collect score

                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Fin du jeu ! Avec un score de : $score ! bravo !")
                    builder.setOnDismissListener {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }

        }
        else if (findViewById<LittleBox>(boxToMoveId).boxType == "changeBox")
        {
            if (changeBoxNumber<2)
            {
                allowChangeBox = true
                changeBoxNumber ++
                numbChange.text = changeBoxNumber.toString()
            }

            return true
        }
        else if (findViewById<LittleBox>(boxToMoveId).boxType == "victoryPoint")
        {
            score += 10
            scoreNumber.text = score.toString()
            updateLevel (score)
            return true
        }

        return false
    }

    private fun getBoxAtPosition (pos:Int) : LittleBox {

        val boxList = activity_dungeon_card.children.filter { it is LittleBox && it.boxPosition != 5 }

        boxList.forEach {
            it as LittleBox
            if(it.boxPosition == pos)
            {
                return it
            }
        }
        return box5
    }

    private fun updateLevel (score:Int) {
        gameLevel = (score/50) + 1
        levelNumber.text = gameLevel.toString()
        nbPaPvMax.text = (15+gameLevel*5).toString()

    }

    private fun readBackupFromJson() {
        val sharedPrefDungeon = this.getSharedPreferences("sharedPrefDungeon",Context.MODE_PRIVATE) ?: return
        val readString = sharedPrefDungeon.getString("backupGameJson", "") ?:""
        val jsonArray = JSONArray(readString)


        Log.d("DungeonCardActivityREAD", jsonArray.toString())
        val listBox = activity_dungeon_card.children.filter { it is LittleBox }
        listBox.forEachIndexed { index, littleBox ->
            littleBox as LittleBox

            littleBox.boxPosition = jsonArray.getJSONObject(index).getString(pos).toInt()
            littleBox.boxType = jsonArray.getJSONObject(index).getString(type).toString()


            score = jsonArray.getJSONObject(index).getString(scoreJson).toInt()
            scoreNumber.text = score.toString()
            difficulty = jsonArray.getJSONObject(index).getString(difficultyJson).toInt()



            changeBoxNumber = jsonArray.getJSONObject(index).getString(changeBoxJson).toInt()
            numbChange.text = changeBoxNumber.toString()

            gameLevel = jsonArray.getJSONObject(index).getString(gameLevelJson).toInt()

            when (littleBox.boxType) {
                "sword" ->
                {
                    when((gameLevel - 1)/4 % 3) {
                        0 -> littleBox.setIm(R.drawable.swordone)
                        1 -> littleBox.setIm(R.drawable.swordtwo)
                        2 -> littleBox.setIm(R.drawable.swordthree)
                    }
                    littleBox.pvText.visibility = View.INVISIBLE
                    littleBox.pvNumb.visibility = View.INVISIBLE

                }


                "potion" ->
                {
                    littleBox.setIm(R.drawable.potion)
                    littleBox.paText.visibility = View.INVISIBLE
                    littleBox.paNumb.visibility = View.INVISIBLE
                }

                "changeBox" ->
                {
                    littleBox.setIm(R.drawable.changebox)
                    littleBox.paText.visibility = View.INVISIBLE
                    littleBox.paNumb.visibility = View.INVISIBLE
                    littleBox.pvText.visibility = View.INVISIBLE
                    littleBox.pvNumb.visibility = View.INVISIBLE
                }
                "victoryPoint" ->
                {
                    littleBox.setIm(R.drawable.victorypoint)
                    littleBox.paText.visibility = View.INVISIBLE
                    littleBox.paNumb.visibility = View.INVISIBLE
                    littleBox.pvText.visibility = View.INVISIBLE
                    littleBox.pvNumb.visibility = View.INVISIBLE
                }

                "monster" ->
                {
                    when((gameLevel - 1)/2 % 3) {
                        0 -> littleBox.setIm(R.drawable.monsterone)
                        1 -> littleBox.setIm(R.drawable.monstertwo)
                        2 -> littleBox.setIm(R.drawable.monsterthree)
                    }
                    littleBox.paText.visibility = View.INVISIBLE
                    littleBox.paNumb.visibility = View.INVISIBLE
                }

                "gamer" -> littleBox.setIm(R.drawable.gamer)
            }

            littleBox.setPv(jsonArray.getJSONObject(index).getString(pv).toInt())
            littleBox.setPa(jsonArray.getJSONObject(index).getString(pa).toInt())
            littleBox.textBoxNumber.text = littleBox.boxPosition.toString()
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
        }
    }



    private fun saveBoxDataInJson() { //write in the save file

        val jsonArray = JSONArray()

        arrayBoxes.forEach {
            val jsonObj = JSONObject()
            jsonObj.put(pos,it.boxPosition)
            jsonObj.put(type,it.boxType)
            jsonObj.put(pv, it.getPv())
            jsonObj.put(pa, it.getPa())
            jsonObj.put(x,it.x)
            jsonObj.put(y,it.y)
            jsonObj.put(scoreJson,score)
            jsonObj.put(gameLevelJson,gameLevel)
            jsonObj.put(difficultyJson,difficulty)
            jsonObj.put(changeBoxJson,changeBoxNumber)
            jsonArray.put(jsonObj)
        }

        val sharedPrefDungeon = this.getSharedPreferences("sharedPrefDungeon",Context.MODE_PRIVATE) ?: return
        with(sharedPrefDungeon.edit()) {
            putString("backupGameJson", jsonArray.toString())
            putBoolean("saveState",true)
            commit()
        }
    }
}


