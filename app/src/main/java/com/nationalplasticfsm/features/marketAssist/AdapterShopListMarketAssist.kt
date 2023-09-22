package com.nationalplasticfsm.features.marketAssist

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.AppDatabase
import com.nationalplasticfsm.app.types.FragType
import com.nationalplasticfsm.app.uiaction.IntentActionable
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.cv_row_shop_dtls_ma_contact
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.cv_row_shop_list_ma_beat_root
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.cv_row_shop_list_ma_catagory_retailer_root
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.cv_row_shop_list_ma_party_status_root
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.cv_shop_list_ma_direction
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.ll_row_shop_list_ma_assist
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.tv_row_shop_list_ma_addr
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.tv_row_shop_list_ma_name
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.tv_row_shop_list_ma_name_pref
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.tv_row_shop_list_ma_shop_beat
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.tv_row_shop_list_ma_shop_catagory_retailer
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.tv_row_shop_list_ma_shop_contact
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.tv_row_shop_list_ma_shop_party_status
import kotlinx.android.synthetic.main.row_shop_list_market_assist.view.tv_row_shop_list_ma_shop_type
import java.util.Random

class AdapterShopListMarketAssist(var mContext:Context,var mList:ArrayList<ShopDtls>):
RecyclerView.Adapter<AdapterShopListMarketAssist.ShopListMarketAssistViewHolder>(){

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListMarketAssistViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_shop_list_market_assist,parent,false)
        return ShopListMarketAssistViewHolder(v)
    }

    override fun onBindViewHolder(holder: ShopListMarketAssistViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ShopListMarketAssistViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        fun bindItems(){

            val m = random.nextInt(9 - 1) + 1
            itemView.tv_row_shop_list_ma_name_pref.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCodeL.get(m))))

            itemView.tv_row_shop_list_ma_name_pref.text = mList.get(adapterPosition).shop_name.get(0).toString()
            itemView.tv_row_shop_list_ma_name.text = mList.get(adapterPosition).shop_name
            itemView.tv_row_shop_list_ma_addr.text = mList.get(adapterPosition).address
            itemView.tv_row_shop_list_ma_shop_type.text = mList.get(adapterPosition).shopType
            itemView.tv_row_shop_list_ma_shop_contact.text = mList.get(adapterPosition).owner_contact_number
            if(mList.get(adapterPosition).beatName.equals("")){
                itemView.cv_row_shop_list_ma_beat_root.visibility = View.GONE
            }else{
                itemView.cv_row_shop_list_ma_beat_root.visibility = View.VISIBLE
                itemView.tv_row_shop_list_ma_shop_beat.text = mList.get(adapterPosition).beatName
            }


            itemView.ll_row_shop_list_ma_assist.setOnClickListener {
                (mContext as DashboardActivity).loadFragment(FragType.ShopDtlsMarketAssistFrag, true, mList.get(adapterPosition))
            }

            itemView.cv_shop_list_ma_direction.setOnClickListener {
                try{
                    var intentGmap: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${mList.get(adapterPosition).shopLat},${mList.get(adapterPosition).shopLong}&mode=1"))
                    intentGmap.setPackage("com.google.android.apps.maps")
                    if(intentGmap.resolveActivity(mContext!!.packageManager) !=null){
                        mContext!!.startActivity(intentGmap)
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
            }

            itemView.cv_row_shop_dtls_ma_contact.setOnClickListener {
                IntentActionable.initiatePhoneCall(mContext, mList.get(adapterPosition).owner_contact_number)
            }

            try{
                if(mList.get(adapterPosition).party_status_id.equals("")){
                    itemView.cv_row_shop_list_ma_party_status_root.visibility = View.GONE
                }else{
                    var partyStatusN = AppDatabase.getDBInstance()?.partyStatusDao()?.getSingleItem(mList.get(adapterPosition).party_status_id)!!.name
                    itemView.tv_row_shop_list_ma_shop_party_status.text = partyStatusN
                    itemView.cv_row_shop_list_ma_party_status_root.visibility = View.VISIBLE
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                itemView.cv_row_shop_list_ma_party_status_root.visibility = View.GONE
            }
            try{
                if(mList.get(adapterPosition).retailer_id.equals("")){
                    itemView.cv_row_shop_list_ma_catagory_retailer_root.visibility = View.GONE
                }else{
                    var catagoryRetailerN = AppDatabase.getDBInstance()?.retailerDao()?.getSingleItem(mList.get(adapterPosition).retailer_id.toString())!!.name
                    itemView.tv_row_shop_list_ma_shop_catagory_retailer.text = catagoryRetailerN
                    itemView.cv_row_shop_list_ma_catagory_retailer_root.visibility = View.VISIBLE
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                itemView.cv_row_shop_list_ma_catagory_retailer_root.visibility = View.GONE
            }

        }
    }

}