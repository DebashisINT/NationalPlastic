package com.breezenationalplasticfsm.features.dailyPlan.prsentation

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pnikosis.materialishprogress.ProgressWheel
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.*
import com.breezenationalplasticfsm.app.domain.SelectedRouteEntity
import com.breezenationalplasticfsm.app.domain.SelectedRouteShopListEntity
import com.breezenationalplasticfsm.app.domain.SelectedWorkTypeEntity
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.NotificationUtils
import com.breezenationalplasticfsm.app.utils.PermissionUtils
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.addAttendence.FingerprintDialog
import com.breezenationalplasticfsm.features.addAttendence.PrimaryValueAdapter
import com.breezenationalplasticfsm.features.addAttendence.SelfieDialog
import com.breezenationalplasticfsm.features.addAttendence.api.addattendenceapi.AddAttendenceRepoProvider
import com.breezenationalplasticfsm.features.addAttendence.model.AddAttendenceInpuModel
import com.breezenationalplasticfsm.features.addAttendence.model.UpdatePlanListModel
import com.breezenationalplasticfsm.features.commondialog.presentation.CommonDialog
import com.breezenationalplasticfsm.features.commondialog.presentation.CommonDialogClickListener
import com.breezenationalplasticfsm.features.dailyPlan.api.PlanRepoProvider
import com.breezenationalplasticfsm.features.dailyPlan.model.GetPlanListDataModel
import com.breezenationalplasticfsm.features.dailyPlan.model.GetPlanListResponseModel
import com.breezenationalplasticfsm.features.dailyPlan.model.UpdatePlanListInputParamsModel
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.login.UserLoginDataEntity
import com.breezenationalplasticfsm.features.login.presentation.LoginActivity
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import com.themechangeapp.pickimage.PermissionHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * Created by Saikat on 20-12-2019.
 */
// Revision History
// 1.0 DailyPlanListFragment saheli 24-02-2032 AppV 4.0.7 mantis 0025683
class DailyPlanListFragment : BaseFragment() {

    private lateinit var mContext: Context

    private lateinit var rv_daily_plan_list: RecyclerView
    private lateinit var tv_submit_btn: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var tv_no_attendance: AppCustomTextView
    private lateinit var tv_updated_shop_no: AppCustomTextView

    private var updatePlanListModelList: ArrayList<UpdatePlanListModel>? = null
    private var updatePlanFinalList: ArrayList<UpdatePlanListModel>? = null
    private var planListAdapter: DailyPlanListAdapter? = null
    private var updatePlanList: ArrayList<GetPlanListDataModel>? = null
    private var fingerprintDialog: FingerprintDialog? = null
    private var selfieDialog: SelfieDialog? = null

    companion object {

        private var addAttendenceModel: AddAttendenceInpuModel? = null

        fun newInstance(objects: Any): DailyPlanListFragment {
            val fragment = DailyPlanListFragment()

            if (objects is AddAttendenceInpuModel)
                addAttendenceModel = objects

            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_daily_plan_list, container, false)
        initView(view)
        getPlanList()
        return view
    }

    private fun initView(view: View) {
        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()

        tv_no_attendance = view.findViewById(R.id.tv_no_attendance)
        tv_updated_shop_no = view.findViewById(R.id.tv_updated_shop_no)

        rv_daily_plan_list = view.findViewById(R.id.rv_daily_plan_list)
        rv_daily_plan_list.layoutManager = LinearLayoutManager(mContext)

        updatePlanListModelList = ArrayList<UpdatePlanListModel>()
        updatePlanFinalList = ArrayList()

        tv_submit_btn = view.findViewById(R.id.tv_submit_btn)
        tv_submit_btn.setOnClickListener {

            if (AppUtils.isFromAttendance) {
                if (tv_updated_shop_no.text.toString().trim() == "0") {
                    CommonDialog.getInstance(Pref.attendenceAlertHeading, Pref.attendenceAlertText, getString(R.string.no), getString(R.string.yes), false, object : CommonDialogClickListener {
                        override fun onLeftClick() {
                        }

                        override fun onRightClick(editableData: String) {
                            addAttendance()
                        }

                    }).show((mContext as DashboardActivity).supportFragmentManager, "")
                }
                else
                    addAttendance()
            } else {
                if (updatePlanList == null || updatePlanList!!.size == 0)
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_available))
                else if (tv_updated_shop_no.text.toString().trim() == "0")
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_update_plan))
                else
                    callUpdatePlanListApi()
            }
        }

        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    if (updatePlanList != null && updatePlanList!!.size > 0)
                        planListAdapter?.updateList(updatePlanList)
                } else {
                    if (updatePlanList != null && updatePlanList!!.size > 0)
                        planListAdapter?.filter?.filter(query)
                }
            }
        })

        // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 start
        (mContext as DashboardActivity).searchView.setVoiceIcon(R.drawable.ic_mic)
        (mContext as DashboardActivity).searchView.setOnVoiceClickedListener({ startVoiceInput() })
        // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 end
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


    private fun addAttendance(){
        if (addAttendenceModel?.is_on_leave?.toBoolean()!!)
            callAddAttendanceApi()
        else {
            if (Pref.isFingerPrintMandatoryForAttendance) {
                if ((mContext as DashboardActivity).isFingerPrintSupported)
                    showFingerPrintDialog()
                else {
                    if (Pref.isSelfieMandatoryForAttendance)
                        showSelfieDialog()
                    else
                        callAddAttendanceApi()
                }
            }
            else if (Pref.isSelfieMandatoryForAttendance) {
                showSelfieDialog()
            }
            else
                callAddAttendanceApi()
        }
    }

    private fun showSelfieDialog() {
        selfieDialog = SelfieDialog.getInstance({
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                initPermissionCheck()
            else {
                launchCamera()
            }
        }, false)
        selfieDialog?.show((mContext as DashboardActivity).supportFragmentManager, "")
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
                //showPictureDialog()
                launchCamera()
            }

            override fun onPermissionNotGranted() {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
            }

        },permissionList)// arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    fun onRequestPermission(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun launchCamera() {
        if (PermissionHelper.checkCameraPermission(mContext as DashboardActivity) && PermissionHelper.checkStoragePermission(mContext as DashboardActivity)) {
            /*val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, (mContext as DashboardActivity).getPhotoFileUri(System.currentTimeMillis().toString() + ".png"))
            (mContext as DashboardActivity).startActivityForResult(intent, PermissionHelper.REQUEST_CODE_CAMERA)*/

            (mContext as DashboardActivity).captureFrontImage()
        }
    }

    fun setCameraImage(file: File) {
        selfieDialog?.dismiss()

        if (file == null || TextUtils.isEmpty(file.absolutePath)) {
            (mContext as DashboardActivity).showSnackMessage("Invalid Image")
            return
        }

        if (!AppUtils.isOnline(mContext)){
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val repository = AddAttendenceRepoProvider.sendAttendanceImgRepo()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.attendanceWithImage(file.absolutePath, mContext)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as BaseResponse

                            if (response.status == NetworkConstant.SUCCESS) {
                                callAddAttendanceApi()
                            } else {
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }


                        }, { error ->
                            BaseActivity.isApiInitiated = false
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )

    }

    private fun showFingerPrintDialog() {
        (mContext as DashboardActivity).checkForFingerPrint()

        fingerprintDialog = FingerprintDialog()
        fingerprintDialog?.show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    fun continueAddAttendance() {
        if (fingerprintDialog != null && fingerprintDialog?.isVisible!!) {
            fingerprintDialog?.dismiss()

            if (Pref.isSelfieMandatoryForAttendance)
                showSelfieDialog()
            else
                callAddAttendanceApi()
        }
    }

    private fun callUpdatePlanListApi() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val updatePlanListModel = UpdatePlanListInputParamsModel()
        updatePlanListModel.session_token = Pref.session_token!!
        updatePlanListModel.user_id = Pref.user_id!!
        updatePlanListModel.update_plan_list = updatePlanFinalList

        Timber.d("=====UpdatePlan Input Params========")
        Timber.d("session_token=======> " + updatePlanListModel.session_token)
        Timber.d("user_id======> " + updatePlanListModel.user_id)
        Timber.d("update_plan_list size======> " + updatePlanListModel.update_plan_list?.size)
        Timber.d("====================================")

        val repository = PlanRepoProvider.planListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.updatePlanList(updatePlanListModel)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val baseResponse = result as BaseResponse
                            Timber.d("Update Plan Response Code========> " + baseResponse.status)

                            progress_wheel.stopSpinning()

                            (mContext as DashboardActivity).showSnackMessage(baseResponse.message!!)

                            if (baseResponse.status == NetworkConstant.SUCCESS) {

                                if ((mContext as DashboardActivity).isDailyPlanFromAlarm)
                                    (mContext as DashboardActivity).isConfirmed = true

                                (mContext as DashboardActivity).onBackPressed()
                            } else if (baseResponse.status == NetworkConstant.SESSION_MISMATCH) {
                                startActivity(Intent(mContext as DashboardActivity, LoginActivity::class.java))
                                (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                (mContext as DashboardActivity).finish()
                            }

                        }, { error ->
                            BaseActivity.isApiInitiated = false
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            Timber.d("Update Plan ERROR=======> " + error.localizedMessage)
                        })
        )
    }

    private fun callAddAttendanceApi() {

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        addAttendenceModel?.update_plan_list = updatePlanFinalList!!

        Timber.e("==========AddAttendance (From plan list)=============")
        Timber.d("=====AddAttendance Input Params========")
        Timber.d("session_token-----> " + addAttendenceModel?.session_token)
        Timber.d("user_id----------> " + addAttendenceModel?.user_id)
        Timber.d("is_on_leave----------> " + addAttendenceModel?.is_on_leave)
        Timber.d("work_lat----------> " + addAttendenceModel?.work_lat)
        Timber.d("work_long----------> " + addAttendenceModel?.work_long)
        Timber.d("work_address----------> " + addAttendenceModel?.work_address)
        Timber.d("work_type----------> " + addAttendenceModel?.work_type)
        Timber.d("route----------> " + addAttendenceModel?.route)
        Timber.d("leave_from_date----------> " + addAttendenceModel?.leave_from_date)
        Timber.d("leave_to_date----------> " + addAttendenceModel?.leave_to_date)
        Timber.d("leave_type----------> " + addAttendenceModel?.leave_type)
        Timber.d("leave_reason----------> " + addAttendenceModel?.leave_reason)
        Timber.d("work_date_time----------> " + addAttendenceModel?.work_date_time)
        Timber.d("add_attendence_time----------> " + addAttendenceModel?.add_attendence_time)
        Timber.d("order taken----------> " + addAttendenceModel?.order_taken)
        Timber.d("collection taken----------> " + addAttendenceModel?.collection_taken)
        Timber.d("visit new shop----------> " + addAttendenceModel?.new_shop_visit)
        Timber.d("revisit shop----------> " + addAttendenceModel?.revisit_shop)
        Timber.d("state id----------> " + addAttendenceModel?.state_id)
        Timber.d("shop_list size----------> " + addAttendenceModel?.shop_list?.size)
        Timber.d("primary_value_list size----------> " + addAttendenceModel?.primary_value_list?.size)
        Timber.d("update_plan_list size----------> " + addAttendenceModel?.update_plan_list?.size)
        Timber.d("from_id----------> " + addAttendenceModel?.from_id)
        Timber.d("to_id----------> " + addAttendenceModel?.to_id)
        Timber.d("distance----------> " + addAttendenceModel?.distance)
        Timber.d("======End AddAttendance Input Params======")

        val repository = AddAttendenceRepoProvider.addAttendenceRepo()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.addAttendence(addAttendenceModel!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as BaseResponse
                            Timber.d("AddAttendance Response Code========> " + response.status)
                            Timber.d("AddAttendance Response Msg=========> " + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {

                                Pref.visitDistance = (mContext as DashboardActivity).visitDistance

                                Pref.prevOrderCollectionCheckTimeStamp = 0L
                                PrimaryValueAdapter.primaryValueList.clear()

                                val list = AppDatabase.getDBInstance()!!.userAttendanceDataDao().getLoginDate(Pref.user_id!!, AppUtils.getCurrentDateChanged())
                                if (list != null && list.isNotEmpty()) {
                                    AppDatabase.getDBInstance()!!.userAttendanceDataDao().deleteTodaysData(AppUtils.getCurrentDateChanged())
                                }

                                if (AppDatabase.getDBInstance()!!.userAttendanceDataDao().getLoginDate(Pref.user_id!!, AppUtils.getCurrentDateChanged()).isEmpty()) {
                                    val userLoginDataEntity = UserLoginDataEntity()
                                    userLoginDataEntity.logindate = AppUtils.getCurrentDateChanged()
                                    userLoginDataEntity.logindate_number = AppUtils.getTimeStampFromDateOnly(AppUtils.getCurrentDateForShopActi())
                                    //val addAttendenceTime = AppUtils.getCurrentTimeWithMeredian()
                                    //Pref.add_attendence_time = addAttendenceModel?.add_attendence_time
                                    if (!addAttendenceModel?.is_on_leave?.toBoolean()!!) {
                                        userLoginDataEntity.logintime = addAttendenceModel?.add_attendence_time!!
                                        userLoginDataEntity.Isonleave = "false"
                                        Pref.isOnLeave = "false"

                                        if (TextUtils.isEmpty(Pref.isFieldWorkVisible) || Pref.isFieldWorkVisible.equals("true", ignoreCase = true)) {

                                            val list = AppDatabase.getDBInstance()?.workTypeDao()?.getSelectedWork(true)
                                            if (list != null && list.isNotEmpty()) {

                                                for (i in list.indices) {
                                                    val selectedwortkType = SelectedWorkTypeEntity()
                                                    selectedwortkType.ID = list[i].ID
                                                    selectedwortkType.Descrpton = list[i].Descrpton
                                                    selectedwortkType.date = AppUtils.getCurrentDate()
                                                    AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.insertAll(selectedwortkType)
                                                }
                                            }

                                            val routeList = AppDatabase.getDBInstance()?.routeDao()?.getSelectedRoute(true)
                                            if (routeList != null && routeList.isNotEmpty()) {

                                                for (i in routeList.indices) {
                                                    val selectedRoute = SelectedRouteEntity()
                                                    selectedRoute.route_id = routeList[i].route_id
                                                    selectedRoute.route_name = routeList[i].route_name
                                                    selectedRoute.date = AppUtils.getCurrentDate()
                                                    AppDatabase.getDBInstance()?.selectedRouteListDao()?.insert(selectedRoute)
                                                }
                                            }

                                            val routeShopList = AppDatabase.getDBInstance()?.routeShopListDao()?.getSelectedData(true)
                                            if (routeShopList != null && routeShopList.isNotEmpty()) {

                                                for (i in routeShopList.indices) {
                                                    val selectedRouteShop = SelectedRouteShopListEntity()
                                                    selectedRouteShop.route_id = routeShopList[i].route_id
                                                    selectedRouteShop.shop_address = routeShopList[i].shop_address
                                                    selectedRouteShop.shop_contact_no = routeShopList[i].shop_contact_no
                                                    selectedRouteShop.shop_name = routeShopList[i].shop_name
                                                    selectedRouteShop.shop_id = routeShopList[i].shop_id
                                                    selectedRouteShop.date = AppUtils.getCurrentDate()
                                                    AppDatabase.getDBInstance()?.selectedRouteShopListDao()?.insert(selectedRouteShop)
                                                }
                                            }
                                        }

                                        Pref.isAddAttendence = true
                                        Pref.add_attendence_time = addAttendenceModel?.add_attendence_time

                                        (mContext as DashboardActivity).update_worktype_tv.apply {
                                            visibility = if (Pref.isUpdateWorkTypeEnable)
                                                View.VISIBLE
                                            else
                                                View.GONE
                                        }

                                        Pref.distributorName = addAttendenceModel?.distributor_name!!
                                        Pref.marketWorked = addAttendenceModel?.market_worked!!

                                        voiceAttendanceMsg("Hi, your attendance mark successfully.")
                                    } else {
                                        userLoginDataEntity.Isonleave = "true"

                                        if (addAttendenceModel?.leave_from_date == AppUtils.getCurrentDateForShopActi()) {
                                            Pref.isOnLeave = "true"
                                            Pref.isAddAttendence = true
                                            Pref.add_attendence_time = addAttendenceModel?.add_attendence_time

                                            val notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                            notificationManager.cancelAll()
                                        }
                                        else {
                                            Pref.isOnLeave = "false"
                                            Pref.isAddAttendence = false
                                        }

                                        val notification = NotificationUtils(getString(R.string.app_name), "", "", "")
                                        val body = "You have applied leave from date: " + addAttendenceModel?.leave_from_date +
                                                ", to date: " + addAttendenceModel?.leave_to_date + ", type: " + (mContext as DashboardActivity).leaveType
                                        notification.sendLocNotification(mContext, body)
                                        voiceAttendanceMsg("Hi, your leave applied successfully.")
                                    }
                                    userLoginDataEntity.userId = Pref.user_id!!
                                    AppDatabase.getDBInstance()!!.userAttendanceDataDao().insertAll(userLoginDataEntity)
                                }

                                AppUtils.isFromAttendance = false
                                //Pref.isAddAttendence = true
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                (mContext as DashboardActivity).onBackPressed()
                                (mContext as DashboardActivity).onBackPressed()

                            } else {
                                //AppUtils.isFromAttendance = false
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                            Log.e("add attendance", "api work type")

                        }, { error ->
                            Timber.d("AddAttendance Response Msg=========> " + error.message)
                            BaseActivity.isApiInitiated = false
                            //AppUtils.isFromAttendance = false
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun voiceAttendanceMsg(msg: String) {
        if (Pref.isVoiceEnabledForAttendanceSubmit) {
            val speechStatus = (mContext as DashboardActivity).textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
            if (speechStatus == TextToSpeech.ERROR)
                Log.e("Add Attendance", "TTS error in converting Text to Speech!");
        }
    }

    private fun getPlanList() {

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            tv_no_attendance.visibility = View.VISIBLE
            return
        }

        val repository = PlanRepoProvider.planListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getPlanList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->

                            progress_wheel.stopSpinning()

                            val planListResponse = result as GetPlanListResponseModel

                            if (planListResponse.status == NetworkConstant.SUCCESS) {
                                if (planListResponse.update_plan_list != null && planListResponse.update_plan_list!!.size > 0) {
                                    tv_no_attendance.visibility = View.GONE

                                    updatePlanList = planListResponse.update_plan_list
                                    initAdapter(planListResponse.update_plan_list)

                                    for (i in planListResponse.update_plan_list!!.indices) {
                                        val updatePlanListModel = UpdatePlanListModel()
                                        updatePlanListModel.plan_id = planListResponse.update_plan_list!![i].plan_id

                                        /*if (!TextUtils.isEmpty(planListResponse.update_plan_list!![i].last_plan_date))
                                            updatePlanListModel.plan_date = planListResponse.update_plan_list!![i].last_plan_date*/

                                        if (!TextUtils.isEmpty(planListResponse.update_plan_list!![i].last_achv_amount))
                                            updatePlanListModel.achievement_value = planListResponse.update_plan_list!![i].last_achv_amount

                                        if (!TextUtils.isEmpty(planListResponse.update_plan_list!![i].last_achv_feedback))
                                            updatePlanListModel.acheivement_remarks = planListResponse.update_plan_list!![i].last_achv_feedback

                                        if (!TextUtils.isEmpty(planListResponse.update_plan_list!![i].last_plan_value))
                                            updatePlanListModel.plan_value = planListResponse.update_plan_list!![i].last_plan_value

                                        if (!TextUtils.isEmpty(planListResponse.update_plan_list!![i].last_plan_feedback))
                                            updatePlanListModel.plan_remarks = planListResponse.update_plan_list!![i].last_plan_feedback

                                        updatePlanListModelList?.add(updatePlanListModel)
                                    }

                                } else {
                                    tv_no_attendance.visibility = View.VISIBLE
                                    (mContext as DashboardActivity).showSnackMessage(planListResponse.message!!)
                                }
                            } else {
                                tv_no_attendance.visibility = View.VISIBLE
                                (mContext as DashboardActivity).showSnackMessage(planListResponse.message!!)
                            }

                        }, { error ->
                            BaseActivity.isApiInitiated = false
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            tv_no_attendance.visibility = View.VISIBLE
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            Timber.d("DailyPlanList ERROR: " + error.localizedMessage)
                        })
        )
    }

    private fun initAdapter(updatePlanList: ArrayList<GetPlanListDataModel>?) {
        planListAdapter = DailyPlanListAdapter(mContext, updatePlanList, object : DailyPlanListAdapter.OnButtonClickListener {
            override fun onUpdatePlanClick(adapterPosition: Int, plan: GetPlanListDataModel?) {
                UpdatePlanDialog.newInstance(plan!!, object : UpdatePlanDialog.OnSubmitBtnClickListener {
                    override fun onSubmitClick(planValue: String, planRemark: String, planDate: String) {

                        for (i in updatePlanList?.indices!!) {
                            if (updatePlanList[i].plan_id == plan.plan_id) {

                                if (!TextUtils.isEmpty(updatePlanList[i].last_plan_date)) {
                                    if (AppUtils.convertDateStringToLong(updatePlanList[i].last_plan_date) !=
                                            AppUtils.convertDateStringToLong(AppUtils.convertBilingDateToIdealFormat(planDate))) {
                                        updatePlanList[i].last_achv_amount = ""
                                        updatePlanList[i].last_achv_feedback = ""
                                        updatePlanList[i].last_plan_value = ""
                                        updatePlanList[i].last_plan_feedback = ""

                                        try {
                                            updatePlanListModelList?.get(i)?.plan_remarks = ""
                                            updatePlanListModelList?.get(i)?.plan_value = ""
                                            updatePlanListModelList?.get(i)?.acheivement_remarks = ""
                                            updatePlanListModelList?.get(i)?.achievement_value = ""
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }

                                updatePlanList[i].last_plan_date = AppUtils.convertBilingDateToIdealFormat(planDate)
                                updatePlanList[i].last_plan_value = planValue
                                updatePlanList[i].last_plan_feedback = planRemark
                                planListAdapter?.updateList(updatePlanList)

                                try {
                                    updatePlanListModelList?.get(i)?.plan_date = AppUtils.convertBilingDateToIdealFormat(planDate)

                                    updatePlanListModelList?.get(i)?.plan_remarks = planRemark
                                    updatePlanListModelList?.get(i)?.plan_value = planValue
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                }

                                break
                            }
                        }

                        //var isUpdated = false

                        /*for (i in updatePlanListModelList!!.indices) {
                            if (updatePlanListModelList!![i].plan_id == plan.plan_id) {
                                //isUpdated = true

                                try {
                                    if (!TextUtils.isEmpty(updatePlanListModelList?.get(i)?.plan_date)) {
                                        if (AppUtils.convertDateStringToLong(updatePlanListModelList?.get(i)?.plan_date) !=
                                                AppUtils.convertDateStringToLong(AppUtils.convertBilingDateToIdealFormat(planDate))) {
                                            updatePlanListModelList?.get(i)?.plan_remarks = ""
                                            updatePlanListModelList?.get(i)?.plan_value = ""
                                            updatePlanListModelList?.get(i)?.acheivement_remarks = ""
                                            updatePlanListModelList?.get(i)?.achievement_value = ""

                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                updatePlanListModelList?.get(i)?.plan_date = AppUtils.convertBilingDateToIdealFormat(planDate)

                                updatePlanListModelList?.get(i)?.plan_remarks = planRemark
                                updatePlanListModelList?.get(i)?.plan_value = planValue
                                break
                            }
                        }*/

                        /*if (!isUpdated) {
                            val updatePlanListModel = UpdatePlanListModel()
                            updatePlanListModel.plan_id = plan.plan_id
                            updatePlanListModel.plan_date = AppUtils.convertBilingDateToIdealFormat(planDate)
                            updatePlanListModel.plan_remarks = planRemark
                            updatePlanListModel.plan_value = planValue
                            updatePlanListModelList?.add(updatePlanListModel)
                        }*/

                        showShopsUpdatedNo()
                    }

                }).show((mContext as DashboardActivity).supportFragmentManager, "")
            }

            override fun onUpadteAchvClick(adapterPosition: Int, plan: GetPlanListDataModel?) {
                UpdateAchievementDialog.newInstance(plan!!, object : UpdateAchievementDialog.OnSubmitBtnClickListener {
                    override fun onSubmitClick(achvValue: String, achvRemark: String) {

                        for (i in updatePlanList?.indices!!) {
                            if (updatePlanList[i].plan_id == plan.plan_id) {
                                updatePlanList[i].last_achv_amount = achvValue
                                updatePlanList[i].last_achv_feedback = achvRemark
                                planListAdapter?.updateList(updatePlanList)
                                break
                            }
                        }

                        //var isUpdated = false
                        for (i in updatePlanListModelList!!.indices) {
                            if (updatePlanListModelList!![i].plan_id == plan.plan_id) {

                                //isUpdated = true

                                if (TextUtils.isEmpty(updatePlanListModelList?.get(i)?.plan_date))
                                    updatePlanListModelList?.get(i)?.plan_date = AppUtils.getCurrentDateForShopActi()

                                updatePlanListModelList?.get(i)?.acheivement_remarks = achvRemark
                                updatePlanListModelList?.get(i)?.achievement_value = achvValue
                                break
                            }
                        }


                        /*if (!isUpdated) {
                            val updatePlanListModel = UpdatePlanListModel()
                            updatePlanListModel.plan_id = plan.plan_id
                            updatePlanListModel.plan_date = AppUtils.getCurrentDateForShopActi()
                            updatePlanListModel.acheivement_remarks = achvRemark
                            updatePlanListModel.achievement_value = achvValue
                            updatePlanListModelList?.add(updatePlanListModel)
                        }*/


                        showShopsUpdatedNo()
                    }

                }).show((mContext as DashboardActivity).supportFragmentManager, "")
            }

            override fun onViewAllClick(adapterPosition: Int, plan: GetPlanListDataModel?) {
                (mContext as DashboardActivity).loadFragment(FragType.PlanDetailsFragment, true, plan!!)
            }

        })

        rv_daily_plan_list.adapter = planListAdapter
    }

    private fun showShopsUpdatedNo() {
        var updatedShop = 0
        for (i in updatePlanListModelList?.indices!!) {
            if (!TextUtils.isEmpty(updatePlanListModelList?.get(i)?.plan_date) /*|| !TextUtils.isEmpty(updatePlanListModelList?.get(i)?.achievement_value)*/) {
                updatedShop += 1
                updatePlanFinalList?.add(updatePlanListModelList?.get(i)!!)
            }
        }
        tv_updated_shop_no.text = updatedShop.toString()

        val hashSet = HashSet<UpdatePlanListModel>()
        hashSet.addAll(updatePlanFinalList!!)
        updatePlanFinalList?.clear()
        updatePlanFinalList?.addAll(hashSet)
    }
}