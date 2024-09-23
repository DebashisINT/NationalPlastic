package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.CRM_SCHEDULER_MASTER)
data class SchedulerMasterEntity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var scheduler_id:String = "",
    @ColumnInfo var scheduler_name:String = "",
    @ColumnInfo var select_template:String = "",
    @ColumnInfo var template_content:String = "",
    @ColumnInfo var select_mode_id:String = "",
    @ColumnInfo var select_mode:String = "",
    @ColumnInfo var select_rule_id:String = "",
    @ColumnInfo var select_rule:String = "",
   /* @ColumnInfo var select_hour:String = "",
    @ColumnInfo var select_minute:String = "",
    @ColumnInfo var select_time:String = "",
    @ColumnInfo var select_date:String = "",
    @ColumnInfo var select_timestamp:String = "",*/
    @ColumnInfo var repeat_every_month:Boolean = false,
   /* @ColumnInfo var select_contact_id:String = "",
    @ColumnInfo var select_contact:String = "",*/
    @ColumnInfo var save_date_time:String = "",
    @ColumnInfo var save_modify_date_time:String = "",
    @ColumnInfo var isUploaded:Boolean = true,
   // @ColumnInfo var isRepeatOfMonthInSchedulerEnable:Boolean = false,
    @ColumnInfo var isAutoMail:Boolean = false,
    @ColumnInfo var sendingFilePath:String = ""

    )