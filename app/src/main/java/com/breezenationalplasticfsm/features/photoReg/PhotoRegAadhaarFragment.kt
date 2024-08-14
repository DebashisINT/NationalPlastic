package com.breezenationalplasticfsm.features.photoReg

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.breezenationalplasticfsm.CustomStatic
import com.breezenationalplasticfsm.MySingleton
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.myjobs.model.WIPImageSubmit
import com.breezenationalplasticfsm.features.photoReg.api.GetUserListPhotoRegProvider
import com.breezenationalplasticfsm.features.photoReg.model.*
import com.breezenationalplasticfsm.widgets.AppCustomTextView

import com.pnikosis.materialishprogress.ProgressWheel
import com.themechangeapp.pickimage.PermissionHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.util.HashMap

class PhotoRegAadhaarFragment: BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var nameTV: AppCustomTextView
    private lateinit var tv_internet_info: AppCustomTextView
    private lateinit var aadhaarImg: ImageView
    private lateinit var cameraIconImg: ImageView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var submitBtn: Button

    lateinit var imgUri:Uri
    private var imagePath: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object{
        var user_id: String? = null
        var user_name: String? = null
        var user_login_id: String? = null
        var user_contactid: String? = null
        fun getInstance(objects: Any): PhotoRegAadhaarFragment {
            val photoRegAadhaarFragment = PhotoRegAadhaarFragment()
            if (!TextUtils.isEmpty(objects.toString())) {

                var obj = objects as UserListResponseModel

                user_id=obj!!.user_id.toString()
                user_name=obj!!.user_name
                user_login_id=obj!!.user_login_id
                user_contactid=obj!!.user_contactid
            }
            return photoRegAadhaarFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_photo_reg_aadhaar, container, false)
        initView(view)
        return view
    }

    private fun initView(view:View){
        nameTV = view.findViewById(R.id.tv_frag_photo_reg_aadhaar_name)
        aadhaarImg = view.findViewById(R.id.iv_frag_photo_reg_aadhaar_pic)
        submitBtn = view.findViewById(R.id.btn_frag_photo_reg_aadhaar_upload)
        progress_wheel = view.findViewById(R.id.progress_wheel)
        tv_internet_info = view.findViewById(R.id.tv_internet_info)
        cameraIconImg = view.findViewById(R.id.iv_frag_photo_reg_aadhaar_camera_icon)
        progress_wheel.stopSpinning()

        submitBtn.setOnClickListener(this)
        aadhaarImg.setOnClickListener(this)
        //aadhaarImg.setOnClickListener(this)

        nameTV.text = RegisTerFaceFragment.user_name!!


        launchCamera()

        //CustomStatic.IsAadhaarForPhotoReg=true
        //submitCheckAadhaarData("MD Shahadat aarhaar","1974-05-03","247141377595")
        //submitCheckAadhaarData("MD Shahadat aarhaar","null-02-28","247141377595")
    }

    fun launchCamera() {
        if (PermissionHelper.checkCameraPermission(mContext as DashboardActivity) && PermissionHelper.checkStoragePermission(mContext as DashboardActivity)) {
            (mContext as DashboardActivity).captureImage()
        }
    }

    fun setImage(imgRealPath: Uri, fileSizeInKB: Long) {
        imgUri=imgRealPath
        imagePath = imgRealPath.toString()
        if(imagePath!=""){
            cameraIconImg.visibility=View.GONE
        }
        getBitmap(imgRealPath.path)

    }

    fun getBitmap(path: String?) {
        var bitmap: Bitmap? = null
        try {
            val f = File(path)
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
            aadhaarImg.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //return bitmap
    }

    override fun onClick(p0: View?) {
        if(p0!=null){
            when(p0.id){
                R.id.btn_frag_photo_reg_aadhaar_upload -> {
                    if(AppUtils.isOnline(mContext)){
                        if(imagePath.length>0 && imagePath!=""){

                            val simpleDialogg = Dialog(mContext)
                            simpleDialogg.setCancelable(false)
                            simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            simpleDialogg.setContentView(R.layout.dialog_yes_no)
                            val dialogHeader = simpleDialogg.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                            val dialogHead = simpleDialogg.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                            dialogHeader.text="Submit for verification"
                            dialogHead.text="Hi "+Pref.user_name!!+"!"
                            val dialogYes = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                            val dialogNo = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

                            dialogYes.setOnClickListener( { view ->
                                simpleDialogg.cancel()
                                if (AppUtils.isOnline(mContext)){
                                    registerFace()
                                }else{
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                                }

                            })
                            dialogNo.setOnClickListener( { view ->
                                simpleDialogg.cancel()
                            })
                            simpleDialogg.show()
                        }else{
                            Toaster.msgShort(mContext,"Please select required ID picture")
                        }
                    }else{
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                    }

                    /*if(imagePath.length>0 && imagePath!="") {
                        if (AppUtils.isOnline(mContext)) {
                            tv_internet_info.setBackgroundColor(resources.getColor(R.color.color_custom_green))
                            tv_internet_info.text = getString(R.string.registration_net_connected)
                        } else {
                            tv_internet_info.setBackgroundColor(resources.getColor(R.color.color_custom_red))
                            tv_internet_info.text = getString(R.string.reg_net_disconnected)
                            return
                        }


                        val simpleDialogg = Dialog(mContext)
                        simpleDialogg.setCancelable(false)
                        simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        simpleDialogg.setContentView(R.layout.dialog_yes_no)
                        val dialogHeader = simpleDialogg.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                        val dialogHead = simpleDialogg.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                        dialogHeader.text="Submit for verification"
                        dialogHead.text="Hi "+Pref.user_name!!+"!"
                        val dialogYes = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                        val dialogNo = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

                        dialogYes.setOnClickListener( { view ->
                            simpleDialogg.cancel()
                            if (AppUtils.isOnline(mContext)){
                                uploadAadhaarPic()
                            }else{
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                            }

                        })
                        dialogNo.setOnClickListener( { view ->
                            simpleDialogg.cancel()
                        })
                        simpleDialogg.show()
                    }*/
                }
                R.id.iv_frag_photo_reg_aadhaar_pic->{
                    launchCamera()
                }
            }
        }
    }


    private fun uploadAadhaarPic(){
        Timber.d("PhotoRegAadhaarFragment uploadAadhaarPic initiate")
        progress_wheel.spin()
        var obj= UserPhotoRegModel()
        obj.user_id= user_id
        obj.session_token= Pref.session_token
        obj.registration_date_time=AppUtils.getCurrentDateTime()

        val repository = GetUserListPhotoRegProvider.providePhotoReg()
        BaseActivity.compositeDisposable.add(
                repository.addUserAadhaarImg(obj,imagePath,mContext, user_contactid)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AadhaarPicRegResponse
                            if(response.status.toString() == NetworkConstant.SUCCESS){
                                Timber.d("PhotoRegAadhaarFragment AadharImageDetection/AadharImageSave response : "+response.status+" link : "+response.aadhaar_image_link+" "+AppUtils.getCurrentDateTime().toString())
                                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.face_reg_success))
                                Handler(Looper.getMainLooper()).postDelayed({
                                    progress_wheel.stopSpinning()
                                    CustomStatic.AadhaarPicRegUrl=response.aadhaar_image_link
                                    if(CustomStatic.FacePicRegUrl!="" && CustomStatic.AadhaarPicRegUrl!=""){
                                        Timber.d("PhotoRegAadhaarFragment face url: "+CustomStatic.FacePicRegUrl+" aadhaar url : "+CustomStatic.AadhaarPicRegUrl+AppUtils.getCurrentDateTime().toString())
                                        faceAadhaarCompareParam(CustomStatic.FacePicRegUrl,CustomStatic.AadhaarPicRegUrl)
                                        //extractAadhaarDtls(CustomStatic.AadhaarPicRegUrl)
                                    }else{
                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                        deletePicApi(user_id!!,"Something went wrong. Please try again later")
                                    }
                                }, 500)

                            }else{
                                progress_wheel.stopSpinning()
                                CustomStatic.AadhaarPicRegUrl=""
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                Timber.d("PhotoRegAadhaarFragment : AadharImageDetection/AadharImageSave : " + response.status.toString() +", : "  + ", Failed: "+AppUtils.getCurrentDateTime().toString())
                                deletePicApi(user_id!!,"Something went wrong. Please try again later")
                            }
                        },{
                            error ->
                            CustomStatic.AadhaarPicRegUrl=""
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            progress_wheel.stopSpinning()
                            if (error != null) {
                                Timber.d("PhotoRegAadhaarFragment : AadharImageDetection/AadharImageSave : " + " : "  + ", ERROR: " + error.localizedMessage)
                            }
                            deletePicApi(user_id!!,"Something went wrong. Please try again later")
                        })
        )
    }

    private fun faceAadhaarCompareParam( doc1:String, doc2:String) {
        try {
            val jsonObject = JSONObject()
            val notificationBody = JSONObject()
            notificationBody.put("document1", doc1)
            notificationBody.put("document2", doc2)
            jsonObject.put("data", notificationBody)
            val jsonArray = JSONArray()
            jsonObject.put("task_id", user_id)
            jsonObject.put("group_id", user_id)
            faceAadhaarCompare(jsonObject)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun faceAadhaarCompare(notification: JSONObject) {
        try{
            progress_wheel.spin()
            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest("https://eve.idfy.com/v3/tasks/sync/compare/face", notification,
                object : Response.Listener<JSONObject?> {
                    override fun onResponse(response: JSONObject?) {
                        var jObj:JSONObject= JSONObject()
                        jObj=response!!.getJSONObject("result")
                        var isMatch=jObj.getBoolean("is_a_match")
                        var matchScore=jObj.getDouble("match_score")
                        //(mContext as DashboardActivity).showSnackMessage(matchScore.toString())
                        progress_wheel.stopSpinning()
                        Timber.d("face-doc-Compare : idfy response : isMatch "+isMatch.toString() + " matchScore : "+matchScore.toString() )
                        if(isMatch){
                            if(matchScore>45.00){
                                //extractAadhaarDtls(CustomStatic.AadhaarPicRegUrl)
                                submitCheckAadhaarData(user_name+"($user_id)","1990-02-28","null")
                            }else{
                                (mContext as DashboardActivity).showSnackMessage("Face Not Match")
                                deletePicApi(user_id!!,"Face Not Match.")
                            }
                        }else{
                            (mContext as DashboardActivity).showSnackMessage("Face Not Match")
                            deletePicApi(user_id!!,"Face Not Match.")
                        }

                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        progress_wheel.stopSpinning()
                        try{
                            Timber.d("PhotoRegAadhaarFragment faceAadhaarCompare : idfy response : "+error.toString())
                        }catch (ex:java.lang.Exception){

                        }
                        deletePicApi(user_id!!,"Please provide valid ID card photo to register. Thanks.")
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    //params["api-key"] = "dfe0a602-7e79-4a5b-af00-509fc0e8349a" //test
                    params["api-key"] = "cebc560a-855d-429e-a050-8882b9debf60"
                    params["Content-Type"] = "application/json"
                    //params["account-id"] = "aaa73f1c1bdb/fa4cf738-2dda-41db-b0e5-0b406ebe6d2f"  //test
                    params["account-id"] = "1a3ae2d3a141/68665e20-bc63-4bb8-b725-f126521f3264"
                    return params
                }
            }
            jsonObjectRequest.setRetryPolicy(DefaultRetryPolicy(
                12000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
            MySingleton.getInstance(mContext.applicationContext)!!.addToRequestQueue(jsonObjectRequest)
        }catch (ex:Exception){ex.printStackTrace()}

    }

    fun extractAadhaarDtls(doc:String){
        progress_wheel.spin()
        try {
            val jsonObject = JSONObject()
            val notificationBody = JSONObject()
            notificationBody.put("document1", doc)
            notificationBody.put("consent", "yes")
            jsonObject.put("data", notificationBody)
            val jsonArray = JSONArray()
            jsonObject.put("task_id", user_id)
            jsonObject.put("group_id", user_id)


            var apiStr=""
            if(CustomStatic.IsAadhaarForPhotoReg)
                apiStr="https://eve.idfy.com/v3/tasks/sync/extract/ind_aadhaar"
            else if(CustomStatic.IsVoterForPhotoReg)
                apiStr="https://eve.idfy.com/v3/tasks/sync/extract/ind_voter_id"
            else if(CustomStatic.IsPanForPhotoReg)
                apiStr="https://eve.idfy.com/v3/tasks/sync/extract/ind_pan"

            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(apiStr, jsonObject,
                    object : Response.Listener<JSONObject?> {
                        override fun onResponse(response: JSONObject?) {
                            progress_wheel.stopSpinning()

                            try{
                                var jObj:JSONObject= JSONObject()
                                jObj=response!!.getJSONObject("result")
                                var tt=jObj.getJSONObject("extraction_output")
                                var dob_aadhaar=tt.getString("date_of_birth")
                                var year_dob_aadhaar=""
                                if(!CustomStatic.IsPanForPhotoReg) {
                                    year_dob_aadhaar = tt.getString("year_of_birth")
                                }
                                var name_aadhaar=tt.getString("name_on_card")
                                var aadhaar_no_aadhaar=tt.getString("id_number")
                                try{
                                    //if(tt.getString("date_of_birth").equals("null")){
                                    if(dob_aadhaar.equals("null") || tt.getString("date_of_birth").equals("null")){
                                        if(year_dob_aadhaar.length>0 && !year_dob_aadhaar.equals("null")){
                                            dob_aadhaar=year_dob_aadhaar+"-02-28"
                                        }else{
                                            dob_aadhaar="1990-02-28"
                                        }
                                    }
                                }catch (ex:Exception){
                                    dob_aadhaar="1990-02-28"
                                }

                                if(name_aadhaar.equals("null")){
                                    name_aadhaar="Unknown"
                                }
                                if(dob_aadhaar.contains("null")){
                                    dob_aadhaar="1990-02-28"
                                }

                                Timber.d("PhotoRegAadhaarFragment : idfy response : user_id:"+ user_id+" name:"+name_aadhaar+" aarhaarDOB:"+dob_aadhaar+" aadhaar_no:"+aadhaar_no_aadhaar)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    submitCheckAadhaarData(name_aadhaar,dob_aadhaar,aadhaar_no_aadhaar)
                                }, 1000)

                            }catch (ex:Exception){
                                submitCheckAadhaarData("Unknown","1990-02-28","null")
                            }
                        }
                    },
                    object : Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError?) {
                            progress_wheel.stopSpinning()
                            try{
                                Timber.d("PhotoRegAadhaarFragment extractAadhaarDtls : idfy response : "+error.toString())
                            }catch (ex:java.lang.Exception){

                            }

                            deletePicApi(user_id!!,"Please provide valid ID card photo to register. Thanks.")
                        }
                    }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    //params["api-key"] = "dfe0a602-7e79-4a5b-af00-509fc0e8349a"  //test
                    params["api-key"] = "cebc560a-855d-429e-a050-8882b9debf60"
                    params["Content-Type"] = "application/json"
                    //params["account-id"] = "aaa73f1c1bdb/fa4cf738-2dda-41db-b0e5-0b406ebe6d2f"  //test
                    params["account-id"] = "1a3ae2d3a141/68665e20-bc63-4bb8-b725-f126521f3264"
                    return params
                }
            }
            jsonObjectRequest.setRetryPolicy(DefaultRetryPolicy(
                    12000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
            MySingleton.getInstance(mContext.applicationContext)!!.addToRequestQueue(jsonObjectRequest)



        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            progress_wheel.stopSpinning()
        }
    }

    private fun submitCheckAadhaarData(aadhaar_name:String,aarhaarDOB:String,aadhaar_no:String){

        Timber.d("PhotoRegAadhaarFragment : submitCheckAadhaarData : user_id:"+ user_id+" name:"+aadhaar_name+" aarhaarDOB:"+aarhaarDOB+" aadhaar_no:"+aadhaar_no+" DocType ${CustomStatic.IsAadhaarForPhotoReg}")
        progress_wheel.spin()
        var aadhaarSubmitData: AadhaarSubmitDataNew = AadhaarSubmitDataNew()
        aadhaarSubmitData.user_id= user_id!!
        aadhaarSubmitData.session_token= Pref.session_token!!
        aadhaarSubmitData.name_on_aadhaar= aadhaar_name!!
        aadhaarSubmitData.DOB_on_aadhaar= aarhaarDOB!!
        aadhaarSubmitData.Aadhaar_number= aadhaar_no!!

        aadhaarSubmitData.REG_DOC_TYP = "Aadhaar"

        if(CustomStatic.IsAadhaarForPhotoReg)
            aadhaarSubmitData.REG_DOC_TYP="Aadhaar"
        else if(CustomStatic.IsVoterForPhotoReg)
            aadhaarSubmitData.REG_DOC_TYP="Voter"
        else if(CustomStatic.IsPanForPhotoReg)
            aadhaarSubmitData.REG_DOC_TYP="PAN"

        var responseOuter=""

        try{
            val repository = GetUserListPhotoRegProvider.provideUserListPhotoReg()
            BaseActivity.compositeDisposable.add(
                    //repository.sendUserAadhaarInfoNewApi(aadhaarSubmitData)
                    repository.sendUserAadhaarInfoNewApi(aadhaarSubmitData.user_id,aadhaarSubmitData.name_on_aadhaar, aadhaarSubmitData.DOB_on_aadhaar,
                        aadhaarSubmitData.Aadhaar_number,aadhaarSubmitData.REG_DOC_TYP)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->

                                progress_wheel.stopSpinning()

                                val response = result as BaseResponse
                                responseOuter=response!!.status!!
                                Timber.d("PhotoRegAadhaarFragment : submitCheckAadhaarData response: "+response.status)
                                if (response.status == NetworkConstant.SUCCESS) {
                                    dialogSuccess()
                                }
                                else {
                                    deletePicApi(user_id!!,"ID already registered with same name & DOB. Please use unique ID card for this registration. Thanks.")
                                }

                            }, { error ->
                                error.printStackTrace()
                                progress_wheel.stopSpinning()

                                if(!responseOuter.equals("200")){
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                    Timber.d("PhotoRegAadhaarFragment : submitCheckAadhaarData errorr : "+error.message.toString() + " "+error.localizedMessage.toString() + " "+ error.cause.toString())
                                    //deletePicApi(user_id!!,"Duplicate ID Number. Please enter Unique ID number for current person. Thanks.")
                                    deletePicApi(user_id!!,"Please try again. Error info (${error.message})")
                                }
                                Timber.d("PhotoRegAadhaarFragment : submitCheckAadhaarData errorr : "+error.message.toString() + " "+error.localizedMessage.toString() + " "+ error.cause.toString())

                            })
            )
        }catch (ex:Exception){
            ex.printStackTrace()
            progress_wheel.stopSpinning()
            Timber.d("submitCheckAadhaarData ex : erro : "+ex.message)
            deletePicApi(user_id!!,"Please try again. Error info (${ex.message})")
        }

    }

    fun deletePicApi(usr_id: String,msg:String) {
        progress_wheel.spin()
        val repository = GetUserListPhotoRegProvider.providePhotoReg()
        BaseActivity.compositeDisposable.add(
                repository.deleteUserPicApi(usr_id, Pref.session_token!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            var response = result as DeleteUserPicResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                dialogFailed(msg)
                            } else {
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            Timber.d("PhotoRegAadhaarFragment : deletePicApi error: "+error.message)
//                            (mContext as DashboardActivity).showSnackMessage("ERROR")
                        })
        )

    }


    fun dialogSuccess(){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
        dialogHeader.text = "Registration Success."
        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            (mContext as DashboardActivity).loadFragment(FragType.ProtoRegistrationFragment, false, "")
        })
        simpleDialog.show()
        voiceAttendanceMsg(AppUtils.hiFirstNameText() +" Registration Success.")
    }

    fun dialogFailed(msg:String){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
        //dialogHeader.text = "Duplicate Aadhaar Number. Please enter Unique Aadhaar number for current person. Thanks."
        dialogHeader.text = msg
        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            (mContext as DashboardActivity).loadFragment(FragType.ProtoRegistrationFragment, false, "")
        })
        simpleDialog.show()
        voiceAttendanceMsg(msg)
    }


    private fun voiceAttendanceMsg(msg: String) {
        if (Pref.isVoiceEnabledForAttendanceSubmit) {
            val speechStatus = (mContext as DashboardActivity).textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
            if (speechStatus == TextToSpeech.ERROR)
                Log.e("Add Day Start", "TTS error in converting Text to Speech!");
        }
    }


    private fun registerFace(){
        progress_wheel.spin()
        var obj= UserPhotoRegModel()
        //obj.user_id= Pref.user_id
        obj.user_id= RegisTerFaceFragment.user_id
        obj.session_token=Pref.session_token

        //obj.registration_date_time=AppUtils.getCurrentDateTimeDDMMYY()
        obj.registration_date_time=AppUtils.getCurrentDateTime()

        val repository = GetUserListPhotoRegProvider.providePhotoReg()
        BaseActivity.compositeDisposable.add(
                repository.addUserFaceRegImg(obj,CustomStatic.FaceRegFaceImgPath,mContext, user_contactid)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as FaceRegResponse
                            if(response.status== NetworkConstant.SUCCESS){
                                Timber.d("Face Reg Url : "+response.face_image_link)
                                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.face_reg_success))
                                Handler(Looper.getMainLooper()).postDelayed({
                                    progress_wheel.stopSpinning()
                                    //(mContext as DashboardActivity).loadFragment(FragType.ProtoRegistrationFragment, false, "")
                                    CustomStatic.FacePicRegUrl=response.face_image_link
                                    //afterFaceRegistered()
                                    uploadDocPreparation()

                                    //(mContext as DashboardActivity).loadFragment(FragType.PhotoRegAadhaarFragment,true,valueData)
                                }, 500)

                                Timber.d(" RegisTerFaceFragment : FaceImageDetection/FaceImage" +response.status.toString() +", : "  + ", Success: "+AppUtils.getCurrentDateTime().toString())
                            }else{
                                progress_wheel.stopSpinning()
                                CustomStatic.FacePicRegUrl=""
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_reg_face))
                                deletePicApi(user_id!!,"Something went wrong. Please try again later")
                                Timber.d("RegisTerFaceFragment : FaceImageDetection/FaceImage : " + response.status.toString() +", : "  + ", Failed: "+AppUtils.getCurrentDateTime().toString())
                            }
                        },{
                            error ->
                            progress_wheel.stopSpinning()
                            CustomStatic.FacePicRegUrl=""
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_reg_face))
                            deletePicApi(user_id!!,"Something went wrong. Please try again later")
                            if (error != null) {
                                Timber.d("RegisTerFaceFragment : FaceImageDetection/FaceImage : " + " : "  + ", ERROR: " + error.localizedMessage)
                            }
                        })
        )
    }

    private fun uploadDocPreparation(){
        if(imagePath.length>0 && imagePath!="") {
            if (AppUtils.isOnline(mContext)) {
                tv_internet_info.setBackgroundColor(resources.getColor(R.color.color_custom_green))
                tv_internet_info.text = getString(R.string.registration_net_connected)
            } else {
                tv_internet_info.setBackgroundColor(resources.getColor(R.color.color_custom_red))
                tv_internet_info.text = getString(R.string.reg_net_disconnected)
                return
            }

            uploadAadhaarPic()

            /*val simpleDialogg = Dialog(mContext)
            simpleDialogg.setCancelable(false)
            simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialogg.setContentView(R.layout.dialog_yes_no)
            val dialogHeader = simpleDialogg.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
            val dialogHead = simpleDialogg.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
            dialogHeader.text="Submit for verification"
            dialogHead.text="Hi "+Pref.user_name!!+"!"
            val dialogYes = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
            val dialogNo = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

            dialogYes.setOnClickListener( { view ->
                simpleDialogg.cancel()
                if (AppUtils.isOnline(mContext)){
                    //uploadAadhaarPic()
                }else{
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                }

            })
            dialogNo.setOnClickListener( { view ->
                simpleDialogg.cancel()
            })
            simpleDialogg.show()*/
        }
    }


}