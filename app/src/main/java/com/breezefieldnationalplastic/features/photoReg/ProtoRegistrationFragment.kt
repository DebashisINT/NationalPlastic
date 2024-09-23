package com.breezefieldnationalplastic.features.photoReg

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.breezefieldnationalplastic.MySingleton
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.MaterialSearchView
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.NewFileUtils
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.SearchListener
import com.breezefieldnationalplastic.app.domain.VisitRevisitWhatsappStatus
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.uiaction.IntentActionable
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.FTStorageUtils
import com.breezefieldnationalplastic.app.utils.PermissionUtils
import com.breezefieldnationalplastic.app.utils.ProcessImageUtils_v1
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.myjobs.model.WIPImageSubmit
import com.breezefieldnationalplastic.features.photoReg.adapter.AdapterUserList
import com.breezefieldnationalplastic.features.photoReg.adapter.PhotoRegUserListner
import com.breezefieldnationalplastic.features.photoReg.api.GetUserListPhotoRegProvider
import com.breezefieldnationalplastic.features.photoReg.model.AadhaarSubmitData
import com.breezefieldnationalplastic.features.photoReg.model.DeleteUserPicResponse
import com.breezefieldnationalplastic.features.photoReg.model.GetAllAadhaarResponse
import com.breezefieldnationalplastic.features.photoReg.model.GetUserListResponse
import com.breezefieldnationalplastic.features.photoReg.model.UpdateUserNameModel
import com.breezefieldnationalplastic.features.photoReg.model.UpdateUserNameResponse
import com.breezefieldnationalplastic.features.photoReg.model.UserListResponseModel
import com.breezefieldnationalplastic.features.photoReg.present.UpdateDSTypeStatusDialog
import com.breezefieldnationalplastic.features.reimbursement.presentation.FullImageDialog
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.gson.JsonParser
import com.itextpdf.text.pdf.PdfName.XML
import com.squareup.picasso.Cache
import com.squareup.picasso.LruCache
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.RequestTransformer
import com.themechangeapp.pickimage.PermissionHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.util.Locale


class ProtoRegistrationFragment : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var mRv_userList: RecyclerView
    private lateinit var progress_wheel: com.pnikosis.materialishprogress.ProgressWheel
    var userList: ArrayList<UserListResponseModel> = ArrayList()
    var userList_temp: ArrayList<UserListResponseModel> = ArrayList()
    private var adapter: AdapterUserList? = null
    private var str_aadhaarNo: String = ""

    private lateinit var et_attachment: AppCustomEditText
    private lateinit var et_photo: AppCustomEditText
    private lateinit var tv_cust_no_frag_reg: TextView

    private var isAttachment = false
    private var dataPath = ""
    private var imagePath = ""

    private var aadhaarList: ArrayList<String> = ArrayList()

    private data class AadhaarListUser(var user_id: String, var RegisteredAadhaarNo: String)
    private var aadhaarListUserList:ArrayList<AadhaarListUser> = ArrayList()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        var user_uid: String = ""
        fun getInstance(objects: Any): ProtoRegistrationFragment {
            val protoRegistrationFragment = ProtoRegistrationFragment()
            if (!TextUtils.isEmpty(objects.toString())) {
                user_uid = objects.toString()
            }
            return protoRegistrationFragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_photo_registration, container, false)
        initView(view)



        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    userList?.let {
                        adapter?.refreshList(it)
                        //tv_cust_no_frag_reg.text = "Total customer(s) : " + it.size
                    }
                } else {
                    adapter?.filter?.filter(query)
                    //tv_cust_no_frag_reg.text = "Total customer(s) : "+ CustomStatic.PhotoRegUserSearchCount.toString()

                }
            }
        })

        // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 start
        (mContext as DashboardActivity).searchView.setVoiceIcon(R.drawable.ic_mic)
        (mContext as DashboardActivity).searchView.setOnVoiceClickedListener({ startVoiceInput() })
        // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 end

        return view
    }

    private fun initView(view: View) {

        //extra code
        /*var chart:PieChartView = view.findViewById(R.id.chart)
        val pieData: ArrayList<SliceValue> = ArrayList()
        pieData.add(SliceValue(15f, Color.BLUE).setLabel("A"))
        pieData.add(SliceValue(25f, Color.GRAY).setLabel("B"))
        pieData.add(SliceValue(10f, Color.RED).setLabel("C"))
        pieData.add(SliceValue(60f, Color.MAGENTA).setLabel("D : 60%"))
        val pieChartData = PieChartData(pieData)
        pieChartData.setHasLabels(true)
        pieChartData.valueLabelTextSize = 15
        pieChartData.setHasCenterCircle(true)
        pieChartData.centerText1 = "Center"
        pieChartData.slicesSpacing = 5
        chart.setPieChartData(pieChartData);*/


        et_attachment = view.findViewById(R.id.et_attachment)
        et_photo = view.findViewById(R.id.et_photo)
        mRv_userList = view.findViewById(R.id.rv_frag_photo_reg)
        progress_wheel = view.findViewById(R.id.progress_wheel)
        tv_cust_no_frag_reg = view.findViewById(R.id.tv_cust_no_frag_reg)

        tv_cust_no_frag_reg.visibility=View.GONE

        mRv_userList.layoutManager = LinearLayoutManager(mContext)

        initPermissionCheck()
        progress_wheel.spin()
        Handler(Looper.getMainLooper()).postDelayed({
            callUSerListApi()
        }, 3000)

        //startVoiceInput()
    }
    // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 start



    private fun startVoiceInput() {
        val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi")
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.ENGLISH)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?")
        try {
            startActivityForResult(intent, 7009)
        } catch (a: ActivityNotFoundException) {
            a.printStackTrace()
        }
    }
    // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 end

    fun loadUpdateList(){
        Handler(Looper.getMainLooper()).postDelayed({
            callUSerListApi()
        }, 300)
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
                var grant = true
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

    fun onRequestPermission(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun callUSerListApi() {
        userList.clear()
        aadhaarList.clear()
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
                                        /*for(j in 0..userList.size-1){
                                            if(userList.get(j).IsAadhaarRegistered!!){
                                                aadhaarList.add(userList.get(j).RegisteredAadhaarNo!!)
                                            }
                                        }*/
                                        uiThread {
                                            callAllUserAadhaarDetailsApi()
                                            //setAdapter()
                                        }
                                    }

                                } else {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_date_found))
                                }
//
                            }
                            else {
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_date_found))
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
//                            (mContext as DashboardActivity).showSnackMessage("ERROR")
                        })
        )
    }

    private fun callAllUserAadhaarDetailsApi() {
        aadhaarList.clear()
        aadhaarListUserList.clear()
        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getAllAadhaar(Pref.session_token!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var response = result as GetAllAadhaarResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                if (response.all_aadhaar_list != null && response.all_aadhaar_list!!.size > 0) {
                                    doAsync {
                                        var allAadhaarList = response.all_aadhaar_list
                                        /*for(j in 0..allAadhaarList!!.size-1){
                                            aadhaarList.add(allAadhaarList.get(j).RegisteredAadhaarNo)
                                        }*/

                                        for (l in 0..response.all_aadhaar_list!!.size - 1) {
                                            aadhaarList.add(allAadhaarList!!.get(l).RegisteredAadhaarNo)
                                            var obj: AadhaarListUser = AadhaarListUser("", "")
                                            obj.user_id = response.all_aadhaar_list!!.get(l).user_id.toString()
                                            obj.RegisteredAadhaarNo = response.all_aadhaar_list!!.get(l).RegisteredAadhaarNo
                                            aadhaarListUserList.add(obj)
                                        }

                                        uiThread {
                                            setAdapter()
                                        }
                                    }
                                } else {
                                    setAdapter()
                                }
                            } else {
                                setAdapter()
                                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.no_date_found))
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            setAdapter()
                        })
        )
    }

    private fun getBytesForMemCache(percent: Int): Int {
        val mi: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()
        val activityManager: ActivityManager = mContext!!.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        val availableMemory: Double = mi.availMem.toDouble()
        return (percent * availableMemory / 100).toInt()
    }

    private fun getCustomPicasso(): Picasso? {
        val builder = Picasso.Builder(mContext)
        //set 12% of available app memory for image cache
        builder.memoryCache(LruCache(getBytesForMemCache(12)))
        //set request transformer
        val requestTransformer = RequestTransformer { request ->
            Log.d("image request", request.toString())
            request
        }
        builder.requestTransformer(requestTransformer)
        return builder.build()
    }


    private fun setAdapter() {

        //Toast.makeText(mContext,userList.size.toString(),Toast.LENGTH_SHORT).show()
        //tv_cust_no_frag_reg.text = "Total customer(s) : " + userList!!.size

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

        adapter = AdapterUserList(mContext, userList!!, object : PhotoRegUserListner {

            override fun getUserInfoOnLick(obj: UserListResponseModel) {
                showFaceIns(obj)
                //(mContext as DashboardActivity).loadFragment(FragType.RegisTerFaceFragment, true, obj)
            }

            override fun getPhoneOnLick(phone: String) {
                IntentActionable.initiatePhoneCall(mContext, phone)
            }

            override fun getWhatsappOnLick(phone: String) {
                var phone = "+91" + phone
                sendWhats(phone)
                //openWhatsApp(phone)
            }

            override fun deletePicOnLick(obj: UserListResponseModel) {


                val simpleDialogg = Dialog(mContext)
                simpleDialogg.setCancelable(false)
                simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogg.setContentView(R.layout.dialog_yes_no)
                val dialogHeader = simpleDialogg.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                val dialogHeaderHeader = simpleDialogg.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                //dialogHeader.text = "Are you sure?"
                dialogHeader.text = "Wish to Delete Face Registration?"
                dialogHeaderHeader.text = "Hi " + Pref.user_name!! + "!"
                val dialogYes = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                val dialogNo = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

                dialogYes.setOnClickListener({ view ->
                    simpleDialogg.cancel()

                    if (AppUtils.isOnline(mContext)) {
                        deletePicApi(obj.user_id.toString())
                    } else {
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                    }


                })
                dialogNo.setOnClickListener({ view ->
                    simpleDialogg.cancel()
                })
                simpleDialogg.show()


            }

            override fun viewPicOnLick(img_link: String, name: String) {
                progress_wheel.spin()
                val simpleDialogg = Dialog(mContext)
                simpleDialogg.setCancelable(true)
                simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogg.setContentView(R.layout.view_face_img)


                val faceImg = simpleDialogg.findViewById(R.id.iv_face_img) as ImageView
                faceImg.setImageDrawable(null)
                faceImg.setBackgroundDrawable(null)
                faceImg.invalidate();
                faceImg.setImageBitmap(null);
                val faceName = simpleDialogg.findViewById(R.id.face_name) as AppCustomTextView
                val faceCanel = simpleDialogg.findViewById(R.id.iv_face_reg_cancel) as ImageView
                faceName.text = name

                val picasso = Picasso.Builder(mContext)
                        .memoryCache(Cache.NONE)
                        .indicatorsEnabled(false)
                        .loggingEnabled(true)
                        .build()

                picasso.load(Uri.parse(img_link))
                        .centerCrop()
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .resize(500, 500)
                        .into(faceImg)

                progress_wheel.stopSpinning()

                simpleDialogg.show()

                faceCanel.setOnClickListener({ view ->
                    simpleDialogg.dismiss()
                })

                simpleDialogg.setOnCancelListener({ view ->
                    simpleDialogg.dismiss()

                })
                simpleDialogg.setOnDismissListener({ view ->
                    simpleDialogg.dismiss()

                })
            }

            override fun getAadhaarOnLick(obj: UserListResponseModel) {
                //OpenDialogForAdhaarReg(obj)
                if(!obj.aadhar_image_link!!.contains("CommonFolder"))
                    return

                progress_wheel.spin()
                val simpleDialogg = Dialog(mContext)
                simpleDialogg.setCancelable(true)
                simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogg.setContentView(R.layout.view_face_img)


                val faceImg = simpleDialogg.findViewById(R.id.iv_face_img) as ImageView
                faceImg.setImageDrawable(null)
                faceImg.setBackgroundDrawable(null)
                faceImg.invalidate();
                faceImg.setImageBitmap(null);
                val faceName = simpleDialogg.findViewById(R.id.face_name) as AppCustomTextView
                val faceCanel = simpleDialogg.findViewById(R.id.iv_face_reg_cancel) as ImageView
                faceName.text = obj.user_name!!

                val picasso = Picasso.Builder(mContext)
                        .memoryCache(Cache.NONE)
                        .indicatorsEnabled(false)
                        .loggingEnabled(true)
                        .build()

                picasso.load(Uri.parse(obj.aadhar_image_link))
                        .centerCrop()
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .resize(700, 400)
                        .into(faceImg)


                progress_wheel.stopSpinning()


                simpleDialogg.show()

                faceCanel.setOnClickListener({ view ->
                    simpleDialogg.dismiss()
                })

                simpleDialogg.setOnCancelListener({ view ->
                    simpleDialogg.dismiss()

                })
                simpleDialogg.setOnDismissListener({ view ->
                    simpleDialogg.dismiss()

                })
            }

            override fun updateTypeOnClick(obj: UserListResponseModel) {
                UpdateDSTypeStatusDialog.getInstance(obj.user_name!!, "Cancel", "Confirm", true,obj.type_name.toString(),obj.user_id.toString()!!,
                        object : UpdateDSTypeStatusDialog.OnDSButtonClickListener {
                    override fun onLeftClick() {

                    }

                            override fun onRightClick(typeId: String, typeName: String, usrId: String) {
                                if(!typeName.equals("") && typeName.length>0)
                                    updateUserType(typeId,usrId)
                            }
                        }).show((mContext as DashboardActivity).supportFragmentManager, "")
            }

            override fun updateContactOnClick(obj: UserListResponseModel) {
                val simpleDialogg = Dialog(mContext)
                simpleDialogg.setCancelable(true)
                simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogg.setContentView(R.layout.dialog_update_contact_photo_reg)

                val heading = simpleDialogg.findViewById(R.id.tv_dialog_upsate_ph_name) as TextView
                val cancel = simpleDialogg.findViewById(R.id.cancel_TV) as AppCustomTextView
                val update = simpleDialogg.findViewById(R.id.ok_TV) as AppCustomTextView
                val et_phone = simpleDialogg.findViewById(R.id.et_dialog_upsate_ph_no) as EditText

                if(obj.emp_phone_no!!.length>0){
                    heading.text=obj!!.user_name+"   (  "+obj.emp_phone_no+"   )"
                }
                else{
                    heading.text=obj!!.user_name
                }

                cancel.setOnClickListener({ view ->
                    simpleDialogg.dismiss()
                })
                update.setOnClickListener({ view ->

                    var cont=et_phone.text.toString()
                    if(cont.length==10){
                        simpleDialogg.dismiss()
                        updateEmpPhone(obj.emp_phone_no!!,cont,obj.user_id.toString(),obj.user_contactid!!)
                    }else{
                        et_phone.setError("Enter valid Phone No.")
                        et_phone.requestFocus()
                    }

                })

                simpleDialogg.show()
            }

            override fun addContactOnClick(obj: UserListResponseModel) {
                val simpleDialogg = Dialog(mContext)
                simpleDialogg.setCancelable(true)
                simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogg.setContentView(R.layout.dialog_update_contact_photo_reg)

                val heading = simpleDialogg.findViewById(R.id.tv_dialog_upsate_ph_name) as TextView
                val cancel = simpleDialogg.findViewById(R.id.cancel_TV) as AppCustomTextView
                val update = simpleDialogg.findViewById(R.id.ok_TV) as AppCustomTextView
                val et_phone = simpleDialogg.findViewById(R.id.et_dialog_upsate_ph_no) as EditText

                if(obj.emp_phone_no!!.length>0){
                    heading.text=obj!!.user_name+"   (  "+obj.emp_phone_no+"   )"
                }
                else{
                    heading.text=obj!!.user_name
                }

                update.text="Add Phone"

                cancel.setOnClickListener({ view ->
                    simpleDialogg.dismiss()
                })
                update.setOnClickListener({ view ->

                    var cont=et_phone.text.toString()
                    if(cont.length==10){
                        simpleDialogg.dismiss()
                        addEmpPhone(cont,obj.user_id!!.toString(),obj!!.user_contactid!!)
                    }else{
                        et_phone.setError("Enter valid Phone No.")
                        et_phone.requestFocus()
                    }

                })

                simpleDialogg.show()
            }

            override fun updateUserNameOnClick(obj: UserListResponseModel) {
                updateUserName(obj)
            }

            override fun updateOtherIDOnClick(obj: UserListResponseModel) {
                val simpleDialogg = Dialog(mContext)
                simpleDialogg.setCancelable(true)
                simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogg.setContentView(R.layout.dialog_update_contact_photo_reg)

                val heading = simpleDialogg.findViewById(R.id.tv_dialog_upsate_ph_name) as TextView
                val cancel = simpleDialogg.findViewById(R.id.cancel_TV) as AppCustomTextView
                val update = simpleDialogg.findViewById(R.id.ok_TV) as AppCustomTextView
                val et_phone = simpleDialogg.findViewById(R.id.et_dialog_upsate_ph_no) as EditText

                heading.text=obj!!.user_name
                et_phone.setHint("Enter Other ID")
                et_phone.inputType=InputType.TYPE_CLASS_TEXT

                cancel.setOnClickListener({ view ->
                    simpleDialogg.dismiss()
                })
                update.setOnClickListener({ view ->
                    simpleDialogg.dismiss()
                    var cont=et_phone.text.toString()
                    updateOtherID(cont,obj.user_contactid!!)
                })
                simpleDialogg.show()
            }

            override fun updateLoginIDOnClick(obj: UserListResponseModel) {
                val simpleDialogg = Dialog(mContext)
                simpleDialogg.setCancelable(true)
                simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogg.setContentView(R.layout.dialog_update_contact_photo_reg)

                val heading = simpleDialogg.findViewById(R.id.tv_dialog_upsate_ph_name) as TextView
                val cancel = simpleDialogg.findViewById(R.id.cancel_TV) as AppCustomTextView
                val update = simpleDialogg.findViewById(R.id.ok_TV) as AppCustomTextView
                val et_phone = simpleDialogg.findViewById(R.id.et_dialog_upsate_ph_no) as EditText

                heading.text=obj!!.user_name
                et_phone.setHint("Enter User ID")
                et_phone.inputType=InputType.TYPE_CLASS_TEXT

                cancel.setOnClickListener({ view ->
                    simpleDialogg.dismiss()
                })
                update.setOnClickListener({ view ->
                    var cont=et_phone.text.toString()
                    if(cont.length!=0){
                        simpleDialogg.dismiss()
                        updateUserLoginID(obj.user_id!!.toString(),cont)
                    }else{
                        et_phone.setError("Enter User ID")
                        et_phone.requestFocus()
                    }

                })
                simpleDialogg.show()
            }
        }, {
            it
        })

        mRv_userList.adapter = adapter
    }

    private fun updateUserName(obj: UserListResponseModel){
        val simpleDialogg = Dialog(mContext)
        simpleDialogg.setCancelable(true)
        simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialogg.setContentView(R.layout.dialog_update_user_name_photo_reg)

        val heading = simpleDialogg.findViewById(R.id.tv_dialog_update_usr_name_header) as TextView
        val cancel = simpleDialogg.findViewById(R.id.cancel_TV) as AppCustomTextView
        val update = simpleDialogg.findViewById(R.id.ok_TV) as AppCustomTextView

        val et_first_name = simpleDialogg.findViewById(R.id.et_dialog_update_usr_name_first) as EditText
        val et_middle_name = simpleDialogg.findViewById(R.id.et_dialog_update_usr_name_middle) as EditText
        val et_last_name = simpleDialogg.findViewById(R.id.et_dialog_update_usr_name_last) as EditText


        heading.text="Update Name of : "+obj!!.user_name

        cancel.setOnClickListener({ view ->
            simpleDialogg.dismiss()
        })
        update.setOnClickListener({ view ->

            var cont=et_first_name.text.toString()
            if(cont.length>0){
                simpleDialogg.dismiss()
                updateUserNameApi(obj,
                        et_first_name.text.toString()+" "+et_middle_name.text.toString()+" "+et_last_name.text.toString(),
                        et_first_name.text.toString(),et_middle_name.text.toString(),et_last_name.text.toString())
            }else{
                et_first_name.setError("Enter First Name")
                et_first_name.requestFocus()
            }

        })

        simpleDialogg.show()
    }


    private fun updateUserNameApi(obj: UserListResponseModel,name:String,firstName:String,middleName:String,lastName:String){
        try{
            progress_wheel.spin()

            var userModel=UpdateUserNameModel()
            userModel.name_updation_user_id=obj.user_id.toString()
            userModel.updated_name=name
            userModel.updated_first_name=firstName
            userModel.updated_middle_name=middleName
            userModel.updated_last_name=lastName
            userModel.updation_date_time=AppUtils.getCurrentDateTime()

            val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
            BaseActivity.compositeDisposable.add(
                    repository.updateUserName(userModel)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                progress_wheel.stopSpinning()
                                var response = result as UpdateUserNameResponse
                                if (response.status == NetworkConstant.SUCCESS) {
                                    showMessage("Name updation success for "+response.updated_name!!+".")
                                    //callUSerListApi()
                                } else {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                }
                            }, { error ->
                                progress_wheel.stopSpinning()
                                error.printStackTrace()
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            })
            )
        }catch (ex:Exception){
            progress_wheel.stopSpinning()
            ex.printStackTrace()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }

    }


    private fun showFaceIns(obj: UserListResponseModel){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message_face_aadhaar_guide)
        val body = simpleDialog.findViewById(R.id.dialog_message_header_TV) as TextView
        val header = simpleDialog.findViewById(R.id.dialog_message_headerTV) as TextView
        val iv_photo = simpleDialog.findViewById(R.id.iv_dialog_msg_face_aadhaar_g) as ImageView


        iv_photo.setImageDrawable(getResources().getDrawable(R.drawable.face_sample));

        header.text = "Face/Photo Registration Guide:"
        body.text = "1. Avoid Background area.\n" +
                "2. Take Only Face Area, check the below photo face area.\n" +
                "3. Don't take any side face.\n" +
                "4. Take Photo in Normal daylight.\n" +
                "5. Take Photo of Normal Face look / Expression which you have 99% times.\n" +
                "6. Important : Always take photo of Front Face and Closure Look."


        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            //simpleDialog.cancel()

            val simpleDialogYN = Dialog(mContext)
            simpleDialogYN.setCancelable(false)
            simpleDialogYN.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialogYN.setContentView(R.layout.dialog_yes_no)
            val dialogHeader = simpleDialogYN.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
            val dialog_yes_no_headerTV = simpleDialogYN.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
            dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
            dialogHeader.text = "Have you read the instruction?"
            val dialogYes = simpleDialogYN.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
            val dialogNo = simpleDialogYN.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
            dialogYes.setOnClickListener({ view ->
                simpleDialog.cancel()
                simpleDialogYN.cancel()
                (mContext as DashboardActivity).loadFragment(FragType.RegisTerFaceFragment, true, obj)
            })
            dialogNo.setOnClickListener({ view ->
                simpleDialogYN.cancel()
            })
            simpleDialogYN.show()


        })
        simpleDialog.show()
    }


    override fun onClick(p0: View?) {

    }


    fun deletePicApi(usr_id: String) {

        val repository = GetUserListPhotoRegProvider.providePhotoReg()
        BaseActivity.compositeDisposable.add(
                repository.deleteUserPicApi(usr_id, Pref.session_token!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var response = result as DeleteUserPicResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                callUSerListApi()

                            } else {
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
//                            (mContext as DashboardActivity).showSnackMessage("ERROR")
                        })
        )

    }

    /////////////////////////////////////////////

    private fun sendWhats(phone: String) {
        val packageManager: PackageManager = mContext.getPackageManager()
        val i = Intent(Intent.ACTION_VIEW)
        try {
            //val url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode("", "UTF-8")
            val url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + " "
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openWhatsApp(num: String) {
        val isAppInstalled = appInstalledOrNot("com.whatsapp")
        if (isAppInstalled) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$num"))
            startActivity(intent)
        } else {
            // WhatsApp not installed show toast or dialog
        }
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = requireActivity().packageManager
        return try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


    lateinit var simpleDialog: Dialog
    lateinit var iv_takenImg: ImageView
    lateinit var dialogCameraclickCancel: ImageView
    lateinit var dialogDocclickCancel: ImageView
    lateinit var tv_docShow: TextView
    lateinit var tv_docUrl: TextView
    private fun OpenDialogForAdhaarReg(obj: UserListResponseModel) {
        simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_adhaar_reg)

        val headerName = simpleDialog.findViewById(R.id.dialog_adhaar_reg_adhharTV_header) as AppCustomTextView
        headerName.text = "Enter Aadhaar for " + obj.user_name

        val dialogEtCardNumber1 = simpleDialog.findViewById(R.id.dialog_adhaar_reg_et_no_et_1) as AppCustomEditText
        val dialogEtCardNumber2 = simpleDialog.findViewById(R.id.dialog_adhaar_reg_et_no_et_2) as AppCustomEditText
        val dialogEtCardNumber3 = simpleDialog.findViewById(R.id.dialog_adhaar_reg_et_no_et_3) as AppCustomEditText

        val dialogEtFeedback = simpleDialog.findViewById(R.id.tv_dialog_adhaar_reg_feedback) as AppCustomEditText
        val dialogCameraclick = simpleDialog.findViewById(R.id.tv_dialog_adhaar_reg_iv_camera) as ImageView
        dialogCameraclickCancel = simpleDialog.findViewById(R.id.iv_dialog_aadhaar_reg_cancel_pic) as ImageView
        dialogDocclickCancel = simpleDialog.findViewById(R.id.iv_dialog_aadhaar_reg_cancel_pic_doc) as ImageView
        iv_takenImg = simpleDialog.findViewById(R.id.iv_dialog_aadhaar_reg_pic) as ImageView
        tv_docShow = simpleDialog.findViewById(R.id.tv_dialog_aadhaar_reg_doc) as TextView
        tv_docUrl = simpleDialog.findViewById(R.id.tv_dialog_aadhaar_reg_doc_url) as TextView

        val dialogConfirm = simpleDialog.findViewById(R.id.tv_dialog_adhaar_reg_confirm) as AppCustomTextView
        val dialogCancel = simpleDialog.findViewById(R.id.tv_dialog_adhaar_reg_cancel) as AppCustomTextView

        dialogEtFeedback.setText(obj.aadhaar_remarks)


        dialogEtCardNumber1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (dialogEtCardNumber1.text.toString().length == 4) {
                    dialogEtCardNumber2.setSelection(dialogEtCardNumber2.text.toString().length)
                    dialogEtCardNumber2.requestFocus()
                }
            }
        })
        dialogEtCardNumber2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (dialogEtCardNumber2.text.toString().length == 4) {
                    dialogEtCardNumber3.setSelection(dialogEtCardNumber3.text.toString().length)
                    dialogEtCardNumber3.requestFocus()
                }
            }
        })

        /* val key: OnKeyListener = object : OnKeyListener {
             override fun onKey(v: View, keyCode: Int, event: KeyEvent?): Boolean {
                 if((v as EditText).length()==4){
                     if (!(v as EditText).toString().isEmpty()){
                         v.focusSearch(View.FOCUS_RIGHT).requestFocus()
                     }

                 }
                 return false
             }
         }*/

        //dialogEtCardNumber1.setOnKeyListener(key)
        //dialogEtCardNumber2.setOnKeyListener(key)
        //dialogEtCardNumber3.setOnKeyListener(key)

        /*  if(dialogEtCardNumber1.getText().toString().length==4)
          {
              if(dialogEtCardNumber1.text.toString().isNotEmpty())
              {
                  dialogEtCardNumber1.requestFocus()
              }
              else
              {

              }
          }*/


        if (obj.RegisteredAadhaarNo != null && obj.RegisteredAadhaarNo!!.length > 0) {
            dialogEtCardNumber1.setText(obj.RegisteredAadhaarNo!!.get(0).toString() + obj.RegisteredAadhaarNo!!.get(1).toString() +
                    obj.RegisteredAadhaarNo!!.get(2).toString() + obj.RegisteredAadhaarNo!!.get(3).toString())
            dialogEtCardNumber2.setText(obj.RegisteredAadhaarNo!!.get(4).toString() + obj.RegisteredAadhaarNo!!.get(5).toString() +
                    obj.RegisteredAadhaarNo!!.get(6).toString() + obj.RegisteredAadhaarNo!!.get(7).toString())
            dialogEtCardNumber3.setText(obj.RegisteredAadhaarNo!!.get(8).toString() + obj.RegisteredAadhaarNo!!.get(9).toString() +
                    obj.RegisteredAadhaarNo!!.get(10).toString() + obj.RegisteredAadhaarNo!!.get(11).toString())
        }
        if (obj.RegisteredAadhaarDocLink != null && obj.RegisteredAadhaarDocLink!!.length > 0 && obj.RegisteredAadhaarDocLink!!.contains("jpg")) {
            iv_takenImg.visibility = View.VISIBLE
            dialogCameraclickCancel.visibility = View.VISIBLE
            Picasso.get()
                    .load(obj.RegisteredAadhaarDocLink)
                    .resize(500, 500)
                    .into(iv_takenImg)
        } else if (obj.RegisteredAadhaarDocLink != null && obj.RegisteredAadhaarDocLink!!.length > 0) {
            //Toaster.msgLong(mContext,obj.RegisteredAadhaarDocLink!!.toString())
            iv_takenImg.visibility = View.GONE
            dialogCameraclickCancel.visibility = View.GONE


            if (obj.RegisteredAadhaarDocLink!!.contains("CommonFolder")) {
                tv_docShow.text = "Document Attached."
                tv_docUrl.text = obj.RegisteredAadhaarDocLink

            }

            // download document here
            tv_docUrl.setOnClickListener { view ->

                val file = File(obj.RegisteredAadhaarDocLink!!)
                var strFileName = ""
                if (!obj.RegisteredAadhaarDocLink!!.startsWith("http")!!) {
                    strFileName = file.name
                } else {
                    strFileName = obj.RegisteredAadhaarDocLink!!.substring(obj.RegisteredAadhaarDocLink!!.lastIndexOf("/")!! + 1)
                }

                //downloadFile(obj.RegisteredAadhaarDocLink,tv_docUrl.text.toString().trim())
                downloadFile(obj.RegisteredAadhaarDocLink, strFileName)
            }


        }

        dialogCameraclick.setOnClickListener { v: View? ->
            iv_takenImg.visibility = View.GONE
            dialogCameraclickCancel.visibility = View.GONE
            dialogDocclickCancel.visibility = View.GONE
            tv_docShow.visibility = View.GONE
            showPictureDialog()
        }
        dialogCameraclickCancel.setOnClickListener { v: View? ->

            iv_takenImg.setImageBitmap(null)
            dialogCameraclickCancel.visibility = View.GONE
            dataPath = ""
            imagePath = ""
        }
        dialogDocclickCancel.setOnClickListener { v: View? ->

            tv_docShow.visibility = View.GONE
            dialogDocclickCancel.visibility = View.GONE

            dataPath = ""
            imagePath = ""
        }
        dialogCancel.setOnClickListener { v: View? ->
            progress_wheel.stopSpinning()
            simpleDialog.cancel()
        }

        dialogConfirm.setOnClickListener({ view ->
            //simpleDialog.cancel()

            if (dialogEtCardNumber1.text.toString().length == 4) {
                if (dialogEtCardNumber2.text.toString().length == 4) {
                    if (dialogEtCardNumber3.text.toString().length == 4) {
                        ////////
                        val simpleDialogInner = Dialog(mContext)
                        simpleDialogInner.setCancelable(false)
                        simpleDialogInner.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        simpleDialogInner.setContentView(R.layout.dialog_yes_no)
                        val dialogHeader = simpleDialogInner.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                        val dialogHeaderTTV = simpleDialogInner.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                        dialogHeader.text = "Are you sure?"
                        dialogHeaderTTV.text = "Hi " + Pref.user_name + "!"
                        val dialogYes = simpleDialogInner.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                        val dialogNo = simpleDialogInner.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

                        dialogYes.setOnClickListener({ view ->
                            simpleDialogInner.cancel()
                            str_aadhaarNo = dialogEtCardNumber1.text.toString() + dialogEtCardNumber2.text.toString() + dialogEtCardNumber3.text.toString()
                            // check aadhar unique or not

                            var tagAadhaar = false


                            /*for (j in 0..aadhaarList.size - 1) {
                                if (str_aadhaarNo.equals(aadhaarList.get(j))) {
                                    tagAadhaar = true
                                }
                            }
*/
                            /* if (obj.IsAadhaarRegistered!!) {
                                tagAadhaar = false
                            }*/

                            if (aadhaarListUserList!!.size > 0 && aadhaarListUserList != null) {
                                for (p in 0..aadhaarListUserList!!.size - 1) {
                                    if (str_aadhaarNo.equals(aadhaarListUserList.get(p).RegisteredAadhaarNo)) {
                                        if (obj.user_id!!.toString().equals(aadhaarListUserList.get(p).user_id)) {
                                            tagAadhaar = false
                                            break
                                        } else {
                                            tagAadhaar = true
                                            break
                                        }
                                    }
                                }
                            }



                            if (tagAadhaar == false) {
                                simpleDialog.cancel()
                                submitAadhaarDetails(obj, dialogEtFeedback.text.toString())
                            }
                            else {

                                val simpleDialog = Dialog(mContext)
                                simpleDialog.setCancelable(false)
                                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                simpleDialog.setContentView(R.layout.dialog_message)
                                val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
                                val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
                                //dialog_yes_no_headerTV.text = "Hi "+Pref.user_name?.substring(0, Pref.user_name?.indexOf(" ")!!)+"!"
                                dialog_yes_no_headerTV.text = "Hi " + Pref.user_name!! + "!"
                                dialogHeader.text = "Duplicate Aadhaar Number.Please enter Unique for Current Person.Thanks."
                                val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
                                dialogYes.setOnClickListener({ view ->
                                    simpleDialog.cancel()
                                })
                                simpleDialog.show()

                                //Toaster.msgShort(mContext, "Duplicate Aadhaar Number.Please enter Unique for Current Person.Thanks.")
                                voiceAttendanceMsg("Duplicate Aadhaar Number.Please enter Unique for Current Person.")
                                //(mContext as DashboardActivity).showSnackMessage("Duplication Aadhaar Number.Please enter Unique for Current Person.Thanks.")
                            }


                        })
                        dialogNo.setOnClickListener({ view ->
                            simpleDialogInner.cancel()
                        })
                        simpleDialogInner.show()

                        ///////

                    } else {
                        dialogEtCardNumber3.setError("Please Enter Aadhaad No")
                        dialogEtCardNumber3.requestFocus()
                    }
                } else {
                    dialogEtCardNumber2.setError("Please Enter Aadhaad No")
                    dialogEtCardNumber2.requestFocus()
                }
            } else {
                dialogEtCardNumber1.setError("Please Enter Aadhaad No")
                dialogEtCardNumber1.requestFocus()
            }

        })
        simpleDialog.show()

    }


    private fun submitAadhaarDetails(obj: UserListResponseModel, feedBac: String) {
        progress_wheel.spin()
        var aadhaarSubmitData: AadhaarSubmitData = AadhaarSubmitData()
        aadhaarSubmitData.session_token = Pref.session_token.toString()
        aadhaarSubmitData.aadhaar_holder_user_id = obj.user_id.toString()
        aadhaarSubmitData.aadhaar_holder_user_contactid = obj.user_contactid.toString()
        aadhaarSubmitData.aadhaar_no = str_aadhaarNo
        aadhaarSubmitData.date = AppUtils.getCurrentDateForShopActi()
        aadhaarSubmitData.feedback = feedBac
        aadhaarSubmitData.address = ""

        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        BaseActivity.compositeDisposable.add(
                repository.sendUserAadhaarApi(aadhaarSubmitData)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            progress_wheel.stopSpinning()
                            //(mContext as DashboardActivity).showSnackMessage(response.message!!)
                            //(mContext as DashboardActivity).showSnackMessage("Aadhaar registered successfully")

                            if (response.status == NetworkConstant.SUCCESS) {
                                //Toaster.msgShort(mContext,response.status.toString())
                                if (!TextUtils.isEmpty(et_attachment.text.toString().trim()) || !TextUtils.isEmpty(et_photo.text.toString().trim())) {
                                    val imgList = java.util.ArrayList<WIPImageSubmit>()

                                    if (!TextUtils.isEmpty(et_attachment.text.toString()))
                                        imgList.add(WIPImageSubmit(dataPath, "attachment"))

                                    if (!TextUtils.isEmpty(et_photo.text.toString()))
                                        imgList.add(WIPImageSubmit(imagePath, "image"))

                                    val repository = GetUserListPhotoRegProvider.jobMultipartRepoProvider()
                                    BaseActivity.compositeDisposable.add(
                                            repository.submitAadhaarDetails(aadhaarSubmitData, imgList, mContext)
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribeOn(Schedulers.io())
                                                    .subscribe({ result ->
                                                        val response = result as BaseResponse
                                                        progress_wheel.stopSpinning()
                                                        //(mContext as DashboardActivity).showSnackMessage(response.message!!)
                                                        //(mContext as DashboardActivity).showSnackMessage("Aadhar registered successfully")
                                                        if (response.status == NetworkConstant.SUCCESS) {
                                                            aadharSuccessDialogShow(obj)
                                                            //voiceAttendanceMsg("Aadhaar registered successfully")
                                                            /* Handler(Looper.getMainLooper()).postDelayed({
                                                                callUSerListApi()
                                                            }, 300)*/
                                                            //(mContext as DashboardActivity).loadFragment(FragType.ProtoRegistrationFragment, false, "")
                                                        }
                                                        else if (response.status ==NetworkConstant.SESSION_MISMATCH){
                                                            progress_wheel.stopSpinning()
                                                            (mContext as DashboardActivity).showSnackMessage(message = response.message.toString())
                                                        }

                                                    }, { error ->
                                                        progress_wheel.stopSpinning()
                                                        error.printStackTrace()
                                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                                    })
                                    )
                                } else {
                                    progress_wheel.stopSpinning()
                                    aadharSuccessDialogShow(obj)
                                    //voiceAttendanceMsg("Aadhaar registered successfully")
                                    //(mContext as DashboardActivity).loadFragment(FragType.ProtoRegistrationFragment, false, "")
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage("Duplicate Aadhaar Number.Please enter Unique for Current Person.Thanks.")
                                voiceAttendanceMsg("Duplicate Aadhaar Number.Please enter Unique for Current Person.")
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )


    }

    private fun aadharSuccessDialogShow(obj: UserListResponseModel) {
        val simpleDialogAdhhar = Dialog(mContext)
        simpleDialogAdhhar.setCancelable(false)
        simpleDialogAdhhar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialogAdhhar.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialogAdhhar.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialogHeaderTTV = simpleDialogAdhhar.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialogHeader.text = "Aadhaar registered successfully for "+obj.user_name
        dialogHeaderTTV.text = "Hi " + Pref.user_name + "!"
        val tv_message_ok = simpleDialogAdhhar.findViewById(R.id.tv_message_ok) as AppCustomTextView

        tv_message_ok.setOnClickListener({ view ->
            simpleDialogAdhhar.cancel()
            voiceAttendanceMsg("Aadhaar registered successfully for " + obj.user_name)
        })
        simpleDialogAdhhar.show()
    }

    private fun voiceAttendanceMsg(msg: String) {
        if (Pref.isVoiceEnabledForAttendanceSubmit) {
            val speechStatus = (mContext as DashboardActivity).textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
            if (speechStatus == TextToSpeech.ERROR)
                Log.e("Add Day Start", "TTS error in converting Text to Speech!");
        }
        Handler(Looper.getMainLooper()).postDelayed({
            callUSerListApi()
        }, 300)
    }


    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(mContext)
        pictureDialog.setTitle("Select Action")
        //val pictureDialogItems = arrayOf("Select photo from gallery", "Capture Image", "Select file from file manager")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture Image")
        pictureDialog.setItems(pictureDialogItems,
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        0 -> {
                            isAttachment = false
                            selectImageInAlbum()
                        }
                        1 -> {
                            isAttachment = false
                            launchCamera()
                        }
                        /*2 -> {
                            isAttachment = true
                            (mContext as DashboardActivity).openFileManager()
                        }*/
                    }
                })
        pictureDialog.show()
    }

    private fun selectImageInAlbum() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        (mContext as DashboardActivity).startActivityForResult(galleryIntent, PermissionHelper.REQUEST_CODE_STORAGE)
    }


    private fun launchCamera() {
        (mContext as DashboardActivity).captureImage()
    }


    /*fun setImage(file: File) {
        if (isAttachment) {
            et_attachment.setText(file.name)
            dataPath = file.absolutePath
        }
        else {
            imagePath = file.absolutePath
            et_photo.setText(file.name)
        }
    }*/

    fun setImage(filePath: String) {
        val file = File(filePath)
        var newFile: File? = null
        progress_wheel.spin()
        doAsync {
            val processImage = ProcessImageUtils_v1(mContext, file, 50)
            newFile = processImage.ProcessImageSelfie()
            uiThread {
                if (newFile != null) {
                    Timber.e("=========Image from new technique==========")
                    val fileSize = AppUtils.getCompressImage(filePath)
                    var tyy = filePath

                    if (isAttachment) {
                        et_attachment.setText(newFile!!.name)
                        dataPath = newFile!!.absolutePath
                    } else {
                        imagePath = newFile!!.absolutePath
                        et_photo.setText(newFile!!.name)

                        val f = File(newFile!!.absolutePath)
                        val options: BitmapFactory.Options = BitmapFactory.Options()
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888
                        var bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
                        iv_takenImg.setImageBitmap(bitmap)
                        var tt = "asd"

                    }
                    iv_takenImg.visibility = View.VISIBLE
                    dialogCameraclickCancel.visibility = View.VISIBLE
                    progress_wheel.stopSpinning()
                } else {
                    // Image compression
                    val fileSize = AppUtils.getCompressImage(filePath)
                    var tyy = filePath
                    progress_wheel.stopSpinning()
                    iv_takenImg.visibility = View.VISIBLE
                    dialogCameraclickCancel.visibility = View.VISIBLE
                }
            }
        }
    }

    fun setDoc(file: File) {
        if (isAttachment) {
            et_attachment.setText(file.name)
            dataPath = file.absolutePath
            iv_takenImg.visibility = View.GONE
            tv_docShow.text = "Document Attached."
            tv_docShow.visibility = View.VISIBLE
            dialogDocclickCancel.visibility = View.VISIBLE
        }
    }


    /* fun setImage(filePath: String) {

         val file = File(filePath)
         var newFile: File? = null

         progress_wheel.spin()
         doAsync {

             val processImage = ProcessImageUtils_v1(mContext, file, 50)
             newFile = processImage.ProcessImage()

             uiThread {
                 if (newFile != null) {
                     Timber.e("=========Image from new technique==========")
                     //reimbursementEditPic(newFile!!.length(), newFile?.absolutePath!!)
                 } else {
                     // Image compression
                     val fileSize = AppUtils.getCompressImage(filePath)
                     //reimbursementEditPic(fileSize, filePath)
                 }
             }
         }
     }*/


    private fun downloadFile(downloadUrl: String?, fileName: String) {
        try {
            if (!AppUtils.isOnline(mContext)) {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                return
            }

            progress_wheel.spin()

            val folder = File(FTStorageUtils.getFolderPath(mContext) + "/", fileName)
            if (folder.exists()) {
                folder.delete()
                if (folder.exists()) {
                    folder.canonicalFile.delete()
                    if (folder.exists()) {
                        mContext.deleteFile(folder.getName())
                    }
                }
            }

            PRDownloader.download(downloadUrl, FTStorageUtils.getFolderPath(mContext) + "/", fileName)
                    .build()
                    .setOnProgressListener {
                        Log.e("Aadhaar Details", "Attachment Download Progress======> $it")
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            progress_wheel.stopSpinning()
                            val file = File(FTStorageUtils.getFolderPath(mContext) + "/" + fileName)
                            openFile(file)
                        }

                        override fun onError(error: Error) {
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage("Download failed")
                            Log.e("Aadhaar Details", "Attachment download error msg=======> " + error.serverErrorMessage)
                        }
                    })

        } catch (e: Exception) {
            (mContext as DashboardActivity).showSnackMessage("Download failed")
            progress_wheel.stopSpinning()
            e.printStackTrace()
        }
    }

    private fun openFile(file: File) {

        val mimeType = NewFileUtils.getMemeTypeFromFile(file.absolutePath + "." + NewFileUtils.getExtension(file))

        if (mimeType?.equals("application/pdf")!!) {
//            val path1 = Uri.fromFile(file)
            val path1 = FileProvider.getUriForFile(mContext, requireContext().applicationContext.packageName.toString() + ".provider", file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(path1, "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                (mContext as DashboardActivity).showSnackMessage("No Application Available to View Pdf")
            }
        } else if (mimeType == "application/msword") {
            //val path1 = Uri.fromFile(file)
            val path1 = FileProvider.getUriForFile(mContext, requireContext().applicationContext.packageName.toString() + ".provider", file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(path1, "application/msword")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                (mContext as DashboardActivity).showSnackMessage("No Application Available to View Document")
            }
        } else if (mimeType == "application/vnd.ms-excel") {
            //val path1 = Uri.fromFile(file)
            val path1 = FileProvider.getUriForFile(mContext, requireContext().applicationContext.packageName.toString() + ".provider", file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(path1, "application/vnd.ms-excel")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                (mContext as DashboardActivity).showSnackMessage("No Application Available to View Excel")
            }

        } else if (mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.template") {
            //val path1 = Uri.fromFile(file)
            val path1 = FileProvider.getUriForFile(mContext, requireContext().applicationContext.packageName.toString() + ".provider", file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(path1, "application/vnd.openxmlformats-officedocument.wordprocessingml.template")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                (mContext as DashboardActivity).showSnackMessage("No Application Available to View Document")
            }
        } else if (mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document") {
            //val path1 = Uri.fromFile(file)
            val path1 = FileProvider.getUriForFile(mContext, requireContext().applicationContext.packageName.toString() + ".provider", file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(path1, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                (mContext as DashboardActivity).showSnackMessage("No Application Available to View Document")
            }

        } else if (mimeType == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
            //val path1 = Uri.fromFile(file)
            val path1 = FileProvider.getUriForFile(mContext, requireContext().applicationContext.packageName.toString() + ".provider", file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(path1, "application/vnd.ms-excel")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                (mContext as DashboardActivity).showSnackMessage("No Application Available to View Excel")
            }
        } else if (mimeType == "application/vnd.openxmlformats-officedocument.spreadsheetml.template") {
            //val path1 = Uri.fromFile(file)
            val path1 = FileProvider.getUriForFile(mContext, requireContext().applicationContext.packageName.toString() + ".provider", file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(path1, "application/vnd.ms-excel")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                (mContext as DashboardActivity).showSnackMessage("No Application Available to View Excel")
            }
        } else if (mimeType == "image/jpeg" || mimeType == "image/png") {
            FullImageDialog.getInstance(file.absolutePath).show((mContext as DashboardActivity).supportFragmentManager, "")
        }
    }


    private fun checkPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result: Int = ContextCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE)
            val result1: Int = ContextCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        /*if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent: Intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(java.lang.String.format("package:%s", mContext.getApplicationContext().getPackageName()))
                startActivityForResult(intent, 2296)
            } catch (e: java.lang.Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 2296)
            }
        }*/
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    callUSerListApi()
                } else {
                    Toast.makeText(mContext, "Allow permission for storage access!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        /* if(requestCode == 7009){
             val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
             var t= result!![0]
             Toaster.msgShort(mContext,t)
         }*/

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


    private fun updateUserType(typeID:String,usrId:String){
        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.updateUserType(usrId!!, Pref.session_token!!,typeID!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var response = result as BaseResponse

                            if (response.status == NetworkConstant.SUCCESS) {
                                (mContext as DashboardActivity).showSnackMessage("Type updation successful.")
                                voiceAttendanceMsg("Type updation successful.")
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

    private fun addEmpPhone(phone:String,usrID:String,usrContID:String){
        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.addEmpPhone(usrID!!, Pref.session_token!!,usrContID!!,phone)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                (mContext as DashboardActivity).showSnackMessage("Phone added successful.")
                                voiceAttendanceMsg("Phone added successful.")
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

    private fun updateEmpPhone(oldPhone:String,newPhone:String,usrID:String,usrContID:String){
        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.updateEmpPhone(usrID!!, Pref.session_token!!,usrContID!!,oldPhone!!,newPhone!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                (mContext as DashboardActivity).showSnackMessage("Phone updation successful.")
                                voiceAttendanceMsg("Phone updation successful.")
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

    private fun updateOtherID(value:String,contactid:String){
        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.updateOtherID(contactid,value)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                //(mContext as DashboardActivity).showSnackMessage("Updation successful.")
                                //voiceAttendanceMsg("Updation successful.")
                                showMessage("Updation successful.")
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

    private fun updateUserLoginID(updating_user_id:String,new_login_id:String){
        val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.updateUserLoginID(updating_user_id,new_login_id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                //(mContext as DashboardActivity).showSnackMessage("Updation successful.")
                                //voiceAttendanceMsg("Updation successful.")
                                showMessage("Updation successful.")
                            } else if(response.status == NetworkConstant.NO_DATA){
                                val simpleDialogAdhhar = Dialog(mContext)
                                simpleDialogAdhhar.setCancelable(false)
                                simpleDialogAdhhar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                simpleDialogAdhhar.setContentView(R.layout.dialog_message)
                                val dialogHeader = simpleDialogAdhhar.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
                                val dialogBody = simpleDialogAdhhar.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
                                dialogHeader.text = "Hi! "+Pref.user_name
                                dialogBody.text = "Duplicate ID"
                                val tv_message_ok = simpleDialogAdhhar.findViewById(R.id.tv_message_ok) as AppCustomTextView

                                tv_message_ok.setOnClickListener({ view ->
                                    simpleDialogAdhhar.cancel()
                                    callUSerListApi()
                                })
                                simpleDialogAdhhar.show()
                            }else{
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun showMessage(msg:String){
        val simpleDialogAdhhar = Dialog(mContext)
        simpleDialogAdhhar.setCancelable(false)
        simpleDialogAdhhar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialogAdhhar.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialogAdhhar.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        val dialogBody = simpleDialogAdhhar.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        dialogHeader.text = "Hi! "+Pref.user_name
        dialogBody.text = msg
        val tv_message_ok = simpleDialogAdhhar.findViewById(R.id.tv_message_ok) as AppCustomTextView

        tv_message_ok.setOnClickListener({ view ->
            simpleDialogAdhhar.cancel()
            callUSerListApi()
        })
        simpleDialogAdhhar.show()
    }

}