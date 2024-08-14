package com.breezenationalplasticfsm.features.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.OpportunityAddEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.iv_delete_opt
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.iv_edit_opt
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.iv_view_opt
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.oprtnty_amount_tv
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.oprtnty_date_tv
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.tv_oprtnty_dscrptn
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.tv_oprtnty_status


class AdapterOpportunityList(var mContext:Context, var oprtntyL:ArrayList<OpportunityAddEntity>, var listner:onClick):
    RecyclerView.Adapter<AdapterOpportunityList.OpportunityListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpportunityListViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.inflate_oprtnty_history_item,parent,false)
        return OpportunityListViewHolder(v)
    }

    override fun onBindViewHolder(holder: OpportunityListViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return oprtntyL.size
    }

    inner class OpportunityListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                if (oprtntyL.get(adapterPosition).opportunity_amount.equals("NA") || oprtntyL.get(adapterPosition).opportunity_amount.equals("0.0") || oprtntyL.get(adapterPosition).opportunity_amount.equals("0") || oprtntyL.get(adapterPosition).opportunity_amount.equals("")) {
                    oprtnty_amount_tv.text = "0"
                }else{
                    oprtnty_amount_tv.text = oprtntyL.get(adapterPosition).opportunity_amount
                }
                oprtnty_date_tv.text = AppUtils.getFormatedDateNew(oprtntyL.get(adapterPosition).opportunity_created_date,"yyyy-mm-dd","dd-mm-yyyy")
                tv_oprtnty_status.text = oprtntyL.get(adapterPosition).opportunity_status_name
                tv_oprtnty_dscrptn.text = oprtntyL.get(adapterPosition).opportunity_description

                if (Pref.IsEditEnableforOpportunity){
                    iv_edit_opt.visibility =View.VISIBLE
                }else{
                    iv_edit_opt.visibility =View.GONE
                }
                if (Pref.IsDeleteEnableforOpportunity){
                    iv_delete_opt.visibility =View.VISIBLE
                }else{
                    iv_delete_opt.visibility =View.GONE
                }
                iv_view_opt.setOnClickListener {

                    listner.onInfoClick(iv_view_opt , oprtntyL.get(adapterPosition))
                }

                println("edit_load_tag $adapterPosition")
                iv_edit_opt.isEnabled = true
                iv_edit_opt.setOnClickListener {
                    println("edit_load_tag click")
                    iv_edit_opt.isEnabled = false
                    listner.onEditClick(oprtntyL.get(adapterPosition))
                }

                iv_delete_opt.setOnClickListener {
                    listner.onDeleteClick(iv_delete_opt , oprtntyL.get(adapterPosition))
                }
            }
        }
    }

    interface onClick{
        fun onInfoClick(iv_view_opt: LinearLayout, obj: OpportunityAddEntity)
        fun onEditClick(obj:OpportunityAddEntity)
        fun onDeleteClick(iv_delete_opt: LinearLayout, obj: OpportunityAddEntity)
    }

}