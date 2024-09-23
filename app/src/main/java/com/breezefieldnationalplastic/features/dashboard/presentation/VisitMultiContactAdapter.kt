package com.breezefieldnationalplastic.features.dashboard.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.domain.ShopExtraContactEntity
import com.breezefieldnationalplastic.app.domain.VisitRemarksEntity
import kotlinx.android.synthetic.main.inflate_revisit_multi_contact.view.*

class VisitMultiContactAdapter(context: Context, private val list: ArrayList<ShopExtraContactEntity>?, private val listener: OnItemClickListener):
RecyclerView.Adapter<VisitMultiContactAdapter.MyViewHolder>(){

    private val layoutInflater: LayoutInflater
    private var context: Context

    init {
        layoutInflater = LayoutInflater.from(context)
        this.context = context
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, list, listener)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.inflate_revisit_multi_contact, parent, false)
        return MyViewHolder(v)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(context: Context, list: ArrayList<ShopExtraContactEntity>?, listener: OnItemClickListener) {
            itemView.tv_inlf_rev_multi_cont_show.text = list!!.get(adapterPosition).contact_name + " (${list!!.get(adapterPosition).contact_number})"
            itemView.tv_inlf_rev_multi_cont_show.setOnClickListener {
                listener.onItemClick(list!!.get(adapterPosition))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(obj:ShopExtraContactEntity)
    }

}

