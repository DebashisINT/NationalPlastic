package com.breezefieldnationalplastic.features.viewAllOrder.orderOptimized

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.*
import com.breezefieldnationalplastic.app.domain.ProductListEntity
import com.breezefieldnationalplastic.app.domain.ProductOnlineRateTempEntity
import com.breezefieldnationalplastic.app.domain.ProductRateEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.ToasterMiddle
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.addshop.model.StockAllResponse
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.login.api.opportunity.OpportunityRepoProvider
import com.breezefieldnationalplastic.features.login.api.productlistapi.ProductListRepoProvider
import com.breezefieldnationalplastic.features.login.model.productlistmodel.ProductRateDataModel
import com.breezefieldnationalplastic.features.login.model.productlistmodel.ProductRateListResponseModel
import com.breezefieldnationalplastic.features.viewAllOrder.orderNew.NewOrderScrActiFragment
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.github.clans.fab.FloatingActionButton
import com.itextpdf.text.pdf.parser.Line
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_order_type_list_new.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

// 4.0 OrderProductListFrag AppV 4.0.8 Suman    12/04/2023 Adapter list generation update 0025876
// 2.0 OrderProductListFrag  v 4.1.6 stock optmization mantis 0026391 20-06-2023 saheli
// 3.0 OrderProductListFrag  v 4.1.6 Saheli mantis 0026430 Play console report fixing 23-06-2023
//3.0  v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product
class OrderProductListFrag : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var ll_grSel: LinearLayout
    private lateinit var ll_catagorySel: LinearLayout
    private lateinit var ll_measureSel: LinearLayout
    private lateinit var tv_grSel: TextView
    private lateinit var tv_catagorySel: TextView
    private lateinit var tv_measureSel: TextView
    private lateinit var tv_productCount: TextView
    private lateinit var tv_productAmt: TextView
    private lateinit var ll_cart: LinearLayout

    private lateinit var etSearch: AppCustomEditText
    private lateinit var ivSearch: ImageView
    private lateinit var ivFilter: ImageView
    private lateinit var llFilterRoot: LinearLayout

    private lateinit var ivMic: ImageView

    private var selGrIDStr = ""
    private var selCategoryIDStr = ""
    private var selMeasureIDStr = ""

    private lateinit var productAdapter: AdapterOrdProductOptimized
    private lateinit var rv_product: RecyclerView
    private lateinit var progrwss_wheel: ProgressWheel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDestroy() {
        super.onDestroy()
        // start 3.0 OrderProductListFrag  v 4.1.6 Saheli mantis 0026430 Play console report fixing 23-06-2023
        try{
            progrwss_wheel.stopSpinning()
        }catch(ex:Exception){
            ex.printStackTrace()
        }
        // end 3.0 OrderProductListFrag  v 4.1.6 Saheli mantis 0026430 Play console report fixing 23-06-2023
    }

    companion object {
        var finalOrderDataList: ArrayList<FinalOrderData> = ArrayList()
        var shop_id: String = ""
        fun getInstance(objects: Any): OrderProductListFrag {
            val Fragment = OrderProductListFrag()
            shop_id = objects.toString()
            return Fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.order_product_list_frag, container, false)
        initView(view)
        return view
    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun initView(view: View?) {
        ll_grSel = view!!.findViewById(R.id.ll_frag_ord_pro_list_gr_sel_root)
        ll_catagorySel = view.findViewById(R.id.ll_frag_ord_pro_list_catag_sel_root)
        ll_measureSel = view.findViewById(R.id.ll_frag_ord_pro_list_measure_sel_root)
        tv_grSel = view.findViewById(R.id.tv_frag_ord_pro_list_gr_sel)
        tv_catagorySel = view.findViewById(R.id.tv_frag_ord_pro_list_catag_sel)
        tv_measureSel = view.findViewById(R.id.tv_frag_ord_pro_list_measure_sel)
        tv_productCount = view.findViewById(R.id.tv_ord_prod_list_frag_count)
        tv_productAmt = view.findViewById(R.id.tv_ord_prod_list_frag_amt)
        ll_cart = view.findViewById(R.id.ll_ord_prod_list_frag_cart)
        etSearch = view.findViewById(R.id.et_frag_ord_prod_search)
        ivSearch = view.findViewById(R.id.iv_frag_ord_prod_search)
        ivFilter = view.findViewById(R.id.iv_frag_ord_prod_filter)
        llFilterRoot = view.findViewById(R.id.ll_ord_pro_list_frag_filter_root)
        ivMic = view.findViewById(R.id.iv_frag_ord_prod_mic)

        llFilterRoot.visibility= View.GONE

        rv_product = view.findViewById(R.id.rv_ord_pro_list_frag_productlist)
        progrwss_wheel = view.findViewById(R.id.pw_frag_ord_pro_list)
        progrwss_wheel.stopSpinning()

        ll_grSel.setOnClickListener(this)
        ll_catagorySel.setOnClickListener(this)
        ll_measureSel.setOnClickListener(this)
        ll_cart.setOnClickListener(this)

        ivSearch.setOnClickListener(this)
        ivFilter.setOnClickListener(this)
        ivMic.setOnClickListener(this)

        finalOrderDataList = ArrayList()

        doAsync {
            if(Pref.IsStockCheckFeatureOn && AppUtils.isOnline(mContext)){
                getAllStock()
            }
            uiThread {
                if(Pref.isRateOnline){
                    if(AppUtils.isOnline(mContext)){
                        progrwss_wheel.stopSpinning()
                        getProductRateListByShopID()
                    }else{
                        ToasterMiddle.msgShort(mContext,"Internet not connected.")
                    }
                }else{
                    initProduct()
                }
            }
        }


        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().length == 0){
                    filterAutoSearchData()
                }
                /*if(p0.toString().length>3){
                    filterAutoSearchData()
                }else if(p0.toString().length == 0){
                    filterAutoSearchData()
                }*/
            }
        })
    }

    private fun getAllStock(){
        try {
            val repository = OpportunityRepoProvider.opportunityListRepo()
            BaseActivity.compositeDisposable.add(
                repository.getAllStock(Pref.user_id!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as StockAllResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            var stock_list = response.stock_list
                            if (stock_list != null && stock_list.isNotEmpty()) {
                                AppDatabase.getDBInstance()?.stockAllDao()?.deleteAll()
                                AppDatabase.getDBInstance()?.stockAllDao()?.insertAll(stock_list)
                            }
                        }
                    }, { error ->
                        error.printStackTrace()
                    })
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun filterAutoSearchData(){
        if (etSearch.text.toString().equals("")) {
            if(Pref.isRateOnline){
                loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllFromOnlineV1() as ArrayList<CustomProductRate>)
            }else{
                loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllV1() as ArrayList<CustomProductRate>)
            }
        }else{
            progrwss_wheel.spin()
            //AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
            Handler().postDelayed(Runnable {
                var allProductL : ArrayList<CustomProductRate> = ArrayList()
                if(Pref.isRateOnline){
                    allProductL = AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllFromOnlineV1() as ArrayList<CustomProductRate>
                }else{
                    allProductL = AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllV1() as ArrayList<CustomProductRate>
                }

                var searchObj = etSearch.text.toString()
                loadProduct(allProductL.filter { it.product_name.contains(searchObj, ignoreCase = true) || it.brand.contains(searchObj, ignoreCase = true) } as ArrayList<CustomProductRate>)
            }, 500)
        }
    }

    private fun initProduct(){
        if (Pref.isShowAllProduct) {
            progrwss_wheel.spin()

            doAsync {
                var productL :ArrayList<CustomProductRate> = ArrayList()
                if(Pref.isRateOnline){
                    productL = AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllFromOnlineV1() as ArrayList<CustomProductRate>
                }else{
                    productL = AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllV1() as ArrayList<CustomProductRate>
                }
                uiThread {
                    loadProduct(productL)
                }
            }

            /*Handler().postDelayed(Runnable {
                if(Pref.isRateOnline){
                    loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllFromOnlineV1() as ArrayList<CustomProductRate>)
                }else{
                    loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllV1() as ArrayList<CustomProductRate>)
                }
            }, 500)*/
        }
    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ll_frag_ord_pro_list_gr_sel_root -> {
                var brandList = AppDatabase.getDBInstance()?.productListDao()?.getDistinctBrandList() as ArrayList<CommonProductCatagory>
                if (brandList.size > 0) {
                    ProductCatagoryDialog.newInstance(brandList, "Select Group", {
                        var str =
                            "<font color=#425066>Group : </font> <font color=#013446>${it.name_sel}</font>"
                        tv_grSel.text = Html.fromHtml(str)
                        selGrIDStr = it.id_sel

                        selCategoryIDStr = ""
                        tv_catagorySel.text = "Search by Category"

                        selMeasureIDStr = ""
                        tv_measureSel.text = "Search by Measurement"

                        if(Pref.isRateOnline){
                            loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListByBeandIDFromOnlineRateV1(selGrIDStr) as ArrayList<CustomProductRate>)
                        }else{
                            loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListByBeandIDV1(selGrIDStr) as ArrayList<CustomProductRate>)
                        }

                    }).show((mContext as DashboardActivity).supportFragmentManager, "")
                } else {
                    ToasterMiddle.msgShort(mContext, "No Group Found")
                }
            }
            R.id.ll_frag_ord_pro_list_catag_sel_root -> {
                if (!selGrIDStr.equals("")) {
                    var categoryList = AppDatabase.getDBInstance()?.productListDao()
                        ?.getDistinctCategoryList(selGrIDStr) as ArrayList<CommonProductCatagory>
                    if (categoryList.size > 0) {
                        ProductCatagoryDialog.newInstance(categoryList, "Select Category", {
                            var str = "<font color=#425066>Category : </font> <font color=#013446>${it.name_sel}</font>"
                            tv_catagorySel.text = Html.fromHtml(str)
                            selCategoryIDStr = it.id_sel

                            selMeasureIDStr = ""
                            tv_measureSel.text = "Search by Measurement"

                            if(Pref.isRateOnline){
                                loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListByBeandIDCategoryIDFromOnlineRateV1(selGrIDStr, selCategoryIDStr) as ArrayList<CustomProductRate>)
                            }else{
                                loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListByBeandIDCategoryIDV1(selGrIDStr, selCategoryIDStr) as ArrayList<CustomProductRate>)
                            }
                        }).show((mContext as DashboardActivity).supportFragmentManager, "")
                    } else {
                        ToasterMiddle.msgShort(mContext, "No Category Found")
                    }
                }
            }
            R.id.ll_frag_ord_pro_list_measure_sel_root -> {
                if (!selGrIDStr.equals("")) {
                    if(!selCategoryIDStr.equals("")){
                        var measureList: ArrayList<CommonProductCatagory> = ArrayList()
                        if (selCategoryIDStr.equals("")) {
                            measureList = AppDatabase.getDBInstance()?.productListDao()
                                ?.getDistinctWattList1(selGrIDStr) as ArrayList<CommonProductCatagory>
                        } else {

                            measureList = AppDatabase.getDBInstance()?.productListDao()?.getDistinctWattList(selGrIDStr, selCategoryIDStr) as ArrayList<CommonProductCatagory>
                        }
                        if (measureList.size > 0) {
                            ProductCatagoryDialog.newInstance(measureList, "Select Measurement", {
                                var str = "<font color=#425066>Measurement : </font> <font color=#013446>${it.name_sel}</font>"
                                tv_measureSel.text = Html.fromHtml(str)
                                selMeasureIDStr = it.id_sel

                                if(Pref.isRateOnline){
                                    loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListByBeandIDCategoryIDWattIDFromOnlineRateV1(selGrIDStr, selCategoryIDStr, selMeasureIDStr) as ArrayList<CustomProductRate>)
                                }else {
                                    loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListByBeandIDCategoryIDWattIDV1(selGrIDStr, selCategoryIDStr, selMeasureIDStr) as ArrayList<CustomProductRate>)
                                }
                            }).show((mContext as DashboardActivity).supportFragmentManager, "")
                        } else {
                            ToasterMiddle.msgShort(mContext, "No Measurement Found")
                        }
                    }else{
                        ToasterMiddle.msgShort(mContext, "Please select Measurement first")
                    }
                } else {
                    ToasterMiddle.msgShort(mContext, "Please select Group first")
                }
            }
            R.id.ll_ord_prod_list_frag_cart-> {
                if (finalOrderDataList.size > 0) {
                    var finalOrderDataWithShopID = FinalOrderDataWithShopID(shop_id, finalOrderDataList)
                    (mContext as DashboardActivity).loadFragment(
                        FragType.OrderProductCartFrag, true, finalOrderDataWithShopID)
                }
            }
            R.id.iv_frag_ord_prod_search -> {
                llFilterRoot.visibility = View.GONE
                if (etSearch.text.toString().equals("")) {
                    if(Pref.isRateOnline){
                        loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllFromOnlineV1() as ArrayList<CustomProductRate>)
                    }else{
                        loadProduct(AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllV1() as ArrayList<CustomProductRate>)
                    }
                }else{
                    progrwss_wheel.spin()
                    AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    Handler().postDelayed(Runnable {
                        var allProductL : ArrayList<CustomProductRate> = ArrayList()
                        if(Pref.isRateOnline){
                            allProductL = AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllFromOnlineV1() as ArrayList<CustomProductRate>
                        }else{
                            allProductL = AppDatabase.getDBInstance()?.productListDao()?.getCustomizeProductListAllV1() as ArrayList<CustomProductRate>
                        }

                        var searchObj = etSearch.text.toString().replace("  "," ")
                        loadProduct(allProductL.filter { it.product_name.replace("  "," ").contains(searchObj, ignoreCase = true) || it.brand.contains(searchObj, ignoreCase = true) } as ArrayList<CustomProductRate>)
                    }, 500)
                }
            }
            R.id.iv_frag_ord_prod_filter->{
                if(llFilterRoot.visibility == View.VISIBLE){
                    llFilterRoot.visibility = View.GONE
                }else{
                    llFilterRoot.visibility = View.VISIBLE
                }
            }
            R.id.iv_frag_ord_prod_mic ->{
                progrwss_wheel.spin()
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
                        progrwss_wheel.stopSpinning()
                    }, 3000)

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
                etSearch.setText(result!![0].toString())
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }

    fun loadProduct(productL: ArrayList<CustomProductRate>) {
        //progrwss_wheel.spin()
        println("tag_timer end1 ${AppUtils.getCurrentDateTime()}")
        var productQtyRateSubmit: ArrayList<ProductQtyRateSubmit> = ArrayList()
        var isOnline = AppUtils.isOnline(mContext)
        for (i in 0..productL.size - 1) {
            var obj: ProductQtyRateSubmit = ProductQtyRateSubmit()
            obj.apply {
                product_id = productL.get(i).product_id
                product_name = productL.get(i).product_name
                brand_id = productL.get(i).brand_id
                brand = productL.get(i).brand
                category_id = productL.get(i).category_id
                category = productL.get(i).category
                watt_id = productL.get(i).watt_id
                watt = productL.get(i).watt
                product_mrp_show = productL.get(i).product_mrp_show
                product_discount_show = productL.get(i).product_discount_show
                rate = productL.get(i).rate
                submitedQty = "-1"
                submitedRate = productL.get(i).rate

       // Begin 3.0  v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product
                Qty_per_Unit = productL.get(i).Qty_per_Unit
                Scheme_Qty= productL.get(i).Scheme_Qty
                Effective_Rate = productL.get(i).Effective_Rate
                // end 3.0  v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product


                // 2.0 start OrderProductListFrag  v 4.1.6 stock optmization mantis 0026391 20-06-2023 saheli
                stock_amount =  productL.get(i).stock_amount
                stock_unit = productL.get(i).stock_unit
                isStockShow = if(isOnline) productL.get(i).isStockShow else false
                isRateShow = productL.get(i).isRateShow
                // 2.0 end OrderProductListFrag  v 4.1.6 stock optmization mantis 0026391 20-06-2023 saheli

                // 4.0 OrderProductListFrag AppV 4.0.8 Suman    12/04/2023 Adapter list generation update 0025876
                /*if(Pref.isRateOnline){
                    if((AppDatabase.getDBInstance()?.productOnlineRateTempDao()?.getAll() as ArrayList<ProductOnlineRateTempEntity>).size>0){
                        var secObj = AppDatabase.getDBInstance()?.productOnlineRateTempDao()?.getObjByProductID(productL.get(i).product_id!!)?.firstOrNull()
                        stock_amount = if(secObj != null) secObj.stock_amount.toString() else "0"
                        stock_unit = if(secObj != null) secObj.stock_unit.toString() else ""
                        isStockShow = if(secObj != null) secObj.isStockShow else false
                        isRateShow = if(secObj != null) secObj.isRateShow else false
                    }
                }*/
            }

            productQtyRateSubmit.add(obj)
        }

        println("tag_timer end2 ${AppUtils.getCurrentDateTime()}")

        productAdapter = AdapterOrdProductOptimized(mContext, productQtyRateSubmit, shop_id,finalOrderDataList, object :
                AdapterOrdProductOptimized.OnProductOptiOnClick {
                override fun onProductAddClick(productCount: Int,sumAmt:Double) {
                    AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    tv_productCount.text = "${productCount.toString()}"
                    tv_productAmt.text = "₹ ${String.format("%.2f",sumAmt.toDouble())}"
                }
            })
        rv_product.adapter = productAdapter
        Handler().postDelayed(Runnable {
            progrwss_wheel.stopSpinning()
        }, 1000)

    }
    private fun getProductRateListByShopID() {
        progrwss_wheel.spin()
        BaseActivity.isApiInitiated = true
        val repository = ProductListRepoProvider.productListProvider()
        BaseActivity.compositeDisposable.add(
            repository.getProductRateListByEntity(shop_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    //val response = result as ProductRateListResponseModel
                    val response = result as ProductRateOnlineListResponseModel
                    BaseActivity.isApiInitiated = false
                    if (response.status == NetworkConstant.SUCCESS) {
                        if (response.product_rate_list != null && response.product_rate_list!!.size > 0) {

                            doAsync {
                                AppDatabase.getDBInstance()?.productOnlineRateTempDao()?.deleteAllProduct()
                                AppDatabase.getDBInstance()?.productOnlineRateTempDao()?.insertAll(response.product_rate_list!!)
                                uiThread {
                                    progrwss_wheel.stopSpinning()
                                    initProduct()
                                }
                            }
                        } else {
                            progrwss_wheel.stopSpinning()
                            ToasterMiddle.msgShort(mContext, "No online rate found.")
                        }
                    }
                }, { error ->
                    error.printStackTrace()
                    BaseActivity.isApiInitiated = false
                    progrwss_wheel.stopSpinning()
                })
        )
    }
    fun checkCartSize():Int{
        return finalOrderDataList.size
    }

    fun updateCartSize(){
        try {
            productAdapter.notifyDataSetChanged()
            tv_productCount.text = "${finalOrderDataList.size.toString()}"
            tv_productAmt.text = "₹ "+String.format("%.2f",finalOrderDataList.sumOf { it.rate.toDouble() * it.qty.toDouble() })
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

}