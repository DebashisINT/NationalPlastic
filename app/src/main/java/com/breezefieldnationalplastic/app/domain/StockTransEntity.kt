package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.STOCK_TRANS)
data class StockTransEntity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var order_id:String = "",
    @ColumnInfo var stock_shopcode:String = "",
    @ColumnInfo var stock_productid:String = "",
    @ColumnInfo var stock_productqty:String = "",
    @ColumnInfo var stock_productbalqty:String = "",
    @ColumnInfo var stock_productOrderqty:String = "",
    @ColumnInfo var isUploaded:Boolean = false
)