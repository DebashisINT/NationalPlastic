package com.breezenationalplasticfsm.features.performanceAPP

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.uiaction.IntentActionable
import com.breezenationalplasticfsm.app.utils.AppUtils
import kotlinx.android.synthetic.main.row_no_order_shop_2.view.tv_row_shop_list_ma_addr1
import kotlinx.android.synthetic.main.row_no_order_shop_2.view.tv_row_shop_list_ma_name
import kotlinx.android.synthetic.main.row_no_order_shop_2.view.tv_row_shop_list_ma_shop_catagory_retailer1
import kotlinx.android.synthetic.main.row_no_order_shop_2.view.tv_row_shop_list_ma_shop_count
import kotlinx.android.synthetic.main.row_no_order_shop_2.view.tv_row_shop_list_shop_type1
import kotlinx.android.synthetic.main.row_no_order_shop_2.view.tv_row_shop_name
import kotlinx.android.synthetic.main.row_no_order_shop_2.view.tv_row_shop_phone_number

class AdapterNoOrderTakenShop2(var mContext: Context, var mList:ArrayList<NoOrderTakenShop2>):
    RecyclerView.Adapter<AdapterNoOrderTakenShop2.CrossSellProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrossSellProductViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_no_order_shop_2,parent,false)
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
            itemView.tv_row_shop_list_ma_name.text = mList.get(adapterPosition).shop_name.get(0).toString()
            itemView.tv_row_shop_name.text = mList.get(adapterPosition).shop_name
            itemView.tv_row_shop_phone_number.text = mList.get(adapterPosition).owner_contact_number
            itemView.tv_row_shop_list_ma_addr1.text = mList.get(adapterPosition).address
//            itemView.tv_row_shop_list_ma_shop_count.text =  AppUtils.changeDateFormat1(mList.get(adapterPosition).orderDate).split("T").get(0)
//            itemView.tv_row_shop_list_ma_shop_count.text =  mList.get(adapterPosition).orderDate.split("T").get(0)

            try{
                itemView.tv_row_shop_list_ma_shop_count.text =    AppUtils.getFormatedDateNew( mList.get(adapterPosition).orderDate.split("T").get(0), "yyyy-mm-dd", "dd-mm-yyyy")

            }catch (ex:Exception){
                itemView.tv_row_shop_list_ma_shop_count.text =  "N/A"
            }


            itemView.tv_row_shop_phone_number.setOnClickListener {
                IntentActionable.initiatePhoneCall(mContext, mList.get(adapterPosition).owner_contact_number)
            }

            try{
                var typeName = AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType(mList.get(adapterPosition).type)!!.shoptype_name
                itemView.tv_row_shop_list_shop_type1.text =typeName
            }catch (ex:Exception){
                itemView.tv_row_shop_list_shop_type1.text ="N/A"
            }

            try{
                if(!mList.get(adapterPosition).owner_name.equals("")){
                    itemView.tv_row_shop_list_ma_shop_catagory_retailer1.text = mList.get(adapterPosition).owner_name
                }
                else{
                    itemView.tv_row_shop_list_ma_shop_catagory_retailer1.text = "N/A"
                }

            }catch (ex:Exception){
                itemView.tv_row_shop_list_ma_shop_catagory_retailer1.text = "N/A"
            }


            /*if(adapterPosition%2 == 0){
                itemView.ll_row_pro_cross_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            }else{
                itemView.ll_row_pro_cross_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightest_light_new_gray))
            }*/

        }
    }

}