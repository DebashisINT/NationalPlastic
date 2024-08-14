package com.breezenationalplasticfsm.features.performanceAPP

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.view.PieChartView
import com.breezenationalplasticfsm.widgets.AppCustomTextView


/**
 * Created by Saheli on 26-03-2023 v 4.0.8 mantis 0025860.
 */
class PerformanceAppFragment : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var ownPerformanceTab: AppCustomTextView
    private lateinit var teamPerformanceTab: AppCustomTextView
    private lateinit var allPerformanceTab: AppCustomTextView
    private lateinit var performanceTabPagerAdapter: PerformanceTabPagerAdapter
    private lateinit var performanceViewPager: ViewPager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_performance_app, container, false)
        initView(view)
        initAdapter()

        return view
    }

    private fun initView(view: View) {
        ownPerformanceTab = view.findViewById(R.id.ownPerformace_TV_frag_performace_app)
        teamPerformanceTab = view.findViewById(R.id.teamPerformace_TV_frag_performace_app)
        performanceViewPager = view.findViewById(R.id.performance_vp_frag_performance_app)
        allPerformanceTab = view.findViewById(R.id.all_TV_frag_performace_app)
        ownPerformanceTab.setOnClickListener(this)
        teamPerformanceTab.setOnClickListener(this)
        allPerformanceTab.setOnClickListener(this)
        performanceTabPagerAdapter = PerformanceTabPagerAdapter(fragmentManager)
        performanceViewPager.currentItem = 0
        isPerformanceWise("0")
        performanceViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
               if (position == 0) {
                    isPerformanceWise("0")
                }  else if(position == 1){
                   isPerformanceWise("1")
               }
               else if(position == 2){
                   isPerformanceWise("2")
               }else {
                    isPerformanceWise("0")
                }
            }

        })

    }

    private fun initAdapter() {
        performanceViewPager.adapter = performanceTabPagerAdapter
    }

    open fun refreshAdapter() {
        performanceViewPager.adapter?.notifyDataSetChanged()
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ownPerformace_TV_frag_performace_app -> {
                isPerformanceWise("0")
                performanceViewPager.currentItem = 0
            }
            R.id.teamPerformace_TV_frag_performace_app -> {
                isPerformanceWise("1")
                performanceViewPager.currentItem = 1
            }
            R.id.all_TV_frag_performace_app -> {
                isPerformanceWise("2")
                performanceViewPager.currentItem = 2
            }
        }
    }

    fun isPerformanceWise(isperformanceWise: String) {
        if (isperformanceWise.equals("0")) {
            ownPerformanceTab.isSelected = true
            teamPerformanceTab.isSelected = false
            allPerformanceTab.isSelected = false
        }
        else if (isperformanceWise.equals("2")){
            ownPerformanceTab.isSelected = false
            teamPerformanceTab.isSelected = false
            allPerformanceTab.isSelected = true
        }
        else if (isperformanceWise.equals("1")){
            ownPerformanceTab.isSelected = false
            teamPerformanceTab.isSelected = true
            allPerformanceTab.isSelected = false
        }else {
            ownPerformanceTab.isSelected = true
            teamPerformanceTab.isSelected = false
            allPerformanceTab.isSelected = false
        }
    }
}