package com.breezefieldnationalplastic.features.leaveapplynew.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.breezefieldnationalplastic.features.leaveapplynew.ApprovalPendFrag
import com.breezefieldnationalplastic.features.leaveapplynew.LeaveStatusFrag
import com.breezefieldnationalplastic.features.orderhistory.model.ActionFeed


class LeavePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!), ActionFeed {

    override fun refresh() {
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return ApprovalPendFrag()
        } else if (position == 1) {
            return LeaveStatusFrag()
        }
        else {
            return Fragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}