package com.breezefieldnationalplastic.features.member.presentation

import android.content.Context
import android.location.Location
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.FTStorageUtils
import com.breezefieldnationalplastic.features.location.LocationWizard.Companion.NEARBY_RADIUS
import com.breezefieldnationalplastic.features.member.model.TeamShopListDataModel
import kotlinx.android.synthetic.main.inflate_avg_shop_item.view.*
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.*
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.add_order_ll
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.add_quot_ll
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.iconWrapper_rl
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.last_visited_date_TV
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.ll_dd_name
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.ll_shop_code
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.ll_shop_type
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.myshop_address_TV
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.myshop_name_TV
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.order_view
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.shop_IV
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.shop_damage_ll
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.shop_damage_view
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.total_visited_value_TV
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.tv_dd_name
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.tv_funnel_stage
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.tv_funnel_stage_header
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.tv_shop_code
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.tv_shop_contact_no
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.tv_stage
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.tv_stage_header

/**
 * Created by Saikat on 28-02-2020.
 */
//Revision History
// 1.0 MemberAllShopListAdapter  AppV 4.0.6  Saheli    11/01/2023 IsAllowShopStatusUpdate
// 2.0 MemberAllShopListFragment tufan 02-08-2023 AppV 4.1.6 mantis 0026651
class MemberAllShopListAdapter(private val context: Context, private val teamShopList: ArrayList<TeamShopListDataModel>,private val isViewAll: Boolean ,
                               private val onVisitShopClick: (TeamShopListDataModel) -> Unit,
                               private val listener: (TeamShopListDataModel) -> Unit, private val onUpdateLocClick: (TeamShopListDataModel) -> Unit,
                               private val getListSize: (Int) -> Unit,private val onDamageClick: (TeamShopListDataModel) -> Unit,private val onQuotClick: (TeamShopListDataModel) -> Unit,private val onHistoryClick: (AddShopDBModelEntity) -> Unit
,private val getShopStatusDtls: (TeamShopListDataModel) -> Unit) : RecyclerView.Adapter<MemberAllShopListAdapter.MyViewHolder>(),
        Filterable {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private var shopList: ArrayList<TeamShopListDataModel>? = null
    private var tempshopList: ArrayList<TeamShopListDataModel>? = null
    private var filtershopList: ArrayList<TeamShopListDataModel>? = null

    init {
        shopList = ArrayList()
        tempshopList = ArrayList()
        filtershopList = ArrayList()

        shopList?.addAll(teamShopList)
        tempshopList?.addAll(teamShopList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.inflate_member_shop_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bindItems(context, shopList!!,isViewAll,onVisitShopClick, listener, onUpdateLocClick,onDamageClick,onQuotClick,onHistoryClick,getShopStatusDtls)
    }

    override fun getItemCount(): Int {
        return shopList?.size!!
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, teamShopList: ArrayList<TeamShopListDataModel>, isViewAll: Boolean, onVisitShopClick: (TeamShopListDataModel) -> Unit,
                      listener: (TeamShopListDataModel) -> Unit,
                      onUpdateLocClick: (TeamShopListDataModel) -> Unit,onDamageClick: (TeamShopListDataModel) -> Unit,onQuotClick: (TeamShopListDataModel) -> Unit,
                      onHistoryClick: (AddShopDBModelEntity) -> Unit,getShopStatusDtls:(TeamShopListDataModel) -> Unit) {
            itemView.apply {
                myshop_name_TV.text = teamShopList[adapterPosition].shop_name
                myshop_address_TV.text = teamShopList[adapterPosition].shop_address
                tv_shop_contact_no.text = teamShopList[adapterPosition].shop_contact
                total_visited_value_TV.text = teamShopList[adapterPosition].total_visited
                visit_rl.visibility = View.GONE
                tv_update_address.visibility = View.VISIBLE

                val shopType = AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType(teamShopList[adapterPosition].shop_type)

                if (shopType != null && !TextUtils.isEmpty(shopType.shoptype_name)) {
                    tv_shop_type.text = shopType.shoptype_name
                    ll_shop_type.visibility = View.VISIBLE
                } else
                    ll_shop_type.visibility = View.GONE

                if (!TextUtils.isEmpty(teamShopList[adapterPosition].last_visit_date))
                    last_visited_date_TV.text = AppUtils.changeAttendanceDateFormat(teamShopList[adapterPosition].last_visit_date)
                else
                    last_visited_date_TV.text = "N.A."

                val drawable = TextDrawable.builder().buildRoundRect(teamShopList[adapterPosition].shop_name.trim().toUpperCase().take(1), ColorGenerator.MATERIAL.randomColor, 120)

                shop_IV.setImageDrawable(drawable)

                setOnClickListener {
                    listener(teamShopList[adapterPosition])
                }

                if (Pref.isEntityCodeVisible) {
                    if (!TextUtils.isEmpty(teamShopList[adapterPosition].entity_code)) {
                        ll_shop_code.visibility = View.VISIBLE
                        tv_shop_code.text = teamShopList[adapterPosition].entity_code
                    } else
                        ll_shop_code.visibility = View.GONE
                } else
                    ll_shop_code.visibility = View.GONE


                tv_update_address.setOnClickListener {
                    onUpdateLocClick(teamShopList[adapterPosition])
                }

                if (Pref.isCustomerFeatureEnable) {
                    ll_dd_name.visibility = View.GONE

                    if (!TextUtils.isEmpty(teamShopList[adapterPosition].stage_id)) {
                        val stage = AppDatabase.getDBInstance()?.stageDao()?.getSingleType(teamShopList[adapterPosition].stage_id)

                        if (stage == null) {
                            tv_stage_header.visibility = View.GONE
                            tv_stage.visibility = View.GONE
                        } else {
                            tv_stage_header.visibility = View.VISIBLE
                            tv_stage.visibility = View.VISIBLE

                            tv_stage.text = stage.stage_name
                        }
                    } else {
                        tv_stage_header.visibility = View.GONE
                        tv_stage.visibility = View.GONE
                    }

                    if (!TextUtils.isEmpty(teamShopList[adapterPosition].funnel_stage_id)) {
                        val funnelStage = AppDatabase.getDBInstance()?.funnelStageDao()?.getSingleType(teamShopList[adapterPosition].funnel_stage_id)

                        if (funnelStage == null) {
                            tv_funnel_stage_header.visibility = View.GONE
                            itemView.tv_funnel_stage.visibility = View.GONE
                        } else {
                            tv_funnel_stage_header.visibility = View.VISIBLE
                            tv_funnel_stage.visibility = View.VISIBLE

                            tv_funnel_stage.text = funnelStage.funnel_stage_name
                        }
                    } else {
                        tv_funnel_stage_header.visibility = View.GONE
                        tv_funnel_stage.visibility = View.GONE
                    }
                } else {
                    tv_funnel_stage_header.visibility = View.GONE
                    tv_funnel_stage.visibility = View.GONE
                    tv_stage_header.visibility = View.GONE
                    tv_stage.visibility = View.GONE

                    when {
                        teamShopList[adapterPosition].shop_type == "1" -> {
                            //tv_shop_type.text = context.getString(R.string.shop_type)
                            ll_dd_name.visibility = View.VISIBLE

                            if (!TextUtils.isEmpty(teamShopList[adapterPosition].dd_name))
                                tv_dd_name.text = teamShopList[adapterPosition].dd_name
                            else
                                tv_dd_name.text = "N.A."

                        }
                        teamShopList[adapterPosition].shop_type == "2" -> {
                            //tv_shop_type.text = context.getString(R.string.pp_type)
                            ll_dd_name.visibility = View.GONE
                        }
                        teamShopList[adapterPosition].shop_type == "3" -> {
                            //tv_shop_type.text = context.getString(R.string.new_party_type)
                            ll_dd_name.visibility = View.GONE
                        }
                        teamShopList[adapterPosition].shop_type == "4" -> {
                            //tv_shop_type.text = context.getString(R.string.distributor_type)
                            ll_dd_name.visibility = View.GONE
                        }
                        teamShopList[adapterPosition].shop_type == "5" -> {
                            //tv_shop_type.text = context.getString(R.string.diamond_type)
                            ll_dd_name.visibility = View.VISIBLE

                            if (!TextUtils.isEmpty(teamShopList[adapterPosition].dd_name))
                                tv_dd_name.text = teamShopList[adapterPosition].dd_name
                            else
                                tv_dd_name.text = "N.A."
                        }
                        else -> {
                            ll_dd_name.visibility = View.GONE
                        }
                    }
                }

                if(Pref.IsNewQuotationfeatureOn){
                    iconWrapper_rl.visibility = View.VISIBLE
                    add_order_ll.visibility = View.GONE
                    add_quot_ll.visibility = View.VISIBLE
                    order_view.visibility = View.GONE
                    history_vvview.visibility = View.VISIBLE
                }
                else{
                    iconWrapper_rl.visibility = View.GONE
                    add_order_ll.visibility = View.GONE
                    add_quot_ll.visibility = View.GONE
                    order_view.visibility = View.GONE
                    history_vvview.visibility = View.GONE
                }
                add_quot_ll.setOnClickListener {
                    onQuotClick(teamShopList[adapterPosition])
                }

                if(Pref.IsFeedbackHistoryActivated){
                    iconWrapper_rl.visibility = View.VISIBLE
                    history_llll.visibility = View.VISIBLE
                }
                else{
                    iconWrapper_rl.visibility = View.GONE
                    history_llll.visibility = View.GONE
                }

                history_llll.setOnClickListener {
                    var obj: AddShopDBModelEntity = AddShopDBModelEntity()
                    obj.apply {
                        shop_id=teamShopList[adapterPosition].shop_id
                        shopName=teamShopList[adapterPosition].shop_name
                        address=teamShopList[adapterPosition].shop_address
                        pinCode=teamShopList[adapterPosition].shop_pincode
                        shopLat=teamShopList[adapterPosition].shop_lat!!.toDouble()
                        shopLong=teamShopList[adapterPosition].shop_long!!.toDouble()
                        ownerContactNumber=teamShopList[adapterPosition].shop_contact
                        totalVisitCount=teamShopList[adapterPosition].total_visited
                        lastVisitedDate = teamShopList[adapterPosition].last_visit_date
                        type = teamShopList[adapterPosition].shop_type
                        assigned_to_dd_id = teamShopList[adapterPosition].assign_to_dd_id
                        assigned_to_pp_id = teamShopList[adapterPosition].assign_to_pp_id
                        entity_code = teamShopList[adapterPosition].entity_code
                    }
                    onHistoryClick(obj)
                }

                if (Pref.IsAllowBreakageTrackingunderTeam) {
                    itemView.shop_damage_ll.visibility = View.VISIBLE
                    itemView.shop_damage_view.visibility = View.VISIBLE
                }
                else {
                    itemView.shop_damage_ll.visibility = View.GONE
                    itemView.shop_damage_view.visibility = View.GONE
                }
                itemView.shop_damage_ll.setOnClickListener{
                    onDamageClick(teamShopList[adapterPosition])
                }

                if(Pref.IsNewQuotationfeatureOn || Pref.IsFeedbackHistoryActivated){
                    iconWrapper_rl.visibility = View.VISIBLE
                }
                // 1.0 MemberAllShopListAdpater  AppV 4.0.6  IsAllowShopStatusUpdate
                if(Pref.IsAllowShopStatusUpdate) {
                    tv_update_status.visibility = View.VISIBLE
                }
                else {
                    tv_update_status.visibility = View.GONE
                }
                tv_update_status.setOnClickListener {
                    getShopStatusDtls(teamShopList[adapterPosition])
                }

// 4.0 MemberAllShopListFragment tufan 02-08-2023 AppV 4.1.6 mantis 0026651 start
               /* val shopLocation = Location("")
                shopLocation.latitude = teamShopList[adapterPosition].shop_lat.toDouble()
                shopLocation.longitude = teamShopList[adapterPosition].shop_long.toDouble()
                val myLoc = Location("")
                myLoc.latitude  = Pref.current_latitude.toString().toDouble()
                myLoc.longitude =Pref.current_longitude.toString().toDouble()
                var mRadious:Int = NEARBY_RADIUS
                val isShopNearby = FTStorageUtils.checkShopPositionWithinRadious(myLoc, shopLocation, mRadious)
                if(isShopNearby && isViewAll){
                    visit_rl.visibility =View.VISIBLE
                }
                val isPresent = AppDatabase.getDBInstance()!!.shopActivityDao().isShopActivityAvailable(teamShopList[adapterPosition].shop_id, AppUtils.getCurrentDateForShopActi())
                if (isPresent) {
                    visit_icon.visibility = View.VISIBLE
                    visit_TV.text = "VISITED"
                    iconWrapper_rl.visibility = View.VISIBLE
                } else {
                    visit_icon.visibility = View.GONE
                    visit_TV.text = "VISIT THIS " + Pref.shopText.toUpperCase()
                    iconWrapper_rl.visibility = View.GONE
                }

                visit_rl.setOnClickListener {
                    onVisitShopClick(teamShopList[adapterPosition])
                }*/
                // 4.0 MemberAllShopListFragment tufan 02-08-2023 AppV 4.1.6 mantis 0026651 end
            }

        }
    }

    override fun getFilter(): Filter {
        return SearchFilter()
    }

    inner class SearchFilter : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()

            filtershopList?.clear()

            tempshopList?.indices!!
                    .filter { tempshopList?.get(it)?.shop_name?.toLowerCase()?.contains(p0?.toString()?.toLowerCase()!!)!! }
                    .forEach { filtershopList?.add(tempshopList?.get(it)!!) }

            results.values = filtershopList
            results.count = filtershopList?.size!!

            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {

            try {
                filtershopList = results?.values as ArrayList<TeamShopListDataModel>?
                shopList?.clear()
                val hashSet = HashSet<String>()
                if (filtershopList != null) {

                    filtershopList?.indices!!
                            .filter { hashSet.add(filtershopList?.get(it)?.shop_id!!) }
                            .forEach { shopList?.add(filtershopList?.get(it)!!) }

                    getListSize(shopList?.size!!)

                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshList(teamShopList: ArrayList<TeamShopListDataModel>) {
        shopList?.clear()
        shopList?.addAll(teamShopList)

        tempshopList?.clear()
        tempshopList?.addAll(teamShopList)

        if (filtershopList == null)
            filtershopList = ArrayList()
        filtershopList?.clear()

        notifyDataSetChanged()
    }
}