package com.breezenationalplasticfsm.features.shopFeedbackHistory.adapter

import android.content.Context
import android.text.TextUtils.substring
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.features.lead.model.CustomerLeadList
import com.breezenationalplasticfsm.features.login.ShopFeedbackEntity
import com.google.zxing.common.StringUtils
import kotlinx.android.synthetic.main.row_feedback_list.view.*
import org.apache.commons.lang3.StringUtils.substring

class FeedBackListAdapter(var mContext:Context, var list:ArrayList<ShopFeedbackEntity>) :
   RecyclerView.Adapter<FeedBackListAdapter.CustomerLeadViewHolder>() {

    private var mList: ArrayList<ShopFeedbackEntity>? = null

    init {
        mList = ArrayList()
        mList?.addAll(list)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerLeadViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_feedback_list, parent, false)
        return CustomerLeadViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun onBindViewHolder(holder: CustomerLeadViewHolder, position: Int) {
        var ddate=list.get(position).date_time!!.substring(0,10)
        holder.dateShow.text=  AppUtils.getFormatedDateNew(ddate,"yyyy-mm-dd","dd-mm-yyyy")
        holder.feedbackShow.text=list.get(position).feedback
        holder.nameShow.text=list.get(position).multi_contact_name
    }

    inner class CustomerLeadViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var dateShow = itemView.date_tv
        var feedbackShow = itemView.feedback_tv
        var nameShow = itemView.tv_feedback_name
    }

}