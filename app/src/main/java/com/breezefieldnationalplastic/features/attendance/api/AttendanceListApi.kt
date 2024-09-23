package com.breezefieldnationalplastic.features.attendance.api

import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.features.attendance.model.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Pratishruti on 28-11-2017.
 */
interface AttendanceListApi {
    /*@POST("Attendance/Records")*/ @POST("Attendance/List")
    fun getAttendanceList(@Body attendanceReq: AttendanceRequest?): Observable<AttendanceResponse>

    @POST("UserWiseDayStartEnd/UserDayStartEndList")
    fun getDayStartEndListAPI(@Body attendanceReq: AttendanceRequest?): Observable<DayStartEndListResponse>

    @POST("Shoplist/PartyNotVisitedList")
    fun getPartyListNotVisited(@Body reqBody: InputRequest?): Observable<OutputResponse>

    /**
     * Companion object to create the AttendanceListApi
     */
    companion object Factory {
        fun create(): AttendanceListApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(AttendanceListApi::class.java)
        }
    }

}