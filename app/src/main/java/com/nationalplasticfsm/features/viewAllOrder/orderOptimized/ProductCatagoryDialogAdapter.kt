package com.nationalplasticfsm.features.viewAllOrder.orderOptimized

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.R
import com.nationalplasticfsm.features.viewAllOrder.model.color_list
import kotlinx.android.synthetic.main.row_dialog_new_order_color.view.*

class ProductCatagoryDialogAdapter(var context: Context, var list:List<CommonProductCatagory>, var listner: OrderOptiCatagoryOnClick):
    RecyclerView.Adapter<ProductCatagoryDialogAdapter.ProductCatagoryViewHolder>(), Filterable {

    private var arrayList_bean: ArrayList<CommonProductCatagory>? = ArrayList()
    private var arrayList_catagory: ArrayList<CommonProductCatagory>? = ArrayList()
    private var valueFilter: ValueFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCatagoryViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.row_dialog_new_order_color,parent,false)
        return ProductCatagoryViewHolder(view)
    }

    init {
        arrayList_bean?.addAll(list)
        arrayList_catagory?.addAll(list)
    }

    override fun getItemCount(): Int {
        return arrayList_catagory!!.size

    }



    override fun onBindViewHolder(holder: ProductCatagoryViewHolder, position: Int) {
        holder.tv_color.text=arrayList_catagory!!.get(position).name_sel
        holder.tv_color.setOnClickListener { listner?.catagoryListOnClick(arrayList_catagory!!.get(holder.adapterPosition!!)) }
    }

    inner class ProductCatagoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tv_color = itemView!!.tv_row_dialog_new_order_color
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
                val arrayList_filter: ArrayList<CommonProductCatagory> = ArrayList()
                for(i in 0..list.size-1){
                    if(list.get(i).name_sel.contains(constraint.toString(),ignoreCase = true))
                        arrayList_filter.add(list.get(i))
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
            arrayList_catagory = results.values as ArrayList<CommonProductCatagory>
            notifyDataSetChanged()
        }
    }

}