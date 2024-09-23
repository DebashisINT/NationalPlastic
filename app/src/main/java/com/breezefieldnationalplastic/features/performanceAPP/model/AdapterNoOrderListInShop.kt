package com.breezefieldnationalplastic.features.performanceAPP.model

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.uiaction.IntentActionable
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.features.performanceAPP.NoOrderTakenList
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_addr1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_name
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_shop_catagory_retailer1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_shop_count
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_shop_type1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_tv_text_dynamic
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_name
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_phone_number


class AdapterNoOrderListInShop(var mContext: Context, var mList:ArrayList<NoOrderTakenList>,var isFromZeroOrderParty:Boolean=false):
    RecyclerView.Adapter<AdapterNoOrderListInShop.CrossSellProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrossSellProductViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_no_zero_order_shop,parent,false)
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

            try{
//                itemView.tv_row_shop_list_tv_text_dynamic.text = "age since party creation"
                itemView.tv_row_shop_list_tv_text_dynamic.text = "age since last visit"
//                var lastOrderD = AppDatabase.getDBInstance()?.orderDetailsListDao()?.getLastOrderDate(mList.get(adapterPosition).shop_id)
////                itemView.tv_row_shop_list_ma_shop_count.text = AppUtils.changeDateFormat2(lastVisitedD!!).replace("/", "_")
//                itemView.tv_row_shop_list_ma_shop_count.text = lastOrderD
                    itemView.tv_row_shop_list_ma_shop_count.text = mList.get(adapterPosition).age_since_party_creation_count.toDouble().toInt().toString()
            }catch (ex:Exception){
                itemView.tv_row_shop_list_ma_shop_count.text =  "N/A"
            }

            /*if(adapterPosition%2 == 0){
                itemView.ll_row_pro_cross_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            }else{
                itemView.ll_row_pro_cross_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightest_light_new_gray))
            }*/

        }
    }

}