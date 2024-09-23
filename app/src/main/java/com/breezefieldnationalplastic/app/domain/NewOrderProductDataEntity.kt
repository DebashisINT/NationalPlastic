package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.NEW_ORDER_PRODUCT_DATA)
data class NewOrderProductDataEntity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var order_id:String = "",
    @ColumnInfo var product_id:String = "",
    @ColumnInfo var product_name:String = "",
    @ColumnInfo var submitedQty:String = "",
    @ColumnInfo var submitedSpecialRate:String = "",
)