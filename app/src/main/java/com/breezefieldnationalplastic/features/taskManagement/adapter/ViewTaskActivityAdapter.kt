package com.breezefieldnationalplastic.features.taskManagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.features.lead.model.CustomerLeadList
import com.breezefieldnationalplastic.features.lead.model.activity_dtls_list
import com.breezefieldnationalplastic.features.taskManagement.Taskdtls_list
import kotlinx.android.synthetic.main.row_view_activity_lead.view.*

// create by saheli 05-05-2023 mantis 0026023
class ViewTaskActivityAdapter(var mContext:Context, var list:ArrayList<Taskdtls_list>, private val listener: OnViewActiClickListener):
    RecyclerView.Adapter<ViewTaskActivityAdapter.ViewActivityViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewActivityViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_view_activity_task, parent, false)
        return ViewActivityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewActivityViewHolder, position: Int) {
        holder.tv_date.text=list.get(position).task_date
        holder.tv_time.text=list.get(position).task_time
        holder.tv_status.text=list.get(position).task_status
        holder.tv_dtls.text=list.get(position).task_details
        holder.tv_remark.text=list.get(position).other_remarks

        if(CustomStatic.IsViewTaskFromInProcess){
            holder.iv_edit.visibility=View.VISIBLE
        }else{
            holder.iv_edit.visibility=View.GONE
        }

        holder.iv_edit.setOnClickListener {
            listener.onEditClick(list.get(holder.adapterPosition),holder.adapterPosition)
        }
    }

    inner class ViewActivityViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var tv_date = itemView.tv_row_view_acti_lead_date
        var tv_time = itemView.tv_row_view_acti_lead_time
        var tv_status = itemView.tv_row_view_acti_lead_status
        var tv_type = itemView.tv_row_view_acti_lead_type
        var tv_dtls = itemView.tv_row_view_acti_lead_dtls
        var tv_remark = itemView.tv_row_view_acti_lead_remarks

        var iv_edit = itemView.iv_row_view_acti_lead_edit
    }

    interface OnViewActiClickListener {
        fun onEditClick(obj: Taskdtls_list,adapterPos:Int)
    }


}