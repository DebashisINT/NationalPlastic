package com.breezenationalplasticfsm.features.device_info.presentation

import android.content.Context
import android.hardware.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.BatteryNetStatusEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.location.api.LocationRepoProvider
import com.breezenationalplasticfsm.features.location.model.AppInfoDataModel
import com.breezenationalplasticfsm.features.location.model.AppInfoInputModel
import com.breezenationalplasticfsm.features.location.model.AppInfoResponseModel
import com.breezenationalplasticfsm.widgets.AppCustomTextView

import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class DeviceInfoListFragment : BaseFragment() {

    private lateinit var mContext: Context

    private lateinit var rv_device_info_list: RecyclerView
    private lateinit var tv_no_data_available: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var tv_pick_date: AppCustomTextView
    private lateinit var backcamera: TextView

    private var selectedDate = ""

    private val myCalendar: Calendar by lazy {
        Calendar.getInstance(Locale.ENGLISH)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_device_info_list, container, false)

        initView(view)
        selectedDate = AppUtils.getCurrentDateForShopActi()

        val list = AppDatabase.getDBInstance()?.batteryNetDao()?.getAll()
        if (list != null && list.isNotEmpty()) {
            initAdapter()
        }
        else {
            getListFromApi()
        }

        return view
    }

    private fun initView(view: View) {
        view.apply {
            backcamera = findViewById(R.id.backcamera)
            rv_device_info_list = findViewById(R.id.rv_device_info_list)
            tv_no_data_available = findViewById(R.id.tv_no_data_available)
            progress_wheel = findViewById(R.id.progress_wheel)
            tv_pick_date = findViewById(R.id.tv_pick_date)
        }
//        backcamera.text = getBackCameraResolutionInMp().toString()

        tv_pick_date.text = AppUtils.getFormattedDate(myCalendar.time)
        progress_wheel.stopSpinning()
        rv_device_info_list.layoutManager = LinearLayoutManager(mContext)

        tv_pick_date.setOnClickListener {
            val datePicker = android.app.DatePickerDialog(mContext, R.style.DatePickerTheme, date, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
            datePicker.datePicker.maxDate = Calendar.getInstance(Locale.ENGLISH).timeInMillis
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.add(Calendar.DATE, -10)
            datePicker.datePicker.minDate = cal.timeInMillis
            datePicker.show()
            datePicker.show()
        }
    }

    fun getBackCameraResolutionInMp(): Float {
        val noOfCameras: Int = Camera.getNumberOfCameras()
        var maxResolution = -1f
        var pixelCount: Long = -1
        for (i in 0 until noOfCameras) {
            val cameraInfo: Camera.CameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(i, cameraInfo)
            if (cameraInfo.facing === Camera.CameraInfo.CAMERA_FACING_BACK) {
                val camera: Camera = Camera.open(i)
                val cameraParams: Camera.Parameters = camera.getParameters()
                for (j in 0 until cameraParams.getSupportedPictureSizes().size) {
                    val pixelCountTemp: Int = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height // Just changed i to j in this loop
                    if (pixelCountTemp > pixelCount) {
                        pixelCount = pixelCountTemp.toLong()
                        maxResolution = pixelCountTemp.toFloat() / 1024000.0f

                    }
                }
                camera.release()
            }
        }
        return maxResolution
    }


    val date = android.app.DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        tv_pick_date.text = AppUtils.getFormattedDate(myCalendar.time)
        selectedDate = AppUtils.getFormattedDateForApi(myCalendar.time)

        initAdapter()
    }

    private fun initAdapter() {
        val dateWiseList = AppDatabase.getDBInstance()?.batteryNetDao()?.getDataDateWise(selectedDate)
        if (dateWiseList != null && dateWiseList.isNotEmpty()) {
            tv_no_data_available.visibility = View.GONE
            rv_device_info_list.visibility = View.VISIBLE
            rv_device_info_list.adapter = DeviceInfoAdapter(mContext, dateWiseList as ArrayList<BatteryNetStatusEntity>) {
                syncDeviceInfo(it)
            }
        }
        else {
            rv_device_info_list.visibility = View.GONE
            tv_no_data_available.visibility = View.VISIBLE
        }
    }

    private fun getListFromApi() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        progress_wheel.spin()
        val repository = LocationRepoProvider.provideLocationRepository()
        BaseActivity.compositeDisposable.add(
                repository.getAppInfo()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AppInfoResponseModel
                            Timber.e("Get App Info : RESPONSE : " + response.status + ":" + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {
                                doAsync {

                                    response.app_info_list?.forEach {
                                        val deviceInfo = BatteryNetStatusEntity()
                                        AppDatabase.getDBInstance()?.batteryNetDao()?.insert(deviceInfo.apply {
                                            bat_net_id = it.id
                                            date_time = it.date_time
                                            date = AppUtils.changeAttendanceDateFormatToCurrent(it.date_time)
                                            android_version = it.android_version
                                            device_model = it.device_model
                                            bat_level = it.battery_percentage
                                            bat_status = it.battery_status
                                            net_type = it.network_type
                                            mob_net_type = it.mobile_network_type
                                            isUploaded = true
                                            Power_Saver_Status = Pref.PowerSaverStatus
                                        })
                                    }

                                    uiThread {
                                        progress_wheel.stopSpinning()
                                        initAdapter()
                                    }
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            error.printStackTrace()
                            Timber.e("Get App Info : ERROR : " + error.localizedMessage)
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun syncDeviceInfo(deviceInfo: BatteryNetStatusEntity) {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        if (AppUtils.isAppInfoUpdating) {
            (mContext as DashboardActivity).showSnackMessage("Already updating..")
            return
        }

        AppUtils.isAppInfoUpdating = true

        val appInfoList = ArrayList<AppInfoDataModel>()

        deviceInfo.also {
            appInfoList.add(AppInfoDataModel(it.bat_net_id!!, it.date_time!!, it.bat_status!!, it.bat_level!!, it.net_type!!,
                    it.mob_net_type!!, it.device_model!!, it.android_version!!))
        }

        val appInfoInput = AppInfoInputModel(Pref.session_token!!, Pref.user_id!!, appInfoList)

        Timber.d("============App Info Input(Device Info List)===========")
        Timber.d("session_token==========> " + appInfoInput.session_token)
        Timber.d("user_id==========> " + appInfoInput.user_id)
        Timber.d("app_info_list.size==========> " + appInfoInput.app_info_list?.size)
        Timber.d("==============================================================")

        progress_wheel.spin()
        val repository = LocationRepoProvider.provideLocationRepository()
        BaseActivity.compositeDisposable.add(
                repository.appInfo(appInfoInput)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            Timber.e("App Info : RESPONSE : " + response.status + ":" + response.message)
                            AppUtils.isAppInfoUpdating = false

                            if (response.status == NetworkConstant.SUCCESS) {
                                AppDatabase.getDBInstance()?.batteryNetDao()?.updateIsUploadedAccordingToId(true, deviceInfo.id)
                                initAdapter()
                            }

                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(response.message!!)

                        }, { error ->
                            AppUtils.isAppInfoUpdating = false
                            error.printStackTrace()
                            Timber.e("App Info : ERROR : " + error.localizedMessage)
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }
}