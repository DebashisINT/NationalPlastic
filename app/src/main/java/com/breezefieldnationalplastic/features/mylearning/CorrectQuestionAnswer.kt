package com.breezefieldnationalplastic.features.mylearning
// CorrectQuestionAnswer.kt
data class CorrectQuestionAnswer(
    val questionId: Int,
    val question: String,
    val answered: String,
    val optionList: List<Option>
)

data class Option(
    val optionId: Int,
    val optionNo1: String,
    val optionPoint1: Int,
    val isCorrect1: Boolean,
    val optionNo2: String,
    val optionPoint2: Int,
    val isCorrect2: Boolean,
    val optionNo3: String,
    val optionPoint3: Int,
    val isCorrect3: Boolean,
    val optionNo4: String,
    val optionPoint4: Int,
    val isCorrect4: Boolean
)
