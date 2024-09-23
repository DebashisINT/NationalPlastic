package com.breezefieldnationalplastic.features.member.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.domain.NewOrderGenderEntity
import com.breezefieldnationalplastic.app.domain.NewOrderProductEntity
import com.breezefieldnationalplastic.features.NewQuotation.interfaces.SalesmanOnClick
import com.breezefieldnationalplastic.features.NewQuotation.interfaces.TaxOnclick
import com.breezefieldnationalplastic.features.member.CustomerOnClick
import com.breezefieldnationalplastic.features.member.model.CustomerDataModel
import com.breezefieldnationalplastic.features.member.model.TeamListDataModel
import com.breezefieldnationalplastic.features.viewAllOrder.interf.GenderListOnClick
import com.breezefieldnationalplastic.features.viewAllOrder.presentation.ProductListNewOrderAdapter
import kotlinx.android.synthetic.main.exp_popup_window_list_item.view.*
import kotlinx.android.synthetic.main.row_dialog_new_order_gender.view.*
import kotlinx.android.synthetic.main.row_dialog_new_order_gender.view.tv_row_dialog_new_order_gender
import kotlinx.android.synthetic.main.row_dialog_tax.view.*
// created 1.0 CustomerListDialog AppV 4.0.6 mantis 25644 search issue on pjpadded customer
class Customer2Adapter(private var context:Context, private var msalesmanList: ArrayList<CustomerDataModel>, private val listner: CustomerOnClick):
  RecyclerView.Adapter<Customer2Adapter.ViewHolder>(), Filterable {
    private var arrayList_bean: ArrayList<CustomerDataModel>? = ArrayList()
    private var arrayList_product: ArrayList<CustomerDataModel>? = ArrayList()
    private var valueFilter: Customer2Adapter.ValueFilter? = null

    init {
        arrayList_bean?.addAll(msalesmanList)
        arrayList_product?.addAll(msalesmanList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.exp_popup_window_list_item,parent,false)
        val view = LayoutInflater.from(context).inflate(R.layout.row_dialog_tax,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList_product!!.size!!
    }

    override fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter as Customer2Adapter.ValueFilter
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null && constraint.length > 0) {
                val arrayList_filter: ArrayList<CustomerDataModel> = ArrayList()
                for (i in 0..msalesmanList!!.size-1) {
                    if (msalesmanList!!.get(i).cust_name!!.contains(constraint.toString(),ignoreCase = true)) {
                        val setGetSalesUserDetails = CustomerDataModel()
                        setGetSalesUserDetails.cust_name=msalesmanList!!.get(i).cust_name
                        setGetSalesUserDetails.cust_id=msalesmanList!!.get(i).cust_id
                        arrayList_filter.add(setGetSalesUserDetails)
                    }
                }
                filterResults.count = arrayList_filter!!.size
                filterResults.values = arrayList_filter
            } else {
                filterResults.count = arrayList_bean!!.size
                filterResults.values = arrayList_bean
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            arrayList_product = results.values as ArrayList<CustomerDataModel>
            notifyDataSetChanged()
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        holder.item_tv.text = arrayList_product!!.get(holder.adapterPosition).cust_name
//        holder.item_tv.setOnClickListener { listner?.OnClick(arrayList_product!!.get(holder.adapterPosition)) }

        holder.item_tv.text = arrayList_product!!.get(holder.adapterPosition).cust_name
        holder.cv_tax.setOnClickListener { listner?.OnClick(arrayList_product!!.get(holder.adapterPosition)) }


    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
//        val item_tv = itemView.list_item_tv
        val cv_tax = itemView.cv_tax
        val item_tv = itemView.tv_row_dialog_tax
    }

}

