package com.breezenationalplasticfsm.features.dashboard.presentation

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.SelectedRouteShopListEntity
import com.breezenationalplasticfsm.app.domain.SelectedWorkTypeEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import kotlinx.android.synthetic.main.inflate_work_plan_item.view.*
import kotlinx.android.synthetic.main.inflate_work_plan_item1.view.shop_name_TV


/**
 * Created by Saikat on 30-11-2018.
 */
class TodaysWorkAdapter1(context: Context, list: ArrayList<SelectedWorkTypeEntity>) : RecyclerView.Adapter<TodaysWorkAdapter1.MyViewHolder>() {

    private val layoutInflater: LayoutInflater
    private var context: Context
    private lateinit var mList: ArrayList<SelectedWorkTypeEntity>


    init {
        Log.e("works adapter", "========init block=============")
        layoutInflater = LayoutInflater.from(context)
        this.context = context
        mList = list
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.e("works adapter", "mList.position---------> $position")
        holder.bindItems(context, mList, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       // val v = layoutInflater.inflate(R.layout.inflate_work_plan_item, parent, false)
        val v = layoutInflater.inflate(R.layout.inflate_work_plan_item1, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.e("works adapter", "mList.size---------> " + mList.size)
        return mList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, list: ArrayList<SelectedWorkTypeEntity>, position: Int) {
            itemView.shop_name_TV.text = list[adapterPosition].Descrpton
        }

    }
}