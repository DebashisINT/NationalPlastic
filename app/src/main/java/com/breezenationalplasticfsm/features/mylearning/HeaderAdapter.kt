package com.breezenationalplasticfsm.features.mylearning
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.breezenationalplasticfsm.R

class HeaderAdapter(private val headers: List<String>) : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.header_item_for_question_answer_setup_for_lms, parent, false)
        return HeaderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(headers[position])
    }

    override fun getItemCount(): Int {
        return headers.size
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerTextView: TextView = itemView.findViewById(R.id.header_text)

        fun bind(header: String) {
            headerTextView.text = header
        }
    }
}