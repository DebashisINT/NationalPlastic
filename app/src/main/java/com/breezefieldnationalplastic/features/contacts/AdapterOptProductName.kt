package com.breezefieldnationalplastic.features.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import kotlinx.android.synthetic.main.row_cont_select.view.cb_row_cont_sel_name
import kotlinx.android.synthetic.main.row_cont_select.view.tv_row_cont_sel_name
import kotlinx.android.synthetic.main.row_dialog_tax_multiple.view.cb_row_row_dialog_tax_multiple

class AdapterOptProductName(var mContext:Context, var productList:ArrayList<ProductDtls>, var listner:onClick, private val getSize: (Int) -> Unit):
    RecyclerView.Adapter<AdapterOptProductName.ContactNameViewHolder>(), Filterable {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }

    private var mList: ArrayList<ProductDtls>? = null
    private var tempList: ArrayList<ProductDtls>? = null
    private var filterList: ArrayList<ProductDtls>? = null

    init {
        mList = ArrayList()
        tempList = ArrayList()
        filterList = ArrayList()

        mList?.addAll(productList)
        tempList?.addAll(productList)
    }
   /* public fun selectAll() {
        println("tag_check_cb select all call")
        for(i in 0..mList!!.size-1){
            if(mList!!.get(i).isTick == false){
                mList!!.get(i).isTick = true

                if(mList!!.get(i).isTick){
                    listner.onTickUntick(mList!!.get(i),true)
                }else{
                    listner.onTickUntick(mList!!.get(i),false)
                }
            }
        }
        notifyDataSetChanged()
    }
    public fun deselectAll() {
        for(i in 0..mList!!.size-1){
            mList!!.get(i).isTick = false

            if(mList!!.get(i).isTick){
                listner.onTickUntick(mList!!.get(i),true)
            }else{
                listner.onTickUntick(mList!!.get(i),false)
            }
        }
        notifyDataSetChanged()
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactNameViewHolder {
        val v = layoutInflater.inflate(R.layout.row_cont_select, parent, false)
        return ContactNameViewHolder(v)
    }

    override fun onBindViewHolder(holder: ContactNameViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
       return mList?.size!!
    }

    inner class ContactNameViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_cont_sel_name.text = mList?.get(adapterPosition)?.product_name
                if(mList?.get(adapterPosition)!!.isTick){
                    cb_row_cont_sel_name.isChecked = true
                }else{
                    cb_row_cont_sel_name.isChecked = false
                }
                cb_row_cont_sel_name.setOnClickListener {
                    if(mList!!.get(adapterPosition).isTick){
                        mList!!.get(adapterPosition).isTick = false
                        cb_row_cont_sel_name.isChecked = false
                        listner.onTickUntick(mList!!.get(adapterPosition),false)
                    }else{
                        mList!!.get(adapterPosition).isTick = true
                        cb_row_cont_sel_name.isChecked = true
                        listner.onTickUntick(mList!!.get(adapterPosition),true)
                    }
                }

                tv_row_cont_sel_name.setOnClickListener {
                    if(mList!!.get(adapterPosition).isTick){
                        mList!!.get(adapterPosition).isTick = false
                        cb_row_cont_sel_name.isChecked = false
                        listner.onTickUntick(mList!!.get(adapterPosition),false)
                    }else{
                        mList!!.get(adapterPosition).isTick = true
                        cb_row_cont_sel_name.isChecked = true
                        listner.onTickUntick(mList!!.get(adapterPosition),true)
                    }
                }

            }
        }
    }

    override fun getFilter(): Filter {
        return SearchFilter()
    }

    inner class SearchFilter : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()

            filterList?.clear()

            tempList?.indices!!
                .filter { tempList?.get(it)?.product_name?.toLowerCase()?.contains(p0?.toString()?.toLowerCase()!!)!! }
                .forEach { filterList?.add(tempList?.get(it)!!) }

            results.values = filterList
            results.count = filterList?.size!!

            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {

            try {
                filterList = results?.values as ArrayList<ProductDtls>?
                mList?.clear()
                val hashSet = HashSet<String>()
                if (filterList != null) {
                    filterList?.forEach {
                        mList?.add(it)
                    }
                    getSize(mList?.size!!)

                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshList(list: ArrayList<ProductDtls>) {
        mList?.clear()
        mList?.addAll(list)

        tempList?.clear()
        tempList?.addAll(list)

        if (filterList == null)
            filterList = ArrayList()
        filterList?.clear()

        notifyDataSetChanged()
    }

    interface onClick{
        fun onTickUntick(obj:ProductDtls,isTick: Boolean)
    }
}