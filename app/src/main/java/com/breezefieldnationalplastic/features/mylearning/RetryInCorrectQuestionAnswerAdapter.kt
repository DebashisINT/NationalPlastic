package com.breezefieldnationalplastic.features.mylearning

// RetryCorrectQuestionAnswerAdapter.kt
import com.breezefieldnationalplastic.R

import kotlinx.android.synthetic.main.item_incorrect_question_answer.view.bt_retry
import kotlinx.android.synthetic.main.item_incorrect_question_answer.view.tv_incorrect_answer
import kotlinx.android.synthetic.main.item_incorrect_question_answer.view.tv_incorrect_question
// QuestionAnswerAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RetryInCorrectQuestionAnswerAdapter(
    private val items: List<CorrectQuestionAnswer>,
    private val onRetryClick: (Int) -> Unit // Callback for retry button
) : RecyclerView.Adapter<RetryInCorrectQuestionAnswerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val questionTextView = view.tv_incorrect_question
        //val answerTextView = view.tv_incorrect_answer
        //val retryButton = view.bt_retry
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_incorrect_question_answer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
       // holder.questionTextView.text = "${position + 1}. ${item.question}"
        //holder.answerTextView.text = item.answer

        /*holder.retryButton.setOnClickListener {
            onRetryClick(position) // Notify when the retry button is clicked
        }*/
    }

    override fun getItemCount(): Int = items.size
}
