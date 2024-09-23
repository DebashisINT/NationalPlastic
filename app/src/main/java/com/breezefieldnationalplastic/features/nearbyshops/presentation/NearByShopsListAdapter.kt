package com.breezefieldnationalplastic.features.nearbyshops.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.net.Uri
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.domain.OrderDetailsListEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.FTStorageUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.location.LocationWizard
import com.breezefieldnationalplastic.features.location.SingleShotLocationProvider
import com.breezefieldnationalplastic.features.nearbyshops.model.NewOrderModel
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.inflate_registered_shops.view.activity_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.add_multiple_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.add_order_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.add_quot_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.avg_order_amount_tv
import kotlinx.android.synthetic.main.inflate_registered_shops.view.call_iv
import kotlinx.android.synthetic.main.inflate_registered_shops.view.call_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.call_log_his_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.call_log_his_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.call_tv
import kotlinx.android.synthetic.main.inflate_registered_shops.view.collection_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.direction_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.direction_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.high_value_month_tv
import kotlinx.android.synthetic.main.inflate_registered_shops.view.highest_order_amount_tv
import kotlinx.android.synthetic.main.inflate_registered_shops.view.iv_create_qr
import kotlinx.android.synthetic.main.inflate_registered_shops.view.iv_createorder
import kotlinx.android.synthetic.main.inflate_registered_shops.view.iv_range
import kotlinx.android.synthetic.main.inflate_registered_shops.view.iv_sms
import kotlinx.android.synthetic.main.inflate_registered_shops.view.iv_whatsapp
import kotlinx.android.synthetic.main.inflate_registered_shops.view.last_visited_date_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.lead_new_question_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.lead_new_question_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.lead_return_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.lead_return_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_activity
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_average_visit_time
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_collection
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_dd_name
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_distance
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_last_visit_age
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_nearby_shop_create_order_root
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_order
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_order_range
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_range
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_shop_code
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_shop_type
import kotlinx.android.synthetic.main.inflate_registered_shops.view.ll_stock
import kotlinx.android.synthetic.main.inflate_registered_shops.view.low_value_month_tv
import kotlinx.android.synthetic.main.inflate_registered_shops.view.lowest_order_amount_tv
import kotlinx.android.synthetic.main.inflate_registered_shops.view.myshop_Gstin_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.myshop_Pan_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.myshop_address_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.myshop_name_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.new_multi_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.next_visit_date_RL
import kotlinx.android.synthetic.main.inflate_registered_shops.view.next_visit_date_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.order_amount_tv
import kotlinx.android.synthetic.main.inflate_registered_shops.view.order_amt_p_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.order_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.rl_beat_type
import kotlinx.android.synthetic.main.inflate_registered_shops.view.rl_entity_type
import kotlinx.android.synthetic.main.inflate_registered_shops.view.rl_party_status
import kotlinx.android.synthetic.main.inflate_registered_shops.view.share_icon
import kotlinx.android.synthetic.main.inflate_registered_shops.view.share_loc_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.share_loc_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_IV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_damage_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_damage_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_extra_contact_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_extra_contact_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_history_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_history_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_image_IV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_list_LL
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_survey_ll
import kotlinx.android.synthetic.main.inflate_registered_shops.view.shop_survey_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.stock_view
import kotlinx.android.synthetic.main.inflate_registered_shops.view.sync_icon
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tag_iv
import kotlinx.android.synthetic.main.inflate_registered_shops.view.total_visited_value_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_avg_visit_time
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_beat_type
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_dd_name
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_distance
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_entity_type
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_funnel_stage
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_funnel_stage_header
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_last_visit_age
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_party_status
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_range
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_retailer_entity
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_retailer_entity_headerr
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_shop_code
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_shop_contact_no
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_stage
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_stage_header
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_type
import kotlinx.android.synthetic.main.inflate_registered_shops.view.tv_update_status_inflate_registered_shops
import kotlinx.android.synthetic.main.inflate_registered_shops.view.update_address_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.update_bank_details_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.update_party_status_TV
import kotlinx.android.synthetic.main.inflate_registered_shops.view.update_stage_TV
import timber.log.Timber


/**
 * Created by Pratishruti on 30-10-2017.
 */
//Revision History
// 1.0 NearByShopsListAdapter  AppV 4.0.6  Saheli   10/01/2023 phone number calling added
// 2.0 NearByShopsListAdapter  AppV 4.0.6  Saheli   11/01/2023 IsAllowShopStatusUpdate
// 3.0 NearByShopsListAdapter  AppV 4.0.6  Suman   31/01/2023 Retailer/Entity show from room db mantis_id 25636
class NearByShopsListAdapter(context: Context, list: List<AddShopDBModelEntity>, val listener: NearByShopsListClickListener) : RecyclerView.Adapter<NearByShopsListAdapter.MyViewHolder>() {

    private val layoutInflater: LayoutInflater
    private var context: Context
    private var mList: List<AddShopDBModelEntity>


    init {
        layoutInflater = LayoutInflater.from(context)
        this.context = context
        mList = list
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, mList, listener)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.inflate_registered_shops, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val newOrderList = ArrayList<NewOrderModel>()
        fun bindItems(context: Context, list: List<AddShopDBModelEntity>, listener: NearByShopsListClickListener) {
            //Picasso.with(context).load(list[adapterPosition].shopImageLocalPath).into(itemView.shop_image_IV)
            println("time_check NearByShopsListAdapterstart")
            try {
                if (!TextUtils.isEmpty(list[adapterPosition].shopImageLocalPath)) {
                    Picasso.get()
                            .load(list[adapterPosition].shopImageLocalPath)
                            .resize(100, 100)
                            .into(itemView.shop_image_IV)
                }
                itemView.myshop_name_TV.text = list[adapterPosition].shopName
                var address: String = list[adapterPosition].address + ", " + list[adapterPosition].pinCode
                itemView.myshop_address_TV.text = address

                /*if (list[adapterPosition].isAddressUpdated)
                    itemView.update_address_TV.visibility = View.GONE
                else
                    itemView.update_address_TV.visibility = View.VISIBLE*/

                if (list[adapterPosition].isUploaded) {
                    if (AppUtils.isAddressUpdated == "0")
                        itemView.update_address_TV.visibility = View.GONE
                    else {

                        val visitedShop = AppDatabase.getDBInstance()!!.shopActivityDao().getVisitedShopForDay(list[adapterPosition].shop_id, AppUtils.getCurrentDateForShopActi(),
                                true)

                        if (visitedShop == null){
                            itemView.update_address_TV.visibility = View.VISIBLE

                            // begin Suman 12-10-2023 mantis id 0026874
                            if(Pref.IsDisabledUpdateAddress && list[adapterPosition].isUpdateAddressFromShopMaster){
                                itemView.update_address_TV.visibility = View.GONE
                            }
                            // begin Suman 12-10-2023 mantis id 0026874

                        }
                        else
                            itemView.update_address_TV.visibility = View.GONE
                    }
                } else
                    itemView.update_address_TV.visibility = View.GONE

                val drawable = TextDrawable.builder()
                        .buildRoundRect(list[adapterPosition].shopName.trim().toUpperCase().take(1), ColorGenerator.MATERIAL.randomColor, 120)

                itemView.shop_IV.setImageDrawable(drawable)

                itemView.shop_image_IV.findViewById<ImageView>(R.id.shop_image_IV).setOnClickListener(View.OnClickListener {
                    listener.OnNearByShopsListClick(adapterPosition)
                })

                itemView.call_ll.findViewById<LinearLayout>(R.id.call_ll).setOnClickListener(View.OnClickListener {
                    listener.callClick(adapterPosition)
                })

                itemView.direction_ll.findViewById<LinearLayout>(R.id.direction_ll).setOnClickListener(View.OnClickListener {
                    //listener.mapClick(adapterPosition)
                    try{
                        //val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=22.497013652788425,88.3154464620276&destination=22.462972465878618,88.3071007426955&waypoints=22.475403007798953,88.30885895679373|22.471053209879425,88.3098540562982&travelmode=driving")
                        //val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=22.497013652788425,88.3154464620276&destination=22.462972465878618,88.3071007426955&waypoints=22.475403007798953,88.30885895679373|22.471053209879425,88.3098540562982&travelmode=driving&dir_action=navigate")
                        //val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=22.497013652788425,88.3154464620276&destination=22.462972465878618,88.3071007426955&waypoints=22.475403007798953,88.30885895679373|22.471053209879425,88.3098540562982&mode=1&dir_action=navigate")
                        //var intentGmap: Intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

                        var intentGmap: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${list[adapterPosition].shopLat},${list[adapterPosition].shopLong}&mode=1"))
                        intentGmap.setPackage("com.google.android.apps.maps")
                        if(intentGmap.resolveActivity(context.packageManager) !=null){
                            context.startActivity(intentGmap)
                        }
                    }catch (ex:Exception){
                        ex.printStackTrace()
                    }
                })

                itemView.add_order_ll.findViewById<LinearLayout>(R.id.add_order_ll).setOnClickListener(View.OnClickListener {
                    listener.orderClick(adapterPosition)
                    //(context as DashboardActivity).showSnackMessage(context.getString(R.string.functionality_disabled))
                })
                itemView.update_address_TV.findViewById<AppCustomTextView>(R.id.update_address_TV).setOnClickListener(View.OnClickListener {
                    listener.updateLocClick(adapterPosition)
                })



                itemView.order_amt_p_TV.text = " " + context.getString(R.string.zero_order_in_value)
                itemView.total_visited_value_TV.text = " " + list[adapterPosition].totalVisitCount
                itemView.last_visited_date_TV.text = " " + list[adapterPosition].lastVisitedDate
                itemView.sync_icon.visibility = View.VISIBLE
                if (list[adapterPosition].isUploaded) {
                    if (list[adapterPosition].isEditUploaded == 0) {
                        itemView.sync_icon.setImageResource(R.drawable.ic_registered_shop_not_sync)
                        itemView.sync_icon.setOnClickListener(View.OnClickListener {
                            listener.syncClick(adapterPosition)
                        })
                    } else
                        itemView.sync_icon.setImageResource(R.drawable.ic_registered_shop_sync)
                }
                else {
                    itemView.sync_icon.setImageResource(R.drawable.ic_registered_shop_not_sync)
                    itemView.sync_icon.setOnClickListener(View.OnClickListener {
                        listener.syncClick(adapterPosition)
                    })
                }

                val shopType = AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType(list[adapterPosition].type)

                if (shopType != null && !TextUtils.isEmpty(shopType.shoptype_name)) {
                    itemView.tv_type.text = shopType.shoptype_name
                    itemView.ll_shop_type.visibility = View.VISIBLE
                } else{
                    itemView.tv_type.text = "NA"
                    //itemView.ll_shop_type.visibility = View.GONE
                }
                // 2.0 NearByShopsListAdapter  AppV 4.0.6 IsAllowShopStatusUpdate
                if(Pref.IsAllowShopStatusUpdate) {
                    itemView.tv_update_status_inflate_registered_shops.visibility = View.VISIBLE
                }
                else {
                    itemView.tv_update_status_inflate_registered_shops.visibility = View.GONE
                }

                itemView.tv_update_status_inflate_registered_shops.setOnClickListener {
                    listener.onUpdateStatusClick(list[adapterPosition])
                }
                if(Pref.isCollectioninMenuShow) {
                    itemView.ll_collection.visibility = View.VISIBLE
                    itemView.collection_view.visibility = View.VISIBLE
                }
                else {
                    itemView.ll_collection.visibility = View.GONE
                    itemView.collection_view.visibility = View.GONE
                }
                /*Beat Name show*/
                try{
                    val shopBeatType = AppDatabase.getDBInstance()?.beatDao()?.getSingleItem(list[adapterPosition].beat_id)
                    if(shopBeatType!=null && Pref.isShowBeatGroup && !TextUtils.isEmpty(shopBeatType.name)) {
                        itemView.rl_beat_type.visibility = View.VISIBLE
                        itemView.tv_beat_type.text = shopBeatType.name
                    }
                    else {
                        itemView.tv_beat_type.text ="NA"
                    }
                }catch (ex:Exception){
                   ex.printStackTrace()
                }



                itemView.ll_collection.setOnClickListener {
                    listener.onCollectionClick(adapterPosition)
                }

                itemView.shop_list_LL.setOnClickListener(View.OnClickListener {
                    listener.OnNearByShopsListClick(adapterPosition)
                })

                itemView.tag_iv.setOnClickListener(View.OnClickListener {
                    (context as DashboardActivity).loadFragment(FragType.MarketingPagerFragment, true, list[adapterPosition].shop_id)
                })


                itemView.ll_stock.setOnClickListener {
                    listener.onStockClick(adapterPosition)
                }

                itemView.update_stage_TV.setOnClickListener {
                    listener.onUpdateStageClick(adapterPosition)
                }

                itemView.add_quot_ll.setOnClickListener {
                    listener.onQuotationClick(adapterPosition)
                }

                itemView.ll_activity.setOnClickListener {
                    listener.onActivityClick(adapterPosition)
                }

                itemView.share_icon.setOnClickListener {
                    listener.onShareClick(adapterPosition)
                }
                itemView.share_loc_ll.setOnClickListener {
                    listener.onLocationShareClick(adapterPosition)
                }

                itemView.lead_new_question_ll.setOnClickListener {
                    listener.onQuestionnarieClick(list[adapterPosition].shop_id!!)
                }
                /*17-12-2021 modify*/
                if(Pref.IsReturnEnableforParty) {
                    if(Pref.IsReturnActivatedforPP){
                        if(list[adapterPosition].type!!.equals("2")){
                            itemView.lead_return_ll.visibility = View.VISIBLE
                            itemView.lead_return_view.visibility =  View.VISIBLE
                        }
                        else{
                            itemView.lead_return_ll.visibility = View.GONE
                            itemView.lead_return_view.visibility = View.GONE
                        }
                    }
                    else if(Pref.IsReturnActivatedforDD){
                        if(list[adapterPosition].type!!.equals("4")){
                            itemView.lead_return_ll.visibility = View.VISIBLE
                            itemView.lead_return_view.visibility =  View.VISIBLE
                        }
                        else{
                            itemView.lead_return_ll.visibility = View.GONE
                            itemView.lead_return_view.visibility = View.GONE
                        }
                    }
                    else if(Pref.IsReturnActivatedforSHOP){
                        if(list[adapterPosition].type!!.equals("1")){
                            itemView.lead_return_ll.visibility = View.VISIBLE
                            itemView.lead_return_view.visibility =  View.VISIBLE
                        }
                        else{
                            itemView.lead_return_ll.visibility = View.GONE
                            itemView.lead_return_view.visibility = View.GONE
                        }
                    }
                }
                else{
                    itemView.lead_return_ll.visibility = View.GONE
                    itemView.lead_return_view.visibility = View.GONE
                }

                /*Survey Icon*/
                if(Pref.IsSurveyRequiredforDealer && list[adapterPosition].type!!.equals("1")) {
                        itemView.shop_survey_ll.visibility = View.VISIBLE
                        itemView.shop_survey_view.visibility = View.VISIBLE
                }
                else if(Pref.IsSurveyRequiredforNewParty && list[adapterPosition].type!!.equals("3")){
                        itemView.shop_survey_ll.visibility = View.VISIBLE
                        itemView.shop_survey_view.visibility = View.VISIBLE
                }
                else{
                    itemView.shop_survey_ll.visibility = View.GONE
                    itemView.shop_survey_view.visibility = View.GONE
                }

                /*20-12-2021*/
                val OrderavalibleByShopId = AppDatabase.getDBInstance()?.orderDetailsListDao()?.getListAccordingToShopId(list[adapterPosition].shop_id) as ArrayList<OrderDetailsListEntity>
                if(OrderavalibleByShopId.size>0){
                    //itemView.lead_return_ll.isEnabled=true
                    itemView.lead_return_ll.setOnClickListener {
                        listener.onReturnClick(adapterPosition)
                    }
                }
                else{
                    itemView.lead_return_ll.setOnClickListener {
                        Toaster.msgShort(context,"No Minimum Order Avalible to return.")
                    }
                    //itemView.lead_return_ll.isEnabled=false
                }


                if(Pref.IsnewleadtypeforRuby && list[adapterPosition].type.equals("16")){
                    itemView.lead_new_question_view.visibility = View.VISIBLE
                    itemView.lead_new_question_ll.visibility = View.VISIBLE
                }
                else{
                    itemView.lead_new_question_view.visibility = View.GONE
                    itemView.lead_new_question_ll.visibility = View.GONE
                }



                if (list[adapterPosition].is_otp_verified.equals("true", ignoreCase = true)) {
                    itemView.call_tv.text = context.getString(R.string.verified)
                    itemView.call_tv.setTextColor(context.resources.getColor(R.color.colorPrimary))
                    itemView.call_iv.setImageResource(R.drawable.ic_registered_shop_call_select_green)
                    itemView.call_iv.setColorFilter( Color.parseColor("#119C25"), PorterDuff.Mode.SRC_IN)
                } else {
                    itemView.call_tv.text = context.getString(R.string.unverified)
                    itemView.call_tv.setTextColor(context.resources.getColor(R.color.login_txt_color))
                    itemView.call_iv.setImageResource(R.drawable.ic_registered_shop_call_deselect)
                    itemView.call_iv.setColorFilter( Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN)
                }

                val orderList = AppDatabase.getDBInstance()!!.orderDetailsListDao().getListAccordingToShopId(list[adapterPosition].shop_id) as ArrayList<OrderDetailsListEntity>
                if (orderList != null && orderList.isNotEmpty()) {
                    /*itemView.order_amount_tv.visibility = View.VISIBLE
                    itemView.highest_order_amount_tv.visibility = View.VISIBLE
                    itemView.avg_order_amount_tv.visibility = View.VISIBLE
                    itemView.lowest_order_amount_tv.visibility = View.VISIBLE
                    itemView.high_value_month_tv.visibility = View.VISIBLE
                    itemView.low_value_month_tv.visibility = View.VISIBLE*/

                    val amountList = ArrayList<Double>()
                    var amount = 0.0
                    var month = ""
                    var year = ""
                    var newAmount = 0.0
                    var max = 0.0
                    var min = 0.0
                    var maxIndex = 0
                    var minIndex = 0

                    for (i in orderList.indices) {
                        if (!TextUtils.isEmpty(orderList[i].amount)) {
                            amount += orderList[i].amount?.toDouble()!!
                            amountList.add(orderList[i].amount?.toDouble()!!)

                            if (i == 0) {
                                newAmount = orderList[i].amount?.toDouble()!!
                                month = AppUtils.getMonthNoFromReverseFormat(AppUtils.getCurrentDateFormatInTa(orderList[i].only_date!!))
                                year = AppUtils.getYearFromReverseFormat(AppUtils.getCurrentDateFormatInTa(orderList[i].only_date!!))

                                if (i == orderList.size - 1)
                                    newOrderList.add(NewOrderModel(newAmount, month, year))
                            }
                            else if (i == orderList.size - 1) {
                                val newMonth = AppUtils.getMonthNoFromReverseFormat(AppUtils.getCurrentDateFormatInTa(orderList[i].only_date!!))
                                val newYear = AppUtils.getYearFromReverseFormat(AppUtils.getCurrentDateFormatInTa(orderList[i].only_date!!))

                                if (month == newMonth && year == newYear) {
                                    newAmount += orderList[i].amount?.toDouble()!!
                                    newOrderList.add(NewOrderModel(newAmount, newMonth, newYear))
                                }
                                else {
                                    newOrderList.add(NewOrderModel(newAmount, month, year))
                                    newOrderList.add(NewOrderModel(orderList[i].amount?.toDouble()!!, newMonth, newYear))
                                }
                            }
                            else {
                                val newMonth = AppUtils.getMonthNoFromReverseFormat(AppUtils.getCurrentDateFormatInTa(orderList[i].only_date!!))
                                val newYear = AppUtils.getYearFromReverseFormat(AppUtils.getCurrentDateFormatInTa(orderList[i].only_date!!))

                                if (month == newMonth && year == newYear) {
                                    newAmount += orderList[i].amount?.toDouble()!!
                                    //newOrderList.add(NewOrderModel(newAmount, newMonth, newYear))
                                }
                                else {
                                    newOrderList.add(NewOrderModel(newAmount, month, year))
                                    newAmount = orderList[i].amount?.toDouble()!!
                                }

                                month = newMonth
                                year = newYear
                            }
                        }
                    }

                    //val finalAmount = String.format("%.2f", amount.toFloat())
                    //mantis id 26274
                    val finalAmount = String.format("%.2f", amount.toDouble())

                    val builder = SpannableStringBuilder()

                    val str1 = SpannableString("Total Order Value (till now): ")
                    builder.append(str1)

                    val str2 = SpannableString("₹ $finalAmount")
                    str2.setSpan(ForegroundColorSpan(Color.BLACK), 0, str2.length, 0)
                    builder.append(str2)
                    itemView.order_amount_tv.setText(builder, TextView.BufferType.SPANNABLE)

                    builder.clear()
                    val str3 = SpannableString("Average Order Value (till now): ")
                    builder.append(str3)

                    var avgOrder = "0.00"
                    if (amount.toInt() != 0){
                        //avgOrder = String.format("%.2f", (amount.toFloat() / orderList.size))
                        //mantis id 26274
                        avgOrder = String.format("%.2f", (amount.toDouble() / orderList.size).toDouble())
                    }
                    val str4 = SpannableString("₹ $avgOrder")
                    str4.setSpan(ForegroundColorSpan(Color.BLACK), 0, str4.length, 0)
                    builder.append(str4)
                    itemView.avg_order_amount_tv.setText(builder, TextView.BufferType.SPANNABLE)

                    builder.clear()
                    val str5 = SpannableString("Highest Order Value (till now): ")
                    builder.append(str5)

                    var maxOrder = "0.00"
                    if (amountList.isNotEmpty()){
                        //maxOrder = String.format("%.2f", amountList.maxOrNull()?.toFloat())
                        //mantis id 26274
                        maxOrder = String.format("%.2f", amountList.maxOrNull()?.toDouble())
                    }
                    val str6 = SpannableString("₹ $maxOrder")
                    str6.setSpan(ForegroundColorSpan(Color.BLACK), 0, str6.length, 0)
                    builder.append(str6)
                    itemView.highest_order_amount_tv.setText(builder, TextView.BufferType.SPANNABLE)

                    builder.clear()
                    val str7 = SpannableString("Lowest Order Value (till now): ")
                    builder.append(str7)

                    var minOrder = "0.00"
                    if (amountList.isNotEmpty()){
                        //minOrder = String.format("%.2f", amountList.minOrNull()?.toFloat())
                        //mantis id 26274
                        minOrder = String.format("%.2f", amountList.minOrNull()?.toDouble())
                    }
                    val str8 = SpannableString("₹ $minOrder")
                    str8.setSpan(ForegroundColorSpan(Color.BLACK), 0, str8.length, 0)
                    builder.append(str8)
                    itemView.lowest_order_amount_tv.setText(builder, TextView.BufferType.SPANNABLE)

                    newOrderList.forEachIndexed { i, it ->
                        if (i == 0) {
                            max = it.amount
                            min = it.amount
                            maxIndex = i
                            minIndex = i
                        }
                        else {
                            if (it.amount > max) {
                                max = it.amount
                                maxIndex = i
                            }

                            if (it.amount < min) {
                                min = it.amount
                                minIndex = i
                            }
                        }
                    }

                    builder.clear()
                    val str9 = SpannableString("Month of High value Business: ")
                    builder.append(str9)

                    val str10 = SpannableString(AppUtils.getMonthFromValue(newOrderList[maxIndex].month) + ", " + newOrderList[maxIndex].year)
                    str10.setSpan(ForegroundColorSpan(Color.BLACK), 0, str10.length, 0)
                    builder.append(str10)
                    itemView.high_value_month_tv.setText(builder, TextView.BufferType.SPANNABLE)

                    builder.clear()
                    val str11 = SpannableString("Month of Low value Business: ")
                    builder.append(str11)

                    val str12 = SpannableString(AppUtils.getMonthFromValue(newOrderList[minIndex].month) + ", " + newOrderList[minIndex].year)
                    str12.setSpan(ForegroundColorSpan(Color.BLACK), 0, str12.length, 0)
                    builder.append(str12)
                    itemView.low_value_month_tv.setText(builder, TextView.BufferType.SPANNABLE)
                }
                else {
                    /*itemView.order_amount_tv.visibility = View.GONE
                    itemView.highest_order_amount_tv.visibility = View.GONE
                    itemView.avg_order_amount_tv.visibility = View.GONE
                    itemView.lowest_order_amount_tv.visibility = View.GONE
                    itemView.high_value_month_tv.visibility = View.GONE
                    itemView.low_value_month_tv.visibility = View.GONE*/

                    val builder = SpannableStringBuilder()

                    val str1 = SpannableString("Total Order Value (till now): ")
                    builder.append(str1)

                    val str2 = SpannableString("₹ 0.00")
                    str2.setSpan(ForegroundColorSpan(Color.BLACK), 0, str2.length, 0)
                    builder.append(str2)
                    itemView.order_amount_tv.setText(builder, TextView.BufferType.SPANNABLE)

                    builder.clear()
                    val str3 = SpannableString("Average Order Value (till now): ")
                    builder.append(str3)

                    val str4 = SpannableString("₹ 0.00")
                    str4.setSpan(ForegroundColorSpan(Color.BLACK), 0, str4.length, 0)
                    builder.append(str4)
                    itemView.avg_order_amount_tv.setText(builder, TextView.BufferType.SPANNABLE)

                    builder.clear()
                    val str5 = SpannableString("Highest Order Value (till now): ")
                    builder.append(str5)

                    val str6 = SpannableString("₹ 0.00")
                    str6.setSpan(ForegroundColorSpan(Color.BLACK), 0, str6.length, 0)
                    builder.append(str6)
                    itemView.highest_order_amount_tv.setText(builder, TextView.BufferType.SPANNABLE)

                    builder.clear()
                    val str7 = SpannableString("Lowest Order Value (till now): ")
                    builder.append(str7)

                    val str8 = SpannableString("₹ 0.00")
                    str8.setSpan(ForegroundColorSpan(Color.BLACK), 0, str8.length, 0)
                    builder.append(str8)
                    itemView.lowest_order_amount_tv.setText(builder, TextView.BufferType.SPANNABLE)

                    builder.clear()
                    val str9 = SpannableString("Month of High value Business: ")
                    builder.append(str9)

                    val str10 = SpannableString("N.A.")
                    str10.setSpan(ForegroundColorSpan(Color.BLACK), 0, str10.length, 0)
                    builder.append(str10)
                    itemView.high_value_month_tv.setText(builder, TextView.BufferType.SPANNABLE)

                    builder.clear()
                    val str11 = SpannableString("Month of Low value Business: ")
                    builder.append(str11)

                    val str12 = SpannableString("N.A.")
                    str12.setSpan(ForegroundColorSpan(Color.BLACK), 0, str12.length, 0)
                    builder.append(str12)
                    itemView.low_value_month_tv.setText(builder, TextView.BufferType.SPANNABLE)
                }

                if (Pref.willStockShow) {
                    if (Pref.isStockAvailableForAll) {
                        itemView.ll_stock.visibility = View.VISIBLE
                        itemView.stock_view.visibility = View.VISIBLE
                    } else {
                        if (list[adapterPosition].type == "4") {
                            itemView.ll_stock.visibility = View.VISIBLE
                            itemView.stock_view.visibility = View.VISIBLE
                        } else {
                            itemView.ll_stock.visibility = View.GONE
                            itemView.stock_view.visibility = View.GONE
                        }
                    }
                }
                else {
                    itemView.ll_stock.visibility = View.GONE
                    itemView.stock_view.visibility = View.GONE
                }
                itemView.tv_shop_contact_no.text = list[adapterPosition].ownerName + " (${list[adapterPosition].ownerContactNumber})"

                // 1.0 NearByShopsListAdapter phone number calling added
                itemView.tv_shop_contact_no.setOnClickListener(View.OnClickListener {
                    listener.callClick(adapterPosition)
                })

                if (Pref.isOrderShow) {
                    itemView.add_order_ll.visibility = View.VISIBLE
                    itemView.direction_view.visibility = View.VISIBLE
                } else {
                    itemView.add_order_ll.visibility = View.GONE
                    itemView.direction_view.visibility = View.GONE
                }


                if (list[adapterPosition].type == "7" || list[adapterPosition].type == "8") {
                    itemView.ll_activity.visibility = View.VISIBLE
                    itemView.activity_view.visibility = View.VISIBLE
                }
                else if (Pref.willActivityShow) {
                    itemView.ll_activity.visibility = View.VISIBLE
                    itemView.activity_view.visibility = View.VISIBLE
                }
                else {
                    itemView.ll_activity.visibility = View.GONE
                    itemView.activity_view.visibility = View.GONE
                }

                if (Pref.isEntityCodeVisible) {
                    if (!TextUtils.isEmpty(list[adapterPosition].entity_code)) {
                        itemView.ll_shop_code.visibility = View.VISIBLE
                        itemView.tv_shop_code.text = list[adapterPosition].entity_code
                    } else
                        itemView.ll_shop_code.visibility = View.GONE
                } else
                    itemView.ll_shop_code.visibility = View.GONE


                Log.e("Shop List", "Shop name=======> " + list[adapterPosition].shopName)
                Log.e("Shop List", "Shop Id=======> " + list[adapterPosition].shop_id)
                Log.e("Shop List", "Stage Id=======> " + list[adapterPosition].stage_id)

                if (Pref.isCustomerFeatureEnable) {

                    itemView.ll_dd_name.visibility = View.GONE
                    itemView.update_stage_TV.visibility = View.VISIBLE

                    if (!TextUtils.isEmpty(list[adapterPosition].stage_id)) {
                        val stage = AppDatabase.getDBInstance()?.stageDao()?.getSingleType(list[adapterPosition].stage_id)

                        if (stage == null) {
                            itemView.tv_stage_header.visibility = View.GONE
                            itemView.tv_stage.visibility = View.GONE
                        } else {
                            itemView.tv_stage_header.visibility = View.VISIBLE
                            itemView.tv_stage.visibility = View.VISIBLE

                            itemView.tv_stage.text = stage.stage_name
                        }
                    } else {
                        itemView.tv_stage_header.visibility = View.GONE
                        itemView.tv_stage.visibility = View.GONE
                    }

                    if (!TextUtils.isEmpty(list[adapterPosition].funnel_stage_id)) {
                        val funnelStage = AppDatabase.getDBInstance()?.funnelStageDao()?.getSingleType(list[adapterPosition].funnel_stage_id)

                        if (funnelStage == null) {
                            itemView.tv_funnel_stage_header.visibility = View.GONE
                            itemView.tv_funnel_stage.visibility = View.GONE
                        } else {
                            itemView.tv_funnel_stage_header.visibility = View.VISIBLE
                            itemView.tv_funnel_stage.visibility = View.VISIBLE

                            itemView.tv_funnel_stage.text = funnelStage.funnel_stage_name
                        }
                    } else {
                        itemView.tv_funnel_stage_header.visibility = View.GONE
                        itemView.tv_funnel_stage.visibility = View.GONE
                    }

                    if (Pref.isQuotationShow) {
                        itemView.add_quot_ll.visibility = View.VISIBLE
                        itemView.order_view.visibility = View.VISIBLE
                    } else {
                        itemView.add_quot_ll.visibility = View.GONE
                        itemView.order_view.visibility = View.GONE
                    }
                }
                else {
                    itemView.add_quot_ll.visibility = View.GONE
                    itemView.order_view.visibility = View.GONE
                    itemView.tv_funnel_stage_header.visibility = View.GONE
                    itemView.tv_funnel_stage.visibility = View.GONE
                    itemView.tv_stage_header.visibility = View.GONE
                    itemView.tv_stage.visibility = View.GONE
                    itemView.update_stage_TV.visibility = View.GONE

                    when (list[adapterPosition].type) {
                        "1" -> {
                            /*itemView.run {
                                            tv_type.text = context.getString(R.string.shop_type)
                                        }*/

                            if (!TextUtils.isEmpty(list[adapterPosition].assigned_to_dd_id)) {
                                list[adapterPosition].assigned_to_dd_id.let {
                                    AppDatabase.getDBInstance()?.ddListDao()?.getSingleValue(it)
                                }?.run {
                                    itemView.also {
                                        if (!TextUtils.isEmpty(dd_name)) {
                                            it.tv_dd_name.text = dd_name
                                            it.ll_dd_name.visibility = View.VISIBLE
                                        } else
                                            it.ll_dd_name.visibility = View.GONE
                                    }
                                }
                            } else
                                itemView.ll_dd_name.visibility = View.GONE

                        }
                        "2" -> {
                            itemView.run {
                                //tv_type.text = context.getString(R.string.pp_type)
                                ll_dd_name.visibility = View.GONE
                            }
                        }
                        "3" -> {
                            itemView.run {
                                //tv_type.text = context.getString(R.string.new_party_type)
                                ll_dd_name.visibility = View.GONE
                            }
                        }
                        "4" -> {
                            itemView.run {
                                //tv_type.text = context.getString(R.string.distributor_type)
                                ll_dd_name.visibility = View.GONE
                            }
                        }
                        "5" -> {
                            /*itemView.run {
                                            tv_type.text = context.getString(R.string.diamond_type)
                                        }*/

                            if (!TextUtils.isEmpty(list[adapterPosition].assigned_to_dd_id)) {
                                list[adapterPosition].assigned_to_dd_id.let {
                                    AppDatabase.getDBInstance()?.ddListDao()?.getSingleValue(it)
                                }?.run {
                                    itemView.also {
                                        if (!TextUtils.isEmpty(dd_name)) {
                                            it.tv_dd_name.text = dd_name
                                            it.ll_dd_name.visibility = View.VISIBLE
                                        } else
                                            it.ll_dd_name.visibility = View.GONE
                                    }
                                }
                            } else
                                itemView.ll_dd_name.visibility = View.GONE
                        }
                        else -> {
                            itemView.ll_dd_name.visibility = View.GONE
                        }
                    }
                    /*AutoDDSelect Feature*/
                    if(Pref.AutoDDSelect){
                        itemView.ll_dd_name.visibility = View.VISIBLE
                    }
                    else{
                        itemView.ll_dd_name.visibility = View.GONE
                    }
                }

                val shopActivityList = AppDatabase.getDBInstance()?.shopActivityDao()?.getShopActivityForIdDescVisitDate(list[adapterPosition].shop_id)
                if (shopActivityList != null && shopActivityList.isNotEmpty()) {

                    var averageTimeSpent = ""
                    var totalTimeSpent = 0L

                    shopActivityList.forEach {
                        if (!TextUtils.isEmpty(it.totalMinute))
                            totalTimeSpent += it.totalMinute.toLong()
                    }

                    averageTimeSpent = if (!TextUtils.isEmpty(list[adapterPosition].totalVisitCount) && totalTimeSpent != 0L)
                        AppUtils.getHourMinuteFromMins(totalTimeSpent / list[adapterPosition].totalVisitCount.toLong())
                    else
                        "00:00"

                    itemView.tv_avg_visit_time.text = "$averageTimeSpent (hh:mm Approx.)"


                    if (!TextUtils.isEmpty(shopActivityList[0].next_visit_date)) {
                        itemView.next_visit_date_RL.visibility = View.VISIBLE
                        itemView.next_visit_date_TV.text = AppUtils.changeAttendanceDateFormat(shopActivityList[0].next_visit_date!!)

                        if (shopActivityList[0].next_visit_date == AppUtils.getCurrentDateForShopActi())
                            itemView.next_visit_date_TV.setTextColor(context.resources.getColor(android.R.color.holo_green_dark))
                        else
                            itemView.next_visit_date_TV.setTextColor(context.resources.getColor(R.color.login_txt_color))
                    } else
                        itemView.next_visit_date_RL.visibility = View.GONE
                } else
                    itemView.next_visit_date_RL.visibility = View.GONE

                if(Pref.ShowApproxDistanceInNearbyShopList){
                    itemView.ll_distance.visibility = View.VISIBLE
                val distance = LocationWizard.getDistance(list[adapterPosition].shopLat, list[adapterPosition].shopLong,
                        Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())
                itemView.tv_distance.text = "$distance (Approx. from current location)"
                }
                else{
                    itemView.ll_distance.visibility = View.GONE
                }

                itemView.iv_whatsapp.setOnClickListener {
                    listener.onWhatsAppClick(list[adapterPosition].ownerContactNumber)
                }

                if (Pref.isShowSmsForParty)
                    itemView.iv_sms.visibility = View.VISIBLE
                else
                    itemView.iv_sms.visibility = View.GONE

                itemView.iv_sms.setOnClickListener {
                    listener.onSmsClick(list[adapterPosition].ownerContactNumber)
                }

                val lastVisitAge = AppUtils.getDayFromSubtractDates(AppUtils.getLongTimeStampFromDate2(list[adapterPosition].lastVisitedDate),
                        AppUtils.convertDateStringToLong(AppUtils.getCurrentDateForShopActi()))
                itemView.tv_last_visit_age.text = "$lastVisitAge Day(s)"

                if (Pref.isCreateQrCode)
                    itemView.iv_create_qr.visibility = View.VISIBLE
                else
                    itemView.iv_create_qr.visibility = View.GONE

                itemView.iv_create_qr.setOnClickListener {
                    listener.onCreateQrClick(adapterPosition)
                }

                if (Pref.willShowPartyStatus) {
                    itemView.rl_party_status.visibility = View.VISIBLE
                    itemView.update_party_status_TV.visibility = View.VISIBLE
                }
                else {
                    itemView.rl_party_status.visibility = View.GONE
                    itemView.update_party_status_TV.visibility = View.GONE
                }

                if (Pref.willShowEntityTypeforShop && list[adapterPosition].type == "1")
                    itemView.rl_entity_type.visibility = View.VISIBLE
                else
                    itemView.rl_entity_type.visibility = View.GONE

                if (!TextUtils.isEmpty(list[adapterPosition].entity_id)) {
                    val entity = AppDatabase.getDBInstance()?.entityDao()?.getSingleItem(list[adapterPosition].entity_id)
                    itemView.tv_entity_type.text = entity?.name
                }
                else
                    itemView.tv_entity_type.text = "N.A."

                if (!TextUtils.isEmpty(list[adapterPosition].party_status_id)) {
                    val partyStatus = AppDatabase.getDBInstance()?.partyStatusDao()?.getSingleItem(list[adapterPosition].party_status_id)
                    itemView.tv_party_status.text = partyStatus?.name
                }
                else
                    itemView.tv_party_status.text = "N.A."

                itemView.tv_retailer_entity_headerr.text = "Party Category: "
                try{
                    if(list[adapterPosition].retailer_id == null || list[adapterPosition].retailer_id.equals("")){
                        itemView.tv_retailer_entity.text = "N.A."
                    }else{
                        itemView.tv_retailer_entity.text = AppDatabase.getDBInstance()?.retailerDao()?.getSingleItem(list[adapterPosition].retailer_id.toString())!!.name
                    }
                }catch (ex:Exception){
                    itemView.tv_retailer_entity.text = "N.A."
                }

                itemView.update_party_status_TV.setOnClickListener {
                    listener.onUpdatePartyStatusClick(adapterPosition)
                }

                if (Pref.isShowBankDetailsForShop)
                    itemView.update_bank_details_TV.visibility = View.VISIBLE
                else
                    itemView.update_bank_details_TV.visibility = View.GONE


                itemView.update_bank_details_TV.setOnClickListener{
                    listener.onUpdateBankDetailsClick(adapterPosition)
                }

                if(Pref.IsFeedbackHistoryActivated){
                    itemView.shop_history_ll.visibility = View.VISIBLE
                    itemView.shop_history_view.visibility = View.VISIBLE
                }else{
                    itemView.shop_history_ll.visibility = View.GONE
                    itemView.shop_history_view.visibility = View.GONE
                }


                itemView.shop_history_ll.setOnClickListener {
                    listener.onHistoryClick(list[adapterPosition])
                }

                if (Pref.IsAllowBreakageTracking) {
                    itemView.shop_damage_ll.visibility = View.VISIBLE
                    itemView.shop_damage_view.visibility = View.VISIBLE
                }
                else {
                    itemView.shop_damage_ll.visibility = View.GONE
                    itemView.shop_damage_view.visibility = View.GONE
                }

                itemView.shop_damage_ll.setOnClickListener{
                    listener.onDamageClick(list[adapterPosition].shop_id)
                }
                itemView.shop_survey_ll.setOnClickListener{
                    listener.onSurveyClick(list[adapterPosition].shop_id)
                }
                if(Pref.IsMultipleContactEnableforShop){
                    itemView.shop_extra_contact_ll.visibility = View.VISIBLE
                    itemView.shop_extra_contact_view.visibility = View.VISIBLE
                }else{
                    itemView.shop_extra_contact_ll.visibility = View.GONE
                    itemView.shop_extra_contact_view.visibility = View.GONE
                }
                itemView.shop_extra_contact_ll.setOnClickListener{
                    listener.onExtraContactClick(list[adapterPosition].shop_id)
                }

                // 3.0 Pref  AppV 4.0.7 Suman    10/03/2023 Pdf generation settings wise  mantis 25650
                //Hardcoded for EuroBond
                if(Pref.IsShowQuotationFooterforEurobond){
                itemView.ll_last_visit_age.visibility=View.GONE
                itemView.ll_average_visit_time.visibility=View.GONE
                itemView.ll_distance.visibility=View.GONE
                itemView.order_amount_tv.visibility=View.GONE
                itemView.highest_order_amount_tv.visibility=View.GONE
                itemView.avg_order_amount_tv.visibility=View.GONE
                itemView.lowest_order_amount_tv.visibility=View.GONE
                itemView.high_value_month_tv.visibility=View.GONE
                itemView.low_value_month_tv.visibility=View.GONE
                }

                // 3.0 Pref  AppV 4.0.7 Suman    10/03/2023 Pdf generation settings wise  mantis 25650
                //Hardcoded for Pure chemical
                if(!Pref.IsShowOtherInfoinShopMaster){
                    itemView.ll_last_visit_age.visibility=View.GONE
                    itemView.ll_average_visit_time.visibility=View.GONE
                    itemView.ll_distance.visibility=View.GONE
                    itemView.order_amount_tv.visibility=View.GONE
                    itemView.highest_order_amount_tv.visibility=View.GONE
                    itemView.avg_order_amount_tv.visibility=View.GONE
                    itemView.lowest_order_amount_tv.visibility=View.GONE
                    itemView.high_value_month_tv.visibility=View.GONE
                    itemView.low_value_month_tv.visibility=View.GONE
                    itemView.tv_funnel_stage_header.visibility = View.GONE
                    itemView.tv_funnel_stage.visibility = View.GONE
                    itemView.rl_beat_type.visibility = View.GONE
                    itemView.rl_entity_type.visibility = View.GONE
                    itemView.rl_party_status.visibility = View.GONE
                    itemView.next_visit_date_RL.visibility = View.GONE
                    itemView.ll_shop_code.visibility = View.GONE
                }


            } catch (e: Exception) {
                e.printStackTrace()
                itemView.order_amount_tv.visibility = View.GONE
            }

            try{
                if(Pref.IsGSTINPANEnableInShop) {
                    if (list[adapterPosition].gstN_Number.isNotEmpty()) {
                        itemView.myshop_Gstin_TV.text = "GSTIN : " + list[adapterPosition].gstN_Number
                        itemView.myshop_Gstin_TV.visibility = View.VISIBLE
                    } else {
                        itemView.myshop_Gstin_TV.text = "GSTIN : " + "N.A"
                        itemView.myshop_Gstin_TV.visibility = View.VISIBLE
                    }
                }else{
                    itemView.myshop_Gstin_TV.visibility = View.GONE
                }
                if(Pref.IsGSTINPANEnableInShop) {
                    if (list[adapterPosition].shopOwner_PAN.isNotEmpty()) {
                        itemView.myshop_Pan_TV.text = "PAN     : " + list[adapterPosition].shopOwner_PAN
                        itemView.myshop_Pan_TV.visibility = View.VISIBLE
                    } else {
                        itemView.myshop_Pan_TV.text = "PAN     : " + "N.A"
                        itemView.myshop_Pan_TV.visibility = View.VISIBLE
                    }
                }else{
                    itemView.myshop_Pan_TV.visibility = View.GONE
                }
            }
            catch (ex:Exception){
                itemView.myshop_Gstin_TV.text =  "GSTIN : "+"N.A"
                itemView.myshop_Pan_TV.text = "PAN     : "+"N.A"
            }


                    if(Pref.IsMultipleImagesRequired){
                        itemView.add_multiple_ll.visibility = View.VISIBLE
                        itemView.new_multi_view.visibility = View.VISIBLE
                        itemView.add_multiple_ll.setOnClickListener {
                            listener.onMultipleImageClick(list[adapterPosition],adapterPosition)
                        }
                    }
                    else{
                        itemView.add_multiple_ll.visibility = View.GONE
                        itemView.new_multi_view.visibility = View.GONE
                    }

            if(Pref.IsCallLogHistoryActivated){
                itemView.call_log_his_ll.visibility = View.VISIBLE
                itemView.call_log_his_view.visibility = View.VISIBLE
            }else{
                itemView.call_log_his_ll.visibility = View.GONE
                itemView.call_log_his_view.visibility = View.GONE
            }

            if(Pref.IsShowCustomerLocationShare){
                itemView.share_loc_ll.visibility = View.VISIBLE
                itemView.share_loc_view.visibility = View.VISIBLE
            }else{
                itemView.share_loc_ll.visibility = View.GONE
                itemView.share_loc_view.visibility = View.GONE
            }

            itemView.call_log_his_ll.setOnClickListener {
                (context as DashboardActivity).loadFragment(FragType.ShopCallHisFrag, true, list[adapterPosition].shop_id)
            }
            println("sett_check ${Pref.ShowPartyWithGeoFence} ${Pref.ShowUserwisePartyWithGeoFence} ${Pref.ShowPartyWithCreateOrder} ${Pref.ShowUserwisePartyWithCreateOrder}")

            itemView.ll_order_range.setOnClickListener {
                Timber.d("tag_range_click")
            }

            if (Pref.ShowPartyWithGeoFence && Pref.ShowUserwisePartyWithGeoFence) {
                try {
                    itemView.ll_range.visibility = View.VISIBLE
                    itemView.ll_order_range.visibility = View.VISIBLE
                    var mRadious: Int = LocationWizard.NEARBY_RADIUS
                    var location = Location("")
                    location.latitude = Pref.current_latitude.toDouble()
                    location.longitude = Pref.current_longitude.toDouble()
                    var shopLocation = Location("")
                    shopLocation.latitude = list[adapterPosition].shopLat
                    shopLocation.longitude = list[adapterPosition].shopLong
                    val isShopNearby = FTStorageUtils.checkShopPositionWithinRadious(
                        location,
                        shopLocation,
                        mRadious
                    )
                    if (isShopNearby) {
                        itemView.tv_range.text = "In Range"
                        itemView.iv_range.setBackgroundResource(R.drawable.inrange);
                        itemView.tv_range.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.right_rounded_corner_green_drawable
                            )
                        );
                        itemView.ll_range.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.bacgreen_round_corner_1
                            )
                        );

                    } else {
                        itemView.tv_range.text = "Out Range"
                        itemView.iv_range.setBackgroundResource(R.drawable.outrange)
                        itemView.tv_range.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.right_rounded_corner_red_drawable
                            )
                        );
                        itemView.ll_range.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.bacred_round_corner_1
                            )
                        );

                    }
                    println("rangeexception " + "success")

                } catch (e: Exception) {
                    e.printStackTrace()
                    println("rangeexception " + e.message)
                }
            }
            else{
                itemView.ll_range.visibility = View.GONE
            }
            if (Pref.ShowPartyWithCreateOrder && Pref.ShowUserwisePartyWithCreateOrder){
                itemView.ll_order.visibility = View.VISIBLE
                itemView.ll_order_range.visibility = View.VISIBLE
                itemView.iv_createorder.visibility = View.VISIBLE
                itemView.ll_order.text = "Create Order"
            }else{
                itemView.ll_order.visibility = View.GONE
            }
            itemView.ll_nearby_shop_create_order_root.setOnClickListener {
                listener.createITCOrderClick(list[adapterPosition])
            }

        }
    }

    fun updateAdapter(mlist: List<AddShopDBModelEntity>) {
        this.mList = mlist
        notifyDataSetChanged()
    }


}