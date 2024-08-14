package com.breezenationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezenationalplasticfsm.app.AppConstant


@Dao
interface CallHisDao {

    @Insert
    fun insert(vararg obj: CallHisEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<CallHisEntity>)

    @Query("select * from call_his")
    fun getAllData():List<CallHisEntity>

    @Query("select * from call_his where call_number=:call_number and call_date=:call_date and call_time=:call_time and call_type=:call_type and call_duration_sec=:call_duration_sec")
    fun getFilterData(call_number:String,call_date:String,call_time:String,call_type:String,call_duration_sec:String):List<CallHisEntity>

    @Query("select * from call_his where shop_id=:shop_id order by call_date desc")
    fun getCallListByID(shop_id:String):List<CallHisEntity>

    @Query("select * from call_his where call_number=:call_number")
    fun getCallListByPhone(call_number:String):List<CallHisEntity>

    @Query("update call_his set isUploaded=:isUploaded where shop_id=:shop_id and call_number=:call_number and call_time=:call_time and call_date=:call_date")
    fun updateCallHisIsUpload(shop_id:String,call_number:String,call_time:String,call_date:String,isUploaded:Boolean)

    @Query("select * from call_his where isUploaded=:isUploaded")
    fun getUnSyncData(isUploaded:Boolean):List<CallHisEntity>

}