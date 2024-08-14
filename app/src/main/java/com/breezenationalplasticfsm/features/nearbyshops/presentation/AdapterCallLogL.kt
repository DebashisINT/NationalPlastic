package com.breezenationalplasticfsm.features.nearbyshops.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.domain.CallHisEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import kotlinx.android.synthetic.main.row_call_log_list.view.iv_row_call_log_his_type
import kotlinx.android.synthetic.main.row_call_log_list.view.tv_row_call_log_his_call_status
import kotlinx.android.synthetic.main.row_call_log_list.view.tv_row_call_log_his_date
import kotlinx.android.synthetic.main.row_call_log_list.view.tv_row_call_log_his_duration
import kotlinx.android.synthetic.main.row_call_log_list.view.tv_row_call_log_his_sync_status
import kotlinx.android.synthetic.main.row_call_log_list.view.tv_row_call_log_his_time
import kotlinx.android.synthetic.main.row_churn_shop_l.view.iv_tag1
import kotlinx.android.synthetic.main.row_ord_opti_product_list.view.iv_row_ord_opti_product_list_add_img
import kotlinx.android.synthetic.main.row_ord_opti_product_list.view.ll_row_ord_opti_product_list_add_text_root

class AdapterCallLogL(var mContext: Context,var callL:ArrayList<CallHisEntity>,var isSyncShow:Boolean = true,var listner:onClick):RecyclerView.Adapter<AdapterCallLogL.CallLogLViewHolder>() {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogLViewHolder {
        val v = layoutInflater.inflate(R.layout.row_call_log_list, parent, false)
        return CallLogLViewHolder(v)
    }

    override fun onBindViewHolder(holder: CallLogLViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return callL.size
    }

    inner class CallLogLViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_call_log_his_date.text = AppUtils.getFormatedDateNew(callL.get(adapterPosition).call_date,"yyyy-mm-dd","dd-mm-yyyy")
                tv_row_call_log_his_time.text = callL.get(adapterPosition).call_time
                if(callL.get(adapterPosition).call_type.equals("OUTGOING",ignoreCase = true)){
                    //iv_row_call_log_his_type.setBackground(getResources().getDrawable(R.drawable.ic_outgoing_call));
                    iv_row_call_log_his_type.setBackground(getResources().getDrawable(R.drawable.ic_outgoing_nw));
                    //iv_row_call_log_his_type.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8AC659")))
                    tv_row_call_log_his_call_status.text = "Outgoing"
                }else if(callL.get(adapterPosition).call_type.equals("INCOMING",ignoreCase = true)){
                    //iv_row_call_log_his_type.setBackground(getResources().getDrawable(R.drawable.ic_incomming_call));
                    iv_row_call_log_his_type.setBackground(getResources().getDrawable(R.drawable.ic_incoming_nw));
                    //iv_row_call_log_his_type.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57a518")))
                    tv_row_call_log_his_call_status.text = "Incoming"
                }else if(callL.get(adapterPosition).call_type.equals("MISSED",ignoreCase = true)){
                    //iv_row_call_log_his_type.setBackground(getResources().getDrawable(R.drawable.ic_call_missed));
                    iv_row_call_log_his_type.setBackground(getResources().getDrawable(R.drawable.ic_missed_nw));
                    //iv_row_call_log_his_type.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8424e")))
                    tv_row_call_log_his_call_status.text = "Missed"
                }
                if(callL.get(adapterPosition).isUploaded){
                    tv_row_call_log_his_sync_status.setImageResource(R.drawable.ic_registered_shop_sync)
                }else{
                    tv_row_call_log_his_sync_status.setImageResource(R.drawable.ic_registered_shop_not_sync)
                }
                tv_row_call_log_his_duration.text = AppUtils.getMMSSfromSeconds(callL.get(adapterPosition).call_duration_sec.toInt())

                if(isSyncShow==false){
                    tv_row_call_log_his_sync_status.visibility = View.GONE
                    tv_row_call_log_his_call_status.visibility = View.VISIBLE
                }

                tv_row_call_log_his_sync_status.setOnClickListener {
                    if(!callL.get(adapterPosition).isUploaded){
                        listner.onSyncClick(callL.get(adapterPosition))
                    }
                }

            }
        }
    }

    interface onClick{
        fun onSyncClick(obj:CallHisEntity)
    }

}