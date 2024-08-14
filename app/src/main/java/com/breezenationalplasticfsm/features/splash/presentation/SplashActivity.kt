package com.breezenationalplasticfsm.features.splash.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.breezenationalplasticfsm.BuildConfig
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.uiaction.DisplayAlert
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.FileLoggingTree
import com.breezenationalplasticfsm.app.utils.PermissionUtils
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.features.alarm.presetation.AlarmBootReceiver
import com.breezenationalplasticfsm.features.commondialog.presentation.CommonDialog
import com.breezenationalplasticfsm.features.commondialog.presentation.CommonDialogClickListener
import com.breezenationalplasticfsm.features.commondialogsinglebtn.CommonDialogSingleBtn
import com.breezenationalplasticfsm.features.commondialogsinglebtn.OnDialogClickListener
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.location.SingleShotLocationProvider
import com.breezenationalplasticfsm.features.login.presentation.LoginActivity
import com.breezenationalplasticfsm.features.splash.presentation.api.VersionCheckingRepoProvider
import com.breezenationalplasticfsm.features.splash.presentation.model.VersionCheckingReponseModel
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import com.itextpdf.text.pdf.PdfFileSpecification.url
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.fragment_new_order_screen_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.alexandroid.gps.GpsStatusDetector
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.exitProcess


/**
 * Created by Pratishruti on 26-10-2017.
 */
// Revision History
// 1.0 SplashActivity AppV 4.0.7 Saheli    02/03/2023 Timber Log Implementation
// 2.0 SplashActivity AppV 4.0.7 Suman    21/03/2023 Location rectification for previous location 25760
class SplashActivity : BaseActivity(), GpsStatusDetector.GpsStatusDetectorCallBack {

    private var isLoginLoaded: Boolean = false
    private var permissionUtils: PermissionUtils? = null
    private var mGpsStatusDetector: GpsStatusDetector? = null
    private lateinit var progress_wheel: com.pnikosis.materialishprogress.ProgressWheel

    private lateinit var locDiscloserDialog : Dialog

    var permList = mutableListOf<PermissionDetails>()
    var permListDenied = mutableListOf<PermissionDetails>()
    data class PermissionDetails(var permissionName: String, var permissionTag: Int)

//test
    @SuppressLint("SuspiciousIndentation", "WrongConstant")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

   /* var fir:File = File(applicationContext.filesDir,"OPLOG")
    val filename = "OPLOG"
    val fileContents = "Hello world! ${AppUtils.getCurrentDateTime()}"
    applicationContext.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(fileContents.toByteArray())
    }
    applicationContext.deleteFile(filename)

    applicationContext.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(fileContents.toByteArray())
    }*/

    /*FileLoggingTree.context = this.applicationContext*/

    Timber.plant(Timber.DebugTree())
    Timber.plant(FileLoggingTree())

    /*val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource("http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Sell Me This Pen.mp4", HashMap<String, String>())
    val bmFrame = mediaMetadataRetriever.getFrameAtTime(1000) //unit in microsecond
    var bb = bmFrame

    val mediaMetadataRetriever1 = MediaMetadataRetriever()
    mediaMetadataRetriever1.setDataSource("http://3.7.30.86:8073/Commonfolder/LMS/nature shorts video.mp4", HashMap<String, String>())
    val bmFrame1 = mediaMetadataRetriever.getFrameAtTime(1000) //unit in microsecond
    var bb1 = bmFrame1*/


    //startActivity( Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS))


        //Handler().postDelayed({ goToNextScreen() }, 2000)
        //Code by wasim
        // this is for test purpose timing seeting
        // AlarmReceiver.setAlarm(this, 17, 45, 2017)

    /*FirebaseMessaging.getInstance().subscribeToTopic("newss").addOnSuccessListener(object : OnSuccessListener<Void?> {
        override fun onSuccess(aVoid: Void?) {
            //Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
        }
    })*/

    /* val email = Intent(Intent.ACTION_SENDTO)
    email.setData(Uri.parse("mailto:"))
    email.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("saheli.bhattacharjee@indusnet.co.in"))
    email.putExtra(Intent.EXTRA_SUBJECT, "sub")
    email.putExtra(Intent.EXTRA_TEXT, "msg")
    //email.type = "message/rfc822"
    startActivity(Intent.createChooser(email, "Send mail..."))*/

    val receiver = ComponentName(this, AlarmBootReceiver::class.java)
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)

        progress_wheel = findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (Pref.isLocationPermissionGranted)
                initPermissionCheck()
            else {
                /*LocationPermissionDialog.newInstance(object : LocationPermissionDialog.OnItemSelectedListener {
                    override fun onOkClick() {
                        //initPermissionCheck()
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R && Pref.isLocationHintPermissionGranted == false){
                            locDesc()
                        }else{
                            initPermissionCheck()
                        }
                    }
                    override fun onCrossClick() {
                        finish()
                    }
                }).show(supportFragmentManager, "")*/



                locDiscloserDialog = Dialog(this)
                locDiscloserDialog.setCancelable(false)
                locDiscloserDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                locDiscloserDialog.setContentView(R.layout.dialog_loc)

                val tv_body = locDiscloserDialog.findViewById(R.id.tv_loc_dialog_body) as AppCustomTextView
                var tv_ok = locDiscloserDialog.findViewById(R.id.tv_loc_dialog_ok) as AppCustomTextView
                val tv_not_ok = locDiscloserDialog.findViewById(R.id.tv_loc_dialog_not_ok) as AppCustomTextView
                var appN ="This"
                try {
                     appN = this.getResources().getString(R.string.app_name)
                } catch (e: Exception) {
                    TODO("Not yet implemented")
                }
                tv_body.text = "$appN App collects location data after you open and " +
                        "login into the App, to identify nearby Parties location even when the app is " +
                        "running in the background and not in use. This app collects location data to " +
                        "enable nearby shops, GPS route, even when the app is closed or not in use. " +
                        "Reimbursement is issued with distance travelled for specific GPS route. This is " +
                        "a core functionality of this app."

                tv_ok.setOnClickListener {
                    initPermissionCheck()
                }
                tv_not_ok.setOnClickListener {
                    finish()
                }
                locDiscloserDialog.show()

                /*LocPermissionDialog.newInstance(object :LocPermissionDialog.OnItemSelectedListener{
                    override fun onOkClick() {
                        *//*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R && Pref.isLocationHintPermissionGranted == false){
                            locDesc()
                        }else{
                            initPermissionCheck()
                        }*//*

                        initPermissionCheck()
                    }

                    override fun onCrossClick() {
                        finish()
                    }
                }).show(supportFragmentManager, "")*/
            }
        else {
            checkGPSProvider()
        }
        permissionCheck()
    }

    private fun extractFrameFromVideo(videoPath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(videoPath)
            retriever.getFrameAtTime(1000000) // 1 second (1000000 microseconds)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }

    fun checkBatteryOptiSettings(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName = packageName
            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {

                //Toaster.msgLong(this,"Please Don't optimize your battery for this app.")
                CommonDialog.getInstance(getString(R.string.app_name), "You must select the option 'Don't Optimise' to use this app. " ,
                    "Cancel", "Ok", false, object : CommonDialogClickListener {
                        override fun onLeftClick() {
                           finish()
                        }

                        override fun onRightClick(editableData: String) {
                            goTONextActi()
                        }

                    }).show(supportFragmentManager, "")
                println("battery dialog scr")
            } else{
                println("battery next scr")
                goTONextActi()
            }
        }
    }


    private fun locDesc(){
        LocationHintDialog.newInstance(object : LocationHintDialog.OnItemSelectedListener {
            override fun onOkClick() {
                Pref.isLocationHintPermissionGranted = true
                initPermissionCheck()
            }
        }).show(supportFragmentManager, "")
    }

    private fun permissionCheck() {
        var strSub:String=""
        permList.clear()
        var info: PackageInfo = this.packageManager.getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
        var list = info.requestedPermissionsFlags
        var list1 = info.requestedPermissions
        for (i in 0..list.size - 1) {
            if (list1.get(i) != "android.permission.ACCESS_GPS") {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && list1.get(i) == "android.permission.ACCESS_BACKGROUND_LOCATION"){
                    strSub=" (For Android 10 & Later)"
                }

                if ( list1.get(i) == "android.permission.USE_FULL_SCREEN_INTENT" || list1.get(i) == "android.permission.SYSTEM_ALERT_WINDOW"
                        || list1.get(i) == "android.permission.FOREGROUND_SERVICE"){
                    strSub=" (System Defined)"
                }

                var obj: PermissionDetails = PermissionDetails(list1.get(i).replace("android.permission.", "").replace("_", " ")
                        .replace("com.google.android.c2dm.permission.RECEIVE", "Receive Data from Internet").replace("com.rubyfood.permission.C2D", "") + strSub, list.get(i))

                strSub=""
                if (list.get(i) == 3) {
                    permList.add(obj)
                } else {
                    permListDenied.add(obj)
                }
            }
        }
        val notifi: Boolean = NotificationManagerCompat.from(this).areNotificationsEnabled()

        if (notifi) {
            permList.add(PermissionDetails("Notification", 3))
        } else {
            permListDenied.add(PermissionDetails("Notification", 1))
        }
        permList = (permList + permListDenied).toMutableList()

        for(i in 0..permList.size-1){
            // 1.0 SplashActivity AppV 4.0.7 Timber Log Implementation
//            XLog.d("Permission Name"+permList.get(i).permissionName + " Status : Granted")
            Timber.d("Permission Name"+permList.get(i).permissionName + " Status : Granted")
        }
        for(i in 0..permListDenied.size-1){
            // 1.0 SplashActivity AppV 4.0.7 Timber Log Implementation
//            XLog.d("Permission Name"+permListDenied.get(i).permissionName + " Status : Denied")
            Timber.d("Permission Name"+permListDenied.get(i).permissionName + " Status : Denied")
        }
    }


    private fun initPermissionCheck() {

        var permissionLists : Array<String> ?= null

        permissionLists = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            //arrayOf<String>(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            arrayOf<String>(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        else
            arrayOf<String>(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

        permissionUtils = PermissionUtils(this, object : PermissionUtils.OnPermissionListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPermissionGranted() {
                //Pref.isLocationPermissionGranted = true
                //checkGPSProvider()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    accessBackLoc()
                }else{
                    Pref.isLocationPermissionGranted = true
                    checkGPSProvider()
                }
            }

            override fun onPermissionNotGranted() {
                //AppUtils.showButtonSnackBar(this@SplashActivity, rl_splash_main, getString(R.string.error_loc_permission_request_msg))
                DisplayAlert.showSnackMessage(this@SplashActivity, alert_splash_snack_bar, getString(R.string.accept_permission))
                Handler().postDelayed(Runnable {
                    finish()
                    exitProcess(0)
                }, 3000)
            }

        }, permissionLists)
    }

    private fun accessBackLoc(){
        var permissionLists : Array<String> ?= null

        permissionLists = arrayOf<String>( Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        permissionLists += Manifest.permission.FOREGROUND_SERVICE
        permissionUtils = PermissionUtils(this, object : PermissionUtils.OnPermissionListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPermissionGranted() {
                Pref.isLocationPermissionGranted = true
                checkGPSProvider()
            }

            override fun onPermissionNotGranted() {
                //AppUtils.showButtonSnackBar(this@SplashActivity, rl_splash_main, getString(R.string.error_loc_permission_request_msg))
                DisplayAlert.showSnackMessage(this@SplashActivity, alert_splash_snack_bar, getString(R.string.accept_permission))
                Handler().postDelayed(Runnable {
                    finish()
                    exitProcess(0)
                }, 3000)
            }

        }, permissionLists)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkGPSProvider() {
        try {
            locDiscloserDialog.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) /*&& PermissionHelper.checkLocationPermission(this, 0)*/) {
            checkGPSAvailability()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isIgnoringBatteryOptimizations())
                checkBatteryOptimization()
            else
                doAfterPermissionFunctionality()

        } else {
            showGPSDisabledAlertToUser()
        }
    }

    private fun doAfterPermissionFunctionality() {
        Handler().postDelayed(Runnable {
            //goToNextScreen()
            if (!Pref.isAutoLogout)
                callVersionCheckingApi()
            else
                goToNextScreen()

        }, 1000)
    }

    private fun checkBatteryOptimization() {
        val intent = Intent()
        intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, 100)
    }

    private fun isIgnoringBatteryOptimizations(): Boolean {
        val pwrm = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        val name = applicationContext.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return pwrm.isIgnoringBatteryOptimizations(name)
        }
        return true
    }

    private fun callVersionCheckingApi() {

        if (!AppUtils.isOnline(this)) {
            goToNextScreen()
            return
        }

        progress_wheel.spin()
        val repository = VersionCheckingRepoProvider.versionCheckingRepository()
        BaseActivity.compositeDisposable.add(
                repository.versionChecking()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as VersionCheckingReponseModel
                            // 1.0 SplashActivity AppV 4.0.7 Timber Log Implementation
//                            XLog.d("VERSION CHECKING RESPONSE: " + "STATUS: " + response.status + ", MESSAGE:" + result.message)
                            Timber.d("VERSION CHECKING RESPONSE: " + "STATUS: " + response.status + ", MESSAGE:" + result.message)

                            if (response.status == NetworkConstant.SUCCESS) {

                             /*   XLog.d("===========VERSION CHECKING SUCCESS RESPONSE===========")
                                XLog.d("min version=====> " + response.min_req_version)
                                XLog.d("store version=====> " + response.play_store_version)
                                XLog.d("mandatory msg======> " + response.mandatory_msg)
                                XLog.d("optional msg=====> " + response.optional_msg)
                                XLog.d("apk url======> " + response.apk_url)
                                XLog.d("=======================================================")*/
                                Timber.d("===========VERSION CHECKING SUCCESS RESPONSE===========")
                                Timber.d("min version=====> " + response.min_req_version)
                                Timber.d("store version=====> " + response.play_store_version)
                                Timber.d("mandatory msg======> " + response.mandatory_msg)
                                Timber.d("optional msg=====> " + response.optional_msg)
                                Timber.d("apk url======> " + response.apk_url)
                                Timber.d("=======================================================")

                                versionChecking(response)
                                //goToNextScreen()
                            } else {
                                goToNextScreen()
                            }
                            isApiInitiated = false

                        }, { error ->
                            isApiInitiated = false
//                            XLog.d("VERSION CHECKING ERROR: " + "MESSAGE:" + error.message)
                            Timber.d("VERSION CHECKING ERROR: " + "MESSAGE:" + error.message) // 1.0 SplashActivity AppV 4.0.7 Timber Log Implementation
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            goToNextScreen()
                        })
        )
    }


    private fun showGPSDisabledAlertToUser() {
        mGpsStatusDetector = GpsStatusDetector(this)
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mGpsStatusDetector?.checkGpsStatus()
        }
    }

    private fun versionChecking(response: VersionCheckingReponseModel) {

        try {

            val minVersion = Integer.parseInt(response.min_req_version?.replace(".", "").toString())
            val storeVersion = Integer.parseInt(response.play_store_version?.replace(".", "").toString())
            val currentVersion = Integer.parseInt(BuildConfig.VERSION_NAME.replace(".", ""))

            when {

                storeVersion.toInt()-currentVersion.toInt() > 2 -> {

                    val simpleDialogV = Dialog(this)
                    simpleDialogV.setCancelable(false)
                    simpleDialogV.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialogV.setContentView(R.layout.dialog_message)
                    val dialogHeaderV =
                        simpleDialogV.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
                    val dialog_yes_no_headerTVV =
                        simpleDialogV.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
                    if (Pref.user_name != null) {
                        dialog_yes_no_headerTVV.text = "Hi " + Pref.user_name!! + "!"
                    } else {
                        dialog_yes_no_headerTVV.text = "Hi User" + "!"
                    }
                    dialogHeaderV.text = "You are using lower Version of Application. Please Uninstall this and Install Latest version from Playstore."

                    val dialogYesV = simpleDialogV.findViewById(R.id.tv_message_ok) as AppCustomTextView
                    dialogYesV.text = "Uninstall"
                    dialogYesV.setOnClickListener({ view ->
                        simpleDialogV.cancel()
                        dialogYesV.text = "Please wait......"
                        val intent = Intent(Intent.ACTION_DELETE)
                        intent.data = Uri.parse("package:${this.getPackageName()}")
                        startActivity(intent)
                    })
                    simpleDialogV.show()
                }

                currentVersion >= storeVersion -> goToNextScreen()
                currentVersion in minVersion until storeVersion -> {
                    CommonDialog.getInstance("New Update", response.optional_msg!!,
                            "Cancel", "Ok", false, object : CommonDialogClickListener {
                        override fun onLeftClick() {
                            goToNextScreen()
                        }

                        override fun onRightClick(editableData: String) {
                            if (!TextUtils.isEmpty(response.apk_url)) {
                                val webLaunch = Intent(Intent.ACTION_VIEW, Uri.parse(response.apk_url))
                                startActivity(webLaunch)
                                finish()
                                exitProcess(0)
                            }
                            else
                                goToNextScreen()
                        }

                    }).show(supportFragmentManager, "")
                }
                else -> {
                    CommonDialogSingleBtn.getInstance("New Update", response.mandatory_msg!!,
                            "OK", object : OnDialogClickListener {
                        override fun onOkClick() {

                            /*market://details?id=com.fieldtrackingsystem*/

                            if (!TextUtils.isEmpty(response.apk_url)) {
                                val webLaunch = Intent(Intent.ACTION_VIEW, Uri.parse(response.apk_url))
                                startActivity(webLaunch)
                                finish()
                                exitProcess(0)
                            }
                            else
                                goToNextScreen()
                        }
                    }).show(supportFragmentManager, "")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            goToNextScreen()
        }
    }

    /*private fun goToNextScreen() {
        var manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //if (/*manager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&*/ PermissionHelper.checkLocationPermission(this, 0)) {
        if (TextUtils.isEmpty(Pref.user_id) || Pref.user_id.isNullOrBlank()) {
            if (!isLoginLoaded) {
                isLoginLoaded = true
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }

        } else {
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
        //}
        /*else if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }*/
    }*/

    private fun goToNextScreen() {
        addAutoStartup()
    }

    private fun addAutoStartup() {
        try {
            val intent = Intent()
            val manufacturer = Build.MANUFACTURER
            if ("xiaomi".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
            } else if ("oppo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")
            } else if ("vivo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
            } else if ("Letv".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")
            } else if ("Honor".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")
            }
            val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list.size > 0 && Pref.AutostartPermissionStatus==false) {
                //startActivity(intent)
                Pref.AutostartPermissionStatus = true
                startActivityForResult(intent,401)
            }else{
                goTONextActi()
            }
        } catch (e: java.lang.Exception) {
            Log.e("exc", e.toString())
            goTONextActi()
        }
    }


    fun goTONextActi(){

        /*val intent = Intent()
        val packageName = packageName
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        var t=pm.isIgnoringBatteryOptimizations(packageName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !pm.isIgnoringBatteryOptimizations(packageName)) {
            Handler().postDelayed(Runnable {
                println("battery hit 175")
                startActivityForResult( Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),175) }, 1000)
            return
        }*/

        if (TextUtils.isEmpty(Pref.user_id) || Pref.user_id.isNullOrBlank()) {
            if (!isLoginLoaded) {
                isLoginLoaded = true

                // 2.0 SplashActivity AppV 4.0.7 Suman    21/03/2023 Location rectification for previous location 25760
                println("loc_fetch_tag splash begin")
                progress_wheel.spin()
                try{
                    SingleShotLocationProvider.requestSingleUpdate(this,
                        object : SingleShotLocationProvider.LocationCallback {
                            override fun onStatusChanged(status: String) {
                            }

                            override fun onProviderEnabled(status: String) {
                            }

                            override fun onProviderDisabled(status: String) {
                            }

                            override fun onNewLocationAvailable(location: Location) {
                                println("loc_fetch_tag splash end")
                                Pref.latitude = location.latitude.toString()
                                Pref.longitude = location.longitude.toString()
                                Pref.current_latitude = location.latitude.toString()
                                Pref.current_longitude = location.longitude.toString()
                                Timber.d("Splash onNewLocationAvailable ${Pref.latitude} ${Pref.longitude}")
                                progress_wheel.stopSpinning()
                            }
                        })
                }
                catch (ex:Exception){
                    ex.printStackTrace()
                    Timber.d("Splash onNewLocationAvailable ex ${ex.message}")
                    progress_wheel.stopSpinning()
                }

                Handler().postDelayed(Runnable {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                }, 2200)
            }
        } else {
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        /*Handler().postDelayed({
            goToNextScreen()
        }, 2000)*/
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /*if (requestCode == PermissionHelper.TAG_LOCATION_RESULTCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    goToNextScreen()
                }

            } else {

                PermissionHelper.checkLocationPermission(this, 0)
//                Toast.makeText(this, "Location permission has not been granted", Toast.LENGTH_LONG).show()
            }

        }*/

        permissionUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 401){
            goTONextActi()
        }

        /*if(requestCode == 175){
            println("battery get 175")
            checkBatteryOptiSettings()
            return
        }*/

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                if (!Pref.isAutoLogout)
                    callVersionCheckingApi()
                else
                    goToNextScreen()
            }
            else {
                mGpsStatusDetector?.checkOnActivityResult(requestCode, resultCode)
                checkGPSAvailability()
                if (!Pref.isAutoLogout)
                    callVersionCheckingApi()
                else
                    goToNextScreen()
            }
        } else {

            /*DisplayAlert.showSnackMessage(this@SplashActivity, alert_splash_snack_bar, getString(R.string.alert_nolocation))

            Handler().postDelayed(Runnable {
                finish()
                System.exit(0)
            },1000)*/

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isIgnoringBatteryOptimizations())
                Toaster.msgShort(this, "Please allow battery optimization")

            val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                mGpsStatusDetector?.checkGpsStatus()
            else {
                checkGPSAvailability()

                Handler().postDelayed(Runnable {
                    if (!Pref.isAutoLogout)
                        callVersionCheckingApi()
                    else
                        goToNextScreen()
                }, 300)
            }
        }
    }

    // GpsStatusDetectorCallBack
    override fun onGpsSettingStatus(enabled: Boolean) {

        if (enabled)
            Log.e("splash", "GPS enabled")
        else
            Log.e("splash", "GPS disabled")
    }

    override fun onGpsAlertCanceledByUser() {
    }
}
