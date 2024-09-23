package com.breezefieldnationalplastic.features.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.domain.NewOrderProductEntity
import com.breezefieldnationalplastic.features.member.model.TeamListDataModel
import com.breezefieldnationalplastic.features.viewAllOrder.presentation.ProductListNewOrderAdapter
import kotlinx.android.synthetic.main.row_dialog_team_member.view.tv_row_team_name

class AdapterTeamShow(var mContext:Context,var teamL:ArrayList<TeamListDataModel>,var listner:onCLick):
    RecyclerView.Adapter<AdapterTeamShow.TeamShowViewModel>(),Filterable{

    private var arrayList_Bean: ArrayList<TeamListDataModel>? = ArrayList()
    private var arrayList_Team: ArrayList<TeamListDataModel>? = ArrayList()
    private var valueFilter: ValueFilter? = null

    init {
        arrayList_Bean?.addAll(teamL)
        arrayList_Team?.addAll(teamL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamShowViewModel {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_dialog_team_member,parent,false)
        return TeamShowViewModel(view)
    }

    override fun onBindViewHolder(holder: TeamShowViewModel, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return arrayList_Team!!.size
    }

    inner class TeamShowViewModel(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.apply {
                tv_row_team_name.text = arrayList_Team?.get(adapterPosition)?.user_name
                tv_row_team_name.setOnClickListener {
                    listner.onclick(arrayList_Team?.get(adapterPosition)!!)
                }
            }
        }
    }

    override fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter as ValueFilter
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null && constraint.length > 0) {
                val arrayList_filter: ArrayList<TeamListDataModel> = ArrayList()
                for (i in 0..teamL!!.size-1) {
                    if (teamL!!.get(i).user_name!!.contains(constraint.toString(),ignoreCase = true)) {
                        arrayList_filter.add(TeamListDataModel(teamL!!.get(i).user_id,teamL!!.get(i).user_name,teamL!!.get(i).contact_no))
                    }
                }
                filterResults.count = arrayList_filter!!.size
                filterResults.values = arrayList_filter
            } else {
                filterResults.count = arrayList_Bean!!.size
                filterResults.values = arrayList_Bean
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            arrayList_Team = results.values as ArrayList<TeamListDataModel>
            notifyDataSetChanged()
        }
    }

    interface onCLick{
        fun onclick(obj:TeamListDataModel)
    }


}