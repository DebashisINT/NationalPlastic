package com.breezefieldnationalplastic.features.contacts

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.ActivityEntity
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.uiaction.IntentActionable
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pnikosis.materialishprogress.ProgressWheel
import kotlinx.android.synthetic.main.frag_activity_dtls.fab_frag_ord_add_activity
import kotlinx.android.synthetic.main.fragment_leave_list.fab
import kotlinx.android.synthetic.main.row_activity_dtls.view.tv_row_acti_dtls
import kotlinx.android.synthetic.main.row_activity_dtls.view.tv_row_activity_activity
import kotlinx.android.synthetic.main.row_activity_dtls.view.tv_row_activity_due_date
import kotlinx.android.synthetic.main.row_activity_dtls.view.tv_row_activity_type

class ActivityDtlsFrag: BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    lateinit var shopObj : AddShopDBModelEntity
    private lateinit var tv_crmName:TextView
    private lateinit var tv_crmAddr:TextView
    private lateinit var tv_crmContactNo:TextView

    private lateinit var ll_no_data_root: LinearLayout
    private lateinit var tv_noDataHeader:TextView
    private lateinit var tv_noDataBody:TextView

    private lateinit var rvDtls:RecyclerView

    private lateinit var progress_wheel: ProgressWheel
    private lateinit var ll_callRoot: LinearLayout

    private lateinit var adapter:AdapterActivityDtls
    private lateinit var fabAdd:FloatingActionButton

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object{
        var shop_id:String = ""
        fun getInstance(objects: Any): ActivityDtlsFrag {
            val activityDtlsFrag = ActivityDtlsFrag()
            if (!TextUtils.isEmpty(objects.toString())) {
                shop_id=objects.toString()
            }
            return activityDtlsFrag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_activity_dtls, container, false)
        initView(view)
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initView(view: View) {
        shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shop_id)

        tv_crmName = view!!.findViewById(R.id.tv_frag_ord_list_crm_name)
        tv_crmAddr = view!!.findViewById(R.id.tv_frag_crm_addr)
        tv_crmContactNo = view!!.findViewById(R.id.tv_frag_crm_contact_no)

        ll_no_data_root = view.findViewById(R.id.ll_no_data_root)
        tv_noDataHeader = view.findViewById(R.id.tv_empty_page_msg_head)
        tv_noDataBody = view.findViewById(R.id.tv_empty_page_msg)

        rvDtls = view.findViewById(R.id.rv_frag_activity_list_dtls)
        progress_wheel = view.findViewById(R.id.pw_frag_ord_list)
        fabAdd = view.findViewById(R.id.fab_frag_ord_add_activity)
        ll_callRoot = view.findViewById(R.id.ll_crm_activity_phone_root)

        progress_wheel.stopSpinning()

        fabAdd.setOnClickListener(this)
        ll_callRoot.setOnClickListener(this)

        try {
            tv_crmName.text = shopObj.shopName
            tv_crmAddr.text = if(shopObj.address.equals("NA")) "" else shopObj.address
            tv_crmContactNo.text = shopObj.ownerContactNumber
        } catch (e: Exception) {
            e.printStackTrace()
        }

        showActivityData()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.fab_frag_ord_add_activity -> {
                (mContext as DashboardActivity).isFromMenu = false
                (mContext as DashboardActivity).loadFragment(FragType.AddActivityFragment, true, shopObj)
            }
            R.id.ll_crm_activity_phone_root->{
                IntentActionable.initiatePhoneCall(mContext, shopObj.ownerContactNumber)
            }
        }
    }

    private fun showActivityData(){
        progress_wheel.spin()
        var activityDtlsL = AppDatabase.getDBInstance()?.activDao()?.getShopIdWiseDesc(shop_id) as ArrayList<ActivityEntity>
        if(activityDtlsL.size>0){
            ll_no_data_root.visibility = View.GONE
            rvDtls.visibility = View.VISIBLE

            adapter = AdapterActivityDtls(mContext,activityDtlsL,object :AdapterActivityDtls.OnActionClick{
                override fun onViewClick(obj: ActivityEntity) {
                    showActivityDtlsDialog(obj)
                }

            })
            rvDtls.adapter = adapter
            progress_wheel.stopSpinning()
        }else{
            ll_no_data_root.visibility = View.VISIBLE
            rvDtls.visibility = View.GONE
            progress_wheel.stopSpinning()
            tv_noDataHeader.text = "No Activity found."
            tv_noDataBody.text = "Click + to add activity."
        }
    }

    fun updateList(){
        showActivityData()
    }

    fun showActivityDtlsDialog(obj: ActivityEntity){
        try {
            val simpleDialog = Dialog(mContext)
            simpleDialog.setCancelable(true)
            simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialog.setContentView(R.layout.dialog_activity_dtls)

            val tv_activityDate = simpleDialog.findViewById(R.id.tv_dialog_activity_date) as TextView
            val tv_activity = simpleDialog.findViewById(R.id.tv_dialog_activity_activity) as TextView
            val tv_type = simpleDialog.findViewById(R.id.tv_dialog_activity_type) as TextView
            val tv_dueDate = simpleDialog.findViewById(R.id.tv_dialog_activity_due_date) as TextView
            val tv_dtls = simpleDialog.findViewById(R.id.tv_dialog_acti_dtls) as TextView


            tv_activityDate.text= Html.fromHtml("<font color=" + mContext.resources.getColor(R.color.colorAccent) + ">Date-Time : </font> <font color="+
                    mContext.resources.getColor(R.color.colorAccent) + ">" +AppUtils.convertToBillingFormat(obj.date!!)+" "+obj.time)


            val activity = AppDatabase.getDBInstance()?.activityDropdownDao()?.getSingleItem(obj?.activity_dropdown_id!!)
            if (!TextUtils.isEmpty(activity?.activity_name))
                tv_activity.text = activity?.activity_name
            else
                tv_activity.text = "N.A."

            val type = AppDatabase.getDBInstance()?.typeDao()?.getSingleType(obj?.type_id!!)
            if (!TextUtils.isEmpty(type?.name))
                tv_type.text = type?.name
            else
                tv_type.text = "N.A."

            if(obj?.due_date.isNullOrEmpty()){
                tv_dueDate.text = "N.A."
            }else{
                tv_dueDate.text = AppUtils.convertToBillingFormat(obj.due_date!!)
            }

            if(obj?.details.isNullOrEmpty()){
                tv_dtls.text = "N.A."
            }else{
                tv_dtls.text = obj?.details
            }

            val tv_ok = simpleDialog.findViewById(R.id.tv_dialog_activity_yes) as AppCustomTextView
            tv_ok.setOnClickListener({ view ->
                simpleDialog.cancel()
            })
            simpleDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}