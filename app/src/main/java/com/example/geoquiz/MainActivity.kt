package com.example.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
private const val KEY_INDEX = "Index"
private const val CORRECT_ANSWERS = "Correct answers"
private const val REQUEST_CODE_CHEAT = 5
private var isCheat = false

class MainActivity : AppCompatActivity() {

    private lateinit var nextButton: ImageButton
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX,0) ?: 0
        quizViewModel.currentIndex = currentIndex

        val correctAnswers = savedInstanceState?.getInt(CORRECT_ANSWERS,0) ?: 0
        quizViewModel.correctAnswers = correctAnswers

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)

        trueButton.setOnClickListener { view: View ->
            quizViewModel.correctAnswers = if(checkAnswer(true))
                quizViewModel.correctAnswers + 1 else quizViewModel.correctAnswers + 0
            blockAnswerButtons()
        }
        falseButton.setOnClickListener {view: View ->
            quizViewModel.correctAnswers = if(checkAnswer(false))
                quizViewModel.correctAnswers + 1 else quizViewModel.correctAnswers + 0
            blockAnswerButtons()
        }

        cheatButton.setOnClickListener { view: View ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        } // cheatButton.setOnClickListener

        nextButton.setOnClickListener { view: View ->
            if(quizViewModel.moveToNext())
                showResult()
            unblockAnswerButtons()
            updateQuestion()
        } // nextButton.setOnClickListener

        questionTextView.setOnClickListener { view: View ->
            if(quizViewModel.moveToNext())
                showResult()
            unblockAnswerButtons()
            updateQuestion()
        }
        updateQuestion()
    } // onCreate

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN,false) ?: false
        }
    } // onActivityResult

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
        Log.d(TAG,"Updating question text",Exception())
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

    } // updateQuestion

    private fun checkAnswer(userAnswer: Boolean): Boolean{
        var isCorrectAnswer = false
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> {
                isCorrectAnswer = true
                R.string.toast_correct
            }
            else -> {
                isCorrectAnswer = false
                R.string.toast_incorrect
            }
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show()
        return isCorrectAnswer
    } //checkAnswer

    private fun blockAnswerButtons(){
        trueButton.isEnabled = false
        falseButton.isEnabled = false
    } // blockAnswerButtons

    private fun unblockAnswerButtons(){
        trueButton.isEnabled = true
        falseButton.isEnabled = true
    } // unblockAnswerButtons

    private fun showResult(){
        Toast.makeText(this,
            "You answered ${quizViewModel.correctAnswers 
                    * 100 / quizViewModel.questionBankSize}%" +
                    " of the questions correctly", Toast.LENGTH_LONG).show()
        quizViewModel.correctAnswers = 0
    } //showResult

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putInt(CORRECT_ANSWERS, quizViewModel.correctAnswers)
    }

}