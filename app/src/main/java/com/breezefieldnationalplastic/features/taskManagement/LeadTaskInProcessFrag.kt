package com.breezefieldnationalplastic.features.taskManagement

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.MaterialSearchView
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.SearchListener
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.uiaction.IntentActionable
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.FTStorageUtils
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.addshop.api.AddShopRepositoryProvider
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.lead.api.GetLeadRegProvider
import com.breezefieldnationalplastic.features.lead.model.CustomerLeadList
import com.breezefieldnationalplastic.features.lead.model.TaskList
import com.breezefieldnationalplastic.features.lead.model.TaskResponse
import com.breezefieldnationalplastic.features.taskManagement.TaskManagementFrag.Companion.reqData_inProcess_LeadFrag
import com.breezefieldnationalplastic.features.taskManagement.adapter.CustomerTaskManagementAdapter
import com.breezefieldnationalplastic.features.taskManagement.model.TaskListReq
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

// create by saheli 05-05-2023 mantis  0026023
// 1.0 LeadTaskInProcessFrag mantis 0026024 saheli v 4.0.8  :under the 'Assigned Lead' page
class LeadTaskInProcessFrag : BaseFragment(),  DatePickerDialog.OnDateSetListener,View.OnClickListener{

    private lateinit var mContext: Context
    private  var adapter: CustomerTaskManagementAdapter? = null
    private var tempList:ArrayList<TaskList> = ArrayList()

    private lateinit var rv_list: RecyclerView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var tv_no_data: AppCustomTextView
    private lateinit var date_range: AppCompatRadioButton
    private lateinit var date_rangeCv:CardView
    private lateinit var radioList: ArrayList<RadioButton>
    private var isChkChanged: Boolean = false
    private val mAutoHighlight: Boolean = false
    private lateinit var date_rangeDisplay: AppCustomTextView
//    private lateinit var enquiryTypeSelectSpinner: AppCustomTextView
    private lateinit var showButton: ImageView
    private var fromDate:String = ""
    private var toDate:String = ""
    private lateinit var leadSearch: AppCustomEditText

    var reqData = TaskListReq()

    private lateinit var sel_priority: AppCustomTextView
    private var adapterpriorityList: AdapterMultiPrioritySel? = null
    var selectedPriorityL : ArrayList<TaskPriorityResponse> = ArrayList()
    private var selPriorityId:String=""


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_task_management_pending, container, false)
        initView(view)

        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    tempList?.let {
                        adapter?.refreshList(it)
                    }
                } else {
                    adapter?.filter?.filter(query)
                }
            }
        })

        // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 start
        (mContext as DashboardActivity).searchView.setVoiceIcon(R.drawable.ic_mic)
        (mContext as DashboardActivity).searchView.setOnVoiceClickedListener({ startVoiceInput() })
        // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 end


        return view
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

    private fun initView(view: View){
        progress_wheel=view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()
        tv_no_data=view.findViewById(R.id.tv_no_data)
        rv_list=view.findViewById(R.id.rv_frag_lead_pending_dtls)
        radioList = ArrayList()
        date_range=view.findViewById(R.id.frag_lead_pending_date_range)
        date_rangeCv = view.findViewById(R.id.cv_date_range)
        date_rangeDisplay = view.findViewById(R.id.frag_lead_pending_date_range_display)
//        enquiryTypeSelectSpinner = view.findViewById(R.id.frag_lead_pending_spinnerType)
        radioList.add(date_range)
        showButton =  view.findViewById(R.id.frag_lead_pending_show)
        leadSearch = view.findViewById(R.id.et_frag_lead_pending_search)

        date_range.setOnClickListener(this)
        date_rangeCv.setOnClickListener(this)
//        enquiryTypeSelectSpinner.setOnClickListener(this)
        showButton.setOnClickListener(this)


        sel_priority = view.findViewById(R.id.frag_task_managment_spinnerType)
        sel_priority.setOnClickListener(this)
    }

    override fun onResume() {

        initTextChangeListener()

        super.onResume()
        if(CustomStatic.IsViewTaskAddUpdate){
            isChkChanged = false
            date_range.isChecked = false
        }
        if(reqData_inProcess_LeadFrag.user_id!=null){
            isChkChanged = true
            date_range.isChecked = true
            sel_priority.text=reqData_inProcess_LeadFrag.task_priority_name
            var strt_date=reqData_inProcess_LeadFrag.from_date
            var end_date=reqData_inProcess_LeadFrag.to_date
            onDateSetCustom(strt_date!!.substring(0,4).toInt(),strt_date!!.substring(5,7).replace("0","").toInt()-1, strt_date!!.substring(8,10).replace("0","").toInt(),
                    end_date!!.substring(0,4).toInt(),end_date!!.substring(5,7).replace("0","").toInt()-1, end_date!!.substring(8,10).replace("0","").toInt())
        }
    }

    private fun initTextChangeListener() {
        leadSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter!!.getFilter().filter(leadSearch.text.toString().trim())
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frag_lead_pending_date_range,R.id.cv_date_range -> {
                if (!isChkChanged) {
                    date_range.isChecked = true
                    val now = Calendar.getInstance(Locale.ENGLISH)
                    val dpd = DatePickerDialog.newInstance(
                            this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    )
                    dpd.isAutoHighlight = mAutoHighlight
                    // start 1.0 LeadTaskInProcessFrag  mantis 0026024 saheli v 4.0.8
                    val dateF = Calendar.getInstance(Locale.ENGLISH)
                    dateF.add(Calendar.DATE, 30)
                    dpd.maxDate = dateF
                    //end 1.0 LeadTaskInProcessFrag  mantis 0026024 saheli v 4.0.8
                    dpd.show((context as Activity).fragmentManager, "Datepickerdialog")
                } else {
                    isChkChanged = false
                }
            }
            R.id.frag_task_managment_spinnerType->{
                sel_priority.text="All"
                ApicallPriorityList()
               /* var List:ArrayList<String> = ArrayList()
                List.add("All")
                List.add("IndiaMart (ARCHER)")
                List.add("MccoyMart")
                List.add("Website")
                List.add("Direct Call")
                List.add("Exhibition")
                List.add("Twak")
                EnqListDialog.newInstance(List,"Select Priority"){
                    sel_priority.text=it
                }.show((mContext as DashboardActivity).supportFragmentManager, "")*/
            }

            R.id.frag_lead_pending_show->{
                // start 1.0 LeadTaskInProcessFrag  mantis 0026024 saheli v 4.0.8
               /* if( enquiryTypeSelectSpinner.text.toString().equals("Select Enquiry")){
                    (mContext as DashboardActivity).showSnackMessage("Please select enquiry")
                    return
                }*/
                // end rev 1.0 0026024 mantis
                getLeadFetch()
            }
        }
    }
    var extraPriorityL:ArrayList<TaskPriorityResponse> = ArrayList()
    private fun ApicallPriorityList() {
        try{
            val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.fetchPriorityData(Pref.session_token!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        var viewResult = result as PriorityTaskSel
                        try{
                            //add default data
                            extraPriorityL.clear()
                          /*  val taskPriorityResponse = TaskPriorityResponse()
                            taskPriorityResponse.task_priority_id = ""
                            taskPriorityResponse.task_priority_name = "All"
                            extraPriorityL.add(taskPriorityResponse)*/
                            extraPriorityL.addAll(viewResult.task_priority_list)
                            println("list" + extraPriorityL)
                        }
                        catch (e:Exception) {
                            e.printStackTrace()
                        }
                        if (viewResult!!.status == NetworkConstant.SUCCESS) {
                            selectedPriorityL.clear()
                            dialogOpenPriority()
                        }
                        }, { error ->
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    }))
                }
        catch (ex:Exception){
            ex.printStackTrace()

        }
    }
    fun dialogOpenPriority() {
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_priorty)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_priorty_headerTV) as AppCustomTextView
        val  rv_PriortyList = simpleDialog.findViewById(R.id.rv_priorty_list) as RecyclerView
        rv_PriortyList.layoutManager = LinearLayoutManager(mContext)

        adapterpriorityList = AdapterMultiPrioritySel(mContext,extraPriorityL,object : AdapterMultiPrioritySel.OnClickListener {
            override fun onTickUntickView(obj: TaskPriorityResponse, isTick: Boolean) {
                if(isTick){
                    selectedPriorityL.add(obj)
                } else{
                    selectedPriorityL.remove(obj)
                }
                var prioritySel = ""
                var prioritySelId = ""
                if(selectedPriorityL.size>0){
                    for(i in 0..selectedPriorityL.size-1){
                        prioritySel = prioritySel+selectedPriorityL.get(i).task_priority_name+","
                        prioritySelId = prioritySelId+selectedPriorityL.get(i).task_priority_id+","
                    }
                    sel_priority.text = prioritySel
                    selPriorityId = prioritySelId
                }else{
                    sel_priority.text = ""
                    selPriorityId = ""
                }
            }
        })
        rv_PriortyList.adapter = adapterpriorityList

        dialogHeader.text = "Select Priority Type"

        val dialogYes = simpleDialog.findViewById(R.id.dialog_priorty_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int, yearEnd: Int, monthOfYearEnd: Int, dayOfMonthEnd: Int) {
        var monthOfYear = monthOfYear
        var monthOfYearEnd = monthOfYearEnd
        val date = "From " + dayOfMonth + AppUtils.getDayNumberSuffix(dayOfMonth) + " " + FTStorageUtils.formatMonth((++monthOfYear).toString()) + " " + year + " To " + dayOfMonthEnd + AppUtils.getDayNumberSuffix(dayOfMonthEnd) + " " + FTStorageUtils.formatMonth((++monthOfYearEnd).toString()) + " " + yearEnd
        date_rangeDisplay.text = date
        var day = "" + dayOfMonth
        var dayEnd = "" + dayOfMonthEnd
        if (dayOfMonth < 10)
            day = "0$dayOfMonth"
        if (dayOfMonthEnd < 10)
            dayEnd = "0$dayOfMonthEnd"
        var fronString: String = day + "-" + FTStorageUtils.formatMonth((monthOfYear /*+ 1*/).toString() + "") + "-" + year
        var endString: String = dayEnd + "-" + FTStorageUtils.formatMonth((monthOfYearEnd /*+ 1*/).toString() + "") + "-" + yearEnd
        fromDate = AppUtils.changeLocalDateFormatToAtt(fronString).replace("/","-")
        toDate = AppUtils.changeLocalDateFormatToAtt(endString).replace("/","-")
    }

     fun onDateSetCustom(year: Int, monthOfYear: Int, dayOfMonth: Int, yearEnd: Int, monthOfYearEnd: Int, dayOfMonthEnd: Int) {
        var monthOfYear = monthOfYear
        var monthOfYearEnd = monthOfYearEnd
        val date = "From " + dayOfMonth + AppUtils.getDayNumberSuffix(dayOfMonth) + " " + FTStorageUtils.formatMonth((++monthOfYear).toString()) + " " + year + " To " + dayOfMonthEnd + AppUtils.getDayNumberSuffix(dayOfMonthEnd) + " " + FTStorageUtils.formatMonth((++monthOfYearEnd).toString()) + " " + yearEnd
        date_rangeDisplay.text = date
        var day = "" + dayOfMonth
        var dayEnd = "" + dayOfMonthEnd
        if (dayOfMonth < 10)
            day = "0$dayOfMonth"
        if (dayOfMonthEnd < 10)
            dayEnd = "0$dayOfMonthEnd"
        var fronString: String = day + "-" + FTStorageUtils.formatMonth((monthOfYear /*+ 1*/).toString() + "") + "-" + year
        var endString: String = dayEnd + "-" + FTStorageUtils.formatMonth((monthOfYearEnd /*+ 1*/).toString() + "") + "-" + yearEnd
        fromDate = AppUtils.changeLocalDateFormatToAtt(fronString).replace("/","-")
        toDate = AppUtils.changeLocalDateFormatToAtt(endString).replace("/","-")

         getLeadFetch()
    }

    private fun getLeadFetch() {
        reqData.from_date =  fromDate
        reqData.to_date = toDate
        //Start REv 1.0 LeadTaskInProcess AppV 4.0.8 saheli mantis 0026024
//        reqData.enquiry_from = "all"
        reqData.task_priority_id = selPriorityId.dropLast(1)
        reqData.task_priority_name =  sel_priority.text.toString().dropLast(1)
        // End 1.0 LeadTaskInProcessFrag  mantis 0026024 saheli v 4.0.8
        reqData.user_id = Pref.user_id

        try {
            if (!AppUtils.isOnline(mContext)) {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                return
            }
            BaseActivity.isApiInitiated = true
            progress_wheel.spin()
            val repository = GetLeadRegProvider.provideList()
            BaseActivity.compositeDisposable.add(
                    repository.TaskList(reqData)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as TaskResponse
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    if(addShopResult.task_dtls_list.size>0){
                                        rv_list.visibility=View.VISIBLE
                                        leadSearch.visibility=View.VISIBLE
                                        reqData_inProcess_LeadFrag=reqData

                                        setAdapter(addShopResult.task_dtls_list)
                                    }
                                } else if(addShopResult.status == NetworkConstant.NO_DATA) {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_found))
                                    tv_no_data.visibility=View.VISIBLE
                                    rv_list.visibility=View.GONE
                                    leadSearch.visibility=View.GONE
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

    private fun setAdapter(list:ArrayList<TaskList>){

        tv_no_data.visibility=View.GONE
        tempList = ArrayList()
        for(i in 0..list.size-1){
            try{
                if(!list.get(i).status.toUpperCase().equals("PENDING") && !list.get(i).status.toUpperCase().equals("NOT INTERESTED")){
                    tempList.add(list.get(i))
                }
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
        if(tempList.size==0){
            rv_list.visibility=View.GONE
            tv_no_data.visibility=View.VISIBLE
            leadSearch.visibility=View.GONE
            return
        }

        adapter = CustomerTaskManagementAdapter(mContext,tempList,object : CustomerTaskManagementAdapter.OnPendingLeadClickListener{
            override fun onActivityClick(obj: TaskList) {
                doActivity(obj)
            }
            override fun onPhoneClick(obj: TaskList) {
            }
        }, {
            it
        })
        rv_list.adapter=adapter

    }

    private fun doActivity(obj:TaskList){
        CustomStatic.IsViewTaskFromInProcess=true
        (mContext as DashboardActivity).loadFragment(FragType.ViewTaskManagementFrag, true, obj)
    }

}