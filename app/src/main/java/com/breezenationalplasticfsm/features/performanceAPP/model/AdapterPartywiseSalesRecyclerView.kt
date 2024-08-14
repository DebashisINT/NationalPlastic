package com.breezenationalplasticfsm.features.performanceAPP.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.features.performanceAPP.PartyWiseDataModel
import kotlinx.android.synthetic.main.row_party_wise_sales_list.view.*

/**
 * Created by Saheli on 13-04-2023 v 4.0.8 mantis 0025860.
 */
class AdapterPartywiseSalesRecyclerView(var context: Context, var dataList:ArrayList<PartyWiseDataModel>):
    RecyclerView.Adapter<AdapterPartywiseSalesRecyclerView.AdapterPartyNotVisitViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPartyNotVisitViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_party_wise_sales_list,parent,false)
        return AdapterPartyNotVisitViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterPartyNotVisitViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    inner class AdapterPartyNotVisitViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val shoppartyWiseList = itemView.aa_chart_view_items_party_wise_sales_list


    }

}