package com.breezefieldnationalplastic.features.contacts

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.domain.OpportunityAddEntity
import com.breezefieldnationalplastic.app.utils.AppUtils
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.iv_delete_opt
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.iv_edit_opt
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.iv_view_opt
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.oprtnty_amount_tv
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.oprtnty_date_tv
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.tv_oprtnty_dscrptn
import kotlinx.android.synthetic.main.inflate_oprtnty_history_item.view.tv_oprtnty_status
import kotlinx.android.synthetic.main.inflate_registered_shops.view.add_order_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.direction_view
import kotlinx.android.synthetic.main.inflate_slct_oprtnty_prdct_item.view.tv_row_slct_prdct_oprtnty
import kotlinx.android.synthetic.main.row_call_log_list.view.tv_row_call_log_his_sync_status
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_cont_list_cont_number
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_cont_list_cont_number_email
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_cont_list_cont_number_emailNew
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_cont_list_cont_number_info
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_cont_list_cont_number_infoNew
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_cont_list_cont_number_whatsapp
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_contact_edit
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_contact_share
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_contact_sync_unsync
import kotlinx.android.synthetic.main.row_contact_list.view.ll_addedDateTimeContactsForm
import kotlinx.android.synthetic.main.row_contact_list.view.ll_assignedTo
import kotlinx.android.synthetic.main.row_contact_list.view.ll_conatctsEmail
import kotlinx.android.synthetic.main.row_contact_list.view.ll_conatctsEmailNew
import kotlinx.android.synthetic.main.row_contact_list.view.ll_expected_sales_value
import kotlinx.android.synthetic.main.row_contact_list.view.ll_goneView
import kotlinx.android.synthetic.main.row_contact_list.view.ll_headView
import kotlinx.android.synthetic.main.row_contact_list.view.ll_row_contact_addr_root
import kotlinx.android.synthetic.main.row_contact_list.view.ll_row_contact_list_auto_activity
import kotlinx.android.synthetic.main.row_contact_list.view.ll_row_contact_list_order_show
import kotlinx.android.synthetic.main.row_contact_list.view.ll_row_contact_list_update_addr
import kotlinx.android.synthetic.main.row_contact_list.view.ll_row_contact_list_will_activity_show
import kotlinx.android.synthetic.main.row_contact_list.view.ll_row_contact_list_will_direction
import kotlinx.android.synthetic.main.row_contact_list.view.ll_row_opportunity_show
import kotlinx.android.synthetic.main.row_contact_list.view.ll_souce
import kotlinx.android.synthetic.main.row_contact_list.view.ll_view_contact_number
import kotlinx.android.synthetic.main.row_contact_list.view.ll_view_contact_numberNew
import kotlinx.android.synthetic.main.row_contact_list.view.ll_view_whatsapp
import kotlinx.android.synthetic.main.row_contact_list.view.ll_view_whatsappNew
import kotlinx.android.synthetic.main.row_contact_list.view.space_for_ll_assignedTo
import kotlinx.android.synthetic.main.row_contact_list.view.space_for_ll_source
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_added_addition_source
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_added_dt_ti
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_addr
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_assign_to
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_number
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_numberNew
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_number_email
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_number_emailNew
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_number_whatsapp
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_number_whatsappNew
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_source
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_stage
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_stageNew
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_status
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_statusNew
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_cont_type
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_expctd_sales_value
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_name
import kotlinx.android.synthetic.main.row_contact_list.view.tv_row_cont_list_name_initial


class AdapterSlctdPrdctLstAdapter(var mContext:Context, var oprtntySlctdPrdctL:ArrayList<ProductDtls>):
    RecyclerView.Adapter<AdapterSlctdPrdctLstAdapter.OpportunityListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpportunityListViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.inflate_slct_oprtnty_prdct_item,parent,false)
        return OpportunityListViewHolder(v)
    }

    override fun onBindViewHolder(holder: OpportunityListViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return oprtntySlctdPrdctL.size
    }

    inner class OpportunityListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_slct_prdct_oprtnty.text = oprtntySlctdPrdctL.get(adapterPosition).product_name
            }
        }
    }

}