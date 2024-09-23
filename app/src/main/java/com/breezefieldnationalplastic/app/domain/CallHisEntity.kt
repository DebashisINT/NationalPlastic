package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.CALL_HIS)
data class CallHisEntity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var shop_id:String = "",
    @ColumnInfo var call_number:String = "",
    @ColumnInfo var call_date:String = "",
    @ColumnInfo var call_time:String = "",
    @ColumnInfo var call_date_time:String = "",
    @ColumnInfo var call_type:String = "",
    @ColumnInfo var call_duration_sec:String = "",
    @ColumnInfo var call_duration:String = "",
    @ColumnInfo var isUploaded:Boolean = false
)