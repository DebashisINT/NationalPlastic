package com.breezefieldnationalplastic.features.NewQuotation

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.NewOrderColorEntity
import com.breezefieldnationalplastic.app.domain.NewOrderProductEntity
import com.breezefieldnationalplastic.app.domain.ShopExtraContactEntity
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.NewQuotation.adapter.AdapterMultiContactQuto
import com.breezefieldnationalplastic.features.NewQuotation.adapter.ShowAddedProdAdapter
import com.breezefieldnationalplastic.features.NewQuotation.api.GetQuotRegProvider
import com.breezefieldnationalplastic.features.NewQuotation.dialog.FreightListDialog
import com.breezefieldnationalplastic.features.NewQuotation.dialog.MemberSalesmanListDialog
import com.breezefieldnationalplastic.features.NewQuotation.dialog.TaxListDialog
import com.breezefieldnationalplastic.features.NewQuotation.model.AddQuotRequestData
import com.breezefieldnationalplastic.features.NewQuotation.model.Extra_contact_list
import com.breezefieldnationalplastic.features.NewQuotation.model.product_list
import com.breezefieldnationalplastic.features.addshop.api.AddShopRepositoryProvider
import com.breezefieldnationalplastic.features.addshop.presentation.ShopExtraContactResponse
import com.breezefieldnationalplastic.features.addshop.presentation.ShopListSubmitResponse
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.location.LocationWizard
import com.breezefieldnationalplastic.features.member.api.TeamRepoProvider
import com.breezefieldnationalplastic.features.member.model.TeamListDataModel
import com.breezefieldnationalplastic.features.member.model.TeamListResponseModel
import com.breezefieldnationalplastic.features.member.model.TeamShopListDataModel
import com.breezefieldnationalplastic.features.viewAllOrder.api.OrderDetailsListRepoProvider
import com.breezefieldnationalplastic.features.viewAllOrder.model.NewOrderDataModel
import com.breezefieldnationalplastic.features.viewAllOrder.presentation.ColorListDialog
import com.breezefieldnationalplastic.features.viewAllOrder.presentation.ProductListNewOrderDialog
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.breezefieldnationalplastic.widgets.AppCustomTextView

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

//Revision History
// 1.0 AddQuotFormFragment  AppV 4.0.6  Saheli    06/01/2023 addQuot contactmulti design functionlity add
// 2.0 AddQuotFormFragment  AppV 4.0.6 Saheli    09/01/2023 addQuot pdf design add template
// 3.0 AddQuotFormFragment  AppV 4.0.6 Saheli    09/01/2023 addQuot contactmulti  functionlity
// 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix

class AddQuotFormFragment: BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var quot_no: AppCustomEditText
    private lateinit var etRemarks: AppCustomEditText
    private lateinit var date: AppCustomTextView
    // 1.0 AddQuotFormFragment  AppV 4.0.6 addQuot contactmulti design
    private lateinit var tvCustomerPerson: AppCustomTextView

    // 1.0 AddQuotFormFragment  AppV 4.0.6 addQuot contactmulti design
    private lateinit var custName: AppCustomEditText
    private lateinit var projName: AppCustomEditText
    private lateinit var product: AppCustomTextView
    private lateinit var color: AppCustomTextView
    private lateinit var sqFt: AppCustomEditText
    private lateinit var sqMtr: AppCustomEditText
    private lateinit var taxes: AppCustomTextView
    private lateinit var freight: AppCustomTextView
    private lateinit var del_time: AppCustomEditText
    private lateinit var validilty: AppCustomEditText
    private lateinit var billing: AppCustomEditText
    private lateinit var product_tolrence:AppCustomEditText
    private lateinit var salesmsan:AppCustomTextView
    private lateinit var product_coating_tolrence:AppCustomEditText
    private lateinit var qty:AppCustomEditText
    private lateinit var amount:AppCustomEditText
    private lateinit var btnSave: AppCustomTextView
    private lateinit var payment:AppCustomEditText

    private lateinit var ll_qtyRoot:LinearLayout
    private lateinit var ll_amtRoot:LinearLayout
    private lateinit var lltermsRoot:LinearLayout

    private var myCalendar = Calendar.getInstance()
    private var selectedDate = ""

    private var product_list: List<NewOrderProductEntity> = listOf()
    private var isProductSel: Boolean = false
    private var productId: Int = 0
    private var colorId: Int = 0
    private var color_list: List<NewOrderColorEntity> = listOf()
    private var member_list: ArrayList<TeamListDataModel>? = null

    var addQuotData = AddQuotRequestData()
    private var salesman_userID: String =""

    private lateinit var progress_wheel: ProgressWheel
    private lateinit var fabAddProduct:FloatingActionButton
    private lateinit var rv_addedProduct:RecyclerView
    private lateinit var rv_multiContact:RecyclerView
    var addedProdList:ArrayList<product_list> = ArrayList()

    var showAddedProdAdapter: ShowAddedProdAdapter? = null

    var selectedContactL : ArrayList<ShopExtraContactEntity> = ArrayList()

    // 2.0 AddQuotFormFragment addQuot pdf design add template
    private lateinit var rbgSelectTemplate: RadioGroup
    // 3.0 AddQuotFormFragment functionlity
    private lateinit var ll_contact: LinearLayout
    private var sel_teamplate : String = ""

    companion object {
        var shop_id:String = ""
        var shop_name: String? = null
        var obj = TeamShopListDataModel()
        fun getInstance(shopObj: Any?): AddQuotFormFragment {
            val mQuotListFragment = AddQuotFormFragment()
            if (!TextUtils.isEmpty(shopObj.toString())){
                obj = shopObj as TeamShopListDataModel
                shop_id =obj!!.shop_id
                shop_name =obj!!.shop_name

            }
            return mQuotListFragment
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_add_quot_form, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        // 1.0 AddQuotFormFragment  AppV 4.0.6 addQuot contactmulti design functionlity addstart
        tvCustomerPerson = view.findViewById(R.id.tv_frag_add_quot_form_customer_p)

        // 1.0 AddQuotFormFragment  AppV 4.0.6 addQuot contactmulti design functionlity addend
        quot_no = view.findViewById(R.id.et_frag_add_quot_form_Quot_no)
        etRemarks = view.findViewById(R.id.et_frag_add_quot_form_remarks)
        date = view.findViewById(R.id.tv_frag_add_quot_form_date)
        custName = view.findViewById(R.id.tv_frag_add_quot_form_customer_name)
        projName = view.findViewById(R.id.et_frag_add_quot_form_project_name)
        product = view.findViewById(R.id.tv_frag_add_quot_form_product)
        color = view.findViewById(R.id.tv_frag_add_quot_form_color)
        sqFt = view.findViewById(R.id.et_frag_add_quot_form_sqft)
        sqMtr = view.findViewById(R.id.et_frag_add_quot_form_sqmrt)
        taxes = view.findViewById(R.id.tv_frag_add_quot_form_taxes)
        freight = view.findViewById(R.id.tv_frag_add_quot_form_freight)
        del_time = view.findViewById(R.id.et_frag_add_quot_form_del_time)
        validilty = view.findViewById(R.id.et_frag_add_quot_form_validity)
        billing = view.findViewById(R.id.et_frag_add_quot_form_billing)
        product_tolrence = view.findViewById(R.id.et_frag_add_quot_form_product_tolrence)
        salesmsan = view.findViewById(R.id.tv_frag_add_quot_form_product_salemans)
        product_coating_tolrence = view.findViewById(R.id.et_frag_add_quot_form_coating_tolrence)
        qty = view.findViewById(R.id.et_frag_add_quot_form_qty)
        amount = view.findViewById(R.id.et_frag_add_quot_form_amount)
        payment = view.findViewById(R.id.et_frag_add_quot_form_payment)

        btnSave = view.findViewById(R.id.btn_frag_add_quot_save_TV)

        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()
        fabAddProduct=view.findViewById(R.id.fb_frag_add_quot_add_product)
        rv_addedProduct=view.findViewById(R.id.rv_frag_add_quot_added_product)
        rv_multiContact=view.findViewById(R.id.rv_frag_add_quto_multi_cont)

        ll_qtyRoot=view.findViewById(R.id.ll_frag_add_quot_quantity)
        ll_amtRoot=view.findViewById(R.id.ll_frag_add_quot_amount)

        lltermsRoot = view.findViewById(R.id.ll_terms_frag_add_quot)


        salesmsan.text = Pref.user_name
        // 2.0 AddQuotFormFragment addQuot pdf design add template
        rbgSelectTemplate = view.findViewById(R.id.rbg_frag_add_quot_form)

        ll_contact = view.findViewById(R.id.ll_contact)

        // 3.0 AddQuotFormFragment functionlity
        if(Pref.IsContactPersonRequiredinQuotation){
            ll_contact.visibility = View.VISIBLE
        } else {
            ll_contact.visibility = View.GONE
        }

        if(Pref.NewQuotationShowTermsAndCondition){
            lltermsRoot.visibility=View.VISIBLE
        }else{
            lltermsRoot.visibility=View.GONE
        }

        if(Pref.ShowAmountNewQuotation){
            ll_amtRoot.visibility=View.VISIBLE
        }else{
            ll_amtRoot.visibility=View.GONE
        }
        if(Pref.ShowQuantityNewQuotation){
            ll_qtyRoot.visibility=View.VISIBLE
        }else{
            ll_qtyRoot.visibility=View.GONE
        }

         if (AppUtils.isOnline(mContext)){
             getTeamList()
         }
        else{
             Toaster.msgShort(mContext, "No Internet connection")
         }

        salesmsan.text=Pref.user_name

        setData()
        OnclickLisener(view)

        // change listener for our radio group.
        rbgSelectTemplate.setOnCheckedChangeListener { group, checkedId ->
            // on below line we are getting radio button from our group.
            val radioButton = view.findViewById<RadioButton>(checkedId)
            // on below line we are displaying a toast message.
            sel_teamplate =radioButton.text.toString()

        }

        sqFt.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!=null && s!="" && s.toString().length>0){
                    var value=s.toString().toDouble()*Pref.SqMtrRateCalculationforQuotEuro.toDouble()
                    sqMtr.setText(String.format("%.2f",value))
                }else{
                    sqMtr.setText("")
                }
            }
        })

    }

    private fun OnclickLisener(view: View) {
        btnSave.setOnClickListener(this)
        date.setOnClickListener(this)
        product.setOnClickListener(this)
        color.setOnClickListener(this)
        taxes.setOnClickListener(this)
        freight.setOnClickListener(this)
        salesmsan.setOnClickListener(this)
        fabAddProduct.setOnClickListener(this)
        tvCustomerPerson.setOnClickListener(this)
    }

    private fun setData() {
        custName.setText(shop_name)
        billing.setText("Billing will be in Sq. Mt.")
        product_tolrence.setText("Coil ±0.02mm & Panel ±0.20mm")
        product_coating_tolrence.setText("Front Coil / Top Skin - 25μ (Two Coat)" +
                "Back Coil / Bottom Skin - 7μ Service Coat")
        //product_list= AppDatabase.getDBInstance()?.newOrderProductDao()?.getAllProduct()!!

        Handler(Looper.getMainLooper()).postDelayed({
            product_list= AppDatabase.getDBInstance()?.newOrderProductDao()?.getAllProduct()!!
            if(product_list.size==0){
                getNewOrderDataList()
            }
        }, 1000)

        if(!Pref.IsNewQuotationNumberManual){
            autoGeneratedQuot()
        }

    }

    fun getNewOrderDataList() {
        try {
            AppDatabase.getDBInstance()?.newOrderGenderDao()?.deleteAll()
            AppDatabase.getDBInstance()?.newOrderProductDao()?.deleteAll()
            AppDatabase.getDBInstance()?.newOrderColorDao()?.deleteAll()
            AppDatabase.getDBInstance()?.newOrderSizeDao()?.deleteAll()

            val repository = OrderDetailsListRepoProvider.provideOrderDetailsListRepository()
            progress_wheel.spin()
            BaseActivity.compositeDisposable.add(
                repository.getNewOrderData()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as NewOrderDataModel
                        if (response.status == NetworkConstant.SUCCESS) {
                            var list_gender = response.Gender_list
                            var list_product = response.Product_list
                            var list_color = response.Color_list
                            var list_size = response.size_list

                            if (list_gender != null && list_gender.isNotEmpty()) {
                                doAsync {

                                    for (l in 0..list_gender.size - 1) {
                                        if (list_gender.get(l).gender_id == 1) {
                                            Pref.new_ord_gender_male = list_gender.get(l).gender.toString().toUpperCase()
                                        }
                                        if (list_gender.get(l).gender_id == 2) {
                                            Pref.new_ord_gender_female = list_gender.get(l).gender.toString().toUpperCase()
                                        }
                                    }

                                    AppDatabase.getDBInstance()?.newOrderGenderDao()?.insertAll(list_gender)
                                    AppDatabase.getDBInstance()?.newOrderGenderDao()?.updateGendertoUpperCase()

                                    if (list_product != null && list_product.isNotEmpty()) {
                                        AppDatabase.getDBInstance()?.newOrderProductDao()?.insertAll(list_product)
                                        AppDatabase.getDBInstance()?.newOrderProductDao()?.updateProducttoUpperCase()
                                    }
                                    if (list_color != null && list_color.isNotEmpty()) {
                                        AppDatabase.getDBInstance()?.newOrderColorDao()?.insertAll(list_color)
                                        AppDatabase.getDBInstance()?.newOrderColorDao()?.updateColorNametoUpperCase()
                                    }
                                    if (list_size != null && list_size.isNotEmpty()) {
                                        AppDatabase.getDBInstance()?.newOrderSizeDao()?.insertAll(list_size)
                                        AppDatabase.getDBInstance()?.newOrderSizeDao()?.updateSizeNametoUpperCase()
                                    }

                                    uiThread {
                                        progress_wheel.stopSpinning()
                                        product_list= AppDatabase.getDBInstance()?.newOrderProductDao()?.getAllProduct()!!
                                    }
                                }
                            }
                            else if(list_product != null && list_product.isNotEmpty()){

                                if (list_product != null && list_product.isNotEmpty()) {
                                    AppDatabase.getDBInstance()?.newOrderProductDao()?.insertAll(list_product)
                                    AppDatabase.getDBInstance()?.newOrderProductDao()?.updateProducttoUpperCase()
                                }
                                if (list_color != null && list_color.isNotEmpty()) {
                                    AppDatabase.getDBInstance()?.newOrderColorDao()?.insertAll(list_color)
                                    AppDatabase.getDBInstance()?.newOrderColorDao()?.updateColorNametoUpperCase()
                                }

                                progress_wheel.stopSpinning()
                                product_list= AppDatabase.getDBInstance()?.newOrderProductDao()?.getAllProduct()!!
                            }
                        } else {
                            progress_wheel.stopSpinning()
                        }

                    }, { error ->
                        progress_wheel.stopSpinning()
                        error.printStackTrace()
                    })
            )
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_frag_add_quot_form_customer_p->{
                //if(CustomStatic.TeamUserSelect_user_id.equals(Pref.user_id)){
                    //callMultiContactDialog()
                //}else{
                //tvCustomerPerson.isEnabled = false
                fetchMultiContactDetails()
                //}

            }
            R.id.btn_frag_add_quot_save_TV -> {
                // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
                btnSave.isEnabled = false
                // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
              checkValidation(p0)
            }
            R.id.tv_frag_add_quot_form_date -> {
                val datePicker = DatePickerDialog(mContext, R.style.DatePickerTheme, dates, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
                datePicker.show()
            }

            R.id.tv_frag_add_quot_form_product -> {
                color.text=""
                color.hint="Select  Color"
                loadProduct()
            }
            R.id.tv_frag_add_quot_form_color -> {
                if (isProductSel) {
                    loadColor()
                } else{
                    Toaster.msgShort(mContext, "Please Select a Product")
                }
            }
            R.id.tv_frag_add_quot_form_taxes -> {
                loadTax()
            }
            R.id.tv_frag_add_quot_form_freight -> {
                loadFreight()
            }
          /*  R.id.tv_frag_add_quot_form_product_salemans->{
                loadSaleman()
            }*/
            R.id.fb_frag_add_quot_add_product->{

                if (TextUtils.isEmpty(product.text.toString())) {
                    (mContext as DashboardActivity).showSnackMessage("Please select Product ")
                    BaseActivity.isApiInitiated = false
                    return
                }
                else if (TextUtils.isEmpty(color.text.toString())) {
                    (mContext as DashboardActivity).showSnackMessage("Please select Color ")
                    BaseActivity.isApiInitiated = false
                    return
                }
                else if (TextUtils.isEmpty(sqFt.text.toString())) {
                    (mContext as DashboardActivity).showSnackMessage("Please select Rate/SqFt ")
                    BaseActivity.isApiInitiated = false
                    return
                }
                else if (TextUtils.isEmpty(sqMtr.text.toString())) {
                    (mContext as DashboardActivity).showSnackMessage("Please select Rate/SqMtr ")
                    BaseActivity.isApiInitiated = false
                    return
                }


                if(addedProdList.size>0){
                    for(i in 0..addedProdList.size-1){
                        if(productId==addedProdList.get(i).product_id!!.toInt()){
                            if(colorId==addedProdList.get(i).color_id!!.toInt()){
                                Toaster.msgShort(mContext,"Product with same color already added.")
                                return
                            }
                        }
                    }
                }

                if(addedProdList.size==10){
                    Toaster.msgShort(mContext,"Already added 10 products.")
                    return
                }

                var obj:product_list = product_list()
                obj.product_id=productId.toString()
                obj.product_name=product.text.toString()
                obj.color_id=colorId.toString()
                obj.color_name=color.text.toString()
                obj.rate_sqft=sqFt.text.toString()
                obj.rate_sqmtr=sqMtr.text.toString()
                obj.qty=qty.text.toString()
                obj.amount=amount.text.toString()

                if(obj!!.qty!!.length == 0){
                    obj!!.qty="0"
                }
                if(obj!!.amount!!.length == 0){
                    obj!!.amount="0"
                }
                addedProdList.add(obj)
                rv_addedProduct.layoutParams.height=200*addedProdList.size
                showAddedProdAdapter=ShowAddedProdAdapter(mContext,addedProdList)
                rv_addedProduct.adapter=showAddedProdAdapter

                product.text=""
                color.text=""
                sqFt.setText("")
                sqMtr.setText("")
                qty.setText("")
                amount.setText("")

                sqFt.clearFocus()
                sqMtr.clearFocus()
                qty.clearFocus()
                amount.clearFocus()

                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
            }

        }
    }

    val dates = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        selectedDate = AppUtils.getFormattedDateForApi(myCalendar.time)
        updateLabel()
    }

    private fun updateLabel() {
        date.setText(AppUtils.getFormattedDate(myCalendar.time))
    }

    private fun autoGeneratedQuot() {

    }

    private fun loadProduct() {
        if (product_list != null && product_list.isNotEmpty()) {
            ProductListNewOrderDialog.newInstance(product_list as ArrayList<NewOrderProductEntity>) {
                isProductSel = true
                productId = it.product_id!!
                product.setText(it.product_name)
                color_list = emptyList()
                color_list = AppDatabase.getDBInstance()?.newOrderColorDao()?.getColorListProductWise(it.product_id!!) as List<NewOrderColorEntity>
            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        } else {
            Toaster.msgShort(mContext, "No Product Found")
        }
    }

    private fun loadColor() {
        if (color_list != null && color_list.isNotEmpty()) {
            ColorListDialog.newInstance(color_list as ArrayList<NewOrderColorEntity>) {
                color.setText( it.color_name)
                colorId = it.color_id!!
            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        } else {
            Toaster.msgShort(mContext, "No Color Found")
        }
    }

    private fun loadTax() {
        var taxList:ArrayList<String> = ArrayList()
        taxList.add("Exclusive 18% GST")
        taxList.add("Inclusive 18% GST")
        TaxListDialog.newInstance(taxList){
            taxes.text=it
        }.show((mContext as DashboardActivity).supportFragmentManager, "")

    }

    private fun loadFreight() {
        var freightList:ArrayList<String> = ArrayList()
        freightList.add("Exclusive (Extra as applicable)")
        freightList.add("Inclusive (Extra as applicable)")
        FreightListDialog.newInstance(freightList){
            freight.text=it
        }.show((mContext as DashboardActivity).supportFragmentManager, "")

    }

    private fun getTeamList() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }
        progress_wheel.spin()
        val repository = TeamRepoProvider.teamRepoProvider()
        BaseActivity.compositeDisposable.add(
                repository.teamList(Pref.user_id!!, true, true)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as TeamListResponseModel
                            Timber.d("GET TEAM DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {
                                progress_wheel.stopSpinning()
                                if (response.member_list != null && response.member_list!!.size > 0) {
                                    member_list = response.member_list!!
                                } else {
                                    (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            Timber.d("GET TEAM DATA : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun loadSaleman() {
        MemberSalesmanListDialog.newInstance("Select Salesman",member_list!!){
            salesmsan.text=it.user_name
            salesman_userID=it.user_id
        }.show((mContext as DashboardActivity).supportFragmentManager, "")

    }

    private fun checkValidation(view:View) {
//        if (TextUtils.isEmpty(quot_no.text.toString())) {
//            (mContext as DashboardActivity).showSnackMessage("Please select Quot to ")
//            BaseActivity.isApiInitiated = false
//            return
//        }

        if (TextUtils.isEmpty(date.text.toString())) {
            (mContext as DashboardActivity).showSnackMessage("Please select date ")
            BaseActivity.isApiInitiated = false
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
            btnSave.isEnabled = true
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
            return
        }
        else if (TextUtils.isEmpty(projName.text.toString())) {
            (mContext as DashboardActivity).showSnackMessage("Please select Project Name ")
            BaseActivity.isApiInitiated = false
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
            btnSave.isEnabled = true
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
            return
        }
//        else if (TextUtils.isEmpty(custName.text.toString())) {
//            (mContext as DashboardActivity).showSnackMessage("Please select Customer Name ")
//            BaseActivity.isApiInitiated = false
//            return
//        }
        else if (TextUtils.isEmpty(taxes.text.toString())) {
            (mContext as DashboardActivity).showSnackMessage("Please select taxes ")
            BaseActivity.isApiInitiated = false
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
            btnSave.isEnabled = true
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
            return
        }
        else if (TextUtils.isEmpty(freight.text.toString())) {
            (mContext as DashboardActivity).showSnackMessage("Please select Freight ")
            BaseActivity.isApiInitiated = false
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
            btnSave.isEnabled = true
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
            return
        }
//        else if (TextUtils.isEmpty(del_time.text.toString())) {
//            (mContext as DashboardActivity).showSnackMessage("Please select Delivery Time ")
//            BaseActivity.isApiInitiated = false
//            return
//        }
//        else if (TextUtils.isEmpty(validilty.text.toString())) {
//            (mContext as DashboardActivity).showSnackMessage("Please select Validilty ")
//            BaseActivity.isApiInitiated = false
//            return
//        }
//        else if (TextUtils.isEmpty(billing.text.toString())) {
//            (mContext as DashboardActivity).showSnackMessage("Please select Billing ")
//            BaseActivity.isApiInitiated = false
//            return
//        }
//        else if (TextUtils.isEmpty(product_tolrence.text.toString())) {
//            (mContext as DashboardActivity).showSnackMessage("Please select Product Tolerence ")
//            BaseActivity.isApiInitiated = false
//            return
//        }
        else if (TextUtils.isEmpty(salesmsan.text.toString())) {
            (mContext as DashboardActivity).showSnackMessage("Please select Salesman ")
            BaseActivity.isApiInitiated = false
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
            btnSave.isEnabled = true
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
            return
        }
        if (Pref.user_id == null || Pref.user_id == "" || Pref.user_id == " ") {
            (mContext as DashboardActivity).showSnackMessage("Please login again")
            BaseActivity.isApiInitiated = false
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
            btnSave.isEnabled = true
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
            return
        }

        if (addedProdList.size==0) {
            (mContext as DashboardActivity).showSnackMessage("Please add a product")
            BaseActivity.isApiInitiated = false
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
            btnSave.isEnabled = true
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
            return
        }
        // 3.0 AddQuotFormFragment functionlity
        var selectedTemplateId= rbgSelectTemplate.getCheckedRadioButtonId()
        if(selectedTemplateId == -1){
            (mContext as DashboardActivity).showSnackMessage("Please select any template")
            BaseActivity.isApiInitiated = false
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
            btnSave.isEnabled = true
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
            return
        }

        addQuotData.user_id = Pref.user_id

        // 3.0 AddQuotFormFragment functionlity
        // 2.0 AddQuotFormFragment addQuot pdf design add template
        addQuotData.sel_quotation_pdf_template = sel_teamplate
        if(Pref.IsContactPersonRequiredinQuotation){
            if(selectedContactL.size==0){
                (mContext as DashboardActivity).showSnackMessage("Please select Contact Person")
                BaseActivity.isApiInitiated = false
                // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
                btnSave.isEnabled = true
                // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
                return
            }else{
                var finalExtCntL : ArrayList<Extra_contact_list> = ArrayList()
                for(i in 0..selectedContactL.size-1){
                    var ob = Extra_contact_list()
                    ob.apply {
                        quotation_contact_person = selectedContactL.get(i).contact_name
                        quotation_contact_number = selectedContactL.get(i).contact_number
                        quotation_contact_email = selectedContactL.get(i).contact_email
                        quotation_contact_doa = selectedContactL.get(i).contact_doa
                        quotation_contact_dob = selectedContactL.get(i).contact_dob
                    }
                    finalExtCntL.add(ob)
                }
                addQuotData.extra_contact_list=finalExtCntL
            }
        }else{
            addQuotData.extra_contact_list=ArrayList()
        }

//        addQuotData.quotation_number = quot_no.text.toString()
        addQuotData.save_date_time = AppUtils.getCurrentDateTime()
        addQuotData.quotation_date_selection =  selectedDate
        addQuotData.project_name = projName.text.toString()
        addQuotData.shop_id = shop_id
        addQuotData.taxes = taxes.text.toString()
        addQuotData.Freight = freight.text.toString()
        addQuotData.delivery_time = del_time.text.toString()
        addQuotData.payment = payment.text.toString()
        addQuotData.validity = validilty.text.toString()
        addQuotData.billing = billing.text.toString()
        addQuotData.product_tolerance_of_thickness = product_tolrence.text.toString()
        addQuotData.tolerance_of_coating_thickness = product_coating_tolrence.text.toString()
//        addQuotData.salesman_user_id = salesman_userID
        addQuotData.salesman_user_id = Pref.user_id
        addQuotData.quotation_created_lat = Pref.latitude.toString()
        addQuotData.quotation_created_long = Pref.longitude.toString()
        addQuotData.quotation_created_address = LocationWizard.getAdressFromLatlng(mContext, Pref.latitude!!.toDouble(), Pref.longitude!!.toDouble())
        addQuotData.document_number = Pref.user_id + AppUtils.getCurrentDateTime().replace(" ","").replace("-","").replace(":","")
        addQuotData.Remarks = etRemarks.text.toString()
        addQuotData.quotation_status = "Pending"
        addQuotData.product_list= ArrayList()

        addQuotData.product_list=addedProdList

        saveButtonCall(addQuotData)

    }

    private fun saveButtonCall(addQuot: AddQuotRequestData) {
        try{
            BaseActivity.isApiInitiated = true
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix begin
            btnSave.isEnabled = true
            // 4.0 AddQuotFormFragment  AppV 4.2.6 Suman    25/04/2024 save button multi click fix end
            progress_wheel.spin()
            val repository = GetQuotRegProvider.provideSaveButton()
            BaseActivity.compositeDisposable.add(
                    repository.addQuot(addQuot)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as BaseResponse
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    //(mContext as DashboardActivity).showSnackMessage("Quotation saved successfully.")
                                    Handler().postDelayed(Runnable {
                                        sendSuccessEmail(addQuot)
                                    }, 200)


                                } else if (addShopResult.status == NetworkConstant.SESSION_MISMATCH) {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                } else if (addShopResult.status == "205") {
                                    (mContext as DashboardActivity).showSnackMessage("Duplicate Quotation Number.")
                                } else {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                }
                            }, { error ->
                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                if (error != null) {
                                }
                            })
            )
        }catch (ex:Exception){
            ex.printStackTrace()
            BaseActivity.isApiInitiated = false
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
            progress_wheel.stopSpinning()
        }

    }

    private fun showSuccessDialog( msgBody:String){
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()+"!"
        dialogHeader.text = msgBody
        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            Handler().postDelayed(Runnable {
                (mContext as DashboardActivity).onBackPressed()
            }, 500)
        })
        simpleDialog.show()
    }

    fun sendSuccessEmail(addQuot: AddQuotRequestData){
        try {
            progress_wheel.spin()
            //Suman 28-06-2024 mantis id 27584
            var isMailSend = true
            doAsync {


                try {
                    var m = Mail()
                    var toArr = arrayOf("")



                    if(!Pref.automail_sending_email.equals("") && !Pref.automail_sending_pass.equals("") && !Pref.recipient_email_ids.equals("")){
                        var emailRecpL = Pref.recipient_email_ids.split(",")
                        m = Mail(Pref.automail_sending_email, Pref.automail_sending_pass)
                        toArr = Array<String>(emailRecpL.size){""}
                        for(i in 0..emailRecpL.size-1){
                            toArr[i]=emailRecpL[i]
                        }

                        m.setTo(toArr)
                        m.setFrom("TEAM");
                        m.setSubject("Quotation Generated by ${Pref.user_name}.")
                        m.setBody("Quotation Generated by  ${Pref.user_name} dated  ${AppUtils.getCurrentDate_DD_MM_YYYY()}.")
                        m.send()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    isMailSend = false
                    Timber.d("Automail send error ${e.printStackTrace()}")
                }

                uiThread {
                    progress_wheel.stopSpinning()
                    if(isMailSend == false){
                        val simpleDialog = Dialog(mContext)
                        simpleDialog.setCancelable(false)
                        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        simpleDialog.setContentView(R.layout.dialog_message)
                        val dialogHeader = simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
                        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
                        dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()+"!"
                        dialogHeader.text = "Either e-mail ID or password is incorrect. Auto mail can't be send. Please contact to Administrator."
                        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
                        dialogYes.setOnClickListener({ view ->
                            simpleDialog.cancel()
                            Handler().postDelayed(Runnable {
                                showSuccessDialog("Quotation saved successfully.")
                            }, 500)
                        })
                        simpleDialog.show()
                    }else{
                        showSuccessDialog("Quotation saved successfully.")
                    }
                }
            }
        }catch (ex:Exception){
            ex.printStackTrace()
            progress_wheel.stopSpinning()
        }

    }


    // 1.0 AddQuotFormFragment  AppV 4.0.6 addQuot contactmulti design functionlity add
    /*private fun callMultiContactDialog(){
        var extraContL = AppDatabase.getDBInstance()?.shopExtraContactDao()?.getExtraContListByShopID(shop_id!!) as ArrayList<ShopExtraContactEntity>
        var shopDtlsInfo = AppDatabase.getDBInstance()?.addShopEntryDao()!!.getShopDetail(shop_id)

        var obj : ShopExtraContactEntity = ShopExtraContactEntity()
        obj.apply {
            shop_id = shopDtlsInfo.shop_id
            contact_serial = ""
            contact_name = shopDtlsInfo.ownerName
            contact_number = shopDtlsInfo.ownerContactNumber
            contact_email = shopDtlsInfo.ownerEmailId
            contact_doa = shopDtlsInfo.dateOfAniversary
            isUploaded = true
        }
        extraContL.add(obj)

        if(extraContL!!.size>0){
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
            val customView = inflater!!.inflate(R.layout.popup_multi_caontact, null)
            visitMultiContactPopupWindow = PopupWindow(customView, resources.getDimensionPixelOffset(R.dimen._220sdp), RelativeLayout.LayoutParams.WRAP_CONTENT)
            val rv_multi_contact_type_list = customView.findViewById(R.id.rv_multi_contact_type_list) as RecyclerView
            rv_multi_contact_type_list.layoutManager = LinearLayoutManager(mContext)

            visitMultiContactPopupWindow?.elevation = 200f
            visitMultiContactPopupWindow?.isFocusable = true
            visitMultiContactPopupWindow?.update()

            var selectedContactL : ArrayList<ShopExtraContactEntity> = ArrayList()
            rv_multiContact.adapter = AdapterMultiContactQuto(mContext,extraContL, object :
                AdapterMultiContactQuto.OnClickListener {
                override fun onTickUntickView(obj: ShopExtraContactEntity, isTick: Boolean) {
                    if(isTick){
                        selectedContactL.add(obj)
                    }
                    else{
                        selectedContactL.remove(obj)
                    }

                }

            })
            return

            rv_multi_contact_type_list.adapter = VisitMultiContactwithoutPhoneAdapter(mContext, extraContL as ArrayList<ShopExtraContactEntity>, object : VisitMultiContactwithoutPhoneAdapter.OnItemClickListener {
                override fun onItemClick(obj: ShopExtraContactEntity) {

                    sel_extraContName = obj.contact_name!!.toString()
                    sel_extraContPh = obj.contact_number!!.toString()
                    sel_extraContemail = obj.contact_email!!.toString()
//                    sel_extraContDoa = obj.contact_doa!!.toString()
                    tvCustomerPerson.text = obj.contact_name

                    tvCustomerEmail.text = obj.contact_email
                    tvCustomerDoa.text =  if(obj.contact_doa == null) "" else AppUtils.getFormatedDateNew(obj.contact_doa, "yyyy-mm-dd","dd-mm-yyyy")
                    visitMultiContactPopupWindow?.dismiss()
                }
            })

            if (visitMultiContactPopupWindow != null && !visitMultiContactPopupWindow?.isShowing!!) {
                visitMultiContactPopupWindow?.showAsDropDown(tvCustomerPerson, tvCustomerPerson.width - visitMultiContactPopupWindow?.width!!, 0)
            }
        }
    }*/


    fun fetchMultiContactDetails(){
        try{
                val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
                BaseActivity.compositeDisposable.add(
                    repository.fetchMultiContactData(CustomStatic.TeamUserSelect_user_id!!,Pref.session_token!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            var viewResult = result as ShopListSubmitResponse
                            var extraContL:ArrayList<ShopExtraContactEntity> = ArrayList()
//                          obj.owner_name = "Owner Jh"
                            try{
                                //add default data
                                val shopExtraContactObj = ShopExtraContactEntity()
                                shopExtraContactObj.shop_id = shop_id
                                shopExtraContactObj.contact_serial = "100"
                                shopExtraContactObj.contact_name = obj.owner_name
                                shopExtraContactObj.contact_number = obj.shop_contact
                                shopExtraContactObj.contact_email = if(obj.owner_email == null) "" else obj.owner_email
                                shopExtraContactObj.contact_dob = ""
                                shopExtraContactObj.contact_doa = ""
                                shopExtraContactObj.isUploaded = true
                                extraContL.add(shopExtraContactObj)
                            }
                            catch (e:Exception) {
                                e.printStackTrace()
                            }

                            if (viewResult!!.status == NetworkConstant.SUCCESS) {
                                if(viewResult.shop_list.size>0){
                                    var reqShopObj = viewResult.copy()
                                    reqShopObj.shop_list = ArrayList()
                                    reqShopObj.shop_list = viewResult.shop_list.filter { it.shop_id.equals(shop_id) } as ArrayList<ShopExtraContactResponse>
                                    viewResult = reqShopObj

                                    for(b in 0..viewResult.shop_list.size-1){
                                        if(viewResult.shop_list.get(b).contact_serial1.toString().equals("1") && !viewResult.shop_list.get(b).contact_name1.toString().equals("")){
                                            val shopExtraContactEntity = ShopExtraContactEntity()
                                            shopExtraContactEntity.shop_id = viewResult.shop_list.get(b).shop_id.toString()
                                            shopExtraContactEntity.contact_serial = viewResult.shop_list.get(b).contact_serial1.toString()
                                            shopExtraContactEntity.contact_name = viewResult.shop_list.get(b).contact_name1.toString()
                                            shopExtraContactEntity.contact_number = viewResult.shop_list.get(b).contact_number1.toString()
                                            shopExtraContactEntity.contact_email = if(viewResult.shop_list.get(b).contact_email1 == null) "" else viewResult.shop_list.get(b).contact_email1.toString()
                                            shopExtraContactEntity.contact_doa = ""
                                            shopExtraContactEntity.contact_dob = ""
                                            shopExtraContactEntity.isUploaded = true
                                            extraContL.add(shopExtraContactEntity)
                                        }
                                        if(viewResult.shop_list.get(b).contact_serial2.toString().equals("2") && !viewResult.shop_list.get(b).contact_name2.toString().equals("")){
                                            val shopExtraContactEntity = ShopExtraContactEntity()
                                            shopExtraContactEntity.shop_id = viewResult.shop_list.get(b).shop_id.toString()
                                            shopExtraContactEntity.contact_serial = viewResult.shop_list.get(b).contact_serial2.toString()
                                            shopExtraContactEntity.contact_name = viewResult.shop_list.get(b).contact_name2.toString()
                                            shopExtraContactEntity.contact_number = viewResult.shop_list.get(b).contact_number2.toString()
                                            shopExtraContactEntity.contact_email = if(viewResult.shop_list.get(b).contact_email2 == null) "" else viewResult.shop_list.get(b).contact_email2.toString()
                                            shopExtraContactEntity.contact_doa = ""
                                            shopExtraContactEntity.contact_dob = ""
                                            shopExtraContactEntity.isUploaded = true
                                            extraContL.add(shopExtraContactEntity)

                                        }
                                        if(viewResult.shop_list.get(b).contact_serial3.toString().equals("3") && !viewResult.shop_list.get(b).contact_name3.toString().equals("")){
                                            val shopExtraContactEntity = ShopExtraContactEntity()
                                            shopExtraContactEntity.contact_serial = viewResult.shop_list.get(b).contact_serial3.toString()
                                            shopExtraContactEntity.contact_name = viewResult.shop_list.get(b).contact_name3.toString()
                                            shopExtraContactEntity.contact_number = viewResult.shop_list.get(b).contact_number3.toString()
                                            shopExtraContactEntity.contact_email = if(viewResult.shop_list.get(b).contact_email3 == null) "" else viewResult.shop_list.get(b).contact_email3.toString()
                                            shopExtraContactEntity.contact_doa = ""
                                            shopExtraContactEntity.contact_dob = ""
                                            shopExtraContactEntity.isUploaded = true
                                            extraContL.add(shopExtraContactEntity)
                                        }
                                        if(viewResult.shop_list.get(b).contact_serial4.toString().equals("4")&& !viewResult.shop_list.get(b).contact_name4.toString().equals("")){
                                            val shopExtraContactEntity = ShopExtraContactEntity()
                                            shopExtraContactEntity.contact_serial = viewResult.shop_list.get(b).contact_serial4.toString()
                                            shopExtraContactEntity.contact_name = viewResult.shop_list.get(b).contact_name4.toString()
                                            shopExtraContactEntity.contact_number = viewResult.shop_list.get(b).contact_number4.toString()
                                            shopExtraContactEntity.contact_email = if(viewResult.shop_list.get(b).contact_email4 == null) "" else viewResult.shop_list.get(b).contact_email4.toString()
                                            shopExtraContactEntity.contact_doa = ""
                                            shopExtraContactEntity.contact_dob = ""
                                            shopExtraContactEntity.isUploaded = true
                                            extraContL.add(shopExtraContactEntity)
                                        }
                                        if(viewResult.shop_list.get(b).contact_serial5.toString().equals("5") && !viewResult.shop_list.get(b).contact_name5.toString().equals("")){
                                            val shopExtraContactEntity = ShopExtraContactEntity()
                                            shopExtraContactEntity.contact_serial = viewResult.shop_list.get(b).contact_serial5.toString()
                                            shopExtraContactEntity.contact_name = viewResult.shop_list.get(b).contact_name5.toString()
                                            shopExtraContactEntity.contact_number = viewResult.shop_list.get(b).contact_number5.toString()
                                            shopExtraContactEntity.contact_email = if(viewResult.shop_list.get(b).contact_email5 == null) "" else viewResult.shop_list.get(b).contact_email5.toString()
                                            shopExtraContactEntity.contact_doa = ""
                                            shopExtraContactEntity.contact_dob = ""
                                            shopExtraContactEntity.isUploaded = true
                                            extraContL.add(shopExtraContactEntity)
                                        }
                                        if(viewResult.shop_list.get(b).contact_serial6.toString().equals("6") && !viewResult.shop_list.get(b).contact_name6.toString().equals("")){
                                            val shopExtraContactEntity = ShopExtraContactEntity()
                                            shopExtraContactEntity.contact_serial = viewResult.shop_list.get(b).contact_serial6.toString()
                                            shopExtraContactEntity.contact_name = viewResult.shop_list.get(b).contact_name6.toString()
                                            shopExtraContactEntity.contact_number = viewResult.shop_list.get(b).contact_number6.toString()
                                            shopExtraContactEntity.contact_email = if(viewResult.shop_list.get(b).contact_email6 == null) "" else viewResult.shop_list.get(b).contact_email6.toString()
                                            shopExtraContactEntity.contact_doa = ""
                                            shopExtraContactEntity.contact_dob = ""
                                            shopExtraContactEntity.isUploaded = true
                                            extraContL.add(shopExtraContactEntity)
                                        }
                                    }
                                }
                            }
                            rv_multiContact.layoutParams.height=120*extraContL.size
                            rv_multiContact.adapter = AdapterMultiContactQuto(mContext,extraContL, object : AdapterMultiContactQuto.OnClickListener {
                                override fun onTickUntickView(obj: ShopExtraContactEntity, isTick: Boolean) {
                                    if(isTick){
                                        selectedContactL.add(obj)
                                    } else{
                                        selectedContactL.remove(obj)
                                    }
                                    var nameSel = ""
                                    if(selectedContactL.size>0){
                                        for(i in 0..selectedContactL.size-1){
                                            nameSel = nameSel+selectedContactL.get(i).contact_name+","
                                        }
                                        tvCustomerPerson.text = nameSel
                                    }else{
                                        tvCustomerPerson.text = ""
                                    }
                                }
                            })
                        }, { error ->
                                error.printStackTrace()
                        })
                )
        }
        catch (ex:Exception){
            ex.printStackTrace()

        }
    }

}