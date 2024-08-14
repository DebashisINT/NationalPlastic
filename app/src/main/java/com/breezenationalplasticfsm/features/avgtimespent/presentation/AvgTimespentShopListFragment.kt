package com.breezenationalplasticfsm.features.avgtimespent.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView

import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import com.pnikosis.materialishprogress.ProgressWheel
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.*
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.addshop.api.AddShopRepositoryProvider
import com.breezenationalplasticfsm.features.addshop.api.assignToPPList.AssignToPPListRepoProvider
import com.breezenationalplasticfsm.features.addshop.api.assignedToDDList.AssignToDDListRepoProvider
import com.breezenationalplasticfsm.features.addshop.api.typeList.TypeListRepoProvider
import com.breezenationalplasticfsm.features.addshop.model.AddShopRequestData
import com.breezenationalplasticfsm.features.addshop.model.AddShopResponse
import com.breezenationalplasticfsm.features.addshop.model.AssignedToShopListResponseModel
import com.breezenationalplasticfsm.features.addshop.model.assigntoddlist.AssignToDDListResponseModel
import com.breezenationalplasticfsm.features.addshop.model.assigntopplist.AssignToPPListResponseModel
import com.breezenationalplasticfsm.features.averageshop.business.InfoWizard
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.dashboard.presentation.api.ShopVisitImageUploadRepoProvider
import com.breezenationalplasticfsm.features.dashboard.presentation.model.ShopVisitImageUploadInputModel
import com.breezenationalplasticfsm.features.location.LocationWizard
import com.breezenationalplasticfsm.features.location.model.ShopDurationRequest
import com.breezenationalplasticfsm.features.location.model.ShopDurationRequestData
import com.breezenationalplasticfsm.features.location.shopdurationapi.ShopDurationRepositoryProvider
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.joda.time.DateTime
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Kinsuk on 15-11-2017.
 */
// 1.0 AvgTimespentShopListFragment AppV 4.0.6 saheli 12-01-2023 multiple contact Data added on Api called
class AvgTimespentShopListFragment : BaseFragment(), DatePickerListener, View.OnClickListener {


    private lateinit var mAvgTimeSpentListAdapter: AvgTimeSpentListAdapter
    private lateinit var nearByShopsList: RecyclerView
    private lateinit var mContext: Context
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var noShopAvailable: AppCompatTextView
    private lateinit var shopActivityEntityList: List<ShopActivityEntity>
    private lateinit var noOfShop: AppCustomTextView
    private lateinit var total_time_spent_tv: AppCustomTextView
    private lateinit var picker: HorizontalPicker
    private lateinit var selectedDate: String
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var sync_all_tv: AppCustomTextView
    var syncAllInc: Int = 0
    private var j: Int = 0

    private var isType99InTypeMaster:Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_average_time_shops, container, false)
        selectedDate = AppUtils.getCurrentDateForShopActi()
        initView(view)
        return view
    }

    private fun initView(view: View) {

        // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration begin
        try {
            var type99 =  AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType("99")
            if(type99 == null){
                println("tag_type99 no type found")
                isType99InTypeMaster = false
            }else{
                println("tag_type99 type found")
                isType99InTypeMaster = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration end

        if (AppUtils.isOnline(mContext)) {
            syncShopList()
        }
        /*NEW CALENDER*/
        picker = view.findViewById<HorizontalPicker>(R.id.datePicker)
        picker.setListener(this)
                .setDays(60)
                .setOffset(30)
                .setDateSelectedColor(ContextCompat.getColor(mContext, R.color.colorPrimary))//box color
                .setDateSelectedTextColor(ContextCompat.getColor(mContext, R.color.white))
                .setMonthAndYearTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))//month color
                .setTodayButtonTextColor(ContextCompat.getColor(mContext, R.color.date_selector_color))
                .setTodayDateTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setTodayDateBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))//
                .setUnselectedDayTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setDayOfWeekTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setUnselectedDayTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .showTodayButton(false)
                .init()
        picker.backgroundColor = Color.WHITE
        picker.setDate(DateTime())


        /*NEW CALENDER*/
        nearByShopsList = view.findViewById(R.id.near_by_shops_RCV)
        noShopAvailable = view.findViewById(R.id.no_shop_tv)
        noOfShop = view.findViewById(R.id.no_of_shop_TV)
        total_time_spent_tv = view.findViewById(R.id.total_time_spent_tv)
        progress_wheel = view.findViewById(R.id.progress_wheel)
        sync_all_tv = view.findViewById(R.id.sync_all_tv)
        sync_all_tv.setOnClickListener(this)
        progress_wheel.stopSpinning()
    }

    override fun onResume() {
        super.onResume()
       /* noOfShop.text = InfoWizard.getAverageShopVisitTimeDuration()
        total_time_spent_tv.text = InfoWizard.getTotalShopVisitTime(selectedDate)*/

    }

    override fun onClick(p0: View?) {
        syncAllInc = 0
        when (p0?.id) {
            R.id.sync_all_tv -> {
                    // tested on 24-11-2021 begin
                for (i in shopActivityEntityList.indices) {
                    if (!shopActivityEntityList[i].isDurationCalculated && shopActivityEntityList[i].startTimeStamp != "0" && shopActivityEntityList[i].isUploaded==false) {
                        Pref.durationCompletedShopId = shopActivityEntityList[i].shopid!!
                        val endTimeStamp = System.currentTimeMillis().toString()
                        val totalMinute = AppUtils.getMinuteFromTimeStamp(shopActivityEntityList[i].startTimeStamp, endTimeStamp)
                        val duration = AppUtils.getTimeFromTimeSpan(shopActivityEntityList[i].startTimeStamp, endTimeStamp)

                        if (!Pref.isMultipleVisitEnable) {
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivityEntityList[i].shopid!!, totalMinute, selectedDate)
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateEndTimeOfShop(endTimeStamp, shopActivityEntityList[i].shopid!!, selectedDate)
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivityEntityList[i].shopid!!, duration,selectedDate)
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateDurationAvailable(true, shopActivityEntityList[i].shopid!!, selectedDate)
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateIsUploaded(false, shopActivityEntityList[i].shopid!!, selectedDate)
                        }
                        else {
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivityEntityList[i].shopid!!, totalMinute, selectedDate, shopActivityEntityList[i].startTimeStamp)
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateEndTimeOfShop(endTimeStamp, shopActivityEntityList[i].shopid!!, selectedDate, shopActivityEntityList[i].startTimeStamp)
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivityEntityList[i].shopid!!, duration, selectedDate, shopActivityEntityList[i].startTimeStamp)
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateDurationAvailable(true, shopActivityEntityList[i].shopid!!, selectedDate, shopActivityEntityList[i].startTimeStamp)
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateIsUploaded(false, shopActivityEntityList[i].shopid!!,selectedDate, shopActivityEntityList[i].startTimeStamp)
                        }
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateOutTime(AppUtils.getCurrentTimeWithMeredian(), shopActivityEntityList[i].shopid!!, selectedDate, shopActivityEntityList[i].startTimeStamp)
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateOutLocation(LocationWizard.getNewLocationName(mContext, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble()), shopActivityEntityList[i].shopid!!, selectedDate, shopActivityEntityList[i].startTimeStamp)

                        val netStatus = if (AppUtils.isOnline(mContext))
                            "Online"
                        else
                            "Offline"

                        val netType = if (AppUtils.getNetworkType(mContext).equals("wifi", ignoreCase = true))
                            AppUtils.getNetworkType(mContext)
                        else
                            "Mobile ${AppUtils.mobNetType(mContext)}"

                        if (!Pref.isMultipleVisitEnable) {
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateDeviceStatusReason(AppUtils.getDeviceName(), AppUtils.getAndroidVersion(),
                                    AppUtils.getBatteryPercentage(mContext).toString(), netStatus, netType.toString(), shopActivityEntityList[i].shopid!!,selectedDate)
                        }
                        else {
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateDeviceStatusReason(AppUtils.getDeviceName(), AppUtils.getAndroidVersion(),
                                    AppUtils.getBatteryPercentage(mContext).toString(), netStatus, netType.toString(), shopActivityEntityList[i].shopid!!,selectedDate, shopActivityEntityList[i].startTimeStamp)
                        }
//                    AppUtils.isShopVisited = false

                        Pref.isShopVisited=false
                        /*if (Pref.willShowShopVisitReason && totalMinute.toInt() < Pref.minVisitDurationSpentTime.toInt()) {
                            Pref.isShowShopVisitReason = true
                            showRevisitReasonDialog(shopActivityList[i].startTimeStamp)
                        }*/
                    }
                }
                shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                Collections.reverse(shopActivityEntityList)
                // tested on 24-11-2021 end


                if (!Pref.isMultipleVisitEnable) {
                    if (shopActivityEntityList != null && shopActivityEntityList.isNotEmpty()) {

                        val list = ArrayList<ShopActivityEntity>()

                        for (i in shopActivityEntityList.indices) {

                            val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(shopActivityEntityList[i].shopid)
                            if (shop.isUploaded) {
                                if (shopActivityEntityList[i].isDurationCalculated /*&& !shopActivityEntityList[i].isUploaded*/) {
                                    //list.add(shopActivityEntityList[i])

                                    if (AppUtils.isVisitSync == "1")
                                        list.add(shopActivityEntityList[i])
                                    else {
                                        if (!shopActivityEntityList[i].isUploaded)
                                            list.add(shopActivityEntityList[i])
                                    }
                                }
                            }
                        }

                        if (list.size > 0)
                            syncAllShopActivity(list[syncAllInc].shopid!!, list)
                        else
                            syncShopVisitImage()

                    } else {
                        syncShopVisitImage()
                    }
                }
                else {
                    if (shopActivityEntityList != null && shopActivityEntityList.isNotEmpty()) {

                        val list = ArrayList<ShopActivityEntity>()

                        for (i in shopActivityEntityList.indices) {
                            val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(shopActivityEntityList[i].shopid)
                            if (shop.isUploaded) {
                                if (shopActivityEntityList[i].isDurationCalculated /*&& !ShopActivityEntityList[i].isUploaded*/) {
                                    if (AppUtils.isVisitSync == "1")
                                        list.add(shopActivityEntityList[i])
                                    else {
                                        if (!shopActivityEntityList[i].isUploaded)
                                            list.add(shopActivityEntityList[i])
                                    }
                                }
                            }
                        }

                        if (list.size > 0)
                            syncAllShopActivityForMultiVisit(list)
                    }
                }

            }
        }
    }

    private fun syncAllShopActivityForMultiVisit(list_: ArrayList<ShopActivityEntity>) {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val shopDataList: MutableList<ShopDurationRequestData> = ArrayList()
        val shopDurationApiReq = ShopDurationRequest()
        shopDurationApiReq.user_id = Pref.user_id
        shopDurationApiReq.session_token = Pref.session_token

        for (i in list_.indices) {
            val shopActivity = list_[i]

            val shopDurationData = ShopDurationRequestData()
            shopDurationData.shop_id = shopActivity.shopid
            if (shopActivity.startTimeStamp != "0" && !shopActivity.isDurationCalculated) {
                val totalMinute = AppUtils.getMinuteFromTimeStamp(shopActivity.startTimeStamp, System.currentTimeMillis().toString())
                val duration = AppUtils.getTimeFromTimeSpan(shopActivity.startTimeStamp, System.currentTimeMillis().toString())

                if (!Pref.isMultipleVisitEnable) {
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivity.shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi())
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivity.shopid!!, duration, AppUtils.getCurrentDateForShopActi())
                } else {
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivity.shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi(), shopActivity.startTimeStamp)
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivity.shopid!!, duration, AppUtils.getCurrentDateForShopActi(), shopActivity.startTimeStamp)
                }

                shopDurationData.spent_duration = duration
            } else {
                shopDurationData.spent_duration = shopActivity.duration_spent
            }
            shopDurationData.visited_date = shopActivity.visited_date
            shopDurationData.visited_time = shopActivity.visited_date
            if (TextUtils.isEmpty(shopActivity.distance_travelled))
                shopActivity.distance_travelled = "0.0"
            shopDurationData.distance_travelled = shopActivity.distance_travelled
            val list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdList(shopDurationData.shop_id)
            if (list != null && list.isNotEmpty())
                shopDurationData.total_visit_count = list[0].totalVisitCount

            if (!TextUtils.isEmpty(shopActivity.feedback))
                shopDurationData.feedback = shopActivity.feedback
            else
                shopDurationData.feedback = ""

            shopDurationData.isFirstShopVisited = shopActivity.isFirstShopVisited
            shopDurationData.distanceFromHomeLoc = shopActivity.distance_from_home_loc

            shopDurationData.next_visit_date = shopActivity.next_visit_date

            if (!TextUtils.isEmpty(shopActivity.early_revisit_reason))
                shopDurationData.early_revisit_reason = shopActivity.early_revisit_reason
            else
                shopDurationData.early_revisit_reason = ""

            shopDurationData.device_model = shopActivity.device_model
            shopDurationData.android_version = shopActivity.android_version
            shopDurationData.battery = shopActivity.battery
            shopDurationData.net_status = shopActivity.net_status
            shopDurationData.net_type = shopActivity.net_type
            shopDurationData.in_time = shopActivity.in_time
            shopDurationData.out_time = shopActivity.out_time
            shopDurationData.start_timestamp = shopActivity.startTimeStamp
            shopDurationData.in_location = shopActivity.in_loc
            shopDurationData.out_location = shopActivity.out_loc



            shopDurationData.shop_revisit_uniqKey = shopActivity.shop_revisit_uniqKey!!
            /*10-12-2021*/
            shopDurationData.updated_by = Pref.user_id

            try{
                shopDurationData.updated_on = shopActivity.updated_on!!
            }catch (ex:Exception){
                shopDurationData.updated_on = ""
            }


            if (!TextUtils.isEmpty(shopActivity.pros_id!!))
                shopDurationData.pros_id = shopActivity.pros_id!!
            else
                shopDurationData.pros_id = ""

            if (!TextUtils.isEmpty(shopActivity.agency_name!!))
                shopDurationData.agency_name =shopActivity.agency_name!!
            else
                shopDurationData.agency_name = ""

            if (!TextUtils.isEmpty(shopActivity.approximate_1st_billing_value))
                shopDurationData.approximate_1st_billing_value = shopActivity.approximate_1st_billing_value!!
            else
                shopDurationData.approximate_1st_billing_value = ""
            //duration garbage fix
            try{
                if(shopDurationData.spent_duration!!.contains("-") || shopDurationData.spent_duration!!.length != 8)
                {
                    shopDurationData.spent_duration="00:00:10"
                }
            }catch (ex:Exception){
                shopDurationData.spent_duration="00:00:10"
            }
            //New shop Create issue
            shopDurationData.isnewShop = shopActivity.isnewShop!!

            // 1.0 AvgTimespentShopListFragment AppV 4.0.6  multiple contact Data added on Api called
            shopDurationData.multi_contact_name = shopActivity.multi_contact_name
            shopDurationData.multi_contact_number = shopActivity.multi_contact_number

            // Suman 06-05-2024 Suman SyncActivity update mantis 27335  begin
            try {
                var shopOb = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopDurationData.shop_id)
                shopDurationData.shop_lat=shopOb.shopLat.toString()
                shopDurationData.shop_long=shopOb.shopLong.toString()
                shopDurationData.shop_addr=shopOb.address.toString()
            }catch (ex:Exception){
                ex.printStackTrace()
            }
            // Suman 06-05-2024 Suman SyncActivity update mantis 27335  end

            shopDataList.add(shopDurationData)

            Timber.d("========SYNC ALL VISITED SHOP DATA (AVERAGE SHOP)=====")
            Timber.d("SHOP ID======> " + shopDurationData.shop_id)
            Timber.d("SPENT DURATION======> " + shopDurationData.spent_duration)
            Timber.d("VISIT DATE=========> " + shopDurationData.visited_date)
            Timber.d("VISIT DATE TIME==========> " + shopDurationData.visited_date)
            Timber.d("TOTAL VISIT COUNT========> " + shopDurationData.total_visit_count)
            Timber.d("DISTANCE TRAVELLED========> " + shopDurationData.distance_travelled)
            Timber.d("FEEDBACK========> " + shopDurationData.feedback)
            Timber.d("isFirstShopVisited========> " + shopDurationData.isFirstShopVisited)
            Timber.d("distanceFromHomeLoc========> " + shopDurationData.distanceFromHomeLoc)
            Timber.d("next_visit_date========> " + shopDurationData.next_visit_date)
            Timber.d("early_revisit_reason========> " + shopDurationData.early_revisit_reason)
            Timber.d("device_model========> " + shopDurationData.device_model)
            Timber.d("android_version========> " + shopDurationData.android_version)
            Timber.d("battery========> " + shopDurationData.battery)
            Timber.d("net_status========> " + shopDurationData.net_status)
            Timber.d("net_type========> " + shopDurationData.net_type)
            Timber.d("in_time========> " + shopDurationData.in_time)
            Timber.d("out_time========> " + shopDurationData.out_time)
            Timber.d("start_timestamp========> " + shopDurationData.start_timestamp)
            Timber.d("in_location========> " + shopDurationData.in_location)
            Timber.d("out_location========> " + shopDurationData.out_location)
            Timber.d("=======================================================")
        }

        if (shopDataList.isEmpty()) {
            return
        }

        Log.e("Average Shop", "isShopActivityUpdating====> " + BaseActivity.isShopActivityUpdating)
        if (BaseActivity.isShopActivityUpdating)
            return

        BaseActivity.isShopActivityUpdating = true

        progress_wheel.spin()
        shopDurationApiReq.shop_list = shopDataList
        val repository = ShopDurationRepositoryProvider.provideShopDurationRepository()

        BaseActivity.compositeDisposable.add(
                repository.shopDuration(shopDurationApiReq)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            Timber.d("ShopActivityFromAverageShop : RESPONSE STATUS:= " + result.status + ", RESPONSE MESSAGE:= " + result.message +
                                    "\nUser Id" + Pref.user_id + ", Session Token" + Pref.session_token)
                            if (result.status == NetworkConstant.SUCCESS) {
                                shopDataList.forEach {
                                    AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, it.shop_id!!, AppUtils.changeAttendanceDateFormatToCurrent(it.visited_date!!), it.start_timestamp!!)
                                }
                                shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                Collections.reverse(shopActivityEntityList)

                                progress_wheel.stopSpinning()
                                BaseActivity.isShopActivityUpdating = false

                                Timber.d("===========INIT ADAPTER FOR SYNC ALL REVISIT SHOP DATA (AVERAGE TIME SPENT SHOP)========")
                                Timber.d("shop list size====> " + shopActivityEntityList.size)
                                Timber.d("specific date====> $selectedDate")

                                initAdapter()

                            } else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(mContext.getString(R.string.unable_to_sync))
                                BaseActivity.isShopActivityUpdating = false
                                shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                Collections.reverse(shopActivityEntityList)
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            BaseActivity.isShopActivityUpdating = false
                            if (error != null) {
                                Timber.d("ShopActivityFromAverageShop : ERROR:= " + error.localizedMessage + "\nUser Id" + Pref.user_id +
                                        ", Session Token" + Pref.session_token)
                                (mContext as DashboardActivity).showSnackMessage(mContext.getString(R.string.unable_to_sync))

                                shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                Collections.reverse(shopActivityEntityList)
                            }
                        })
        )

    }

    private fun syncShopVisitImage() {
        val unSyncedList = ArrayList<ShopVisitImageModelEntity>()
        if (shopActivityEntityList != null) {
            for (i in shopActivityEntityList.indices) {
                if (shopActivityEntityList[i].isDurationCalculated && shopActivityEntityList[i].isUploaded) {
                    //val unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(false, shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)

                    var unSyncedData: List<ShopVisitImageModelEntity>? = null
                    if (AppUtils.isVisitSync == "1")
                        unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysListAccordingToShopId(shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)
                    else
                        unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(false,
                                shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)

                    if (unSyncedData != null && unSyncedData.isNotEmpty()) {
                        unSyncedList.add(unSyncedData[0])
                    }
                }
            }

            if (unSyncedList.size > 0) {
                j = 0
                callShopVisitImageUploadApiForAll(unSyncedList)
            }
            else {
                val unSyncedAudioList = ArrayList<ShopVisitAudioEntity>()
                for (i in shopActivityEntityList.indices) {
                    if (shopActivityEntityList[i].isDurationCalculated && shopActivityEntityList[i].isUploaded) {
                        //val unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(false, shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)

                        var unSyncedData: List<ShopVisitAudioEntity>? = null
                        if (AppUtils.isVisitSync == "1")
                            unSyncedData = AppDatabase.getDBInstance()!!.shopVisitAudioDao().getTodaysListAccordingToShopId(shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)
                        else
                            unSyncedData = AppDatabase.getDBInstance()!!.shopVisitAudioDao().getTodaysUnSyncedListAccordingToShopId(false,
                                    shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)

                        if (unSyncedData != null && unSyncedData.isNotEmpty()) {
                            unSyncedAudioList.add(unSyncedData[0])
                        }
                    }
                }

                if (unSyncedAudioList.isNotEmpty()) {
                    j = 0
                    callShopVisitAudioUploadApiForAll(unSyncedAudioList)
                }
            }
        }
    }

    private fun callShopVisitImageUploadApiForAll(unSyncedList: List<ShopVisitImageModelEntity>) {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val visitImageShop = ShopVisitImageUploadInputModel()
        visitImageShop.session_token = Pref.session_token
        visitImageShop.user_id = Pref.user_id
        visitImageShop.shop_id = unSyncedList[j].shop_id
        visitImageShop.visit_datetime = unSyncedList[j].visit_datetime

        Log.e("Average Time spent Shop", "isShopActivityUpdating=============> " + BaseActivity.isShopActivityUpdating)
        if (BaseActivity.isShopActivityUpdating)
            return

        BaseActivity.isShopActivityUpdating = true

        Timber.d("======UPLOAD REVISIT ALL IMAGE INPUT PARAMS (AVERAGE TIME SPENT SHOP)=======")
        Timber.d("USER ID======> " + visitImageShop.user_id)
        Timber.d("SESSION ID======> " + visitImageShop.session_token)
        Timber.d("SHOP ID=========> " + visitImageShop.shop_id)
        Timber.d("VISIT DATE TIME==========> " + visitImageShop.visit_datetime)
        Timber.d("IMAGE========> " + unSyncedList[j].shop_image)
        Timber.d("============================================================================")

        val repository = ShopVisitImageUploadRepoProvider.provideAddShopRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.visitShopWithImage(visitImageShop, unSyncedList[j].shop_image!!, mContext)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val logoutResponse = result as BaseResponse
                            Timber.d("UPLOAD REVISIT ALL IMAGE : " + "RESPONSE : " + logoutResponse.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + logoutResponse.message)
                            if (logoutResponse.status == NetworkConstant.SUCCESS) {
                                AppDatabase.getDBInstance()!!.shopVisitImageDao().updateisUploaded(true, unSyncedList.get(j).shop_id!!)

                                j++
                                if (j < unSyncedList.size) {
                                    BaseActivity.isShopActivityUpdating = false
                                    callShopVisitImageUploadApiForAll(unSyncedList)
                                } else {
                                    j = 0
                                    (mContext as DashboardActivity).showSnackMessage("Sync Successful")
                                    BaseActivity.isShopActivityUpdating = false
                                    progress_wheel.stopSpinning()

                                    val unSyncedAudioList = ArrayList<ShopVisitAudioEntity>()
                                    for (i in shopActivityEntityList.indices) {
                                        if (shopActivityEntityList[i].isDurationCalculated && shopActivityEntityList[i].isUploaded) {
                                            //val unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(false, shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)

                                            var unSyncedData: List<ShopVisitAudioEntity>? = null
                                            if (AppUtils.isVisitSync == "1")
                                                unSyncedData = AppDatabase.getDBInstance()!!.shopVisitAudioDao().getTodaysListAccordingToShopId(shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)
                                            else
                                                unSyncedData = AppDatabase.getDBInstance()!!.shopVisitAudioDao().getTodaysUnSyncedListAccordingToShopId(false,
                                                        shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)

                                            if (unSyncedData != null && unSyncedData.isNotEmpty()) {
                                                unSyncedAudioList.add(unSyncedData[0])
                                            }
                                        }
                                    }

                                    if (unSyncedAudioList.isNotEmpty()) {
                                        j = 0
                                        callShopVisitAudioUploadApiForAll(unSyncedAudioList)
                                    }
                                    else {
                                        //mAvgTimeSpentListAdapter.updateList(AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate))
                                        shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                        Collections.reverse(shopActivityEntityList)

                                        Timber.d("===========INIT ADAPTER FOR SYNC ALL IMAGE (AVERAGE TIME SPENT SHOP)========")
                                        Timber.d("shop list size====> " + shopActivityEntityList.size)
                                        Timber.d("specific date====> $selectedDate")

                                        initAdapter()
                                        //callShopDurationApi()
                                    }
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                BaseActivity.isShopActivityUpdating = false
                                (mContext as DashboardActivity).showSnackMessage(logoutResponse.message!!)
                                shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                Collections.reverse(shopActivityEntityList)
                            }
                        }, { error ->
                            BaseActivity.isShopActivityUpdating = false
                            Timber.d("UPLOAD REVISIT ALL IMAGE : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                            Collections.reverse(shopActivityEntityList)
                            (mContext as DashboardActivity).showSnackMessage(mContext.getString(R.string.unable_to_sync))
                        })
        )
    }

    private fun callShopVisitAudioUploadApiForAll(unSyncedList: List<ShopVisitAudioEntity>) {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val visitImageShop = ShopVisitImageUploadInputModel()
        visitImageShop.session_token = Pref.session_token
        visitImageShop.user_id = Pref.user_id
        visitImageShop.shop_id = unSyncedList[j].shop_id
        visitImageShop.visit_datetime = unSyncedList[j].visit_datetime

        Log.e("Average Shop", "isShopActivityUpdating=============> " + BaseActivity.isShopActivityUpdating)
        if (BaseActivity.isShopActivityUpdating)
            return

        BaseActivity.isShopActivityUpdating = true

        Timber.d("========UPLOAD REVISIT ALL AUDIO INPUT PARAMS (AVERAGE SHOP)======")
        Timber.d("USER ID======> " + visitImageShop.user_id)
        Timber.d("SESSION ID======> " + visitImageShop.session_token)
        Timber.d("SHOP ID=========> " + visitImageShop.shop_id)
        Timber.d("VISIT DATE TIME==========> " + visitImageShop.visit_datetime)
        Timber.d("AUDIO========> " + unSyncedList[j].audio)
        Timber.d("=====================================================================")

        val repository = ShopVisitImageUploadRepoProvider.provideAddShopRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.visitShopWithAudio(visitImageShop, unSyncedList[j].audio!!, mContext)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val logoutResponse = result as BaseResponse
                            Timber.d("UPLOAD REVISIT ALL AUDIO : " + "RESPONSE : " + logoutResponse.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + logoutResponse.message)
                            if (logoutResponse.status == NetworkConstant.SUCCESS) {
                                AppDatabase.getDBInstance()!!.shopVisitAudioDao().updateisUploaded(true, unSyncedList.get(j).shop_id!!)

                                j++
                                if (j < unSyncedList.size) {
                                    progress_wheel.stopSpinning()
                                    BaseActivity.isShopActivityUpdating = false
                                    callShopVisitAudioUploadApiForAll(unSyncedList)
                                } else {
                                    j = 0
                                    BaseActivity.isShopActivityUpdating = false
                                    (mContext as DashboardActivity).showSnackMessage("Sync Successful")
                                    progress_wheel.stopSpinning()

                                    val list = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)

                                    Timber.d("=======UPDATE ADAPTER FOR SYNC ALL AUDIO (AVERAGE SHOP)=======")
                                    Timber.d("shop list size====> " + list.size)
                                    Timber.d("specific date====> $selectedDate")

                                    initAdapter()
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                BaseActivity.isShopActivityUpdating = false
                                (mContext as DashboardActivity).showSnackMessage(logoutResponse.message!!)
                            }
                        }, { error ->
                            Timber.d("UPLOAD REVISIT ALL AUDIO : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            BaseActivity.isShopActivityUpdating = false
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(mContext.getString(R.string.unable_to_sync))
                        })
        )
    }


    override fun onDateSelected(dateSelected: DateTime) {
        var dateTime = dateSelected.toString()
        var dateFormat = dateTime.substring(0, dateTime.indexOf('T'))
        selectedDate = dateFormat
        getListFromDatabase(dateFormat)
    }


    private fun getListFromDatabase(date: String) {

        shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(date)
        Collections.reverse(shopActivityEntityList)

        if (shopActivityEntityList.isNotEmpty()) {
            noShopAvailable.visibility = View.GONE
            nearByShopsList.visibility = View.VISIBLE

            Timber.d("===========INIT ADAPTER FOR SPECIFIC DATE (AVERAGE TIME SPENT SHOP)========")
            Timber.d("shop list size====> " + shopActivityEntityList.size)
            Timber.d("specific date====> $date")

            try {
                initAdapter()
            } catch (e: UninitializedPropertyAccessException) {
                initAdapter()
            }

        } else {
            noShopAvailable.visibility = View.VISIBLE
            nearByShopsList.visibility = View.GONE

            noOfShop.text = InfoWizard.getAverageShopVisitTimeDuration(selectedDate)
            total_time_spent_tv.text = InfoWizard.getTotalShopVisitTime(selectedDate)
        }

    }

    private var mPosition = 0
    @SuppressLint("UseRequireInsteadOfGet", "WrongConstant")
    private fun initAdapter() {

        noOfShop.text = InfoWizard.getAverageShopVisitTimeDuration(selectedDate)
        total_time_spent_tv.text = InfoWizard.getTotalShopVisitTime(selectedDate)

        // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration begin
        try {
            if(!isType99InTypeMaster){
                var rectifyShopListWithType :ArrayList<ShopActivityEntity> = ArrayList()
                for(i in 0..shopActivityEntityList.size-1){
                    var shopDtls = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopActivityEntityList.get(i).shopid)
                    if (shopDtls!=null){
                        if(!shopDtls.type.equals("99")){
                            rectifyShopListWithType.add(shopActivityEntityList.get(i))
                        }
                    }
                }
                shopActivityEntityList = rectifyShopListWithType
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration end

        mAvgTimeSpentListAdapter = AvgTimeSpentListAdapter(this.context!!, shopActivityEntityList, object : AvgTimeSpentListClickListener {
            override fun onSyncClick(position: Int) {
                mPosition = position
                try {
                    val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(shopActivityEntityList[position].shopid)

                    if (shop != null) {

                        if (shop.isUploaded) {
                            if (!shopActivityEntityList[position].isUploaded)
                                syncShopActivity(shopActivityEntityList[position].shopid!!)
                            else {
                                val unSyncedList = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(false,
                                        shopActivityEntityList[position].shopid!!, shopActivityEntityList[position].visited_date!!)

                                if (unSyncedList != null && unSyncedList.isNotEmpty()) {
                                    callShopVisitImageUploadApi(unSyncedList, false, null)
                                }
                                else {
                                    val unSyncedAudioList = AppDatabase.getDBInstance()!!.shopVisitAudioDao().getTodaysUnSyncedListAccordingToShopId(false,
                                            shopActivityEntityList[position].shopid!!, shopActivityEntityList[position].visited_date!!)

                                    if (unSyncedAudioList != null && unSyncedAudioList.isNotEmpty()) {
                                        callShopVisitAudioUploadApi(unSyncedAudioList, false, null)
                                    }
                                }
                            }
                        } else {
                            syncShop(position, shop)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun OnImageClick(position: Int) {
                (mContext as DashboardActivity).loadFragment(FragType.ShopDetailFragment, true, shopActivityEntityList[position].shopid!!)
            }

            override fun OnTimeSpentListClick(position: Int) {
                (mContext as DashboardActivity).loadFragment(FragType.ShopDetailFragment, true, shopActivityEntityList[position].shopid!!)
            }

            override fun menuClick(position: Int, view: View) {
                initiatePopupWindow(view, position)
            }
        })
        layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
        nearByShopsList.layoutManager = layoutManager
        nearByShopsList.adapter = mAvgTimeSpentListAdapter
    }

    private fun syncShop(position: Int, shop: AddShopDBModelEntity) {
        val addShopData = AddShopRequestData()
        //if (!shop.isUploaded) {
        addShopData.session_token = Pref.session_token
        addShopData.address = shop.address
        addShopData.owner_contact_no = shop.ownerContactNumber
        addShopData.owner_email = shop.ownerEmailId
        addShopData.owner_name = shop.ownerName
        addShopData.pin_code = shop.pinCode
        addShopData.shop_lat = shop.shopLat.toString()
        addShopData.shop_long = shop.shopLong.toString()
        addShopData.shop_name = shop.shopName.toString()
        addShopData.type = shop.type.toString()
        addShopData.shop_id = shop.shop_id
        addShopData.user_id = Pref.user_id

        if (!TextUtils.isEmpty(shop.dateOfBirth))
            addShopData.dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.dateOfBirth)

        if (!TextUtils.isEmpty(shop.dateOfAniversary))
            addShopData.date_aniversary = AppUtils.changeAttendanceDateFormatToCurrent(shop.dateOfAniversary)

        addShopData.assigned_to_dd_id = shop.assigned_to_dd_id
        addShopData.assigned_to_pp_id = shop.assigned_to_pp_id
        addShopData.added_date = shop.added_date
        addShopData.amount = shop.amount
        addShopData.area_id = shop.area_id
        addShopData.model_id = shop.model_id
        addShopData.primary_app_id = shop.primary_app_id
        addShopData.secondary_app_id = shop.secondary_app_id
        addShopData.lead_id = shop.lead_id
        addShopData.stage_id = shop.stage_id
        addShopData.funnel_stage_id = shop.funnel_stage_id
        addShopData.booking_amount = shop.booking_amount
        addShopData.type_id = shop.type_id

        addShopData.director_name = shop.director_name
        addShopData.key_person_name = shop.person_name
        addShopData.phone_no = shop.person_no

        if (!TextUtils.isEmpty(shop.family_member_dob))
            addShopData.family_member_dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.family_member_dob)

        if (!TextUtils.isEmpty(shop.add_dob))
            addShopData.addtional_dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.add_dob)

        if (!TextUtils.isEmpty(shop.add_doa))
            addShopData.addtional_doa = AppUtils.changeAttendanceDateFormatToCurrent(shop.add_doa)

        addShopData.specialization = shop.specialization
        addShopData.category = shop.category
        addShopData.doc_address = shop.doc_address
        addShopData.doc_pincode = shop.doc_pincode
        addShopData.is_chamber_same_headquarter = shop.chamber_status.toString()
        addShopData.is_chamber_same_headquarter_remarks = shop.remarks
        addShopData.chemist_name = shop.chemist_name
        addShopData.chemist_address = shop.chemist_address
        addShopData.chemist_pincode = shop.chemist_pincode
        addShopData.assistant_contact_no = shop.assistant_no
        addShopData.average_patient_per_day = shop.patient_count
        addShopData.assistant_name = shop.assistant_name

        if (!TextUtils.isEmpty(shop.doc_family_dob))
            addShopData.doc_family_member_dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.doc_family_dob)

        if (!TextUtils.isEmpty(shop.assistant_dob))
            addShopData.assistant_dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.assistant_dob)

        if (!TextUtils.isEmpty(shop.assistant_doa))
            addShopData.assistant_doa = AppUtils.changeAttendanceDateFormatToCurrent(shop.assistant_doa)

        if (!TextUtils.isEmpty(shop.assistant_family_dob))
            addShopData.assistant_family_dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.assistant_family_dob)

        addShopData.entity_id = shop.entity_id
        addShopData.party_status_id = shop.party_status_id
        addShopData.retailer_id = shop.retailer_id
        addShopData.dealer_id = shop.dealer_id
        addShopData.beat_id = shop.beat_id
        addShopData.assigned_to_shop_id = shop.assigned_to_shop_id
        addShopData.actual_address = shop.actual_address

        var uniqKeyObj=AppDatabase.getDBInstance()!!.shopActivityDao().getNewShopActivityKey(shop.shop_id,false)
        addShopData.shop_revisit_uniqKey=uniqKeyObj?.shop_revisit_uniqKey!!

        addShopData.project_name = shop.project_name
        addShopData.landline_number = shop.landline_number
        addShopData.agency_name = shop.agency_name

        addShopData.alternateNoForCustomer = shop.alternateNoForCustomer
        addShopData.whatsappNoForCustomer = shop.whatsappNoForCustomer

        // duplicate shop api call
        addShopData.isShopDuplicate=shop.isShopDuplicate

         addShopData.purpose=shop.purpose

        addShopData.GSTN_Number=shop.gstN_Number
        addShopData.ShopOwner_PAN=shop.shopOwner_PAN


        callAddShopApi(addShopData, shop.shopImageLocalPath, shop.doc_degree, position)
        //}
    }

    private fun callAddShopApi(addShop: AddShopRequestData, shop_imgPath: String?, degree_imgPath: String?, position: Int) {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        progress_wheel.spin()


        Timber.d("===========SyncShop Input Params (Average Shop time spent)=============")
        Timber.d("shop id=======> " + addShop.shop_id)
        val index = addShop.shop_id!!.indexOf("_")
        Timber.d("decoded shop id=======> " + addShop.user_id + "_" + AppUtils.getDate(addShop.shop_id!!.substring(index + 1, addShop.shop_id!!.length).toLong()))
        Timber.d("shop added date=======> " + addShop.added_date)
        Timber.d("shop address=======> " + addShop.address)
        Timber.d("assigned to dd id=======> " + addShop.assigned_to_dd_id)
        Timber.d("assigned to pp id=======> " + addShop.assigned_to_pp_id)
        Timber.d("date aniversery=======> " + addShop.date_aniversary)
        Timber.d("dob=======> " + addShop.dob)
        Timber.d("shop owner phn no=======> " + addShop.owner_contact_no)
        Timber.d("shop owner email=======> " + addShop.owner_email)
        Timber.d("shop owner name=======> " + addShop.owner_name)
        Timber.d("shop pincode=======> " + addShop.pin_code)
        Timber.d("session token=======> " + addShop.session_token)
        Timber.d("shop lat=======> " + addShop.shop_lat)
        Timber.d("shop long=======> " + addShop.shop_long)
        Timber.d("shop name=======> " + addShop.shop_name)
        Timber.d("shop type=======> " + addShop.type)
        Timber.d("user id=======> " + addShop.user_id)
        Timber.d("amount=======> " + addShop.amount)
        Timber.d("area id=======> " + addShop.area_id)
        Timber.d("model id=======> " + addShop.model_id)
        Timber.d("primary app id=======> " + addShop.primary_app_id)
        Timber.d("secondary app id=======> " + addShop.secondary_app_id)
        Timber.d("lead id=======> " + addShop.lead_id)
        Timber.d("stage id=======> " + addShop.stage_id)
        Timber.d("funnel stage id=======> " + addShop.funnel_stage_id)
        Timber.d("booking amount=======> " + addShop.booking_amount)
        Timber.d("type id=======> " + addShop.type_id)

        if (shop_imgPath != null)
            Timber.d("shop image path=======> $shop_imgPath")

        Timber.d("director name=======> " + addShop.director_name)
        Timber.d("family member dob=======> " + addShop.family_member_dob)
        Timber.d("key person's name=======> " + addShop.key_person_name)
        Timber.d("phone no=======> " + addShop.phone_no)
        Timber.d("additional dob=======> " + addShop.addtional_dob)
        Timber.d("additional doa=======> " + addShop.addtional_doa)
        Timber.d("doctor family member dob=======> " + addShop.doc_family_member_dob)
        Timber.d("specialization=======> " + addShop.specialization)
        Timber.d("average patient count per day=======> " + addShop.average_patient_per_day)
        Timber.d("category=======> " + addShop.category)
        Timber.d("doctor address=======> " + addShop.doc_address)
        Timber.d("doctor pincode=======> " + addShop.doc_pincode)
        Timber.d("chambers or hospital under same headquarter=======> " + addShop.is_chamber_same_headquarter)
        Timber.d("chamber related remarks=======> " + addShop.is_chamber_same_headquarter_remarks)
        Timber.d("chemist name=======> " + addShop.chemist_name)
        Timber.d("chemist name=======> " + addShop.chemist_address)
        Timber.d("chemist pincode=======> " + addShop.chemist_pincode)
        Timber.d("assistant name=======> " + addShop.assistant_name)
        Timber.d("assistant contact no=======> " + addShop.assistant_contact_no)
        Timber.d("assistant dob=======> " + addShop.assistant_dob)
        Timber.d("assistant date of anniversary=======> " + addShop.assistant_doa)
        Timber.d("assistant family dob=======> " + addShop.assistant_family_dob)
        Timber.d("entity id=======> " + addShop.entity_id)
        Timber.d("party status id=======> " + addShop.party_status_id)
        Timber.d("retailer id=======> " + addShop.retailer_id)
        Timber.d("dealer id=======> " + addShop.dealer_id)
        Timber.d("beat id=======> " + addShop.beat_id)
        Timber.d("assigned to shop id=======> " + addShop.assigned_to_shop_id)
        Timber.d("actual address=======> " + addShop.actual_address)

        if (degree_imgPath != null)
            Timber.d("doctor degree image path=======> $degree_imgPath")
        Timber.d("======================================================================")

        if (TextUtils.isEmpty(shop_imgPath) && TextUtils.isEmpty(degree_imgPath)) {
            val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addShop(addShop)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as AddShopResponse
                                Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + ", RESPONSE:" + result.message)
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)

                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                progress_wheel.stopSpinning()
                                                getAssignedPPListApi(addShop.shop_id, position)
                                            }

                                        }
                                    }

                                } else if (addShopResult.status == NetworkConstant.DUPLICATE_SHOP_ID) {
                                    Timber.d("DuplicateShop : " + ", SHOP: " + addShop.shop_name)
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)
                                    //progress_wheel.stopSpinning()
                                    //(mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)
                                    if (AppDatabase.getDBInstance()!!.addShopEntryDao().getDuplicateShopData(addShop.owner_contact_no).size > 0) {
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().deleteShopById(addShop.shop_id)

                                        Timber.d("=========Duplicate shop deleted from shop activity table (Average Shop Timespent)========")
                                        AppDatabase.getDBInstance()!!.shopActivityDao().deleteShopByIdAndDate(addShop.shop_id!!, AppUtils.getCurrentDateForShopActi())
                                    }
                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                progress_wheel.stopSpinning()
                                                getAssignedPPListApi(addShop.shop_id, position)
                                            }

                                        }
                                    }
                                    //getAssignedPPListApi(addShop.shop_id, position)
                                } else {
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                }


                            }, { error ->
                                error.printStackTrace()
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                if (error != null)
                                    Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + error.localizedMessage)
                            })
            )
        }
        else {
            val repository = AddShopRepositoryProvider.provideAddShopRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addShopWithImage(addShop, shop_imgPath, degree_imgPath, mContext)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as AddShopResponse
                                Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + ", RESPONSE:" + result.message)
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)

                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                progress_wheel.stopSpinning()
                                                getAssignedPPListApi(addShop.shop_id, position)
                                            }

                                        }
                                    }

                                } else if (addShopResult.status == NetworkConstant.DUPLICATE_SHOP_ID) {
                                    Timber.d("DuplicateShop : " + ", SHOP: " + addShop.shop_name)
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)
                                    //progress_wheel.stopSpinning()
                                    //(mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)
                                    if (AppDatabase.getDBInstance()!!.addShopEntryDao().getDuplicateShopData(addShop.owner_contact_no).size > 0) {
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().deleteShopById(addShop.shop_id)

                                        Timber.d("=========Duplicate shop deleted from shop activity table (Average Shop Timespent)========")
                                        AppDatabase.getDBInstance()!!.shopActivityDao().deleteShopByIdAndDate(addShop.shop_id!!, AppUtils.getCurrentDateForShopActi())
                                    }
                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                progress_wheel.stopSpinning()
                                                getAssignedPPListApi(addShop.shop_id, position)
                                            }

                                        }
                                    }
                                    //getAssignedPPListApi(addShop.shop_id, position)
                                } else {
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                }


                            }, { error ->
                                error.printStackTrace()
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                if (error != null)
                                    Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + error.localizedMessage)
                            })
            )
        }
    }

    private fun runLongTask(shop_id: String?): Any {
        val shopActivity = AppDatabase.getDBInstance()!!.shopActivityDao().durationAvailableForShop(shop_id!!, true, false)
        if (shopActivity != null)
            callShopActivitySubmit(shop_id)
        return true
    }

    private var shop_duration = ""
    private var startTimeStamp = ""
    private fun callShopActivitySubmit(shopId: String) {
        var list = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(shopId, AppUtils.getCurrentDateForShopActi())
        if (list.isEmpty())
            return
        var shopDataList: MutableList<ShopDurationRequestData> = java.util.ArrayList()
        var shopDurationApiReq = ShopDurationRequest()
        shopDurationApiReq.user_id = Pref.user_id
        shopDurationApiReq.session_token = Pref.session_token

        if (!Pref.isMultipleVisitEnable) {
            var shopActivity = list[0]

            var shopDurationData = ShopDurationRequestData()
            shopDurationData.shop_id = shopActivity.shopid
            if (shopActivity.startTimeStamp != "0" && !shopActivity.isDurationCalculated) {
                val totalMinute = AppUtils.getMinuteFromTimeStamp(shopActivity.startTimeStamp, System.currentTimeMillis().toString())
                val duration = AppUtils.getTimeFromTimeSpan(shopActivity.startTimeStamp, System.currentTimeMillis().toString())

                AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivity.shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi())
                AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivity.shopid!!, duration, AppUtils.getCurrentDateForShopActi())

                shopDurationData.spent_duration = duration
            } else {
                shopDurationData.spent_duration = shopActivity.duration_spent
            }
            shopDurationData.visited_date = shopActivity.visited_date
            shopDurationData.visited_time = shopActivity.visited_date
            if (TextUtils.isEmpty(shopActivity.distance_travelled))
                shopActivity.distance_travelled = "0.0"
            shopDurationData.distance_travelled = shopActivity.distance_travelled
            var sList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdList(shopDurationData.shop_id)
            if (sList != null && sList.isNotEmpty())
                shopDurationData.total_visit_count = sList[0].totalVisitCount

            if (!TextUtils.isEmpty(shopActivity.feedback))
                shopDurationData.feedback = shopActivity.feedback
            else
                shopDurationData.feedback = ""

            shopDurationData.isFirstShopVisited = shopActivity.isFirstShopVisited
            shopDurationData.distanceFromHomeLoc = shopActivity.distance_from_home_loc
            shopDurationData.next_visit_date = shopActivity.next_visit_date

            if (!TextUtils.isEmpty(shopActivity.early_revisit_reason))
                shopDurationData.early_revisit_reason = shopActivity.early_revisit_reason
            else
                shopDurationData.early_revisit_reason = ""

            shopDurationData.device_model = shopActivity.device_model
            shopDurationData.android_version = shopActivity.android_version
            shopDurationData.battery = shopActivity.battery
            shopDurationData.net_status = shopActivity.net_status
            shopDurationData.net_type = shopActivity.net_type
            shopDurationData.in_time = shopActivity.in_time
            shopDurationData.out_time = shopActivity.out_time
            shopDurationData.start_timestamp = shopActivity.startTimeStamp
            shopDurationData.in_location = shopActivity.in_loc
            shopDurationData.out_location = shopActivity.out_loc

            shopDurationData.shop_revisit_uniqKey = shopActivity.shop_revisit_uniqKey!!

            /*10-12-2021*/
            shopDurationData.updated_by = Pref.user_id
            try{
                shopDurationData.updated_on = shopActivity.updated_on!!
            }catch (ex:Exception){
                shopDurationData.updated_on = ""
            }


            if (!TextUtils.isEmpty(shopActivity.pros_id!!))
                shopDurationData.pros_id = shopActivity.pros_id!!
            else
                shopDurationData.pros_id = ""

            if (!TextUtils.isEmpty(shopActivity.agency_name!!))
                shopDurationData.agency_name =shopActivity.agency_name!!
            else
                shopDurationData.agency_name = ""

            if (!TextUtils.isEmpty(shopActivity.approximate_1st_billing_value))
                shopDurationData.approximate_1st_billing_value = shopActivity.approximate_1st_billing_value!!
            else
                shopDurationData.approximate_1st_billing_value = ""

            //duration garbage fix
            try{
                if(shopDurationData.spent_duration!!.contains("-") || shopDurationData.spent_duration!!.length != 8)
                {
                    shopDurationData.spent_duration="00:00:10"
                }
            }catch (ex:Exception){
                shopDurationData.spent_duration="00:00:10"
            }

            // Suman 06-05-2024 Suman SyncActivity update mantis 27335  begin
            try {
                var shopOb = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopDurationData.shop_id)
                shopDurationData.shop_lat=shopOb.shopLat.toString()
                shopDurationData.shop_long=shopOb.shopLong.toString()
                shopDurationData.shop_addr=shopOb.address.toString()
            }catch (ex:Exception){
                ex.printStackTrace()
            }
            // Suman 06-05-2024 Suman SyncActivity update mantis 27335  end

            shopDataList.add(shopDurationData)
        }
        else {
            for (i in list.indices) {
                var shopActivity = list[i]

                var shopDurationData = ShopDurationRequestData()
                shopDurationData.shop_id = shopActivity.shopid
                if (shopActivity.startTimeStamp != "0" && !shopActivity.isDurationCalculated) {
                    val totalMinute = AppUtils.getMinuteFromTimeStamp(shopActivity.startTimeStamp, System.currentTimeMillis().toString())
                    val duration = AppUtils.getTimeFromTimeSpan(shopActivity.startTimeStamp, System.currentTimeMillis().toString())

                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivity.shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi(), shopActivity.startTimeStamp)
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivity.shopid!!, duration, AppUtils.getCurrentDateForShopActi(), shopActivity.startTimeStamp)

                    shopDurationData.spent_duration = duration
                } else {
                    shopDurationData.spent_duration = shopActivity.duration_spent
                }
                shopDurationData.visited_date = shopActivity.visited_date
                shopDurationData.visited_time = shopActivity.visited_date

                if (TextUtils.isEmpty(shopActivity.distance_travelled))
                    shopActivity.distance_travelled = "0.0"

                shopDurationData.distance_travelled = shopActivity.distance_travelled

                var sList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdList(shopDurationData.shop_id)
                if (sList != null && sList.isNotEmpty())
                    shopDurationData.total_visit_count = sList[0].totalVisitCount

                if (!TextUtils.isEmpty(shopActivity.feedback))
                    shopDurationData.feedback = shopActivity.feedback
                else
                    shopDurationData.feedback = ""

                shopDurationData.isFirstShopVisited = shopActivity.isFirstShopVisited
                shopDurationData.distanceFromHomeLoc = shopActivity.distance_from_home_loc
                shopDurationData.next_visit_date = shopActivity.next_visit_date

                if (!TextUtils.isEmpty(shopActivity.early_revisit_reason))
                    shopDurationData.early_revisit_reason = shopActivity.early_revisit_reason
                else
                    shopDurationData.early_revisit_reason = ""

                shopDurationData.device_model = shopActivity.device_model
                shopDurationData.android_version = shopActivity.android_version
                shopDurationData.battery = shopActivity.battery
                shopDurationData.net_status = shopActivity.net_status
                shopDurationData.net_type = shopActivity.net_type
                shopDurationData.in_time = shopActivity.in_time
                shopDurationData.out_time = shopActivity.out_time
                shopDurationData.start_timestamp = shopActivity.startTimeStamp
                shopDurationData.in_location = shopActivity.in_loc
                shopDurationData.out_location = shopActivity.out_loc

                shopDurationData.shop_revisit_uniqKey = shopActivity.shop_revisit_uniqKey!!


                /*10-12-2021*/
                shopDurationData.updated_by = Pref.user_id
                try{
                    shopDurationData.updated_on = shopActivity.updated_on!!
                }catch (ex:Exception){
                    shopDurationData.updated_on = ""
                }



                if (!TextUtils.isEmpty(shopActivity.pros_id!!))
                    shopDurationData.pros_id = shopActivity.pros_id!!
                else
                    shopDurationData.pros_id = ""

                if (!TextUtils.isEmpty(shopActivity.agency_name!!))
                    shopDurationData.agency_name =shopActivity.agency_name!!
                else
                    shopDurationData.agency_name = ""

                if (!TextUtils.isEmpty(shopActivity.approximate_1st_billing_value))
                    shopDurationData.approximate_1st_billing_value = shopActivity.approximate_1st_billing_value!!
                else
                    shopDurationData.approximate_1st_billing_value = ""
                //duration garbage fix
                try{
                    if(shopDurationData.spent_duration!!.contains("-") || shopDurationData.spent_duration!!.length != 8)
                    {
                        shopDurationData.spent_duration="00:00:10"
                    }
                }catch (ex:Exception){
                    shopDurationData.spent_duration="00:00:10"
                }
                //New shop Create issue
                shopDurationData.isnewShop = shopActivity.isnewShop!!

                // 1.0 AvgTimespentShopListFragment AppV 4.0.6  multiple contact Data added on Api called
                shopDurationData.multi_contact_name = shopActivity.multi_contact_name
                shopDurationData.multi_contact_number = shopActivity.multi_contact_number

                // Suman 06-05-2024 Suman SyncActivity update mantis 27335  begin
                try {
                    var shopOb = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopDurationData.shop_id)
                    shopDurationData.shop_lat=shopOb.shopLat.toString()
                    shopDurationData.shop_long=shopOb.shopLong.toString()
                    shopDurationData.shop_addr=shopOb.address.toString()
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
                // Suman 06-05-2024 Suman SyncActivity update mantis 27335  end

                shopDataList.add(shopDurationData)
            }
        }

        if (shopDataList.isEmpty()) {
            return
        }

        shopDurationApiReq.shop_list = shopDataList
        val repository = ShopDurationRepositoryProvider.provideShopDurationRepository()

        BaseActivity.compositeDisposable.add(
                repository.shopDuration(shopDurationApiReq)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            Timber.d("syncShopActivityFromShopList : " + ", SHOP: " + list[0].shop_name + ", RESPONSE:" + result.message)
                            if (result.status == NetworkConstant.SUCCESS) {

                            }

                        }, { error ->
                            error.printStackTrace()
                            if (error != null)
                                Timber.d("syncShopActivityFromShopList : " + ", SHOP: " + list[0].shop_name + error.localizedMessage)
//                                (mContext as DashboardActivity).showSnackMessage("ERROR")
                        })
        )

    }

    private fun getAssignedPPListApi(shop_id: String?, position: Int) {
        val repository = AssignToPPListRepoProvider.provideAssignPPListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.assignToPPList(Pref.profile_state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AssignToPPListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.assigned_to_pp_list

                                if (list != null && list.isNotEmpty()) {

                                    doAsync {

                                        val assignPPList = AppDatabase.getDBInstance()?.ppListDao()?.getAll()
                                        if (assignPPList != null)
                                            AppDatabase.getDBInstance()?.ppListDao()?.delete()

                                        for (i in list.indices) {
                                            val assignToPP = AssignToPPEntity()
                                            assignToPP.pp_id = list[i].assigned_to_pp_id
                                            assignToPP.pp_name = list[i].assigned_to_pp_authorizer_name
                                            assignToPP.pp_phn_no = list[i].phn_no
                                            AppDatabase.getDBInstance()?.ppListDao()?.insert(assignToPP)
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getAssignedDDListApi(shop_id, position)
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getAssignedDDListApi(shop_id, position)
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                getAssignedDDListApi(shop_id, position)
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            getAssignedDDListApi(shop_id, position)
                        })
        )
    }

    private fun getAssignedDDListApi(shop_id: String?, position: Int) {
        val repository = AssignToDDListRepoProvider.provideAssignDDListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.assignToDDList(Pref.profile_state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AssignToDDListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.assigned_to_dd_list

                                if (list != null && list.isNotEmpty()) {

                                    doAsync {

                                        val assignDDList = AppDatabase.getDBInstance()?.ddListDao()?.getAll()
                                        if (assignDDList != null)
                                            AppDatabase.getDBInstance()?.ddListDao()?.delete()

                                        for (i in list.indices) {
                                            val assignToDD = AssignToDDEntity()
                                            assignToDD.dd_id = list[i].assigned_to_dd_id
                                            assignToDD.dd_name = list[i].assigned_to_dd_authorizer_name
                                            assignToDD.dd_phn_no = list[i].phn_no
                                            assignToDD.pp_id = list[i].assigned_to_pp_id
                                            assignToDD.type_id = list[i].type_id
                                            assignToDD.dd_latitude = list[i].dd_latitude
                                            assignToDD.dd_longitude = list[i].dd_longitude
                                            AppDatabase.getDBInstance()?.ddListDao()?.insert(assignToDD)
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getAssignedToShopApi(shop_id, position)

                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getAssignedToShopApi(shop_id, position)
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                getAssignedToShopApi(shop_id, position)
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            getAssignedToShopApi(shop_id, position)
                        })
        )
    }

    private fun getAssignedToShopApi(shop_id: String?, position: Int) {
        val repository = TypeListRepoProvider.provideTypeListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.assignToShopList(Pref.profile_state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AssignedToShopListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.shop_list

                                AppDatabase.getDBInstance()?.assignToShopDao()?.delete()

                                doAsync {
                                    list?.forEach {
                                        val shop = AssignToShopEntity()
                                        AppDatabase.getDBInstance()?.assignToShopDao()?.insert(shop.apply {
                                            assigned_to_shop_id = it.assigned_to_shop_id
                                            name = it.name
                                            phn_no = it.phn_no
                                            type_id = it.type_id
                                        })
                                    }

                                    uiThread {
                                        progress_wheel.stopSpinning()
                                        if (!Pref.isMultipleVisitEnable)
                                            AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shop_id!!, selectedDate)
                                        else
                                            AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shop_id!!, selectedDate, startTimeStamp)

                                        (mContext as DashboardActivity).showSnackMessage("Synced successfully")
                                        shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                        Collections.reverse(shopActivityEntityList)
                                        initAdapter()
                                    }
                                }
                            }
                            else {
                                progress_wheel.stopSpinning()
                                if (!Pref.isMultipleVisitEnable)
                                    AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shop_id!!, selectedDate)
                                else
                                    AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shop_id!!, selectedDate, startTimeStamp)

                                (mContext as DashboardActivity).showSnackMessage("Synced successfully")
                                shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                Collections.reverse(shopActivityEntityList)
                                initAdapter()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            if (!Pref.isMultipleVisitEnable)
                                AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shop_id!!, selectedDate)
                            else
                                AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shop_id!!, selectedDate, startTimeStamp)

                            (mContext as DashboardActivity).showSnackMessage("Synced successfully")
                            shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                            Collections.reverse(shopActivityEntityList)
                            initAdapter()
                        })
        )
    }

    private fun syncShopActivity(shopId: String) {

        try {

            if (!AppUtils.isOnline(mContext)) {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                return
            }
            val mList = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(shopId, selectedDate)
            if (mList.isEmpty())
                return
            val shopActivity = mList[0]
//        var shopActivity = AppDatabase.getDBInstance()!!.shopActivityDao().getShopActivityForId(shopId)
            val shopDurationApiReq = ShopDurationRequest()
            shopDurationApiReq.user_id = Pref.user_id
            shopDurationApiReq.session_token = Pref.session_token
            val shopDataList: MutableList<ShopDurationRequestData> = ArrayList()
            val shopDurationData = ShopDurationRequestData()
            shopDurationData.shop_id = shopActivity.shopid
            if (shopActivity.startTimeStamp != "0" && !shopActivity.isDurationCalculated) {
                val totalMinute = AppUtils.getMinuteFromTimeStamp(shopActivity.startTimeStamp, System.currentTimeMillis().toString())
                val duration = AppUtils.getTimeFromTimeSpan(shopActivity.startTimeStamp, System.currentTimeMillis().toString())

                if (!Pref.isMultipleVisitEnable) {
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivity.shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi())
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivity.shopid!!, duration, AppUtils.getCurrentDateForShopActi())
                }
                else {
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivity.shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi(), shopActivity.startTimeStamp)
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivity.shopid!!, duration, AppUtils.getCurrentDateForShopActi(), shopActivity.startTimeStamp)
                }

                shopDurationData.spent_duration = duration
            } else {
                shopDurationData.spent_duration = shopActivity.duration_spent
            }
            shopDurationData.visited_date = shopActivity.visited_date
            shopDurationData.visited_time = shopActivity.visited_date
            if (TextUtils.isEmpty(shopActivity.distance_travelled))
                shopActivity.distance_travelled = "0.0"
            shopDurationData.distance_travelled = shopActivity.distance_travelled
            val list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdList(shopDurationData.shop_id)
            if (list != null && list.isNotEmpty())
                shopDurationData.total_visit_count = list[0].totalVisitCount

            if (!TextUtils.isEmpty(shopActivity.feedback))
                shopDurationData.feedback = shopActivity.feedback
            else
                shopDurationData.feedback = ""

            shopDurationData.isFirstShopVisited = shopActivity.isFirstShopVisited
            shopDurationData.distanceFromHomeLoc = shopActivity.distance_from_home_loc
            shopDurationData.next_visit_date = shopActivity.next_visit_date

            if (!TextUtils.isEmpty(shopActivity.early_revisit_reason))
                shopDurationData.early_revisit_reason = shopActivity.early_revisit_reason
            else
                shopDurationData.early_revisit_reason = ""

            shopDurationData.device_model = shopActivity.device_model
            shopDurationData.android_version = shopActivity.android_version
            shopDurationData.battery = shopActivity.battery
            shopDurationData.net_status = shopActivity.net_status
            shopDurationData.net_type = shopActivity.net_type
            shopDurationData.in_time = shopActivity.in_time
            shopDurationData.out_time = shopActivity.out_time
            shopDurationData.start_timestamp = shopActivity.startTimeStamp
            shopDurationData.in_location = shopActivity.in_loc
            shopDurationData.out_location = shopActivity.out_loc

            shopDurationData.shop_revisit_uniqKey = shopActivity.shop_revisit_uniqKey

            /*10-12-2021*/
            shopDurationData.updated_by = Pref.user_id
            try{
                shopDurationData.updated_on = shopActivity.updated_on!!
            }
            catch (ex:Exception){
                shopDurationData.updated_on = ""
            }


            if (!TextUtils.isEmpty(shopActivity.pros_id!!))
                shopDurationData.pros_id = shopActivity.pros_id!!
            else
                shopDurationData.pros_id = ""

            if (!TextUtils.isEmpty(shopActivity.agency_name!!))
                shopDurationData.agency_name =shopActivity.agency_name!!
            else
                shopDurationData.agency_name = ""

            if (!TextUtils.isEmpty(shopActivity.approximate_1st_billing_value))
                shopDurationData.approximate_1st_billing_value = shopActivity.approximate_1st_billing_value!!
            else
                shopDurationData.approximate_1st_billing_value = ""
            //duration garbage fix
            try{
                if(shopDurationData.spent_duration!!.contains("-") || shopDurationData.spent_duration!!.length != 8)
                {
                    shopDurationData.spent_duration="00:00:10"
                }
            }catch (ex:Exception){
                shopDurationData.spent_duration="00:00:10"
            }
            //New shop Create issue
            shopDurationData.isnewShop = shopActivity.isnewShop!!

            // 1.0 AvgTimespentShopListFragment AppV 4.0.6  multiple contact Data added on Api called
            shopDurationData.multi_contact_name = shopActivity.multi_contact_name
            shopDurationData.multi_contact_number = shopActivity.multi_contact_number

            // Suman 06-05-2024 Suman SyncActivity update mantis 27335  begin
            try {
                var shopOb = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopDurationData.shop_id)
                shopDurationData.shop_lat=shopOb.shopLat.toString()
                shopDurationData.shop_long=shopOb.shopLong.toString()
                shopDurationData.shop_addr=shopOb.address.toString()
            }catch (ex:Exception){
                ex.printStackTrace()
            }
            // Suman 06-05-2024 Suman SyncActivity update mantis 27335  end

            shopDataList.add(shopDurationData)

            if (shopDataList.isEmpty()) {
                return
            }

            Timber.d("===========SYNC VISITED SHOP DATA (AVERAGE SHOP)==============")
            Timber.d("SHOP ID======> " + shopDurationData.shop_id)
            Timber.d("SPENT DURATION======> " + shopDurationData.spent_duration)
            Timber.d("VISIT DATE=========> " + shopDurationData.visited_date)
            Timber.d("VISIT DATE TIME==========> " + shopDurationData.visited_date)
            Timber.d("TOTAL VISIT COUNT========> " + shopDurationData.total_visit_count)
            Timber.d("DISTANCE TRAVELLED========> " + shopDurationData.distance_travelled)
            Timber.d("FEEDBACK========> " + shopDurationData.feedback)
            Timber.d("isFirstShopVisited========> " + shopDurationData.isFirstShopVisited)
            Timber.d("distanceFromHomeLoc========> " + shopDurationData.distanceFromHomeLoc)
            Timber.d("next_visit_date========> " + shopDurationData.next_visit_date)
            Timber.d("early_revisit_reason========> " + shopDurationData.early_revisit_reason)
            Timber.d("device_model========> " + shopDurationData.device_model)
            Timber.d("android_version========> " + shopDurationData.android_version)
            Timber.d("battery========> " + shopDurationData.battery)
            Timber.d("net_status========> " + shopDurationData.net_status)
            Timber.d("net_type========> " + shopDurationData.net_type)
            Timber.d("in_time========> " + shopDurationData.in_time)
            Timber.d("out_time========> " + shopDurationData.out_time)
            Timber.d("start_timestamp========> " + shopDurationData.start_timestamp)
            Timber.d("in_location========> " + shopDurationData.in_location)
            Timber.d("out_location========> " + shopDurationData.out_location)
            Timber.d("===================================================================")

            progress_wheel.spin()
            shopDurationApiReq.shop_list = shopDataList
            val repository = ShopDurationRepositoryProvider.provideShopDurationRepository()

            BaseActivity.compositeDisposable.add(
                    repository.shopDuration(shopDurationApiReq)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                Timber.d("ShopActivityFromAverageTime : " + "User Id" + Pref.user_id + ", Session Token" + Pref.session_token + ", SHOP_ID: " + mList[0].shopid + ", SHOP: " + mList[0].shop_name + ", RESPONSE:" + result.message)
                                if (result.status == NetworkConstant.SUCCESS) {
                                    //AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shopId, selectedDate)
                                    //mAvgTimeSpentListAdapter.updateList(AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate))

                                    doAsync {

                                        if (!Pref.isMultipleVisitEnable)
                                            AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shopId, selectedDate)
                                        else
                                            AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shopId, selectedDate, shopActivity.startTimeStamp)

                                        /*var unSyncedList: List<ShopVisitImageModelEntity>? = null
                                    for (i in shopDataList.indices) {
                                        unSyncedList = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(false, shopDataList[i].shop_id!!, shopDataList[i].visited_date!!)
                                    }*/


                                        val unSyncedList = ArrayList<ShopVisitImageModelEntity>()

                                        if (!Pref.isMultipleVisitEnable) {
                                            for (i in shopDataList.indices) {
                                                val unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(false, shopDataList[i].shop_id!!, shopDataList[i].visited_date!!)

                                                if (unSyncedData != null && unSyncedData.isNotEmpty()) {
                                                    unSyncedList.add(unSyncedData[0])
                                                }
                                            }
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            if (unSyncedList.size > 0) {
                                                callShopVisitImageUploadApi(unSyncedList, false, null)
                                            } else {

                                                val unSyncedAudioList = ArrayList<ShopVisitAudioEntity>()

                                                if (!Pref.isMultipleVisitEnable) {
                                                    for (i in shopDataList.indices) {
                                                        val unSyncedData = AppDatabase.getDBInstance()!!.shopVisitAudioDao().getTodaysUnSyncedListAccordingToShopId(false, shopDataList[i].shop_id!!, shopDataList[i].visited_date!!)

                                                        if (unSyncedData != null && unSyncedData.isNotEmpty()) {
                                                            unSyncedAudioList.add(unSyncedData[0])
                                                        }
                                                    }
                                                }

                                                if (unSyncedAudioList.isNotEmpty())
                                                    callShopVisitAudioUploadApi(unSyncedAudioList, false, null)
                                                else {
                                                    shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                                    Collections.reverse(shopActivityEntityList)
                                                    (mContext as DashboardActivity).showSnackMessage("Synced successfully")
                                                    mAvgTimeSpentListAdapter.updateList(AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate))
                                                }
                                            }
                                        }
                                    }


                                } else {
                                    shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                    Collections.reverse(shopActivityEntityList)
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage(mContext.getString(R.string.unable_to_sync))
                                }

                            }, { error ->
                                error.printStackTrace()
                                progress_wheel.stopSpinning()
                                shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                Collections.reverse(shopActivityEntityList)
                                (mContext as DashboardActivity).showSnackMessage(mContext.getString(R.string.unable_to_sync))

                                if (error != null) {
                                    Timber.d("ShopActivityFromAverageTime : " + "User Id" + Pref.user_id + ", Session Token" + Pref.session_token + ", SHOP_ID: " + mList[0].shopid + ", SHOP: " + mList[0].shop_name + ", ERROR:" + error.localizedMessage)
                                    // Timber.d("ShopActivityFromAverageTime : " + ", SHOP: " + mList[0].shop_name + ", ERROR:" + error.localizedMessage)
                                }
                            })
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun doNothing() {

    }


    private fun initiatePopupWindow(view: View, position: Int) {
        val popup = PopupWindow(context)
        val layout = layoutInflater.inflate(R.layout.popup_window_shop_item, null)

        popup.contentView = layout
        popup.isOutsideTouchable = true
        popup.isFocusable = true

        var call_ll: LinearLayout = layout.findViewById(R.id.call_ll)
        var direction_ll: LinearLayout = layout.findViewById(R.id.direction_ll)
        var add_order_ll: LinearLayout = layout.findViewById(R.id.add_order_ll)

        var call_iv: ImageView = layout.findViewById(R.id.call_iv)
        var call_tv: TextView = layout.findViewById(R.id.call_tv)
        var direction_iv: ImageView = layout.findViewById(R.id.direction_iv)
        var direction_tv: TextView = layout.findViewById(R.id.direction_tv)
        var order_iv: ImageView = layout.findViewById(R.id.order_iv)
        var order_tv: TextView = layout.findViewById(R.id.order_tv)


        call_ll.setOnClickListener(View.OnClickListener {
            call_iv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_registered_shop_call_select))

            order_iv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_registered_shop_add_order_deselect))
            direction_iv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_registered_shop_direction_deselect))
            order_tv.setTextColor(ContextCompat.getColor(mContext, R.color.login_txt_color))
            direction_tv.setTextColor(ContextCompat.getColor(mContext, R.color.login_txt_color))

            call_tv.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
            popup.dismiss()

        })

        direction_ll.setOnClickListener(View.OnClickListener {
            direction_iv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_registered_shop_direction_select))

            call_iv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_registered_shop_call_deselect))
            order_iv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_registered_shop_add_order_deselect))
            call_tv.setTextColor(ContextCompat.getColor(mContext, R.color.login_txt_color))
            order_tv.setTextColor(ContextCompat.getColor(mContext, R.color.login_txt_color))

            direction_tv.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
            popup.dismiss()
            (mContext as DashboardActivity).openLocationWithTrack()

        })

        add_order_ll.setOnClickListener(View.OnClickListener {
            order_iv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_registered_shop_add_order_select))

            call_iv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_registered_shop_call_deselect))
            direction_iv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_registered_shop_direction_deselect))
            call_tv.setTextColor(ContextCompat.getColor(mContext, R.color.login_txt_color))
            direction_tv.setTextColor(ContextCompat.getColor(mContext, R.color.login_txt_color))

            order_tv.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
            popup.dismiss()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.functionality_disabled))

        })

        popup.setBackgroundDrawable(BitmapDrawable())
        popup.showAsDropDown(view)
        popup.update()

    }

    private fun syncAllShopActivity(shopId: String, list_: ArrayList<ShopActivityEntity>) {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }
        val mList = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(shopId, selectedDate)
        if (mList.isEmpty())
            return
        val shopActivity = mList[0]
//        var shopActivity = AppDatabase.getDBInstance()!!.shopActivityDao().getShopActivityForId(shopId)
        val shopDurationApiReq = ShopDurationRequest()
        shopDurationApiReq.user_id = Pref.user_id
        shopDurationApiReq.session_token = Pref.session_token
        val shopDataList: MutableList<ShopDurationRequestData> = ArrayList()
        val shopDurationData = ShopDurationRequestData()
        shopDurationData.shop_id = shopActivity.shopid
        if (shopActivity.startTimeStamp != "0" && !shopActivity.isDurationCalculated) {
            val totalMinute = AppUtils.getMinuteFromTimeStamp(shopActivity.startTimeStamp, System.currentTimeMillis().toString())
            val duration = AppUtils.getTimeFromTimeSpan(shopActivity.startTimeStamp, System.currentTimeMillis().toString())

            if (!Pref.isMultipleVisitEnable) {
                AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivity.shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi())
                AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivity.shopid!!, duration, AppUtils.getCurrentDateForShopActi())
            }
            else {
                AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivity.shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi(), shopActivity.startTimeStamp)
                AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivity.shopid!!, duration, AppUtils.getCurrentDateForShopActi(), shopActivity.startTimeStamp)
            }

            shopDurationData.spent_duration = duration
        } else {
            shopDurationData.spent_duration = shopActivity.duration_spent
        }
        shopDurationData.visited_date = shopActivity.visited_date
        shopDurationData.visited_time = shopActivity.visited_date
        if (TextUtils.isEmpty(shopActivity.distance_travelled))
            shopActivity.distance_travelled = "0.0"
        shopDurationData.distance_travelled = shopActivity.distance_travelled
        val list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdList(shopDurationData.shop_id)
        if (list != null && list.isNotEmpty())
            shopDurationData.total_visit_count = list[0].totalVisitCount

        if (!TextUtils.isEmpty(shopActivity.feedback))
            shopDurationData.feedback = shopActivity.feedback
        else
            shopDurationData.feedback = ""

        shopDurationData.isFirstShopVisited = shopActivity.isFirstShopVisited
        shopDurationData.distanceFromHomeLoc = shopActivity.distance_from_home_loc

        shopDurationData.next_visit_date = shopActivity.next_visit_date

        if (!TextUtils.isEmpty(shopActivity.early_revisit_reason))
            shopDurationData.early_revisit_reason = shopActivity.early_revisit_reason
        else
            shopDurationData.early_revisit_reason = ""

        shopDurationData.device_model = shopActivity.device_model
        shopDurationData.android_version = shopActivity.android_version
        shopDurationData.battery = shopActivity.battery
        shopDurationData.net_status = shopActivity.net_status
        shopDurationData.net_type = shopActivity.net_type

        shopDurationData.in_time = shopActivity.in_time
        shopDurationData.out_time = shopActivity.out_time
        shopDurationData.start_timestamp = shopActivity.startTimeStamp
        shopDurationData.in_location = shopActivity.in_loc
        shopDurationData.out_location = shopActivity.out_loc


        shopDurationData.shop_revisit_uniqKey = shopActivity.shop_revisit_uniqKey!!


        /*10-12-2021*/
        shopDurationData.updated_by = Pref.user_id
        try{
            shopDurationData.updated_on = shopActivity.updated_on!!
        }catch(ex:Exception){
            shopDurationData.updated_on = ""
        }


        if (!TextUtils.isEmpty(shopActivity.pros_id!!))
            shopDurationData.pros_id = shopActivity.pros_id!!
        else
            shopDurationData.pros_id = ""

        if (!TextUtils.isEmpty(shopActivity.agency_name!!))
            shopDurationData.agency_name =shopActivity.agency_name!!
        else
            shopDurationData.agency_name = ""

        if (!TextUtils.isEmpty(shopActivity.approximate_1st_billing_value))
            shopDurationData.approximate_1st_billing_value = shopActivity.approximate_1st_billing_value!!
        else
            shopDurationData.approximate_1st_billing_value = ""
        //duration garbage fix
        try{
            if(shopDurationData.spent_duration!!.contains("-") || shopDurationData.spent_duration!!.length != 8)
            {
                shopDurationData.spent_duration="00:00:10"
            }
        }catch (ex:Exception){
            shopDurationData.spent_duration="00:00:10"
        }
        //New shop Create issue
        shopDurationData.isnewShop = shopActivity.isnewShop!!

        // 1.0 AvgTimespentShopListFragment AppV 4.0.6  multiple contact Data added on Api called
        shopDurationData.multi_contact_name = shopActivity.multi_contact_name
        shopDurationData.multi_contact_number = shopActivity.multi_contact_number

        // Suman 06-05-2024 Suman SyncActivity update mantis 27335  begin
        try {
            var shopOb = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopDurationData.shop_id)
            shopDurationData.shop_lat=shopOb.shopLat.toString()
            shopDurationData.shop_long=shopOb.shopLong.toString()
            shopDurationData.shop_addr=shopOb.address.toString()
        }catch (ex:Exception){
            ex.printStackTrace()
        }
        // Suman 06-05-2024 Suman SyncActivity update mantis 27335  end

        shopDataList.add(shopDurationData)

        if (shopDataList.isEmpty()) {
            return
        }

        Log.e("Average Time spent Shop", "isShopActivityUpdating=============> " + BaseActivity.isShopActivityUpdating)
        if (BaseActivity.isShopActivityUpdating)
            return

        BaseActivity.isShopActivityUpdating = true

        Timber.d("===========SYNC ALL VISITED SHOP DATA (AVERAGE SHOP)========")
        Timber.d("SHOP ID======> " + shopDurationData.shop_id)
        Timber.d("SPENT DURATION======> " + shopDurationData.spent_duration)
        Timber.d("VISIT DATE=========> " + shopDurationData.visited_date)
        Timber.d("VISIT DATE TIME==========> " + shopDurationData.visited_date)
        Timber.d("TOTAL VISIT COUNT========> " + shopDurationData.total_visit_count)
        Timber.d("DISTANCE TRAVELLED========> " + shopDurationData.distance_travelled)
        Timber.d("FEEDBACK========> " + shopDurationData.feedback)
        Timber.d("isFirstShopVisited========> " + shopDurationData.isFirstShopVisited)
        Timber.d("distanceFromHomeLoc========> " + shopDurationData.distanceFromHomeLoc)
        Timber.d("next_visit_date========> " + shopDurationData.next_visit_date)
        Timber.d("early_revisit_reason========> " + shopDurationData.early_revisit_reason)
        Timber.d("device_model========> " + shopDurationData.device_model)
        Timber.d("android_version========> " + shopDurationData.android_version)
        Timber.d("battery========> " + shopDurationData.battery)
        Timber.d("net_status========> " + shopDurationData.net_status)
        Timber.d("net_type========> " + shopDurationData.net_type)
        Timber.d("in_time========> " + shopDurationData.in_time)
        Timber.d("out_time========> " + shopDurationData.out_time)
        Timber.d("start_timestamp========> " + shopDurationData.start_timestamp)
        Timber.d("in_location========> " + shopDurationData.in_location)
        Timber.d("out_location========> " + shopDurationData.out_location)
        Timber.d("==============================================================")

        progress_wheel.spin()
        shopDurationApiReq.shop_list = shopDataList
        val repository = ShopDurationRepositoryProvider.provideShopDurationRepository()

        BaseActivity.compositeDisposable.add(
                repository.shopDuration(shopDurationApiReq)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            Timber.d("ShopActivityFromAverageTime : RESPONSE STATUS:= " + result.status + ", RESPONSE MESSAGE:= " + result.message +
                                    "\nUser Id" + Pref.user_id + ", Session Token" + Pref.session_token + ", SHOP_ID: " + mList[0].shopid +
                                    ", SHOP: " + mList[0].shop_name)
                            if (result.status == NetworkConstant.SUCCESS) {
                                if (!Pref.isMultipleVisitEnable)
                                    AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shopId, selectedDate)
                                else
                                    AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shopId, selectedDate, shopActivity.startTimeStamp)
                                //
                                syncAllInc++
                                if (syncAllInc < list_.size) {

                                    /*val unSyncedList = ArrayList<ShopVisitImageModelEntity>()
                                    for (i in shopDataList.indices) {
                                        val unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(false, shopDataList[i].shop_id!!, shopDataList[i].visited_date!!)

                                        if (unSyncedData != null && unSyncedData.isNotEmpty()) {
                                            unSyncedList.add(unSyncedData[0])
                                        }
                                    }*/

                                    progress_wheel.stopSpinning()
                                    /*if (unSyncedList.size > 0) {
                                        callShopVisitImageUploadApi(unSyncedList, true, list_)
                                    } else {*/
                                    BaseActivity.isShopActivityUpdating = false
                                    syncAllShopActivity(list_[syncAllInc].shopid!!, list_)
                                    //}

                                } else {
                                    syncAllInc = 0
                                    val unSyncedList = ArrayList<ShopVisitImageModelEntity>()

                                    if (!Pref.isMultipleVisitEnable) {
                                        for (i in list_.indices) {
                                            //val unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(false, list_[i].shopid!!, list_[i].visited_date!!)

                                            var unSyncedData: List<ShopVisitImageModelEntity>? = null

                                            if (AppUtils.isVisitSync == "1") {
                                                unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysListAccordingToShopId(
                                                        list_[i].shopid!!, list_[i].visited_date!!)
                                            } else {
                                                unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(
                                                        false, list_[i].shopid!!, list_[i].visited_date!!)
                                            }

                                            if (unSyncedData != null && unSyncedData.isNotEmpty()) {
                                                unSyncedList.add(unSyncedData[0])
                                            }
                                        }
                                    }
                                    progress_wheel.stopSpinning()
                                    if (unSyncedList.size > 0) {
                                        BaseActivity.isShopActivityUpdating = false
                                        //callShopVisitImageUploadApi(unSyncedList, true, list_)
                                        j = 0
                                        callShopVisitImageUploadApiForAll(unSyncedList)
                                    } else {
                                        BaseActivity.isShopActivityUpdating = false

                                        val unSyncedAudioList = ArrayList<ShopVisitAudioEntity>()

                                        if (!Pref.isMultipleVisitEnable) {
                                            for (i in shopActivityEntityList.indices) {
                                                if (shopActivityEntityList[i].isDurationCalculated && shopActivityEntityList[i].isUploaded) {
                                                    //val unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao().getTodaysUnSyncedListAccordingToShopId(false, shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)

                                                    var unSyncedData: List<ShopVisitAudioEntity>? = null
                                                    if (AppUtils.isVisitSync == "1")
                                                        unSyncedData = AppDatabase.getDBInstance()!!.shopVisitAudioDao().getTodaysListAccordingToShopId(shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)
                                                    else
                                                        unSyncedData = AppDatabase.getDBInstance()!!.shopVisitAudioDao().getTodaysUnSyncedListAccordingToShopId(false,
                                                                shopActivityEntityList[i].shopid!!, shopActivityEntityList[i].visited_date!!)

                                                    if (unSyncedData != null && unSyncedData.isNotEmpty()) {
                                                        unSyncedAudioList.add(unSyncedData[0])
                                                    }
                                                }
                                            }
                                        }

                                        if (unSyncedAudioList.isNotEmpty()) {
                                            j = 0
                                            callShopVisitAudioUploadApiForAll(unSyncedAudioList)
                                        }
                                        else {
                                            shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                            Collections.reverse(shopActivityEntityList)
                                            //mAvgTimeSpentListAdapter.updateList(AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate))

                                            Timber.d("===========INIT ADAPTER FOR SYNC ALL REVISIT SHOP DATA (AVERAGE TIME SPENT SHOP)========")
                                            Timber.d("shop list size====> " + shopActivityEntityList.size)
                                            Timber.d("specific date====> $selectedDate")

                                            initAdapter()
                                        }
                                    }
                                }

                            } else {
                                BaseActivity.isShopActivityUpdating = false
                                shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                Collections.reverse(shopActivityEntityList)
                                (mContext as DashboardActivity).showSnackMessage(mContext.getString(R.string.unable_to_sync))
                            }

                        }, { error ->
                            error.printStackTrace()
                            BaseActivity.isShopActivityUpdating = false
                            progress_wheel.stopSpinning()
                            shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                            Collections.reverse(shopActivityEntityList)
                            Timber.d("ShopActivityFromAverageTime : ERROR:= " + error.localizedMessage + "\nUser Id" + Pref.user_id +
                                    ", Session Token" + Pref.session_token + ", SHOP_ID: " + mList[0].shopid + ", SHOP: " + mList[0].shop_name)
                            if (error != null) {
                                //  Timber.d("ShopActivityFromAverageShop : " + ", SHOP: " + mList[0].shop_name + ", ERROR:" + error.localizedMessage)
                                (mContext as DashboardActivity).showSnackMessage(mContext.getString(R.string.unable_to_sync))
                            }
                        })
        )

    }

    private fun callShopVisitImageUploadApi(unSyncedList: List<ShopVisitImageModelEntity>, isAllSync: Boolean, list_: ArrayList<ShopActivityEntity>?) {

        try {

            val visitImageShop = ShopVisitImageUploadInputModel()
            visitImageShop.session_token = Pref.session_token
            visitImageShop.user_id = Pref.user_id
            visitImageShop.shop_id = unSyncedList[0].shop_id
            visitImageShop.visit_datetime = unSyncedList[0].visit_datetime

            Timber.d("========UPLOAD REVISIT SINGLE IMAGE INPUT PARAMS (AVERAGE TIME SPENT SHOP)======")
            Timber.d("USER ID======> " + visitImageShop.user_id)
            Timber.d("SESSION ID======> " + visitImageShop.session_token)
            Timber.d("SHOP ID=========> " + visitImageShop.shop_id)
            Timber.d("VISIT DATE TIME==========> " + visitImageShop.visit_datetime)
            Timber.d("IMAGE========> " + unSyncedList[j].shop_image)
            Timber.d("================================================================================")

            val repository = ShopVisitImageUploadRepoProvider.provideAddShopRepository()

            progress_wheel.spin()
            BaseActivity.compositeDisposable.add(
                    repository.visitShopWithImage(visitImageShop, unSyncedList[0].shop_image!!, mContext)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val logoutResponse = result as BaseResponse
                                Timber.d("UPLOAD REVISIT SINGLE IMAGE : " + "RESPONSE : " + logoutResponse.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + logoutResponse.message)
                                progress_wheel.stopSpinning()
                                if (logoutResponse.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.shopVisitImageDao().updateisUploaded(true, unSyncedList[0].shop_id!!)

                                    val unSyncedAudioList = AppDatabase.getDBInstance()!!.shopVisitAudioDao().getTodaysUnSyncedListAccordingToShopId(false,
                                            shopActivityEntityList[mPosition].shopid!!, shopActivityEntityList[mPosition].visited_date!!)

                                    if (unSyncedAudioList != null && unSyncedAudioList.isNotEmpty()) {
                                        callShopVisitAudioUploadApi(unSyncedAudioList, false, null)
                                    }
                                } else {
                                    if (!isAllSync) {
                                        shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                        Collections.reverse(shopActivityEntityList)
                                        (mContext as DashboardActivity).showSnackMessage(mContext.getString(R.string.unable_to_sync))
                                    } else {
                                        BaseActivity.isShopActivityUpdating = false
                                        syncAllShopActivity(list_?.get(syncAllInc)?.shopid!!, list_)
                                    }
                                }

                            }, { error ->
                                error.printStackTrace()
                                Timber.d("UPLOAD REVISIT SINGLE IMAGE : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                                progress_wheel.stopSpinning()
                                if (!isAllSync) {
                                    shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                    Collections.reverse(shopActivityEntityList)
                                    (mContext as DashboardActivity).showSnackMessage(mContext.getString(R.string.unable_to_sync))
                                } else {
                                    BaseActivity.isShopActivityUpdating = false
                                    syncAllShopActivity(list_?.get(syncAllInc)?.shopid!!, list_)
                                }
                            })
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callShopVisitAudioUploadApi(unSyncedAudioList: List<ShopVisitAudioEntity>, isAllSync: Boolean, list_: ArrayList<ShopActivityEntity>?) {
        try {

            val visitImageShop = ShopVisitImageUploadInputModel()
            visitImageShop.session_token = Pref.session_token
            visitImageShop.user_id = Pref.user_id
            visitImageShop.shop_id = unSyncedAudioList[0].shop_id
            visitImageShop.visit_datetime = unSyncedAudioList[0].visit_datetime

            val repository = ShopVisitImageUploadRepoProvider.provideAddShopRepository()

            Timber.d("=======UPLOAD REVISIT SINGLE AUDIO INPUT PARAMS (AVERAGE SHOP)=======")
            Timber.d("USER ID======> " + visitImageShop.user_id)
            Timber.d("SESSION ID======> " + visitImageShop.session_token)
            Timber.d("SHOP ID=========> " + visitImageShop.shop_id)
            Timber.d("VISIT DATE TIME==========> " + visitImageShop.visit_datetime)
            Timber.d("AUDIO========> " + unSyncedAudioList[0].audio)
            Timber.d("======================================================================")

            progress_wheel.spin()
            BaseActivity.compositeDisposable.add(
                    repository.visitShopWithAudio(visitImageShop, unSyncedAudioList[0].audio!!, mContext)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val logoutResponse = result as BaseResponse
                                progress_wheel.stopSpinning()
                                Timber.d("UPLOAD REVISIT SINGLE IMAGE : " + "RESPONSE : " + logoutResponse.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + logoutResponse.message)
                                if (logoutResponse.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.shopVisitAudioDao().updateisUploaded(true, unSyncedAudioList[0].shop_id!!)

                                    if (!isAllSync) {
                                        shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                        Collections.reverse(shopActivityEntityList)
                                        //mAvgTimeSpentListAdapter.updateList(AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate))
                                        initAdapter()
                                        (mContext as DashboardActivity).showSnackMessage("Synced successfully")
                                    } else {
                                        BaseActivity.isShopActivityUpdating = false
                                        syncAllShopActivity(list_?.get(syncAllInc)?.shopid!!, list_)
                                    }
                                } else {
                                    if (!isAllSync) {
                                        shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                        Collections.reverse(shopActivityEntityList)
                                        //mAvgTimeSpentListAdapter.updateList(AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate))
                                        initAdapter()
                                        (mContext as DashboardActivity).showSnackMessage("Synced successfully")
                                    } else {
                                        BaseActivity.isShopActivityUpdating = false
                                        syncAllShopActivity(list_?.get(syncAllInc)?.shopid!!, list_)
                                    }
                                }

                            }, { error ->
                                Timber.d("UPLOAD REVISIT SINGLE IMAGE : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                                error.printStackTrace()
                                progress_wheel.stopSpinning()
                                if (!isAllSync) {
                                    shopActivityEntityList = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate)
                                    Collections.reverse(shopActivityEntityList)
                                    //mAvgTimeSpentListAdapter.updateList(AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(selectedDate))
                                    initAdapter()
                                    (mContext as DashboardActivity).showSnackMessage("Synced successfully")
                                } else {
                                    BaseActivity.isShopActivityUpdating = false
                                    syncAllShopActivity(list_?.get(syncAllInc)?.shopid!!, list_)
                                }
                            })
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun syncShopList() {
        val shopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getUnSyncedShops(false)
        if (shopList.isEmpty()){

        }
        else {
            val addShopData = AddShopRequestData()
            val mAddShopDBModelEntity = shopList[0]
            addShopData.session_token = Pref.session_token
            addShopData.address = mAddShopDBModelEntity.address
            addShopData.owner_contact_no = mAddShopDBModelEntity.ownerContactNumber
            addShopData.owner_email = mAddShopDBModelEntity.ownerEmailId
            addShopData.owner_name = mAddShopDBModelEntity.ownerName
            addShopData.pin_code = mAddShopDBModelEntity.pinCode
            addShopData.shop_lat = mAddShopDBModelEntity.shopLat.toString()
            addShopData.shop_long = mAddShopDBModelEntity.shopLong.toString()
            addShopData.shop_name = mAddShopDBModelEntity.shopName.toString()
            addShopData.type = mAddShopDBModelEntity.type.toString()
            addShopData.shop_id = mAddShopDBModelEntity.shop_id
            addShopData.user_id = Pref.user_id
            addShopData.assigned_to_dd_id = mAddShopDBModelEntity.assigned_to_dd_id
            addShopData.assigned_to_pp_id = mAddShopDBModelEntity.assigned_to_pp_id
            addShopData.added_date = mAddShopDBModelEntity.added_date
            addShopData.amount = mAddShopDBModelEntity.amount
            addShopData.area_id = mAddShopDBModelEntity.area_id
            addShopData.model_id = mAddShopDBModelEntity.model_id
            addShopData.primary_app_id = mAddShopDBModelEntity.primary_app_id
            addShopData.secondary_app_id = mAddShopDBModelEntity.secondary_app_id
            addShopData.lead_id = mAddShopDBModelEntity.lead_id
            addShopData.stage_id = mAddShopDBModelEntity.stage_id
            addShopData.funnel_stage_id = mAddShopDBModelEntity.funnel_stage_id
            addShopData.booking_amount = mAddShopDBModelEntity.booking_amount
            addShopData.type_id = mAddShopDBModelEntity.type_id

            addShopData.director_name = mAddShopDBModelEntity.director_name
            addShopData.key_person_name = mAddShopDBModelEntity.person_name
            addShopData.phone_no = mAddShopDBModelEntity.person_no

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.family_member_dob))
                addShopData.family_member_dob = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.family_member_dob)

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.add_dob))
                addShopData.addtional_dob = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.add_dob)

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.add_doa))
                addShopData.addtional_doa = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.add_doa)

            addShopData.specialization = mAddShopDBModelEntity.specialization
            addShopData.category = mAddShopDBModelEntity.category
            addShopData.doc_address = mAddShopDBModelEntity.doc_address
            addShopData.doc_pincode = mAddShopDBModelEntity.doc_pincode
            addShopData.is_chamber_same_headquarter = mAddShopDBModelEntity.chamber_status.toString()
            addShopData.is_chamber_same_headquarter_remarks = mAddShopDBModelEntity.remarks
            addShopData.chemist_name = mAddShopDBModelEntity.chemist_name
            addShopData.chemist_address = mAddShopDBModelEntity.chemist_address
            addShopData.chemist_pincode = mAddShopDBModelEntity.chemist_pincode
            addShopData.assistant_contact_no = mAddShopDBModelEntity.assistant_no
            addShopData.average_patient_per_day = mAddShopDBModelEntity.patient_count
            addShopData.assistant_name = mAddShopDBModelEntity.assistant_name

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.doc_family_dob))
                addShopData.doc_family_member_dob = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.doc_family_dob)

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.assistant_dob))
                addShopData.assistant_dob = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.assistant_dob)

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.assistant_doa))
                addShopData.assistant_doa = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.assistant_doa)

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.assistant_family_dob))
                addShopData.assistant_family_dob = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.assistant_family_dob)

            addShopData.entity_id = mAddShopDBModelEntity.entity_id
            addShopData.party_status_id = mAddShopDBModelEntity.party_status_id
            addShopData.retailer_id = mAddShopDBModelEntity.retailer_id
            addShopData.dealer_id = mAddShopDBModelEntity.dealer_id
            addShopData.beat_id = mAddShopDBModelEntity.beat_id
            addShopData.assigned_to_shop_id = mAddShopDBModelEntity.assigned_to_shop_id
            addShopData.actual_address = mAddShopDBModelEntity.actual_address

            try {
                var uniqKeyObj = AppDatabase.getDBInstance()!!.shopActivityDao().getNewShopActivityKey(mAddShopDBModelEntity.shop_id, false)
                addShopData.shop_revisit_uniqKey = uniqKeyObj?.shop_revisit_uniqKey!!
            }catch (ex:Exception){
                addShopData.shop_revisit_uniqKey = ""
            }

            addShopData.project_name = mAddShopDBModelEntity.project_name
            addShopData.landline_number = mAddShopDBModelEntity.landline_number
            addShopData.agency_name = mAddShopDBModelEntity.agency_name

            addShopData.alternateNoForCustomer = mAddShopDBModelEntity.alternateNoForCustomer
            addShopData.whatsappNoForCustomer = mAddShopDBModelEntity.whatsappNoForCustomer

             // duplicate shop api call
            addShopData.isShopDuplicate=mAddShopDBModelEntity.isShopDuplicate
            addShopData.purpose=mAddShopDBModelEntity.purpose

            addShopData.GSTN_Number=mAddShopDBModelEntity.gstN_Number
            addShopData.ShopOwner_PAN=mAddShopDBModelEntity.shopOwner_PAN

            callAddShopApi(addShopData, mAddShopDBModelEntity.shopImageLocalPath, shopList, true,
                    mAddShopDBModelEntity.doc_degree)

        }


    }

    fun callAddShopApi(addShop: AddShopRequestData, shop_imgPath: String?, shopList: MutableList<AddShopDBModelEntity>?,
                       isFromInitView: Boolean, degree_imgPath: String?) {
        if (!AppUtils.isOnline(mContext)) {
            (this as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        Timber.d("=============SyncShop Input Params=================")
        Timber.d("shop id=======> " + addShop.shop_id)
        val index = addShop.shop_id!!.indexOf("_")
        Timber.d("decoded shop id=======> " + addShop.user_id + "_" + AppUtils.getDate(addShop.shop_id!!.substring(index + 1, addShop.shop_id!!.length).toLong()))
        Timber.d("shop added date=======> " + addShop.added_date)
        Timber.d("shop address=======> " + addShop.address)
        Timber.d("assigned to dd id=======> " + addShop.assigned_to_dd_id)
        Timber.d("assigned to pp id=======> " + addShop.assigned_to_pp_id)
        Timber.d("date aniversery=======> " + addShop.date_aniversary)
        Timber.d("dob=======> " + addShop.dob)
        Timber.d("shop owner phn no=======> " + addShop.owner_contact_no)
        Timber.d("shop owner email=======> " + addShop.owner_email)
        Timber.d("shop owner name=======> " + addShop.owner_name)
        Timber.d("shop pincode=======> " + addShop.pin_code)
        Timber.d("session token=======> " + addShop.session_token)
        Timber.d("shop lat=======> " + addShop.shop_lat)
        Timber.d("shop long=======> " + addShop.shop_long)
        Timber.d("shop name=======> " + addShop.shop_name)
        Timber.d("shop type=======> " + addShop.type)
        Timber.d("user id=======> " + addShop.user_id)
        Timber.d("amount=======> " + addShop.amount)
        Timber.d("area id=======> " + addShop.area_id)
        Timber.d("model id=======> " + addShop.model_id)
        Timber.d("primary app id=======> " + addShop.primary_app_id)
        Timber.d("secondary app id=======> " + addShop.secondary_app_id)
        Timber.d("lead id=======> " + addShop.lead_id)
        Timber.d("stage id=======> " + addShop.stage_id)
        Timber.d("funnel stage id=======> " + addShop.funnel_stage_id)
        Timber.d("booking amount=======> " + addShop.booking_amount)
        Timber.d("type id=======> " + addShop.type_id)

        if (shop_imgPath != null)
            Timber.d("shop image path=======> $shop_imgPath")

        Timber.d("director name=======> " + addShop.director_name)
        Timber.d("family member dob=======> " + addShop.family_member_dob)
        Timber.d("key person's name=======> " + addShop.key_person_name)
        Timber.d("phone no=======> " + addShop.phone_no)
        Timber.d("additional dob=======> " + addShop.addtional_dob)
        Timber.d("additional doa=======> " + addShop.addtional_doa)
        Timber.d("family member dob=======> " + addShop.family_member_dob)
        Timber.d("key person's name=======> " + addShop.key_person_name)
        Timber.d("phone no=======> " + addShop.phone_no)
        Timber.d("additional dob=======> " + addShop.addtional_dob)
        Timber.d("additional doa=======> " + addShop.addtional_doa)
        Timber.d("doctor family member dob=======> " + addShop.doc_family_member_dob)
        Timber.d("specialization=======> " + addShop.specialization)
        Timber.d("average patient count per day=======> " + addShop.average_patient_per_day)
        Timber.d("category=======> " + addShop.category)
        Timber.d("doctor address=======> " + addShop.doc_address)
        Timber.d("doctor pincode=======> " + addShop.doc_pincode)
        Timber.d("chambers or hospital under same headquarter=======> " + addShop.is_chamber_same_headquarter)
        Timber.d("chamber related remarks=======> " + addShop.is_chamber_same_headquarter_remarks)
        Timber.d("chemist name=======> " + addShop.chemist_name)
        Timber.d("chemist name=======> " + addShop.chemist_address)
        Timber.d("chemist pincode=======> " + addShop.chemist_pincode)
        Timber.d("assistant name=======> " + addShop.assistant_name)
        Timber.d("assistant contact no=======> " + addShop.assistant_contact_no)
        Timber.d("assistant dob=======> " + addShop.assistant_dob)
        Timber.d("assistant date of anniversary=======> " + addShop.assistant_doa)
        Timber.d("assistant family dob=======> " + addShop.assistant_family_dob)
        Timber.d("entity id=======> " + addShop.entity_id)
        Timber.d("party status id=======> " + addShop.party_status_id)
        Timber.d("retailer id=======> " + addShop.retailer_id)
        Timber.d("dealer id=======> " + addShop.dealer_id)
        Timber.d("beat id=======> " + addShop.beat_id)
        Timber.d("assigned to shop id=======> " + addShop.assigned_to_shop_id)
        Timber.d("actual address=======> " + addShop.actual_address)

        if (degree_imgPath != null)
            Timber.d("doctor degree image path=======> $degree_imgPath")
        Timber.d("====================================================")

        if (TextUtils.isEmpty(shop_imgPath) && TextUtils.isEmpty(degree_imgPath)) {
            val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addShop(addShop)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as AddShopResponse
                                Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + ", RESPONSE:" + result.message)

                                when (addShopResult.status) {
                                    NetworkConstant.SUCCESS -> {
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)

                                        doAsync {
                                            uiThread {
                                                syncShopList()
                                            }
                                        }
                                    }
                                    NetworkConstant.DUPLICATE_SHOP_ID -> {
                                        Timber.d("DuplicateShop : " + ", SHOP: " + addShop.shop_name)
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)


                                        if (AppDatabase.getDBInstance()!!.addShopEntryDao().getDuplicateShopData(addShop.owner_contact_no).size > 0) {
                                            AppDatabase.getDBInstance()!!.addShopEntryDao().deleteShopById(addShop.shop_id)
                                            AppDatabase.getDBInstance()!!.shopActivityDao().deleteShopByIdAndDate(addShop.shop_id!!, AppUtils.getCurrentDateForShopActi())
                                        }
                                        doAsync {
                                            uiThread {
                                                syncShopList()
                                            }
                                        }

                                    }
                                    else -> {
                                        (this as DashboardActivity).showSnackMessage(addShopResult.message!!)
                                    }
                                }
                            }, { error ->
                                error.printStackTrace()
                                (this as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                if (error != null)
                                    Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + error.localizedMessage)
                            })
            )
        }
        else {
            val repository = AddShopRepositoryProvider.provideAddShopRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addShopWithImage(addShop, shop_imgPath, degree_imgPath, mContext)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as AddShopResponse
                                Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + ", RESPONSE:" + result.message)

                                when (addShopResult.status) {
                                    NetworkConstant.SUCCESS -> {
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)


                                        doAsync {
                                            uiThread {
                                                syncShopList()
                                            }
                                        }
                                    }
                                    NetworkConstant.DUPLICATE_SHOP_ID -> {
                                        Timber.d("DuplicateShop : " + ", SHOP: " + addShop.shop_name)
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)

                                        if (AppDatabase.getDBInstance()!!.addShopEntryDao().getDuplicateShopData(addShop.owner_contact_no).size > 0) {
                                            AppDatabase.getDBInstance()!!.addShopEntryDao().deleteShopById(addShop.shop_id)
                                            AppDatabase.getDBInstance()!!.shopActivityDao().deleteShopByIdAndDate(addShop.shop_id!!, AppUtils.getCurrentDateForShopActi())
                                        }
                                        doAsync {
                                            uiThread {
                                                syncShopList()
                                            }
                                        }

                                    }
                                    else -> {
                                        (this as DashboardActivity).showSnackMessage(addShopResult.message!!)
                                    }
                                }
                            }, { error ->
                                error.printStackTrace()
                                (this as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                if (error != null)
                                    Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + error.localizedMessage)
                            })
            )
        }
    }

}