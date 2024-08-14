package com.breezenationalplasticfsm.app

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.FTStorageUtils
import com.breezenationalplasticfsm.features.location.LocationFuzedService
import com.breezenationalplasticfsm.features.location.LocationJobService
import timber.log.Timber

/**
 * Created by Saikat on 30-04-2019.
 */
//Revision History
// 1.0 NewAlarmReceiver AppV 4.0.7 Saheli    03/03/2023 Timber Log Implementation
class NewAlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onReceive(context: Context, intent: Intent) {
        println("tag_service_call_check onReceive")
        if (intent.hasExtra("request_code")) {
            if (intent.getIntExtra("request_code", 0) == 123) {
                println("tag_service_call_check onReceive if")
                Timber.e("Time(NewAlarmReceiver): " + AppUtils.getCurrentDateTime())

                if (FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, context)) {
                    Timber.e("==========Service is running (NewAlarmReceiver)===========")
                    println("tag_service_call_check onReceive service running")
                } else {
                    println("tag_service_call_check onReceive service not running")
                    Timber.e("==========Service is stopped (NewAlarmReceiver)===========")

                    if (Pref.user_id != null && Pref.user_id!!.isNotEmpty()) {

                        if(Pref.IsLeavePressed==true && Pref.IsLeaveGPSTrack == false){
                            return
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                            val componentName = ComponentName(context, LocationJobService::class.java)
                            val jobInfo = JobInfo.Builder(12, componentName)
                                    //.setRequiresCharging(true)
                                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                    //.setRequiresDeviceIdle(true)
                                    .setOverrideDeadline(1000)
                                    .build()

                            Timber.d("TAG_CHECK_LOC_SERVICE_STATUS")
                            val resultCode = jobScheduler.schedule(jobInfo)

                            if (resultCode == JobScheduler.RESULT_SUCCESS) {
//                                XLog.d("===============================Job scheduled (NewAlarmReceiver)============================")
                                Timber.d("===============================Job scheduled (NewAlarmReceiver)============================")
                            } else {
//                                XLog.d("=====================Job not scheduled (NewAlarmReceiver)====================================")
                                Timber.d("=====================Job not scheduled (NewAlarmReceiver)====================================")
                            }
                        } else {
                            val serviceLauncher = Intent(context, LocationFuzedService::class.java)
                            Timber.d("TAG_CHECK_LOC_SERVICE_STATUS")
                            context.startService(serviceLauncher)
                        }
                    }
                }
            }
        }
    }
}