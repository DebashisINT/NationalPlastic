package com.breezefieldnationalplastic.features.mylearning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.utils.AppUtils
import kotlinx.android.synthetic.main.item_header_for_lms_notification.view.nested_recycler_view
import kotlinx.android.synthetic.main.item_header_for_lms_notification.view.noti_header_date

class HeaderAdapterLMSNoti(private val notiRootL: ArrayList<LMSNotiFilterData>) : RecyclerView.Adapter<HeaderAdapterLMSNoti.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_header_for_lms_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return notiRootL.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(){
            try {
                var dt =
                if(AppUtils.getCurrentDateForShopActi().equals(notiRootL.get(adapterPosition).noti_date)) "Today"
                else if(AppUtils.getOneDayPreviousDate(AppUtils.getCurrentDateForShopActi()).equals(notiRootL.get(adapterPosition).noti_date)) "Yesterday"
                else AppUtils.getFormatedDateNew(notiRootL.get(adapterPosition).noti_date,"yyyy-mm-dd","dd-mm-yyyy")!!

                itemView.noti_header_date.text = dt

                val valueAdapter = ValueAdapterLMSNoti(notiRootL.get(adapterPosition).notiL)
                itemView.nested_recycler_view.adapter = valueAdapter
            } catch (e: Exception) {
               e.printStackTrace()
            }
        }
    }
}