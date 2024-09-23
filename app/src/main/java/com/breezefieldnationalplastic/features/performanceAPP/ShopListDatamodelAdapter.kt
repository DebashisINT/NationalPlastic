package com.breezefieldnationalplastic.features.performanceAPP

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.features.nearbyshops.model.ShopData
import com.breezefieldnationalplastic.features.nearbyshops.model.ShopTypeDataModel
import kotlinx.android.synthetic.main.row_dialog_tax.view.*

/**
 * Created by Saheli on 12-03-2023 v 4.0.8 mantis 0025860.
 */
class ShopListDatamodelAdapter(private var context:Context, private var mshopTypeList: ArrayList<AddShopDBModelEntity>, private val listner: OnClick):
  RecyclerView.Adapter<ShopListDatamodelAdapter.GenderListViewHolder>(), Filterable {
    private var arrayList_bean: ArrayList<AddShopDBModelEntity>? = ArrayList()
    private var arrayList_product: ArrayList<AddShopDBModelEntity>? = ArrayList()
    private var valueFilter: ShopListDatamodelAdapter.ValueFilter? = null

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
        return valueFilter as ShopListDatamodelAdapter.ValueFilter
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null && constraint.length > 0) {
                val arrayList_filter: ArrayList<AddShopDBModelEntity> = ArrayList()
                for (i in 0..mshopTypeList!!.size-1) {
                    if (mshopTypeList!!.get(i).shopName!!.contains(constraint.toString(),ignoreCase = true)) {
                        //val setGetSalesUserDetails = AddShopDBModelEntity()
                        //setGetSalesUserDetails.shopName=mshopTypeList!!.get(i).shopName
                        //setGetSalesUserDetails.shop_id=mshopTypeList!!.get(i).shop_id
                        //arrayList_filter.add(setGetSalesUserDetails)
                        arrayList_filter.add(mshopTypeList!!.get(i))


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
            arrayList_product = results.values as ArrayList<AddShopDBModelEntity>
            notifyDataSetChanged()
        }
    }


    override fun onBindViewHolder(holder: GenderListViewHolder, position: Int) {
        holder.tv_tax.text=arrayList_product!!.get(position).shopName!!
        holder.cv_tax.setOnClickListener { listner?.OnClick(arrayList_product!!.get(holder.adapterPosition)) }
    }

    inner class GenderListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tv_tax = itemView.tv_row_dialog_tax
        val cv_tax = itemView.cv_tax
    }
    interface OnClick {
        fun OnClick(obj: AddShopDBModelEntity)
    }

}

