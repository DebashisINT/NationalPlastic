package com.breezenationalplasticfsm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment

object DialogLoading:DialogFragment() {
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCanceledOnTouchOutside(false)    //restrict dialog close on touch screen outside dialog
        dialog?.setCancelable(false)//restrict dialog close on touch back button ** setCancelable is superior to setCanceledOnTouchOutside
        val v = inflater.inflate(R.layout.dialog_loading,container,false)
        dialog!!.show()
        return v
    }

}