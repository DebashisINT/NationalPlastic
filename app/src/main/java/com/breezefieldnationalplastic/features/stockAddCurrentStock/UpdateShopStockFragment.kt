package com.breezefieldnationalplastic.features.stockAddCurrentStock

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.domain.CurrentStockEntryModelEntity
import com.breezefieldnationalplastic.app.domain.CurrentStockEntryProductModelEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.stockAddCurrentStock.`interface`.ShowStockOnClick
import com.breezefieldnationalplastic.features.stockAddCurrentStock.adapter.AdapterShowStockList
import com.breezefieldnationalplastic.features.stockAddCurrentStock.api.ShopAddStockProvider
import com.breezefieldnationalplastic.features.stockAddCurrentStock.model.CurrentStockGetData
import com.breezefieldnationalplastic.features.stockCompetetorStock.model.CompetetorStockGetData
import com.breezefieldnationalplastic.widgets.AppCustomTextView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber

//1.0 Rev UpdateShopStockFragment AppV 4.0.8 saheli    12/05/2023 mantis 26101
class UpdateShopStockFragment : BaseFragment(), View.OnClickListener{

    private lateinit var mContext: Context
    private lateinit var myshop_name_TV: AppCustomTextView
    private lateinit var myshop_addr_TV: AppCustomTextView
    private lateinit var myshop_contact_TV: AppCustomTextView
    private lateinit var addShopStockLL : LinearLayout
    private lateinit var rvStockDetails : RecyclerView
    private lateinit var  tvattachment:AppCustomTextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    companion object {
        var mAddShopDataObj: AddShopDBModelEntity? = null
        var shop_id:String = ""
        fun getInstance(objects: Any): UpdateShopStockFragment {
            val updateStockFragment = UpdateShopStockFragment()
            if (!TextUtils.isEmpty(objects.toString())) {
                shop_id=objects.toString()
                mAddShopDataObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shop_id)

            }
            return updateStockFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_update_shop_stock, container, false)

        initView(view)
        return view
    }

    override fun onResume() {
        super.onResume()

        var shopAll=AppDatabase.getDBInstance()!!.shopCurrentStockEntryDao().getShopStockAll()
        if (shopAll != null && shopAll.isNotEmpty()){
            getStockList()
        }else{
            if (AppUtils.isOnline(mContext)){
                getCurrentStockApi()
            }else{
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                return
            }
        }
    }

    private fun initView(view: View){
        myshop_name_TV = view.findViewById(R.id.myshop_name_TV)
        myshop_addr_TV = view.findViewById(R.id.myshop_address_TV)
        myshop_contact_TV = view.findViewById(R.id.tv_contact_number)
        addShopStockLL = view.findViewById(R.id.ll_frag_update_shop_stock_add)
        rvStockDetails = view.findViewById(R.id.rv_current_stock_list)
        rvStockDetails.layoutManager=LinearLayoutManager(mContext)

        myshop_name_TV.text = mAddShopDataObj!!.shopName
        myshop_addr_TV.text = mAddShopDataObj!!.address
        myshop_contact_TV.text = "Owner Contact Number: " +mAddShopDataObj!!.ownerContactNumber
        //1.0 start Rev UpdateShopStockFragment AppV 4.0.8 saheli    12/05/2023 mantis 26101
        tvattachment = view.findViewById(R.id.tv_attachment_frag_update_shop_stock)

        if(Pref.isCurrentStockEnable){
            if(Pref.IsCurrentStockApplicableforAll){
                if(Pref.IsAttachmentAvailableForCurrentStock){
                    tvattachment.visibility = View.VISIBLE
                }else{
                    tvattachment.visibility = View.GONE
                }
            }else{
                tvattachment.visibility = View.GONE
            }
        }else{
            tvattachment.visibility = View.GONE
        }


        tvattachment.setOnClickListener(this)
        //1.0 end Rev UpdateShopStockFragment AppV 4.0.8 saheli    12/05/2023 mantis 26101
        addShopStockLL.setOnClickListener(this)
    }

    private fun getStockList(){
        var list = AppDatabase.getDBInstance()?.shopCurrentStockEntryDao()!!.getShopStockAllByShopID(shop_id)
        if(list.size>0){
            rvStockDetails.adapter= AdapterShowStockList(mContext,list,object: ShowStockOnClick{
                override fun stockListOnClick(stockID: String) {
                    (mContext as DashboardActivity).loadFragment(FragType.ViewStockDetailsFragment, true, stockID)
                }

                //1.0 start Rev UpdateShopStockFragment AppV 4.0.8 saheli    12/05/2023 mantis 26101
                override fun stockattachment(stockID: String) {
                    (mContext as DashboardActivity).loadFragment(FragType.MultipleImageFileUploadonStock, true, stockID)
                }
                //1.0 end Rev UpdateShopStockFragment AppV 4.0.8 saheli    12/05/2023 mantis 26101
            })
        }else{
            return
        }

    }

    override fun onClick(p0: View?) {
        if(p0!=null){
            when(p0.id){
                R.id.ll_frag_update_shop_stock_add ->{
                    if (Pref.isAddAttendence){
                        (mContext as DashboardActivity).loadFragment(FragType.AddShopStockFragment, true, mAddShopDataObj!!)
                    }
                }
            }
        }
    }

    fun update() {
        getStockList()
    }

    private fun getCurrentStockApi(){
        try{
            val repository = ShopAddStockProvider.provideShopAddStockRepository()
            BaseActivity.compositeDisposable.add(
                    repository.getCurrStockList(Pref.session_token!!, Pref.user_id!!, "")
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                Timber.d("Stock/CurrentStockList " + result.status)
                                val response = result as CurrentStockGetData
                                if (response.status == NetworkConstant.SUCCESS){
                                    if (response.stock_list!! != null && response.stock_list!!.isNotEmpty()){

                                        doAsync {

                                            for(i in response.stock_list?.indices!!){
                                                var obj = CurrentStockEntryModelEntity()
                                                obj.user_id=Pref.user_id!!
                                                obj.stock_id=response.stock_list?.get(i)?.stock_id!!
                                                obj.shop_id=response.stock_list?.get(i)?.shop_id!!
                                                obj.visited_datetime=response.stock_list?.get(i)?.visited_datetime!!
                                                obj.visited_date=response.stock_list?.get(i)?.visited_datetime?.take(10)
                                                obj.total_product_stock_qty=response.stock_list?.get(i)?.total_qty!!
                                                obj.isUploaded=true
                                                AppDatabase.getDBInstance()?.shopCurrentStockEntryDao()!!.insert(obj)

                                                val proDuctList=response.stock_list?.get(i)?.product_list
                                                for(j in proDuctList?.indices!!){
                                                    var objjj = CurrentStockEntryProductModelEntity()
                                                    objjj.stock_id=response.stock_list?.get(i)?.stock_id!!
                                                    objjj.shop_id= response.stock_list?.get(i)?.shop_id!!
                                                    objjj.product_id= proDuctList?.get(j)!!.product_id.toString()!!
                                                    objjj.product_stock_qty=proDuctList?.get(j)!!.product_stock_qty!!
                                                    objjj.user_id=Pref.user_id
                                                    objjj.isUploaded=true

                                                    AppDatabase.getDBInstance()?.shopCurrentStockProductsEntryDao()!!.insert(objjj)
                                                }

                                            }

                                            uiThread {
                                                getStockList()
                                            }
                                        }
                                    }else{
                                        getStockList()
                                    }

                                }
                            },{error ->
                                if (error == null) {
                                    Timber.d("Stock/CurrentStockList : ERROR " + "UNEXPECTED ERROR IN Add Stock ACTIVITY API")
                                } else {
                                    Timber.d("Stock/CurrentStockList : ERROR " + error.localizedMessage)
                                    error.printStackTrace()
                                }
                            })
            )
        }catch (ex:Exception){
            Timber.d("Stock/CurrentStockList : ERROR " + "UNEXPECTED ERROR IN Add Stock ACTIVITY API")
        }
    }

}