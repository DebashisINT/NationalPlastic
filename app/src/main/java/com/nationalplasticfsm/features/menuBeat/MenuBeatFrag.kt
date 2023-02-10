package com.nationalplasticfsm.features.menuBeat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.nationalplasticfsm.R
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.widgets.AppCustomTextView

// 1.0 MenuBeatFrag AppV 4.0.6 Suman 17-01-2023  Menu-Beat work mantis 0025587

class MenuBeatFrag  : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var currentTab: AppCustomTextView
    private lateinit var hierarchyTab: AppCustomTextView
    private lateinit var beatViewPager: ViewPager

    private lateinit var beatPagerAdapter: BeatPagerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.menu_beat_frag, container, false)
        initView(view)
        initAdapter()
        return view
    }

    private fun initView(view:View){
        currentTab = view.findViewById(R.id.tv_menu_beat_current_status)
        hierarchyTab = view.findViewById(R.id.tv_menu_beat_hierarchy_status)
        beatViewPager = view.findViewById(R.id.vp_menu_beat_viewpager)

        currentTab.setOnClickListener(this)
        hierarchyTab.setOnClickListener(this)

        beatPagerAdapter = BeatPagerAdapter(fragmentManager)

        beatViewPager.currentItem = 0
        isTabSelWise(true)
    }

    fun isTabSelWise(isCurrent: Boolean) {
        if(isCurrent){
            currentTab.isSelected=true
            hierarchyTab.isSelected=false
        }else{
            currentTab.isSelected=false
            hierarchyTab.isSelected=true
        }

    }

    private fun initAdapter() {
        beatViewPager.adapter = beatPagerAdapter
    }

    open fun refreshAdapter() {
        beatViewPager.adapter?.notifyDataSetChanged()
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_menu_beat_current_status->{
                isTabSelWise(true)
                beatViewPager.currentItem=0
            }
            R.id.tv_menu_beat_hierarchy_status->{
                isTabSelWise(false)
                beatViewPager.currentItem=1
            }
        }
    }
}