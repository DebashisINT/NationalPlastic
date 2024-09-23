package com.breezefieldnationalplastic.features.addAttendence

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.features.addAttendence.model.AreaList
import kotlinx.android.synthetic.main.row_area_list.view.iv_check_area
import kotlinx.android.synthetic.main.row_area_list.view.ll_row_area_root
import kotlinx.android.synthetic.main.row_area_list.view.tv_row_area

class AreaListAdapter(var mContext:Context, var areaList:ArrayList<AreaList>, var listner:onCLick) :
    RecyclerView.Adapter<AreaListAdapter.AreaListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaListViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_area_list,parent,false)
        return AreaListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AreaListViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return areaList.size
    }

    inner class AreaListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindItems() {
            itemView.apply {
                iv_check_area.isSelected = areaList?.get(adapterPosition)?.isSelected!!
                tv_row_area.text = areaList.get(adapterPosition).area_location_name

                ll_row_area_root.setOnClickListener{
                    if (tv_row_area.isSelected) {
                        areaList!![adapterPosition].isSelected = false
                    }else{
                        areaList!!.forEach {
                            it.isSelected = false
                        }
                        areaList!!.get(adapterPosition).isSelected = true
                    }
                    listner?.onTick(areaList!!.get(adapterPosition))
                    notifyDataSetChanged()
                }

            }
        }
    }

    interface onCLick{
        fun onTick(obj:AreaList)
    }

}