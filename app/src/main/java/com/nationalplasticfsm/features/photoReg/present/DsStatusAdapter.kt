package com.nationalplasticfsm.features.photoReg.present

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.domain.ProspectEntity
import kotlinx.android.synthetic.main.row_ds_status.view.*

class DsStatusAdapter(var context: Context,var dsList:ArrayList<ProspectEntity>,var listner: DsStatusListner):
    RecyclerView.Adapter<DsStatusAdapter.DsStatusViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DsStatusViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_ds_status,parent,false)
        return DsStatusViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dsList.size
    }

    override fun onBindViewHolder(holder: DsStatusViewHolder, position: Int) {
        holder.tv_ds.text=dsList.get(position).pros_name
        holder.tv_ds.setOnClickListener { listner?.getDSInfoOnLick(dsList.get(position)) }
    }

    inner class DsStatusViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val tv_ds = itemView.tv_ds_status
    }


}