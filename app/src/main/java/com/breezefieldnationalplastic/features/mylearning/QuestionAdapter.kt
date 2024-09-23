package com.breezefieldnationalplastic.features.mylearning

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.breezefieldnationalplastic.R
import kotlinx.android.synthetic.main.question_item_for_lms.view.answer_text1
import kotlinx.android.synthetic.main.question_item_for_lms.view.answer_text2
import kotlinx.android.synthetic.main.question_item_for_lms.view.answer_text3
import kotlinx.android.synthetic.main.question_item_for_lms.view.answer_text4
import kotlinx.android.synthetic.main.question_item_for_lms.view.header_text
import kotlinx.android.synthetic.main.question_item_for_lms.view.ll_question_view1
import kotlinx.android.synthetic.main.question_item_for_lms.view.ll_question_view2
import kotlinx.android.synthetic.main.question_item_for_lms.view.ll_question_view3
import kotlinx.android.synthetic.main.question_item_for_lms.view.ll_question_view4
import kotlinx.android.synthetic.main.question_item_for_lms.view.option_radio_button1
import kotlinx.android.synthetic.main.question_item_for_lms.view.option_radio_button2
import kotlinx.android.synthetic.main.question_item_for_lms.view.option_radio_button3
import kotlinx.android.synthetic.main.question_item_for_lms.view.option_radio_button4
import kotlinx.android.synthetic.main.question_item_for_lms.view.tv_row_question_lms_no

class QuestionAdapter(private val setUpQuestionAnswer: ArrayList<QuestionL>) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    companion object {
        var questionOptionsL: ArrayList<QuestionL> = ArrayList()
    }

    init {
        questionOptionsL =  setUpQuestionAnswer.clone() as ArrayList<QuestionL>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_item_for_lms, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return questionOptionsL.size
    }

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            try {
                if (questionOptionsL.get(adapterPosition).option_list.size > 0) {


                    itemView.tv_row_question_lms_no.text = "Question : ${adapterPosition + 1}"
                    itemView.header_text.text = questionOptionsL.get(adapterPosition).question
                    itemView.answer_text1.text = questionOptionsL.get(adapterPosition).option_list.get(0).option_no_1
                    itemView.answer_text2.text = questionOptionsL.get(adapterPosition).option_list.get(0).option_no_2
                    itemView.answer_text3.text = questionOptionsL.get(adapterPosition).option_list.get(0).option_no_3
                    itemView.answer_text4.text = questionOptionsL.get(adapterPosition).option_list.get(0).option_no_4

                    if(questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_1){
                        itemView.option_radio_button1.isChecked = true
                        itemView.ll_question_view1.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
                    }else{
                        itemView.option_radio_button1.isChecked = false
                        itemView.ll_question_view1.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
                    }
                    if(questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_2){
                        itemView.option_radio_button2.isChecked = true
                        itemView.ll_question_view2.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
                    }else{
                        itemView.option_radio_button2.isChecked = false
                        itemView.ll_question_view2.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
                    }
                    if(questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_3){
                        itemView.option_radio_button3.isChecked = true
                        itemView.ll_question_view3.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
                    }else{
                        itemView.option_radio_button3.isChecked = false
                        itemView.ll_question_view3.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
                    }
                    if(questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_4){
                        itemView.option_radio_button4.isChecked = true
                        itemView.ll_question_view4.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
                    }else{
                        itemView.option_radio_button4.isChecked = false
                        itemView.ll_question_view4.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
                    }

                    itemView.ll_question_view1.setOnClickListener {
                        if(questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_1){
                            questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_1=false
                            itemView.option_radio_button1.isChecked = false
                            itemView.ll_question_view1.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
                        }else{
                            questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_1=true
                            itemView.option_radio_button1.isChecked = true
                            itemView.ll_question_view1.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
                        }
                    }
                    itemView.ll_question_view2.setOnClickListener {
                        if(questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_2){
                            questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_2=false
                            itemView.option_radio_button2.isChecked = false
                            itemView.ll_question_view2.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
                        }else{
                            questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_2=true
                            itemView.option_radio_button2.isChecked = true
                            itemView.ll_question_view2.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
                        }
                    }
                    itemView.ll_question_view3.setOnClickListener {
                        if(questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_3){
                            questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_3=false
                            itemView.option_radio_button3.isChecked = false
                            itemView.ll_question_view3.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
                        }else{
                            questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_3=true
                            itemView.option_radio_button3.isChecked = true
                            itemView.ll_question_view3.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
                        }
                    }
                    itemView.ll_question_view4.setOnClickListener {
                        if(questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_4){
                            questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_4=false
                            itemView.option_radio_button4.isChecked = false
                            itemView.ll_question_view4.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
                        }else{
                            questionOptionsL.get(adapterPosition).option_list.get(0).isSelected_4=true
                            itemView.option_radio_button4.isChecked = true
                            itemView.ll_question_view4.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
                        }
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}