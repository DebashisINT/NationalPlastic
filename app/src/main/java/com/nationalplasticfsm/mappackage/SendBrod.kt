package com.nationalplasticfsm.mappackage

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import com.nationalplasticfsm.MonitorBroadcast
import com.nationalplasticfsm.MonitorCollPending
import com.nationalplasticfsm.app.Pref


class SendBrod {

    companion object{
        var monitorNotiID:Int = 201
        var monitorNotiIDColl:Int = 202
        var monitorNotiIDZeroOrder:Int = 203
        var monitorNotiIDDoaDob:Int = 204
        fun sendBrod(context: Context){
            if(Pref.user_id.toString().length > 0){
                //var notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
                //notificationManager.cancel(monitorNotiID)
                MonitorBroadcast.isSound=Pref.GPSAlertwithSound
                MonitorBroadcast.isVibrator = Pref.GPSAlertwithVibration
                val intent: Intent = Intent(context, MonitorBroadcast::class.java)
                intent.putExtra("notiId", monitorNotiID)
                intent.putExtra("fuzedLoc", "Fuzed Stop")
                context.sendBroadcast(intent)
            }
        }

        fun stopBrod(context: Context){
            if (monitorNotiID != 0){
                if(MonitorBroadcast.player!=null){
                    try{
                        MonitorBroadcast.player.stop()
                        MonitorBroadcast.player=null
                    }catch (ex:Exception){
                        ex.printStackTrace()
                        MonitorBroadcast.player=null
                    }
                   try{
                        MonitorBroadcast.vibrator.cancel()
                        MonitorBroadcast.vibrator=null
                    }catch (ex:Exception){
                        ex.printStackTrace()
                         MonitorBroadcast.vibrator=null
                    }
                }
                var notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(monitorNotiID)
            }
        }


        fun sendBrodColl(context: Context){
            if(Pref.user_id.toString().length > 0){
                val intent: Intent = Intent(context, MonitorCollPending::class.java)
                intent.putExtra("notiId", monitorNotiIDColl)
                intent.putExtra("coll", "Pending Collection")
                context.sendBroadcast(intent)
            }
        }

        fun stopBrodColl(context: Context){
            if (monitorNotiIDColl != 0){
                if(MonitorCollPending.player!=null){
                    MonitorCollPending.player.stop()
                    MonitorCollPending.player=null
                    try {
                        MonitorCollPending.vibrator.cancel()
                        MonitorCollPending.vibrator=null
                    }catch (ex:Exception){
                        ex.printStackTrace()
                        MonitorCollPending.vibrator=null
                    }
                }
                var notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(monitorNotiIDColl)
            }
        }

        fun sendBrodZeroOrder(context: Context){
            if(Pref.user_id.toString().length > 0){
                val intent: Intent = Intent(context, MonitorCollPending::class.java)
                intent.putExtra("notiId", monitorNotiIDZeroOrder)
                intent.putExtra("coll", "Zero Order")
                context.sendBroadcast(intent)
            }
        }

        fun stopBrodZeroOrder(context: Context){
            if (monitorNotiIDZeroOrder != 0){
                if(MonitorCollPending.player!=null){
                    try{
                        MonitorCollPending.player.stop()
                        MonitorCollPending.player=null
                    }catch (ex:Exception){
                        ex.printStackTrace()
                        MonitorCollPending.player=null
                    }
                    try{
                        MonitorCollPending.vibrator.cancel()
                        MonitorCollPending.vibrator=null
                    }catch (ex:Exception){
                        ex.printStackTrace()
                        MonitorCollPending.vibrator=null
                    }
                }
                var notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(monitorNotiIDZeroOrder)
            }
        }

        fun sendBrodDOBDOA(context: Context){
            if(Pref.user_id.toString().length > 0){
                val intent: Intent = Intent(context, MonitorCollPending::class.java)
                intent.putExtra("notiId", monitorNotiIDDoaDob)
                intent.putExtra("coll", "Doa Dob")
                context.sendBroadcast(intent)
            }
        }

        fun stopBrodDOBDOA(context: Context){
            if (monitorNotiIDDoaDob != 0){
                if(MonitorCollPending.player!=null){
                    try{
                        MonitorCollPending.player.stop()
                        MonitorCollPending.player=null
                    }catch (ex:Exception){
                        ex.printStackTrace()
                        MonitorCollPending.player=null
                    }
                    try{
                        MonitorCollPending.vibrator.cancel()
                        MonitorCollPending.vibrator=null
                    }catch (ex:Exception){
                        ex.printStackTrace()
                        MonitorCollPending.vibrator=null
                    }
                }
                var notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(monitorNotiIDDoaDob)
            }
        }

    }

}