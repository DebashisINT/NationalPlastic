package com.breezenationalplasticfsm.features.performanceAPP

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.features.attendance.model.last_visit_order_list
import kotlinx.android.synthetic.main.row_party_notvisited_list.view.*

/**
 * Created by Saheli on 13-04-2023 v 4.0.8 mantis 0025860.
 */
class AdapterPartyNotVisitRecyclerView(var context: Context, var dataList:ArrayList<last_visit_order_list>):
    RecyclerView.Adapter<AdapterPartyNotVisitRecyclerView.AdapterPartyNotVisitViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPartyNotVisitViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_party_notvisited_list,parent,false)
        return AdapterPartyNotVisitViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterPartyNotVisitViewHolder, position: Int) {
        holder.shopNameTV.text=dataList.get(position).shop_name
        holder.shopTypeTv.text=dataList.get(position).shop_TypeName
        holder.shopwiseLastOrderDt.text="Last Order Date \n "+ AppUtils.convertPartyNotVisitedFormat(dataList.get(position).last_order_date)
        holder.shopwiseLastVisitDt.text="Last Visit Date \n "+ AppUtils.convertPartyNotVisitedFormat(dataList.get(position).last_visited_date)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class AdapterPartyNotVisitViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val shopNameTV = itemView.tv_row_party_notviisted_listShopName
        val shopTypeTv = itemView.tv_row_party_notviisted_listShopType
        val shopwiseLastOrderDt = itemView.tv_row_party_notviisted_listlastOrderDt
        val shopwiseLastVisitDt = itemView.tv_row_party_notviisted_listlastvisitDt

    }

}