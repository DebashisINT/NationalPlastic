package com.breezenationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SchedulerDateTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<SchedulerDateTimeEntity>)

    @Query("select * from crm_scheduler_master_date_time where scheduler_id=:scheduler_id order by select_date asc")
    fun getAll(scheduler_id:String): List<SchedulerDateTimeEntity>

    @Query("delete from crm_scheduler_master_date_time where scheduler_id=:scheduler_id")
    fun deleteScheduler(scheduler_id:String)
}