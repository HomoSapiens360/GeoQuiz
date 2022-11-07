package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americans, true),
        Question(R.string.question_asia, true)
    )
    var currentIndex = 0
    var correctAnswers = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val questionBankSize: Int
        get() = questionBank.size

    fun moveToNext(): Boolean {
        var isTheLastQuestion: Boolean = false
        currentIndex++
        if(currentIndex == questionBank.size){
            isTheLastQuestion = true
            currentIndex = 0;
        }
        return isTheLastQuestion
    }

}