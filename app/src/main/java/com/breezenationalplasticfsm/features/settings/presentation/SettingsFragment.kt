package com.breezenationalplasticfsm.features.settings.presentation

import android.app.Activity
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.changepassword.presentation.ChangePasswordDialog
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import android.content.Context
import java.util.*
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.PowerManager
import android.provider.Settings
import androidx.cardview.widget.CardView
import android.widget.LinearLayout
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.AutoStartHelper
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.features.commondialog.presentation.CommonDialog
import com.breezenationalplasticfsm.features.commondialog.presentation.CommonDialogClickListener
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.fasterxml.jackson.databind.util.ClassUtil.getPackageName

/**
 * Created by Pratishruti on 31-10-2017.
 */
class SettingsFragment : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context

    private lateinit var cv_auto_start: CardView
    private lateinit var cv_over_other_apps: CardView
    private lateinit var cv_camera: CardView
    private lateinit var cv_loc: CardView
    private lateinit var cv_phone: CardView
    private lateinit var cv_gallery: CardView
    private lateinit var cv_audio: CardView
    private lateinit var cv_calender: CardView
    private lateinit var ll_settings_main: LinearLayout
    private lateinit var cv_batOpti: CardView




    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        initView(view)
        initClickListener()

        return view

    }

    private fun initView(view: View) {
        view.apply {
            cv_auto_start = findViewById(R.id.cv_auto_start)
            cv_over_other_apps = findViewById(R.id.cv_over_other_apps)
            cv_camera = findViewById(R.id.cv_camera)
            cv_loc = findViewById(R.id.cv_loc)
            cv_phone = findViewById(R.id.cv_phone)
            cv_gallery = findViewById(R.id.cv_gallery)
            cv_audio = findViewById(R.id.cv_audio)
            cv_calender = findViewById(R.id.cv_calender)
            ll_settings_main = findViewById(R.id.ll_settings_main)
            cv_batOpti = findViewById(R.id.cv_frag_settings_batOpti)

        }
    }

    private fun initClickListener() {
        cv_auto_start.setOnClickListener(this)
        cv_over_other_apps.setOnClickListener(this)
        cv_camera.setOnClickListener(this)
        cv_loc.setOnClickListener(this)
        cv_phone.setOnClickListener(this)
        cv_gallery.setOnClickListener(this)
        cv_audio.setOnClickListener(this)
        cv_calender.setOnClickListener(this)
        ll_settings_main.setOnClickListener(null)
        cv_batOpti.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.cv_auto_start -> {
                AutoStartHelper.instance.getAutoStartPermission(mContext)
            }

            R.id.cv_over_other_apps -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M /*&& !Settings.canDrawOverlays(this)*/) {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${mContext.packageName}"))
                    startActivity(intent)
                }
            }

            R.id.cv_camera -> {
                openAppInfo()
            }

            R.id.cv_loc -> {
                openAppInfo()
            }

            R.id.cv_phone -> {
                openAppInfo()
            }

            R.id.cv_gallery -> {
                openAppInfo()
            }

            R.id.cv_audio -> {
                openAppInfo()
            }

            R.id.cv_calender -> {
                openAppInfo()
            }
            R.id.cv_frag_settings_batOpti->{
                var dev1= AppUtils.getDeviceName()
                val dev2 = Build.MANUFACTURER
                var dev3 = dev1 + " " + dev2
                if(dev3.contains("OPPO",ignoreCase = true) || dev3.contains("Vivo",ignoreCase = true)){
                    val intent = Intent()
                    val packageName = mContext.packageName
                    val pm = mContext.getSystemService(Context.POWER_SERVICE) as PowerManager
                    var t=pm.isIgnoringBatteryOptimizations(packageName)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !pm.isIgnoringBatteryOptimizations(packageName)) {
                        Handler().postDelayed(Runnable {
                            println("battery hit 175")
                            startActivityForResult(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),175) }, 1000)
                        return
                    }else{
                        Toaster.msgShort(mContext,"Battery already optimized.")
                    }
                }else{
                    Toaster.msgShort(mContext,"Not Compatible setting for your device.")
                }
        }
    }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 175){
            println("battery get 175")
            checkBatteryOptiSettings()
            return
        }
    }

    fun checkBatteryOptiSettings(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName = mContext.packageName
            val pm = mContext.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                CommonDialog.getInstance(getString(R.string.app_name), "You must select the option 'Don't Optimise' to use this app. " ,
                    "Cancel", "Ok", false, object : CommonDialogClickListener {
                        override fun onLeftClick() {

                        }
                        override fun onRightClick(editableData: String) {
                            goTONextActi()
                        }
                    }).show((mContext as DashboardActivity).supportFragmentManager, "")
                println("battery dialog scr")
            } else{
                println("battery next scr")
                goTONextActi()
            }
        }
    }

    fun goTONextActi(){
        val intent = Intent()
        val packageName = mContext.packageName
        val pm = mContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        var t=pm.isIgnoringBatteryOptimizations(packageName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !pm.isIgnoringBatteryOptimizations(packageName)) {
            Handler().postDelayed(Runnable {
                println("battery hit 175")
                startActivityForResult(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),175) }, 1000)
            return
        }
    }

    private fun openAppInfo() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", mContext.packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}