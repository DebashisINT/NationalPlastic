package com.breezedsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezenationalplasticfsm.app.AppConstant

@Entity(tableName = AppConstant.NEW_ORDER_DATA)
data class NewOrderDataEntity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var order_id:String = "",
    @ColumnInfo var order_date:String = "",
    @ColumnInfo var order_time:String = "",
    @ColumnInfo var order_date_time:String = "",
    @ColumnInfo var shop_id:String = "",
    @ColumnInfo var shop_name:String = "",
    @ColumnInfo var shop_type:String = "",
    @ColumnInfo var isInrange:Boolean = false,
    @ColumnInfo var order_lat:String = "",
    @ColumnInfo var order_long:String = "",
    @ColumnInfo var shop_addr:String = "",
    @ColumnInfo var shop_pincode:String = "",
    @ColumnInfo var order_total_amt:String = "",
    @ColumnInfo var order_remarks:String = "",
    @ColumnInfo var isUploaded:Boolean = false,

)