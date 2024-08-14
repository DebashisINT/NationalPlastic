package com.breezenationalplasticfsm.features.dashboard.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import kotlinx.android.synthetic.main.row_menu_adv.view.cv_menu_item_image_root
import kotlinx.android.synthetic.main.row_menu_adv.view.iv_menu_adv_arrow
import kotlinx.android.synthetic.main.row_menu_adv.view.ll_row_menu_adv_root
import kotlinx.android.synthetic.main.row_menu_adv.view.rv_row_menu_adv
import kotlinx.android.synthetic.main.row_menu_adv.view.tv_menu_item_name

class AdapterSubMenuAdv(var mContext:Context, var mList:ArrayList<DashboardActivity.MenuItems>, var listner: AdapterSubMenuAdv.OnSubClick)
    :RecyclerView.Adapter<AdapterSubMenuAdv.SubMenuAdvVIewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubMenuAdvVIewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_menu_adv,parent,false)
        return SubMenuAdvVIewHolder(view)
    }

    override fun getItemCount(): Int {
       return mList.size
    }

    override fun onBindViewHolder(holder: SubMenuAdvVIewHolder, position: Int) {
        holder.bindItems()
    }

    inner class SubMenuAdvVIewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.rv_row_menu_adv.visibility = View.GONE
            itemView.cv_menu_item_image_root.visibility = View.GONE
            itemView.iv_menu_adv_arrow.visibility = View.GONE
            itemView.tv_menu_item_name.text = mList.get(adapterPosition).name
            itemView.ll_row_menu_adv_root.setOnClickListener {
                listner.onClick(mList.get(adapterPosition))
            }
        }
    }

    interface OnSubClick{
        fun onClick(obj:DashboardActivity.MenuItems)
    }
}