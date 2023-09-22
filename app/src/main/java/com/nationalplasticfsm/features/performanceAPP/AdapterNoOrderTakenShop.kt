package com.nationalplasticfsm.features.performanceAPP

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.AppDatabase
import com.nationalplasticfsm.app.uiaction.IntentActionable
import com.nationalplasticfsm.app.utils.AppUtils
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_addr1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_shop_catagory_retailer1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_name
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_phone_number
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_name
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_shop_count
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_shop_type1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_tv_text_dynamic
import kotlinx.android.synthetic.main.row_shop_list_ma.view.ll_row_shop_dtls_ma_contact1

class AdapterNoOrderTakenShop(var mContext: Context, var mList:ArrayList<NoOrderTakenShop>,var isFromCollection:Boolean=false,var isFromVisit:Boolean=false):
    RecyclerView.Adapter<AdapterNoOrderTakenShop.CrossSellProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrossSellProductViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_no_order_shop,parent,false)
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

            if(isFromCollection){
                try{
                    itemView.tv_row_shop_list_tv_text_dynamic.text = "last collection date"
                    var lastCollectD = AppDatabase.getDBInstance()?.collectionDetailsDao()?.getLastCollectionDate2(mList.get(adapterPosition).shop_id)
                    itemView.tv_row_shop_list_ma_shop_count.text =AppUtils.changeDateFormat1( lastCollectD!!).replace("/", "-")
                }catch (ex:Exception){
                    itemView.tv_row_shop_list_ma_shop_count.text ="N/A"
                }

            }else{
                try{
                    itemView.tv_row_shop_list_tv_text_dynamic.text = "last visit date"
                    var lastVisitedD = AppDatabase.getDBInstance()?.addShopEntryDao()?.getLastVisitedDate(mList.get(adapterPosition).shop_id)
                    itemView.tv_row_shop_list_ma_shop_count.text = AppUtils.changeDateFormat1(lastVisitedD!!).replace("/", "-")
                }catch (ex:Exception){
                    itemView.tv_row_shop_list_ma_shop_count.text ="N/A"
                }

            }




            /*if(adapterPosition%2 == 0){
                itemView.ll_row_pro_cross_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            }else{
                itemView.ll_row_pro_cross_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightest_light_new_gray))
            }*/

        }
    }

}