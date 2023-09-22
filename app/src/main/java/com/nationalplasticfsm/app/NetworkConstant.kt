package com.nationalplasticfsm.app

import com.nationalplasticfsm.app.utils.AppUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by Pratishruti on 28-11-2017.
 */
class NetworkConstant {
    //Base URL
    companion object {


        var BASE_URL = "http://fts.indusnettechnologies.com:7030/API/"
        var ADD_SHOP_BASE_URL = "http://fts.indusnettechnologies.com:7030/"
//        var BASE_URL = "http://3.7.30.86:8072/API/"
//        var ADD_SHOP_BASE_URL = "http://3.7.30.86:8072/"


        //var BASE_URL = "http://localhost:8081/API/"
        //var ADD_SHOP_BASE_URL = "http://localhost:8081/"

        //var BASE_URL = "http://192.168.4.174:8081/API/"
        //var ADD_SHOP_BASE_URL = "http://192.168.4.174:8081/"









        /*var BASE_URL = "http://fts.indusnettechnologies.com:8094/API/"
        var ADD_SHOP_BASE_URL = "http://fts.indusnettechnologies.com:8094/"*/


        /*var BASE_URL = "http://hrms.indusnettechnologies.com:8081/API/"
        var ADD_SHOP_BASE_URL = "http://hrms.indusnettechnologies.com:8081/"*/

        /*DEV Internal*/
//         var BASE_URL="http://hrms.indusnettechnologies.com:7025/API/"
//         var ADD_SHOP_BASE_URL="http://hrms.indusnettechnologies.com:7025"

        /*nationalplasticfsm INTERNAL*/
//        var BASE_URL="http://geapl.breezeerp.in:8083/API/"
//        var ADD_SHOP_BASE_URL="http://geapl.breezeerp.in:8083/"

        val WEATHER_URL = "http://api.openweathermap.org/data/2.5/"

        //Add Shop
        var ADD_SHOP = "ShopRegistration/RegisterShop"
        //Shop List
        var SHOP_LIST = "Shoplist/List"
        //Login
        var LOGIN = "UserLogin/Login"
        //Location Update
        var LOCATION_UPDATE = "LocationCapture/Sendlocation"
        var SESSION_MISMATCH = "202"
        var NO_DATA = "205"
        var SUCCESS = "200"
        var DUPLICATE_SHOP_ID = "203"

        fun setTimeOut(): OkHttpClient {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient().newBuilder()
                    .connectTimeout(60 * 10, TimeUnit.SECONDS)
                    .readTimeout(60 * 10, TimeUnit.SECONDS)
                    .writeTimeout(60 * 10, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .retryOnConnectionFailure(false)
                    .build()
        }

        fun setTimeOutNoRetry(): OkHttpClient {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient().newBuilder()
                    .connectTimeout(60 * 1, TimeUnit.SECONDS)
                    .readTimeout(60 * 1, TimeUnit.SECONDS)
                    .writeTimeout(60 * 1, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .retryOnConnectionFailure(false)
                    .build()
        }

        fun setNewTimeOut(): OkHttpClient {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient().newBuilder()
                    .connectTimeout(60 * 4, TimeUnit.SECONDS)
                    .readTimeout(60 * 4, TimeUnit.SECONDS)
                    .writeTimeout(60 * 4, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .retryOnConnectionFailure(false)
                    .build()
        }

        fun setConfigTimeOut(): OkHttpClient {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient().newBuilder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    /*.addInterceptor {
                        it.proceed(it.request()?.newBuilder()?.addHeader("version_name", "v2")?.build())
                    }*/
                    .addInterceptor(loggingInterceptor)
                    .retryOnConnectionFailure(false)
                    .build()
        }
    }




}