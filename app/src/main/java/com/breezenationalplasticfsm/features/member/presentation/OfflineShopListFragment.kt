package com.breezenationalplasticfsm.features.member.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.*
import com.breezenationalplasticfsm.app.domain.MemberShopEntity
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.FTStorageUtils
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.location.LocationWizard
import com.breezenationalplasticfsm.features.location.SingleShotLocationProvider
import com.breezenationalplasticfsm.features.member.api.TeamRepoProvider
import com.breezenationalplasticfsm.features.member.model.TeamShopListDataModel
import com.breezenationalplasticfsm.features.member.model.TeamShopListResponseModel
import com.breezenationalplasticfsm.widgets.AppCustomTextView

import com.github.clans.fab.FloatingActionMenu
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by Saikat on 06-Jul-20.
 */
// Revision Histroy
// 1.0 OfflineShopListFragment saheli 24-02-2032 AppV 4.0.7 mantis 0025683
class OfflineShopListFragment : BaseFragment() {

    private lateinit var mContext: Context

    private lateinit var rv_team_shop_list: RecyclerView
    private lateinit var tv_no_data_available: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var rl_member_list_shop_main: RelativeLayout
    private lateinit var floating_fab: FloatingActionMenu
    private lateinit var tv_team_struct: AppCustomTextView
    private lateinit var tv_shop_count: AppCustomTextView
    private lateinit var tv_shop_loading: AppCustomTextView

    private var userId = ""
    private var isGetLocation = -1
    private var isVisitShop = false

    private var shopList: ArrayList<MemberShopEntity>? = null
    private var totalShopList: ArrayList<MemberShopEntity>? = null
    private var adapter: OfflineShopAdapter? = null

    companion object {

        fun newInstance(userId: Any): OfflineShopListFragment {
            val fragment = OfflineShopListFragment()

            if (userId is String) {
                val bundle = Bundle()
                bundle.putString("user_id", userId)
                fragment.arguments = bundle
            }

            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        userId = arguments?.getString("user_id")?.toString()!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_team_shop_list, container, false)

        initView(view)

        if (Pref.isOfflineShopSaved)
            getDataFromDb()
        else {
            tv_shop_loading.visibility = View.VISIBLE
            progress_wheel.spin()
        }

        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    adapter?.refreshList(shopList!!)
                    tv_shop_count.text = "Total " + Pref.shopText + "(s): " + shopList?.size
                } else {
                    adapter?.filter?.filter(query)
                }
            }
        })

        // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 start
        (mContext as DashboardActivity).searchView.setVoiceIcon(R.drawable.ic_mic)
        (mContext as DashboardActivity).searchView.setOnVoiceClickedListener({ startVoiceInput() })
        // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 end

        return view
    }

    // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 start
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
        catch (ex:Exception) {
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
            catch (ex:Exception) {
                ex.printStackTrace()
            }

//            tv_search_frag_order_type_list.setText(t)
//            tv_search_frag_order_type_list.setSelection(t.length);
        }
    }
    // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 end

    private fun initView(view: View) {
        floating_fab = view.findViewById(R.id.floating_fab)
        rv_team_shop_list = view.findViewById(R.id.rv_team_shop_list)
        rv_team_shop_list.layoutManager = LinearLayoutManager(mContext)

        tv_no_data_available = view.findViewById(R.id.tv_no_data_available)
        tv_team_struct = view.findViewById(R.id.tv_team_struct)
        tv_shop_count = view.findViewById(R.id.tv_shop_count)
        tv_shop_loading = view.findViewById(R.id.tv_shop_loading)

        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()

        rl_member_list_shop_main = view.findViewById(R.id.rl_member_list_shop_main)
        rl_member_list_shop_main.setOnClickListener(null)

        tv_team_struct.apply {
            (mContext as DashboardActivity).teamHierarchy.takeIf { it.isNotEmpty() }?.let {
                visibility = View.VISIBLE

                it.forEachIndexed { i, name ->
                    text = if (i == 0)
                        name
                    else
                        text.toString().trim() + "-> " + name

                }

            } ?: let {
                visibility = View.GONE
            }
        }

        tv_shop_count.text = "Total " + Pref.shopText + "(s): 0"
        tv_no_data_available.text = "No " + Pref.shopText + " Available"
    }

    private fun getDataFromDb() {
        doAsync {

            val list: List<MemberShopEntity> = if (TextUtils.isEmpty((mContext as DashboardActivity).areaId))
                AppDatabase.getDBInstance()?.memberShopDao()?.getSingleUserShop(userId)!!
            else
                AppDatabase.getDBInstance()?.memberShopDao()?.getSingleUserShopAreaWise(userId, (mContext as DashboardActivity).areaId)!!

            uiThread {
                progress_wheel.stopSpinning()
                if (list != null && list.isNotEmpty()) {
                    totalShopList = list as ArrayList<MemberShopEntity>
                    fetchNearbyShops(list)
                } else
                    tv_no_data_available.visibility = View.VISIBLE

            }
        }
    }

    private fun fetchNearbyShops(shop_list: List<MemberShopEntity>) {
        if (AppUtils.mLocation != null) {
            if (AppUtils.mLocation!!.accuracy <= Pref.gpsAccuracy.toInt())
                getNearbyShopList(AppUtils.mLocation!!, shop_list)
            else {
                Timber.d("=====Inaccurate current location (Team Shop List)=====")
                singleLocation(shop_list)
            }
        } else {
            Timber.d("=====null location (Team Shop List)======")
            singleLocation(shop_list)
        }
    }

    private fun getNearbyShopList(location: Location, allShopList: List<MemberShopEntity>) {

        val newShopList = ArrayList<MemberShopEntity>()

        allShopList?.takeIf { it.isNotEmpty() }?.let {

            Timber.d("Local Shop List: all shop list size======> " + allShopList.size)
            Timber.d("======Local Shop List======")

            it/*.filter { teamShopListDataModel ->
                teamShopListDataModel.shop_lat.toDouble() != null && teamShopListDataModel.shop_long.toDouble() != null
            }*/.forEach { teamShop ->
                val shopLat = teamShop.shop_lat?.toDouble()
                val shopLong = teamShop.shop_long?.toDouble()

                if (shopLat != null && shopLong != null) {

                    val shopLocation = Location("")
                    shopLocation.let {
                        it.latitude = shopLat
                        it.longitude = shopLong
                        FTStorageUtils.checkShopPositionWithinRadious(location, it, LocationWizard.NEARBY_RADIUS)
                    }.takeIf { it }?.apply {
                        Timber.d("shop_id====> " + teamShop.shop_id)
                        Timber.d("shopName====> " + teamShop.shop_name)
                        Timber.d("shopLat====> $shopLat")
                        Timber.d("shopLong====> $shopLong")
                        Timber.d("lat=====> " + location.latitude)
                        Timber.d("long=====> " + location.longitude)
                        Timber.d("NEARBY_RADIUS====> ${LocationWizard.NEARBY_RADIUS}")
                        Timber.d("=====" + teamShop.shop_name + " is nearby=====")
                        newShopList.add(teamShop)
                    }
                } else {
                    Timber.d("shop_id====> " + teamShop.shop_id)
                    Timber.d("shopName===> " + teamShop.shop_name)

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
            Timber.d("=============================================")

        } ?: let {
            Timber.d("====empty shop list (Local Shop List)======")
        }

        shopList = newShopList

        shopList?.let {
            initAdapter(it)

            if (it.isEmpty())
                tv_no_data_available.visibility = View.VISIBLE
        }
    }

    private fun singleLocation(shop_list: List<MemberShopEntity>) {
        progress_wheel.spin()
        SingleShotLocationProvider.requestSingleUpdate(mContext, object : SingleShotLocationProvider.LocationCallback {
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
                    } else {
                        progress_wheel.stopSpinning()
                        getNearbyShopList(location, shop_list)
                    }
                }
            }
        })

        Timer().schedule(15000) {
            try {
                if (isGetLocation == -1) {
                    isGetLocation = 1
                    progress_wheel.stopSpinning()
                    (mContext as DashboardActivity).showSnackMessage("Unable to fetch accurate GPS data. Please try again.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initAdapter(shop_list: ArrayList<MemberShopEntity>) {
        tv_no_data_available.visibility = View.GONE

        tv_shop_count.text = "Total " + Pref.shopText + "(s): " + shop_list.size
        adapter = OfflineShopAdapter(mContext, shop_list, isVisitShop, { teamShop: MemberShopEntity ->
            if (!Pref.isAddAttendence)
                (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
            else {
                (mContext as DashboardActivity).callDialog(teamShop)
            }
        }, { teamShop: MemberShopEntity ->
            if (!Pref.isAddAttendence)
                (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
            else {
                val addShopData = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(teamShop.shop_id)
                if(Pref.IsActivateNewOrderScreenwithSize){//13-09-2021
                    (context as DashboardActivity).loadFragment(FragType.NewOrderScrOrderDetailsFragment, true, addShopData!!.shop_id)
                }else {
                    (mContext as DashboardActivity).loadFragment(FragType.ViewAllOrderListFragment, true, addShopData)
                }
            }
        }, { size: Int ->
            tv_shop_count.text = "Total " + Pref.shopText + "(s): " + size
        }, { teamShop: MemberShopEntity ->
            if (!Pref.isAddAttendence)
                (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
            else {
                val addShopData = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(teamShop.shop_id)
                (mContext as DashboardActivity).isBack = true
                (mContext as DashboardActivity).loadFragment(FragType.QuotationListFragment, true, addShopData.shop_id)
            }
        })

        rv_team_shop_list.adapter = adapter
    }

    fun updateAdapter() {
        //findSpecificTypeList(mShopType)

        shopList?.let {
            initAdapter(it)

            if (it.isEmpty())
                tv_no_data_available.visibility = View.VISIBLE
        }
    }

    fun updateUi() {
        tv_shop_loading.visibility = View.GONE
        progress_wheel.stopSpinning()
        getDataFromDb()
    }

    fun refreshList() {
        progress_wheel.spin()

        doAsync {
            val list = AppDatabase.getDBInstance()?.memberShopDao()?.getAll()

            uiThread {
                progress_wheel.stopSpinning()

                if (list != null && list.isNotEmpty())
                    callMemberShopListApi(list[list.size - 1].date_time!!)
                else
                    callMemberShopListApi("")
            }
        }
    }

    private fun callMemberShopListApi(date: String) {

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        progress_wheel.spin()

        val repository = TeamRepoProvider.teamRepoProvider()
        BaseActivity.compositeDisposable.add(
                repository.offlineTeamShopList(date)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as TeamShopListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.shop_list != null && response.shop_list!!.isNotEmpty()) {

                                    doAsync {

                                        response.shop_list?.forEach {
                                            val memberShop = MemberShopEntity()
                                            AppDatabase.getDBInstance()?.memberShopDao()?.insertAll(memberShop.apply {
                                                user_id = it.user_id
                                                shop_id = it.shop_id
                                                shop_name = it.shop_name
                                                shop_lat = it.shop_lat
                                                shop_long = it.shop_long
                                                shop_address = it.shop_address
                                                shop_pincode = it.shop_pincode
                                                shop_contact = it.shop_contact
                                                total_visited = it.total_visited
                                                last_visit_date = it.last_visit_date
                                                shop_type = it.shop_type
                                                dd_name = it.dd_name
                                                entity_code = it.entity_code
                                                model_id = it.model_id
                                                primary_app_id = it.primary_app_id
                                                secondary_app_id = it.secondary_app_id
                                                lead_id = it.lead_id
                                                funnel_stage_id = it.funnel_stage_id
                                                stage_id = it.stage_id
                                                booking_amount = it.booking_amount
                                                type_id = it.type_id
                                                area_id = it.area_id
                                                assign_to_pp_id = it.assign_to_pp_id
                                                assign_to_dd_id = it.assign_to_dd_id
                                                isUploaded = true
                                                date_time = AppUtils.getCurrentISODateTime()
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getDataFromDb()
                                        }
                                    }
                                }
                                else {
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                }

                            }
                            else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }
}