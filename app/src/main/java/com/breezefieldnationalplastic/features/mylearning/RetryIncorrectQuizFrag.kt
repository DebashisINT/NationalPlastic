package com.breezefieldnationalplastic.features.mylearning

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.pnikosis.materialishprogress.ProgressWheel

class RetryIncorrectQuizFrag : BaseFragment() , View.OnClickListener {
    private lateinit var mContext: Context
    lateinit var progress_wheel: ProgressWheel

    private lateinit var tv_correct_tab: AppCustomTextView
    private lateinit var tv_incorrect_tab: AppCustomTextView
    private lateinit var retryTabPagerAdapter: RetryTabPagerAdapter
    private lateinit var tab_viewpager: ViewPager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    companion object {
        var topic_id: String = ""
        var topic_name: String = ""
        var previousFrag: String = ""
        fun getInstance(objects: Any): RetryIncorrectQuizFrag {
            val retryIncorrectQuizFrag = RetryIncorrectQuizFrag()

            if (!TextUtils.isEmpty(objects.toString())) {
                val parts = objects.toString().split("~")
                topic_id = parts[0]
                topic_name = parts[1]
            } else {
                topic_id = ""
                topic_name = ""
            }
            println("tag_topic_id" + topic_id)

            return retryIncorrectQuizFrag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_retry_incorrect_quiz, container, false)
        (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initView(view)
        initAdapter()
        return view
    }

    private fun initView(view: View) {

        tv_correct_tab = view.findViewById(R.id.tv_correct_tab)
        tv_incorrect_tab = view.findViewById(R.id.tv_incorrect_tab)
        tab_viewpager = view.findViewById(R.id.tab_viewpager)
        retryTabPagerAdapter = RetryTabPagerAdapter(fragmentManager)
        tv_correct_tab.setOnClickListener(this)
        tv_incorrect_tab.setOnClickListener(this)
        tab_viewpager.currentItem = 0
        isCorrect(true)
        tv_correct_tab.setBackgroundColor(Color.parseColor("#0ea139"))
        tv_correct_tab.setTextColor(Color.parseColor("#FFFFFF"))
        tv_incorrect_tab.setBackgroundColor(Color.parseColor("#E9E9E9"))
        tv_incorrect_tab.setTextColor(resources.getColor(R.color.black_50))

        tab_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    isCorrect(true)

                } else {
                    isCorrect(false)

                }
            }

        })
    }

    private fun initAdapter() {
        tab_viewpager.adapter = retryTabPagerAdapter
    }

    open fun refreshAdapter() {
        tab_viewpager.adapter?.notifyDataSetChanged()
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_correct_tab -> {
                isCorrect(true)
                tab_viewpager.currentItem = 0
            }
            R.id.tv_incorrect_tab -> {
                isCorrect(false)
                tab_viewpager.currentItem = 1
            }
        }
    }

    fun isCorrect(isCorrect: Boolean) {
        if (isCorrect) {
            tv_correct_tab.isSelected = true
            tv_incorrect_tab.isSelected = false
            tv_correct_tab.setBackgroundColor(Color.parseColor("#0ea139"))
            tv_correct_tab.setTextColor(Color.parseColor("#FFFFFF"))
            tv_incorrect_tab.setBackgroundColor(Color.parseColor("#E9E9E9"))
            tv_incorrect_tab.setTextColor(resources.getColor(R.color.black_50))
        } else {
            tv_correct_tab.isSelected = false
            tv_incorrect_tab.isSelected = true
            tv_incorrect_tab  .setBackgroundColor(Color.parseColor("#8D09B8"))
            tv_incorrect_tab  .setTextColor(Color.parseColor("#FFFFFF"))
            tv_correct_tab.setBackgroundColor(Color.parseColor("#E9E9E9"))
            tv_correct_tab.setTextColor(resources.getColor(R.color.black_50))
        }
    }


}