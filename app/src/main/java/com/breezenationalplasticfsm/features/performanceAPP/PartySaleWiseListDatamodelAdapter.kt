package com.breezenationalplasticfsm.features.performanceAPP

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import kotlinx.android.synthetic.main.row_dialog_tax_multiple.view.*

/**
 * Created by Saheli on 17-04-2023 v 4.0.8 mantis 0025860.
 */
class PartySaleWiseListDatamodelAdapter(private var context:Context, private var mshopTypeList: ArrayList<AddShopDBModelEntity>, private var finalL:ArrayList<PerformDataClass>,private val listner: OnClick):
  RecyclerView.Adapter<PartySaleWiseListDatamodelAdapter.GenderListViewHolder>(), Filterable {
    private var arrayList_bean: ArrayList<AddShopDBModelEntity>? = ArrayList()
    private var arrayList_product: ArrayList<AddShopDBModelEntity>? = ArrayList()
    private var valueFilter: PartySaleWiseListDatamodelAdapter.ValueFilter? = null

    init {
        arrayList_bean?.addAll(mshopTypeList)
        arrayList_product?.addAll(mshopTypeList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_dialog_tax_multiple,parent,false)
        return GenderListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList_product!!.size!!
    }

    override fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter as PartySaleWiseListDatamodelAdapter.ValueFilter
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null && constraint.length > 0) {
                val arrayList_filter: ArrayList<AddShopDBModelEntity> = ArrayList()
                for (i in 0..mshopTypeList!!.size-1) {
                    if (mshopTypeList!!.get(i).shopName!!.contains(constraint.toString(),ignoreCase = true)) {
                        val setGetSalesUserDetails = AddShopDBModelEntity()
                        setGetSalesUserDetails.shopName=mshopTypeList!!.get(i).shopName
                        setGetSalesUserDetails.shop_id=mshopTypeList!!.get(i).shop_id
                        setGetSalesUserDetails.type=mshopTypeList!!.get(i).type
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
            arrayList_product = results.values as ArrayList<AddShopDBModelEntity>
            notifyDataSetChanged()
        }
    }

  override fun onBindViewHolder(holder: GenderListViewHolder, adapterPosition: Int) {
        holder.tv_party_Nm.text=finalL!!.get(holder.adapterPosition).shop_name!!

        if(finalL.get(holder.adapterPosition).isChecked){
            holder.cb_tick.isChecked = true
        }else{
            holder.cb_tick.isChecked = false
        }

        holder.cb_tick.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                finalL.get(holder.adapterPosition).isChecked = true
                listner.onTickUntickView(mshopTypeList!!.get(adapterPosition),true)
            }else{
                finalL.get(holder.adapterPosition).isChecked = false
                listner.onTickUntickView(mshopTypeList!!.get(adapterPosition),false)
            }
        }


    }

    inner class GenderListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tv_party_Nm = itemView.tv_row_dialog_tax
        val cb_tick = itemView.cb_row_row_dialog_tax_multiple
    }
    interface OnClick {
        fun onTickUntickView(obj: AddShopDBModelEntity,isTick:Boolean)
    }



}

