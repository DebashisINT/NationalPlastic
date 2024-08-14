package com.breezenationalplasticfsm.features.performanceAPP

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.alarm.api.performance_report_list_api.PerformanceReportRepoProvider
import com.breezenationalplasticfsm.features.alarm.model.PerformanceReportDataModel
import com.breezenationalplasticfsm.features.alarm.model.PerformanceReportResponseModel
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Calendar

/*Created by saheli 06-07-2023*/

class allPerformanceFrag: BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var ll_row_shop_list_ma_assist1: LinearLayout
    private lateinit var  iv_allperformance_list_ma_pointer:ImageView
    private lateinit var  tv_allperformance_list_ma_pointer: TextView
    private lateinit var rv_summary_list:RecyclerView
    private lateinit var performance_report_list: ArrayList<PerformanceReportDataModel>
    private lateinit var iv_loader_spin:ImageView
    private lateinit var iv_background_color_set:ImageView
    private lateinit var threeMonthsAgoDateformat:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_all_performance, container, false)
        initView(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun initView(view: View) {
        iv_allperformance_list_ma_pointer =  view.findViewById(R.id.iv_allperformance_list_ma_pointer)
        tv_allperformance_list_ma_pointer= view.findViewById(R.id.tv_allperformance_list_ma_pointer)
        ll_row_shop_list_ma_assist1 = view.findViewById(R.id.ll_row_shop_list_ma_assist1)
        iv_background_color_set = view.findViewById(R.id.iv_background_color_set)
        iv_loader_spin = view.findViewById(R.id.iv_loader_spin)
        rv_summary_list = view.findViewById(R.id.rv_summary_list)
        iv_allperformance_list_ma_pointer.setOnClickListener(this)
        tv_allperformance_list_ma_pointer.setOnClickListener(this)

        var calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        var threeMonthsAgoDate = calendar.time
        var dateFormat = SimpleDateFormat("yyyy-MM-dd")

        threeMonthsAgoDateformat = dateFormat.format(threeMonthsAgoDate)


        Glide.with(this)
            .load(R.drawable.icon_pointer_gif)
            .into(iv_allperformance_list_ma_pointer)

    }



    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_allperformance_list_ma_pointer,R.id.tv_allperformance_list_ma_pointer->{
                loadProgress()
                getPerformanceReport(threeMonthsAgoDateformat, AppUtils.getCurrentDateyymmdd())
            }
        }
    }


    private fun getPerformanceReport(fromDateCalender: String, toDateCalender: String) {
        if (!AppUtils.isOnline(mContext)) {
            (this as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        var fromDate = fromDateCalender
        var toDate = toDateCalender
        loadProgress()
        val repository = PerformanceReportRepoProvider.providePerformanceReportRepository()
        BaseActivity.compositeDisposable.add(
            repository.getPerformanceReportList(fromDate, toDate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val performanceList = result as PerformanceReportResponseModel
                    loadNotProgress()
                    when {
                        performanceList.status == NetworkConstant.SUCCESS -> {
                            performance_report_list = ArrayList()
                            performance_report_list = performanceList.performance_report_list!!
                            loadNotProgress()
                            initAdapter(performance_report_list)
                        }
                        performanceList.status == NetworkConstant.SESSION_MISMATCH -> {
                            (mContext as DashboardActivity).showSnackMessage(performanceList.message!!)
                        }
                        performanceList.status == NetworkConstant.NO_DATA -> {
                            (mContext as DashboardActivity).showSnackMessage(performanceList.message!!)

                        }
                        else -> {
                            (mContext as DashboardActivity).showSnackMessage(performanceList.message!!)
                        }
                    }

                }, { error ->
                    loadNotProgress()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                })
        )
    }

    private fun initAdapter(performance_report_list: ArrayList<PerformanceReportDataModel>) {
        rv_summary_list.adapter = AdapterSummaryEmpList(mContext, performance_report_list)

    }

    private fun loadProgress(){
        disableScreen()
        iv_background_color_set.setBackgroundColor(resources.getColor(R.color.color_transparent_blue))
        iv_background_color_set.visibility = View.VISIBLE
        iv_loader_spin.visibility = View.VISIBLE
        Glide.with(this)
            .load(R.drawable.loadernew_2)
            .into(iv_loader_spin)
    }
    private fun loadNotProgress(){
        enableScreen()
        iv_background_color_set.visibility = View.GONE
        iv_loader_spin.visibility = View.GONE
    }
    private fun disableScreen(){
        requireActivity().getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private fun enableScreen(){
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}