package com.breezefieldnationalplastic.features.splash.presentation

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.widgets.AppCustomTextView

class LocPermissionDialog: DialogFragment(), View.OnClickListener {
    private lateinit var mContext: Context

    private lateinit var tv_ok: AppCustomTextView
    private lateinit var tv_not_ok: AppCustomTextView

    companion object {
        private var listener: LocPermissionDialog.OnItemSelectedListener? = null
        fun newInstance(param: LocPermissionDialog.OnItemSelectedListener): LocPermissionDialog {
            val dialogFragment = LocPermissionDialog()

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
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val v = inflater.inflate(R.layout.dialog_loc, container, false)

        isCancelable = false

        initView(v)
        initClickListener()

        return v
    }

    private fun initView(v: View) {
        v.apply {
            tv_ok = findViewById(R.id.tv_loc_dialog_ok)
            tv_not_ok = findViewById(R.id.tv_loc_dialog_not_ok)
        }
    }

    private fun initClickListener() {
        tv_ok.setOnClickListener(this)
        tv_not_ok.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.tv_loc_dialog_ok -> {
                //dismiss()
                LocPermissionDialog.listener?.onOkClick()
            }

            R.id.tv_loc_dialog_not_ok -> {
                dismiss()
                LocPermissionDialog.listener?.onCrossClick()
            }
        }
    }

    interface OnItemSelectedListener {
        fun onOkClick()

        fun onCrossClick()
    }

}