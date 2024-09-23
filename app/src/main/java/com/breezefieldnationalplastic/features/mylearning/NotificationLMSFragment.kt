package com.breezefieldnationalplastic.features.mylearning

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.domain.LMSNotiEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity

class NotificationLMSFragment : BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var main_ll_notifi: LinearLayout

    private lateinit var ll_lms_performance: LinearLayout
    private lateinit var iv_lms_performance: ImageView
    private lateinit var tv_lms_performance: TextView

    private lateinit var ll_lms_mylearning: LinearLayout
    private lateinit var iv_lms_mylearning: ImageView
    private lateinit var tv_lms_mylearning: TextView

    private lateinit var ll_lms_leaderboard: LinearLayout
    private lateinit var iv_lms_leaderboard: ImageView
    private lateinit var tv_lms_leaderboard: TextView

    private lateinit var ll_lms_knowledgehub: LinearLayout
    private lateinit var iv_lms_knowledgehub: ImageView
    private lateinit var tv_lms_knowledgehub: TextView

    private lateinit var lmsNotiFilterData: ArrayList<LMSNotiFilterData>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        fun getInstance(objects: Any): NotificationLMSFragment {
            val fragment = NotificationLMSFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater!!.inflate(R.layout.fragment_notification_l_m_s, container, false)

        initView(view)
        return view
    }

    private fun initView(view: View) {

        AppDatabase.getDBInstance()!!.lmsNotiDao().updateISViwed(true)

        recyclerView = view.findViewById(R.id.rv_notification)
        main_ll_notifi = view.findViewById(R.id.main_ll_notifi)

        //performance
        ll_lms_performance = view.findViewById(R.id.ll_lms_performance)
        iv_lms_performance = view.findViewById(R.id.iv_lms_performance)
        tv_lms_performance = view.findViewById(R.id.tv_lms_performance)

        //mylearning
        ll_lms_mylearning = view.findViewById(R.id.ll_lms_mylearning)
        iv_lms_mylearning = view.findViewById(R.id.iv_lms_mylearning)
        tv_lms_mylearning = view.findViewById(R.id.tv_lms_mylearning)

        //leaderboard
        ll_lms_leaderboard = view.findViewById(R.id.ll_lms_leaderboard)
        iv_lms_leaderboard = view.findViewById(R.id.iv_lms_leaderboard)
        tv_lms_leaderboard = view.findViewById(R.id.tv_lms_leaderboard)

        //knowledgehub
        ll_lms_knowledgehub = view.findViewById(R.id.ll_lms_knowledgehub)
        iv_lms_knowledgehub = view.findViewById(R.id.iv_lms_knowledgehub)
        tv_lms_knowledgehub = view.findViewById(R.id.tv_lms_knowledgehub)

        //iv_lms_leaderboard.setImageResource(R.drawable.leaderboard_new)
        //iv_lms_performance.setImageResource(R.drawable.my_performance_new)
        //iv_lms_mylearning.setImageResource(R.drawable.my_learning_new)
        //iv_lms_knowledgehub.setImageResource(R.drawable.knowledge_hub_new)

        iv_lms_performance.setImageResource(R.drawable.performance_insights)
        iv_lms_mylearning.setImageResource(R.drawable.open_book_lms_)
        iv_lms_knowledgehub.setImageResource(R.drawable.set_of_books_lms)

        /*iv_lms_leaderboard.setColorFilter(
            ContextCompat.getColor(mContext, R.color.black),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
        iv_lms_performance.setColorFilter(
            ContextCompat.getColor(mContext, R.color.black),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
        iv_lms_mylearning.setColorFilter(
            ContextCompat.getColor(mContext, R.color.black),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
        iv_lms_knowledgehub.setColorFilter(
            ContextCompat.getColor(mContext, R.color.black),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )*/

        ll_lms_performance.setOnClickListener(this)
        ll_lms_mylearning.setOnClickListener(this)
        ll_lms_leaderboard.setOnClickListener(this)
        ll_lms_knowledgehub.setOnClickListener(this)

        try {
            lmsNotiFilterData = ArrayList()
            var dateL =
                AppDatabase.getDBInstance()!!.lmsNotiDao().getDistinctDate() as ArrayList<String>
            for (i in 0..dateL.size - 1) {
                var obj: LMSNotiFilterData = LMSNotiFilterData()
                obj.noti_date = dateL.get(i)
                obj.notiL = AppDatabase.getDBInstance()!!.lmsNotiDao()
                    .getNotiByDate(dateL.get(i)) as ArrayList<LMSNotiEntity>
                lmsNotiFilterData.add(obj)
            }

            val headerAdapter = HeaderAdapterLMSNoti(lmsNotiFilterData)
            recyclerView.layoutManager = LinearLayoutManager(mContext)
            recyclerView.adapter = headerAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(p0: View?) {

        when (p0?.id) {
            ll_lms_mylearning.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.SearchLmsFrag,
                    true,
                    ""
                )
            }

            ll_lms_leaderboard.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.MyLearningFragment, true, "")
            }

            ll_lms_knowledgehub.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.SearchLmsKnowledgeFrag,
                    true,
                    ""
                )
            }

            ll_lms_performance.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.PerformanceInsightPage, true, "")
            }

        }
    }
}