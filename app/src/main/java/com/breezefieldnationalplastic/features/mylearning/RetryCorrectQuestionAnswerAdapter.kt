package com.breezefieldnationalplastic.features.mylearning

// RetryCorrectQuestionAnswerAdapter.kt
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R



class RetryCorrectQuestionAnswerAdapter(
    private val questions: List<CorrectQuestionAnswer>,
    mContext: Context
) : RecyclerView.Adapter<RetryCorrectQuestionAnswerAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionText: TextView = itemView.findViewById(R.id.question_text)
        private val tv_qstn_nmbr: TextView = itemView.findViewById(R.id.tv_qstn_nmbr)
        private val option1: TextView = itemView.findViewById(R.id.option1)
        private val option2: TextView = itemView.findViewById(R.id.option2)
        private val option3: TextView = itemView.findViewById(R.id.option3)
        private val option4: TextView = itemView.findViewById(R.id.option4)
        private val ll_op1: LinearLayout = itemView.findViewById(R.id.ll_op1)
        private val ll_op2: LinearLayout = itemView.findViewById(R.id.ll_op2)
        private val ll_op3: LinearLayout = itemView.findViewById(R.id.ll_op3)
        private val ll_op4: LinearLayout = itemView.findViewById(R.id.ll_op4)
        private val iv_correct_tick_op1: ImageView = itemView.findViewById(R.id.iv_correct_tick_op1)
        private val iv_correct_tick_op2: ImageView = itemView.findViewById(R.id.iv_correct_tick_op2)
        private val iv_correct_tick_op3: ImageView = itemView.findViewById(R.id.iv_correct_tick_op3)
        private val iv_correct_tick_op4: ImageView = itemView.findViewById(R.id.iv_correct_tick_op4)

        fun bind(question: CorrectQuestionAnswer) {
            questionText.text = "${question.question}" // Adding 1 to position for 1-based index
            tv_qstn_nmbr.text = "Question "+(position + 1).toString()
            // Prepend letters to options
            option1.text = "A. ${question.optionList.get(0).optionNo1}"
            option2.text = "B. ${question.optionList.get(0).optionNo2}"
            option3.text = "C. ${question.optionList.get(0).optionNo3}"
            option4.text = "D. ${question.optionList.get(0).optionNo4}"

            // Highlight the answered option
            when (question.answered) {
                "optionNo1" -> {
                    ll_op1.setBackgroundResource(R.drawable.back_corner_green)
                    iv_correct_tick_op1.visibility = View.VISIBLE
                }
                "optionNo2" -> {
                    ll_op2.setBackgroundResource(R.drawable.back_corner_green)
                    iv_correct_tick_op2.visibility = View.VISIBLE
                }
                "optionNo3" -> {
                    ll_op3.setBackgroundResource(R.drawable.back_corner_green)
                    iv_correct_tick_op3.visibility = View.VISIBLE
                }
                "optionNo4" -> {
                    ll_op4.setBackgroundResource(R.drawable.back_corner_green)
                    iv_correct_tick_op4.visibility = View.VISIBLE
                }
            }

           /* // Hide the separator view if this is the last item
            separator_view.visibility = if (position == itemCount - 1) View.GONE else View.VISIBLE*/

            // Set click listeners for each option
            val options = listOf(option1, option2, option3, option4)
            options.forEachIndexed { index, textView ->

            }
        }
    }

    private fun highlightAnsweredOption(answered: String) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_correct_question_answer, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size
}