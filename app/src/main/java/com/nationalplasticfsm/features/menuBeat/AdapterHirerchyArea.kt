package com.nationalplasticfsm.features.menuBeat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.R
import kotlinx.android.synthetic.main.row_beat_hirerchy.view.*
// 3.0 BeatHierarchyStatusFrag AppV 4.0.6 Suman 20-01-2023  hirerchy design updation
class AdapterHirerchyArea(var mContext:Context, var areaL:ArrayList<MenuBeatAreaResponse>):RecyclerView.Adapter<AdapterHirerchyArea.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.row_beat_hirerchy,parent,false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return areaL.size
    }

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_beat_hir_area_name.text = "Area: "+areaL.get(adapterPosition).area_name
                var adapterHirerchyRoute = AdapterHirerchyRoute(mContext,areaL.get(adapterPosition).route_list)
                rv_row_beat_hir_route.adapter = adapterHirerchyRoute
            }
        }
    }

}