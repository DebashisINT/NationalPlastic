package com.breezenationalplasticfsm.features.orderITC

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezedsm.app.domain.NewOrderDataEntity
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.app.domain.NewOrderProductDataEntity
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.FTStorageUtils
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.location.LocationWizard
import com.breezenationalplasticfsm.features.login.api.productlistapi.ProductListRepoProvider
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import com.downloader.Progress
import com.google.android.material.textfield.TextInputEditText
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Random

class CartListFrag: BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context

    private lateinit var rv_list: RecyclerView
    private lateinit var progress_wheel: ProgressWheel

    private lateinit var cartAdapter: AdapterCartList
    private lateinit var tv_totalItem: TextView
    private lateinit var tv_totalValue: TextView
    private lateinit var ll_placeOrder: LinearLayout

    var isOrderProcessing:Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        var shop_id: String = ""
        var iseditCommit:Boolean = true
        fun getInstance(objects: Any): CartListFrag {
            val fragment = CartListFrag()
            shop_id = (objects as FinalData).shop_id
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_cart_l, container, false)
        initView(view)
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initView(view: View?) {
        rv_list = view!!.findViewById(R.id.rv_ord_pro_list_frag_cart_list)
        progress_wheel = view.findViewById(R.id.pw_frag_ord_cart_list)
        tv_totalItem = view.findViewById(R.id.tv_ord_prod_cart_frag_total_item)
        tv_totalValue = view.findViewById(R.id.tv_ord_prod_cart_frag_total_value)
        ll_placeOrder = view.findViewById(R.id.ll_ord_prod_cart_frag_place_order)

        progress_wheel.stopSpinning()

        ll_placeOrder.setOnClickListener(this)

        tv_totalItem.text= ProductListFrag.finalOrderDataList.sumOf { it.submitedQty.toInt() }.toString()
        tv_totalValue.text= String.format("%.2f",ProductListFrag.finalOrderDataList!!.sumByDouble { it.submitedQty.toInt() * it.submitedRate.toDouble() }).toString()

        showCartData()
    }

    fun showCartData(){
        cartAdapter = AdapterCartList(mContext,ProductListFrag.finalOrderDataList,object:AdapterCartList.OnCartOptiOnClick{
            override fun onDelChangeClick(cartSize: Int) {
                tv_totalItem.text= ProductListFrag.finalOrderDataList.sumOf { it.submitedQty.toInt() }.toString()
                tv_totalValue.text= String.format("%.2f",ProductListFrag.finalOrderDataList!!.sumByDouble { it.submitedQty.toInt() * it.submitedRate.toDouble() }).toString()

            }

            override fun onRateQtyChange() {
                tv_totalItem.text= ProductListFrag.finalOrderDataList.sumOf { it.submitedQty.toInt() }.toString()
                tv_totalValue.text= String.format("%.2f",ProductListFrag.finalOrderDataList!!.sumByDouble { it.submitedQty.toInt() * it.submitedRate.toDouble() }).toString()
            }

        })
        rv_list.adapter = cartAdapter
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            ll_placeOrder.id ->{
                if(iseditCommit){
                    ll_placeOrder.isEnabled = false
                    showCheckAlert("Order Confirmation", "Would you like to confirm the order?")
                }else{
                    ll_placeOrder.isEnabled = true
                    openDialog("Please click on tick to save this edit.")
                }
            }
        }
    }

    private fun showCheckAlert(header: String, title: String) {
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_yes_no)
        val tvHeader = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
        val tvBody = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
        val dialogNo = simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

        tvHeader.text = "Order Confirmation"
        tvBody.text="Would you like to confirm the order?"
        dialogYes.setOnClickListener {
            simpleDialog.cancel()
            if (!Pref.isShowOrderRemarks)
                saveOrder()
            else
                showRemarksAlert()
        }
        dialogNo.setOnClickListener {
            ll_placeOrder.isEnabled = true
            simpleDialog.cancel()
        }
        simpleDialog.show()
    }

    private fun saveOrder(remarks:String=""){
        isOrderProcessing = true
        ll_placeOrder.isEnabled = true
        progress_wheel.spin()
        Handler().postDelayed(Runnable {
            val orderListDetails = NewOrderDataEntity()

            val todayOrdL = AppDatabase.getDBInstance()!!.newOrderDataDao().getTodayOrder(AppUtils.getCurrentDateForShopActi()) as ArrayList<NewOrderDataEntity>
            if(todayOrdL.size==0){
                orderListDetails.order_id = Pref.user_id + AppUtils.getCurrentDateMonth() + "0001"
            }else{
                val lastId = todayOrdL[todayOrdL.size-1].order_id?.toLong()
                val finalId = lastId!! + 1
                orderListDetails.order_id = finalId.toString()
            }
            orderListDetails.order_date = AppUtils.getCurrentDateForShopActi()
            orderListDetails.order_time = AppUtils.getCurrentTime()
            orderListDetails.order_date_time = orderListDetails.order_date+" "+orderListDetails.order_time
            orderListDetails.shop_id = shop_id

            val addShopData = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(shop_id)
            var mRadious: Int = LocationWizard.NEARBY_RADIUS
            var location = Location("")
            location.latitude = Pref.current_latitude.toDouble()
            location.longitude = Pref.current_longitude.toDouble()
            var shopLocation = Location("")
            shopLocation.latitude = addShopData.shopLat
            shopLocation.longitude = addShopData.shopLong
            val isShopNearby = FTStorageUtils.checkShopPositionWithinRadious(location, shopLocation, mRadious)

            orderListDetails.shop_name = addShopData.shopName
            orderListDetails.shop_type = addShopData.type
            if (isShopNearby) {
                orderListDetails.isInrange = true
            }else{
                orderListDetails.isInrange = false
            }
            orderListDetails.order_lat = Pref.current_latitude.toString()
            orderListDetails.order_long = Pref.current_longitude.toString()
            orderListDetails.shop_addr = addShopData.address
            orderListDetails.shop_pincode = addShopData.pinCode
            orderListDetails.order_total_amt = String.format("%.2f",ProductListFrag.finalOrderDataList!!.sumByDouble { it.submitedQty.toInt() * it.submitedRate.toDouble() }).toString()
            orderListDetails.order_remarks = remarks
            orderListDetails.isUploaded = false
            AppDatabase.getDBInstance()!!.newOrderDataDao().insert(orderListDetails)
            doAsync {
                var orderProductDtls : ArrayList<NewOrderProductDataEntity> = ArrayList()
                for(i in 0..ProductListFrag.finalOrderDataList.size-1){
                    var obj : NewOrderProductDataEntity = NewOrderProductDataEntity()
                    obj.order_id = orderListDetails.order_id
                    obj.product_id = ProductListFrag.finalOrderDataList.get(i).product_id
                    obj.product_name = ProductListFrag.finalOrderDataList.get(i).product_name
                    obj.submitedQty = ProductListFrag.finalOrderDataList.get(i).submitedQty
                    obj.submitedSpecialRate = ProductListFrag.finalOrderDataList.get(i).submitedRate
                    orderProductDtls.add(obj)
                }
                AppDatabase.getDBInstance()!!.newOrderProductDataDao().insertAll(orderProductDtls)
                uiThread {
                    if(AppUtils.isOnline(mContext)){
                        syncOrd(orderListDetails.order_id,addShopData)
                    }else{
                        progress_wheel.stopSpinning()
                        msgShow("${AppUtils.hiFirstNameText()}. Your order for ${addShopData.shopName} has been placed successfully. Order No. is ${orderListDetails.order_id}")
                    }

                }
            }
        }, 1000)
    }

    private fun showRemarksAlert(){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_remarks_1)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_remarks_header_TV) as AppCustomTextView
        val tvOk = simpleDialog.findViewById(R.id.tv_remarks_ok) as AppCustomTextView
        val ivCancel = simpleDialog.findViewById(R.id.iv_remarks_cancel) as ImageView
        val etRemarks = simpleDialog.findViewById(R.id.et_remarks) as TextInputEditText
        dialogHeader.text = "Order Remarks"
        tvOk.setOnClickListener {
            var remarks = etRemarks.text.toString().trim()
            saveOrder(remarks)
            simpleDialog.cancel()
        }
        ivCancel.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()
    }

    private fun syncOrd(ordId:String, addShopData: AddShopDBModelEntity){
        progress_wheel.spin()
        var ordDtls = AppDatabase.getDBInstance()!!.newOrderDataDao().getOrderByID(ordId)
        var ordProductDtls = AppDatabase.getDBInstance()!!.newOrderProductDataDao().getProductsOrder(ordId)
        var syncOrd = SyncOrd()
        var syncOrdProductL:ArrayList<SyncOrdProductL> = ArrayList()

        doAsync {
            syncOrd.user_id = Pref.user_id!!
            syncOrd.order_id = ordId
            syncOrd.order_date = ordDtls.order_date
            syncOrd.order_time = ordDtls.order_time
            syncOrd.order_date_time = ordDtls.order_date_time
            syncOrd.shop_id = ordDtls.shop_id
            syncOrd.shop_name = ordDtls.shop_name
            syncOrd.shop_type = ordDtls.shop_type
            syncOrd.isInrange = if(ordDtls.isInrange) 1 else 0
            syncOrd.order_lat = ordDtls.order_lat
            syncOrd.order_long = ordDtls.order_long
            syncOrd.shop_addr = ordDtls.shop_addr
            syncOrd.shop_pincode = ordDtls.shop_pincode
            syncOrd.order_total_amt = ordDtls.order_total_amt.toDouble()
            syncOrd.order_remarks = ordDtls.order_remarks

            for(i in 0..ordProductDtls.size-1){
                var obj = SyncOrdProductL()
                obj.order_id=ordProductDtls.get(i).order_id
                obj.product_id=ordProductDtls.get(i).product_id
                obj.product_name=ordProductDtls.get(i).product_name
                obj.submitedQty=ordProductDtls.get(i).submitedQty.toDouble()
                obj.submitedSpecialRate=ordProductDtls.get(i).submitedSpecialRate.toDouble()

                syncOrdProductL.add(obj)
            }
            syncOrd.product_list = syncOrdProductL

            uiThread {
                val repository = ProductListRepoProvider.productListProvider()
                BaseActivity.compositeDisposable.add(
                    repository.syncProductListITC(syncOrd)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                doAsync {
                                    AppDatabase.getDBInstance()!!.newOrderDataDao().updateIsUploaded(syncOrd.order_id,true)
                                    uiThread {
                                        progress_wheel.stopSpinning()
                                        msgShow("${AppUtils.hiFirstNameText()}. Your order for ${addShopData.shopName} has been placed successfully. Order No. is ${syncOrd.order_id}")
                                    }
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
                )
            }
        }

    }

    fun openDialog(text:String){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_ok)

        try {
            simpleDialog.setCancelable(true)
            simpleDialog.setCanceledOnTouchOutside(false)
            val dialogName = simpleDialog.findViewById(R.id.tv_dialog_ok_name) as AppCustomTextView
            val dialogCross = simpleDialog.findViewById(R.id.tv_dialog_ok_cancel) as ImageView
            dialogName.text = AppUtils.hiFirstNameText()
            dialogCross.setOnClickListener {
                simpleDialog.cancel()
                ll_placeOrder.isEnabled = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
        dialogHeader.text = text
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()
    }

    fun msgShow(msg:String){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val headerTv = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        val bodyTv = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val okTV = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        headerTv.text = "Congrats!"
        bodyTv.text = msg
        okTV.setOnClickListener({ view ->
            simpleDialog.cancel()
            (mContext as DashboardActivity).loadFragment(FragType.OrderListFrag, false, shop_id)
        })
        simpleDialog.show()
        voiceOrderMsg()
    }

    private fun voiceOrderMsg() {
        if (Pref.isVoiceEnabledForOrderSaved) {
            val msg = "Hi, Order saved successfully."
            val speechStatus = (mContext as DashboardActivity).textToSpeech.speak(
                msg,
                TextToSpeech.QUEUE_FLUSH,
                null
            )
            if (speechStatus == TextToSpeech.ERROR)
                Log.e("Add Order", "TTS error in converting Text to Speech!")
        }
    }
}