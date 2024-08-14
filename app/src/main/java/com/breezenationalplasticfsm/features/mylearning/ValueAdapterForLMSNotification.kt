package com.breezenationalplasticfsm.features.mylearning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R

class ValueAdapterForLMSNotification(private val valueItems: List<ValueItem>) : RecyclerView.Adapter<ValueAdapterForLMSNotification.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_value_for_lms_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val valueItem = valueItems[position]
        holder.value_text_header.text = valueItem.valueHeader
        holder.valueText.text = valueItem.valueText
        holder.imageView.setImageResource(valueItem.imageResId)
    }

    override fun getItemCount(): Int {
        return valueItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val valueText: TextView = itemView.findViewById(R.id.value_text)
        val value_text_header: TextView = itemView.findViewById(R.id.value_text_header)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }
}