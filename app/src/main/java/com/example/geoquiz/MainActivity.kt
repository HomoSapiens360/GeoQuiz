package com.example.geoquiz

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var nextButton: Button
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            //Toast.makeText(this,R.string.toast_correct,Toast.LENGTH_SHORT).show()
        }
        falseButton.setOnClickListener{view: View ->
            checkAnswer(false)
            //Toast.makeText(this,R.string.toast_incorrect,Toast.LENGTH_SHORT).show()
        }
        nextButton.setOnClickListener { view: View ->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()
    }

    private fun updateQuestion(){
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer){
            R.string.toast_correct
        } else {
            R.string.toast_incorrect
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show()
    }


}