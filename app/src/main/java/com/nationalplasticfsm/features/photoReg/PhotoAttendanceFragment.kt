package com.nationalplasticfsm.features.photoReg

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.*
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.CustomStatic
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.*
import com.nationalplasticfsm.app.domain.AddShopDBModelEntity
import com.nationalplasticfsm.app.domain.AssignToDDEntity
import com.nationalplasticfsm.app.domain.ProspectEntity
import com.nationalplasticfsm.app.types.FragType
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.app.utils.FTStorageUtils
import com.nationalplasticfsm.app.utils.PermissionUtils
import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.base.presentation.BaseActivity
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.faceRec.DetectorActivity
import com.nationalplasticfsm.faceRec.FaceStartActivity
import com.nationalplasticfsm.faceRec.tflite.SimilarityClassifier
import com.nationalplasticfsm.faceRec.tflite.TFLiteObjectDetectionAPIModel
import com.nationalplasticfsm.features.addAttendence.PrimaryValueAdapter
import com.nationalplasticfsm.features.addAttendence.api.addattendenceapi.AddAttendenceRepoProvider
import com.nationalplasticfsm.features.addAttendence.api.leavetytpeapi.LeaveTypeRepoProvider
import com.nationalplasticfsm.features.addAttendence.model.AddAttendenceInpuModel
import com.nationalplasticfsm.features.addAttendence.model.PrimaryValueDataModel
import com.nationalplasticfsm.features.attendance.api.AttendanceRepositoryProvider
import com.nationalplasticfsm.features.attendance.model.AttendanceRequest
import com.nationalplasticfsm.features.attendance.model.AttendanceResponse
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.features.dashboard.presentation.api.dayStartEnd.DayStartEndRepoProvider
import com.nationalplasticfsm.features.dashboard.presentation.model.DaystartDayendRequest
import com.nationalplasticfsm.features.leaveapplynew.model.clearAttendanceonRejectReqModelRejectReqModel
import com.nationalplasticfsm.features.location.LocationWizard
import com.nationalplasticfsm.features.location.SingleShotLocationProvider
import com.nationalplasticfsm.features.logout.presentation.api.LogoutRepositoryProvider
import com.nationalplasticfsm.features.photoReg.adapter.AdapterUserListAttenD
import com.nationalplasticfsm.features.photoReg.adapter.PhotoAttendanceListner
import com.nationalplasticfsm.features.photoReg.adapter.ProsListSelectionAdapter
import com.nationalplasticfsm.features.photoReg.adapter.ProsListSelectionListner
import com.nationalplasticfsm.features.photoReg.api.GetUserListPhotoRegProvider
import com.nationalplasticfsm.features.photoReg.model.GetUserListResponse
import com.nationalplasticfsm.features.photoReg.model.ProsCustom
import com.nationalplasticfsm.features.photoReg.model.UserListResponseModel
import com.nationalplasticfsm.widgets.AppCustomEditText
import com.nationalplasticfsm.widgets.AppCustomTextView

import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

// Revision Histroy
// 1.0 PhotoAttendanceFragment saheli 24-02-2032 AppV 4.0.7 mantis 0025683
class PhotoAttendanceFragment: BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var tv_prospect: AppCustomTextView
    private lateinit var rvUserDtls:RecyclerView
    private lateinit var ll_type_view_root:LinearLayout
    private lateinit var progress_wheel: com.pnikosis.materialishprogress.ProgressWheel
    var userList:ArrayList<UserListResponseModel> = ArrayList()
    private var adapter: AdapterUserListAttenD?= null

    protected var previewWidth = 0
    protected var previewHeight = 0
    private var rgbFrameBitmap: Bitmap? = null
    private var faceBmp: Bitmap? = null
    private var portraitBmp: Bitmap? = null

    var cropToFrameTransform: Matrix? = Matrix()
    var faceDetector: FaceDetector? = null
    private val TF_OD_API_MODEL_FILE = "mobile_face_net.tflite"
    val TF_OD_API_IS_QUANTIZED = false
    val TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt"
    val TF_OD_API_INPUT_SIZE = 112

    var prosCusList:ArrayList<ProsCustom> = ArrayList()

    var prosPopupWindow: PopupWindow? = null

    private val addAttendenceModel: AddAttendenceInpuModel by lazy {
        AddAttendenceInpuModel()
    }

    lateinit var obj_temp :UserListResponseModel

    lateinit var simpleDialogProcess : Dialog
    lateinit var dialogHeaderProcess: AppCustomTextView
    lateinit var dialog_yes_no_headerTVProcess: AppCustomTextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object{
        var user_uid: String = ""
        fun getInstance(objects: Any): PhotoAttendanceFragment {
            val photoAttendanceFragment = PhotoAttendanceFragment()
            if (!TextUtils.isEmpty(objects.toString())) {
                user_uid=objects.toString()
            }
            return photoAttendanceFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_photo_attendance, container, false)
        initView(view)

        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    userList?.let {
                        adapter?.refreshList(it)
                        //tv_cust_no.text = "Total customer(s): " + it.size
                    }
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

    // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 end

    private fun initView(view: View){
        rvUserDtls = view.findViewById(R.id.rv_frag_photo_attend_user_details)
        progress_wheel = view.findViewById(R.id.progress_wheel_frag_photo_attend)
        tv_prospect = view.findViewById(R.id.tv_prospect)
        ll_type_view_root = view.findViewById(R.id.ll_type_view_root)
        tv_prospect.setOnClickListener(this)
        ll_type_view_root.setOnClickListener(this)

        faceDetectorSetUp()

        initPermissionCheck()


        var prospectList = AppDatabase.getDBInstance()!!.prosDao().getAll() as ArrayList<ProspectEntity>
        prosCusList.clear()
        prosCusList.add(ProsCustom("0","All"))
        for(i in 0..prospectList.size-1){
            prosCusList.add(ProsCustom(prospectList.get(i).pros_id!!,prospectList.get(i).pros_name!!))
        }
        tv_prospect.text=prosCusList.get(0).prosName

        simpleDialogProcess = Dialog(mContext)
        simpleDialogProcess.setCancelable(false)
        simpleDialogProcess.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialogProcess.setContentView(R.layout.dialog_message)
        dialogHeaderProcess = simpleDialogProcess.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        dialog_yes_no_headerTVProcess = simpleDialogProcess.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView

        progress_wheel.spin()
        Handler(Looper.getMainLooper()).postDelayed({
            callUSerListApi()
        }, 1000)

        enableScreen()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ll_type_view_root,R.id.tv_prospect->{
                if (prosPopupWindow != null && prosPopupWindow!!.isShowing)
                    prosPopupWindow?.dismiss()
                else
                    showProsTypePopup()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showProsTypePopup(){
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val customView = inflater!!.inflate(R.layout.dialog_months, null)
        prosPopupWindow = PopupWindow(customView, resources.getDimensionPixelOffset(R.dimen._110sdp), resources.getDimensionPixelOffset(R.dimen._100sdp))
        prosPopupWindow?.isFocusable=true
        val rv_pros_list = customView.findViewById(R.id.rv_months) as RecyclerView
        rv_pros_list.layoutManager = LinearLayoutManager(mContext)
        val et_search = customView.findViewById<AppCustomEditText>(R.id.et_search)
        et_search.visibility = View.GONE

        prosPopupWindow?.elevation = 200f
        prosPopupWindow!!.width=resources.getDimensionPixelOffset(R.dimen._200sdp)
        prosPopupWindow!!.height=resources.getDimensionPixelOffset(R.dimen._200sdp)
        rv_pros_list.adapter = ProsListSelectionAdapter(mContext, prosCusList, object : ProsListSelectionListner {
            override fun getInfo(obj: ProsCustom) {
                prosPopupWindow?.dismiss()
                tv_prospect.text=obj.prosName
                if(obj.prosid.equals("0")){
                    callUSerListApi()
                }else{
                    loadFilteredList(obj.prosid)
                }

            }
        })

        if (prosPopupWindow != null && !prosPopupWindow?.isShowing!!) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                prosPopupWindow?.showAsDropDown(ll_type_view_root, resources.getDimensionPixelOffset(R.dimen._10sdp), 0, Gravity.BOTTOM)
            } else {
                prosPopupWindow?.showAsDropDown(ll_type_view_root, ll_type_view_root.width - prosPopupWindow?.width!!, 0)
            }
        }

    }

    private fun loadFilteredList(typeId:String){
        userList.clear()
        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getUserListApi(Pref.user_id!!, Pref.session_token!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var response = result as GetUserListResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                if (response.user_list!!.size > 0 && response.user_list!! != null) {

                                    doAsync {
                                        var list=response.user_list

                                        for(i in 0..list!!.size-1){
                                            if(list.get(i).type_id == typeId.toInt())
                                                userList.add(list.get(i))
                                        }

                                        uiThread {
                                            setAdapter()
                                        }
                                    }

                                } else {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_date_found))
                                }
//
                            } else {
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_date_found))
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage("Please try again later.")
                        })
        )
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
                callUSerListApi()
            }

            override fun onPermissionNotGranted() {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
            }
            // mantis id 26741 Storage permission updation Suman 22-08-2023
        },permissionList)// arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    private fun callUSerListApi(){
        userList.clear()
        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getUserListApi(Pref.user_id!!, Pref.session_token!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var response = result as GetUserListResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                if (response.user_list!!.size > 0 && response.user_list!! != null) {

                                    doAsync {
                                        userList = response.user_list!!

                                        uiThread {
                                            setAdapter()
                                        }
                                    }

                                } else {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_date_found))
                                }
//
                            } else {
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_date_found))
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.try_again_later))
//                            (mContext as DashboardActivity).showSnackMessage("ERROR")
                        })
        )
    }

    private fun setAdapter(){

        //filter userList for duplicate value
        var userList_temp:ArrayList<UserListResponseModel> = ArrayList()

        for(i in 0..userList.size-1){
            if(userList_temp.size==0){
                userList_temp.add(userList.get(i))
            }
            var isDuplicate = false
            for(j in 0..userList_temp.size-1){
                if(userList.get(i).user_id == userList_temp.get(j).user_id){
                    isDuplicate=true
                    break
                }
            }
            if(!isDuplicate){
                userList_temp.add(userList.get(i))
            }
        }
        userList.clear()
        userList=userList_temp

        adapter=AdapterUserListAttenD(mContext,userList,object : PhotoAttendanceListner{
            override fun getUserInfoOnLick(obj: UserListResponseModel) {

                progress_wheel.spin()
                faceDetector=null
                faceDetectorSetUp()
                Timber.d("AdapterUserListAttenD onclick")
                //GetImageFromUrl().execute(obj.face_image_link!!)
                //GetImageFromUrl().execute("http://fts.indusnettechnologies.com:7007/CommonFolder/FaceImageDetection/EMA0003358.jpg")
                //GetImageFromUrl().execute("http://3.7.30.86:82/CommonFolder/FaceImageDetection/EMK0000014.jpg")
                //return

                //Handler(Looper.getMainLooper()).postDelayed(Runnable {
                Handler().postDelayed(Runnable {
                    progress_wheel.stopSpinning()
                    CustomStatic.FaceDetectionAccuracyLower=Pref.FaceDetectionAccuracyLower
                    CustomStatic.FaceDetectionAccuracyUpper=Pref.FaceDetectionAccuracyUpper
                    obj_temp=obj
                    //obj_temp.IsActiveUser=false
                    if(!obj_temp.IsActiveUser!!){
                        inactiveUserMsg("Login is inactive for ${obj_temp.user_name}. Please talk to administrator.")
                    }else if(AppUtils.isOnline(mContext)){
                        if(Pref.isAddAttendence || true){
                            if(obj_temp.isFaceRegistered!! || obj_temp.IsTeamAttenWithoutPhoto!!){
                                if(AppUtils.isOnline(mContext)){
                                    progress_wheel.spin()
                                    checkCurrentDayAttdUserWise()
                                    //prepareAddAttendanceInputParams()
                                }else{
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                                }
                                //getLocforDD()
                                //GetImageFromUrl().execute(obj_temp.face_image_link)
                            }else{
                                (mContext as DashboardActivity).showSnackMessage("Face Not Registered")
                            }
                        }else{
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.choose_attendance_type))
                        }
                    }
                    else{
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                    }
                }, 500)
            }

            override fun getUserInfoAttendReportOnLick(obj: UserListResponseModel) {
                (context as DashboardActivity).loadFragment(FragType.TeamAttendanceFragment, true, obj!!)
            }
        }, {
            it
        })

        rvUserDtls.adapter=adapter
    }

    private fun inactiveUserMsg(msgBody:String){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()+"!"
        dialogHeader.text = msgBody
        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()
    }

    private fun checkCurrentDayAttdUserWise(){
        val attendanceReq = AttendanceRequest()
        attendanceReq.user_id = obj_temp.user_id.toString()
        attendanceReq.session_token = Pref.session_token
        attendanceReq.start_date = AppUtils.getCurrentDateForCons()
        attendanceReq.end_date = AppUtils.getCurrentDateForCons()

        val repository = AttendanceRepositoryProvider.provideAttendanceRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getAttendanceList(attendanceReq)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val attendanceList = result as AttendanceResponse
                            if (attendanceList.status == "205"){
                                progress_wheel.stopSpinning()
                                try{
                                    CustomStatic.FaceUrl=obj_temp.face_image_link
                                }catch (ex:Exception){

                                }
                                getLocforDD()

                                //GetImageFromUrl().execute(obj_temp.face_image_link!!)
                            }else if(attendanceList.status == NetworkConstant.SUCCESS){
                                progress_wheel.stopSpinning()
                                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.team_attendance_submited))
                                //(mContext as DashboardActivity).showSnackMessage("Attendance submitted for toay.")
                                attedSubmitedDialog()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun attedSubmitedDialog(){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()+"!"
        //dialogHeader.text = "Attendance of "+obj_temp!!.user_name +" submitted for today."
        dialogHeader.text = "Visit for "+obj_temp!!.user_name+" already marked for today."
        voiceAttendanceMsg("Visit for "+obj_temp!!.user_name+" already marked for today.")
        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()
    }

    private fun getLocforDD() {
        if (AppUtils.isOnline(mContext) || true) {
            if (AppUtils.mLocation != null) {
                if (AppUtils.mLocation!!.accuracy <= Pref.gpsAccuracy.toInt()) {
                    if (AppUtils.mLocation!!.accuracy <= Pref.shopLocAccuracy.toFloat()) {
                        getNearyShopListDD(AppUtils.mLocation!!)
                    } else {
                        //getDDList(AppUtils.mLocation!!)
                        singleLocationDD()
                    }
                } else {
                    Timber.d("=====Inaccurate current location (Local Shop List)=====")
                    singleLocationDD()
                }
            } else {
                Timber.d("=====null location (Local Shop List)======")
                singleLocationDD()
            }
        } else
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
    }

    private var isGetLocation = -1
    private fun singleLocationDD() {
        progress_wheel.spin()
        isGetLocation = -1
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
                        if (isGetLocation == -1) {
                            isGetLocation = 0
                            if (location.accuracy > Pref.gpsAccuracy.toInt()) {
                                Timber.d("PhotoAttendanceFragment : loc.accuracy : "+location.accuracy.toString()+
                                        " Pref.gpsAccuracy : "+Pref.gpsAccuracy.toInt().toString())
                                (mContext as DashboardActivity).showSnackMessage("Unable to fetch accurate GPS data. Please try again.")
                                progress_wheel.stopSpinning()
                            } else
                                getNearyShopListDD(location)
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

    private fun getNearyShopListDD(location: Location) {
        progress_wheel.spin()
        var nearBy: Double = Pref.shopLocAccuracy.toDouble()
        var shop_id: String = ""
        var finalNearByShop: AddShopDBModelEntity = AddShopDBModelEntity()
        var finalNearByDD: AssignToDDEntity = AssignToDDEntity()

        val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
        val newList = java.util.ArrayList<AddShopDBModelEntity>()
        for (i in allShopList.indices) {
            newList.add(allShopList[i])
        }


        val allDDList = AppDatabase.getDBInstance()!!.ddListDao().getAll()
        val newDDList = java.util.ArrayList<AssignToDDEntity>()
        for (i in allDDList.indices) {
            newDDList.add(allDDList[i])
        }

        if (newDDList != null && newDDList.size > 0) {
            for (i in 0 until newDDList.size) {
                val ddLat: Double = newDDList[i].dd_latitude!!.toDouble()
                val ddLong: Double = newDDList[i].dd_longitude!!.toDouble()
                if (ddLat != null && ddLong != null) {
                    val ddLocation = Location("")
                    ddLocation.latitude = ddLat
                    ddLocation.longitude = ddLong
                    val isShopNearby = FTStorageUtils.checkShopPositionWithinRadious(location, ddLocation, LocationWizard.NEARBY_RADIUS)
                    if (isShopNearby) {
                        if ((location.distanceTo(ddLocation)) < nearBy) {
                            nearBy = location.distanceTo(ddLocation).toDouble()
                            finalNearByDD = newDDList[i]
                        }
                        //startDay(newList[i], location)
                        //break
                    }
                }
            }

        } else {
            //(mContext as DashboardActivity).showSnackMessage("No Shop Found")
        }

        //test code
        //prepareAddAttendanceInputParams()
        //return


        //finalNearByDD=newDDList[5]
        progress_wheel.stopSpinning()
        if (finalNearByDD.dd_id != null && finalNearByDD.dd_id!!.length > 1) {
            Timber.d("PhotoAtend nearby found onclick")
            if(obj_temp.IsTeamAttenWithoutPhoto!!){
                prepareAddAttendanceInputParams()
            }else{
                GetImageFromUrl().execute(obj_temp.face_image_link)
            }
            //prepareAddAttendanceInputParams()
        }  else {
            progress_wheel.stopSpinning()
            val simpleDialog = Dialog(mContext)
            simpleDialog.setCancelable(false)
            simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialog.setContentView(R.layout.dialog_message)
            val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
            val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
            dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()+"!"
            dialogHeader.text = "No nearby Shop/Point found..."
            val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
            dialogYes.setOnClickListener({ view ->
                simpleDialog.cancel()
            })
            simpleDialog.show()
        }


    }

    ///////
    inner class GetImageFromUrl : AsyncTask<String?, Void?, Bitmap?>() {
        fun GetImageFromUrl() {
            //this.imageView = img;
        }
        override fun doInBackground(vararg url: String?): Bitmap {
            var bitmappppx: Bitmap? = null
            val stringUrl = url[0]
            bitmappppx = null
            val inputStream: InputStream
            try {
                inputStream = URL(stringUrl).openStream()
                bitmappppx = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bitmappppx!!
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            println("reg_face - registerFace called"+AppUtils.getCurrentDateTime());
            registerFace(getResizedBitmap(result!!,240,240))
            //registerFace(result)
        }
    }

    fun getResizedBitmap(bm: Bitmap, newHeight: Int, newWidth: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height

        // create a matrix for the manipulation
        val matrix = Matrix()

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight)

        // recreate the new Bitmap
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
    }

    fun faceDetectorSetUp(){
        try {
            FaceStartActivity.detector = TFLiteObjectDetectionAPIModel.create(
                    mContext.getAssets(),
                    TF_OD_API_MODEL_FILE,
                    TF_OD_API_LABELS_FILE,
                    TF_OD_API_INPUT_SIZE,
                    TF_OD_API_IS_QUANTIZED)
            //cropSize = TF_OD_API_INPUT_SIZE;
        } catch (e: IOException) {
            e.printStackTrace()
            //LOGGER.e(e, "Exception initializing classifier!");
            val toast = Toast.makeText(mContext, "Classifier could not be initialized", Toast.LENGTH_SHORT)
            toast.show()
            //finish()
        }
        val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                //.setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                //.setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
                .build()

        val detector = FaceDetection.getClient(options)

        faceDetector = detector
    }

    private fun registerFace(mBitmap: Bitmap?) {
        //BaseActivity.isApiInitiated=false
        println("reg_face - add_attendance_registerFace"+AppUtils.getCurrentDateTime());
        try {
            if (mBitmap == null) {
                //Toast.makeText(this, "No File", Toast.LENGTH_SHORT).show()
                return
            }
            progress_wheel.spin()
            //ivFace.setImageBitmap(mBitmap)
            previewWidth = mBitmap.width
            previewHeight = mBitmap.height
            rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
            portraitBmp = mBitmap
            val image = InputImage.fromBitmap(mBitmap, 0)
            faceBmp = Bitmap.createBitmap(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, Bitmap.Config.ARGB_8888)
            faceDetector?.process(image)?.addOnSuccessListener(OnSuccessListener<List<Face>> { faces ->
                if (faces.size == 0) {
                    progress_wheel.stopSpinning()
                    println("reg_face - add_attendance_registerFace no face detected"+AppUtils.getCurrentDateTime());
                    return@OnSuccessListener
                }
                Handler().post {
                    object : Thread() {
                        override fun run() {
                            //action
                            progress_wheel.stopSpinning()
                            println("reg_face - add_attendance_registerFace face detected"+AppUtils.getCurrentDateTime());
                            onFacesDetected(1, faces, true) //no need to add currtime
                        }
                    }.start()
                }
            })



        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            progress_wheel.stopSpinning()
        }
    }

    fun onFacesDetected(currTimestamp: Long, faces: List<Face>, add: Boolean) {
        progress_wheel.spin()
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.0f
        val mappedRecognitions: MutableList<SimilarityClassifier.Recognition> = LinkedList()


        //final List<Classifier.Recognition> results = new ArrayList<>();

        // Note this can be done only once
        val sourceW = rgbFrameBitmap!!.width
        val sourceH = rgbFrameBitmap!!.height
        val targetW = portraitBmp!!.width
        val targetH = portraitBmp!!.height
        val transform = createTransform(
                sourceW,
                sourceH,
                targetW,
                targetH,
                90)
        val mutableBitmap = portraitBmp!!.copy(Bitmap.Config.ARGB_8888, true)
        val cv = Canvas(mutableBitmap)

        // draws the original image in portrait mode.
        cv.drawBitmap(rgbFrameBitmap!!, transform!!, null)
        val cvFace = Canvas(faceBmp!!)
        val saved = false
        for (face in faces) {
            //results = detector.recognizeImage(croppedBitmap);
            val boundingBox = RectF(face.boundingBox)

            //final boolean goodConfidence = result.getConfidence() >= minimumConfidence;
            val goodConfidence = true //face.get;
            if (boundingBox != null && goodConfidence) {

                // maps crop coordinates to original
                cropToFrameTransform?.mapRect(boundingBox)

                // maps original coordinates to portrait coordinates
                val faceBB = RectF(boundingBox)
                transform.mapRect(faceBB)

                // translates portrait to origin and scales to fit input inference size
                //cv.drawRect(faceBB, paint);
                val sx = TF_OD_API_INPUT_SIZE.toFloat() / faceBB.width()
                val sy = TF_OD_API_INPUT_SIZE.toFloat() / faceBB.height()
                val matrix = Matrix()
                matrix.postTranslate(-faceBB.left, -faceBB.top)
                matrix.postScale(sx, sy)
                cvFace.drawBitmap(portraitBmp!!, matrix, null)

                //canvas.drawRect(faceBB, paint);
                var label = ""
                var confidence = -1f
                var color = Color.BLUE
                var extra: Any? = null
                var crop: Bitmap? = null
                if (add) {
                    try {
                        crop = Bitmap.createBitmap(portraitBmp!!,
                                faceBB.left.toInt(),
                                faceBB.top.toInt(),
                                faceBB.width().toInt(),
                                faceBB.height().toInt())
                    } catch (eon: java.lang.Exception) {
                        //runOnUiThread(Runnable { Toast.makeText(mContext, "Failed to detect", Toast.LENGTH_LONG) })
                    }
                }
                val startTime = SystemClock.uptimeMillis()
                val resultsAux = FaceStartActivity.detector.recognizeImage(faceBmp, add)
                val lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime
                if (resultsAux.size > 0) {
                    val result = resultsAux[0]
                    extra = result.extra
                    //          Object extra = result.getExtra();
//          if (extra != null) {
//            LOGGER.i("embeeding retrieved " + extra.toString());
//          }
                    val conf = result.distance
                    if (conf < 1.0f) {
                        confidence = conf
                        label = result.title
                        color = if (result.id == "0") {
                            Color.GREEN
                        } else {
                            Color.RED
                        }
                    }
                }
                val flip = Matrix()
                flip.postScale(1f, -1f, previewWidth / 2.0f, previewHeight / 2.0f)

                //flip.postScale(1, -1, targetW / 2.0f, targetH / 2.0f);
                flip.mapRect(boundingBox)
                val result = SimilarityClassifier.Recognition(
                        "0", label, confidence, boundingBox)
                result.color = color
                result.location = boundingBox
                result.extra = extra
                result.crop = crop
                mappedRecognitions.add(result)
            }
        }

        //    if (saved) {
//      lastSaved = System.currentTimeMillis();
//    }

        CustomStatic.IsCameraFacingFromTeamAttdCametaStatus=true
        CustomStatic.IsCameraFacingFromTeamAttd=true
        Log.e("xc", "startabc" )
        val rec = mappedRecognitions[0]
        FaceStartActivity.detector.register("", rec)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(mContext, DetectorActivity::class.java)
            startActivityForResult(intent, 172)
        }, 200)


//        startActivity(new Intent(this,DetectorActivity.class));
//        finish();

        // detector.register("Sakil", rec);
        /*   runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivFace.setImageBitmap(rec.getCrop());
                //showAddFaceDialog(rec);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.image_edit_dialog, null);
                ImageView ivFace = dialogLayout.findViewById(R.id.dlg_image);
                TextView tvTitle = dialogLayout.findViewById(R.id.dlg_title);
                EditText etName = dialogLayout.findViewById(R.id.dlg_input);

                tvTitle.setText("Register Your Face");
                ivFace.setImageBitmap(rec.getCrop());
                etName.setHint("Please tell your name");
                detector.register("sam", rec); //for register a face

                //button.setPressed(true);
                //button.performClick();
            }

        });*/

        // updateResults(currTimestamp, mappedRecognitions);
    }

    fun createTransform(srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int, applyRotation: Int): Matrix? {
        val matrix = Matrix()
        if (applyRotation != 0) {
            if (applyRotation % 90 != 0) {
                // LOGGER.w("Rotation of %d % 90 != 0", applyRotation);
            }

            // Translate so center of image is at origin.
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f)

            // Rotate around origin.
            matrix.postRotate(applyRotation.toFloat())
        }

//        // Account for the already applied rotation, if any, and then determine how
//        // much scaling is needed for each axis.
//        final boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;
//        final int inWidth = transpose ? srcHeight : srcWidth;
//        final int inHeight = transpose ? srcWidth : srcHeight;
        if (applyRotation != 0) {

            // Translate back from origin centered reference to destination frame.
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f)
        }
        return matrix
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 172) {
            progress_wheel.stopSpinning()
            if (resultCode == Activity.RESULT_OK) {
                prepareAddAttendanceInputParams()
            }
        }
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

    private fun prepareAddAttendanceInputParams() {
        disableScreen()
        progress_wheel.stopSpinning()

        try {
            addAttendenceModel.session_token = Pref.session_token!!
            addAttendenceModel.user_id = obj_temp!!.user_id.toString()
            addAttendenceModel.is_on_leave = "false"

            if (true) {
                if (TextUtils.isEmpty(Pref.current_latitude))
                    addAttendenceModel.work_lat = "0.0"
                else
                    addAttendenceModel.work_lat = Pref.current_latitude

                if (TextUtils.isEmpty(Pref.current_longitude))
                    addAttendenceModel.work_long = "0.0"
                else
                    addAttendenceModel.work_long = Pref.current_longitude

                if (TextUtils.isEmpty(Pref.current_latitude))
                    addAttendenceModel.work_address = ""
                else
                    addAttendenceModel.work_address = LocationWizard.getLocationName(mContext, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())

                addAttendenceModel.work_desc = ""
                //addAttendenceModel.work_date_time = AppUtils.getCurrentDateTime12FormatToAttr(tv_current_date_time.text.toString().trim())
                addAttendenceModel.work_lat = Pref.current_latitude
                addAttendenceModel.work_type = "9"

                addAttendenceModel.order_taken="0"
                addAttendenceModel.collection_taken="0"
                addAttendenceModel.new_shop_visit="0"
                addAttendenceModel.revisit_shop="0"
                addAttendenceModel.route=""


                if (!TextUtils.isEmpty(Pref.profile_state))
                    addAttendenceModel.state_id = Pref.profile_state


                if (!TextUtils.isEmpty(Pref.current_latitude) && !TextUtils.isEmpty(Pref.current_longitude)) {
                    Pref.source_latitude = Pref.current_latitude
                    Pref.source_longitude = Pref.current_longitude
                }

               var stateList = AppUtils.loadSharedPreferencesStateList(mContext)
                if (stateList != null && stateList!!.size > 0) {

                    val primaryList = ArrayList<PrimaryValueDataModel>()

                    for (i in PrimaryValueAdapter.primaryValueList.indices) {
                        val primaryValue = PrimaryValueDataModel()
                        primaryValue.id = stateList?.get(i)?.id!!
                        primaryValue.primary_value = PrimaryValueAdapter.primaryValueList[i]
                        primaryList.add(primaryValue)
                    }

                    addAttendenceModel.primary_value_list = primaryList
                }

                if (Pref.isDDFieldEnabled) {
                    addAttendenceModel.distributor_name = ""
                    addAttendenceModel.market_worked = ""
                }

            }

            addAttendenceModel.work_date_time =  AppUtils.getCurrentISODateTime()//AppUtils.getCurrentDateTime12FormatToAttr(AppUtils.getCurrentDateTime12Format())
            val addAttendenceTime =  /*"06:05 PM"*/ AppUtils.getCurrentTimeWithMeredian()
            addAttendenceModel.add_attendence_time = addAttendenceTime
            addAttendenceModel.from_id = ""
            addAttendenceModel.to_id = ""

            addAttendenceModel.distance = ""



            try{
                dialog_yes_no_headerTVProcess.text = AppUtils.hiFirstNameText()!!+"!"
            }catch (ex:Exception){
                dialog_yes_no_headerTVProcess.text = "Hi"+"!"
            }

            dialogHeaderProcess.text = "Submitting Visit, Please wait...."
            val dialogYes = simpleDialogProcess.findViewById(R.id.tv_message_ok) as AppCustomTextView
            dialogYes.setOnClickListener {
                simpleDialogProcess.dismiss()
            }
            simpleDialogProcess.show()

            doAttendance()

        } catch (e: Exception) {
            enableScreen()
            e.printStackTrace()
        }
    }

    private fun doAttendance(){
        Timber.d("PhotoAttendance : doAttendance " +AppUtils.getCurrentDateTime())
        Timber.d("PhotoAttendance : doAttendance :add_attendence_time " +addAttendenceModel.add_attendence_time)
        //Timber.d("PhotoAttendance : doAttendance :work_date_time " +addAttendenceModel.work_date_time)
        Timber.d("PhotoAttendance : doAttendance :work_lat : work_long " +addAttendenceModel.work_lat + " "+addAttendenceModel.work_long)
        Timber.d("PhotoAttendance : doAttendance :user_id " +addAttendenceModel.user_id)

        BaseActivity.isApiInitiated = true
        val repository = AddAttendenceRepoProvider.addAttendenceRepo()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.addAttendence(addAttendenceModel)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as BaseResponse
                            Timber.d("VISIT AddAttendance Response Code========> " + response.status)
                            Timber.d("VISIT AddAttendance Response Msg=========> " + response.message)
                            enableScreen()
                            if (response.status == NetworkConstant.SUCCESS) {
                                BaseActivity.isApiInitiated = false
                                if(Pref.user_id!!.equals(addAttendenceModel.user_id)){
                                    Pref.isAddAttendence=true
                                }
                                //showAttendSuccessMsg()

                                Handler().postDelayed(Runnable {
                                    //getLocforStart(obj_temp!!.user_id.toString())

                                    var locc : Location = Location("")
                                    locc.latitude=Pref.current_latitude.toDouble()
                                    locc.longitude=Pref.current_longitude.toDouble()
                                    calllogoutApi(locc,obj_temp!!.user_id.toString())
                                }, 1500)

                            } else {
                                BaseActivity.isApiInitiated = false
                                //(mContext as DashboardActivity).showSnackMessage(response.message!!)
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.try_again_later))
                            }
                        }, { error ->

                            //delete attendance-daystartend
                            apiCallOnClearAttenReject(obj_temp!!.user_id.toString())

                            Timber.d("VISIT AddAttendance team Response Msg=========> " + error.message)
                            enableScreen()
                            BaseActivity.isApiInitiated = false
                            progress_wheel.stopSpinning()
                            //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun showAttendSuccessMsg(){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()!!+"!"
        dialogHeader.text = "Visit successfully marked for "+obj_temp.user_name+". Thanks."
        voiceAttendanceMsg("Visit successfully marked for "+obj_temp.user_name)
        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            callUSerListApi()
            //(mContext as DashboardActivity).loadFragment(FragType.PhotoAttendanceFragment, false, "")
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

    fun getLocforStart(usrID:String) {
        if (AppUtils.isOnline(mContext)) {
            if(AppUtils.mLocation!=null && false){
                startDay(AppUtils.mLocation!!,usrID)
            }else{
                var locNw = Location("")
                locNw.latitude = Pref.current_latitude.toDouble()
                locNw.longitude = Pref.current_longitude.toDouble()
                AppUtils.mLocation = locNw
                startDay(AppUtils.mLocation!!,usrID)
            }
        }else{
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
        }

        /*if (AppUtils.isOnline(mContext)) {
            if (AppUtils.mLocation != null) {
                if (AppUtils.mLocation!!.accuracy <= Pref.gpsAccuracy.toInt()) {
                    if (AppUtils.mLocation!!.accuracy <= Pref.shopLocAccuracy.toFloat()) {
                        startDay(AppUtils.mLocation!!,usrID)
                    } else {
                        //getDDList(AppUtils.mLocation!!)
                        singleLocation(usrID)
                    }
                } else {
                    Timber.d("=====Inaccurate current location (Local Shop List)=====")
                    singleLocation(usrID)
                }
            } else {
                Timber.d("=====null location (Local Shop List)======")
                singleLocation(usrID)
            }
        } else
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))*/

    }

    private fun singleLocation(usrID:String) {
        progress_wheel.spin()
        isGetLocation = -1
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
                                Timber.d("PhotoAttendanceFragment : loc.accuracy : "+location.accuracy.toString()+
                                        " Pref.gpsAccuracy : "+Pref.gpsAccuracy.toInt().toString())
                                (mContext as DashboardActivity).showSnackMessage("Unable to fetch accurate GPS data. Please try again.")
                                progress_wheel.stopSpinning()
                            } else
                                startDay(location,usrID)
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

    fun startDay(loc: Location,usrID:String) {
        try{
            Timber.d("PhotoAtendFrag : startDay  ========> " +AppUtils.getCurrentDateTime())
        }catch (ex:Exception){ex.printStackTrace()}

        disableScreen()
        progress_wheel.spin()
        try {
            var dayst: DaystartDayendRequest = DaystartDayendRequest()
            dayst.user_id = usrID
            dayst.session_token = Pref.session_token
            //dayst.date = AppUtils.getCurrentDateTime()
            dayst.date = AppUtils.getCurrentDateTimeNew()
            if(dayst.date.equals("") || dayst.date==null){
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                return
            }
            if (!TextUtils.isEmpty(loc.latitude.toString()) && !TextUtils.isEmpty(loc.longitude.toString())) {
            dayst.location_name = LocationWizard.getNewLocationName(mContext, loc.latitude, loc.longitude)
            }else{
                dayst.location_name=""
            }
            if (!TextUtils.isEmpty(loc.latitude.toString())){
                    dayst.latitude = loc.latitude.toString()
            }else{
                dayst.latitude = ""
            }
            if (!TextUtils.isEmpty(loc.longitude.toString())){
                dayst.longitude = loc.longitude.toString()
            }else{
                dayst.longitude = ""
            }
            dayst.IsDDvistedOnceByDay = "0"
            dayst.visit_distributor_date_time = ""
            dayst.visit_distributor_id = ""
            dayst.visit_distributor_name = ""

            dayst.shop_type = ""
            dayst.shop_id = ""
            dayst.isStart = "1"
            dayst.isEnd = "0"
            dayst.sale_Value = "0.0"
            dayst.remarks = ""

            var addr=""
            try{
                addr=LocationWizard.getAdressFromLatlng(mContext, loc.latitude, loc.longitude)
            }catch (ex:Exception){
                addr=""
            }
            try{
                Timber.d("PhotoAtendFrag : startDay : getCurrentDateTime() " +AppUtils.getCurrentDateTime())
                Timber.d("PhotoAtendFrag : startDay : getCurrentDateTimeNew() " +AppUtils.getCurrentDateTimeNew())
                Timber.d("PhotoAtendFrag : startDay : user_id ${dayst.user_id}")
                Timber.d("PhotoAtendFrag : startDay : date ${dayst.date}")
                Timber.d("PhotoAtendFrag : startDay : locationName ${dayst.location_name}")
                Timber.d("PhotoAtendFrag : startDay : address $addr")
                Timber.d("PhotoAtendFrag : startDay :  lat : " +dayst.latitude.toString() + " long : "+dayst.longitude)
            }catch (ex:Exception){
                ex.printStackTrace()
            }

            val repository = DayStartEndRepoProvider.dayStartRepositiry()
            BaseActivity.compositeDisposable.add(
                    repository.dayStart(dayst)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                Timber.d("DayStart (PhotoAttendanceFragment): DayStarted Success status " + result.status + " usr_id : "+usrID+" lat "+
                                        loc.latitude.toString()+ " long "+ loc.longitude.toString()+" addr "+addr+" "+AppUtils.getCurrentDateTime() )
                                progress_wheel.stopSpinning()
                                enableScreen()
                                val response = result as BaseResponse
                                if (response.status == NetworkConstant.SUCCESS) {
                                    Handler().postDelayed(Runnable {
                                        endDay(loc,usrID)
                                    }, 1500)

                                }
                            }, { error ->
                                enableScreen()

                                //delete attendance-daystartend
                                apiCallOnClearAttenReject(obj_temp!!.user_id.toString())

                                if (error == null) {
                                    Timber.d("DayStart (PhotoAttendanceFragment) : ERROR " + " usr_id : "+usrID+" UNEXPECTED ERROR IN DayStart API "+AppUtils.getCurrentDateTime())
                                } else {
                                    Timber.d("DayStart (PhotoAttendanceFragment) : ERROR " +" usr_id : "+usrID+ " "+error.localizedMessage+" "+AppUtils.getCurrentDateTime())
                                    error.printStackTrace()
                                }
                                progress_wheel.stopSpinning()
                                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            })
            )

        } catch (ex: Exception) {
            //delete attendance-daystartend
            apiCallOnClearAttenReject(obj_temp!!.user_id.toString())

            Timber.d("DayStart (PhotoAttendanceFragment) : exception " + " usr_id : "+usrID+" UNEXPECTED ERROR IN DayStart API "+AppUtils.getCurrentDateTime())
            enableScreen()
            ex.printStackTrace()
            progress_wheel.stopSpinning()
            //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }

    }

    fun endDay(loc: Location,usrID:String) {
        disableScreen()
        progress_wheel.spin()
        try {
            var dayst: DaystartDayendRequest = DaystartDayendRequest()
            dayst.user_id = usrID
            dayst.session_token = Pref.session_token
            //dayst.date = AppUtils.getCurrentDateTime()
            dayst.date = AppUtils.getCurrentDateTimeNew()
            dayst.location_name = LocationWizard.getNewLocationName(mContext, loc.latitude, loc.longitude)
            dayst.latitude = loc.latitude.toString()
            dayst.longitude = loc.longitude.toString()
            dayst.IsDDvistedOnceByDay = "0"
            dayst.visit_distributor_date_time = ""
            dayst.visit_distributor_id = ""
            dayst.visit_distributor_name = ""

            dayst.shop_type = ""
            dayst.shop_id = ""
            dayst.isStart = "0"
            dayst.isEnd = "1"
            dayst.sale_Value = "0.0"
            dayst.remarks = ""

            var addr=""
            try{
                addr=LocationWizard.getAdressFromLatlng(mContext, loc.latitude, loc.longitude)
            }catch (ex:Exception){
                addr=""
            }
            try{
                Timber.d("PhotoAtendFrag : endDay :  AppUtils.getCurrentDateTime() " +AppUtils.getCurrentDateTime())
                Timber.d("PhotoAtendFrag : endDay :  AppUtils.getCurrentDateTimeNew() " +AppUtils.getCurrentDateTimeNew())

                Timber.d("PhotoAtendFrag : endDay : user_id ${dayst.user_id}")
                Timber.d("PhotoAtendFrag : endDay : date ${dayst.date}")
                Timber.d("PhotoAtendFrag : endDay : locationName ${dayst.location_name}")
                Timber.d("PhotoAtendFrag : endDay : address $addr")
                Timber.d("PhotoAtendFrag : endDay :  lat : " +dayst.latitude.toString() + " long : "+dayst.longitude)
            }catch (ex:Exception){
                ex.printStackTrace()
            }

            val repository = DayStartEndRepoProvider.dayStartRepositiry()
            BaseActivity.compositeDisposable.add(
                    repository.dayStart(dayst)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                Timber.d("DayEnd (PhotoAttendanceFragment): DayStarted Success status " + result.status + " usr_id : "+usrID+" lat "+
                                        loc.latitude.toString()+ " long "+ loc.longitude.toString()+" addr "+addr+" "+AppUtils.getCurrentDateTime())
                                progress_wheel.stopSpinning()
                                enableScreen()
                                val response = result as BaseResponse
                                if (response.status == NetworkConstant.SUCCESS) {
                                    Handler().postDelayed(Runnable {
                                        calllogoutApi(loc,usrID)
                                    }, 1500)
                                }
                            }, { error ->
                                enableScreen()

                                //delete attendance-daystartend
                                apiCallOnClearAttenReject(obj_temp!!.user_id.toString())

                                if (error == null) {
                                    Timber.d("DayEnd (PhotoAttendanceFragment) : ERROR " + " usr_id : "+usrID+" UNEXPECTED ERROR IN DayStart API "+AppUtils.getCurrentDateTime())
                                } else {
                                    Timber.d("DayEnd (PhotoAttendanceFragment) : ERROR " +" usr_id : "+usrID+ " "+error.localizedMessage+" "+AppUtils.getCurrentDateTime())
                                    error.printStackTrace()
                                }
                                progress_wheel.stopSpinning()
                                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            })
            )

        } catch (ex: Exception) {
            Timber.d("DayEnd (PhotoAttendanceFragment) : ERRORRR " + " usr_id : "+usrID+" err $ex "+AppUtils.getCurrentDateTime())
            enableScreen()

            //delete attendance-daystartend
            apiCallOnClearAttenReject(obj_temp!!.user_id.toString())

            ex.printStackTrace()
            progress_wheel.stopSpinning()
            //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }

    }

    private fun calllogoutApi(loc: Location,usrID:String) {
        disableScreen()
        var distance = 0.0
        var location = ""

        if (loc.latitude.toString() != "0.0" && loc.longitude.toString() != "0.0") {
            location = LocationWizard.getAdressFromLatlng(mContext, loc.latitude.toDouble(), loc.longitude.toDouble())
            if (location.contains("http"))
                location = "Unknown"
        }
        val repository = LogoutRepositoryProvider.provideLogoutRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                //repository.logout(usrID, Pref.session_token!!, loc.latitude.toString(), loc.longitude.toString(), AppUtils.getCurrentDateTime(), distance.toString(), "0", location)
                repository.logout(usrID, Pref.session_token!!, loc.latitude.toString(), loc.longitude.toString(), AppUtils.getCurrentDateTimeNew(), distance.toString(), "0", location)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val logoutResponse = result as BaseResponse
                            Timber.d("PhotoAttendanceFragment LOGOUT : " + "RESPONSE : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + usrID + ",MESSAGE : " + logoutResponse.message)
                            enableScreen()
                            simpleDialogProcess.dismiss()
                            if (logoutResponse.status == NetworkConstant.SUCCESS) {
                                (mContext as DashboardActivity).isChangedPassword = false
                                Pref.tempDistance = "0.0"
                                BaseActivity.isApiInitiated = false
                                showAttendSuccessMsg()

                            } else if (logoutResponse.status == NetworkConstant.SESSION_MISMATCH) {
                                //clearData()
                                (mContext as DashboardActivity).isChangedPassword = false
                                /*startActivity(Intent(mContext, LoginActivity::class.java))
                                (mContext as DashboardActivity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                                (mContext as DashboardActivity).finishAffinity()*/
                            } else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage("Failed to logout")

                                if ((mContext as DashboardActivity).isChangedPassword) {
                                    (mContext as DashboardActivity).isChangedPassword = false
                                    (mContext as DashboardActivity).onBackPressed()
                                }
                            }
                            BaseActivity.isApiInitiated = false

                        }, { error ->
                            enableScreen()
                            BaseActivity.isApiInitiated = false
                            progress_wheel.stopSpinning()
                            simpleDialogProcess.dismiss()
                            error.printStackTrace()
                            Timber.d("PhotoAttendanceFragment LOGOUT : " + "RESPONSE ERROR: " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + usrID + ",MESSAGE : " + error.localizedMessage)
                            //(mContext as DashboardActivity).showSnackMessage(error.localizedMessage)

                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.try_again_later))

                            //delete attendance-daystartend
                            apiCallOnClearAttenReject(obj_temp!!.user_id.toString())

                            if ((mContext as DashboardActivity).isChangedPassword) {
                                (mContext as DashboardActivity).isChangedPassword = false
                                (mContext as DashboardActivity).onBackPressed()
                            }

                        })
        )
    }

    private fun disableScreen(){
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private fun enableScreen(){
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private fun apiCallOnClearAttenReject(usrID:String) {  // clearing leave if isOnLeave is true

        simpleDialogProcess.dismiss()
        /*(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        return*/

        var req : clearAttendanceonRejectReqModelRejectReqModel = clearAttendanceonRejectReqModelRejectReqModel()
        req.user_id=usrID
        req.leave_apply_date=AppUtils.getCurrentDateForShopActi()
        req.isOnLeave=true
        req.IsLeaveDelete = "1"

        val repository = LeaveTypeRepoProvider.leaveTypeListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.clearAttendanceonRejectclick(req)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as BaseResponse
                    progress_wheel.stopSpinning()
                    if (response.status == NetworkConstant.SUCCESS) {
                        Handler().postDelayed(Runnable {
                            apiCallOnClearAttenReject1(usrID)
                        }, 500)

                    }
                }, { error ->
                    progress_wheel.stopSpinning()
                    //(mContext as DashboardActivity).showSnackMessage("ERROR")
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.try_again_later))
                })
        )
    }

    private fun apiCallOnClearAttenReject1(usrID:String) { // clearing attendance if isOnLeave is false
        var req : clearAttendanceonRejectReqModelRejectReqModel = clearAttendanceonRejectReqModelRejectReqModel()
        req.user_id=usrID
        req.leave_apply_date=AppUtils.getCurrentDateForShopActi()
        req.isOnLeave=false
        req.IsLeaveDelete = "1"

        val repository = LeaveTypeRepoProvider.leaveTypeListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.clearAttendanceonRejectclick(req)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as BaseResponse
                    progress_wheel.stopSpinning()
                    if (response.status == NetworkConstant.SUCCESS) {
                        Handler().postDelayed(Runnable {
                            deleteDaystartEnd(usrID)
                        }, 500)

                    }
                }, { error ->
                    progress_wheel.stopSpinning()
                    //(mContext as DashboardActivity).showSnackMessage("ERROR")
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.try_again_later))
                })
        )
    }

    fun deleteDaystartEnd(usrID:String) {
        progress_wheel.spin()
        try {
            val repository = DayStartEndRepoProvider.dayStartRepositiry()
            BaseActivity.compositeDisposable.add(
                repository.daystartendDelete(Pref.session_token.toString(),usrID,AppUtils.getCurrentDateForShopActi(),"1","1")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        progress_wheel.stopSpinning()
                        enableScreen()
                        val response = result as BaseResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            Handler().postDelayed(Runnable {
                                tryAgainMsg()
                            }, 500)

                        }
                    }, { error ->
                        enableScreen()
                        if (error == null) {
                            Timber.d("DayStart (PhotoAttendanceFragment) : ERROR " + " usr_id : "+usrID+" UNEXPECTED ERROR IN DayStart API "+AppUtils.getCurrentDateTime())
                        } else {
                            Timber.d("DayStart (PhotoAttendanceFragment) : ERROR " +" usr_id : "+usrID+ " "+error.localizedMessage+" "+AppUtils.getCurrentDateTime())
                            error.printStackTrace()
                        }
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )

        } catch (ex: Exception) {
            Timber.d("DayStart (PhotoAttendanceFragment) : exception " + " usr_id : "+usrID+" UNEXPECTED ERROR IN DayStart API "+AppUtils.getCurrentDateTime())
            enableScreen()
            ex.printStackTrace()
            progress_wheel.stopSpinning()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }
    }

    fun tryAgainMsg(){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()!!+"!"
        dialogHeader.text = "Please try Again."
        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            callUSerListApi()
            //(mContext as DashboardActivity).loadFragment(FragType.PhotoAttendanceFragment, false, "")
        })
        simpleDialog.show()
    }


}