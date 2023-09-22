package com.nationalplasticfsm.features.taskManagement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.domain.ShopExtraContactEntity
import com.nationalplasticfsm.app.utils.Toaster
import com.nationalplasticfsm.features.NewQuotation.model.product_list
import com.nationalplasticfsm.features.NewQuotation.model.shop_wise_quotation_list
import com.nationalplasticfsm.features.viewAllOrder.interf.QaOnCLick
import kotlinx.android.synthetic.main.row_new_quot_added_prod.view.*
import kotlinx.android.synthetic.main.row_priority_multi_sel.view.cb_row_priority_multi_sel
import kotlinx.android.synthetic.main.row_priority_multi_sel.view.tv_row_priority_multi_sel_priority_name
import kotlinx.android.synthetic.main.row_quto_multi_cont.view.*

// Create by Saheli v 4.0.8 15-05-2023 mantis 26121
class AdapterMultiPrioritySel(private var context: Context, private var priorityList: ArrayList<TaskPriorityResponse>, var listner: OnClickListener) :
    RecyclerView.Adapter<AdapterMultiPrioritySel.ShowMultiContViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowMultiContViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_priority_multi_sel, parent, false)
        return ShowMultiContViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowMultiContViewHolder, position: Int) {
        holder.bindItems(context, priorityList)
    }

    override fun getItemCount(): Int {
        return priorityList.size
    }

    inner class ShowMultiContViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(context: Context, mList: ArrayList<TaskPriorityResponse>?) {
            itemView.tv_row_priority_multi_sel_priority_name.text = mList!!.get(adapterPosition).task_priority_name
            itemView.cb_row_priority_multi_sel.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    /*if(!mList!!.get(adapterPosition).task_priority_name.equals("All")){
                        buttonView.isChecked = false
                        return@setOnCheckedChangeListener
                    }*/
                    listner.onTickUntickView(mList!!.get(adapterPosition),true)
                }else{
                    listner.onTickUntickView(mList!!.get(adapterPosition),false)
                }
            }
        }
    }

    interface OnClickListener {
        fun onTickUntickView(obj: TaskPriorityResponse,isTick:Boolean)
    }
    
}