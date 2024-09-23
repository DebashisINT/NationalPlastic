package com.breezefieldnationalplastic.features.powerSavingSettings

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import net.alexandroid.gps.GpsStatusDetector


class PowerSavingSettingsActivity : BaseActivity(){

    private lateinit var mContext: Context
    private lateinit var tv_power_gps: AppCustomTextView
    private lateinit var rl_power_main: RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_power_savings_screen)
        rl_power_main = findViewById(R.id.rl_power_main)
        rl_power_main.setOnClickListener(null)
        tv_power_gps =findViewById(R.id.tv_power_gps)
        tv_power_gps.setOnClickListener({
            startActivity(Intent(Settings.ACTION_SETTINGS))
        })
    }








}