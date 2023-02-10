package com.nationalplasticfsm.features.addAttendence

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.domain.AddShopDBModelEntity
import com.nationalplasticfsm.app.domain.AssignToDDEntity
import kotlinx.android.synthetic.main.row_dialog_new_order_gender.view.*

class DDAssignedWiseBeatTypeListAdapter(private var context: Context, private var gender_list:ArrayList<AssignToDDEntity>, private val listner: beatNameOnClick):
    RecyclerView.Adapter<DDAssignedWiseBeatTypeListAdapter.GenderListViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_dialog_new_order_gender,parent,false)
        return GenderListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gender_list!!.size!!
    }

    override fun onBindViewHolder(holder: GenderListViewHolder, position: Int) {
        holder.tv_gender.text=gender_list.get(position).dd_name

        holder.cv_gender.setOnClickListener { listner?.OnClick(gender_list.get(holder.adapterPosition!!)) }
    }

    inner class GenderListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tv_gender = itemView.tv_row_dialog_new_order_gender
        val cv_gender = itemView.cv_gender
    }

    interface beatNameOnClick {
        fun OnClick(data: AssignToDDEntity)
    }


}

