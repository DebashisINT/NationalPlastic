package com.breezefieldnationalplastic.features.leaderboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.features.contacts.ScheduleContactDtls
import com.breezefieldnationalplastic.features.leaderboard.api.SubBranchData
import com.breezefieldnationalplastic.features.newcollectionreport.PendingCollData
import kotlinx.android.synthetic.main.row_cont_select.view.cb_row_cont_sel_name
import kotlinx.android.synthetic.main.row_cont_select.view.tv_row_cont_sel_name
import kotlinx.android.synthetic.main.row_dialog_tax_multiple.view.cb_row_row_dialog_tax_multiple
import kotlinx.android.synthetic.main.row_subbranch_select.view.cb_row_subBranch_sel_name
import org.jetbrains.annotations.Async.Schedule

class AdapterSubBranchName(var mContext:Context, var subBranchList:ArrayList<SubBranchData> ,var listner: OnSubBrCLick , private val getSize: (Int) -> Unit):
    RecyclerView.Adapter<AdapterSubBranchName.SubBranchnameViewHolder>(), Filterable {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }

    private var mList: ArrayList<SubBranchData>? = null
    private var tempList: ArrayList<SubBranchData>? = null
    private var filterList: ArrayList<SubBranchData>? = null

    init {
        mList = ArrayList()
        tempList = ArrayList()
        filterList = ArrayList()

        mList?.addAll(subBranchList)
        tempList?.addAll(subBranchList)
    }
    
    public fun selectAll() {

        for(i in 0..mList!!.size-1){
            if(mList!!.get(i).isTick == false){
                mList!!.get(i).isTick = true

                if(mList!!.get(i).isTick){
                    listner.onTick(mList!!.get(i),true)
                }else{
                    listner.onTick(mList!!.get(i),false)
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
                    listner.onTick(mList!!.get(i),true)
                }else{
                    listner.onTick(mList!!.get(i),false)
                }
          //  }
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubBranchnameViewHolder {
        val v = layoutInflater.inflate(R.layout.row_subbranch_select, parent, false)
        return SubBranchnameViewHolder(v)
    }

    override fun onBindViewHolder(holder: SubBranchnameViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
       return mList?.size!!
    }

    inner class SubBranchnameViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                try {
                    cb_row_subBranch_sel_name.text = mList?.get(adapterPosition)?.value
                    if(mList!!.get(adapterPosition).isTick){
                        cb_row_subBranch_sel_name.isChecked = true
                    }else{
                        cb_row_subBranch_sel_name.isChecked = false
                    }

                    cb_row_subBranch_sel_name.setOnClickListener {
                        if(mList!!.get(adapterPosition).isTick){
                            mList!!.get(adapterPosition).isTick = false
                            cb_row_subBranch_sel_name.isChecked = false
                            listner.onTick(mList!!.get(adapterPosition),false)
                        }else{
                            mList!!.get(adapterPosition).isTick = true
                            cb_row_subBranch_sel_name.isChecked = true
                            listner.onTick(mList!!.get(adapterPosition),true)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
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
                .filter {
                    tempList?.get(it)?.value?.toLowerCase()
                        ?.contains(p0?.toString()?.toLowerCase()!!)!!
                }
                .forEach { filterList?.add(tempList?.get(it)!!) }

            results.values = filterList
            results.count = filterList?.size!!

            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {
            try {
                filterList = results?.values as ArrayList<SubBranchData>?
                mList?.clear()
                val hashSet = HashSet<String>()
                if (filterList != null) {

                    filterList?.indices!!
                        .filter { hashSet.add(filterList?.get(it)?.value!!) }
                        .forEach { mList?.add(filterList?.get(it)!!) }

                    getSize(mList?.size!!)

                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun refreshList(list: ArrayList<SubBranchData>) {
        mList?.clear()
        mList?.addAll(list)

        tempList?.clear()
        tempList?.addAll(list)

        if (filterList == null)
            filterList = ArrayList()
        filterList?.clear()

        notifyDataSetChanged()
    }

    interface OnSubBrCLick{
        fun onTick(obj: SubBranchData, isTick:Boolean)
    }

    }

