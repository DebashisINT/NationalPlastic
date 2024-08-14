package com.breezenationalplasticfsm.features.marketAssist

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import kotlinx.android.synthetic.main.row_churn_shop_l.view.iv_tag1
import kotlinx.android.synthetic.main.row_churn_shop_l.view.iv_tag2
import kotlinx.android.synthetic.main.row_churn_shop_l.view.iv_tag3
import kotlinx.android.synthetic.main.row_churn_shop_l.view.iv_tag4
import kotlinx.android.synthetic.main.row_churn_shop_l.view.iv_tag5
import kotlinx.android.synthetic.main.row_churn_shop_l.view.iv_tag6
import kotlinx.android.synthetic.main.row_churn_shop_l.view.risktxt
import kotlinx.android.synthetic.main.row_churn_shop_l.view.tv_row_shop_list_churn_addr
import kotlinx.android.synthetic.main.row_churn_shop_l.view.tv_row_shop_list_churn_name
import kotlinx.android.synthetic.main.row_churn_shop_l.view.tv_row_shop_list_churn_shop_contact
import kotlinx.android.synthetic.main.row_churn_shop_l.view.tv_row_shop_list_churn_shop_type1
import kotlinx.android.synthetic.main.row_churn_shop_l.view.tv_tag1
import kotlinx.android.synthetic.main.row_churn_shop_l.view.tv_tag2
import kotlinx.android.synthetic.main.row_churn_shop_l.view.tv_tag3
import kotlinx.android.synthetic.main.row_churn_shop_l.view.tv_tag4
import kotlinx.android.synthetic.main.row_churn_shop_l.view.tv_tag5
import kotlinx.android.synthetic.main.row_churn_shop_l.view.tv_tag6

class AdapterChurnShopL(var mContext:Context,var mList:ArrayList<ChurnShopL>):
    RecyclerView.Adapter<AdapterChurnShopL.ChurnShopLViewHolder>(){

    var count: Int=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChurnShopLViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_churn_shop_l,parent,false)
        return ChurnShopLViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChurnShopLViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return mList.size!!
    }

    inner class ChurnShopLViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            itemView.tv_row_shop_list_churn_name.text = mList.get(adapterPosition).shop_name
            itemView.tv_row_shop_list_churn_addr.text = mList.get(adapterPosition).address
            itemView.tv_row_shop_list_churn_shop_type1.text = mList.get(adapterPosition).shopType
            itemView.tv_row_shop_list_churn_shop_contact.text = mList.get(adapterPosition).owner_contact_number

            if(mList.get(adapterPosition).lastPurchaseAge.equals("")){
                itemView.tv_tag1.text = "Customers who have not made a purchase:"+"\n N/A"
            }else{
                itemView.tv_tag1.text = "Customers who have not made a purchase:"+"\n ${mList.get(adapterPosition).lastPurchaseAge} days"
            }
            if(mList.get(adapterPosition).tag1){
                itemView.iv_tag1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57a518")))
                itemView.iv_tag1.rotationX = 0f
            }else{
                itemView.iv_tag1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8424e")))
                itemView.iv_tag1.rotationX = 150f

            }

            if(mList.get(adapterPosition).lastVisitAge.equals("")){
                itemView.tv_tag2.text = "Number of days since the customer's last visit:"+"\n N/A"
            }else{
                itemView.tv_tag2.text = "Number of days since the customer's last visit:"+"\n ${mList.get(adapterPosition).lastVisitAge} days"
            }
            if(mList.get(adapterPosition).tag2){
                itemView.iv_tag2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57a518")))
                itemView.iv_tag2.rotationX = 0f
            }else{
                itemView.iv_tag2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8424e")))
                itemView.iv_tag2.rotationX = 150f

            }

            if(mList.get(adapterPosition).avgShopOrdAmt.equals("")){
                itemView.tv_tag3.text = "Monetary Value: N/A "
            }else{
                itemView.tv_tag3.text = "Monetary Value: (INR) ${mList.get(adapterPosition).avgShopOrdAmt}"
            }
            if(mList.get(adapterPosition).tag3){
                itemView.iv_tag3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57a518")))
                itemView.iv_tag3.rotationX = 0f
            }else{
                itemView.iv_tag3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8424e")))
                itemView.iv_tag3.rotationX = 150f

            }

            if(mList.get(adapterPosition).avgTimeSinceFirstOrd.equals("")){
                itemView.tv_tag4.text = "Time since First Order: N/A"
            }else{
                itemView.tv_tag4.text = "Time since First Order: ${mList.get(adapterPosition).avgTimeSinceFirstOrd} days"
            }
            if(mList.get(adapterPosition).tag4){
                itemView.iv_tag4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57a518")))
                itemView.iv_tag4.rotationX = 0f
            }else{
                itemView.iv_tag4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8424e")))
                itemView.iv_tag4.rotationX = 150f

            }

            if(mList.get(adapterPosition).avgTimeSinceFirstOrd.equals("")){
                itemView.tv_tag5.text = "Customer Segment On Order behavior: N/A"
            }else{
                //itemView.tv_tag5.text = "Customer Segment On Order behavior:\n ${mList.get(adapterPosition).avgTimeSinceFirstOrd} days"
                itemView.tv_tag5.text = "Customer Segment On Order behavior:\n ${mList.get(adapterPosition).orderBehav}"
            }
            if(mList.get(adapterPosition).tag5){
                itemView.iv_tag5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57a518")))
                itemView.iv_tag5.rotationX = 0f

            }else{
                itemView.iv_tag5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8424e")))
                itemView.iv_tag5.rotationX = 150f

            }

            if(mList.get(adapterPosition).shopVisitAvg.equals("")){
                itemView.tv_tag6.text = "Customer Segment On Visit behavior: N/A"
            }else{
                itemView.tv_tag6.text = "Customer Segment On Visit behavior:\n ${mList.get(adapterPosition).shopVisitAvg} days"
            }
            if(mList.get(adapterPosition).tag6){
                itemView.iv_tag6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57a518")))
                itemView.iv_tag6.rotationX = 0f
            }else{
                itemView.iv_tag6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8424e")))
                itemView.iv_tag6.rotationX = 150f

            }

            // Puja mantis-0027005 start //

            val booleanList = listOf(mList.get(adapterPosition).tag1, mList.get(adapterPosition).tag2, mList.get(adapterPosition).tag3, mList.get(adapterPosition).tag4, mList.get(adapterPosition).tag5, mList.get(adapterPosition).tag6)
            // Count the number of false values
            val falseCount = booleanList.count { it == false }
            // Print the result
            println("Number of false values: $falseCount")
            println("size of booleanList values: ${booleanList.size}")

            if (falseCount>=4){
                itemView.risktxt.text ="HIGH RISK"
            }
            else if (falseCount>=2 && falseCount<4){
                itemView.risktxt.text ="MODERATE RISK"
            }
            else{
                itemView.risktxt.text =""

            }

            // Puja mantis-0027005 end //

            var anim:Animation=AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left)
            itemView.startAnimation(anim)

        }
    }

}