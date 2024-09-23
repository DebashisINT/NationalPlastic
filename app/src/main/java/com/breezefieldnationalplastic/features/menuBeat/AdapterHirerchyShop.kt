package com.breezefieldnationalplastic.features.menuBeat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import kotlinx.android.synthetic.main.row_beat_hirerchy_beat.view.*
import kotlinx.android.synthetic.main.row_beat_hirerchy_shop.view.*
// 3.0 BeatHierarchyStatusFrag AppV 4.0.6 Suman 20-01-2023  hirerchy design updation
class AdapterHirerchyShop(var mContext: Context, var shopL:ArrayList<AddShopDBModelEntity>):
    RecyclerView.Adapter<AdapterHirerchyShop.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.row_beat_hirerchy_shop,parent,false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return shopL.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_beat_hir_shopname.text = "Shop: "+shopL.get(adapterPosition).shopName
            }
        }
    }

}