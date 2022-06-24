package com.nationalplasticfsm.features.dailyPlan.prsentation

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.elvishew.xlog.XLog
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.NetworkConstant
import com.nationalplasticfsm.app.SearchListener
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.base.presentation.BaseActivity
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.features.dailyPlan.api.PlanRepoProvider
import com.nationalplasticfsm.features.dailyPlan.model.AllPlanListDataModel
import com.nationalplasticfsm.features.dailyPlan.model.AllPlanListResponseModel
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.widgets.AppCustomTextView
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Saikat on 03-01-2020.
 */
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


        return view
    }

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

                            XLog.d("AllPlanList RESPONSE=======> " + planListResponse.status)

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
                            XLog.d("AllPlanList ERROR=======> " + error.localizedMessage)
                        })
        )
    }

    private fun initAdapter(plan_data: ArrayList<AllPlanListDataModel>?) {
        allPlanAdapter = AllPlanAdapter(mContext, plan_data!!)
        rv_all_plan_list.adapter = allPlanAdapter
    }
}