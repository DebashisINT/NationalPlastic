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
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_addr1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_name
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_shop_catagory_retailer1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_shop_type1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_name
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_phone_number
import kotlinx.android.synthetic.main.row_no_zero_order_shop.view.tv_row_shop_list_ma_shop_count

class AdapterNoOrderListInShop1(var mContext: Context, var mList:ArrayList<String>):
    RecyclerView.Adapter<AdapterNoOrderListInShop1.CrossSellProductViewHolder>(){

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

            var shopDtlsObj =  AppDatabase.getDBInstance()?.addShopEntryDao()?.getCustomShopDtls(mList.get(adapterPosition).toString())

            try{
                itemView.tv_row_shop_list_ma_name.text = shopDtlsObj!!.shop_name.get(0).toString()
                itemView.tv_row_shop_name.text = shopDtlsObj!!.shop_name
                itemView.tv_row_shop_phone_number.text = shopDtlsObj!!.owner_contact_number
                itemView.tv_row_shop_list_ma_addr1.text = shopDtlsObj!!.address

            }catch (ex:Exception){
                itemView.tv_row_shop_list_ma_name.text = "X"
                itemView.tv_row_shop_name.text = ""
                itemView.tv_row_shop_phone_number.text = ""
                itemView.tv_row_shop_list_ma_addr1.text = ""

            }

            itemView.tv_row_shop_phone_number.setOnClickListener {
                IntentActionable.initiatePhoneCall(mContext, shopDtlsObj!!.owner_contact_number)
            }

            try{
                var typeName = AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType(shopDtlsObj!!.type)!!.shoptype_name
                itemView.tv_row_shop_list_shop_type1.text =typeName
            }catch (ex:Exception){
                itemView.tv_row_shop_list_shop_type1.text ="N/A"
            }

            try{
                if(!shopDtlsObj!!.owner_name.equals("")){
                    itemView.tv_row_shop_list_ma_shop_catagory_retailer1.text = shopDtlsObj!!.owner_name
                }
                else{
                    itemView.tv_row_shop_list_ma_shop_catagory_retailer1.text = "N/A"
                }

            }catch (ex:Exception){
                itemView.tv_row_shop_list_ma_shop_catagory_retailer1.text = "N/A"
            }

            try{
                itemView.tv_row_shop_list_ma_shop_count.text = shopDtlsObj!!.age_since_party_creation_count.toDouble().toInt().toString()
            }catch (ex:Exception){
                itemView.tv_row_shop_list_ma_shop_count.text =  "N/A"
            }
        }
    }

}