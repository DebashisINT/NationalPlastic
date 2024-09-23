package com.breezefieldnationalplastic.features.contacts

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.ScheduleTemplateEntity
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.google.android.material.textfield.TextInputEditText

class TemplateAddFrag: BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context

    private lateinit var etTemplateName : EditText
    private lateinit var etTemplateDesc :EditText
    private lateinit var ivCopy1 :ImageView
    private lateinit var tvCopy1 :TextView
    private lateinit var ivCopy2 :ImageView
    private lateinit var tvCopy2 :TextView
    private lateinit var btnSubmit :TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_template_add, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        etTemplateName = view.findViewById(R.id.tv_template_name)
        etTemplateDesc = view.findViewById(R.id.tv_template_desc)
        ivCopy1 = view.findViewById(R.id.iv_frag_templ_copy1)
        tvCopy1 = view.findViewById(R.id.tv_frag_templ_copy1)
        ivCopy2 = view.findViewById(R.id.iv_frag_templ_copy2)
        tvCopy2 = view.findViewById(R.id.tv_frag_templ_copy2)
        btnSubmit = view.findViewById(R.id.btn_template_submit)

        ivCopy1.setOnClickListener(this)
        tvCopy1.setOnClickListener(this)
        ivCopy2.setOnClickListener(this)
        tvCopy2.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)

        //etTemplateDesc.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            ivCopy1.id,tvCopy1.id -> {
                try{
                    var content = etTemplateDesc.text.toString()
                    content = content + " @ToName "
                    etTemplateDesc.setText(content)
                    val pos: Int = etTemplateDesc.getText()!!.length
                    etTemplateDesc.setSelection(pos)
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
            }
            ivCopy2.id,tvCopy2.id -> {
                try{
                    var content = etTemplateDesc.text.toString()
                    content = content + " @FromName "
                    etTemplateDesc.setText(content)
                    val pos: Int = etTemplateDesc.getText()!!.length
                    etTemplateDesc.setSelection(pos)
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
            }
            btnSubmit.id -> {
                if(etTemplateName.text.toString().length == 0){
                    etTemplateName.setError("Enter template name")
                    etTemplateName.requestFocus()
                    return
                }
                if(AppDatabase.getDBInstance()?.scheduleTemplateDao()?.getDuplicateTemplateData(etTemplateName.text.toString())!!.size > 0){
                    etTemplateName.setError("Duplicate Template Name")
                    etTemplateName.requestFocus()
                    return
                }
                if(etTemplateDesc.text.toString().length == 0){
                    etTemplateDesc.setError("Enter template description")
                    etTemplateDesc.requestFocus()
                    return
                }

                var obj = ScheduleTemplateEntity()
                obj.template_id = Pref.user_id+System.currentTimeMillis().toString()
                obj.template_name = etTemplateName.text.toString()
                obj.template_desc = etTemplateDesc.text.toString()

                obj.template_desc = obj.template_desc.replace("@to name","@ToName").replace("@from name","@FromName")
                obj.template_desc = obj.template_desc.replace("@toname","@ToName").replace("@fromname","@FromName")
                obj.template_desc = obj.template_desc.replace("@Toname","@ToName").replace("@Fromname","@FromName")
                obj.template_desc = obj.template_desc.replace("@To Name","@ToName").replace("@From Name","@FromName")
                obj.template_desc = obj.template_desc.replace("@To name","@ToName").replace("@From name","@FromName")

                AppDatabase.getDBInstance()?.scheduleTemplateDao()?.insert(obj)
                Toaster.msgShort(mContext,"Template saved successfully.")
                (mContext as DashboardActivity).onBackPressed()
            }
        }
    }
}