package com.breezenationalplasticfsm.features.mylearning
import com.breezenationalplasticfsm.R
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.answer_option_item_for_lms.view.answer_text
import kotlinx.android.synthetic.main.answer_option_item_for_lms.view.ll_question_view
import kotlinx.android.synthetic.main.answer_option_item_for_lms.view.option_radio_button

class AnswerOptionAdapter(private val answerOptions: ArrayList<QuestionOptions>) :
    RecyclerView.Adapter<AnswerOptionAdapter.AnswerViewHolder>() {

    private var selectedItemPosition = -1

    inner class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.answer_text.text = answerOptions.get(adapterPosition).desc
            if(answerOptions.get(adapterPosition).isSelected){
                itemView.option_radio_button.isChecked = true
                itemView.ll_question_view.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
            }else{
                itemView.option_radio_button.isChecked = false
                itemView.ll_question_view.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
            }

            itemView.ll_question_view.setOnClickListener {
                if(answerOptions.get(adapterPosition).isSelected){
                    answerOptions.get(adapterPosition).isSelected=false
                    itemView.option_radio_button.isChecked = false
                    itemView.ll_question_view.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
                }else{
                    answerOptions.get(adapterPosition).isSelected=true
                    itemView.option_radio_button.isChecked = true
                    itemView.ll_question_view.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
                }
            }

          /*  if (radioButton.isChecked) {
                radioButtonL.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_green)
            } else {
                radioButtonL.background = ContextCompat.getDrawable(itemView.context, R.drawable.back_round_corner_7)
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.answer_option_item_for_lms, parent, false)
        return AnswerViewHolder(itemView)
    }

    private val selectedPositions = mutableSetOf<Int>()

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind()
      /*  holder.itemView.ll_question_view.setOnClickListener {
            if (isSelected) {
                selectedPositions.remove(position)
            } else {
                selectedPositions.add(position)
            }
            notifyDataSetChanged()
        }*/
    }
    override fun getItemCount(): Int {
        return answerOptions.size
    }
}