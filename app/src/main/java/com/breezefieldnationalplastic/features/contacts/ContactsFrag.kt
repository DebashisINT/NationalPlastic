package com.breezefieldnationalplastic.features.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.MobileContact
import com.breezefieldnationalplastic.MySingleton
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.domain.CallHisEntity
import com.breezefieldnationalplastic.app.domain.CompanyMasterEntity
import com.breezefieldnationalplastic.app.domain.ContactActivityEntity
import com.breezefieldnationalplastic.app.domain.ShopActivityEntity
import com.breezefieldnationalplastic.app.domain.SourceMasterEntity
import com.breezefieldnationalplastic.app.domain.StageMasterEntity
import com.breezefieldnationalplastic.app.domain.StatusMasterEntity
import com.breezefieldnationalplastic.app.domain.TeamListEntity
import com.breezefieldnationalplastic.app.domain.TypeMasterEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.uiaction.IntentActionable
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.PermissionUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.app.widgets.MovableFloatingActionButton
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.addshop.api.AddShopRepositoryProvider
import com.breezefieldnationalplastic.features.addshop.model.AddShopRequestData
import com.breezefieldnationalplastic.features.addshop.model.AddShopResponse
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.location.LocationWizard
import com.breezefieldnationalplastic.features.location.SingleShotLocationProvider
import com.breezefieldnationalplastic.features.login.api.global_config.ConfigFetchRepoProvider
import com.breezefieldnationalplastic.features.login.api.opportunity.OpportunityRepoProvider
import com.breezefieldnationalplastic.features.login.api.productlistapi.ProductListRepoProvider
import com.breezefieldnationalplastic.features.login.api.user_config.UserConfigRepoProvider
import com.breezefieldnationalplastic.features.login.model.globalconfig.ConfigFetchResponseModel
import com.breezefieldnationalplastic.features.login.model.opportunitymodel.OpportunityStatusListResponseModel
import com.breezefieldnationalplastic.features.login.model.productlistmodel.ProductListResponseModel
import com.breezefieldnationalplastic.features.login.model.userconfig.UserConfigResponseModel
import com.breezefieldnationalplastic.features.login.presentation.LoginActivity
import com.breezefieldnationalplastic.features.member.api.TeamRepoProvider
import com.breezefieldnationalplastic.features.nearbyshops.api.ShopListRepositoryProvider
import com.breezefieldnationalplastic.features.nearbyshops.model.ShopData
import com.breezefieldnationalplastic.features.nearbyshops.model.ShopListResponse
import com.breezefieldnationalplastic.features.nearbyshops.presentation.AdapterCallLogL
import com.breezefieldnationalplastic.features.nearbyshops.presentation.ShopAddressUpdateListener
import com.breezefieldnationalplastic.features.nearbyshops.presentation.UpdateShopAddressDialog
import com.breezefieldnationalplastic.features.nearbyshops.presentation.UpdateShopStatusDialog
import com.breezefieldnationalplastic.features.shopdetail.presentation.api.EditShopRepoProvider
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.google.gson.Gson
import com.itextpdf.text.BadElementException
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.HashMap
import java.util.Locale
import java.util.Random


class ContactsFrag : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var mFab: MovableFloatingActionButton
    private lateinit var tvNodata: TextView
    private lateinit var ivContactSync: LinearLayout
    private lateinit var iv_frag_APICheckTest: LinearLayout
    private lateinit var ivContactSyncDel: ImageView
    private lateinit var iv_click_scheduler: LinearLayout
    private lateinit var adapterContGr:AdapterContactGr
    private lateinit var adapterContName:AdapterContactName
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var rvContactL: RecyclerView
    private lateinit var tv_syncAll: LinearLayout

    private lateinit var et_search: AppCustomEditText
    private lateinit var iv_search: ImageView
    private lateinit var iv_mic: ImageView
    private lateinit var tv_filteredOnText: TextView

    private var locationStr_lat:String = ""
    private var locationStr_long:String = ""
    private var locationStr:String = ""
    private var location_pinStr:String = ""

    private lateinit var adapterContactList: AdapterContactList

    private var permissionUtils: PermissionUtils? = null
    private var contGrDialog: Dialog? = null
    private var instructionDialog: Dialog? = null

    private lateinit var floating_fab: FloatingActionMenu

    private lateinit var adapterCallLogL : AdapterCallLogL
    private lateinit var getFloatingVal: ArrayList<String>
    private lateinit var programFab1: FloatingActionButton
    private lateinit var programFab2: FloatingActionButton
    private lateinit var programFab3: FloatingActionButton
    private lateinit var programFab4: FloatingActionButton
    private lateinit var programFab5: FloatingActionButton
    private lateinit var programFab6: FloatingActionButton

    lateinit var simpleDialogProcess : Dialog
    lateinit var dialogHeaderProcess: AppCustomTextView
    lateinit var dialog_yes_no_headerTVProcess: AppCustomTextView
    lateinit var dialog_tv_message_ok: AppCustomTextView
    lateinit var dialog_pg: ProgressWheel
    private lateinit var ll_no_data_root:LinearLayout
    private lateinit var tv_empty_page_msg_head:TextView
    private lateinit var tv_empty_page_msg:TextView
    private lateinit var img_direction:ImageView

    private lateinit var viewPhoneBook:View
    private lateinit var viewScheduler:View

    private  var str_filterEntity=""
    private  var str_filterEntityValue=""
    private  var str_filterText=""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private var myCalendar = Calendar.getInstance()
    private var selectedDate = ""
    private var selectedShopIdForActivity = ""
    val dates = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        selectedDate = AppUtils.getFormattedDateForApi(myCalendar.time)
        updateLabel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_contacts, container, false)
        initView(view)
        return view
    }

    @SuppressLint("RestrictedApi")
    private fun initView(view: View) {
        progress_wheel = view.findViewById(R.id.progress_wheel_frag_add_cont)
        tvNodata = view.findViewById(R.id.tv_frag_add_cont_noData)
        rvContactL = view.findViewById(R.id.rv_frag_contacts_list)
        mFab = view.findViewById(R.id.fab_frag_contacts_add_contacts)
        ivContactSync = view.findViewById(R.id.iv_frag_contacts_dialog)
        ivContactSyncDel = view.findViewById(R.id.iv_frag_contacts_dialog_del)
        iv_click_scheduler = view.findViewById(R.id.iv_click_scheduler)

        iv_mic = view.findViewById(R.id.iv_frag_contacts_mic)
        et_search = view.findViewById(R.id.et_frag_contacts_search)
        iv_search = view.findViewById(R.id.iv_frag_contacts_search)
        floating_fab = view.findViewById(R.id.floating_fab_contact_frag)

        tv_syncAll = view.findViewById(R.id.tv_frag_contact_sync_all)
        ll_no_data_root = view.findViewById(R.id.ll_no_data_root)
        tv_empty_page_msg_head = view.findViewById(R.id.tv_empty_page_msg_head)
        tv_empty_page_msg = view.findViewById(R.id.tv_empty_page_msg)
        img_direction = view.findViewById(R.id.img_direction)
        iv_frag_APICheckTest = view.findViewById(R.id.iv_frag_APICheckTest)


        viewPhoneBook = view.findViewById(R.id.view_frag_cont_phone_book)
        viewScheduler = view.findViewById(R.id.view_frag_cont_scheduler)
        tv_filteredOnText = view.findViewById(R.id.tv_frag_cont_filtered_on_text)

        tv_filteredOnText.visibility = View.GONE

        floating_fab.menuIconView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_dashboard_filter_icon))
        floating_fab.menuButtonColorNormal = mContext.resources.getColor(R.color.colorAccent)
        floating_fab.menuButtonColorPressed = mContext.resources.getColor(R.color.colorPrimaryDark)
        floating_fab.menuButtonColorRipple = mContext.resources.getColor(R.color.colorPrimary)
        floating_fab.isIconAnimated = false
        floating_fab.setClosedOnTouchOutside(true)

        getFloatingVal = ArrayList()
        getFloatingVal.add("Alphabetically")
        getFloatingVal.add("Added Date")
        //getFloatingVal.add("Most Visited")
        var preid = 100

        getFloatingVal.add("Status")
        getFloatingVal.add("Stage")

        if(Pref.AdditionalinfoRequiredforContactAdd){
            getFloatingVal.add("Contact Type")
            getFloatingVal.add("Source")
        }

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
            if (i == 3) {
                programFab4 = FloatingActionButton(activity)
                programFab4.buttonSize = FloatingActionButton.SIZE_MINI
                programFab4.id = preid + i
                programFab4.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab4.colorPressed = mContext.resources.getColor(R.color.delivery_status_green)
                programFab4.colorRipple = mContext.resources.getColor(R.color.delivery_status_green)
                programFab4.labelText = getFloatingVal[3]
                floating_fab.addMenuButton(programFab4)
                programFab4.setOnClickListener(this)
            }
            if (i == 4) {
                programFab5 = FloatingActionButton(activity)
                programFab5.buttonSize = FloatingActionButton.SIZE_MINI
                programFab5.id = preid + i
                programFab5.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab5.colorPressed = mContext.resources.getColor(R.color.delivery_status_green)
                programFab5.colorRipple = mContext.resources.getColor(R.color.delivery_status_green)
                programFab5.labelText = getFloatingVal[4]
                floating_fab.addMenuButton(programFab5)
                programFab5.setOnClickListener(this)
            }
            if (i == 5) {
                programFab6 = FloatingActionButton(activity)
                programFab6.buttonSize = FloatingActionButton.SIZE_MINI
                programFab6.id = preid + i
                programFab6.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                programFab6.colorPressed = mContext.resources.getColor(R.color.delivery_status_green)
                programFab6.colorRipple = mContext.resources.getColor(R.color.delivery_status_green)
                programFab6.labelText = getFloatingVal[5]
                floating_fab.addMenuButton(programFab6)
                programFab6.setOnClickListener(this)
            }
            if (i == 0) {
                programFab1.setImageResource(R.drawable.ic_tick_float_icon)
                programFab1.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
            } else if (i == 1) {
                programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
            } else if(i==2) {
                programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)
            }else if(i==3){
                programFab4.setImageResource(R.drawable.ic_tick_float_icon_gray)
            }else if(i==4){
                programFab5.setImageResource(R.drawable.ic_tick_float_icon_gray)
            }else if(i==5){
                programFab6.setImageResource(R.drawable.ic_tick_float_icon_gray)
            }
        }

        mFab.setOnClickListener(this)
        ivContactSync.setOnClickListener(this)
        iv_frag_APICheckTest.setOnClickListener(this)
        iv_click_scheduler.setOnClickListener(this)
        ivContactSyncDel.setOnClickListener(this)
        iv_search.setOnClickListener(this)
        iv_mic.setOnClickListener(this)
        tv_syncAll.setOnClickListener(this)
        mFab.setCustomClickListener {
            if (!Pref.isAddAttendence) {
                (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
            }else{
                try {
                    floating_fab.close(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                (mContext as DashboardActivity).loadFragment(FragType.ContactsAddFrag, true, "")
            }
        }
        initPermissionCheckOne()
        if(AppUtils.isOnline(mContext)){
            singleLocation()
        }else{
            locationStr = LocationWizard.getNewLocationName(mContext, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())
            location_pinStr = LocationWizard.getPostalCode(mContext, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())
            locationStr_lat =Pref.current_latitude.toDouble().toString()
            locationStr_long =Pref.current_longitude.toDouble().toString()
            if(location_pinStr.equals("")){
                location_pinStr = Pref.current_pincode
            }
            progress_wheel.stopSpinning()
        }
        shopContactList("")

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().length == 0){
                    shopContactList("")
                }
            }
        })

        simpleDialogProcess = Dialog(mContext)
        simpleDialogProcess.setCancelable(false)
        simpleDialogProcess.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialogProcess.setContentView(R.layout.dialog_message)
        dialogHeaderProcess = simpleDialogProcess.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        dialog_yes_no_headerTVProcess = simpleDialogProcess.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_tv_message_ok = simpleDialogProcess.findViewById(R.id.tv_message_ok) as AppCustomTextView
        var ll_dialog_msg_progress_root = simpleDialogProcess.findViewById(R.id.ll_dialog_msg_progress_root) as LinearLayout
        dialog_pg = simpleDialogProcess.findViewById(R.id.pw_dialog_msg_progress) as ProgressWheel
        ll_dialog_msg_progress_root.visibility = View.VISIBLE

        dialog_tv_message_ok.visibility = View.GONE
        dialog_yes_no_headerTVProcess.text = AppUtils.hiFirstNameText()
        dialogHeaderProcess.text = "Please wait while fetching contacts ........."

        if(Pref.IsCRMPhonebookSyncEnable){
            ivContactSync.visibility=View.VISIBLE
            viewPhoneBook.visibility=View.VISIBLE
        }else{
            ivContactSync.visibility=View.GONE
            viewPhoneBook.visibility=View.GONE
        }
        if(Pref.IsCRMSchedulerEnable){
            iv_click_scheduler.visibility=View.VISIBLE
            viewScheduler.visibility=View.VISIBLE
        }else{
            iv_click_scheduler.visibility=View.GONE
            viewScheduler.visibility=View.GONE
        }
        if(Pref.IsCRMAddEnable){
            mFab.visibility = View.VISIBLE
            tv_empty_page_msg.visibility = View.VISIBLE
            img_direction.visibility = View.VISIBLE
        }else{
            mFab.visibility = View.GONE
            tv_empty_page_msg.visibility = View.GONE
            img_direction.visibility = View.GONE
        }
    }

    fun initPermissionCheckOne() {
        var permissionList = arrayOf<String>( Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CONTACTS)
        permissionUtils = PermissionUtils(mContext as Activity, object : PermissionUtils.OnPermissionListener {
            @TargetApi(Build.VERSION_CODES.M)
            override fun onPermissionGranted() {

            }
            override fun onPermissionNotGranted() {

            }
        },permissionList)
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            tv_syncAll.id->{
                if (!Pref.isAddAttendence){
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                }
                else{
                    if(AppUtils.isOnline(mContext)){
                        tv_syncAll.isEnabled=false
                        println("tag_sync_all_click call")
                        syncCompanyMaster("")
                        Handler().postDelayed(Runnable {
                            //syncShopAll()
                            callUserConfigApi()
                        }, 1900)
                    }else{
                        tv_syncAll.isEnabled=true
                        Toaster.msgShort(mContext,"Please connect to internet.")
                    }
                }
            }
            R.id.iv_frag_contacts_dialog_del->{
                AppDatabase.getDBInstance()!!.addShopEntryDao().del99()
                shopContactList("")
            }
            R.id.iv_click_scheduler->{
                if (!Pref.isAddAttendence){
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                }
                else{
                    (mContext as DashboardActivity).loadFragment(FragType.SchedulerViewFrag, true, "")
                }

/*                if (Pref.storeGmailId==null && Pref.storeGmailPassword==null){
                    instructionDialog = Dialog(mContext)
                    instructionDialog!!.setCancelable(false)
                    instructionDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    instructionDialog!!.setContentView(R.layout.dialog_gmail_instruction)
                    val tvHeader = instructionDialog!!.findViewById(R.id.dialog_contact_gr_header) as TextView
                    val tv_instruction = instructionDialog!!.findViewById(R.id.tv_instruction) as TextView
                    val tv_save_instruction = instructionDialog!!.findViewById(R.id.tv_save_instruction) as TextView
                    val et_user_gmail_id = instructionDialog!!.findViewById(R.id.et_user_gmail_id) as EditText
                    val et_user_password = instructionDialog!!.findViewById(R.id.et_user_password) as EditText
                    val tv_headerOfSetVerification = instructionDialog!!.findViewById(R.id.tv_headerOfSetVerification) as TextView
                    val rvContactGrName = instructionDialog!!.findViewById(R.id.rv_dialog_cont_gr) as RecyclerView
                    val iv_close = instructionDialog!!.findViewById(R.id.iv_dialog_instruction_close_icon) as ImageView

                    tv_save_instruction.setOnClickListener {
                        if (et_user_gmail_id.text.toString().equals("") && et_user_password.text.toString().trim().equals("")) {
                            Toast.makeText(
                                mContext,
                                "Put your credentials",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else if (!et_user_gmail_id.text.equals(".gmail") || et_user_password.text.toString().trim().length < 16) {
                                Toast.makeText(
                                    mContext,
                                    "Put your credentials correctly",
                                    Toast.LENGTH_LONG
                                ).show()
                        }
                        else{
                            Pref.storeGmailId = et_user_gmail_id.text.toString().trim()
                            Pref.storeGmailPassword = et_user_gmail_id.text.toString().trim()
                            // After save 2 step verification
                            (mContext as DashboardActivity).loadFragment(FragType.SchedulerViewFrag, true, "")
                            instructionDialog!!.dismiss()

                        }
                    }
                    iv_close.setOnClickListener {
                        instructionDialog!!.dismiss()
                    }
                    rvContactGrName.visibility=View.GONE
                    tvHeader.text = "Read Instruction"
                    instructionDialog!!.show()
                }
                else{
                    (mContext as DashboardActivity).loadFragment(FragType.SchedulerViewFrag, true, "")
                }*/

            }
            R.id.iv_frag_contacts_dialog -> {
                if (!Pref.isAddAttendence){
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                }
                else{
                    contGrDialog = Dialog(mContext)
                    contGrDialog!!.setCancelable(true)
                    contGrDialog!!.setCanceledOnTouchOutside(false)
                    contGrDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    contGrDialog!!.setContentView(R.layout.dialog_contact_gr)
                    val tvHeader = contGrDialog!!.findViewById(R.id.dialog_contact_gr_header) as TextView
                    val rvContactGrName = contGrDialog!!.findViewById(R.id.rv_dialog_cont_gr) as RecyclerView
                    val iv_close = contGrDialog!!.findViewById(R.id.iv_dialog_generic_list_close_icon) as ImageView

                    tvHeader.text = "Select contact group"
                    contGrDialog!!.show()

                    doAsync {
                        progress_wheel.spin()
                        var grNameL = AppUtils.getPhoneBookGroups(mContext) as ArrayList<ContactGr>
                        uiThread {
                            progress_wheel.stopSpinning()
                            if(grNameL.size>0){
                                adapterContGr = AdapterContactGr(mContext,grNameL,object :AdapterContactGr.onClick
                                {
                                    override fun onGrClick(obj: ContactGr) {
                                        // contGrDialog.dismiss()
                                        showContactNameL(obj)
                                    }
                                })
                                rvContactGrName.adapter = adapterContGr
                            }
                        }
                    }
                    iv_close.setOnClickListener {
                        contGrDialog!!.dismiss()
                        progress_wheel.stopSpinning()
                    }
                }
            }
            R.id.iv_frag_APICheckTest -> {

                getwhatsappTemplateL()
                return

                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("messaging_product", "whatsapp")
                    jsonObject.put("to", "918017845376")
                    jsonObject.put("type", "template")

                    val templateObject = JSONObject()
                    templateObject.put("name", "hello_world")

                    val languageObject = JSONObject()
                    languageObject.put("code", "en_US")

                    templateObject.put("language", languageObject)

                    jsonObject.put("template", templateObject)
                    postGraphAPICall(jsonObject)

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            R.id.iv_frag_contacts_search->{
                shopContactList(et_search.text.toString())
            }
            R.id.iv_frag_contacts_mic->{
                progress_wheel.spin()
                val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                Handler().postDelayed(Runnable {
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi")
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?")
                }, 1000)
                try {
                    startActivityForResult(intent, 7009)
                    Handler().postDelayed(Runnable {
                        progress_wheel.stopSpinning()
                    }, 3000)

                } catch (a: ActivityNotFoundException) {
                    a.printStackTrace()
                }
            }
            100->{
                str_filterEntity=""
                str_filterText = ""
                shopContactList("")
                try {
                    floating_fab.close(true)
                    programFab1.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
                    programFab2.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab3.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab4.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab5.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab6.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab1.setImageResource(R.drawable.ic_tick_float_icon)
                    programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab4.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab5.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab6.setImageResource(R.drawable.ic_tick_float_icon_gray)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            101 -> {
                str_filterEntity=""
                str_filterText = "Filtered on : Added Date"
                shopContactList("","addedDate")
                try {
                    floating_fab.close(true)
                    programFab1.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab2.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
                    programFab3.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab4.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab5.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab6.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab1.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab2.setImageResource(R.drawable.ic_tick_float_icon)
                    programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab4.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab5.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab6.setImageResource(R.drawable.ic_tick_float_icon_gray)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            102 -> {
                getCRMStatusList()
                try {
                    floating_fab.close(true)
                    programFab1.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab2.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab3.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
                    programFab4.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab5.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab6.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab1.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab3.setImageResource(R.drawable.ic_tick_float_icon)
                    programFab4.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab5.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab6.setImageResource(R.drawable.ic_tick_float_icon_gray)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            103 -> {
                getCRMStageList()
                try {
                    floating_fab.close(true)
                    programFab1.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab2.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab3.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab4.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
                    programFab5.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab6.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab1.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab4.setImageResource(R.drawable.ic_tick_float_icon)
                    programFab5.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab6.setImageResource(R.drawable.ic_tick_float_icon_gray)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            104 -> {
                getCRMTypeList()
                try {
                    floating_fab.close(true)
                    programFab1.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab2.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab3.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab4.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab5.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
                    programFab6.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab1.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab4.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab5.setImageResource(R.drawable.ic_tick_float_icon)
                    programFab6.setImageResource(R.drawable.ic_tick_float_icon_gray)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            105 -> {
                getCRMSourceList()
                try {
                    floating_fab.close(true)
                    programFab1.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab2.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab3.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab4.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab5.colorNormal = mContext.resources.getColor(R.color.colorAccent)
                    programFab6.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
                    programFab1.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab2.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab3.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab4.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab5.setImageResource(R.drawable.ic_tick_float_icon_gray)
                    programFab6.setImageResource(R.drawable.ic_tick_float_icon)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun postGraphAPICall(jsonObject: JSONObject) {

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest("https://graph.facebook.com/v18.0/109092662037205/messages", jsonObject,
            object : Response.Listener<JSONObject?> {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(mContext, ""+response, Toast.LENGTH_LONG).show()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(mContext, ""+error.toString(), Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer"+" "+"EAAYdZB0nzeMgBO87GPmP7b28lokaaguZBYuwwbZAKzfyFP6jWsIj6sE62APVlBsvindVqeAKeAjj5Sirl7KgZCleGKB5ZCKjfGPUTQ5KpQMQG2TWEnPd326JQbxAD8HGubvyhxb6OKwMRCSdqHT5KJAvjwt7YShZBZAMs8i2lQoCffvSrUuhuT3nxs3IXwneBJ8cpkBeRBp6t6ou7TDY7dSZCTjL47EZD"
                params["Content-Type"] = "application/json"
                return params
            }
        }
        MySingleton.getInstance(mContext)!!.addToRequestQueue(jsonObjectRequest)
    }

    fun syncShopAll(){
        tv_syncAll.isEnabled=true
        //var allUnSyncContact = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcUnsyncList(false) as ArrayList<AddShopDBModelEntity>
        var allUnSyncContact = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcUnsyncListTopX(false,5) as ArrayList<AddShopDBModelEntity>
        if(allUnSyncContact.size>0){
                for(i in 0..allUnSyncContact.size-1){
                    var obj = allUnSyncContact.get(i)
                    var compID = "0"
                    if(!obj.companyName.equals("")){
                        compID = AppDatabase.getDBInstance()!!.companyMasterDao().getInfoByName(obj.companyName).company_id.toString()
                    }
                    obj.companyName_id = compID
                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateCompanyID(obj.companyName_id,obj.shop_id)
                    if(i==allUnSyncContact.size-1){
                        Handler().postDelayed(Runnable {
                            syncContact(obj,true,true)
                        }, 1100)
                    }else{
                        Handler().postDelayed(Runnable {
                            syncContact(obj,true,false)
                        }, 1100)
                    }
                }
        }
        else{
            //Toaster.msgShort(mContext,"No unsync data found. Thanks.")
            checkModifiedShop()
            progress_wheel.stopSpinning()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 7009){
            try {
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                et_search.setText(result!![0].toString())
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }

    private fun singleLocation() {
        try{
            //progress_wheel.spin()
            SingleShotLocationProvider.requestSingleUpdate(mContext,
                object : SingleShotLocationProvider.LocationCallback {
                    override fun onStatusChanged(status: String) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onProviderEnabled(status: String) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onProviderDisabled(status: String) {

                    }

                    override fun onNewLocationAvailable(location: Location) {
                        if(location!=null){
                            locationStr = LocationWizard.getNewLocationName(mContext, location.latitude, location.longitude)
                            location_pinStr = LocationWizard.getPostalCode(mContext, location.latitude, location.longitude)
                            locationStr_lat =location.latitude.toString()
                            locationStr_long =location.longitude.toString()
                        } else{
                            var lloc: Location = Location("")
                            lloc.latitude=Pref.current_latitude.toDouble()
                            lloc.longitude=Pref.current_longitude.toDouble()
                            locationStr_lat =lloc.latitude.toString()
                            locationStr_long =lloc.longitude.toString()
                            locationStr = LocationWizard.getNewLocationName(mContext, lloc.latitude, lloc.longitude)
                            location_pinStr = LocationWizard.getPostalCode(mContext, lloc.latitude, lloc.longitude)
                        }
                        //progress_wheel.stopSpinning()
                    }

                })
        }catch (ex:Exception){
            ex.printStackTrace()
            //progress_wheel.stopSpinning()
        }
    }

    fun showContactNameL(obj:ContactGr){

        doAsync {

            /*try {

                var g = AppUtils.getContactByGrNw(obj.gr_id,obj.gr_name,mContext)

                println("tag_cont new process selected gr ${obj.gr_name} start")
                var mob : MobileContact = MobileContact("",MobileContact.Type.PHONE,mContext)
                var gg = mob.getAllContactsNew(obj.gr_id)
                var finalCont = gg.filter { it.type == MobileContact.Type.PHONE }
                for(l in 0..gg.size-1){
                    println("tag_new_conta_fetch index : $l ${gg.get(l).name} ${gg.get(l).contact}")
                }
                println("tag_cont new process selected gr ${obj.gr_name} ${gg.size} end")
            }catch (ex:Exception){
                ex.printStackTrace()
            }*/

            //
            var contactL : ArrayList<ContactDtls> = ArrayList()
            try{
                (mContext!! as Activity).runOnUiThread {
                    simpleDialogProcess.show()
                    dialog_pg.spin()
                }
                println("tag_cont selected gr ${obj.gr_name} start")
                //contactL = AppUtils.getContactsFormGroup(obj.gr_id,obj.gr_name,mContext)
                //contactL = AppUtils.getContactByGrNw(obj.gr_id,obj.gr_name,mContext)
                contactL = AppUtils.getContactByGrNwWithAddr(obj.gr_id,obj.gr_name,mContext)
                println("tag_cont selected gr ${obj.gr_name} ${contactL.size} end")
            }catch (ex:Exception){
                ex.printStackTrace()
                (mContext as DashboardActivity).showSnackMessage("Something went wrong.")
                simpleDialogProcess.dismiss()
            }

            uiThread {
                //progress_wheel.stopSpinning()
                if(contactL.size>0){
                    var myShopContactL = AppDatabase.getDBInstance()!!.addShopEntryDao().getAllOwnerContact() as ArrayList<String>
                    var contactLFinal = contactL.clone() as  ArrayList<ContactDtls>
                    for (i in 0..contactL.size-1){
                        if(myShopContactL.contains(contactL.get(i).number)){
                            contactLFinal.remove(contactL.get(i))
                        }
                    }
                    contactL = contactLFinal
                }

                Handler().postDelayed(Runnable {
                    simpleDialogProcess.dismiss()
                }, 500)

                if(contactL.size>0){
                    var contactLTemp : ArrayList<ContactDtls> = contactL.clone() as ArrayList<ContactDtls>
                    //contactLTemp.addAll(contactL)

                    val contactDialog = Dialog(mContext)
                    contactDialog.setCancelable(true)
                    contactDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    contactDialog.setContentView(R.layout.dialog_cont_select)
                    val rvContactL = contactDialog.findViewById(R.id.rv_dialog_cont_list) as RecyclerView
                    val tvHeader = contactDialog.findViewById(R.id.tv_dialog_cont_sel_header) as TextView
                    val submit = contactDialog.findViewById(R.id.tv_dialog_cont_list_submit) as TextView
                    val et_contactNameSearch = contactDialog.findViewById(R.id.et_dialog_contact_search) as AppCustomEditText
                    val cb_selectAll = contactDialog.findViewById(R.id.cb_dialog_cont_select_all) as CheckBox
                    val iv_close = contactDialog.findViewById(R.id.iv_dialog_generic_list_close_icon) as ImageView
                    tvHeader.text = "Select Contact(s)"


                    iv_close.setOnClickListener {
                        contactDialog.dismiss()
                        progress_wheel.stopSpinning()
                    }

                    for(i in 0..contactL.size-1){
                        var ob = contactL.get(i)
                        var  isPresentInDb= (AppDatabase.getDBInstance()!!.addShopEntryDao().getCustomData(ob.name,ob.number) as ArrayList<AddShopDBModelEntity>).size
                        if(isPresentInDb!=0){
                            contactLTemp.remove(ob)
                        }
                    }

                    if(contactLTemp.size>0){
                        var finalL : ArrayList<ContactDtls> = ArrayList()
                        try {
                            if(contactLTemp.size>2){
                                finalL = contactLTemp.sortedByDescending { it.name }.reversed() as ArrayList<ContactDtls>
                            }else{
                                finalL = contactLTemp.clone() as ArrayList<ContactDtls>
                            }
                        }catch (ex:Exception){
                            ex.printStackTrace()
                        }
                        var contactTickL : ArrayList<ContactDtls> = ArrayList()
                        //rvContactL.layoutManager = LinearLayoutManager(mContext)
                        tvHeader.text = "Select Contact(s): ${finalL.size}"
                        adapterContName = AdapterContactName(mContext,finalL,object :AdapterContactName.onClick{
                            override fun onTickUntick(obj: ContactDtls, isTick: Boolean) {
                                if(isTick){
                                    contactTickL.add(obj)
                                    finalL.filter { it.name.equals(obj.name) }.first().isTick = true
                                    tvHeader.text = "Selected Contact(s) : (${contactTickL.size})"
                                }else{
                                    contactTickL.remove(obj)
                                    finalL.filter { it.name.equals(obj.name) }.first().isTick = false
                                    tvHeader.text = "Selected Contact(s) : (${contactTickL.size})"

                                }
                            }
                        },{
                            it
                        })
                        /*   cb_selectAll.setOnClickListener {
                               adapterContName.selectAll()
                           }*/

                        cb_selectAll.setOnCheckedChangeListener { compoundButton, b ->

                            if (compoundButton.isChecked) {
                                adapterContName.selectAll()
                                cb_selectAll.setText("Deselect All")
                            }
                            else{

                                adapterContName.deselectAll()
                                cb_selectAll.setText("Select All")

                            }
                        }

                        et_contactNameSearch.addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(p0: Editable?) {
                            }

                            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            }

                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                adapterContName!!.getFilter().filter(et_contactNameSearch.text.toString().trim())
                            }
                        })

                        submit.setOnClickListener {
                            if(contactTickL.size>0){
                                contactDialog.dismiss()
                                contGrDialog!!.dismiss()
                                submitContact(contactTickL)
                            }else{
                                contactDialog.dismiss()
                                contGrDialog!!.dismiss()
                                Toaster.msgShort(mContext,"Select Contact(s)")
                            }
                        }
                        rvContactL.adapter = adapterContName
                        contactDialog.show()
                        //contGrDialog!!.dismiss()

                    }else{
                        Toaster.msgShort(mContext,"No CRM avaliable")
                    }
                }else{
                    Handler().postDelayed(Runnable {
                        val simpleDialog = Dialog(mContext)
                        simpleDialog.setCancelable(false)
                        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        simpleDialog.setContentView(R.layout.dialog_ok)

                        try {
                            simpleDialog.setCancelable(true)
                            simpleDialog.setCanceledOnTouchOutside(false)
                            val dialogName = simpleDialog.findViewById(R.id.tv_dialog_ok_name) as AppCustomTextView
                            val dialogCross = simpleDialog.findViewById(R.id.tv_dialog_ok_cancel) as ImageView
                            dialogName.text = AppUtils.hiFirstNameText()
                            dialogCross.setOnClickListener {
                                simpleDialog.cancel()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
                        dialogHeader.text = "No CRM is pending for sync!"
                        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
                        dialogYes.setOnClickListener({ view ->
                            simpleDialog.cancel()
                        })
                        simpleDialog.show()
                    }, 500)
                }
            }
        }
    }


    fun submitContact(contactTickL : ArrayList<ContactDtls>){
        doAsync {
            progress_wheel.spin()
            for(i in 0..contactTickL.size-1){
                var shopObj:AddShopDBModelEntity = AddShopDBModelEntity()
                val random = Random()
                shopObj.shop_id = Pref.user_id + "_" + System.currentTimeMillis().toString() +  (random.nextInt(999 - 100) + 100).toString()
                shopObj.shopName = contactTickL.get(i).name
                shopObj.ownerName = contactTickL.get(i).name
                shopObj.crm_firstName =  contactTickL.get(i).name
                shopObj.crm_lastName = ""
                shopObj.companyName_id = ""
                shopObj.companyName = ""
                shopObj.jobTitle = ""
                shopObj.ownerEmailId = ""
                shopObj.ownerContactNumber = if(contactTickL.get(i).number!!.contains("+91")) contactTickL.get(i).number!!.replace("+","").removeRange(0,2) else contactTickL.get(i).number!!//contactTickL.get(i).number
                if(locationStr.equals("") || location_pinStr.equals("") || locationStr_lat.equals("") || locationStr_long.equals("")){
                    locationStr = LocationWizard.getNewLocationName(mContext, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())
                    location_pinStr = LocationWizard.getPostalCode(mContext, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())
                    locationStr_lat =Pref.current_latitude.toDouble().toString()
                    locationStr_long =Pref.current_longitude.toDouble().toString()
                    if(location_pinStr.equals("")){
                        location_pinStr = Pref.current_pincode
                    }
                }
                try {
                    if(contactTickL.get(i).addr.equals("")){
                            shopObj.address = "NA"
                        }else{
                            shopObj.address = contactTickL.get(i).addr
                        }
                        shopObj.pinCode = "0"
                        shopObj.shopLat = 0.0
                        shopObj.shopLong = 0.0

                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                shopObj.crm_assignTo = Pref.user_name.toString()
                shopObj.crm_assignTo_ID = Pref.user_id
                shopObj.crm_type = ""
                shopObj.crm_type_ID = ""
                shopObj.crm_status=""
                shopObj.crm_status_ID=""
                shopObj.crm_source=""
                shopObj.crm_source_ID=""
                shopObj.crm_reference=""
                shopObj.crm_reference_ID=""
                shopObj.crm_reference_ID_type=""
                shopObj.remarks = ""
                shopObj.amount = ""
                shopObj.crm_stage=""
                shopObj.crm_stage_ID=""
                shopObj.crm_reference = ""
                shopObj.crm_reference_ID = ""
                shopObj.type = "99"
                shopObj.added_date = AppUtils.getCurrentDateTime()
                shopObj.crm_saved_from = "Phone Book"
                shopObj.isUploaded = false
                shopObj.totalVisitCount = "1"
                shopObj.lastVisitedDate = AppUtils.getCurrentDateChanged()

              /*  var isWhatsapp = AppUtils.checkContactWhatsapp(mContext,contactTickL.get(i).contact_id,shopObj.ownerName,shopObj.ownerContactNumber)
                AppUtils.checkContactWhatsappAll(mContext,contactTickL.get(i).contact_id,shopObj.ownerName,shopObj.ownerContactNumber,contactTickL.get(i).contact_id)
                if(isWhatsapp){
                    shopObj.whatsappNoForCustomer =  if(shopObj.ownerContactNumber.contains("+91")) shopObj.ownerContactNumber.replace("+","").removeRange(0,2) else shopObj.ownerContactNumber
                }*/

                AppDatabase.getDBInstance()!!.addShopEntryDao().insert(shopObj)

                //shop activity work begin
                val shopActivityEntity = ShopActivityEntity()
                shopActivityEntity.shopid = shopObj.shop_id
                shopActivityEntity.shop_name = shopObj.ownerName
                shopActivityEntity.shop_address = shopObj.address
                shopActivityEntity.date = AppUtils.getCurrentDateForShopActi()
                shopActivityEntity.duration_spent = "00:00:00"
                shopActivityEntity.visited_date = AppUtils.getCurrentISODateTime()
                shopActivityEntity.isUploaded = false
                shopActivityEntity.isVisited = true
                shopActivityEntity.isDurationCalculated = true
                shopActivityEntity.startTimeStamp = System.currentTimeMillis().toString()
                shopActivityEntity.next_visit_date = ""
                shopActivityEntity.distance_travelled = "0"

                val todaysVisitedShop = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(AppUtils.getCurrentDateForShopActi())
                if (todaysVisitedShop == null || todaysVisitedShop.isEmpty()) {
                    shopActivityEntity.isFirstShopVisited = true

                    if (!TextUtils.isEmpty(Pref.home_latitude) && !TextUtils.isEmpty(Pref.home_longitude)) {
                        shopActivityEntity.distance_from_home_loc = "0.0"
                    } else
                        shopActivityEntity.distance_from_home_loc = "0.0"
                } else {
                    shopActivityEntity.isFirstShopVisited = false
                    shopActivityEntity.distance_from_home_loc = ""
                }
                shopActivityEntity.in_time = AppUtils.getCurrentTimeWithMeredian()
                shopActivityEntity.in_loc = shopObj.actual_address
                shopActivityEntity.agency_name = shopObj.ownerName
                shopActivityEntity.updated_by=Pref.user_id
                shopActivityEntity.updated_on= AppUtils.getCurrentDateForShopActi()
                shopActivityEntity.shop_revisit_uniqKey = ""
                shopActivityEntity.pros_id = ""
                shopActivityEntity.agency_name = ""
                shopActivityEntity.isnewShop = true

                AppDatabase.getDBInstance()!!.shopActivityDao().insertAll(shopActivityEntity)
                //shop activity work end
            }
            uiThread {
                Handler().postDelayed(Runnable {
                    progress_wheel.stopSpinning()

                    if (AppUtils.isOnline(mContext)){
                        syncShopAll()
                    }else{
                        voiceMsg("Contact added successfully")
                        Toaster.msgShort(mContext,"Contact added successfully.")
                        shopContactList("")
                    }
                }, 1500)
            }
        }
    }

    fun shopContactList(searchObj:String,sortBy:String="",crm_status_ID:String="",crm_stage_ID:String="",crm_type_ID:String="",crm_source_ID:String=""){
        doAsync {
            progress_wheel.spin()
            var contL : ArrayList<AddShopDBModelEntity> = ArrayList()
            contL = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcShops() as ArrayList<AddShopDBModelEntity>

            if(sortBy.equals("addedDate")){
                contL = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcShopsByAddedDate() as ArrayList<AddShopDBModelEntity>
            }
            if(!crm_status_ID.equals("")){
                contL = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcShopsBycrm_status_ID(crm_status_ID) as ArrayList<AddShopDBModelEntity>
            }
            if(!crm_stage_ID.equals("")){
                contL = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcShopsBycrm_stage_ID(crm_stage_ID) as ArrayList<AddShopDBModelEntity>
            }
            if(!crm_type_ID.equals("")){
                contL = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcShopsBycrm_type_ID(crm_type_ID) as ArrayList<AddShopDBModelEntity>
            }
            if(!crm_source_ID.equals("")){
                contL = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcShopsBycrm_source_ID(crm_source_ID) as ArrayList<AddShopDBModelEntity>
            }

            if(!searchObj.equals("")){
                /*var searchL = contL.filter { it.shopName.contains(searchObj, ignoreCase = true) || it.ownerContactNumber.contains(searchObj, ignoreCase = true) || it.ownerEmailId.contains(searchObj, ignoreCase = true) ||
                        it.crm_stage.contains(searchObj, ignoreCase = true) || it.crm_source.contains(searchObj, ignoreCase = true) ||
                        it.crm_status.contains(searchObj, ignoreCase = true) || it.crm_type.contains(searchObj, ignoreCase = true) || it.crm_saved_from.contains(searchObj, ignoreCase = true)} as ArrayList<AddShopDBModelEntity>
                */
                try {
                    contL = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcShopsFilter(searchObj) as ArrayList<AddShopDBModelEntity>
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //contL = searchL
            }
            uiThread {
                progress_wheel.stopSpinning()
                if(contL.size>0){
                    tv_filteredOnText.visibility = View.VISIBLE
                    tv_filteredOnText.text = str_filterText
                    if(str_filterText.equals("")){
                        tv_filteredOnText.visibility = View.GONE
                    }
                    for(i in 0..contL.size-1){
                        println("tag_conta_show ${contL.get(i).ownerName} ${contL.get(i).isUploaded}")
                    }
                    tvNodata.visibility = View.GONE
                    (mContext as DashboardActivity).setTopBarTitle("CRM : ${contL.size}")
                    adapterContactList = AdapterContactList(mContext,contL,et_search.text.toString(),object :AdapterContactList.onClick{
                        override fun onCallClick(obj: AddShopDBModelEntity) {
                            IntentActionable.initiatePhoneCall(mContext, obj.ownerContactNumber)
                        }

                        override fun onWhatsClick(obj: AddShopDBModelEntity) {
                            /*val url = "https://api.whatsapp.com/send?phone=+91${obj.ownerContactNumber}&text='Hello User'"
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
                            }*/

                            //Code start by Puja
                            //val url = "https://api.whatsapp.com/send?phone=+91${obj.ownerContactNumber}"

                            if (obj.whatsappNoForCustomer .isNullOrEmpty() ) {
                                val packageName = "com.whatsapp"
                                val packageManager = mContext!!.packageManager

                                val intent = packageManager.getLaunchIntentForPackage(packageName)
                                intent?.let {
                                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    mContext!!.startActivity(it)
                                } ?: run {
                                    // If WhatsApp is not installed, you can redirect the user to the Play Store
                                    Toast.makeText(mContext, "WhatsApp is not installed", Toast.LENGTH_LONG).show()
                                    /*val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                                    mContext!!.startActivity(playStoreIntent)*/
                                }

                            }else{
                                val url =
                                    "https://api.whatsapp.com/send?phone=+91${obj.whatsappNoForCustomer}"
                                try {
                                    val pm = mContext.packageManager
                                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                                    val i = Intent(Intent.ACTION_VIEW)
                                    i.data = Uri.parse(url)
                                    startActivity(i)
                                } catch (e: PackageManager.NameNotFoundException) {
                                    e.printStackTrace()
                                    Toast.makeText(mContext, "WhatsApp is not installed", Toast.LENGTH_LONG).show()
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                    Toast.makeText(mContext, "This is not whatsApp no.", Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                        override fun onEmailClick(obj: AddShopDBModelEntity) {
                            //IntentActionable.sendMail(mContext, obj.ownerEmailId, "")

                            try {
                                val intent = Intent(Intent.ACTION_SENDTO)
                                intent.setData(Uri.parse("mailto:")) // only email apps should handle this
                                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("${obj.ownerEmailId}"))
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Hi ${obj.ownerName}")
                                intent.putExtra(Intent.EXTRA_TEXT, "Welcome")
                                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                                    startActivity(intent)
                                }
                            } catch (e: Exception) {
                                TODO("Not yet implemented")
                            }
                        }

                        override fun onInfoClick(obj: AddShopDBModelEntity) {
                            saveCallHisToDB(obj)
                        }

                        override fun onSyncUnsyncClick(obj: AddShopDBModelEntity) {
                            if(AppUtils.isOnline(mContext)){
                                if(!obj.companyName.equals("")){
                                    if(AppDatabase.getDBInstance()!!.companyMasterDao().getInfoByName(obj.companyName).isUploaded == false){
                                        syncCompanyMaster(obj.companyName)
                                    }
                                }
                                Handler().postDelayed(Runnable {
                                    var compID = "0"
                                    if(!obj.companyName.equals("")){
                                        compID = AppDatabase.getDBInstance()!!.companyMasterDao().getInfoByName(obj.companyName).company_id.toString()
                                    }
                                    obj.companyName_id = compID
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateCompanyID(obj.companyName_id,obj.shop_id)
                                    syncContact(obj)
                                }, 1900)
                            }else{
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                            }
                        }
                        override fun onEditClick(obj: AddShopDBModelEntity) {
                            try {
                                floating_fab.close(true)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            (mContext as DashboardActivity).loadFragment(FragType.ContactsAddFrag, true, obj.shop_id)
                        }
                        override fun onUpdateStatusClick(obj: AddShopDBModelEntity) {
                            UpdateShopStatusDialog.getInstance(obj.shopName!!, "Cancel", "Confirm", true,"",obj.user_id.toString()!!,"Select Contact Status",
                                object : UpdateShopStatusDialog.OnDSButtonClickListener {
                                    override fun onLeftClick() {

                                    }
                                    override fun onRightClick(status: String) {
                                        if(!status.equals("")){
                                            if(status.equals("Inactive")){
                                                AppDatabase.getDBInstance()!!.addShopEntryDao().updateShopStatus(obj.shop_id,"0")
                                                AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsEditUploaded(0,obj.shop_id)
                                                if(AppUtils.isOnline(mContext)) {
                                                    convertToReqAndApiCallForShopStatus(AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(obj.shop_id!!))
                                                } else{
                                                    shopContactList("")
                                                    (mContext as DashboardActivity).showSnackMessage("Status updated successfully")
                                                }
                                            }
                                            if(status.equals("Active")){
                                                shopContactList("")
                                                AppDatabase.getDBInstance()!!.addShopEntryDao().updateShopStatus(obj.shop_id,"1")
                                            }
                                        }
                                    }
                                }).show((mContext as DashboardActivity).supportFragmentManager, "")
                        }

                        override fun onDtlsShareClick(obj: AddShopDBModelEntity) {
                            generateContactDtlsPdf(obj)
                        }

                        override fun onAutoActivityClick(obj: AddShopDBModelEntity) {

                            selectedShopIdForActivity = obj.shop_id

                            val datePicker = DatePickerDialog(mContext, R.style.DatePickerTheme, dates, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH))
                            datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
                            datePicker.show()
                        }

                        override fun onDirectionClick(obj: AddShopDBModelEntity) {
                            try{
                                //val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=22.497013652788425,88.3154464620276&destination=22.462972465878618,88.3071007426955&waypoints=22.475403007798953,88.30885895679373|22.471053209879425,88.3098540562982&travelmode=driving")
                                //val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=22.497013652788425,88.3154464620276&destination=22.462972465878618,88.3071007426955&waypoints=22.475403007798953,88.30885895679373|22.471053209879425,88.3098540562982&travelmode=driving&dir_action=navigate")
                                //val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=22.497013652788425,88.3154464620276&destination=22.462972465878618,88.3071007426955&waypoints=22.475403007798953,88.30885895679373|22.471053209879425,88.3098540562982&mode=1&dir_action=navigate")
                                //var intentGmap: Intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

                                var intentGmap: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${obj.shopLat},${obj.shopLong}&mode=1"))
                                intentGmap.setPackage("com.google.android.apps.maps")
                                if(intentGmap.resolveActivity(mContext.packageManager) !=null){
                                    mContext.startActivity(intentGmap)
                                }
                            }catch (ex:Exception){
                                ex.printStackTrace()
                            }
                        }

                        override fun onActivityClick(obj: AddShopDBModelEntity) {

                            (mContext as DashboardActivity).loadFragment(FragType.ActivityDtlsFrag, true, obj.shop_id)

                            //(mContext as DashboardActivity).isFromMenu = false
                            //(mContext as DashboardActivity).loadFragment(FragType.AddActivityFragment, true, obj)
                        }

                        override fun onUpdateAddrClick(obj: AddShopDBModelEntity) {
                            if (AppUtils.mLocation != null) {
                                openAddressUpdateDialog(obj, AppUtils.mLocation!!)
                            } else {
                                Timber.d("=====Saved current location is null (Shop List)======")
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            }
                        }
                        override fun onOrderClick(obj: AddShopDBModelEntity) {
                            if(Pref.IsActivateNewOrderScreenwithSize){
                                (mContext as DashboardActivity).loadFragment(FragType.NewOrderScrOrderDetailsFragment, true, obj.shop_id)
                            }else{
                                CustomStatic.IsOrderLoadFromCRM = true
                                (mContext as DashboardActivity).loadFragment(FragType.ViewAllOrderListFragment, true, obj)
                            }
                        }

                        override fun onOpportunityClick(obj: AddShopDBModelEntity) {
                                (mContext as DashboardActivity).loadFragment(FragType.ViewCrmOpptFrag, true, obj.shop_id)

                        }

                        override fun onSourceUpdateClick(obj: AddShopDBModelEntity) {
                            updateSource(obj)
                        }
                        override fun onStageUpdateClick(obj: AddShopDBModelEntity) {
                            updateStage(obj)
                        }
                        override fun onStatusUpdateClick(obj: AddShopDBModelEntity) {
                            updateStatus(obj)
                        }
                        override fun onAssignToUpdateClick(obj: AddShopDBModelEntity) {
                            assignToUpdate(obj)
                        }

                    })
                    rvContactL.adapter = adapterContactList
                    rvContactL.visibility = View.VISIBLE
                    ll_no_data_root.visibility = View.GONE

                }
                else{
                    tv_filteredOnText.visibility = View.GONE
                    (mContext as DashboardActivity).setTopBarTitle("CRM")
                    //  tvNodata.visibility = View.VISIBLE
                    rvContactL.visibility = View.GONE
                    ll_no_data_root.visibility = View.VISIBLE
                    tv_empty_page_msg_head.text = "No CRM Found"
                    tv_empty_page_msg.text = "Click + to add your CRM"
                    img_direction.animate().rotationY(180F).start()

                    if(!str_filterEntity.equals("")){
                        tv_empty_page_msg_head.text = "No CRM Found for $str_filterEntity($str_filterEntityValue)"
                    }

                }
            }
        }


    }





    private fun openAddressUpdateDialog(addShopModelEntity: AddShopDBModelEntity, location: Location) {
        try {
            UpdateShopAddressDialog.getInstance(addShopModelEntity.shop_id, location, object :
                ShopAddressUpdateListener {
                override fun onAddedDataSuccess() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun getDialogInstance(mdialog: Dialog?) {
                }

                override fun onUpdateClick(address: AddShopDBModelEntity?) {
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
            e.printStackTrace()
        }
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
            addShopReqData.dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.dateOfBirth)

        if (!TextUtils.isEmpty(addShopData.dateOfAniversary))
            addShopReqData.date_aniversary =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.dateOfAniversary)

        addShopReqData.director_name = addShopData.director_name
        addShopReqData.key_person_name = addShopData.person_name
        addShopReqData.phone_no = addShopData.person_no

        if (!TextUtils.isEmpty(addShopData.family_member_dob))
            addShopReqData.family_member_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.family_member_dob)

        if (!TextUtils.isEmpty(addShopData.add_dob))
            addShopReqData.addtional_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.add_dob)

        if (!TextUtils.isEmpty(addShopData.add_doa))
            addShopReqData.addtional_doa =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.add_doa)

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
            addShopReqData.doc_family_member_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.doc_family_dob)

        if (!TextUtils.isEmpty(addShopData.assistant_dob))
            addShopReqData.assistant_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.assistant_dob)

        if (!TextUtils.isEmpty(addShopData.assistant_doa))
            addShopReqData.assistant_doa =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.assistant_doa)

        if (!TextUtils.isEmpty(addShopData.assistant_family_dob))
            addShopReqData.assistant_family_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.assistant_family_dob)

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
                    shopContactList("")
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

        val index = addShopReqData.shop_id!!.indexOf("_")

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

                                    shopContactList("")
                                } else {
                                    (mContext as DashboardActivity).showSnackMessage("Stage updated successfully")
                                }
                            }
                            val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
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

                                    shopContactList("")
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
                                shopContactList("")
                            } else {
                                (mContext as DashboardActivity).showSnackMessage("Stage updated successfully")
                            }
                        }
                    })
            )
        }
    }

    private fun syncCompanyMaster(compName:String){
        progress_wheel.spin()
        //var unsyncL = AppDatabase.getDBInstance()!!.companyMasterDao().getUnSync(false,compName)

        var unsyncL = AppDatabase.getDBInstance()!!.companyMasterDao().getUnSyncList(false) as ArrayList<CompanyMasterEntity>
        var compReq :CompanyReqData = CompanyReqData()
        if(unsyncL.size>0){
            compReq.created_by = Pref.user_id.toString()
            compReq.session_token = Pref.session_token.toString()
            for(i in 0..unsyncL.size-1){
                compReq.company_name_list.add(CompanyName(unsyncL.get(i).company_name.toString()))
            }
            val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.saveCompanyMasterNw(compReq)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val resp = result as BaseResponse
                        progress_wheel.stopSpinning()
                        if(resp.status == NetworkConstant.SUCCESS){
                            callCRMCompanyMasterApi()
                        }else{
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        }
                    }, { error ->
                        error.printStackTrace()
                        tv_syncAll.isEnabled=true
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        Timber.d("AddShop err : ${error.message}")
                    })
            )
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun callCRMCompanyMasterApi(){
        progress_wheel.spin()
        val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
        BaseActivity.compositeDisposable.add(
            repository.callCompanyMaster(Pref.session_token.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val resp = result as ContactMasterRes
                    progress_wheel.stopSpinning()
                    if(resp.status == NetworkConstant.SUCCESS){
                        AppDatabase.getDBInstance()?.companyMasterDao()?.deleteAll()
                        AppDatabase.getDBInstance()?.companyMasterDao()?.insertAll(resp.company_list)
                    }else{
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    }
                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    Timber.d("AddShop err : ${error.message}")
                })
        )

    }


    fun saveCallHisToDB(obj:AddShopDBModelEntity){
        try{
            println("cont_frag call his saveCallHisToDB")
            progress_wheel.spin()
            doAsync {
                //var callHisL = AppUtils.obtenerDetallesLlamadas(mContext) as java.util.ArrayList<AppUtils.Companion.PhoneCallDtls>
                var callHisL = AppUtils.obtenerDetallesLlamadasByNumber(mContext,obj.ownerContactNumber) as java.util.ArrayList<AppUtils.Companion.PhoneCallDtls>
                println("cont_frag call his size ${callHisL.size}")
                if(callHisL.size>0){
                    for(i in 0..callHisL.size-1){
                        try{
                            var obj: CallHisEntity = CallHisEntity()
                            var callNo = if(callHisL.get(i).number!!.contains("+91")) callHisL.get(i).number!!.replace("+","").removeRange(0,2) else callHisL.get(i).number!!
                            var isMyShop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByPhone(callNo) as ArrayList<AddShopDBModelEntity>
                            if(isMyShop.size>0){
                                obj.apply {
                                    shop_id = isMyShop.get(0).shop_id.toString()
                                    call_number = callNo
                                    call_date = callHisL.get(i).callDateTime!!.split(" ").get(0)
                                    call_time = callHisL.get(i).callDateTime!!.split(" ").get(1)
                                    call_date_time = callHisL.get(i).callDateTime!!
                                    call_type = callHisL.get(i).type!!
                                    if(call_type.equals("MISSED",ignoreCase = true)){
                                        call_duration_sec = "0"
                                    }else{
                                        call_duration_sec = callHisL.get(i).callDuration!!
                                    }
                                    call_duration = AppUtils.getMMSSfromSeconds(call_duration_sec.toInt())
                                }
                                var isPresent = (AppDatabase.getDBInstance()!!.callhisDao().getFilterData(obj.call_number,obj.call_date,obj.call_time,obj.call_type,obj.call_duration_sec) as ArrayList<CallHisEntity>).size
                                if(isPresent==0){
                                    println("cont_frag call his insert ${obj.call_number}")
                                    Timber.d("tag_log_insert ${obj.call_number} ${obj.call_duration}")
                                    AppDatabase.getDBInstance()!!.callhisDao().insert(obj)
                                }
                            }
                        }catch (ex:Exception){
                            ex.printStackTrace()
                            println("cont_frag call his err inner ${ex.message}")
                        }
                    }
                }
                uiThread {
                    progress_wheel.stopSpinning()
                    showCallInfo(obj)
                }
            }
        }catch (ex:Exception){
            ex.printStackTrace()
            println("cont_frag call his err ${ex.message}")
        }
    }

    fun showCallInfo(obj:AddShopDBModelEntity){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.setCanceledOnTouchOutside(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_info_1)
        val dialogHeader = simpleDialog.findViewById(R.id.tv_dialog_info_1_header) as TextView
        val rvList = simpleDialog.findViewById(R.id.rv_dialog_info_1_info) as RecyclerView
        val dialogCross = simpleDialog.findViewById(R.id.iv_dialog_info_1_cross) as ImageView
        val tv_noData = simpleDialog.findViewById(R.id.tv_dialog_info_1_info_na) as TextView
        val tv_totalCallDuration = simpleDialog.findViewById(R.id.tv_dialog_info_1_info_total_call_duraton) as TextView
        val tv_totalCallCount = simpleDialog.findViewById(R.id.tv_dialog_info_1_info_total_call_count) as TextView
        val ll_countRoot = simpleDialog.findViewById(R.id.ll_dialog_info_1_count_root) as LinearLayout

        dialogHeader.text = obj.ownerName

        dialogCross.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()

        var callL = AppDatabase.getDBInstance()!!.callhisDao().getCallListByPhone(obj.ownerContactNumber!!) as ArrayList<CallHisEntity>
        //var callL = AppDatabase.getDBInstance()!!.callhisDao().getCallListByPhone("9830916971") as ArrayList<CallHisEntity>
        if(callL.size>0){

            dialogHeader.text = obj.ownerName+" (Count : ${callL.size})"

            tv_noData.visibility = View.GONE
            rvList.visibility = View.VISIBLE
            ll_countRoot.visibility = View.VISIBLE
            adapterCallLogL = AdapterCallLogL(mContext,callL,false,object :AdapterCallLogL.onClick{
                override fun onSyncClick(obj: CallHisEntity) {

                }
            })
            rvList.adapter=adapterCallLogL

            var totalCallDurationInSec: Int = callL.sumOf { it.call_duration_sec.toInt() }
            tv_totalCallDuration.text = "Total Call duration : ${AppUtils.getMMSSfromSeconds(totalCallDurationInSec.toInt())}"
            tv_totalCallCount.text = "Total Call count : ${callL.size}"

        }else{
            tv_noData.visibility = View.VISIBLE
            rvList.visibility = View.GONE
            ll_countRoot.visibility = View.GONE
        }
    }

    private fun syncContact(shopObj: AddShopDBModelEntity,isFromSyncAll:Boolean = false,isFromSyncAllLast:Boolean=false){
        println("tag_conta_show ${shopObj.ownerName} $isFromSyncAllLast")
        progress_wheel.spin()
        var addShopRequestData: AddShopRequestData = AddShopRequestData()
        addShopRequestData.user_id = Pref.user_id
        addShopRequestData.session_token = Pref.session_token
        addShopRequestData.shop_id = shopObj.shop_id
        addShopRequestData.shop_name = shopObj.shopName
        addShopRequestData.address = shopObj.address
        addShopRequestData.actual_address = shopObj.address
        addShopRequestData.pin_code = shopObj.pinCode
        addShopRequestData.type = shopObj.type
        addShopRequestData.shop_lat = shopObj.shopLat.toString()
        addShopRequestData.shop_long = shopObj.shopLong.toString()
        addShopRequestData.owner_email = shopObj.ownerEmailId.toString()
        addShopRequestData.owner_name = shopObj.shopName.toString()
        addShopRequestData.owner_contact_no = shopObj.ownerContactNumber.toString()
        addShopRequestData.amount = shopObj.amount.toString()

        addShopRequestData.shop_firstName=  shopObj.crm_firstName.toString()
        addShopRequestData.shop_lastName=  shopObj.crm_lastName.toString()
        addShopRequestData.crm_companyID=  if(shopObj.companyName_id.equals("")) "0" else shopObj.companyName_id
        addShopRequestData.crm_jobTitle=  shopObj.jobTitle
        addShopRequestData.crm_typeID=  if(shopObj.crm_type_ID.equals("")) "0" else shopObj.crm_type_ID
        addShopRequestData.crm_statusID=  if(shopObj.crm_status_ID.equals("")) "0" else shopObj.crm_status_ID
        addShopRequestData.crm_sourceID= if(shopObj.crm_source_ID.equals("")) "0" else shopObj.crm_source_ID
        addShopRequestData.crm_reference=  shopObj.crm_reference
        addShopRequestData.crm_referenceID=  if(shopObj.crm_reference_ID.equals("")) "0" else shopObj.crm_reference_ID
        addShopRequestData.crm_referenceID_type=  shopObj.crm_reference_ID_type
        addShopRequestData.crm_stage_ID=  if(shopObj.crm_stage_ID.equals("")) "0" else shopObj.crm_stage_ID
        addShopRequestData.assign_to=  shopObj.crm_assignTo_ID
        addShopRequestData.saved_from_status=  shopObj.crm_saved_from
        addShopRequestData.isFromCRM = 1


        val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
        BaseActivity.compositeDisposable.add(
            repository.addShop(addShopRequestData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val addShopResult = result as AddShopResponse
                    Timber.d("tag_conta_show AddShop : , SHOP: " + addShopRequestData.shop_name + ", RESPONSE:" + result.message)
                    progress_wheel.stopSpinning()
                    if (addShopResult.status == NetworkConstant.SUCCESS) {
                        AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShopRequestData.shop_id)
                        if(isFromSyncAll){
                            if(isFromSyncAllLast){
                                Handler().postDelayed(Runnable {
                                    showMsg("Sync Successfully done. Thanks.")
                                    voiceMsg("Sync Successfully done. Thanks.")
                                }, 2600)
                                checkModifiedShop()
                            }
                        }else{
                            Handler().postDelayed(Runnable {
                                showMsg("Sync Successfully done. Thanks.")
                                voiceMsg("Sync Successfully done. Thanks.")
                            }, 2600)

                            checkModifiedShop()
                        }
                    }
                    else {
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    }
                }, { error ->
                    progress_wheel.stopSpinning()
                    tv_syncAll.isEnabled=true
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    Timber.d("AddShop err : ${error.message}")
                })
        )
    }

    fun showMsg(msg:String){
        /*progress_wheel.spin()
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_ok)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
        dialogHeader.text = msg
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            Handler().postDelayed(Runnable {
                progress_wheel.stopSpinning()
                shopContactList("")
            }, 3000)

        })
        simpleDialog.show()*/

        progress_wheel.spin()
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.generic_dialog)
        val head = simpleDialog.findViewById(R.id.tv_generic_dialog_header) as AppCustomTextView
        val cross = simpleDialog.findViewById(R.id.iv_generic_dialog_cancel) as ImageView
        head.text = AppUtils.hiFirstNameText()
        val dialogHeader = simpleDialog.findViewById(R.id.tv_generic_dialog_body) as AppCustomTextView
        dialogHeader.text = msg
        val dialogYes = simpleDialog.findViewById(R.id.tv_generic_dialog_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            Handler().postDelayed(Runnable {
                progress_wheel.stopSpinning()
                shopContactList("")
            }, 3000)

        })
        cross.setOnClickListener({ view ->
            simpleDialog.cancel()
            Handler().postDelayed(Runnable {
                progress_wheel.stopSpinning()
                shopContactList("")
            }, 3000)

        })
        simpleDialog.show()
    }

    //Refresh contact list
    fun checkModifiedShop() {

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            tv_syncAll.isEnabled=true
            return
        }
        progress_wheel.spin()
        callCRMCompanyMasterApiRefresh()
    }

    fun callCRMCompanyMasterApiRefresh() {
            val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.callCompanyMaster(Pref.session_token.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val resp = result as ContactMasterRes
                        if(resp.status == NetworkConstant.SUCCESS){
                            AppDatabase.getDBInstance()?.companyMasterDao()?.deleteAll()
                            AppDatabase.getDBInstance()?.companyMasterDao()?.insertAll(resp.company_list)
                            callCRMTypeMasterAPI()
                        }else{
                            callCRMTypeMasterAPI()
                        }
                    }, { error ->
                        error.printStackTrace()
                        tv_syncAll.isEnabled=true
                        callCRMTypeMasterAPI()
                    })
            )
    }

    fun callCRMTypeMasterAPI(){
            val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.callTypeMaster(Pref.session_token.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val resp = result as TypeMasterRes
                        if(resp.status == NetworkConstant.SUCCESS){
                            AppDatabase.getDBInstance()?.typeMasterDao()?.deleteAll()
                            AppDatabase.getDBInstance()?.typeMasterDao()?.insertAll(resp.type_list)
                            callCRMStatusMasterAPI()

                        }else{
                            callCRMStatusMasterAPI()
                        }
                    }, { error ->
                        error.printStackTrace()
                        tv_syncAll.isEnabled=true
                        callCRMStatusMasterAPI()
                    })
            )
    }

    fun callCRMStatusMasterAPI(){
            val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.callStatusMaster(Pref.session_token.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val resp = result as StatusMasterRes
                        if(resp.status == NetworkConstant.SUCCESS){
                            AppDatabase.getDBInstance()?.statusMasterDao()?.deleteAll()
                            AppDatabase.getDBInstance()?.statusMasterDao()?.insertAll(resp.status_list)
                            callCRMSourceMasterAPI()

                        }else{
                            callCRMSourceMasterAPI()
                        }
                    }, { error ->
                        error.printStackTrace()
                        tv_syncAll.isEnabled=true
                        callCRMSourceMasterAPI()
                    })
            )
    }

    fun callCRMSourceMasterAPI(){
            val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.callSourceMaster(Pref.session_token.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val resp = result as SourceMasterRes
                        if(resp.status == NetworkConstant.SUCCESS){
                            AppDatabase.getDBInstance()?.sourceMasterDao()?.deleteAll()
                            AppDatabase.getDBInstance()?.sourceMasterDao()?.insertAll(resp.source_list)
                            callCRMStageMasterAPI()
                        }else{
                            callCRMStageMasterAPI()
                        }
                    }, { error ->
                        error.printStackTrace()
                        tv_syncAll.isEnabled=true
                        callCRMStageMasterAPI()
                    })
            )
    }

    fun callCRMStageMasterAPI(){
            val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.callStageMaster(Pref.session_token.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val resp = result as StageMasterRes
                        if(resp.status == NetworkConstant.SUCCESS){
                            AppDatabase.getDBInstance()?.stageMasterDao()?.deleteAll()
                            AppDatabase.getDBInstance()?.stageMasterDao()?.insertAll(resp.stage_list)
                            getShopListApiSync()
                        }else{
                            getShopListApiSync()
                        }
                    }, { error ->
                        error.printStackTrace()
                        tv_syncAll.isEnabled=true
                        getShopListApiSync()
                    })
            )

    }

    fun getShopListApiSync() {
        val repository = ShopListRepositoryProvider.provideShopListRepository()
      //  progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.getShopList(Pref.session_token!!, Pref.user_id!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val shopList = result as ShopListResponse
                    if (shopList.status == NetworkConstant.SUCCESS) {
                        progress_wheel.stopSpinning()
                        if (shopList.data!!.shop_list == null || shopList.data!!.shop_list!!.isEmpty()) {

                        } else {
                            convertToShopListSetAdapter(shopList.data!!.shop_list!!)
                        }
                    }else {
                        progress_wheel.stopSpinning()
                    }
                }, { error ->
                    error.printStackTrace()
                    tv_syncAll.isEnabled=true
                    progress_wheel.stopSpinning()

                })
        )
    }


    fun convertToShopListSetAdapter(shop_list: List<ShopData>) {
        progress_wheel.spin()
        AppDatabase.getDBInstance()!!.addShopEntryDao().deleteAll()
        val list: MutableList<AddShopDBModelEntity> = ArrayList()
        val shopObj = AddShopDBModelEntity()
        doAsync {
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
                //Code start by Puja
                //shopObj.remarks = shop_list[i].is_chamber_same_headquarter_remarks
                shopObj.remarks = shop_list[i].remarks
                //Code end by Puja
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

                try {
                    shopObj.Shop_NextFollowupDate = if(shop_list[i].Shop_NextFollowupDate.isNullOrEmpty() || shop_list[i].Shop_NextFollowupDate.equals("1900-01-01")) "" else shop_list[i].Shop_NextFollowupDate
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                list.add(shopObj)
                AppDatabase.getDBInstance()!!.addShopEntryDao().insert(shopObj)
            }
            uiThread {
                progress_wheel.stopSpinning()
                Toaster.msgShort(mContext,"CRM data refreshed successfully.")
                tv_syncAll.isEnabled=true
                getOpptStatus()
            }
        }
    }

    private fun getOpptStatus() {
        progress_wheel.spin()
        val repository = OpportunityRepoProvider.opportunityListRepo()
        BaseActivity.compositeDisposable.add(
            repository.getOpportunityStatus(Pref.session_token!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as OpportunityStatusListResponseModel
                    if (response.status == NetworkConstant.SUCCESS) {
                        var list = response.status_list
                        if (list != null && list.isNotEmpty()) {
                            doAsync {
                                AppDatabase.getDBInstance()!!.opportunityStatusDao().deleteAll()
                                AppDatabase.getDBInstance()?.opportunityStatusDao()?.insertAll(list!!)
                                uiThread {
                                    progress_wheel.stopSpinning()
                                    getProductList()
                                }
                            }
                        } else {
                            progress_wheel.stopSpinning()
                            getProductList()
                        }
                    } else {
                        progress_wheel.stopSpinning()
                        getProductList()
                    }
                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    getProductList()
                })
        )
    }

    private fun getProductList() {
        progress_wheel.spin()
        val repositoryP = ProductListRepoProvider.productListProvider()
        BaseActivity.compositeDisposable.add(
            repositoryP.getProductList(Pref.session_token!!, Pref.user_id!!, "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as ProductListResponseModel
                    println("tag_dash_product api call response ${response.status}")
                    if (response.status == NetworkConstant.SUCCESS) {
                        val list = response.product_list
                        if (list != null && list.isNotEmpty()) {
                            doAsync {
                                AppDatabase.getDBInstance()?.productListDao()?.deleteAllProduct()
                                AppDatabase.getDBInstance()?.productListDao()?.insertAll(list!!)
                                uiThread {
                                    progress_wheel.stopSpinning()
                                    shopContactList("")
                                }
                            }
                        } else {
                            progress_wheel.stopSpinning()
                            shopContactList("")
                        }
                    } else {
                        progress_wheel.stopSpinning()
                        shopContactList("")
                    }

                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    shopContactList("")
                })
        )
    }

    private fun convertToReqAndApiCallForShopStatus(addShopData: AddShopDBModelEntity) {
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
            addShopReqData.dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.dateOfBirth)

        if (!TextUtils.isEmpty(addShopData.dateOfAniversary))
            addShopReqData.date_aniversary =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.dateOfAniversary)

        addShopReqData.director_name = addShopData.director_name
        addShopReqData.key_person_name = addShopData.person_name
        addShopReqData.phone_no = addShopData.person_no

        if (!TextUtils.isEmpty(addShopData.family_member_dob))
            addShopReqData.family_member_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.family_member_dob)

        if (!TextUtils.isEmpty(addShopData.add_dob))
            addShopReqData.addtional_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.add_dob)

        if (!TextUtils.isEmpty(addShopData.add_doa))
            addShopReqData.addtional_doa =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.add_doa)

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
            addShopReqData.doc_family_member_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.doc_family_dob)

        if (!TextUtils.isEmpty(addShopData.assistant_dob))
            addShopReqData.assistant_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.assistant_dob)

        if (!TextUtils.isEmpty(addShopData.assistant_doa))
            addShopReqData.assistant_doa =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.assistant_doa)

        if (!TextUtils.isEmpty(addShopData.assistant_family_dob))
            addShopReqData.assistant_family_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.assistant_family_dob)

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

        callEditShopApiForShopStatus(addShopReqData)
    }

    private fun callEditShopApiForShopStatus(addShopReqData: AddShopRequestData) {
        val index = addShopReqData.shop_id!!.indexOf("_")
        progress_wheel.spin()
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
                            shopContactList("")
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage("Status updated successfully")
                        }
                    }, { error ->
                        error.printStackTrace()
                        progress_wheel.stopSpinning()
                    })
            )
    }

    fun generateContactDtlsPdf(shopObj:AddShopDBModelEntity){
        var document: Document = Document(PageSize.A4, 36f, 36f, 36f, 80f)
        val time = System.currentTimeMillis()
        var fileName = "CRM" +  "_" + time
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/FSMApp/CallHis/"

        var pathNew = ""

        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        try{
            try {
                PdfWriter.getInstance(document, FileOutputStream(path + fileName + ".pdf"))
            }catch (ex:Exception){
                ex.printStackTrace()

                pathNew = mContext.filesDir.toString() + "/" + fileName+".pdf"
                //val file = File(mContext.filesDir.toString() + "/" + fileName)
                PdfWriter.getInstance(document, FileOutputStream(pathNew))
            }

            document.open()

            var font: Font = Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD)
            var font1: Font = Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL)
            var fontBoldU: Font = Font(Font.FontFamily.HELVETICA, 9f, Font.UNDERLINE or Font.BOLD)
            var fontBoldUHeader: Font = Font(Font.FontFamily.HELVETICA, 12f, Font.UNDERLINE or Font.BOLD)

            //image add
            //code start by Puja mantis-0027395 date-23.04.24 v4.2.6
            //val bm: Bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
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
            document.add(img)

            val para = Paragraph()
            para.alignment = Element.ALIGN_CENTER
            para.indentationLeft = 220f
            val glue = Chunk(VerticalPositionMark())
            val ph = Phrase()
            ph.add(Chunk("CRM Details", fontBoldUHeader))
            ph.add(glue)
           // ph.add(Chunk("DATE: " + AppUtils.getCurrentDate_DD_MM_YYYY() + " ", font1))
            para.add(ph)
            document.add(para)

            val spac = Paragraph("", font)
            spac.spacingAfter = 15f
            document.add(spac)

            val name = Paragraph("Name                              :      " + shopObj?.shopName, font1)
            name.alignment = Element.ALIGN_LEFT
            name.spacingAfter = 2f
            document.add(name)

            val addr = Paragraph("Address                          :      " + shopObj?.address+" "+ if (shopObj?.pinCode.equals("0")) "" else shopObj.pinCode, font1)
            addr.alignment = Element.ALIGN_LEFT
            addr.spacingAfter = 2f
            document.add(addr)

            val contNo = Paragraph("Contact No.                    :      " + shopObj?.ownerContactNumber, font1)
            contNo.alignment = Element.ALIGN_LEFT
            contNo.spacingAfter = 2f
            document.add(contNo)

            try {
                val whatsNo = Paragraph("Whatsapp No.                :      " + if(shopObj?.whatsappNoForCustomer.isNullOrEmpty()) "None" else shopObj?.whatsappNoForCustomer , font1)
                whatsNo.alignment = Element.ALIGN_LEFT
                whatsNo.spacingAfter = 2f
                document.add(whatsNo)
            }catch (ex:Exception){
                ex.printStackTrace()
            }


            val email = Paragraph("Email                              :      " + if(shopObj?.ownerEmailId.isNullOrEmpty()) "None" else shopObj?.ownerEmailId, font1)
            email.alignment = Element.ALIGN_LEFT
            email.spacingAfter = 2f
            document.add(email)

            val crm_company = Paragraph("Company                       :      " + if(shopObj?.companyName.isNullOrEmpty()) "None" else shopObj?.companyName, font1)
            crm_company.alignment = Element.ALIGN_LEFT
            crm_company.spacingAfter = 2f
            document.add(crm_company)

            val jobTitle = Paragraph("Job Title                         :      " + if(shopObj?.jobTitle.isNullOrEmpty()) "None" else shopObj?.jobTitle, font1)
            jobTitle.alignment = Element.ALIGN_LEFT
            jobTitle.spacingAfter = 2f
            document.add(jobTitle)

            val type = Paragraph("Type                              :      " + if(shopObj?.crm_type.isNullOrEmpty()) "None" else shopObj?.crm_type, font1)
            type.alignment = Element.ALIGN_LEFT
            type.spacingAfter = 2f
            document.add(type)

            val source = Paragraph("Source                           :      " + if(shopObj?.crm_source.isNullOrEmpty()) "None" else shopObj?.crm_source, font1)
            source.alignment = Element.ALIGN_LEFT
            source.spacingAfter = 2f
            document.add(source)

            val ref = Paragraph("Reference                      :      " + if(shopObj?.crm_reference.isNullOrEmpty()) "None" else shopObj?.crm_reference, font1)
            ref.alignment = Element.ALIGN_LEFT
            ref.spacingAfter = 2f
            document.add(ref)

            val stage = Paragraph("Stage                             :      " + if(shopObj?.crm_stage.isNullOrEmpty()) "None" else shopObj?.crm_stage, font1)
            stage.alignment = Element.ALIGN_LEFT
            stage.spacingAfter = 2f
            document.add(stage)

            val status = Paragraph("Status                            :      " + if(shopObj?.crm_status.isNullOrEmpty()) "None" else shopObj?.crm_status, font1)
            status.alignment = Element.ALIGN_LEFT
            status.spacingAfter = 2f
            document.add(status)

            var added_date = shopObj.added_date.replace("T"," ").split(" ").get(0).toString()
            var added_time = shopObj.added_date.replace("T"," ").split(" ").get(1).toString()
            val addedDtTi = Paragraph("Added Date-Time          :      " + if(added_date.isNullOrEmpty()) "None" else AppUtils.getFormatedDateNew(added_date,"yyyy-mm-dd","dd-mm-yyyy")+" - "+added_time.substring(0,5).toString(), font1)
            addedDtTi.alignment = Element.ALIGN_LEFT
            addedDtTi.spacingAfter = 2f
            document.add(addedDtTi)

            val crmFrom = Paragraph("Contact From                :      " + if(shopObj?.crm_saved_from.isNullOrEmpty()) "None" else shopObj?.crm_saved_from, font1)
            crmFrom.alignment = Element.ALIGN_LEFT
            crmFrom.spacingAfter = 2f
            document.add(crmFrom)

            val expSaleV = Paragraph("Expected Sales Value   :      " + if(shopObj?.amount.isNullOrEmpty()) "0.00" else shopObj?.amount, font1)
            expSaleV.alignment = Element.ALIGN_LEFT
            expSaleV.spacingAfter = 2f
            document.add(expSaleV)

            //Code start by Puja
            val remarksField = Paragraph("Remarks                       :      " + if(shopObj?.remarks.isNullOrEmpty()) "0.00" else shopObj?.remarks, font1)
            remarksField.alignment = Element.ALIGN_LEFT
            remarksField.spacingAfter = 2f
            document.add(remarksField)
            //Code end by Puja

            document.add(spac)

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

        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    private fun updateLabel() {
        selectedDate = AppUtils.getFormattedDateForApi(myCalendar.time)
        var obj: ContactActivityEntity = ContactActivityEntity()
        obj.apply {
            shop_id  = selectedShopIdForActivity
            activity_date = selectedDate
            create_date_time = AppUtils.getCurrentDateTime()
            isActivityDone = false
        }
        AppDatabase.getDBInstance()?.contactActivityDao()?.insert(obj)

        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_ok)

        try {
            simpleDialog.setCancelable(true)
            simpleDialog.setCanceledOnTouchOutside(false)
            val dialogName = simpleDialog.findViewById(R.id.tv_dialog_ok_name) as AppCustomTextView
            val dialogCross = simpleDialog.findViewById(R.id.tv_dialog_ok_cancel) as ImageView
            dialogName.text = AppUtils.hiFirstNameText()
            dialogCross.setOnClickListener {
                simpleDialog.cancel()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
        dialogHeader.text = "CRM auto activity set for ${AppUtils.getFormatedDateNew(selectedDate,"yyyy-mm-dd","dd-mm-yyyy")} is successful."
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            BaseActivity.isApiInitiated = false
        })
        simpleDialog.show()
    }

    fun updateToolbar() {
        super.onResume()
        try {
            var contL : ArrayList<AddShopDBModelEntity> = ArrayList()
            contL = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcShops() as ArrayList<AddShopDBModelEntity>
            (mContext as DashboardActivity).setTopBarTitle("CRM : ${contL.size}")
        }catch (ex:Exception){
            ex.printStackTrace()
            (mContext as DashboardActivity).setTopBarTitle("CRM")
        }
    }

    fun getCRMStatusList(){
        try {
            var crmStatusList = AppDatabase.getDBInstance()?.statusMasterDao()?.getAll() as ArrayList<StatusMasterEntity>
            if(crmStatusList.size>0){
                var genericL : ArrayList<CustomData> = ArrayList()
                for(i in 0..crmStatusList.size-1){
                    genericL.add(CustomData(crmStatusList.get(i).status_id.toString(),crmStatusList.get(i).status_name.toString()))
                }
                GenericDialog.newInstance("Status",genericL as ArrayList<CustomData>){
                    str_filterEntity = "Status"
                    str_filterEntityValue = it.name
                    str_filterText = "Filtered on : $str_filterEntity($str_filterEntityValue)"
                    shopContactList("",  crm_status_ID = it.id)
                }.show((mContext as DashboardActivity).supportFragmentManager, "")
            }else{
                Toaster.msgShort(mContext, "No Status Found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCRMStageList(){
        try {
            var crmStageList = AppDatabase.getDBInstance()?.stageMasterDao()?.getAll() as ArrayList<StageMasterEntity>
            if(crmStageList.size>0){
                var genericL : ArrayList<CustomData> = ArrayList()
                for(i in 0..crmStageList.size-1){
                    genericL.add(CustomData(crmStageList.get(i).stage_id.toString(),crmStageList.get(i).stage_name.toString()))
                }
                GenericDialog.newInstance("Stage",genericL as ArrayList<CustomData>){
                    str_filterEntity = "Stage"
                    str_filterEntityValue = it.name
                    str_filterText = "Filtered on : $str_filterEntity($str_filterEntityValue)"
                    shopContactList("",crm_stage_ID = it.id)
                }.show((mContext as DashboardActivity).supportFragmentManager, "")
            }else{
                Toaster.msgShort(mContext, "No Stage Found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCRMTypeList(){
        try {
            var crmTypeList =  AppDatabase.getDBInstance()?.typeMasterDao()?.getAll() as ArrayList<TypeMasterEntity>
            if(crmTypeList.size>0){
                var genericL : ArrayList<CustomData> = ArrayList()
                for(i in 0..crmTypeList.size-1){
                    genericL.add(CustomData(crmTypeList.get(i).type_id.toString(),crmTypeList.get(i).type_name.toString()))
                }
                GenericDialog.newInstance("Contact Type",genericL as ArrayList<CustomData>){
                    str_filterEntity = "Contact Type"
                    str_filterEntityValue = it.name
                    str_filterText = "Filtered on : $str_filterEntity($str_filterEntityValue)"
                    shopContactList("",crm_type_ID = it.id)
                }.show((mContext as DashboardActivity).supportFragmentManager, "")
            }else{
                Toaster.msgShort(mContext, "No Type Found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCRMSourceList(){
        var crmSourceList = AppDatabase.getDBInstance()?.sourceMasterDao()?.getAll() as ArrayList<SourceMasterEntity>
        if(crmSourceList.size>0){
            var genericL : ArrayList<CustomData> = ArrayList()
            for(i in 0..crmSourceList.size-1){
                genericL.add(CustomData(crmSourceList.get(i).source_id.toString(),crmSourceList.get(i).source_name.toString()))
            }
            GenericDialog.newInstance("Source",genericL as ArrayList<CustomData>){
                str_filterEntity = "Source"
                str_filterEntityValue = it.name
                str_filterText = "Filtered on : $str_filterEntity($str_filterEntityValue)"
                shopContactList("",crm_source_ID=it.id)
            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        }else{
            Toaster.msgShort(mContext, "No Source Found")
        }
    }

    private fun callUserConfigApi() {
        val repository = UserConfigRepoProvider.provideUserConfigRepository()
        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
            repository.userConfig(Pref.user_id!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as UserConfigResponseModel
                    if (response.status == NetworkConstant.SUCCESS) {
                        try {
                            if (response.getconfigure != null && response.getconfigure!!.size > 0) {
                                for (i in response.getconfigure!!.indices) {
                                    if (response.getconfigure!![i].Key.equals("isVisitSync", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            AppUtils.isVisitSync = response.getconfigure!![i].Value!!
                                    } else if (response.getconfigure!![i].Key.equals("isAddressUpdate", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            AppUtils.isAddressUpdated = response.getconfigure!![i].Value!!
                                    } else if (response.getconfigure!![i].Key.equals("willShowUpdateDayPlan", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willShowUpdateDayPlan = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("updateDayPlanText", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.updateDayPlanText = response.getconfigure!![i].Value!!
                                    } else if (response.getconfigure!![i].Key.equals("dailyPlanListHeaderText", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.dailyPlanListHeaderText = response.getconfigure!![i].Value!!
                                    } else if (response.getconfigure!![i].Key.equals("isRateNotEditable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isRateNotEditable = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isMeetingAvailable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isMeetingAvailable = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure?.get(i)?.Key.equals("willShowTeamDetails", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.willShowTeamDetails = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isAllowPJPUpdateForTeam", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isAllowPJPUpdateForTeam = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("willLeaveApprovalEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willLeaveApprovalEnable = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure?.get(i)?.Key.equals("willReportShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.willReportShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("willAttendanceReportShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.willAttendanceReportShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("willPerformanceReportShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.willPerformanceReportShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("willVisitReportShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.willVisitReportShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("attendance_text", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.attendance_text = response.getconfigure?.get(i)?.Value!!
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("willTimesheetShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.willTimesheetShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isAttendanceFeatureOnly", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isAttendanceFeatureOnly = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("iscollectioninMenuShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isCollectioninMenuShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isVisitShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isVisitShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isOrderShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isOrderShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isShopAddEditAvailable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShopAddEditAvailable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isEntityCodeVisible", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isEntityCodeVisible = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isAreaMandatoryInPartyCreation", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isAreaMandatoryInPartyCreation = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isShowPartyInAreaWiseTeam", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowPartyInAreaWiseTeam = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isChangePasswordAllowed", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isChangePasswordAllowed = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isQuotationShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isQuotationShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isQuotationPopupShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isQuotationPopupShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isHomeRestrictAttendance", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isHomeRestrictAttendance = response.getconfigure?.get(i)?.Value!!
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("homeLocDistance", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.homeLocDistance = response.getconfigure?.get(i)?.Value!!
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("shopLocAccuracy", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.shopLocAccuracy = response.getconfigure?.get(i)?.Value!!
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isMultipleAttendanceSelection", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isMultipleAttendanceSelection = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isOrderReplacedWithTeam", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isOrderReplacedWithTeam = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isDDShowForMeeting", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isDDShowForMeeting = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isDDMandatoryForMeeting", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isDDMandatoryForMeeting = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isOfflineTeam", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isOfflineTeam = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isAllTeamAvailable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isAllTeamAvailable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isNextVisitDateMandatory", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isNextVisitDateMandatory = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isRecordAudioEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isRecordAudioEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isShowCurrentLocNotifiaction", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowCurrentLocNotifiaction = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isUpdateWorkTypeEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isUpdateWorkTypeEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isAchievementEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isAchievementEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isTarVsAchvEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isTarVsAchvEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isLeaveEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isLeaveEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isOrderMailVisible", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isOrderMailVisible = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isShopEditEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShopEditEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isTaskEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isTaskEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("isAppInfoEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isAppInfoEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("appInfoMins", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.appInfoMins = response.getconfigure?.get(i)?.Value!!
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("autoRevisitDistance", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.autoRevisitDistance = response.getconfigure!![i].Value!!
                                    } else if (response.getconfigure!![i].Key.equals("autoRevisitTime", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.autoRevisitTime = response.getconfigure!![i].Value!!
                                    } else if (response.getconfigure!![i].Key.equals("willAutoRevisitEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willAutoRevisitEnable = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("dynamicFormName", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.dynamicFormName = response.getconfigure!![i].Value!!
                                    } else if (response.getconfigure!![i].Key.equals("willDynamicShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willDynamicShow = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("willActivityShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willActivityShow = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("willMoreVisitUpdateCompulsory", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willMoreVisitUpdateCompulsory = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("willMoreVisitUpdateOptional", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willMoreVisitUpdateOptional = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isDocumentRepoShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isDocumentRepoShow = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isChatBotShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isChatBotShow = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isAttendanceBotShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isAttendanceBotShow = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isVisitBotShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isVisitBotShow = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isShowOrderRemarks", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isShowOrderRemarks = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isShowOrderSignature", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isShowOrderSignature = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isShowSmsForParty", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isShowSmsForParty = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isVisitPlanShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isVisitPlanShow = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isVisitPlanMandatory", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isVisitPlanMandatory = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isAttendanceDistanceShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isAttendanceDistanceShow = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("willTimelineWithFixedLocationShow", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willTimelineWithFixedLocationShow = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isShowTimeline", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isShowTimeline = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("willScanVisitingCard", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willScanVisitingCard = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isCreateQrCode", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isCreateQrCode = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isScanQrForRevisit", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isScanQrForRevisit = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("isShowLogoutReason", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.isShowLogoutReason = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("willShowHomeLocReason", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willShowHomeLocReason = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("willShowShopVisitReason", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willShowShopVisitReason = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("minVisitDurationSpentTime", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.minVisitDurationSpentTime = response.getconfigure?.get(i)?.Value!!
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("willShowPartyStatus", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                            Pref.willShowPartyStatus = response.getconfigure!![i].Value == "1"
                                    } else if (response.getconfigure!![i].Key.equals("willShowEntityTypeforShop", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.willShowEntityTypeforShop = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isShowRetailerEntity", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowRetailerEntity = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isShowDealerForDD", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowDealerForDD = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isShowBeatGroup", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowBeatGroup = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isShowShopBeatWise", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowShopBeatWise = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isShowBankDetailsForShop", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowBankDetailsForShop = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isShowOTPVerificationPopup", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowOTPVerificationPopup = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("locationTrackInterval", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.locationTrackInterval = response.getconfigure!![i].Value!!
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isShowMicroLearning", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowMicroLearning = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("homeLocReasonCheckMins", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.homeLocReasonCheckMins = response.getconfigure!![i].Value!!
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("currentLocationNotificationMins", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.currentLocationNotificationMins = response.getconfigure!![i].Value!!
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isMultipleVisitEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isMultipleVisitEnable = response.getconfigure!![i].Value!! == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isShowVisitRemarks", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowVisitRemarks = response.getconfigure!![i].Value!! == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isShowNearbyCustomer", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShowNearbyCustomer = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isServiceFeatureEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isServiceFeatureEnable = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isPatientDetailsShowInOrder", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isPatientDetailsShowInOrder = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isPatientDetailsShowInCollection", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isPatientDetailsShowInCollection = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isShopImageMandatory", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.isShopImageMandatory = response.getconfigure!![i].Value == "1"
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("isLogShareinLogin", ignoreCase = true)) {
                                        if (response.getconfigure!![i].Value == "1") {
                                            AppUtils.saveSharedPreferenceslogShareinLogin(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferenceslogShareinLogin(mContext, false)
                                        }

                                    } else if (response.getconfigure!![i].Key.equals("IsCompetitorenable", ignoreCase = true)) {
                                        Pref.isCompetitorImgEnable = response.getconfigure!![i].Value == "1"
                                        if (Pref.isCompetitorImgEnable) {
                                            AppUtils.saveSharedPreferencesCompetitorImgEnable(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferencesCompetitorImgEnable(mContext, false)
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("IsOrderStatusRequired", ignoreCase = true)) {
                                        Pref.isOrderStatusRequired = response.getconfigure!![i].Value == "1"
                                        if (Pref.isOrderStatusRequired) {
                                            AppUtils.saveSharedPreferencesOrderStatusRequired(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferencesOrderStatusRequired(mContext, false)
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("IsCurrentStockEnable", ignoreCase = true)) {
                                        Pref.isCurrentStockEnable = response.getconfigure!![i].Value == "1"

                                        if (Pref.isCurrentStockEnable) {
                                            AppUtils.saveSharedPreferencesCurrentStock(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferencesCurrentStock(mContext, false)
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("IsCurrentStockApplicableforAll", ignoreCase = true)) {
                                        Pref.IsCurrentStockApplicableforAll = response.getconfigure!![i].Value == "1"

                                        if (Pref.IsCurrentStockApplicableforAll) {
                                            AppUtils.saveSharedPreferencesCurrentStockApplicableForAll(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferencesCurrentStockApplicableForAll(mContext, false)
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("IscompetitorStockRequired", ignoreCase = true)) {
                                        Pref.IscompetitorStockRequired = response.getconfigure!![i].Value == "1"

                                        if (Pref.IscompetitorStockRequired) {
                                            AppUtils.saveSharedPreferencesIscompetitorStockRequired(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferencesIscompetitorStockRequired(mContext, false)
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("IsCompetitorStockforParty", ignoreCase = true)) {
                                        Pref.IsCompetitorStockforParty = response.getconfigure!![i].Value == "1"

                                        if (Pref.IsCompetitorStockforParty) {
                                            AppUtils.saveSharedPreferencesIsCompetitorStockforParty(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferencesIsCompetitorStockforParty(mContext, false)
                                        }
                                    }
//                                            else if (response.getconfigure!![i].Key.equals("IsFaceDetectionOn", ignoreCase = true)) {
                                    else if (response.getconfigure!![i].Key.equals("ShowFaceRegInMenu", ignoreCase = true)) {
                                        Pref.IsFaceDetectionOn = response.getconfigure!![i].Value == "1"
                                        if (Pref.IsFaceDetectionOn) {
                                            AppUtils.saveSharedPreferencesIsFaceDetectionOn(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferencesIsFaceDetectionOn(mContext, false)
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("IsFaceDetection", ignoreCase = true)) {
                                        Pref.IsFaceDetection = response.getconfigure!![i].Value == "1"
                                        if (Pref.IsFaceDetection) {
                                            AppUtils.saveSharedPreferencesIsFaceDetection(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferencesIsFaceDetection(mContext, false)
                                        }
                                    } else if (response.getconfigure!![i].Key.equals("IsFaceDetectionWithCaptcha", ignoreCase = true)) {
                                        Pref.IsFaceDetectionWithCaptcha = response.getconfigure!![i].Value == "1"

                                        if (Pref.IsFaceDetectionWithCaptcha) {
                                            AppUtils.saveSharedPreferencesIsFaceDetectionWithCaptcha(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferencesIsFaceDetectionWithCaptcha(mContext, false)
                                        }
                                    }
                                    //code start Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7
                                    /*else if (response.getconfigure!![i].Key.equals("IsScreenRecorderEnable", ignoreCase = true)) {
                                        Pref.IsScreenRecorderEnable = response.getconfigure!![i].Value == "1"
                                        if (Pref.IsScreenRecorderEnable) {
                                            AppUtils.saveSharedPreferencesIsScreenRecorderEnable(mContext, true)
                                        } else {
                                            AppUtils.saveSharedPreferencesIsScreenRecorderEnable(mContext, false)
                                        }
                                    }*/
                                    //code end Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7

//                                            else if (response.getconfigure?.get(i)?.Key.equals("IsFromPortal", ignoreCase = true)) {
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsDocRepoFromPortal", ignoreCase = true)) {
                                        Pref.IsFromPortal = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsFromPortal = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsDocRepShareDownloadAllowed", ignoreCase = true)) {
                                        Pref.IsDocRepShareDownloadAllowed = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsDocRepShareDownloadAllowed = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                        println("tag_IsDocRepShareDownloadAllowed dash ${Pref.IsDocRepShareDownloadAllowed}")
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuAddAttendance", ignoreCase = true)) {
                                        Pref.IsShowMenuAddAttendance = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuAddAttendance = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuAttendance", ignoreCase = true)) {
                                        Pref.IsShowMenuAttendance = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuAttendance = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuMIS Report", ignoreCase = true)) {
                                        Pref.IsShowMenuMIS_Report = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuMIS_Report = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuAnyDesk", ignoreCase = true)) {
                                        Pref.IsShowMenuAnyDesk = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuAnyDesk = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuPermission Info", ignoreCase = true)) {
                                        Pref.IsShowMenuPermission_Info = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuPermission_Info = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuScan QR Code", ignoreCase = true)) {
                                        Pref.IsShowMenuScan_QR_Code = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuScan_QR_Code = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuChat", ignoreCase = true)) {
                                        Pref.IsShowMenuChat = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuChat = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuWeather Details", ignoreCase = true)) {
                                        Pref.IsShowMenuWeather_Details = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuWeather_Details = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuHome Location", ignoreCase = true)) {
                                        Pref.IsShowMenuHome_Location = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuHome_Location = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuShare Location", ignoreCase = true)) {
                                        Pref.IsShowMenuShare_Location = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuShare_Location = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuMap View", ignoreCase = true)) {
                                        Pref.IsShowMenuMap_View = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuMap_View = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuReimbursement", ignoreCase = true)) {
                                        Pref.IsShowMenuReimbursement = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuReimbursement = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuOutstanding Details PP/DD", ignoreCase = true)) {
                                        Pref.IsShowMenuOutstanding_Details_PP_DD = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuOutstanding_Details_PP_DD = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuStock Details - PP/DD", ignoreCase = true)) {
                                        Pref.IsShowMenuStock_Details_PP_DD = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuStock_Details_PP_DD = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsLeaveGPSTrack", ignoreCase = true)) {
                                        Pref.IsLeaveGPSTrack = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsLeaveGPSTrack = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowActivitiesInTeam", ignoreCase = true)) {
                                        Pref.IsShowActivitiesInTeam = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowActivitiesInTeam = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    // Added setting Login 12-08-21
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsShowPartyOnAppDashboard", ignoreCase = true)) {
                                        Pref.IsShowPartyOnAppDashboard = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowPartyOnAppDashboard = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowAttendanceOnAppDashboard", ignoreCase = true)) {
                                        Pref.IsShowAttendanceOnAppDashboard = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowAttendanceOnAppDashboard = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowTotalVisitsOnAppDashboard", ignoreCase = true)) {
                                        Pref.IsShowTotalVisitsOnAppDashboard = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowTotalVisitsOnAppDashboard = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowVisitDurationOnAppDashboard", ignoreCase = true)) {
                                        Pref.IsShowVisitDurationOnAppDashboard = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowVisitDurationOnAppDashboard = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowDayStart", ignoreCase = true)) {
                                        Pref.IsShowDayStart = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowDayStart = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsshowDayStartSelfie", ignoreCase = true)) {
                                        Pref.IsshowDayStartSelfie = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsshowDayStartSelfie = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowDayEnd", ignoreCase = true)) {
                                        Pref.IsShowDayEnd = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowDayEnd = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsshowDayEndSelfie", ignoreCase = true)) {
                                        Pref.IsshowDayEndSelfie = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsshowDayEndSelfie = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsShowLeaveInAttendance", ignoreCase = true)) {
                                        Pref.IsShowLeaveInAttendance = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowLeaveInAttendance = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    //19-08-21
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsShowMarkDistVisitOnDshbrd", ignoreCase = true)) {
                                        Pref.IsShowMarkDistVisitOnDshbrd = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMarkDistVisitOnDshbrd = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsActivateNewOrderScreenwithSize", ignoreCase = true)) {
                                        Pref.IsActivateNewOrderScreenwithSize = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsActivateNewOrderScreenwithSize = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsPhotoDeleteShow", ignoreCase = true)) {
                                        Pref.IsPhotoDeleteShow = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsPhotoDeleteShow = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    /*28-09-2021 For Gupta Power*/
                                    else if (response.getconfigure?.get(i)?.Key.equals("RevisitRemarksMandatory", ignoreCase = true)) {
                                        Pref.RevisitRemarksMandatory = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.RevisitRemarksMandatory = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("GPSAlert", ignoreCase = true)) {
                                        Pref.GPSAlert = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.GPSAlert = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("GPSAlertwithSound", ignoreCase = true)) {
                                        Pref.GPSAlertwithSound = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.GPSAlertwithSound = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    /*29-10-2021 Team Attendance*/
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsTeamAttendance", ignoreCase = true)) {
                                        Pref.IsTeamAttendance = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsTeamAttendance = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    /*24-11-2021 ITC face And Distributoraccu*/
                                    else if (response.getconfigure?.get(i)?.Key.equals("FaceDetectionAccuracyUpper", ignoreCase = true)) {
                                        Pref.FaceDetectionAccuracyUpper = response.getconfigure!![i].Value!!
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.FaceDetectionAccuracyUpper = response.getconfigure?.get(i)?.Value!!
                                        }
                                        CustomStatic.FaceDetectionAccuracyUpper = Pref.FaceDetectionAccuracyUpper
                                    } else if (response.getconfigure?.get(i)?.Key.equals("FaceDetectionAccuracyLower", ignoreCase = true)) {
                                        Pref.FaceDetectionAccuracyLower = response.getconfigure!![i].Value!!
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.FaceDetectionAccuracyLower = response.getconfigure?.get(i)?.Value!!
                                        }
                                        CustomStatic.FaceDetectionAccuracyLower = Pref.FaceDetectionAccuracyLower
                                    } else if (response.getconfigure?.get(i)?.Key.equals("DistributorGPSAccuracy", ignoreCase = true)) {
                                        Pref.DistributorGPSAccuracy = response.getconfigure!![i].Value!!
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.DistributorGPSAccuracy = response.getconfigure?.get(i)?.Value!!
                                        }
                                        if (Pref.DistributorGPSAccuracy.length == 0 || Pref.DistributorGPSAccuracy.equals("")) {
                                            Pref.DistributorGPSAccuracy = "500"
                                        }
                                        Timber.d("DistributorGPSAccuracy " + Pref.DistributorGPSAccuracy)
                                    }

                                    /*26-10-2021*/
                                    else if (response.getconfigure?.get(i)?.Key.equals("BatterySetting", ignoreCase = true)) {
                                        Pref.BatterySetting = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.BatterySetting = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("PowerSaverSetting", ignoreCase = true)) {
                                        Pref.PowerSaverSetting = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.PowerSaverSetting = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    /*16-12-2021 return features*/
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsReturnEnableforParty", ignoreCase = true)) {
                                        Pref.IsReturnEnableforParty = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsReturnEnableforParty = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("MRPInOrder", ignoreCase = true)) {
                                        Pref.MRPInOrder = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.MRPInOrder = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("FaceRegistrationFrontCamera", ignoreCase = true)) {
                                        Pref.FaceRegistrationFrontCamera = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.FaceRegistrationFrontCamera = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("IslandlineforCustomer", ignoreCase = true)) {
                                        Pref.IslandlineforCustomer = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IslandlineforCustomer = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsprojectforCustomer", ignoreCase = true)) {
                                        Pref.IsprojectforCustomer = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsprojectforCustomer = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("Leaveapprovalfromsupervisor", ignoreCase = true)) {
                                        Pref.Leaveapprovalfromsupervisor = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.Leaveapprovalfromsupervisor = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("Leaveapprovalfromsupervisorinteam", ignoreCase = true)) {
                                        Pref.Leaveapprovalfromsupervisorinteam = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.Leaveapprovalfromsupervisorinteam = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsRestrictNearbyGeofence", ignoreCase = true)) {
                                        Pref.IsRestrictNearbyGeofence = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsRestrictNearbyGeofence = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsNewQuotationfeatureOn", ignoreCase = true)) {
                                        Pref.IsNewQuotationfeatureOn = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsNewQuotationfeatureOn = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsAlternateNoForCustomer", ignoreCase = true)) {
                                        Pref.IsAlternateNoForCustomer = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsAlternateNoForCustomer = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsWhatsappNoForCustomer", ignoreCase = true)) {
                                        Pref.IsWhatsappNoForCustomer = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsWhatsappNoForCustomer = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsNewQuotationNumberManual", ignoreCase = true)) {
                                        Pref.IsNewQuotationNumberManual = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsNewQuotationNumberManual = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("ShowQuantityNewQuotation", ignoreCase = true)) {
                                        Pref.ShowQuantityNewQuotation = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowQuantityNewQuotation = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("ShowAmountNewQuotation", ignoreCase = true)) {
                                        Pref.ShowAmountNewQuotation = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowQuantityNewQuotation = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("ShowUserwiseLeadMenu", ignoreCase = true)) {
                                        Pref.ShowUserwiseLeadMenu = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowUserwiseLeadMenu = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("GeofencingRelaxationinMeter", ignoreCase = true)) {
                                        try{
                                            Pref.GeofencingRelaxationinMeter = response.getconfigure!![i].Value!!.toInt()
                                            if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                Pref.GeofencingRelaxationinMeter = response.getconfigure!![i].Value!!.toInt()
                                            }
                                        }catch(ex:Exception){
                                            Pref.GeofencingRelaxationinMeter = 100
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsFeedbackHistoryActivated", ignoreCase = true)) {
                                        Pref.IsFeedbackHistoryActivated = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsFeedbackHistoryActivated = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    } else if (response.getconfigure?.get(i)?.Key.equals("IsAutoLeadActivityDateTime", ignoreCase = true)) {
                                        Pref.IsAutoLeadActivityDateTime = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsAutoLeadActivityDateTime = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("LogoutWithLogFile", ignoreCase = true)) {
                                        Pref.LogoutWithLogFile = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.LogoutWithLogFile = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("ShowCollectionAlert", ignoreCase = true)) {
                                        Pref.ShowCollectionAlert = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowCollectionAlert = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("ShowZeroCollectioninAlert", ignoreCase = true)) {
                                        Pref.ShowZeroCollectioninAlert = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowZeroCollectioninAlert = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    else if (response.getconfigure?.get(i)?.Key.equals("IsCollectionOrderWise", ignoreCase = true)) {
                                        Pref.IsCollectionOrderWise = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsCollectionOrderWise = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("ShowCollectionOnlywithInvoiceDetails", ignoreCase = true)) {
                                        Pref.ShowCollectionOnlywithInvoiceDetails = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowCollectionOnlywithInvoiceDetails = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    else if (response.getconfigure?.get(i)?.Key.equals("IsPendingCollectionRequiredUnderTeam", ignoreCase = true)) {
                                        Pref.IsPendingCollectionRequiredUnderTeam = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsPendingCollectionRequiredUnderTeam = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    else if (response.getconfigure?.get(i)?.Key.equals("IsShowRepeatOrderinNotification", ignoreCase = true)) {
                                        Pref.IsShowRepeatOrderinNotification = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowRepeatOrderinNotification = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsShowRepeatOrdersNotificationinTeam", ignoreCase = true)) {
                                        Pref.IsShowRepeatOrdersNotificationinTeam = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowRepeatOrdersNotificationinTeam = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("AutoDDSelect", ignoreCase = true)) {
                                        Pref.AutoDDSelect = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.AutoDDSelect = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("ShowPurposeInShopVisit", ignoreCase = true)) {
                                        Pref.ShowPurposeInShopVisit = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowPurposeInShopVisit = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("GPSAlertwithVibration", ignoreCase = true)) {
                                        Pref.GPSAlertwithVibration = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.GPSAlertwithVibration = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("WillRoomDBShareinLogin", ignoreCase = true)) {
                                        Pref.WillRoomDBShareinLogin = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.WillRoomDBShareinLogin = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("ShopScreenAftVisitRevisit", ignoreCase = true)) {
                                        Pref.ShopScreenAftVisitRevisit = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShopScreenAftVisitRevisit = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure!![i].Key.equals("IsShowNearByTeam", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowNearByTeam = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IsFeedbackAvailableInShop", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsFeedbackAvailableInShop = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IsFeedbackMandatoryforNewShop", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsFeedbackMandatoryforNewShop = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IsLoginSelfieRequired", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsLoginSelfieRequired = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IsAllowBreakageTracking", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsAllowBreakageTracking = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IsAllowBreakageTrackingunderTeam", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsAllowBreakageTrackingunderTeam = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IsRateEnabledforNewOrderScreenwithSize", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsRateEnabledforNewOrderScreenwithSize = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IgnoreNumberCheckwhileShopCreation", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IgnoreNumberCheckwhileShopCreation = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("Showdistributorwisepartyorderreport", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.Showdistributorwisepartyorderreport = response.getconfigure!![i].Value == "1"
                                        }
                                    }

                                    else if (response.getconfigure?.get(i)?.Key.equals("IsShowHomeLocationMap", ignoreCase = true)) {
                                        Pref.IsShowHomeLocationMap = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowHomeLocationMap =
                                                response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("ShowAttednaceClearmenu", ignoreCase = true)) {
                                        Pref.ShowAttednaceClearmenu = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowAttednaceClearmenu= response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsBeatRouteReportAvailableinTeam", ignoreCase = true)) {
                                        Pref.IsBeatRouteReportAvailableinTeam = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsBeatRouteReportAvailableinTeam= response.getconfigure?.get(i)?.Value == "1"
                                        }

                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("OfflineShopAccuracy")) {
                                        try {
                                            Pref.OfflineShopAccuracy = response.getconfigure!![i].Value!!
                                            if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                Pref.OfflineShopAccuracy = response.getconfigure?.get(i)?.Value!!
                                            }
                                            if (Pref.OfflineShopAccuracy.length == 0 || Pref.OfflineShopAccuracy.equals("")) {
                                                Pref.OfflineShopAccuracy = "700"
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            Pref.OfflineShopAccuracy = "700"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("GPSNetworkIntervalMins", ignoreCase = true)) {
                                        try{
                                            Pref.GPSNetworkIntervalMins =response.getconfigure!![i].Value!!
                                        }catch (e: Exception) {
                                            e.printStackTrace()
                                            Pref.GPSNetworkIntervalMins = "0"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("ShowAutoRevisitInAppMenu", ignoreCase = true)) {
                                        Pref.ShowAutoRevisitInAppMenu = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowAutoRevisitInAppMenu = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IsJointVisitEnable", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsJointVisitEnable = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IsShowAllEmployeeforJointVisit", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowAllEmployeeforJointVisit = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IsAllowClickForVisit", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsAllowClickForVisit = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsShowTypeInRegistration", ignoreCase = true)) {
                                        Pref.IsShowTypeInRegistration = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowTypeInRegistration = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("UpdateUserName", ignoreCase = true)) {
                                        Pref.UpdateUserName =
                                            response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.UpdateUserName =
                                                response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsAllowClickForPhotoRegister", ignoreCase = true)) {
                                        Pref.IsAllowClickForPhotoRegister =
                                            response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsAllowClickForPhotoRegister =
                                                response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsFaceRecognitionOnEyeblink", ignoreCase = true)) {
                                        Pref.IsFaceRecognitionOnEyeblink = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsFaceRecognitionOnEyeblink = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                        CustomStatic.IsFaceRecognitionOnEyeblink = Pref.IsFaceRecognitionOnEyeblink
                                    }else if (response.getconfigure!![i].Key.equals("PartyUpdateAddrMandatory", ignoreCase = true)) { // 2.0 AppV 4.0.6
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.PartyUpdateAddrMandatory = response.getconfigure!![i].Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsAttendVisitShowInDashboard", ignoreCase = true)) { // 2.0 DashboardFragment  AppV 4.0.6
                                        Pref.IsAttendVisitShowInDashboard = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsAttendVisitShowInDashboard = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("CommonAINotification", ignoreCase = true)) {// 1.0  AppV 4.0.6 LocationFuzedService
                                        Pref.CommonAINotification = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.CommonAINotification = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsIMEICheck", ignoreCase = true)) {//1.0 LoginActivity  AppV 4.0.6
                                        Pref.IsIMEICheck = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsIMEICheck = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("Show_App_Logout_Notification", ignoreCase = true)) {//2.0 LocationFuzedService  AppV 4.0.6
                                        Pref.Show_App_Logout_Notification = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.Show_App_Logout_Notification = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure!![i].Key.equals("AllowProfileUpdate", ignoreCase = true)) {// 1.0 MyProfileFragment  AppV 4.0.6
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.AllowProfileUpdate = response.getconfigure!![i].Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("ShowAutoRevisitInDashboard", ignoreCase = true)) {
                                        Pref.ShowAutoRevisitInDashboard = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowAutoRevisitInDashboard = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    // 3.0  AppV 4.0.6  DashboardActivity
                                    else if (response.getconfigure!![i].Key.equals("ShowTotalVisitAppMenu", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowTotalVisitAppMenu = response.getconfigure!![i].Value == "1"
                                        }
                                    }

                                    else if (response.getconfigure!![i].Key.equals("IsMultipleContactEnableforShop", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsMultipleContactEnableforShop = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure!![i].Key.equals("IsContactPersonSelectionRequiredinRevisit", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsContactPersonSelectionRequiredinRevisit = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    // 3.0  AppV 4.0.6 Addquot work
                                    else if (response.getconfigure!![i].Key.equals("IsContactPersonRequiredinQuotation", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsContactPersonRequiredinQuotation = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    //end rev 3.0
                                    // 5.0 DashboardFragment  AppV 4.0.6  IsAllowShopStatusUpdate
                                    else if (response.getconfigure!![i].Key.equals("IsAllowShopStatusUpdate", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsAllowShopStatusUpdate = response.getconfigure!![i].Value == "1"
                                        }
                                    }
                                    //end rev 5.0
                                    else if (response.getconfigure!![i].Key.equals("IsShowBeatInMenu", ignoreCase = true)) {
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowBeatInMenu = response.getconfigure!![i].Value == "1"
                                        }
                                    }

                                    else if (response.getconfigure?.get(i)?.Key.equals("IsAssignedDDAvailableForAllUser", ignoreCase = true)) {//10.0 DashboradFrag  AppV 4.0.8 mantis 0025780
                                        Pref.IsAssignedDDAvailableForAllUser = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsAssignedDDAvailableForAllUser = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    //end rev 10.0

                                    else if (response.getconfigure?.get(i)?.Key.equals("IsShowEmployeePerformance", ignoreCase = true)) {//11.0 DashboradFrag  AppV 4.0.8 mantis 25860
                                        Pref.IsShowEmployeePerformance = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowEmployeePerformance = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    //end rev 11.0

                                    else if (response.getconfigure?.get(i)?.Key.equals("IsMenuShowAIMarketAssistant", ignoreCase = true)) {
                                        Pref.IsMenuShowAIMarketAssistant = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsMenuShowAIMarketAssistant = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    //Begin 17.0 DashboardFragment v 4.1.6 Suman 13/07/2023 mantis 26555 Usersettings
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsUsbDebuggingRestricted", ignoreCase = true)) {
                                        Pref.IsUsbDebuggingRestricted = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsUsbDebuggingRestricted = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    //End 17.0 DashboardFragment v 4.1.6 Suman 13/07/2023 mantis 26555 Usersettings

                                    else if (response.getconfigure?.get(i)?.Key.equals("IsDisabledUpdateAddress", ignoreCase = true)) {
                                        Pref.IsDisabledUpdateAddress = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsDisabledUpdateAddress = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsShowMenuCRMContacts", ignoreCase = true)) {
                                        Pref.IsShowMenuCRMContacts = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowMenuCRMContacts = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsCallLogHistoryActivated", ignoreCase = true)) {
                                        Pref.IsCallLogHistoryActivated = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsCallLogHistoryActivated = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    //begin mantis id 0027255 AdditionalInfoRequiredForTimelines functionality Puja 20-02-2024
                                    else if (response.getconfigure?.get(i)?.Key.equals("AdditionalInfoRequiredForTimelines", ignoreCase = true)) {
                                        Pref.AdditionalInfoRequiredForTimelines = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.AdditionalInfoRequiredForTimelines = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    //end mantis id 0027255 AdditionalInfoRequiredForTimelines functionality Puja 20-02-2024

                                    //begin mantis id 0027389 AdditionalinfoRequiredforContactListing functionality Puja 23-04-2024
                                    else if (response.getconfigure?.get(i)?.Key.equals("AdditionalinfoRequiredforContactListing", ignoreCase = true)) {
                                        Pref.AdditionalinfoRequiredforContactListing = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.AdditionalinfoRequiredforContactListing = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    //end mantis id 0027389 AdditionalinfoRequiredforContactListing functionality Puja 23-04-2024
                                    //begin mantis id 0027389 AdditionalinfoRequiredforContactAdd functionality Puja 23-04-2024
                                    else if (response.getconfigure?.get(i)?.Key.equals("AdditionalinfoRequiredforContactAdd", ignoreCase = true)) {
                                        Pref.AdditionalinfoRequiredforContactAdd = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.AdditionalinfoRequiredforContactAdd = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    //end mantis id 0027389 AdditionalinfoRequiredforContactAdd functionality Puja 23-04-2024
                                    else if (response.getconfigure?.get(i)?.Key.equals("ContactAddresswithGeofence", ignoreCase = true)) {
                                        Pref.ContactAddresswithGeofence = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ContactAddresswithGeofence = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsShowOtherInfoinActivity", ignoreCase = true)) {
                                        Pref.IsShowOtherInfoinActivity = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowOtherInfoinActivity = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    else if (response.getconfigure?.get(i)?.Key.equals("ShowUserwisePartyWithGeoFence", ignoreCase = true)) {
                                        Pref.ShowUserwisePartyWithGeoFence = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowUserwisePartyWithGeoFence = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("ShowUserwisePartyWithCreateOrder", ignoreCase = true)) {
                                        Pref.ShowUserwisePartyWithCreateOrder = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.ShowUserwisePartyWithCreateOrder = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsRouteUpdateForShopUser", ignoreCase = true)) {
                                        Pref.IsRouteUpdateForShopUser = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsRouteUpdateForShopUser = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsCRMPhonebookSyncEnable", ignoreCase = true)) {
                                        Pref.IsCRMPhonebookSyncEnable = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsCRMPhonebookSyncEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsCRMSchedulerEnable", ignoreCase = true)) {
                                        Pref.IsCRMSchedulerEnable = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsCRMSchedulerEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsCRMAddEnable", ignoreCase = true)) {
                                        Pref.IsCRMAddEnable = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsCRMAddEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }else if (response.getconfigure?.get(i)?.Key.equals("IsCRMEditEnable", ignoreCase = true)) {
                                        Pref.IsCRMEditEnable = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsCRMEditEnable = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }
                                    else if (response.getconfigure?.get(i)?.Key.equals("IsShowCRMOpportunity", ignoreCase = true)) {
                                        Pref.IsShowCRMOpportunity = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsShowCRMOpportunity = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    else if (response.getconfigure?.get(i)?.Key.equals("IsEditEnableforOpportunity", ignoreCase = true)) {
                                        Pref.IsEditEnableforOpportunity = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsEditEnableforOpportunity = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                    else if (response.getconfigure?.get(i)?.Key.equals("IsDeleteEnableforOpportunity", ignoreCase = true)) {
                                        Pref.IsDeleteEnableforOpportunity = response.getconfigure!![i].Value == "1"
                                        if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                            Pref.IsDeleteEnableforOpportunity = response.getconfigure?.get(i)?.Value == "1"
                                        }
                                    }

                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    progress_wheel.stopSpinning()
                    getConfigFetchApi()
                }, { error ->
                    error.printStackTrace()
                    tv_syncAll.isEnabled=true
                    progress_wheel.stopSpinning()
                    getConfigFetchApi()
                })
        )
    }

    @SuppressLint("RestrictedApi")
    private fun getConfigFetchApi() {
        val repository = ConfigFetchRepoProvider.provideConfigFetchRepository()
        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
            repository.configFetch()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val configResponse = result as ConfigFetchResponseModel
                    progress_wheel.stopSpinning()
                    if (configResponse.status == NetworkConstant.SUCCESS) {

                        if (!TextUtils.isEmpty(configResponse.min_distance))
                            AppUtils.minDistance = configResponse.min_distance!!

                        if (!TextUtils.isEmpty(configResponse.max_distance))
                            AppUtils.maxDistance = configResponse.max_distance!!

                        if (!TextUtils.isEmpty(configResponse.max_accuracy))
                            AppUtils.maxAccuracy = configResponse.max_accuracy!!

                        if (!TextUtils.isEmpty(configResponse.min_accuracy))
                        //AppUtils.minAccuracy = configResponse.min_accuracy!!
                            Pref.minAccuracy = configResponse.min_accuracy!!

                        /*if (!TextUtils.isEmpty(configResponse.idle_time))
                            AppUtils.idle_time = configResponse.idle_time!!*/

                        if (configResponse.willStockShow != null)
                            Pref.willStockShow = configResponse.willStockShow!!

                        // From Hahnemann
                        if (configResponse.isPrimaryTargetMandatory != null)
                            Pref.isPrimaryTargetMandatory = configResponse.isPrimaryTargetMandatory!!

                        if (configResponse.isRevisitCaptureImage != null)
                            Pref.isRevisitCaptureImage = configResponse.isRevisitCaptureImage!!

                        if (configResponse.isShowAllProduct != null)
                            Pref.isShowAllProduct = configResponse.isShowAllProduct!!

                        if (configResponse.isStockAvailableForAll != null)
                            Pref.isStockAvailableForAll = configResponse.isStockAvailableForAll!!

                        if (configResponse.isStockAvailableForPopup != null)
                            Pref.isStockAvailableForPopup = configResponse.isStockAvailableForPopup!!

                        if (configResponse.isOrderAvailableForPopup != null)
                            Pref.isOrderAvailableForPopup = configResponse.isOrderAvailableForPopup!!

                        if (configResponse.isCollectionAvailableForPopup != null)
                            Pref.isCollectionAvailableForPopup = configResponse.isCollectionAvailableForPopup!!

                        if (configResponse.isDDFieldEnabled != null)
                            Pref.isDDFieldEnabled = configResponse.isDDFieldEnabled!!

                        if (!TextUtils.isEmpty(configResponse.maxFileSize))
                            Pref.maxFileSize = configResponse.maxFileSize!!

                        if (configResponse.willKnowYourStateShow != null)
                            Pref.willKnowYourStateShow = configResponse.willKnowYourStateShow!!

                        if (configResponse.willAttachmentCompulsory != null)
                            Pref.willAttachmentCompulsory = configResponse.willAttachmentCompulsory!!

                        if (configResponse.canAddBillingFromBillingList != null)
                            Pref.canAddBillingFromBillingList = configResponse.canAddBillingFromBillingList!!

                        if (!TextUtils.isEmpty(configResponse.allPlanListHeaderText))
                            Pref.allPlanListHeaderText = configResponse.allPlanListHeaderText!!

                        if (configResponse.willSetYourTodaysTargetVisible != null)
                            Pref.willSetYourTodaysTargetVisible = configResponse.willSetYourTodaysTargetVisible!!

                        if (!TextUtils.isEmpty(configResponse.attendenceAlertHeading))
                            Pref.attendenceAlertHeading = configResponse.attendenceAlertHeading!!

                        if (!TextUtils.isEmpty(configResponse.attendenceAlertText))
                            Pref.attendenceAlertText = configResponse.attendenceAlertText!!

                        if (!TextUtils.isEmpty(configResponse.meetingText))
                            Pref.meetingText = configResponse.meetingText!!

                        if (!TextUtils.isEmpty(configResponse.meetingDistance))
                            Pref.meetingDistance = configResponse.meetingDistance!!

                        if (configResponse.isActivatePJPFeature != null)
                            Pref.isActivatePJPFeature = configResponse.isActivatePJPFeature!!

                        if (configResponse.willReimbursementShow != null)
                            Pref.willReimbursementShow = configResponse.willReimbursementShow!!

                        if (!TextUtils.isEmpty(configResponse.updateBillingText))
                            Pref.updateBillingText = configResponse.updateBillingText!!

                        if (configResponse.isRateOnline != null)
                            Pref.isRateOnline = configResponse.isRateOnline!!

                        if (!TextUtils.isEmpty(configResponse.ppText))
                            Pref.ppText = configResponse.ppText

                        if (!TextUtils.isEmpty(configResponse.ddText))
                            Pref.ddText = configResponse.ddText

                        if (!TextUtils.isEmpty(configResponse.shopText))
                            Pref.shopText = configResponse.shopText

                        if (configResponse.isCustomerFeatureEnable != null)
                            Pref.isCustomerFeatureEnable = configResponse.isCustomerFeatureEnable!!

                        if (configResponse.isAreaVisible != null)
                            Pref.isAreaVisible = configResponse.isAreaVisible!!

                        if (!TextUtils.isEmpty(configResponse.cgstPercentage))
                            Pref.cgstPercentage = configResponse.cgstPercentage

                        if (!TextUtils.isEmpty(configResponse.sgstPercentage))
                            Pref.sgstPercentage = configResponse.sgstPercentage

                        if (!TextUtils.isEmpty(configResponse.tcsPercentage))
                            Pref.tcsPercentage = configResponse.tcsPercentage

                        if (!TextUtils.isEmpty(configResponse.docAttachmentNo))
                            Pref.docAttachmentNo = configResponse.docAttachmentNo

                        if (!TextUtils.isEmpty(configResponse.chatBotMsg))
                            Pref.chatBotMsg = configResponse.chatBotMsg

                        if (!TextUtils.isEmpty(configResponse.contactMail))
                            Pref.contactMail = configResponse.contactMail

                        if (configResponse.isVoiceEnabledForAttendanceSubmit != null)
                            Pref.isVoiceEnabledForAttendanceSubmit = configResponse.isVoiceEnabledForAttendanceSubmit!!

                        if (configResponse.isVoiceEnabledForOrderSaved != null)
                            Pref.isVoiceEnabledForOrderSaved = configResponse.isVoiceEnabledForOrderSaved!!

                        if (configResponse.isVoiceEnabledForInvoiceSaved != null)
                            Pref.isVoiceEnabledForInvoiceSaved = configResponse.isVoiceEnabledForInvoiceSaved!!

                        if (configResponse.isVoiceEnabledForCollectionSaved != null)
                            Pref.isVoiceEnabledForCollectionSaved = configResponse.isVoiceEnabledForCollectionSaved!!

                        if (configResponse.isVoiceEnabledForHelpAndTipsInBot != null)
                            Pref.isVoiceEnabledForHelpAndTipsInBot = configResponse.isVoiceEnabledForHelpAndTipsInBot!!

                        if (configResponse.GPSAlert != null)
                            Pref.GPSAlertGlobal = configResponse.GPSAlert!!

                        //02-11-2021
                        if (configResponse.IsDuplicateShopContactnoAllowedOnline != null)
                            Pref.IsDuplicateShopContactnoAllowedOnline = configResponse.IsDuplicateShopContactnoAllowedOnline!!


                        /*26-11-2021*/
                        if (configResponse.BatterySetting != null)
                            Pref.BatterySettingGlobal = configResponse.BatterySetting!!

                        if (configResponse.PowerSaverSetting != null)
                            Pref.PowerSaverSettingGlobal = configResponse.PowerSaverSetting!!

                        /*1-12-2021*/
                        if (configResponse.IsnewleadtypeforRuby != null)
                            Pref.IsnewleadtypeforRuby = configResponse.IsnewleadtypeforRuby!!


                        /*16-12-2021*/
                        if (configResponse.IsReturnActivatedforPP != null)
                            Pref.IsReturnActivatedforPP = configResponse.IsReturnActivatedforPP!!

                        if (configResponse.IsReturnActivatedforDD != null)
                            Pref.IsReturnActivatedforDD = configResponse.IsReturnActivatedforDD!!

                        if (configResponse.IsReturnActivatedforSHOP != null)
                            Pref.IsReturnActivatedforSHOP = configResponse.IsReturnActivatedforSHOP!!
                        /*06-01-2022*/
                        if (configResponse.MRPInOrder != null)
                            Pref.MRPInOrderGlobal = configResponse.MRPInOrder!!
                        if (configResponse.FaceRegistrationFrontCamera != null)
                            Pref.FaceRegistrationOpenFrontCamera = configResponse.FaceRegistrationFrontCamera!!

                        //if (configResponse.SqMtrRateCalculationforQuotEuro != null)
                        try{
                            Pref.SqMtrRateCalculationforQuotEuro = configResponse.SqMtrRateCalculationforQuotEuro!!.toString()
                        }catch (ex:Exception){
                            Pref.SqMtrRateCalculationforQuotEuro = "0.0"
                        }
                        /*17-02-2022*/
                        if (configResponse.NewQuotationRateCaption != null)
                            Pref.NewQuotationRateCaption = configResponse.NewQuotationRateCaption!!

                        if (configResponse.NewQuotationShowTermsAndCondition != null)
                            Pref.NewQuotationShowTermsAndCondition = configResponse.NewQuotationShowTermsAndCondition!!

                        if (configResponse.IsCollectionEntryConsiderOrderOrInvoice != null)
                            Pref.IsCollectionEntryConsiderOrderOrInvoice = configResponse.IsCollectionEntryConsiderOrderOrInvoice!!

                        if (!TextUtils.isEmpty(configResponse.contactNameText))
                            Pref.contactNameText = configResponse.contactNameText

                        if (!TextUtils.isEmpty(configResponse.contactNumberText))
                            Pref.contactNumberText = configResponse.contactNumberText

                        if (!TextUtils.isEmpty(configResponse.emailText))
                            Pref.emailText = configResponse.emailText

                        if (!TextUtils.isEmpty(configResponse.dobText))
                            Pref.dobText = configResponse.dobText

                        if (!TextUtils.isEmpty(configResponse.dateOfAnniversaryText))
                            Pref.dateOfAnniversaryText = configResponse.dateOfAnniversaryText

                        if (configResponse.ShopScreenAftVisitRevisit != null)
                            Pref.ShopScreenAftVisitRevisitGlobal = configResponse.ShopScreenAftVisitRevisit!!

                        if (configResponse.IsSurveyRequiredforNewParty != null)
                            Pref.IsSurveyRequiredforNewParty = configResponse.IsSurveyRequiredforNewParty!!

                        if (configResponse.IsSurveyRequiredforDealer != null)
                            Pref.IsSurveyRequiredforDealer = configResponse.IsSurveyRequiredforDealer!!

                        if (configResponse.IsShowHomeLocationMap != null)
                            Pref.IsShowHomeLocationMapGlobal = configResponse.IsShowHomeLocationMap!!

                        if (configResponse.IsBeatRouteAvailableinAttendance != null)
                            Pref.IsBeatRouteAvailableinAttendance = configResponse.IsBeatRouteAvailableinAttendance!!

                        if (configResponse.IsAllBeatAvailable != null)
                            Pref.IsAllBeatAvailableforParty = configResponse.IsAllBeatAvailable!!

                        if (configResponse.BeatText != null)
                            Pref.beatText=configResponse.BeatText!!

                        if (configResponse.TodaysTaskText != null)
                            Pref.TodaysTaskText=configResponse.TodaysTaskText!!

                        if (configResponse.IsDistributorSelectionRequiredinAttendance != null)
                            Pref.IsDistributorSelectionRequiredinAttendance = configResponse.IsDistributorSelectionRequiredinAttendance!!

                        if (configResponse.IsAllowNearbyshopWithBeat != null)
                            Pref.IsAllowNearbyshopWithBeat = configResponse.IsAllowNearbyshopWithBeat!!

                        if (configResponse.IsGSTINPANEnableInShop != null)
                            Pref.IsGSTINPANEnableInShop = configResponse.IsGSTINPANEnableInShop!!

                        if (configResponse.IsMultipleImagesRequired != null)
                            Pref.IsMultipleImagesRequired = configResponse.IsMultipleImagesRequired!!

                        if (configResponse.IsALLDDRequiredforAttendance != null)
                            Pref.IsALLDDRequiredforAttendance = configResponse.IsALLDDRequiredforAttendance!!

                        if (configResponse.IsShowNewOrderCart != null)
                            Pref.IsShowNewOrderCart = configResponse.IsShowNewOrderCart!!

                        if (configResponse.IsmanualInOutTimeRequired != null)
                            Pref.IsmanualInOutTimeRequired = configResponse.IsmanualInOutTimeRequired!!

                        if (!TextUtils.isEmpty(configResponse.surveytext))
                            Pref.surveytext = configResponse.surveytext

                        if (configResponse.IsDiscountInOrder != null)
                            Pref.IsDiscountInOrder = configResponse.IsDiscountInOrder!!

                        if (configResponse.IsViewMRPInOrder != null)
                            Pref.IsViewMRPInOrder = configResponse.IsViewMRPInOrder!!

                        if (configResponse.IsShowStateInTeam != null)
                            Pref.IsShowStateInTeam = configResponse.IsShowStateInTeam!!

                        if (configResponse.IsShowBranchInTeam != null)
                            Pref.IsShowBranchInTeam = configResponse.IsShowBranchInTeam!!

                        if (configResponse.IsShowDesignationInTeam != null)
                            Pref.IsShowDesignationInTeam = configResponse.IsShowDesignationInTeam!!

                        if (configResponse.IsShowInPortalManualPhotoRegn != null)
                            Pref.IsShowInPortalManualPhotoRegn = configResponse.IsShowInPortalManualPhotoRegn!!

                        if (configResponse.IsAttendVisitShowInDashboard != null) // 2.0 DashboardFragment  AppV 4.0.6
                            Pref.IsAttendVisitShowInDashboardGlobal = configResponse.IsAttendVisitShowInDashboard!!

                        if (configResponse.Show_App_Logout_Notification != null)//2.0 LocationFuzedService  AppV 4.0.6
                            Pref.Show_App_Logout_Notification_Global = configResponse.Show_App_Logout_Notification!!

                        if (configResponse.IsBeatAvailable != null)
                            Pref.IsBeatAvailable = configResponse.IsBeatAvailable!!

                        // 7.0 AppV 4.0.6 mantis 25623
                        if (configResponse.IsDiscountEditableInOrder != null)
                            Pref.IsDiscountEditableInOrder = configResponse.IsDiscountEditableInOrder!!

                        // 6.0 LoginActivity AppV 4.0.6 mantis 25607
                        if (configResponse.isExpenseFeatureAvailable != null)
                            Pref.isExpenseFeatureAvailable = configResponse.isExpenseFeatureAvailable!!

                        // 7.0 LoginActivity AppV 4.0.6 mantis 25637
                        if (configResponse.IsRouteStartFromAttendance != null)
                            Pref.IsRouteStartFromAttendance = configResponse.IsRouteStartFromAttendance!!

                        // 3.0 Pref  AppV 4.0.7 Suman    10/03/2023 Pdf generation settings wise  mantis 25650
                        if (configResponse.IsShowQuotationFooterforEurobond != null)
                            Pref.IsShowQuotationFooterforEurobond = configResponse.IsShowQuotationFooterforEurobond!!
                        if (configResponse.IsShowOtherInfoinShopMaster != null)
                            Pref.IsShowOtherInfoinShopMaster = configResponse.IsShowOtherInfoinShopMaster!!

                        if (configResponse.IsAllowZeroRateOrder != null)
                            Pref.IsAllowZeroRateOrder = configResponse.IsAllowZeroRateOrder!!

                        // 4.0 Pref  AppV 4.0.7 Suman    23/03/2023 ShowApproxDistanceInNearbyShopList Show approx distance in nearby + shopmaster  mantis 0025742
                        if (configResponse.ShowApproxDistanceInNearbyShopList != null)
                            Pref.ShowApproxDistanceInNearbyShopList = configResponse.ShowApproxDistanceInNearbyShopList!!
                        //10.0 dashboardFrag  AppV 4.0.8  mantis 0025780
                        if (configResponse.IsAssignedDDAvailableForAllUser != null)
                            Pref.IsAssignedDDAvailableForAllUserGlobal = configResponse.IsAssignedDDAvailableForAllUser!!
                        //11.0 dashboardFrag  AppV 4.0.8  mantis 25860
                        if (configResponse.IsShowEmployeePerformance != null)
                            Pref.IsShowEmployeePerformanceGlobal = configResponse.IsShowEmployeePerformance!!
                        // end rev 11.0

                        // 12.0  dashboardFrag AppV 4.0.8 Saheli    08/05/2023  26023
                        if (configResponse.IsTaskManagementAvailable != null)
                            Pref.IsTaskManagementAvailable = configResponse.IsTaskManagementAvailable!!
                        // end rev 12.0

                        if (configResponse.IsShowPrivacyPolicyInMenu != null)
                            Pref.IsShowPrivacyPolicyInMenu = configResponse.IsShowPrivacyPolicyInMenu!!

                        if (configResponse.IsAttendanceCheckedforExpense != null)
                            Pref.IsAttendanceCheckedforExpense = configResponse.IsAttendanceCheckedforExpense!!
                        if (configResponse.IsShowLocalinExpense != null)
                            Pref.IsShowLocalinExpense = configResponse.IsShowLocalinExpense!!
                        if (configResponse.IsShowOutStationinExpense != null)
                            Pref.IsShowOutStationinExpense = configResponse.IsShowOutStationinExpense!!
                        if (configResponse.IsSingleDayTAApplyRestriction != null)
                            Pref.IsSingleDayTAApplyRestriction = configResponse.IsSingleDayTAApplyRestriction!!
                        if (configResponse.IsTAAttachment1Mandatory != null)
                            Pref.IsTAAttachment1Mandatory = configResponse.IsTAAttachment1Mandatory!!
                        if (configResponse.IsTAAttachment2Mandatory != null)
                            Pref.IsTAAttachment2Mandatory = configResponse.IsTAAttachment2Mandatory!!
                        if (configResponse.NameforConveyanceAttachment1 != null)
                            Pref.NameforConveyanceAttachment1 = configResponse.NameforConveyanceAttachment1!!
                        if (configResponse.NameforConveyanceAttachment2 != null)
                            Pref.NameforConveyanceAttachment2 = configResponse.NameforConveyanceAttachment2!!

                        // 13.0  dashboardFrag AppV 4.0.8 Saheli    12/05/2023  0026101
                        if (configResponse.IsAttachmentAvailableForCurrentStock != null)
                            Pref.IsAttachmentAvailableForCurrentStock = configResponse.IsAttachmentAvailableForCurrentStock!!
                        // end rev 13.0

                        if (configResponse.IsShowReimbursementTypeInAttendance != null)
                            Pref.IsShowReimbursementTypeInAttendance = configResponse.IsShowReimbursementTypeInAttendance!!

                        //Begin 14.0  DashboardFragment AppV 4.0.8 Suman    19/05/2023 26163
                        if (configResponse.IsBeatPlanAvailable != null)
                            Pref.IsBeatPlanAvailable = configResponse.IsBeatPlanAvailable!!
                        //End of 14.0  DashboardFragment AppV 4.0.8 Suman    19/05/2023 26163

                        if (configResponse.IsUpdateVisitDataInTodayTable != null)
                            Pref.IsUpdateVisitDataInTodayTable = configResponse.IsUpdateVisitDataInTodayTable!!

                        //Begin 16.0 DashboardFragment v 4.1.6 Tufan 11/07/2023 mantis 26546 revisit sync time
                        if (configResponse.ShopSyncIntervalInMinutes != null)
                            Pref.ShopSyncIntervalInMinutes = configResponse.ShopSyncIntervalInMinutes!!
                        //End 16.0 DashboardFragment v 4.1.6 Tufan 11/07/2023 mantis 26546 revisit sync time

                        if (configResponse.IsShowWhatsAppIconforVisit != null)
                            Pref.IsShowWhatsAppIconforVisit = configResponse.IsShowWhatsAppIconforVisit!!
                        if (configResponse.IsAutomatedWhatsAppSendforRevisit != null)
                            Pref.IsAutomatedWhatsAppSendforRevisit = configResponse.IsAutomatedWhatsAppSendforRevisit!!

                        if (configResponse.IsAllowBackdatedOrderEntry != null)
                            Pref.IsAllowBackdatedOrderEntry = configResponse.IsAllowBackdatedOrderEntry!!
                        try{
                            Pref.Order_Past_Days = configResponse.Order_Past_Days!!.toString()
                        }catch (ex:Exception){
                            Pref.Order_Past_Days = "0"
                        }
                        //Begin 15.0 Pref v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product
                        if (configResponse.Show_distributor_scheme_with_Product != null)
                            Pref.Show_distributor_scheme_with_Product = configResponse.Show_distributor_scheme_with_Product!!
                        //End 15.0 Pref v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product

                        //Begin 16.0 Pref v 4.1.6 Tufan 07/09/2023 mantis 26785 Multi visit Interval in Minutes Against the Same Shop
                        if (configResponse.MultiVisitIntervalInMinutes != null)
                            Pref.MultiVisitIntervalInMinutes = configResponse.MultiVisitIntervalInMinutes!!
                        //End 16.0 Pref v 4.1.6 Tufan 07/09/2023 mantis 26785 Multi visit Interval in Minutes Against the Same Shop

                        //Begin v 4.1.6 Tufan 21/09/2023 mantis 26812 AND 26813  FSSAI Lic No and GSTINPANMandatoryforSHOPTYPE4 In add shop page edit
                        if (configResponse.GSTINPANMandatoryforSHOPTYPE4 != null)
                            Pref.GSTINPANMandatoryforSHOPTYPE4 = configResponse.GSTINPANMandatoryforSHOPTYPE4!!
                        if (configResponse.FSSAILicNoEnableInShop != null)
                            Pref.FSSAILicNoEnableInShop = configResponse.FSSAILicNoEnableInShop!!
                        if (configResponse.FSSAILicNoMandatoryInShop4 != null)
                            Pref.FSSAILicNoMandatoryInShop4 = configResponse.FSSAILicNoMandatoryInShop4!!
                        //Edit v 4.1.6 Tufan 21/09/2023 mantis 26812 AND 26813  FSSAI Lic No and GSTINPANMandatoryforSHOPTYPE4 In add shop page edit

                        //Begin Puja 16.11.23 mantis-0026997 //

                        if (configResponse.isLeadContactNumber != null)
                            Pref.isLeadContactNumber = configResponse.isLeadContactNumber!!

                        if (configResponse.isModelEnable != null)
                            Pref.isModelEnable = configResponse.isModelEnable!!

                        if (configResponse.isPrimaryApplicationEnable != null)
                            Pref.isPrimaryApplicationEnable = configResponse.isPrimaryApplicationEnable!!

                        if (configResponse.isSecondaryApplicationEnable != null)
                            Pref.isSecondaryApplicationEnable = configResponse.isSecondaryApplicationEnable!!

                        if (configResponse.isBookingAmount != null)
                            Pref.isBookingAmount = configResponse.isBookingAmount!!

                        if (configResponse.isLeadTypeEnable != null)
                            Pref.isLeadTypeEnable = configResponse.isLeadTypeEnable!!

                        if (configResponse.isStageEnable != null)
                            Pref.isStageEnable = configResponse.isStageEnable!!

                        if (configResponse.isFunnelStageEnable != null)
                            Pref.isFunnelStageEnable = configResponse.isFunnelStageEnable!!
                        //End Puja 16.11.23 mantis-0026997 //
                        if (configResponse.IsGPSRouteSync != null)
                            Pref.IsGPSRouteSync = configResponse.IsGPSRouteSync!!
                        if (configResponse.IsSyncBellNotificationInApp != null)
                            Pref.IsSyncBellNotificationInApp = configResponse.IsSyncBellNotificationInApp!!
                        if (configResponse.IsShowCustomerLocationShare != null)
                            Pref.IsShowCustomerLocationShare = configResponse.IsShowCustomerLocationShare!!
                        //begin mantis id 0027255 AdditionalInfoRequiredForTimelines functionality Puja 21-02-2024
                        if (configResponse.AdditionalInfoRequiredForTimelines != null)
                            Pref.AdditionalInfoRequiredForTimelines = configResponse.AdditionalInfoRequiredForTimelines!!
                        //end mantis id 0027255 AdditionalInfoRequiredForTimelines functionality Puja 21-02-2024

                        //begin mantis id 0027279 ShowPartyWithGeoFence functionality Puja 01-03-2024
                        if (configResponse.ShowPartyWithGeoFence != null)
                            Pref.ShowPartyWithGeoFence = configResponse.ShowPartyWithGeoFence!!

                        //end mantis id 0027279 ShowPartyWithGeoFence functionality Puja 01-03-2024

                        //begin mantis id 0027285 ShowPartyWithCreateOrder functionality Puja 01-03-2024
                        if (configResponse.ShowPartyWithCreateOrder != null)
                            Pref.ShowPartyWithCreateOrder = configResponse.ShowPartyWithCreateOrder!!
                        //end mantis id 0027285 ShowPartyWithCreateOrder functionality Puja 01-03-2024

                        //begin mantis id 0027282 Allow_past_days_for_apply_reimbursement functionality Puja 01-03-2024 v4.2.6
                        if (configResponse.Allow_past_days_for_apply_reimbursement != null) {
                            Pref.Allow_past_days_for_apply_reimbursement =
                                configResponse.Allow_past_days_for_apply_reimbursement.toString()
                        }else{
                            Pref.Allow_past_days_for_apply_reimbursement = ""
                        }
                        //end mantis id 0027282 Allow_past_days_for_apply_reimbursement functionality Puja 01-03-2024  v4.2.6

                        //begin mantis id 0027298 IsShowLeaderBoard functionality Puja 12-03-2024 v4.2.6
                        if (configResponse.IsShowLeaderBoard != null)
                            Pref.IsShowLeaderBoard = configResponse.IsShowLeaderBoard!!
                        //end mantis id 0027298 IsShowLeaderBoard functionality Puja 12-03-2024  v4.2.6

                        //begin mantis id 0027432 loc_k functionality Puja 08-05-2024 v4.2.7
                        if (configResponse.loc_k != null)
                            Pref.loc_k = configResponse.loc_k!!
                        //end mantis id 0027432 loc_k functionality Puja 08-05-2024  v4.2.7

                        //begin mantis id 0027432 firebase_k functionality Puja 08-05-2024 v4.2.7
                        if (configResponse.firebase_k != null)
                            Pref.firebase_k = "key="+configResponse.firebase_k!!
                        //end mantis id 0027432 firebase_k functionality Puja 08-05-2024  v4.2.7
                    }
                    BaseActivity.isApiInitiated = false

                    if(Pref.IsBeatAvailable==false){
                        Pref.IsAllBeatAvailableforParty = false
                        Pref.IsBeatRouteAvailableinAttendance = false
                        Pref.IsBeatRouteReportAvailableinTeam = false
                        Pref.isShowShopBeatWise = false
                        Pref.isShowBeatGroup = false
                        Pref.IsShowBeatInMenu = false
                    }
                    if(Pref.isCustomerFeatureEnable==false){
                        Pref.isLeadContactNumber = false
                        Pref.isModelEnable = false
                        Pref.isPrimaryApplicationEnable = false
                        Pref.isSecondaryApplicationEnable = false
                        Pref.isBookingAmount = false
                        Pref.isLeadTypeEnable = false
                        Pref.isStageEnable = false
                        Pref.isFunnelStageEnable = false
                    }
                    if(Pref.IsCRMPhonebookSyncEnable){
                        ivContactSync.visibility=View.VISIBLE
                        viewPhoneBook.visibility=View.VISIBLE
                    }else{
                        ivContactSync.visibility=View.GONE
                        viewPhoneBook.visibility=View.GONE
                    }
                    if(Pref.IsCRMSchedulerEnable){
                        iv_click_scheduler.visibility=View.VISIBLE
                        viewScheduler.visibility=View.VISIBLE
                    }else{
                        iv_click_scheduler.visibility=View.GONE
                        viewScheduler.visibility=View.GONE
                    }
                    if(Pref.IsCRMAddEnable){
                        mFab.visibility = View.VISIBLE
                        tv_empty_page_msg.visibility = View.VISIBLE
                        img_direction.visibility = View.VISIBLE
                    }else{
                        mFab.visibility = View.GONE
                        tv_empty_page_msg.visibility = View.GONE
                        img_direction.visibility = View.GONE
                    }

                    Handler().postDelayed(Runnable {
                        syncShopAll()
                    }, 600)


                }, { error ->
                    BaseActivity.isApiInitiated = false
                    error.printStackTrace()
                    tv_syncAll.isEnabled=true
                    progress_wheel.stopSpinning()
                    syncShopAll()
                })
        )
    }

    //whatsapp test code
    fun getwhatsappTemplateL(){
        var url = "https://server.gallabox.com/devapi/accounts/6604f779127ce0e3b59cdc96/whatsappTemplates"
        val strRrq :StringRequest = object :StringRequest(Request.Method.GET,url,Response.Listener<String> {response->
            var res = response
            try {
                var responseL = JSONArray(res)
                var templateNameL : ArrayList<String> = ArrayList()
                for(i in 0..responseL.length()-1){
                    var rObj = JSONObject(responseL.get(i).toString())
                    templateNameL.add(rObj.optString("name"))
                }
                showWhatsappDialog(templateNameL)
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        },Response.ErrorListener {
            var err = it
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["apiSecret"] = "71400c1d1e384da38ef5cd6852ce07bb"
                params["Content-Type"] = "application/json"
                params["apiKey"] = "664b23b402fc9498c685699d"
                return params
            }
        }

        var queue = Volley.newRequestQueue(mContext)
        queue.add(strRrq)
    }

    fun showWhatsappDialog(templateNameL:ArrayList<String>){
        simpleDialogProcess = Dialog(mContext)
        simpleDialogProcess.setCancelable(false)
        simpleDialogProcess.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialogProcess.setContentView(R.layout.dialog_multi_whatsapp)

        var tvHeader = simpleDialogProcess.findViewById(R.id.tv_dialog_multi_whats_header) as TextView
        var llSelectTemplate = simpleDialogProcess.findViewById(R.id.ll_whats_template) as LinearLayout
        var tvSelectTemplate = simpleDialogProcess.findViewById(R.id.tv_whats_template) as TextView
        var llSelectContact = simpleDialogProcess.findViewById(R.id.ll_select_contact) as LinearLayout
        var tvSelectContact = simpleDialogProcess.findViewById(R.id.tv_select_contact) as TextView
        var tv_message_ok = simpleDialogProcess.findViewById(R.id.tv_message_ok) as AppCustomTextView


        var contactTickL: ArrayList<ScheduleContactDtls> = ArrayList()
        var adapterScheduleContactName:AdapterScheduleContactName
        var finalL: ArrayList<ScheduleContactDtls> = ArrayList()

        llSelectTemplate.setOnClickListener {
            var genericL : ArrayList<CustomData> = ArrayList()
            for(i in 0..templateNameL.size-1){
                genericL.add(CustomData(templateNameL.get(i).toString(),templateNameL.get(i).toString()))
            }
            GenericDialog.newInstance("Source",genericL as ArrayList<CustomData>){
                tvSelectTemplate.text = it.name
            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        }


        llSelectContact.setOnClickListener {
            val contactDialog = Dialog(mContext)
            contactDialog.setCancelable(true)
            contactDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            contactDialog.setContentView(R.layout.dialog_cont_select)
            val rvContactL = contactDialog.findViewById(R.id.rv_dialog_cont_list) as RecyclerView
            val tvHeader = contactDialog.findViewById(R.id.tv_dialog_cont_sel_header) as TextView
            val submit = contactDialog.findViewById(R.id.tv_dialog_cont_list_submit) as TextView
            val et_contactNameSearch =
                contactDialog.findViewById(R.id.et_dialog_contact_search) as AppCustomEditText
            val cb_selectAll =
                contactDialog.findViewById(R.id.cb_dialog_cont_select_all) as CheckBox
            val iv_close =
                contactDialog.findViewById(R.id.iv_dialog_generic_list_close_icon) as ImageView
            tvHeader.text = if(contactTickL.size == 0) "Select Contact(s)" else "Selected Contact(s) : (${contactTickL.size})"
            iv_close.setOnClickListener {
                contactDialog.dismiss()
            }

            var contL = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcShopsByName() as ArrayList<AddShopDBModelEntity>
            for(i in 0..contL.size-1){
                finalL.add(ScheduleContactDtls(contL.get(i).shopName,contL.get(i).ownerContactNumber,contL.get(i).shop_id,false))
            }

            adapterScheduleContactName = AdapterScheduleContactName(mContext, finalL, object : AdapterScheduleContactName.onClick {
                override fun onTickUntick(obj: ScheduleContactDtls, isTick: Boolean) {
                    if (isTick) {
                        contactTickL.add(obj)
                        finalL.filter { it.contact_id.equals(obj.contact_id) }.first().isTick = true
                        tvHeader.text = "Selected Contact(s) : (${contactTickL.size})"
                    } else {
                        contactTickL.removeIf { it.contact_id.equals(obj.contact_id) }
                        finalL.filter { it.contact_id.equals(obj.contact_id) }.first().isTick = false
                        tvHeader.text = "Selected Contact(s) : (${contactTickL.size})"
                    }
                }
            }, {
                it
            })
            cb_selectAll.setOnCheckedChangeListener { compoundButton, b ->
                if (compoundButton.isChecked) {
                    adapterScheduleContactName.selectAll()
                    cb_selectAll.setText("Deselect All")
                } else {
                    adapterScheduleContactName.deselectAll()
                    cb_selectAll.setText("Select All")
                }
            }

            et_contactNameSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    adapterScheduleContactName!!.getFilter().filter(et_contactNameSearch.text.toString().trim())
                }
            })
            submit.setOnClickListener {
                if (contactTickL.size > 0) {
                    contactDialog.dismiss()
                    var nameText = ""
                    if(contactTickL.size==1){
                        nameText = contactTickL.get(0).contact_name
                    }else{
                        for(i in 0..contactTickL.size-1) {
                            nameText = nameText+contactTickL.get(i).contact_name+","
                        }
                    }
                    if (nameText.endsWith(",")) {
                        nameText = nameText.substring(0, nameText.length - 1);
                    }
                    tvSelectContact.setText(nameText)
                }
                else{
                    contactDialog.dismiss()
                    tvSelectContact.setText("")
                }
            }

            rvContactL.adapter = adapterScheduleContactName
            contactDialog.show()
        }

        tv_message_ok.setOnClickListener {
        for(l in 0..contactTickL.size-1){
            Handler().postDelayed(Runnable {
                gallaboxApiTest("91"+contactTickL.get(l).contact_number,contactTickL.get(l).contact_name)
            }, 1000)
        }
            simpleDialogProcess.dismiss()
        }

        simpleDialogProcess.show()
    }

    fun gallaboxApiTest(sendingNo:String,sendingName:String){
        val jsonObject = JSONObject()
        val notificationBody = JSONObject()
        notificationBody.put("channelId", "664b0eba596b4d9106362ddb")
        notificationBody.put("channelType", "whatsapp")

        var notificationBody_recipient = JSONObject()
        notificationBody_recipient.put("name", sendingName)
        notificationBody_recipient.put("phone", sendingNo)

        var notificationBody_context = JSONObject()
        notificationBody_context.put("type", "notification")

        var notificationBody_whatsapp = JSONObject()
        notificationBody_whatsapp.put("type","template")
        var notificationBody_whatsapp_template = JSONObject()
        var notificationBody_whatsapp_template_body = JSONObject()
        notificationBody_whatsapp_template_body.put("Name",sendingName)
        notificationBody_whatsapp_template.put("templateName","independence_day_celeb")
        notificationBody_whatsapp_template.put("bodyValues",notificationBody_whatsapp_template_body)
        notificationBody_whatsapp.put("template",notificationBody_whatsapp_template)

        notificationBody.put("recipient",notificationBody_recipient)
        notificationBody.put("context",notificationBody_recipient)
        notificationBody.put("recipient",notificationBody_recipient)

        notificationBody.put("recipient", notificationBody_recipient)
        notificationBody.put("context", notificationBody_context)
        notificationBody.put("whatsapp", notificationBody_whatsapp)

        var jsonObjectRequest:JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, "https://server.gallabox.com/devapi/messages/whatsapp", notificationBody,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject) {
                   Timber.d("whats send success")
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    var e = error.localizedMessage
                    Timber.d("whats send error $e")
                }
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["apiSecret"] = "71400c1d1e384da38ef5cd6852ce07bb"
                params["Content-Type"] = "application/json"
                params["apiKey"] = "664b23b402fc9498c685699d"
                return params
            }
        }
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        )
        MySingleton.getInstance(mContext.applicationContext)!!.addToRequestQueue(jsonObjectRequest)


    }


    var selectedSourceID=""
    var selectedSourceIDName=""
    var selectedStageID=""
    var selectedStageIDName=""
    var selectedStatusID=""
    var selectedStatusIDName=""
    var selectedUserID=""
    var selectedUserName=""

    fun updateSource(obj: AddShopDBModelEntity){
        var crmSourceList = AppDatabase.getDBInstance()?.sourceMasterDao()?.getAll() as ArrayList<SourceMasterEntity>
        if(crmSourceList.size>0){
            var genericL : ArrayList<CustomData> = ArrayList()
            for(i in 0..crmSourceList.size-1){
                genericL.add(CustomData(crmSourceList.get(i).source_id.toString(),crmSourceList.get(i).source_name.toString()))
            }
            GenericDialog.newInstance("Source",genericL as ArrayList<CustomData>){
                //update source
                 selectedSourceID = it.id
                 selectedSourceIDName = it.name
                var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(obj.shop_id)
                //AppDatabase.getDBInstance()?.addShopEntryDao()?.updateSourceOnly(obj.shop_id,selectedSourceID,selectedSourceIDName,0)
                showOuterUpdateMsg("Source updated to $selectedSourceIDName for contact ${shopObj.shopName}",obj.shop_id,isSourceUpdate = true)
            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        }else{
            Toaster.msgShort(mContext, "No Source Found")
        }
    }

    fun updateStage(obj: AddShopDBModelEntity){
        try {
            var crmStageList = AppDatabase.getDBInstance()?.stageMasterDao()?.getAll() as ArrayList<StageMasterEntity>
            if(crmStageList.size>0){
                var genericL : ArrayList<CustomData> = ArrayList()
                for(i in 0..crmStageList.size-1){
                    genericL.add(CustomData(crmStageList.get(i).stage_id.toString(),crmStageList.get(i).stage_name.toString()))
                }
                GenericDialog.newInstance("Stage",genericL as ArrayList<CustomData>){
                     selectedStageID = it.id
                     selectedStageIDName = it.name
                    var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(obj.shop_id)
                    //AppDatabase.getDBInstance()?.addShopEntryDao()?.updateStageOnly(obj.shop_id,selectedStageID,selectedStageIDName,0)
                    showOuterUpdateMsg("Stage updated to $selectedStageIDName for contact ${shopObj.shopName}",obj.shop_id, isStageUpdate = true)
                }.show((mContext as DashboardActivity).supportFragmentManager, "")
            }else{
                Toaster.msgShort(mContext, "No Stage Found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateStatus(obj: AddShopDBModelEntity){
        try {
            var crmStatusList = AppDatabase.getDBInstance()?.statusMasterDao()?.getAll() as ArrayList<StatusMasterEntity>
            if(crmStatusList.size>0){
                var genericL : ArrayList<CustomData> = ArrayList()
                for(i in 0..crmStatusList.size-1){
                    genericL.add(CustomData(crmStatusList.get(i).status_id.toString(),crmStatusList.get(i).status_name.toString()))
                }
                GenericDialog.newInstance("Status",genericL as ArrayList<CustomData>){
                     selectedStatusID = it.id
                     selectedStatusIDName = it.name
                    var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(obj.shop_id)
                    //AppDatabase.getDBInstance()?.addShopEntryDao()?.updateStatusOnly(obj.shop_id,selectedStatusID,selectedStatusIDName,0)
                    showOuterUpdateMsg("Status updated to $selectedStatusIDName for contact ${shopObj.shopName}",obj.shop_id, isStatusUpdate = true)
                }.show((mContext as DashboardActivity).supportFragmentManager, "")
            }else{
                Toaster.msgShort(mContext, "No Status Found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showOuterUpdateMsg(msg:String,shop_id: String,isSourceUpdate:Boolean=false,isStageUpdate:Boolean=false,isStatusUpdate:Boolean=false,isAssignTo:Boolean=false){
        var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shop_id)
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_yes_no)

        val tv_header = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
        val tv_body = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
        val tv_yes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
        val tv_no = simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

        tv_header.text = AppUtils.hiFirstNameText()
        tv_body.text=msg

        tv_yes.setOnClickListener {
            if(isSourceUpdate){
                AppDatabase.getDBInstance()?.addShopEntryDao()?.updateSourceOnly(shopObj.shop_id,selectedSourceID,selectedSourceIDName,0)
            }
            if(isStageUpdate){
                AppDatabase.getDBInstance()?.addShopEntryDao()?.updateStageOnly(shopObj.shop_id,selectedStageID,selectedStageIDName,0)
            }
            if(isStatusUpdate){
                AppDatabase.getDBInstance()?.addShopEntryDao()?.updateStatusOnly(shopObj.shop_id,selectedStatusID,selectedStatusIDName,0)
            }
            if(isAssignTo){
                AppDatabase.getDBInstance()?.addShopEntryDao()?.updateAssignToOnly(shopObj.shop_id,selectedUserID,selectedUserName,0)
            }
            if (AppUtils.isOnline(mContext)){
                editSyncContact(shopObj)
            }
            else{
                showSimpleMsg("Contact updated successfully.")
            }
            simpleDialog.dismiss()
        }
        tv_no.setOnClickListener {
            simpleDialog.dismiss()
        }
        simpleDialog.show()
    }

    fun showSimpleMsg(msg:String){
        if(msg.contains("Contact updated successfully",ignoreCase = true)){
            voiceMsg("Contact updated successfully")
        }
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.generic_dialog)
        val head = simpleDialog.findViewById(R.id.tv_generic_dialog_header) as AppCustomTextView
        val cross = simpleDialog.findViewById(R.id.iv_generic_dialog_cancel) as ImageView
        head.text = AppUtils.hiFirstNameText()
        cross.visibility = View.GONE
        val dialogHeader = simpleDialog.findViewById(R.id.tv_generic_dialog_body) as AppCustomTextView
        dialogHeader.text = msg
        val dialogYes = simpleDialog.findViewById(R.id.tv_generic_dialog_ok) as AppCustomTextView

        dialogYes.setOnClickListener {
            shopContactList("")
            //adapterContactList.notifyDataSetChanged()
            simpleDialog.dismiss()
        }

        simpleDialog.show()
    }

    private fun voiceMsg(msg: String) {
        if (Pref.isVoiceEnabledForAttendanceSubmit) {
            val speechStatus = (mContext as DashboardActivity).textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
            if (speechStatus == TextToSpeech.ERROR)
                Log.e("Add Day Start", "TTS error in converting Text to Speech!");
        }
    }

    fun editSyncContact(shopOb: AddShopDBModelEntity) {
        var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopOb.shop_id)
        var addShopRequestData: AddShopRequestData = AddShopRequestData()
        addShopRequestData.user_id = Pref.user_id
        addShopRequestData.session_token = Pref.session_token
        addShopRequestData.shop_id = shopObj.shop_id
        addShopRequestData.shop_name = shopObj.shopName
        addShopRequestData.address = shopObj.address
        addShopRequestData.actual_address = shopObj.address
        addShopRequestData.pin_code = shopObj.pinCode
        addShopRequestData.type = shopObj.type
        addShopRequestData.shop_lat = shopObj.shopLat.toString()
        addShopRequestData.shop_long = shopObj.shopLong.toString()
        addShopRequestData.owner_email = shopObj.ownerEmailId.toString()
        addShopRequestData.owner_name = shopObj.shopName.toString()
        addShopRequestData.owner_contact_no = shopObj.ownerContactNumber.toString()
        addShopRequestData.amount = shopObj.amount.toString()

        addShopRequestData.shop_firstName= shopObj.crm_firstName
        addShopRequestData.shop_lastName=  shopObj.crm_lastName
        addShopRequestData.crm_companyID=  if(shopObj.companyName_id.equals("")) "0" else shopObj.companyName_id
        addShopRequestData.crm_jobTitle=  shopObj.jobTitle
        addShopRequestData.crm_typeID=  if(shopObj.crm_type_ID.equals("")) "0" else shopObj.crm_type_ID
        addShopRequestData.crm_statusID=  if(shopObj.crm_status_ID.equals("")) "0" else shopObj.crm_status_ID
        addShopRequestData.crm_sourceID= if(shopObj.crm_source_ID.equals("")) "0" else shopObj.crm_source_ID
        addShopRequestData.crm_reference=  shopObj.crm_reference
        addShopRequestData.crm_referenceID=  if(shopObj.crm_reference_ID.equals("")) "0" else shopObj.crm_reference_ID
        addShopRequestData.crm_referenceID_type=  shopObj.crm_reference_ID_type
        addShopRequestData.crm_stage_ID=  if(shopObj.crm_stage_ID.equals("")) "0" else shopObj.crm_stage_ID
        addShopRequestData.assign_to=  shopObj.crm_assignTo_ID
        addShopRequestData.saved_from_status=  shopObj.crm_saved_from
        addShopRequestData.isFromCRM = 1
        addShopRequestData.whatsappNoForCustomer = shopObj.whatsappNoForCustomer
        addShopRequestData.Remarks = shopObj.remarks
        addShopRequestData.Shop_NextFollowupDate = shopObj.Shop_NextFollowupDate

        progress_wheel.spin()
        val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
        BaseActivity.compositeDisposable.add(
            repository.editShop(addShopRequestData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val addShopResult = result as AddShopResponse
                    Timber.d("EditShop : " + ", SHOP: " + addShopRequestData.shop_name + ", RESPONSE:" + result.message)
                    progress_wheel.stopSpinning()
                    if (addShopResult.status == NetworkConstant.SUCCESS) {

                        AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsEditUploaded(1, addShopRequestData.shop_id)

                        showSimpleMsg("Contact updated successfully.")
                    }
                    else {
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    }
                }, { error ->
                    progress_wheel.stopSpinning()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    Timber.d("AddShop err : ${error.message}")
                })
        )
    }

    fun assignToUpdate(obj: AddShopDBModelEntity){
        if((AppDatabase.getDBInstance()?.teamListDao()?.getAll() as ArrayList<TeamListEntity>).size>0){
            progress_wheel.spin()
            loadTeamMember(AppDatabase.getDBInstance()?.teamListDao()?.getAll() as ArrayList<TeamListEntity>,obj)
        }else{
            getTeamList(obj)
        }
    }

    private fun getTeamList(obj: AddShopDBModelEntity) {
        println("tag_team_api call")
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage("Internet connectivity is required for data sync purposes.")
            return
        }
        progress_wheel.spin()
        val repository = TeamRepoProvider.teamRepoProvider()
        BaseActivity.compositeDisposable.add(
            repository.teamListNew(Pref.user_id!!, false, false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as TeamListRes
                    if (response.status == NetworkConstant.SUCCESS) {
                        progress_wheel.stopSpinning()
                        if (response.member_list != null && response.member_list!!.size > 0) {
                            AppDatabase.getDBInstance()?.teamListDao()?.insertAll(response.member_list!!)
                            assignToUpdate(obj)
                        }
                    } else {
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(response.message!!)
                    }
                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                })
        )
    }

    private fun loadTeamMember(member_list:ArrayList<TeamListEntity>,obj: AddShopDBModelEntity) {
        if (member_list.size>0) {
            var genericL : ArrayList<CustomData> = ArrayList()
            for(i in 0..member_list.size-1){
                genericL.add(CustomData(member_list.get(i).user_id.toString(),member_list.get(i).user_name.toString()+"("+member_list.get(i).contact_no.toString()+")"))
            }
            GenericDialog.newInstance("Assign To",genericL as ArrayList<CustomData>){
                 selectedUserID = it.id
                 selectedUserName = it.name
                var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(obj.shop_id)
                //AppDatabase.getDBInstance()?.addShopEntryDao()?.updateAssignToOnly(obj.shop_id,selectedUserID,selectedUserName,0)
                showOuterUpdateMsg("Assigned contact ${shopObj.shopName} to $selectedUserName?",obj.shop_id, isAssignTo =true)
            }.show((mContext as DashboardActivity).supportFragmentManager, "")

            Handler().postDelayed(Runnable {
                progress_wheel.stopSpinning()
            }, 1500)
        } else {
            Toaster.msgShort(mContext, "No Member Found")
        }
    }
}