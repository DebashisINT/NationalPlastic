package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface TypeMasterDao {

    @Insert
    fun insert(vararg obj: TypeMasterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<TypeMasterEntity>)

    @Query("select * from " + AppConstant.CRM_TYPE_MASTER)
    fun getAll(): List<TypeMasterEntity>

    @Query("delete from " + AppConstant.CRM_TYPE_MASTER)
    fun deleteAll()

}