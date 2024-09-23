package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SchedulerContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<SchedulerContactEntity>)

    @Query("select * from crm_scheduler_master_contacts where scheduler_id=:scheduler_id")
    fun getContDtlsBySchID(scheduler_id:String): List<SchedulerContactEntity>

    @Query("delete from crm_scheduler_master_contacts where scheduler_id=:scheduler_id")
    fun deleteScheduler(scheduler_id:String)
}