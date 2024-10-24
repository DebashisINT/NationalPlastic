package com.breezefieldnationalplastic.app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.breezefieldnationalplastic.R

object ToasterCustom {
    private val SHORT_TOAST_DURATION = 2000
    private val TOAST_DURATION_MILLS: Long = 1000 //change if need longer
    private var toast: Toast? = null

    @SuppressLint("MissingInflatedId")
    fun msgLong(context: Context?, /*view: View,*/ msg: String?) {
        if (context != null && msg != null) {
            if (ToasterCustom.toast != null) {
                ToasterCustom.toast!!.cancel()
            }
            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.toast_custom, null)

            val text = layout.findViewById(R.id.tv_toast_custom_msg) as TextView
            text.text = msg
            Handler(context.mainLooper).post {
                ToasterCustom.toast = Toast(context)
                ToasterCustom.toast!!.duration = Toast.LENGTH_LONG
                /*val offset = getOffsetAboveButton(view)
                if (offset != null && offset.size == 2)
                    toast!!.setGravity(Gravity.CENTER, offset[0], offset[1])
                else
                    toast!!.setGravity(Gravity.CENTER, 0, 200)*/
                ToasterCustom.toast!!.view = layout


                object : CountDownTimer(Math.max(ToasterCustom.TOAST_DURATION_MILLS - ToasterCustom.SHORT_TOAST_DURATION, 1000), 1000) {
                    override fun onFinish() {

                        ToasterCustom.toast!!.show()
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        ToasterCustom.toast!!.show()
                    }
                }.start()
            }
        }
    }

    fun msgShort(context: Context?, msg: String?) {
        if (context != null && msg != null) {
            if (ToasterCustom.toast != null) {
                ToasterCustom.toast!!.cancel()
            }

            Handler(context.mainLooper).post {
                val inflater = LayoutInflater.from(context)
                val layout: View

                layout = inflater.inflate(R.layout.toast_custom, null)

                val text = layout.findViewById(R.id.tv_toast_custom_msg) as TextView
                text.text = msg
                ToasterCustom.toast = Toast(context)
                ToasterCustom.toast!!.view = layout

                toast!!.setGravity(Gravity.CENTER, 0, 200)

                ToasterCustom.toast!!.duration = Toast.LENGTH_LONG


                ToasterCustom.toast!!.show()
            }
        }
    }
}