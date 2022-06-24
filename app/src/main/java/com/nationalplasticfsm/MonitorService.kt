package com.nationalplasticfsm

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.*
import android.provider.Settings
import android.util.Log
import com.nationalplasticfsm.Customdialog.CustomDialog
import com.nationalplasticfsm.Customdialog.OnDialogCustomClickListener
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.app.utils.FTStorageUtils
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.features.location.LocationFuzedService
import com.nationalplasticfsm.features.powerSavingSettings.PowerSavingSettingsActivity
import com.nationalplasticfsm.mappackage.SendBrod
import com.elvishew.xlog.XLog
import java.util.*

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

        // 15 mins is 60000 * 15


        // 15 mins is 60000 * 15R
        return START_STICKY
    }

    fun serviceStatusActionable(){
        Log.e("abc", "startabc" )
        monitorBroadcast=MonitorBroadcast()

        var powerMode:String = ""
        val powerManager = this.getSystemService(POWER_SERVICE) as PowerManager
       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if(powerManager.isPowerSaveMode){
                powerMode = "Power Save Mode ON"

                Log.e("pww", "Power Save Mode ON" )
                XLog.d("pww : Power Save Mode ON" + " Time :" + AppUtils.getCurrentDateTime())

                Handler(Looper.getMainLooper()).postDelayed({
                    if(Pref.GPSAlertGlobal){
                        if(Pref.GPSAlert){
                            SendBrod.sendBrod(this)

                            /*val intent = Intent(this, PowerSavingSettingsActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)*/

                        }}
                }, 500)

                powerSaver=true
                //sendGPSOffBroadcast()
            }else{
                //Log.e("pww", "Power Save Mode OFF" )
                //XLog.d("pww : Power Save Mode OFF" + " Time :" + AppUtils.getCurrentDateTime())

                powerMode = "Power Save Mode OFF"

                powerSaver=false
                Handler(Looper.getMainLooper()).postDelayed({
                    if(!Pref.isLocFuzedBroadPlaying){
                        SendBrod.stopBrod(this)

                     /*   val intent = Intent(this, DashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)*/

                    }

                }, 500)
                //cancelGpsBroadcast()
            }
        }

        var manu= Build.MANUFACTURER.toUpperCase(Locale.getDefault())
        if(manu.equals("XIAOMI")){
            if(isPowerSaveModeCompat(this) ){

                println("pww - Power Save Mode ON xm")
                Log.e("pww", "Power Save Mode ON xm" )
                XLog.d("pww : Power Save Mode ON xm" + " Time :" + AppUtils.getCurrentDateTime())


                powerMode = "Power Save Mode ON"

                if(Pref.GPSAlertGlobal){
                    if(Pref.GPSAlert){
                        SendBrod.sendBrod(this)

                     /*   val intent = Intent(this, PowerSavingSettingsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)*/
                    }}
                //sendGPSOffBroadcast()
            }else{

                println("pww - Power Save Mode OFF xm")
               // Log.e("pww", "Power Save Mode OFF xm" )
                //XLog.d("pww : Power Save Mode OFF xm" + " Time :" + AppUtils.getCurrentDateTime())


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
                //XLog.d("MonitorService LocationFuzedService : " + "true" + "," + " Time :" + AppUtils.getCurrentDateTime())
                //XLog.d("MonitorService Power Save Mode Status : " + powerMode + "," + " Time :" + AppUtils.getCurrentDateTime())
                /*if(powerSaver){
                    sendGPSOffBroadcast()
                }else{
                    cancelGpsBroadcast()
                }*/
            }else{
                XLog.d("MonitorService LocationFuzedService : " + "false" + "," + " Time :" + AppUtils.getCurrentDateTime())
                XLog.d("MonitorService  Power Save Mode Status : " + powerMode + "," + " Time :" + AppUtils.getCurrentDateTime())
                XLog.d("Monitor Service Stopped" + "" + "," + " Time :" + AppUtils.getCurrentDateTime())
                if(!isFirst){
                    Log.e("abc", "abc stoptimer" )
                    timer!!.cancel()
                }
                isFirst=false
            }
        }

    }

    fun sendGPSOffBroadcast(){
        if(Pref.user_id.toString().length > 0){
            XLog.d("MonitorService Called for Battery Broadcast :  Time :" + AppUtils.getCurrentDateTime())
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
        stopForeground(true)
        stopSelf()
        return super.stopService(name)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        stopSelf()
        timer!!.cancel()
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
        return if (Math.abs(System.currentTimeMillis() - Pref.prevShopActivityTimeStampMonitorService) > 20000) {
            Pref.prevShopActivityTimeStampMonitorService = System.currentTimeMillis()
            true
            //server timestamp is within 5 minutes of current system time
        } else {
            false
        }
    }


}