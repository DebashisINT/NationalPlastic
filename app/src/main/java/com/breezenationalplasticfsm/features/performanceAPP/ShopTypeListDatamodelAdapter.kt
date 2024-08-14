package com.breezenationalplasticfsm.features.performanceAPP

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.features.nearbyshops.model.ShopTypeDataModel
import kotlinx.android.synthetic.main.row_dialog_tax.view.*

/**
 * Created by Saheli on 12-03-2023 v 4.0.8 mantis 0025860.
 */
class ShopTypeListDatamodelAdapter(private var context:Context, private var mshopTypeList: ArrayList<ShopTypeDataModel>, private val listner: OnClick):
  RecyclerView.Adapter<ShopTypeListDatamodelAdapter.GenderListViewHolder>(), Filterable {
    private var arrayList_bean: ArrayList<ShopTypeDataModel>? = ArrayList()
    private var arrayList_product: ArrayList<ShopTypeDataModel>? = ArrayList()
    private var valueFilter: ShopTypeListDatamodelAdapter.ValueFilter? = null

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
        return valueFilter as ShopTypeListDatamodelAdapter.ValueFilter
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null && constraint.length > 0) {
                val arrayList_filter: ArrayList<ShopTypeDataModel> = ArrayList()
                for (i in 0..mshopTypeList!!.size-1) {
                    if (mshopTypeList!!.get(i).shoptype_name!!.contains(constraint.toString(),ignoreCase = true)) {
                        val setGetSalesUserDetails = ShopTypeDataModel()
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
            arrayList_product = results.values as ArrayList<ShopTypeDataModel>
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
        fun OnClick(obj: ShopTypeDataModel)
    }

}

