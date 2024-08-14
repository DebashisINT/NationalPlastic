package com.breezenationalplasticfsm.features.NewQuotation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.domain.NewOrderGenderEntity
import com.breezenationalplasticfsm.features.NewQuotation.interfaces.TaxOnclick
import com.breezenationalplasticfsm.features.viewAllOrder.interf.GenderListOnClick
import kotlinx.android.synthetic.main.row_dialog_new_order_gender.view.*
import kotlinx.android.synthetic.main.row_dialog_new_order_gender.view.tv_row_dialog_new_order_gender
import kotlinx.android.synthetic.main.row_dialog_tax.view.*

class TaxListAdapter(private var context:Context, private var tax_list:ArrayList<String>, private val listner: TaxOnclick):
  RecyclerView.Adapter<TaxListAdapter.GenderListViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_dialog_tax,parent,false)
        return GenderListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tax_list!!.size!!
    }

    override fun onBindViewHolder(holder: GenderListViewHolder, position: Int) {
        holder.tv_tax.text=tax_list.get(position)!!
        holder.cv_tax.setOnClickListener { listner?.OnClick(tax_list.get(holder.adapterPosition)!!) }
    }

    inner class GenderListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tv_tax = itemView.tv_row_dialog_tax
        val cv_tax = itemView.cv_tax
    }

}

