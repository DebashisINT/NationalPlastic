package com.breezefieldnationalplastic.features.marketAssist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.features.orderhistory.model.ActionFeed
import com.breezefieldnationalplastic.features.performanceAPP.OwnPerformanceFragment
import com.breezefieldnationalplastic.features.performanceAPP.TeamPerformanceFragment

class MATabPagerAdapter (fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!), ActionFeed {
    override fun refresh() {
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return ShopListMarketAssistFrag()
        } else if (position == 1) {
            return ChurnProbFrag()
        } else {
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