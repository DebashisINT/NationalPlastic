package com.breezenationalplasticfsm.features.dashboard.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import kotlinx.android.synthetic.main.row_menu_adv.view.iv_menu_adv_arrow
import kotlinx.android.synthetic.main.row_menu_adv.view.iv_menu_item_image
import kotlinx.android.synthetic.main.row_menu_adv.view.ll_row_menu_adv_root
import kotlinx.android.synthetic.main.row_menu_adv.view.rv_row_menu_adv
import kotlinx.android.synthetic.main.row_menu_adv.view.tv_menu_item_name

class AdapterMenuAdv(var mContext: Context, var list:ArrayList<DashboardActivity.MenuItems>,var subList:ArrayList<DashboardActivity.MenuSubItems>, var listner: AdapterMenuAdv.OnClick, private val getSize: (Int) -> Unit):
    RecyclerView.Adapter<AdapterMenuAdv.MenuAdvVIewHolder>(), Filterable {

    private var mList: ArrayList<DashboardActivity.MenuItems>? = null
    private var tempList: ArrayList<DashboardActivity.MenuItems>? = null
    private var filterList: ArrayList<DashboardActivity.MenuItems>? = null

    private lateinit var adapterSUbL : AdapterSubMenuAdv

    init {
        mList = ArrayList()
        tempList = ArrayList()
        filterList = ArrayList()

        mList?.addAll(list)
        tempList?.addAll(list)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdvVIewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_menu_adv,parent,false)
        return MenuAdvVIewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun onBindViewHolder(holder: MenuAdvVIewHolder, position: Int) {
        holder.bindItems()
    }

    inner class MenuAdvVIewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.tv_menu_item_name.text = mList!!.get(adapterPosition).name
            itemView.iv_menu_item_image.setImageResource(mList!!.get(adapterPosition).icon)

            if(mList!!.get(adapterPosition).name.equals("Reports") || mList!!.get(adapterPosition).name.equals("Collection") || mList!!.get(adapterPosition).name.equals("LMS")){
                itemView.iv_menu_adv_arrow.visibility = View.VISIBLE
            }else{
                itemView.iv_menu_adv_arrow.visibility = View.GONE
            }

            itemView.ll_row_menu_adv_root.setOnClickListener {
                if(mList!!.get(adapterPosition).name.equals("Reports")){
                    try {
                        if(itemView.rv_row_menu_adv.visibility == View.VISIBLE){
                            itemView.rv_row_menu_adv.visibility = View.GONE
                            itemView.iv_menu_adv_arrow.animate().rotation(180f).start()
                        }else{
                            itemView.rv_row_menu_adv.visibility = View.VISIBLE
                            itemView.iv_menu_adv_arrow.animate().rotation(270f).start()
                            var subL = subList.filter { it.parentMenuName.equals("Reports") }.first().subList
                            adapterSUbL = AdapterSubMenuAdv(mContext,subL,object :AdapterSubMenuAdv.OnSubClick{
                                override fun onClick(obj: DashboardActivity.MenuItems) {
                                    listner.onClick(obj)
                                }

                            })
                            itemView.rv_row_menu_adv.adapter = adapterSUbL
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                else if(mList!!.get(adapterPosition).name.equals("Collection")){
                    try {
                        if(itemView.rv_row_menu_adv.visibility == View.VISIBLE){
                            itemView.rv_row_menu_adv.visibility = View.GONE
                            itemView.iv_menu_adv_arrow.animate().rotation(180f).start()
                        }else{
                            itemView.rv_row_menu_adv.visibility = View.VISIBLE
                            itemView.iv_menu_adv_arrow.animate().rotation(270f).start()
                            var subL = subList.filter { it.parentMenuName.equals("Collection") }.first().subList
                            adapterSUbL = AdapterSubMenuAdv(mContext,subL,object :AdapterSubMenuAdv.OnSubClick{
                                override fun onClick(obj: DashboardActivity.MenuItems) {
                                    listner.onClick(obj)
                                }

                            })
                            itemView.rv_row_menu_adv.adapter = adapterSUbL
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                else if(mList!!.get(adapterPosition).name.equals("LMS")){
                    try {
                        if(itemView.rv_row_menu_adv.visibility == View.VISIBLE){
                            itemView.rv_row_menu_adv.visibility = View.GONE
                            itemView.iv_menu_adv_arrow.animate().rotation(180f).start()
                        }else{
                            itemView.rv_row_menu_adv.visibility = View.VISIBLE
                            itemView.iv_menu_adv_arrow.animate().rotation(270f).start()
                            var subL = subList.filter { it.parentMenuName.equals("LMS") }.first().subList
                            adapterSUbL = AdapterSubMenuAdv(mContext,subL,object :AdapterSubMenuAdv.OnSubClick{
                                override fun onClick(obj: DashboardActivity.MenuItems) {
                                    listner.onClick(obj)
                                }

                            })
                            itemView.rv_row_menu_adv.adapter = adapterSUbL
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                else{
                    listner.onClick(mList!!.get(adapterPosition))
                }
            }
            itemView.rv_row_menu_adv.visibility = View.GONE
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
                .filter { tempList?.get(it)?.name?.toLowerCase()?.contains(p0?.toString()?.toLowerCase()!!)!! }
                .forEach { filterList?.add(tempList?.get(it)!!) }
            results.values = filterList
            results.count = filterList?.size!!
            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {
            try {
                filterList = results?.values as ArrayList<DashboardActivity.MenuItems>?
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

    fun refreshList(list: ArrayList<DashboardActivity.MenuItems>) {
        mList?.clear()
        mList?.addAll(list)

        tempList?.clear()
        tempList?.addAll(list)

        if (filterList == null)
            filterList = ArrayList()
        filterList?.clear()

        notifyDataSetChanged()
    }

    interface OnClick{
        fun onClick(obj:DashboardActivity.MenuItems)
    }

}