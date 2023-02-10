package com.nationalplasticfsm.features.menuBeat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.base.presentation.BaseActivity
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.elvishew.xlog.XLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// 1.0 BeatHierarchyStatusFrag AppV 4.0.6 Suman 18-01-2023  thread updation mantis 0025587
// 2.0 BeatHierarchyStatusFrag AppV 4.0.6 Suman 20-01-2023  design updation mantis
// 3.0 BeatHierarchyStatusFrag AppV 4.0.6 Suman 20-01-2023  hirerchy design updation

class BeatHierarchyStatusFrag: BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var mRvDetails: RecyclerView
    private lateinit var hirerchyRootAdapter: AdapterHirerchyArea

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.beat_hierarchy_status_frag, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View){
        mRvDetails = view.findViewById(R.id.rv_beat_hierar_status_frag)
        getHirerchyData()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    private fun getHirerchyData() {
        try{
            val repository = MenuBeatRepositoryProvider.provideAddShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.hirerchyTabMenubeat(Pref.session_token!!, Pref.user_id!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        var viewResult = result as MenuBeatAreaRouteResponse
                        if(viewResult.status.equals("200")){
                            setAdapter(viewResult.area_list)
                        }
                    }, { error ->
                        error.printStackTrace()
                        XLog.d("BeatHierarchyStatusFrag getHirerchyData() ${AppUtils.getCurrentDateTime()} error " + error.localizedMessage)
                    })
            )
        }
        catch (ex:Exception){
            ex.printStackTrace()
            XLog.d("BeatHierarchyStatusFrag getHirerchyData() ex ${AppUtils.getCurrentDateTime()} error " + ex.localizedMessage)
        }
    }

    private fun setAdapter(areaL:ArrayList<MenuBeatAreaResponse>){
        hirerchyRootAdapter = AdapterHirerchyArea(mContext,areaL)
        mRvDetails.adapter = hirerchyRootAdapter
    }

}