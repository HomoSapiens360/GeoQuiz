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
import androidx.lifecycle.ViewModelProvider


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var nextButton: ImageButton
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var questionTextView: TextView
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            quizViewModel.correctAnswers = if(checkAnswer(true))
                quizViewModel.correctAnswers + 1 else quizViewModel.correctAnswers + 0
            blockAnswerButons()
        }
        falseButton.setOnClickListener{view: View ->
            quizViewModel.correctAnswers = if(checkAnswer(false))
                quizViewModel.correctAnswers + 1 else quizViewModel.correctAnswers + 0
            blockAnswerButons()
        }
        nextButton.setOnClickListener { view: View ->
            if(quizViewModel.moveToNext())
                showResult()
            unblockAnswerButtons()
            updateQuestion()
        }

        questionTextView.setOnClickListener { view: View ->
            if(quizViewModel.moveToNext())
                showResult()
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
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean): Boolean{
        val isCorrectAnswer: Boolean
        val correctAnswer = quizViewModel.currentQuestionAnswer
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
            "You answered ${quizViewModel.correctAnswers 
                    * 100 / quizViewModel.questionBankSize}%" +
                    " of the questions correctly", Toast.LENGTH_LONG).show()
        quizViewModel.correctAnswers = 0
    }

}