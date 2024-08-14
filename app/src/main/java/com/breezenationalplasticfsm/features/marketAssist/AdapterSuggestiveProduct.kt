package com.breezenationalplasticfsm.features.marketAssist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import kotlinx.android.synthetic.main.row_suggest_product.view.ll_row_pro_sugg_root
import kotlinx.android.synthetic.main.row_suggest_product.view.tv_row_pro_sugg_name
import kotlinx.android.synthetic.main.row_suggest_product.view.tv_row_pro_sugg_qty
import kotlinx.android.synthetic.main.row_suggest_product.view.tv_row_pro_sugg_rate

class AdapterSuggestiveProduct(var mContext:Context,var mList:ArrayList<SuggestiveProductFinal>):
    RecyclerView.Adapter<AdapterSuggestiveProduct.SuggestiveProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestiveProductViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_suggest_product,parent,false)
        return SuggestiveProductViewHolder(v)
    }

    override fun onBindViewHolder(holder: SuggestiveProductViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class SuggestiveProductViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        fun bindItems(){
            itemView.tv_row_pro_sugg_name.text = mList.get(adapterPosition).product_name
            itemView.tv_row_pro_sugg_rate.text = mList.get(adapterPosition).suggestiveOrdRate+" or above."
            itemView.tv_row_pro_sugg_qty.text = mList.get(adapterPosition).suggestiveOrdQty+" or above."

            if(adapterPosition%2 == 0){
                itemView.ll_row_pro_sugg_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            }else{
                itemView.ll_row_pro_sugg_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightest_light_new_gray))
            }

            /*if(adapterPosition%2 == 0){
                itemView.ll_row_pro_sugg_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.custom_gray1))
                itemView.tv_row_pro_sugg_name.setTextColor(mContext.resources.getColor(R.color.black))
                itemView.tv_row_pro_sugg_rate.setTextColor(mContext.resources.getColor(R.color.black))
                itemView.tv_row_pro_sugg_qty.setTextColor(mContext.resources.getColor(R.color.black))
            }else{
                itemView.ll_row_pro_sugg_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.custom_blue1))
                itemView.tv_row_pro_sugg_name.setTextColor(mContext.resources.getColor(R.color.white))
                itemView.tv_row_pro_sugg_rate.setTextColor(mContext.resources.getColor(R.color.white))
                itemView.tv_row_pro_sugg_qty.setTextColor(mContext.resources.getColor(R.color.white))
            }*/
        }
    }

}