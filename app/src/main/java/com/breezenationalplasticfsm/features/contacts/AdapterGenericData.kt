package com.breezenationalplasticfsm.features.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.domain.CallHisEntity
import kotlinx.android.synthetic.main.row_generic_dt.view.tv_row_generic_dt

class AdapterGenericData(var mContext: Context, var genericL:ArrayList<String>):RecyclerView.Adapter<AdapterGenericData.CallLogLViewHolder>() {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }

    inner class CallLogLViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_generic_dt.text = genericL.get(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogLViewHolder {
        val v = layoutInflater.inflate(R.layout.row_generic_dt, parent, false)
        return CallLogLViewHolder(v)
    }

    override fun getItemCount(): Int {
        return genericL.size
    }

    override fun onBindViewHolder(holder: CallLogLViewHolder, position: Int) {
        holder.bindItems()
    }


}