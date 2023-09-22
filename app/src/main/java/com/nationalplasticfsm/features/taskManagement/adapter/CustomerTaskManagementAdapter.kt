package com.nationalplasticfsm.features.taskManagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.nationalplasticfsm.R
import com.nationalplasticfsm.features.lead.model.CustomerLeadList
import com.nationalplasticfsm.features.lead.model.TaskList
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_ShopNameTV
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_Shopaddress_TV
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_Shopcontact_no
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_ivImage
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_ll_update
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_tv_date
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_tv_email
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_tv_enqury_dts
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_tv_order_values
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_tv_productReq
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_tv_qty
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_tv_source
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_tv_status
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_tv_time
import kotlinx.android.synthetic.main.row_customer_lead_list.view.row_cutomer_lead_list_tv_uom
import kotlinx.android.synthetic.main.row_customer_task_list.view.row_cutomer_lead_list_tv_update

// create by saheli 05-05-2023 mantis 0026023
class CustomerTaskManagementAdapter(var mContext:Context, var list:ArrayList<TaskList>, private val listener: OnPendingLeadClickListener, private val getSize: (Int) -> Unit) :
   RecyclerView.Adapter<CustomerTaskManagementAdapter.CustomerLeadViewHolder>(), Filterable {
    private var mList: ArrayList<TaskList>? = null
    private var tempList: ArrayList<TaskList>? = null
    private var filterList: ArrayList<TaskList>? = null

    init {
        mList = ArrayList()
        tempList = ArrayList()
        filterList = ArrayList()

        mList?.addAll(list)
        tempList?.addAll(list)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerLeadViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_customer_task_list, parent, false)
        return CustomerLeadViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun onBindViewHolder(holder: CustomerLeadViewHolder, position: Int) {

        val drawable = TextDrawable.builder().buildRoundRect(mList!!.get(position).task_name.trim().toUpperCase().take(1), ColorGenerator.MATERIAL.randomColor, 120)

        holder.imageShop.setImageDrawable(drawable)
        holder.shopName.text="Task Name -"+mList!!.get(position).task_name
        holder.shopSource.text=mList!!.get(position).priority_type_name
        holder.shopTime.text=mList!!.get(position).due_time
        holder.shopDate.text=mList!!.get(position).due_date

        holder.enquiry_dtls.text=mList!!.get(position).task_details
        if(mList!!.get(position).status.toUpperCase().equals("PENDING")){
            holder.status.text="Pending"
        }else{
            holder.status.text=mList!!.get(position).status
        }

        holder.iv_activityTv.setOnClickListener {listener.onActivityClick(mList!!.get(holder.adapterPosition))  }


    }

    inner class CustomerLeadViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var imageShop = itemView.row_cutomer_lead_list_ivImage
        var updateLL = itemView.row_cutomer_lead_list_ll_update
        var shopName = itemView.row_cutomer_lead_list_ShopNameTV
        var shopAdd = itemView.row_cutomer_lead_list_Shopaddress_TV
        var shopPhone = itemView.row_cutomer_lead_list_Shopcontact_no
        var shopSource= itemView.row_cutomer_lead_list_tv_source
        var shopTime = itemView.row_cutomer_lead_list_tv_time
        var shopDate = itemView.row_cutomer_lead_list_tv_date
        var email = itemView.row_cutomer_lead_list_tv_email
        var productReq = itemView.row_cutomer_lead_list_tv_productReq
        var qty = itemView.row_cutomer_lead_list_tv_qty
        var uom = itemView.row_cutomer_lead_list_tv_uom
        var order_values = itemView.row_cutomer_lead_list_tv_order_values
        var enquiry_dtls = itemView.row_cutomer_lead_list_tv_enqury_dts
        var status = itemView.row_cutomer_lead_list_tv_status

//        var iv_activity = itemView.row_cutomer_lead_list_iv_update
        var iv_activityTv = itemView.row_cutomer_lead_list_tv_update



    }

    override fun getFilter(): Filter {
        return SearchFilter()
    }

    inner class SearchFilter : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()

            filterList?.clear()

            tempList?.indices!!
                    .filter { tempList?.get(it)?.task_name?.toLowerCase()?.contains(p0?.toString()?.toLowerCase()!!)!! ||
                            tempList?.get(it)?.task_details?.toLowerCase()?.contains(p0?.toString()?.toLowerCase()!!)!! ||
                            tempList?.get(it)?.status?.toLowerCase()?.contains(p0?.toString()?.toLowerCase()!!)!! }

                    .forEach { filterList?.add(tempList?.get(it)!!) }

            results.values = filterList
            results.count = filterList?.size!!

            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {

            try {
                filterList = results?.values as ArrayList<TaskList>?
                mList?.clear()
                val hashSet = HashSet<String>()
                if (filterList != null) {

                    filterList?.indices!!
                            .filter { hashSet.add(filterList?.get(it)?.task_name!!) }
                            .forEach { mList?.add(filterList?.get(it)!!) }

                    getSize(mList?.size!!)

                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshList(list: ArrayList<TaskList>) {
        mList?.clear()
        mList?.addAll(list)

        tempList?.clear()
        tempList?.addAll(list)

        if (filterList == null)
            filterList = ArrayList()
        filterList?.clear()

        notifyDataSetChanged()
    }


    interface OnPendingLeadClickListener {
        fun onActivityClick(obj:TaskList)
        fun onPhoneClick(obj:TaskList)
    }


}