package com.breezenationalplasticfsm.features.marketAssist

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.app.domain.ShopActivityEntity
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.AppUtils.Companion.bitmapDescriptorFromVector
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardFragment
import com.breezenationalplasticfsm.features.location.LocationWizard
import com.breezenationalplasticfsm.features.location.SingleShotLocationProvider
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.pnikosis.materialishprogress.ProgressWheel
import kotlinx.android.synthetic.main.activity_login_new.iv_background_color_set
import kotlinx.android.synthetic.main.activity_login_new.iv_loader_spin
import kotlinx.android.synthetic.main.row_shop_list_ma.view.iv_row_shop_list_ma_pointer
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.Collections
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.math.max
import kotlin.math.min


class ShopDtlsMarketAssistFrag : BaseFragment(), View.OnClickListener, OnMapReadyCallback {

    private lateinit var mContext: Context

    private lateinit var shopNameTV: TextView
    private lateinit var avgOrdValueTV: TextView

    private lateinit var prodNameMaxVal: TextView
    private lateinit var prodNameMaxAmt: TextView
    private lateinit var prodNameMinVal: TextView
    private lateinit var prodNameMinAmt: TextView

    private lateinit var prodNameMaxQty: TextView
    private lateinit var prodNameMaxQtyAmt: TextView
    private lateinit var prodNameMinQty: TextView
    private lateinit var prodNameMinQtyAmt: TextView

    private lateinit var lastVisitDateTV: TextView
    private lateinit var lastVisitTimeTV: TextView
    private lateinit var avgVisitTimeTV: TextView
    private lateinit var avgVisitTimeTVCaption: TextView
    private lateinit var visitBetweenTV: TextView

    private lateinit var distanceTV: TextView
    private lateinit var mapDirectionCV: CardView
    private lateinit var mapDirectionPointer: ImageView

    private lateinit var rvProductSuggest: RecyclerView
    private lateinit var rvProductCross: RecyclerView

    private lateinit var stayAtLeastTV: TextView
    private lateinit var visitOrdValTV: TextView
    private lateinit var visitOrdQtyTV: TextView

    private lateinit var visitDayTV: TextView
    private lateinit var avoidDayTV: TextView

    private lateinit var cvSuggestProdRoot:CardView
    private lateinit var cvCrossProdRoot:CardView

    var mapFragment: SupportMapFragment? = null
    private var mapCustomer: GoogleMap? = null

    var marksAddr: ArrayList<LatLng?> = ArrayList()
    var currentLat: String = ""
    var currentLong: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        var shopObj: ShopDtls = ShopDtls()
        fun getInstance(objects: Any): ShopDtlsMarketAssistFrag {
            val frag = ShopDtlsMarketAssistFrag()
            shopObj = objects as ShopDtls
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_shop_dtls_market_assist, container, false)
        initView(view)
        return view
    }


    private lateinit var iv_loader_spin_ma_back:ImageView
    private lateinit var iv_loader_spin_ma_loader:ImageView
    private fun initView(view: View) {
        iv_loader_spin_ma_back = view.findViewById(R.id.iv_loader_spin_ma_back)
        iv_loader_spin_ma_loader = view.findViewById(R.id.iv_loader_spin_ma_loader)

        shopNameTV = view.findViewById(R.id.tv_frag_shop_dtls_shop_name)
        avgOrdValueTV = view.findViewById(R.id.tv_frag_shop_dtls_avg_ord_amt)
        rvProductSuggest = view.findViewById(R.id.rv_ma_suggest_product)
        rvProductCross = view.findViewById(R.id.rv_ma_cross_product)

        prodNameMaxVal = view.findViewById(R.id.tv_product_ord_by_val_max_name)
        prodNameMaxAmt = view.findViewById(R.id.tv_product_ord_by_val_max_val)
        prodNameMinVal = view.findViewById(R.id.tv_product_ord_by_val_min_name)
        prodNameMinAmt = view.findViewById(R.id.tv_product_ord_by_val_min_val)

        prodNameMaxQty = view.findViewById(R.id.tv_product_ord_by_qty_max_name)
        prodNameMaxQtyAmt = view.findViewById(R.id.tv_product_ord_by_qty_max_amt)
        prodNameMinQty = view.findViewById(R.id.tv_product_ord_by_qty_min_name)
        prodNameMinQtyAmt = view.findViewById(R.id.tv_product_ord_by_qty_min_amt)

        lastVisitDateTV = view.findViewById(R.id.tv_frag_shop_dtls_ma_last_visit_date)
        lastVisitTimeTV = view.findViewById(R.id.tv_frag_shop_dtls_ma_last_visit_time)
        avgVisitTimeTV = view.findViewById(R.id.tv_frag_shop_dtls_ma_avg_visit_time)
        avgVisitTimeTVCaption = view.findViewById(R.id.tv_frag_shop_dtls_ma_avg_visit_time_caption)

        distanceTV = view.findViewById(R.id.tv_frag_shop_dtls_ma_dist)
        mapDirectionCV = view.findViewById(R.id.iv_frag_shop_dtls_ma_map_direction)
        mapDirectionPointer = view.findViewById(R.id.iv_frag_shop_dtls_ma_map_direction_pointer)

        stayAtLeastTV = view.findViewById(R.id.tv_frag_shop_dtls_ma_stay_at_least)
        visitOrdValTV = view.findViewById(R.id.tv_frag_shop_dtls_ma_visit_ord_val)
        visitOrdQtyTV = view.findViewById(R.id.tv_frag_shop_dtls_ma_visit_ord_qty)

        visitDayTV = view.findViewById(R.id.tv_frag_shop_dtls_ma_visit_day)
        avoidDayTV = view.findViewById(R.id.tv_frag_shop_dtls_ma_avoid_day)
        visitBetweenTV = view.findViewById(R.id.tv_frag_shop_dtls_ma_visit_between)

        cvSuggestProdRoot = view.findViewById(R.id.cv_frag_shop_dtls_ma_suggestive_prod_root)
        cvCrossProdRoot = view.findViewById(R.id.cv_frag_shop_dtls_ma_cross_prod_root)

        mapDirectionCV.setOnClickListener(this)

        Glide.with(mContext)
            .load(R.drawable.icon_pointer_gif)
            .into(mapDirectionPointer)

        mapFragment = childFragmentManager.findFragmentById(R.id.map_frag_shop_dtls_ma) as SupportMapFragment

        loadProgress()
        Handler().postDelayed(Runnable {
            getCurrentLoc()
        }, 2000)

    }

    fun getCurrentLoc() {
        SingleShotLocationProvider.requestSingleUpdate(mContext,
            object : SingleShotLocationProvider.LocationCallback {
                override fun onStatusChanged(status: String) {
                }

                override fun onProviderEnabled(status: String) {
                }

                override fun onProviderDisabled(status: String) {
                }

                override fun onNewLocationAvailable(location: Location) {
                    currentLat = location.latitude.toString()
                    currentLong = location.longitude.toString()
                    setData()
                }
            })
    }


    private fun setData() {
        try {
            shopNameTV.text = shopObj.shop_name
            avgOrdValueTV.text = String.format("%.2f", AppDatabase.getDBInstance()!!.orderDetailsListDao().getAvgOrderValue(shopObj.shop_id).toDouble())
            doAsync {
                var allProeuctL: ArrayList<SuggestiveProduct> = AppDatabase.getDBInstance()!!.orderDetailsListDao().getSuggestProductAll(shopObj.shop_id!!) as ArrayList<SuggestiveProduct>
                var productValueQtySumL: ArrayList<PurchasedProductTotal> = ArrayList()
                if(allProeuctL.size>0){
                    var distinctProductID = allProeuctL.distinctBy { it.product_id }.map { it.product_id } as ArrayList<String>
                    for (i in 0..distinctProductID.size - 1) {
                        var qtyTotal =
                            allProeuctL.filter { it.product_id.equals(distinctProductID.get(i)) }
                                .map { it.qty.toDouble() }.sum()
                        var amtTotal =
                            allProeuctL.filter { it.product_id.equals(distinctProductID.get(i)) }
                                .map { it.total_price.toDouble() }.sum()

                        var obj: PurchasedProductTotal = PurchasedProductTotal()
                        obj.product_id = distinctProductID.get(i)
                        obj.product_name =
                            allProeuctL.filter { it.product_id.equals(distinctProductID.get(i)) }
                                .first().product_name
                        obj.totalValue = amtTotal.toString()
                        obj.totalQty = qtyTotal.toString()
                        productValueQtySumL.add(obj)
                    }
                }
                uiThread {
                    if(allProeuctL.size>0){
                        var minValueObj = productValueQtySumL.minByOrNull { it.totalValue.toDouble() }
                        var maxValueObj = productValueQtySumL.maxByOrNull { it.totalValue.toDouble() }
                        var minQtyObj = productValueQtySumL.minByOrNull { it.totalQty.toDouble() }
                        var maxQtyObj = productValueQtySumL.maxByOrNull { it.totalQty.toDouble() }

                        prodNameMaxVal.text = maxValueObj!!.product_name
                        prodNameMaxAmt.text = resources.getString(R.string.rupee_symbol_with_space) + " " + String.format("%.02f",maxValueObj!!.totalValue.toDouble())
                        prodNameMinVal.text = minValueObj!!.product_name
                        prodNameMinAmt.text = resources.getString(R.string.rupee_symbol_with_space) + " " + String.format("%.02f",minValueObj!!.totalValue.toDouble())

                        //prodNameMaxVal.isSelected = true
                        //prodNameMaxAmt.isSelected = true
                        //prodNameMinVal.isSelected = true
                        //prodNameMinAmt.isSelected = true

                        prodNameMaxQty.text = maxQtyObj!!.product_name
                        prodNameMaxQtyAmt.text = String.format("%.02f",maxQtyObj!!.totalQty.toDouble())
                        prodNameMinQty.text = minQtyObj!!.product_name
                        prodNameMinQtyAmt.text = String.format("%.02f",minQtyObj!!.totalQty.toDouble())

                        //prodNameMaxQty.isSelected = true
                        //prodNameMaxQtyAmt.isSelected = true
                        //prodNameMinQty.isSelected = true
                        //prodNameMinQtyAmt.isSelected = true
                    }else{
                        prodNameMaxVal.text = "N/A"
                        prodNameMaxAmt.text = "N/A"
                        prodNameMinVal.text = "N/A"
                        prodNameMinAmt.text = "N/A"

                        prodNameMaxQty.text = "N/A"
                        prodNameMaxQtyAmt.text = "N/A"
                        prodNameMinQty.text = "N/A"
                        prodNameMinQtyAmt.text = "N/A"
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            hideProgress()
        }

        try {
            var lastVisitRecord = AppDatabase.getDBInstance()!!.shopActivityDao().getLastRow(shopObj.shop_id)
            if(lastVisitRecord == null){
                val shopDtlObj : AddShopDBModelEntity = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopObj.shop_id)
                var lastVisitDate = AppUtils.getFormatedDateNew(AppUtils.changeDateFormat1(shopDtlObj.lastVisitedDate)!!.replace("/", "-"), "dd-mm-yyyy", "yyyy-mm-dd")!!
                lastVisitDateTV.text = "Date: " + AppUtils.getFormatedDateNew(lastVisitDate,"yyyy-mm-dd","dd-mm-yyyy")!!.replace("-","/") + " " + AppUtils.getDayName(lastVisitDate!!)
            }else{
                var lastVisitDate = lastVisitRecord.visited_date!!.replace("T", " ").split(" ").get(0)
                lastVisitDateTV.text = "Date: " + AppUtils.getFormatedDateNew(lastVisitDate, "yyyy-mm-dd", "dd-mm-yyyy")!!.replace("-", "/") + " " + AppUtils.getDayName(lastVisitDate)
                //lastVisitDateTV.isSelected = true
            }
            try{
                lastVisitTimeTV.text = "Time: " + AppUtils.getMeredianTimeFromISODateTime(lastVisitRecord.visited_date!!)
            }catch (ex:Exception){
                lastVisitTimeTV.text = "Time: " + "N/A"
            }

            var shopActivityL30Days: ArrayList<ShopActivityEntity> = AppDatabase.getDBInstance()!!.shopActivityDao().getShopActivity(shopObj.shop_id) as ArrayList<ShopActivityEntity>
            var durationL: ArrayList<String> = shopActivityL30Days.map { it.duration_spent } as ArrayList<String>

            if(durationL.size==0){
                avgVisitTimeTV.text = "N/A"
                avgVisitTimeTVCaption.text = ""
            }else{
                var hrTotal = 0
                var minTotal = 0
                var secTotal = 0
                for (i in 0..durationL.size - 1) {
                    var obj = durationL.get(i)
                    hrTotal = hrTotal + obj.split(":").get(0).toInt()
                    minTotal = minTotal + obj.split(":").get(1).toInt()
                    secTotal = secTotal + obj.split(":").get(2).toInt()
                }
                if (secTotal > 60) {
                    minTotal = minTotal + secTotal / 60
                    secTotal = secTotal % 60
                }
                if (minTotal > 60) {
                    hrTotal = hrTotal + minTotal / 60
                    minTotal = minTotal % 60
                }

                var totalAvgTimeInSec = ((hrTotal * 60 * 60) + (minTotal * 60) + secTotal) / durationL.size
                var avgSec = totalAvgTimeInSec % 60
                var avgMin = totalAvgTimeInSec / 60
                var avgHr = avgMin / 60
                avgMin = avgMin % 60

                var avgDuration: String = ""
                if (avgHr == 0) {
                    avgDuration =
                        avgMin.toInt().toString() + "mins " + avgSec.toInt().toString() + "secs"
                }
                else {
                    avgDuration = avgHr.toInt().toString() + "hrs " + avgMin.toInt()
                        .toString() + "mins " + avgSec.toInt().toString() + "secs"
                }
                avgVisitTimeTV.text = avgDuration
                avgVisitTimeTVCaption.text = "Per Visit"
            }




        } catch (ex: Exception) {
            ex.printStackTrace()
            hideProgress()
        }

        marksAddr = ArrayList()
        marksAddr.add(LatLng(currentLat.toDouble(), currentLong.toDouble()))
        marksAddr.add(LatLng(shopObj.shopLat.toDouble(), shopObj.shopLong.toDouble()))
        var dist = LocationWizard.getDistance(currentLat.toDouble(), currentLong.toDouble(), shopObj.shopLat.toDouble(), shopObj.shopLong.toDouble())
        var kmDist = dist.toInt()
        var mDist = Math.round((dist - kmDist) * 1000).toInt()
        if(kmDist == 0){
            distanceTV.text = "Distance : $mDist mtr"
        }else{
            distanceTV.text = "Distance : $kmDist km $mDist mtr"
        }
        showMap()

        try {
            doAsync {
                var suggestProductL: ArrayList<SuggestiveProduct> = AppDatabase.getDBInstance()!!.orderDetailsListDao().getSuggestProduct(shopObj.shop_id!!) as ArrayList<SuggestiveProduct>
                var finalSuggestProductList: ArrayList<SuggestiveProductFinal> = ArrayList()
                var productOccuranceL: ArrayList<ProductOccur> = ArrayList()
                if(suggestProductL.size>0){
                    var suggestProductLDistinctId = suggestProductL.distinctBy { it.product_id }
                    for (i in 0..suggestProductLDistinctId.size - 1) {
                        var proID = suggestProductLDistinctId.get(i).product_id
                        productOccuranceL.add(ProductOccur(proID, suggestProductL.count { it.product_id == proID }))
                    }
                    var sortedL = ArrayList(productOccuranceL).sortedWith(compareBy { it.occur }).reversed()
                    productOccuranceL.clear()
                    productOccuranceL.addAll(sortedL)

                    for (i in 0..productOccuranceL.size - 1) {
                        var objL = suggestProductL.filter { it.product_id.equals(productOccuranceL.get(i).product_id) } as ArrayList<SuggestiveProduct>
                        var totalOrdValue = 0.0
                        var totalOrdQty = 0.0
                        for (j in 0..objL.size - 1) {
                            totalOrdValue = totalOrdValue + objL.get(j).total_price.toDouble()
                            totalOrdQty = totalOrdQty + objL.get(j).qty.toDouble()
                        }
                        finalSuggestProductList.add(SuggestiveProductFinal(
                            objL.get(0).product_id,
                            objL.get(0).product_name,
                            String.format("%.02f", (totalOrdValue / totalOrdQty).toDouble()),
                            String.format("%.02f", (totalOrdQty / objL.size).toDouble())
                        )
                        )
                        if (i == 4) {
                            break
                        }
                    }
                }
                uiThread {
                    if(suggestProductL.size>0){
                        rvProductSuggest.adapter = AdapterSuggestiveProduct(mContext,finalSuggestProductList )
                        cvSuggestProdRoot.visibility = View.VISIBLE
                    }else{
                        cvSuggestProdRoot.visibility = View.GONE
                    }

                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            hideProgress()
        }

        //code for cross sell
        try {
            doAsync {
                var suggestProductL: ArrayList<SuggestiveProduct> = AppDatabase.getDBInstance()!!.orderDetailsListDao().getSuggestProduct(shopObj.shop_id!!) as ArrayList<SuggestiveProduct>
                var finalSuggestProductList: ArrayList<SuggestiveProductFinal> = ArrayList()
                if(suggestProductL.size>0) {
                    var suggestProductLDistinctId = suggestProductL.distinctBy { it.product_id }
                    var productQtyL: ArrayList<ProductOccur> = ArrayList()
                    var productOccuranceL: ArrayList<ProductOccur> = ArrayList()
                    for (i in 0..suggestProductLDistinctId.size - 1) {
                        var proID = suggestProductLDistinctId.get(i).product_id
                        var totalQty = suggestProductL.filter { it.product_id.equals(proID) }.map { it.qty.toDouble() }.sum()
                        productOccuranceL.add(ProductOccur(proID, suggestProductL.count { it.product_id == proID }, totalQty.toString()))
                    }
                    var sortedL = ArrayList(productOccuranceL).sortedWith(compareBy { it.totalQty.toDouble() }).reversed().reversed()
                    productOccuranceL.clear()
                    productOccuranceL.addAll(sortedL)

                    for (i in 0..productOccuranceL.size - 1) {
                        println("tag_ma $i")
                        var objL = suggestProductL.filter { it.product_id.equals(productOccuranceL.get(i).product_id) } as ArrayList<SuggestiveProduct>
                        var totalOrdValue = 0.0
                        var totalOrdQty = 0.0
                        for (j in 0..objL.size - 1) {
                            totalOrdValue = totalOrdValue + objL.get(j).total_price.toDouble()
                            totalOrdQty = totalOrdQty + objL.get(j).qty.toDouble()
                        }
                        finalSuggestProductList.add(
                            SuggestiveProductFinal(
                                objL.get(0).product_id,
                                objL.get(0).product_name,
                                String.format("%.02f", (totalOrdValue / totalOrdQty).toDouble()),
                                String.format("%.02f", (totalOrdQty / objL.size).toDouble())
                            )
                        )
                        if (i == 4) {
                            break
                        }
                    }
                }
                uiThread {
                    if(suggestProductL.size>0) {
                        rvProductCross.adapter = AdapterCrossSellProduct(mContext, finalSuggestProductList)
                        cvCrossProdRoot.visibility = View.VISIBLE
                    }else{
                        cvCrossProdRoot.visibility = View.GONE
                    }

                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            hideProgress()
        }

        //code for productive visit
        try{
            stayAtLeastTV.text = avgVisitTimeTV.text.toString()
            var last30DayOrdAmtL: ArrayList<String> = AppDatabase.getDBInstance()!!.orderDetailsListDao().getLast30DaysOrderAmt(shopObj.shop_id!!) as ArrayList<String>
            var totalSumAmt = last30DayOrdAmtL.sumOf { it.toDouble() }
            visitOrdValTV.text = if(String.format("%.02f",(totalSumAmt/last30DayOrdAmtL.size).toDouble()).equals("NaN")) "N/A" else String.format("%.02f",(totalSumAmt/last30DayOrdAmtL.size).toDouble())+" or above."

            var last30DayOrdL: ArrayList<SuggestiveProduct> = AppDatabase.getDBInstance()!!.orderDetailsListDao().getSuggestProduct30Days(shopObj.shop_id!!) as ArrayList<SuggestiveProduct>
            var last30DayOrdQtySum = last30DayOrdL.map { it.qty.toDouble() }.sum()
            visitOrdQtyTV.text = if(String.format("%.02f",(last30DayOrdQtySum/last30DayOrdAmtL.size).toDouble()).equals("NaN")) "N/A" else String.format("%.02f",(last30DayOrdQtySum/last30DayOrdAmtL.size).toDouble())+" or above."

            var last30DayOrdDtls: ArrayList<OrderDtlsLast30Days> = AppDatabase.getDBInstance()!!.orderDetailsListDao().getLast30DaysOrderDtls(shopObj.shop_id!!) as ArrayList<OrderDtlsLast30Days>
            var uniqDateL :ArrayList<String> =  last30DayOrdDtls.distinctBy { it.order_id }.map { it.date } as ArrayList<String>
            var dateNameL : ArrayList<String> = ArrayList()
            for(i in 0..uniqDateL.size-1){
                var obj = AppUtils.getDayName(uniqDateL.get(i).replace("T"," ").split(" ").get(0))
                dateNameL.add(obj)
            }


            data class DT(var day:String="",var cnt:Int=0)
            var fL :ArrayList<DT> = ArrayList()
            for(item in dateNameL.distinct()){
                fL.add(DT(item!!,Collections.frequency(dateNameL,item)))
            }
            var maxCnt = 0
            var minCnt = 0
            try{
                 maxCnt = fL.maxByOrNull{it.cnt}!!.cnt
                 minCnt = fL.minByOrNull { it.cnt }!!.cnt
            }catch (ex:Exception){
                maxCnt = 0
                minCnt = 0
            }

            var maxDates=""
            var minDates=""
            if(maxCnt == minCnt){
                var maxDL = fL.filter { it.cnt == maxCnt }.map { it.day } as ArrayList<String>
                for(j in 0..maxDL.size-1){
                    maxDates = maxDates+maxDL.get(j).toString()+", "
                }
                minDates="N/A"
                maxDates = maxDates.dropLast(2)
            }else{
                var maxDL = fL.filter { it.cnt == maxCnt }.map { it.day } as ArrayList<String>
                var minDL = fL.filter { it.cnt == minCnt }.map { it.day } as ArrayList<String>
                for(j in 0..maxDL.size-1){
                    maxDates = maxDates+maxDL.get(j).toString()+", "
                }
                for(j in 0..minDL.size-1){
                    minDates = minDates+minDL.get(j).toString()+", "
                }
                maxDates = maxDates.dropLast(2)
                minDates = minDates.dropLast(2)
            }

            //var visitDay = dateNameL.groupingBy { it }.eachCount().maxByOrNull{ it.value }.toString().split("=").get(0).toString()
            //var avoidDay = dateNameL.groupingBy { it }.eachCount().minByOrNull{ it.value }.toString().split("=").get(0).toString()
            //visitDayTV.text = if(visitDay.equals("null")) "N/A" else visitDay
            visitDayTV.text = if(maxDates.equals("")) "N/A" else maxDates
            //var avoidD = if(avoidDay.equals("null")) "N/A" else avoidDay
            //avoidDayTV.text = if(avoidD.equals(visitDayTV.text.toString())) "N/A" else avoidD
            avoidDayTV.text = if(minDates.equals("")) "N/A" else minDates

            var shopActivityL30DaysOrdWise: ArrayList<ShopActivityEntity> = AppDatabase.getDBInstance()!!.shopActivityDao().getShopActivityOrderWise(shopObj.shop_id) as ArrayList<ShopActivityEntity>
            var visitTimeL:ArrayList<String> = shopActivityL30DaysOrdWise.map { it.visited_date!!.replace("T"," ").split(" ").get(1).toString() } as ArrayList<String>

            if(visitTimeL.size==0){
                visitBetweenTV.text = "N/A"
            }else{
                var avgTime = ""
                for(j in 0..visitTimeL.size-1){
                    avgTime = avgTime+visitTimeL.get(j)+" "
                }
                avgTime = AppUtils.avgTime(avgTime.dropLast(1)).toString()
                var avgHr = AppUtils.avgTime(avgTime.dropLast(1)).toString().split(":").get(0).toInt()
                var avgMin = AppUtils.avgTime(avgTime.dropLast(1)).toString().split(":").get(1).toInt()

                var pref45Min = AppUtils.addORsubMinInTime(-45,"$avgHr:$avgMin")
                var later45Min = AppUtils.addORsubMinInTime(45,"$avgHr:$avgMin")

                visitBetweenTV.text = "${AppUtils.getCurrentTimeWithMeredian(pref45Min)} to ${AppUtils.getCurrentTimeWithMeredian(later45Min)}"
            }
            hideProgress()
        }catch (ex:Exception){
            ex.printStackTrace()
            hideProgress()
        }
    }

    private fun showMap() {
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapCustomer = googleMap
        for (i in 0..marksAddr.size - 1) {
            if (i == 0) {
                /*val mark = marksAddr[i]
                mapCustomer!!.addMarker(
                    MarkerOptions()
                    .position(mark!!)
                    .icon(bitmapDescriptorFromVector(mContext, R.drawable.scooter_man))
                    .title("Your Location"))
                mapCustomer!!.moveCamera(CameraUpdateFactory.newLatLng(mark))
                mapCustomer!!.animateCamera(CameraUpdateFactory.zoomTo(14f))*/
            } else if (i == marksAddr.size - 1) {
                val mark = marksAddr[i]
                mapCustomer!!.addMarker(
                    MarkerOptions()
                        .position(mark!!)
                        .icon(bitmapDescriptorFromVector(mContext, R.drawable.shop_pointer1))
                        .title("Shop Location")
                )
                mapCustomer!!.moveCamera(CameraUpdateFactory.newLatLng(mark))
                mapCustomer!!.animateCamera(CameraUpdateFactory.zoomTo(14f))
            }
        }

        val mark = marksAddr[0]
        mapCustomer!!.addMarker(
            MarkerOptions()
                .position(mark!!)
                .icon(bitmapDescriptorFromVector(mContext, R.drawable.scooter_man))
                .title("Your Location")
        )
        mapCustomer!!.moveCamera(CameraUpdateFactory.newLatLngZoom(marksAddr[0]!!, 15f))

        val opts = PolylineOptions().addAll(marksAddr).color(Color.GREEN).width(8f)
        mapCustomer!!.addPolyline(opts)
        mapCustomer!!.uiSettings.isZoomControlsEnabled = true
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.iv_frag_shop_dtls_ma_map_direction -> {
                try{
                    var intentGmap: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${shopObj.shopLat},${shopObj.shopLong}&mode=1"))
                    intentGmap.setPackage("com.google.android.apps.maps")
                    if(intentGmap.resolveActivity(mContext!!.packageManager) !=null){
                        mContext!!.startActivity(intentGmap)
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
            }
        }
    }


    private fun loadProgress(){
        iv_loader_spin_ma_back.visibility = View.VISIBLE
        iv_loader_spin_ma_loader.visibility = View.VISIBLE
        Glide.with(this)
            .load(R.drawable.loadernew_2)
            .into(iv_loader_spin_ma_loader)
    }
    private fun hideProgress(){
        iv_loader_spin_ma_back.visibility = View.GONE
        iv_loader_spin_ma_loader.visibility = View.GONE
    }
}