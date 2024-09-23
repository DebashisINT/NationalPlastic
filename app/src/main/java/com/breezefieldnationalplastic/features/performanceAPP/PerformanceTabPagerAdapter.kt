package com.breezefieldnationalplastic.features.performanceAPP

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.breezefieldnationalplastic.features.orderhistory.model.ActionFeed


/**
 * Created by Saheli on 26-03-2023 v 4.0.8 mantis 0025860.
 */
class PerformanceTabPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!), ActionFeed {

    override fun refresh() {
        notifyDataSetChanged()
    }


    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return OwnPerformanceFragment()
        } else if (position == 1) {
            return TeamPerformanceFragment()
        }else if (position == 2) {
            return allPerformanceFrag()
        } else {
            return Fragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getItemPosition(`object`: Any): Int {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return PagerAdapter.POSITION_NONE
    }
}