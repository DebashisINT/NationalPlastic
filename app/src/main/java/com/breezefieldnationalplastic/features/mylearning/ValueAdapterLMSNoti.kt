package com.breezefieldnationalplastic.features.mylearning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.domain.LMSNotiEntity
import kotlinx.android.synthetic.main.item_value_for_lms_notification.view.value_text
import kotlinx.android.synthetic.main.item_value_for_lms_notification.view.value_text_header

class ValueAdapterLMSNoti(private val notiL: ArrayList<LMSNotiEntity>) : RecyclerView.Adapter<ValueAdapterLMSNoti.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_value_for_lms_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.holderItems()
    }

    override fun getItemCount(): Int {
        return notiL.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun holderItems(){
            itemView.value_text_header.text = notiL.get(adapterPosition).noti_header
            itemView.value_text.text = notiL.get(adapterPosition).noti_message
        }
    }
}