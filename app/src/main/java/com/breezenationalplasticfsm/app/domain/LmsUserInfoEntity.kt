package com.breezenationalplasticfsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezenationalplasticfsm.app.AppConstant

@Entity(tableName = AppConstant.LMS_USER_INFO)
data class LmsUserInfoEntity
    (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var module_name:String = "",
    @ColumnInfo var count_of_use:String = "",
    @ColumnInfo var time_spend:String = "",
    @ColumnInfo var last_current_loc_lat:String = "",
    @ColumnInfo var last_current_loc_long:String = "",
    @ColumnInfo var last_current_loc_address:String = "",
    @ColumnInfo var date_time:String = "",
    @ColumnInfo var phone_model:String = "",
    @ColumnInfo var isUploaded:Boolean = false,
    @ColumnInfo var isCalculated:Boolean = false,
    @ColumnInfo var module_startTimeInMilli:String = "",
    @ColumnInfo var module_endTimeInMilli:String = ""
)