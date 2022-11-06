package com.example.geoquiz

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
//import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var nextButton: ImageButton
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var questionTextView: TextView
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americans, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0
    private var correctAnswers = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate(Bundle?) called")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            correctAnswers = if(checkAnswer(true))
                correctAnswers + 1 else correctAnswers + 0
            blockAnswerButons()
        }
        falseButton.setOnClickListener{view: View ->
            correctAnswers = if(checkAnswer(false))
                correctAnswers + 1 else correctAnswers + 0
            blockAnswerButons()
        }
        nextButton.setOnClickListener { view: View ->
            currentIndex++
            if(currentIndex == questionBank.size){
                showResult()
                currentIndex = 0;
            }
            unblockAnswerButtons()
            updateQuestion()
        }

        questionTextView.setOnClickListener { view: View ->
            currentIndex++
            if(currentIndex == questionBank.size){
                showResult()
                currentIndex = 0;
            }
            unblockAnswerButtons()
            updateQuestion()
        }
        updateQuestion()
    } // onCreate

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "onStart() called")
    } // onStart

    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume() called")
    }// onResume

    override fun onPause(){
        super.onPause()
        Log.d(TAG, "onPause() called")
    } // onPause

    override fun onStop(){
        super.onStop()
        Log.d(TAG, "onStop called")
    } // onStop

    override fun onDestroy(){
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    } // onDestroy

    private fun updateQuestion(){
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean): Boolean{
        val isCorrectAnswer: Boolean
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer){
            isCorrectAnswer = true
            R.string.toast_correct
        } else {
            isCorrectAnswer = false
            R.string.toast_incorrect
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show()
        return isCorrectAnswer
    }

    private fun blockAnswerButons(){
        trueButton.isEnabled = false
        falseButton.isEnabled = false
    }

    private fun unblockAnswerButtons(){
        trueButton.isEnabled = true
        falseButton.isEnabled = true
    }

    private fun showResult(){
        Toast.makeText(this,
            "You answered ${correctAnswers * 100 / questionBank.size}%" +
                    " of the questions correctly", Toast.LENGTH_LONG).show()
    }

}