package com.breezefieldnationalplastic.features.photoReg.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.features.photoReg.model.ProsCustom
import kotlinx.android.synthetic.main.row_pros_team_attend.view.*

class ProsListSelectionAdapter(var context:Context,var list:ArrayList<ProsCustom>,var listner:ProsListSelectionListner):
        RecyclerView.Adapter<ProsListSelectionAdapter.ProsListSelectionViewHolder>(){

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProsListSelectionViewHolder {
        val v = layoutInflater.inflate(R.layout.row_pros_team_attend, parent, false)
        return ProsListSelectionViewHolder(v)
    }

    override fun getItemCount(): Int {
       return list.size!!
    }

    override fun onBindViewHolder(holder: ProsListSelectionViewHolder, position: Int) {
        holder.prosName.text=list.get(position).prosName
        holder.prosName.setOnClickListener{listner.getInfo(list.get(holder.adapterPosition))}
    }

    inner class ProsListSelectionViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var prosName = itemView.tv_row_pros_team_attend_name
    }

}