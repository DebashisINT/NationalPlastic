package com.breezenationalplasticfsm

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.FTStorageUtils
import com.breezenationalplasticfsm.features.location.LocationFuzedService
import com.breezenationalplasticfsm.features.location.LocationJobService
import timber.log.Timber

class AlarmServiceRestart: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("AlarmServiceRestart onReceive")

        Timber.d("AlarmServiceRestart running : Time :" + AppUtils.getCurrentDateTime())
        if (FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, context)) {
            Timber.d("AlarmServiceRestart loc service check service running : Time :" + AppUtils.getCurrentDateTime())
        }else{
            Timber.d("AlarmServiceRestart loc service check service not running : Time :" + AppUtils.getCurrentDateTime())
            Timber.d("restarting loc service")
            restartLocationService(context!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun restartLocationService(context: Context) {
        try {
            if(Pref.IsLeavePressed== true && Pref.IsLeaveGPSTrack == false){
                return
            }
            if (Pref.user_id != null && Pref.user_id!!.isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                    val componentName = ComponentName(context, LocationJobService::class.java)
                    val jobInfo = JobInfo.Builder(12, componentName)
                        //.setRequiresCharging(true)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        //.setRequiresDeviceIdle(true)
                        .setOverrideDeadline(1000)
                        .build()

                    Timber.d("tag AlarmServiceRestart")
                    val resultCode = jobScheduler.schedule(jobInfo)

                    if (resultCode == JobScheduler.RESULT_SUCCESS) {
                        Timber.d("===============================From AlarmServiceRestart LocationFuzedService   Job scheduled " + AppUtils.getCurrentDateTime() + "============================")
                    } else {
                        Timber.d("=====================From AlarmServiceRestart LocationFuzedService Job not scheduled " + AppUtils.getCurrentDateTime() + "====================================")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d("error AlarmServiceRestart ${e.printStackTrace()}")
        }
    }


}