package com.breezenationalplasticfsm.features.contacts

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
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import kotlinx.android.synthetic.main.inflate_registered_shops.view.add_order_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.direction_view
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
import kotlinx.android.synthetic.main.row_contact_list.view.ll_contacStatus
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
import kotlinx.android.synthetic.main.row_contact_list.view.ll_stage_1
import kotlinx.android.synthetic.main.row_contact_list.view.ll_stage_root
import kotlinx.android.synthetic.main.row_contact_list.view.ll_status_root
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


class AdapterContactList(var mContext:Context,var shopL:ArrayList<AddShopDBModelEntity>,var searchText:String,var listner:onClick):
    RecyclerView.Adapter<AdapterContactList.ContactListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_contact_list,parent,false)
        return ContactListViewHolder(v)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return shopL.size
    }

    inner class ContactListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                //Code start by Puja
                // tv_row_cont_list_name_initial.text= shopL.get(adapterPosition).shopName!!.get(0).toString()
                //Code end by Puja
                println("shopname"+shopL.get(adapterPosition).shopName)
                tv_row_cont_list_name.text = shopL.get(adapterPosition).shopName
                if (shopL.get(adapterPosition).address.equals("NA") || shopL.get(adapterPosition).address.equals("")) {
                    tv_row_cont_list_addr.text = ""
                }else{
                    tv_row_cont_list_addr.text = shopL.get(adapterPosition).address

                }
                tv_row_cont_list_cont_number.text = shopL.get(adapterPosition).ownerContactNumber
                tv_row_cont_list_cont_numberNew.text = shopL.get(adapterPosition).ownerContactNumber
                var added_date = shopL.get(adapterPosition).added_date.replace("T"," ").split(" ").get(0).toString()
                var added_time = shopL.get(adapterPosition).added_date.replace("T"," ").split(" ").get(1).toString()
                tv_row_cont_list_added_dt_ti.text = AppUtils.getFormatedDateNew(added_date,"yyyy-mm-dd","dd-mm-yyyy")+"   "+added_time.substring(0,5).toString()
                tv_row_cont_list_cont_type.text = if(shopL.get(adapterPosition).crm_type.isNullOrEmpty()) "None" else shopL.get(adapterPosition).crm_type
                tv_row_cont_list_cont_source.text = if(shopL.get(adapterPosition).crm_source.isNullOrEmpty()) "None" else  shopL.get(adapterPosition).crm_source
                tv_row_cont_list_cont_stage.text = if(shopL.get(adapterPosition).crm_stage.isNullOrEmpty()) "None" else  shopL.get(adapterPosition).crm_stage
                tv_row_cont_list_cont_stageNew.text = if(shopL.get(adapterPosition).crm_stage.isNullOrEmpty()) "None" else  shopL.get(adapterPosition).crm_stage
                tv_row_cont_list_cont_assign_to.text = if(shopL.get(adapterPosition).crm_assignTo.isNullOrEmpty() || shopL.get(adapterPosition).crm_assignTo.equals("0") ) "${Pref.user_name}" else  shopL.get(adapterPosition).crm_assignTo
                tv_row_cont_list_cont_status.text = if(shopL.get(adapterPosition).crm_status.isNullOrEmpty()) "None" else  shopL.get(adapterPosition).crm_status
                tv_row_cont_list_cont_statusNew.text = if(shopL.get(adapterPosition).crm_status.isNullOrEmpty()) "None" else  shopL.get(adapterPosition).crm_status
                tv_row_cont_list_expctd_sales_value.text = if(shopL.get(adapterPosition).amount.isNullOrEmpty()) "0.00" else  shopL.get(adapterPosition).amount
                tv_row_cont_list_cont_number_whatsapp.text = if(shopL.get(adapterPosition).whatsappNoForCustomer.isNullOrEmpty()) "None" else shopL.get(adapterPosition).whatsappNoForCustomer
                tv_row_cont_list_cont_number_whatsappNew.text = if(shopL.get(adapterPosition).whatsappNoForCustomer.isNullOrEmpty()) "None" else shopL.get(adapterPosition).whatsappNoForCustomer
                tv_row_cont_list_cont_number_email.text = if(shopL.get(adapterPosition).ownerEmailId.isNullOrEmpty()) "None" else shopL.get(adapterPosition).ownerEmailId
                tv_row_cont_list_cont_number_emailNew.text = if(shopL.get(adapterPosition).ownerEmailId.isNullOrEmpty()) "None" else shopL.get(adapterPosition).ownerEmailId
                tv_row_cont_list_added_addition_source.text = if(shopL.get(adapterPosition).crm_saved_from.isNullOrEmpty()) "None" else shopL.get(adapterPosition).crm_saved_from

                //begin mantis id 0027389 AdditionalinfoRequiredforContactListing functionality Puja 23-04-2024 v4.0.6
                if (Pref.AdditionalinfoRequiredforContactListing){
                    ll_headView.visibility = View.VISIBLE
                    ll_goneView.visibility = View.GONE
                }else{
                    ll_goneView.visibility = View.VISIBLE
                    ll_headView.visibility = View.GONE
                }
                //end mantis id 0027389 AdditionalinfoRequiredforContactListing functionality Puja 23-04-2024 v4.0.6


                if(Pref.ContactAddresswithGeofence){
                    ll_row_contact_list_update_addr.visibility = View.VISIBLE
                    ll_row_contact_list_will_direction.visibility = View.VISIBLE
                }else{
                    ll_row_contact_list_update_addr.visibility = View.GONE
                    ll_row_contact_list_will_direction.visibility = View.GONE
                }


                if (tv_row_cont_list_cont_number.text.contains(searchText)) {
                    val startPos: Int = tv_row_cont_list_cont_number.text.indexOf(searchText)
                    val endPos: Int = startPos + searchText.length
                    val spanString =
                        Spannable.Factory.getInstance().newSpannable(tv_row_cont_list_cont_number.text)
                    spanString.setSpan(
                        ForegroundColorSpan(getResources().getColor(R.color.search_text)),
                        startPos,
                        endPos,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tv_row_cont_list_cont_number.setText(spanString)
                }
                //code start by Puja
               /* iv_row_cont_list_cont_number_whatsapp.setOnClickListener {
                    if(!shopL.get(adapterPosition).whatsappNoForCustomer.isNullOrEmpty()){
                        if(!shopL.get(adapterPosition).whatsappNoForCustomer.equals("")){
                            listner.onWhatsClick(shopL.get(adapterPosition))
                        }
                    }
                }*/

                ll_view_whatsapp.setOnClickListener {
                    //if(!shopL.get(adapterPosition).whatsappNoForCustomer.isNullOrEmpty()){
                    // if(!shopL.get(adapterPosition).whatsappNoForCustomer.equals("")){
                    if (shopL.get(adapterPosition).whatsappNoForCustomer.isNullOrEmpty()){
                        listner.onWhatsClick(shopL.get(adapterPosition))
                    }else{
                        listner.onWhatsClick(shopL.get(adapterPosition))
                    }

                       // }
                    //}
                }
                //begin mantis id 0027389 AdditionalinfoRequiredforContactListing functionality Puja 24-04-2024 v4.0.6
                ll_view_whatsappNew.setOnClickListener {
                    //if(!shopL.get(adapterPosition).whatsappNoForCustomer.isNullOrEmpty()){
                    // if(!shopL.get(adapterPosition).whatsappNoForCustomer.equals("")){
                    if (shopL.get(adapterPosition).whatsappNoForCustomer.isNullOrEmpty()){
                        listner.onWhatsClick(shopL.get(adapterPosition))
                    }else{
                        listner.onWhatsClick(shopL.get(adapterPosition))
                    }
                    //end mantis id 0027389 AdditionalinfoRequiredforContactListing functionality Puja 24-04-2024 v4.0.6

                    // }
                    //}
                }

                //code end by Puja
                ll_conatctsEmail.setOnClickListener {
                    try {
                        if(!shopL.get(adapterPosition).ownerEmailId.equals("")){
                            listner.onEmailClick(shopL.get(adapterPosition))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                ll_conatctsEmailNew.setOnClickListener {
                    try {
                        if(!shopL.get(adapterPosition).ownerEmailId.equals("")){
                            listner.onEmailClick(shopL.get(adapterPosition))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                iv_row_cont_list_cont_number.setOnClickListener {
                    listner.onCallClick(shopL.get(adapterPosition))
                }
                //Code start By Puja
                ll_view_contact_number.setOnClickListener {
                    listner.onCallClick(shopL.get(adapterPosition))
                }
                ll_view_contact_numberNew.setOnClickListener {
                    listner.onCallClick(shopL.get(adapterPosition))
                }
                //code end by puja
                iv_row_cont_list_cont_number_info.setOnClickListener {
                    listner.onInfoClick(shopL.get(adapterPosition))
                }
                iv_row_cont_list_cont_number_infoNew.setOnClickListener {
                    listner.onInfoClick(shopL.get(adapterPosition))
                }
                ll_row_contact_addr_root.setOnClickListener {
                    if(Pref.ContactAddresswithGeofence){
                        try{
                            var intentGmap: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${shopL.get(adapterPosition).shopLat},${shopL.get(adapterPosition).shopLong}&mode=1"))
                            intentGmap.setPackage("com.google.android.apps.maps")
                            if(intentGmap.resolveActivity(mContext.packageManager) !=null){
                                mContext.startActivity(intentGmap)
                            }
                        }catch (ex:Exception){
                            ex.printStackTrace()
                        }
                    }
                }

                if(shopL.get(adapterPosition).isUploaded){
                    iv_row_contact_sync_unsync.setImageResource(R.drawable.ic_registered_shop_sync)
                }else{
                    iv_row_contact_sync_unsync.setImageResource(R.drawable.ic_registered_shop_not_sync)
                }
                iv_row_contact_sync_unsync.setOnClickListener {
                    if(!shopL.get(adapterPosition).isUploaded){
                        listner.onSyncUnsyncClick(shopL.get(adapterPosition))
                    }
                }
                iv_row_contact_edit.setOnClickListener {
                    listner.onEditClick(shopL.get(adapterPosition))
                }

                if(Pref.willActivityShow){
                    ll_row_contact_list_will_activity_show.visibility = View.VISIBLE
                }else{
                    ll_row_contact_list_will_activity_show.visibility = View.GONE
                }

                ll_row_contact_list_will_activity_show.setOnClickListener{
                    listner.onActivityClick(shopL.get(adapterPosition))
                }
                ll_row_contact_list_update_addr.setOnClickListener {
                    listner.onUpdateAddrClick(shopL.get(adapterPosition))
                }

                ll_row_contact_list_will_direction.setOnClickListener {
                    listner.onDirectionClick(shopL.get(adapterPosition))
                }

                iv_row_contact_share.setOnClickListener {
                    listner.onDtlsShareClick(shopL.get(adapterPosition))
                }
                ll_row_contact_list_auto_activity.setOnClickListener {
                    listner.onAutoActivityClick(shopL.get(adapterPosition))
                }

                ll_row_contact_list_order_show.setOnClickListener {
                    listner.onOrderClick(shopL.get(adapterPosition))
                }

                if (Pref.isOrderShow) {
                    ll_row_contact_list_order_show.visibility = View.VISIBLE
                } else {
                    ll_row_contact_list_order_show.visibility = View.GONE
                }


                if (Pref.IsShowCRMOpportunity){
                    ll_row_opportunity_show.visibility =View.VISIBLE
                }else{
                    ll_row_opportunity_show.visibility =View.GONE
                }

                ll_row_opportunity_show.setOnClickListener {
                    listner.onOpportunityClick(shopL.get(adapterPosition))

                if(Pref.IsCRMEditEnable){
                    iv_row_contact_edit.visibility = View.VISIBLE
                }else{
                    iv_row_contact_edit.visibility = View.GONE
                }
            }
                ll_souce.setOnClickListener {
                    listner.onSourceUpdateClick(shopL.get(adapterPosition))
                }
                ll_stage_root.setOnClickListener {
                    listner.onStageUpdateClick(shopL.get(adapterPosition))
                }
                ll_stage_1.setOnClickListener {
                    listner.onStageUpdateClick(shopL.get(adapterPosition))
                }
                ll_status_root.setOnClickListener {
                    listner.onStatusUpdateClick(shopL.get(adapterPosition))
                }
                ll_contacStatus.setOnClickListener {
                    listner.onStatusUpdateClick(shopL.get(adapterPosition))
                }
                ll_assignedTo.setOnClickListener {
                    listner.onAssignToUpdateClick(shopL.get(adapterPosition))
                }
        }
    }
    }

    interface onClick{
        fun onCallClick(obj:AddShopDBModelEntity)
        fun onWhatsClick(obj:AddShopDBModelEntity)
        fun onEmailClick(obj:AddShopDBModelEntity)
        fun onInfoClick(obj:AddShopDBModelEntity)
        fun onSyncUnsyncClick(obj:AddShopDBModelEntity)
        fun onEditClick(obj:AddShopDBModelEntity)
        fun onUpdateStatusClick(obj:AddShopDBModelEntity)
        fun onDtlsShareClick(obj:AddShopDBModelEntity)
        fun onAutoActivityClick(obj:AddShopDBModelEntity)
        fun onDirectionClick(obj:AddShopDBModelEntity)
        fun onActivityClick(obj:AddShopDBModelEntity)
        fun onUpdateAddrClick(obj:AddShopDBModelEntity)
        fun onOrderClick(obj:AddShopDBModelEntity)
        fun onOpportunityClick(obj:AddShopDBModelEntity)
        fun onSourceUpdateClick(obj:AddShopDBModelEntity)
        fun onStageUpdateClick(obj:AddShopDBModelEntity)
        fun onStatusUpdateClick(obj:AddShopDBModelEntity)
        fun onAssignToUpdateClick(obj:AddShopDBModelEntity)
    }

}
