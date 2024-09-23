package com.breezefieldnationalplastic.features.location

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.text.TextUtils

import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.FTStorageUtils
import com.breezefieldnationalplastic.features.dashboard.presentation.SystemEventReceiver
import timber.log.Timber


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
/**
 * Created by riddhi on 7/11/17.
 */
class LocationJobService : JobService() {

    /*private val eventReceiver: SystemEventReceiver by lazy {
        SystemEventReceiver()
    }*/

    companion object {
        private var updateFence = ""

        fun updateFence(s: String) {
            updateFence = s
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(p0: JobParameters?): Boolean {

        if(Pref.IsLeavePressed== true && Pref.IsLeaveGPSTrack == false){
            return true
        }

        /*// Rev 25.0 LocationJobService v 4.2.6 stock optmization mantis 0027421 06-05-2024 Suman begin
        try {
            if (FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, applicationContext)) {
                println("tag_service_call_check LocationJobService LocationFuzedService running")
                return true
            }else{
                println("tag_service_call_check LocationJobService LocationFuzedService not")
            }
        }catch (ex:Exception){
            ex.printStackTrace()
            return true
        }
        // Rev 25.0 LocationJobService v 4.2.6 stock optmization mantis 0027421 06-05-2024 Suman end
        println("tag_service_call_check LocationJobService onStartJob call")
        Timber.d("=============================Start Job " + AppUtils.getCurrentDateTime() + "==============================")*/

        val myIntent = Intent(this, LocationFuzedService::class.java)
        Timber.d("TAG_CHECK_LOC_SERVICE_STATUS")

        if (!TextUtils.isEmpty(updateFence)) {
            val bundle = Bundle()
            bundle.putString("ACTION", "UPDATE_FENCE")
            myIntent.putExtras(bundle)
        }

        /*try {
            startService(myIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            startForegroundService(myIntent)
        }*/
        Timber.d("service_call_tag LocationJobService onStartJob ${AppUtils.getCurrentDateTime()} ")

        try {
            if (FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, this)) {
                Timber.d("MonitorService loc service check service running : Time :" + AppUtils.getCurrentDateTime())
            }else{
                Timber.d("MonitorService loc service check service not running : Time :" + AppUtils.getCurrentDateTime())
                Timber.d("restarting loc service")
                startForegroundService(myIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d("error ${e.printStackTrace()}")
        }

        //startForegroundService(myIntent)

        /*registerReceiver(eventReceiver, IntentFilter().apply {
            addAction("android.intent.action.AIRPLANE_MODE")
            addAction("android.intent.action.BOOT_COMPLETED")
        })*/

        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Timber.d("=========================Stop Job " + AppUtils.getCurrentDateTime() + "============================")

        //unregisterReceiver(eventReceiver)

        if (Pref.user_id != null && Pref.user_id!!.isNotEmpty()) {
            val componentName = ComponentName(this, LocationJobService::class.java)
            val jobInfo = JobInfo.Builder(12, componentName)
                    //.setRequiresCharging(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY/*JobInfo.NETWORK_TYPE_NONE*/)
                    //.setRequiresDeviceIdle(true)
                    //.setPersisted(true)
                    .setOverrideDeadline(1000)
                    .build()

            val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            Timber.d("TAG_CHECK_LOC_SERVICE_STATUS")

            val resultCode = jobScheduler.schedule(jobInfo)

            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Timber.d("========================Job rescheduled (LocationJobService) " + AppUtils.getCurrentDateTime() + "==============================")
            } else {
                Timber.d("========================Job not rescheduled (LocationJobService) " + AppUtils.getCurrentDateTime() + "==========================")
            }
        }

        return true
    }
}