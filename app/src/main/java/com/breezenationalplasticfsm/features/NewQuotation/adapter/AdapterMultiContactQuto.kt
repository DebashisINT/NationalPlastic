package com.breezenationalplasticfsm.features.NewQuotation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.domain.ShopExtraContactEntity
import com.breezenationalplasticfsm.features.NewQuotation.model.product_list
import com.breezenationalplasticfsm.features.NewQuotation.model.shop_wise_quotation_list
import com.breezenationalplasticfsm.features.viewAllOrder.interf.QaOnCLick
import kotlinx.android.synthetic.main.row_new_quot_added_prod.view.*
import kotlinx.android.synthetic.main.row_quto_multi_cont.view.*

class AdapterMultiContactQuto(private var context: Context, private var prodList: ArrayList<ShopExtraContactEntity>, var listner: OnClickListener) :
    RecyclerView.Adapter<AdapterMultiContactQuto.ShowMultiContViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowMultiContViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_quto_multi_cont, parent, false)
        return ShowMultiContViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowMultiContViewHolder, position: Int) {
        holder.bindItems(context, prodList)
    }

    override fun getItemCount(): Int {
        return prodList.size
    }

    inner class ShowMultiContViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(context: Context, mList: ArrayList<ShopExtraContactEntity>?) {
            itemView.tv_row_quto_multi_cont_name_ph.text = mList!!.get(adapterPosition).contact_name +" "+(mList!!.get(adapterPosition).contact_number)
            itemView.tv_row_quto_multi_cont_email.text = "Email : "+mList!!.get(adapterPosition).contact_email

            itemView.cb_row_quto_multi_cont.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    listner.onTickUntickView(mList!!.get(adapterPosition),true)
                }else{
                    listner.onTickUntickView(mList!!.get(adapterPosition),false)
                }
            }
        }
    }

    interface OnClickListener {
        fun onTickUntickView(obj: ShopExtraContactEntity,isTick:Boolean)
    }
    
}