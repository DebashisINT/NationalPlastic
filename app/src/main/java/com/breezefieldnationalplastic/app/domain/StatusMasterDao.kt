package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface StatusMasterDao {

    @Insert
    fun insert(vararg obj: StatusMasterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<StatusMasterEntity>)

    @Query("select * from crm_status_master")
    fun getAll(): List<StatusMasterEntity>

    @Query("delete from " + AppConstant.CRM_STATUS_MASTER)
    fun deleteAll()
}