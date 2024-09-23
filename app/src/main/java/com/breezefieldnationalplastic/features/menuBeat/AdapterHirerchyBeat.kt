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
import kotlinx.android.synthetic.main.row_beat_hirerchy_route.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
// 3.0 BeatHierarchyStatusFrag AppV 4.0.6 Suman 20-01-2023  hirerchy design updation
class AdapterHirerchyBeat(var mContext: Context, var beatL:ArrayList<MenuBeat>):
    RecyclerView.Adapter<AdapterHirerchyBeat.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.row_beat_hirerchy_beat,parent,false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return beatL.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_beat_hir_beat_name.text = "Beat: "+beatL.get(adapterPosition).beat_name

                doAsync {
                    var beatShopL = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(beatL.get(adapterPosition).beat_id) as ArrayList<AddShopDBModelEntity>
                    uiThread {
                        if(beatShopL.size>0){
                            var adapterHirerchyShop = AdapterHirerchyShop(mContext,beatShopL)
                            rv_row_beat_hir_shop.adapter = adapterHirerchyShop
                        }
                    }
                }

                rv_row_beat_hir_shop.visibility=View.GONE
                iv_row_beat_hir_plus_shop.setOnClickListener {
                    if(rv_row_beat_hir_shop.visibility == View.GONE){
                        rv_row_beat_hir_shop.visibility=View.VISIBLE
                        iv_row_beat_hir_plus_shop.setBackgroundResource(R.drawable.ic_minus_icon)
                    }else{
                        rv_row_beat_hir_shop.visibility=View.GONE
                        iv_row_beat_hir_plus_shop.setBackgroundResource(R.drawable.ic_plus_icon)
                    }
                }
            }
        }
    }

}