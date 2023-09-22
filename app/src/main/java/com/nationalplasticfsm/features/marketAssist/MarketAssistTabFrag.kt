package com.nationalplasticfsm.features.marketAssist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.nationalplasticfsm.CustomStatic
import com.nationalplasticfsm.R
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.features.performanceAPP.PerformanceTabPagerAdapter
import com.nationalplasticfsm.widgets.AppCustomTextView

class MarketAssistTabFrag  : BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var shopLTab: AppCustomTextView
    private lateinit var churnTab: AppCustomTextView
    private lateinit var tabPagerAdapter: MATabPagerAdapter
    private lateinit var tabViewPager: ViewPager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_ma_assist_tab, container, false)
        initView(view)
        initAdapter()

        return view
    }

    private fun initView(view: View) {
        shopLTab = view.findViewById(R.id.ma_TV_frag_1)
        churnTab = view.findViewById(R.id.ma_TV_frag_2)
        tabViewPager = view.findViewById(R.id.ma_vp_frag)

        shopLTab.setOnClickListener(this)
        churnTab.setOnClickListener(this)
        tabPagerAdapter = MATabPagerAdapter(fragmentManager)
        tabViewPager.currentItem = 0
        isPerformanceWise(true)
        tabViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
                    isPerformanceWise(true)
                } else {
                    isPerformanceWise(false)
                }
            }

        })

    }

    private fun initAdapter() {
        tabViewPager.adapter = tabPagerAdapter
    }

    open fun refreshAdapter() {
        tabViewPager.adapter?.notifyDataSetChanged()
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ma_TV_frag_1 -> {
                isPerformanceWise(true)
                tabViewPager.currentItem = 0
            }
            R.id.ma_TV_frag_2 -> {
                isPerformanceWise(false)
                tabViewPager.currentItem = 1
            }
        }
    }

    fun isPerformanceWise(isperformanceWise: Boolean) {
        if (isperformanceWise) {
            shopLTab.isSelected = true
            churnTab.isSelected = false
        } else {
            shopLTab.isSelected = false
            churnTab.isSelected = true
        }
    }

}