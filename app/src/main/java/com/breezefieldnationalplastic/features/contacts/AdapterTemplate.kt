package com.breezefieldnationalplastic.features.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.domain.ScheduleTemplateEntity
import com.breezefieldnationalplastic.app.domain.SchedulerMasterEntity
import kotlinx.android.synthetic.main.row_template_list.view.iv_row_templ_del
import kotlinx.android.synthetic.main.row_template_list.view.iv_row_template_info
import kotlinx.android.synthetic.main.row_template_list.view.tv_row_template_desc
import kotlinx.android.synthetic.main.row_template_list.view.tv_row_template_name

class AdapterTemplate(var mContext:Context,var mList:ArrayList<ScheduleTemplateEntity>,var listner:OnCLick):RecyclerView.Adapter<AdapterTemplate.TemplateViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_template_list,parent,false)
        return TemplateViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        holder.bindItems()
    }

    inner class TemplateViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_template_name.setText(mList.get(adapterPosition).template_name.toString())
                tv_row_template_desc.text = mList.get(adapterPosition).template_desc
                iv_row_template_info.setOnClickListener{
                    listner.onIClick(mList.get(adapterPosition))
                }
                iv_row_templ_del.setOnClickListener {
                    listner.onDelClick(mList.get(adapterPosition))
                }
            }
        }
    }


    interface OnCLick{
        fun onIClick(obj: ScheduleTemplateEntity)
        fun onDelClick(obj: ScheduleTemplateEntity)
    }

}