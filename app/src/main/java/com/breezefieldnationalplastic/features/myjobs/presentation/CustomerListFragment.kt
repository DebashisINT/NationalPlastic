package com.breezefieldnationalplastic.features.myjobs.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.MaterialSearchView
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.SearchListener
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.myjobs.api.MyJobRepoProvider
import com.breezefieldnationalplastic.features.myjobs.model.CustListResponseModel
import com.breezefieldnationalplastic.features.myjobs.model.CustomerDataModel
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

// Revision Histroy
// 1.0 CustomerListFragment saheli 24-02-2032 AppV 4.0.7 mantis 0025683
class CustomerListFragment: BaseFragment() {

    private lateinit var mContext: Context

    private lateinit var rv_cust_list: RecyclerView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var tv_no_data_available: AppCustomTextView
    private lateinit var date_CV: CardView
    private lateinit var tv_cust_no: AppCustomTextView

    private var customerList: ArrayList<CustomerDataModel>?= null
    private var adapter: CustomerListAdapter?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext= context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_myjobs, container, false)

        initView(view)
        getCustomerListApi()

        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    customerList?.let {
                        adapter?.refreshList(it)
                        tv_cust_no.text = "Total customer(s): " + it.size
                    }
                } else {
                    adapter?.filter?.filter(query)
                }
            }
        })

        // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 start
        (mContext as DashboardActivity).searchView.setVoiceIcon(R.drawable.ic_mic)
        (mContext as DashboardActivity).searchView.setOnVoiceClickedListener({ startVoiceInput() })
        // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 end

        return  view
    }
    // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 start
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
    // 1.0 MemberListFragment AppV 4.0.7 mantis 0025683 end


    private fun initView(view: View) {
        rv_cust_list = view.findViewById(R.id.rv_cust_list)
        progress_wheel = view.findViewById(R.id.progress_wheel)
        tv_no_data_available = view.findViewById(R.id.tv_no_data_available)
        date_CV = view.findViewById(R.id.date_CV)
        tv_cust_no = view.findViewById(R.id.tv_cust_no)

        date_CV.visibility = View.GONE
        tv_cust_no.visibility = View.VISIBLE
        progress_wheel.stopSpinning()

        rv_cust_list.layoutManager = LinearLayoutManager(mContext)
    }

    private fun getCustomerListApi() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        progress_wheel.spin()
        val repository =  MyJobRepoProvider.jobRepoProvider()
        BaseActivity.compositeDisposable.add(
                repository.getCustomerList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as CustListResponseModel
                            progress_wheel.stopSpinning()

                            when (response.status) {
                                NetworkConstant.SUCCESS -> {
                                    customerList = response.customer_list!!
                                    initAdapter()
                                }
                                else -> (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun initAdapter() {
        tv_no_data_available.visibility = View.GONE
        tv_cust_no.text = "Total customer(s): " + customerList?.size

        adapter = CustomerListAdapter(mContext, customerList!!, {
            tv_cust_no.text = "Total customer(s): " + it
        })

        rv_cust_list.adapter = adapter
    }
}