package com.nationalplasticfsm.features.commondialogsinglebtn

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.Selection
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.nationalplasticfsm.MySingleton
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.AppDatabase
import com.nationalplasticfsm.app.NetworkConstant
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.domain.AddShopDBModelEntity
import com.nationalplasticfsm.app.domain.ProspectEntity
import com.nationalplasticfsm.app.domain.ShopExtraContactEntity
import com.nationalplasticfsm.app.domain.ShopVisitCompetetorModelEntity
import com.nationalplasticfsm.app.domain.VisitRemarksEntity
import com.nationalplasticfsm.app.domain.VisitRevisitWhatsappStatus
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.app.utils.FTStorageUtils
import com.nationalplasticfsm.app.utils.PermissionUtils
import com.nationalplasticfsm.app.utils.Toaster
import com.nationalplasticfsm.base.presentation.BaseActivity
import com.nationalplasticfsm.features.addshop.presentation.ProspectListDialog
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.features.dashboard.presentation.VisitMultiContactAdapter
import com.nationalplasticfsm.features.dashboard.presentation.VisitRemarksTypeAdapter
import com.nationalplasticfsm.features.nearbyshops.api.ShopListRepositoryProvider
import com.nationalplasticfsm.features.nearbyshops.model.ProsListResponseModel
import com.nationalplasticfsm.widgets.AppCustomEditText
import com.nationalplasticfsm.widgets.AppCustomTextView

import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import com.themechangeapp.pickimage.PermissionHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.billing_adapter.view.*
import kotlinx.android.synthetic.main.dialog_add_feedback_single_btn.*
import kotlinx.android.synthetic.main.fragment_add_shop.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.File
import java.util.*

/**
 * Created by Saikat on 31-01-2020.
 */
// 1.0  AppV 4.0.6  AddFeedbackSingleBtnDialog  Saheli    03/01/2023 Checking block feedback issue RevisitRemarksMandatory is true mantis 0025557
// 2.0  AppV 4.0.6  AddFeedbackSingleBtnDialog  Suman 20/01/2023 contact person selection mandatory if IsContactPersonSelectionRequiredinRevisit is true
// 3.0  AppV 4.0.7  AddFeedbackSingleBtnDialog  Saheli    07/01/2023 mantis 25649 add feedback using  voice
// 4.0  AppV 4.0.7  AddFeedbackSingleBtnDialog  Saheli    13/01/2023 mantis 25649 add feedback using  voice plus text handle
// 5.0  AppV 4.0.7  AddFeedbackSingleBtnDialog  Saheli    22/01/2023 mantis 25649 modified due to UI problem

class AddFeedbackSingleBtnDialog : DialogFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var dialogHeader: AppCustomTextView
    private lateinit var et_feedback: AppCustomEditText
    private lateinit var dialogOk: AppCustomTextView
    private lateinit var iv_close_icon: AppCompatImageView
    private lateinit var et_next_visit_date: AppCustomEditText
    private lateinit var tv_visit_date_asterisk_mark: AppCustomTextView
    private lateinit var et_audio: AppCustomEditText
    private lateinit var rl_audio: RelativeLayout
    private lateinit var tv_remarks_dropdown: AppCustomTextView
    private lateinit var rl_remarks: RelativeLayout
    private lateinit var til_feedback: TextInputLayout
    private lateinit var ll_competitorImg: RelativeLayout

    private lateinit var rl_approxvalue_main: RelativeLayout

    private lateinit var rl_prospect_main: RelativeLayout
    private lateinit var iv_prospect_dropdownn: AppCustomTextView

    private lateinit var et_approxvalue_name: AppCustomEditText
    private lateinit var rl_multiContact: RelativeLayout
    private lateinit var tv_multiContact: AppCustomTextView

    private var sel_extraContName : String = ""
    private var sel_extraContPh : String = ""


    private lateinit var tv_visit_asterisk_mark: AppCustomTextView

    private var visitRemarksPopupWindow: PopupWindow? = null
    private var visitMultiContactPopupWindow: PopupWindow? = null
    private  var audioFile: File? = null
    private var nextVisitDate = ""
    private var filePath = ""

    private var ProsId = ""

    private var shopType = ""

    private lateinit var iv_dialog_add_feedback_mic:ImageView // 3.0  AppV 4.0.7  AddFeedbackSingleBtnDialog mantis 25649 add feedback using voice


    private var  suffixText:String = "" // 4.0  AppV 4.0.7  AddFeedbackSingleBtnDialog mantis 25649 add feedback using  voice plus text handle

    private val myCalendar by lazy {
        Calendar.getInstance(Locale.ENGLISH)
    }

    companion object {
        private lateinit var mHeader: String
        private lateinit var mActionBtn: String
        private lateinit var mShopID: String
        //private lateinit var mRightBtn: String
        private lateinit var mListener: OnOkClickListener

        fun getInstance(header: String, actionBtn: String,shopID: String, listener: OnOkClickListener): AddFeedbackSingleBtnDialog {
            val cardFragment = AddFeedbackSingleBtnDialog()
            mHeader = header
            mActionBtn = actionBtn
            mShopID = shopID
            mListener = listener
            return cardFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window!!.setBackgroundDrawableResource(R.drawable.rounded_corner_white_bg)
        val v = inflater.inflate(R.layout.dialog_add_feedback_single_btn, container, false)
        initView(v)

        isCancelable = false

        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun initView(v: View) {
        dialogHeader = v.findViewById(R.id.dialog_header_TV)
        et_feedback = v.findViewById(R.id.et_feedback)
        dialogOk = v.findViewById(R.id.ok_TV)
        dialogOk.isSelected = false
        dialogOk.text = mActionBtn
        iv_close_icon = v.findViewById(R.id.iv_close_icon)
        et_next_visit_date = v.findViewById(R.id.et_next_visit_date)
        tv_visit_date_asterisk_mark = v.findViewById(R.id.tv_visit_date_asterisk_mark)
        tv_visit_asterisk_mark = v.findViewById(R.id.tv_visit_asterisk_mark)
        et_audio = v.findViewById(R.id.et_audio)
        rl_audio = v.findViewById(R.id.rl_audio)
        tv_remarks_dropdown = v.findViewById(R.id.tv_remarks_dropdown)
        rl_remarks = v.findViewById(R.id.rl_remarks)
        til_feedback = v.findViewById(R.id.til_feedback)
        ll_competitorImg = v.findViewById(R.id.rl_competitor_image)

        /*12-12-2021*/
        rl_approxvalue_main = v.findViewById(R.id.rl_approxvalue_main)
        rl_prospect_main = v.findViewById(R.id.rl_prospect_main)
        iv_prospect_dropdownn = v.findViewById(R.id.iv_prospect_dropdownn)
        et_approxvalue_name =  v.findViewById(R.id.et_approxvalue_name)

        rl_multiContact = v.findViewById(R.id.rl_dialog_add_feed_single_extra_contact_root)
        tv_multiContact = v.findViewById(R.id.tv_dialog_add_feed_single_extra_contact_dropdown)
        iv_dialog_add_feedback_mic =  v.findViewById(R.id.iv_dialog_add_feedback_mic)// 3.0  AppV 4.0.7  AddFeedbackSingleBtnDialog mantis 25649 add feedback using voice
        tv_multiContact.setOnClickListener(this)

        if(Pref.IsContactPersonSelectionRequiredinRevisit){
            rl_multiContact.visibility = View.VISIBLE
        }else{
            rl_multiContact.visibility = View.GONE
        }


        /*13-12-2021*/
        shopType = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopType(mShopID).toString()

        dialogHeader.text = mHeader

        if (Pref.isNextVisitDateMandatory) {
            tv_visit_date_asterisk_mark.visibility = View.VISIBLE
            //iv_close_icon.visibility = View.GONE
        } else {
            tv_visit_date_asterisk_mark.visibility = View.GONE
            //iv_close_icon.visibility = View.VISIBLE
        }

        if (Pref.isRecordAudioEnable)
            rl_audio.visibility = View.VISIBLE
        else
            rl_audio.visibility = View.GONE

        if (Pref.isRecordAudioEnable || Pref.isNextVisitDateMandatory)
            iv_close_icon.visibility = View.GONE
        else
            iv_close_icon.visibility = View.VISIBLE

        if (Pref.isShowVisitRemarks) {
            rl_remarks.visibility = View.VISIBLE
            til_feedback.visibility = View.GONE
            iv_dialog_add_feedback_mic.visibility = View.GONE //5.0  AppV 4.0.7  AddFeedbackSingleBtnDialog mantis 25649 modified due to UI problem
        }
        else {
            rl_remarks.visibility = View.GONE
            til_feedback.visibility = View.VISIBLE
        }

        if(AppUtils.getSharedPreferenceslogCompetitorImgEnable(mContext))
            ll_competitorImg.visibility=View.VISIBLE
        else
            ll_competitorImg.visibility=View.GONE


        if(Pref.IsnewleadtypeforRuby && shopType.equals("16")){
            rl_approxvalue_main.visibility = View.VISIBLE
            rl_prospect_main.visibility = View.VISIBLE
            ll_competitorImg.visibility=View.GONE
//            var list  = AppDatabase.getDBInstance()!!.shopActivityDao().getAll()
//            if(list.size==0){
//                Toaster.msgShort(mContext, "please wait,background data under snyc")
//                return
//            }
//            else{
                var shopActivityListToProsId = AppDatabase.getDBInstance()!!.shopActivityDao().getProsId(mShopID).toString()
                var prosNameByID=""
                if(shopActivityListToProsId!=null || !shopActivityListToProsId.equals("")){
                    prosNameByID = AppDatabase.getDBInstance()!!.prosDao().getProsNameByProsId(shopActivityListToProsId)
                }
                iv_prospect_dropdownn.text = prosNameByID // select pros name showing
                ProsId=shopActivityListToProsId


        }




        /*28-09-2021 For Gupta Power*/
        if (Pref.RevisitRemarksMandatory) {
            tv_visit_asterisk_mark.visibility = View.VISIBLE

        } else {
            tv_visit_asterisk_mark.visibility = View.GONE
        }

        dialogOk.setOnClickListener(this)
        iv_close_icon.setOnClickListener(this)
        et_next_visit_date.setOnClickListener(this)
        et_audio.setOnClickListener(this)
        tv_remarks_dropdown.setOnClickListener(this)

        ll_competitorImg.setOnClickListener(this)
        rl_prospect_main.setOnClickListener(this)
        rl_approxvalue_main.setOnClickListener(this)
        iv_dialog_add_feedback_mic.setOnClickListener(this)// 3.0  AppV 4.0.7  AddFeedbackSingleBtnDialog mantis 25649 add feedback using voice
    }

    override fun onClick(p0: View?) {
//        deSelectAll()
        when (p0!!.id) {
            R.id.ok_TV -> {
                iv_close_icon.isEnabled=true

                var str_remarks =  ""
                str_remarks = tv_remarks_dropdown.text.toString().trim().toString()

                var msg = ""
                    if(TextUtils.isEmpty(tv_remarks_dropdown.text.toString().trim().toString()) && Pref.isShowVisitRemarks)
                        msg =  "Please put the remarks"
                    else if(TextUtils.isEmpty(et_feedback.text.toString().trim()) && str_remarks.equals(""))
                        msg = "Please put the feedback"

                if(Pref.IsContactPersonSelectionRequiredinRevisit && sel_extraContName.equals("")){
                    Toaster.msgShort(mContext, "Please select Contact Person")
                    return
                }

                //if (Pref.RevisitRemarksMandatory && TextUtils.isEmpty(tv_remarks_dropdown.text.toString().trim()))
                if (Pref.RevisitRemarksMandatory && !msg.equals(""))
                    Toaster.msgShort(mContext, msg)
                else if (Pref.isNextVisitDateMandatory && TextUtils.isEmpty(nextVisitDate))
                    Toaster.msgShort(mContext, getString(R.string.error_message_next_visit_date))
                else if (Pref.isRecordAudioEnable && TextUtils.isEmpty(et_audio.text.toString().trim()))
                    Toaster.msgShort(mContext, getString(R.string.error_message_audio))
                else if (Pref.IsnewleadtypeforRuby && shopType.equals("16") && TextUtils.isEmpty(et_approxvalue_name.text.toString().trim()))
                    Toaster.msgShort(mContext, getString(R.string.error_message_approx))
                else {
                    dialogOk.isSelected = true

                    if(Pref.IsAutomatedWhatsAppSendforRevisit && sel_extraContPh!= ""){
                        var shopObj: AddShopDBModelEntity = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(mShopID)
                        var obj = VisitRevisitWhatsappStatus()
                        obj.shop_id = mShopID
                        obj.shop_name = shopObj.shopName
                        obj.contactNo = sel_extraContPh
                        obj.isNewShop = false
                        obj.date = AppUtils.getCurrentDateForShopActi()
                        obj.time = AppUtils.getCurrentTime()
                        obj.isWhatsappSent = false
                        obj.whatsappSentMsg =""
                        obj.isUploaded = false
                        AppDatabase.getDBInstance()?.visitRevisitWhatsappStatusDao()!!.insert(obj)

                        if(AppUtils.isOnline(mContext)){
                            var shopWiseWhatsObj = AppDatabase.getDBInstance()?.visitRevisitWhatsappStatusDao()!!.getByShopIDDate(mShopID,AppUtils.getCurrentDateForShopActi())
                            try{
                                val stringRequest: StringRequest = object : StringRequest(
                                    Request.Method.POST, "https://theultimate.io/WAApi/send",
                                    Response.Listener<String?> { response ->

                                        var resp = JsonParser.parseString(response)
                                        var statusCode = resp.asJsonObject.get("statusCode").toString().drop(1).dropLast(1)
                                        var statusMsg = resp.asJsonObject.get("reason").toString().drop(1).dropLast(1)
                                        var transId = resp.asJsonObject.get("transactionId").toString().drop(1).dropLast(1)
                                        if(transId == null){
                                            transId = ""
                                        }

                                        if(statusCode.equals("200",ignoreCase = true) && statusMsg.equals("success",ignoreCase = true)){
                                            AppDatabase.getDBInstance()?.visitRevisitWhatsappStatusDao()!!.
                                            updateWhatsStatus(true,"Sent Successfully",shopWiseWhatsObj!!.sl_no,transId)
                                        }else{
                                            AppDatabase.getDBInstance()?.visitRevisitWhatsappStatusDao()!!.
                                            updateWhatsStatus(false,statusMsg.toString(),shopWiseWhatsObj!!.sl_no,transId)
                                        }
                                    },
                                    Response.ErrorListener { error ->
                                        var e = error.toString()
                                    })
                                {
                                    override fun getParams(): Map<String, String>? {
                                        val params: MutableMap<String, String> = HashMap()
                                        params.put("userid", "eurobondwa")
                                        params.put("msg", "Hey there!\n" +
                                                "Hope you had a successful meeting with (${Pref.user_name} - ${Pref.UserLoginContactID})\n" +
                                                "We’ll be happy to assist you further with any inquiries or support you may require.\n" +
                                                "*Team Eurobond*\n")
                                        params.put("wabaNumber", "917888488891")
                                        params.put("output", "json")
                                        //params.put("mobile", "919830916971")
                                        params.put("mobile", "91${obj.contactNo}")
                                        params.put("sendMethod", "quick")
                                        params.put("msgType", "text")
                                        params.put("templateName", "incoming_call_response_2")
                                        return params
                                    }
                                    override fun getHeaders(): MutableMap<String, String> {
                                        val params: MutableMap<String, String> = HashMap()
                                        params["apikey"] = "36328e9735f7012988e6ed58f9fffaec4c7a79eb"
                                        return params
                                    }
                                }
                                MySingleton.getInstance(mContext.applicationContext)!!.addToRequestQueue(stringRequest)
                            }
                            catch (ex:Exception){
                                ex.printStackTrace()
                            }
                        }
                    }

                    Handler().postDelayed(Runnable {
                        dismiss()
                        if (Pref.RevisitRemarksMandatory){
//                        mListener.onOkClick(tv_remarks_dropdown.text.toString().trim(), nextVisitDate, filePath,et_approxvalue_name.text.toString(),ProsId)
                            // 1.0  AppV 4.0.6  AddFeedbackSingleBtnDialog  start
                            if (!Pref.isShowVisitRemarks)
                                mListener.onOkClick(et_feedback.text.toString().trim(), nextVisitDate, filePath,et_approxvalue_name.text.toString(),ProsId,sel_extraContName,sel_extraContPh)
                            else
                                mListener.onOkClick(tv_remarks_dropdown.text.toString().trim(), nextVisitDate, filePath,et_approxvalue_name.text.toString(),ProsId,sel_extraContName,sel_extraContPh)
                            // 1.0  AppV 4.0.6  AddFeedbackSingleBtnDialog  end
                        }
                        else{
                            if (!Pref.isShowVisitRemarks)
                                mListener.onOkClick(et_feedback.text.toString().trim(), nextVisitDate, filePath,et_approxvalue_name.text.toString(),ProsId,sel_extraContName,sel_extraContPh)
                            else
                                mListener.onOkClick(tv_remarks_dropdown.text.toString().trim(), nextVisitDate, filePath,et_approxvalue_name.text.toString(),ProsId,sel_extraContName,sel_extraContPh)
                        }
                    }, 1700)


                }
            }
            R.id.iv_close_icon -> {

                var str_remarks =  ""
                str_remarks = tv_remarks_dropdown.text.toString().trim().toString()

                if(Pref.IsContactPersonSelectionRequiredinRevisit && sel_extraContName.equals("")){
                    Toaster.msgShort(mContext, "Please select Contact Person")
                    return
                }

                var msg = ""
                if(TextUtils.isEmpty(tv_remarks_dropdown.text.toString().trim()) && Pref.isShowVisitRemarks)
                    msg =  "Please put the remarks"
                else if(TextUtils.isEmpty(et_feedback.text.toString().trim()) && str_remarks.equals(""))
                    msg = "Please put the feedback"

                //if (Pref.RevisitRemarksMandatory && TextUtils.isEmpty(tv_remarks_dropdown.text.toString().trim()))
                if (Pref.RevisitRemarksMandatory && !msg.equals(""))
                    Toaster.msgShort(mContext, msg)
                else{
                    // 9.0 DashboardActivity AppV 4.0.6 Suman 24-01-2023  Corss button with multi contact select
                    dismiss()
                    mListener.onCloseClick(tv_remarks_dropdown.text.toString().trim(),sel_extraContName,sel_extraContPh)
                }
            }

            R.id.et_next_visit_date -> {
                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                val aniDatePicker = DatePickerDialog(mContext, R.style.DatePickerTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                aniDatePicker.datePicker.minDate = Calendar.getInstance(Locale.ENGLISH).timeInMillis + (1000 * 60 * 60 * 24)
                aniDatePicker.show()
            }

            R.id.et_audio -> {
                val folderPath = FTStorageUtils.getFolderPath(mContext)
                audioFile = File("$folderPath/" + System.currentTimeMillis() + ".mp3")

                AndroidAudioRecorder.with(mContext as DashboardActivity)
                        // Required
                        .setFilePath(audioFile?.absolutePath)
                        .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                        .setRequestCode(PermissionHelper.REQUEST_CODE_AUDIO)
                        .setAutoStart(false)
                        .setKeepDisplayOn(true)
                    .setSampleRate(AudioSampleRate.HZ_100)
                        // Start recording
                        .record()
            }

            R.id.tv_remarks_dropdown -> {
                val list = AppDatabase.getDBInstance()?.visitRemarksDao()?.getAll()
                if (list == null || list.isEmpty())
                    Toaster.msgShort(mContext, getString(R.string.no_data_found))
                else {
                    if (visitRemarksPopupWindow != null && visitRemarksPopupWindow?.isShowing!!)
                        visitRemarksPopupWindow?.dismiss()

                    callMeetingTypeDropDownPopUp(list)
                }
            }
            R.id.rl_competitor_image -> {
                mListener.onClickCompetitorImg()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    initPermissionCheckOne()
                else
                    showPictureDialog()
            }

            /*12-12-2021*/
            R.id.rl_prospect_main -> {
                val list = AppDatabase.getDBInstance()?.prosDao()?.getAll() as ArrayList<ProspectEntity>
                if (list == null || list.isEmpty())
                    getProspectApi()
                else
                    showProsDialog(list)
            }


            R.id.rl_approxvalue_main->{

            }
            R.id.tv_dialog_add_feed_single_extra_contact_dropdown ->{
                if (visitRemarksPopupWindow != null && visitRemarksPopupWindow?.isShowing!!)
                    visitRemarksPopupWindow?.dismiss()
                callMultiContactDialog()
            }
            // 3.0  AppV 4.0.7  AddFeedbackSingleBtnDialog mantis 25649 add feedback using voice
            R.id.iv_dialog_add_feedback_mic->{
                suffixText = et_feedback.text.toString().trim() // 4.0  AppV 4.0.7  AddFeedbackSingleBtnDialog mantis 25649 add feedback using  voice plus text handle
                startVoiceInput()
            }

        }
    }

    @SuppressLint("MissingInflatedId")
    private fun callMultiContactDialog(){
        var extraContL = AppDatabase.getDBInstance()?.shopExtraContactDao()?.getExtraContListByShopID(mShopID!!) as ArrayList<ShopExtraContactEntity>
        var shopDtlsInfo = AppDatabase.getDBInstance()?.addShopEntryDao()!!.getShopDetail(mShopID)

        var obj :ShopExtraContactEntity = ShopExtraContactEntity()
        obj.apply {
            shop_id = shopDtlsInfo.shop_id
            contact_serial = ""
            contact_name = shopDtlsInfo.ownerName
            contact_number = shopDtlsInfo.ownerContactNumber
            contact_email = shopDtlsInfo.ownerEmailId
            contact_doa = shopDtlsInfo.dateOfAniversary
            isUploaded = true
        }
        extraContL.add(obj)

        if(extraContL!!.size>0){
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
            val customView = inflater!!.inflate(R.layout.popup_multi_caontact, null)
            visitMultiContactPopupWindow = PopupWindow(customView, resources.getDimensionPixelOffset(R.dimen._220sdp), RelativeLayout.LayoutParams.WRAP_CONTENT)
            val rv_multi_contact_type_list = customView.findViewById(R.id.rv_multi_contact_type_list) as RecyclerView
            rv_multi_contact_type_list.layoutManager = LinearLayoutManager(mContext)

            visitMultiContactPopupWindow?.elevation = 200f
            visitMultiContactPopupWindow?.isFocusable = true
            visitMultiContactPopupWindow?.update()

            rv_multi_contact_type_list.adapter = VisitMultiContactAdapter(mContext, extraContL as ArrayList<ShopExtraContactEntity>, object : VisitMultiContactAdapter.OnItemClickListener {
                override fun onItemClick(obj: ShopExtraContactEntity) {
                    tv_multiContact.text = obj.contact_name+" (${obj.contact_number})"
                    sel_extraContName = obj.contact_name!!.toString()
                    sel_extraContPh = obj.contact_number!!.toString()
                    visitMultiContactPopupWindow?.dismiss()
                }
            })

            if (visitMultiContactPopupWindow != null && !visitMultiContactPopupWindow?.isShowing!!) {
                visitMultiContactPopupWindow?.showAsDropDown(tv_multiContact, tv_multiContact.width - visitMultiContactPopupWindow?.width!!, 0)

            }
        }
    }

    private var permissionUtils: PermissionUtils? = null
    private fun initPermissionCheckOne() {

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
                showPictureDialog()
            }

            override fun onPermissionNotGranted() {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
            }
// mantis id 26741 Storage permission updation Suman 22-08-2023
        },permissionList)// arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(mContext)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> selectImageInAlbum()
                1 -> launchCamera()
            }
        }
        pictureDialog.show()
    }

    fun selectImageInAlbum() {
        if (PermissionHelper.checkStoragePermission(mContext as DashboardActivity)) {
            val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            (mContext as DashboardActivity).startActivityForResult(intent, PermissionHelper.REQUEST_CODE_STORAGE)

        }
    }

    fun launchCamera() {
        if (PermissionHelper.checkCameraPermission(mContext as DashboardActivity) && PermissionHelper.checkStoragePermission(mContext as DashboardActivity)) {
            /*val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, (mContext as DashboardActivity).getPhotoFileUri(System.currentTimeMillis().toString() + ".png"))
            (mContext as DashboardActivity).startActivityForResult(intent, PermissionHelper.REQUEST_CODE_CAMERA)*/

            (mContext as DashboardActivity).captureImage()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun callMeetingTypeDropDownPopUp(list: List<VisitRemarksEntity>) {

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

        // Inflate the custom layout/view
        val customView = inflater!!.inflate(R.layout.popup_meeting_type, null)

        visitRemarksPopupWindow = PopupWindow(customView, resources.getDimensionPixelOffset(R.dimen._220sdp), RelativeLayout.LayoutParams.WRAP_CONTENT)
        val rv_meeting_type_list = customView.findViewById(R.id.rv_meeting_type_list) as RecyclerView
        rv_meeting_type_list.layoutManager = LinearLayoutManager(mContext)

        visitRemarksPopupWindow?.elevation = 200f
        visitRemarksPopupWindow?.isFocusable = true
        visitRemarksPopupWindow?.update()


        rv_meeting_type_list.adapter = VisitRemarksTypeAdapter(mContext, list as ArrayList<VisitRemarksEntity>, object : VisitRemarksTypeAdapter.OnItemClickListener {
            override fun onItemClick(adapterPosition: Int) {
                tv_remarks_dropdown.text = list[adapterPosition].name
                visitRemarksPopupWindow?.dismiss()
            }
        })

        if (visitRemarksPopupWindow != null && !visitRemarksPopupWindow?.isShowing!!) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                rl_remarks.post(Runnable {
                    visitRemarksPopupWindow?.showAsDropDown(tv_remarks_dropdown, resources.getDimensionPixelOffset(R.dimen._1sdp), resources.getDimensionPixelOffset(R.dimen._10sdp), Gravity.BOTTOM)
                })
            } else {
                visitRemarksPopupWindow?.showAsDropDown(tv_remarks_dropdown, tv_remarks_dropdown.width - visitRemarksPopupWindow?.width!!, 0)
            }
        }
    }

    val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        nextVisitDate = AppUtils.getFormattedDateForApi(myCalendar.time)
        et_next_visit_date.setText(AppUtils.changeAttendanceDateFormat(AppUtils.getDobFormattedDate(myCalendar.time)))
    }

    private fun deSelectAll() {
        dialogOk.isSelected = false
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            //if (!dialog.isShowing) {
            val ft = manager?.beginTransaction()
            ft?.add(this, tag)
            ft?.commitAllowingStateLoss()
            //}
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    interface OnOkClickListener {
        fun onOkClick(feedback: String, nextVisitDate: String, filePath: String,approxValue:String,prosId:String,sel_extraContNameStr :String,sel_extraContPhStr : String)

        fun onCloseClick(mfeedback: String,sel_extraContNameStr :String,sel_extraContPhStr : String)

        fun onClickCompetitorImg()
    }

    fun setAudio(){
        filePath = audioFile?.absolutePath!!
        et_audio.setText(audioFile?.absolutePath)
    }

    fun setImage(imgRealPath: Uri, fileSizeInKB: Long) {
        val imagePathCompetitor = imgRealPath.toString()
        Picasso.get()
                .load(imgRealPath)
                .resize(500, 100)
                .into(iv_competitor_image_view_feedback)

        var obj: ShopVisitCompetetorModelEntity = ShopVisitCompetetorModelEntity()
        obj.session_token=Pref.session_token!!
        obj.shop_id=mShopID
        obj.user_id=Pref.user_id!!
        obj.shop_image=imagePathCompetitor
        obj.isUploaded=false
        obj.visited_date=AppUtils.getCurrentDateTime()
        AppDatabase.getDBInstance()!!.shopVisitCompetetorImageDao().insert(obj)

        iv_close_icon.isEnabled=false
    }

    private fun getProspectApi() {
        try {
            val list = AppDatabase.getDBInstance()?.prosDao()?.getAll()
            if (list!!.size == 0) {
                val repository = ShopListRepositoryProvider.provideShopListRepository()
                BaseActivity.compositeDisposable.add(
                        repository.getProsList()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe({ result ->
                                    val response = result as ProsListResponseModel
                                    Timber.d("GET PROS DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                                    if (response.status == NetworkConstant.SUCCESS) {
                                        if (response.Prospect_list != null && response.Prospect_list!!.isNotEmpty()) {
                                            doAsync {
                                                AppDatabase.getDBInstance()?.prosDao()?.insertAll(response.Prospect_list!!)
                                                uiThread {

                                                }
                                            }
                                        } else {

                                        }
                                    } else {

                                    }

                                }, { error ->


                                })
                )
            } else {

            }
        } catch (ex: Exception) {
            ex.printStackTrace()


        }

    }

    private fun showProsDialog(prosList: ArrayList<ProspectEntity>) {
        ProspectListDialog.newInstance(prosList) { pros: ProspectEntity ->
            iv_prospect_dropdownn.text = pros.pros_name
            ProsId = pros.pros_id!!
        }.show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    // 3.0  AppV 4.0.7  AddFeedbackSingleBtnDialog mantis 25649 add feedback using voice
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 7009){
            try{
            val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            var t= result!![0]
            // 4.0  AppV 4.0.7  AddFeedbackSingleBtnDialog mantis 25649 add feedback using  voice plus text handle
            if(suffixText.length>0 && !suffixText.equals("")){
                var setFullText = suffixText+t
                et_feedback.setText(suffixText+t)
                et_feedback.setSelection(setFullText.length);
            }else{
                var SuffixPostText = t+et_feedback.text.toString()
                et_feedback.setText(SuffixPostText)
                et_feedback.setSelection(SuffixPostText.length);
            }
            }
            catch (ex:Exception) {
                ex.printStackTrace()
            }
//            et_feedback.setText(t)
        }
    }
}