package com.nationalplasticfsm.features.NewQuotation.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.domain.ProductListEntity
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.features.NewQuotation.model.shop_wise_quotation_list
import kotlinx.android.synthetic.main.inflater_quot_history_item.view.*
import kotlinx.android.synthetic.main.row_new_quot_added_prod.view.*
import kotlinx.android.synthetic.main.row_new_quot_added_prod.view.tv_row_new_quot_added_prod_name

class ViewAllQuotViewAdapter(private val context: Context, private val selectedProductList: ArrayList<shop_wise_quotation_list>?, private val listener: OnClickListener) :
        RecyclerView.Adapter<ViewAllQuotViewAdapter.MyViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.inflater_quot_history_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, selectedProductList, listener)
    }

    override fun getItemCount(): Int {
        return selectedProductList!!.size!!
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(context: Context, categoryList: ArrayList<shop_wise_quotation_list>?, listener: OnClickListener) {
            itemView.quot_date_tv.text=AppUtils.convertDateTimeToCommonFormat(categoryList!!.get(adapterPosition).save_date_time!!.subSequence(0,10).toString()).toString()
            itemView.quot_no_tv.text=categoryList!!.get(adapterPosition).quotation_number

            itemView.tv_quot_view.setOnClickListener {
                listener.onView(adapterPosition = adapterPosition,QuotId = categoryList!!.get(adapterPosition).quotation_number!!)
            }
            itemView.share_iv.setOnClickListener {
                listener.onShare(adapterPosition = adapterPosition)
            }
            itemView.tv_quot_del.setOnClickListener {
                listener.onDelete(adapterPosition = adapterPosition,QuotId = categoryList!!.get(adapterPosition).quotation_number!!)
            }

        }
    }

    interface OnClickListener {
        fun onView(adapterPosition: Int,QuotId:String)
        fun onShare(adapterPosition: Int)
        fun onDelete(adapterPosition: Int,QuotId:String)
    }
}