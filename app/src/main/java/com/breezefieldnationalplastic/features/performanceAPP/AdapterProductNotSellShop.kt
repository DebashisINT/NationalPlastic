package com.breezefieldnationalplastic.features.performanceAPP

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_name

class AdapterProductNotSellShop(var mContext: Context, var mList:ArrayList<NoProductSoldShop>):
    RecyclerView.Adapter<AdapterProductNotSellShop.CrossSellProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrossSellProductViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_no_product_shop,parent,false)
        return CrossSellProductViewHolder(v)
    }

    override fun onBindViewHolder(holder: CrossSellProductViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class CrossSellProductViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        @SuppressLint("ResourceType")
        fun bindItems(){
            itemView.tv_row_shop_name.text = mList.get(adapterPosition).product_name
            /*if(adapterPosition%2 == 0){
                itemView.ll_row_pro_cross_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            }else{
                itemView.ll_row_pro_cross_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightest_light_new_gray))
            }*/

        }
    }

}