package com.breezenationalplasticfsm.features.menuBeat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.CustomStatic
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.app.domain.PjpListEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.dashboard.presentation.PJPClickListner
import com.breezenationalplasticfsm.features.dashboard.presentation.PjpAdapter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

//Revision History
// 1.0 BeatCurrentStatusFrag  AppV 4.0.6  Saheli    16/01/2023 beatApi for currentTab
// 1.0 BeatCurrentStatusFrag  AppV 4.0.6  Suman 19/01/2023 show shop name with beat + area + route

class BeatCurrentStatusFrag: BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var rvBeatCurrent: RecyclerView
    private var areaNameStr = ""
    private var routeNameStr = ""
    private var beatNameStr = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.beat_current_status_frag, container, false)
        initView(view)
        return view
    }

    private fun initView(view:View){
        rvBeatCurrent = view.findViewById(R.id.rv_beat_current_status)
        getCurrentTabData()// 1.0 BeatCurrentStatusFrag  AppV 4.0.6 beatApi for currentTab

    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    // 1.0 BeatCurrentStatusFrag  AppV 4.0.6 beatApi for currentTab
    private fun getCurrentTabData() {
        try{
            val repository = MenuBeatRepositoryProvider.provideAddShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.currentTabMenubeat(Pref.session_token!!,Pref.user_id!!,Pref.SelectedBeatIDFromAttend)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        var viewResult = result as MenuBeatResponse
                        if(viewResult.status.equals("200")){
                            areaNameStr = viewResult.area_name
                            routeNameStr = viewResult.route_name
                            beatNameStr = viewResult.beat_name
                            getPjp()
                        }
                    }, { error ->
                        error.printStackTrace()
                        Timber.d("BeatCurrentStatusFrag getCurrentTabData() ${AppUtils.getCurrentDateTime()} error " + error.localizedMessage)
                    })
            )
        }
        catch (ex:Exception){
            ex.printStackTrace()
            Timber.d("BeatCurrentStatusFrag getCurrentTabData() ex ${AppUtils.getCurrentDateTime()} error " + ex.localizedMessage)
        }
    }


    private fun getPjp(){
        var pjpList = AppDatabase.getDBInstance()?.pjpListDao()?.getAllByDate(AppUtils.getCurrentDateForShopActi()) as ArrayList<PjpListEntity>
        if(!Pref.SelectedBeatIDFromAttend.equals("-1") && Pref.IsBeatRouteAvailableinAttendance && Pref.isAddAttendence){
            try{
                var beatName:String = "Beat Name: " + AppDatabase.getDBInstance()?.beatDao()?.getSingleItem(Pref.SelectedBeatIDFromAttend)!!.name
                var beatShopSize = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(Pref.SelectedBeatIDFromAttend).size
            }catch (ex:Exception){
                ex.printStackTrace()
            }
            scope.launch {
                pjpList = loadpjpWithThread(pjpList)
            }.invokeOnCompletion {
                if (pjpList != null && pjpList.isNotEmpty()) {
                    rvBeatCurrent.visibility = View.VISIBLE
                    var pjpAdapterNew = PjpAdapter(mContext, pjpList, object : PJPClickListner {
                        override fun visitShop(shop: Any) {
                            if (!Pref.isAddAttendence) {
                                (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                            }
                            else {
                                val nearbyShop: AddShopDBModelEntity = shop as AddShopDBModelEntity
                                (mContext as DashboardActivity).callShopVisitConfirmationDialog(nearbyShop.shopName, nearbyShop.shop_id)
                            }
                        }
                    })
                    rvBeatCurrent.adapter = pjpAdapterNew
                }
                else {
                    rvBeatCurrent.visibility = View.GONE
                }
            }
        }
        else{
            if (pjpList != null && pjpList.isNotEmpty()) {
                rvBeatCurrent.visibility = View.VISIBLE
                rvBeatCurrent.adapter = PjpAdapter(mContext, pjpList, object : PJPClickListner {
                    override fun visitShop(shop: Any) {
                        if (!Pref.isAddAttendence) {
                            (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                        }
                        else {
                            val nearbyShop: AddShopDBModelEntity = shop as AddShopDBModelEntity
                            (mContext as DashboardActivity).callShopVisitConfirmationDialog(nearbyShop.shopName, nearbyShop.shop_id)
                        }
                    }
                })
            }
            else {
                rvBeatCurrent.visibility = View.GONE
            }
        }
    }

    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    suspend fun loadpjpWithThread(pjL:ArrayList<PjpListEntity>): ArrayList<PjpListEntity>{

        val res  = CoroutineScope(Dispatchers.IO).launch {
            try {
                var shopListWithBeat:Any
                if(Pref.IsDistributorSelectionRequiredinAttendance){
                    shopListWithBeat = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWiseDD(Pref.SelectedBeatIDFromAttend,Pref.SelectedDDIDFromAttend)
                }else{
                    shopListWithBeat = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBeatWise(Pref.SelectedBeatIDFromAttend)
                }
                if(shopListWithBeat.size>0){
                    for(l in 0..shopListWithBeat.size-1){
                        var obj :PjpListEntity = PjpListEntity()
                        if(pjL.size>0){
                            obj.pjp_id=(pjL.get(pjL.size-1).pjp_id!!.toInt()+1).toString()
                        }else{
                            obj.pjp_id="1"
                        }
                        obj.from_time=""
                        obj.to_time=""
                        var beatName = AppDatabase.getDBInstance()?.beatDao()?.getSingleItem(shopListWithBeat.get(l).beat_id)!!.name
                        obj.customer_name="Area : $areaNameStr \nRoute : $routeNameStr\n"+"${Pref.beatText}"+" : "+beatName+"\n"+"${Pref.shopText}"+" : "+shopListWithBeat.get(l).shopName
                        obj.customer_id=shopListWithBeat.get(l).shop_id
                        obj.location=""
                        obj.date=AppUtils.getCurrentDateForShopActi()
                        obj.remarks=""
                        pjL.add(obj)
                        println("pjp_tag ${obj.pjp_id}");
                    }
                }
                true
            } catch (ex: Exception) {
                ex.printStackTrace()
                println("tag_ res error ${Thread.currentThread().name}")
                false
            }
        }
        res.join()
        return pjL
    }

}