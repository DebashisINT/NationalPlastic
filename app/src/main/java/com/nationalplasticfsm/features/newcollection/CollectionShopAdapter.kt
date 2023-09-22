package com.nationalplasticfsm.features.newcollection

import android.content.Context
import android.text.AutoText.getSize
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nationalplasticfsm.R
import com.nationalplasticfsm.features.newcollection.model.CollectionShopListDataModel
import com.nationalplasticfsm.features.photoReg.model.UserListResponseModel
import kotlinx.android.synthetic.main.inflate_collection_shop_item.view.*

class CollectionShopAdapter(context: Context, private val amountList: ArrayList<CollectionShopListDataModel>?,
                            private val onItemClick: (CollectionShopListDataModel) -> Unit) : RecyclerView.Adapter<CollectionShopAdapter.MyViewHolder>(),
    Filterable {

    private val layoutInflater: LayoutInflater
    private var context: Context

    private var mList: ArrayList<CollectionShopListDataModel>? = null
    private var tempList: ArrayList<CollectionShopListDataModel>? = null
    private var filterList: ArrayList<CollectionShopListDataModel>? = null

    init {
        layoutInflater = LayoutInflater.from(context)
        this.context = context

        mList = ArrayList()
        tempList = ArrayList()
        filterList = ArrayList()

        if (amountList != null) {
            mList?.addAll(amountList)
        }
        if (amountList != null) {
            tempList?.addAll(amountList)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.inflate_collection_shop_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mList?.size!!
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems() {

            try {

                itemView.apply {
                    myshop_name_TV.text = mList?.get(adapterPosition)?.shop_name
                    tv_total_amount.text = context.getString(R.string.rupee_symbol_with_space) + mList?.get(adapterPosition)?.total_amount
                    tv_total_collection.text = context.getString(R.string.rupee_symbol_with_space) + mList?.get(adapterPosition)?.total_collection
                    tv_total_bal.text = context.getString(R.string.rupee_symbol_with_space) + mList?.get(adapterPosition)?.total_bal

                    Glide.with(context)
                            .load(mList?.get(adapterPosition)?.shop_image)
                            .apply(RequestOptions.placeholderOf(R.drawable.ic_logo).error(R.drawable.ic_logo))
                            .into(shop_image_IV)

                    setOnClickListener {
                        onItemClick(mList?.get(adapterPosition)!!)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Begin Rev 1.0 CollectionShopListFragment Suman 04/05/2023 Search option for shops  mantis id - 26026

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
                filterList = results?.values as ArrayList<CollectionShopListDataModel>?
                mList?.clear()
                val hashSet = HashSet<String>()
                if (filterList != null) {
                    filterList?.indices!!
                        .filter { hashSet.add(filterList?.get(it)?.shop_name!!) }
                        .forEach { mList?.add(filterList?.get(it)!!) }

                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshList(list: ArrayList<CollectionShopListDataModel>) {
        mList?.clear()
        mList?.addAll(list)
        tempList?.clear()
        tempList?.addAll(list)
        if (filterList == null)
            filterList = ArrayList()
        filterList?.clear()
        notifyDataSetChanged()
    }

    // End of Rev 1.0 CollectionShopListFragment Suman 04/05/2023 Search option for shops  mantis id - 26026

}