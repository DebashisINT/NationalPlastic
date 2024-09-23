package com.breezefieldnationalplastic

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.*
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.GpsStatusEntity
import com.breezefieldnationalplastic.app.domain.NewGpsStatusEntity
import com.breezefieldnationalplastic.app.domain.PerformanceEntity
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.FTStorageUtils
import com.breezefieldnationalplastic.features.location.LocationFuzedService
import com.breezefieldnationalplastic.features.location.LocationJobService
import com.breezefieldnationalplastic.mappackage.SendBrod

import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
//Revision History
// 1.0 MonitorService  AppV 4.0.6  Saheli    11/01/2023 GPS_SERVICE_STATUS & NETWORK_STATUS
// 2.0 NewAlarmReceiver AppV 4.0.7 Saheli    03/03/2023 Timber Log Implementation
// 3.0 MonitorService AppV 4.0.8 Suman    18/04/2023 onDestroy updation 0025875
// 4.0 MonitorService AppV 4.1.3 Saheli    26/04/2023 mantis 0025932 Log file update in service classes for GPS on off time.
// 5.0 MonitorService AppV 4.1.3 Suman    07/04/2023 mantis 26046 monitor broadcast restrictuin with Xiomi for normal flow.

class MonitorService:Service() {
    private val monitorNotiID = 201
    private var monitorBroadcast : MonitorBroadcast? = null
    var powerSaver:Boolean = false
    var isFirst:Boolean = true

    var timer : Timer? = null
    private val POWER_SAVE_MODE_SETTING_NAMES = arrayOf(
            "SmartModeStatus", // huawei setting name
            "POWER_SAVE_MODE_OPEN" // xiaomi setting name
    )

    @SuppressLint("NewApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//        if (intent != null) {
//            val action = intent.action
//            if (action != null) {
//                if (action == CustomConstants.START_MONITOR_SERVICE) {
//                    serviceStatusActionable()
//                } else if (action == CustomConstants.STOP_MONITOR_SERVICE) {
//                    //stopMonitorService()
//                }
//            }
//        }
//        return super.onStartCommand(intent, flags, startId)

        timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                println("abc - 3 sec method");
                serviceStatusActionable()

            }
        }
        timer!!.schedule(task, 0, 8000)
        //timer!!.schedule(task, 0, 50000)

        // 15 mins is 60000 * 15


        // 15 mins is 60000 * 15R
        return START_STICKY
    }

    fun serviceStatusActionable() {

        Timber.d("MonitorService running : Time :" + AppUtils.getCurrentDateTime())
        try {
            if (FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, this)) {
                Timber.d("MonitorService loc service check service running : Time :" + AppUtils.getCurrentDateTime())
            }else{
                Timber.d("MonitorService loc service check service not running : Time :" + AppUtils.getCurrentDateTime())
                Timber.d("restarting loc service")
                restartLocationService()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d("tag_error ${e.printStackTrace()}")
        }
        return

        Log.e("abc", "startabc")
        monitorBroadcast = MonitorBroadcast()

        //Begin 5.0 MonitorService AppV 4.1.3 Suman    07/04/2023 mantis 26046 monitor broadcast restrictuin with Xiomi for normal flow.
        var manu = Build.MANUFACTURER.toUpperCase(Locale.getDefault())
        //End of 5.0 MonitorService AppV 4.1.3 Suman    07/04/2023 mantis 26046 monitor broadcast restrictuin with Xiomi for normal flow.
        var powerMode: String = ""
        val powerManager = this.getSystemService(POWER_SERVICE) as PowerManager
        //Begin 5.0 MonitorService AppV 4.1.3 Suman    07/04/2023 mantis 26046 monitor broadcast restrictuin with Xiomi for normal flow.
        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && !manu.equals("XIAOMI")) {
            //End of 5.0 MonitorService AppV 4.1.3 Suman    07/04/2023 mantis 26046 monitor broadcast restrictuin with Xiomi for normal flow.
            if (powerManager.isPowerSaveMode) {
                Pref.PowerSaverStatus = "On"
                powerMode = "Power Save Mode ON"

                Log.e("pww", "Power Save Mode ON")
                Timber.d("pww : Power Save Mode ON" + " Time :" + AppUtils.getCurrentDateTime())

                Handler(Looper.getMainLooper()).postDelayed({
                    if (Pref.GPSAlertGlobal) {
                        if (Pref.GPSAlert) {
                            SendBrod.sendBrod(this)

                            /*val intent = Intent(this, PowerSavingSettingsActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)*/

                        }
                    }
                }, 500)

                powerSaver = true

                calculategpsStatus(false)
                //sendGPSOffBroadcast()
            } else {
                //Log.e("pww", "Power Save Mode OFF" )
                //Timber.d("pww : Power Save Mode OFF" + " Time :" + AppUtils.getCurrentDateTime())
                Pref.PowerSaverStatus = "Off"
                if (powerSaver) {
                    calculategpsStatus(true)
                }


                powerMode = "Power Save Mode OFF"

                powerSaver = false
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!Pref.isLocFuzedBroadPlaying) {
                        SendBrod.stopBrod(this)

                        /*   val intent = Intent(this, DashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)*/

                    }

                }, 500)
                //cancelGpsBroadcast()
            }
        }
        // 1.0 MonitorService  AppV 4.0.6 GPS_SERVICE_STATUS & NETWORK_STATUS
        val newNetStatusObj = NewGpsStatusEntity()
        if (shouldShopActivityUpdate()) {
            newNetStatusObj.date_time = AppUtils.getCurrentDateTime()
            newNetStatusObj.network_status = if (AppUtils.isOnline(this)) "Online" else "Offline"
            if (FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, this)) {
                //Timber.d("MonitorService LocationFuzedService : " + "true" + "," + " Time :" + AppUtils.getCurrentDateTime())
                //Timber.d("MonitorService Power Save Mode Status : " + powerMode + "," + " Time :" + AppUtils.getCurrentDateTime())
                /*if(powerSaver){
                    sendGPSOffBroadcast()
                }else{
                    cancelGpsBroadcast()
                }*/
                newNetStatusObj.gps_service_status = "Started"
            } else {
                newNetStatusObj.gps_service_status = "Stopped"
                if (!FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, this)) {
                    restartLocationService()
                }
                Timber.d("MonitorService LocationFuzedService : " + "false" + "," + " Time :" + AppUtils.getCurrentDateTime())
                Timber.d("MonitorService  Power Save Mode Status : " + powerMode + "," + " Time :" + AppUtils.getCurrentDateTime())
                Timber.d("Monitor Service Stopped" + "" + "," + " Time :" + AppUtils.getCurrentDateTime())
                if (!isFirst) {
                    Log.e("abc", "abc stoptimer")
                    timer!!.cancel()
                }
                isFirst = false
            }

            Log.e("inside outside shouldGpsNetSyncDuration", AppUtils.getCurrentDateTime())
            if (shouldGpsNetSyncDuration() && !Pref.GPSNetworkIntervalMins.equals("0")) {
                Log.e("inside shouldGpsNetSyncDuration", AppUtils.getCurrentDateTime())
                AppDatabase.getDBInstance()?.newGpsStatusDao()?.insert(newNetStatusObj)
            }

        }


        if (manu.equals("XIAOMI")) {
            if (isPowerSaveModeCompat(this)) {

                println("pww - Power Save Mode ON xm")
                Log.e("pww", "Power Save Mode ON xm")
                Timber.d("pww : Power Save Mode ON xm" + " Time :" + AppUtils.getCurrentDateTime())


                powerMode = "Power Save Mode ON"

                if (Pref.GPSAlertGlobal) {
                    if (Pref.GPSAlert) {
                        SendBrod.sendBrod(this)

                        /*   val intent = Intent(this, PowerSavingSettingsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)*/
                    }
                }
                //sendGPSOffBroadcast()
            } else {

                println("pww - Power Save Mode OFF xm")
                // Log.e("pww", "Power Save Mode OFF xm" )
                //Timber.d("pww : Power Save Mode OFF xm" + " Time :" + AppUtils.getCurrentDateTime())


                powerMode = "Power Save Mode OFF"
                SendBrod.stopBrod(this)

                /*        val intent = Intent(this, DashboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)*/

                //cancelGpsBroadcast()
            }
        }


        if(shouldShopActivityUpdate()){
            if (FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, this)) {
                Timber.d("MonitorService LocationFuzedService : " + "trueee" + "," + " Time :" + AppUtils.getCurrentDateTime())
                //Timber.d("MonitorService Power Save Mode Status : " + powerMode + "," + " Time :" + AppUtils.getCurrentDateTime())
                /*if(powerSaver){
                    sendGPSOffBroadcast()
                }else{
                    cancelGpsBroadcast()
                }*/

                println("monitor_track on")
            }else{
                println("monitor_track off")
                if (!FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, this)) {
                    restartLocationService()
                }

                Timber.d("MonitorService LocationFuzedService : " + "false" + "," + " Time :" + AppUtils.getCurrentDateTime())
                Timber.d("MonitorService  Power Save Mode Status : " + powerMode + "," + " Time :" + AppUtils.getCurrentDateTime())
                Timber.d("Monitor Service Stopped" + "" + "," + " Time :" + AppUtils.getCurrentDateTime())
                if(!isFirst){
                    Log.e("abc", "abc stoptimer" )
                    timer!!.cancel()
                }
                isFirst=false
            }
        }

    }
    // 1.0 MonitorService  AppV 4.0.6 GPS_SERVICE_STATUS & NETWORK_STATUS
    private fun shouldGpsNetSyncDuration(): Boolean {
        AppUtils.changeLanguage(this,"en")

        var t = Math.abs(System.currentTimeMillis() - Pref.prevGpsNetSyncTimeStamp)

        return if (Math.abs(System.currentTimeMillis() - Pref.prevGpsNetSyncTimeStamp) > 1000 * 60 * Pref.GPSNetworkIntervalMins.toInt()) {
            Pref.prevGpsNetSyncTimeStamp = System.currentTimeMillis()
            //changeLocale()
            true
            //server timestamp is within 10 minutes of current system time
        } else {
            //changeLocale()
            false
        }
    }

    fun sendGPSOffBroadcast(){
        if(Pref.user_id.toString().length > 0){
            Timber.d("MonitorService Called for Battery Broadcast :  Time :" + AppUtils.getCurrentDateTime())
            //var notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            //notificationManager.cancel(monitorNotiID)
            MonitorBroadcast.isSound=Pref.GPSAlertwithSound
            var intent: Intent = Intent(this, MonitorBroadcast::class.java)
            intent.putExtra("notiId", monitorNotiID)
            intent.putExtra("fuzedLoc", "Fuzed Stop.")
            this.sendBroadcast(intent)
        }
    }


    fun cancelGpsBroadcast(){
        if (monitorNotiID != 0){
            if(MonitorBroadcast.player!=null){
                MonitorBroadcast.player.stop()
                MonitorBroadcast.player=null
                MonitorBroadcast.vibrator.cancel()
                MonitorBroadcast.vibrator=null
            }
            var notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(monitorNotiID)
        }
    }

    private fun isPowerSaveModeCompat(context: Context): Boolean {
        for (name in POWER_SAVE_MODE_SETTING_NAMES) {
            val mode = Settings.System.getInt(context.contentResolver, name, -1)
            if (mode != -1) {
                return POWER_SAVE_MODE_VALUES[Build.MANUFACTURER.toUpperCase(Locale.getDefault())] == mode
            }
        }
        return false
    }

    private val POWER_SAVE_MODE_VALUES = mapOf(
            "HUAWEI" to 4,
            "XIAOMI" to 1
    )

    override fun stopService(name: Intent?): Boolean {
        println("monitor_s stopService")
        stopForeground(true)
        stopSelf()
        return super.stopService(name)
    }

    override fun onDestroy() {
        try{
            println("monitor_s onDestroy")
            super.onDestroy()
            // 3.0 MonitorService AppV 4.0.8 Suman    18/04/2023 onDestroy updation 0025875
            stopForeground(true)
            stopSelf()
            timer!!.cancel()
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    @SuppressLint("NewApi")
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        //serviceStatusActionable()
    }

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not Yet Implemented")
    }

    private fun checkGpsStatus() {
        val locationManager: LocationManager =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else {

        }
    }


    fun shouldShopActivityUpdate(): Boolean {
        return if (Math.abs(System.currentTimeMillis() - Pref.prevShopActivityTimeStampMonitorService) > 10000) {
            Pref.prevShopActivityTimeStampMonitorService = System.currentTimeMillis()
            true
            //server timestamp is within 5 minutes of current system time
        } else {
            false
        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun restartLocationService() {
        try {
            if(Pref.IsLeavePressed== true && Pref.IsLeaveGPSTrack == false){
                return
            }
            val serviceLauncher = Intent(this, LocationFuzedService::class.java)
            Timber.d("TAG_CHECK_LOC_SERVICE_STATUS")

            if (Pref.user_id != null && Pref.user_id!!.isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                    val componentName = ComponentName(this, LocationJobService::class.java)
                    val jobInfo = JobInfo.Builder(12, componentName)
                        //.setRequiresCharging(true)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        //.setRequiresDeviceIdle(true)
                        .setOverrideDeadline(1000)
                        .build()

                    Timber.d("TAG_CHECK_LOC_SERVICE_STATUS")
                    val resultCode = jobScheduler.schedule(jobInfo)

                    if (resultCode == JobScheduler.RESULT_SUCCESS) {
                        Timber.d("===============================From MonitorS LocationFuzedService   Job scheduled (Base Activity) " + AppUtils.getCurrentDateTime() + "============================")
                    } else {
                        Timber.d("=====================From MonitorS LocationFuzedService Job not scheduled (Base Activity) " + AppUtils.getCurrentDateTime() + "====================================")
                    }
                } else
                    startService(serviceLauncher)
            } else {
                /*stopService(serviceLauncher)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                    jobScheduler.cancelAll()
                    Timber.d("===============================From MonitorS LocationFuzedService Job scheduler cancel (Base Activity)" + AppUtils.getCurrentDateTime() + "============================")
                }

                AlarmReceiver.stopServiceAlarm(this, 123)
                Timber.d("===========From MonitorS LocationFuzedService Service alarm is stopped (Base Activity)================")*/
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
                    //4.0 MonitorService AppV 4.1.3  mantis 0025932 Log file update in service classes for GPS on off time.
                    Timber.d("GpsLocationReceiver", "===========GPS is disabled=============")
                    //4.0 end rev
                    AppUtils.gpsOffTime = dateFormat.parse(/*"18:14:55"*/AppUtils.getCurrentTime()).time
                    AppUtils.gpsDisabledTime = AppUtils.getCurrentTimeWithMeredian()
                    Log.e("GpsLocationReceiver", "gpsOffTime------------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOffTime)+"gpsOff" +AppUtils.gpsDisabledTime)
                    //4.0 MonitorService AppV 4.1.3  mantis 0025932 Log file update in service classes for GPS on off time.
                    Timber.d("GpsLocationReceiver", "gpsOffTime------------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOffTime))
                    //4.0 end rev
                    /*val local_intent = Intent()
                    local_intent.action = AppUtils.gpsDisabledAction
                    sendBroadcast(local_intent)*/
                }
            } else {
                if (AppUtils.isGpsOffCalled) {
                    AppUtils.isGpsOffCalled = false
                    Log.e("GpsLocationReceiver", "===========GPS is enabled================")
                    AppUtils.gpsOnTime = dateFormat.parse(AppUtils.getCurrentTime()).time
                    AppUtils.gpsEnabledTime = AppUtils.getCurrentTimeWithMeredian()
                    //4.0 MonitorService AppV 4.1.3  mantis 0025932 Log file update in service classes for GPS on off time.
                    Timber.d("GpsLocationReceiver", "gpsOnTime---------------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime)+"gpsOn" +AppUtils.gpsEnabledTime)
                    //4.0 end rev
                    Log.e("GpsLocationReceiver", "gpsOnTime---------------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime))

                    /*val local_intent = Intent()
                    local_intent.action = AppUtils.gpsEnabledAction
                    sendBroadcast(local_intent)*/
                }
            }

            val performance = AppDatabase.getDBInstance()!!.performanceDao().getTodaysData(AppUtils.getCurrentDateForShopActi())
            if (performance == null) {
                if ((AppUtils.gpsOnTime - AppUtils.gpsOffTime) > 0) {
                    val performanceEntity = PerformanceEntity()
                    performanceEntity.date = AppUtils.getCurrentDateForShopActi()
                    performanceEntity.gps_off_duration = (AppUtils.gpsOnTime - AppUtils.gpsOffTime).toString()
                    Log.e("GpsLocationReceiver", "duration----------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime - AppUtils.gpsOffTime))
                    //4.0 MonitorService AppV 4.1.3  mantis 0025932 Log file update in service classes for GPS on off time.
                    Timber.d("GpsLocationReceiver", "duration----------------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime - AppUtils.gpsOffTime))
                    //4.0 end rev
                    AppDatabase.getDBInstance()!!.performanceDao().insert(performanceEntity)
                    saveGPSStatus((AppUtils.gpsOnTime - AppUtils.gpsOffTime).toString())
                    AppUtils.gpsOnTime = 0
                    AppUtils.gpsOffTime = 0
                }
            } else {
                if (TextUtils.isEmpty(performance.gps_off_duration)) {
                    if ((AppUtils.gpsOnTime - AppUtils.gpsOffTime) > 0) {
                        AppDatabase.getDBInstance()!!.performanceDao().updateGPSoffDuration((AppUtils.gpsOnTime - AppUtils.gpsOffTime).toString(), AppUtils.getCurrentDateForShopActi())
                        Log.e("GpsLocationReceiver", "duration----------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime - AppUtils.gpsOffTime))
                        //4.0 MonitorService AppV 4.1.3  mantis 0025932 Log file update in service classes for GPS on off time.
                        Timber.d("GpsLocationReceiver", "duration----------> " + AppUtils.getTimeInHourMinuteFormat(AppUtils.gpsOnTime - AppUtils.gpsOffTime))
                        //4.0 end rev
                        saveGPSStatus((AppUtils.gpsOnTime - AppUtils.gpsOffTime).toString())
                        AppUtils.gpsOnTime = 0
                        AppUtils.gpsOffTime = 0
                    }
                } else {
                    if ((AppUtils.gpsOnTime - AppUtils.gpsOffTime) > 0) {
                        val duration = AppUtils.gpsOnTime - AppUtils.gpsOffTime
                        val totalDuration = performance.gps_off_duration?.toLong()!! + duration
                        Log.e("GpsLocationReceiver", "duration-------> " + AppUtils.getTimeInHourMinuteFormat(totalDuration))
                        //4.0 MonitorService AppV 4.1.3  mantis 0025932 Log file update in service classes for GPS on off time.
                        Timber.d("GpsLocationReceiver", "duration-------> " + AppUtils.getTimeInHourMinuteFormat(totalDuration))
                        //4.0 end rev
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
        AppDatabase.getDBInstance()!!.gpsStatusDao().insert(gpsStatus)
        AppUtils.gpsDisabledTime = ""
        AppUtils.gpsEnabledTime = ""
        AppUtils.isGpsReceiverCalled = false
    }

}