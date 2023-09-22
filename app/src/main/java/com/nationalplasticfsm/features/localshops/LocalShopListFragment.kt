package com.nationalplasticfsm.features.localshops

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout

import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.AppDatabase
import com.nationalplasticfsm.app.MaterialSearchView
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.SearchListener
import com.nationalplasticfsm.app.domain.AddShopDBModelEntity
import com.nationalplasticfsm.app.types.FragType
import com.nationalplasticfsm.app.uiaction.IntentActionable
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.app.utils.FTStorageUtils
import com.nationalplasticfsm.app.utils.PermissionUtils
import com.nationalplasticfsm.app.utils.Toaster
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.features.commondialogsinglebtn.AddFeedbackSingleBtnDialog
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.features.location.LocationWizard.Companion.NEARBY_RADIUS
import com.nationalplasticfsm.features.location.SingleShotLocationProvider
import com.nationalplasticfsm.widgets.AppCustomTextView
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by riddhi on 2/1/18.
 */
//Revision History
// 1.0 LocalShopListFragment saheli 24-02-2032 AppV 4.0.7 mantis 0025683
class LocalShopListFragment : BaseFragment(), View.OnClickListener {


    private var localShopsListAdapter: LocalShopsListAdapter?= null
    private lateinit var nearByShopsList: RecyclerView
    private lateinit var mContext: Context
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var noShopAvailable: AppCompatTextView
    private var list: MutableList<AddShopDBModelEntity> = ArrayList()
    private lateinit var floating_fab: FloatingActionMenu
    private lateinit var programFab1: FloatingActionButton
    private lateinit var programFab2: FloatingActionButton
    private lateinit var programFab3: FloatingActionButton
    private lateinit var shop_list_parent_rl: RelativeLayout
    private lateinit var progress_wheel: com.pnikosis.materialishprogress.ProgressWheel

    private lateinit var getFloatingVal: ArrayList<String>
    private val preid: Int = 100
    private var isGetLocation = -1
    private lateinit var geofenceTv: AppCompatTextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_nearby_shops, container, false)
        initView(view)

        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    (list as ArrayList<AddShopDBModelEntity>)?.let {
                        localShopsListAdapter?.refreshList(it)
                        //tv_cust_no.text = "Total customer(s): " + it.size
                    }
                } else {
                    localShopsListAdapter?.filter?.filter(query)
                }
            }
        })

        // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 start
        (mContext as DashboardActivity).searchView.setVoiceIcon(R.drawable.ic_mic)
        (mContext as DashboardActivity).searchView.setOnVoiceClickedListener({ startVoiceInput() })
        // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 end

        return view
    }

    // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 start
    private fun startVoiceInput() {
        try {
            val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?")
            try {
                startActivityForResult(intent, MaterialSearchView.REQUEST_VOICE)
            } catch (a: ActivityNotFoundException) {
                a.printStackTrace()
            }
        }
        catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MaterialSearchView.REQUEST_VOICE){
            try {
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                var t= result!![0]
                (mContext as DashboardActivity).searchView.setQuery(t,false)
            }
            catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

//            tv_search_frag_order_type_list.setText(t)
//            tv_search_frag_order_type_list.setSelection(t.length);
        }
    }
    // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 end

    override fun updateUI(any: Any) {
        super.updateUI(any)

        nearByShopsList.visibility = View.GONE
        isGetLocation = -1

        fetchNearbyShops()
    }


    private fun initView(view: View) {
        getFloatingVal = ArrayList<String>()
        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()
        nearByShopsList = view.findViewById(R.id.near_by_shops_RCV)
        noShopAvailable = view.findViewById(R.id.no_shop_tv)
        shop_list_parent_rl = view.findViewById(R.id.shop_list_parent_rl)
        geofenceTv = view.findViewById(R.id.tv_geofence_relax)

        if(Pref.IsRestrictNearbyGeofence){
            geofenceTv.visibility = View.VISIBLE
            geofenceTv.text ="Geofence Relaxed :  " + Pref.GeofencingRelaxationinMeter + " mtr"
        }
        else{
            geofenceTv.visibility = View.GONE
        }

        shop_list_parent_rl.setOnClickListener { view ->
            floating_fab.close(true)
        }
        floating_fab = view.findViewById(R.id.floating_fab)
        floating_fab.menuIconView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_dashboard_filter_icon))
        floating_fab.menuButtonColorNormal = mContext.resources.getColor(R.color.colorAccent)
        floating_fab.menuButtonColorPressed = mContext.resources.getColor(R.color.colorPrimaryDark)
        floating_fab.menuButtonColorRipple = mContext.resources.getColor(R.color.colorPrimary)

        floating_fab.isIconAnimated = false
        floating_fab.setClosedOnTouchOutside(true)

        getFloatingVal.add("Alphabetically")
        getFloatingVal.add("Visit Date")
        getFloatingVal.add("Most Visited")
        floating_fab.visibility = View.GONE

        for (i in getFloatingVal.indices) {
            if (i == 0) {
                programFab1 = FloatingActionButton(activity)
                programFab1.buttonSize = FloatingActionButton.SIZE_MINI
                programFab1.id = preid + i
                programFab1.colorNormal = mContext.resources.getColor(R.color.colorPrimaryDark)
                programFab1.colorPressed = mContext.resources.getColor(R.color.delivery_status_green)
                programFab1.colorRipple = mContext.resources.getColor(R.color.delivery_status_green)
                programFab1.labelText = getFloatingVal[0]
                floating_fab.addMenuButton(programFab1)
                programFab1.setOnClickListener(this)

            }
            if (i == 1) {
                programFab2 = FloatingActionButton(activity)
                programFab2.buttonSize = FloatingActionButton.SIZE_MINI
                programFab2.id = preid + i
                programFab2.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab2.colorPressed = mContext.resources.getColor(R.color.delivery_status_green)
                programFab2.colorRipple = mContext.resources.getColor(R.color.delivery_status_green)
                programFab2.labelText = getFloatingVal[1]
                floating_fab.addMenuButton(programFab2)
                programFab2.setOnClickListener(this)

            }

            if (i == 2) {
                programFab3 = FloatingActionButton(activity)
                programFab3.buttonSize = FloatingActionButton.SIZE_MINI
                programFab3.id = preid + i
                programFab3.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab3.colorPressed = mContext.resources.getColor(R.color.delivery_status_green)
                programFab3.colorRipple = mContext.resources.getColor(R.color.delivery_status_green)
                programFab3.labelText = getFloatingVal[2]
                floating_fab.addMenuButton(programFab3)
                programFab3.setOnClickListener(this)


            }
            //programFab1.setImageResource(R.drawable.ic_filter);
            if (i == 0) {
                programFab1.setImageResource(R.drawable.ic_tick_float_icon)
                programFab1.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
            } else if (i == 1)
                programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
            else
                programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)

        }

//        noShopAvailable.text = "No Registered " + Pref.shopText + " Available"+"\n(Suggestion: Click on the Home icon &amp; go to Shops/Customer -> Search the Customer name whom you are Nearby -> Press 'Update Address' button &amp; check again in Nearby Shops)"
        var text1 = "No Registered " + Pref.shopText + "Available"
        var text2 = "\n\n(Suggestion: Click on the Home icon & go to Shops/Customer -> Search the Customer name whom you are Nearby -> Press 'Update Address' button & check again in Nearby Shops)"
        val span1 = SpannableString(text1)
        span1.setSpan(AbsoluteSizeSpan(46), 0, text1.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        val span2 = SpannableString(text2)
        span2.setSpan(AbsoluteSizeSpan(10), 0, text2.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        val finalText: CharSequence = TextUtils.concat(span1, " ", span2)
        noShopAvailable.text =finalText.toString()
        if(Pref.IsnewleadtypeforRuby){
            initPermissionCheck()
        }

        fetchNearbyShops()
    }

    private var permissionUtils: PermissionUtils? = null
    private fun initPermissionCheck() {

        //begin mantis id 26741 Storage permission updation Suman 22-08-2023
        var permissionList = arrayOf<String>( Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permissionList += Manifest.permission.READ_MEDIA_IMAGES
            permissionList += Manifest.permission.READ_MEDIA_AUDIO
            permissionList += Manifest.permission.READ_MEDIA_VIDEO
        }else{
            permissionList += Manifest.permission.WRITE_EXTERNAL_STORAGE
            permissionList += Manifest.permission.READ_EXTERNAL_STORAGE
        }
//end mantis id 26741 Storage permission updation Suman 22-08-2023

        permissionUtils = PermissionUtils(mContext as Activity, object : PermissionUtils.OnPermissionListener {
            override fun onPermissionGranted() {
                /*if(SDK_INT >= 30){
                    if (!Environment.isExternalStorageManager()){
                        requestPermission()
                    }else{
                        callUSerListApi()
                    }
                }else{
                    callUSerListApi()
                }*/

                //callUSerListApi()
            }

            override fun onPermissionNotGranted() {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
            }
// mantis id 26741 Storage permission updation Suman 22-08-2023
        },permissionList)// arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    @SuppressLint("WrongConstant")
    private fun initAdapter() {

        if (list != null && list.size > 0) {

            Timber.d("Local Shop List:== selected list size=====> " + list.size)

            val newList = ArrayList<AddShopDBModelEntity>()

            for (i in list.indices) {
                val userId = list[i].shop_id.substring(0, list[i].shop_id.indexOf("_"))
                if (userId == Pref.user_id)
                    newList.add(list[i])
            }

            Timber.d("Local Shop List:== new selected list size=====> " + newList.size)

            noShopAvailable.visibility = View.GONE
            nearByShopsList.visibility = View.VISIBLE

            try {

                localShopsListAdapter = LocalShopsListAdapter(mContext, list,
                    object : LocalShopListClickListener {
                        override fun outLocation(shop_id: String) {

                            var ob = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shop_id)
                            var feedbackDialog: AddFeedbackSingleBtnDialog? = null
                            feedbackDialog = AddFeedbackSingleBtnDialog.getInstance(ob.shopName + "\n" + ob.ownerContactNumber, getString(R.string.confirm_revisit), shop_id, object : AddFeedbackSingleBtnDialog.OnOkClickListener {
                                override fun onOkClick(mFeedback: String, mNextVisitDate: String, filePath: String, mapproxValue: String, mprosId: String,sel_extraContNameStr:String,sel_extraContPhStr:String) {
                                    AppDatabase.getDBInstance()!!.shopActivityDao().updateFeedbackVisitdate(mFeedback,mNextVisitDate, shop_id, AppUtils.getCurrentDateForShopActi())
                                    manualProceed(shop_id)
                                }

                                override fun onCloseClick(mfeedback: String,sel_extraContNameStr :String,sel_extraContPhStr : String) {
                                    //
                                    manualProceed(shop_id)
                                }

                                override fun onClickCompetitorImg() {
                                    //
                                }
                            })
                            feedbackDialog?.show((mContext as DashboardActivity).supportFragmentManager, "AddFeedbackSingleBtnDialog")

                        }

                        override fun onQuationClick(shop: Any) {
                        (mContext as DashboardActivity).isBack = true
                        val nearbyShop: AddShopDBModelEntity = shop as AddShopDBModelEntity
                        (mContext as DashboardActivity).loadFragment(FragType.QuotationListFragment, true, nearbyShop.shop_id)
                    }

                    override fun onReturnClick(position: Int) {
                        (mContext as DashboardActivity).loadFragment(FragType.ViewAllReturnListFragment, true,list[position])
                    }

                    override fun onCallClick(shop: Any) {
                        val nearbyShop: AddShopDBModelEntity = shop as AddShopDBModelEntity
                        IntentActionable.initiatePhoneCall(mContext, nearbyShop.ownerContactNumber)
                    }

                    override fun onOrderClick(shop: Any) {
                        val nearbyShop: AddShopDBModelEntity = shop as AddShopDBModelEntity
                        if(Pref.IsActivateNewOrderScreenwithSize){
                            (mContext as DashboardActivity).loadFragment(FragType.NewOrderScrOrderDetailsFragment, true, nearbyShop.shop_id)

//                            (mContext as DashboardActivity).loadFragment(FragType.NewOrderScrActiFragment, true, nearbyShop)
                        }else{
                            (mContext as DashboardActivity).loadFragment(FragType.ViewAllOrderListFragment, true, nearbyShop)
                        }


                    }

                    override fun onLocationClick(shop: Any) {
                        val nearbyShop: AddShopDBModelEntity = shop as AddShopDBModelEntity
                        (mContext as DashboardActivity).openLocationMap(nearbyShop, false)
                    }

                    override fun visitShop(shop: Any) {
                        var list  = AppDatabase.getDBInstance()!!.shopActivityDao().getAll()
                        var shopType = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopType((shop as AddShopDBModelEntity).shop_id).toString()
                        if(list.size==0 && shopType.equals("16") && Pref.IsnewleadtypeforRuby){
                            Toaster.msgShort(mContext, "please wait,background data under snyc")
                        }else{
                            if (!Pref.isAddAttendence)
                                (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                            else {
                                val nearbyShop: AddShopDBModelEntity = shop as AddShopDBModelEntity
                                (mContext as DashboardActivity).callShopVisitConfirmationDialog(nearbyShop.shopName, nearbyShop.shop_id)
                            }
                        }
                    }

                    override fun onHistoryClick(shop: Any) {
                        (mContext as DashboardActivity).loadFragment(FragType.ShopFeedbackHisFrag, true, shop)
                    }

                    override fun onDamageClick(shop_id: String) {
                        (mContext as DashboardActivity).loadFragment(FragType.ShopDamageProductListFrag, true, shop_id+"~"+Pref.user_id)
                    }

                    override fun onSurveyClick(shop_id: String) {
                        if(Pref.isAddAttendence){
                            (mContext as DashboardActivity).loadFragment(FragType.SurveyViewFrag, true, shop_id)
                        }else{
                            (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                        }
                    }
                }, {
                    it
                })

                (mContext as DashboardActivity).nearbyShopList = list

                layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
                nearByShopsList.layoutManager = layoutManager
                nearByShopsList.adapter = localShopsListAdapter
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {

            Timber.d("=====empty selected list (Local Shop List)=======")

            noShopAvailable.visibility = View.VISIBLE
            nearByShopsList.visibility = View.GONE
        }

        progress_wheel.stopSpinning()

    }

    fun manualProceed(shop_id:String){
        progress_wheel.spin()
        AppUtils.endShopDuration(shop_id, mContext)

        var statusL = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(shop_id, AppUtils.getCurrentDateForShopActi())
        val duration = AppUtils.getTimeFromTimeSpan(statusL.get(0).startTimeStamp,statusL.get(0).endTimeStamp)
        progress_wheel.stopSpinning()

        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_ok)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
        dialogHeader.text = "Your spend duration for the outlet ${statusL.get(0).shop_name} is $duration"
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            getNearyShopList(AppUtils.mLocation!!)
        })
        simpleDialog.show()
    }


    private fun fetchNearbyShops() {

        /*if (!TextUtils.isEmpty(Pref.latitude) && !TextUtils.isEmpty(Pref.longitude)) {
            val location = Location("")
            location.longitude = Pref.latitude?.toDouble()!!
            location.latitude = Pref.longitude?.toDouble()!!
            getNearyShopList(location)
        }
        else {
            Timber.d("====================null location (Local Shop List)===================")

            progress_wheel.spin()
            SingleShotLocationProvider.requestSingleUpdate(mContext,
                    object : SingleShotLocationProvider.LocationCallback {
                        override fun onStatusChanged(status: String) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onProviderEnabled(status: String) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onProviderDisabled(status: String) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onNewLocationAvailable(location: Location) {
                            if (location.accuracy > 50) {
                                (mContext as DashboardActivity).showSnackMessage("Unable to fetch accurate GPS data. Please try again.")
                                progress_wheel.stopSpinning()
                            } else
                                getNearyShopList(location)
                        }

                    })
        }*/

        if (Pref.isOnLeave.equals("true", ignoreCase = true)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_you_are_in_leave))
            return
        }


        if (AppUtils.mLocation != null) {
            if (AppUtils.mLocation!!.accuracy <= Pref.gpsAccuracy.toInt())
                getNearyShopList(AppUtils.mLocation!!)
            else {
                Timber.d("=====Inaccurate current location (Local Shop List)=====")
                singleLocation()
            }
        } else {
            Timber.d("=====null location (Local Shop List)======")
            singleLocation()
        }
    }

    private fun singleLocation() {
        progress_wheel.spin()
        SingleShotLocationProvider.requestSingleUpdate(mContext,
                object : SingleShotLocationProvider.LocationCallback {
                    override fun onStatusChanged(status: String) {

                    }

                    override fun onProviderEnabled(status: String) {

                    }

                    override fun onProviderDisabled(status: String) {

                    }

                    override fun onNewLocationAvailable(location: Location) {
                        if (isGetLocation == -1) {
                            isGetLocation = 0
                            if (location.accuracy > Pref.gpsAccuracy.toInt()) {
                                (mContext as DashboardActivity).showSnackMessage("Unable to fetch accurate GPS data. Please try again.")
                                progress_wheel.stopSpinning()
                            } else
                                getNearyShopList(location)
                        }
                    }

                })

        val t = Timer()
        t.schedule(object : TimerTask() {
            override fun run() {
                try {
                    if (isGetLocation == -1) {
                        isGetLocation = 1
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage("GPS data to show nearby party is inaccurate. Please stop " +
                                "internet, stop GPS/Location service, and then restart internet and GPS services to get nearby party list.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, 15000)
    }


    fun getNearyShopList(location: Location) {

        list.clear()
        //val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
        val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getAllOwn(true)

        val newList = java.util.ArrayList<AddShopDBModelEntity>()
        progress_wheel.spin()

        for (i in allShopList.indices) {
            /*val userId = allShopList[i].shop_id.substring(0, allShopList[i].shop_id.indexOf("_"))
            if (userId == Pref.user_id)*/
                newList.add(allShopList[i])
        }



        if (newList != null && newList.size > 0) {
            Timber.d("Local Shop List: all shop list size======> " + newList.size)
            Timber.d("======Local Shop List======")
            for (i in 0 until newList.size) {
                println("\nnearby_count $i ${AppUtils.getCurrentDateTime()}" );
                val shopLat: Double = newList[i].shopLat
                val shopLong: Double = newList[i].shopLong

                if (shopLat != null && shopLong != null) {
                    val shopLocation = Location("")
                    shopLocation.latitude = shopLat
                    shopLocation.longitude = shopLong

                    /*Timber.d("shop_id====> " + allShopList[i].shop_id)
                    Timber.d("shopName====> " + allShopList[i].shopName)
                    Timber.d("shopLat====> $shopLat")
                    Timber.d("shopLong====> $shopLong")
                    Timber.d("lat=====> " + location.latitude)
                    Timber.d("long=====> " + location.longitude)
                    Timber.d("NEARBY_RADIUS====> $NEARBY_RADIUS")*/

                    var mRadious:Int = NEARBY_RADIUS
                    if(Pref.IsRestrictNearbyGeofence){
                        mRadious = Pref.GeofencingRelaxationinMeter
//                        mRadious=9999000
                    }
                    //val isShopNearby = FTStorageUtils.checkShopPositionWithinRadious(location, shopLocation, NEARBY_RADIUS)
                    val isShopNearby = FTStorageUtils.checkShopPositionWithinRadious(location, shopLocation, mRadious)
                    if (isShopNearby) {
                        //Timber.d("shop_id====> " + newList[i].shop_id+ " shopName====> " + newList[i].shopName)
                        //Timber.d("shopLat====> $shopLat"+" shopLong====> $shopLong")
                        //Timber.d("lat=====> " + location.latitude+" long=====> " + location.longitude)
                        //Timber.d("NEARBY_RADIUS====> $NEARBY_RADIUS")
                        //Timber.d("=====" + newList[i].shopName + " is nearby=====")
                        newList[i].visited = !shoulIBotherToUpdate(newList[i].shop_id)
                        list.add(newList[i])
                    } else {
                        // Timber.d("=============" + allShopList[i].shopName + " is NOT nearby===============")
                    }

                } else {
                    Timber.d("shop_id====> " + newList[i].shop_id+ " shopName===> " + newList[i].shopName)

                    if (shopLat != null)
                        Timber.d("shopLat===> $shopLat")
                    else
                        Timber.d("shopLat===> null")

                    if (shopLong != null)
                        Timber.d("shopLong====> $shopLong")
                    else
                        Timber.d("shopLong====> null")
                }
            }
            //Timber.d("=============================================")

        } else {
            Timber.d("====empty shop list (Local Shop List)======")
        }

        initAdapter()
        progress_wheel.stopSpinning()
    }

    fun shoulIBotherToUpdate(shopId: String): Boolean {
        return !AppDatabase.getDBInstance()!!.shopActivityDao().isShopActivityAvailable(shopId, AppUtils.getCurrentDateForShopActi())
    }
}