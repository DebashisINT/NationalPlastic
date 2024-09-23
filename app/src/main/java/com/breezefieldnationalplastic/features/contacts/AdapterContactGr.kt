package com.breezefieldnationalplastic.features.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import kotlinx.android.synthetic.main.row_contact_gr.view.tv_row_cont_gr_name

class AdapterContactGr(var mContext:Context,var mList:ArrayList<ContactGr>,var listner:onClick):
    RecyclerView.Adapter<AdapterContactGr.ContactGrViewHolder>(){


    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactGrViewHolder {
        val v = layoutInflater.inflate(R.layout.row_contact_gr, parent, false)
        return ContactGrViewHolder(v)
    }

    override fun onBindViewHolder(holder: ContactGrViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ContactGrViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_cont_gr_name.text = mList.get(adapterPosition).gr_name

                tv_row_cont_gr_name.setOnClickListener {
                    listner.onGrClick(mList.get(adapterPosition))
                }
            }
        }
    }

    interface onClick{
        fun onGrClick(obj:ContactGr)
    }

}