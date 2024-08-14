package com.breezenationalplasticfsm.features.mylearning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R

class HeaderAdapterForLMSNotification(private val headerItems: List<HeaderItem>) : RecyclerView.Adapter<HeaderAdapterForLMSNotification.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_header_for_lms_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val headerItem = headerItems[position]
        holder.headerText.text = headerItem.headerText

        val valueItems = headerItem.valueItems
        val valueAdapter = ValueAdapterForLMSNotification(valueItems)
        holder.nestedRecyclerView.adapter = valueAdapter
    }

    override fun getItemCount(): Int {
        return headerItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerText: TextView = itemView.findViewById(R.id.header_text)
        val nestedRecyclerView: RecyclerView = itemView.findViewById(R.id.nested_recycler_view)
    }
}