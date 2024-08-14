package com.breezenationalplasticfsm.features.taskManagement

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.breezenationalplasticfsm.CustomStatic
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.ActivityDropDownEntity
import com.breezenationalplasticfsm.app.domain.TypeEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.activities.api.ActivityRepoProvider
import com.breezenationalplasticfsm.features.activities.model.ActivityDropdownListResponseModel
import com.breezenationalplasticfsm.features.activities.model.TypeListResponseModel
import com.breezenationalplasticfsm.features.activities.presentation.ActivityListDialog
import com.breezenationalplasticfsm.features.activities.presentation.TypeListDialog
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.lead.adapter.ViewActivityAdapter
import com.breezenationalplasticfsm.features.lead.api.GetLeadRegProvider
import com.breezenationalplasticfsm.features.lead.dialog.EnqListDialog
import com.breezenationalplasticfsm.features.lead.model.*
import com.breezenationalplasticfsm.features.photoReg.model.UserListResponseModel
import com.breezenationalplasticfsm.features.report.model.TargetVsAchvDataModel
import com.breezenationalplasticfsm.features.report.presentation.TargetVsAchvDetailsFragment
import com.breezenationalplasticfsm.features.taskManagement.adapter.ViewTaskActivityAdapter
import com.breezenationalplasticfsm.features.taskManagement.model.TaskManagmentEntity
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import com.downloader.Progress
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login_new.*
import kotlinx.android.synthetic.main.dialog_reason.*
import kotlinx.android.synthetic.main.fragment_add_activity.*
import kotlinx.android.synthetic.main.fragment_add_pjp.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

// create by saheli 05-05-2023 mantis  0026023
// 1.0  ViewTaskManagementFrag mantis 0026025:Task Management' page modification
// 2.0  ViewTaskManagementFrag mantis 0026028:Modification in Add Activity Page
// 3.0  ViewTaskManagementFrag mantis 0026029:Modification in Select Activity Status pop up screen
class ViewTaskManagementFrag : BaseFragment(), View.OnClickListener{

    private lateinit var mContext: Context
    private lateinit var rv_list:RecyclerView
    private lateinit var progress_wheel:ProgressWheel
    private lateinit var tv_no_data:AppCustomTextView
    private lateinit var shopName:AppCustomTextView
    private lateinit var shopImage:ImageView
    private lateinit var fab_view_frag:FloatingActionButton
    private var viewActivityAdapter: ViewTaskActivityAdapter? = null

    var addActivityReq = AddTaskReq()
    var editActivityReq = EditTaskReq()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        var task_id:String?=null
        var shopNames:String?=null

        fun getInstance(objects: Any): ViewTaskManagementFrag {
            val fragment = ViewTaskManagementFrag()
            var obj = objects as TaskList
            task_id=obj.task_id
            shopNames = obj.task_name

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.view_task_management_frag, container, false)
        initView(view)

        return view
    }


    private fun initView(view: View){
        rv_list=view.findViewById(R.id.rv_view_lead_frag_stls)
        tv_no_data=view.findViewById(R.id.tv_no_data)
        fab_view_frag=view.findViewById(R.id.fab_view_frag)
        progress_wheel=view.findViewById(R.id.progress_wheel)
        shopName = view.findViewById(R.id.row_cutomer_lead_list_ShopNameTV)
        shopImage = view.findViewById(R.id.row_cutomer_lead_list_ivImage)

        progress_wheel.stopSpinning()

        fab_view_frag.setOnClickListener(this)
        // start 1.0  ViewTaskManagementFrag mantis 0026025
        shopName.text = "Task Name - "+shopNames
        // end 1.0  ViewTaskManagementFrag mantis 0026025
        val drawable = TextDrawable.builder()
                .buildRoundRect(shopNames!!.trim().toUpperCase().take(1), ColorGenerator.MATERIAL.randomColor, 120)
        shopImage.setImageDrawable(drawable)

        getActivityList()
    }

    private fun getActivityList(){
        try {
            if (!AppUtils.isOnline(mContext)) {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                return
            }
            BaseActivity.isApiInitiated = true
            progress_wheel.spin()
            val repository = GetLeadRegProvider.provideList()
            BaseActivity.compositeDisposable.add(
                    repository.getTaskList(task_id!!)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as TaskViewRes
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    if(addShopResult.task_status_dtls_list.size>0){
                                        tv_no_data.visibility=View.GONE
                                        setAdapter(addShopResult.task_status_dtls_list!!)
                                    }
                                } else  {
                                    tv_no_data.visibility=View.VISIBLE
                                }
                            }, { error ->
                                tv_no_data.visibility=View.VISIBLE
                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                if (error != null) {
                                }
                            })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            tv_no_data.visibility=View.VISIBLE
            BaseActivity.isApiInitiated = false
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
            progress_wheel.stopSpinning()
        }
    }

    private fun setAdapter(list: ArrayList<Taskdtls_list>){
        viewActivityAdapter=ViewTaskActivityAdapter(mContext,list,object: ViewTaskActivityAdapter.OnViewActiClickListener{
            override fun onEditClick(obj: Taskdtls_list,adapterPos:Int) {
                var isLast:Boolean=false
                if((list.size-1) == adapterPos){
                    isLast=true
                }else{
                    isLast=false
                }
                onEdit(obj, isLast)
            }
        })
        rv_list.adapter=viewActivityAdapter
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.fab_view_frag->{
                openDialog()
                /*val list = AppDatabase.getDBInstance()?.activityDropdownDao()?.getAll()
                if (list == null || list.isEmpty())
                    getActivityDropdownList()
                else
                    openActivityList(list)*/
            }
        }
    }

   lateinit var simpleDialog :Dialog
    lateinit var tv_date_dialog:TextView
    lateinit var nextDate:TextView
    lateinit var activityType:AppCustomTextView
    private fun openDialog() {
        simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // start 1.0  ViewTaskManagementFrag mantis 0026028
        simpleDialog.setContentView(R.layout.dialog_add_task_customer)
        //End rev 1.0 mantis 0026028

        val activityStatus = simpleDialog.findViewById(R.id.dialog_add_activity_spinnerType) as AppCustomTextView
        activityType = simpleDialog.findViewById(R.id.dialog_add_activity_activity_Type) as AppCustomTextView
        tv_date_dialog = simpleDialog.findViewById(R.id.tv_dialog_add_acti_date) as TextView
        var tv_time = simpleDialog.findViewById(R.id.tv_dialog_add_acti_time) as TextView
        val tv_message_ok = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView

        val iv_cross = simpleDialog.findViewById(R.id.iv_dialog_add_acti_cus_cross) as ImageView

        val et_dtls = simpleDialog.findViewById(R.id.et_dialog_add_acti_cus_dtls) as EditText
        val et_remarks = simpleDialog.findViewById(R.id.et_dialog_add_acti_cus_remarks) as EditText

        val header = simpleDialog.findViewById(R.id.tv_dialog_add_acti_header) as AppCustomTextView
        nextDate = simpleDialog.findViewById(R.id.dialog_add_activity_next_date) as TextView

        // start 1.0  ViewTaskManagementFrag mantis 0026025
        header.text = "Update Task Details"
        // end 1.0  ViewTaskManagementFrag mantis 0026025

        // start 1.0  ViewTaskManagementFrag mantis 0026028
        et_dtls.setText(shopNames)
        // end 1.0 mantis 0026028

        if(Pref.IsAutoLeadActivityDateTime){
            tv_time.text = AppUtils.getCurrentTimes()
            tv_date_dialog.text = AppUtils.getCurrentDate_DD_MM_YYYY()
            tv_time.isEnabled = false
            tv_date_dialog.isEnabled = false
            tv_time.setTextColor(Color.parseColor("#88000000"))
            tv_date_dialog.setTextColor(Color.parseColor("#88000000"))

        }
        else{
            tv_time.isEnabled = true
            tv_date_dialog.isEnabled = true
            tv_time.setOnClickListener{
                var timeMilis = 0L
                val cal = Calendar.getInstance(Locale.ENGLISH)
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    timeMilis = cal.timeInMillis
                    tv_time.text = SimpleDateFormat("HH:mm:ss").format(cal.time)
                }
                val timePicker = TimePickerDialog(mContext, R.style.DatePickerTheme, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
                timePicker.show()
            }
            tv_date_dialog.setOnClickListener{
                tv_date_dialog.error=null
                val datePicker = android.app.DatePickerDialog(mContext, R.style.DatePickerTheme, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                datePicker.show()
            }
        }
        // start 3.0  ViewTaskManagementFrag mantis 0026029
        activityStatus.setOnClickListener {
            activityStatus.error=null
            var List:ArrayList<String> = ArrayList()
            List.add("Confirmed ")
            List.add("In Process")
            List.add("Pending")

            EnqListDialog.newInstance(List,"Select Task Status"){
                activityStatus.text=it
            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        }
        // start 3.0  ViewTaskManagementFrag mantis 0026029

        activityType.setOnClickListener{
            val list = AppDatabase.getDBInstance()?.activityDropdownDao()?.getAll()
            if (list == null || list.isEmpty())
                getActivityDropdownList()
            else
                openActivityList(list)
        }

        nextDate.setOnClickListener{
            nextDate.error=null
            AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
            val aniDatePicker = android.app.DatePickerDialog(mContext, R.style.DatePickerTheme, date1, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
            aniDatePicker.datePicker.minDate = Calendar.getInstance(Locale.ENGLISH).timeInMillis + (1000 * 60 * 60 * 24)
            aniDatePicker.show()
        }

        tv_message_ok.setOnClickListener({ view ->
            var validating = true

            // start 2.0  ViewTaskManagementFrag mantis 0026025
            if(tv_date_dialog.text.toString().equals("")){
                tv_date_dialog.requestFocus()
                tv_date_dialog.setError("Select date")
                validating=false
            }else if(et_remarks.text.toString().equals("")){
                et_remarks.requestFocus()
                et_remarks.setError("Enter Task Remarks")
                validating=false
            }else if(activityStatus.text.toString().equals("")){
                activityStatus.requestFocus()
                activityStatus.setError("Select Task Status")
                validating=false
            }


            // end 2.0  ViewTaskManagementFrag mantis 0026025
            if(validating){
                val simpleDialogYesNo = Dialog(mContext)
                simpleDialogYesNo.setCancelable(false)
                simpleDialogYesNo.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogYesNo.setContentView(R.layout.dialog_yes_no)
                val dialogHeader = simpleDialogYesNo.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                val dialog_yes_no_headerTV = simpleDialogYesNo.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()+"!"
                dialogHeader.text = "Want to submit Task Management ? "
                val dialogYes = simpleDialogYesNo.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                val dialogNo = simpleDialogYesNo.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
                dialogYes.setOnClickListener({ view ->
                    addActivityReq.user_id = Pref.user_id
                    addActivityReq.task_date=tv_date_dialog.text.toString()
                    addActivityReq.task_time=tv_time.text.toString()
                    addActivityReq.task_details=et_dtls.text.toString()
                    addActivityReq.other_remarks=et_remarks.text.toString()
                    addActivityReq.task_status=activityStatus.text.toString()
                    addActivityReq.task_next_date=nextDate.text.toString()
                    simpleDialogYesNo.cancel()
                    simpleDialog.cancel()
                    submitActivityAPI()

                })
                dialogNo.setOnClickListener({ view ->
                    simpleDialogYesNo.cancel()
                })
                simpleDialogYesNo.show()

            }

        })

        iv_cross.setOnClickListener {
            simpleDialog.cancel()
        }

        simpleDialog.show()

    }

    private val myCalendar: Calendar by lazy {
        Calendar.getInstance(Locale.ENGLISH)
    }

    val date = android.app.DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        //tv_date_dialog.text=  AppUtils.getFormatedDateNew(AppUtils.getBillingDateFromCorrectDate(AppUtils.getFormattedDateForApi(myCalendar.time)),"dd-mm-yyyy","yyyy-mm-dd")
        tv_date_dialog.text=  AppUtils.getBillingDateFromCorrectDate(AppUtils.getFormattedDateForApi(myCalendar.time))
    }

    val date1 = android.app.DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        nextDate.text=  AppUtils.getBillingDateFromCorrectDate(AppUtils.getDobFormattedDate(myCalendar.time))
    }

    private fun getActivityDropdownList() {

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val repository = ActivityRepoProvider.activityRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.activityDropdownList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as ActivityDropdownListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.activity_list

                                if (list != null && list.isNotEmpty()) {

                                    doAsync {

                                        list.forEach {
                                            val activity = ActivityDropDownEntity()
                                            AppDatabase.getDBInstance()?.activityDropdownDao()?.insertAll(activity.apply {
                                                activity_id = it.id
                                                activity_name = it.name
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            openActivityList(AppDatabase.getDBInstance()?.activityDropdownDao()?.getAll()!!)
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
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                        })
        )
    }

    private fun openActivityList(list: List<ActivityDropDownEntity>) {
        ActivityListDialog.newInstance(list as ArrayList<ActivityDropDownEntity>) {
            activityType.text=it.activity_name
        }.show((mContext as DashboardActivity).supportFragmentManager, "")
    }


    @SuppressLint("SuspiciousIndentation")
    private fun submitActivityAPI(){
        addActivityReq.task_id= task_id

        try {
            if (!AppUtils.isOnline(mContext)) {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                return
            }

            var formatDate = AppUtils.getFormatedDateNew(addActivityReq.task_date,"dd-mm-yyyy","yyyy-mm-dd")
            addActivityReq.task_date=formatDate

            if(!addActivityReq.task_next_date.equals("")){
                formatDate = AppUtils.getFormatedDateNew(addActivityReq.task_next_date,"dd-mm-yyyy","yyyy-mm-dd")
                addActivityReq.task_next_date=formatDate
            }else{
                addActivityReq.task_next_date=AppUtils.getNextDateForShopActi()
            }


            if(Pref.IsAutoLeadActivityDateTime){
                addActivityReq.task_time=AppUtils.getCurrentTimes()
            }

            BaseActivity.isApiInitiated = true
            progress_wheel.spin()
            val repository = GetLeadRegProvider.provideList()
            BaseActivity.compositeDisposable.add(
                    repository.submitTask(addActivityReq)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as BaseResponse
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    doAsync {
                                      var obj:TaskManagmentEntity = TaskManagmentEntity()
                                        obj.apply {
                                            task_status_id = Companion.task_id
                                            task_date = addActivityReq.task_date
                                            task_time = addActivityReq.task_time
                                            task_status = addActivityReq.task_status
                                            other_remarks = addActivityReq.other_remarks
                                            task_next_date = addActivityReq.task_next_date
                                        }
                                        AppDatabase.getDBInstance()!!.taskManagementDao().insertAll(obj)

                                        uiThread {
                                            showMsg("Task submitted successfully.")
                                        }
                                    }

                                } else  {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                }
                            }, { error ->
                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                if (error != null) {
                                }
                            })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            BaseActivity.isApiInitiated = false
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
            progress_wheel.stopSpinning()
        }
    }

    private fun onEdit(obj: Taskdtls_list,isLast:Boolean){
        simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_add_task_customer)

        val activityStatus = simpleDialog.findViewById(R.id.dialog_add_activity_spinnerType) as AppCustomTextView
        activityType = simpleDialog.findViewById(R.id.dialog_add_activity_activity_Type) as AppCustomTextView
        tv_date_dialog = simpleDialog.findViewById(R.id.tv_dialog_add_acti_date) as TextView
        val tv_time = simpleDialog.findViewById(R.id.tv_dialog_add_acti_time) as TextView
        val tv_message_ok = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView

        val iv_cross = simpleDialog.findViewById(R.id.iv_dialog_add_acti_cus_cross) as ImageView

        val et_dtls = simpleDialog.findViewById(R.id.et_dialog_add_acti_cus_dtls) as EditText
        val et_remarks = simpleDialog.findViewById(R.id.et_dialog_add_acti_cus_remarks) as EditText

        val header = simpleDialog.findViewById(R.id.tv_dialog_add_acti_header) as AppCustomTextView
        // start 1.0  ViewTaskManagementFrag mantis 26025
        header.text = "Update Task Details"
        // end 1.0  ViewTaskManagementFrag mantis 26025

        nextDate = simpleDialog.findViewById(R.id.dialog_add_activity_next_date) as TextView

        // start 1.0  ViewTaskManagementFrag mantis 0026028
        et_dtls.setText(shopNames)
        // end 1.0 ViewTaskManagementFrag 0026028


        //data set
        tv_date_dialog.text=obj.task_date
        if(isLast){
            tv_date_dialog.isEnabled=true
        }else{
            tv_date_dialog.isEnabled=false
        }
        tv_time.text=obj.task_time
        //tv_time.isEnabled=false
        et_dtls.setText(obj.task_details)
        et_remarks.setText(obj.other_remarks)
        activityStatus.text=obj.task_status
        nextDate.text = obj.task_next_date


        // start 2.0  ViewTaskManagementFrag mantis 0026028
        activityStatus.setOnClickListener {
            activityStatus.error=null
            var List:ArrayList<String> = ArrayList()
            List.add("Confirmed ")
            List.add("In Process")
            List.add("Pending")

            EnqListDialog.newInstance(List,"Select Task Status"){
                activityStatus.text=it
            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        }
        // end 2.0  ViewTaskManagementFrag mantis 0026028

        activityType.setOnClickListener{
            val list = AppDatabase.getDBInstance()?.activityDropdownDao()?.getAll()
            if (list == null || list.isEmpty())
                getActivityDropdownList()
            else
                openActivityList(list)
        }

        tv_date_dialog.setOnClickListener{
            tv_date_dialog.error=null
            val datePicker = android.app.DatePickerDialog(mContext, R.style.DatePickerTheme, date, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }
        tv_time.setOnClickListener{
            var timeMilis = 0L
            val cal = Calendar.getInstance(Locale.ENGLISH)
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeMilis = cal.timeInMillis
                tv_time.text = SimpleDateFormat("HH:mm:ss").format(cal.time)
            }
            val timePicker = TimePickerDialog(mContext, R.style.DatePickerTheme, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
            timePicker.show()
        }

        nextDate.setOnClickListener{
            nextDate.error=null
            AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
            val aniDatePicker = android.app.DatePickerDialog(mContext, R.style.DatePickerTheme, date1, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
            aniDatePicker.datePicker.minDate = Calendar.getInstance(Locale.ENGLISH).timeInMillis + (1000 * 60 * 60 * 24)
            aniDatePicker.show()
        }

        // start 1.0  ViewTaskManagementFrag mantis 26025
        tv_message_ok.setOnClickListener({ view ->
            var validating = true

            if(tv_date_dialog.text.toString().equals("")){
                tv_date_dialog.requestFocus()
                tv_date_dialog.setError("Select date")
                validating=false
            }else if(et_remarks.text.toString().equals("")){
                et_remarks.requestFocus()
                et_remarks.setError("Enter Task Remarks")
                validating=false
            }
            else if(activityStatus.text.toString().equals("")){
                activityStatus.requestFocus()
                activityStatus.setError("Select Task Status")
                validating=false
            }

            // start 1.0  ViewTaskManagementFrag mantis 26025
            if(validating){
                val simpleDialogYesNo = Dialog(mContext)
                simpleDialogYesNo.setCancelable(false)
                simpleDialogYesNo.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogYesNo.setContentView(R.layout.dialog_yes_no)
                val dialogHeader = simpleDialogYesNo.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                val dialog_yes_no_headerTV = simpleDialogYesNo.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()+"!"
                dialogHeader.text = "Want to update Task ? "
                val dialogYes = simpleDialogYesNo.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                val dialogNo = simpleDialogYesNo.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
                dialogYes.setOnClickListener({ view ->
                    editActivityReq.user_id =  Pref.user_id
                    editActivityReq.task_status_id=obj.task_status_id
                    editActivityReq.task_id= task_id
                    editActivityReq.task_date=tv_date_dialog.text.toString()
                    editActivityReq.task_time=tv_time.text.toString()
                    editActivityReq.task_details=et_dtls.text.toString()
                    editActivityReq.other_remarks=et_remarks.text.toString()
                    editActivityReq.task_status=activityStatus.text.toString()
                    editActivityReq.task_next_date=nextDate.text.toString()

                    simpleDialogYesNo.cancel()
                    simpleDialog.cancel()


                    submitEditActivityAPI()

                })
                dialogNo.setOnClickListener({ view ->
                    simpleDialogYesNo.cancel()
                })
                simpleDialogYesNo.show()

            }

        })

        iv_cross.setOnClickListener {
            simpleDialog.cancel()
        }

        simpleDialog.show()
    }

    private fun submitEditActivityAPI(){
        try {
            if (!AppUtils.isOnline(mContext)) {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                return
            }

            var formatDate = AppUtils.getFormatedDateNew(editActivityReq.task_date,"dd-mm-yyyy","yyyy-mm-dd")
            editActivityReq.task_date=formatDate

            var formatNextDate = AppUtils.getFormatedDateNew(editActivityReq.task_next_date,"dd-mm-yyyy","yyyy-mm-dd")
            editActivityReq.task_next_date=formatNextDate

            BaseActivity.isApiInitiated = true
            progress_wheel.spin()
            val repository = GetLeadRegProvider.provideList()
            BaseActivity.compositeDisposable.add(
                    repository.editTask(editActivityReq)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as BaseResponse
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    showMsg("Task updated successfully.")
                                } else  {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                }
                            }, { error ->
                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                if (error != null) {
                                }
                            })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            BaseActivity.isApiInitiated = false
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
            progress_wheel.stopSpinning()
        }
    }


    private fun showMsg(body:String){

        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()+"!"
        dialogHeader.text = body
        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            CustomStatic.IsViewTaskAddUpdate=true
            (mContext as DashboardActivity).onBackPressed()
        })
        simpleDialog.show()


    }



}