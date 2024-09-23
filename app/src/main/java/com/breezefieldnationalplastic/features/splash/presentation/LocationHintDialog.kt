package com.breezefieldnationalplastic.features.splash.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.widgets.AppCustomTextView


class LocationHintDialog : DialogFragment(), View.OnClickListener {

    private lateinit var mContext: Context

    private lateinit var tv_ok: AppCustomTextView
    private lateinit var tv_Header: TextView

    companion object {

        private var listener: OnItemSelectedListener? = null

        fun newInstance(param: OnItemSelectedListener): LocationHintDialog {
            val dialogFragment = LocationHintDialog()

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

        val v = inflater.inflate(R.layout.dialog_loc_hint, container, false)

        isCancelable = false

        initView(v)
        initClickListener()

        return v
    }

    private fun initView(v: View) {
        v.apply {
            tv_ok = findViewById(R.id.tv_ok_loc_hint)
            tv_Header = findViewById(R.id.tv_dialog_loc_hint_app_name_text)
        }

        tv_Header.text = "Allow ${getString(R.string.app_name)} to \n access this device's location"
    }

    private fun initClickListener() {
        tv_ok.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.tv_ok_loc_hint -> {
                dismiss()
                listener?.onOkClick()
            }
        }
    }

    interface OnItemSelectedListener {
        fun onOkClick()
    }
}