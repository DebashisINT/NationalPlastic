package com.breezefieldnationalplastic.features.dashboard.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import com.breezefieldnationalplastic.app.AlarmReceiver
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.GpsStatusEntity
import com.breezefieldnationalplastic.app.domain.PerformanceEntity
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.AppUtils.Companion.reasontagforGPS
import com.breezefieldnationalplastic.features.location.LocationWizard
import com.breezefieldnationalplastic.mappackage.SendBrod

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

// Revision History
// 1.0 SystemEventReceiver AppV 4.1.3 Saheli    26/04/2023 mantis 0025932 Log file update in service classes for GPS on off time.
class SystemEventReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED" || intent.action == "android.intent.action.AIRPLANE_MODE" ||
                intent.action == "android.intent.action.ACTION_SHUTDOWN") {

            if (intent.action == "android.intent.action.BOOT_COMPLETED")
                Timber.e("=======================Boot Completed successfully ${AppUtils.getCurrentDateTime()} (SystemEventReceiver)=======================")
            else if(intent.action == "android.intent.action.AIRPLANE_MODE") {
                var text = ""

                if (Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0){
                    text = "Airplane Mode is On "
                    AppUtils.reasontagforGPS = "Airplane Mode is On "
                    SendBrod.sendBrod(context)
                    calculategpsStatus(false)
                }
                else{
                    try {
                        Timber.e("First time airplane off detect")
                        text = "Airplane Mode is Off "
                        AppUtils.reasontagforGPS = "Airplane Mode is Off "
                        SendBrod.stopBrod(context)
                        calculategpsStatus(true)
                    }catch (ex : Exception){
                        Timber.e("First time airplane off detect ${ex.message}")
                    }

                }
                Timber.e("========================${text + AppUtils.getCurrentDateTime()}=======================")

            }else if(intent.action == "android.intent.action.ACTION_SHUTDOWN"){
                // code start by puja 05.04.2024 mantis id - 27333 v4.2.6
                /*  val locationName = LocationWizard.getLocationName(context, Pref.latitude!!.toDouble(), Pref.longitude!!.toDouble())
                  Timber.e("\n======================== \n Phone Shutdown || DateTime : ${AppUtils.getCurrentDateTime()} || Location : last_lat: ${Pref.latitude} || last_long: ${Pref.longitude} || LocationName ${locationName} \n=======================")
                  AppUtils.reasontagforGPS = "Phone shutdown"*/
                if (Pref.latitude!="" && Pref.longitude!="" ) {
                    val locationName = LocationWizard.getLocationName(
                        context,
                        Pref.latitude!!.toDouble(),
                        Pref.longitude!!.toDouble()
                    )
                    Timber.e("\n======================== \n Phone Shutdown || DateTime : ${AppUtils.getCurrentDateTime()} || Location : last_lat: ${Pref.latitude} || last_long: ${Pref.longitude} || LocationName ${locationName} \n=======================")
                }
                else{
                    Timber.e("\n LatLong not found")
                }
                AppUtils.reasontagforGPS = "Phone shutdown"
                // code end by puja 05.04.2024 mantis id - 27333 v4.2.6
            }else if(intent.action == "android.os.action.POWER_SAVE_MODE_CHANGED"){
                Timber.e("\n android.os.action.POWER_SAVE_MODE_CHANGED")
                AppUtils.reasontagforGPS = "Power save mode on"
            }

        }
    }

    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private fun calculategpsStatus(gpsStatus: Boolean) {

        if (!AppUtils.isOnReceived) {
            Timber.e("First time airplane off detect working")
            AppUtils.isOnReceived = true

            if (!gpsStatus) {
                //Toast.makeText(context, "GPS is disabled!", Toast.LENGTH_LONG).show()
                if (!AppUtils.isGpsOffCalled) {
                    AppUtils.isGpsOffCalled = true
                    Log.e("GpsLocationReceiver", "===========GPS is disabled=============")
                    // 1.0 SystemEventReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                    Timber.d("GpsLocationReceiver", "===========GPS is disabled=============")
                    // 1.0 rev end.
                    AppUtils.gpsOffTime = dateFormat.parse(/*"18:14:55"*/AppUtils.getCurrentTime()).time
                    AppUtils.gpsDisabledTime = AppUtils.getCurrentTimeWithMeredian()
                    Log.e("GpsLocationReceiver", "gpsOffTime------------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOffTime))
                    // 1.0 SystemEventReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                    Timber.d("GpsLocationReceiver", "gpsOffTime------------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOffTime)+"gpsoff"+AppUtils.gpsOffTime)
                    // 1.0 rev end.
                    /*val local_intent = Intent()
                    local_intent.action = AppUtils.gpsDisabledAction
                    sendBroadcast(local_intent)*/
                    AppUtils.reasontagforGPS = "GPS is disabled"
                }
            }
            else {
                if (AppUtils.isGpsOffCalled) {
                    AppUtils.isGpsOffCalled = false
                    Log.e("GpsLocationReceiver", "===========GPS is enabled================")
                    // 1.0 SystemEventReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                    Timber.d("GpsLocationReceiver", "===========GPS is enabled================")
                    // 1.0 rev end.
                    AppUtils.gpsOnTime = dateFormat.parse(AppUtils.getCurrentTime()).time
                    AppUtils.gpsEnabledTime = AppUtils.getCurrentTimeWithMeredian()
                    Log.e("GpsLocationReceiver", "gpsOnTime---------------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime))
                    // 1.0 SystemEventReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                    Timber.d("GpsLocationReceiver", "gpsOnTime---------------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime)+"gpsOnTime"+AppUtils.gpsOnTime)
                    // 1.0 rev end.
                    /*val local_intent = Intent()
                    local_intent.action = AppUtils.gpsEnabledAction
                    sendBroadcast(local_intent)*/
                    AppUtils.reasontagforGPS = "GPS is enabled"

                }
            }

            val performance = AppDatabase.getDBInstance()!!.performanceDao().getTodaysData(AppUtils.getCurrentDateForShopActi())
            if (performance == null) {
                if ((AppUtils.gpsOnTime - AppUtils.gpsOffTime) > 0) {
                    val performanceEntity = PerformanceEntity()
                    performanceEntity.date = AppUtils.getCurrentDateForShopActi()
                    performanceEntity.gps_off_duration = (AppUtils.gpsOnTime - AppUtils.gpsOffTime).toString()
                    Log.e("GpsLocationReceiver", "duration----------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime - AppUtils.gpsOffTime))
                    // 1.0 SystemEventReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                    Timber.d("GpsLocationReceiver", "duration----------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime - AppUtils.gpsOffTime))
                    // 1.0 rev end.
                    AppDatabase.getDBInstance()!!.performanceDao().insert(performanceEntity)
                    saveGPSStatus((AppUtils.gpsOnTime - AppUtils.gpsOffTime).toString())
                    AppUtils.gpsOnTime = 0
                    AppUtils.gpsOffTime = 0
                }
            }
            else {
                if (TextUtils.isEmpty(performance.gps_off_duration)) {
                    if ((AppUtils.gpsOnTime - AppUtils.gpsOffTime) > 0) {
                        AppDatabase.getDBInstance()!!.performanceDao().updateGPSoffDuration((AppUtils.gpsOnTime - AppUtils.gpsOffTime).toString(), AppUtils.getCurrentDateForShopActi())
                        Log.e("GpsLocationReceiver", "duration----------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime - AppUtils.gpsOffTime))
                        // 1.0 SystemEventReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                        Timber.d("GpsLocationReceiver", "duration----------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime - AppUtils.gpsOffTime))
                        // 1.0 rev end.
                        saveGPSStatus((AppUtils.gpsOnTime - AppUtils.gpsOffTime).toString())
                        AppUtils.gpsOnTime = 0
                        AppUtils.gpsOffTime = 0
                    }
                } else {
                    if ((AppUtils.gpsOnTime - AppUtils.gpsOffTime) > 0) {
                        val duration = AppUtils.gpsOnTime - AppUtils.gpsOffTime
                        val totalDuration = performance.gps_off_duration?.toLong()!! + duration
                        Log.e("GpsLocationReceiver", "duration-------> " + AppUtils.getTimeInHourMinuteFormat(totalDuration))
                        // 1.0 SystemEventReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                        Timber.d("GpsLocationReceiver", "duration-------> " + AppUtils.getTimeInHourMinuteFormat(totalDuration))
                        // 1.0 rev end.
                        AppDatabase.getDBInstance()!!.performanceDao().updateGPSoffDuration(totalDuration.toString(), AppUtils.getCurrentDateForShopActi())
                        saveGPSStatus(duration.toString())
                        AppUtils.gpsOnTime = 0
                        AppUtils.gpsOffTime = 0
                    }
                }
            }
            AppUtils.isOnReceived = false
        }
    }

    private fun saveGPSStatus(duration: String) {
        val gpsStatus = GpsStatusEntity()
        val random = Random()
        val m = random.nextInt(9999 - 1000) + 1000

        gpsStatus.gps_id = Pref.user_id + "_" + m + m
        gpsStatus.date = AppUtils.getCurrentDateForShopActi()
        gpsStatus.gps_off_time = AppUtils.gpsDisabledTime
        gpsStatus.gps_on_time = AppUtils.gpsEnabledTime
        gpsStatus.duration = duration
        gpsStatus.reasontagforGPS =  AppUtils.reasontagforGPS
        AppDatabase.getDBInstance()!!.gpsStatusDao().insert(gpsStatus)
        AppUtils.gpsDisabledTime = ""
        AppUtils.gpsEnabledTime = ""
        AppUtils.reasontagforGPS = ""
        AppUtils.isGpsReceiverCalled = false
    }

}