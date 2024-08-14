package com.breezenationalplasticfsm.features.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import kotlinx.android.synthetic.main.row_cont_select.view.cb_row_cont_sel_name
import kotlinx.android.synthetic.main.row_cont_select.view.tv_row_cont_sel_name
import kotlinx.android.synthetic.main.row_dialog_tax_multiple.view.cb_row_row_dialog_tax_multiple

class AdapterContactName(var mContext:Context,var customerList:ArrayList<ContactDtls>,var listner:onClick,private val getSize: (Int) -> Unit):
    RecyclerView.Adapter<AdapterContactName.ContactNameViewHolder>(), Filterable {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }

    private var mList: ArrayList<ContactDtls>? = null
    private var tempList: ArrayList<ContactDtls>? = null
    private var filterList: ArrayList<ContactDtls>? = null

    init {
        mList = ArrayList()
        tempList = ArrayList()
        filterList = ArrayList()

        mList?.addAll(customerList)
        tempList?.addAll(customerList)
    }
    
    public fun selectAll() {
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
          //  if(mList!!.get(i).isTick == true){

                mList!!.get(i).isTick = false

                if(mList!!.get(i).isTick){
                    listner.onTickUntick(mList!!.get(i),true)
                }else{
                    listner.onTickUntick(mList!!.get(i),false)
                }
          //  }
        }
        notifyDataSetChanged()
    }


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
                tv_row_cont_sel_name.text = mList?.get(adapterPosition)?.name +" ("+mList?.get(adapterPosition)?.number+")"
                if(mList?.get(adapterPosition)!!.isTick){
                    cb_row_cont_sel_name.isChecked = true
                }else{
                    cb_row_cont_sel_name.isChecked = false
                }
                /*cb_row_cont_sel_name.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked){
                        mList?.get(adapterPosition)?.isTick = true
                        listner.onTick(mList!!.get(adapterPosition))
                    }else{
                        mList?.get(adapterPosition)?.isTick = false
                        listner.onUnTick(mList!!.get(adapterPosition))
                    }
                }*/

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
                .filter { tempList?.get(it)?.name?.toLowerCase()?.contains(p0?.toString()?.toLowerCase()!!)!! || tempList?.get(it)?.number?.contains(p0?.toString()?.toLowerCase()!!)!!}
                .forEach { filterList?.add(tempList?.get(it)!!) }

            results.values = filterList
            results.count = filterList?.size!!

            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {

            try {
                filterList = results?.values as ArrayList<ContactDtls>?
                mList?.clear()
                val hashSet = HashSet<String>()
                if (filterList != null) {

                    /*filterList?.indices!!
                        .filter { hashSet.add(filterList?.get(it)?.name!!) }
                        .forEach { mList?.add(filterList?.get(it)!!) }*/

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

    fun refreshList(list: ArrayList<ContactDtls>) {
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
        fun onTickUntick(obj:ContactDtls,isTick: Boolean)
    }

}