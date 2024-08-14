package com.breezenationalplasticfsm.features.contacts

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.breezenationalplasticfsm.EventDayDecorator
import com.breezenationalplasticfsm.EventDayDecorator1
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.SearchListener
import com.breezenationalplasticfsm.app.domain.ScheduleTemplateEntity
import com.breezenationalplasticfsm.app.domain.SchedulerContactEntity
import com.breezenationalplasticfsm.app.domain.SchedulerDateTimeEntity
import com.breezenationalplasticfsm.app.domain.SchedulerMasterEntity
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.app.widgets.MovableFloatingActionButton
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import com.pnikosis.materialishprogress.ProgressWheel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.row_quto_multi_cont.view.cb_row_quto_multi_cont
import kotlinx.android.synthetic.main.row_shop_list_ma.view.iv_row_shop_list_ma_pointer
import org.w3c.dom.Text

class SchedulerViewFrag : BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var rvSchedulL: RecyclerView
    private lateinit var ll_frag_sch_add_template: LinearLayout
    private lateinit var mFab: MovableFloatingActionButton
    private lateinit var adapterSchedulerList: AdapterSchedulerList

    private lateinit var tv_empty_page_msg_head:TextView
    private lateinit var tv_empty_page_msg:TextView
    private lateinit var ll_no_data_root:LinearLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object{
        fun getInstance(objects: Any): SchedulerViewFrag {
            val objFragment = SchedulerViewFrag()
            var obj = objects as String

            return objFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_scheduler_add, container, false)
        initView(view)

        //Search scheduler list by puja
        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    //val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
                   // val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().getAllOwn(true)
                    val schedulerL = AppDatabase.getDBInstance()!!.schedulerMasterDao().getAll() as ArrayList<SchedulerMasterEntity>

                    callSchedulerListSearch(schedulerL)

                   // tv_shop_count.text = "Total " + Pref.shopText + "(s): " + list.size

                    if (adapterSchedulerList != null)
                        adapterSchedulerList.updateAdapter(schedulerL)
                }
                else {
//                    val searchedList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBySearchData(query)
                   // var searchedList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBySearchDataNew(query)
                    var searchedList = AppDatabase.getDBInstance()!!.schedulerMasterDao().getSchedulerBySearchDataNew(query)

                    callSchedulerListSearch(searchedList)

                  //  tv_shop_count.text = "Total " + Pref.shopText + "(s): " + list.size

                    if (adapterSchedulerList != null)
                        adapterSchedulerList.updateAdapter(searchedList)
                }

            }

        })

     /*   var schObj =  AppDatabase.getDBInstance()!!.schedulerMasterDao().getAll()
        for (l in 0..schObj.size - 1) {

          //  var isRepeatOfMonthInSchedulerEnable =  schObj.get(l).isRepeatOfMonthInSchedulerEnable
        }*/
            return view
    }

    private fun callSchedulerListSearch(schedulerL: List<SchedulerMasterEntity>) {

        progress_wheel.spin()
        val newList = ArrayList<SchedulerMasterEntity>()

        for (i in schedulerL.indices) {
            /*val userId = allShopList[i].shop_id.substring(0, allShopList[i].shop_id.indexOf("_"))
            if (userId == Pref.user_id)*/
            newList.add(schedulerL[i])
        }

        progress_wheel.stopSpinning()
    }

    private fun initView(view: View) {
        progress_wheel = view.findViewById(R.id.progress_wheel_frag_schedule_his_list)
        rvSchedulL = view.findViewById(R.id.rv_frag_scheduler_list)
        tv_empty_page_msg_head = view.findViewById(R.id.tv_empty_page_msg_head)
        tv_empty_page_msg = view.findViewById(R.id.tv_empty_page_msg)
        ll_no_data_root = view.findViewById(R.id.ll_no_data_root)
        mFab = view.findViewById(R.id.fab_frag_add_schedule_in_contact)
        ll_frag_sch_add_template = view.findViewById(R.id.ll_frag_sch_add_template)

        ll_frag_sch_add_template.setOnClickListener {
            (mContext as DashboardActivity).loadFragment(FragType.TemplateViewFrag, true, "")
        }

        mFab.setOnClickListener(this)

        mFab.setCustomClickListener {
            var templateList = AppDatabase.getDBInstance()?.scheduleTemplateDao()?.getAllExceptManually() as ArrayList<ScheduleTemplateEntity>
            if ( (Pref.storeGmailId==null || Pref.storeGmailPassword==null) && false){
                val simpleDialog = Dialog(mContext)
                simpleDialog.setCancelable(false)
                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialog.setContentView(R.layout.generic_dialog)
                val dialogHeader = simpleDialog.findViewById(R.id.tv_generic_dialog_header) as AppCustomTextView
                val dialogBody = simpleDialog.findViewById(R.id.tv_generic_dialog_body) as AppCustomTextView
                val tvOk = simpleDialog.findViewById(R.id.tv_generic_dialog_ok) as AppCustomTextView
                val ivCancel = simpleDialog.findViewById(R.id.iv_generic_dialog_cancel) as ImageView

                dialogHeader.text = AppUtils.hiFirstNameText()
                dialogBody.text = "Hi! You may use Automatic email facility from Gmail Account with scheduled date. For the same, Please provide the Gmail account Login ID and Two Step Verification password to configure auto email."

                tvOk.setOnClickListener({ view ->
                    simpleDialog.cancel()
                    //showDialogOfGmail()
                    showDialogGmailAuthPic()
                })
                ivCancel.setOnClickListener {
                    simpleDialog.cancel()
                }
                simpleDialog.show()
            }else if(templateList.size==0 && Pref.isTemplateDialogAlertShow){
                showTemplateDialog()
            }else {
                (mContext as DashboardActivity).loadFragment(FragType.SchedulerAddFormFrag, true, "")
            }
        }

        setTemplateData()
        callSchedulerList()
        Handler().postDelayed(Runnable {
            checkConfig()
        }, 300)

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

    fun checkConfig(){
        var templateList = AppDatabase.getDBInstance()?.scheduleTemplateDao()?.getAllExceptManually() as ArrayList<ScheduleTemplateEntity>
        if ( Pref.storeGmailId==null || Pref.storeGmailPassword==null){
            val simpleDialog = Dialog(mContext)
            simpleDialog.setCancelable(false)
            simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialog.setContentView(R.layout.generic_dialog)
            val dialogHeader = simpleDialog.findViewById(R.id.tv_generic_dialog_header) as AppCustomTextView
            val dialogBody = simpleDialog.findViewById(R.id.tv_generic_dialog_body) as AppCustomTextView
            val tvOk = simpleDialog.findViewById(R.id.tv_generic_dialog_ok) as AppCustomTextView
            val ivCancel = simpleDialog.findViewById(R.id.iv_generic_dialog_cancel) as ImageView

            dialogHeader.text = AppUtils.hiFirstNameText()
            dialogBody.text = "Hi! You may use Automatic email facility from Gmail Account with scheduled date. For the same, Please provide the Gmail account Login ID and Two Step Verification password to configure auto email."

            tvOk.setOnClickListener({ view ->
                simpleDialog.cancel()
                //showDialogOfGmail()
                showDialogGmailAuthPic()
            })
            ivCancel.setOnClickListener {
                simpleDialog.cancel()
            }
            simpleDialog.show()
        }else if(templateList.size==0 && Pref.isTemplateDialogAlertShow){
            showTemplateDialog()
        }else {
            //(mContext as DashboardActivity).loadFragment(FragType.SchedulerAddFormFrag, true, "")
        }
    }

    fun showTemplateDialog(){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_yes_no_1)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
        val dialogBody = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
        val tvYes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
        val tvNo = simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
        val cbDontShow = simpleDialog.findViewById(R.id.cb_dialog_y_n) as CheckBox

        dialogHeader.text = AppUtils.hiFirstNameText()
        dialogBody.text = "Hi! To use readymade template in automatic email, please first define the template. Wish to create it now? Please select OK or otherwise select Cancel to create schedular without readymade template."

        tvNo.setOnClickListener {
            simpleDialog.dismiss()
            (mContext as DashboardActivity).loadFragment(FragType.SchedulerAddFormFrag, true, "")
        }
        tvYes.setOnClickListener {
            simpleDialog.dismiss()
            (mContext as DashboardActivity).loadFragment(FragType.TemplateAddFrag, true, "")
        }

        cbDontShow.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                Pref.isTemplateDialogAlertShow = false
            }else{
                Pref.isTemplateDialogAlertShow = true
            }
        }

        simpleDialog.show()
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
                showDialogOfGmail()
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
                showDialogOfGmail()
            }
        }
        simpleDialog.show()
    }

    private fun showDialogOfGmail() {

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

            showInstructionOfTwoStepVerification()

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

    fun callSchedulerList() {
        var schedulerL : ArrayList<SchedulerMasterEntity> = ArrayList()
        schedulerL = AppDatabase.getDBInstance()!!.schedulerMasterDao().getAll() as ArrayList<SchedulerMasterEntity>

        if(schedulerL.size>0){
            ll_no_data_root.visibility = View.GONE
            (mContext as DashboardActivity).setTopBarTitle("Scheduler(s) : ${schedulerL.size}")
            adapterSchedulerList = AdapterSchedulerList(mContext,schedulerL,"",object :AdapterSchedulerList.onClick{
                override fun onWhatsClick(obj: SchedulerMasterEntity) {
                }

                override fun onEmailClick(obj: SchedulerMasterEntity) {
                   // IntentActionable.sendMail(mContext, obj.ownerEmailId, "")
                }

                override fun onSelectedDateInfoClick(obj: SchedulerMasterEntity) {
                    calView(obj)
                    return
                    var scheduleDateL = AppDatabase.getDBInstance()!!.schedulerDateTimeDao().getAll(obj.scheduler_id) as ArrayList<SchedulerDateTimeEntity>
                    var scheduleDtls = AppDatabase.getDBInstance()!!.schedulerMasterDao().getSchedulerDtls(obj.scheduler_id) as SchedulerMasterEntity

                    val simpleDialog = Dialog(mContext)
                    simpleDialog.setCancelable(false)
                    simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialog.setContentView(R.layout.dialog_info2)
                    val dialogHeader = simpleDialog.findViewById(R.id.tv_dialog_info_2_header) as TextView
                    val dialogCross = simpleDialog.findViewById(R.id.iv_dialog_info_2_cross) as ImageView
                    val tv_NoData = simpleDialog.findViewById(R.id.tv_dialog_info_2_info_na) as TextView
                    val tv_dialog_info_2_info_content = simpleDialog.findViewById(R.id.tv_dialog_info_2_info_content) as TextView
                    val tv_subject = simpleDialog.findViewById(R.id.tv_subject) as TextView
                    val rv_list = simpleDialog.findViewById(R.id.rv_dialog_info_2_info) as RecyclerView

                    dialogHeader.text = scheduleDtls.scheduler_name
                    tv_NoData.visibility = View.GONE
                    tv_dialog_info_2_info_content.visibility = View.GONE
                    tv_subject.text = "Selected Dates : "
                    rv_list.adapter = AdapterGenericData(mContext,scheduleDateL.map { it.select_date + " : "+it.select_time +" "+if(it.isDone) "(Activity Done)" else "" } as ArrayList<String>)
                    dialogCross.setOnClickListener({ view ->
                        simpleDialog.cancel()
                    })
                    simpleDialog.show()

                }

                override fun onSelectedDateInfoClickForManual(obj: SchedulerMasterEntity) {

                    calViewForManual(obj)
                    return
                }

                override fun onSelectedContactClick(obj: SchedulerMasterEntity) {
                    var scheduleContactL = AppDatabase.getDBInstance()!!.schedulerContactDao().getContDtlsBySchID(obj.scheduler_id) as ArrayList<SchedulerContactEntity>
                    var scheduleDtls = AppDatabase.getDBInstance()!!.schedulerMasterDao().getSchedulerDtls(obj.scheduler_id) as SchedulerMasterEntity

                    val simpleDialog = Dialog(mContext)
                    simpleDialog.setCancelable(false)
                    simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialog.setContentView(R.layout.dialog_info2)
                    val dialogHeader = simpleDialog.findViewById(R.id.tv_dialog_info_2_header) as TextView
                    val dialogCross = simpleDialog.findViewById(R.id.iv_dialog_info_2_cross) as ImageView
                    val tv_NoData = simpleDialog.findViewById(R.id.tv_dialog_info_2_info_na) as TextView
                    val tv_dialog_info_2_info_content = simpleDialog.findViewById(R.id.tv_dialog_info_2_info_content) as TextView
                    val tv_subject = simpleDialog.findViewById(R.id.tv_subject) as TextView
                    val rv_list = simpleDialog.findViewById(R.id.rv_dialog_info_2_info) as RecyclerView

                    dialogHeader.text = scheduleDtls.scheduler_name
                    tv_NoData.visibility = View.GONE
                    tv_dialog_info_2_info_content.visibility = View.GONE
                    tv_subject.text = "Selected Contacts : "
                    var contactNamePhoneL = scheduleContactL.map { it.select_contact +" ("+ it.select_contact_number+")" } as ArrayList<String>
                    rv_list.adapter = AdapterGenericData(mContext,contactNamePhoneL)
                    dialogCross.setOnClickListener({ view ->
                        simpleDialog.cancel()
                    })
                    simpleDialog.show()
                }

                override fun onSchedulerContentInfoClick(obj: SchedulerMasterEntity) {
                    var scheduleDtls = AppDatabase.getDBInstance()!!.schedulerMasterDao().getSchedulerDtls(obj.scheduler_id) as SchedulerMasterEntity

                    val simpleDialog = Dialog(mContext)
                    simpleDialog.setCancelable(false)
                    simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialog.setContentView(R.layout.dialog_info2)
                    val dialogHeader = simpleDialog.findViewById(R.id.tv_dialog_info_2_header) as TextView
                    val dialogCross = simpleDialog.findViewById(R.id.iv_dialog_info_2_cross) as ImageView
                    val tv_NoData = simpleDialog.findViewById(R.id.tv_dialog_info_2_info_na) as TextView
                    val tv_dialog_info_2_info_content = simpleDialog.findViewById(R.id.tv_dialog_info_2_info_content) as TextView
                    val tv_subject = simpleDialog.findViewById(R.id.tv_subject) as TextView
                    val rv_list = simpleDialog.findViewById(R.id.rv_dialog_info_2_info) as RecyclerView

                    dialogHeader.text = scheduleDtls.scheduler_name
                    rv_list.visibility = View.GONE
                    tv_NoData.visibility = View.GONE
                    tv_dialog_info_2_info_content.visibility = View.VISIBLE
                    tv_subject.text = "Scheduler Content : "
                    tv_dialog_info_2_info_content.text = scheduleDtls.template_content.trim()
                    dialogCross.setOnClickListener({ view ->
                        simpleDialog.cancel()
                    })
                    simpleDialog.show()
                }

                override fun onDeleteClick(obj: SchedulerMasterEntity) {
                  /*  AppUtils.addMonths("2024-12-18")
                    return*/

                    var simpleDialog = Dialog(mContext)
                    simpleDialog.setCancelable(false)
                    simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialog.setContentView(R.layout.dialog_yes_no)
                    val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                    val dialogBody = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                    val btn_no = simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
                    val btn_yes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView

                    dialogHeader.text = AppUtils.hiFirstNameText() + "!"
                    dialogBody.text = "Do you want to delete this Scheduler?"

                    btn_yes.setOnClickListener({ view ->
                        progress_wheel.spin()
                        AppDatabase.getDBInstance()!!.schedulerMasterDao().deleteScheduler(obj.scheduler_id)
                        AppDatabase.getDBInstance()!!.schedulerDateTimeDao().deleteScheduler(obj.scheduler_id)
                        AppDatabase.getDBInstance()!!.schedulerContactDao().deleteScheduler(obj.scheduler_id)
                        simpleDialog.cancel()

                        Handler().postDelayed(Runnable {
                            progress_wheel.stopSpinning()
                            callSchedulerList()
                        }, 700)
                    })
                    btn_no.setOnClickListener({ view ->
                        simpleDialog.cancel()
                    })
                    simpleDialog.show()
                }

                override fun onEditClick(obj: SchedulerMasterEntity) {
                    (mContext as DashboardActivity).loadFragment(FragType.SchedulerAddFormFrag, true, obj.scheduler_id)
                }
            })
            rvSchedulL.adapter = adapterSchedulerList
            rvSchedulL.visibility = View.VISIBLE
        }else{
            (mContext as DashboardActivity).setTopBarTitle("Contact(s)")
            ll_no_data_root.visibility = View.VISIBLE
            tv_empty_page_msg_head.text = "No Scheduler Found"
            tv_empty_page_msg.text = "Click + to add your Scheduler"
            rvSchedulL.visibility = View.GONE
        }

    }

    override fun onClick(p0: View?) {

    }

    fun calView(obj: SchedulerMasterEntity){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_cal_view)
        val calView = simpleDialog.findViewById(R.id.calendarView_dt) as MaterialCalendarView
        val tvHeader = simpleDialog.findViewById(R.id.tv_dialog_cal_view_header) as TextView
        val tvTime = simpleDialog.findViewById(R.id.tv_dialog_cal_view_time) as TextView
        val ivCross = simpleDialog.findViewById(R.id.iv_dialog_cal_view_cross) as ImageView
        calView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE)
        tvHeader.text = AppUtils.hiFirstNameText()

        var mEventDaysDone: ArrayList<CalendarDay> = ArrayList()
        var mEventDaysNotDone: ArrayList<CalendarDay> = ArrayList()
        var scheduleDateL = AppDatabase.getDBInstance()!!.schedulerDateTimeDao().getAll(obj.scheduler_id) as ArrayList<SchedulerDateTimeEntity>

        tvTime.text = "Schedule Time : "+scheduleDateL.get(0).select_time

        for(i in 0..scheduleDateL.size-1){
            var y = scheduleDateL.get(i).select_date.split("-").get(0).toInt()
            var m = scheduleDateL.get(i).select_date.split("-").get(1).toInt()
            var d = scheduleDateL.get(i).select_date.split("-").get(2).toInt()
            if(scheduleDateL.get(i).isDone){
                mEventDaysDone.add(CalendarDay.from(y,m,d))
            }else{
                mEventDaysNotDone.add(CalendarDay.from(y,m,d))
            }
            //calView.setDateSelected(CalendarDay.from(y,m,d), true);
        }


        calView.addDecorator(EventDayDecorator(mContext, mEventDaysDone))
        calView.addDecorator(EventDayDecorator1(mContext, mEventDaysNotDone))

        ivCross.setOnClickListener { simpleDialog.dismiss() }

        simpleDialog.show()

    }

    fun calViewForManual(obj: SchedulerMasterEntity){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_cal_view)
        val calView = simpleDialog.findViewById(R.id.calendarView_dt) as MaterialCalendarView
        val tvHeader = simpleDialog.findViewById(R.id.tv_dialog_cal_view_header) as TextView
        val tvTime = simpleDialog.findViewById(R.id.tv_dialog_cal_view_time) as TextView
        val ivCross = simpleDialog.findViewById(R.id.iv_dialog_cal_view_cross) as ImageView
        calView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE)
        tvHeader.text = AppUtils.hiFirstNameText()

        var mEventDaysDone: ArrayList<CalendarDay> = ArrayList()
        var mEventDaysNotDone: ArrayList<CalendarDay> = ArrayList()
        //var scheduleDateL = AppDatabase.getDBInstance()!!.schedulerDateTimeDao().getAll(obj.scheduler_id) as ArrayList<SchedulerDateTimeEntity>
        var strParts = obj.save_date_time!!.toString().trim() . split(" ")
        var strFirstStringpart1 :String = strParts[0]
        var strFirstStringpart2 :String = strParts[1]

        tvTime.text = "Schedule Time : "+strFirstStringpart2

            var y = strFirstStringpart1.split("-").get(0).toInt()
            var m = strFirstStringpart1.split("-").get(1).toInt()
            var d = strFirstStringpart1.split("-").get(2).toInt()
                mEventDaysDone.add(CalendarDay.from(y,m,d))

        calView.addDecorator(EventDayDecorator(mContext, mEventDaysDone))
        calView.addDecorator(EventDayDecorator1(mContext, mEventDaysNotDone))

        ivCross.setOnClickListener { simpleDialog.dismiss() }

        simpleDialog.show()

    }


}