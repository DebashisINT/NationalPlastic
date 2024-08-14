package com.breezenationalplasticfsm.features.nearbyshops.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.domain.ProspectEntity
import kotlinx.android.synthetic.main.row_ds_status.view.*

class ShopUpdateStatusAdapter(var context: Context, var dsList:ArrayList<String>, var listner: ShopStatusListner):
    RecyclerView.Adapter<ShopUpdateStatusAdapter.DsStatusViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DsStatusViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_ds_status,parent,false)
        return DsStatusViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dsList.size
    }

    override fun onBindViewHolder(holder: DsStatusViewHolder, position: Int) {
        holder.tv_ds.text=dsList.get(position)
        holder.tv_ds.setOnClickListener { listner?.getStatusInfoOnLick(dsList.get(position)) }
    }

    inner class DsStatusViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val tv_ds = itemView.tv_ds_status
    }


}