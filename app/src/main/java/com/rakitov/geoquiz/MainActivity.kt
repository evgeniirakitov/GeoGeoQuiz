package com.rakitov.geoquiz

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }
    var countCorrectAnswer = 0
    var countInCorrectAnswer = 0





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        previousButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    private fun updateQuestion()
    {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        showAnswerBottom()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId: Int
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            countCorrectAnswer++
        } else {
            messageResId = R.string.incorrect_toast
            countInCorrectAnswer++
        }

        if ((countCorrectAnswer + countInCorrectAnswer) == 6) {
            Toast.makeText(
                    this,
                    printResult(countInCorrectAnswer, countInCorrectAnswer),
                    Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(this, messageResId, Toast.LENGTH_LONG)
                    .show()
        }
        hideAnswerBottom()
    }

    private fun printResult(correct: Int, incorrect: Int): String {
        return "Ваш результат: /nПравильных ответов: $correct/n Неправильных ответов: $incorrect"
    }

    private fun hideAnswerBottom() {
        trueButton.isEnabled = false
        falseButton.isEnabled = false
    }

    private fun showAnswerBottom() {
        trueButton.isEnabled = true
        falseButton.isEnabled = true
    }
}