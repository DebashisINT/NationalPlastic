package com.breezenationalplasticfsm.features.taskManagement

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.breezenationalplasticfsm.features.orderhistory.model.ActionFeed

// create by saheli 05-05-2023 mantis  0026023
class TaskManagementViewPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!), ActionFeed {
    override fun refresh() {
        notifyDataSetChanged()
    }


    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return LeadTaskPendingFrag()
        } else if (position == 1) {
            return LeadTaskInProcessFrag()
        } else {
            return Fragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItemPosition(`object`: Any): Int {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return PagerAdapter.POSITION_NONE
    }
}