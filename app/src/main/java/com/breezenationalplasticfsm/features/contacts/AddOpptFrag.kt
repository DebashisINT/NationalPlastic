package com.breezenationalplasticfsm.features.contacts

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.app.domain.OpportunityAddEntity
import com.breezenationalplasticfsm.app.domain.OpportunityProductEntity
import com.breezenationalplasticfsm.app.domain.OpportunityStatusEntity
import com.breezenationalplasticfsm.app.uiaction.IntentActionable
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.CustomSpecialTextWatcher1
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.commondialog.presentation.CommonDialog
import com.breezenationalplasticfsm.features.commondialog.presentation.CommonDialogClickListener
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.login.api.opportunity.OpportunityRepoProvider
import com.breezenationalplasticfsm.features.login.api.productlistapi.ProductListRepoProvider
import com.breezenationalplasticfsm.features.login.model.opportunitymodel.OpportunityStatusListResponseModel
import com.breezenationalplasticfsm.features.login.model.productlistmodel.ProductListResponseModel
import com.breezenationalplasticfsm.features.orderITC.SyncEditOppt
import com.breezenationalplasticfsm.features.orderITC.SyncOppt
import com.breezenationalplasticfsm.features.orderITC.SyncOpptProductL
import com.breezenationalplasticfsm.widgets.AppCustomEditText
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.util.Locale
import kotlin.collections.ArrayList

class AddOpptFrag : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    lateinit var shopObj : AddShopDBModelEntity
    private lateinit var tv_add_frag_oppt_crm_name: TextView
    private lateinit var tv_add_frag_oppt_crm_addr: TextView
    private lateinit var tv_add_frag_oppt_crm_ph: TextView
    private lateinit var et_add_oppt_frag_add_desc: TextInputEditText
    private lateinit var et_add_oppt_frag_add_estamt: TextInputEditText
    private lateinit var tv_add_oppt_frag_add_product: TextInputEditText
    private lateinit var tv_add_oppt_frag_add_status: TextInputEditText
    private lateinit var ll_add_frag_oppt_crm_addr_root: LinearLayout
    private lateinit var ll_add_frag_oppt_crm_ph_root: LinearLayout
    private lateinit var cv_add_oppt_frag_add_submit: CardView
    private lateinit var iv_add_oppt_frag_add_voice1: ImageView
    private lateinit var iv_frag_oppt_add_prdct: LinearLayout
    private lateinit var iv_frag_oppt_add_status: LinearLayout
    private lateinit var til_oppt_description: TextInputLayout
    private lateinit var til_oppt_product: TextInputLayout
    private lateinit var til_oppt_amount: TextInputLayout
    private lateinit var cv_add_frag_oppt_cntct: CardView
    private lateinit var til_oppt_status: TextInputLayout
    private lateinit var iv_add_frag_addr: ImageView
    private lateinit var iv_add_frag_cntct: ImageView
    private lateinit var cv_add_frag_oppt_address: CardView
    private lateinit var progress_wheel_add_oppt_frag_add_oppt: ProgressWheel
    private lateinit var adapterOptProductName:AdapterOptProductName
    var activity: OpportunityAddEntity = OpportunityAddEntity()
    private var str_statusID:String = ""
    private var str_statusName:String = ""
    var productNameText:String =""
    var idProduct:String =""
    private var productL : ArrayList<ProductDtls> = ArrayList()
    var finalSubmittingproductL : ArrayList<ProductDtls> = ArrayList()
    var isValueInserted :Boolean =false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_oppt, container, false)
        initView(view)
        return view
    }

    companion object{
        var editOprtntyID: String = ""
        var shop_id: String = ""
        fun getInstance(objects: Any): AddOpptFrag {
            val objFragment = AddOpptFrag()

            var obj = objects as String
            shop_id=obj.split(",").get(0)
            editOprtntyID=obj.split(",").get(1)
            println("shop_id+editOprtntyID"+obj)
            println("shop_id"+shop_id)
            println("editOprtntyID"+editOprtntyID)
            return objFragment
        }
    }

    private fun initView(view: View) {
        try {
            productL = AppDatabase.getDBInstance()?.productListDao()?.getProducts() as ArrayList<ProductDtls>
        }catch (ex:Exception){
            ex.printStackTrace()
        }

        shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shop_id)

        tv_add_frag_oppt_crm_name = view.findViewById(R.id.tv_add_frag_oppt_crm_name)
        ll_add_frag_oppt_crm_addr_root = view.findViewById(R.id.ll_add_frag_oppt_crm_addr_root)
        tv_add_frag_oppt_crm_addr = view.findViewById(R.id.tv_add_frag_oppt_crm_addr)
        ll_add_frag_oppt_crm_ph_root = view.findViewById(R.id.ll_add_frag_oppt_crm_ph_root)
        tv_add_frag_oppt_crm_ph = view.findViewById(R.id.tv_add_frag_oppt_crm_ph)
        et_add_oppt_frag_add_desc = view.findViewById(R.id.et_add_oppt_frag_add_desc)
        iv_add_oppt_frag_add_voice1 = view.findViewById(R.id.iv_add_oppt_frag_add_voice1)
        tv_add_oppt_frag_add_product = view.findViewById(R.id.tv_add_oppt_frag_add_product)
        et_add_oppt_frag_add_estamt = view.findViewById(R.id.et_add_oppt_frag_add_estamt)
        tv_add_oppt_frag_add_status = view.findViewById(R.id.tv_add_oppt_frag_add_status)
        progress_wheel_add_oppt_frag_add_oppt = view.findViewById(R.id.progress_wheel_add_oppt_frag_add_oppt)
        cv_add_oppt_frag_add_submit = view.findViewById(R.id.cv_add_oppt_frag_add_submit)

        til_oppt_description = view.findViewById(R.id.til_oppt_description)
        til_oppt_product = view.findViewById(R.id.til_oppt_product)
        til_oppt_amount = view.findViewById(R.id.til_oppt_amount)
        til_oppt_status = view.findViewById(R.id.til_oppt_status)
        iv_frag_oppt_add_prdct = view.findViewById(R.id.iv_frag_oppt_add_prdct)
        iv_frag_oppt_add_status = view.findViewById(R.id.iv_frag_oppt_add_status)

        cv_add_frag_oppt_cntct = view.findViewById(R.id.cv_add_frag_oppt_cntct)
        cv_add_frag_oppt_address = view.findViewById(R.id.cv_add_frag_oppt_address)
        iv_add_frag_cntct = view.findViewById(R.id.iv_add_frag_cntct)
        iv_add_frag_addr = view.findViewById(R.id.iv_add_frag_addr)

        Glide.with(mContext)
            .load(R.drawable.animationcall)
            .into(iv_add_frag_cntct)

        Glide.with(mContext)
            .load(R.drawable.animationaddress)
            .into(iv_add_frag_addr)

        iv_add_oppt_frag_add_voice1.setOnClickListener(this)
        tv_add_oppt_frag_add_product.setOnClickListener(this)
        tv_add_oppt_frag_add_status.setOnClickListener(this)
        cv_add_oppt_frag_add_submit.setOnClickListener(this)
        iv_frag_oppt_add_prdct.setOnClickListener(this)
        iv_frag_oppt_add_status.setOnClickListener(this)

        til_oppt_description.setOnClickListener(this)
        til_oppt_product.setOnClickListener(this)
        til_oppt_amount.setOnClickListener(this)
        til_oppt_status.setOnClickListener(this)
       // ll_add_frag_oppt_crm_addr_root.setOnClickListener(this)
        cv_add_frag_oppt_cntct.setOnClickListener(this)
        cv_add_frag_oppt_address.setOnClickListener(this)

        try {
            tv_add_frag_oppt_crm_name.text = shopObj.shopName
            if(shopObj.address.equals("NA")){
                ll_add_frag_oppt_crm_addr_root.visibility = View.VISIBLE
                tv_add_frag_oppt_crm_addr.text = "No address found"

            }else{
                ll_add_frag_oppt_crm_addr_root.visibility = View.VISIBLE
                tv_add_frag_oppt_crm_addr.text = shopObj.address
            }
            tv_add_frag_oppt_crm_ph.text = shopObj.ownerContactNumber
        } catch (e: Exception) {
            e.printStackTrace()
        }

        cv_add_frag_oppt_cntct.setOnClickListener {
            IntentActionable.initiatePhoneCall(mContext, shopObj.ownerContactNumber)
        }

        cv_add_frag_oppt_address.setOnClickListener {
            if (Pref.ContactAddresswithGeofence) {
                try {
                    if (shopObj.shopLat != 0.0 && shopObj.shopLong != 0.0) {
                        var intentGmap: Intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=${shopObj.shopLat},${shopObj.shopLong}&mode=1")
                        )
                        intentGmap.setPackage("com.google.android.apps.maps")
                        if (intentGmap.resolveActivity(mContext.packageManager) != null) {
                            mContext.startActivity(intentGmap)
                        }
                    }else{

                        cv_add_frag_oppt_address.isEnabled = false
                        val simpleDialog = Dialog(mContext)
                        simpleDialog.setCancelable(true)
                        simpleDialog.setCanceledOnTouchOutside(false)
                        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        simpleDialog.setContentView(R.layout.dialog_yes_no_3)
                        val dialogHeader = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                        dialogHeader.text = "GPS co-ordinates not found. Unable to navigate the map."
                        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                        dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
                        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                        dialogYes.setOnClickListener({ view ->
                            simpleDialog.cancel()
                            cv_add_frag_oppt_address.isEnabled = true
                        })
                        simpleDialog.setOnDismissListener {
                            cv_add_frag_oppt_address.isEnabled = true
                        }
                        simpleDialog.show()

                        //Toast.makeText(mContext, "GPS co-ordinates not found. Unable to navigate the map.", Toast.LENGTH_SHORT).show()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

        et_add_oppt_frag_add_estamt.clearFocus()
        et_add_oppt_frag_add_estamt.setOnFocusChangeListener(object :View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus){
                    try{
                        var prevRateText = et_add_oppt_frag_add_estamt.text.toString()
                        if(prevRateText.toDouble() == 0.0){
                            et_add_oppt_frag_add_estamt.setText("")
                        }
                    }catch (ex:Exception){
                        ex.printStackTrace()
                    }

                    et_add_oppt_frag_add_estamt.addTextChangedListener(
                        CustomSpecialTextWatcher1(et_add_oppt_frag_add_estamt, 12, 2, object : CustomSpecialTextWatcher1.GetCustomTextChangeListener {
                            override fun beforeTextChange(text: String) {

                            }

                            override fun customTextChange(text: String) {

                            }
                        })
                    )
                }else{
                    AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                }
            }
        })



        if(!editOprtntyID.equals("")){
            (mContext as DashboardActivity).setTopBarTitle("Edit Opportunity")
            progress_wheel_add_oppt_frag_add_oppt.spin()
            Handler().postDelayed(Runnable {
                setEditData(editOprtntyID)
            }, 1500)
        }else{
            (mContext as DashboardActivity).setTopBarTitle("Add Opportunity")
        }

        if (editOprtntyID.equals("")) {
            et_add_oppt_frag_add_desc.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().length != 0) {
                        isValueInserted = true
                    }
                }
            })
        }

        if (editOprtntyID.equals("")) {
            et_add_oppt_frag_add_desc.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().length != 0) {
                        isValueInserted = true
                    }
                }
            })
        }
    }

    private fun setEditData(editOprtntyID: String) {

        progress_wheel_add_oppt_frag_add_oppt.stopSpinning()


        var oprtntyObj =  AppDatabase.getDBInstance()!!.opportunityAddDao().getSingleOpportunityL(editOprtntyID)
        var oprtntyProductObj =  AppDatabase.getDBInstance()!!.opportunityProductDao().getOpportunityPrdctL(oprtntyObj.opportunity_id)
        et_add_oppt_frag_add_desc.setText(oprtntyObj.opportunity_description)
        productNameText = ""

        for (i in 0..oprtntyProductObj!!.size-1) {
            if (!oprtntyProductObj.get(i).product_name.equals("")) {
                productNameText = productNameText + oprtntyProductObj.get(i).product_name+","
                finalSubmittingproductL.add(ProductDtls(oprtntyProductObj.get(i).product_id , oprtntyProductObj.get(i).product_name , true))
            } else {
                tv_add_oppt_frag_add_product.setText("")
                tv_add_oppt_frag_add_product.isEnabled = true
               // finalSubmittingproductL.add(ProductDtls("0" , "" , true))
            }
        }
        if (productNameText.endsWith(",")) {
            productNameText = productNameText.substring(0, productNameText.length - 1);
        }
        tv_add_oppt_frag_add_product.setText(productNameText)
        tv_add_oppt_frag_add_product.isEnabled = true

        if (!oprtntyObj.opportunity_amount.equals("")) {
            et_add_oppt_frag_add_estamt.setText(oprtntyObj.opportunity_amount)
        }else{
            et_add_oppt_frag_add_estamt.setText("")
        }
        tv_add_oppt_frag_add_status.setText(oprtntyObj.opportunity_status_name)

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_add_oppt_frag_add_voice1 -> {
                try {
                    progress_wheel_add_oppt_frag_add_oppt.spin()
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
                            progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                        }, 3000)

                    } catch (a: ActivityNotFoundException) {
                        a.printStackTrace()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            R.id.tv_add_oppt_frag_add_product ,  R.id.til_oppt_product  , R.id.iv_frag_oppt_add_prdct   -> {
                productDialog()
                return


                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                try {
                    tv_add_oppt_frag_add_product.isEnabled =false
                    til_oppt_product.isEnabled =false
                    iv_frag_oppt_add_prdct.isEnabled =false
                    val productDialog = Dialog(mContext)
                    productDialog.setCancelable(true)

                    productDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    productDialog.setContentView(R.layout.dialog_cont_select)
                    val rvProductL = productDialog.findViewById(R.id.rv_dialog_cont_list) as RecyclerView
                    val tvHeader = productDialog.findViewById(R.id.tv_dialog_cont_sel_header) as TextView
                    val submit = productDialog.findViewById(R.id.tv_dialog_cont_list_submit) as TextView
                    val et_productNameSearch = productDialog.findViewById(R.id.et_dialog_contact_search) as AppCustomEditText
                   // val cb_selectAll = productDialog.findViewById(R.id.cb_dialog_cont_select_all) as CheckBox
                    val iv_close = productDialog.findViewById(R.id.iv_dialog_generic_list_close_icon) as ImageView
                    tvHeader.text = "Selected Product(s)"


                    iv_close.setOnClickListener {
                        productDialog.dismiss()
                        progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                        tv_add_oppt_frag_add_product.isEnabled =true
                        til_oppt_product.isEnabled =true
                        iv_frag_oppt_add_prdct.isEnabled =true
                    }
                    var productTickL : ArrayList<ProductDtls> = ArrayList()
                    if (editOprtntyID !="" && finalSubmittingproductL.size==0){
                        var oprtntyObj =  AppDatabase.getDBInstance()!!.opportunityAddDao().getSingleOpportunityL(editOprtntyID)
                        var oprtntyProductObj =  AppDatabase.getDBInstance()!!.opportunityProductDao().getOpportunityPrdctL(oprtntyObj.opportunity_id)
                        var selPrdctIdL = oprtntyProductObj.map { it.product_id }
                        println("selPrdctIdL"+selPrdctIdL.size)
                        productL= AppDatabase.getDBInstance()!!.productListDao().getSelectedProductIdList(selPrdctIdL) as ArrayList<ProductDtls>
                        println("pujaa1110"+productL.size)
                        tvHeader.text = "Selected Product(s) : (${selPrdctIdL.size})"
                        productTickL= AppDatabase.getDBInstance()!!.opportunityProductDao().getOpportunitySelectedPrdctL(shop_id,editOprtntyID) as ArrayList<ProductDtls>
                    }else{
                        productTickL =  finalSubmittingproductL.clone() as ArrayList<ProductDtls>
                    }


                        println("productLsize"+productL.size)
                        //tvHeader.text = "Selected Product(s): ${productL.size}"
                        tvHeader.text = "Selected Product(s): "
                        adapterOptProductName = AdapterOptProductName(mContext,productL,object :AdapterOptProductName.onClick{
                            override fun onTickUntick(obj: ProductDtls, isTick: Boolean) {
                                if(isTick){
                                    productTickL.add(obj)
                                    productL.filter { it.product_name.equals(obj.product_name) }.first().isTick = true
                                    tvHeader.text = "Selected Product(s) : (${productTickL.size})"
                                }else{
                                    productTickL.removeIf{it.product_id.equals(obj.product_id)}
                                    productL.filter { it.product_name.equals(obj.product_name) }.first().isTick = false
                                    tvHeader.text = "Selected Product(s) : (${productTickL.size})"

                                }
                            }
                        },{
                            it
                        })

                    /*cb_selectAll.setOnCheckedChangeListener { compoundButton, b ->

                        if (compoundButton.isChecked) {
                            adapterOptProductName.selectAll()
                            cb_selectAll.setText("Deselect All")
                        }
                        else{

                            adapterOptProductName.deselectAll()
                            cb_selectAll.setText("Select All")

                        }
                    }*/

                    et_productNameSearch.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(p0: Editable?) {
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            adapterOptProductName!!.getFilter().filter(et_productNameSearch.text.toString().trim())
                        }
                    })


                    submit.setOnClickListener {
                       /* if ( productTickL.size == 0){
                            Toast.makeText(mContext, "Please select atleast one product", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }*/
                        tv_add_oppt_frag_add_product.isEnabled =true
                        til_oppt_product.isEnabled =true
                        iv_frag_oppt_add_prdct.isEnabled =true

                        if (productTickL.size > 0) {
                            isValueInserted = true
                            productDialog.dismiss()
                            finalSubmittingproductL.clear()
                            finalSubmittingproductL = productTickL.clone() as ArrayList<ProductDtls>
                            var a = finalSubmittingproductL
                            productNameText = ""
                            idProduct = ""
                            if(productTickL.size==1){
                                productNameText = productTickL.get(0).product_name
                                idProduct = productTickL.get(0).product_id
                            }else{
                                for(i in 0..productTickL.size-1) {
                                    productNameText = productNameText+productTickL.get(i).product_name+","
                                    idProduct = idProduct+productTickL.get(i).product_id+","
                                }
                            }
                            if (productNameText.endsWith(",")) {
                                productNameText = productNameText.substring(0, productNameText.length - 1);
                            }
                            if (idProduct.endsWith(",")) {
                                idProduct = idProduct.substring(0, idProduct.length - 1);
                            }
                            tv_add_oppt_frag_add_product.setText(productNameText)
                            tv_add_oppt_frag_add_product.isEnabled = true
                        }
                        else{
                            productDialog.dismiss()
                            tv_add_oppt_frag_add_product.setText("")
                            tv_add_oppt_frag_add_product.isEnabled = true
                        }
                    }

                    rvProductL.adapter = adapterOptProductName
                    productDialog.show()
                    tv_add_oppt_frag_add_product.isEnabled =false
                    til_oppt_product.isEnabled =false
                    iv_frag_oppt_add_prdct.isEnabled =false


                    productDialog.setOnDismissListener {
                        tv_add_oppt_frag_add_product.isEnabled =true
                        til_oppt_product.isEnabled =true
                        iv_frag_oppt_add_prdct.isEnabled =true
                    }

                    }
                    catch (a: ActivityNotFoundException) {
                        a.printStackTrace()
                        println("errorblock"+a.printStackTrace())
                    }

            }

            R.id.tv_add_oppt_frag_add_status , R.id.til_oppt_status ,  R.id.iv_frag_oppt_add_status -> {
                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                var crmStatusOpptList = AppDatabase.getDBInstance()?.opportunityStatusDao()?.getAll() as ArrayList<OpportunityStatusEntity>
                println("crmStatusOpptList"+crmStatusOpptList.size)

                tv_add_oppt_frag_add_status.isEnabled =false
                til_oppt_status.isEnabled =false
                iv_frag_oppt_add_status.isEnabled =false
                if(crmStatusOpptList.size>0){
                    var genericL : ArrayList<CustomData> = ArrayList()
                    for(i in 0..crmStatusOpptList.size-1){
                        genericL.add(CustomData(crmStatusOpptList.get(i).status_id.toString(),crmStatusOpptList.get(i).status_name.toString()))
                    }
                  /*  GenericDialog.newInstance("Status",genericL as ArrayList<CustomData>){
                        isValueInserted = true
                        str_statusID = it.id
                        str_statusName = it.name
                        tv_add_oppt_frag_add_status.setText(it.name)
                        tv_add_oppt_frag_add_status.isEnabled =true
                        til_oppt_status.isEnabled =true
                        iv_frag_oppt_add_status.isEnabled =true
                    }.show((mContext as DashboardActivity).supportFragmentManager, "")*/

                    GenericDialog1.newInstance("Status",genericL as ArrayList<CustomData> , {
                        isValueInserted = true
                        str_statusID = it.id
                        str_statusName = it.name
                        tv_add_oppt_frag_add_status.setText(it.name)
                        tv_add_oppt_frag_add_status.isEnabled =true
                        til_oppt_status.isEnabled =true
                        iv_frag_oppt_add_status.isEnabled =true
                    } , {
                        //Toast.makeText(mContext, "Cancel", Toast.LENGTH_SHORT).show()
                        tv_add_oppt_frag_add_status.isEnabled =true
                        til_oppt_status.isEnabled =true
                        iv_frag_oppt_add_status.isEnabled =true

                    }).show((mContext as DashboardActivity).supportFragmentManager, "")

                    tv_add_oppt_frag_add_status.isEnabled =false
                    til_oppt_status.isEnabled =false
                    iv_frag_oppt_add_status.isEnabled =false



                }else{
                    Toaster.msgShort(mContext, "No Status Found")
                }
            }

            R.id.cv_add_oppt_frag_add_submit -> {
                cv_add_oppt_frag_add_submit.isEnabled = false
                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                if(editOprtntyID.equals("")){
                    submitValidationCheck(shop_id)
                }else{
                    editValidationCheck(editOprtntyID)
                }
                /*Handler().postDelayed(Runnable {
                    cv_add_oppt_frag_add_submit.isEnabled = true
                },1000)*/
            }
        }
    }

    private fun editValidationCheck(editOprtntyID: String) {

        if(et_add_oppt_frag_add_desc.text.toString().length==0 || et_add_oppt_frag_add_desc.text.toString().trim().equals("")){
            Toast.makeText(mContext, "Please enter description", Toast.LENGTH_SHORT).show()
            progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
            cv_add_oppt_frag_add_submit.isEnabled = true
            return
        }
        if(tv_add_oppt_frag_add_status.text.toString().trim().length == 0){
            Toast.makeText(mContext, "Please enter status", Toast.LENGTH_SHORT).show()
            progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
            cv_add_oppt_frag_add_submit.isEnabled = true
            return
        }
        else{

            showCheckAlert(
                "Opportunity Confirmation",
                "Would you like to save the opportunity?",
                editOprtntyID,
                Companion.editOprtntyID
            )

        }

    }

    private fun submitValidationCheck(shop_id: String) {

        if(et_add_oppt_frag_add_desc.text.toString().length==0 || et_add_oppt_frag_add_desc.text.toString().trim().equals("")){
            Toast.makeText(mContext, "Please enter description", Toast.LENGTH_SHORT).show()
            progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
            cv_add_oppt_frag_add_submit.isEnabled = true
            return
        }
        if(tv_add_oppt_frag_add_status.text.toString().trim().length == 0){
            Toast.makeText(mContext, "Please enter status", Toast.LENGTH_SHORT).show()
            progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
            cv_add_oppt_frag_add_submit.isEnabled = true
            return
        }
        if(et_add_oppt_frag_add_estamt.text.toString().equals("0")){
            Toast.makeText(mContext, "Please enter proper amount", Toast.LENGTH_SHORT).show()
            progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
            cv_add_oppt_frag_add_submit.isEnabled = true
            return
        }
        else{

            showCheckAlert(
                "Opportunity Confirmation",
                "Would you like to save the opportunity?",
                editOprtntyID,shop_id
            )

        }
    }

    private fun showCheckAlert(
        header: String,
        title: String,
        editOprtntyID: String,
        shop_id: String
    ) {
        CommonDialog.getInstance(header, title, getString(R.string.no), getString(R.string.yes), false, object :
            CommonDialogClickListener {
            override fun onLeftClick() {
                progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                isValueInserted =true
                cv_add_oppt_frag_add_submit.isEnabled = true
            }
            override fun onRightClick(editableData: String) {
                Timber.d("Order onRightClick ${AppUtils.getCurrentDateTime()}")
                if (editOprtntyID.equals("")) {
                    isValueInserted =false
                    saveOpportunity(shop_id)
                }
                else{
                    isValueInserted =false
                    editedSaveOpportunity(editOprtntyID)
                }
            }


        }).show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private fun editedSaveOpportunity(editOprtntyID: String) {
        progress_wheel_add_oppt_frag_add_oppt.spin()
        var editedOpportunityL = OpportunityAddEntity()
        editedOpportunityL = AppDatabase.getDBInstance()!!.opportunityAddDao().getOpportunityByIdN(editOprtntyID)

        editedOpportunityL.shop_id = shop_id
        editedOpportunityL.shop_name = shopObj.shopName
        editedOpportunityL.shop_type = shopObj.type
        editedOpportunityL.opportunity_id = editOprtntyID
        editedOpportunityL.opportunity_description = et_add_oppt_frag_add_desc.text.toString().trim()

        editedOpportunityL.opportunity_amount = et_add_oppt_frag_add_estamt.text.toString().trim()
        if (str_statusID.equals("")){
            editedOpportunityL.opportunity_status_id = editedOpportunityL.opportunity_status_id
        }else{
            editedOpportunityL.opportunity_status_id = str_statusID
        }
        if (str_statusName.equals("")){
            editedOpportunityL.opportunity_status_name = editedOpportunityL.opportunity_status_name
        }else{
            editedOpportunityL.opportunity_status_name = str_statusName
        }
        editedOpportunityL.opportunity_created_date = editedOpportunityL.opportunity_created_date
        editedOpportunityL.opportunity_created_time = editedOpportunityL.opportunity_created_time
        editedOpportunityL.opportunity_created_date_time = editedOpportunityL.opportunity_created_date_time
        editedOpportunityL.opportunity_edited_date_time = AppUtils.getCurrentDateTimeNew()
        editedOpportunityL.isUpload = editedOpportunityL.isUpload
        if (editedOpportunityL.isUpload) {
            editedOpportunityL.isEdited = true
        }else{
            editedOpportunityL.isEdited = false
        }
        editedOpportunityL.isDeleted = false


        //if(finalSubmittingproductL.size != 0){
            AppDatabase.getDBInstance()!!.opportunityProductDao().deleteOprtntyById(editOprtntyID)
       // }

        if (finalSubmittingproductL.size>0) {
            for (l in 0..finalSubmittingproductL.size - 1) {
                var obj: OpportunityProductEntity = OpportunityProductEntity()
                obj.shop_id = shop_id
                obj.opportunity_id = editOprtntyID
                obj.product_id = finalSubmittingproductL.get(l).product_id
                obj.product_name = finalSubmittingproductL.get(l).product_name
                AppDatabase.getDBInstance()!!.opportunityProductDao().insertAll(obj)
            }
        }
        else{
            var obj: OpportunityProductEntity = OpportunityProductEntity()
            obj.shop_id = shop_id
            obj.opportunity_id = editOprtntyID
            obj.product_id = "0"
            obj.product_name = ""
            AppDatabase.getDBInstance()!!.opportunityProductDao().insertAll(obj)
        }

        AppDatabase.getDBInstance()!!.opportunityAddDao().updateIsEdited(editedOpportunityL.opportunity_description,
            editedOpportunityL.opportunity_amount,editedOpportunityL.opportunity_status_id,editedOpportunityL.opportunity_status_name,
            editedOpportunityL.opportunity_created_date,editedOpportunityL.opportunity_created_time,editedOpportunityL.opportunity_created_date_time,
            editedOpportunityL.opportunity_edited_date_time,editedOpportunityL.isUpload,false,true, editOprtntyID)

        progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
        cv_add_oppt_frag_add_submit.isEnabled = false
       /* if (!AppUtils.isOnline(mContext)){

        }
        else{
            showSucessDialog()
        }*/

        if (AppUtils.isOnline(mContext) && editedOpportunityL.isUpload ){
            editSyncOppt(shop_id , editOprtntyID)
        }
        else{

            showSucessDialog_edit()
        }
    }

    private fun editSyncOppt(shop_id_: String, editOprtntyID: String) {
        try {
            cv_add_oppt_frag_add_submit.isEnabled = false
            til_oppt_status.isEnabled = false
            tv_add_oppt_frag_add_status.isEnabled = false
            iv_frag_oppt_add_status.isEnabled = false
            progress_wheel_add_oppt_frag_add_oppt.spin()
            var opprDtls = AppDatabase.getDBInstance()!!.opportunityAddDao().getSingleOpportunityL(editOprtntyID)
            var opptProductL = AppDatabase.getDBInstance()!!.opportunityProductDao().getOpportunityPrdctL(editOprtntyID) as ArrayList<OpportunityProductEntity>

            var syncObj : SyncEditOppt = SyncEditOppt()
            syncObj.user_id = Pref.user_id.toString()
            syncObj.session_token = Pref.session_token.toString()
            syncObj.shop_id = opprDtls.shop_id
            syncObj.shop_name = opprDtls.shop_name
            syncObj.shop_type = opprDtls.shop_type
            syncObj.opportunity_id = opprDtls.opportunity_id
            syncObj.opportunity_description = opprDtls.opportunity_description
            if (opprDtls.opportunity_amount.equals("")) {
                syncObj.opportunity_amount = "0"
            }else {
                syncObj.opportunity_amount = opprDtls.opportunity_amount
            }
            syncObj.opportunity_status_id = opprDtls.opportunity_status_id
            syncObj.opportunity_status_name = opprDtls.opportunity_status_name
            syncObj.opportunity_created_date = opprDtls.opportunity_created_date
            syncObj.opportunity_created_time = opprDtls.opportunity_created_time
            syncObj.opportunity_created_date_time = opprDtls.opportunity_created_date_time
            syncObj.opportunity_edited_date_time = opprDtls.opportunity_edited_date_time
            if (opptProductL.size>0){
                for (l in 0..opptProductL.size - 1) {
                    var obj : SyncOpptProductL = SyncOpptProductL()
                    obj.opportunity_id = opprDtls.opportunity_id
                    obj.shop_id = opprDtls.shop_id
                    obj.product_id = opptProductL.get(l).product_id
                    obj.product_name = opptProductL.get(l).product_name
                    syncObj.edit_opportunity_product_list.add(obj)
                }
            }else{
                    var obj: SyncOpptProductL = SyncOpptProductL()
                    obj.shop_id = shop_id
                    obj.opportunity_id = editOprtntyID
                    obj.product_id = "0"
                    obj.product_name = ""
                    syncObj.edit_opportunity_product_list.add(obj)
            }


            val repository = OpportunityRepoProvider.opportunityListRepo()
            BaseActivity.compositeDisposable.add(
                repository.editOpportunity(syncObj)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                        val response = result as BaseResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            AppDatabase.getDBInstance()!!.opportunityAddDao().updateIsEditUploaded(true,syncObj.opportunity_id)
                            showSucessDialog_edit()
                        } else {
                            progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        }
                    }, { error ->
                        println("erroreditsync"+error.message)
                        progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )

        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun showSucessDialog_edit() {

        isValueInserted =false
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.setCanceledOnTouchOutside(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_yes_no_3)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
        dialogHeader.text = mContext.applicationContext.getString(R.string.opt_updt)
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            isValueInserted =false
            simpleDialog.cancel()
            voiceMsg("Opportunity updated successfully")
            Toaster.msgShort(mContext,"Opportunity updated successfully")
            editOprtntyID = ""
            cv_add_oppt_frag_add_submit.isEnabled = true
            til_oppt_status.isEnabled = true
            tv_add_oppt_frag_add_status.isEnabled = true
            iv_frag_oppt_add_status.isEnabled = true
            (mContext as DashboardActivity).onBackPressed()

        })
        simpleDialog.show()
    }

    private fun saveOpportunity(shop_id_: String) {

        progress_wheel_add_oppt_frag_add_oppt.spin()
        activity = OpportunityAddEntity()
        AppDatabase.getDBInstance()?.opportunityAddDao()?.insertAll(activity.apply {
            shop_id = shop_id_
            shop_name = shopObj.shopName
            shop_type = shopObj.type
            opportunity_id = Pref.user_id + "_oppt_" + System.currentTimeMillis()
            opportunity_description = et_add_oppt_frag_add_desc.text.toString().trim()
            opportunity_amount = et_add_oppt_frag_add_estamt.text.toString().trim()
            opportunity_status_id = str_statusID
            opportunity_status_name = str_statusName
            opportunity_created_date = AppUtils.getCurrentDateForShopActi()
            opportunity_created_time = AppUtils.getCurrentTime()
            opportunity_created_date_time = AppUtils.getCurrentDateTimeNew()
            opportunity_edited_date_time = ""
            isUpload = false
            isEdited = false
            isDeleted = false

            if (finalSubmittingproductL.size>0) {
                for (l in 0..finalSubmittingproductL.size - 1) {
                    var obj: OpportunityProductEntity = OpportunityProductEntity()
                    obj.shop_id = shop_id_
                    obj.opportunity_id = opportunity_id
                    obj.product_id = finalSubmittingproductL.get(l).product_id
                    obj.product_name = finalSubmittingproductL.get(l).product_name
                    AppDatabase.getDBInstance()!!.opportunityProductDao().insertAll(obj)
                }
            }else{
                var obj: OpportunityProductEntity = OpportunityProductEntity()
                obj.shop_id = shop_id_
                obj.opportunity_id = opportunity_id
                obj.product_id = "0"
                obj.product_name = ""
                AppDatabase.getDBInstance()!!.opportunityProductDao().insertAll(obj)
            }

        })
        progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
        cv_add_oppt_frag_add_submit.isEnabled = false
        if (AppUtils.isOnline(mContext)){
            syncopportunity(shop_id_ , activity.opportunity_id)
        }
        else{
            showSucessDialog()
        }
        //showSucessDialog()
    }


    private fun syncopportunity(shop_id_: String, opportunity_id: String) {
        try {
            progress_wheel_add_oppt_frag_add_oppt.spin()
            cv_add_oppt_frag_add_submit.isEnabled = false
            til_oppt_status.isEnabled = false
            tv_add_oppt_frag_add_status.isEnabled = false
            iv_frag_oppt_add_status.isEnabled = false

            var opprDtls = AppDatabase.getDBInstance()!!.opportunityAddDao().getSingleOpportunityL(opportunity_id)
            var opptProductL = AppDatabase.getDBInstance()!!.opportunityProductDao().getOpportunityPrdctL(opportunity_id) as ArrayList<OpportunityProductEntity>

            var syncObj :SyncOppt = SyncOppt()
            syncObj.user_id = Pref.user_id.toString()
            syncObj.session_token = Pref.session_token.toString()
            syncObj.shop_id = opprDtls.shop_id
            syncObj.shop_name = opprDtls.shop_name
            syncObj.shop_type = opprDtls.shop_type
            syncObj.opportunity_id = opprDtls.opportunity_id
            syncObj.opportunity_description = opprDtls.opportunity_description
            if (opprDtls.opportunity_amount.equals("")) {
                syncObj.opportunity_amount = "0"
            }else {
                syncObj.opportunity_amount = opprDtls.opportunity_amount
            }
            syncObj.opportunity_status_id = opprDtls.opportunity_status_id
            syncObj.opportunity_status_name = opprDtls.opportunity_status_name
            syncObj.opportunity_created_date = opprDtls.opportunity_created_date
            syncObj.opportunity_created_time = opprDtls.opportunity_created_time
            syncObj.opportunity_created_date_time = opprDtls.opportunity_created_date_time
            if (opptProductL.size>0){
                for (l in 0..opptProductL.size - 1) {
                    var obj : SyncOpptProductL = SyncOpptProductL()
                    obj.opportunity_id = opprDtls.opportunity_id
                    obj.shop_id = opprDtls.shop_id
                    obj.product_id = opptProductL.get(l).product_id
                    obj.product_name = opptProductL.get(l).product_name
                    syncObj.opportunity_product_list.add(obj)
                }
            }else{

                    var obj: SyncOpptProductL = SyncOpptProductL()
                    obj.shop_id = shop_id_
                    obj.opportunity_id = opportunity_id
                    obj.product_id = "0"
                    obj.product_name = ""

                    syncObj.opportunity_product_list.add(obj)
            }

            val repository = OpportunityRepoProvider.opportunityListRepo()
            BaseActivity.compositeDisposable.add(
                repository.saveOpportunity(syncObj)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as BaseResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            AppDatabase.getDBInstance()!!.opportunityAddDao().updateIsUploaded(opportunity_id,true)
                            showSucessDialog()
                        } else {
                            progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        }
                    }, { error ->
                        progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun showSucessDialog() {
        isValueInserted =false
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.setCanceledOnTouchOutside(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_yes_no_3)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
        dialogHeader.text = mContext.applicationContext.getString(R.string.opt_sucs)
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            isValueInserted =false
            simpleDialog.cancel()
            cv_add_oppt_frag_add_submit.isEnabled = true
            til_oppt_status.isEnabled = true
            tv_add_oppt_frag_add_status.isEnabled = true
            iv_frag_oppt_add_status.isEnabled = true

            voiceMsg("Opportunity saved successfully")
            Toaster.msgShort(mContext,"Opportunity saved successfully")
            editOprtntyID = ""
            (mContext as DashboardActivity).onBackPressed()

        })
        simpleDialog.show()
    }

    private fun voiceMsg(msg: String) {
            val speechStatus = (mContext as DashboardActivity).textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
            if (speechStatus == TextToSpeech.ERROR)
                Log.e("Add Day Start", "TTS error in converting Text to Speech!");

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 7009){
            try {
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                et_add_oppt_frag_add_desc.setText(result!![0].toString())
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }

    fun checkIsEdited():Boolean{
        return isValueInserted
    }
    fun setISEdited(value:Boolean){
        isValueInserted = value
    }

    fun checkModifiedstatusProduct() {
        progress_wheel_add_oppt_frag_add_oppt.spin()
         val repository = OpportunityRepoProvider.opportunityListRepo()
            BaseActivity.compositeDisposable.add(
                repository.getOpportunityStatus(Pref.session_token!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as OpportunityStatusListResponseModel
                        if (response.status == NetworkConstant.SUCCESS) {
                            var list = response.status_list
                            if (list != null && list.isNotEmpty()) {
                                doAsync {
                                    AppDatabase.getDBInstance()!!.opportunityStatusDao().deleteAll()
                                    AppDatabase.getDBInstance()?.opportunityStatusDao()?.insertAll(list!!)
                                    uiThread {
                                        progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                                    }
                                }
                            } else {
                                progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                            }
                        } else {
                            progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                        }
                    }, { error ->
                        error.printStackTrace()
                        progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                    })
            )

        progress_wheel_add_oppt_frag_add_oppt.spin()
        val repositoryP = ProductListRepoProvider.productListProvider()
        BaseActivity.compositeDisposable.add(
            repositoryP.getProductList(Pref.session_token!!, Pref.user_id!!, "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->

                    val response = result as ProductListResponseModel
                    println("tag_dash_product api call response ${response.status}")
                    if (response.status == NetworkConstant.SUCCESS) {
                        val list = response.product_list
                        if (list != null && list.isNotEmpty()) {
                            doAsync {
                                AppDatabase.getDBInstance()?.productListDao()?.deleteAllProduct()
                                AppDatabase.getDBInstance()?.productListDao()?.insertAll(list!!)
                                uiThread {
                                    progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                                }
                            }
                        } else {
                            progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                        }
                    } else {
                        progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                    }

                }, { error ->
                    error.printStackTrace()
                    progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                })
        )

    }

    var selectedProductL :  ArrayList<ProductDtls> = ArrayList()
    fun productDialog(){
        AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
        try {
            val productDialog = Dialog(mContext)
            productDialog.setCancelable(true)
            productDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            productDialog.setContentView(R.layout.dialog_cont_select)
            val rvProductL = productDialog.findViewById(R.id.rv_dialog_cont_list) as RecyclerView
            val tvHeader = productDialog.findViewById(R.id.tv_dialog_cont_sel_header) as TextView
            val submit = productDialog.findViewById(R.id.tv_dialog_cont_list_submit) as TextView
            val et_productNameSearch = productDialog.findViewById(R.id.et_dialog_contact_search) as AppCustomEditText
          //  val cb_selectAll = productDialog.findViewById(R.id.cb_dialog_cont_select_all) as CheckBox
            val iv_close = productDialog.findViewById(R.id.iv_dialog_generic_list_close_icon) as ImageView


            //edit work begin
            if(!editOprtntyID.equals("") && selectedProductL.size==0){
                var opportunity_productL =  AppDatabase.getDBInstance()!!.opportunityProductDao().getOpportunityRealPrdctL(editOprtntyID) as ArrayList<OpportunityProductEntity>
                if(opportunity_productL.size>0){
                    var selPrdctIdL = opportunity_productL.map { it.product_id }
                    productL = AppDatabase.getDBInstance()!!.productListDao().getSelectedProductIdList(selPrdctIdL) as ArrayList<ProductDtls>
                }
            }
            //edit work end

            selectedProductL = productL.filter { it.isTick } as ArrayList<ProductDtls>
            if(selectedProductL.size==0){
                //tvHeader.text = "Selected Product(s): ${productL.size}"
                tvHeader.text = "Selected Product(s): "
            }else{
                tvHeader.text = "Selected Product(s) : (${selectedProductL.size})"
            }



            adapterOptProductName = AdapterOptProductName(mContext,productL,object :AdapterOptProductName.onClick{
                override fun onTickUntick(obj: ProductDtls, isTick: Boolean) {
                    if(isTick){
                        productL.filter { it.product_id.equals(obj.product_id) }.first().isTick = true
                        tvHeader.text = "Selected Product(s) : (${productL.filter { it.isTick }.size})"
                    }else{
                        productL.filter { it.product_id.equals(obj.product_id) }.first().isTick = false
                        tvHeader.text = "Selected Product(s) : (${productL.filter { it.isTick }.size})"
                    }
                }
            },{
                it
            })

            rvProductL.adapter = adapterOptProductName

            iv_close.setOnClickListener {
                productDialog.dismiss()
                progress_wheel_add_oppt_frag_add_oppt.stopSpinning()
                tv_add_oppt_frag_add_product.isEnabled =true
                til_oppt_product.isEnabled =true
                iv_frag_oppt_add_prdct.isEnabled =true

                //edit work begin
                if(!editOprtntyID.equals("")){
                    var opportunity_productL =  AppDatabase.getDBInstance()!!.opportunityProductDao().getOpportunityRealPrdctL(editOprtntyID) as ArrayList<OpportunityProductEntity>
                    if(opportunity_productL.size>0){
                        var selPrdctIdL = opportunity_productL.map { it.product_id }
                        productL = AppDatabase.getDBInstance()!!.productListDao().getSelectedProductIdList(selPrdctIdL) as ArrayList<ProductDtls>
                    }
                }
                selectedProductL = productL.filter { it.isTick } as ArrayList<ProductDtls>
                var productText = ""
                for(i in 0..selectedProductL.size-1) {
                    productText = productText+selectedProductL.get(i).product_name+","
                }
                if (productText.endsWith(",")) {
                    productText = productText.substring(0, productText.length - 1);
                }
                tv_add_oppt_frag_add_product.setText(productText)
                //edit work end
            }

            /*//test code
            if(productL.size == selectedProductL.size){
                println("tag_check_cb programatically checked")
                cb_selectAll.isChecked = true
            }else{
                println("tag_check_cb programatically un-checked")
                cb_selectAll.isChecked = false
            }*/


           /* cb_selectAll.setOnCheckedChangeListener { compoundButton, b ->
                if (compoundButton.isChecked) {
                    println("tag_check_cb checked")
                    adapterOptProductName.selectAll()
                    cb_selectAll.setText("Deselect All")
                }
                else{
                    println("tag_check_cb un-checked")
                    adapterOptProductName.deselectAll()
                    cb_selectAll.setText("Select All")
                }
            }*/

            et_productNameSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    adapterOptProductName!!.getFilter().filter(et_productNameSearch.text.toString().trim())
                }
            })

            submit.setOnClickListener {
                tv_add_oppt_frag_add_product.isEnabled =true
                til_oppt_product.isEnabled =true
                iv_frag_oppt_add_prdct.isEnabled =true

                selectedProductL = productL.filter { it.isTick } as ArrayList<ProductDtls>

                if (selectedProductL.size > 0) {
                    productDialog.dismiss()
                    var productText = ""
                    for(i in 0..selectedProductL.size-1) {
                        productText = productText+selectedProductL.get(i).product_name+","
                    }
                    if (productText.endsWith(",")) {
                        productText = productText.substring(0, productText.length - 1);
                    }
                    tv_add_oppt_frag_add_product.setText(productText)
                    finalSubmittingproductL = selectedProductL
                    tv_add_oppt_frag_add_product.isEnabled = true
                } else{
                    productDialog.dismiss()
                    finalSubmittingproductL = ArrayList()
                    tv_add_oppt_frag_add_product.setText("")
                    tv_add_oppt_frag_add_product.isEnabled = true
                }
            }

            productDialog.show()

        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

}