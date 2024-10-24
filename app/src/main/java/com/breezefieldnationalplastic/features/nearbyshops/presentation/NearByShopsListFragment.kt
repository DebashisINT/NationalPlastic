package com.breezefieldnationalplastic.features.nearbyshops.presentation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.CustomStatic
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.*
import com.breezefieldnationalplastic.app.domain.*
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.uiaction.IntentActionable
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.AppUtils.Companion.changeAttendanceDateFormatToCurrent
import com.breezefieldnationalplastic.app.utils.FTStorageUtils
import com.breezefieldnationalplastic.app.utils.NotificationUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.addshop.api.AddShopRepositoryProvider
import com.breezefieldnationalplastic.features.addshop.api.assignToPPList.AssignToPPListRepoProvider
import com.breezefieldnationalplastic.features.addshop.api.assignedToDDList.AssignToDDListRepoProvider
import com.breezefieldnationalplastic.features.addshop.api.typeList.TypeListRepoProvider
import com.breezefieldnationalplastic.features.addshop.model.AddShopRequestData
import com.breezefieldnationalplastic.features.addshop.model.AddShopResponse
import com.breezefieldnationalplastic.features.addshop.model.AssignedToShopListResponseModel
import com.breezefieldnationalplastic.features.addshop.model.QuestionSubmit
import com.breezefieldnationalplastic.features.addshop.model.UpdateAddrReq
import com.breezefieldnationalplastic.features.addshop.model.UpdateAddressShop
import com.breezefieldnationalplastic.features.addshop.model.assigntoddlist.AssignToDDListResponseModel
import com.breezefieldnationalplastic.features.addshop.model.assigntopplist.AssignToPPListResponseModel
import com.breezefieldnationalplastic.features.addshop.presentation.*
import com.breezefieldnationalplastic.features.contacts.CallHisDtls
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.location.LocationWizard
import com.breezefieldnationalplastic.features.location.SingleShotLocationProvider
import com.breezefieldnationalplastic.features.location.model.ShopDurationRequest
import com.breezefieldnationalplastic.features.location.model.ShopDurationRequestData
import com.breezefieldnationalplastic.features.location.shopdurationapi.ShopDurationRepositoryProvider
import com.breezefieldnationalplastic.features.login.presentation.LoginActivity
import com.breezefieldnationalplastic.features.marketing.api.marketingcategorylist.MarketingCatListRepoProvider
import com.breezefieldnationalplastic.features.marketing.model.MarketingCategoryListResponse
import com.breezefieldnationalplastic.features.nearbyshops.api.ShopListRepositoryProvider
import com.breezefieldnationalplastic.features.nearbyshops.api.updateaddress.ShopAddressUpdateRepoProvider
import com.breezefieldnationalplastic.features.nearbyshops.model.ShopData
import com.breezefieldnationalplastic.features.nearbyshops.model.ShopListResponse
import com.breezefieldnationalplastic.features.nearbyshops.model.StageListResponseModel
import com.breezefieldnationalplastic.features.nearbyshops.model.updateaddress.AddressUpdateRequest
import com.breezefieldnationalplastic.features.performance.model.Gps_status_list
import com.breezefieldnationalplastic.features.performance.model.UpdateGpsInputListParamsModel
import com.breezefieldnationalplastic.features.shopdetail.presentation.AddCollectionDialog
import com.breezefieldnationalplastic.features.shopdetail.presentation.api.EditShopRepoProvider
import com.breezefieldnationalplastic.features.shopdetail.presentation.api.addcollection.AddCollectionRepoProvider
import com.breezefieldnationalplastic.features.shopdetail.presentation.model.addcollection.AddCollectionInputParamsModel
import com.breezefieldnationalplastic.features.viewAllOrder.interf.QaOnCLick
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.itextpdf.text.BadElementException
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfWriter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Pratishruti on 30-10-2017.
 */
//Revision History
// 1.0 NearByShopsListFragment  AppV 4.0.6  Suman
// 2.0 NearByShopsListFragment AppV 4.0.6  Saheli    10/01/2023  Contact Multi Api called Add & Update
// 3.0 NearByShopsListFragment AppV 4.0.6 saheli 12-01-2023 multiple contact Data added on Api called
// 4.0 NearByShopsListFragment AppV 4.0.6 Suman 18-01-2023 show dob in extra contact
// 5.0 NearByShopsListFragment AppV 4.0.6 Suman 03-02-2023 updateModifiedShop + sendModifiedShopList  for shop update mantis 25624
// 6.0 NearByShopsListFragment AppV 4.0.7 saheli 20-02-2023 voice search mantis 0025683
// 7.0 NearByShopsListFragment AppV 4.0.7 saheli 21-02-2023 voice search mantis 0025683
// 8.0 NearByShopsListFragment AppV 4.0.7 saheli 08-06-2023 0026307 mantis  Play store console report issues
// 9.0 NearByShopsListFragment AppV 4.0.7 Suman 26-06-2023 0026307 mantis  26437
// 10.0 fix collection not sync issue sometimes puja 05-04-2024 mantis id 0027352 v4.2.6
// Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration

class NearByShopsListFragment : BaseFragment(), View.OnClickListener {

    private lateinit var mNearByShopsListAdapter: NearByShopsListAdapter
    private lateinit var nearByShopsList: RecyclerView
    private lateinit var iv_nearbyImage: ImageView
    private lateinit var mContext: Context
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var noShopAvailable: AppCompatTextView
    private lateinit var list: List<AddShopDBModelEntity>
    private lateinit var floating_fab: FloatingActionMenu
    private lateinit var programFab1: FloatingActionButton
    private lateinit var programFab2: FloatingActionButton
    private lateinit var programFab3: FloatingActionButton
    private lateinit var shop_list_parent_rl: RelativeLayout
    private lateinit var progress_wheel: com.pnikosis.materialishprogress.ProgressWheel
    private lateinit var tv_shop_count: AppCustomTextView
    private lateinit var tv_beat_name: AppCustomTextView

    private lateinit var getFloatingVal: ArrayList<String>
    private val preid: Int = 100
    private var isShopRegistrationInProcess = false
    private var dialog: AccuracyIssueDialog? = null
    private var isAddressUpdating = false
    private var i = 0
    private var collectionDialog: AddCollectionDialog?= null
    private var beatId = ""

    var rv_qaList: ArrayList<QuestionEntity> = ArrayList()
    var quesAnsList:ArrayList<AddShopFragment.QuestionAns> = ArrayList()
    var quesAnsListTemp:ArrayList<QuestionSubmit> = ArrayList()
    private var adapterqaList: AdapterQuestionList? = null
    private lateinit var simpleDialogRoot : Dialog

    private var isType99InTypeMaster:Boolean = false

//    /*Interface to update Shoplist Frag on search event*/
//    private lateinit var searchListener:SearchListener
//    public fun setSearchListener(searchListener:SearchListener){
//        this.searchListener=searchListener
//    }

    var shopExtraContactList:ArrayList<ShopExtraContactEntity> = ArrayList()
    private var shopDataModel = AddShopDBModelEntity()

    private lateinit var iv_frag_nearby_shops_mic: ImageView // 6.0 NearByShopsListFragment AppV 4.0.7 voice search mantis 0025683

//    private lateinit var queryText:String

    companion object {
        fun getInstance(beatId: Any?): NearByShopsListFragment {
            val fragment = NearByShopsListFragment()

            val bundle = Bundle()
            bundle.putString("beatId", beatId.toString())
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        println("fab_check onAttach")
        try {
            beatId = arguments?.getString("beatId").toString()
        }
        catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_nearby_shops, container, false)
        println("fab_check onCreateView")
        println("time_check onCreateView")

        // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration begin
        var type99 =  AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType("99")
        if(type99 == null){
            println("tag_type99 no type found")
            isType99InTypeMaster = false
        }else{
            println("tag_type99 type found")
            isType99InTypeMaster = true
        }
        // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration end

        initView(view)
//        if (AppDatabase.getDBInstance()!!.marketingCategoryMasterDao().getAll().isEmpty())
//            callMarketingCategoryListApi()
        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    //val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
                    var allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getAllOwn(true)

                    // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration begin
                    if(isType99InTypeMaster){
                         allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getAllOwn(true)
                    }else{
                         allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getAllOwnFilterType99(true,"99")
                    }
                    // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration end

                    getOwnShop(allShopList)

                    if (beatId.isNotEmpty())
                        list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(beatId)

                    tv_shop_count.text = "Total " + Pref.shopText + "(s): " + list.size

                    if (mNearByShopsListAdapter != null)
                        mNearByShopsListAdapter.updateAdapter(list)
                }
              /*  else if (queryText.isNotBlank()){
                    (mContext as DashboardActivity).searchView.openSearch()
                    (mContext as DashboardActivity).searchView.setQuery(queryText,false)
                    var searchedList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBySearchDataNew(query)
                    searchedList = searchedList.filter { it.isOwnshop==true }

                    getOwnShop(searchedList)

                    if (beatId.isNotEmpty())
                        list = AppDatabase.getDBInstance()!!.addShopEntryDao().getSearchedShopBeatWise(beatId, query)

                    tv_shop_count.text = "Total " + Pref.shopText + "(s): " + list.size

                    if (mNearByShopsListAdapter != null)
                        mNearByShopsListAdapter.updateAdapter(list)
                }*/
                else {
//                    val searchedList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBySearchData(query)

                    // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration begin
                    //var searchedList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBySearchDataNew(query)
                    //searchedList = searchedList.filter { it.isOwnshop==true }

                    var searchedList:ArrayList<AddShopDBModelEntity> = ArrayList()
                    if(isType99InTypeMaster){
                         searchedList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBySearchDataNewWithOwnShop(query,true) as ArrayList<AddShopDBModelEntity>
                    }else{
                        searchedList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBySearchDataNewWithOwnShopType(query,true,"99") as ArrayList<AddShopDBModelEntity>
                    }
                    // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration end

                    getOwnShop(searchedList)

                    if (beatId.isNotEmpty())
                        list = AppDatabase.getDBInstance()!!.addShopEntryDao().getSearchedShopBeatWise(beatId, query)

                    tv_shop_count.text = "Total " + Pref.shopText + "(s): " + list.size

                    if (mNearByShopsListAdapter != null)
                        mNearByShopsListAdapter.updateAdapter(list)
                }


//                Toast.makeText(mContext, query, Toast.LENGTH_SHORT).show()
            }

        })
        // 7.0 NearByShopsListFragment AppV 4.0.7  voice search mantis 0025683 start
        (mContext as DashboardActivity).searchView.setVoiceIcon(R.drawable.ic_mic)
        (mContext as DashboardActivity).searchView.setOnVoiceClickedListener({ startVoiceInput() })
        // 7.0 NearByShopsListFragment AppV 4.0.7  voice search mantis 0025683 start

        return view

    }

    private fun getOwnShop(allShopList: MutableList<AddShopDBModelEntity>) {
        progress_wheel.spin()
        val newList = java.util.ArrayList<AddShopDBModelEntity>()

        for (i in allShopList.indices) {
            /*val userId = allShopList[i].shop_id.substring(0, allShopList[i].shop_id.indexOf("_"))
            if (userId == Pref.user_id)*/
                newList.add(allShopList[i])
        }

        list = newList
        progress_wheel.stopSpinning()
    }

    private fun callMarketingCategoryListApi() {
        progress_wheel.spin()
        var repository = MarketingCatListRepoProvider.provideMarketingCatList()
        BaseActivity.compositeDisposable.add(
                repository.getMarketingCategoryList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var marketingPagerResult = result as MarketingCategoryListResponse
                            if (marketingPagerResult.status == NetworkConstant.SUCCESS) {
                                saveInDB(marketingPagerResult)
//                                (mContext as DashboardActivity).showSnackMessage("SUCCESS")
                            } else {
//                                (mContext as DashboardActivity).showSnackMessage("NOT SUCCESS")
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
//                            (mContext as DashboardActivity).showSnackMessage("ERROR")
                        })
        )
    }

    private fun saveInDB(marketingPagerResult: MarketingCategoryListResponse) {
        var retail_branding_list = marketingPagerResult.RetailBranding
        var pop_material = marketingPagerResult.POPMaterial
        for (i in 0 until retail_branding_list.size) {
            var marketingCatEntity = MarketingCategoryMasterEntity()
            marketingCatEntity.material_id = retail_branding_list[i].material_id.toString()
            marketingCatEntity.material_name = retail_branding_list[i].material_name
            marketingCatEntity.type_id = "1"
            AppDatabase.getDBInstance()!!.marketingCategoryMasterDao().insertAll(marketingCatEntity)
        }
        for (i in 0 until pop_material.size) {
            var marketingCatEntity = MarketingCategoryMasterEntity()
            marketingCatEntity.material_id = pop_material[i].material_id.toString()
            marketingCatEntity.material_name = pop_material[i].material_name
            marketingCatEntity.type_id = "2"
            AppDatabase.getDBInstance()!!.marketingCategoryMasterDao().insertAll(marketingCatEntity)
        }
    }

    private fun getShopListApi(isFromInitView: Boolean) {

        /*if (!isFromInitView)
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.wait_msg), 1000)*/

        val repository = ShopListRepositoryProvider.provideShopListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getShopList(Pref.session_token!!, Pref.user_id!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val shopList = result as ShopListResponse
                            if (shopList.status == NetworkConstant.SUCCESS) {
                                progress_wheel.stopSpinning()
                                if (shopList.data!!.shop_list == null || shopList.data!!.shop_list!!.isEmpty()) {
                                    noShopAvailable.visibility = View.VISIBLE
                                    nearByShopsList.visibility = View.GONE

                                    if (!isFromInitView)
                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.success_msg), 1000)

                                } else {
                                    convertToShopListSetAdapter(shopList.data!!.shop_list!!, isFromInitView)
                                }
//                                (mContext as DashboardActivity).showSnackMessage("SUCCESS")
                            } else if (isFromInitView && shopList.status == NetworkConstant.SESSION_MISMATCH) {
                                (mContext as DashboardActivity).showSnackMessage(shopList.message!!)
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).clearData()
                                startActivity(Intent(mContext as DashboardActivity, LoginActivity::class.java))
                                (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                (mContext as DashboardActivity).finish()
                            } else {
                                progress_wheel.stopSpinning()

                                if (isFromInitView)
                                    (mContext as DashboardActivity).showSnackMessage(shopList.message!!)
                                else
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_msg), 1000)
                            }
                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()

                            if (isFromInitView)
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            else
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_msg), 1000)
                        })
        )
    }

    override fun onResume() {
        super.onResume()
    }

    override fun updateUI(any: Any) {
        super.updateUI(any)
        getListFromDatabase()
    }

    private fun convertToShopListSetAdapter(shop_list: List<ShopData>, isFromInitView: Boolean) {
        if (!isFromInitView)
            AppDatabase.getDBInstance()!!.addShopEntryDao().deleteAll()

        val list: MutableList<AddShopDBModelEntity> = ArrayList()
        val shopObj = AddShopDBModelEntity()
        for (i in 0 until shop_list.size) {
            shopObj.shop_id = shop_list[i].shop_id
            shopObj.shopName = shop_list[i].shop_name
            shopObj.shopImageLocalPath = shop_list[i].Shop_Image
            try {
                shopObj.shopLat = shop_list[i].shop_lat!!.toDouble()
                shopObj.shopLong = shop_list[i].shop_long!!.toDouble()
            }catch (ex:Exception){
                ex.printStackTrace()
                shopObj.shopLat = 0.0
                shopObj.shopLong = 0.0
            }
            shopObj.duration = "0"
            shopObj.endTimeStamp = "0"
            shopObj.timeStamp = "0"
            shopObj.dateOfBirth = shop_list[i].dob
            shopObj.dateOfAniversary = shop_list[i].date_aniversary
            //shopObj.visited = true
            shopObj.visitDate = AppUtils.getCurrentDate()
            //shopObj.totalVisitCount = "1"
            if (shop_list[i].total_visit_count == "0")
                shopObj.totalVisitCount = "1"
            else
                shopObj.totalVisitCount = shop_list[i].total_visit_count
            shopObj.address = shop_list[i].address
            shopObj.ownerEmailId = shop_list[i].owner_email
            shopObj.ownerContactNumber = shop_list[i].owner_contact_no
            shopObj.pinCode = shop_list[i].pin_code
            shopObj.isUploaded = true

            if(shop_list[i].owner_name==null){
                shopObj.ownerName = shop_list[i].shop_name
            }else{
                shopObj.ownerName = shop_list[i].owner_name
            }
            shopObj.user_id = Pref.user_id
            shopObj.orderValue = 0
            shopObj.type = shop_list[i].type
            shopObj.assigned_to_dd_id = shop_list[i].assigned_to_dd_id
            shopObj.assigned_to_pp_id = shop_list[i].assigned_to_pp_id
            //shopObj.lastVisitedDate = AppUtils.getCurrentDate()

            if (shop_list[i].last_visit_date!!.contains(".")) {
                shopObj.lastVisitedDate =
                    AppUtils.changeAttendanceDateFormat(shop_list[i].last_visit_date!!.split(".")[0])
            }
            else {
                shopObj.lastVisitedDate = AppUtils.changeAttendanceDateFormat(shop_list[i].last_visit_date!!)
            }
            if (shopObj.lastVisitedDate == AppUtils.getCurrentDateChanged()) {
                shopObj.visited = true
                }
            else {
                shopObj.visited = false
                }

            shopObj.is_otp_verified = shop_list[i].is_otp_verified
            shopObj.added_date = shop_list[i].added_date

            if (shop_list[i].amount == null || shop_list[i].amount == "0.00") {
                shopObj.amount = ""
                }
            else {
                shopObj.amount = shop_list[i].amount
                }

            if (shop_list[i].entity_code == null) {
                shopObj.entity_code = ""
                }
            else {
                shopObj.entity_code = shop_list[i].entity_code
                }

            if (shop_list[i].area_id == null) {
                shopObj.area_id = ""
                }
            else {
                shopObj.area_id = shop_list[i].area_id
                }

            if (TextUtils.isEmpty(shop_list[i].model_id)) {
                shopObj.model_id = ""
                }
            else {
                shopObj.model_id = shop_list[i].model_id
                }

            if (TextUtils.isEmpty(shop_list[i].primary_app_id)) {
                shopObj.primary_app_id = ""
                }
            else {
                shopObj.primary_app_id = shop_list[i].primary_app_id
                }

            if (TextUtils.isEmpty(shop_list[i].secondary_app_id)) {
                shopObj.secondary_app_id = ""
                }
            else {
                shopObj.secondary_app_id = shop_list[i].secondary_app_id
                }

            if (TextUtils.isEmpty(shop_list[i].lead_id)) {
                shopObj.lead_id = ""
                }
            else {
                shopObj.lead_id = shop_list[i].lead_id
                }

            if (TextUtils.isEmpty(shop_list[i].stage_id)) {
                shopObj.stage_id = ""
                }
            else {
                shopObj.stage_id = shop_list[i].stage_id
                }

            if (TextUtils.isEmpty(shop_list[i].funnel_stage_id)) {
                shopObj.funnel_stage_id = ""
                }
            else {
                shopObj.funnel_stage_id = shop_list[i].funnel_stage_id
                }

            if (TextUtils.isEmpty(shop_list[i].booking_amount)) {
                shopObj.booking_amount = ""
                }
            else {
                shopObj.booking_amount = shop_list[i].booking_amount
                }

            if (TextUtils.isEmpty(shop_list[i].type_id)) {
                shopObj.type_id = ""
                }
            else {
                shopObj.type_id = shop_list[i].type_id
                }

            shopObj.family_member_dob = shop_list[i].family_member_dob
            shopObj.director_name = shop_list[i].director_name
            shopObj.person_name = shop_list[i].key_person_name
            shopObj.person_no = shop_list[i].phone_no
            shopObj.add_dob = shop_list[i].addtional_dob
            shopObj.add_doa = shop_list[i].addtional_doa

            shopObj.doc_degree = shop_list[i].degree
            shopObj.doc_family_dob = shop_list[i].doc_family_member_dob
            shopObj.specialization = shop_list[i].specialization
            shopObj.patient_count = shop_list[i].average_patient_per_day
            shopObj.category = shop_list[i].category
            shopObj.doc_address = shop_list[i].doc_address
            shopObj.doc_pincode = shop_list[i].doc_pincode
            shopObj.chamber_status = shop_list[i].is_chamber_same_headquarter.toInt()
            shopObj.remarks = shop_list[i].is_chamber_same_headquarter_remarks
            shopObj.chemist_name = shop_list[i].chemist_name
            shopObj.chemist_address = shop_list[i].chemist_address
            shopObj.chemist_pincode = shop_list[i].chemist_pincode
            shopObj.assistant_name = shop_list[i].assistant_name
            shopObj.assistant_no = shop_list[i].assistant_contact_no
            shopObj.assistant_dob = shop_list[i].assistant_dob
            shopObj.assistant_doa = shop_list[i].assistant_doa
            shopObj.assistant_family_dob = shop_list[i].assistant_family_dob

            if (TextUtils.isEmpty(shop_list[i].entity_id)) {
                shopObj.entity_id = ""
                }
            else {
                shopObj.entity_id = shop_list[i].entity_id
                }

            if (TextUtils.isEmpty(shop_list[i].party_status_id)) {
                shopObj.party_status_id = ""
                }
            else {
                shopObj.party_status_id = shop_list[i].party_status_id
                }

            if (TextUtils.isEmpty(shop_list[i].retailer_id)) {
                shopObj.retailer_id = ""
                }
            else {
                shopObj.retailer_id = shop_list[i].retailer_id
                }

            if (TextUtils.isEmpty(shop_list[i].dealer_id)) {
                shopObj.dealer_id = ""
                }
            else {
                shopObj.dealer_id = shop_list[i].dealer_id
            }
            if (TextUtils.isEmpty(shop_list[i].beat_id)) {
                shopObj.beat_id = ""
                }
            else {
                shopObj.beat_id = shop_list[i].beat_id
                }

            if (TextUtils.isEmpty(shop_list[i].account_holder)) {
                shopObj.account_holder = ""
                }
            else {
                shopObj.account_holder = shop_list[i].account_holder
                }

            if (TextUtils.isEmpty(shop_list[i].account_no)) {
                shopObj.account_no = ""
                }
            else {
                shopObj.account_no = shop_list[i].account_no
                }

            if (TextUtils.isEmpty(shop_list[i].bank_name)) {
                shopObj.bank_name = ""
                }
            else {
                shopObj.bank_name = shop_list[i].bank_name
            }

            if (TextUtils.isEmpty(shop_list[i].ifsc_code)) {
                shopObj.ifsc_code = ""
                }
            else {
                shopObj.ifsc_code = shop_list[i].ifsc_code
            }

            if (TextUtils.isEmpty(shop_list[i].upi)) {
                shopObj.upi_id = ""
            }
            else {
                shopObj.upi_id = shop_list[i].upi
            }
            if (TextUtils.isEmpty(shop_list[i].assigned_to_shop_id)) {
                shopObj.assigned_to_shop_id = ""
                }
            else {
                shopObj.assigned_to_shop_id = shop_list[i].assigned_to_shop_id
                }

            shopObj.project_name=shop_list[i].project_name
            shopObj.landline_number=shop_list[i].landline_number
            shopObj.agency_name=shop_list[i].agency_name
            /*10-2-2022*/
            shopObj.alternateNoForCustomer=shop_list[i].alternateNoForCustomer
            shopObj.whatsappNoForCustomer=shop_list[i].whatsappNoForCustomer
            shopObj.isShopDuplicate=shop_list[i].isShopDuplicate

            shopObj.purpose=shop_list[i].purpose

            //start AppV 4.2.2 tufan    20/09/2023 FSSAI Lic No Implementation 26813
            try {
                shopObj.FSSAILicNo = shop_list[i].FSSAILicNo
            }catch (ex:Exception){
                ex.printStackTrace()
                shopObj.FSSAILicNo = ""
            }
//end AppV 4.2.2 tufan    20/09/2023 FSSAI Lic No Implementation 26813


            /*GSTIN & PAN NUMBER*/
            shopObj.gstN_Number=shop_list[i].GSTN_Number
            shopObj.shopOwner_PAN=shop_list[i].ShopOwner_PAN

            //crm details
            shopObj.companyName_id = if(shop_list[i].crm_companyID.isNullOrEmpty()) "" else shop_list[i].crm_companyID
            shopObj.companyName = if(shop_list[i].crm_companyName.isNullOrEmpty()) "" else shop_list[i].crm_companyName
            shopObj.jobTitle = if(shop_list[i].crm_jobTitle.isNullOrEmpty()) "" else shop_list[i].crm_jobTitle
            shopObj.crm_type_ID = if(shop_list[i].crm_typeID.isNullOrEmpty()) "" else shop_list[i].crm_typeID
            shopObj.crm_type = if(shop_list[i].crm_type.isNullOrEmpty()) "" else shop_list[i].crm_type
            shopObj.crm_status_ID = if(shop_list[i].crm_statusID.isNullOrEmpty()) "" else shop_list[i].crm_statusID
            shopObj.crm_status = if(shop_list[i].crm_status.isNullOrEmpty()) "" else shop_list[i].crm_status
            shopObj.crm_source_ID = if(shop_list[i].crm_sourceID.isNullOrEmpty()) "" else shop_list[i].crm_sourceID
            shopObj.crm_source = if(shop_list[i].crm_source.isNullOrEmpty()) "" else shop_list[i].crm_source
            shopObj.crm_reference = if(shop_list[i].crm_reference.isNullOrEmpty()) "" else shop_list[i].crm_reference
            shopObj.crm_reference_ID = if(shop_list[i].crm_referenceID.isNullOrEmpty()) "" else shop_list[i].crm_referenceID
            shopObj.crm_reference_ID_type = if(shop_list[i].crm_referenceID_type.isNullOrEmpty()) "" else shop_list[i].crm_referenceID_type
            shopObj.crm_stage_ID = if(shop_list[i].crm_stage_ID.isNullOrEmpty()) "" else shop_list[i].crm_stage_ID
            shopObj.crm_stage = if(shop_list[i].crm_stage.isNullOrEmpty()) "" else shop_list[i].crm_stage
            shopObj.crm_assignTo = if(shop_list[i].assign_to.isNullOrEmpty()) "" else shop_list[i].assign_to
            shopObj.crm_saved_from = if(shop_list[i].saved_from_status.isNullOrEmpty()) "" else shop_list[i].saved_from_status
            shopObj.crm_firstName = if(shop_list[i].shop_firstName.isNullOrEmpty()) "" else shop_list[i].shop_firstName
            shopObj.crm_lastName = if(shop_list[i].shop_lastName.isNullOrEmpty()) "" else shop_list[i].shop_lastName

            list.add(shopObj)
            AppDatabase.getDBInstance()!!.addShopEntryDao().insert(shopObj)
        }

        if (!isFromInitView)
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.success_msg), 1000)

        initAdapter()
    }

    private fun initView(view: View) {
        iv_frag_nearby_shops_mic = view.findViewById(R.id.iv_frag_nearby_shops_mic) // 6.0 NearByShopsListFragment AppV 4.0.7 voice search mantis 0025683
        val simpleDialogRoot = Dialog(mContext)
        simpleDialogRoot.setCancelable(true)
        simpleDialogRoot.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialogRoot.setContentView(R.layout.dialog_multiple_contact_root)

        getFloatingVal = ArrayList<String>()
        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()
        nearByShopsList = view.findViewById(R.id.near_by_shops_RCV)
        noShopAvailable = view.findViewById(R.id.no_shop_tv)
        shop_list_parent_rl = view.findViewById(R.id.shop_list_parent_rl)
        iv_nearbyImage = view.findViewById(R.id.iv_nearbyImage)
        tv_shop_count = view.findViewById(R.id.tv_shop_count)
        tv_shop_count.visibility = View.VISIBLE
        tv_beat_name = view.findViewById(R.id.tv_beat_name)

        if (beatId.isNotEmpty()) {
            tv_beat_name.visibility = View.VISIBLE
            val beat = AppDatabase.getDBInstance()?.beatDao()?.getSingleItem(beatId)
            tv_beat_name.text = "Beat Name: " + beat?.name
        }

        shop_list_parent_rl.setOnClickListener { view ->
            //if (!(mContext as DashboardActivity).isShopFromChatBot)
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
            } else if (i == 1) {
                programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
                }
            else {
                programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)
                }

        }

        noShopAvailable.text = "No Registered " + Pref.shopText + " Available"
        getListFromDatabase()

        // 6.0 NearByShopsListFragment AppV 4.0.7 voice search mantis 0025683 start
      /*  iv_frag_nearby_shops_mic.setOnClickListener {
//            (mContext as DashboardActivity).searchView.setOnVoiceClickedListener(this)
            (mContext as DashboardActivity).searchView.onVoiceClicked()
//            startVoiceInput()
        }*/
//        iv_frag_nearby_shops_mic.setOnClickListener {
//            (mContext as DashboardActivity).searchView.setOnVoiceClickedListener(OnVoiceClickedListener { startVoiceInput() })
//
//        }

    }
    private fun startVoiceInput() {
        try {
            val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.ENGLISH)
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
    // 6.0 NearByShopsListFragment AppV 4.0.7 voice search mantis 0025683 end
    private fun getListFromDatabase() {
        val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
        getOwnShop(allShopList)
        if (list.isEmpty()) {
            if (AppUtils.isOnline(mContext))
                getShopListApi(true)
            else
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
        } else {
            for (i in list.indices) {
                val listnew = AppDatabase.getDBInstance()!!.addShopEntryDao().getVisitedShopListByName(list[i].shopName, true)
                list[i].totalVisitCount = listnew.size.toString()
                if (listnew.size > 0)
                    list[i].lastVisitedDate = listnew[listnew.size - 1].visitDate
            }
            initAdapter()
            syncShopList()
        }
    }

    private fun setListVisiBility(): Boolean {
//        return true
        return if (list.isNotEmpty()) {
            noShopAvailable.visibility = View.GONE
            nearByShopsList.visibility = View.VISIBLE
//            initAdapter()
            true
        } else {
            noShopAvailable.visibility = View.VISIBLE
            nearByShopsList.visibility = View.GONE
            false
        }

    }

    @SuppressLint("WrongConstant")
    private fun initAdapter() {
        /*noShopAvailable.visibility = View.GONE
        nearByShopsList.visibility = View.VISIBLE*/

        //val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
        println("time_check initAdapterstart")
        var allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getAllOwn(true)

        // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration begin
        if(isType99InTypeMaster){
             allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getAllOwn(true)
        }else{
             allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getAllOwnFilterType99(true,"99")
        }
        // Revision 11.0 Suman 11-04-2024 mantis id 27362 v4.2.6 shop type 99 consideration end

        getOwnShop(allShopList)
//        ( list as ArrayList).set()

        /*if (Pref.isReplaceShopText)
            tv_shop_count.text = "Total Customer(s): " + list.size
        else
            tv_shop_count.text = "Total Shop(s): " + list.size*/

        if (beatId.isNotEmpty())
            list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(beatId)

        setListVisiBility()
        tv_shop_count.text = "Total " + Pref.shopText + "(s): " + list.size


        //Begin 9.0 NearByShopsListFragment AppV 4.0.7 Suman 26-06-2023 0026307 mantis  26437
        try{
            //sortAlphabatically()
            floating_fab.close(true)
            programFab1.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
            programFab2.colorNormal = mContext.resources.getColor(R.color.colorAccent)
            programFab3.colorNormal = mContext.resources.getColor(R.color.colorAccent)
            programFab1.setImageResource(R.drawable.ic_tick_float_icon)
            programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
            programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)
            println("fab_check ok")
        }catch (ex:Exception){
            ex.printStackTrace()
            println("fab_check err ${ex.message}")
        }
        //ENd of 9.0 NearByShopsListFragment AppV 4.0.7 Suman 26-06-2023 0026307 mantis  26437

        mNearByShopsListAdapter = NearByShopsListAdapter(this.mContext!!, list, object : NearByShopsListClickListener {

            override fun onUpdateStatusClick(obj: AddShopDBModelEntity) {
                UpdateShopStatusDialog.getInstance(obj.shopName!!, "Cancel", "Confirm", true,"",obj.user_id.toString()!!,"Select Shop Status",
                    object : UpdateShopStatusDialog.OnDSButtonClickListener {
                        override fun onLeftClick() {

                        }
                        override fun onRightClick(status: String) {
                            if(!status.equals("")){
                                if(status.equals("Inactive")){
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateShopStatus(obj.shop_id,"0")
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsEditUploaded(0,obj.shop_id)
                                    if(AppUtils.isOnline(mContext)) {
                                        convertToReqAndApiCallForShopStatus(
                                            AppDatabase.getDBInstance()!!.addShopEntryDao()
                                                .getShopByIdN(obj.shop_id!!)
                                        )
                                    }
                                    else{
                                        (mContext as DashboardActivity).showSnackMessage("Status updated successfully")
                                        initAdapter()
                                    }
                                }
                                if(status.equals("Active")){
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateShopStatus(obj.shop_id,"1")
                                }
                            }

                        }
                    }).show((mContext as DashboardActivity).supportFragmentManager, "")
            }

            override fun createITCOrderClick(obj: AddShopDBModelEntity) {
                (mContext as DashboardActivity).loadFragment(FragType.OrderListFrag, true, obj.shop_id)
            }

            override fun onExtraContactClick(shop_idSel: String) {
                shopExtraContactList = ArrayList()
                 simpleDialogRoot = Dialog(mContext)
                simpleDialogRoot.setCancelable(true)
                simpleDialogRoot.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogRoot.setContentView(R.layout.dialog_multiple_contact_root)

                val iv_cross = simpleDialogRoot.findViewById(R.id.iv_dialog_multi_cont_root_cross) as ImageView

                val cont1 = simpleDialogRoot.findViewById(R.id.tv_dialog_frag_add_shop_add_contact1) as TextView
                val cont2 = simpleDialogRoot.findViewById(R.id.tv_dialog_frag_add_shop_add_contact2) as TextView
                val cont3 = simpleDialogRoot.findViewById(R.id.tv_dialog_frag_add_shop_add_contact3) as TextView
                val cont4 = simpleDialogRoot.findViewById(R.id.tv_dialog_frag_add_shop_add_contact4) as TextView
                val cont5 = simpleDialogRoot.findViewById(R.id.tv_dialog_frag_add_shop_add_contact5) as TextView
                val cont6 = simpleDialogRoot.findViewById(R.id.tv_dialog_frag_add_shop_add_contact6) as TextView

                var extraContL = AppDatabase.getDBInstance()?.shopExtraContactDao()?.getExtraContListByShopID(shop_idSel!!) as ArrayList<ShopExtraContactEntity>
                if(extraContL!!.size>0){

                    if(extraContL.size == 1){
                        cont1.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont2.visibility = View.VISIBLE
                        cont3.visibility = View.GONE
                        cont4.visibility = View.GONE
                        cont5.visibility = View.GONE
                        cont6.visibility = View.GONE
                    }else if(extraContL.size == 2){
                        cont1.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont2.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont2.visibility = View.VISIBLE
                        cont3.visibility = View.VISIBLE
                        cont4.visibility = View.GONE
                        cont5.visibility = View.GONE
                        cont6.visibility = View.GONE
                    }else if(extraContL.size == 3){
                        cont1.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont2.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont3.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont2.visibility = View.VISIBLE
                        cont3.visibility = View.VISIBLE
                        cont4.visibility = View.VISIBLE
                        cont5.visibility = View.GONE
                        cont6.visibility = View.GONE
                    }else if(extraContL.size == 4){
                        cont1.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont2.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont3.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont4.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont2.visibility = View.VISIBLE
                        cont3.visibility = View.VISIBLE
                        cont4.visibility = View.VISIBLE
                        cont5.visibility = View.VISIBLE
                        cont6.visibility = View.GONE
                    }else if(extraContL.size == 5){
                        cont1.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont2.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont3.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont4.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont5.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont2.visibility = View.VISIBLE
                        cont3.visibility = View.VISIBLE
                        cont4.visibility = View.VISIBLE
                        cont5.visibility = View.VISIBLE
                        cont6.visibility = View.VISIBLE
                    }else if(extraContL.size == 6){
                        cont1.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont2.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont3.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont4.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont5.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                        cont6.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.approved_green))
                    }

                    simpleDialogRoot.show()

                }else{
                    cont1.visibility = View.VISIBLE
                    cont2.visibility = View.GONE
                    cont3.visibility = View.GONE
                    cont4.visibility = View.GONE
                    cont5.visibility = View.GONE
                    cont6.visibility = View.GONE
                    simpleDialogRoot.show()
                }

                iv_cross.setOnClickListener {
                    simpleDialogRoot.dismiss()
                }

                  cont1.setOnClickListener {
                    var ob = extraContL!!.filter { it.contact_serial.equals("1")} as ArrayList<ShopExtraContactEntity>
                    if(ob.size == 0){
                        addExtraContactDtls("1",shop_idSel)
                        // 2.0 NearByShopsListFragment AppV 4.0.6   Contact Multi Api called Add & Update
//                        Handler().postDelayed(Runnable {
//                            callAddMultiContactapi()
//                        }, 500)
                    }else{
                        if(extraContL.get(0).contact_serial.equals("1")){
                            showExtraContactDtls(extraContL.get(0).contact_name.toString(),extraContL.get(0).contact_number.toString(),
                                extraContL.get(0).contact_email.toString(),extraContL.get(0).contact_doa.toString(),extraContL.get(0).contact_dob.toString())

                        }
                    }
                }
                cont2.setOnClickListener {
                    var ob = extraContL!!.filter { it.contact_serial.equals("2")} as ArrayList<ShopExtraContactEntity>
                    if(ob.size == 0){
                        addExtraContactDtls("2",shop_idSel)
                        // 2.0 NearByShopsListFragment AppV 4.0.6   Contact Multi Api called Add & Update
//                        Handler().postDelayed(Runnable {
//                            callUpdateMultiContactapi()
//                        }, 500)
                    }else{
                        if(extraContL.get(1).contact_serial.equals("2")){
                            showExtraContactDtls(extraContL.get(1).contact_name.toString(),extraContL.get(1).contact_number.toString(),
                                extraContL.get(1).contact_email.toString(),extraContL.get(1).contact_doa.toString(),extraContL.get(1).contact_dob.toString())

                        }}
                }
                cont3.setOnClickListener {
                    var ob = extraContL!!.filter { it.contact_serial.equals("3")} as ArrayList<ShopExtraContactEntity>
                    if(ob.size == 0){
                        addExtraContactDtls("3",shop_idSel)
                    }else{
                        if(extraContL.get(2).contact_serial.equals("3")){
                            showExtraContactDtls(extraContL.get(2).contact_name.toString(),extraContL.get(2).contact_number.toString(),
                                extraContL.get(2).contact_email.toString(),extraContL.get(2).contact_doa.toString(),extraContL.get(2).contact_dob.toString())
                        }
                    }
                }
                cont4.setOnClickListener {
                    var ob = extraContL!!.filter { it.contact_serial.equals("4")} as ArrayList<ShopExtraContactEntity>
                    if(ob.size == 0){
                        addExtraContactDtls("4",shop_idSel)
                    }else{
                        if(extraContL.get(3).contact_serial.equals("4")){
                            showExtraContactDtls(extraContL.get(3).contact_name.toString(),extraContL.get(3).contact_number.toString(),
                                extraContL.get(3).contact_email.toString(),extraContL.get(3).contact_doa.toString(),extraContL.get(3).contact_dob.toString())
                        }
                    }
                }
                cont5.setOnClickListener {
                    var ob = extraContL!!.filter { it.contact_serial.equals("5")} as ArrayList<ShopExtraContactEntity>
                    if(ob.size == 0){
                        addExtraContactDtls("5",shop_idSel)
                    }else{
                        if(extraContL.get(4).contact_serial.equals("5")){
                            showExtraContactDtls(extraContL.get(4).contact_name.toString(),extraContL.get(4).contact_number.toString(),
                                extraContL.get(4).contact_email.toString(),extraContL.get(4).contact_doa.toString(),extraContL.get(4).contact_dob.toString())
                        }
                    }
                }
                cont6.setOnClickListener {
                    var ob = extraContL!!.filter { it.contact_serial.equals("6")} as ArrayList<ShopExtraContactEntity>
                    if(ob.size == 0){
                        addExtraContactDtls("6",shop_idSel)
                    }else{
                        if(extraContL.get(5).contact_serial.equals("6")){
                            showExtraContactDtls(extraContL.get(5).contact_name.toString(),extraContL.get(5).contact_number.toString(),
                                extraContL.get(5).contact_email.toString(),extraContL.get(5).contact_doa.toString(),extraContL.get(5).contact_dob.toString())
                        }
                    }
                }
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

            override fun onMultipleImageClick(shop: Any,position: Int) {
                if (AppUtils.isOnline(mContext)) {
                    var shopIsuploaded =AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(list[position].shop_id).isUploaded
                    if(shopIsuploaded){
                        if(Pref.IsMultipleImagesRequired){
                            (mContext as DashboardActivity).loadFragment(FragType.MultipleImageFragment, true, shop)
                        }
                    }
                    else{
                        (this as DashboardActivity).showSnackMessage("Please snyc shop First..")
                    }

                }
                else{
                    (this as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                }


            }

            override fun onUpdateStageClick(position: Int) {
                if (list[position].isUploaded) {
                    val stageList = AppDatabase.getDBInstance()?.stageDao()?.getAll() as ArrayList<StageEntity>

                    if (stageList == null || stageList.isEmpty()) {
                        geStageApi(position)
                        }
                    else {
                        showStageDialog(stageList, position)
                    }
                } else {
                    /*if (Pref.isReplaceShopText)
                        (mContext as DashboardActivity).showSnackMessage("Please sync this customer first.")
                    else
                        (mContext as DashboardActivity).showSnackMessage("Please sync this shop first.")*/

                    (mContext as DashboardActivity).showSnackMessage("Please sync this " + Pref.shopText + " first.")
                }
            }

            override fun onQuotationClick(position: Int) {
                floating_fab.close(true)
                (mContext as DashboardActivity).isBack = true
                (mContext as DashboardActivity).loadFragment(FragType.QuotationListFragment, true, list[position].shop_id)
            }

            override fun onActivityClick(position: Int) {
                floating_fab.close(true)

                when (list[position].type) {
                    "7" -> {
                        (mContext as DashboardActivity).isFromShop = true
                        (mContext as DashboardActivity).loadFragment(FragType.ChemistActivityListFragment, true, list[position])
                    }
                    "8" -> {
                        (mContext as DashboardActivity).isFromShop = true
                        (mContext as DashboardActivity).loadFragment(FragType.DoctorActivityListFragment, true, list[position])
                    }
                    else -> {
                        (mContext as DashboardActivity).isFromMenu = false
                        (mContext as DashboardActivity).loadFragment(FragType.AddActivityFragment, true, list[position])
                    }
                }
            }

            override fun onLocationShareClick(position: Int) {

              //  val uri = ("geo:"+list[position].shopLat  + "," + list[position].shopLat) + "?q=" + Pref.latitude + "," + Pref.longitude
              /*  val uri = ("geo:"+Pref.latitude  + "," + Pref.longitude) + "?q=" + list[position].shopLat + "," + list[position].shopLong
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(uri)
                    )
                )*/

                val latitude: Double = list[position].shopLat
                val longitude: Double = list[position].shopLong

                val uri = "https://www.google.com/maps/?q=" + latitude + "," + longitude
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.let {
                    it.type = "text/plain"
                    it.putExtra(Intent.EXTRA_TEXT, uri)
                    startActivity(Intent.createChooser(it, "Share via"))
                }
            }

            override fun onShareClick(position: Int) {
                val heading = "${Pref.shopText.toUpperCase()} DETAILS"
                var pdfBody = "\n\n\n\n${Pref.shopText} Name: " + list[position].shopName + "\n\nAddress: " + list[position].address +
                        "\n\nOwner Name: " + list[position].ownerName + "\n\nContact No.: " + list[position].ownerContactNumber

                if (!TextUtils.isEmpty(list[position].ownerEmailId))
                    pdfBody += "\n\nEmail ID: " + list[position].ownerEmailId

                val shopType = AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType(list[position].type)
                shopType?.let {
                    pdfBody += "\n\n${Pref.shopText} Type: " + it.shoptype_name
                }

                pdfBody += "\n\nLat: " + list[position].shopLat + ",  Long: " + list[position].shopLong + "\n\nCreated User: " +
                        Pref.user_name

                if (!TextUtils.isEmpty(list[position].added_date))
                    pdfBody += "\n\n${Pref.shopText} Created Date: " + AppUtils.convertDateTimeToCommonFormat(list[position].added_date)


                /*if (!TextUtils.isEmpty(list[position].shopImageLocalPath)) {
                    var image: Bitmap? = null//BitmapFactory.decodeResource(mContext.resources, R.mipmap.ic_launcher)
                    Glide.with(mContext)
                        .asBitmap()
                        .load(list[position].shopImageLocalPath)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                image = resource

                                val path = FTStorageUtils.stringToPdf(pdfBody, mContext, "FTS_" + list[position].shop_id + ".pdf",
                                    image, heading, 2.7f)

                                if (!TextUtils.isEmpty(path)) {
                                    try {
                                        val shareIntent = Intent(Intent.ACTION_SEND)
                                        val fileUrl = Uri.parse(path)

                                        val file = File(fileUrl.path)
//                                            val uri = Uri.fromFile(file)
                                        val uri:Uri= FileProvider.getUriForFile(mContext, context!!.applicationContext.packageName.toString() + ".provider", file)
                                        shareIntent.type = "image/png"
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                                        startActivity(Intent.createChooser(shareIntent, "Share pdf using"));
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    (mContext as DashboardActivity).showSnackMessage("Pdf can not be sent.")
                                }
                            }
                        })
                }
                else {
                    val path = FTStorageUtils.stringToPdf(pdfBody, mContext, "FTS_" + list[position].shop_id + ".pdf",
                        null, heading, 2.7f)

                    if (!TextUtils.isEmpty(path)) {
                        try {
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            val fileUrl = Uri.parse(path)

                            val file = File(fileUrl.path)
//                            val uri = Uri.fromFile(file)
                            val uri:Uri= FileProvider.getUriForFile(mContext, context!!.applicationContext.packageName.toString() + ".provider", file)
                            shareIntent.type = "image/png"
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                            startActivity(Intent.createChooser(shareIntent, "Share pdf using"));
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        (mContext as DashboardActivity).showSnackMessage("Pdf can not be sent.")
                    }
                }*/

                //create pdf
                val shop = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(list[position].shop_id)
                var document: Document = Document()
                var fileName = "FTS"+ "_" + shop!!.shop_id
                fileName = fileName.replace("/", "_")
                val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/breezefieldnationalplasticApp/SHOPDtls/"
                var pathNew = ""
                val dir = File(path)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                try {
                    try {
                        PdfWriter.getInstance(document, FileOutputStream(path + fileName + ".pdf"))
                    }catch (ex:Exception){
                        ex.printStackTrace()
                        pathNew = mContext.filesDir.toString() + "/" + fileName+".pdf"
                        PdfWriter.getInstance(document, FileOutputStream(pathNew))
                    }
                    document.open()
                    var font: Font = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)
                    var fontBoldU: Font = Font(Font.FontFamily.HELVETICA, 12f, Font.UNDERLINE or Font.BOLD)

                    val bm: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.breezelogo)
                    //code end by Puja mantis-0027395 date-23.04.24 v4.2.6
                    val bitmap = Bitmap.createScaledBitmap(bm, 80, 80, true);
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    var img: Image? = null
                    val byteArray: ByteArray = stream.toByteArray()
                    try {
                        img = Image.getInstance(byteArray)
                        img.scaleToFit(110f, 110f)
                        img.scalePercent(70f)
                        img.alignment = Image.ALIGN_LEFT
                    } catch (e: BadElementException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val h = Paragraph("${Pref.shopText} Details", fontBoldU)
                    h.alignment = Element.ALIGN_CENTER
                    val pHead = Paragraph()
                    pHead.add(Chunk(img, 0f, -30f))
                    pHead.add(h)
                    document.add(pHead)

                    val xz = Paragraph("", font)
                    xz.spacingAfter = 20f
                    document.add(xz)

                    val parties = Paragraph("${Pref.shopText} Name     :     " + shop?.shopName, font)
                    parties.alignment = Element.ALIGN_LEFT
                    parties.spacingAfter = 2f
                    document.add(parties)

                    val addr = Paragraph("Address     :     " + shop?.address, font)
                    addr.alignment = Element.ALIGN_LEFT
                    addr.spacingAfter = 2f
                    document.add(addr)

                    val owner = Paragraph("Owner Name     :     " + shop?.ownerName, font)
                    owner.alignment = Element.ALIGN_LEFT
                    owner.spacingAfter = 2f
                    document.add(owner)

                    val ownerPh = Paragraph("Owner Contact     :     " + shop?.ownerContactNumber, font)
                    ownerPh.alignment = Element.ALIGN_LEFT
                    ownerPh.spacingAfter = 2f
                    document.add(ownerPh)

                    var typeN = AppDatabase.getDBInstance()!!.shopTypeDao().getShopTypeNameById(shop.shop_id)
                    val shopType = Paragraph("${Pref.shopText} Type     :     " + typeN, font)
                    shopType.alignment = Element.ALIGN_LEFT
                    shopType.spacingAfter = 2f
                    document.add(shopType)

                    document.close()

                    var sendingPath = path + fileName + ".pdf"
                    if(!pathNew.equals("")){
                        sendingPath = pathNew
                    }
                    if (!TextUtils.isEmpty(sendingPath)) {
                        try {
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            val fileUrl = Uri.parse(sendingPath)
                            val file = File(fileUrl.path)
                            val uri: Uri = FileProvider.getUriForFile(mContext, mContext.applicationContext.packageName.toString() + ".provider", file)
                            shareIntent.type = "image/png"
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                            startActivity(Intent.createChooser(shareIntent, "Share pdf using"))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong1))
                        }
                    }

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }


            override fun onCollectionClick(position: Int) {
                floating_fab.close(true)
                try {

                    if (!Pref.isAddAttendence) {
                        (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                    }
                    else {
                        if(Pref.IsCollectionOrderWise){
                            (mContext as DashboardActivity).loadFragment(FragType.NewOrderListFragment, true, "")
                        }else{
                        collectionDialog = AddCollectionDialog.getInstance(list[position], true, list[position].shopName!!, "", "", "", object : AddCollectionDialog.AddCollectionClickLisneter {
                            override fun onClick(collection: String, date: String, paymentId: String, instrument: String, bank: String, filePath: String, feedback: String, patientName: String, patientAddress: String, patinetNo: String,
                            hospital:String,emailAddress:String,order_id:String) {


                                val addShop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(list[position].shop_id)
                                if (addShop != null) {

                                    //if (addShop.isUploaded) {

                                    doAsync {

                                        val collectionDetails = CollectionDetailsEntity()
                                        collectionDetails.collection = collection/*.substring(1)*/

                                        val random = Random()
                                        val m = random.nextInt(9999 - 1000) + 1000
                                        // start fix collection not sync issue sometimes puja 05-04-2024 mantis id 0027352 v4.2.6
                                        collectionDetails.collection_id = Pref.user_id + "_" + m + "_" + System.currentTimeMillis().toString()
                                        //collectionDetails.collection_id = Pref.user_id + "c" + m
                                        // end fix collection not sync issue sometimes puja 05-04-2024 mantis id 0027352 v4.2.6
                                        collectionDetails.shop_id = list[position].shop_id
                                        collectionDetails.date = date //AppUtils.getCurrentDate()
                                        collectionDetails.only_time = AppUtils.getCurrentTime()  //AppUtils.getCurrentDate()
                                        collectionDetails.bill_id = ""
                                        collectionDetails.order_id = ""
                                        collectionDetails.payment_id = paymentId
                                        collectionDetails.bank = bank
                                        collectionDetails.instrument_no = instrument
                                        collectionDetails.file_path = filePath
                                        collectionDetails.feedback = feedback
                                        collectionDetails.patient_name = patientName
                                        collectionDetails.patient_address = patientAddress
                                        collectionDetails.patient_no = patinetNo

                                        /*06-01-2022*/
                                        collectionDetails.Hospital = hospital
                                        collectionDetails.Email_Address = emailAddress

                                        collectionDetails.order_id = order_id
                                        AppDatabase.getDBInstance()!!.collectionDetailsDao().insert(collectionDetails)

                                        val collectionDate = AppUtils.getCurrentDateForShopActi() + "T" + collectionDetails.only_time

                                        uiThread {

                                            if (AppUtils.isOnline(mContext)) {
                                                if (addShop.isUploaded) {
                                                    addCollectionApi(collectionDetails.shop_id, collectionDetails.collection_id, "",
                                                            "", collection, collectionDate, collectionDetails.bill_id, collectionDetails)
                                                } else {
                                                    syncShopForCollection(addShop, collectionDetails.shop_id, collectionDetails.collection_id, "", "", collection,
                                                            collectionDate, collectionDetails.bill_id, collectionDetails)
                                                }


                                            } else {
                                                (mContext as DashboardActivity).showSnackMessage("Collection added successfully")
                                                voiceCollectionMsg()
                                            }
                                        }
                                    }
                                }
                            }
                        })
                        collectionDialog?.show((mContext as DashboardActivity).supportFragmentManager, "AddCollectionDialog")
                        }
                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onWhatsAppClick(no: String) {
                floating_fab.close(true)

                val url = "https://api.whatsapp.com/send?phone=+91$no"

                try {
                    val pm = mContext.packageManager
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                } catch (e: PackageManager.NameNotFoundException ) {
                    e.printStackTrace()
                    (mContext as DashboardActivity).showSnackMessage("Whatsapp app not installed in your phone.")
                }
                catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    (mContext as DashboardActivity).showSnackMessage("This is not whatsApp no.")
                }
            }

            override fun onSmsClick(no: String) {
                floating_fab.close(true)

                val uri = Uri.parse("smsto:$no")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                //intent.putExtra("sms_body", "The SMS text")
                startActivity(intent)
            }

            override fun onCreateQrClick(position: Int) {
                floating_fab.close(true)
                var content = ""
                list[position].apply {
                    content += shop_id + "\n" + shopName + "\n" + ownerName + "\n" + ownerContactNumber + "\n"

                    if (!TextUtils.isEmpty(ownerEmailId))
                        content += ownerEmailId + "\n"

                    content += address + "\n" + pinCode

                    if (!TextUtils.isEmpty(area_id)) {
                        val area = AppDatabase.getDBInstance()?.areaListDao()?.getSingleArea(area_id)
                        content += "\n" + area?.area_name
                    }
                }
                val bitmap = AppUtils.createQrCode(content)

                if (bitmap != null)
                    QrCodeDialog.newInstance(bitmap, list[position].shop_id, list[position].shopName, "",
                            "Create QR of ${Pref.shopText}").show((mContext as DashboardActivity).supportFragmentManager, "")
            }

            override fun onUpdatePartyStatusClick(position: Int) {
                floating_fab.close(true)
                UpdatePartyStatusDialog.getInstance(AppUtils.hiFirstNameText()+"!", getString(R.string.cancel),
                        getString(R.string.confirm), false, true, false, list[position].party_status_id, object : UpdatePartyStatusDialog.OnButtonClickListener {
                    override fun onLeftClick() {
                    }

                    override fun onRightClick(editableData: String, selectedTypeId: String) {
                        if (list[position].isUploaded) {
                            updatePartyStatus(selectedTypeId, editableData, list[position])
                        }
                        else {
                            (mContext as DashboardActivity).showSnackMessage("Please sync this " + Pref.shopText + " first.")
                            }
                    }
                }).show((mContext as DashboardActivity).supportFragmentManager, "")
            }

            override fun onUpdateBankDetailsClick(position: Int) {
                floating_fab.close(true)
                UpdateBankDetailsDialog.getInstance(AppUtils.hiFirstNameText()+"!", getString(R.string.cancel),
                        getString(R.string.confirm), false, true, false, list[position], object : UpdateBankDetailsDialog.OnButtonClickListener {
                    override fun onLeftClick() {
                    }

                    override fun onRightClick(accountHolder: String, accountNo: String, bankName: String, ifsc: String, upi: String) {
                        if (list[position].isUploaded) {
                            updateBankDetails(accountHolder, accountNo, bankName, ifsc, upi, list[position])
                        }
                        else {
                            (mContext as DashboardActivity).showSnackMessage("Please sync this " + Pref.shopText + " first.")
                            }
                    }
                }).show((mContext as DashboardActivity).supportFragmentManager, "")

            }

            /*10-12-2021*/
            override fun onQuestionnarieClick(shopId: String) {
                dialogOpenQa(shopId)
            }
            /*17-12-2021*/
            override fun onReturnClick(position: Int) {
                (mContext as DashboardActivity).loadFragment(FragType.ViewAllReturnListFragment, true, list[position])
            }

            override fun onStockClick(position: Int) {
                floating_fab.close(true)
                (mContext as DashboardActivity).loadFragment(FragType.StockListFragment, true, list[position])
            }


            override fun updateLocClick(position: Int) {

                if (AppUtils.mLocation != null) {
                    if (AppUtils.mLocation!!.accuracy <= Pref.shopLocAccuracy.toFloat()) {
                        openAddressUpdateDialog(list[position], AppUtils.mLocation!!)
                    } else {
                        Timber.d("======Saved current location is inaccurate (Shop List)========")
                        getShopLatLong(position)
                    }
                } else {
                    Timber.d("=====Saved current location is null (Shop List)======")
                    getShopLatLong(position)
                }
            }

            override fun syncClick(position: Int) {
                val addShopData = AddShopRequestData()
                val mAddShopDBModelEntity = list[position]
                if (mAddShopDBModelEntity.isUploaded == false) {
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
                    addShopData.dob = changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.dateOfBirth)
                    addShopData.date_aniversary = changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.dateOfAniversary)
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

                    var uniqKeyObj=AppDatabase.getDBInstance()!!.shopActivityDao().getNewShopActivityKey(mAddShopDBModelEntity.shop_id,false)
                    addShopData.shop_revisit_uniqKey=uniqKeyObj?.shop_revisit_uniqKey!!

                    addShopData.project_name = mAddShopDBModelEntity.project_name
                    addShopData.landline_number = mAddShopDBModelEntity.landline_number
                    addShopData.agency_name = mAddShopDBModelEntity.agency_name

                    addShopData.alternateNoForCustomer = mAddShopDBModelEntity.alternateNoForCustomer
                    addShopData.whatsappNoForCustomer = mAddShopDBModelEntity.whatsappNoForCustomer

                    // duplicate shop api call
                    addShopData.isShopDuplicate=mAddShopDBModelEntity.isShopDuplicate

                    addShopData.purpose=mAddShopDBModelEntity.purpose
//start AppV 4.2.2 tufan    20/09/2023 FSSAI Lic No Implementation 26813
                    try {
                        addShopData.FSSAILicNo = mAddShopDBModelEntity.FSSAILicNo
                    }catch (ex:Exception){
                        ex.printStackTrace()
                        addShopData.FSSAILicNo = ""
                    }
//end AppV 4.2.2 tufan    20/09/2023 FSSAI Lic No Implementation 26813

                    addShopData.GSTN_Number=mAddShopDBModelEntity.gstN_Number
                    addShopData.ShopOwner_PAN=mAddShopDBModelEntity.shopOwner_PAN


                    callAddShopApi(addShopData, mAddShopDBModelEntity.shopImageLocalPath, null, true,
                            mAddShopDBModelEntity.doc_degree)
                    //callAddShopApi(addShopData, "")
                } else if (mAddShopDBModelEntity.isEditUploaded == 0) {
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
                    addShopData.added_date = ""
                    addShopData.amount = addShopData.amount
                    addShopData.model_id = addShopData.model_id
                    addShopData.primary_app_id = addShopData.primary_app_id
                    addShopData.secondary_app_id = addShopData.secondary_app_id
                    addShopData.lead_id = addShopData.lead_id
                    addShopData.stage_id = addShopData.stage_id
                    addShopData.funnel_stage_id = addShopData.funnel_stage_id
                    addShopData.booking_amount = addShopData.booking_amount

                    if (!TextUtils.isEmpty(mAddShopDBModelEntity.dateOfBirth))
                        addShopData.dob = changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.dateOfBirth)

                    if (!TextUtils.isEmpty(mAddShopDBModelEntity.dateOfAniversary))
                        addShopData.date_aniversary = changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.dateOfAniversary)

                    addShopData.director_name = mAddShopDBModelEntity.director_name
                    addShopData.key_person_name = mAddShopDBModelEntity.person_name
                    addShopData.phone_no = mAddShopDBModelEntity.person_no

                    if (!TextUtils.isEmpty(mAddShopDBModelEntity.family_member_dob))
                        addShopData.family_member_dob = changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.family_member_dob)

                    if (!TextUtils.isEmpty(mAddShopDBModelEntity.add_dob))
                        addShopData.addtional_dob = changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.add_dob)

                    if (!TextUtils.isEmpty(mAddShopDBModelEntity.add_doa))
                        addShopData.addtional_doa = changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.add_doa)

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

                    addShopData.director_name = mAddShopDBModelEntity.director_name
                    addShopData.key_person_name = mAddShopDBModelEntity.person_name
                    addShopData.phone_no = mAddShopDBModelEntity.person_no

                    if (!TextUtils.isEmpty(mAddShopDBModelEntity.family_member_dob))
                        addShopData.family_member_dob = changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.family_member_dob)

                    if (!TextUtils.isEmpty(mAddShopDBModelEntity.add_dob))
                        addShopData.addtional_dob = changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.add_dob)

                    if (!TextUtils.isEmpty(mAddShopDBModelEntity.add_doa))
                        addShopData.addtional_doa = changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.add_doa)

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

                    addShopData.entity_id = addShopData.entity_id
                    addShopData.party_status_id = addShopData.party_status_id
                    addShopData.retailer_id = addShopData.retailer_id
                    addShopData.dealer_id = addShopData.dealer_id
                    addShopData.beat_id = addShopData.beat_id
                    addShopData.assigned_to_shop_id = addShopData.assigned_to_shop_id

                    // contact module
                    try{
                        addShopData.address = mAddShopDBModelEntity.address
                        addShopData.actual_address = mAddShopDBModelEntity.address
                        addShopData.shop_firstName= mAddShopDBModelEntity.crm_firstName
                        addShopData.shop_lastName=  mAddShopDBModelEntity.crm_lastName
                        addShopData.crm_companyID=  if(mAddShopDBModelEntity.companyName_id.equals("")) "0" else mAddShopDBModelEntity.companyName_id
                        addShopData.crm_jobTitle=  mAddShopDBModelEntity.jobTitle
                        addShopData.crm_typeID=  if(mAddShopDBModelEntity.crm_type_ID.equals("")) "0" else mAddShopDBModelEntity.crm_type_ID
                        addShopData.crm_statusID=  if(mAddShopDBModelEntity.crm_status_ID.equals("")) "0" else mAddShopDBModelEntity.crm_status_ID
                        addShopData.crm_sourceID= if(mAddShopDBModelEntity.crm_source_ID.equals("")) "0" else mAddShopDBModelEntity.crm_source_ID
                        addShopData.crm_reference=  mAddShopDBModelEntity.crm_reference
                        addShopData.crm_referenceID=  if(mAddShopDBModelEntity.crm_reference_ID.equals("")) "0" else mAddShopDBModelEntity.crm_reference_ID
                        addShopData.crm_referenceID_type=  mAddShopDBModelEntity.crm_reference_ID_type
                        addShopData.crm_stage_ID=  if(mAddShopDBModelEntity.crm_stage_ID.equals("")) "0" else mAddShopDBModelEntity.crm_stage_ID
                        addShopData.assign_to=  mAddShopDBModelEntity.crm_assignTo_ID
                        addShopData.saved_from_status=  mAddShopDBModelEntity.crm_saved_from
                    }catch (ex:Exception){
                        ex.printStackTrace()
                        Timber.d("Logout edit sync err ${ex.message}")
                    }

                    callEditShopApi(addShopData, mAddShopDBModelEntity.shopImageLocalPath, true, false,
                            mAddShopDBModelEntity.doc_degree)
                }
            }

            override fun mapClick(position: Int) {
                floating_fab.close(true)
//                (mContext as DashboardActivity).openLocationWithTrack()
                (mContext as DashboardActivity).openLocationMap(list[position], true)
            }

            override fun orderClick(position: Int) {
                floating_fab.close(true)
                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.functionality_disabled))



                if(Pref.IsActivateNewOrderScreenwithSize){
                    (mContext as DashboardActivity).loadFragment(FragType.NewOrderScrOrderDetailsFragment, true, list.get(position).shop_id)
                }else{
                    CustomStatic.IsOrderLoadFromCRM = false
                    CustomStatic.IsOrderLoadFromShop = true
                    (mContext as DashboardActivity).loadFragment(FragType.ViewAllOrderListFragment, true, list[position])
                }


            }

            @SuppressLint("NewApi")
            override fun callClick(position: Int) {
                floating_fab.close(true)
                IntentActionable.initiatePhoneCall(mContext, list[position].ownerContactNumber)
//                initiatePopupWindow(view,position)
            }

            override fun OnNearByShopsListClick(position: Int) {
                floating_fab.close(true)
                (mContext as DashboardActivity).loadFragment(FragType.ShopDetailFragment, true, list[position].shop_id)
            }


            override fun onHistoryClick(shop: Any) {
                (mContext as DashboardActivity).loadFragment(FragType.ShopFeedbackHisFrag, true, shop)
            }
        })

        sortAlphabatically()

        layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager
        nearByShopsList.layoutManager = layoutManager
        nearByShopsList.adapter = mNearByShopsListAdapter

        println("time_check initAdapterend")

    }



    @SuppressLint("RestrictedApi")
    fun showExtraContactDtls(name:String, phno:String, email:String, doa:String,dob:String){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_multiple_contact)

        val tv_header = simpleDialog.findViewById(R.id.tv_header_dialog_multi_contact) as TextView
        val ic_cross = simpleDialog.findViewById(R.id.iv_dialog_multi_cont_cross) as ImageView

        val tv_captionName = simpleDialog.findViewById(R.id.tv_dialog_multi_contact_name_caption) as TextView
        val tv_captionNumber = simpleDialog.findViewById(R.id.tv_dialog_multi_contact_number_caption) as TextView
        val tv_captionEmail = simpleDialog.findViewById(R.id.tv_dialog_multi_contact_email_caption) as TextView
        val tv_captionDOA = simpleDialog.findViewById(R.id.tv_dialog_multi_contact_anniv_caption) as TextView
        val tv_captionDOB = simpleDialog.findViewById(R.id.tv_dialog_multi_contact_dob_caption) as TextView

        tv_captionName.visibility = View.VISIBLE
        tv_captionNumber.visibility = View.VISIBLE
        tv_captionEmail.visibility = View.VISIBLE
        tv_captionDOA.visibility = View.VISIBLE
        tv_captionDOB.visibility = View.VISIBLE

        val et_contactName = simpleDialog.findViewById(R.id.et_dialog_multi_contact_name) as EditText
        val et_contactPhno = simpleDialog.findViewById(R.id.et_dialog_multi_contact_phno) as EditText
        val et_contact_email = simpleDialog.findViewById(R.id.et_dialog_multi_contact_email) as EditText
        val et_anniv = simpleDialog.findViewById(R.id.tv_dialog_multi_contact_anniv) as TextView
        val et_dob = simpleDialog.findViewById(R.id.tv_dialog_multi_contact_dob) as TextView
        val fab_add = simpleDialog.findViewById(R.id.fab_dialog_multi_contact_plus) as com.google.android.material.floatingactionbutton.FloatingActionButton

        et_contactName.setText(name)
        et_contactPhno.setText(phno)
        et_contact_email.setText(if(email.toString().length>0) email.toString() else "NA")
        et_anniv.setText(if(doa.toString().length>0) AppUtils.getFormatedDateNew(doa,"yyyy-mm-dd","dd-mm-yyyy") else "NA")
        et_dob.setText(if(dob.toString().length>0) AppUtils.getFormatedDateNew(dob,"yyyy-mm-dd","dd-mm-yyyy") else "NA")

        tv_header.text = "View Contact"

        et_contactName.isEnabled = false
        et_contactPhno.isEnabled = false
        et_contact_email.isEnabled = false
        et_anniv.isEnabled = false
        et_dob.isEnabled = false
        fab_add.isEnabled = false
        fab_add.visibility = View.GONE

        ic_cross.setOnClickListener {
            simpleDialog.dismiss()
        }

        simpleDialog.show()
    }

    fun addExtraContactDtls(serial:String,shop_idSel:String){
        var myCalendar = Calendar.getInstance(Locale.ENGLISH)
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_multiple_contact)

        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        val ic_cross = simpleDialog.findViewById(R.id.iv_dialog_multi_cont_cross) as ImageView
        val et_contactName = simpleDialog.findViewById(R.id.et_dialog_multi_contact_name) as EditText
        val et_contactPhno = simpleDialog.findViewById(R.id.et_dialog_multi_contact_phno) as EditText
        val et_contact_email = simpleDialog.findViewById(R.id.et_dialog_multi_contact_email) as EditText
        val et_dob = simpleDialog.findViewById(R.id.tv_dialog_multi_contact_dob) as TextView
        val et_anniv = simpleDialog.findViewById(R.id.tv_dialog_multi_contact_anniv) as TextView
        val fab_add = simpleDialog.findViewById(R.id.fab_dialog_multi_contact_plus) as com.google.android.material.floatingactionbutton.FloatingActionButton

        val dateOtherAnniv = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            //et_anniv.setText(AppUtils.changeAttendanceDateFormat(AppUtils.getDobFormattedDate(myCalendar.time)))
            //et_anniv.setText(AppUtils.getFormattedDateForApi(myCalendar.time))
            et_anniv.setText(AppUtils.getFormattedDateForApi1(myCalendar.time))
        }
        val dateOtherDOB = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            //et_anniv.setText(AppUtils.changeAttendanceDateFormat(AppUtils.getDobFormattedDate(myCalendar.time)))
            //et_anniv.setText(AppUtils.getFormattedDateForApi(myCalendar.time))
            et_dob.setText(AppUtils.getFormattedDateForApi1(myCalendar.time))
        }
        et_anniv.setOnClickListener({ view ->
            AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
            var aniDatePicker = DatePickerDialog(mContext, R.style.DatePickerTheme, dateOtherAnniv, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
            aniDatePicker.datePicker.maxDate = Calendar.getInstance(Locale.ENGLISH).timeInMillis
            aniDatePicker.show()
        })
        et_dob.setOnClickListener({ view ->
            AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
            var aniDatePicker = DatePickerDialog(mContext, R.style.DatePickerTheme, dateOtherDOB, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
            aniDatePicker.datePicker.maxDate = Calendar.getInstance(Locale.ENGLISH).timeInMillis
            aniDatePicker.show()
        })

        fab_add.isEnabled = true

        fab_add.setOnClickListener {
            if(et_contactName.text.toString().length == 0){
                Toaster.msgShort(mContext,"Please enter Contact Name")
                return@setOnClickListener
            }
            if(et_contactPhno.text.toString().length == 0 || et_contactPhno.text.toString().length !=10){
                Toaster.msgShort(mContext,"Please enter valid Contact Phone Number")
                return@setOnClickListener
            }
            var obj : ShopExtraContactEntity = ShopExtraContactEntity()
            obj.apply {
                shop_id = shop_idSel
                contact_serial = serial
                contact_name = et_contactName.text.toString()
                contact_number = et_contactPhno.text.toString()
                contact_email = et_contact_email.text.toString()
                contact_dob = if(et_dob.text.toString().length>0) AppUtils.getFormatedDateNew(et_dob.text.toString(),"dd-mm-yyyy","yyyy-mm-dd") else ""
                contact_doa = if(et_anniv.text.toString().length>0) AppUtils.getFormatedDateNew(et_anniv.text.toString(),"dd-mm-yyyy","yyyy-mm-dd") else ""
                isUploaded = false
            }
//            AppDatabase.getDBInstance()?.shopExtraContactDao()?.insert(obj)

            // 2.0 NearByShopsListFragment AppV 4.0.6   Contact Multi Api called Add & Update
            shopExtraContactList.add(obj)
            if(serial.equals("1")){
                callAddMultiContactapi(shop_idSel)
            } else{
                callUpdateMultiContactapi(shop_idSel)
            }
            // 2.0 NearByShopsListFragment AppV 4.0.6   Contact Multi Api called Add & Update

            Toaster.msgShort(mContext,"Contact added")
            simpleDialog.dismiss()
            simpleDialogRoot.dismiss()
        }

        ic_cross.setOnClickListener {
            simpleDialog.dismiss()
        }

        simpleDialog.show()

    }

    private fun updatePartyStatus(selectedTypeId: String, editableData: String, addShopDBModelEntity: AddShopDBModelEntity) {
        /*if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage("Your network connection is offine. Make it online to proceed with update.")
            return
        }*/

        val repository = TypeListRepoProvider.provideTypeListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.updatePartyStatus(addShopDBModelEntity.shop_id, selectedTypeId, editableData)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                AppDatabase.getDBInstance()?.addShopEntryDao()?.updatePartyStatus(selectedTypeId, addShopDBModelEntity.shop_id)
                                initAdapter()
                            }

                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(response.message!!)

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                        })
        )
    }

    private fun updateBankDetails(accountHolder: String, accountNo: String, bankName: String, ifsc: String, upi: String, addShopDBModelEntity: AddShopDBModelEntity) {
        val repository = TypeListRepoProvider.provideTypeListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.updateBankDetails(addShopDBModelEntity.shop_id, accountHolder, accountNo, bankName, ifsc, upi)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                AppDatabase.getDBInstance()?.addShopEntryDao()?.updateBankDetails(accountHolder, accountNo, bankName, ifsc, upi, addShopDBModelEntity.shop_id)
                                initAdapter()
                            }

                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(response.message!!)

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                        })
        )
    }

    private fun voiceCollectionMsg() {
        if (Pref.isVoiceEnabledForCollectionSaved) {
            val msg = "Hi, Collection saved successfully."
            val speechStatus = (mContext as DashboardActivity).textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
            if (speechStatus == TextToSpeech.ERROR)
                Log.e("Add Collection", "TTS error in converting Text to Speech!");

        }
    }


    private fun syncShopForCollection(addShop: AddShopDBModelEntity, shop_id: String?, order_id: String?, amount: String, desc: String, collection: String,
                                      currentDateForShopActi: String, billId: String?, collectionDetails: CollectionDetailsEntity) {
        val addShopData = AddShopRequestData()
        val mAddShopDBModelEntity = addShop
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
        addShopData.assigned_to_pp_id = mAddShopDBModelEntity.assigned_to_pp_id
        addShopData.assigned_to_dd_id = mAddShopDBModelEntity.assigned_to_dd_id
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

        var uniqKeyObj=AppDatabase.getDBInstance()!!.shopActivityDao().getNewShopActivityKey(mAddShopDBModelEntity.shop_id,false)
        addShopData.shop_revisit_uniqKey=uniqKeyObj?.shop_revisit_uniqKey!!

        addShopData.project_name = mAddShopDBModelEntity.project_name
        addShopData.landline_number = mAddShopDBModelEntity.landline_number
        addShopData.agency_name = mAddShopDBModelEntity.agency_name

        addShopData.alternateNoForCustomer = mAddShopDBModelEntity.alternateNoForCustomer
        addShopData.whatsappNoForCustomer = mAddShopDBModelEntity.whatsappNoForCustomer

        // duplicate shop api call
        addShopData.isShopDuplicate=mAddShopDBModelEntity.isShopDuplicate

        addShopData.purpose=mAddShopDBModelEntity.purpose
//start AppV 4.2.2 tufan    20/09/2023 FSSAI Lic No Implementation 26813
        try {
            addShopData.FSSAILicNo = mAddShopDBModelEntity.FSSAILicNo
        }catch (ex:Exception){
            ex.printStackTrace()
            addShopData.FSSAILicNo = ""
        }
//end AppV 4.2.2 tufan    20/09/2023 FSSAI Lic No Implementation 26813

        addShopData.GSTN_Number=mAddShopDBModelEntity.gstN_Number
        addShopData.ShopOwner_PAN=mAddShopDBModelEntity.shopOwner_PAN

        //contact shop sync
        try{
            addShopData.actual_address = mAddShopDBModelEntity.address
            addShopData.shop_firstName=  mAddShopDBModelEntity.crm_firstName
            addShopData.shop_lastName=  mAddShopDBModelEntity.crm_lastName
            addShopData.crm_companyID=  if(mAddShopDBModelEntity.companyName_id.equals("")) "0" else mAddShopDBModelEntity.companyName_id
            addShopData.crm_jobTitle=  mAddShopDBModelEntity.jobTitle
            addShopData.crm_typeID=  if(mAddShopDBModelEntity.crm_type_ID.equals("")) "0" else mAddShopDBModelEntity.crm_type_ID
            addShopData.crm_statusID=  if(mAddShopDBModelEntity.crm_status_ID.equals("")) "0" else mAddShopDBModelEntity.crm_status_ID
            addShopData.crm_sourceID= if(mAddShopDBModelEntity.crm_source_ID.equals("")) "0" else mAddShopDBModelEntity.crm_source_ID
            addShopData.crm_reference=  mAddShopDBModelEntity.crm_reference
            addShopData.crm_referenceID=  if(mAddShopDBModelEntity.crm_reference_ID.equals("")) "0" else mAddShopDBModelEntity.crm_reference_ID
            addShopData.crm_referenceID_type=  mAddShopDBModelEntity.crm_reference_ID_type
            addShopData.crm_stage_ID=  if(mAddShopDBModelEntity.crm_stage_ID.equals("")) "0" else mAddShopDBModelEntity.crm_stage_ID
            addShopData.assign_to=  mAddShopDBModelEntity.crm_assignTo_ID
            addShopData.saved_from_status=  mAddShopDBModelEntity.crm_saved_from
        }catch (ex:Exception){
            ex.printStackTrace()
        }


        callAddShopApi(addShopData, mAddShopDBModelEntity.shopImageLocalPath, shop_id, order_id, amount, collection,
                currentDateForShopActi, desc, billId, mAddShopDBModelEntity.doc_degree, collectionDetails)
    }

    private fun callAddShopApi(addShop: AddShopRequestData, shop_imgPath: String?, shop_id: String?, order_id: String?, amount: String, collection: String,
                               currentDateForShopActi: String, desc: String, billId: String?, degree_imgPath: String?, collectionDetails: CollectionDetailsEntity) {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }
        if (isShopRegistrationInProcess)
            return

        progress_wheel.spin()

        isShopRegistrationInProcess = true

        Timber.d("==============================SyncShop Input Params(Shop List)==============================")
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
        Timber.d("=================================================================================")

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
                                    //(mContext as DashboardActivity).showSnackMessage("Synced successfully")
                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                addCollectionApi(shop_id, order_id, amount, desc, collection, currentDateForShopActi,
                                                        billId, collectionDetails)
                                            }
                                        }
                                    }
                                    progress_wheel.stopSpinning()
                                    isShopRegistrationInProcess = false

                                } else if (addShopResult.status == NetworkConstant.DUPLICATE_SHOP_ID) {
                                    Timber.d("DuplicateShop : " + ", SHOP: " + addShop.shop_name)
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)
                                    if (AppDatabase.getDBInstance()!!.addShopEntryDao().getDuplicateShopData(addShop.owner_contact_no).size > 0) {
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().deleteShopById(addShop.shop_id)
                                        AppDatabase.getDBInstance()!!.shopActivityDao().deleteShopByIdAndDate(addShop.shop_id!!, AppUtils.getCurrentDateForShopActi())
                                    }
                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                addCollectionApi(shop_id, order_id, amount, desc, collection, currentDateForShopActi,
                                                        billId, collectionDetails)
                                            }
                                        }
                                    }
                                    isShopRegistrationInProcess = false

                                } else {
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage("Collection added successfully")

                                    isShopRegistrationInProcess = false
                                }

                            }, { error ->
                                error.printStackTrace()
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage("Collection added successfully")
                                isShopRegistrationInProcess = false
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
                                    //(mContext as DashboardActivity).showSnackMessage("Synced successfully")
                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                addCollectionApi(shop_id, order_id, amount, desc, collection, currentDateForShopActi,
                                                        billId, collectionDetails)
                                            }
                                        }
                                    }
                                    progress_wheel.stopSpinning()
                                    isShopRegistrationInProcess = false

                                } else if (addShopResult.status == NetworkConstant.DUPLICATE_SHOP_ID) {
                                    Timber.d("DuplicateShop : " + ", SHOP: " + addShop.shop_name)
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)
                                    if (AppDatabase.getDBInstance()!!.addShopEntryDao().getDuplicateShopData(addShop.owner_contact_no).size > 0) {
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().deleteShopById(addShop.shop_id)
                                        AppDatabase.getDBInstance()!!.shopActivityDao().deleteShopByIdAndDate(addShop.shop_id!!, AppUtils.getCurrentDateForShopActi())
                                    }
                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                addCollectionApi(shop_id, order_id, amount, desc, collection, currentDateForShopActi,
                                                        billId, collectionDetails)
                                            }
                                        }
                                    }
                                    isShopRegistrationInProcess = false

                                } else {
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage("Collection added successfully")

                                    isShopRegistrationInProcess = false
                                }

                            }, { error ->
                                error.printStackTrace()
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage("Collection added successfully")
                                isShopRegistrationInProcess = false
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

            if (!TextUtils.isEmpty(shopActivity.feedback)) {
                shopDurationData.feedback = shopActivity.feedback
                }
            else {
                shopDurationData.feedback = ""
                }

            shopDurationData.isFirstShopVisited = shopActivity.isFirstShopVisited
            shopDurationData.distanceFromHomeLoc = shopActivity.distance_from_home_loc
            shopDurationData.next_visit_date = shopActivity.next_visit_date

            if (!TextUtils.isEmpty(shopActivity.early_revisit_reason)) {
                shopDurationData.early_revisit_reason = shopActivity.early_revisit_reason
                }
            else {
                shopDurationData.early_revisit_reason = ""
                }

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
            try {
                shopDurationData.updated_on = shopActivity.updated_on!!
            }
            catch(ex:Exception){
                shopDurationData.updated_on = ""
            }

            if (!TextUtils.isEmpty(shopActivity.pros_id!!)) {
                shopDurationData.pros_id = shopActivity.pros_id!!
                }
            else {
                shopDurationData.pros_id = ""
            }
            if (!TextUtils.isEmpty(shopActivity.agency_name!!)) {
                shopDurationData.agency_name = shopActivity.agency_name!!
            }
            else {
                shopDurationData.agency_name = ""
            }
            if (!TextUtils.isEmpty(shopActivity.approximate_1st_billing_value)) {
                shopDurationData.approximate_1st_billing_value =
                    shopActivity.approximate_1st_billing_value!!
            }
            else {
                shopDurationData.approximate_1st_billing_value = ""
                }
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

            // 3.0 NearByShopsListFragment AppV 4.0.6  multiple contact Data added on Api called
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

                if (!TextUtils.isEmpty(shopActivity.feedback)) {
                    shopDurationData.feedback = shopActivity.feedback
                    }
                else {
                    shopDurationData.feedback = ""
                    }

                shopDurationData.isFirstShopVisited = shopActivity.isFirstShopVisited
                shopDurationData.distanceFromHomeLoc = shopActivity.distance_from_home_loc
                shopDurationData.next_visit_date = shopActivity.next_visit_date

                if (!TextUtils.isEmpty(shopActivity.early_revisit_reason)) {
                    shopDurationData.early_revisit_reason = shopActivity.early_revisit_reason
                    }
                else {
                    shopDurationData.early_revisit_reason = ""
                }

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
                try {
                    shopDurationData.updated_on = shopActivity.updated_on!!
                }
                catch(ex:Exception){
                    shopDurationData.updated_on = ""
                }

                if (!TextUtils.isEmpty(shopActivity.pros_id!!)) {
                    shopDurationData.pros_id = shopActivity.pros_id!!
                    }
                else {
                    shopDurationData.pros_id = ""
                    }

                if (!TextUtils.isEmpty(shopActivity.agency_name!!)) {
                    shopDurationData.agency_name = shopActivity.agency_name!!
                }
                else {
                    shopDurationData.agency_name = ""
                    }

                if (!TextUtils.isEmpty(shopActivity.approximate_1st_billing_value)) {
                    shopDurationData.approximate_1st_billing_value =
                        shopActivity.approximate_1st_billing_value!!
                }
                else {
                    shopDurationData.approximate_1st_billing_value = ""
                    }
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
                // 3.0 NearByShopsListFragment AppV 4.0.6  multiple contact Data added on Api called
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


    private fun addCollectionApi(shop_id: String?, order_id: String?, amount: String, desc: String, collection: String, date: String?,
                                 billId: String?, collectionDetails: CollectionDetailsEntity) {

        val addCollection = AddCollectionInputParamsModel()
        addCollection.collection = collection
        addCollection.collection_date = date
        addCollection.collection_id = order_id
        addCollection.session_token = Pref.session_token
        addCollection.user_id = Pref.user_id
        addCollection.shop_id = shop_id

        if (!TextUtils.isEmpty(billId))
            addCollection.bill_id = billId!!
        else
            addCollection.bill_id = ""

        addCollection.order_id = ""
        addCollection.payment_id = collectionDetails.payment_id!!

        if (collectionDetails.instrument_no != null)
            addCollection.instrument_no = collectionDetails.instrument_no!!

        if(collectionDetails.bank != null)
            addCollection.bank = collectionDetails.bank!!

        if (collectionDetails.feedback != null)
            addCollection.remarks = collectionDetails.feedback!!

        if (collectionDetails.patient_name != null)
            addCollection.patient_name = collectionDetails.patient_name!!

        if (collectionDetails.patient_address != null)
            addCollection.patient_address = collectionDetails.patient_address!!

        if (collectionDetails.patient_no != null)
            addCollection.patient_no = collectionDetails.patient_no!!
        /*06-01-2022*/
        if (collectionDetails.Hospital != null)
            addCollection.Hospital = collectionDetails.Hospital!!

        if (collectionDetails.Email_Address != null)
            addCollection.Email_Address = collectionDetails.Email_Address!!

        progress_wheel.spin()

        if (TextUtils.isEmpty(collectionDetails.file_path)) {
            val repository = AddCollectionRepoProvider.addCollectionRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addCollection(addCollection)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val orderList = result as BaseResponse
                                Timber.d("addCollectionApi " + "RESPONSE : " + orderList.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name)
                                progress_wheel.stopSpinning()
                                if (orderList.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.collectionDetailsDao().updateIsUploaded(true, order_id!!)
                                }

                                (mContext as DashboardActivity).showSnackMessage("Collection added successfully")
                                voiceCollectionMsg()

                            }, { error ->
                                error.printStackTrace()
                                Timber.d("addCollectionApi " + "error " + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name)
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage("Collection added successfully")
                                voiceCollectionMsg()
                            })
            )
        }
        else {
            val repository = AddCollectionRepoProvider.addCollectionMultipartRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addCollection(addCollection, collectionDetails.file_path, mContext)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val orderList = result as BaseResponse
                                progress_wheel.stopSpinning()
                                Timber.d("addCollectionApi withImg " + "RESPONSE : " + orderList.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name)
                                if (orderList.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.collectionDetailsDao().updateIsUploaded(true, order_id!!)
                                }

                                (mContext as DashboardActivity).showSnackMessage("Collection added successfully")
                                voiceCollectionMsg()

                            }, { error ->
                                error.printStackTrace()
                                Timber.d("addCollectionApi withImg " + "error " + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name)
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage("Collection added successfully")
                                voiceCollectionMsg()
                            })
            )
        }
    }


    private fun getShopLatLong(position: Int) {
        if (isAddressUpdating)
            return

        isAddressUpdating = true
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
                        progress_wheel.stopSpinning()
                        isAddressUpdating = false
                        if (location.accuracy > Pref.shopLocAccuracy.toFloat()) {
                            if (dialog == null) {
                                dialog = AccuracyIssueDialog()
                                dialog?.show((mContext as DashboardActivity).supportFragmentManager, "AccuracyIssueDialog")
                            } else {
                                dialog?.dismissAllowingStateLoss()
                                dialog?.show((mContext as DashboardActivity).supportFragmentManager, "AccuracyIssueDialog")

                            }
                        } else {
                            openAddressUpdateDialog(list[position], location)
                        }
                    }
                })
    }

    private fun geStageApi(position: Int) {

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        progress_wheel.spin()
        val repository = ShopListRepositoryProvider.provideShopListRepository()
        BaseActivity.compositeDisposable.add(
                repository.getStagList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as StageListResponseModel
                            Timber.d("GET STAGE DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.stage_list != null && response.stage_list!!.isNotEmpty()) {

                                    doAsync {

                                        response.stage_list?.forEach {
                                            val stageEntity = StageEntity()
                                            AppDatabase.getDBInstance()?.stageDao()?.insertAll(stageEntity.apply {
                                                stage_id = it.id
                                                stage_name = it.name
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            showStageDialog(AppDatabase.getDBInstance()?.stageDao()?.getAll() as ArrayList<StageEntity>, position)
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                }


                            } else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            Timber.d("GET STAGE DATA : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun showStageDialog(stageList: ArrayList<StageEntity>, position: Int) {
        StageListDialog.newInstance(stageList) { stage: StageEntity ->
            val shop = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(list[position].shop_id)

            if (shop != null) {
                shop.stage_id = stage.stage_id
                shop.isEditUploaded = 0
                AppDatabase.getDBInstance()?.addShopEntryDao()?.updateAddShop(shop)

                convertToReqAndApiCall(shop, false)
            }

        }.show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private fun convertToReqAndApiCall(addShopData: AddShopDBModelEntity, isAddressUpdated: Boolean) {
        if (Pref.user_id == null || Pref.user_id == "" || Pref.user_id == " ") {
            (mContext as DashboardActivity).showSnackMessage("Please login again")
            BaseActivity.isApiInitiated = false
            return
        }

        val addShopReqData = AddShopRequestData()
        addShopReqData.session_token = Pref.session_token
        addShopReqData.address = addShopData.address
        addShopReqData.actual_address = addShopData.address
        addShopReqData.owner_contact_no = addShopData.ownerContactNumber
        addShopReqData.owner_email = addShopData.ownerEmailId
        addShopReqData.owner_name = addShopData.ownerName
        addShopReqData.pin_code = addShopData.pinCode
        addShopReqData.shop_lat = addShopData.shopLat.toString()
        addShopReqData.shop_long = addShopData.shopLong.toString()
        addShopReqData.shop_name = addShopData.shopName.toString()
        addShopReqData.shop_id = addShopData.shop_id
        addShopReqData.added_date = ""
        addShopReqData.user_id = Pref.user_id
        addShopReqData.type = addShopData.type
        addShopReqData.assigned_to_pp_id = addShopData.assigned_to_pp_id
        addShopReqData.assigned_to_dd_id = addShopData.assigned_to_dd_id
        /*addShopReqData.dob = addShopData.dateOfBirth
        addShopReqData.date_aniversary = addShopData.dateOfAniversary*/
        addShopReqData.amount = addShopData.amount
        addShopReqData.area_id = addShopData.area_id
        /*val addShop = AddShopRequest()
        addShop.data = addShopReqData*/

        addShopReqData.model_id = addShopData.model_id
        addShopReqData.primary_app_id = addShopData.primary_app_id
        addShopReqData.secondary_app_id = addShopData.secondary_app_id
        addShopReqData.lead_id = addShopData.lead_id
        addShopReqData.stage_id = addShopData.stage_id
        addShopReqData.funnel_stage_id = addShopData.funnel_stage_id
        addShopReqData.booking_amount = addShopData.booking_amount
        addShopReqData.type_id = addShopData.type_id

        if (!TextUtils.isEmpty(addShopData.dateOfBirth))
            addShopReqData.dob = changeAttendanceDateFormatToCurrent(addShopData.dateOfBirth)

        if (!TextUtils.isEmpty(addShopData.dateOfAniversary))
            addShopReqData.date_aniversary = changeAttendanceDateFormatToCurrent(addShopData.dateOfAniversary)

        addShopReqData.director_name = addShopData.director_name
        addShopReqData.key_person_name = addShopData.person_name
        addShopReqData.phone_no = addShopData.person_no

        if (!TextUtils.isEmpty(addShopData.family_member_dob))
            addShopReqData.family_member_dob = changeAttendanceDateFormatToCurrent(addShopData.family_member_dob)

        if (!TextUtils.isEmpty(addShopData.add_dob))
            addShopReqData.addtional_dob = changeAttendanceDateFormatToCurrent(addShopData.add_dob)

        if (!TextUtils.isEmpty(addShopData.add_doa))
            addShopReqData.addtional_doa = changeAttendanceDateFormatToCurrent(addShopData.add_doa)

        addShopReqData.specialization = addShopData.specialization
        addShopReqData.category = addShopData.category
        addShopReqData.doc_address = addShopData.doc_address
        addShopReqData.doc_pincode = addShopData.doc_pincode
        addShopReqData.is_chamber_same_headquarter = addShopData.chamber_status.toString()
        addShopReqData.is_chamber_same_headquarter_remarks = addShopData.remarks
        addShopReqData.chemist_name = addShopData.chemist_name
        addShopReqData.chemist_address = addShopData.chemist_address
        addShopReqData.chemist_pincode = addShopData.chemist_pincode
        addShopReqData.assistant_contact_no = addShopData.assistant_no
        addShopReqData.average_patient_per_day = addShopData.patient_count
        addShopReqData.assistant_name = addShopData.assistant_name

        if (!TextUtils.isEmpty(addShopData.doc_family_dob))
            addShopReqData.doc_family_member_dob = changeAttendanceDateFormatToCurrent(addShopData.doc_family_dob)

        if (!TextUtils.isEmpty(addShopData.assistant_dob))
            addShopReqData.assistant_dob = changeAttendanceDateFormatToCurrent(addShopData.assistant_dob)

        if (!TextUtils.isEmpty(addShopData.assistant_doa))
            addShopReqData.assistant_doa = changeAttendanceDateFormatToCurrent(addShopData.assistant_doa)

        if (!TextUtils.isEmpty(addShopData.assistant_family_dob))
            addShopReqData.assistant_family_dob = changeAttendanceDateFormatToCurrent(addShopData.assistant_family_dob)

        addShopReqData.entity_id = addShopData.entity_id
        addShopReqData.party_status_id = addShopData.party_status_id
        addShopReqData.retailer_id = addShopData.retailer_id
        addShopReqData.dealer_id = addShopData.dealer_id
        addShopReqData.beat_id = addShopData.beat_id
        addShopReqData.assigned_to_shop_id = addShopData.assigned_to_shop_id
        addShopReqData.actual_address = addShopData.address


        addShopReqData.GSTN_Number = addShopData.gstN_Number
        addShopReqData.ShopOwner_PAN = addShopData.shopOwner_PAN

        //begin Suman 12-10-2023 mantis id 26874
        if(isAddressUpdated){
            addShopReqData.isUpdateAddressFromShopMaster = true
        }
        //end Suman 12-10-2023 mantis id 26874

        // contact module
        try{
            addShopReqData.address = addShopData.address
            addShopReqData.actual_address = addShopData.address
            addShopReqData.shop_firstName= addShopData.crm_firstName
            addShopReqData.shop_lastName=  addShopData.crm_lastName
            addShopReqData.crm_companyID=  if(addShopData.companyName_id.equals("")) "0" else addShopData.companyName_id
            addShopReqData.crm_jobTitle=  addShopData.jobTitle
            addShopReqData.crm_typeID=  if(addShopData.crm_type_ID.equals("")) "0" else addShopData.crm_type_ID
            addShopReqData.crm_statusID=  if(addShopData.crm_status_ID.equals("")) "0" else addShopData.crm_status_ID
            addShopReqData.crm_sourceID= if(addShopData.crm_source_ID.equals("")) "0" else addShopData.crm_source_ID
            addShopReqData.crm_reference=  addShopData.crm_reference
            addShopReqData.crm_referenceID=  if(addShopData.crm_reference_ID.equals("")) "0" else addShopData.crm_reference_ID
            addShopReqData.crm_referenceID_type=  addShopData.crm_reference_ID_type
            addShopReqData.crm_stage_ID=  if(addShopData.crm_stage_ID.equals("")) "0" else addShopData.crm_stage_ID
            addShopReqData.assign_to=  addShopData.crm_assignTo_ID
            addShopReqData.saved_from_status=  addShopData.crm_saved_from
        }catch (ex:Exception){
            ex.printStackTrace()
            Timber.d("Logout edit sync err ${ex.message}")
        }

        callEditShopApi(addShopReqData, addShopData.shopImageLocalPath, false, isAddressUpdated, addShopData.doc_degree)
    }

    private fun callEditShopApi(addShopReqData: AddShopRequestData, shopImageLocalPath: String?, isSync: Boolean,
                                isAddressUpdated: Boolean, doc_degree: String?) {

        if (!AppUtils.isOnline(mContext)) {
            if (isSync) {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                }
            else {
                if (isAddressUpdated) {
                    (mContext as DashboardActivity).showSnackMessage("Address updated successfully")

                   //
                     initAdapter()
                    sendNotification(addShopReqData.shop_id!!)
                }
                else {
                    (mContext as DashboardActivity).showSnackMessage("Stage updated successfully")
                    }
            }
            return
        }

        if (BaseActivity.isApiInitiated)
            return

        BaseActivity.isApiInitiated = true

        Timber.d("=====Sync EditShop Input Params (Shop List)======")
        Timber.d("shop id====> " + addShopReqData.shop_id)
        val index = addShopReqData.shop_id!!.indexOf("_")
        Timber.d("decoded shop id====> " + addShopReqData.user_id + "_" + AppUtils.getDate(addShopReqData.shop_id!!.substring(index + 1, addShopReqData.shop_id!!.length).toLong()))
        Timber.d("shop added date====> " + addShopReqData.added_date)
        Timber.d("shop address====> " + addShopReqData.address)
        Timber.d("assigned to dd id====> " + addShopReqData.assigned_to_dd_id)
        Timber.d("assigned to pp id=====> " + addShopReqData.assigned_to_pp_id)
        Timber.d("date aniversery=====> " + addShopReqData.date_aniversary)
        Timber.d("dob====> " + addShopReqData.dob)
        Timber.d("shop owner phn no===> " + addShopReqData.owner_contact_no)
        Timber.d("shop owner email====> " + addShopReqData.owner_email)
        Timber.d("shop owner name====> " + addShopReqData.owner_name)
        Timber.d("shop pincode====> " + addShopReqData.pin_code)
        Timber.d("session token====> " + addShopReqData.session_token)
        Timber.d("shop lat====> " + addShopReqData.shop_lat)
        Timber.d("shop long===> " + addShopReqData.shop_long)
        Timber.d("shop name====> " + addShopReqData.shop_name)
        Timber.d("shop type===> " + addShopReqData.type)
        Timber.d("user id====> " + addShopReqData.user_id)
        Timber.d("amount=======> " + addShopReqData.amount)
        Timber.d("area id=======> " + addShopReqData.area_id)
        Timber.d("model id=======> " + addShopReqData.model_id)
        Timber.d("primary app id=======> " + addShopReqData.primary_app_id)
        Timber.d("secondary app id=======> " + addShopReqData.secondary_app_id)
        Timber.d("lead id=======> " + addShopReqData.lead_id)
        Timber.d("stage id=======> " + addShopReqData.stage_id)
        Timber.d("funnel stage id=======> " + addShopReqData.funnel_stage_id)
        Timber.d("booking amount=======> " + addShopReqData.booking_amount)
        Timber.d("type id=======> " + addShopReqData.type_id)

        if (shopImageLocalPath != null)
            Timber.d("shop image path====> $shopImageLocalPath")

        Timber.d("family member dob=======> " + addShopReqData.family_member_dob)
        Timber.d("director name=======> " + addShopReqData.director_name)
        Timber.d("key person's name=======> " + addShopReqData.key_person_name)
        Timber.d("phone no=======> " + addShopReqData.phone_no)
        Timber.d("additional dob=======> " + addShopReqData.addtional_dob)
        Timber.d("additional doa=======> " + addShopReqData.addtional_doa)
        Timber.d("doctor family member dob=======> " + addShopReqData.doc_family_member_dob)
        Timber.d("specialization=======> " + addShopReqData.specialization)
        Timber.d("average patient count per day=======> " + addShopReqData.average_patient_per_day)
        Timber.d("category=======> " + addShopReqData.category)
        Timber.d("doctor address=======> " + addShopReqData.doc_address)
        Timber.d("doctor pincode=======> " + addShopReqData.doc_pincode)
        Timber.d("chambers or hospital under same headquarter=======> " + addShopReqData.is_chamber_same_headquarter)
        Timber.d("chamber related remarks=======> " + addShopReqData.is_chamber_same_headquarter_remarks)
        Timber.d("chemist name=======> " + addShopReqData.chemist_name)
        Timber.d("chemist name=======> " + addShopReqData.chemist_address)
        Timber.d("chemist pincode=======> " + addShopReqData.chemist_pincode)
        Timber.d("assistant name=======> " + addShopReqData.assistant_name)
        Timber.d("assistant contact no=======> " + addShopReqData.assistant_contact_no)
        Timber.d("assistant dob=======> " + addShopReqData.assistant_dob)
        Timber.d("assistant date of anniversary=======> " + addShopReqData.assistant_doa)
        Timber.d("assistant family dob=======> " + addShopReqData.assistant_family_dob)
        Timber.d("entity id=======> " + addShopReqData.entity_id)
        Timber.d("party status id=======> " + addShopReqData.party_status_id)
        Timber.d("retailer id=======> " + addShopReqData.retailer_id)
        Timber.d("dealer id=======> " + addShopReqData.dealer_id)
        Timber.d("beat id=======> " + addShopReqData.beat_id)
        Timber.d("actual_address=======> " + addShopReqData.actual_address)

        if (doc_degree != null)
            Timber.d("doctor degree image path=======> $doc_degree")
        Timber.d("assigned to shop id=======> " + addShopReqData.assigned_to_shop_id)
        Timber.d("==========================================")

        progress_wheel.spin()

        if (TextUtils.isEmpty(shopImageLocalPath) && TextUtils.isEmpty(doc_degree)) {
            val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                    repository.editShop(addShopReqData)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as AddShopResponse
                                Timber.d("Edit Shop : " + ", SHOP: " + addShopReqData.shop_name + ", RESPONSE:" + result.message)
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsEditUploaded(1, addShopReqData.shop_id)
                                    progress_wheel.stopSpinning()
                                    if (isSync) {
                                        (mContext as DashboardActivity).showSnackMessage("Synced successfully")
                                    }
                                    else {
                                        if (isAddressUpdated) {
                                            (mContext as DashboardActivity).showSnackMessage("Address updated successfully")

                                            initAdapter()
                                            sendNotification(addShopReqData.shop_id!!)
                                        } else {
                                            (mContext as DashboardActivity).showSnackMessage("Stage updated successfully")
                                            }
                                        //initAdapter()
                                    }

                                    val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
                                    getOwnShop(allShopList)

                                    if (beatId.isNotEmpty())
                                        list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(beatId)
                                    mNearByShopsListAdapter.updateAdapter(list)

                                    getAssignedPPListApi(addShopReqData.shop_id, true)
                                } else if (addShopResult.status == NetworkConstant.SESSION_MISMATCH) {
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).clearData()
                                    startActivity(Intent(mContext as DashboardActivity, LoginActivity::class.java))
                                    (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                    (mContext as DashboardActivity).finish()
                                } else {
                                    progress_wheel.stopSpinning()

                                    if (isSync) {
                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                        }
                                    else {
                                        if (isAddressUpdated) {
                                            (mContext as DashboardActivity).showSnackMessage("Address updated successfully")

                                            initAdapter()
                                            sendNotification(addShopReqData.shop_id!!)
                                        } else
                                            (mContext as DashboardActivity).showSnackMessage("Stage updated successfully")
                                        //initAdapter()
                                    }
                                }
                                BaseActivity.isApiInitiated = false
                            }, { error ->
                                error.printStackTrace()
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()

                                if (isSync) {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                    }
                                else {
                                    if (isAddressUpdated) {
                                        (mContext as DashboardActivity).showSnackMessage("Address updated successfully")

                                        initAdapter()
                                        sendNotification(addShopReqData.shop_id!!)
                                    } else {
                                        (mContext as DashboardActivity).showSnackMessage("Stage updated successfully")
                                    }
                                    //initAdapter()
                                }
                            })
            )
        }
        else {
            val repository = EditShopRepoProvider.provideEditShopRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addShopWithImage(addShopReqData, shopImageLocalPath, doc_degree, mContext)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as AddShopResponse
                                Timber.d("Edit Shop : " + ", SHOP: " + addShopReqData.shop_name + ", RESPONSE:" + result.message)
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsEditUploaded(1, addShopReqData.shop_id)
                                    progress_wheel.stopSpinning()
                                    if (isSync) {
                                        (mContext as DashboardActivity).showSnackMessage("Synced successfully")
                                        }
                                    else {
                                        if (isAddressUpdated) {
                                            (mContext as DashboardActivity).showSnackMessage("Address updated successfully")

                                            initAdapter()
                                            sendNotification(addShopReqData.shop_id!!)
                                        } else {
                                            (mContext as DashboardActivity).showSnackMessage("Stage updated successfully")
                                            }
                                        //initAdapter()
                                    }

                                    val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
                                    getOwnShop(allShopList)

                                    if (beatId.isNotEmpty())
                                        list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(beatId)
                                    mNearByShopsListAdapter.updateAdapter(list)

                                    getAssignedPPListApi(addShopReqData.shop_id, true)

                                } else if (addShopResult.status == NetworkConstant.SESSION_MISMATCH) {
                                    progress_wheel.stopSpinning()
                                    (mContext as DashboardActivity).clearData()
                                    startActivity(Intent(mContext as DashboardActivity, LoginActivity::class.java))
                                    (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                    (mContext as DashboardActivity).finish()
                                } else {
                                    progress_wheel.stopSpinning()

                                    if (isSync) {
                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                        }
                                    else {
                                        if (isAddressUpdated) {
                                            (mContext as DashboardActivity).showSnackMessage("Address updated successfully")

                                            initAdapter()
                                            sendNotification(addShopReqData.shop_id!!)
                                        } else {
                                            (mContext as DashboardActivity).showSnackMessage("Stage updated successfully")
                                            }
                                        //initAdapter()
                                    }
                                }
                                BaseActivity.isApiInitiated = false
                            }, { error ->
                                error.printStackTrace()
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()

                                if (isSync) {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                    }
                                else {
                                    if (isAddressUpdated) {
                                        (mContext as DashboardActivity).showSnackMessage("Address updated successfully")

                                        initAdapter()
                                        sendNotification(addShopReqData.shop_id!!)
                                    } else {
                                        (mContext as DashboardActivity).showSnackMessage("Stage updated successfully")
                                        }
                                    //initAdapter()
                                }
                            })
            )
        }
    }

    private fun openAddressUpdateDialog(addShopModelEntity: AddShopDBModelEntity, location: Location) {
        try {
            UpdateShopAddressDialog.getInstance(addShopModelEntity.shop_id, location, object : ShopAddressUpdateListener {
                override fun onAddedDataSuccess() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun getDialogInstance(mdialog: Dialog?) {
                }

                override fun onUpdateClick(address: AddShopDBModelEntity?) {
                   /* // update address work mantis id-0027309 begin
                    var shopObj = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(addShopModelEntity.shop_id)
                    if (shopObj != null) {
                        var address_ = LocationWizard.getAdressFromLatlng(mContext, address?.shopLat, address?.shopLong)
                        if (address_.contains("http"))
                            address_ = "Unknown"

                        AppDatabase.getDBInstance()?.addShopEntryDao()?.updateShopDetails(address?.shopLat,address?.shopLong,address?.address,address?.pinCode,address_,addShopModelEntity.shop_id)
                        shopObj = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(addShopModelEntity.shop_id)
                        if (AppUtils.isOnline(mContext)){
                            updateAddressAPICalling(shopObj)
                        }else{
                            (mContext as DashboardActivity).showSnackMessage("Address updated successfully")

                            initAdapter()
                            sendNotification(shopObj!!.shop_id!!)
                        }
                    }
                    return
                    // update address work mantis id-0027309 end
*/
                    /*if (AppUtils.isOnline(mContext))
                        callShopAddressUpdateApi(address!!)
                    else
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))*/


                    val shop = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(addShopModelEntity.shop_id)

                    if (shop != null) {
                        shop.shopLat = address?.shopLat
                        shop.shopLong = address?.shopLong
                        shop.address = address?.address
                        shop.pinCode = address?.pinCode

                        var address_ = LocationWizard.getAdressFromLatlng(mContext, address?.shopLat, address?.shopLong)
                        Timber.e("Actual Shop address (Update address)======> $address_")

                        if (address_.contains("http"))
                            address_ = "Unknown"

                        shop.actual_address = address_
                        shop.isEditUploaded = 0

                        // begin Suman 12-10-2023 mantis id 26874
                        shop.isUpdateAddressFromShopMaster = true
                        // end Suman 12-10-2023 mantis id 26874

                        AppDatabase.getDBInstance()?.addShopEntryDao()?.updateAddShop(shop)

                        convertToReqAndApiCall(shop, true)
                    }
                }

            }).show((mContext as DashboardActivity).supportFragmentManager, "UpdateShopAddressDialog")
        } catch (e: Exception) {
            //openAddressUpdateDialog(addShopModelEntity)
            e.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAddressAPICalling(shop: AddShopDBModelEntity?) {
        var updateAddrReq = UpdateAddrReq()
        updateAddrReq.user_id = Pref.user_id.toString()
        var updateAddressShop = UpdateAddressShop()
        updateAddressShop.shop_id = shop!!.shop_id
        updateAddressShop.shop_updated_lat = shop!!.shopLat.toString()
        updateAddressShop.shop_updated_long = shop!!.shopLong.toString()
        updateAddressShop.shop_updated_address = shop!!.address

        updateAddrReq.shop_list.add(updateAddressShop)

        progress_wheel.spin()
        val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.callUpdateAdressShopSaveApi(updateAddrReq)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->

                        progress_wheel.stopSpinning()
                        val resp = result as BaseResponse
                        if (resp.status == NetworkConstant.SUCCESS) {

                            (mContext as DashboardActivity).showSnackMessage("Address updated successfully")
                            initAdapter()
                            sendNotification(shop!!.shop_id!!)
                          /*  val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
                            getOwnShop(allShopList)

                            if (beatId.isNotEmpty())
                                list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(beatId)
                            mNearByShopsListAdapter.updateAdapter(list)

                            getAssignedPPListApi(shop.shop_id, true)*/

                        } else {
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        }
                    }, { error ->
                        progress_wheel.stopSpinning()

                        error.printStackTrace()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
    }

    private fun callShopAddressUpdateApi(addShopModelEntity: AddShopDBModelEntity) {
        val repository = ShopAddressUpdateRepoProvider.provideShopAddressUpdateRepo()
        progress_wheel.spin()
        val addressUpdateReq = AddressUpdateRequest()
        addressUpdateReq.user_id = Pref.user_id
        addressUpdateReq.shop_id = addShopModelEntity.shop_id
        addressUpdateReq.shop_lat = addShopModelEntity.shopLat.toString()
        addressUpdateReq.shop_long = addShopModelEntity.shopLong.toString()
        addressUpdateReq.shop_address = addShopModelEntity.address
        addressUpdateReq.isAddressUpdated = "1"
        addressUpdateReq.pincode = addShopModelEntity.pinCode

        BaseActivity.compositeDisposable.add(
                repository.getShopAddressUpdate(addressUpdateReq)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ response ->
                            progress_wheel.stopSpinning()
                            if (response.status == NetworkConstant.SUCCESS) {
//                                AppDatabase.getDBInstance()?.addShopEntryDao()?.updateIsAddressUpdated(addShopModelEntity.shop_id,true)
                                addShopModelEntity.isAddressUpdated = false
                                AppDatabase.getDBInstance()?.addShopEntryDao()?.updateShopDao(addShopModelEntity)
                                //(mContext as DashboardActivity).updateFence()
                                //mNearByShopsListAdapter.updateAdapter(AppDatabase.getDBInstance()!!.addShopEntryDao().all)
                                initAdapter()
                                sendNotification(addShopModelEntity.shop_id)
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            } else {
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                            /*AppDatabase.getDBInstance()?.addShopEntryDao()?.updateShopDao(addShopModelEntity)
                            mNearByShopsListAdapter.updateAdapter(AppDatabase.getDBInstance()!!.addShopEntryDao().all)
                            sendNotification(addShopModelEntity.shop_id)*/

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage("Unable to update address")
                        })
        )

    }

    private fun sendNotification(shopId: String) {
        val list = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(shopId, AppUtils.getCurrentDateForShopActi())
        var isDurationCalculated = false
        var shopName = ""
        if (list.isEmpty()) {
            if (AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopId) == null)
                return
            shopName = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopId).shopName
        } else {
            isDurationCalculated = list[0].isDurationCalculated
            shopName = list[0].shop_name!!
        }
        Timber.d("Geofence: ENTER : " + "ShopName : " + shopName + ",IS_DURATION_CALCULATED" + isDurationCalculated)
        if (isDurationCalculated)
            return

        Timber.d("Geofence: NearToShop : " + "ShopName : " + shopName)
        // Get an instance of the Notification manager
        val notification = NotificationUtils(getString(R.string.app_name), shopName, shopId, "")
        notification.CreateNotification(mContext, shopId)
//        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun callAddShopApi(addShop: AddShopRequestData, shop_imgPath: String?, shopList: MutableList<AddShopDBModelEntity>?,
                       isFromInitView: Boolean, degree_imgPath: String?) {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }
        if (isShopRegistrationInProcess)
            return

        progress_wheel.spin()

        isShopRegistrationInProcess = true

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
                                isShopRegistrationInProcess = false

                                when (addShopResult.status) {
                                    NetworkConstant.SUCCESS -> {
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)

                                        if (isFromInitView)
                                            (mContext as DashboardActivity).showSnackMessage("Synced successfully")

                                        doAsync {
                                            val resultAs = runLongTask(addShop.shop_id)
                                            uiThread {
                                                if (resultAs == true) {
                                                    if (isFromInitView) {
                                                        if (mNearByShopsListAdapter != null) {
                                                            val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
                                                            getOwnShop(allShopList)

                                                            if (beatId.isNotEmpty())
                                                                list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(beatId)
                                                            mNearByShopsListAdapter.updateAdapter(list)
                                                        }
                                                        progress_wheel.stopSpinning()
                                                        getAssignedPPListApi(addShop.shop_id, isFromInitView)
                                                    } else {
                                                        i++
                                                        if (i < shopList?.size!!) {
                                                            syncShop(shopList[i], shopList)
                                                        } else {
                                                            i = 0
                                                            progress_wheel.stopSpinning()
                                                            getAssignedPPListApi(addShop.shop_id, isFromInitView)

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        /*if (isFromInitView) {
                                                                                progress_wheel.stopSpinning()
                                                                                getAssignedPPListApi(addShop.shop_id)
                                                                            }*/

                                    }
                                    NetworkConstant.DUPLICATE_SHOP_ID -> {
                                        Timber.d("DuplicateShop : " + ", SHOP: " + addShop.shop_name)
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)
                                        //progress_wheel.stopSpinning()
                                        if (isFromInitView)
                                            (mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)

                                        if (AppDatabase.getDBInstance()!!.addShopEntryDao().getDuplicateShopData(addShop.owner_contact_no).size > 0) {
                                            AppDatabase.getDBInstance()!!.addShopEntryDao().deleteShopById(addShop.shop_id)
                                            AppDatabase.getDBInstance()!!.shopActivityDao().deleteShopByIdAndDate(addShop.shop_id!!, AppUtils.getCurrentDateForShopActi())
                                        }
                                        doAsync {
                                            val resultAs = runLongTask(addShop.shop_id)
                                            uiThread {
                                                if (resultAs == true) {
                                                    if (isFromInitView) {
                                                        if (mNearByShopsListAdapter != null) {
                                                            val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
                                                            getOwnShop(allShopList)

                                                            if (beatId.isNotEmpty())
                                                                list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(beatId)
                                                            mNearByShopsListAdapter.updateAdapter(list)
                                                        }
                                                        progress_wheel.stopSpinning()
                                                        getAssignedPPListApi(addShop.shop_id, isFromInitView)
                                                    } else {
                                                        i++
                                                        if (i < shopList?.size!!) {
                                                            syncShop(shopList[i], shopList)
                                                        } else {
                                                            i = 0
                                                            progress_wheel.stopSpinning()
                                                            getAssignedPPListApi(addShop.shop_id, isFromInitView)

                                                        }
                                                    }

                                                }
                                            }
                                        }
                                        //getAssignedPPListApi(addShop.shop_id)
                                    }
                                    else -> {
                                        progress_wheel.stopSpinning()
                                        (mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)


                                    }
                                }

                                /*if (!AppDatabase.getDBInstance()!!.addShopEntryDao().getUnSyncedShops(false).isEmpty())
                                syncShopList()*/

                            }, { error ->
                                error.printStackTrace()
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                isShopRegistrationInProcess = false
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
                                isShopRegistrationInProcess = false

                                when (addShopResult.status) {
                                    NetworkConstant.SUCCESS -> {
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)

                                        if (isFromInitView)
                                            (mContext as DashboardActivity).showSnackMessage("Synced successfully")

                                        doAsync {
                                            val resultAs = runLongTask(addShop.shop_id)
                                            uiThread {
                                                if (resultAs == true) {
                                                    if (isFromInitView) {
                                                        if (mNearByShopsListAdapter != null) {
                                                            val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
                                                            getOwnShop(allShopList)

                                                            if (beatId.isNotEmpty())
                                                                list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(beatId)
                                                            mNearByShopsListAdapter.updateAdapter(list)
                                                        }
                                                        progress_wheel.stopSpinning()
                                                        getAssignedPPListApi(addShop.shop_id, isFromInitView)
                                                    } else {
                                                        i++
                                                        if (i < shopList?.size!!) {
                                                            syncShop(shopList[i], shopList)
                                                        } else {
                                                            i = 0
                                                            progress_wheel.stopSpinning()
                                                            getAssignedPPListApi(addShop.shop_id, isFromInitView)

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        /*if (isFromInitView) {
                                                                                progress_wheel.stopSpinning()
                                                                                getAssignedPPListApi(addShop.shop_id)
                                                                            }*/

                                    }
                                    NetworkConstant.DUPLICATE_SHOP_ID -> {
                                        Timber.d("DuplicateShop : " + ", SHOP: " + addShop.shop_name)
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)
                                        //progress_wheel.stopSpinning()
                                        if (isFromInitView)
                                            (mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)

                                        if (AppDatabase.getDBInstance()!!.addShopEntryDao().getDuplicateShopData(addShop.owner_contact_no).size > 0) {
                                            AppDatabase.getDBInstance()!!.addShopEntryDao().deleteShopById(addShop.shop_id)
                                            AppDatabase.getDBInstance()!!.shopActivityDao().deleteShopByIdAndDate(addShop.shop_id!!, AppUtils.getCurrentDateForShopActi())
                                        }
                                        doAsync {
                                            val resultAs = runLongTask(addShop.shop_id)
                                            uiThread {
                                                if (resultAs == true) {
                                                    if (isFromInitView) {
                                                        if (mNearByShopsListAdapter != null) {
                                                            val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
                                                            getOwnShop(allShopList)

                                                            if (beatId.isNotEmpty())
                                                                list = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(beatId)
                                                            mNearByShopsListAdapter.updateAdapter(list)
                                                        }
                                                        progress_wheel.stopSpinning()
                                                        getAssignedPPListApi(addShop.shop_id, isFromInitView)
                                                    } else {
                                                        i++
                                                        if (i < shopList?.size!!) {
                                                            syncShop(shopList[i], shopList)
                                                        } else {
                                                            i = 0
                                                            progress_wheel.stopSpinning()
                                                            getAssignedPPListApi(addShop.shop_id, isFromInitView)

                                                        }
                                                    }

                                                }
                                            }
                                        }
                                        //getAssignedPPListApi(addShop.shop_id)
                                    }
                                    else -> {
                                        progress_wheel.stopSpinning()
                                        (mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)


                                    }
                                }

                                /*if (!AppDatabase.getDBInstance()!!.addShopEntryDao().getUnSyncedShops(false).isEmpty())
                                syncShopList()*/

                            }, { error ->
                                error.printStackTrace()
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                                isShopRegistrationInProcess = false
                                if (error != null)
                                    Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + error.localizedMessage)
                            })
            )
        }

    }


    private fun getAssignedPPListApi(shop_id: String?, isFromInitView: Boolean) {

        // start 8.0 NearByShopsListFragment AppV 4.0.7 saheli 08-06-2023 0026307 mantis  Play store console report issues
        try{
            if (!isFromInitView)
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.wait_msg), 1000)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        // end 8.0 NearByShopsListFragment AppV 4.0.7 saheli 08-06-2023 0026307 mantis  Play store console report issues


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
                                            getAssignedDDListApi(shop_id, isFromInitView)
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getAssignedDDListApi(shop_id, isFromInitView)
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                getAssignedDDListApi(shop_id, isFromInitView)
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            getAssignedDDListApi(shop_id, isFromInitView)
                        })
        )
    }

    private fun getAssignedDDListApi(shop_id: String?, isFromInitView: Boolean) {
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
                                            getAssignedToShopApi(shop_id, isFromInitView)
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getAssignedToShopApi(shop_id, isFromInitView)
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                getAssignedToShopApi(shop_id, isFromInitView)
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            getAssignedToShopApi(shop_id, isFromInitView)
                        })
        )
    }

    private fun getAssignedToShopApi(shop_id: String?, isFromInitView: Boolean) {
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
                                        if (!isFromInitView)
                                            getShopListApi(isFromInitView)
                                    }
                                }
                            }
                            else {
                                progress_wheel.stopSpinning()
                                if (!isFromInitView)
                                    getShopListApi(isFromInitView)
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            if (!isFromInitView)
                                getShopListApi(isFromInitView)
                        })
        )
    }

    private fun doNothing() {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initiatePopupWindow(view: View, position: Int) {
        val popup = PopupWindow(mContext)
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
            call_iv.setImageDrawable(mContext.getDrawable(R.drawable.ic_registered_shop_call_select))

            order_iv.setImageDrawable(mContext.getDrawable(R.drawable.ic_registered_shop_add_order_deselect))
            direction_iv.setImageDrawable(mContext.getDrawable(R.drawable.ic_registered_shop_direction_deselect))
            order_tv.setTextColor(mContext.getColor(R.color.login_txt_color))
            direction_tv.setTextColor(mContext.getColor(R.color.login_txt_color))

            call_tv.setTextColor(mContext.getColor(R.color.colorPrimary))
            popup.dismiss()
            IntentActionable.initiatePhoneCall(mContext, list[position].ownerContactNumber)
        })

        direction_ll.setOnClickListener(View.OnClickListener {
            direction_iv.setImageDrawable(mContext.getDrawable(R.drawable.ic_registered_shop_direction_select))

            call_iv.setImageDrawable(mContext.getDrawable(R.drawable.ic_registered_shop_call_deselect))
            order_iv.setImageDrawable(mContext.getDrawable(R.drawable.ic_registered_shop_add_order_deselect))
            call_tv.setTextColor(mContext.getColor(R.color.login_txt_color))
            order_tv.setTextColor(mContext.getColor(R.color.login_txt_color))

            direction_tv.setTextColor(mContext.getColor(R.color.colorPrimary))
            popup.dismiss()
            (mContext as DashboardActivity).openLocationWithTrack()

        })

        add_order_ll.setOnClickListener(View.OnClickListener {
            order_iv.setImageDrawable(mContext.getDrawable(R.drawable.ic_registered_shop_add_order_select))

            call_iv.setImageDrawable(mContext.getDrawable(R.drawable.ic_registered_shop_call_deselect))
            direction_iv.setImageDrawable(mContext.getDrawable(R.drawable.ic_registered_shop_direction_deselect))
            call_tv.setTextColor(mContext.getColor(R.color.login_txt_color))
            direction_tv.setTextColor(mContext.getColor(R.color.login_txt_color))

            order_tv.setTextColor(mContext.getColor(R.color.colorPrimary))
            popup.dismiss()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.functionality_disabled))

        })

        popup.setBackgroundDrawable(BitmapDrawable())
        popup.showAsDropDown(view)
        popup.update()

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            100 -> {
                sortAlphabatically()
                floating_fab.close(true)
                programFab1.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
                programFab2.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab3.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab1.setImageResource(R.drawable.ic_tick_float_icon)
                programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
                programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)


            }
            101 -> {
                sortByVisitDate()
                floating_fab.close(true)
                programFab1.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab2.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
                programFab3.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab1.setImageResource(R.drawable.ic_tick_float_icon_gray)
                programFab2.setImageResource(R.drawable.ic_tick_float_icon)
                programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)
            }
            102 -> {
                sortByMostVisited()
                floating_fab.close(true)
                programFab1.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab2.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab3.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
                programFab1.setImageResource(R.drawable.ic_tick_float_icon_gray)
                programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
                programFab3.setImageResource(R.drawable.ic_tick_float_icon)
            }
        }
    }

    fun sortAlphabatically() {

        if (!setListVisiBility())
            return

        Collections.sort(list, object : Comparator<AddShopDBModelEntity> {
            override fun compare(o1: AddShopDBModelEntity, o2: AddShopDBModelEntity): Int {
                return o1.shopName.toUpperCase().compareTo(o2.shopName.toUpperCase())
            }
        })
        if (mNearByShopsListAdapter != null)
            mNearByShopsListAdapter.notifyDataSetChanged()
    }

    fun sortByVisitDate() {

        if (!setListVisiBility())
            return

        Collections.sort(list, object : Comparator<AddShopDBModelEntity> {
            override fun compare(o1: AddShopDBModelEntity, o2: AddShopDBModelEntity): Int {
                return (AppUtils.getLongTimeStampFromDate(o1.lastVisitedDate)).compareTo(AppUtils.getLongTimeStampFromDate(o2.lastVisitedDate))
            }
        })

        Collections.reverse(list)

        if (mNearByShopsListAdapter != null)
            mNearByShopsListAdapter.notifyDataSetChanged()

    }

    fun sortByMostVisited() {

        if (!setListVisiBility())
            return

        Collections.sort(list, object : Comparator<AddShopDBModelEntity> {
            override fun compare(o1: AddShopDBModelEntity, o2: AddShopDBModelEntity): Int {
                return extractInt(o1.totalVisitCount) - extractInt(o2.totalVisitCount)
            }

            internal fun extractInt(s: String): Int {
                val num = s.replace("\\D".toRegex(), "")
                // return 0 if no digits found
                return if (num.isEmpty()) 0 else Integer.parseInt(num)
            }
        })

        Collections.reverse(list)

        if (mNearByShopsListAdapter != null)
            mNearByShopsListAdapter.notifyDataSetChanged()


    }


    private fun syncShopList() {
        val shopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getUnSyncedShops(false)

        if (shopList.isEmpty())
            return

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

        try{
            var uniqKeyObj=AppDatabase.getDBInstance()!!.shopActivityDao().getNewShopActivityKey(mAddShopDBModelEntity.shop_id,false)
            addShopData.shop_revisit_uniqKey=uniqKeyObj?.shop_revisit_uniqKey!!
        }catch (ex:Exception){
            addShopData.shop_revisit_uniqKey=""
        }


        addShopData.project_name = mAddShopDBModelEntity.project_name
        addShopData.landline_number = mAddShopDBModelEntity.landline_number
        addShopData.agency_name = mAddShopDBModelEntity.agency_name


        addShopData.alternateNoForCustomer = mAddShopDBModelEntity.alternateNoForCustomer
        addShopData.whatsappNoForCustomer = mAddShopDBModelEntity.whatsappNoForCustomer

        // duplicate shop api call
        addShopData.isShopDuplicate=mAddShopDBModelEntity.isShopDuplicate

        addShopData.purpose=mAddShopDBModelEntity.purpose
//start AppV 4.2.2 tufan    20/09/2023 FSSAI Lic No Implementation 26813
        try {
            addShopData.FSSAILicNo = mAddShopDBModelEntity.FSSAILicNo
        }catch (ex:Exception){
            ex.printStackTrace()
            addShopData.FSSAILicNo = ""
        }
//end AppV 4.2.2 tufan    20/09/2023 FSSAI Lic No Implementation 26813

        addShopData.GSTN_Number=mAddShopDBModelEntity.gstN_Number
        addShopData.ShopOwner_PAN=mAddShopDBModelEntity.shopOwner_PAN


        callAddShopApi(addShopData, mAddShopDBModelEntity.shopImageLocalPath, shopList, true,
                mAddShopDBModelEntity.doc_degree)
        //callAddShopApi(addShopData, "")

    }

    public fun getFile(): File {
        var bm: Bitmap? = null
        if (bm == null) {
            val bitmap = (iv_nearbyImage.getDrawable() as BitmapDrawable).bitmap
            bm = bitmap
        }
        val bytes = ByteArrayOutputStream()
        bm!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

        /*var destination = File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg")*/

        var destination = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                System.currentTimeMillis().toString() + ".jpg")

        val camera_image_path = destination?.absolutePath
        val fo: FileOutputStream
        try {
            destination?.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return destination
    }

    fun refreshShopList() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val shopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getUnSyncedShops(false)

        if (shopList != null && shopList.isNotEmpty()) {
            i = 0
            syncShop(shopList[i], shopList)

        } else {
            getAssignedPPListApi("", false)
            //getShopListApi(false)
        }
    }

    // 5.0 NearByShopsListFragment AppV 4.0.6 Suman 03-02-2023 updateModifiedShop + sendModifiedShopList  for shop update mantis 25624
    fun checkModifiedShop(){
        println("checkAPIcalling>>>>>")
        if(!AppUtils.isOnline(mContext)){
            Toaster.msgShort(mContext,"No Internet connection")
            return
        }
        val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
        BaseActivity.compositeDisposable.add(
            repository.checkModifiedShopList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as ShopModifiedListResponse
                    if (response.status==NetworkConstant.SUCCESS){
                        if(response.modified_shop_list.size>0){
                            updateModifiedShop(response.modified_shop_list)
                        }else{
                            refreshShopList()
                        }
                    }else{
                        refreshShopList()
                    }
                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    refreshShopList()

                })
        )
    }

    // 5.0 NearByShopsListFragment AppV 4.0.6 Suman 03-02-2023 updateModifiedShop + sendModifiedShopList  for shop update mantis 25624
    fun updateModifiedShop(shop_list:ArrayList<ShopData>){
        doAsync {
            try{
                for(i in 0..shop_list.size-1){
                    var obj =shop_list.get(i)

                    AppDatabase.getDBInstance()?.addShopEntryDao()?.updateShopDtlsAll(obj.shop_id,obj.shop_name,obj.address,obj.pin_code,obj.owner_name,obj.owner_contact_no,
                        obj.owner_email,obj.shop_lat,obj.shop_long,obj.dob,obj.date_aniversary,obj.last_visit_date,obj.total_visit_count,obj.type,obj.type_id,
                        obj.assigned_to_pp_id,obj.assigned_to_dd_id,obj.amount,obj.entity_code,obj.area_id,obj.model_id,obj.lead_id,obj.funnel_stage_id,obj.stage_id,
                        obj.party_status_id,obj.retailer_id,obj.beat_id,obj.assigned_to_shop_id,obj.agency_name,obj.GSTN_Number,obj.ShopOwner_PAN,
                        obj.project_name,obj.dealer_id,obj.account_holder,obj.account_no,obj.bank_name,obj.ifsc_code,obj.upi)
                }
            }catch (ex:Exception){
                ex.printStackTrace()
            }
            uiThread {
                sendModifiedShopList(shop_list)
            }
        }
    }

    fun sendModifiedShopList(shop_list:ArrayList<ShopData>){
        var shopIdL : ArrayList<ShopIdModified> = ArrayList()
        for(i in 0..shop_list.size-1){
            shopIdL.add(ShopIdModified(shop_list.get(i).shop_id!!))
        }

        var obj = ShopModifiedUpdateList()
        obj.user_id = Pref.user_id!!
        obj.shop_modified_list = shopIdL!!

        val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
        BaseActivity.compositeDisposable.add(
            repository.updateModifiedShopList(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as BaseResponse
                    if (response.status==NetworkConstant.SUCCESS){
                        refreshShopList()
                    }else{
                        refreshShopList()
                    }
                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    refreshShopList()
                })
        )
    }

    private fun syncShop(addShopDBModelEntity: AddShopDBModelEntity, shopList: MutableList<AddShopDBModelEntity>) {
        val addShopData = AddShopRequestData()
        val mAddShopDBModelEntity = addShopDBModelEntity
        addShopData.run {
            session_token = Pref.session_token
            address = mAddShopDBModelEntity.address
            owner_contact_no = mAddShopDBModelEntity.ownerContactNumber
            owner_email = mAddShopDBModelEntity.ownerEmailId
            owner_name = mAddShopDBModelEntity.ownerName
            pin_code = mAddShopDBModelEntity.pinCode
            shop_lat = mAddShopDBModelEntity.shopLat.toString()
            shop_long = mAddShopDBModelEntity.shopLong.toString()
            shop_name = mAddShopDBModelEntity.shopName.toString()
            type = mAddShopDBModelEntity.type.toString()
            shop_id = mAddShopDBModelEntity.shop_id
            user_id = Pref.user_id
            assigned_to_dd_id = mAddShopDBModelEntity.assigned_to_dd_id
            assigned_to_pp_id = mAddShopDBModelEntity.assigned_to_pp_id
            added_date = mAddShopDBModelEntity.added_date
            amount = mAddShopDBModelEntity.amount
            area_id = mAddShopDBModelEntity.area_id
            model_id = mAddShopDBModelEntity.model_id
            primary_app_id = mAddShopDBModelEntity.primary_app_id
            secondary_app_id = mAddShopDBModelEntity.secondary_app_id
            lead_id = mAddShopDBModelEntity.lead_id
            stage_id = mAddShopDBModelEntity.stage_id
            funnel_stage_id = mAddShopDBModelEntity.funnel_stage_id
            booking_amount = mAddShopDBModelEntity.booking_amount
            type_id = mAddShopDBModelEntity.type_id

            director_name = mAddShopDBModelEntity.director_name
            key_person_name = mAddShopDBModelEntity.person_name
            phone_no = mAddShopDBModelEntity.person_no

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.family_member_dob))
                family_member_dob = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.family_member_dob)

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.add_dob))
                addtional_dob = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.add_dob)

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.add_doa))
                addtional_doa = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.add_doa)

            specialization = mAddShopDBModelEntity.specialization
            category = mAddShopDBModelEntity.category
            doc_address = mAddShopDBModelEntity.doc_address
            doc_pincode = mAddShopDBModelEntity.doc_pincode
            is_chamber_same_headquarter = mAddShopDBModelEntity.chamber_status.toString()
            is_chamber_same_headquarter_remarks = mAddShopDBModelEntity.remarks
            chemist_name = mAddShopDBModelEntity.chemist_name
            chemist_address = mAddShopDBModelEntity.chemist_address
            chemist_pincode = mAddShopDBModelEntity.chemist_pincode
            assistant_contact_no = mAddShopDBModelEntity.assistant_no
            average_patient_per_day = mAddShopDBModelEntity.patient_count
            assistant_name = mAddShopDBModelEntity.assistant_name

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.doc_family_dob))
                doc_family_member_dob = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.doc_family_dob)

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.assistant_dob))
                assistant_dob = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.assistant_dob)

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.assistant_doa))
                assistant_doa = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.assistant_doa)

            if (!TextUtils.isEmpty(mAddShopDBModelEntity.assistant_family_dob))
                assistant_family_dob = AppUtils.changeAttendanceDateFormatToCurrent(mAddShopDBModelEntity.assistant_family_dob)

            addShopData.entity_id = mAddShopDBModelEntity.entity_id
            addShopData.party_status_id = mAddShopDBModelEntity.party_status_id
            addShopData.retailer_id = mAddShopDBModelEntity.retailer_id
            addShopData.dealer_id = mAddShopDBModelEntity.dealer_id
            addShopData.beat_id = mAddShopDBModelEntity.beat_id
            addShopData.assigned_to_shop_id = mAddShopDBModelEntity.assigned_to_shop_id
            addShopData.actual_address = mAddShopDBModelEntity.actual_address

            var uniqKeyObj=AppDatabase.getDBInstance()!!.shopActivityDao().getNewShopActivityKey(mAddShopDBModelEntity.shop_id,false)
            addShopData.shop_revisit_uniqKey=uniqKeyObj?.shop_revisit_uniqKey!!

            addShopData.project_name = mAddShopDBModelEntity.project_name
            addShopData.landline_number = mAddShopDBModelEntity.landline_number
            addShopData.agency_name = mAddShopDBModelEntity.agency_name

            addShopData.alternateNoForCustomer = mAddShopDBModelEntity.alternateNoForCustomer
            addShopData.whatsappNoForCustomer = mAddShopDBModelEntity.whatsappNoForCustomer

            // duplicate shop api call
            addShopData.isShopDuplicate=mAddShopDBModelEntity.isShopDuplicate
            addShopData.purpose=mAddShopDBModelEntity.purpose


            addShopData
        }.let {
            callAddShopApi(it, mAddShopDBModelEntity.shopImageLocalPath, shopList, false, mAddShopDBModelEntity.doc_degree)
        }
    }

    fun onRequestPermission(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        collectionDialog?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun setImage(file: File) {
        collectionDialog?.setImage(file)
    }

    fun dialogOpenQa(shopId:String) {
        var qsAnsByShopIDList=AppDatabase.getDBInstance()?.questionSubmitDao()?.getQsAnsByShopIDToInt(shopId!!) as ArrayList<QuestionSubmit>

        if(qsAnsByShopIDList==null || qsAnsByShopIDList.isEmpty()) {
            Toaster.msgShort(mContext, "No List Found")
            return
        }
        quesAnsList.clear()
        quesAnsListTemp.clear()

        for(l in 0..qsAnsByShopIDList.size-1){
            if(qsAnsByShopIDList.get(l).answer!!){
                quesAnsList.add(AddShopFragment.QuestionAns(qsAnsByShopIDList.get(l).question_id!!, "1"))
            }else{
                quesAnsList.add(AddShopFragment.QuestionAns(qsAnsByShopIDList.get(l).question_id!!, "0"))
            }
        }
        rv_qaList = AppDatabase.getDBInstance()?.questionMasterDao()?.getAll() as ArrayList<QuestionEntity>
        if(rv_qaList==null || rv_qaList.isEmpty()) {
            Toaster.msgShort(mContext, "No List Found")
            return
        }

        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_qa)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_qa_headerTV) as AppCustomTextView
        val  rv_QAList = simpleDialog.findViewById(R.id.rv_qa_list) as RecyclerView
        rv_QAList.layoutManager = LinearLayoutManager(mContext)

        for(i in 0..rv_qaList.size-1){
            try{
                var obj=AppDatabase.getDBInstance()?.questionSubmitDao()?.getQsAnsByShopID(shopId!!,rv_qaList.get(i).question_id!!) as QuestionSubmit
                quesAnsListTemp.add(obj)
            }catch (ex:Exception){

            }

        }

        if(quesAnsListTemp.size!=rv_qaList.size){
            (mContext as DashboardActivity).showSnackMessage("Invalid Question-Ans")
            return
        }

        quesAnsList.clear()
        for(l in 0..quesAnsListTemp.size-1){
            if(quesAnsListTemp.get(l).answer!!){
                quesAnsList.add(AddShopFragment.QuestionAns(quesAnsListTemp.get(l).question_id!!, "1"))
            }else{
                quesAnsList.add(AddShopFragment.QuestionAns(quesAnsListTemp.get(l).question_id!!, "0"))
            }
        }

        adapterqaList = AdapterQuestionList(mContext,quesAnsList,rv_qaList,false,object : QaOnCLick {
            override fun getQaID(qaID: String, ans: String) {
                AppDatabase.getDBInstance()?.questionSubmitDao()?.updateAnswerByQueAndShopIdNew(ans,qaID,shopId,false)
            }
        })
        rv_QAList.adapter = adapterqaList
        dialogHeader.text = "Hi " + Pref.user_name!! + "!"
        val dialogYes = simpleDialog.findViewById(R.id.dialog_qa_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()
    }

    private fun voiceAttendanceMsg(msg: String) {
        if (Pref.isVoiceEnabledForAttendanceSubmit) {
            val speechStatus = (mContext as DashboardActivity).textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
            if (speechStatus == TextToSpeech.ERROR)
                Log.e("Add Day Start", "TTS error in converting Text to Speech!");
        }
    }

    // 2.0 NearByShopsListFragment AppV 4.0.6   Contact Multi Api called Add & Update
    private fun callAddMultiContactapi(shop_idSel:String) {
        var shopListSubmitReq : multiContactRequestData = multiContactRequestData()
        if(shopExtraContactList.size>0){
            for(o in 0..shopExtraContactList.size-1){
                shopExtraContactList.get(o).shop_id = shop_idSel
                AppDatabase.getDBInstance()?.shopExtraContactDao()?.insert(shopExtraContactList.get(o))
            }
            // new code for multi contact response
            var extraContL = AppDatabase.getDBInstance()?.shopExtraContactDao()?.getExtraContListByShopID(shop_idSel) as ArrayList<ShopExtraContactEntity>
            var extraContResponseObj : ShopExtraContactReq = ShopExtraContactReq()
            extraContResponseObj.shop_id = shop_idSel

            for(a in 0..extraContL.size-1){
                if(a==0){
                    extraContResponseObj.apply {
                        contact_name1 = extraContL.get(a).contact_name.toString()
                        contact_number1 = extraContL.get(a).contact_number.toString()
                        contact_email1 = extraContL.get(a).contact_email.toString()
                        contact_doa1 = extraContL.get(a).contact_doa.toString()
                        contact_dob1 = extraContL.get(a).contact_doa.toString()
                    }
                }
               if(a==1){
                    extraContResponseObj.apply {
                        contact_name2 = extraContL.get(a).contact_name.toString()
                        contact_number2 = extraContL.get(a).contact_number.toString()
                        contact_email2 = extraContL.get(a).contact_email.toString()
                        contact_doa2 = extraContL.get(a).contact_doa.toString()
                        contact_dob2 = extraContL.get(a).contact_doa.toString()
                    }
                }
                if(a==2){
                    extraContResponseObj.apply {
                        contact_name3 = extraContL.get(a).contact_name.toString()
                        contact_number3 = extraContL.get(a).contact_number.toString()
                        contact_email3 = extraContL.get(a).contact_email.toString()
                        contact_doa3 = extraContL.get(a).contact_doa.toString()
                        contact_dob3 = extraContL.get(a).contact_doa.toString()
                    }
                }
                if(a==3){
                    extraContResponseObj.apply {
                        contact_name4 = extraContL.get(a).contact_name.toString()
                        contact_number4 = extraContL.get(a).contact_number.toString()
                        contact_email4 = extraContL.get(a).contact_email.toString()
                        contact_doa4 = extraContL.get(a).contact_doa.toString()
                        contact_dob4 = extraContL.get(a).contact_doa.toString()
                    }
                }
                if(a==4){
                    extraContResponseObj.apply {
                        contact_name5 = extraContL.get(a).contact_name.toString()
                        contact_number5 = extraContL.get(a).contact_number.toString()
                        contact_email5 = extraContL.get(a).contact_email.toString()
                        contact_doa5 = extraContL.get(a).contact_doa.toString()
                        contact_dob5 = extraContL.get(a).contact_doa.toString()
                    }
                }
                if(a==5){
                    extraContResponseObj.apply {
                        contact_name6 = extraContL.get(a).contact_name.toString()
                        contact_number6 = extraContL.get(a).contact_number.toString()
                        contact_email6 = extraContL.get(a).contact_email.toString()
                        contact_doa6 = extraContL.get(a).contact_doa.toString()
                        contact_dob6 = extraContL.get(a).contact_doa.toString()
                    }
                }
            }


            shopListSubmitReq.user_id = Pref.user_id!!
            shopListSubmitReq.session_token = Pref.session_token!!
            shopListSubmitReq.shop_list.add(extraContResponseObj)

        }

        if(!AppUtils.isOnline(mContext)){
            Toaster.msgShort(mContext,"Update successfully")
            return
        }

        val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
        BaseActivity.compositeDisposable.add(
            repository.addMutiContact(shopListSubmitReq)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                        val addmutliContactResult = result as BaseResponse
                        if (addmutliContactResult.status==NetworkConstant.SUCCESS){
                            val obj = shopListSubmitReq.shop_list.get(0)
                            if(obj.contact_serial1.equals("1") && !obj.contact_name1.equals("")){
                                AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial1)
                            }
                            if(obj.contact_serial2.equals("2") && !obj.contact_name2.equals("")){
                                AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial2)
                            }
                            if(obj.contact_serial3.equals("3") && !obj.contact_name3.equals("")){
                                AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial3)
                            }
                            if(obj.contact_serial4.equals("4") && !obj.contact_name4.equals("")){
                                AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial4)
                            }
                            if(obj.contact_serial5.equals("5") && !obj.contact_name5.equals("")){
                                AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial5)
                            }
                            if(obj.contact_serial6.equals("6") && !obj.contact_name6.equals("")){
                                AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial6)
                            }
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(addmutliContactResult.message!!)
                        }
                    }, { error ->
                        error.printStackTrace()
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage("Error added contact")

                    })
                )
}

    private fun callUpdateMultiContactapi(shop_idSel:String) {
        var shopListSubmitReq : multiContactRequestData = multiContactRequestData()
        if(shopExtraContactList.size>0){
            for(o in 0..shopExtraContactList.size-1){
                shopExtraContactList.get(o).shop_id = shop_idSel
                var chkL = AppDatabase.getDBInstance()?.shopExtraContactDao()?.getExtraContListByShopIDContactSl(shop_idSel,shopExtraContactList.get(o).contact_serial!!) as ArrayList<ShopExtraContactEntity>
                if(chkL.size==0){
                    AppDatabase.getDBInstance()?.shopExtraContactDao()?.insert(shopExtraContactList.get(o))
                }
            }
            // new code for multi contact response
            var extraContL = AppDatabase.getDBInstance()?.shopExtraContactDao()?.getExtraContListByShopID(shop_idSel) as ArrayList<ShopExtraContactEntity>
            var extraContResponseObj : ShopExtraContactReq = ShopExtraContactReq()
            extraContResponseObj.shop_id = shop_idSel

            for(a in 0..extraContL.size-1){
                if(a==0){
                    extraContResponseObj.apply {
                        contact_name1 = extraContL.get(a).contact_name.toString()
                        contact_number1 = extraContL.get(a).contact_number.toString()
                        contact_email1 = extraContL.get(a).contact_email.toString()
                        contact_doa1 = extraContL.get(a).contact_doa.toString()
                        contact_dob1 = extraContL.get(a).contact_dob.toString()
                    }
                }
                if(a==1){
                    extraContResponseObj.apply {
                        contact_name2 = extraContL.get(a).contact_name.toString()
                        contact_number2 = extraContL.get(a).contact_number.toString()
                        contact_email2 = extraContL.get(a).contact_email.toString()
                        contact_doa2 = extraContL.get(a).contact_doa.toString()
                        contact_dob2 = extraContL.get(a).contact_dob.toString()
                    }
                }
                if(a==2){
                    extraContResponseObj.apply {
                        contact_name3 = extraContL.get(a).contact_name.toString()
                        contact_number3 = extraContL.get(a).contact_number.toString()
                        contact_email3 = extraContL.get(a).contact_email.toString()
                        contact_doa3 = extraContL.get(a).contact_doa.toString()
                        contact_dob3 = extraContL.get(a).contact_dob.toString()
                    }
                }
                if(a==3){
                    extraContResponseObj.apply {
                        contact_name4 = extraContL.get(a).contact_name.toString()
                        contact_number4 = extraContL.get(a).contact_number.toString()
                        contact_email4 = extraContL.get(a).contact_email.toString()
                        contact_doa4 = extraContL.get(a).contact_doa.toString()
                        contact_dob4 = extraContL.get(a).contact_dob.toString()
                    }
                }
                if(a==4){
                    extraContResponseObj.apply {
                        contact_name5 = extraContL.get(a).contact_name.toString()
                        contact_number5 = extraContL.get(a).contact_number.toString()
                        contact_email5 = extraContL.get(a).contact_email.toString()
                        contact_doa5 = extraContL.get(a).contact_doa.toString()
                        contact_dob5 = extraContL.get(a).contact_dob.toString()
                    }
                }
                if(a==5){
                    extraContResponseObj.apply {
                        contact_name6 = extraContL.get(a).contact_name.toString()
                        contact_number6 = extraContL.get(a).contact_number.toString()
                        contact_email6 = extraContL.get(a).contact_email.toString()
                        contact_doa6 = extraContL.get(a).contact_doa.toString()
                        contact_dob6 = extraContL.get(a).contact_dob.toString()
                    }
                }
            }
            shopListSubmitReq.user_id = Pref.user_id!!
            shopListSubmitReq.session_token = Pref.session_token!!
            shopListSubmitReq.shop_list.add(extraContResponseObj)

        }

        if(!AppUtils.isOnline(mContext)){
            Toaster.msgShort(mContext,"Update successfully")
            return
        }

        val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
        BaseActivity.compositeDisposable.add(
            repository.updateMutiContact(shopListSubmitReq)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val addmutliContactResult = result as BaseResponse
                    if (addmutliContactResult.status == NetworkConstant.SUCCESS){

                        val obj = shopListSubmitReq.shop_list.get(0)
                        if(obj.contact_serial1.equals("1") && !obj.contact_name1.equals("")){
                            AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial1)
                        }
                        if(obj.contact_serial2.equals("2") && !obj.contact_name2.equals("")){
                            AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial2)
                        }
                        if(obj.contact_serial3.equals("3") && !obj.contact_name3.equals("")){
                            AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial3)
                        }
                        if(obj.contact_serial4.equals("4") && !obj.contact_name4.equals("")){
                            AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial4)
                        }
                        if(obj.contact_serial5.equals("5") && !obj.contact_name5.equals("")){
                            AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial5)
                        }
                        if(obj.contact_serial6.equals("6") && !obj.contact_name6.equals("")){
                            AppDatabase.getDBInstance()?.shopExtraContactDao()?.updateIsUploaded(true,obj.shop_id,obj.contact_serial6)
                        }
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(addmutliContactResult.message!!)
                    }
                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    (mContext as DashboardActivity).showSnackMessage("Error update contact")

                })
        )
    }

    private fun convertToReqAndApiCallForShopStatus(addShopData: AddShopDBModelEntity) {
        if (Pref.user_id == null || Pref.user_id == "" || Pref.user_id == " ") {
            (mContext as DashboardActivity).showSnackMessage("Please login again")
            BaseActivity.isApiInitiated = false
            return
        }

        val addShopReqData = AddShopRequestData()
        addShopReqData.session_token = Pref.session_token
        addShopReqData.address = addShopData.address
        addShopReqData.actual_address = addShopData.address
        addShopReqData.owner_contact_no = addShopData.ownerContactNumber
        addShopReqData.owner_email = addShopData.ownerEmailId
        addShopReqData.owner_name = addShopData.ownerName
        addShopReqData.pin_code = addShopData.pinCode
        addShopReqData.shop_lat = addShopData.shopLat.toString()
        addShopReqData.shop_long = addShopData.shopLong.toString()
        addShopReqData.shop_name = addShopData.shopName.toString()
        addShopReqData.shop_id = addShopData.shop_id
        addShopReqData.added_date = ""
        addShopReqData.user_id = Pref.user_id
        addShopReqData.type = addShopData.type
        addShopReqData.assigned_to_pp_id = addShopData.assigned_to_pp_id
        addShopReqData.assigned_to_dd_id = addShopData.assigned_to_dd_id
        /*addShopReqData.dob = addShopData.dateOfBirth
        addShopReqData.date_aniversary = addShopData.dateOfAniversary*/
        addShopReqData.amount = addShopData.amount
        addShopReqData.area_id = addShopData.area_id
        /*val addShop = AddShopRequest()
        addShop.data = addShopReqData*/

        addShopReqData.model_id = addShopData.model_id
        addShopReqData.primary_app_id = addShopData.primary_app_id
        addShopReqData.secondary_app_id = addShopData.secondary_app_id
        addShopReqData.lead_id = addShopData.lead_id
        addShopReqData.stage_id = addShopData.stage_id
        addShopReqData.funnel_stage_id = addShopData.funnel_stage_id
        addShopReqData.booking_amount = addShopData.booking_amount
        addShopReqData.type_id = addShopData.type_id

        if (!TextUtils.isEmpty(addShopData.dateOfBirth))
            addShopReqData.dob = changeAttendanceDateFormatToCurrent(addShopData.dateOfBirth)

        if (!TextUtils.isEmpty(addShopData.dateOfAniversary))
            addShopReqData.date_aniversary = changeAttendanceDateFormatToCurrent(addShopData.dateOfAniversary)

        addShopReqData.director_name = addShopData.director_name
        addShopReqData.key_person_name = addShopData.person_name
        addShopReqData.phone_no = addShopData.person_no

        if (!TextUtils.isEmpty(addShopData.family_member_dob))
            addShopReqData.family_member_dob = changeAttendanceDateFormatToCurrent(addShopData.family_member_dob)

        if (!TextUtils.isEmpty(addShopData.add_dob))
            addShopReqData.addtional_dob = changeAttendanceDateFormatToCurrent(addShopData.add_dob)

        if (!TextUtils.isEmpty(addShopData.add_doa))
            addShopReqData.addtional_doa = changeAttendanceDateFormatToCurrent(addShopData.add_doa)

        addShopReqData.specialization = addShopData.specialization
        addShopReqData.category = addShopData.category
        addShopReqData.doc_address = addShopData.doc_address
        addShopReqData.doc_pincode = addShopData.doc_pincode
        addShopReqData.is_chamber_same_headquarter = addShopData.chamber_status.toString()
        addShopReqData.is_chamber_same_headquarter_remarks = addShopData.remarks
        addShopReqData.chemist_name = addShopData.chemist_name
        addShopReqData.chemist_address = addShopData.chemist_address
        addShopReqData.chemist_pincode = addShopData.chemist_pincode
        addShopReqData.assistant_contact_no = addShopData.assistant_no
        addShopReqData.average_patient_per_day = addShopData.patient_count
        addShopReqData.assistant_name = addShopData.assistant_name

        if (!TextUtils.isEmpty(addShopData.doc_family_dob))
            addShopReqData.doc_family_member_dob = changeAttendanceDateFormatToCurrent(addShopData.doc_family_dob)

        if (!TextUtils.isEmpty(addShopData.assistant_dob))
            addShopReqData.assistant_dob = changeAttendanceDateFormatToCurrent(addShopData.assistant_dob)

        if (!TextUtils.isEmpty(addShopData.assistant_doa))
            addShopReqData.assistant_doa = changeAttendanceDateFormatToCurrent(addShopData.assistant_doa)

        if (!TextUtils.isEmpty(addShopData.assistant_family_dob))
            addShopReqData.assistant_family_dob = changeAttendanceDateFormatToCurrent(addShopData.assistant_family_dob)

        addShopReqData.entity_id = addShopData.entity_id
        addShopReqData.party_status_id = addShopData.party_status_id
        addShopReqData.retailer_id = addShopData.retailer_id
        addShopReqData.dealer_id = addShopData.dealer_id
        addShopReqData.beat_id = addShopData.beat_id
        addShopReqData.assigned_to_shop_id = addShopData.assigned_to_shop_id
        //addShopReqData.actual_address = addShopData.actual_address


        if(addShopData.shopStatusUpdate.equals("0"))
            addShopReqData.shopStatusUpdate = addShopData.shopStatusUpdate
        else
            addShopReqData.shopStatusUpdate = "1"

        callEditShopApiForShopStatus(addShopReqData)
    }

    private fun callEditShopApiForShopStatus(addShopReqData: AddShopRequestData) {
        if (BaseActivity.isApiInitiated)
            return

        BaseActivity.isApiInitiated = true

        Timber.d("=====Sync EditShop Input Params (Shop List)======")
        Timber.d("shop id====> " + addShopReqData.shop_id)
        val index = addShopReqData.shop_id!!.indexOf("_")
        Timber.d("decoded shop id====> " + addShopReqData.user_id + "_" + AppUtils.getDate(addShopReqData.shop_id!!.substring(index + 1, addShopReqData.shop_id!!.length).toLong()))
        Timber.d("shop added date====> " + addShopReqData.added_date)
        Timber.d("shop address====> " + addShopReqData.address)
        Timber.d("assigned to dd id====> " + addShopReqData.assigned_to_dd_id)
        Timber.d("assigned to pp id=====> " + addShopReqData.assigned_to_pp_id)
        Timber.d("date aniversery=====> " + addShopReqData.date_aniversary)
        Timber.d("dob====> " + addShopReqData.dob)
        Timber.d("shop owner phn no===> " + addShopReqData.owner_contact_no)
        Timber.d("shop owner email====> " + addShopReqData.owner_email)
        Timber.d("shop owner name====> " + addShopReqData.owner_name)
        Timber.d("shop pincode====> " + addShopReqData.pin_code)
        Timber.d("session token====> " + addShopReqData.session_token)
        Timber.d("shop lat====> " + addShopReqData.shop_lat)
        Timber.d("shop long===> " + addShopReqData.shop_long)
        Timber.d("shop name====> " + addShopReqData.shop_name)
        Timber.d("shop type===> " + addShopReqData.type)
        Timber.d("user id====> " + addShopReqData.user_id)
        Timber.d("amount=======> " + addShopReqData.amount)
        Timber.d("area id=======> " + addShopReqData.area_id)
        Timber.d("model id=======> " + addShopReqData.model_id)
        Timber.d("primary app id=======> " + addShopReqData.primary_app_id)
        Timber.d("secondary app id=======> " + addShopReqData.secondary_app_id)
        Timber.d("lead id=======> " + addShopReqData.lead_id)
        Timber.d("stage id=======> " + addShopReqData.stage_id)
        Timber.d("funnel stage id=======> " + addShopReqData.funnel_stage_id)
        Timber.d("booking amount=======> " + addShopReqData.booking_amount)
        Timber.d("type id=======> " + addShopReqData.type_id)

        Timber.d("family member dob=======> " + addShopReqData.family_member_dob)
        Timber.d("director name=======> " + addShopReqData.director_name)
        Timber.d("key person's name=======> " + addShopReqData.key_person_name)
        Timber.d("phone no=======> " + addShopReqData.phone_no)
        Timber.d("additional dob=======> " + addShopReqData.addtional_dob)
        Timber.d("additional doa=======> " + addShopReqData.addtional_doa)
        Timber.d("doctor family member dob=======> " + addShopReqData.doc_family_member_dob)
        Timber.d("specialization=======> " + addShopReqData.specialization)
        Timber.d("average patient count per day=======> " + addShopReqData.average_patient_per_day)
        Timber.d("category=======> " + addShopReqData.category)
        Timber.d("doctor address=======> " + addShopReqData.doc_address)
        Timber.d("doctor pincode=======> " + addShopReqData.doc_pincode)
        Timber.d("chambers or hospital under same headquarter=======> " + addShopReqData.is_chamber_same_headquarter)
        Timber.d("chamber related remarks=======> " + addShopReqData.is_chamber_same_headquarter_remarks)
        Timber.d("chemist name=======> " + addShopReqData.chemist_name)
        Timber.d("chemist name=======> " + addShopReqData.chemist_address)
        Timber.d("chemist pincode=======> " + addShopReqData.chemist_pincode)
        Timber.d("assistant name=======> " + addShopReqData.assistant_name)
        Timber.d("assistant contact no=======> " + addShopReqData.assistant_contact_no)
        Timber.d("assistant dob=======> " + addShopReqData.assistant_dob)
        Timber.d("assistant date of anniversary=======> " + addShopReqData.assistant_doa)
        Timber.d("assistant family dob=======> " + addShopReqData.assistant_family_dob)
        Timber.d("entity id=======> " + addShopReqData.entity_id)
        Timber.d("party status id=======> " + addShopReqData.party_status_id)
        Timber.d("retailer id=======> " + addShopReqData.retailer_id)
        Timber.d("dealer id=======> " + addShopReqData.dealer_id)
        Timber.d("beat id=======> " + addShopReqData.beat_id)
        Timber.d("actual_address=======> " + addShopReqData.actual_address)

        progress_wheel.spin()

        if (true) {
            val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.editShop(addShopReqData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val addShopResult = result as AddShopResponse
                        Timber.d("Edit Shop : " + ", SHOP: " + addShopReqData.shop_name + ", RESPONSE:" + result.message)
                        if (addShopResult.status == NetworkConstant.SUCCESS) {
                            AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsEditUploaded(1, addShopReqData.shop_id)
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage("Status updated successfully")
                            initAdapter()

                        }
                        BaseActivity.isApiInitiated = false
                    }, { error ->
                        error.printStackTrace()
                        BaseActivity.isApiInitiated = false
                        progress_wheel.stopSpinning()
                    })
            )
        }
    }
}