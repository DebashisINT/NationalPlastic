package com.breezenationalplasticfsm.features.menuBeat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import kotlinx.android.synthetic.main.row_beat_hirerchy.view.*
import kotlinx.android.synthetic.main.row_beat_hirerchy_route.view.*
// 3.0 BeatHierarchyStatusFrag AppV 4.0.6 Suman 20-01-2023  hirerchy design updation
class AdapterHirerchyRoute (var mContext: Context, var routeL:ArrayList<MenuBeatRoutesResponse>):
    RecyclerView.Adapter<AdapterHirerchyRoute.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.row_beat_hirerchy_route,parent,false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return routeL.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_beat_hir_route_name.text = "Route: "+routeL.get(adapterPosition).route_name
                var adapterHirerchyBeat = AdapterHirerchyBeat(mContext,routeL.get(adapterPosition).beat_list)
                rv_row_beat_hir_beat.adapter = adapterHirerchyBeat
            }
        }
    }

}