package com.nationalplasticfsm.features.activities.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.*
import com.nationalplasticfsm.app.domain.ActivityEntity
import com.nationalplasticfsm.app.domain.AddShopDBModelEntity
import com.nationalplasticfsm.app.types.FragType
import com.nationalplasticfsm.app.uiaction.IntentActionable
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.base.presentation.BaseActivity
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.features.activities.api.ActivityRepoProvider
import com.nationalplasticfsm.features.activities.model.ActivityListResponseModel
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.widgets.AppCustomTextView
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

// Revision History
// 1.0 ActivityShopListFragment saheli 24-02-2032 AppV 4.0.7 mantis 0025683
class ActivityShopListFragment : BaseFragment() {

    private lateinit var mContext: Context

    private lateinit var tv_count_no: AppCustomTextView
    private lateinit var rv_shop_list: RecyclerView
    private lateinit var tv_no_data_available: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel

    private var adapter: ActivityListAdapter?= null
    private var shopList: ArrayList<AddShopDBModelEntity>?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_activity_shop_list, container, false)

        initView(view)
        initAdapter()

        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                try {
                    if (query.isBlank()) {
                        adapter?.refreshList(shopList!!)
                        tv_count_no.text = "Total count: " + shopList?.size
                    } else
                        adapter?.filter?.filter(query)

                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 start
        (mContext as DashboardActivity).searchView.setVoiceIcon(R.drawable.ic_mic)
        (mContext as DashboardActivity).searchView.setOnVoiceClickedListener({ startVoiceInput() })
        // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 end

        return view
    }

    // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 start
    private fun startVoiceInput() {
        try {
            val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?")
            try {
                startActivityForResult(intent, MaterialSearchView.REQUEST_VOICE)
            } catch (a: ActivityNotFoundException) {
                a.printStackTrace()
            }
        }
        catch (ex:Exception) {
            ex.printStackTrace()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MaterialSearchView.REQUEST_VOICE){
            try {
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                var t= result!![0]
                (mContext as DashboardActivity).searchView.setQuery(t,false)
            }
            catch (ex:Exception) {
                ex.printStackTrace()
            }

//            tv_search_frag_order_type_list.setText(t)
//            tv_search_frag_order_type_list.setSelection(t.length);
        }
    }
    // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 end

    private fun initView(view: View) {
        view.apply {
            tv_count_no = findViewById(R.id.tv_count_no)
            rv_shop_list = findViewById(R.id.rv_shop_list)
            tv_no_data_available = findViewById(R.id.tv_no_data_available)
            progress_wheel = findViewById(R.id.progress_wheel)
        }

        progress_wheel.stopSpinning()

        rv_shop_list.layoutManager = LinearLayoutManager(mContext)
    }

    private fun initAdapter() {
        val list= AppDatabase.getDBInstance()?.addShopEntryDao()?.all

        val activityShopList = ArrayList<AddShopDBModelEntity>()
        val notActivityShopList = ArrayList<AddShopDBModelEntity>()
        shopList = ArrayList()

        list?.forEach {
            val activityList = AppDatabase.getDBInstance()?.activDao()?.getShopIdWise(it.shop_id)

            if (activityList != null && activityList.isNotEmpty())
                activityShopList.add(it)
            else
                notActivityShopList.add(it)
        }

        shopList?.addAll(activityShopList)
        shopList?.addAll(notActivityShopList)

        if (list != null && list.isNotEmpty()) {
            tv_no_data_available.visibility = View.GONE
            tv_count_no.text = "Total count: " + list.size

            adapter = ActivityListAdapter(mContext, list as ArrayList<AddShopDBModelEntity>?, {
                if (TextUtils.isEmpty(it.ownerContactNumber) || it.ownerContactNumber.equals("null", ignoreCase = true)
                        || !AppUtils.isValidateMobile(it.ownerContactNumber!!)) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_phn_no_unavailable))
                } else {
                    IntentActionable.initiatePhoneCall(mContext, it.ownerContactNumber)
                }
            }, {
                (mContext as DashboardActivity).openLocationMap(it, false)
            }, {
                when (it.type) {
                    "7" -> {
                        (mContext as DashboardActivity).isFromShop = false
                        (mContext as DashboardActivity).loadFragment(FragType.ChemistActivityListFragment, true, it)
                    }
                    "8" -> {
                        (mContext as DashboardActivity).isFromShop = false
                        (mContext as DashboardActivity).loadFragment(FragType.DoctorActivityListFragment, true, it)
                    }
                    else -> (mContext as DashboardActivity).loadFragment(FragType.ActivityDetailsListFragment, true, it.shop_id)
                }
            }, {
                tv_count_no.text = "Total count: $it"
            })
            rv_shop_list.adapter = adapter
        }
        else
            tv_no_data_available.visibility = View.VISIBLE
    }
}