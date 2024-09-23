package com.breezefieldnationalplastic.features.NewQuotation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.uiaction.IntentActionable
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.NewQuotation.adapter.ShowAddedProductAdapter
import com.breezefieldnationalplastic.features.NewQuotation.api.GetQuotRegProvider
import com.breezefieldnationalplastic.features.NewQuotation.model.*
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.location.LocationWizard
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//Revision History
// 1.0 ViewDetailsQuotFragment  AppV 4.0.6  Saheli    09/01/2023 Terms&condition
// 2.0 ViewDetailsQuotFragment  AppV 4.0.6  Saheli    09/01/2023 Contact
// 3.0 ViewDetailsQuotFragment  AppV 4.0.6  Suman 20/01/2023 pdf contact name-phone design update
// 4.0 ViewDetailsQuotFragment  AppV 4.0.6  Suman 20/01/2023 quotation_date_selection format bug fixing

class ViewDetailsQuotFragment : BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    lateinit var projectNameTv: TextView
    lateinit var shopName: TextView
    lateinit var phone: ImageView
    lateinit var quotNumber: TextView
    lateinit var time: TextView
    lateinit var progress_wheel: ProgressWheel
    var addedProdList:ArrayList<quotation_product_details_list> = ArrayList()
    var showAddedProdAdapter: ShowAddedProductAdapter? = null
    private lateinit var rv_addedProduct: RecyclerView

    private lateinit var floating_fab: FloatingActionMenu
    private var getFloatingVal:ArrayList<String> = ArrayList()
    private var programFab1: FloatingActionButton? = null

    private lateinit var tv_updates: AppCustomTextView

    lateinit var addQuotEditResult: ViewDetailsQuotResponse

    var addQuotData = EditQuotRequestData()

    // 1.0 ViewDetailsQuotFragment  AppV 4.0.6 Terms&condition
    lateinit var tvTax: TextView
    lateinit var tvFreight: TextView
    lateinit var tvDelTime: TextView
    lateinit var tvPayment: TextView
    lateinit var tvvalidity: TextView
    lateinit var tvBilling: TextView
    lateinit var tvProdtolrence: TextView
    lateinit var tvProdcoattolrence: TextView
    lateinit var tvsalesman: TextView
    lateinit var tvremarks: TextView
    // 2.0 ViewDetailsQuotFragment  AppV 4.0.6  Contact
    lateinit var tvContactP:TextView
    lateinit var tvContactPhone:TextView
    lateinit var tvContactEmail:TextView
    lateinit var tvTemplate:TextView
    lateinit var llContactDtlsRoot:LinearLayout


    companion object {
         var QuotID:String  = ""
         var DocID : String = ""
        var obj = shop_wise_quotation_list()
        fun getInstance(ob: Any?): ViewDetailsQuotFragment {
            val fragment = ViewDetailsQuotFragment()

            if (!TextUtils.isEmpty(ob.toString())) {
                QuotID = ob.toString().split(",").get(0).toString()
                DocID = ob.toString().split(",").get(1).toString()
                if(QuotID.equals("x"))
                    QuotID = ""
                if(DocID.equals("x"))
                    DocID = ""
            }


            /*val bundle = Bundle()
            bundle.putString("QuotId", QuotID as String?)
            bundle.putString("DocId", DocID as String?)
            fragment.arguments = bundle*/

            return fragment
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        //QuotID = arguments?.getString("QuotId").toString()
        //DocID = arguments?.getString("DocId").toString()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_view_details_quot_list, container, false)
        initView(view)
        if(AppUtils.isOnline(mContext)){
            Handler().postDelayed(Runnable {
                if(QuotID.equals(""))
                    docwiseQuotViewListCall(DocID)
                if(DocID.equals(""))
                    quotViewListCall(QuotID)
            }, 400)
        }
        else{
            Toaster.msgShort(mContext, "No Internet connection")
        }
        return view
    }

    private fun initView(view: View) {
        projectNameTv = view.findViewById(R.id.tv_frag_view_dtls_quto_project_name)
        shopName = view.findViewById(R.id.tv_frag_view_details_quot_list_shopName)
        phone = view.findViewById(R.id.iv_frag_view_details_quot_list_phone)
        quotNumber = view.findViewById(R.id.tv_frag_view_details_quot_list_quotId)
        time = view.findViewById(R.id.tv_frag_view_details_quot_list_date)
        progress_wheel = view.findViewById(R.id.progress_wheel)
        rv_addedProduct = view.findViewById(R.id.quot_view_list_rv)
        floating_fab = view.findViewById(R.id.floating_fab_frag_view_dtls)
        tv_updates = view.findViewById(R.id.update_TV_frag_view_details_quot_list)
        // 1.0 ViewDetailsQuotFragment  AppV 4.0.6 Terms&condition
        tvTax = view.findViewById(R.id.tv_frag_view_dtls_quot_list_taxes)
        tvFreight = view.findViewById(R.id.tv_frag_view_dtls_quot_list_freight)
        tvDelTime = view.findViewById(R.id.tv_frag_view_dtls_quot_list_del_time)
        tvPayment = view.findViewById(R.id.tv_frag_view_dtls_quot_list_payment)
        tvvalidity = view.findViewById(R.id.tv_frag_view_dtls_quot_list_validity)
        tvBilling = view.findViewById(R.id.tv_frag_view_dtls_quot_list_billing)
        tvProdtolrence = view.findViewById(R.id.tv_frag_view_dtls_quot_list_product_tolrence)
        tvProdcoattolrence = view.findViewById(R.id.tv_frag_view_dtls_quot_list_coating_tolrence)
        tvsalesman = view.findViewById(R.id.tv_frag_view_dtls_quot_list_product_salemans)
        tvremarks = view.findViewById(R.id.tv_frag_view_dtls_quot_list_remarks)
        // 2.0 ViewDetailsQuotFragment  AppV 4.0.6  Contact
        tvTemplate = view.findViewById(R.id.tv_frag_view_dtls_quot_list_template)
        tvContactP = view.findViewById(R.id.tv_frag_view_details_quot_list_contactP)
        tvContactPhone = view.findViewById(R.id.tv_frag_view_details_quot_list_contactPhone)
        tvContactEmail = view.findViewById(R.id.tv_frag_view_details_quot_list_contactEmail)
        llContactDtlsRoot = view.findViewById(R.id.ll_frag_quto_list_dtls_contact_dtls)

        tv_updates.setOnClickListener(this)

        if(Pref.IsContactPersonRequiredinQuotation){
            llContactDtlsRoot.visibility = View.VISIBLE
        }else{
            llContactDtlsRoot.visibility = View.GONE
        }

        floating_fab.apply {
            menuIconView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_add))
            menuButtonColorNormal = mContext.resources.getColor(R.color.colorAccent)
            menuButtonColorPressed = mContext.resources.getColor(R.color.colorPrimaryDark)
            menuButtonColorRipple = mContext.resources.getColor(R.color.colorPrimary)

            isIconAnimated = false
            setClosedOnTouchOutside(true)
        }

        getFloatingVal.add("Update")

        getFloatingVal.forEachIndexed { i, value ->
            if (i == 0) {
                programFab1 = FloatingActionButton(activity)
                programFab1?.let {
                    it.buttonSize = FloatingActionButton.SIZE_MINI
                    it.id = 100 + i
                    it.colorNormal = mContext.resources.getColor(R.color.colorPrimaryDark)
                    it.colorPressed = mContext.resources.getColor(R.color.delivery_status_green)
                    it.colorRipple = mContext.resources.getColor(R.color.delivery_status_green)
                    it.labelText = getFloatingVal[0]
                    floating_fab.addMenuButton(it)
                    it.setOnClickListener(this)
                    it.setImageResource(R.drawable.ic_tick_float_icon)
                }
            }
        }
    }

    private fun quotViewListCall(quotId: String) {
        try{
            progress_wheel.spin()
            val repository = GetQuotRegProvider.provideSaveButton()
            BaseActivity.compositeDisposable.add(
                    repository.viewDetailsQuot(quotId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addQuotResult = result as ViewDetailsQuotResponse

                                addQuotEditResult=addQuotResult

                                progress_wheel.stopSpinning()
                                if (addQuotResult!!.status == NetworkConstant.SUCCESS) {
                                    setData(addQuotResult)
                                    if(addQuotResult!!.quotation_product_details_list!!.size>0){
                                        addedProdList.clear()
                                        addedProdList.addAll(addQuotResult!!.quotation_product_details_list!!)
                                        setAdapter()
                                    }

                                }else {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                }
                                BaseActivity.isApiInitiated = false
                            }, { error ->
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false
                                if (error != null) {
                                }
                            })
            )
        }catch (ex:Exception){
            ex.printStackTrace()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
            progress_wheel.stopSpinning()
        }

    }

    private fun docwiseQuotViewListCall(docId: String) {
        try{
            progress_wheel.spin()
            val repository = GetQuotRegProvider.provideSaveButton()
            BaseActivity.compositeDisposable.add(
                    repository.viewDetailsDoc(docId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addQuotResult = result as ViewDetailsQuotResponse

                                addQuotEditResult=addQuotResult

                                progress_wheel.stopSpinning()
                                if (addQuotResult!!.status == NetworkConstant.SUCCESS) {
                                    setData(addQuotResult)
                                    if(addQuotResult!!.quotation_product_details_list!!.size>0){
                                        addedProdList.clear()
                                        addedProdList.addAll(addQuotResult!!.quotation_product_details_list!!)
                                        setAdapter()
                                    }

                                }else {
                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                }
                                BaseActivity.isApiInitiated = false
                            }, { error ->
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false
                                if (error != null) {
                                }
                            })
            )
        }catch (ex:Exception){
            ex.printStackTrace()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
            progress_wheel.stopSpinning()
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun setData(addQuotResult: ViewDetailsQuotResponse) {
        projectNameTv.text = "Project Name: "+addQuotResult.project_name
        shopName.setText("Cust Name: "+addQuotResult.shop_name)
        quotNumber.setText("Quot Number:  "+ addQuotResult.quotation_number)
        phone.setOnClickListener {
            IntentActionable.initiatePhoneCall(mContext, addQuotResult.shop_phone_no)
        }
        try{
            var qutoD=addQuotResult.quotation_date_selection!!.subSequence(0,10).toString()
            var qutoDFormat = AppUtils.getFormatedDateNew(qutoD, "dd-mm-yyyy", "yyyy-mm-dd")
            var qutoDMeredianFormat = AppUtils.convertToSelectedDateReimbursement(qutoDFormat!!)
            //time.setText(AppUtils.convertDateTimeToCommonFormat(addQuotResult.quotation_date_selection!!.subSequence(0,10).toString()).toString())
            time.setText(qutoDMeredianFormat)
        }catch (ex:Exception){
            ex.printStackTrace()
        }

        // 1.0 ViewDetailsQuotFragment  AppV 4.0.6 Terms&condition
        tvTax.setText("Tax                     : "+addQuotResult.taxes)
        tvFreight.setText("Freight              : "+addQuotResult.Freight)
        tvDelTime.setText("Delivery Time : "+addQuotResult.delivery_time)
        tvPayment.setText("Payment          : "+addQuotResult.payment)
        tvvalidity.setText("Validity            : "+addQuotResult.validity)
        tvBilling.setText("Billing              : "+addQuotResult.billing)
        tvProdtolrence.setText("Product Tolerance of Thickness     : "+addQuotResult.product_tolerance_of_thickness)
        tvProdcoattolrence.setText("Tolerance of Coating Thickness   : "+addQuotResult.tolerance_of_coating_thickness)
        tvsalesman.setText("Salesman     : "+addQuotResult.salesman_name)
        tvremarks.setText("Remarks       : "+addQuotResult.Remarks)

//        addQuotResult.sel_quotation_pdf_template = "General template"
//        addQuotResult.quotation_contact_person =  "john owner"
//        addQuotResult.quotation_contact_number = "985247568"

        // 2.0 ViewDetailsQuotFragment  AppV 4.0.6  Contact
        tvTemplate.setText("Template      : "+addQuotResult.sel_quotation_pdf_template)

        var emailCollectionStr = ""
        var nameCollectionStr = ""
        var numberCollectionStr = ""
        var finalNamePhStr = ""
        if(addQuotEditResult.extra_contact_list!!.size>0){
            for(i in 0..addQuotEditResult.extra_contact_list!!.size-1){
                var ob = addQuotEditResult.extra_contact_list!!.get(i)
                emailCollectionStr=emailCollectionStr+ if(ob.quotation_contact_email == null) "" else ob.quotation_contact_email +","
                nameCollectionStr=nameCollectionStr+ ob.quotation_contact_person+","
                numberCollectionStr=numberCollectionStr+ ob.quotation_contact_number+","
                finalNamePhStr = finalNamePhStr + ob.quotation_contact_person+" (Mob.No. ${ob.quotation_contact_number} )/"
            }
            nameCollectionStr =  nameCollectionStr.dropLast(1)
            emailCollectionStr =  emailCollectionStr.dropLast(1)
            numberCollectionStr =  numberCollectionStr.dropLast(1)
            finalNamePhStr =  finalNamePhStr.dropLast(1)
        }else{
            emailCollectionStr = if(addQuotEditResult.shop_email==null) "" else addQuotEditResult.shop_email!!
            nameCollectionStr = addQuotEditResult.shop_owner_name.toString()
            numberCollectionStr = addQuotEditResult.shop_phone_no.toString()
            finalNamePhStr = nameCollectionStr+" (Mob.No. $numberCollectionStr )"
        }

        tvContactP.setText("Contact Person : "+nameCollectionStr)
        tvContactPhone.setText("Contact Ph : "+numberCollectionStr)
        tvContactEmail.setText("Contact Email : "+emailCollectionStr)

    }

    private fun setAdapter() {
        showAddedProdAdapter=ShowAddedProductAdapter(mContext,addedProdList,object :ShowAddedProductAdapter.OnClickListener{
            override fun onEditCLick(obj: quotation_product_details_list) {
                editRate(obj)
            }
        })
        rv_addedProduct.adapter=showAddedProdAdapter
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            100 -> {
                floating_fab.close(true)
                programFab1?.colorNormal = mContext.resources.getColor(R.color.delivery_status_green)
                programFab1?.setImageResource(R.drawable.ic_tick_float_icon)
                CustomStatic.IsNewQuotEdit=true
                showAddedProdAdapter!!.notifyDataSetChanged()
            }
            R.id.update_TV_frag_view_details_quot_list ->{
                editQuot()
            }
        }
    }

    private fun editQuot() {
        addQuotData.updated_by_user_id = Pref.user_id
        addQuotData.updated_date_time = AppUtils.getCurrentDateTime()
        addQuotData.quotation_number = addQuotEditResult.quotation_number
        addQuotData.quotation_date_selection = addQuotEditResult.quotation_date_selection
        addQuotData.project_name = addQuotEditResult.project_name
        addQuotData.shop_id = addQuotEditResult.shop_id
        addQuotData.taxes = addQuotEditResult.taxes
        addQuotData.Freight = addQuotEditResult.Freight
        addQuotData.delivery_time = addQuotEditResult.delivery_time
        addQuotData.payment = addQuotEditResult.payment
        addQuotData.validity = addQuotEditResult.validity
        addQuotData.billing = addQuotEditResult.billing
        addQuotData.product_tolerance_of_thickness = addQuotEditResult.product_tolerance_of_thickness
        addQuotData.tolerance_of_coating_thicknes = addQuotEditResult.tolerance_of_coating_thickness
        addQuotData.salesman_user_id = addQuotEditResult.salesman_user_id
        addQuotData.quotation_updated_lat = Pref.latitude.toString()
        addQuotData.quotation_updated_long = Pref.longitude.toString()
        addQuotData.quotation_updated_address = LocationWizard.getAdressFromLatlng(mContext, Pref.latitude!!.toDouble(), Pref.longitude!!.toDouble())

        addQuotData.product_list = ArrayList()
        addQuotData.product_list = addQuotEditResult.quotation_product_details_list!!

        editQuotButtoncall(addQuotData)

    }

    private fun editQuotButtoncall(addQuot: EditQuotRequestData) {
        try{
            BaseActivity.isApiInitiated = true
            progress_wheel.spin()
            val repository = GetQuotRegProvider.provideSaveButton()
            BaseActivity.compositeDisposable.add(
                    repository.editQuot(addQuot)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as BaseResponse
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    (mContext as DashboardActivity).showSnackMessage("Quotation updated successfully.")

                                    Handler().postDelayed(Runnable {
                                        (mContext as DashboardActivity).onBackPressed()
                                    }, 1200)

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




    private fun editRate(obj: quotation_product_details_list){
        var simpleDialog1 = Dialog(mContext)
        simpleDialog1.setCancelable(true)
        simpleDialog1.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog1.setContentView(R.layout.dialog_new_quot_rate_edit)

        val productName = simpleDialog1.findViewById(R.id.tv_row_new_quot_added_prod_name) as TextView
        val colorName = simpleDialog1.findViewById(R.id.tv_row_new_quot_added_prod_color) as TextView
        val rate_sqft = simpleDialog1.findViewById(R.id.et_row_new_quot_added_prod_rate_sqft) as EditText
        val rate_sqmtr = simpleDialog1.findViewById(R.id.et_row_new_quot_added_prod_rate_sqmtr) as EditText
        val tv_update = simpleDialog1.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
        val tv_cancel = simpleDialog1.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

        tv_cancel.setOnClickListener({ view ->
            simpleDialog1.cancel()
        })
        tv_update.setOnClickListener({ view ->
            for(i in 0..addedProdList.size-1){
                if(obj.product_id == addedProdList.get(i).product_id && obj.color_id == addedProdList.get(i).color_id){
                    addedProdList.get(i).rate_sqft=rate_sqft.text.toString()
                    addedProdList.get(i).rate_sqmtr=rate_sqmtr.text.toString()

                    addedProdList.get(i).qty = 0
                    addedProdList.get(i).amount = 0.0

                    addQuotEditResult.quotation_product_details_list=addedProdList!!
                    showAddedProdAdapter!!.notifyDataSetChanged()
                    simpleDialog1.cancel()
                    break
                }
            }
        })

        productName.text = obj.product_name
        colorName.text = "Color : "+obj.color_name

        if(CustomStatic.IsNewQuotEdit){
            tv_updates.visibility=View.VISIBLE
        }else{
            tv_updates.visibility=View.GONE
        }

        simpleDialog1.show()



        rate_sqft.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!=null && s!="" && s.toString().length>0){
                    var value=s.toString().toDouble()* Pref.SqMtrRateCalculationforQuotEuro.toDouble()
                    rate_sqmtr.setText(value.toString())
                }else{
                    rate_sqmtr.setText("")
                }
            }
        })

    }

}