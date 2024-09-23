package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.LMS_NOTIFICATION)
data class LMSNotiEntity(
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var noti_datetime:String = "",
    @ColumnInfo var noti_date:String = "",
    @ColumnInfo var noti_time:String = "",
    @ColumnInfo var noti_header:String = "",
    @ColumnInfo var noti_message:String = "",
    @ColumnInfo var isViwed:Boolean = false
)