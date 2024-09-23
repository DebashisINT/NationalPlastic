package com.breezefieldnationalplastic.features.contacts

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.domain.OpportunityAddEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.uiaction.IntentActionable
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.app.widgets.MovableFloatingActionButton
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.commondialog.presentation.CommonDialog
import com.breezefieldnationalplastic.features.commondialog.presentation.CommonDialogClickListener
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.login.api.opportunity.OpportunityRepoProvider
import com.breezefieldnationalplastic.features.orderITC.SyncDeleteOppt
import com.breezefieldnationalplastic.features.orderITC.SyncDeleteOpptL
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.bumptech.glide.Glide
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class ViewCrmOpptFrag : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    lateinit var shopObj : AddShopDBModelEntity
    private lateinit var tv_frag_oppt_crm_name: TextView
    private lateinit var tv_frag_oppt_crm_addr: TextView
    private lateinit var tv_frag_oppt_crm_ph: TextView

    private lateinit var ll_no_data_root: LinearLayout
    private lateinit var tv_frag_add_oppt_noData: TextView
    private lateinit var tv_noDataHeader:TextView
    private lateinit var tv_noDataBody:TextView
    private lateinit var img_direction:ImageView
    private lateinit var iv_frag_oppt_crm_ph:ImageView
    private lateinit var iv_frag_oppt_crm_addr:ImageView

    private lateinit var rv_frag_oppt_crm_list: RecyclerView

    private lateinit var pw_frag_oppt_crm: ProgressWheel
    private lateinit var ll_frag_oppt_crm_ph_root: LinearLayout
    private lateinit var ll_frag_oppt_crm_addr_root: LinearLayout
    private lateinit var cv_oppt_cntct: CardView
    private lateinit var cv_oppt_address: CardView

    private lateinit var fab_frag_oppt_crm_add_oppt: MovableFloatingActionButton
    private lateinit var adapterOpportunityList: AdapterOpportunityList
    var product_name :String =""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        var shop_id:String = ""
        fun getInstance(objects: Any): ViewCrmOpptFrag {
            val viewCrmOpptFrag = ViewCrmOpptFrag()
            if (!TextUtils.isEmpty(objects.toString())) {
                shop_id=objects.toString()
            }
            return viewCrmOpptFrag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_view_crm_opportunity, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {

        shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shop_id)

        tv_frag_oppt_crm_name = view.findViewById(R.id.tv_frag_oppt_crm_name)
        tv_frag_oppt_crm_addr = view.findViewById(R.id.tv_frag_oppt_crm_addr)
        tv_frag_oppt_crm_ph = view.findViewById(R.id.tv_frag_oppt_crm_ph)

        rv_frag_oppt_crm_list = view.findViewById(R.id.rv_frag_oppt_crm_list)

        ll_frag_oppt_crm_ph_root = view.findViewById(R.id.ll_frag_oppt_crm_ph_root)
        ll_frag_oppt_crm_addr_root = view.findViewById(R.id.ll_frag_oppt_crm_addr_root)
        cv_oppt_address = view.findViewById(R.id.cv_oppt_address)
        cv_oppt_cntct = view.findViewById(R.id.cv_oppt_cntct)

        pw_frag_oppt_crm = view.findViewById(R.id.pw_frag_oppt_crm)
        fab_frag_oppt_crm_add_oppt = view.findViewById(R.id.fab_frag_oppt_crm_add_oppt)

        tv_frag_add_oppt_noData = view.findViewById(R.id.tv_frag_add_oppt_noData)
        ll_no_data_root = view.findViewById(R.id.ll_no_data_root)
        tv_noDataHeader = view.findViewById(R.id.tv_empty_page_msg_head)
        tv_noDataBody = view.findViewById(R.id.tv_empty_page_msg)
        img_direction = view.findViewById(R.id.img_direction)
        iv_frag_oppt_crm_ph = view.findViewById(R.id.iv_frag_oppt_crm_ph)
        iv_frag_oppt_crm_addr = view.findViewById(R.id.iv_frag_oppt_crm_addr)

        Glide.with(mContext)
            .load(R.drawable.animationcall)
            .into(iv_frag_oppt_crm_ph)

        Glide.with(mContext)
            .load(R.drawable.animationaddress)
            .into(iv_frag_oppt_crm_addr)

        fab_frag_oppt_crm_add_oppt.setOnClickListener(this)
        ll_frag_oppt_crm_ph_root.setOnClickListener(this)
        ll_frag_oppt_crm_addr_root.setOnClickListener(this)
        cv_oppt_cntct.setOnClickListener(this)
        cv_oppt_address.setOnClickListener(this)

        tv_noDataHeader.text = "No Opportunity Found"
        tv_noDataBody.text = "Click + to add new opportunity"

        try {
            tv_frag_oppt_crm_name.text = shopObj.shopName
            if(shopObj.address.equals("NA")){
                ll_frag_oppt_crm_addr_root.visibility = View.VISIBLE
                tv_frag_oppt_crm_addr.text = "No address found"

            }else{
                ll_frag_oppt_crm_addr_root.visibility = View.VISIBLE
                tv_frag_oppt_crm_addr.text = shopObj.address
            }
            tv_frag_oppt_crm_ph.text = shopObj.ownerContactNumber
        } catch (e: Exception) {
            e.printStackTrace()
        }

        pw_frag_oppt_crm.stopSpinning()

        val list = AppDatabase.getDBInstance()!!.opportunityAddDao().getAll(false, shop_id) as ArrayList<OpportunityAddEntity>
        if (list.size>0) {
            opportunityList("")
        }
        else {

        }

        fab_frag_oppt_crm_add_oppt.setCustomClickListener {

            if (!Pref.isAddAttendence) {
                (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
            }else {
                (mContext as DashboardActivity).loadFragment(FragType.AddOpptFrag, true, shop_id + "," + "")

            }
        }
        AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
    }




    fun  opportunityList(searchObj:String,sortBy:String="") {
        pw_frag_oppt_crm.spin()
        var opportunityL : ArrayList<OpportunityAddEntity> = ArrayList()
        opportunityL = AppDatabase.getDBInstance()!!.opportunityAddDao().getAll(false, shop_id) as ArrayList<OpportunityAddEntity>

        pw_frag_oppt_crm.stopSpinning()
        if(opportunityL.size>0){
            for(i in 0..opportunityL.size-1){
               // println("tag_conta_show ${opportunityL.get(i).product_id} ${opportunityL.get(i).isUpload}")
            }
            tv_frag_add_oppt_noData.visibility = View.GONE
            (mContext as DashboardActivity).setTopBarTitle("Opportunities : ${opportunityL.size}")
            adapterOpportunityList = AdapterOpportunityList(mContext,opportunityL,object :AdapterOpportunityList.onClick{

                override fun onInfoClick(iv_view_opt: LinearLayout, obj: OpportunityAddEntity) {
                        iv_view_opt.isEnabled =false
                        showDialogOfView(iv_view_opt,obj)
                }

                override fun onEditClick(obj: OpportunityAddEntity) {
                    if (!Pref.isAddAttendence) {
                        (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                    }else {
                        (mContext as DashboardActivity).loadFragment(
                            FragType.AddOpptFrag, true,
                            shop_id + "," + obj.opportunity_id
                        )
                    }

                }

                override fun onDeleteClick(iv_delete_opt: LinearLayout, obj: OpportunityAddEntity) {
                    iv_delete_opt.isEnabled =false
                    showCheckAlert("Opportunity Confirmation", "Would you like to delete the opportunity?", iv_delete_opt, obj)
                }

            })
            rv_frag_oppt_crm_list.adapter = adapterOpportunityList
            rv_frag_oppt_crm_list.visibility = View.VISIBLE
            ll_no_data_root.visibility = View.GONE

        }
        else{
            (mContext as DashboardActivity).setTopBarTitle("Opportunities")
            //  tvNodata.visibility = View.VISIBLE
            rv_frag_oppt_crm_list.visibility = View.GONE
            ll_no_data_root.visibility = View.VISIBLE
            tv_noDataHeader.text = "No Opportunity Found"
            tv_noDataBody.text = "Click + to add new opportunity"
            //img_direction.animate().rotationY(180F).start()
        }

    }

    private fun showDialogOfView(iv_view_opt: LinearLayout, obj: OpportunityAddEntity) {
            iv_view_opt.isEnabled=false
            val simpleDialog = Dialog(mContext)
            simpleDialog.setCancelable(true)
            simpleDialog.setCanceledOnTouchOutside(false)
            simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialog.setContentView(R.layout.dialog_of_view_oprtnty)

            val iv_dialog_oprtnty_close_icon =
                simpleDialog.findViewById(R.id.iv_dialog_oprtnty_close_icon) as ImageView
            val tv_opt_status_View = simpleDialog.findViewById(R.id.tv_opt_status_View) as TextView
            val rv_slctdPrdctList =
                simpleDialog.findViewById(R.id.rv_slctdPrdctList) as RecyclerView
            val tv_opt_estimated_amount_View =
                simpleDialog.findViewById(R.id.tv_opt_estimated_amount_View) as TextView
            val tv_opt_description_View =
                simpleDialog.findViewById(R.id.tv_opt_description_View) as TextView
            val tv_opt_date_View = simpleDialog.findViewById(R.id.tv_opt_date_View) as TextView
            val tv_opt_selectedPrdct_none = simpleDialog.findViewById(R.id.tv_opt_selectedPrdct_none) as TextView
            val ll_prdcts = simpleDialog.findViewById(R.id.ll_prdcts) as LinearLayout

            var opportunityProductL = AppDatabase.getDBInstance()!!.opportunityProductDao().getOpportunitySelectedPrdctL(shop_id, obj.opportunity_id) as ArrayList<ProductDtls>

            var adapterSlctdPrdctLstAdapter: AdapterSlctdPrdctLstAdapter

            tv_opt_status_View.setText(obj.opportunity_status_name)
            if (obj.opportunity_amount.equals("0.0") || obj.opportunity_amount.equals("0") || obj.opportunity_amount.equals("")){
                tv_opt_estimated_amount_View.setText("0")
            }else {
                tv_opt_estimated_amount_View.setText(obj.opportunity_amount)
            }
            tv_opt_date_View.text = AppUtils.getFormatedDateNew(obj.opportunity_created_date,"yyyy-mm-dd","dd-mm-yyyy")
            tv_opt_description_View.setText(obj.opportunity_description)

        if (opportunityProductL.size > 0 && !opportunityProductL.get(0).product_id.equals("0")){
            rv_slctdPrdctList.visibility = View.VISIBLE
            val params: ViewGroup.LayoutParams = ll_prdcts.layoutParams
           /* params.width = LinearLayout.LayoutParams.MATCH_PARENT
            params.height = 400
            ll_prdcts.layoutParams = params*/
            tv_opt_selectedPrdct_none.visibility = View.GONE


            adapterSlctdPrdctLstAdapter = AdapterSlctdPrdctLstAdapter(mContext, opportunityProductL)
            rv_slctdPrdctList.adapter = adapterSlctdPrdctLstAdapter

            if (opportunityProductL.size>5) {

                val params: ViewGroup.LayoutParams = ll_prdcts.layoutParams
                params.width = LinearLayout.LayoutParams.MATCH_PARENT
                params.height = 400
                ll_prdcts.layoutParams = params
                rv_slctdPrdctList.isScrollbarFadingEnabled = false
            }else
            {
                val params: ViewGroup.LayoutParams = ll_prdcts.layoutParams
                params.width = LinearLayout.LayoutParams.MATCH_PARENT
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT
                ll_prdcts.layoutParams = params
                rv_slctdPrdctList.isScrollbarFadingEnabled = true
            }

        }else{
            // Gets the layout params that will allow you to resize the layout
            val params: ViewGroup.LayoutParams = ll_prdcts.layoutParams
            params.width = LinearLayout.LayoutParams.MATCH_PARENT
            params.height = 60
            ll_prdcts.layoutParams = params
            rv_slctdPrdctList.visibility = View.GONE
            tv_opt_selectedPrdct_none.visibility = View.VISIBLE
            tv_opt_selectedPrdct_none.setText("None")
        }
            iv_dialog_oprtnty_close_icon.setOnClickListener {
                iv_view_opt.isEnabled=true
                simpleDialog.dismiss()
            }
            simpleDialog.show()
    }

    private fun showCheckAlert(
        header: String,
        title: String,
        iv_delete_opt: LinearLayout,
        obj: OpportunityAddEntity
    ) {
        iv_delete_opt.isEnabled =false
        CommonDialog.getInstance(header, title, getString(R.string.no), getString(R.string.yes), false, object :
            CommonDialogClickListener {
            override fun onLeftClick() {
                pw_frag_oppt_crm.stopSpinning()
                iv_delete_opt.isEnabled =true
            }
            override fun onRightClick(editableData: String) {
                Timber.d("Order onRightClick ${AppUtils.getCurrentDateTime()}")
                iv_delete_opt.isEnabled =true
                var opprDtls = AppDatabase.getDBInstance()!!.opportunityAddDao().getSingleOpportunityL(obj.opportunity_id)
                if(opprDtls.isUpload){
                    AppDatabase.getDBInstance()!!.opportunityAddDao().updateIsDeleted(isDeleted = true,opprDtls.opportunity_id)

                    if(AppUtils.isOnline(mContext)){
                        var syncObj : SyncDeleteOppt = SyncDeleteOppt()
                        syncObj.user_id = Pref.user_id.toString()
                        syncObj.session_token = Pref.session_token.toString()
                        syncObj.opportunity_delete_list.add(SyncDeleteOpptL(opprDtls.opportunity_id))

                            pw_frag_oppt_crm.spin()
                            val repository = OpportunityRepoProvider.opportunityListRepo()
                            BaseActivity.compositeDisposable.add(
                                repository.deleteOpportunity(syncObj)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({ result ->
                                        pw_frag_oppt_crm.stopSpinning()
                                        val response = result as BaseResponse
                                        if (response.status == NetworkConstant.SUCCESS) {
                                            for(i in 0..syncObj.opportunity_delete_list.size-1){
                                                AppDatabase.getDBInstance()!!.opportunityAddDao().deleteOpportunityById(syncObj.opportunity_delete_list.get(i).opportunity_id)
                                                AppDatabase.getDBInstance()!!.opportunityProductDao().deleteOprtntyById(syncObj.opportunity_delete_list.get(i).opportunity_id)
                                            }
                                            showSucessDialog()
                                        } else {
                                            pw_frag_oppt_crm.stopSpinning()
                                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                        }
                                    }, { error ->
                                        pw_frag_oppt_crm.stopSpinning()
                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                    })
                            )
                    }else{
                        showSucessDialog()
                    }
                }else{
                    AppDatabase.getDBInstance()!!.opportunityAddDao().deleteOpportunityById(opprDtls.opportunity_id)
                    AppDatabase.getDBInstance()!!.opportunityProductDao().deleteOprtntyById(opprDtls.opportunity_id)
                    showSucessDialog()
                }




            }


        }).show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private fun showSucessDialog() {

        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.setCanceledOnTouchOutside(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_yes_no_3)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
        dialogHeader.text = "Opportunity deleted successfully"
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            voiceMsg("Opportunity deleted successfully")
            //Toaster.msgShort(mContext,"Opportunity delete successfully")
            opportunityList("")
            //(mContext as DashboardActivity).onBackPressed()

        })
        simpleDialog.show()
    }

    private fun voiceMsg(msg: String) {
        val speechStatus = (mContext as DashboardActivity).textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
        if (speechStatus == TextToSpeech.ERROR)
            Log.e("Add Day Start", "TTS error in converting Text to Speech!");

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.cv_oppt_address -> {
                try{
                    if(Pref.ContactAddresswithGeofence) {
                        if (shopObj.shopLat != 0.0 && shopObj.shopLong != 0.0){
                            var intentGmap: Intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("google.navigation:q=${shopObj.shopLat},${shopObj.shopLong}&mode=1")
                            )
                            intentGmap.setPackage("com.google.android.apps.maps")
                            if (intentGmap.resolveActivity(mContext.packageManager) != null) {
                                mContext.startActivity(intentGmap)
                            }
                        }
                        else{
                            cv_oppt_address.isEnabled = false
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
                                cv_oppt_address.isEnabled = true
                            })
                            simpleDialog.setOnDismissListener {
                                cv_oppt_address.isEnabled = true
                            }
                            simpleDialog.show()

                           // Toast.makeText(mContext, "GPS co-ordinates not found. Unable to navigate the map.", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                catch (ex:Exception){
                    ex.printStackTrace()
                }
            }
            R.id.cv_oppt_cntct->{
                IntentActionable.initiatePhoneCall(mContext, shopObj.ownerContactNumber)
            }
        }
    }

}