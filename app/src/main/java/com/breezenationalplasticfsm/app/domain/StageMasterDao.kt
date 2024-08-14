package com.breezenationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezenationalplasticfsm.app.AppConstant

@Dao
interface StageMasterDao {


    @Insert
    fun insert(vararg obj: StageMasterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<StageMasterEntity>)

    @Query("select * from crm_stage_master")
    fun getAll(): List<StageMasterEntity>

    @Query("delete from " + AppConstant.CRM_STAGE_MASTER)
    fun deleteAll()
}