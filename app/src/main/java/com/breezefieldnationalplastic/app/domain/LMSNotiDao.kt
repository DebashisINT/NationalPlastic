package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface LMSNotiDao {
    @Insert
    fun insert(vararg obj: LMSNotiEntity)

    @Query("SELECT * FROM " + AppConstant.LMS_NOTIFICATION + " where noti_date =:noti_date order by noti_date DESC , noti_time DESC")
    fun getNotiByDate(noti_date:String): List<LMSNotiEntity>

    @Query("select distinct noti_date from lms_notification")
    fun getDistinctDate(): List<String>

    @Query("update lms_notification set isViwed = :isViwed")
    fun updateISViwed(isViwed:Boolean)

    @Query("select *  from lms_notification where isViwed =:isViwed")
    fun getNotViwed(isViwed:Boolean):List<LMSNotiEntity>
}