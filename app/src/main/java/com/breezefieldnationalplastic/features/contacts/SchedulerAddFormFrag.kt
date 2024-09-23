package com.breezefieldnationalplastic.features.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.breezefieldnationalplastic.MultiFun
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.domain.ModeTemplateEntity
import com.breezefieldnationalplastic.app.domain.RuleTemplateEntity
import com.breezefieldnationalplastic.app.domain.ScheduleTemplateEntity
import com.breezefieldnationalplastic.app.domain.SchedulerContactEntity
import com.breezefieldnationalplastic.app.domain.SchedulerDateTimeEntity
import com.breezefieldnationalplastic.app.domain.SchedulerMasterEntity
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.PermissionUtils
import com.breezefieldnationalplastic.app.utils.ProcessImageUtils_v1
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.NewQuotation.Mail
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.reimbursement.presentation.FullImageDialog
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pnikosis.materialishprogress.ProgressWheel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.themechangeapp.pickimage.PermissionHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Locale
import java.util.Random
import android.provider.OpenableColumns

import android.database.Cursor
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.breezefieldnationalplastic.MySingleton
import org.json.JSONObject
import java.util.HashMap


class SchedulerAddFormFrag : BaseFragment(), View.OnClickListener {

    private var instructionDialog: Dialog? = null
    private var currentMinute: Int = 0
    private var adjustedHour: Int = 0
    private var timestamp: Long? = 0
    private var hr: String=""
    private var min: String=""
    private var particular_date_select: String=""
    private var contactTickL: ArrayList<ScheduleContactDtls> = ArrayList()
    private var selectedHr: String=""
    private var selectedMin: String=""
    private var str_templateID:String = Pref.user_id!!
    private var str_modeoftemplateID:String = Pref.user_id!!
    private var str_ruleoftemplateID:String = Pref.user_id!!
    private var str_contactoftemplate:String = Pref.user_id!!
    private var sortBy:String = ""
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var mContext: Context
    private var filePath = ""
    private var permissionUtils: PermissionUtils? = null
    private lateinit var hour_numPicker:NumberPicker
    private lateinit var min_numPicker:NumberPicker
    private lateinit var tv_selectedTime:TextView
    private lateinit var iv_add_schedular_form_attachment1:ImageView
    private lateinit var iv_row_scheduler_list_email_info:ImageView
    private lateinit var iv_add_schedular_form_attachment2:ImageView
    private lateinit var iv_add_schedular_form_attachment3:ImageView
    private lateinit var schedulername: TextInputEditText
    private lateinit var selectTemplate: TextInputEditText
    private lateinit var et_templateContent: TextInputEditText
    private lateinit var tv_rule_Of_scheduler: TextInputEditText
    private lateinit var selectContactSchedule: TextInputEditText
    private lateinit var selectMode: TextInputEditText
    private lateinit var calendarView : MaterialCalendarView
    private lateinit var cv_calendar : CardView
    private lateinit var sw_repeatmonth : Switch
    private lateinit var adapterScheduleContactName:AdapterScheduleContactName
    private lateinit var iv_template : LinearLayout
    private lateinit var textInLaySelectMode : TextInputLayout
    private lateinit var textInLaySelectContact : TextInputLayout
    private lateinit var textInLaySelectRule : TextInputLayout
    private lateinit var iv_mode_template : LinearLayout
    private lateinit var iv_rule_Of_scheduler_dropdown : LinearLayout
    private lateinit var iv_frag_cont_add_schedule_to : LinearLayout
    private lateinit var ll_frag_sch_add_template_root : LinearLayout
    private lateinit var cv_frag_Shceduler_add_submit : CardView
    private lateinit var cv_scheduler_timer : CardView
    private lateinit var cvAttachRoot : CardView
    private lateinit var tvChoosenFileName : TextView

    private var contL :ArrayList<AddShopDBModelEntity> = ArrayList()
    private var finalL: ArrayList<ScheduleContactDtls> = ArrayList()

    private var isSwitchRepeat = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    companion object{
        var editShchedulerID: String = ""
        fun getInstance(objects: Any): SchedulerAddFormFrag {
            val objFragment = SchedulerAddFormFrag()
            var obj = objects as String
            editShchedulerID =obj
            return objFragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_scheduler_add_form, container, false)
        initView(view)
        return view
    }
    private fun initView(view: View) {
        progress_wheel = view.findViewById(R.id.progress_wheel_frag_add_cont)
        cv_calendar = view.findViewById(R.id.cv_calendar)
        hour_numPicker = view.findViewById(R.id.np_frag_schedule_add_form_hr)
        iv_add_schedular_form_attachment1 = view.findViewById(R.id.iv_add_schedular_form_attachment1)
        iv_add_schedular_form_attachment2 = view.findViewById(R.id.iv_add_schedular_form_attachment2)
        iv_add_schedular_form_attachment3 = view.findViewById(R.id.iv_add_schedular_form_attachment3)
        sw_repeatmonth = view.findViewById(R.id.sw_repeatmonth)
        schedulername = view.findViewById(R.id.et_add_form_schedule_name)
        selectTemplate = view.findViewById(R.id.tv_select_template)
        et_templateContent = view.findViewById(R.id.et_templateContent)
        min_numPicker = view.findViewById(R.id.np_frag_schedule_add_form_min)
        tv_selectedTime = view.findViewById(R.id.tv_frag_schedule_add_form_selected_time)
        iv_row_scheduler_list_email_info = view.findViewById(R.id.iv_row_scheduler_list_email_info)
        calendarView = view.findViewById(R.id.calendarView_frag_schedule_calendar)
        selectMode = view.findViewById(R.id.tv_select_mode_template)
        selectContactSchedule = view.findViewById(R.id.tv_frag_cont_add_contact_to_schedule)
        iv_mode_template = view.findViewById(R.id.iv_select_mode_template)
        tv_rule_Of_scheduler = view.findViewById(R.id.tv_rule_Of_scheduler)
        iv_rule_Of_scheduler_dropdown = view.findViewById(R.id.iv_rule_Of_scheduler_dropdown)
        ll_frag_sch_add_template_root = view.findViewById(R.id.ll_frag_sch_add_template_root)
        iv_frag_cont_add_schedule_to = view.findViewById(R.id.iv_frag_cont_add_schedule_to)
        cv_frag_Shceduler_add_submit = view.findViewById(R.id.cv_frag_Shceduler_add_submit)
        iv_template = view.findViewById(R.id.iv_frag_sched_add_form_template_dropDown)
        textInLaySelectContact = view.findViewById(R.id.textInLaySelectContact)
        textInLaySelectRule = view.findViewById(R.id.textInLaySelectRule)
        textInLaySelectMode = view.findViewById(R.id.textInLaySelectMode)
        cv_scheduler_timer = view.findViewById(R.id.cv_scheduler_timer)
        cvAttachRoot = view.findViewById(R.id.cv_frag_sch_add_form_attach_root)
        tvChoosenFileName = view.findViewById(R.id.tv_frag_sch_add_file_name)
        cv_scheduler_timer.visibility = View.GONE
        iv_row_scheduler_list_email_info.visibility = View.GONE
        tvChoosenFileName.visibility = View.GONE

        iv_template.setOnClickListener(this)
        iv_row_scheduler_list_email_info.setOnClickListener(this)
        selectContactSchedule.setOnClickListener(this)
        textInLaySelectMode.setOnClickListener(this)
        textInLaySelectRule.setOnClickListener(this)
        textInLaySelectContact.setOnClickListener(this)
        selectTemplate.setOnClickListener(this)
        tv_rule_Of_scheduler.setOnClickListener(this)
        iv_mode_template.setOnClickListener(this)
        selectMode.setOnClickListener(this)
        selectContactSchedule.setOnClickListener(this)
        ll_frag_sch_add_template_root.setOnClickListener(this)
        iv_rule_Of_scheduler_dropdown.setOnClickListener(this)
        iv_frag_cont_add_schedule_to.setOnClickListener(this)
        cv_frag_Shceduler_add_submit.setOnClickListener(this)

        contL = ArrayList()
        finalL = ArrayList()
        contactTickL = ArrayList()
        contL = AppDatabase.getDBInstance()!!.addShopEntryDao().getContatcShopsByName() as ArrayList<AddShopDBModelEntity>
        for(i in 0..contL.size-1){
            finalL.add(ScheduleContactDtls(contL.get(i).shopName,contL.get(i).ownerContactNumber,contL.get(i).shop_id,false))
        }
        setTemplateData()
        setModeData()
        setRuleData()

        cv_scheduler_timer.visibility = View.GONE
        cv_calendar.visibility = View.GONE
        sw_repeatmonth.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                isSwitchRepeat = true
            }else{
                isSwitchRepeat = false
            }
        }
        if(!editShchedulerID.equals("")){
            (mContext as DashboardActivity).setTopBarTitle("Edit Scheduler")
            cvAttachRoot.visibility = View.VISIBLE
            setEditData()
        }else{
            (mContext as DashboardActivity).setTopBarTitle("Add Scheduler")
        }

        iv_add_schedular_form_attachment1.setOnClickListener {
            initPermissionCheck()
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun showPictureDialog() {
         val pictureDialog = AlertDialog.Builder(mContext)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItems = arrayOf("Select photo from gallery", "Capture Image", "Select file from file manager")
            pictureDialog.setItems(pictureDialogItems,
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        0 -> selectImageInAlbum()
                        1 -> {
                            //(mContext as DashboardActivity).openFileManager()
                            launchCamera()
                        }
                        2 -> {
                            //(mContext as DashboardActivity).openFileManager()
                            //openFile()
                            openFolder()
                            //(mContext as DashboardActivity).openPDFFileManager()
                        }
                    }
                })
            pictureDialog.show()
        }

    fun openFolder() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 101)
    }

    fun openFile() {
        val mimeTypes = arrayOf(
            "application/pdf" //.pdf
        )
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, mimeTypes)
        }

        startActivityForResult(intent, 2001)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 2001){
            try{
                try{

                    var m = Mail()
                    m = Mail("suman.bachar@indusnet.co.in", "jfekuhstltfkarrv") // generate under 2-step verification -> app password

                    var toArr = arrayOf("sumanbacharofc@gmail.com")
                    m.setTo(toArr)
                    m.setFrom("TEAM");
                    m.setSubject("Sub")
                    var mailBody = "body"
                    m.setBody(mailBody)
                    var sendP = Pref.scheduler_file


                    doAsync {
                        try{
                            m.send(filePath)
                        }catch (ex:Exception){
                            ex.printStackTrace()
                            println("tag_main Mail sent exception ${ex.message}")
                            Timber.e("Mail sent exception: "+ ex.message)

                        }
                        uiThread {

                        }
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
        if(requestCode == 101){
            if (data != null) {

                try {
                   /* var sendingPath = MultiFun.copyFileToInternal(mContext,data.data!!)
                    filePath = sendingPath!!*/
                    var fName = File(data.data!!.path).name
                    val fileUri: Uri = data.data!!
                    fileSize(data.data!!.scheme!! , fileUri , fName)
                   /* tvChoosenFileName.visibility = View.VISIBLE
                    tvChoosenFileName.text = fName.toString()*/
                }catch (ex:Exception){
                    ex.printStackTrace()
                    tvChoosenFileName.visibility = View.GONE
                    Toaster.msgShort(mContext, getString(R.string.something_went_wrong))
                }


                //MultiFun.sendAutoMailWithFile(sendingPath!!)
                /*var uri = data.data!!
                var sendingPath = MultiFun.copyFileToInternal(mContext,uri)
                Pref.scheduler_file = sendingPath
                try{
                    var m = Mail()
                    m = Mail("suman.bachar@indusnet.co.in", "jfekuhstltfkarrv") // generate under 2-step verification -> app password
                    var toArr = arrayOf("sumanbacharofc@gmail.com")
                    m.setTo(toArr)
                    m.setFrom("TEAM");
                    m.setSubject("Sub")
                    var mailBody = "body"
                    m.setBody(mailBody)
                    var sendP = Pref.scheduler_file
                    doAsync {
                        try{
                            m.send(sendingPath)
                        }catch (ex:Exception){
                            ex.printStackTrace()
                            println("tag_main Mail sent exception ${ex.message}")
                            Timber.e("Mail sent exception: "+ ex.message)
                        }
                        uiThread {

                        }
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }*/
            }
        }
    }

    private fun fileSize(scheme: String, filePath1: Uri, fName: String) {

        val returnCursor: Cursor =
            mContext.getContentResolver().query(filePath1, null, null, null, null)!!
        val sizeIndex = returnCursor!!.getColumnIndex(OpenableColumns.SIZE)
        returnCursor!!.moveToFirst()
        val fileSize = returnCursor!!.getString(sizeIndex)
        val fileSizeInKB = fileSize .toLong() / 1024
        Log.e("SchedulerAddFormFrag", "Pdf file size after compression in KB==========> $fileSizeInKB KB")

        println("This is the file size: $fileSize")
        println("This is the file size: $fileSize")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                var sendingPath = MultiFun.copyFileToInternal(mContext,filePath1)
                filePath = sendingPath!!
                tvChoosenFileName.visibility = View.VISIBLE
                tvChoosenFileName.text = fName
            } else
                Toast.makeText(mContext, "More than " + Pref.maxFileSize + " KB file is not allowed", Toast.LENGTH_LONG).show()
        }
    }

    private fun launchCamera() {
        (mContext as DashboardActivity).captureImage()
    }
    private fun selectImageInAlbum() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        (mContext as DashboardActivity).startActivityForResult(galleryIntent, PermissionHelper.REQUEST_CODE_STORAGE)
    }

    private fun initPermissionCheck() {
        var permissionList = arrayOf<String>( Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permissionList += Manifest.permission.READ_MEDIA_IMAGES
            permissionList += Manifest.permission.READ_MEDIA_AUDIO
            permissionList += Manifest.permission.READ_MEDIA_VIDEO
        }else{
            permissionList += Manifest.permission.WRITE_EXTERNAL_STORAGE
            permissionList += Manifest.permission.READ_EXTERNAL_STORAGE
        }
        permissionUtils = PermissionUtils(mContext as Activity, object : PermissionUtils.OnPermissionListener {
            override fun onPermissionGranted() {
                showPictureDialog()
            }

            override fun onPermissionNotGranted() {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
            }

        },permissionList)// arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    private fun setCal(){
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE)

        calendarView.state().edit()
            .setMinimumDate(CalendarDay.today())
            //.setMaximumDate(CalendarDay.today())
            .commit()

        hour_numPicker.minValue = 0
        hour_numPicker.maxValue = 23
        min_numPicker.minValue = 0
        min_numPicker.maxValue = 59
        var hrL = Array<String>(24) { "" }
        for(i in 0..23){
            hrL[i] = "${i}"
        }
        var minL = Array<String>(60) { "" }
        for(i in 0..59){
            minL[i] = "${i}"
        }

        if (editShchedulerID!=""){
            var schedulerObj =AppDatabase.getDBInstance()!!.schedulerMasterDao().getSchedulerDtls(editShchedulerID)
           // if (schedulerObj.select_rule.equals("Manual"))
            var scheduleTimeL = AppDatabase.getDBInstance()!!.schedulerDateTimeDao().getAll(editShchedulerID) as ArrayList<SchedulerDateTimeEntity>
            if (scheduleTimeL.size > 0) {
                scheduleTimeL.get(0).select_hour
                val adjustedHour1 = scheduleTimeL.get(0).select_hour.replace(" h", "")
                val adjustedHour2 = scheduleTimeL.get(0).select_minute.replace(" min", "")
                adjustedHour = Integer.parseInt(adjustedHour1.trim())
                currentMinute = Integer.parseInt(adjustedHour2.trim())
                hour_numPicker.value = adjustedHour
                min_numPicker.value = currentMinute
                for(i in 0..scheduleTimeL.size-1) {
                    var y = scheduleTimeL.get(i).select_date.split("-").get(0).toInt()
                    var m = scheduleTimeL.get(i).select_date.split("-").get(1).toInt()
                    var d = scheduleTimeL.get(i).select_date.split("-").get(2).toInt()

                    calendarView.setDateSelected(CalendarDay.from(y,m,d), true)

                    if(scheduleTimeL.get(i).isDone){
                        calendarView.setDateSelected(CalendarDay.from(y,m,d), true)
                       // calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE)
                    }
                   
                }
            }
            val currentHour = LocalTime.now().hour
            currentMinute = LocalTime.now().minute
            adjustedHour = currentHour % 24
            hour_numPicker.value = adjustedHour
            min_numPicker.value = currentMinute
        }else {
            val currentHour = LocalTime.now().hour
            currentMinute = LocalTime.now().minute
            adjustedHour = currentHour % 24
            hour_numPicker.value = adjustedHour
            min_numPicker.value = currentMinute
        }
       // val adjustedMinute = currentMinute


        hour_numPicker.displayedValues = hrL
        min_numPicker.displayedValues =minL

        selectedHr = adjustedHour.toString()//+" h"
        selectedMin = currentMinute.toString()//+" min"


        tv_selectedTime.text = adjustedHour.toString()+""+" : "+ currentMinute.toString()+""


        hour_numPicker.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(numberPicker: NumberPicker, i: Int, i2: Int) {
                try{
                    selectedHr = hrL[i2].toString()
                    tv_selectedTime.text = selectedHr+" : "+ selectedMin+""

                }catch (ex:Exception){
                    ex.printStackTrace()
                    println("tag_picker_err ${ex.message}")
                }
            }
        })
        min_numPicker.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(numberPicker: NumberPicker, i: Int, i2: Int) {
                try{
                    selectedMin = minL[i2].toString()
                    tv_selectedTime.text =selectedHr+" : "+ selectedMin+" "

                }catch (ex:Exception){
                    ex.printStackTrace()
                    println("tag_picker_err ${ex.message}")
                }
            }
        })
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_frag_sched_add_form_template_dropDown , R.id.tv_select_template->{
                if((AppDatabase.getDBInstance()?.scheduleTemplateDao()?.getAll() as ArrayList<ScheduleTemplateEntity>).size>0){
                    loadTemplateList(AppDatabase.getDBInstance()?.scheduleTemplateDao()?.getAll() as ArrayList<ScheduleTemplateEntity>)
                }else{
                    Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.iv_select_mode_template, R.id.tv_select_mode_template->{
                if((AppDatabase.getDBInstance()?.modeTemplateDao()?.getAll() as ArrayList<ModeTemplateEntity>).size>0){
                    loadModeOfTemplateList(AppDatabase.getDBInstance()?.modeTemplateDao()?.getAll() as ArrayList<ModeTemplateEntity>)
                }else{
                    Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.iv_rule_Of_scheduler_dropdown , R.id.tv_rule_Of_scheduler->{
                if((AppDatabase.getDBInstance()?.modeTemplateDao()?.getAll() as ArrayList<ModeTemplateEntity>).size>0){
                    loadRuleOfTemplateList(AppDatabase.getDBInstance()?.ruleTemplateDao()?.getAll() as ArrayList<RuleTemplateEntity>)
                }else{
                    Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.iv_frag_cont_add_schedule_to , R.id.tv_frag_cont_add_contact_to_schedule->{
                if(contL.size >0){
                    loadContactOfTemplateList()
                } else{
                    Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.cv_frag_Shceduler_add_submit ->{
                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                if(editShchedulerID.equals("")) {
                    submitValidationCheck()
                }
                else{
                    editValidationCheck(editShchedulerID)
                }

            }
            R.id.iv_row_scheduler_list_email_info ->{
                var instructionDialog = Dialog(mContext)
                instructionDialog!!.setCancelable(false)
                instructionDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                instructionDialog!!.setContentView(R.layout.dialog_gmail_instruction_only)
                val tvHeader = instructionDialog!!.findViewById(R.id.dialog_contact_gr_header) as TextView
                val tv_instruction = instructionDialog!!.findViewById(R.id.tv_instruction) as TextView
                val tv_save_instruction = instructionDialog!!.findViewById(R.id.tv_save_instruction) as TextView
                val iv_dialog_instruction_copy = instructionDialog!!.findViewById(R.id.iv_dialog_instruction_copy) as ImageView
                val iv_close = instructionDialog!!.findViewById(R.id.iv_dialog_instruction_close_icon) as ImageView

                val tv_ulink = instructionDialog!!.findViewById(R.id.tv_ulink) as TextView
                tv_ulink.setOnClickListener {
                    var youtubeID = "dM_DlzyeWW8"
                    val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeID))
                    val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeID))
                    try {
                        this.startActivity(intentApp)
                    } catch (ex: ActivityNotFoundException) {
                        this.startActivity(intentBrowser)
                    }
                }

                tv_save_instruction.setOnClickListener {
                    instructionDialog.dismiss()
                }
                iv_close.setOnClickListener {
                    instructionDialog.dismiss()
                }
                iv_dialog_instruction_copy.setOnClickListener {
                    val clipboard: ClipboardManager? = mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
                    val clip = ClipData.newPlainText("label", tv_instruction.text.toString().trim())
                    clipboard!!.setPrimaryClip(clip)
                    Toaster.msgLong(mContext,"Copied to Clipboard")
                }
                tvHeader.text = "E-mail configuration"
                instructionDialog!!.show()


                    /*instructionDialog = Dialog(mContext)
                    instructionDialog!!.setCancelable(false)
                    instructionDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    instructionDialog!!.setContentView(R.layout.dialog_gmail_instruction)
                    val tvHeader = instructionDialog!!.findViewById(R.id.dialog_contact_gr_header) as TextView
                    val tv_instruction = instructionDialog!!.findViewById(R.id.tv_instruction) as TextView
                    val tv_save_instruction = instructionDialog!!.findViewById(R.id.tv_save_instruction) as TextView
                    val et_user_gmail_id = instructionDialog!!.findViewById(R.id.et_user_gmail_id) as AppCustomEditText
                    val et_user_password = instructionDialog!!.findViewById(R.id.et_user_password) as TextInputEditText
                    val tv_headerOfSetVerification = instructionDialog!!.findViewById(R.id.tv_headerOfSetVerification) as TextView
                    val rvContactGrName = instructionDialog!!.findViewById(R.id.rv_dialog_cont_gr) as RecyclerView
                    val iv_close = instructionDialog!!.findViewById(R.id.iv_dialog_instruction_close_icon) as ImageView
                    val iv_dialog_instruction_copy = instructionDialog!!.findViewById(R.id.iv_dialog_instruction_copy) as ImageView
                    val iv_email_info = instructionDialog!!.findViewById(R.id.iv_email_info) as ImageView
                    val lin_credentials_gmail = instructionDialog!!.findViewById(R.id.lin_credentials_gmail) as LinearLayout
                    iv_dialog_instruction_copy.visibility=View.VISIBLE
                    iv_email_info.visibility=View.VISIBLE

                iv_email_info.setOnClickListener {

                    showInstructionOfTwoStepVerification()

                }

                    iv_close.setOnClickListener {
                        instructionDialog!!.dismiss()
                    }

                     iv_dialog_instruction_copy.setOnClickListener {
                         val clipboard: ClipboardManager? = mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
                         val clip = ClipData.newPlainText("label", tv_instruction.text.toString().trim())
                         clipboard!!.setPrimaryClip(clip)
                         Toaster.msgShort(mContext,"Copied")
                     }

                    rvContactGrName.visibility=View.GONE
                    //iv_email_info.visibility=View.GONE
                    lin_credentials_gmail.visibility=View.GONE
                    tv_save_instruction.visibility=View.GONE
                    tv_headerOfSetVerification.visibility=View.GONE
                    tv_instruction.visibility=View.VISIBLE
                    tvHeader.text = "Read Instruction"
                    instructionDialog!!.show()*/

            }
        }


    }

    private fun showInstructionOfTwoStepVerification() {

        var instructionDialog = Dialog(mContext)
        instructionDialog!!.setCancelable(false)
        instructionDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        instructionDialog!!.setContentView(R.layout.dialog_gmail_instruction_only)
        val tvHeader = instructionDialog!!.findViewById(R.id.dialog_contact_gr_header) as TextView
        val tv_instruction = instructionDialog!!.findViewById(R.id.tv_instruction) as TextView
        val tv_save_instruction = instructionDialog!!.findViewById(R.id.tv_save_instruction) as TextView
        val iv_dialog_instruction_copy = instructionDialog!!.findViewById(R.id.iv_dialog_instruction_copy) as ImageView
        val iv_close = instructionDialog!!.findViewById(R.id.iv_dialog_instruction_close_icon) as ImageView

        val tv_ulink = instructionDialog!!.findViewById(R.id.tv_ulink) as TextView
        tv_ulink.setOnClickListener {
            var youtubeID = "dM_DlzyeWW8"
            val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeID))
            val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeID))
            try {
                this.startActivity(intentApp)
            } catch (ex: ActivityNotFoundException) {
                this.startActivity(intentBrowser)
            }
        }

        tv_save_instruction.setOnClickListener {
            instructionDialog.dismiss()
        }
        iv_close.setOnClickListener {
            instructionDialog.dismiss()
        }
        iv_dialog_instruction_copy.setOnClickListener {
            val clipboard: ClipboardManager? = mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", tv_instruction.text.toString().trim())
            clipboard!!.setPrimaryClip(clip)
        }
        tvHeader.text = "E-mail configuration"
        instructionDialog!!.show()
    }

    private fun editValidationCheck(editShchedulerID: String) {
        Pref.scheduler_template=
            et_templateContent.text.toString().trim()+"\n" +
                    "\n" +
                    "\n" +
                    " Regards\n" +
                    "${Pref.user_name}"
        progress_wheel.spin()

        if(schedulername.text.toString().length==0 || schedulername.text.toString().trim().equals("")){

            schedulername.requestFocus()
            schedulername.setError("Enter Scheduler Name")
            progress_wheel.stopSpinning()
            return
        }
        if(selectTemplate.text.toString().length==0 || selectTemplate.text.toString().trim().equals("")){
            (mContext as DashboardActivity).showSnackMessage("Select a template.")
            progress_wheel.stopSpinning()
            return
        }
        if(et_templateContent.text.toString().length==0 || et_templateContent.text.toString().trim().equals("") && et_templateContent.isEnabled==true){
            (mContext as DashboardActivity).showSnackMessage("Write template content")
            schedulername.requestFocus()
            schedulername.setError("Enter Scheduler Name")
            progress_wheel.stopSpinning()
            progress_wheel.stopSpinning()
            return
        }
        if(selectMode.text.toString().length==0 || selectMode.text.toString().trim().equals("")){
            (mContext as DashboardActivity).showSnackMessage("Select a mode")
            progress_wheel.stopSpinning()
            return
        }
        if(tv_rule_Of_scheduler.text.toString().length==0 || tv_rule_Of_scheduler.text.toString().trim().equals("")){
            (mContext as DashboardActivity).showSnackMessage("Select a rule")
            progress_wheel.stopSpinning()
            return
        }

        if (calendarView.selectedDates.size==0 &&  tv_rule_Of_scheduler.text.toString().trim().equals("Auto")){
            (mContext as DashboardActivity).showSnackMessage("Select a date")
            progress_wheel.stopSpinning()
            return
        }
        if(selectContactSchedule.text.toString().length==0 || selectContactSchedule.text.toString().trim().equals("")){
            (mContext as DashboardActivity).showSnackMessage("Select a contact")
            progress_wheel.stopSpinning()
            return
        }

        //
        var curentTimeStamp = System.currentTimeMillis()
        for(j in 0..calendarView.selectedDates.size-1){
            particular_date_select = calendarView.selectedDates.get(j).year.toString() + "-" + String.format("%02d", calendarView.selectedDates.get(j).month.toInt()
            ) + "-" + String.format("%02d", calendarView.selectedDates.get(j).day)
            if (tv_rule_Of_scheduler.text.toString().equals("Auto")) {
                hr = String.format("%02d", selectedHr.replace(" h", "").toInt())
                min = String.format("%02d", selectedMin.replace(" min", "").toInt())

                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = simpleDateFormat.parse(particular_date_select + " $hr:$min:00")
                timestamp = date?.time
            }
        }
        if(timestamp!! <= curentTimeStamp && tv_rule_Of_scheduler.text.toString().equals("Auto")){
            (mContext as DashboardActivity).showSnackMessage("Set a schedule time after current time")
            progress_wheel.stopSpinning()

        }
        else {
            try {
                doAsync {
                    //new code begin
                    val random = Random()
                    var schObj = SchedulerMasterEntity()
                    schObj =  AppDatabase.getDBInstance()!!.schedulerMasterDao().getSchedulerDtls(editShchedulerID)
                    schObj.scheduler_id = editShchedulerID
                    schObj.scheduler_name = schedulername.text.toString().trim()
                    schObj.select_template = selectTemplate.text.toString().trim()
                    schObj.template_content = et_templateContent.text.toString().trim()
                    schObj.select_mode_id = str_modeoftemplateID
                    schObj.select_mode = selectMode.text.toString().trim()
                    schObj.select_rule_id = str_ruleoftemplateID
                    schObj.select_rule = tv_rule_Of_scheduler.text.toString().trim()
                    schObj.save_date_time = AppUtils.getCurrentDateTimeNew()
                    schObj.save_modify_date_time = AppUtils.getCurrentDateTimeNew()
                    schObj.isUploaded = true
                    if (schObj.select_rule.equals("Manual", ignoreCase = true)) {
                        schObj.isAutoMail = false
                    }else{
                        schObj.isAutoMail = true
                    }
                    var dateTimeL : ArrayList<SchedulerDateTimeEntity> = ArrayList()
                    if (schObj.select_rule.equals("Auto", ignoreCase = true)) {
                        hr = String.format("%02d", selectedHr.replace(" h","").toInt())
                        min = String.format("%02d", selectedMin.replace(" min","").toInt())

                        var dtL :ArrayList<String> = ArrayList()
                        for(j in 0..calendarView.selectedDates.size-1){
                            var dateTimeObj = SchedulerDateTimeEntity()
                            dateTimeObj.scheduler_id = schObj.scheduler_id
                            dateTimeObj.select_hour = selectedHr
                            dateTimeObj.select_minute = selectedMin
                            dateTimeObj.select_time = hr + ":" + min + ":00"

                            var y = calendarView.selectedDates.get(j).year.toString()
                            var m = String.format("%02d", calendarView.selectedDates.get(j).month.toInt())
                            var d = String.format("%02d", calendarView.selectedDates.get(j).day.toInt())
                            var dt =  y+ "-" + m + "-" + d
                            dateTimeObj.select_date = dt
                            dtL.add(dt)

                            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val date = simpleDateFormat.parse(dt + " $hr:$min:00")
                            val timestamp = date?.time

                            dateTimeObj.select_timestamp = timestamp.toString()
                            dateTimeL.add(dateTimeObj)
                        }
                        if(isSwitchRepeat){
                            for(i in 0..dtL.size-1){
                                var dobj = dtL.get(i)
                                for(j in 0..11){
                                    var dateTimeObj = SchedulerDateTimeEntity()
                                    dateTimeObj.scheduler_id = schObj.scheduler_id
                                    dateTimeObj.select_hour = selectedHr
                                    dateTimeObj.select_minute = selectedMin
                                    dateTimeObj.select_time = hr + ":" + min + ":00"

                                    dobj = AppUtils.addMonths(dobj)
                                    dateTimeObj.select_date = dobj

                                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    val date = simpleDateFormat.parse(dobj + " $hr:$min:00")
                                    val timestamp = date?.time

                                    dateTimeObj.select_timestamp = timestamp.toString()
                                    dateTimeL.add(dateTimeObj)
                                }
                            }
                        }
                        var tempDtL : List<SchedulerDateTimeEntity> = dateTimeL.distinct()
                        dateTimeL = ArrayList(tempDtL)
                    }
                    var contL : ArrayList<SchedulerContactEntity> = ArrayList()

                    for (j in 0..contactTickL.size - 1) {
                        contL.add(SchedulerContactEntity(scheduler_id =schObj.scheduler_id, select_contact_id = contactTickL.get(j).contact_id,
                            select_contact = contactTickL.get(j).contact_name, select_contact_number = contactTickL.get(j).contact_number))
                    }
                    schObj.sendingFilePath = filePath
                    AppDatabase.getDBInstance()!!.schedulerMasterDao().deleteScheduler(schObj.scheduler_id)
                    AppDatabase.getDBInstance()!!.schedulerDateTimeDao().deleteScheduler(schObj.scheduler_id)
                    AppDatabase.getDBInstance()!!.schedulerContactDao().deleteScheduler(schObj.scheduler_id)
                    AppDatabase.getDBInstance()!!.schedulerMasterDao().insert(schObj)
                    AppDatabase.getDBInstance()!!.schedulerDateTimeDao().insertAll(dateTimeL)
                    AppDatabase.getDBInstance()!!.schedulerContactDao().insertAll(contL)
                    //new code end

                   /* var a = schObj
                    var b = dateTimeL
                    var c = contL*/

                    uiThread {
                        val simpleDialog = Dialog(mContext)
                        simpleDialog.setCancelable(false)
                        simpleDialog.getWindow()!!
                            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

                        val dialogHeader =
                            simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
                        dialogHeader.text = "Wow! Schedular configured successfully.\n" +
                                "Communication with template will sent automatically."
                        val dialogYes =
                            simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
                        dialogYes.setOnClickListener({ view ->
                            simpleDialog.cancel()
                        })
                        simpleDialog.show()

                        //new code begin
                        if(schObj.isAutoMail == false){
                            for (l in 0..contL.size - 1) {
                                var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(contL.get(l).select_contact_id)
                               // MultiFun.autoMailScheduler(shopObj.ownerEmailId, schObj.template_content,shopObj,schObj.scheduler_name) // edit
                                MultiFun.sendAutoMailWithFile(filePath,shopObj.ownerEmailId, schObj.template_content,shopObj,schObj.scheduler_name) // edit
                            }
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).onBackPressed()
                        }else{
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).onBackPressed()
                        }
                        //new code end
                    }
                }

            }
            catch (e: Exception) {
                progress_wheel.stopSpinning()
                e.printStackTrace()
            }
        }
    }

    private fun submitValidationCheck() {
        Pref.scheduler_template=
            et_templateContent.text.toString().trim()+"\n" +
                    "\n" +
                    "\n" +
                    " Regards\n" +
                    "${Pref.user_name}"
        progress_wheel.spin()

        if(schedulername.text.toString().length==0 || schedulername.text.toString().trim().equals("")){
            schedulername.requestFocus()
            schedulername.setError("Enter Scheduler Name")
            progress_wheel.stopSpinning()
            return
        }
        if(AppDatabase.getDBInstance()!!.schedulerMasterDao().getDuplicateSchedulerData(schedulername.text.toString()).size > 0){
            schedulername.requestFocus()
            schedulername.setError("Duplicate Scheduler Name")
            progress_wheel.stopSpinning()
            return
        }
        if(selectTemplate.text.toString().length==0 || selectTemplate.text.toString().trim().equals("")){
            (mContext as DashboardActivity).showSnackMessage("Select a template.")
            progress_wheel.stopSpinning()
            return
        }
        if(et_templateContent.text.toString().length==0 || et_templateContent.text.toString().trim().equals("") && et_templateContent.isEnabled==true){
            //(mContext as DashboardActivity).showSnackMessage("Write template content")

            et_templateContent.requestFocus()
            et_templateContent.setError("Write template content")

            progress_wheel.stopSpinning()
            return
        }
        if(selectMode.text.toString().length==0 || selectMode.text.toString().trim().equals("")){
            (mContext as DashboardActivity).showSnackMessage("Select a mode")
            progress_wheel.stopSpinning()
            return
        }
        if(tv_rule_Of_scheduler.text.toString().length==0 || tv_rule_Of_scheduler.text.toString().trim().equals("")){
            (mContext as DashboardActivity).showSnackMessage("Select a rule")
            progress_wheel.stopSpinning()
            return
        }

       if (calendarView.selectedDates.size==0 &&  tv_rule_Of_scheduler.text.toString().trim().equals("Auto")){
           (mContext as DashboardActivity).showSnackMessage("Select a date")
           progress_wheel.stopSpinning()
            return
        }
        if(selectContactSchedule.text.toString().length==0 || selectContactSchedule.text.toString().trim().equals("")){
            (mContext as DashboardActivity).showSnackMessage("Select a contact")
            progress_wheel.stopSpinning()
            return
        }
        if(selectMode.text.toString().trim().equals("Email") && (Pref.storeGmailId==null || Pref.storeGmailPassword==null) && !selectMode.text.toString().equals("WhatsApp")){
            Toaster.msgShort(mContext,"Store your two step verification id & password")
            progress_wheel.stopSpinning()
            return
        }
        var curentTimeStamp = System.currentTimeMillis()
        for(j in 0..calendarView.selectedDates.size-1){
            particular_date_select = calendarView.selectedDates.get(j).year.toString() + "-" + String.format("%02d", calendarView.selectedDates.get(j).month.toInt()
            ) + "-" + String.format("%02d", calendarView.selectedDates.get(j).day)
            if (tv_rule_Of_scheduler.text.toString().equals("Auto")) {
                hr = String.format("%02d", selectedHr.replace(" h", "").toInt())
                min = String.format("%02d", selectedMin.replace(" min", "").toInt())

                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = simpleDateFormat.parse(particular_date_select + " $hr:$min:00")
                timestamp = date?.time
            }
        }
        if(timestamp!! <= curentTimeStamp && tv_rule_Of_scheduler.text.toString().equals("Auto")){
            (mContext as DashboardActivity).showSnackMessage("Set a schedule time after current time")
            progress_wheel.stopSpinning()
        }
        else {
            try {
                doAsync {
                    //new code begin
                    val random = Random()
                    var schObj = SchedulerMasterEntity()
                    schObj.scheduler_id = Pref.user_id + "_" + System.currentTimeMillis().toString() +  (random.nextInt(999 - 100) + 100).toString()
                    schObj.scheduler_name = schedulername.text.toString().trim()
                    schObj.select_template = selectTemplate.text.toString().trim()
                    schObj.template_content = et_templateContent.text.toString().trim()
                    schObj.select_mode_id = str_modeoftemplateID
                    schObj.select_mode = selectMode.text.toString().trim()
                    schObj.select_rule_id = str_ruleoftemplateID
                    schObj.select_rule = tv_rule_Of_scheduler.text.toString().trim()

                    schObj.save_date_time = AppUtils.getCurrentDateTimeNew()
                    schObj.save_modify_date_time = ""
                    schObj.isUploaded = true
                    if (isSwitchRepeat == true) {
                        schObj.repeat_every_month = true
                    }else{
                        schObj.repeat_every_month = false
                    }
                    if (schObj.select_rule.equals("Manual", ignoreCase = true)) {
                        schObj.isAutoMail = false
                    }else{
                        schObj.isAutoMail = true
                    }

                    var dateTimeL : ArrayList<SchedulerDateTimeEntity> = ArrayList()
                    if (schObj.select_rule.equals("Auto", ignoreCase = true)) {
                        hr = String.format("%02d", selectedHr.replace(" h","").toInt())
                        min = String.format("%02d", selectedMin.replace(" min","").toInt())

                        var dtL :ArrayList<String> = ArrayList()
                        for(j in 0..calendarView.selectedDates.size-1){
                            var dateTimeObj = SchedulerDateTimeEntity()
                            dateTimeObj.scheduler_id = schObj.scheduler_id
                            dateTimeObj.select_hour = selectedHr
                            dateTimeObj.select_minute = selectedMin
                            dateTimeObj.select_time = hr + ":" + min + ":00"

                            var y = calendarView.selectedDates.get(j).year.toString()
                            var m = String.format("%02d", calendarView.selectedDates.get(j).month.toInt())
                            var d = String.format("%02d", calendarView.selectedDates.get(j).day.toInt())
                            var dt =  y+ "-" + m + "-" + d
                            dateTimeObj.select_date = dt
                            dtL.add(dt)

                            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val date = simpleDateFormat.parse(dt + " $hr:$min:00")
                            val timestamp = date?.time

                            dateTimeObj.select_timestamp = timestamp.toString()
                            dateTimeL.add(dateTimeObj)
                        }
                        if(isSwitchRepeat){
                            for(i in 0..dtL.size-1){
                                var dobj = dtL.get(i)
                                for(j in 0..11){
                                    var dateTimeObj = SchedulerDateTimeEntity()
                                    dateTimeObj.scheduler_id = schObj.scheduler_id
                                    dateTimeObj.select_hour = selectedHr
                                    dateTimeObj.select_minute = selectedMin
                                    dateTimeObj.select_time = hr + ":" + min + ":00"

                                    dobj = AppUtils.addMonths(dobj)
                                    dateTimeObj.select_date = dobj

                                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    val date = simpleDateFormat.parse(dobj + " $hr:$min:00")
                                    val timestamp = date?.time

                                    dateTimeObj.select_timestamp = timestamp.toString()
                                    dateTimeL.add(dateTimeObj)
                                }
                            }
                        }
                        var tempDtL : List<SchedulerDateTimeEntity> = dateTimeL.distinct()
                        dateTimeL = ArrayList(tempDtL)
                    }
                    var contL : ArrayList<SchedulerContactEntity> = ArrayList()

                    for (j in 0..contactTickL.size - 1) {
                        contL.add(SchedulerContactEntity(scheduler_id =schObj.scheduler_id, select_contact_id = contactTickL.get(j).contact_id,
                            select_contact = contactTickL.get(j).contact_name, select_contact_number = contactTickL.get(j).contact_number))
                    }
                  //  finalL.filter { it.contact_name.equals(obj.contact_name) }.first().isTick = true
                  //  finalL.filter { it.contact_id.equals(obj.contact_id) }.first().isTick = true

                    schObj.sendingFilePath = filePath
                    AppDatabase.getDBInstance()!!.schedulerMasterDao().insert(schObj)
                    if(dateTimeL.size>0)
                        AppDatabase.getDBInstance()!!.schedulerDateTimeDao().insertAll(dateTimeL)
                    if(contL.size>0)
                        AppDatabase.getDBInstance()!!.schedulerContactDao().insertAll(contL)
                    //new code end

                    uiThread {
                        val simpleDialog = Dialog(mContext)
                        simpleDialog.setCancelable(false)
                        simpleDialog.getWindow()!!
                            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

                        val dialogHeader =
                            simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
                        dialogHeader.text = "Wow! Schedular configured successfully.\n" //+ "Communication with template will sent automatically."
                        val dialogYes =
                            simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
                        dialogYes.setOnClickListener({ view ->
                            simpleDialog.cancel()
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).onBackPressed()
                        })
                        //simpleDialog.show()

                        //new code begin
                        if(schObj.isAutoMail == false){
                            if(selectMode.text.toString().equals("WhatsApp")){
                                var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(contL.get(0).select_contact_id)
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

                                    val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest("https://graph.facebook.com/v18.0/110377482141989/messages", jsonObject,
                                        object : Response.Listener<JSONObject?> {
                                            override fun onResponse(response: JSONObject?) {
                                                //Toast.makeText(mContext, ""+response, Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        object : Response.ErrorListener {
                                            override fun onErrorResponse(error: VolleyError?) {
                                                //Toast.makeText(mContext, ""+error.toString(), Toast.LENGTH_SHORT).show()
                                            }
                                        }) {
                                        @Throws(AuthFailureError::class)
                                        override fun getHeaders(): Map<String, String> {
                                            val params: MutableMap<String, String> = HashMap()
                                            params["Authorization"] = "Bearer"+" "+"EAAYdZB0nzeMgBOxizV7tJeuilIZBwqyzn2PAfefBSiHNbaTtrz5Ce50NrYg6SJAqRYasC2rnPYJcZBhmSMEXllT9mtZAiZBzMScmv85EtnZBZAyltthc0GCHOBFgCdNC0oORzD3riXHSzlsjUvpWvOl02TCZCHbXmp0vDVjHuCghagM38Qsl3j3ZAEwXlhzrAY9hyZCAxrK0bH7Qxy1en7UTH0XpQ0ZBy0ZD"
                                            params["Content-Type"] = "application/json"
                                            return params
                                        }
                                    }
                                    MySingleton.getInstance(mContext)!!.addToRequestQueue(jsonObjectRequest)
                                    simpleDialog.show()
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                }


                                /*schObj.template_content = schObj.template_content.replace("@to name","@ToName").replace("@from name","@FromName")
                                schObj.template_content = schObj.template_content.replace("@toname","@ToName").replace("@fromname","@FromName")
                                schObj.template_content = schObj.template_content.replace("@Toname","@ToName").replace("@Fromname","@FromName")
                                schObj.template_content = schObj.template_content.replace("@To Name","@ToName").replace("@From Name","@FromName")
                                schObj.template_content = schObj.template_content.replace("@To name","@ToName").replace("@From name","@FromName")

                                schObj.template_content = schObj.template_content.replace("@ToName",shopObj.ownerName).replace("@FromName",Pref.user_name!!)

                                val url = "https://api.whatsapp.com/send?phone=+91${shopObj.ownerContactNumber}&text=${schObj.template_content}"
                                try {
                                    val pm = mContext.packageManager
                                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                                    val i = Intent(Intent.ACTION_VIEW)
                                    i.data = Uri.parse(url)
                                    startActivity(i)
                                } catch (e: PackageManager.NameNotFoundException ) {
                                    e.printStackTrace()
                                    (mContext as DashboardActivity).showSnackMessage("Whatsapp app not installed in your phone.")
                                    progress_wheel.stopSpinning()
                                }
                                catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                    (mContext as DashboardActivity).showSnackMessage("This is not whatsApp no.")
                                    progress_wheel.stopSpinning()
                                }*/
                            }else{
                                for (l in 0..contL.size - 1) {
                                    var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(contL.get(l).select_contact_id)
                                    //MultiFun.autoMailScheduler(shopObj.ownerEmailId, schObj.template_content,shopObj,schObj.scheduler_name)
                                    MultiFun.sendAutoMailWithFile(filePath,shopObj.ownerEmailId, schObj.template_content,shopObj,schObj.scheduler_name)
                                    /*MultiFun.autoMailScheduler1(shopObj.ownerEmailId, schObj.template_content,shopObj,schObj.scheduler_name,object :MultiFun.OnMailAction{
                                        override fun onStatus(isSuccess: Boolean) {
                                            var d = isSuccess
                                        }
                                    })*/
                                }
                                progress_wheel.stopSpinning()
                                //(mContext as DashboardActivity).onBackPressed()
                                simpleDialog.show()
                            }
                        }else{
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).onBackPressed()
                            simpleDialog.show()
                        }
                        //new code end
                    }
                }

            }
            catch (e: Exception) {
                progress_wheel.stopSpinning()
                e.printStackTrace()
            }
        }
    }
    private fun loadTemplateList(schedule_list:ArrayList<ScheduleTemplateEntity>) {
        if (schedule_list.size>0) {
            var genericL : ArrayList<CustomData> = ArrayList()
            for(i in 0..schedule_list.size-1){
                genericL.add(CustomData(schedule_list.get(i).template_id.toString(),schedule_list.get(i).template_name.toString()))
            }
            GenericDialog.newInstance("Template",genericL as ArrayList<CustomData>){
                var templateBody = AppDatabase.getDBInstance()?.scheduleTemplateDao()?.getByTemplate(it.id)
                selectTemplate.setText(templateBody!!.template_name)
                str_templateID = it.id
                if (!selectTemplate.text.toString().contains("Manual Template",ignoreCase = true)){
                    et_templateContent.isEnabled = false
                    Pref.scheduler_template=  templateBody!!.template_desc
                    et_templateContent.setText(Pref.scheduler_template)
                    et_templateContent.setTextColor(context?.getResources()!!.getColor(R.color.gray))
                }else{
                    et_templateContent.isEnabled =true
                    et_templateContent.setText("")
                }

            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        } else {
            Toaster.msgShort(mContext, "No Template Found")
        }
    }
    private fun loadModeOfTemplateList(mode_list:ArrayList<ModeTemplateEntity>) {
        if (mode_list.size>0) {
            var genericL : ArrayList<CustomData> = ArrayList()
            for(i in 0..mode_list.size-1){
                genericL.add(CustomData(mode_list.get(i).mode_template_id.toString(),mode_list.get(i).mode_template_name.toString()))
            }
            GenericDialog.newInstance("Mode of Template",genericL as ArrayList<CustomData>){
                selectMode.setText(it.name)
                str_modeoftemplateID = it.id
                if (selectMode.text!!.toString().trim().equals("Email")){
                    iv_row_scheduler_list_email_info.visibility=View.VISIBLE
                    cvAttachRoot.visibility=View.VISIBLE
                }
                else{
                    iv_row_scheduler_list_email_info.visibility=View.GONE
                    cvAttachRoot.visibility=View.GONE
                }

            }.show((mContext as DashboardActivity).supportFragmentManager, "")


        } else {
            Toaster.msgShort(mContext, "No Mode Found")
        }

    }
    private fun loadRuleOfTemplateList(rule_list:ArrayList<RuleTemplateEntity>) {
        if (rule_list.size>0) {
            var genericL : ArrayList<CustomData> = ArrayList()
            for(i in 0..rule_list.size-1){
                genericL.add(CustomData(rule_list.get(i).rule_template_id.toString(),rule_list.get(i).rule_template_name.toString()))
            }
            try{
                if(selectMode.text!!.toString().equals("WhatsApp")){
                    genericL.removeAt(0)
                }
            }catch (ex:Exception){
                ex.printStackTrace()
            }

            GenericDialog.newInstance("Rule of Template",genericL as ArrayList<CustomData>){
                tv_rule_Of_scheduler.setText(it.name)
                str_ruleoftemplateID = it.id
                if (tv_rule_Of_scheduler.text.toString().equals("Manual")){
                    cv_scheduler_timer.visibility = View.GONE
                    cv_calendar.visibility = View.GONE
                    sw_repeatmonth.visibility = View.GONE

                }else{

                    cv_scheduler_timer.visibility = View.VISIBLE
                    cv_calendar.visibility = View.VISIBLE
                    sw_repeatmonth.visibility = View.VISIBLE
                    setCal()
                    calendarView.clearSelection()


                }
            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        } else {
            Toaster.msgShort(mContext, "No Rule Found")
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun loadContactOfTemplateList() {

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
        //contactTickL = ArrayList()
        if (editShchedulerID!=""){
            // modify finalL
            var isContSelected = finalL.filter { it.isTick == true } as ArrayList<ScheduleContactDtls>
            if(isContSelected.size==0){
                    var scheduleContactL = AppDatabase.getDBInstance()!!.schedulerContactDao().getContDtlsBySchID(editShchedulerID) as ArrayList<SchedulerContactEntity>

                try{
                    for (l in 0..scheduleContactL.size - 1) {
                        finalL.filter { it.contact_id.equals(scheduleContactL.get(l).select_contact_id) }.first().isTick = true
                        var obj = scheduleContactL.get(l)
                        contactTickL.add(ScheduleContactDtls(obj.select_contact,obj.select_contact_number,obj.select_contact_id,true))
                    }
                    var tempL : List<ScheduleContactDtls> = contactTickL.distinctBy { it.contact_id }
                    contactTickL = ArrayList(tempL)
                    tvHeader.text = "Selected Contact(s) : (${contactTickL.size})"
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
            }
        }

        adapterScheduleContactName = AdapterScheduleContactName(mContext, finalL, object : AdapterScheduleContactName.onClick {
            override fun onTickUntick(obj: ScheduleContactDtls, isTick: Boolean) {
                if (isTick) {
                    contactTickL.add(obj)
                    //finalL.filter { it.contact_name.equals(obj.contact_name) }.first().isTick = true
                    finalL.filter { it.contact_id.equals(obj.contact_id) }.first().isTick = true

                    tvHeader.text = "Selected Contact(s) : (${contactTickL.size})"


                } else {
                    //contactTickL.remove(obj)
                    contactTickL.removeIf { it.contact_id.equals(obj.contact_id) }
                    //finalL.filter { it.contact_name.equals(obj.contact_name) }.first().isTick = false
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
                    if (editShchedulerID!="" && contactTickL.size == 0){
                        Toast.makeText(mContext, "Please select atleast one contact", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
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
                        selectContactSchedule.setText(nameText)
                    }
                    else{
                            contactDialog.dismiss()
                            selectContactSchedule.setText("")
                            //Toaster.msgShort(mContext, "Select Contact(s)")
                        }
                    }

                rvContactL.adapter = adapterScheduleContactName
                contactDialog.show()

    }

    private fun setEditData() {

        var schedulerObj =  AppDatabase.getDBInstance()!!.schedulerMasterDao().getSchedulerDtls(editShchedulerID)
        schedulername.setText(schedulerObj.scheduler_name)
        schedulername.isEnabled = false
        selectTemplate.setText(schedulerObj.select_template)
        et_templateContent.setText(schedulerObj.template_content)
        selectMode.setText(schedulerObj.select_mode)
        if (selectMode.text!!.toString().trim().equals("Email")){
            iv_row_scheduler_list_email_info.visibility=View.VISIBLE
        }
        else{
            iv_row_scheduler_list_email_info.visibility=View.GONE
        }
        tv_rule_Of_scheduler.setText(schedulerObj.select_rule)
        
        if(schedulerObj.select_rule.equals("Auto")){
            cv_scheduler_timer.visibility = View.VISIBLE
            cv_calendar.visibility = View.VISIBLE
            sw_repeatmonth.visibility = View.VISIBLE

            setCal()
        }else{
            cv_scheduler_timer.visibility = View.GONE
            cv_calendar.visibility = View.GONE
            sw_repeatmonth.visibility = View.GONE
        }
        var scheduleContactL = AppDatabase.getDBInstance()!!.schedulerContactDao().getContDtlsBySchID(editShchedulerID) as ArrayList<SchedulerContactEntity>
        var nameText = ""
        if(scheduleContactL.size==1){
            var ob = scheduleContactL.get(0)
            contactTickL.add(ScheduleContactDtls(ob.select_contact,ob.select_contact_number,ob.select_contact_id,true))
            nameText = scheduleContactL.get(0).select_contact
        }else{
            for(i in 0..scheduleContactL.size-1) {
                var ob = scheduleContactL.get(i)
                contactTickL.add(ScheduleContactDtls(ob.select_contact,ob.select_contact_number,ob.select_contact_id,true))
                nameText = nameText+scheduleContactL.get(i).select_contact+","
            }
        }
        if (nameText.endsWith(",")) {
            nameText = nameText.substring(0, nameText.length - 1);
        }
        selectContactSchedule.setText(nameText)
        tvChoosenFileName.visibility = View.VISIBLE
        var fName = File(schedulerObj.sendingFilePath).name
        tvChoosenFileName.setText(fName)
        filePath=schedulerObj.sendingFilePath
    }


    fun setImage(fPath: String) {
        filePath = fPath
        val file = File(fPath)

        var fName = File(fPath).name
        tvChoosenFileName.visibility = View.VISIBLE
        tvChoosenFileName.text = fName.toString()
        return

        FullImageDialog.getInstance(file.path).show((mContext as DashboardActivity).supportFragmentManager, "")

        var newFile: File? = null

        progress_wheel.spin()
        doAsync {
            val processImage = ProcessImageUtils_v1(mContext, file, 50, true)
            newFile = processImage.ProcessImage()
            uiThread {
                if (newFile != null) {
                    Timber.e("=========Image from new technique==========")
                    documentPic(newFile!!.length(), newFile?.absolutePath!!)
                } else {
                    // Image compression
                    val fileSize = AppUtils.getCompressImage(filePath)
                    documentPic(fileSize, filePath)
                }
            }
        }
    }
    private fun documentPic(fileSize: Long, file_path: String) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Add Document", "image file size after compression=====> $fileSizeInKB KB")

        progress_wheel.stopSpinning()

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB > Pref.maxFileSize.toInt()) {
                (mContext as DashboardActivity).showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
                return
            }
        }

        filePath = file_path
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun setTemplateData(){
        if((AppDatabase.getDBInstance()?.scheduleTemplateDao()?.getAll() as ArrayList<ScheduleTemplateEntity>).size == 0){
            var obj = ScheduleTemplateEntity()
            obj.template_id = Pref.user_id+System.currentTimeMillis().toString()
            obj.template_name = "Manual Template"
            obj.template_desc = ""
            AppDatabase.getDBInstance()?.scheduleTemplateDao()?.insert(obj)
        }
    }
    private fun setModeData(){
        AppDatabase.getDBInstance()?.modeTemplateDao()?.deleteAll()
        if((AppDatabase.getDBInstance()?.modeTemplateDao()?.getAll() as ArrayList<ModeTemplateEntity>).size == 0){
            var objMode1 = ModeTemplateEntity()
            objMode1.mode_template_id = 1
            objMode1.mode_template_name = "WhatsApp"

            var objMode2 = ModeTemplateEntity()
            objMode2.mode_template_id = 2
            objMode2.mode_template_name = "Email"
            AppDatabase.getDBInstance()?.modeTemplateDao()?.insert(objMode1)
            AppDatabase.getDBInstance()?.modeTemplateDao()?.insert(objMode2)
        }
    }
    private fun setRuleData(){
        if((AppDatabase.getDBInstance()?.ruleTemplateDao()?.getAll() as ArrayList<RuleTemplateEntity>).size == 0){
            var objRule1 = RuleTemplateEntity()
            objRule1.rule_template_id = 1
            objRule1.rule_template_name = "Auto"

            var objRule2 = RuleTemplateEntity()
            objRule2.rule_template_id = 2
            objRule2.rule_template_name = "Manual"
            AppDatabase.getDBInstance()?.ruleTemplateDao()?.insert(objRule1)
            AppDatabase.getDBInstance()?.ruleTemplateDao()?.insert(objRule2)
        }
    }

    fun showDialogGmailAuthPic(){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_gmail_auth_pic)
        val ivView = simpleDialog.findViewById(R.id.tv_dialog_gmail_auth_img) as ImageView
        val cvNext = simpleDialog.findViewById(R.id.cv_dialog_gmail_auth_next) as CardView
        val tvNext = simpleDialog.findViewById(R.id.tv_dialog_gmail_auth_next) as TextView

        val cvPrev = simpleDialog.findViewById(R.id.cv_dialog_gmail_auth_prev) as CardView
        val tvPrev = simpleDialog.findViewById(R.id.tv_dialog_gmail_auth_prev) as TextView
        val ivCancel = simpleDialog.findViewById(R.id.iv_generic_dialog_cancel) as ImageView

        ivCancel.setOnClickListener {
            simpleDialog.dismiss()
        }

        cvPrev.visibility = View.GONE

        var picLoc = 1
        tvNext.text = "Next(1/10)"
        ivView.setImageResource(R.drawable.img_gmail_auth_1)
        cvNext.setOnClickListener {
            picLoc++
            cvPrev.visibility = View.VISIBLE
            if(picLoc==2){
                tvNext.text = "Next(2/10)"
                tvPrev.text = "Previous(2/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_2)
            }
            if(picLoc==3){
                tvNext.text = "Next(3/10)"
                tvPrev.text = "Previous(3/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_3)
            }
            if(picLoc==4){
                tvNext.text = "Next(4/10)"
                tvPrev.text = "Previous(4/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_4)
            }
            if(picLoc==5){
                tvNext.text = "Next(5/10)"
                tvPrev.text = "Previous(5/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_5)
            }
            if(picLoc==6){
                tvNext.text = "Next(6/10)"
                tvPrev.text = "Previous(6/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_6)
            }
            if(picLoc==7){
                tvNext.text = "Next(7/10)"
                tvPrev.text = "Previous(7/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_7)
            }
            if(picLoc==8){
                tvNext.text = "Next(8/10)"
                tvPrev.text = "Previous(8/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_8)
            }
            if(picLoc==9){
                tvNext.text = "Next(9/10)"
                tvPrev.text = "Previous(9/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_9)
            }
            if(picLoc==10){
                tvNext.text = "Done(10/10)"
                tvPrev.text = "Previous(10/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_10)
            }
            if(picLoc==11){
                simpleDialog.dismiss()
                showDialogOfGmailNw()
            }
        }
        cvPrev.setOnClickListener {
            picLoc--
            cvPrev.visibility = View.VISIBLE
            if(picLoc==1){
                cvPrev.visibility = View.GONE
                tvNext.text = "Next(1/10)"
                tvPrev.text = "Previous(2/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_1)
            }
            if(picLoc==2){
                tvNext.text = "Next(2/10)"
                tvPrev.text = "Previous(2/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_2)
            }
            if(picLoc==3){
                tvNext.text = "Next(3/10)"
                tvPrev.text = "Previous(3/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_3)
            }
            if(picLoc==4){
                tvNext.text = "Next(4/10)"
                tvPrev.text = "Previous(4/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_4)
            }
            if(picLoc==5){
                tvNext.text = "Next(5/10)"
                tvPrev.text = "Previous(5/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_5)
            }
            if(picLoc==6){
                tvNext.text = "Next(6/10)"
                tvPrev.text = "Previous(6/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_6)
            }
            if(picLoc==7){
                tvNext.text = "Next(7/10)"
                tvPrev.text = "Previous(7/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_7)
            }
            if(picLoc==8){
                tvNext.text = "Next(8/10)"
                tvPrev.text = "Previous(8/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_8)
            }
            if(picLoc==9){
                tvNext.text = "Next(9/10)"
                tvPrev.text = "Previous(9/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_9)
            }
            if(picLoc==10){
                tvNext.text = "Done(10/10)"
                tvPrev.text = "Previous(10/10)"
                ivView.setImageResource(R.drawable.img_gmail_auth_10)
            }
            if(picLoc==11){
                simpleDialog.dismiss()
                showDialogOfGmailNw()
            }
        }
        simpleDialog.show()
    }

     fun showDialogOfGmailNw() {

        var instructionDialog = Dialog(mContext)
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
        val iv_dialog_instruction_copy = instructionDialog!!.findViewById(R.id.iv_dialog_instruction_copy) as ImageView
        val iv_email_info = instructionDialog!!.findViewById(R.id.iv_email_info) as ImageView
        val lin_credentials_gmail = instructionDialog!!.findViewById(R.id.lin_credentials_gmail) as LinearLayout

        val ivFinger = instructionDialog!!.findViewById(R.id.iv_dialog_gmail_inst_pointer) as ImageView
        Glide.with(mContext)
            .load(R.drawable.icon_pointer_gif)
            .into(ivFinger)

        iv_dialog_instruction_copy.visibility= View.GONE
        iv_email_info.visibility= View.VISIBLE
        iv_email_info.setOnClickListener {
            showInstructionOfTwoStepVerificationNw()
        }

        iv_dialog_instruction_copy.setOnClickListener {
            val clipboard: ClipboardManager? = mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", tv_instruction.text.toString().trim())
            clipboard!!.setPrimaryClip(clip)
            Toaster.msgLong(mContext,"Copied to Clipboard")
        }
        tv_save_instruction.setOnClickListener {
            if (et_user_gmail_id.text.toString().equals("") && et_user_password.text.toString().trim().equals("")) {
                Toast.makeText(
                    mContext,
                    "Put your credentials",
                    Toast.LENGTH_LONG
                ).show()
            }
            else{
                Pref.storeGmailId = et_user_gmail_id.text.toString().trim()
                Pref.storeGmailPassword = et_user_password.text.toString().trim()
                // After save 2 step verification
                instructionDialog!!.dismiss()

            }
        }
        iv_close.setOnClickListener {
            instructionDialog!!.dismiss()
        }
        rvContactGrName.visibility=View.GONE
        lin_credentials_gmail.visibility=View.VISIBLE
        tv_save_instruction.visibility=View.VISIBLE
        tv_headerOfSetVerification.visibility=View.VISIBLE
        tv_instruction.visibility=View.GONE
        tvHeader.text = "E-mail configuration"
        instructionDialog!!.show()
    }

     fun showInstructionOfTwoStepVerificationNw() {

        var instructionDialog = Dialog(mContext)
        instructionDialog!!.setCancelable(false)
        instructionDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        instructionDialog!!.setContentView(R.layout.dialog_gmail_instruction_only)
        val tvHeader = instructionDialog!!.findViewById(R.id.dialog_contact_gr_header) as TextView
        val tv_instruction = instructionDialog!!.findViewById(R.id.tv_instruction) as TextView
        val tv_save_instruction = instructionDialog!!.findViewById(R.id.tv_save_instruction) as TextView
        val iv_dialog_instruction_copy = instructionDialog!!.findViewById(R.id.iv_dialog_instruction_copy) as ImageView
        val iv_close = instructionDialog!!.findViewById(R.id.iv_dialog_instruction_close_icon) as ImageView

        val tv_ulink = instructionDialog!!.findViewById(R.id.tv_ulink) as TextView
        tv_ulink.setOnClickListener {
            var youtubeID = "dM_DlzyeWW8"
            val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeID))
            val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeID))
            try {
                this.startActivity(intentApp)
            } catch (ex: ActivityNotFoundException) {
                this.startActivity(intentBrowser)
            }
        }
        iv_dialog_instruction_copy.setOnClickListener {
            val clipboard: ClipboardManager? = mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", tv_instruction.text.toString().trim())
            clipboard!!.setPrimaryClip(clip)
            Toaster.msgLong(mContext,"Copied to Clipboard")
        }

        tv_save_instruction.setOnClickListener {
            instructionDialog.dismiss()
        }
        iv_close.setOnClickListener {
            instructionDialog.dismiss()
        }
        tvHeader.text = "E-mail configuration info"
        instructionDialog!!.show()
    }

}