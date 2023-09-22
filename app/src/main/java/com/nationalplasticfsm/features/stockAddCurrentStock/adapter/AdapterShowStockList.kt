package com.nationalplasticfsm.features.stockAddCurrentStock.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.domain.CurrentStockEntryModelEntity
import com.nationalplasticfsm.app.types.FragType
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.features.stockAddCurrentStock.UpdateShopStockFragment
import com.nationalplasticfsm.features.stockAddCurrentStock.`interface`.ShowStockOnClick
import kotlinx.android.synthetic.main.row_show_stock_list.view.*
import kotlinx.android.synthetic.main.row_show_stock_list.view.stock_date_tv
import kotlinx.android.synthetic.main.row_show_stock_list.view.stock_qty_tv
import kotlinx.android.synthetic.main.row_show_stock_list.view.sync_status_iv
import kotlinx.android.synthetic.main.row_show_stock_list.view.tv_stock_view
import kotlinx.android.synthetic.main.row_view_competetor_stock_list.view.*
//1.0  Rev AdapterShowStockList AppV 4.0.8 saheli    12/05/2023 mantis 26102
class AdapterShowStockList(val context: Context, val stockList: List<CurrentStockEntryModelEntity>,val listner: ShowStockOnClick): RecyclerView.Adapter<AdapterShowStockList.ShowStockViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowStockViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_show_stock_list,parent,false)
        return ShowStockViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stockList!!.size
    }

    override fun onBindViewHolder(holder: ShowStockViewHolder, position: Int) {
        holder.tv_stock_date.text=stockList!!.get(position).visited_date
        holder.tv_stock_qty.text=stockList!!.get(position).total_product_stock_qty
        if(stockList.get(position).isUploaded!!){
            holder.iv_sync.setImageResource(R.drawable.ic_registered_shop_sync)
        }else{
            holder.iv_sync.setImageResource(R.drawable.ic_registered_shop_not_sync)
        }
        holder.tv_stock_view.setOnClickListener { listner.stockListOnClick(stockList!!.get(position).stock_id!!) }

        //1.0 start Rev AdapterShowStockList AppV 4.0.8 saheli    12/05/2023 mantis 26102
        if(Pref.isCurrentStockEnable){
            if(Pref.IsCurrentStockApplicableforAll){
                if(Pref.IsAttachmentAvailableForCurrentStock){
                    holder.tv_attach.visibility = View.VISIBLE
                }else{
                    holder.tv_attach.visibility = View.GONE
                }
            }else{
                holder.tv_attach.visibility = View.GONE
            }
        }else{
            holder.tv_attach.visibility = View.GONE
        }

        holder.tv_attach.setOnClickListener{listner.stockattachment(stockList!!.get(position).stock_id!!)}
        //1.0 end Rev AdapterShowStockList AppV 4.0.8 saheli    12/05/2023 mantis 26102
    }

    inner class ShowStockViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tv_stock_date = itemView.stock_date_tv
        val tv_stock_qty = itemView.stock_qty_tv
        val tv_stock_view = itemView.tv_stock_view
        val iv_sync=itemView.sync_status_iv
        //1.0 start Rev AdapterShowStockList AppV 4.0.8 saheli    12/05/2023 mantis 26101
        val tv_attach = itemView.iv_attach_row_shop_stock_list
        //1.0 end Rev AdapterShowStockList AppV 4.0.8 saheli    12/05/2023 mantis 26101
    }

}