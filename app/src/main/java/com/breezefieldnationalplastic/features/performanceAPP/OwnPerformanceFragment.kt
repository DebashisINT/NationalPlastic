package com.breezefieldnationalplastic.features.performanceAPP

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.domain.ShopTypeEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.attendance.api.AttendanceRepositoryProvider
import com.breezefieldnationalplastic.features.attendance.model.*
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.marketAssist.AdapterSuggestiveProduct
import com.breezefieldnationalplastic.features.marketAssist.ShopDtls
import com.breezefieldnationalplastic.features.marketAssist.SuggestiveProductFinal
import com.breezefieldnationalplastic.features.nearbyshops.model.ShopData
import com.breezefieldnationalplastic.features.nearbyshops.model.ShopListResponse
import com.breezefieldnationalplastic.features.performanceAPP.model.AdapterNoOrderListInShop
import com.breezefieldnationalplastic.features.performanceAPP.model.AdapterNoVisitedRevisitShopList
import com.breezefieldnationalplastic.features.performanceAPP.model.AdapterPartywiseSalesRecyclerView
import com.breezefieldnationalplastic.features.performanceAPP.model.ChartDataModel
import com.breezefieldnationalplastic.features.performanceAPP.model.ChartDataModelNew
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_own_performance.iv_frag_performance_MTDinfo
import kotlinx.android.synthetic.main.fragment_team_performance.no_sales_party_break
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Saheli on 26-03-2023 v 4.0.8 mantis 0025860.
 */
//  Revision Note
// 1.0 OwnPerformanceFragment AppV 4.1.3 Saheli    28/04/2023 mantis 0025971
// 2.0 OwnPerformanceFragment AppV 4.1.3 Saheli    02/05/2023 mantis 0025991 Under Activity Ageing, Below changes need to be done
// 3.0 OwnPerformanceFragment AppV 4.1.3 Suman    22/05/2023 mantis 26188
// 4.0 OwnPerformanceFragment AppV 4.1.3 Saheli   24/05/2023 0026221
// 5.0 OwnPerformanceFragment AppV 4.1.6 Saheli   31/05/2023 26269
// 6.0 v 4.1.6 Saheli mantis 0026315 text value set blank date 09-06-2023
// 7.0  v 4.1.6 Saheli mantis 26324 New Feature in Performance Insights 12-06-2023
// 8.0  v 4.1.6 Saheli mantis 26349 New Feature in Performance Insights 16-06-2023
// 9.0  v 4.1.6 Suman mantis 0026396 New Feature in Performance Insights 21-06-2023
class OwnPerformanceFragment: BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var aaChart: AAChartView
    private lateinit var aaChart1: AAChartView
    private lateinit var aaChart2: AAChartView
    private lateinit var tv_present_atten: TextView
    private lateinit var tv_absent_atten: TextView
    private lateinit var tv_AttendHeader: TextView
    private lateinit var tv_AttendHeaderMonth: TextView
    private lateinit var iv_frag_performance_attenshare: ImageView
    private lateinit var iv_frag_performance_MTDinfo: ImageView

    private lateinit var iv_frag_performance_atteninfo: ImageView
    private lateinit var iv_frag_performance_threemonthinfo: ImageView
    private lateinit var iv_frag_performance_activityageinginfo: ImageView
    private lateinit var iv_frag_performance_partywisesalesinfo: ImageView
    private lateinit var iv_frag_performance_headerCountinfo: ImageView
    private lateinit var iv_frag_performance_headerCountNotvisitedinfo: ImageView
    private lateinit var iv_frag_performance_headerCountNotproductnotsellinfo: ImageView
    private lateinit var iv_frag_performance_headerCountNotcollectioninfo: ImageView
    private lateinit var iv_frag_performance_headerCount_zeroOrderinfo: ImageView
    private lateinit var iv_frag_performance_headerCount_noVisitinfo: ImageView
    private lateinit var iv_frag_performance_last10info: ImageView

    private lateinit var tv_frag_own_perf_mtd_heading_month: TextView
    private lateinit var iv_frag_performance_MTDshare: ImageView
    private lateinit var iv_share_last10: ImageView
    private lateinit var tv_total_ordervalue_frag_own: TextView
    private lateinit var tv_totalOrdercount_frag_own_performance: TextView
    private lateinit var tv_avg_value_frag_own_performance: TextView
    private lateinit var tv_avg_orderCount_frag_own_performance: TextView
    private lateinit var ll_attend_view: LinearLayout
    private lateinit var ll_mtd_view: LinearLayout
    private lateinit var iv_loader_spin: ImageView
    private lateinit var iv_background_color_set: ImageView
    private lateinit var ll_last10order_frag_own: LinearLayout
    private lateinit var tv_frag_own_performnace_sel_shopType: TextView
    private var shopType_list: ArrayList<ShopTypeEntity>? = null
    private var sel_shopTypeID: String = ""
    private var sel_shopTypeName: String = ""
    private lateinit var tv_total_ordervalueshopTypewise_frag_own:TextView
    private lateinit var tv_totalOrdercount_shoptypewise_frag_own_performance:TextView
    private lateinit var tv_avgOrderValueshopTypewise_frag_own_performance:TextView
    private lateinit var tv_frag_own_performnace_sel_party:TextView
    private var mshoplist: ArrayList<AddShopDBModelEntity>? = null
    private lateinit var tv_frag_own_performance_lastvisitbyago:TextView
    private lateinit var tv_frag_own_performance_lastorderbyago:TextView
    private lateinit var tv_frag_own_performance_lastcollectionbyago:TextView
    private lateinit var tv_frag_own_performance_loginbyago:TextView
    private lateinit var ll_activityageing_frag_own:LinearLayout
    private lateinit var iv_share_activityageing: ImageView
//    var calendar: Calendar = Calendar.getInstance()
    var inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    var outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    private var adapterPartynotVisited20days: AdapterPartyNotVisitRecyclerView? = null
    private lateinit var frag_own_performance_last20nitvisited_list_rv:RecyclerView
    private lateinit var shopList:ShopListResponse
    private var adapterPartywiseSales: AdapterPartywiseSalesRecyclerView? = null
    private lateinit var rcv_party_wise_items_own:RecyclerView
    private  var mPartywisesaleslist :ArrayList<listparty_wise_sales>?=null
    private lateinit  var tv_sel_party_multiple_sel_own:TextView
    private lateinit var iv_share_partywisesales: ImageView
    private lateinit var ll_party_wise_sales_performance:LinearLayout
    private lateinit var iv_share_partynotvisitedlast20days: ImageView
    private lateinit var ll_partynotvisitedlast20_frag_own:LinearLayout
    private lateinit var tv_no_party:TextView

    private lateinit var iv_frag_performance_threemonthshare:ImageView
    private lateinit var chart_three_month_performance_report:AAChartView
    private lateinit var ll_last3month_view:LinearLayout
    private lateinit var iv_red_alertperformance_report:ImageView
    private lateinit var iv_red_alert_performance_reportTv:TextView
    private lateinit var no_sales_party_break:TextView




    private lateinit  var samplec:AAChartView


    // start v 4.0.16. saheli 12-06-2023 mantis 26324
    private lateinit var rv_no_order_taken_from_last3months:RecyclerView
    private lateinit var rv_no_visited_taken_from_last3months:RecyclerView
    private lateinit var  rv_no_coll_taken_from_last3months:RecyclerView
    private lateinit var  rv_product_nosell_taken_from_last3months:RecyclerView
    private lateinit var tv_frag_own_performance_headerCount:TextView
    private lateinit var cv_frag_own_performance_noOrder:CardView
    private lateinit var  cv_frag_own_performance_noVisited:CardView
    private lateinit var cv_frag_own_performance_noCollection:CardView
    private lateinit var cv_frag_own_performance_noproductSell:CardView
    private lateinit var tv_frag_own_performance_headerCountNotvisited:TextView
    private lateinit var tv_frag_own_performance_headerCountNotcollection:TextView
    private lateinit var tv_frag_own_performance_headerCountNotproductnotsell:TextView
    private var adapternoOrdersinceLast3months: AdapterNoOrderTakenShop? = null
    // end v 4.0.16. saheli 12-06-2023 mantis 26324

    // start v 4.0.16. saheli 12-06-2023 mantis 26349
    private lateinit var cv_frag_own_performance_noOrder_party:CardView
    private lateinit var tv_frag_own_performance_headerCount_zeroOrder:TextView
    private lateinit var rv_no_order_taken:RecyclerView

    private lateinit var cv_frag_own_performance_noVisit_party:CardView
    private lateinit var tv_frag_own_performance_headerCount_noVisit:TextView
    private lateinit var rv_noVisit:RecyclerView

    private lateinit var cd_attendance_main:CardView



    // end v 4.0.16. saheli 12-06-2023 mantis 26349


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_own_performance, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        cd_attendance_main = view.findViewById(R.id.cd_attendance_main)
        samplec=view.findViewById(R.id.samplec)
        iv_background_color_set = view.findViewById(R.id.iv_background_color_set)
        iv_loader_spin = view.findViewById(R.id.iv_loader_spin)
        ll_attend_view = view.findViewById(R.id.ll_attend_view)
        ll_mtd_view = view.findViewById(R.id.ll_mtd_view)
        tv_AttendHeader = view.findViewById(R.id.tv_frag_own_perf_attend_heading)
        tv_AttendHeaderMonth = view.findViewById(R.id.tv_frag_own_perf_attend_heading_month)
        iv_frag_performance_attenshare = view.findViewById(R.id.iv_frag_performance_attenshare)
        iv_frag_performance_MTDshare = view.findViewById(R.id.iv_frag_performance_MTDshare)

        iv_frag_performance_atteninfo = view.findViewById(R.id.iv_frag_performance_atteninfo)
        iv_frag_performance_threemonthinfo = view.findViewById(R.id.iv_frag_performance_threemonthinfo)
        iv_frag_performance_last10info = view.findViewById(R.id.iv_frag_performance_last10info)
        iv_frag_performance_activityageinginfo = view.findViewById(R.id.iv_frag_performance_activityageinginfo)
        iv_frag_performance_partywisesalesinfo = view.findViewById(R.id.iv_frag_performance_partywisesalesinfo)
        iv_frag_performance_headerCountinfo = view.findViewById(R.id.iv_frag_performance_headerCountinfo)
        iv_frag_performance_headerCountNotvisitedinfo = view.findViewById(R.id.iv_frag_performance_headerCountNotvisitedinfo)
        iv_frag_performance_headerCountNotproductnotsellinfo = view.findViewById(R.id.iv_frag_performance_headerCountNotproductnotsellinfo)
        iv_frag_performance_headerCountNotcollectioninfo = view.findViewById(R.id.iv_frag_performance_headerCountNotcollectioninfo)
        iv_frag_performance_headerCount_zeroOrderinfo = view.findViewById(R.id.iv_frag_performance_headerCount_zeroOrderinfo)
        iv_frag_performance_headerCount_noVisitinfo = view.findViewById(R.id.iv_frag_performance_headerCount_noVisitinfo)
        iv_frag_performance_MTDinfo = view.findViewById(R.id.iv_frag_performance_MTDinfo)

        tv_frag_own_perf_mtd_heading_month = view.findViewById(R.id.tv_frag_own_perf_mtd_heading_month)
        iv_frag_performance_attenshare.setOnClickListener(this)
        iv_frag_performance_MTDshare.setOnClickListener(this)

        iv_frag_performance_MTDinfo.setOnClickListener(this)
        iv_frag_performance_atteninfo.setOnClickListener(this)
        iv_frag_performance_threemonthinfo.setOnClickListener(this)
        iv_frag_performance_last10info.setOnClickListener(this)
        iv_frag_performance_activityageinginfo.setOnClickListener(this)
        iv_frag_performance_partywisesalesinfo.setOnClickListener(this)
        iv_frag_performance_headerCountinfo.setOnClickListener(this)
        iv_frag_performance_headerCountNotvisitedinfo.setOnClickListener(this)
        iv_frag_performance_headerCountNotproductnotsellinfo.setOnClickListener(this)
        iv_frag_performance_headerCountNotcollectioninfo.setOnClickListener(this)
        iv_frag_performance_headerCount_zeroOrderinfo.setOnClickListener(this)
        iv_frag_performance_headerCount_noVisitinfo.setOnClickListener(this)

        iv_share_last10 =  view.findViewById(R.id.iv_share_last10)
        iv_share_last10.setOnClickListener(this)
        ll_last10order_frag_own = view.findViewById(R.id.ll_last10order_frag_own)
        tv_total_ordervalue_frag_own = view.findViewById(R.id.tv_total_ordervalue_frag_own)
        tv_totalOrdercount_frag_own_performance =
            view.findViewById(R.id.tv_totalOrdercount_frag_own_performance)
        tv_avg_value_frag_own_performance = view.findViewById(R.id.tv_avg_value_frag_own_performance)
        tv_avg_orderCount_frag_own_performance = view.findViewById(R.id.tv_avg_orderCount_frag_own_performance)
        tv_frag_own_performnace_sel_shopType = view.findViewById(R.id.tv_frag_own_performnace_sel_shopType)
        tv_frag_own_performnace_sel_shopType.setOnClickListener(this)
        loadProgress()
        tv_total_ordervalueshopTypewise_frag_own = view.findViewById(R.id.tv_total_ordervalueshopTypewise_frag_own)
        tv_totalOrdercount_shoptypewise_frag_own_performance = view.findViewById(R.id.tv_totalOrdercount_shoptypewise_frag_own_performance)
        tv_avgOrderValueshopTypewise_frag_own_performance = view.findViewById(R.id.tv_avgOrderValueshopTypewise_frag_own_performance)
        iv_share_activityageing = view.findViewById(R.id.iv_share_activityageing)
        iv_share_activityageing.setOnClickListener(this)
        /*  tv_AttendHeader = view.findViewById(R.id.tv_frag_own_perf_attend_heading)

        val text = "<font color=" + context?.resources?.getColor(R.color.black) + ">Attendance Report</font> <font color="+
                context?.resources?.getColor(R.color.gray_50) + ">" + "(Last Month)" + "</font>"
        tv_AttendHeader.text = Html.fromHtml(text)*/


        aaChart = view.findViewById<AAChartView>(R.id.aa_chart_view)
        aaChart1 = view.findViewById<AAChartView>(R.id.aa_chart_view1)
        aaChart2 = view.findViewById<AAChartView>(R.id.aa_chart_view2)
        tv_present_atten = view.findViewById(R.id.tv_frag_own_performance_present_atten)
        tv_absent_atten = view.findViewById(R.id.tv_frag_own_performance_absent_atten)
        tv_frag_own_performnace_sel_party =  view.findViewById(R.id.tv_frag_own_performnace_sel_party)
        tv_frag_own_performnace_sel_party.setOnClickListener(this)
        tv_frag_own_performance_lastvisitbyago = view.findViewById(R.id.tv_frag_own_performance_lastvisitbyago)
        tv_frag_own_performance_lastorderbyago =  view.findViewById(R.id.tv_frag_own_performance_lastorderbyago)
        tv_frag_own_performance_lastcollectionbyago = view.findViewById(R.id.tv_frag_own_performance_lastcollectionbyago)// last visited date
        tv_frag_own_performance_loginbyago = view.findViewById(R.id.tv_frag_own_performance_loginbyago)// collection
        ll_activityageing_frag_own = view.findViewById(R.id.ll_activityageing_frag_own)

        frag_own_performance_last20nitvisited_list_rv = view.findViewById(R.id.frag_own_performance_last20nitvisited_list_rv)
        rcv_party_wise_items_own = view.findViewById(R.id.rcv_party_wise_items_own)
        tv_sel_party_multiple_sel_own = view.findViewById(R.id.tv_sel_party_multiple_sel_own)
        no_sales_party_break =   view.findViewById(R.id.no_sales_party_break)
        no_sales_party_break.visibility = View.VISIBLE
        samplec.visibility = View.GONE
        tv_sel_party_multiple_sel_own.setOnClickListener(this)
        iv_share_partywisesales = view.findViewById(R.id.iv_share_partywisesales)
        iv_share_partywisesales.setOnClickListener(this)
        ll_party_wise_sales_performance = view.findViewById(R.id.ll_party_wise_sales_performance)
        iv_share_partynotvisitedlast20days = view.findViewById(R.id.iv_share_partynotvisitedlast20days)
        iv_share_partynotvisitedlast20days.setOnClickListener(this)
        ll_partynotvisitedlast20_frag_own = view.findViewById(R.id.ll_partynotvisitedlast20_frag_own)
        tv_no_party = view.findViewById(R.id.tv_no_party)

        iv_frag_performance_threemonthshare =  view.findViewById(R.id.iv_frag_performance_threemonthshare)
        chart_three_month_performance_report = view.findViewById(R.id.chart_three_month_performance_report)
        iv_red_alertperformance_report =view.findViewById(R.id.iv_red_alert_performance_report)
        iv_red_alert_performance_reportTv = view.findViewById(R.id.iv_red_alert_performance_reportTv)
        ll_last3month_view = view.findViewById(R.id.ll_last3month_view)
        iv_frag_performance_threemonthshare.setOnClickListener(this)
        iv_red_alertperformance_report.visibility = View.GONE
        iv_red_alert_performance_reportTv.visibility = View.GONE
       /* iv_red_alertperformance_report.setOnClickListener {
            openDialogPopup("Your Average Order Value is getting Down.")
        }*/

        // start v 4.0.16. saheli 12-06-2023 mantis 26324
        cv_frag_own_performance_noOrder= view.findViewById(R.id.cv_frag_own_performance_noOrder)
        cv_frag_own_performance_noVisited = view.findViewById(R.id.cv_frag_own_performance_noVisited)
        cv_frag_own_performance_noCollection =  view.findViewById(R.id.cv_frag_own_performance_noCollection)
        cv_frag_own_performance_noproductSell = view.findViewById(R.id.cv_frag_own_performance_noproductSell)
        rv_no_order_taken_from_last3months = view.findViewById(R.id.rv_no_order_taken_from_last3months)
        rv_no_visited_taken_from_last3months =  view.findViewById(R.id.rv_no_visited_taken_from_last3months)
        rv_no_coll_taken_from_last3months = view.findViewById(R.id.rv_no_coll_taken_from_last3months)
        rv_product_nosell_taken_from_last3months = view.findViewById(R.id.rv_product_nosell_taken_from_last3months)
        tv_frag_own_performance_headerCount =  view.findViewById(R.id.tv_frag_own_performance_headerCount)
        tv_frag_own_performance_headerCountNotvisited = view.findViewById(R.id.tv_frag_own_performance_headerCountNotvisited)
        tv_frag_own_performance_headerCountNotcollection = view.findViewById(R.id.tv_frag_own_performance_headerCountNotcollection)
        tv_frag_own_performance_headerCountNotproductnotsell = view.findViewById(R.id.tv_frag_own_performance_headerCountNotproductnotsell)

        //noOrderTakenListOfShop()
        // end v 4.0.16. saheli 12-06-2023 mantis 26324

        // start 8.0  v 4.1.6 Saheli mantis 26349 New Feature in Performance Insights 16-06-2023
        cv_frag_own_performance_noOrder_party = view.findViewById(R.id.cv_frag_own_performance_noOrder_party)
        tv_frag_own_performance_headerCount_zeroOrder =  view.findViewById(R.id.tv_frag_own_performance_headerCount_zeroOrder)
        rv_no_order_taken =  view.findViewById(R.id.rv_no_order_taken)


        cv_frag_own_performance_noVisit_party = view.findViewById(R.id.cv_frag_own_performance_noVisit_party)
        tv_frag_own_performance_headerCount_noVisit =  view.findViewById(R.id.tv_frag_own_performance_headerCount_noVisit)
        rv_noVisit =  view.findViewById(R.id.rv_noVisit)
        // end 8.0  v 4.1.6 Saheli mantis 26349 New Feature in Performance Insights 16-06-2023

//        last20NotVisitedList()

        var calendar1: Calendar = Calendar.getInstance()
        calendar1.add(Calendar.MONTH, -1)
        val sdf = SimpleDateFormat("MMM")
        val lastMonthDate: String = sdf.format(calendar1.time)
        val daysInMonth: Int = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar1.setTime(sdf.parse(lastMonthDate))
        calendar1.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
        val sdf1 = SimpleDateFormat("yyyy-MM-dd")
        val firstDate = sdf1.format(calendar1.time)
        calendar1[Calendar.DAY_OF_MONTH] = daysInMonth
        val lastDate = sdf1.format(calendar1.time)
        println("Month " + lastMonthDate)
        println("month in days " + daysInMonth)
        println("1st Date $lastMonthDate month " + firstDate)
        println("End Date $lastMonthDate month " + lastDate)
        val attendanceReq = AttendanceRequest()
        attendanceReq.user_id = Pref.user_id
        attendanceReq.session_token = Pref.session_token
        attendanceReq.start_date = firstDate
        attendanceReq.end_date = lastDate
        if(AppUtils.isOnline(mContext)){
            callAttendanceListApi(attendanceReq, firstDate, lastDate, daysInMonth)
            cd_attendance_main.visibility = View.VISIBLE
        }
        else{
//            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            cd_attendance_main.visibility = View.GONE
        }



        //Begin 3.0 OwnPerformanceFragment AppV 4.1.3 Suman    22/05/2023 mantis 26188
        val now: LocalDate = LocalDate.now()
        val earlier: LocalDate = now.minusMonths(1)
        //tv_AttendHeaderMonth.text= " (for the last month - ${earlier.getMonth()} ${AppUtils.getCurrentYear()})"
        // begin 5.0 OwnPerformanceFragment AppV 4.1.6 Saheli   31/05/2023 26269
        tv_AttendHeaderMonth.text= " (for the last month - ${AppUtils.getPrevMonthCurrentYear_MMM_YYYY().replace("-"," ")})"
        // end 5.0 OwnPerformanceFragment AppV 4.1.6 Saheli   31/05/2023 26269
        //End 3.0 OwnPerformanceFragment AppV 4.1.3 Suman    22/05/2023 mantis 26188


        /*MTD Calculation*/
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = dateFormat.format(currentDate)
        println(formattedDate)
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        Locale.setDefault(Locale.US)
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = 1
        cal[year, month] = day
        println("current Date " + currentDate)
        val dateFormat1 = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate1 = dateFormat1.format(currentDate)
        println("formate date " + formattedDate1)
        val inputDate = dateFormat1.parse(formattedDate1)
        val calendar = Calendar.getInstance()
        calendar.time = inputDate
        calendar[Calendar.DAY_OF_MONTH] = 1
        val firstDateOfMonth = calendar.time
        val outputDate = dateFormat1.format(firstDateOfMonth)
        println("First date of month: $outputDate")

        calculatedLastThreemonthData()

        //Begin 3.0 OwnPerformanceFragment AppV 4.1.3 Suman    22/05/2023 mantis 26188
        tv_frag_own_perf_mtd_heading_month.text = " (Month To Date ${AppUtils.getFirstDateOfThisMonth_DD_MMM_YY()} TO ${AppUtils.getCurrentDate_DD_MMM_YYYY()})"
        //End of 3.0 OwnPerformanceFragment AppV 4.1.3 Suman    22/05/2023 mantis 26188

        try {
            val totalOrderValueMTDwise = AppDatabase.getDBInstance()!!.orderDetailsListDao().getOrderValueMTD(outputDate, formattedDate1)
            val totalOrderCountMTDwise = AppDatabase.getDBInstance()!!.orderDetailsListDao().getOrderCountMTD(outputDate, formattedDate1)
//            val shopDetailsCount = AppDatabase.getDBInstance()!!.addShopEntryDao().countUsers()
            val totalMTDDates = AppUtils.getCurrentDate_DD_MM_YYYY().split("-").get(0)

            println("Total Order Value+count MTDwise: $totalOrderValueMTDwise $totalOrderCountMTDwise")
            // begin 5.0 OwnPerformanceFragment AppV 4.1.6 Saheli   31/05/2023 26269
            tv_total_ordervalue_frag_own.setText("Total Order Value \n" + String.format("%.2f", totalOrderValueMTDwise.toDouble()))
            tv_totalOrdercount_frag_own_performance.setText("Total Order Count \n" + totalOrderCountMTDwise)
            tv_avg_value_frag_own_performance.setText("Average Order Value \n" + String.format("%.2f", (totalOrderValueMTDwise.toDouble() / totalOrderCountMTDwise.toDouble())))
            val orderavgCount = String.format("%.2f", ((totalOrderValueMTDwise.toDouble() / totalOrderCountMTDwise.toDouble())))
            // end 5.0 OwnPerformanceFragment AppV 4.1.6 Saheli   31/05/2023 26269
            /*tv_avg_orderCount_frag_own_performance.setText(
                "Avg Order Count\n" +
                    String.format(
                    "%.2f",
                    ((orderavgCount.toDouble() / shopDetailsCount))
                )
            )*/
            val averageOrderCount = (totalOrderCountMTDwise.toDouble() / totalMTDDates.toDouble()).toInt()

            tv_avg_orderCount_frag_own_performance.setText(
                "Average Order Count\n" + averageOrderCount

            )
//            val avgCount = String.format("%.2f", ((orderavgCount.toDouble() / totalMTDDates.toDouble()))).toInt()
            // begin 5.0 OwnPerformanceFragment AppV 4.1.6 Saheli   31/05/2023 26269
          /*  aaChart1.aa_drawChartWithChartModel(
                ChartDataModelNew.configurePolarColumnChart(
                    totalOrderValueMTDwise.toDouble(),
                    totalOrderCountMTDwise.toDouble(),
                    orderavgCount.toDouble(),
                    averageOrderCount
                )
            )*/
            // end 5.0 OwnPerformanceFragment AppV 4.1.6 Saheli   31/05/2023 26269
            loadNotProgress()
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            // bengin 5.0 OwnPerformanceFragment AppV 4.1.6 Saheli   31/05/2023 26269
            tv_total_ordervalue_frag_own.setText("Total Order Value \n" + 0)
            tv_totalOrdercount_frag_own_performance.setText("Total Order Count \n" + 0)
            tv_avg_value_frag_own_performance.setText("Average Order Value \n" + 0)
            tv_avg_orderCount_frag_own_performance.setText("Average Order Count \n" + 0)
            // start 1.0 OwnPerformanceFragment AppV 4.1.3 Saheli    28/04/2023 mantis 0025971
           /* aaChart1.aa_drawChartWithChartModel(
                ChartDataModelNew.configurePolarColumnChart(
                    0.0,
                    0.0,
                    0.0,
                    0
                )
            )*/
            // end 5.0 OwnPerformanceFragment AppV 4.1.6 Saheli   31/05/2023 26269
            // end  1.0 OwnPerformanceFragment AppV 4.1.3 Saheli    28/04/2023 mantis 0025971
            loadNotProgress()
        }

        // start v 4.0.16. saheli 12-06-2023 mantis 26324

        Handler().postDelayed(Runnable {
            noOrderTakenListOfShop()
        }, 500)
        Handler().postDelayed(Runnable {
            noVisitMadeListOfShop()
        }, 500)
        Handler().postDelayed(Runnable {
            noCollectionMadeListOfShop()
        }, 500)
        Handler().postDelayed(Runnable {
            noProductSellOfShop()
        }, 500)


        // start v 4.0.16. saheli 12-06-2023 mantis 26324


        // start v 4.0.16. saheli 16-06-2023 mantis 26349 No order taken -->
        Handler().postDelayed(Runnable {
            noOrderTakenShop()
        }, 500)

        Handler().postDelayed(Runnable {
            noVisitPartiesList()
        }, 500)
        // end v 4.0.16. saheli 16-06-2023 mantis 26349 No order taken -->




    }

    private fun noOrderTakenShop() {
        var NoOrderTakenL: ArrayList<NoOrderTakenList>
        doAsync {
            NoOrderTakenL = AppDatabase.getDBInstance()!!.orderDetailsListDao().getNoOrderTakeList() as ArrayList<NoOrderTakenList>
            uiThread {
                if(NoOrderTakenL.size>0){
                    tv_frag_own_performance_headerCount_zeroOrder.text = NoOrderTakenL.size.toString()
                    rv_no_order_taken.adapter = AdapterNoOrderListInShop(mContext, NoOrderTakenL,true)
                    rv_no_order_taken.visibility = View.VISIBLE
                    cv_frag_own_performance_noOrder_party.visibility = View.VISIBLE
                }else{
                    rv_no_order_taken.visibility = View.GONE
                    cv_frag_own_performance_noOrder_party.visibility = View.GONE
                }
            }
        }

    }

    private fun noVisitPartiesList() {
        doAsync {
           //
            var finalShopL :ArrayList<ShopDtlsCustom> = ArrayList()
            var shopDtlsCustomL = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopDtlsCUstom1() as ArrayList<ShopDtlsCustom>
            for(i in 0..shopDtlsCustomL.size-1){
                try {
                    var formatedLastVisitD = AppUtils.changeDateFormat2(shopDtlsCustomL.get(i).lastVisitedDate).replace("/","-")
                    var formatedAddedD = shopDtlsCustomL.get(i).dateAdded.split("T").get(0)
                    var currentD = AppUtils.getCurrentDateForShopActi()
                    //Begin 9.0  v 4.1.6 Suman mantis 0026396 New Feature in Performance Insights 21-06-2023
                    //if(formatedLastVisitD.equals(formatedAddedD)){
                    if(formatedLastVisitD.equals(formatedAddedD) && !formatedAddedD.equals(currentD)){
                        //End of 9.0  v 4.1.6 Suman mantis 0026396 New Feature in Performance Insights 21-06-2023
                        finalShopL.add(shopDtlsCustomL.get(i))
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
            }
            uiThread {
                if(finalShopL.size>0){
                    tv_frag_own_performance_headerCount_noVisit.text = finalShopL.size.toString()
                    rv_noVisit.adapter = AdapterNoVisitedRevisitShopList(mContext, finalShopL)
                    rv_noVisit.visibility = View.VISIBLE
                    cv_frag_own_performance_noVisit_party.visibility = View.VISIBLE
                }else{
                    rv_noVisit.visibility = View.GONE
                    cv_frag_own_performance_noVisit_party.visibility = View.GONE
                }
            }
        }

    }

    private fun noOrderTakenListOfShop() {
        val currentDt = AppUtils.getCurrentDateyymmdd()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        val threeMonthsAgoDate = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val threeMonthsAgoDateformat = dateFormat.format(threeMonthsAgoDate)
        println("1st Date last three month: $threeMonthsAgoDateformat")
        var NoOrderTakenList: ArrayList<NoOrderTakenShop2>
        doAsync {
            NoOrderTakenList = AppDatabase.getDBInstance()!!.orderDetailsListDao().getNoOrderTaken2(threeMonthsAgoDateformat.toString(),AppUtils.getCurrentDateyymmdd()) as ArrayList<NoOrderTakenShop2>
            var countNoOrderTaken = NoOrderTakenList.size
        uiThread {
            if(NoOrderTakenList.size>0){
                tv_frag_own_performance_headerCount.text = countNoOrderTaken.toString()
                rv_no_order_taken_from_last3months.adapter = AdapterNoOrderTakenShop2(mContext, NoOrderTakenList)
                rv_no_order_taken_from_last3months.visibility = View.VISIBLE
                cv_frag_own_performance_noOrder.visibility = View.VISIBLE
            }else{
                rv_no_order_taken_from_last3months.visibility = View.GONE
                cv_frag_own_performance_noOrder.visibility = View.GONE
            }

        }
        }


    }

    private fun noVisitMadeListOfShop() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        val threeMonthsAgoDate = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        var threeMonthsAgoDateformat = dateFormat.format(threeMonthsAgoDate)
        var noVisitDoneLast3Month: ArrayList<NoOrderTakenShop>

        var cal30daysAgo = AppUtils.getDaysAgo(30)
        threeMonthsAgoDateformat = dateFormat.format(cal30daysAgo)

        doAsync {
            noVisitDoneLast3Month = AppDatabase.getDBInstance()!!.orderDetailsListDao().getShopNotVisited30DaysDtls(threeMonthsAgoDateformat.toString(),AppUtils.getCurrentDateyymmdd()) as ArrayList<NoOrderTakenShop>
            var countNolastVisitTaken = noVisitDoneLast3Month.size
            uiThread {
                if(noVisitDoneLast3Month.size>0){
                    tv_frag_own_performance_headerCountNotvisited.text = countNolastVisitTaken.toString()
                    rv_no_visited_taken_from_last3months.adapter = AdapterNoOrderTakenShop(mContext, noVisitDoneLast3Month,false,true)
                    rv_no_visited_taken_from_last3months.visibility = View.VISIBLE
                    cv_frag_own_performance_noVisited.visibility = View.VISIBLE
                }else{
                    rv_no_visited_taken_from_last3months.visibility = View.GONE
                    cv_frag_own_performance_noVisited.visibility = View.GONE
                }

            }
        }


    }

    private fun noCollectionMadeListOfShop() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        var threeMonthsAgoDate = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val threeMonthsAgoDateformat = dateFormat.format(threeMonthsAgoDate)
        var noVisitDoneLast3Month: ArrayList<NoOrderTakenShop>

        var fromD = AppUtils.convertToCommonFormat (threeMonthsAgoDateformat.toString())
        var toD = AppUtils.convertToCommonFormat (AppUtils.getCurrentDateyymmdd())

        doAsync {
            noVisitDoneLast3Month = AppDatabase.getDBInstance()!!.orderDetailsListDao().getShopNotCollection30DaysDtls(fromD,toD) as ArrayList<NoOrderTakenShop>
            var countNoCollectTaken = noVisitDoneLast3Month.size
            uiThread {
                if(noVisitDoneLast3Month.size>0){
                    tv_frag_own_performance_headerCountNotcollection.text = countNoCollectTaken.toString()
                    rv_no_coll_taken_from_last3months.adapter = AdapterNoOrderTakenShop(mContext, noVisitDoneLast3Month,true,false)
                    rv_no_coll_taken_from_last3months.visibility = View.VISIBLE
                    cv_frag_own_performance_noCollection.visibility = View.VISIBLE
                }else{
                    rv_no_coll_taken_from_last3months.visibility = View.GONE
                    cv_frag_own_performance_noCollection.visibility = View.GONE
                }

            }
        }


    }

    private fun noProductSellOfShop() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        var threeMonthsAgoDate = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val threeMonthsAgoDateformat = dateFormat.format(threeMonthsAgoDate)
        var noProductSellLast3Month: ArrayList<NoProductSoldShop>

        var fromD = threeMonthsAgoDateformat
        var toD = AppUtils.getCurrentDateyymmdd()

        doAsync {
            noProductSellLast3Month = AppDatabase.getDBInstance()!!.orderDetailsListDao().getProductNotsell(fromD,toD) as ArrayList<NoProductSoldShop>
            uiThread {
                if(noProductSellLast3Month.size>0){
                    tv_frag_own_performance_headerCountNotproductnotsell.text = noProductSellLast3Month.size.toString()
                    rv_product_nosell_taken_from_last3months.adapter = AdapterProductNotSellShop(mContext, noProductSellLast3Month)
                    rv_product_nosell_taken_from_last3months.visibility = View.VISIBLE
                    cv_frag_own_performance_noproductSell.visibility = View.VISIBLE
                }else{
                    rv_product_nosell_taken_from_last3months.visibility = View.GONE
                    cv_frag_own_performance_noproductSell.visibility = View.GONE
                }

            }
        }


    }

    private fun calculatedLastThreemonthData() {
        val now: LocalDate = LocalDate.now()
        val previousMonth: YearMonth = YearMonth.from(now).minusMonths(1)
        val firstDateOfPreviousMonth: LocalDate = previousMonth.atDay(1)
        val lastDateOfPreviousMonth: LocalDate = previousMonth.atEndOfMonth()
        println(previousMonth)
        println("First Date last three month: $firstDateOfPreviousMonth")
        println("Last Date last three month: $lastDateOfPreviousMonth")
        val previousMonth2: YearMonth = YearMonth.from(now).minusMonths(2)
        val firstDateOfPreviousMonth2: LocalDate = previousMonth2.atDay(1)
        val lastDateOfPreviousMonth2: LocalDate = previousMonth2.atEndOfMonth()
        println(previousMonth2)
        println("First Date last three month2: $firstDateOfPreviousMonth2")
        println("Last Date last three month2: $lastDateOfPreviousMonth2")
        val previousMonth3:YearMonth = YearMonth.from(now).minusMonths(3)
        val firstDateOfPreviousMonth3: LocalDate = previousMonth3.atDay(1)
        val lastDateOfPreviousMonth3: LocalDate = previousMonth3.atEndOfMonth()
        println(previousMonth3)
        println("First Date last three month3: $firstDateOfPreviousMonth3")
        println("Last Date last three month3: $lastDateOfPreviousMonth3")

        try{
            val totalOrderValuePreviousmonth1 = AppDatabase.getDBInstance()!!.orderDetailsListDao().getOrderValueMTD(firstDateOfPreviousMonth.toString(),lastDateOfPreviousMonth.toString())
            val totalOrderValuePreviousmonth2 = AppDatabase.getDBInstance()!!.orderDetailsListDao().getOrderValueMTD(firstDateOfPreviousMonth2.toString(),lastDateOfPreviousMonth2.toString())
            val totalOrderValuePreviousmonth3 = AppDatabase.getDBInstance()!!.orderDetailsListDao().getOrderValueMTD(firstDateOfPreviousMonth3.toString(),lastDateOfPreviousMonth3.toString())
            var avgOrderValuePreviousMonth1 = 0.0
            var avgOrderValuePreviousMonth2 = 0.0
            var avgOrderValuePreviousMonth3 = 0.0

            if(totalOrderValuePreviousmonth1!= null){
               avgOrderValuePreviousMonth1 = totalOrderValuePreviousmonth1.toDouble()/lastDateOfPreviousMonth.dayOfMonth
          }
            if(totalOrderValuePreviousmonth2!= null){
                  avgOrderValuePreviousMonth2 = totalOrderValuePreviousmonth2.toDouble()/lastDateOfPreviousMonth2.dayOfMonth
            }
            if(totalOrderValuePreviousmonth3!= null){
                 avgOrderValuePreviousMonth3 = totalOrderValuePreviousmonth3.toDouble()/lastDateOfPreviousMonth3.dayOfMonth
            }

            chart_three_month_performance_report.aa_drawChartWithChartModel(
                    ChartDataModelNew.configurePolarDynamicColumnChart(
                            String.format("%.0f", avgOrderValuePreviousMonth3).toDouble(),
                            String.format("%.0f", avgOrderValuePreviousMonth2).toDouble(),
                            String.format("%.0f", avgOrderValuePreviousMonth1).toDouble(),
                    )
            )
            if (avgOrderValuePreviousMonth3 > avgOrderValuePreviousMonth2 && avgOrderValuePreviousMonth2 > avgOrderValuePreviousMonth1){
                iv_red_alertperformance_report.visibility = View.VISIBLE
                iv_red_alert_performance_reportTv.visibility = View.VISIBLE
            }
            else{
                iv_red_alertperformance_report.visibility = View.GONE
                iv_red_alert_performance_reportTv.visibility = View.GONE
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }





    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun callAttendanceListApi(
        attendanceReq: AttendanceRequest,
        firstDate: String,
        lastDate: String,
        daysInMonth: Int
    ) {
        val repository = AttendanceRepositoryProvider.provideAttendanceRepository()
        BaseActivity.compositeDisposable.add(
            repository.getAttendanceList(attendanceReq)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val attendanceList = result as AttendanceResponse
                    if (attendanceList.status == NetworkConstant.SUCCESS) {
//                        val filteredAttendanceRecords = attendanceList.shop_list!!.filter { it.login_date!! in formattedFirstDate..formattedLastDate && it.Isonleave!!.equals("false")  }
                        val filteredAttendanceRecords =
                            attendanceList.shop_list!!.filter { it.Isonleave!!.equals("false") }
                        val numPresentAttendances = filteredAttendanceRecords.count()
                        val numAbsentAttendances = daysInMonth - filteredAttendanceRecords.count()
                        println("Present & Absent attendance " + numPresentAttendances + numAbsentAttendances)
                        tv_present_atten.setText(numPresentAttendances.toString())
                        tv_absent_atten.setText(numAbsentAttendances.toString())
                        viewAttendanceReport(numPresentAttendances, numAbsentAttendances)

                    } else if (attendanceList.status == NetworkConstant.SESSION_MISMATCH) {

                    } else if (attendanceList.status == NetworkConstant.NO_DATA) {

                    } else {

                    }
                }, { error ->
                })
        )

    }

    fun viewAttendanceReport(attend: Int, absent: Int) {
        aaChart.aa_drawChartWithChartModel(ChartDataModel.configurePieChart(attend, absent))
        aaChart.aa_drawChartWithChartModel(ChartDataModelNew.configurePieChart(attend, absent))
//        aaChart2.aa_drawChartWithChartModel(ChartDataModelNew.configurePolarBarChart())
/*
       Handler().postDelayed(Runnable {
            var totalH:Int = ll_attend_view.height
            var totalW:Int = ll_attend_view.width
            ll_attend_view.isDrawingCacheEnabled = true
            var b:Bitmap = Bitmap.createBitmap(ll_attend_view.getDrawingCache())
            ll_attend_view.isDrawingCacheEnabled = false
        }, 5000)*/
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun ShareDataAsPdf(ReportName: String) {
        var document: Document = Document(PageSize.A4, 36f, 36f, 36f, 80f)

        val time = System.currentTimeMillis()
        var fileName = ReportName.toUpperCase() + "_" + time
        fileName = fileName.replace("/", "_")
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/FSMApp/PERFORMANCE/"

        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        try {
            //PdfWriter.getInstance(document, FileOutputStream(path + fileName + ".pdf"))
            var pdfWriter: PdfWriter =
                PdfWriter.getInstance(document, FileOutputStream(path + fileName + ".pdf"))

            document.open()
            var font: Font = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)
            val projectName = Paragraph(ReportName + ":", font)
            projectName.alignment = Element.ALIGN_CENTER
            projectName.spacingAfter = 5f
//            document.add(projectName)

            if (ReportName.contains("Attendance REPORT")) {
                ll_attend_view.isDrawingCacheEnabled = true
                var bitM: Bitmap = Bitmap.createBitmap(ll_attend_view.getDrawingCache())
                ll_attend_view.isDrawingCacheEnabled = false
                val bitmapPrint = Bitmap.createScaledBitmap(bitM, bitM.width, bitM.height, false)
                val stream = ByteArrayOutputStream()
                bitmapPrint.compress(Bitmap.CompressFormat.PNG, 100, stream)
                var img: Image? = null
                val byteArray: ByteArray = stream.toByteArray()
                try {
                    img = Image.getInstance(byteArray)
                    img.scaleToFit(190f, 90f)
                    img.scalePercent(20f)
                    img.alignment = Image.ALIGN_LEFT
                } catch (e: BadElementException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                document.add(img)
            } else if(ReportName.contains("MTD")) {
                ll_mtd_view.isDrawingCacheEnabled = true
                var bitM: Bitmap = Bitmap.createBitmap(ll_mtd_view.getDrawingCache())
                ll_mtd_view.isDrawingCacheEnabled = false
                val bitmapPrint = Bitmap.createScaledBitmap(bitM, bitM.width, bitM.height, false)
                val stream = ByteArrayOutputStream()
                bitmapPrint.compress(Bitmap.CompressFormat.PNG, 100, stream)
                var img: Image? = null
                val byteArray: ByteArray = stream.toByteArray()
                try {
                    img = Image.getInstance(byteArray)
                    img.scaleToFit(190f, 90f)
                    img.scalePercent(20f)
                    img.alignment = Image.ALIGN_LEFT
                } catch (e: BadElementException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                document.add(img)

            }
            else if(ReportName.contains("Last Three Months Comparative")) {
                ll_last3month_view.isDrawingCacheEnabled = true
                var bitM: Bitmap = Bitmap.createBitmap(ll_last3month_view.getDrawingCache())
                ll_last3month_view.isDrawingCacheEnabled = false
                val bitmapPrint = Bitmap.createScaledBitmap(bitM, bitM.width, bitM.height, false)
                val stream = ByteArrayOutputStream()
                bitmapPrint.compress(Bitmap.CompressFormat.PNG, 100, stream)
                var img: Image? = null
                val byteArray: ByteArray = stream.toByteArray()
                try {
                    img = Image.getInstance(byteArray)
                    img.scaleToFit(190f, 90f)
                    img.scalePercent(20f)
                    img.alignment = Image.ALIGN_LEFT
                } catch (e: BadElementException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                document.add(img)

            }
            else if(ReportName.contains("Party Not Visited Last 20Days")){
                ll_partynotvisitedlast20_frag_own.isDrawingCacheEnabled = true
                var bitM: Bitmap = Bitmap.createBitmap(ll_partynotvisitedlast20_frag_own.getDrawingCache())
                ll_partynotvisitedlast20_frag_own.isDrawingCacheEnabled = false
                val bitmapPrint = Bitmap.createScaledBitmap(bitM, bitM.width, bitM.height, false)
                val stream = ByteArrayOutputStream()
                bitmapPrint.compress(Bitmap.CompressFormat.PNG, 100, stream)
                var img: Image? = null
                val byteArray: ByteArray = stream.toByteArray()
                try {
                    img = Image.getInstance(byteArray)
                    img.scaleToFit(190f, 90f)
                    img.scalePercent(20f)
                    img.alignment = Image.ALIGN_LEFT
                } catch (e: BadElementException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                document.add(img)
            }
            else if(ReportName.contains("Recent 10 Orders")){
                ll_last10order_frag_own.isDrawingCacheEnabled = true
                var bitM: Bitmap = Bitmap.createBitmap(ll_last10order_frag_own.getDrawingCache())
                ll_last10order_frag_own.isDrawingCacheEnabled = false
                val bitmapPrint = Bitmap.createScaledBitmap(bitM, bitM.width, bitM.height, false)
                val stream = ByteArrayOutputStream()
                bitmapPrint.compress(Bitmap.CompressFormat.PNG, 100, stream)
                var img: Image? = null
                val byteArray: ByteArray = stream.toByteArray()
                try {
                    img = Image.getInstance(byteArray)
                    img.scaleToFit(190f, 90f)
                    img.scalePercent(20f)
                    img.alignment = Image.ALIGN_LEFT
                } catch (e: BadElementException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                document.add(img)
            }
            else if(ReportName.contains("Sales Breakdown by Party")){
                ll_party_wise_sales_performance.isDrawingCacheEnabled = true
                var bitM: Bitmap = Bitmap.createBitmap(ll_party_wise_sales_performance.getDrawingCache())
                ll_party_wise_sales_performance.isDrawingCacheEnabled = false
                val bitmapPrint = Bitmap.createScaledBitmap(bitM, bitM.width, bitM.height, false)
                val stream = ByteArrayOutputStream()
                bitmapPrint.compress(Bitmap.CompressFormat.PNG, 100, stream)
                var img: Image? = null
                val byteArray: ByteArray = stream.toByteArray()
                try {
                    img = Image.getInstance(byteArray)
                    img.scaleToFit(190f, 90f)
                    img.scalePercent(20f)
                    img.alignment = Image.ALIGN_LEFT
                } catch (e: BadElementException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                document.add(img)
            }
            else{
                ll_activityageing_frag_own.isDrawingCacheEnabled = true
                var bitM: Bitmap = Bitmap.createBitmap(ll_activityageing_frag_own.getDrawingCache())
                ll_activityageing_frag_own.isDrawingCacheEnabled = false
                val bitmapPrint = Bitmap.createScaledBitmap(bitM, bitM.width, bitM.height, false)
                val stream = ByteArrayOutputStream()
                bitmapPrint.compress(Bitmap.CompressFormat.PNG, 100, stream)
                var img: Image? = null
                val byteArray: ByteArray = stream.toByteArray()
                try {
                    img = Image.getInstance(byteArray)
                    img.scaleToFit(190f, 90f)
                    img.scalePercent(20f)
                    img.alignment = Image.ALIGN_LEFT
                } catch (e: BadElementException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                document.add(img)
            }

            //val pHead = Paragraph()
            //pHead.add(Chunk(img, 90f, -90f))
            //document.add(pHead)


            document.close()

            var sendingPath = path + fileName + ".pdf"
            if (!TextUtils.isEmpty(sendingPath)) {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    val fileUrl = Uri.parse(sendingPath)
                    val file = File(fileUrl.path)
                    val uri: Uri = FileProvider.getUriForFile(
                        mContext,
                        context!!.applicationContext.packageName.toString() + ".provider",
                        file
                    )
                    shareIntent.type = "image/png"
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    startActivity(Intent.createChooser(shareIntent, "Share pdf using"))
                } catch (e: Exception) {
                    e.printStackTrace()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Toaster.msgShort(mContext, ex.message.toString())
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }

    }

    fun get_Resized_Bitmap(bmp: Bitmap, newHeight: Int, newWidth: Int): Bitmap? {
        val width = bmp.width
        val height = bmp.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)
        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, false)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_frag_performance_attenshare -> {
                ShareDataAsPdf("Attendance REPORT")
            }
            R.id.iv_frag_performance_MTDshare -> {
                ShareDataAsPdf("MTD")
            } R.id.iv_frag_performance_MTDinfo -> {
            iButtonText(
                "1. Total Order Value : Total order value of current month \n" +
                    "2. Total Order Count : Total order count of current month \n "+
                    "3. Average Order Value : (Total order value of current month / Total order count of current month) \n"+
                    "4. Average Order Count : (Total order count of current month / Present date of current month) \n")
//           "3. Average Order Value : Average order value for current month first date to present date (Total Order Value / Total Order Count) \n "+
//                    "4. Average Order Count : Average order count for current month first date to present date (Total Order Count / Total Day)\n")
            }R.id.iv_frag_performance_atteninfo -> {
            iButtonText(
                "1. Present : Total count of attendance of last month\n" +
                        "2. Not Logged In : Total count of not logged in of last month \n" )
            }R.id.iv_frag_performance_threemonthinfo  -> {
            iButtonText(
                "Last 3 months Comparative Average Order Value : (Total order value of the month / Total no of days of the month) \n"
                //" Average order value based on the last 3 month (Total Order Value of Month / Total Day of Month) \n"
                        )
            }R.id.iv_frag_performance_last10info -> {
            iButtonText(
                 "1. Total Order Value : Total value of last 10 orders  \n" +
                     "2. Total Order Count : Total order count of last 10 order  \n"+
                     "3. Average Order Value : (Total value of last 10 orders / Total no of orders) \n"
                    // "3. Average Order Value : Average order value of last 10 order based on the selected shop (Total Order Value / Total Order Count) \n "
                    )
            }R.id.iv_frag_performance_activityageinginfo -> {
            iButtonText(

                "1. Last Visit : No of days since last visit \n" +
                    "2. Most Recent Visit : Last date of visit \n"+
                    "3. Last Order : No of days since last order \n"+
                    "4. Last Collection : No of days since last collection \n"
                                        /*"1. Last Visit : How long ago visit the shop  \n" +
                    "2. Most Recent Visit : Date of most recently visit \n "+
                    "3. Last Order : How long ago was the order taken \n "+
                    "4. Last Collection : How long ago was the collection taken\n "*/)
            }R.id.iv_frag_performance_partywisesalesinfo -> {
            iButtonText(
                    "Total Sales Value : Total sum of Sales Value \n" )
            }R.id.iv_frag_performance_headerCountinfo -> {
            iButtonText(
                    "Total Party Count : No of parties not placed order in last 3 months \n"
                   )
            }R.id.iv_frag_performance_headerCountNotvisitedinfo -> {
            iButtonText(
                "Total Party Count : No of parties not visited in last 3 months \n"

            )
            }R.id.iv_frag_performance_headerCountNotproductnotsellinfo -> {
            iButtonText(
                "Total Product Count : No of non selling product in last 3 months \n"

            )
            }R.id.iv_frag_performance_headerCountNotcollectioninfo -> {
            iButtonText(
                "Total Party Count : No of parties where collection not received from last 3 months \n"

            )
            }R.id.iv_frag_performance_headerCount_zeroOrderinfo -> {
            iButtonText(
                "Total Party Count : No of parties not placed order \n"
            )
            }R.id.iv_frag_performance_headerCount_noVisitinfo -> {
            iButtonText(
                "1. Total Party Count : No of parties not visited \n"
            )            }


            R.id.iv_frag_performance_threemonthshare->{
                ShareDataAsPdf("Last Three Months Comparative")
            }
            R.id.tv_frag_own_performnace_sel_shopType -> {
                loadShopTypeList()
            }
            R.id.iv_share_last10 ->{
                ShareDataAsPdf("Recent 10 Orders")
            }
            R.id.tv_frag_own_performnace_sel_party ->{
                loadPartyList()
            }
            R.id.iv_share_activityageing->{
                ShareDataAsPdf("Ageing Analysis")
            }
            R.id.tv_sel_party_multiple_sel_own->{
                /*Party wise sales Order 17-04-2023*/
                partyWiseSalesOrder()
            }
            R.id.iv_share_partywisesales-> {
                ShareDataAsPdf("Sales Breakdown by Party")
            }
            R.id.iv_share_partynotvisitedlast20days->{
                ShareDataAsPdf("Party Not Visited Last 20Days")
            }
        }
    }

    private fun loadProgress() {
        disableScreen()
        iv_background_color_set.setBackgroundColor(resources.getColor(R.color.color_transparent_blue))
        iv_background_color_set.visibility = View.VISIBLE
        iv_loader_spin.visibility = View.VISIBLE
        Glide.with(this)
            .load(R.drawable.loadernew_2)
            .into(iv_loader_spin)
    }

    private fun loadNotProgress() {
        enableScreen()
        iv_background_color_set.visibility = View.GONE
        iv_loader_spin.visibility = View.GONE
    }

    private fun disableScreen() {
        requireActivity().getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    private fun enableScreen() {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private fun loadShopTypeList() {
        var mOrderValue: Double = 0.0
        var mOrderCount: Double = 0.0
        // 4.0 OwnPerformanceFragment AppV 4.1.3 Saheli   24/05/2023 0026221
        val list = AppDatabase.getDBInstance()?.shopTypeDao()?.getOrderByalphabeticallyAll()
        // 4.0 OwnPerformanceFragment AppV 4.1.3 Saheli   24/05/2023 0026221
        if(list!!.size>0){
            shopType_list = list as ArrayList<ShopTypeEntity>
            ShopTypeListDialog.newInstance("Select shop Type", shopType_list!!) {
                tv_frag_own_performnace_sel_shopType.text = it.shoptype_name
                sel_shopTypeID = it.shoptype_id!!
                sel_shopTypeName = it.shoptype_name!!
                val totalOrderVShopTypWise = AppDatabase.getDBInstance()!!.orderDetailsListDao().getTotalOrdershopTypewise(sel_shopTypeID)
              /*  try {
                    for (i in totalOrderVShopTypWise) {
                        mOrderValue += i.amount!!.toDouble()
                        mOrderCount++
                    }
                    tv_total_ordervalueshopTypewise_frag_own.setText("Total Order value \n"+String.format("%.2f",mOrderValue))
                    tv_totalOrdercount_shoptypewise_frag_own_performance.setText("Total Order Count \n"+String.format("%.2f",mOrderCount))
                    tv_avgOrderValueshopTypewise_frag_own_performance.setText("Avg Order Value \n" + String.format("%.2f", (mOrderValue / mOrderCount)))
                }catch (ex:Exception){
                    ex.printStackTrace()
                    tv_total_ordervalueshopTypewise_frag_own.setText("Total Order value \n"+0)
                    tv_totalOrdercount_shoptypewise_frag_own_performance.setText("Total Order Count \n"+0)
                    tv_avgOrderValueshopTypewise_frag_own_performance.setText("Avg Order Value \n" +0)
                }*/

                val totalOrderCountShopTypWise = AppDatabase.getDBInstance()!!.orderDetailsListDao().getOrderCountshopTypewise(sel_shopTypeID)
               try{
                    if(totalOrderVShopTypWise == null){
                        tv_total_ordervalueshopTypewise_frag_own.setText("Total Order Value \n"+0)
                        tv_totalOrdercount_shoptypewise_frag_own_performance.setText("Total Order Count \n"+0)
                        tv_avgOrderValueshopTypewise_frag_own_performance.setText("Average Order Value \n" +0)

                    }else{
                        tv_total_ordervalueshopTypewise_frag_own.setText("Total Order Value \n"+String.format("%.2f",totalOrderVShopTypWise.toDouble()))
                        tv_totalOrdercount_shoptypewise_frag_own_performance.setText("Total Order Count \n"+String.format("%.2f",totalOrderCountShopTypWise.toDouble()))
                        tv_avgOrderValueshopTypewise_frag_own_performance.setText("Average Order Value \n" + String.format("%.2f", (totalOrderVShopTypWise.toDouble() / totalOrderCountShopTypWise.toDouble())))
                    }

                }catch (ex:Exception){
                    ex.printStackTrace()
                }
            }.show((mContext as DashboardActivity).supportFragmentManager, "")

        }
    }

    private fun loadPartyList() {
        try{
            mshoplist = AppDatabase.getDBInstance()?.addShopEntryDao()?.getOrderByalphabeticallyAll() as ArrayList<AddShopDBModelEntity>?
            ShopListDatamodelDialog.newInstance("Select party", mshoplist!!) {
                tv_frag_own_performnace_sel_party.text = it.shopName
                var mshopId = it.shop_id
                var lastVisitedDate = it.lastVisitedDate
                val lastVisitAge = AppUtils.getDayFromSubtractDates(AppUtils.getLongTimeStampFromDate2(lastVisitedDate),AppUtils.convertDateStringToLong(AppUtils.getCurrentDateForShopActi()))
//                tv_frag_own_performance_lastvisitbyago.text = "$lastVisitAge \n Days Ago"
                // 2.0 OwnPerformanceFragment AppV 4.1.3 Saheli    02/05/2023 mantis 0025991 Under Activity Ageing, Below changes need to be done
                tv_frag_own_performance_lastvisitbyago.text = "$lastVisitAge \n Days"
                // 2.0rev end mantis 0025991 ago remove
                var lastOrderDate = AppDatabase.getDBInstance()!!.orderDetailsListDao().getLastOrderDate(mshopId)
                try {
                    var date_str = lastOrderDate.split("T")[0]
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    val date = format.parse(date_str)
                    val newFormat = SimpleDateFormat("dd-MMM-yy")
                    val formattedDate = newFormat.format(date)
                    var lastOrder = AppUtils.getDayFromSubtractDates(AppUtils.getLongTimeStampFromDate2(formattedDate),AppUtils.convertDateStringToLong(AppUtils.getCurrentDateForShopActi()))
                  //  tv_frag_own_performance_lastorderbyago.text = "$lastOrder \n Days Ago"
                    // 2.0 OwnPerformanceFragment AppV 4.1.3 Saheli    02/05/2023 mantis 0025991 Under Activity Ageing, Below changes need to be done
                    tv_frag_own_performance_lastorderbyago.text = "$lastOrder \n Days"
                    // 2.0 rev end mantis 0025991 ago remove
                } catch (e: Exception) {
                    e.printStackTrace()
                    //start v 4.1.6 Saheli mantis 0026315 text value set blank date 09-06-2023
                    tv_frag_own_performance_lastorderbyago.text = "No Order found"
                    //end v 4.1.6 Saheli mantis 0026315 text value set blank date 09-06-2023
                }
                try {
                    var lastcollection = AppDatabase.getDBInstance()!!.collectionDetailsDao().getLastCollectionDate(mshopId)
                    var lastcollDatebyAgo = AppUtils.getDayFromSubtractDates(AppUtils.getLongTimeStampFromDate2(lastcollection),AppUtils.convertDateStringToLong(AppUtils.getCurrentDateForShopActi()))
//                    tv_frag_own_performance_loginbyago.text = "$lastcollDatebyAgo \n Days Ago"
                    // 2.0 OwnPerformanceFragment AppV 4.1.3 Saheli    02/05/2023 mantis 0025991 Under Activity Ageing, Below changes need to be done
                    tv_frag_own_performance_loginbyago.text = "$lastcollDatebyAgo \n Days"
                    // 2.0 rev end mantis 0025991 ago remove
                }catch (e: Exception) {
                    e.printStackTrace()
//                    tv_frag_own_performance_loginbyago.text = "0 \n Days Ago"
                    // 2.0 OwnPerformanceFragment AppV 4.1.3 Saheli    02/05/2023 mantis 0025991 Under Activity Ageing, Below changes need to be done
                    //start v 4.1.6 Saheli mantis 0026315 text value set blank date 09-06-2023
                    tv_frag_own_performance_loginbyago.text = "No Collection found"
                    //end v 4.1.6 Saheli mantis 0026315 text value set blank date 09-06-2023
                    // 2.0 rev end mantis 0025991 ago remove
                }
                try{
                  /*  var lastlogindayAgo = AppDatabase.getDBInstance()!!.userAttendanceDataDao().getLastLoginDate()
                    var lastlogin = AppUtils.getDayFromSubtractDates(AppUtils.getLongTimeStampFromDate2(lastlogindayAgo),AppUtils.convertDateStringToLong(AppUtils.getCurrentDateForShopActi()))
                    tv_frag_own_performance_loginbyago.text = "$lastlogin \n Days Ago"*/
                    // 2.0 OwnPerformanceFragment AppV 4.1.3 Saheli    02/05/2023 mantis 0025991 Under Activity Ageing, Below changes need to be done
                    tv_frag_own_performance_lastcollectionbyago.text = "$lastVisitedDate \n"
                    // 2.0 rev end mantis 0025991 ago remove
                }catch (e: Exception) {
                    e.printStackTrace()
                    // 2.0 OwnPerformanceFragment AppV 4.1.3 Saheli    02/05/2023 mantis 0025991 Under Activity Ageing, Below changes need to be done
                    tv_frag_own_performance_lastcollectionbyago.text = "0 \n Days"
                    // 2.0 rev end mantis 0025991 ago remove
//                    tv_frag_own_performance_loginbyago.text = "0 \n Days Ago"
                }



            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        }
        catch (ex:Exception){
            ex.printStackTrace()
        }

    }
    private fun last20NotVisitedList(){
        var calendar: Calendar = Calendar.getInstance()
        var dateFormat1 = SimpleDateFormat("yyyy-MM-dd")
        calendar.add(Calendar.DAY_OF_YEAR, -20)
        val date20DaysAgo = calendar.time
        println("last 20 days ago"+ dateFormat1.format(date20DaysAgo))
        val fromDTLast20Ago = dateFormat1.format(date20DaysAgo)
        apicallForPartyNotVisited(fromDTLast20Ago,AppUtils.getCurrentDateyymmdd())
    }

    private fun apicallForPartyNotVisited(fromdate: Any, todate: Any) {
        val inputReq = InputRequest()
        inputReq.user_id = Pref.user_id
        inputReq.session_token = Pref.session_token
        inputReq.from_date = fromdate.toString()
        inputReq.to_date = todate.toString()
        val repository = AttendanceRepositoryProvider.provideAttendanceRepository()
        loadProgress()
        BaseActivity.compositeDisposable.add(
            repository.getNotVisitedPartyList(inputReq)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    loadNotProgress()
                    val obj = result as OutputResponse
                    if (obj.status == NetworkConstant.SUCCESS) {
                        if(obj.last_visit_order_list!!.size>0){
                            frag_own_performance_last20nitvisited_list_rv.visibility = View.VISIBLE
                            tv_no_party.visibility = View.GONE
                            prepareAdpater(obj)

                        }else{
                            (mContext as DashboardActivity).showSnackMessage("No data found.")
                        }
                    }else if(obj.status ==NetworkConstant.NO_DATA){
                        (mContext as DashboardActivity).showSnackMessage("No data found.")
                        frag_own_performance_last20nitvisited_list_rv.visibility = View.GONE
                        tv_no_party.visibility = View.VISIBLE
                    }
                }, { error ->
                    loadNotProgress()
                    error.printStackTrace()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                })
        )
    }

    private fun prepareAdpater(obj: OutputResponse) {
        // start 1.0 OwnPerformanceFragment AppV 4.1.3 Saheli    28/04/2023 mantis 0025971
//        adapterPartynotVisited20days=AdapterPartyNotVisitRecyclerView(mContext,obj.last_visit_order_list!!.take(5) as ArrayList<last_visit_order_list>)
        adapterPartynotVisited20days=AdapterPartyNotVisitRecyclerView(mContext,obj.last_visit_order_list!!)
        // end 1.0 OwnPerformanceFragment AppV 4.1.3 Saheli    28/04/2023 mantis 0025971
        frag_own_performance_last20nitvisited_list_rv.adapter = adapterPartynotVisited20days

    }


    private fun partyWiseSalesOrder(){
        println("tag_shop partyWiseSalesOrder call")
        var mshopId:String=""
        var mshopName:String=""
        mshoplist = AppDatabase.getDBInstance()?.addShopEntryDao()?.getOrderByalphabeticallyAll() as ArrayList<AddShopDBModelEntity>?

        var mShopFilterList :ArrayList<PartyWiseDataModel>? = ArrayList()
        var listwiseData: PartyWiseDataModel
        PartySaleWiseListDatamodelDialog.newInstance("Select party", mshoplist!!,{},object :
            PartySaleWiseListDatamodelDialog.submitListOnCLick{
            override fun onSubmitCLick(list: ArrayList<PerformDataClass>) {
                for (i in 0..list.size-1) {
                    mshopId = list.get(i).shop_id!!
                        try{
                            //val haveAnyOrderShopId = AppDatabase.getDBInstance()!!.orderDetailsListDao().getOrderAmtShop(mshopId)
                            //if(haveAnyOrderShopId.size>0){
                                listwiseData = AppDatabase.getDBInstance()!!.orderDetailsListDao().getTotalShopNTwiseSalesValues(mshopId!!)
                                listwiseData.total_sales_value = listwiseData.total_sales_value.toDouble().toInt().toString()
                                println("data class "+listwiseData)
                                mShopFilterList!!.add(listwiseData)
                                println("data class adapter size"+mShopFilterList.size)
                                println("data class adapter"+mShopFilterList)
                            //}

                        }catch(ex:Exception){
                            ex.printStackTrace()
                        }
                }
                setValuePartywiseList(mShopFilterList!!)
            }
        }).show((mContext as DashboardActivity).supportFragmentManager, "")



    }

    fun  setValuePartywiseList(mShopFilterList:ArrayList<PartyWiseDataModel>){
        samplec.visibility = View.VISIBLE
        no_sales_party_break.visibility = View.GONE
        if(mShopFilterList!!.size>0){

            var nameList = mShopFilterList.map { it.shop_name+"<br />"+it.shop_type_name } as ArrayList<String>
            println("nameL Size"+nameList)
            var valueList = mShopFilterList.map { it.total_sales_value } as ArrayList<String>

            val params: LayoutParams = samplec.getLayoutParams()
            params.height = nameList.size*135
            //params.width = 0
            samplec.setLayoutParams(params)
            samplec.aa_drawChartWithChartModel(ChartDataModelNew.configurePolarBarChart(nameList,valueList))
            //sample_aa.aa_drawChartWithChartModel(ChartDataModelNew.configurePieChart(5,30))

            //adapterPartywiseSales = AdapterPartywiseSalesRecyclerView(mContext,mShopFilterList!!)
            //rcv_party_wise_items_own.adapter = adapterPartywiseSales
            println("tag_shop partyWiseSalesOrder data")
        }
        else{
            Toaster.msgShort(mContext, "No data found")
            println("tag_shop partyWiseSalesOrder no-data")
        }
    }

    fun openDialogPopup(text:String){
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
        dialogHeader.text = "Your Average Order Value is getting Down."
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()
    }


    fun iButtonText(text:String){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_info)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
       // val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
       // dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
        dialogHeader.text = text
       // val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
        val dialogNo = simpleDialog.findViewById(R.id.cancel_info_icon) as ImageView
/*
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            CustomStatic.NewOrderTotalCartItem = 0
         //   super.onBackPressed();
            //(mContext as DashboardActivity).loadFragment(FragType.NewOrderScrOrderDetailsFragment, false, NewOrderScrOrderDetailsFragment.shop_id)
        })
*/
        dialogNo.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()
    }


}



