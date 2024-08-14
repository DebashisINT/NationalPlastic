package com.breezenationalplasticfsm.features.performanceAPP.model
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.features.performanceAPP.PerformDataClass
import kotlinx.android.synthetic.main.row_dialog_tax_multiple.view.*

/**
 * Created by Saheli on 19-04-2023 v 4.0.8 mantis 0025860.
 */
class AdapterUserTestList (var mContext: Context, var customerList:ArrayList<PerformDataClass>, val listner:OnClick, private val getSize: (Int) -> Unit):
        RecyclerView.Adapter<AdapterUserTestList.MyViewHolder>(), Filterable {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }

    private var mList: ArrayList<PerformDataClass>? = null
    private var tempList: ArrayList<PerformDataClass>? = null
    private var filterList: ArrayList<PerformDataClass>? = null

    init {
        mList = ArrayList()
        tempList = ArrayList()
        filterList = ArrayList()

        mList?.addAll(customerList)
        tempList?.addAll(customerList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.row_dialog_tax_multiple, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mList?.size!!
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems()
    }

    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_dialog_tax.text=mList!!.get(adapterPosition).shop_name!!

                if(mList!!.get(adapterPosition).isChecked){
                    cb_row_row_dialog_tax_multiple.isChecked = true
                }else{
                    cb_row_row_dialog_tax_multiple.isChecked = false
                }

                cb_row_row_dialog_tax_multiple.setOnClickListener {
                    var checkedL = mList!!.filter { it.isChecked }
                    if(checkedL.size + 1 >5){
                        if(mList!!.get(adapterPosition).isChecked == false){
                            Toaster.msgShort(mContext,"Max 5 party Selected")
                        }

                        cb_row_row_dialog_tax_multiple.isChecked = false
                        mList!!.get(adapterPosition).isChecked = false
                        listner.onTickUntickView(mList!!.get(adapterPosition),false)
                    }else{
                        if(mList!!.get(adapterPosition).isChecked){
                            mList!!.get(adapterPosition).isChecked = false
                            cb_row_row_dialog_tax_multiple.isChecked = false
                            listner.onTickUntickView(mList!!.get(adapterPosition),false)
                        }else{
                            mList!!.get(adapterPosition).isChecked = true
                            cb_row_row_dialog_tax_multiple.isChecked = true
                            listner.onTickUntickView(mList!!.get(adapterPosition),true)
                        }
                    }
                }


               /* cb_row_row_dialog_tax_multiple.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked){
                        var checkedL = mList!!.filter { it.isChecked }
                        if(checkedL.size>=5){
                            buttonView.isChecked = false
                            Toaster.msgShort(mContext,"Max 5 party Selected")
                            return@setOnCheckedChangeListener
                        }
                        mList!!.get(adapterPosition).isChecked = true
                        listner.onTickUntickView(mList!!.get(adapterPosition),true)
                    }else{
                        mList!!.get(adapterPosition).isChecked = false
                        listner.onTickUntickView(mList!!.get(adapterPosition),false)
                    }
                }*/
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
                    .filter { tempList?.get(it)?.shop_name?.toLowerCase()?.contains(p0?.toString()?.toLowerCase()!!)!!}
                    .forEach { filterList?.add(tempList?.get(it)!!) }

            results.values = filterList
            results.count = filterList?.size!!

            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {

            try {
                filterList = results?.values as ArrayList<PerformDataClass>?
                mList?.clear()
                val hashSet = HashSet<String>()
                if (filterList != null) {

                    filterList?.indices!!
                            .filter { hashSet.add(filterList?.get(it)?.shop_name!!) }
                            .forEach { mList?.add(filterList?.get(it)!!) }

                    getSize(mList?.size!!)

                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshList(list: ArrayList<PerformDataClass>) {
        mList?.clear()
        mList?.addAll(list)

        tempList?.clear()
        tempList?.addAll(list)

        if (filterList == null)
            filterList = ArrayList()
        filterList?.clear()

        notifyDataSetChanged()
    }

    interface OnClick {
        fun onTickUntickView(obj: PerformDataClass,isTick:Boolean)
    }

}