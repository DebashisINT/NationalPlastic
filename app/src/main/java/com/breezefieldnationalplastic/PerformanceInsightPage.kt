package com.breezefieldnationalplastic

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.mylearning.SearchLmsFrag

class PerformanceInsightPage : BaseFragment() , View.OnClickListener {

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

    private lateinit var ll_frag_search_mylearning_root: CardView
    private lateinit var cv_frag_search_mylearning_root: CardView
    private lateinit var cv_lms_leaderboard: CardView

    private lateinit var tv_frag_perf_my_learning_cnt: TextView

    private lateinit var mContext:Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_performance_insight_page, container, false)
        (mContext as Activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        initView(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun initView(view: View) {
        //performance
        ll_lms_performance=view.findViewById(R.id.ll_lms_performance)
        iv_lms_performance=view.findViewById(R.id.iv_lms_performance)
        tv_lms_performance=view.findViewById(R.id.tv_lms_performance)

        //mylearning
        ll_lms_mylearning=view.findViewById(R.id.ll_lms_mylearning)
        iv_lms_mylearning=view.findViewById(R.id.iv_lms_mylearning)
        tv_lms_mylearning=view.findViewById(R.id.tv_lms_mylearning)

        //leaderboard
        ll_lms_leaderboard=view.findViewById(R.id.ll_lms_leaderboard)
        iv_lms_leaderboard=view.findViewById(R.id.iv_lms_leaderboard)
        tv_lms_leaderboard=view.findViewById(R.id.tv_lms_leaderboard)

        //knowledgehub
        ll_lms_knowledgehub=view.findViewById(R.id.ll_lms_knowledgehub)
        iv_lms_knowledgehub=view.findViewById(R.id.iv_lms_knowledgehub)
        tv_lms_knowledgehub=view.findViewById(R.id.tv_lms_knowledgehub)

        ll_frag_search_mylearning_root=view.findViewById(R.id.ll_frag_search_mylearning_root)
        cv_lms_leaderboard=view.findViewById(R.id.cv_lms_leaderboard)
        cv_frag_search_mylearning_root=view.findViewById(R.id.cv_frag_search_mylearning_root)

        tv_frag_perf_my_learning_cnt=view.findViewById(R.id.tv_frag_perf_my_learning_cnt)

        iv_lms_performance.setImageResource(R.drawable.performance_insights_checked)
        iv_lms_mylearning.setImageResource(R.drawable.open_book_lms_)
        iv_lms_knowledgehub.setImageResource(R.drawable.set_of_books_lms)

        tv_lms_performance.setTextColor(getResources().getColor(R.color.toolbar_lms))
        tv_lms_mylearning.setTextColor(getResources().getColor(R.color.black))
        tv_lms_leaderboard.setTextColor(getResources().getColor(R.color.black))
        tv_lms_knowledgehub.setTextColor(getResources().getColor(R.color.black))

        ll_lms_performance.setOnClickListener(this)
        ll_lms_mylearning.setOnClickListener(this)
        ll_lms_leaderboard.setOnClickListener(this)
        ll_lms_knowledgehub.setOnClickListener(this)
        ll_frag_search_mylearning_root.setOnClickListener(this)
        cv_lms_leaderboard.setOnClickListener(this)
        cv_frag_search_mylearning_root.setOnClickListener(this)

        tv_frag_perf_my_learning_cnt.text = CustomStatic.MyLearningTopicCount.toString()
    }

    companion object {
        fun getInstance(objects: Any): PerformanceInsightPage {
            val fragment = PerformanceInsightPage()
            return fragment
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            //Leaderboard for LMS page redirection mantis - 0027571
            cv_lms_leaderboard.id -> {
                setHomeClickFalse()
                (mContext as DashboardActivity).loadFragment(FragType.LeaderboardLmsFrag, true, "")
            }

            //My assigned topics page redirection -> Assigned to user mantis - 0027573
            ll_lms_mylearning.id -> {
                setHomeClickFalse()
                (mContext as DashboardActivity).loadFragment(FragType.SearchLmsFrag, true, "")
            }
            //All topics page redirection  mantis - 0027570
            ll_lms_knowledgehub.id -> {
                setHomeClickFalse()
                (mContext as DashboardActivity).loadFragment(FragType.SearchLmsKnowledgeFrag, true, "")
            }
            //Performance Insight page redirection
            ll_lms_performance.id -> {
                setHomeClickFalse()
                (mContext as DashboardActivity).loadFragment(FragType.PerformanceInsightPage, true, "")

            }
            //My Performance  page redirection mantis - 0027576
            cv_frag_search_mylearning_root.id -> {
                setHomeClickFalse()
                (mContext as DashboardActivity).loadFragment(FragType.MyPerformanceFrag, true, "")
            }
            //My learning topic list page redirection mantis - 0027574
            ll_frag_search_mylearning_root.id -> {
                setHomeClickFalse()
                (mContext as DashboardActivity).loadFragment(FragType.MyLearningTopicList, true, "")
            }
        }

    }

    fun setHomeClickFalse(){
        CustomStatic.IsHomeClick = false
    }

}