package com.breezenationalplasticfsm.features.addAttendence

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.domain.NewOrderProductEntity
import com.breezenationalplasticfsm.features.addAttendence.model.ReimbListModel
import com.breezenationalplasticfsm.features.addAttendence.model.ReimbListOnClick
import com.breezenationalplasticfsm.features.viewAllOrder.interf.ProductListNewOrderOnClick
import com.breezenationalplasticfsm.features.viewAllOrder.presentation.ProductListNewOrderAdapter
import kotlinx.android.synthetic.main.inflate_vehicle_log_type.view.iv_check
import kotlinx.android.synthetic.main.row_dialog_new_order_product.view.tv_row_product_new_order_product
import kotlinx.android.synthetic.main.row_reimb_check_select.view.iv_check_reimb
import kotlinx.android.synthetic.main.row_reimb_check_select.view.ll_reimb_type_main
import kotlinx.android.synthetic.main.row_reimb_check_select.view.tv_log_type_reimb

class ReimbursementListAdapter (var context: Context, var mList:ArrayList<ReimbListModel>, val listner: ReimbListOnClick):
    RecyclerView.Adapter<ReimbursementListAdapter.ReimbOrderViewHolder>(), Filterable {

    private var arrayList_bean: ArrayList<ReimbListModel>? = ArrayList()
    private var arrayList_product: ArrayList<ReimbListModel>? = ArrayList()
    private var valueFilter: ValueFilter? = null

    init {
        arrayList_bean?.addAll(mList)
        arrayList_product?.addAll(mList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReimbOrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_reimb_check_select,parent,false)
        return ReimbOrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList_product!!.size
    }

    override fun onBindViewHolder(holder: ReimbOrderViewHolder, position: Int) {
        holder.tvShow.text=arrayList_product?.get(holder.adapterPosition)?.visit_location
        holder.checkShow.isSelected = arrayList_product?.get(holder.adapterPosition)?.isSelected!!

        holder.llRoot.setOnClickListener{
            if (holder.checkShow.isSelected) {
                //itemView.iv_check.isSelected = false
                arrayList_product!![holder.adapterPosition].isSelected = false
            }else{
                arrayList_product!!.forEach {
                    it.isSelected = false
                }
                arrayList_product!!.get(holder.adapterPosition).isSelected = true
            }
            listner?.reimbOnClick(arrayList_product?.get(holder.adapterPosition)!!)
            notifyDataSetChanged()
        }
    }

    inner class ReimbOrderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvShow = itemView.tv_log_type_reimb
        val checkShow = itemView.iv_check_reimb
        val llRoot = itemView.ll_reimb_type_main
    }

    override fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter as ValueFilter
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null && constraint.length > 0) {
                val arrayList_filter: ArrayList<ReimbListModel> = ArrayList()
                for (i in 0..mList!!.size-1) {
                    if (mList!!.get(i).visit_location!!.contains(constraint.toString(),ignoreCase = true)) {
                        arrayList_filter.add(mList!!.get(i))
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
            arrayList_product = results.values as ArrayList<ReimbListModel>
            notifyDataSetChanged()
        }
    }
}