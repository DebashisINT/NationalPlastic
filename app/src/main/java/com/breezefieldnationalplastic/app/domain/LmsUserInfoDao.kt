package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface LmsUserInfoDao {

    @Insert
    fun insertAll(vararg activity: LmsUserInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<LmsUserInfoEntity>)

    @Query("SELECT * FROM " + AppConstant.LMS_USER_INFO)
    fun getAll(): List<LmsUserInfoEntity>

    @Query("delete FROM " + AppConstant.LMS_USER_INFO)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.LMS_USER_INFO +" where strftime('%Y-%m-%d', :currentDate)" )
    fun getAllByDate(currentDate:String): List<LmsUserInfoEntity>

    @Query("SELECT * FROM " + AppConstant.LMS_USER_INFO +" where strftime('%Y-%m-%d', :currentDate) and isCalculated=0" )
    fun getAllByDateNotCalculated(currentDate:String): List<LmsUserInfoEntity>

    @Query("SELECT * FROM " + AppConstant.LMS_USER_INFO +" where strftime('%Y-%m-%d', :currentDate) and module_name=:moduleName" )
    fun getByNameDate(currentDate:String,moduleName:String): List<LmsUserInfoEntity>

    @Query("update " + AppConstant.LMS_USER_INFO +" set module_endTimeInMilli=:module_endTimeInMilli , isCalculated=:isCalculated,time_spend=:time_spend " +
            "where module_startTimeInMilli=:module_startTimeInMilli" )
    fun updateEnd(module_endTimeInMilli:String,isCalculated:Boolean,module_startTimeInMilli:String,time_spend:String)

}