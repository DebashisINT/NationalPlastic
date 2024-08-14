package com.breezenationalplasticfsm.features.dailyPlan.prsentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.MaterialSearchView
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.SearchListener
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dailyPlan.api.PlanRepoProvider
import com.breezenationalplasticfsm.features.dailyPlan.model.AllPlanListDataModel
import com.breezenationalplasticfsm.features.dailyPlan.model.AllPlanListResponseModel
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Saikat on 03-01-2020.
 */
// Revision History
// 1.0 AllShopListFragment saheli 24-02-2032 AppV 4.0.7 mantis 0025683
class AllShopListFragment : BaseFragment() {

    private lateinit var mContext: Context

    private lateinit var rv_all_plan_list: RecyclerView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var tv_no_data: AppCustomTextView
    private lateinit var rl_all_plan_list_main: RelativeLayout

    private var plan_data: ArrayList<AllPlanListDataModel>? = null
    private var allPlanAdapter: AllPlanAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_all_shop_list, container, false)

        initView(view)
        getAllShopList()


        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    if (plan_data != null && plan_data!!.size > 0)
                        allPlanAdapter?.updateList(plan_data)
                } else {
                    if (plan_data != null && plan_data!!.size > 0)
                        allPlanAdapter?.filter?.filter(query)
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
        catch (ex: java.lang.Exception) {
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
            catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

//            tv_search_frag_order_type_list.setText(t)
//            tv_search_frag_order_type_list.setSelection(t.length);
        }
    }
    // 1.0 MicroLearningListFragment AppV 4.0.7 mantis 0025683 end

    private fun initView(view: View) {
        rv_all_plan_list = view.findViewById(R.id.rv_all_plan_list)
        rv_all_plan_list.layoutManager = LinearLayoutManager(mContext)

        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()

        rl_all_plan_list_main = view.findViewById(R.id.rl_all_plan_list_main)
        rl_all_plan_list_main.setOnClickListener(null)

        tv_no_data = view.findViewById(R.id.tv_no_data)
    }

    private fun getAllShopList() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            tv_no_data.visibility = View.VISIBLE
            return
        }

        val repository = PlanRepoProvider.planListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getAllPlanList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->

                            progress_wheel.stopSpinning()

                            val planListResponse = result as AllPlanListResponseModel

                            Timber.d("AllPlanList RESPONSE=======> " + planListResponse.status)

                            if (planListResponse.status == NetworkConstant.SUCCESS) {
                                if (planListResponse.plan_data != null && planListResponse.plan_data!!.size > 0) {
                                    tv_no_data.visibility = View.GONE

                                    plan_data = planListResponse.plan_data!!
                                    initAdapter(planListResponse.plan_data)

                                } else {
                                    tv_no_data.visibility = View.VISIBLE
                                    (mContext as DashboardActivity).showSnackMessage(planListResponse.message!!)
                                }
                            } else {
                                tv_no_data.visibility = View.VISIBLE
                                (mContext as DashboardActivity).showSnackMessage(planListResponse.message!!)
                            }

                        }, { error ->
                            BaseActivity.isApiInitiated = false
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            tv_no_data.visibility = View.VISIBLE
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            Timber.d("AllPlanList ERROR=======> " + error.localizedMessage)
                        })
        )
    }

    private fun initAdapter(plan_data: ArrayList<AllPlanListDataModel>?) {
        allPlanAdapter = AllPlanAdapter(mContext, plan_data!!)
        rv_all_plan_list.adapter = allPlanAdapter
    }
}