package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SchedulerMasterDao {
    @Insert
    fun insert(vararg obj: SchedulerMasterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<SchedulerMasterEntity>)

    @Query("select * from crm_scheduler_master")
    fun getAll(): List<SchedulerMasterEntity>

    @Query("delete from crm_scheduler_master")
    fun deleteAll()

    @Query("Select * from crm_scheduler_master where scheduler_name LIKE '%' || :schedulerNameorNum || '%' ")
    fun getSchedulerBySearchDataNew(schedulerNameorNum: String): List<SchedulerMasterEntity>

    @Query("Select * from crm_scheduler_master where scheduler_name=:scheduler_name COLLATE NOCASE ")
    fun getDuplicateSchedulerData(scheduler_name: String): List<SchedulerMasterEntity>

    @Query("delete from crm_scheduler_master where scheduler_id=:scheduler_id")
    fun deleteScheduler(scheduler_id:String)
    @Query("select * from crm_scheduler_master_date_time where select_date =:currentDate \n" +
            "and  :currentTimeSt > select_timestamp and isDone =:isDone and scheduler_id in \n" +
            "(select scheduler_id from crm_scheduler_master where isAutoMail = :isAutoMail) order by select_time asc")
    fun getTimeStampToSent(currentDate:String , isDone:Boolean, isAutoMail:Boolean,currentTimeSt:String): List<SchedulerDateTimeEntity>

    @Query("update crm_scheduler_master_date_time set isDone =:isDone where scheduler_id=:scheduler_id and select_timestamp=:select_timestamp")
    fun updateSchedulerSucess1(scheduler_id:String,select_timestamp:String,isDone:Boolean)

    @Query("select * from crm_scheduler_master where scheduler_id=:scheduler_id")
    fun getSchedulerDtls(scheduler_id:String): SchedulerMasterEntity
    @Query("update crm_scheduler_master_date_time set isDone =:isDone where scheduler_id=:scheduler_id and select_timestamp=:select_timestamp")
    fun updateSchedulerAfterEdit(scheduler_id:String,select_timestamp:String,isDone:Boolean)

    @Query("update crm_scheduler_master_date_time set isDone =:isDone")
    fun updateTest(isDone:Boolean)

}