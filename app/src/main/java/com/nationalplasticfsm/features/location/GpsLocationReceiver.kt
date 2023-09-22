package com.nationalplasticfsm.features.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.text.TextUtils
import android.util.Log
import com.nationalplasticfsm.app.AppDatabase
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.domain.GpsStatusEntity
import com.nationalplasticfsm.app.domain.PerformanceEntity
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.app.utils.AppUtils.Companion.gpsDisabledTime
import com.nationalplasticfsm.app.utils.AppUtils.Companion.gpsEnabledTime
import com.nationalplasticfsm.app.utils.AppUtils.Companion.gpsOffTime
import com.nationalplasticfsm.app.utils.AppUtils.Companion.gpsOnTime
import com.nationalplasticfsm.app.utils.AppUtils.Companion.isGpsOffCalled
import com.nationalplasticfsm.app.utils.AppUtils.Companion.isGpsReceiverCalled
import com.nationalplasticfsm.app.utils.AppUtils.Companion.isOnReceived
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Saikat on 24-10-2018.
 */
// Revision History
// 1.0 GpsLocationReceiver AppV 4.1.3 Saheli    26/04/2023 mantis 0025932 Log file update in service classes for GPS on off time.
class GpsLocationReceiver : BroadcastReceiver() {

    /*private var gpsOnTime: Long = 0
    private var gpsOffTime: Long = 0*/
    private var myCalendar = Calendar.getInstance(Locale.ENGLISH)
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    override fun onReceive(context: Context, intent: Intent) {
            Timber.d("GpsLocationReceiver onReceive")
        try {
            if (intent.action!!.matches("android.location.PROVIDERS_CHANGED".toRegex())) {
                /*Toast.makeText(context, "in android.location.PROVIDERS_CHANGED", Toast.LENGTH_SHORT).show();
            Intent pushIntent = new Intent(context, LocalService.class);
            context.startService(pushIntent);*/

                if (TextUtils.isEmpty(Pref.user_id))
                    return

                if (!isGpsReceiverCalled)
                    isGpsReceiverCalled = true

                if (!isOnReceived) {
                    isOnReceived = true
                    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    Timber.d("GpsLocationReceiver onReceive ${manager.isProviderEnabled(LocationManager.GPS_PROVIDER)}")
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        //Toast.makeText(context, "GPS is disabled!", Toast.LENGTH_LONG).show()
                        if (!isGpsOffCalled) {
                            isGpsOffCalled = true
                            Log.e("GpsLocationReceiver", "=======================GPS is disabled======================")
                            // 1.0 GpsLocationReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                            Timber.d("GpsLocationReceiver", "=======================GPS is disabled======================")
                            // 1.0 end rev
                            gpsOffTime = dateFormat.parse(/*"18:14:55"*/AppUtils.getCurrentTime()).time
                            gpsDisabledTime = AppUtils.getCurrentTimeWithMeredian()
                            Log.e("GpsLocationReceiver", "gpsOffTime------------------> " + AppUtils.getTimeInHourMinuteFormat(gpsOffTime))
                            // 1.0 GpsLocationReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                            Timber.d("GpsLocationReceiver", "gpsOffTime------------------> " + AppUtils.getTimeInHourMinuteFormat(gpsOffTime)+"gpsDisabledTime"+gpsDisabledTime)
                            // 1.0 end rev
                            val local_intent = Intent()
                            local_intent.action = AppUtils.gpsDisabledAction
                            context.sendBroadcast(local_intent)
                        }
                    } else {
                        //Toast.makeText(context, "GPS is enabled!", Toast.LENGTH_LONG).show()
                        if (isGpsOffCalled) {
                            isGpsOffCalled = false
                            Log.e("GpsLocationReceiver", "=======================GPS is enabled========================")
                            // 1.0 GpsLocationReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                            Timber.d("GpsLocationReceiver", "=======================GPS is enabled========================")
                            // 1.0 end rev
                            gpsOnTime = dateFormat.parse(AppUtils.getCurrentTime()).time
                            gpsEnabledTime = AppUtils.getCurrentTimeWithMeredian()
                            Log.e("GpsLocationReceiver", "gpsOnTime---------------------> " + AppUtils.getTimeInHourMinuteFormat(gpsOnTime))
                            // 1.0 GpsLocationReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                            Timber.d("GpsLocationReceiver", "gpsOnTime---------------------> " + AppUtils.getTimeInHourMinuteFormat(gpsOnTime)+"gpsEnabledTime"+gpsEnabledTime)
                            // 1.0 end rev
                            val local_intent = Intent()
                            local_intent.action = AppUtils.gpsEnabledAction
                            context.sendBroadcast(local_intent)
                        }
                    }

                    val performance = AppDatabase.getDBInstance()!!.performanceDao().getTodaysData(AppUtils.getCurrentDateForShopActi())
                    if (performance == null) {
                        if ((gpsOnTime - gpsOffTime) > 0) {
                            val performanceEntity = PerformanceEntity()
                            performanceEntity.date = AppUtils.getCurrentDateForShopActi()
                            performanceEntity.gps_off_duration = (gpsOnTime - gpsOffTime).toString()
                            Log.e("GpsLocationReceiver", "duration----------------> " + AppUtils.getTimeInHourMinuteFormat(gpsOnTime - gpsOffTime))
                            // 1.0 GpsLocationReceiver AppV 4.1.3 mantis 0025932 Log file update in service classes for GPS on off time.
                            Timber.d("GpsLocationReceiver", "duration----------------> " + AppUtils.getTimeInHourMinuteFormat(gpsOnTime - gpsOffTime))
                            // 1.0 end rev
                            AppDatabase.getDBInstance()!!.performanceDao().insert(performanceEntity)
                            saveGPSStatus((gpsOnTime - gpsOffTime).toString())
                            gpsOnTime = 0
                            gpsOffTime = 0
                        }
                    } else {
                        if (TextUtils.isEmpty(performance.gps_off_duration)) {
                            if ((gpsOnTime - gpsOffTime) > 0) {
                                AppDatabase.getDBInstance()!!.performanceDao().updateGPSoffDuration((gpsOnTime - gpsOffTime).toString(), AppUtils.getCurrentDateForShopActi())
                                Log.e("GpsLocationReceiver", "duration----------------> " + AppUtils.getTimeInHourMinuteFormat(gpsOnTime - gpsOffTime))
                                saveGPSStatus((gpsOnTime - gpsOffTime).toString())
                                gpsOnTime = 0
                                gpsOffTime = 0
                            }
                        } else {
                            if ((gpsOnTime - gpsOffTime) > 0) {
                                val duration = gpsOnTime - gpsOffTime
                                val totalDuration = performance.gps_off_duration?.toLong()!! + duration
                                Log.e("GpsLocationReceiver", "duration----------------> " + AppUtils.getTimeInHourMinuteFormat(totalDuration))
                                AppDatabase.getDBInstance()!!.performanceDao().updateGPSoffDuration(totalDuration.toString(), AppUtils.getCurrentDateForShopActi())
                                saveGPSStatus(duration.toString())
                                gpsOnTime = 0
                                gpsOffTime = 0
                            }
                        }
                    }
                    isOnReceived = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveGPSStatus(duration: String) {
        val gpsStatus = GpsStatusEntity()
        val random = Random()
        val m = random.nextInt(9999 - 1000) + 1000

        gpsStatus.gps_id = Pref.user_id + "_" + m + m
        gpsStatus.date = AppUtils.getCurrentDateForShopActi()
        gpsStatus.gps_off_time = gpsDisabledTime
        gpsStatus.gps_on_time = gpsEnabledTime
        gpsStatus.duration = duration
        AppDatabase.getDBInstance()!!.gpsStatusDao().insert(gpsStatus)
        gpsDisabledTime = ""
        gpsEnabledTime = ""
        isGpsReceiverCalled = false
    }
}
