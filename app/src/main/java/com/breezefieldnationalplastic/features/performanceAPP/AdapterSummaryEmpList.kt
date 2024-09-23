package com.breezefieldnationalplastic.features.performanceAPP

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.uiaction.IntentActionable
import com.breezefieldnationalplastic.features.alarm.model.PerformanceReportDataModel
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_addr1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_name
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_shop_catagory_retailer1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_shop_type1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_name
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_phone_number
import kotlinx.android.synthetic.main.row_no_zero_order_shop.view.tv_row_shop_list_ma_shop_count
import kotlinx.android.synthetic.main.row_shop_list_ma.view.tv_row_shop_list_ma_name_pref1
import kotlinx.android.synthetic.main.row_summary_emp.view.tv_absent_row_summary_emp
import kotlinx.android.synthetic.main.row_summary_emp.view.tv_attend_row_summary_emp
import kotlinx.android.synthetic.main.row_summary_emp.view.tv_lastOdate
import kotlinx.android.synthetic.main.row_summary_emp.view.tv_lastVdate
import kotlinx.android.synthetic.main.row_summary_emp.view.tv_order_value_last_month
import kotlinx.android.synthetic.main.row_summary_emp.view.tv_row_Order_inctivity3
import kotlinx.android.synthetic.main.row_summary_emp.view.tv_row_emp_name
import kotlinx.android.synthetic.main.row_summary_emp.view.tv_row_incativity_last_3
import java.util.Random

class AdapterSummaryEmpList(var mContext: Context, var mList:ArrayList<PerformanceReportDataModel>):
    RecyclerView.Adapter<AdapterSummaryEmpList.CrossSellProductViewHolder>(){
    private lateinit var colorCodeL : ArrayList<String>
    val random = Random()
    init {
        colorCodeL= ArrayList()
        colorCodeL.add("#0FB9F0")
        colorCodeL.add("#AA33AA")
        colorCodeL.add("#3E2CF1")
        colorCodeL.add("#45560F")
        colorCodeL.add("#DF6714")
        colorCodeL.add("#540422")
        colorCodeL.add("#04544A")
        colorCodeL.add("#958609")
        colorCodeL.add("#283747")
        colorCodeL.add("#1B4F72")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrossSellProductViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_summary_emp,parent,false)
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
            val m = random.nextInt(9 - 1) + 1
//            itemView.tv_row_shop_list_ma_name.setBackgroundTintList(
//                ColorStateList.valueOf(
//                    Color.parseColor(colorCodeL.get(m))))
            itemView.tv_row_shop_list_ma_name.text = mList.get(adapterPosition).member_name!!.get(0).toString()


            itemView.tv_order_value_last_month.text = mList.get(adapterPosition).order_vale
            itemView.tv_row_emp_name.text = mList.get(adapterPosition).member_name

            itemView.tv_attend_row_summary_emp.text =  "Logged in "+mList.get(adapterPosition).attendance_present_count+" Days"
            itemView.tv_absent_row_summary_emp.text =  "Not logged in "+mList.get(adapterPosition).attendance_absent_count+" Days"

            itemView.tv_row_incativity_last_3.text =  mList.get(adapterPosition).visit_inactivity_party_count.toString()
            itemView.tv_row_Order_inctivity3.text =  mList.get(adapterPosition).order_inactivity_party_count.toString()
            itemView.tv_lastVdate.text =  mList.get(adapterPosition).last_visited_date
            itemView.tv_lastOdate.text =  if(mList.get(adapterPosition).last_order_date.equals("")) "N/A" else mList.get(adapterPosition).last_order_date

        }
    }

}