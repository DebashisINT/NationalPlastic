package com.breezenationalplasticfsm.features.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.domain.SchedulerContactEntity
import com.breezenationalplasticfsm.app.domain.SchedulerDateTimeEntity
import com.breezenationalplasticfsm.app.domain.SchedulerMasterEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_cont_list_cont_number_email
import kotlinx.android.synthetic.main.row_contact_list.view.iv_row_cont_list_cont_number_whatsapp
import kotlinx.android.synthetic.main.row_schedule_list.view.iv_row_sch_list_delete
import kotlinx.android.synthetic.main.row_schedule_list.view.iv_row_schedule_list_content_info
import kotlinx.android.synthetic.main.row_schedule_list.view.iv_row_scheduler_edit
import kotlinx.android.synthetic.main.row_schedule_list.view.ll_row_schedul_selected_contacts
import kotlinx.android.synthetic.main.row_schedule_list.view.ll_row_schedul_selected_template_date
import kotlinx.android.synthetic.main.row_schedule_list.view.tv_row_schedul_list_name
import kotlinx.android.synthetic.main.row_schedule_list.view.tv_row_schedul_selected_template
import kotlinx.android.synthetic.main.row_schedule_list.view.tv_row_schedul_selected_template_contact
import kotlinx.android.synthetic.main.row_schedule_list.view.tv_row_schedul_selected_template_contact_modify
import kotlinx.android.synthetic.main.row_schedule_list.view.tv_row_schedul_selected_template_date
import kotlinx.android.synthetic.main.row_schedule_list.view.tv_row_schedul_selected_template_date_modify
import kotlinx.android.synthetic.main.row_schedule_list.view.tv_row_schedul_selected_template_hourminute
import kotlinx.android.synthetic.main.row_schedule_list.view.tv_row_schedul_selected_template_mode
import kotlinx.android.synthetic.main.row_schedule_list.view.tv_row_schedul_selected_template_rule
import kotlinx.android.synthetic.main.row_schedule_list.view.tv_row_schedul_template_content


class AdapterSchedulerList(var mContext:Context, var schedulerL:List<SchedulerMasterEntity>, var searchText:String, var listner:onClick):
    RecyclerView.Adapter<AdapterSchedulerList.ContactListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_schedule_list,parent,false)
        return ContactListViewHolder(v)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return schedulerL.size
    }

    fun updateAdapter(schedulerL: List<SchedulerMasterEntity>) {

        this.schedulerL = schedulerL
        notifyDataSetChanged()
    }

    inner class ContactListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        @SuppressLint("SuspiciousIndentation")
        fun bindItems(){
            itemView.apply {
                tv_row_schedul_list_name.text= schedulerL.get(adapterPosition).scheduler_name!!.toString().trim()
                tv_row_schedul_template_content.setText(schedulerL.get(adapterPosition).template_content.trim())
                tv_row_schedul_selected_template.text = schedulerL.get(adapterPosition).select_template.trim()
                tv_row_schedul_selected_template_mode.text = schedulerL.get(adapterPosition).select_mode.trim()
                tv_row_schedul_selected_template_rule.text = schedulerL.get(adapterPosition).select_rule.trim()

               if (!schedulerL.get(adapterPosition).save_modify_date_time!!.toString().trim().equals("")) {
                   var strParts =
                       schedulerL.get(adapterPosition).save_modify_date_time!!.toString().trim()
                           .split(" ")
                   var modifydate: String = strParts[0]
                   var modifytime: String = strParts[1]

                   tv_row_schedul_selected_template_date_modify.text = AppUtils.getFormatedDateNew(
                           modifydate.trim(), "yyyy-mm-dd", "dd-mm-yyyy")
                   tv_row_schedul_selected_template_contact_modify.text = modifytime.trim()

               }else{
                   tv_row_schedul_selected_template_date_modify.text = ""
                   tv_row_schedul_selected_template_contact_modify.text = ""
               }

                var scheduleContactL = AppDatabase.getDBInstance()!!.schedulerContactDao().getContDtlsBySchID(schedulerL.get(adapterPosition).scheduler_id) as ArrayList<SchedulerContactEntity>
                try {
                    if (scheduleContactL.size == 1) {
                        tv_row_schedul_selected_template_contact.text =
                            scheduleContactL.get(0).select_contact.trim()
                    } else {
                        tv_row_schedul_selected_template_contact.text = "Multiple"
                        tv_row_schedul_selected_template_contact.setTextColor(context.resources.getColor(R.color.link_blue))

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                try {
                    var scheduleTimeL = AppDatabase.getDBInstance()!!.schedulerDateTimeDao()
                        .getAll(schedulerL.get(adapterPosition).scheduler_id) as ArrayList<SchedulerDateTimeEntity>
                    if (scheduleTimeL.size == 0) {
                       // tv_row_schedul_selected_template_hourminute.text = "None"
                        var strParts = schedulerL.get(adapterPosition).save_date_time!!.toString().trim() . split(" ")
                        var strFirstStringpart1 :String = strParts[0]
                        var strFirstStringpart2 :String = strParts[1]
                        tv_row_schedul_selected_template_hourminute.text = strFirstStringpart2.trim()

                    } else {
                        for(i in 0..scheduleTimeL.size-1) {
                            tv_row_schedul_selected_template_hourminute.text =
                                scheduleTimeL.get(i).select_hour + " " + scheduleTimeL.get(
                                    i
                                ).select_minute.toString().trim()

                        }
                    }
                }
                catch (e:Exception){
                    e.printStackTrace()
                }

                var scheduleDateL = AppDatabase.getDBInstance()!!.schedulerDateTimeDao().getAll(schedulerL.get(adapterPosition).scheduler_id) as ArrayList<SchedulerDateTimeEntity>
                try {
                    //new code by Suman
                    if(scheduleDateL.size==0){
                        var strParts = schedulerL.get(adapterPosition).save_date_time!!.toString().trim() . split(" ")
                        var strFirstStringpart1 :String = strParts[0]
                        var strFirstStringpart2 :String = strParts[1]
                        tv_row_schedul_selected_template_date.text = AppUtils.getFormatedDateNew(
                            strFirstStringpart1.trim(), "yyyy-mm-dd", "dd-mm-yyyy")
                    }else if(scheduleDateL.size==1){
                        tv_row_schedul_selected_template_date.text = AppUtils.getFormatedDateNew(
                            scheduleDateL.get(0).select_date.trim(), "yyyy-mm-dd", "dd-mm-yyyy")
                    }else{
                        tv_row_schedul_selected_template_date.text = "Multiple"
                        tv_row_schedul_selected_template_date.setTextColor(context.resources.getColor(R.color.link_blue))

                    }
                    //new code
                }catch (e:Exception){
                    e.printStackTrace()
                }

                if (schedulerL.get(adapterPosition).select_mode.toString().trim().equals("WhatsApp")){
                    iv_row_cont_list_cont_number_whatsapp.setOnClickListener {
                        listner.onWhatsClick(schedulerL.get(adapterPosition))
                    }
                }
                else if (schedulerL.get(adapterPosition).select_mode.toString().trim().equals("Email")){
                    iv_row_cont_list_cont_number_email.setOnClickListener {
                            listner.onEmailClick(schedulerL.get(adapterPosition))
                    }
                }

                ll_row_schedul_selected_template_date.setOnClickListener {
                    if(scheduleDateL.size>0){
                        listner.onSelectedDateInfoClick(schedulerL.get(adapterPosition))
                    }
                    else{
                        listner.onSelectedDateInfoClickForManual(schedulerL.get(adapterPosition))
                    }
                }
                ll_row_schedul_selected_contacts.setOnClickListener {
                    if(scheduleContactL.size>1){
                        listner.onSelectedContactClick(schedulerL.get(adapterPosition))
                    }
                }

                iv_row_schedule_list_content_info.setOnClickListener {
                    listner.onSchedulerContentInfoClick(schedulerL.get(adapterPosition))
                }
                iv_row_sch_list_delete.setOnClickListener {
                    listner.onDeleteClick(schedulerL.get(adapterPosition))
                }
                iv_row_scheduler_edit.setOnClickListener {
                    listner.onEditClick(schedulerL.get(adapterPosition))
                }
            }
        }
    }

    interface onClick{
        fun onWhatsClick(obj:SchedulerMasterEntity)
        fun onEmailClick(obj:SchedulerMasterEntity)
        fun onSelectedDateInfoClick(obj:SchedulerMasterEntity)
        fun onSelectedDateInfoClickForManual(obj:SchedulerMasterEntity)
        fun onSelectedContactClick(obj:SchedulerMasterEntity)
        fun onSchedulerContentInfoClick(obj:SchedulerMasterEntity)
        fun onDeleteClick(obj:SchedulerMasterEntity)
        fun onEditClick(obj:SchedulerMasterEntity)
    }

}