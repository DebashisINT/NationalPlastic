package com.breezefieldnationalplastic.features.performanceAPP

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
import com.breezefieldnationalplastic.app.domain.ShopTypeEntity
import com.breezefieldnationalplastic.features.NewQuotation.interfaces.SalesmanOnClick
import com.breezefieldnationalplastic.features.NewQuotation.interfaces.TaxOnclick
import com.breezefieldnationalplastic.features.member.model.TeamListDataModel
import com.breezefieldnationalplastic.features.viewAllOrder.interf.GenderListOnClick
import com.breezefieldnationalplastic.features.viewAllOrder.presentation.ProductListNewOrderAdapter
import kotlinx.android.synthetic.main.row_dialog_new_order_gender.view.*
import kotlinx.android.synthetic.main.row_dialog_new_order_gender.view.tv_row_dialog_new_order_gender
import kotlinx.android.synthetic.main.row_dialog_tax.view.*

/**
 * Created by Saheli on 11-03-2023 v 4.0.8 mantis 0025860.
 */
class ShopTypeListAdapter(private var context:Context, private var mshopTypeList: ArrayList<ShopTypeEntity>, private val listner: OnClick):
  RecyclerView.Adapter<ShopTypeListAdapter.GenderListViewHolder>(), Filterable {
    private var arrayList_bean: ArrayList<ShopTypeEntity>? = ArrayList()
    private var arrayList_product: ArrayList<ShopTypeEntity>? = ArrayList()
    private var valueFilter: ShopTypeListAdapter.ValueFilter? = null

    init {
        arrayList_bean?.addAll(mshopTypeList)
        arrayList_product?.addAll(mshopTypeList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_dialog_tax,parent,false)
        return GenderListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList_product!!.size!!
    }

    override fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter as ShopTypeListAdapter.ValueFilter
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null && constraint.length > 0) {
                val arrayList_filter: ArrayList<ShopTypeEntity> = ArrayList()
                for (i in 0..mshopTypeList!!.size-1) {
                    if (mshopTypeList!!.get(i).shoptype_name!!.contains(constraint.toString(),ignoreCase = true)) {
                        val setGetSalesUserDetails = ShopTypeEntity()
                        setGetSalesUserDetails.shoptype_name=mshopTypeList!!.get(i).shoptype_name
                        setGetSalesUserDetails.shoptype_id=mshopTypeList!!.get(i).shoptype_id
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
            arrayList_product = results.values as ArrayList<ShopTypeEntity>
            notifyDataSetChanged()
        }
    }


    override fun onBindViewHolder(holder: GenderListViewHolder, position: Int) {
        holder.tv_tax.text=arrayList_product!!.get(position).shoptype_name!!
        holder.cv_tax.setOnClickListener { listner?.OnClick(arrayList_product!!.get(holder.adapterPosition)) }
    }

    inner class GenderListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tv_tax = itemView.tv_row_dialog_tax
        val cv_tax = itemView.cv_tax
    }
    interface OnClick {
        fun OnClick(obj: ShopTypeEntity)
    }

}

