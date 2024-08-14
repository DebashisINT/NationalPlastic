package com.breezenationalplasticfsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezenationalplasticfsm.app.AppConstant

@Entity(tableName = AppConstant.CRM_SCHEDULER_MASTER_DATETIME)
data class SchedulerDateTimeEntity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var scheduler_id:String = "",
    @ColumnInfo var select_hour:String = "",
    @ColumnInfo var select_minute:String = "",
    @ColumnInfo var select_time:String = "",
    @ColumnInfo var select_date:String = "",
    @ColumnInfo var select_timestamp:String = "",
    @ColumnInfo var isDone:Boolean = false
    )