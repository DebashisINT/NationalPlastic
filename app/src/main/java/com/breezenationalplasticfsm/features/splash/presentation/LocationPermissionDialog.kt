package com.breezenationalplasticfsm.features.splash.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.widgets.AppCustomTextView

class LocationPermissionDialog : DialogFragment(), View.OnClickListener {

    private lateinit var mContext: Context

    private lateinit var tv_ok: AppCustomTextView
    private lateinit var tv_body: AppCustomTextView
    private lateinit var iv_close_icon: ImageView

    companion object {

        private var listener: OnItemSelectedListener? = null

        fun newInstance(param: OnItemSelectedListener): LocationPermissionDialog {
            val dialogFragment = LocationPermissionDialog()

            listener = param

            return dialogFragment
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)

        val v = inflater.inflate(R.layout.dialog_location, container, false)

        isCancelable = false

        initView(v)
        initClickListener()

        return v
    }

    private fun initView(v: View) {
        v.apply {
            tv_ok = findViewById(R.id.tv_ok)
            iv_close_icon = findViewById(R.id.iv_close_icon)
            tv_body = findViewById(R.id.tv_body)

            tv_body.text = "${context.getResources().getString(R.string.app_name)} App collects location data after you open and login into the App, to identify nearby Parties location even when the app is running in the background and not in use.This app collects location data to enable nearby shops, GPS route,\n" +
                    " even when the app is closed or not in use.Reimbursement is issued with distance traveled for specific GPS route.\n" +
                    " This is a core functionality of this app."
        }
    }

    private fun initClickListener() {
        tv_ok.setOnClickListener(this)
        iv_close_icon.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.tv_ok -> {
                dismiss()
                listener?.onOkClick()
            }

            R.id.iv_close_icon -> {
                dismiss()
                listener?.onCrossClick()
            }
        }
    }

    interface OnItemSelectedListener {
        fun onOkClick()

        fun onCrossClick()
    }
}