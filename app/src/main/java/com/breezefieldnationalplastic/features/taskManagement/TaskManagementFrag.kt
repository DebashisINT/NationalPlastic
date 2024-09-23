package com.breezefieldnationalplastic.features.taskManagement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.NewQuotation.ViewAllQuotListFragment
import com.breezefieldnationalplastic.features.lead.model.CustomerLeadList
import com.breezefieldnationalplastic.features.lead.model.CustomerListReq
import com.breezefieldnationalplastic.features.taskManagement.model.TaskListReq
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.google.android.gms.common.api.internal.LifecycleCallback.getFragment
import com.pnikosis.materialishprogress.ProgressWheel

// create by saheli 05-05-2023 mantis  0026023
class TaskManagementFrag: BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var tv_pendingTab: AppCustomTextView
    private lateinit var tv_inProcessTab: AppCustomTextView
    private lateinit var taskmanagementPagerAdapter: TaskManagementViewPagerAdapter
    private lateinit var vp_ViewPager: ViewPager

    private lateinit var progress_wheel:ProgressWheel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        var reqData_inProcess_LeadFrag = TaskListReq()
        var reqData_pending_LeadFrag = TaskListReq()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_task_managment, container, false)
        initView(view)
        initAdapter()
         reqData_inProcess_LeadFrag = TaskListReq()
         reqData_pending_LeadFrag = TaskListReq()
        return view
    }

    private fun initView(view: View){
        tv_pendingTab = view.findViewById(R.id.tv_leaf_frag_pending_tab)
        tv_inProcessTab = view.findViewById(R.id.tv_leaf_frag_inprocess_tab)
        vp_ViewPager = view.findViewById(R.id.vp_leaf_frag)
        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()

        tv_pendingTab.setOnClickListener(this)
        tv_inProcessTab.setOnClickListener(this)

        taskmanagementPagerAdapter = TaskManagementViewPagerAdapter(fragmentManager)
        vp_ViewPager.currentItem=0
        isPendingTab(true)
        vp_ViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    isPendingTab(true)
                } else {
                    isPendingTab(false)
                }
            }

        })

        CustomStatic.IsViewTaskAddUpdate=false

    }

    fun isPendingTab(ispendingTab: Boolean) {
        if (ispendingTab) {
            tv_pendingTab.isSelected = true
            tv_inProcessTab.isSelected = false
        } else {
            tv_pendingTab.isSelected = false
            tv_inProcessTab.isSelected = true
        }
    }

    private fun initAdapter() {
        vp_ViewPager.adapter = taskmanagementPagerAdapter
    }

    open fun refreshAdapter() {
        vp_ViewPager.adapter?.notifyDataSetChanged()
    }



    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_leaf_frag_pending_tab -> {
                isPendingTab(true)
                vp_ViewPager.currentItem = 0
            }
            R.id.tv_leaf_frag_inprocess_tab -> {
                isPendingTab(false)
                vp_ViewPager.currentItem = 1
            }
        }
    }

    fun updateView(){
        taskmanagementPagerAdapter.notifyDataSetChanged()
    }

}