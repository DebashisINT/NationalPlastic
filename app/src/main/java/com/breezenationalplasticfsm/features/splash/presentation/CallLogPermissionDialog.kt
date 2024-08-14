package com.breezenationalplasticfsm.features.splash.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.widgets.AppCustomTextView

class CallLogPermissionDialog: DialogFragment(), View.OnClickListener {

    private lateinit var mContext: Context

    private lateinit var tv_ok: TextView
    private lateinit var tv_callLogBody: TextView
    private lateinit var iv_close_icon: ImageView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    companion object {

        private var listener: CallLogPermissionDialog.OnItemSelectedListener? = null

        fun newInstance(param: CallLogPermissionDialog.OnItemSelectedListener): CallLogPermissionDialog {
            val dialogFragment = CallLogPermissionDialog()

            listener = param

            return dialogFragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)

        val v = inflater.inflate(R.layout.dialog_call_log, container, false)

        isCancelable = false

        initView(v)
        initClickListener()

        return v
    }

    private fun initView(v: View) {
        v.apply {
            tv_ok = findViewById(R.id.tv_dialog_call_log_ok)
            iv_close_icon = findViewById(R.id.iv_dialog_call_log_cross)
            tv_callLogBody = findViewById(R.id.tv_call_log_body)

            tv_callLogBody.text="In order to generate business report for CRM need to access call logs and to add contact into app need to access Phone Contact also\n" +
                    " to make phone calls for parties.\n" +
                    " \n\n Allow ${context.getResources().getString(R.string.app_name)} app to get call logs,contacts and make phone calls ?"
        }
    }

    private fun initClickListener() {
        tv_ok.setOnClickListener(this)
        iv_close_icon.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.tv_dialog_call_log_ok -> {
                dismiss()
                CallLogPermissionDialog.listener?.onOkClick()
            }
            R.id.iv_dialog_call_log_cross -> {
                dismiss()
                CallLogPermissionDialog.listener?.onCrossClick()
            }
        }
    }

    interface OnItemSelectedListener {
        fun onOkClick()
        fun onCrossClick()
    }

}