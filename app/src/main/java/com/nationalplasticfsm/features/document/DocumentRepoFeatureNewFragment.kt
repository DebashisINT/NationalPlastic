package com.nationalplasticfsm.features.document

import android.content.Context
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nationalplasticfsm.CustomStatic
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.AppDatabase
import com.nationalplasticfsm.app.NetworkConstant
import com.nationalplasticfsm.app.domain.DocumentypeEntity
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.base.presentation.BaseActivity
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.features.document.api.DocumentRepoProvider
import com.nationalplasticfsm.features.document.model.DocumentTypeResponseModel
import com.nationalplasticfsm.widgets.AppCustomTextView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Saheli
 */
class DocumentRepoFeatureNewFragment : BaseFragment(), View.OnClickListener {
    private lateinit var fromOrganization: AppCustomTextView
    private lateinit var ownFiles: AppCustomTextView
    private lateinit var TabPagerAdapter: TabPagerAdapter
    private lateinit var dayConsViewPager: ViewPager

    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_document_repo, container, false)
        initView(view)
        initAdapter()
        return view
    }

    private fun initView(view: View) {



        fromOrganization = view.findViewById(R.id.fromOrganization_TV)
        ownFiles = view.findViewById(R.id.ownFiles_TV)
        dayConsViewPager = view.findViewById(R.id.day_cons_viewpager)
        TabPagerAdapter = TabPagerAdapter(fragmentManager)
        fromOrganization.setOnClickListener(this)
        ownFiles.setOnClickListener(this)
        dayConsViewPager.currentItem = 0
        isOragnizerWise(true)
        dayConsViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    CustomStatic.IsChooseTab = false
                    CustomStatic.IsDocZero = true
                    isOragnizerWise(true)
                } else {
                    CustomStatic.IsChooseTab = true
                    CustomStatic.IsDocZero = false
                    isOragnizerWise(false)
                }
            }

        })

    }


    private fun initAdapter() {
        dayConsViewPager.adapter = TabPagerAdapter
    }

    open fun refreshAdapter() {
        dayConsViewPager.adapter?.notifyDataSetChanged()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.fromOrganization_TV -> {
                isOragnizerWise(true)
                dayConsViewPager.currentItem = 0
            }
            R.id.ownFiles_TV -> {
                isOragnizerWise(false)
                dayConsViewPager.currentItem = 1
            }
        }
    }

    fun isOragnizerWise(isOrganizer: Boolean) {
        if (isOrganizer) {
            fromOrganization.isSelected = true
            ownFiles.isSelected = false
        } else {
            fromOrganization.isSelected = false
            ownFiles.isSelected = true
        }
    }
}