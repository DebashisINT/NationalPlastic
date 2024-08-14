package com.breezenationalplasticfsm.features.marketAssist

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.CustomStatic
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.app.domain.OrderDetailsListEntity
import com.breezenationalplasticfsm.app.domain.ShopActivityEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.pnikosis.materialishprogress.ProgressWheel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.Locale

class ChurnProbFrag : BaseFragment(), View.OnClickListener{

    private lateinit var mContext: Context
    private lateinit var rvShopL:RecyclerView
    private lateinit var progress:ProgressWheel
    private var finalShopChurnL : ArrayList<ChurnShopL> = ArrayList()
    private lateinit var etSearchShop: EditText
    private lateinit var llSearch: LinearLayout
    private lateinit var ivMic: ImageView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    var _areLecturesLoaded = false
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !_areLecturesLoaded ) {
            _areLecturesLoaded = true;
            progress.spin()
            Handler().postDelayed(Runnable {
                calculateDataNew("")
            }, 1000)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_churn_prob, container, false)
        initView(view)
        return view
    }


    private fun initView(view: View) {
        rvShopL = view.findViewById(R.id.rv_frag_churn_prob_list)
        progress = view.findViewById(R.id.progress_wheel_frag_churn)
        etSearchShop = view.findViewById(R.id.et_frag_shop_list_ma_list_search)
        llSearch = view.findViewById(R.id.ll_frag_shop_list_ma_list_search)
        ivMic = view.findViewById(R.id.iv_frag_shop_list_ma_mic)
        llSearch.setOnClickListener(this)
        ivMic.setOnClickListener(this)


        etSearchShop.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length == 0) {
                    AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    calculateDataNew("")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ll_frag_shop_list_ma_list_search ->{
                var searchText = etSearchShop.text.toString()
                if(searchText.length>0){
                    AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    calculateDataNew(searchText)
                }
            }

            R.id.iv_frag_shop_list_ma_mic ->{
                val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                Handler().postDelayed(Runnable {
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi")
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?")
                }, 1000)
                try {
                    startActivityForResult(intent, 7009)
                    Handler().postDelayed(Runnable {

                    }, 2000)

                } catch (a: ActivityNotFoundException) {
                    a.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 7009){
            try {
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                etSearchShop.setText(result!![0].toString())
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }


    fun calculatDate(){

        doAsync {

            var shopCusFinalL = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopForChurn() as ArrayList<ChurnShopL>

            //Customers who have not made a purchase
            var orderAge = AppDatabase.getDBInstance()!!.orderDetailsListDao().getshopOrderAgeToday(AppUtils.getPrevXMonthDate(3),AppUtils.getCurrentDateyymmdd()) as ArrayList<OrderAge>
            var totalPurchaseAge= orderAge.map { it.dateAge.toInt() }.sum()
            var avgPurchaseAge = totalPurchaseAge/orderAge.size


            //Number of days since the customer's last visit: <days>
            val allShopL = AppDatabase.getDBInstance()?.addShopEntryDao()?.all as ArrayList<AddShopDBModelEntity>
            var finalShopL :ArrayList<ShopLVisit> = ArrayList()
            val startDate = SimpleDateFormat("yyyy-MM-dd").parse(AppUtils.getPrevXMonthDate(3))
            val endDate = SimpleDateFormat("yyyy-MM-dd").parse(AppUtils.getCurrentDateyymmdd())
            for(i in 0.. allShopL.size-1){
                allShopL.get(i).lastVisitedDate = AppUtils.changeDateFormat2(allShopL.get(i).lastVisitedDate).replace("/","-")
                var obj = allShopL.get(i)
                var lastVisitD = SimpleDateFormat("yyyy-MM-dd").parse(allShopL.get(i).lastVisitedDate)
                if ((lastVisitD.equals(startDate) || lastVisitD.after(startDate)) && (lastVisitD.equals(endDate) || lastVisitD.before(endDate))) {
                    finalShopL.add(ShopLVisit(obj.shop_id,obj.shopName,obj.lastVisitedDate,AppUtils.getDateDiff(obj.lastVisitedDate,AppUtils.getCurrentDateyymmdd()).toInt()))
                }
            }
            var totalVisitAge = finalShopL.map{it.lastVisitAge.toInt()}.sum()
            var avgVisitAge = totalVisitAge / finalShopL.size

            // Monetary Value: <amount>
            val ordL = AppDatabase.getDBInstance()!!.orderDetailsListDao().getLastXMonthOrderData(AppUtils.getPrevXMonthDate(3),AppUtils.getCurrentDateyymmdd()) as ArrayList<OrderDetailsListEntity>
            var totalOrderAmt = ordL.map { it.amount!!.toDouble() }.sum().toDouble()
            var uniqShopCount = ordL.distinctBy { it.shop_id }
            var avgOrdAmt = (totalOrderAmt / uniqShopCount.size).toDouble().toBigDecimal().toString()

            //Time since First Order: <days>
            var firstOrdShopL = orderAge.filter { it.countShop.toInt() == 1 }
            var totalAgeFirstOrd = firstOrdShopL.map { it.dateAge.toInt() }.sum()
            var avgAgeFirstOrd = totalAgeFirstOrd / firstOrdShopL.size

            //Customer Segment On Visit behavior: <Frequent, Infrequest>
            var allShopActivityL = AppDatabase.getDBInstance()!!.shopActivityDao().getCUstomShopActivityCount() as ArrayList<ShopActivityCnt>
            var allShopActivityCntL = allShopActivityL.map { it.cnt.toInt() * 3 }
            var avgVisit = (allShopActivityCntL.sum().toDouble() / 90)


            var allFalseL :ArrayList<ChurnShopL> = ArrayList()
            var allFalseL1 :ArrayList<ChurnShopL> = ArrayList()
            var allFalseL2 :ArrayList<ChurnShopL> = ArrayList()
            var allFalseL3 :ArrayList<ChurnShopL> = ArrayList()
            var allFalseL4 :ArrayList<ChurnShopL> = ArrayList()
            var allFalseL5 :ArrayList<ChurnShopL> = ArrayList()
            var allFalseL6 :ArrayList<ChurnShopL> = ArrayList()
            var allFalseLAny2 :ArrayList<ChurnShopL> = ArrayList()

            for(k in 0..shopCusFinalL.size-1){
                var shopObj = shopCusFinalL.get(k)

                try{
                    var ordAge = AppDatabase.getDBInstance()!!.orderDetailsListDao().getshopOrderAgeTodayByShop(AppUtils.getPrevXMonthDate(3),AppUtils.getCurrentDateyymmdd(),shopObj.shop_id) as OrderAge
                    if(ordAge.dateAge.toInt() > avgPurchaseAge.toInt()){
                        shopCusFinalL.get(k).tag1 = false
                        shopCusFinalL.get(k).lastPurchaseAge = ordAge.dateAge.toString()
                    }else{
                        shopCusFinalL.get(k).tag1 = true
                        shopCusFinalL.get(k).lastPurchaseAge = ordAge.dateAge.toString()
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                try{
                    var lastVisitDate = AppUtils.changeDateFormat2(shopObj.lastVisitedDate).replace("/","-")
                    var lastVisitAge = AppUtils.getDateDiff(lastVisitDate,AppUtils.getCurrentDateyymmdd()).toInt()
                    if(lastVisitAge > avgVisitAge){
                        shopCusFinalL.get(k).tag2 = false
                        shopCusFinalL.get(k).lastVisitAge = lastVisitAge.toString()
                    }else{
                        shopCusFinalL.get(k).tag2 = true
                        shopCusFinalL.get(k).lastVisitAge = lastVisitAge.toString()
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                try{
                    var shopAvgOrdWithDateRange= AppDatabase.getDBInstance()!!.orderDetailsListDao().getLastXMonthOrderDataByShopID(AppUtils.getPrevXMonthDate(3),AppUtils.getCurrentDateyymmdd(),shopObj.shop_id) as ArrayList<OrderDetailsListEntity>
                    var avgShopOrdAmt =  (shopAvgOrdWithDateRange.sumOf { it.amount!!.toBigDecimal() }) / shopAvgOrdWithDateRange.size.toBigDecimal()
                    if(avgShopOrdAmt > avgOrdAmt.toBigDecimal()){
                        shopCusFinalL.get(k).tag3 = false
                        shopCusFinalL.get(k).avgShopOrdAmt = avgShopOrdAmt.toString()
                    }else{
                        shopCusFinalL.get(k).tag3 = true
                        shopCusFinalL.get(k).avgShopOrdAmt = avgShopOrdAmt.toString()
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                try{
                    var shopOrdAge = AppDatabase.getDBInstance()!!.orderDetailsListDao().getshopOrderAgeTodayByShopID(AppUtils.getPrevXMonthDate(3),AppUtils.getCurrentDateyymmdd(),shopObj.shop_id) as OrderAge
                    if(shopOrdAge.dateAge.toInt() > avgAgeFirstOrd){
                        shopCusFinalL.get(k).tag4 = false
                        shopCusFinalL.get(k).avgTimeSinceFirstOrd =  shopOrdAge.dateAge.toInt().toString()
                    }else{
                        shopCusFinalL.get(k).tag4 = true
                        shopCusFinalL.get(k).avgTimeSinceFirstOrd =  shopOrdAge.dateAge.toInt().toString()
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                try{
                    var shopVisitObj = allShopActivityL.filter { it.shopid.equals(shopObj.shop_id) }.firstOrNull()
                    var shopVisitAvg = (shopVisitObj?.cnt?.toDouble()?.div(90))?.toDouble()
                    if(shopVisitAvg!!.toDouble() > avgVisit.toDouble()){
                        shopCusFinalL.get(k).tag6 = false
                        shopCusFinalL.get(k).shopVisitAvg = shopVisitAvg!!.toInt().toString()
                    }else{
                        shopCusFinalL.get(k).tag6 = true
                        shopCusFinalL.get(k).shopVisitAvg = shopVisitAvg!!.toInt().toString()
                    }

                }catch (ex:Exception){
                    ex.printStackTrace()
                }


                if(!shopObj.tag1 && !shopObj.tag2 && !shopObj.tag3 && !shopObj.tag4 && !shopObj.tag5 && !shopObj.tag6){
                    allFalseL.add(shopObj)
                }else if(shopObj.tag1 && !shopObj.tag2 && !shopObj.tag3 && !shopObj.tag4 && !shopObj.tag5 && !shopObj.tag6){
                    allFalseL1.add(shopObj)
                }else if(!shopObj.tag1 && shopObj.tag2 && !shopObj.tag3 && !shopObj.tag4 && !shopObj.tag5 && !shopObj.tag6){
                    allFalseL2.add(shopObj)
                }else if(!shopObj.tag1 && !shopObj.tag2 && shopObj.tag3 && !shopObj.tag4 && !shopObj.tag5 && !shopObj.tag6){
                    allFalseL3.add(shopObj)
                }else if(!shopObj.tag1 && !shopObj.tag2 && !shopObj.tag3 && shopObj.tag4 && !shopObj.tag5 && !shopObj.tag6){
                    allFalseL4.add(shopObj)
                }else if(!shopObj.tag1 && !shopObj.tag2 && !shopObj.tag3 && !shopObj.tag4 && shopObj.tag5 && !shopObj.tag6){
                    allFalseL5.add(shopObj)
                }else if(!shopObj.tag1 && !shopObj.tag2 && !shopObj.tag3 && !shopObj.tag4 && !shopObj.tag5 && shopObj.tag6){
                    allFalseL6.add(shopObj)
                }
            }

            var finalL :ArrayList<ChurnShopL> = ArrayList()

            //finalL.addAll(allFalseL)
            finalL.addAll(allFalseL1)
            finalL.addAll(allFalseL2)
            finalL.addAll(allFalseL3)
            finalL.addAll(allFalseL4)
            finalL.addAll(allFalseL5)
            finalL.addAll(allFalseL6)
            finalL.addAll(allFalseLAny2)
            finalL = finalL.distinctBy { it.shop_id } as ArrayList<ChurnShopL>

         //   println("finalL.size"+finalL.size)
         //   println("finalL.toString"+finalL.toString())

            uiThread {
                progress.stopSpinning()
                rvShopL.adapter = AdapterChurnShopL(mContext,finalL)
            }
        }



    }

    fun calculateDataNew(filter:String){

        doAsync {
            /*AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest("54855_1688021010577526","548552906230003","2023-04-29T12:19:46")
            AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest1()
            AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest2()
            AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest3()
            AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest4()
            AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest5()
            AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest6()
            AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest7()
            AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest8()
            AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest9()
            AppDatabase.getDBInstance()!!.orderDetailsListDao().xTest10()*/

            //Customers who have not made a purchase 1
            var orderAge = AppDatabase.getDBInstance()!!.orderDetailsListDao().getshopOrderAgeToday90DayRestriction(AppUtils.getPrevXMonthDate(3),AppUtils.getCurrentDateyymmdd()) as ArrayList<OrderAge>
            var totalPurchaseAge : Int = orderAge.map { it.dateAge.toInt() }.sum()
            var avgPurchaseAge : Double = totalPurchaseAge.toDouble()/orderAge.size

            //Number of days since the customer's last visit 2
            var shopLLastVisitDate = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopListLastVisit() as  ArrayList<ShopLastVisit>
            for(i in 0..shopLLastVisitDate.size-1){
                shopLLastVisitDate.get(i).lastVisitedDate = AppUtils.changeDateFormat2(shopLLastVisitDate.get(i).lastVisitedDate).replace("/","-")
                shopLLastVisitDate.get(i).lastVIsitAge = AppUtils.getDateDiff(shopLLastVisitDate.get(i).lastVisitedDate,AppUtils.getCurrentDateyymmdd())
            }
            var totalVisitAge = shopLLastVisitDate.map{it.lastVIsitAge.toInt()}.sum()
            var avgVisitAge = totalVisitAge.toDouble() / shopLLastVisitDate.size

            // Monetary Value 3
            var totalOrdAmtInLast90Days = orderAge.map { it.totalAmt.toDouble() }.sum()
            var avgOrdAmtInLast90Days = totalOrdAmtInLast90Days.toDouble() / orderAge.size

            //Time since First Order 4
            var firstOrderDtls = orderAge.filter { it.countShop.toInt() == 1 }
            var totalPurchaseAgeForFirstOrd = firstOrderDtls.map { it.dateAge.toInt() }.sum()
            var avgPurchaseAgeForFirstOrd : Double = totalPurchaseAgeForFirstOrd.toDouble() / firstOrderDtls.size

            //Customer Segment On Order behavior 5
            var orderCnt = orderAge.map { it.countShop.toInt() }.sum()
            var orderCntAvg = orderCnt.toDouble() / orderAge.size


            // Customer Segment On Visit behavior 6
            var totalVisitSum = shopLLastVisitDate.map { it.totalVisitCount.toInt() }.sum()
            var avgVisitSum = totalVisitSum.toDouble() / shopLLastVisitDate.size

            var shopCusFinalL = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopForChurn() as ArrayList<ChurnShopL>
            for(k in 0..shopCusFinalL.size-1){

                //tag1
                try{
                    var ordAge = AppDatabase.getDBInstance()!!.orderDetailsListDao().getDateAgeFromLastOrd(shopCusFinalL.get(k).shop_id)
                    if(ordAge!=null){
                        if(ordAge.toInt() > avgPurchaseAge.toInt()){
                            shopCusFinalL.get(k).tag1 = false
                            shopCusFinalL.get(k).lastPurchaseAge = ordAge.toString()
                        }else{
                            shopCusFinalL.get(k).tag1 = true
                            shopCusFinalL.get(k).lastPurchaseAge = ordAge.toString()
                        }
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                //tag2
                try{
                    //var lastVisitD = shopCusFinalL.get(k).lastVisitedDate
                    var lastVisitAge = AppUtils.getDateDiff(AppUtils.changeDateFormat2(shopCusFinalL.get(k).lastVisitedDate).replace("/","-"),AppUtils.getCurrentDateyymmdd())
                    if(lastVisitAge.toInt() > avgVisitAge.toInt()){
                        shopCusFinalL.get(k).tag2 = false
                        shopCusFinalL.get(k).lastVisitAge = lastVisitAge.toString()
                    }else{
                        shopCusFinalL.get(k).tag2 = true
                        shopCusFinalL.get(k).lastVisitAge = lastVisitAge.toString()
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                //tag3
                try {
                    var ordSumInLast90D = AppDatabase.getDBInstance()!!.orderDetailsListDao().getDateAgeFromLastOrd(shopCusFinalL.get(k).shop_id,AppUtils.getPrevXMonthDate(3),AppUtils.getCurrentDateyymmdd()).toBigDecimal()
                    if(ordSumInLast90D!=null){
                        if(ordSumInLast90D >avgOrdAmtInLast90Days.toBigDecimal()){
                            shopCusFinalL.get(k).tag3 = false
                            shopCusFinalL.get(k).avgShopOrdAmt = ordSumInLast90D.toString()
                        }else{
                            shopCusFinalL.get(k).tag2 = true
                            shopCusFinalL.get(k).avgShopOrdAmt = ordSumInLast90D.toString()
                        }
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                //tag4
                try{
                    var ordAge = AppDatabase.getDBInstance()!!.orderDetailsListDao().getDateAgeFromLastOrd(shopCusFinalL.get(k).shop_id)
                    if(ordAge.toDouble() > avgPurchaseAgeForFirstOrd.toDouble()){
                        shopCusFinalL.get(k).tag4 = false
                        shopCusFinalL.get(k).avgTimeSinceFirstOrd = ordAge.toString()
                    }else{
                        shopCusFinalL.get(k).tag4 = true
                        shopCusFinalL.get(k).avgTimeSinceFirstOrd = ordAge.toString()
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                //tag5
                try{
                    var shopOrdCntIn90D =AppDatabase.getDBInstance()!!.orderDetailsListDao().getOrdCntByDateRange(shopCusFinalL.get(k).shop_id,AppUtils.getPrevXMonthDate(3),AppUtils.getCurrentDateyymmdd())
                    if(shopOrdCntIn90D.toInt() > orderCntAvg.toInt()){
                        shopCusFinalL.get(k).tag5 = false
                        shopCusFinalL.get(k).orderBehav = "High"
                    }else{
                        shopCusFinalL.get(k).tag5 = true
                        shopCusFinalL.get(k).orderBehav = "Low"
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                //tag6
                try{
                    var lastVisitDate = AppUtils.changeDateFormat2(shopCusFinalL.get(k).lastVisitedDate).replace("/","-")
                    var lastVisitAge = AppUtils.getDateDiff(lastVisitDate,AppUtils.getCurrentDateyymmdd()).toInt()
                    if(lastVisitAge.toInt() > avgVisitSum.toDouble()){
                        shopCusFinalL.get(k).tag6 = false
                        shopCusFinalL.get(k).shopVisitAvg = lastVisitAge.toString()
                    }else{
                        shopCusFinalL.get(k).tag6 = true
                        shopCusFinalL.get(k).shopVisitAvg = lastVisitAge.toString()
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
            }

            var finalS :ArrayList<ChurnShopL> = ArrayList()
            for(i in 0..shopCusFinalL.size-1){
                var obj = shopCusFinalL.get(i)

                try{
                    var typeN = AppDatabase.getDBInstance()!!.shopTypeDao().getShopTypeNameById(obj.shop_id)
                    obj.shopType = typeN
                }catch (ex:Exception){
                    ex.printStackTrace()
                    obj.shopType = "N/A"
                }

                if(!obj.tag1 && !obj.tag2 && !obj.tag3 && !obj.tag4 && !obj.tag5 && !obj.tag6){
                    finalS.add(obj)
                }else if(obj.tag1 && !obj.tag2 && !obj.tag3 && !obj.tag4 && !obj.tag5 && !obj.tag6){
                    finalS.add(obj)
                }else if(!obj.tag1 && obj.tag2 && !obj.tag3 && !obj.tag4 && !obj.tag5 && !obj.tag6){
                    finalS.add(obj)
                }else if(!obj.tag1 && !obj.tag2 && obj.tag3 && !obj.tag4 && !obj.tag5 && !obj.tag6){
                    finalS.add(obj)
                }else if(!obj.tag1 && !obj.tag2 && !obj.tag3 && obj.tag4 && !obj.tag5 && !obj.tag6){
                    finalS.add(obj)
                }else if(!obj.tag1 && !obj.tag2 && !obj.tag3 && !obj.tag4 && obj.tag5 && !obj.tag6){
                    finalS.add(obj)
                }else if(!obj.tag1 && !obj.tag2 && !obj.tag3 && !obj.tag4 && !obj.tag5 && obj.tag6){
                    finalS.add(obj)
                }

                val booleanList = listOf(obj.tag1, obj.tag2, obj.tag3, obj.tag4, obj.tag5, obj.tag6)
                val falseCount = booleanList.count { it == false }
                if(falseCount>=2){
                    finalS.add(obj)
                }
            }
            uiThread {
                progress.stopSpinning()

                var tempL = finalS.clone() as  ArrayList<ChurnShopL>
                var tempL1 = tempL.distinctBy { it.shop_id }
                finalS = tempL1 as  ArrayList<ChurnShopL>

                if(filter.length==0){
                    rvShopL.adapter = AdapterChurnShopL(mContext,finalS)
                }else{
                    var searchfinal :ArrayList<ChurnShopL> = ArrayList()
                    searchfinal= finalS.filter { it.shop_name.contains(filter,ignoreCase = true) || it.owner_contact_number.contains(filter) } as ArrayList<ChurnShopL>
                    rvShopL.adapter = AdapterChurnShopL(mContext,searchfinal)

                }
            }
        }

    }


}