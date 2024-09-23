package com.breezefieldnationalplastic.features.contacts

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.SearchListener
import com.breezefieldnationalplastic.app.domain.ScheduleTemplateEntity
import com.breezefieldnationalplastic.app.domain.SchedulerMasterEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.widgets.MovableFloatingActionButton
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.github.clans.fab.FloatingActionButton

class TemplateViewFrag : BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context

    private lateinit var rvList:RecyclerView
    private lateinit var fabAddfabAdd: MovableFloatingActionButton
    private lateinit var adapterTemplate:AdapterTemplate
    private lateinit var tv_empty_page_msg_head:TextView
    private lateinit var tv_empty_page_msg:TextView
    private lateinit var ll_no_data_root:LinearLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.template_view_frag, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        rvList = view.findViewById(R.id.rv_template_view_frag_list)
        fabAddfabAdd = view.findViewById(R.id.fab_frag_view_templ_add)
        tv_empty_page_msg_head = view.findViewById(R.id.tv_empty_page_msg_head)
        tv_empty_page_msg = view.findViewById(R.id.tv_empty_page_msg)
        ll_no_data_root = view.findViewById(R.id.ll_no_data_root)

        setData()
        fabAddfabAdd.setCustomClickListener {
            (mContext as DashboardActivity).loadFragment(FragType.TemplateAddFrag, true, "")
        }
    }

    fun setData(){
        //var templateList = AppDatabase.getDBInstance()?.scheduleTemplateDao()?.getAll() as ArrayList<ScheduleTemplateEntity>
        var templateList = AppDatabase.getDBInstance()?.scheduleTemplateDao()?.getAllExceptManually() as ArrayList<ScheduleTemplateEntity>
        if(templateList.size>0){
            rvList.visibility = View.VISIBLE
            ll_no_data_root.visibility = View.GONE
            adapterTemplate = AdapterTemplate(mContext,templateList,object :AdapterTemplate.OnCLick{
                override fun onIClick(obj: ScheduleTemplateEntity) {
                    val simpleDialog = Dialog(mContext)
                    simpleDialog.setCancelable(false)
                    simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialog.setContentView(R.layout.dialog_info_2)
                    val dialogHeader = simpleDialog.findViewById(R.id.tv_dialog_info_2_header) as TextView
                    val ivCross = simpleDialog.findViewById(R.id.iv_dialog_info_2_cross) as ImageView
                    val tvInfo = simpleDialog.findViewById(R.id.tv_dialog_info_2_info_na) as TextView

                    dialogHeader.text = obj.template_name
                    tvInfo.text = obj.template_desc
                    ivCross.setOnClickListener {
                        simpleDialog.dismiss()
                    }

                    simpleDialog.show()

                }

                override fun onDelClick(obj: ScheduleTemplateEntity) {

                    var simpleDialog = Dialog(mContext)
                    simpleDialog.setCancelable(false)
                    simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialog.setContentView(R.layout.dialog_yes_no)
                    val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                    val dialogBody = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                    val btn_no = simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
                    val btn_yes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView

                    dialogHeader.text = AppUtils.hiFirstNameText() + "!"
                    dialogBody.text = "Do you want to delete this Template?"

                    btn_yes.setOnClickListener({ view ->
                        AppDatabase.getDBInstance()?.scheduleTemplateDao()?.delete(obj.template_id)
                        Handler().postDelayed(Runnable {
                            setData()
                        }, 100)
                        simpleDialog.cancel()
                    })
                    btn_no.setOnClickListener({ view ->
                        simpleDialog.cancel()
                    })
                    simpleDialog.show()



                }
            })
            rvList.adapter = adapterTemplate
        }else{
            rvList.visibility = View.GONE
            ll_no_data_root.visibility = View.VISIBLE
            tv_empty_page_msg_head.text = "No Template Found"
            tv_empty_page_msg.text = "Click + to add your Template"
        }

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){

        }
    }


}