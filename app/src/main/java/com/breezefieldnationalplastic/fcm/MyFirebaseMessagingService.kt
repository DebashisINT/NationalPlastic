package com.breezefieldnationalplastic.fcm

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat

import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.LMSNotiEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.utils.AppUtils

import com.breezefieldnationalplastic.app.utils.NotificationUtils
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.fcm.api.UpdateDeviceTokenRepoProvider
import com.breezefieldnationalplastic.features.chat.model.ChatListDataModel
import com.breezefieldnationalplastic.features.chat.model.ChatUserDataModel
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.login.presentation.LoginActivity
import com.breezefieldnationalplastic.features.member.model.TeamShopListDataModel

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber

/**
 * Created by Saikat on 20-09-2018.
 */
// MyFirebaseMessagingService V 4.0.6 saheli 27-01-2023 For new firebase update MyFirebaseInstanceIDService is obsolated and override function onNewToken introduced
// 2.0 MyFirebaseMessagingService AppV 4.0.8 Suman    19/04/2023 thread safe for token updation 0025873
// Rev 3.0 MyFirebaseMessagingService AppV 4.0.8 Suman    26/04/2023 mail repetation fix 25923
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var messageDetails = ""

    override fun onNewToken(token: String) {
        Timber.e("Refreshed token: $token")
        println("MyFirebaseMessagingService onNewToken");

        doAsync {

            var refreshedToken = token

            while (refreshedToken == null) {
                refreshedToken = token
            }

            Timber.e("MyFirebaseInstanceIDService : \nDevice Token=====> $token")
            // 2.0 MyFirebaseMessagingService AppV 4.0.8 Suman    19/04/2023 thread safe for token updation 0025873
            uiThread {

                if (!TextUtils.isEmpty(Pref.user_id)) {


                    doAsync {

                        callUpdateDeviceTokenApi(refreshedToken)

                        uiThread {

                        }
                    }
                }

                Pref.deviceToken = token
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        println("Refreshed token onMessageReceived"+remoteMessage);
        Timber.e("FirebaseMessageService : ============Push has come============ ${AppUtils.getCurrentDateTime()}")


        if (TextUtils.isEmpty(Pref.user_id)) {
            Timber.e("FirebaseMessageService : ============Logged out scenario============")

            if (!TextUtils.isEmpty(remoteMessage?.data?.get("type")) && remoteMessage?.data?.get("type") == "clearData") {
                val packageName = applicationContext.packageName
                val runtime = Runtime.getRuntime()
                runtime.exec("pm clear $packageName")
            }

            return
        }

        //getting the title and the body
        //val title = remoteMessage?.notification?.title
        val body = remoteMessage?.data?.get("body")
        val tag = remoteMessage?.data?.get("flag")

        Timber.d("quto_mail FCM class tag ${remoteMessage?.data?.get("type")}")

        val notification = NotificationUtils(getString(R.string.app_name), "", "", "")

        if (!TextUtils.isEmpty(body)) {
            Timber.e("FirebaseMessageService : \nNotification Message=====> $body")
            //Timber.e("FirebaseMessageService : \nNotification Title=====> $title")
            if (remoteMessage?.data?.get("type") == "clearData") {
                Pref.isClearData = true

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancelAll()
                notification.sendClearDataNotification(applicationContext, body!!)
            }
            else if (remoteMessage?.data?.get("type") == "chat") {
                val intent = Intent()
                intent.action = "FCM_CHAT_ACTION_RECEIVER"
                intent.putExtra("body", body)
                val chatData = ChatListDataModel(remoteMessage.data?.get("msg_id")!!, remoteMessage.data?.get("msg")!!,
                        remoteMessage.data?.get("time")!!, remoteMessage.data?.get("from_id")!!, remoteMessage.data?.get("from_name")!!)
                intent.putExtra("chatData", chatData)

                val chatUser = ChatUserDataModel(remoteMessage.data?.get("from_user_id")!!, remoteMessage.data?.get("from_user_name")!!,
                        remoteMessage.data?.get("isGroup")?.toBoolean()!!, "", "", "", "", "")
                intent.putExtra("chatUser", chatUser)
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

                Handler(Looper.getMainLooper()).postDelayed({
                    if (!AppUtils.isBroadCastRecv)
                        notification.msgNotification(applicationContext, body!!, chatData, chatUser)
                    else
                        AppUtils.isBroadCastRecv = false
                }, 1000)

            }
            else if (remoteMessage?.data?.get("type") == "update_status") {
                val intent = Intent()
                intent.action = "FCM_STATUS_ACTION_RECEIVER"
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }else if(tag.equals("logout")){
                notification.sendLogoutNotificaiton(applicationContext, remoteMessage)
            } else if(tag.equals("flag")){
                notification.sendFCMNotificaitonCustom(applicationContext, remoteMessage)

                val intent = Intent()
                intent.action = "FCM_ACTION_RECEIVER_LEAVE"
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            } else if(tag.equals("flag_status")){
                notification.sendFCMNotificaitonByUCustom(applicationContext, remoteMessage)

                val intent = Intent()
                intent.action = "FCM_ACTION_RECEIVER_LEAVE_STATUS"
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }else if(remoteMessage?.data?.get("type").equals("flag_status_quotation_approval")){
                Timber.d("quto_mail FCM class... ${AppUtils.getCurrentDateTime()}")
                //notification.sendFCMNotificaitonQuotationapprova(applicationContext, remoteMessage)
                notification.sendFCMNotificaitonQuotationapprova1(applicationContext, remoteMessage)
                val intent = Intent()
                intent.action = "FCM_ACTION_RECEIVER_quotation_approval"
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }else if(remoteMessage?.data?.get("type").equals("lead_work")){
                notification.sendFCMNotificaitonLead(applicationContext, remoteMessage)
                //val intent = Intent()
                //intent.action = "FCM_ACTION_RECEIVER_LEAD"
                //LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }else if(remoteMessage?.data?.get("type").equals("lms_content_assign")){

                try {
                    var obj : LMSNotiEntity = LMSNotiEntity()
                    obj.noti_datetime = AppUtils.getCurrentDateTime()
                    obj.noti_date = AppUtils.getCurrentDateForShopActi()//.replace("-20","-19")
                    obj.noti_time = AppUtils.getCurrentTime()
                    obj.noti_header = remoteMessage?.data?.get("header").toString()
                    if(obj.noti_header == "null"){
                        obj.noti_header = "New Assignment"
                    }
                    obj.noti_message = remoteMessage?.data?.get("body").toString()
                    obj.isViwed=false
                    AppDatabase.getDBInstance()!!.lmsNotiDao().insert(obj)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                notification.sendFCMNotificaiton(applicationContext, remoteMessage)

                val intent = Intent()
                intent.action = "FCM_ACTION_RECEIVER"
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }else if(remoteMessage?.data?.get("type").equals("UpdateOrderStatus")){
                notification.sendFCMNotificaiton(applicationContext, remoteMessage)
                val intent = Intent()
                intent.action = "FCM_ACTION_RECEIVER"
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
            else {
                notification.sendFCMNotificaiton(applicationContext, remoteMessage)

                val intent = Intent()
                intent.action = "FCM_ACTION_RECEIVER"
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
        }

        ringtone()
    }

    private fun callUpdateDeviceTokenApi(refreshedToken: String?) {

        if (!AppUtils.isOnline(applicationContext))
            return

        val repository = UpdateDeviceTokenRepoProvider.updateDeviceTokenRepoProvider()

        BaseActivity.compositeDisposable.add(
                repository.updateDeviceToken(refreshedToken!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            Timber.d("UpdateDeviceTokenResponse : " + "\n" + "Status====> " + response.status + ", Message===> " + response.message)

                        }, { error ->
                            error.printStackTrace()
                            Timber.d("UpdateDeviceTokenResponse ERROR: " + error.localizedMessage + "\n" + "Username :" + Pref.user_name + ", Time :" + AppUtils.getCurrentDateTime())
                        })
        )
    }


    private fun ringtone() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            /*val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()*/

            val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
            val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
            ringtone.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

/*    fun getAccessToken():String{
        try {
            var jsonString : String = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"demofsm-fee63\",\n" +
                    "  \"private_key_id\": \"fa4e0aa591d3ba0a173a6f3408401efdad118bdb\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCpS0IokL4jjiry\\ngB6fGcIvmZkZds8Rjs2tPrF353Wd4QyeyqycG+Jj7hU4Nd7lgH0GiiF+0QY+7nrv\\nSCQjdSHj+mJUhulL3vA81lypoSzTyvewTjXN5/yj6U3W0MsVlt1rHqckIRvKIdjH\\nfR50X3BB2m4200i3TAhSvSqWBAHp6ySVTRWfYxM70ugVDZdJsr+BW1cZnn5+z93z\\nWMa4RSqbj24ZQOUNEAtdxu8uJ5M1HnlCqa5WeKXpHuO1oUtwlB/EpELqBG6VWM8E\\nXityD+HTJF+Bch4wJhP18Kj40o1bZv4ZDoesXnpZ1NmamQprOv07QBM6U7Ni5XC1\\nVgjMuc9xAgMBAAECggEAHxYEpfI+F8VJOZIxDUHrmFX5+OUKDM1OExvJ9px3ym/C\\no33PyDKOlY7oMpQhw76eNo8yq1iybufXhwyWJjSh7nzRhXfoatgbAPDTvworcxB3\\n/tW9p3uLtoVml6VrRSGYsszEICw8MBea+LaO2wuTT2ROjJ6rYY0Ckj7ODRHbUBpi\\n16wHZzXk0xHvnU+WpiB8O2o0kyecHDASa2tlPDnBWZAjwvrd6YR2D9/3jq11qWK/\\nVlHliNi+O563ih9VueHHXKidlsJXfFl9lzgRKElqov6BX4fkmuGjYrea/LraQQAc\\nK3qGvdbfttuHUvJOIsZaAhu4VM+gnMB0R6yj+pwu6QKBgQDd0FE3ZktMZDCSf8vb\\nB9xn5vC5ga8X6Nu6/L1K5wENfid1ShQE3n4Ni3GXTlXalWvmLi0gpe9EZNUe7avr\\nF5kEXwNVc5Q4P438MfeAAmUU6kzR1T1Y76JX4xRi2idjoJqfm2SPRGNB6wtoS7Gu\\ngCJ3Urd9dH3/hL0D+e01+MHxywKBgQDDYsTVQxBJsZTq75lhmPKVzsgMDozAfXF2\\n6uk+eVg/QFjsyZagWsSLlACwRXuZwoi8f0spGEIX3Jf8f6dvYg3CIDyDc3Yp2rpm\\n6OpMosugWuJEaFFxlwFz6Gu+5q5rCoWh72reqHJm0FGWGbqevD39U1R/zsU0/LnT\\nTtrP3Z9sMwKBgGagZZNOPvR/PoHpovYaMv3XufT6bXqQgGmJWkN3keMeRT9dINoH\\n3yaBJ/MriUly7NM49iQu4f8w7/I5YNuKtX9yPmag7SkBLr5KmAqgEQiWRyimkpW9\\nec1UATCjYqoTurax/NrUd2AeUc7VhsYH/upaWQ8wgMNiNNnMHtZj28f1AoGACNbu\\nGsvm776WAy8F3HGEAB0T1d/OpGLIgF3OYaIxyOLLYyMXqneQztPKWC88kU9IymZj\\n6x8K1nOHeMf5tkNUZgT5V+UgYnJf3ooJF6CB3+ZcuEWT8bSoPyszvLZJC9S1CQeA\\n6UPrsRUZq9XMKKRRlaVwfDvJlkUczx+RLLhVHxsCgYBwJ0d9poRg3nfGb+OJf2mo\\nfjsjo+2n1e9/ppQK79emITvqRCuqsyXLR+oyiTZIhck4Pz9D898bQnzoMWTj3UUo\\ni5mqby/aZih1ZBYAA63SeuKnh7FAdrXAmGoj/m0D1AELtiiWNN7H5d98kn5md2b/\\nE5awhBdjJd0NoUnhodWslA==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-m1emn@demofsm-fee63.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"115535921552187565842\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-m1emn%40demofsm-fee63.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n"

            val stream = ""
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }*/

}

