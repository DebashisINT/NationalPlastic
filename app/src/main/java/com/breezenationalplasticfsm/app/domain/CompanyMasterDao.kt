package com.breezenationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezenationalplasticfsm.features.viewAllOrder.orderOptimized.CommonProductCatagory

@Dao
interface CompanyMasterDao {
    @Insert
    fun insert(vararg obj: CompanyMasterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<CompanyMasterEntity>)

    @Query("select * from company_master")
    fun getAll(): List<CompanyMasterEntity>

    @Query("delete from company_master")
    fun deleteAll()


    @Query("select * from company_master where isUploaded=:isUploaded and company_name=:company_name")
    fun getUnSync(isUploaded:Boolean,company_name:String): CompanyMasterEntity

    @Query("select * from company_master where isUploaded=:isUploaded")
    fun getUnSyncList(isUploaded:Boolean): List<CompanyMasterEntity>

    @Query("select * from company_master where company_name=:company_name")
    fun getInfoByName(company_name:String): CompanyMasterEntity
}